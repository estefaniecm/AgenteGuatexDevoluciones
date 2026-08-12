package com.guatex.igconfiguraciones.modelos;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.awt.geom.Rectangle2D;
import java.io.IOException;
import java.io.InputStream;

/**
 *
 * @author ESTEFANIECM
 */
public class GeneradorZPL {

    private static Font fuenteCeroTachado;

    static {
        try (InputStream is = GeneradorZPL.class.getResourceAsStream("/com/guatex/igconfiguraciones/util/pixelmix.ttf")) {
            if (is == null) {
                throw new IOException("No se encontró el recurso .ttf en el classpath");
            }
            fuenteCeroTachado = Font.createFont(Font.TRUETYPE_FONT, is).deriveFont(20f);
        } catch (Exception e) {
            e.printStackTrace();
            fuenteCeroTachado = null;
        }
    }

    /**
     * Genera el bloque ^GF de ZPL para un texto, usando la fuente con cero
     * tachado. Incluye recorte de filas en blanco y compresión ASCII nativa de
     * ZPL para reducir el tamaño del campo generado.
     *
     * @param texto texto a renderizar
     * @param alturaPuntos altura deseada del texto en puntos ZPL
     * @return el comando ^GF completo, listo para insertar en el ZPL
     */
    public static String generarCampoGF(String texto, int alturaPuntos) {
        if (fuenteCeroTachado == null) {
            System.out.println("ADVERTENCIA: fuenteCeroTachado es null, no se puede generar el campo GF.");
            return "";
        }

        try {
            Font fuenteEscalada = fuenteCeroTachado.deriveFont((float) alturaPuntos);
            BufferedImage medidor = new BufferedImage(1, 1, BufferedImage.TYPE_INT_RGB);
            Graphics2D gMedidor = medidor.createGraphics();
            gMedidor.setFont(fuenteEscalada);
            FontMetrics fm = gMedidor.getFontMetrics();
            Rectangle2D bounds = fm.getStringBounds(texto, gMedidor);
            gMedidor.dispose();

            int ancho = (int) Math.ceil(bounds.getWidth()) + 4;
            int alto = (int) Math.ceil(bounds.getHeight()) + 4;

            int bytesPorFila = (int) Math.ceil(ancho / 8.0);
            int anchoAjustado = bytesPorFila * 8;

            BufferedImage imagen = new BufferedImage(anchoAjustado, alto, BufferedImage.TYPE_INT_RGB);
            Graphics2D g = imagen.createGraphics();
            g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_OFF);
            g.setColor(Color.WHITE);
            g.fillRect(0, 0, anchoAjustado, alto);
            g.setColor(Color.BLACK);
            g.setFont(fuenteEscalada);
            g.drawString(texto, 2, fm.getAscent() + 2);
            g.dispose();

            // Recorte de filas completamente en blanco (arriba/abajo)
            imagen = recortarFilasBlancas(imagen);
            alto = imagen.getHeight();

            StringBuilder hex = new StringBuilder();
            for (int y = 0; y < alto; y++) {
                int bitBuffer = 0;
                int bitCount = 0;
                for (int x = 0; x < anchoAjustado; x++) {
                    int rgb = imagen.getRGB(x, y) & 0xFFFFFF;
                    boolean esNegro = rgb < 0x808080; // umbral simple blanco/negro
                    bitBuffer = (bitBuffer << 1) | (esNegro ? 1 : 0);
                    bitCount++;
                    if (bitCount == 8) {
                        hex.append(String.format("%02X", bitBuffer));
                        bitBuffer = 0;
                        bitCount = 0;
                    }
                }
                if (bitCount > 0) {
                    bitBuffer <<= (8 - bitCount);
                    hex.append(String.format("%02X", bitBuffer));
                }
            }

            int totalBytes = bytesPorFila * alto;

            // Compresión ASCII nativa de ZPL (b y c siempre reflejan el tamaño SIN comprimir)
            String hexComprimido = comprimirHexZPL(hex.toString(), bytesPorFila);

            // Comando ^GF: A = ASCII hex, totalBytes, totalBytes, bytesPorFila, data
            return "^GFA," + totalBytes + "," + totalBytes + "," + bytesPorFila + "," + hexComprimido;

        } catch (Exception e) {
            e.printStackTrace();
            return "";
        }
    }

    private static BufferedImage recortarFilasBlancas(BufferedImage imagen) {
        int ancho = imagen.getWidth();
        int alto = imagen.getHeight();
        int primeraFila = 0;
        int ultimaFila = alto - 1;

        for (int y = 0; y < alto; y++) {
            if (filaTieneNegro(imagen, y, ancho)) {
                primeraFila = y;
                break;
            }
        }
        for (int y = alto - 1; y >= 0; y--) {
            if (filaTieneNegro(imagen, y, ancho)) {
                ultimaFila = y;
                break;
            }
        }

        if (primeraFila == 0 && ultimaFila == alto - 1) {
            return imagen; // nada que recortar
        }

        int nuevoAlto = ultimaFila - primeraFila + 1;
        if (nuevoAlto <= 0) {
            return imagen;
        }
        return imagen.getSubimage(0, primeraFila, ancho, nuevoAlto);
    }

    private static boolean filaTieneNegro(BufferedImage imagen, int y, int ancho) {
        for (int x = 0; x < ancho; x++) {
            if ((imagen.getRGB(x, y) & 0xFFFFFF) < 0x808080) {
                return true;
            }
        }
        return false;
    }

    private static String comprimirHexZPL(String hexCompleto, int bytesPorFila) {
        int hexPorFila = bytesPorFila * 2;
        int totalFilas = hexCompleto.length() / hexPorFila;
        StringBuilder resultado = new StringBuilder();
        String filaAnterior = null;

        for (int f = 0; f < totalFilas; f++) {
            String fila = hexCompleto.substring(f * hexPorFila, (f + 1) * hexPorFila);
            if (fila.equals(filaAnterior)) {
                resultado.append(":");
            } else {
                resultado.append(comprimirFila(fila));
            }
            filaAnterior = fila;
        }
        return resultado.toString();
    }

    private static String comprimirFila(String fila) {
        StringBuilder out = new StringBuilder();
        int i = 0;
        while (i < fila.length()) {
            char c = fila.charAt(i);
            int reps = 1;
            while (i + reps < fila.length() && fila.charAt(i + reps) == c) {
                reps++;
            }
            // Si de aquí al final de la fila todo es '0', cerramos con ','
            if (c == '0' && i + reps == fila.length()) {
                out.append(",");
                return out.toString();
            }
            out.append(codificarRepeticion(reps)).append(c);
            i += reps;
        }
        return out.toString();
    }

    private static String codificarRepeticion(int n) {
        StringBuilder codigo = new StringBuilder();
        while (n >= 20) {
            int bloque = Math.min(400, (n / 20) * 20);
            char letra = (char) ('g' + (bloque / 20) - 1);
            codigo.append(letra);
            n -= bloque;
        }
        if (n > 0) {
            codigo.append((char) ('G' + n - 1));
        }
        return codigo.toString();
    }

}

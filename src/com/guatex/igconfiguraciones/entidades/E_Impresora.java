package com.guatex.igconfiguraciones.entidades;

import java.util.UUID;

/**
 *
 * @author ESTEFANIECM
 */
public class E_Impresora {

    private String idImpresora = "";
    private String nombre = "";
    private String tipo = "";
    private String destino = ""; 

    public String getIdImpresora() {
        return idImpresora;
    }

    public void setIdImpresora(String id) {
        this.idImpresora = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo.toUpperCase().trim();
    }

    public String getDestino() {
        return destino;
    }

    public void setDestino(String destino) {
        this.destino = destino.trim();
    }

    public String getIp() {
        return esTipoIP() ? destino : "";
    }

    public void setIp(String ip) {
        this.destino = ip.trim();
        if (this.tipo.isEmpty()) {
            this.tipo = "IP";
        }
    }

    public boolean esTipoIP() {
        return "IP".equalsIgnoreCase(tipo);
    }

    public boolean esTipoUSB() {
        return "USB".equalsIgnoreCase(tipo);
    }

    public static String generaIDimpresora() {
        return UUID.randomUUID().toString();
    }

    public String impresoraTexto() {
        return idImpresora + "," + nombre + "," + tipo + "," + destino;
    }

    // Deserialización < lee desde el archivo ───────────────────────────────
    /**
     * Retrocompatible: 3 campos > UUID,NOMBRE,DESTINO formato viejo > tipo = IP
     * (automático) 4 campos > UUID,NOMBRE,TIPO,DESTINO formato nuevo
     */
    public E_Impresora getImpresora(String linea) {
        System.out.println("linea impresora: " + linea);

        String[] datos = linea.split(",");
        E_Impresora i = new E_Impresora();

        if (datos.length == 4) {
            // Formato nuevo 
            i.setIdImpresora(datos[0].trim());
            i.setNombre(datos[1].trim());
            i.setTipo(datos[2].trim());
            i.setDestino(datos[3].trim());

        } else if (datos.length == 3) {
            // Formato viejo -> se trata como IP 
            i.setIdImpresora(datos[0].trim());
            i.setNombre(datos[1].trim());
            i.setTipo("IP");
            i.setDestino(datos[2].trim());
            System.out.println("   >> Impresora en formato viejo, interpretada como IP: " + linea);

        } else {
            System.err.println("   >> Línea con formato inválido, se ignora: " + linea);
        }

        return i;
    }

    @Override
    public String toString() {
        return nombre + " - [" + tipo + "] " + destino;
    }
}

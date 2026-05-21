package com.guatex.igconfiguraciones.archivos;

import com.guatex.igconfiguraciones.entidades.E_Impresora;
import com.guatex.igconfiguraciones.loggs.ArchivoLogs;
import com.guatex.igconfiguraciones.principal.GTXConfiguracionIG;
import com.guatex.igconfiguraciones.util.Parametros;
import com.guatex.igconfiguraciones.util.UtilesArchivos;
import java.io.*;
import java.nio.file.*;
import java.util.*;

/**
 *
 * @author ESTEFANIECM
 */
public class A_Impresoras {

    private final String carpetaArchivo = Parametros.getRutaRaiz() + "SERVICIO_IMPRESION_GUATEX";
    private final String nombreArchivo = "datosImpresoras.txt";
    private final String rutaArchivo = carpetaArchivo + "\\M_IMPRESORAS\\" + nombreArchivo;
    private final String rutaBK = Parametros.getRutaRaiz() + "BACKUP_IMPRESION_GUATEX\\BK_IMPRESORAS\\" + nombreArchivo;

    public ArrayList<E_Impresora> impresorasRegistradas() {
        return impresorasRegistradas(false);
    }

    private ArrayList<E_Impresora> impresorasRegistradas(boolean desdeBackup) {
        ArrayList<E_Impresora> lista = new ArrayList<>();
        try {
            File archivoTxt = new File(rutaArchivo);

            if (!archivoTxt.exists()) {
                System.out.println("x/x/x El archivo de Impresoras no existe x/x/x");
                archivoTxt.getParentFile().mkdirs();
                archivoTxt.createNewFile();
                System.out.println("      El archivo de Impresoras fue creado. Intentando restaurar desde BK...");

                if (!desdeBackup && restaurarDesdeBK()) {
                    return impresorasRegistradas(true);
                } else {
                    System.out.println("** No se pudo restaurar desde BK o archivo BK también vacío **");
                }

            } else if (archivoTxt.length() == 0) {
                System.out.println("** Archivo de Impresoras vacío **");

                if (!desdeBackup && restaurarDesdeBK()) {
                    System.out.println("   Restauración desde BK exitosa. Releyendo...");
                    return impresorasRegistradas(true);
                } else {
                    System.out.println("** No se pudo restaurar o BK también vacío. Se retorna lista vacía **");
                }

            } else {
                System.out.println(">> Archivo de Impresoras con datos <<");
                try (BufferedReader reader = new BufferedReader(new FileReader(archivoTxt))) {
                    String registro;
                    while ((registro = reader.readLine()) != null) {
                        E_Impresora imp = new E_Impresora().getImpresora(registro);
                        lista.add(imp);
                    }
                }
            }

        } catch (IOException e) {
            ArchivoLogs.getInstance().grabaLogFileAdministrador(
                    "------ Excepción - Archivo impresoras - [" + e.getLocalizedMessage() + "]", true);
            e.printStackTrace();
        }

        return lista;
    }

    public boolean guardarImpresorasArchivo() {
        boolean respuesta = false;
        // Verificar si el archivo existe
        File archivoTxt = new File(rutaArchivo);
        boolean archivoExiste = archivoTxt.exists();
        if (archivoExiste) {
            try (BufferedWriter writer = new BufferedWriter(new FileWriter(archivoTxt))) {
                for (E_Impresora i : GTXConfiguracionIG.ListaImpresoras) {
                    writer.write(i.impresoraTexto());
                    writer.newLine();
                }
                respuesta = true;
            } catch (IOException e) {
                ArchivoLogs.getInstance().grabaLogFileAdministrador("------ Excepción - Guardar archivo impresoras - [" + e.getLocalizedMessage() + "]", true);
                respuesta = false;
                // Intentar restaurar desde el archivo de respaldo
                if (restaurarDesdeBK()) {
                    // Intentar guardar de nuevo
                    respuesta = guardarImpresorasArchivo();
                }
            }
        } else {
            respuesta = false;
            // Intentar restaurar desde el archivo de respaldo
            if (restaurarDesdeBK()) {
                // Intentar guardar de nuevo
                respuesta = guardarImpresorasArchivo();
            }
        }
        return respuesta;
    }

    public void realizarBKarchivo() {
        ArchivoLogs.getInstance().grabaLogFileAdministrador("------ Backup Archivo impresoras", false);
        Path rutaOriginal = Paths.get(rutaArchivo);
        Path rutaCopia = Paths.get(rutaBK);
        UtilesArchivos arc = new UtilesArchivos();
        try {
            // Crear la carpeta de destino si no existe
            if (!Files.exists(rutaCopia.getParent())) {
                Files.createDirectories(rutaCopia.getParent());
            }
            arc.ocultarDirectorio(rutaCopia.getParent());
            // Ocultar la carpeta "BACKUP"
            arc.ocultarDirectorio(Paths.get(Parametros.getRutaRaiz() + "BACKUP_IMPRESION_GUATEX"));

            // Verificar que el archivo original exista y sea un archivo regular
            if (Files.exists(rutaOriginal) && Files.isRegularFile(rutaOriginal)) {
                // Copiar el archivo desde el origen hasta el destino, reemplazando si ya existe
                Files.copy(rutaOriginal, rutaCopia, StandardCopyOption.REPLACE_EXISTING);
                arc.ocultarArchivo(rutaCopia);
            }
        } catch (IOException e) {
            ArchivoLogs.getInstance().grabaLogFileAdministrador("------ Excepción - BK impresoras - [" + e.getLocalizedMessage() + "]", true);
            e.printStackTrace();
        }
    }

    private boolean restaurarDesdeBK() {
        Path rutaOriginal = Paths.get(rutaArchivo);
        Path rutaCopia = Paths.get(rutaBK);

        try {
            if (Files.exists(rutaCopia) && Files.isRegularFile(rutaCopia)) {
                // Crear la carpeta de destino si no existe
                if (!Files.exists(rutaOriginal.getParent())) {
                    Files.createDirectories(rutaOriginal.getParent());
                }
                // Copiar el archivo de respaldo a la ruta original
                Files.copy(rutaCopia, rutaOriginal, StandardCopyOption.REPLACE_EXISTING);
                // Quitar el atributo de oculto del archivo restaurado
                Files.setAttribute(rutaOriginal, "dos:hidden", false);
                ArchivoLogs.getInstance().grabaLogFileAdministrador("------ Se realizó restauración de impresoras desde BK", false);
                return true;
            }
        } catch (IOException e) {
            ArchivoLogs.getInstance().grabaLogFileAdministrador("------ Excepción - Restaurar impresoras - [" + e.getLocalizedMessage() + "]", true);
            System.err.println("Error en A_Impresoras -> restaurarDesdeBK: " + e.getLocalizedMessage());
            e.printStackTrace();
        }
        return false;
    }

}

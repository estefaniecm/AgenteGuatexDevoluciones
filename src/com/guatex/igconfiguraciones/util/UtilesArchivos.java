package com.guatex.igconfiguraciones.util;

import com.guatex.igconfiguraciones.loggs.ArchivoLogs;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

/**
 *
 * @author ESTEFANIECM
 */
public class UtilesArchivos {
    
     public void ocultarDirectorio(Path directorio) {
        try {
            Files.setAttribute(directorio, "dos:hidden", true);
            //System.out.println("Directorio oculto: " + directorio);
        } catch (IOException e) {
            ArchivoLogs.getInstance().grabaLogFileAdministrador("------ No se pudo ocultar el directorio " + directorio + " - " + e.getMessage(), true);
        }
    }

    public void ocultarArchivo(Path archivo) {
        try {
            Files.setAttribute(archivo, "dos:hidden", true);
            //System.out.println("Archivo oculto: " + archivo);
        } catch (IOException e) {
            ArchivoLogs.getInstance().grabaLogFileAdministrador("------ No se pudo ocultar el archivo " + archivo + " - " + e.getMessage(), true);
        }
    }
    
}

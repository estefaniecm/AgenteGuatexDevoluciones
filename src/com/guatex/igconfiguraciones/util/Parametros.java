package com.guatex.igconfiguraciones.util;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

/**
 *
 * @author ESTEFANIECM
 */
public class Parametros {

    private static int tiempoEspera = 1;
    private static String password = "AGEGUATEX";
    private static String fecha = "";
    private static String rutaRaiz = "C:\\GUATEX\\";

    static {
        String localAppDataPath = System.getenv("LOCALAPPDATA");
        File antiguaRuta = new File(localAppDataPath, "GUATEX\\SERVICIO_IMPRESION_GUATEX");
        File antiguaRutaBK = new File(localAppDataPath, "GUATEX\\BACKUP_IMPRESION_GUATEX");
        File nuevaRuta = new File(rutaRaiz, "SERVICIO_IMPRESION_GUATEX");
        File nuevaRutaBK = new File(rutaRaiz, "BACKUP_IMPRESION_GUATEX");
        UtilesArchivos arc = new UtilesArchivos();
        // Verificar si la carpeta en LOCALAPPDATA existe
        if (antiguaRuta.exists() && antiguaRuta.isDirectory()) {
            System.out.println("Se encontró la carpeta en: " + antiguaRuta.getAbsolutePath());
            // Mover todo el contenido asegurando que mantenga la estructura
            if (moverConfiguraciones(antiguaRuta, nuevaRuta)) {
                System.out.println("Carpeta trasladada exitosamente a: " + nuevaRuta.getAbsolutePath());
                // Eliminar la carpeta original después de trasladar los archivos
                eliminarCarpeta(antiguaRuta);
                // Verificar si el archivo instanciaIMP.lock existe y eliminarlo
                File archivoLock = new File(localAppDataPath + "instanciaIMPGuatex.lock");
                if (archivoLock.exists()) {
                    archivoLock.delete();
                }
                System.out.println("Se eliminó la carpeta antigua de LOCALAPPDATA.");
            } else {
                System.err.println("Error al mover la carpeta.");
            }
        }
        
        if(antiguaRutaBK.exists() && antiguaRutaBK.isDirectory()){
            if (moverConfiguraciones(antiguaRutaBK, nuevaRutaBK)) {
                arc. ocultarDirectorio(Paths.get(nuevaRutaBK.getAbsolutePath()));
                System.out.println("Carpeta trasladada exitosamente a: " + nuevaRutaBK.getAbsolutePath());
                // Eliminar la carpeta original después de trasladar los archivos
                eliminarCarpeta(antiguaRutaBK);
                System.out.println("Se eliminó la carpeta BK de LOCALAPPDATA.");
            } else {
                System.err.println("Error al mover la carpeta.");
            }
        }
    }

    public static String getRutaRaiz() {
        return rutaRaiz;
    }

    public static void setRutaRaiz(String rutaRaiz) {
        Parametros.rutaRaiz = rutaRaiz;
    }

    public static String getFecha() {
        return fecha;
    }

    public static void setFecha(String fecha) {
        Parametros.fecha = fecha;
    }

    public static int getTiempoEspera() {
        return tiempoEspera * 60 * 1000;
    }

    public static void setTiempoEspera(int tiempoEspera) {
        Parametros.tiempoEspera = tiempoEspera;
    }

    public static String getPassword() {
        return password;
    }

    public static void setPassword(String password) {
        Parametros.password = password.toUpperCase().trim();
    }

    private static boolean moverConfiguraciones(File origen, File destino) {
        try {
            if (!destino.exists()) {
                destino.mkdirs();
            }

            for (File archivo : origen.listFiles()) {
                File nuevoArchivo = new File(destino, archivo.getName());

                if (archivo.isDirectory()) {
                    // Mover subcarpeta
                    if (!moverConfiguraciones(archivo, nuevoArchivo)) {
                        return false;
                    }
                } else {
                    // Mover archivo
                    Files.move(archivo.toPath(), nuevoArchivo.toPath(), StandardCopyOption.REPLACE_EXISTING);
                }
            }
            return true;
        } catch (IOException e) {
            System.err.println("Error al mover carpeta: " + e.getMessage());
            return false;
        }
    }

    private static void eliminarCarpeta(File carpeta) {
        for (File archivo : carpeta.listFiles()) {
            if (archivo.isDirectory()) {
                eliminarCarpeta(archivo);
            }
            archivo.delete();
        }
        carpeta.delete();
    }
}

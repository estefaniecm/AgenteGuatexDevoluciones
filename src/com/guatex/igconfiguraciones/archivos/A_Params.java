package com.guatex.igconfiguraciones.archivos;

import com.guatex.igconfiguraciones.loggs.ArchivoLogs;
import com.guatex.igconfiguraciones.util.CifradoPassword;
import com.guatex.igconfiguraciones.util.Parametros;
import java.io.File;
import java.io.IOException;
import java.io.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Locale;
import java.nio.charset.StandardCharsets;
import java.util.Properties;

/**
 *
 * @author ESTEFANIECM
 */
public class A_Params {

    private final static String carpetaArchivo = Parametros.getRutaRaiz() + "SERVICIO_IMPRESION_GUATEX";
    private final static String nombreArchivo = "config_params.txt";
    private final static String rutaArchivo = carpetaArchivo + "\\M_CONFIGURACION\\" + nombreArchivo;

    public Properties cargarPropiedades() {
        Properties propiedades = new Properties();

        if (existeArchivo()) {
            try (FileInputStream archivo = new FileInputStream(rutaArchivo);
                    InputStreamReader reader = new InputStreamReader(archivo, StandardCharsets.UTF_8)) {
                propiedades.load(reader);
            } catch (IOException e) {
                ArchivoLogs.getInstance().grabaLogFileAdministrador("------ Excepción - Carga parámetros - [" + e.getLocalizedMessage() + "]", true);
                System.err.println("Error A_Params -> cargarPropiedades: " + e.getLocalizedMessage());
                e.printStackTrace();
            }
        }
        return propiedades;
    }

    public static boolean existeArchivo() {
        try {
            File archivoTxt = new File(rutaArchivo);
            if (!archivoTxt.exists()) {
                archivoTxt.getParentFile().mkdirs();
                archivoTxt.createNewFile();
                try (BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(archivoTxt), StandardCharsets.UTF_8))) {
                    writer.write("#Configuración - Parámetros");
                    writer.newLine();
                    writer.write("fechaActualizacion=" + fechaConfiguracion());
                    writer.newLine();
                    writer.write("password=" + passwordConfigurada());
                    writer.newLine();
                    writer.write("minutosEspera=" + tiempoConfigurado());
                    writer.newLine();
                    writer.close();
                    return true;
                } catch (IOException e) {
                    ArchivoLogs.getInstance().grabaLogFileAdministrador("------ Excepción - Archivo parámetros existente - [" + e.getLocalizedMessage() + "]", true);
                    System.err.println("Error en A_Params -> existeArchivo: " + e.getLocalizedMessage());
                    e.printStackTrace();
                }
            } else {
                return true;
            }
        } catch (IOException e) {
            ArchivoLogs.getInstance().grabaLogFileAdministrador("------ Excepción - Archivo parámetros - [" + e.getLocalizedMessage() + "]", true);
            System.err.println("Error en A_Params -> existeArchivo: " + e.getLocalizedMessage());
            e.printStackTrace();
        }
        return false;
    }

    public boolean guardarParametrosArchivo() {
        boolean respuesta = false;
        // Verificar si el archivo existe
        File archivoTxt = new File(rutaArchivo);
        boolean archivoExiste = archivoTxt.exists();
        if (archivoExiste) {
            try (BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(archivoTxt), StandardCharsets.UTF_8))) {
                writer.write("#Configuración - Parámetros");
                writer.newLine();
                writer.write("fechaActualizacion=" + fechaConfiguracion());
                writer.newLine();
                writer.write("password=" + passwordConfigurada());
                writer.newLine();
                writer.write("minutosEspera=" + tiempoConfigurado());
                writer.newLine();
                writer.close();
                respuesta = true;
                ArchivoLogs.getInstance().grabaLogFileAdministrador("------ Parámetros de configuración guardados", true);
            } catch (IOException e) {
                ArchivoLogs.getInstance().grabaLogFileAdministrador("------ Excepción - Guardar parámetros - [" + e.getLocalizedMessage() + "]", true);
                respuesta = false;
            }
        }
        return respuesta;
    }

    private static String fechaConfiguracion() {
        LocalDateTime fechaHoraActual = LocalDateTime.now();
        DateTimeFormatter formato = DateTimeFormatter.ofPattern("EEEE, dd MMM yyyy - HH:mm:ss", new Locale("es", "ES"));
        return fechaHoraActual.format(formato);
    }

    private static String passwordConfigurada() {
        String pass = "";
        try {
            pass = new CifradoPassword().encrypt(Parametros.getPassword());
        } catch (Exception e) {
            System.err.println("Error: " + e.getLocalizedMessage());
            ArchivoLogs.getInstance().grabaLogFileAdministrador("------ Excepción - Parámetros - [" + e.getLocalizedMessage() + "]", true);
            e.printStackTrace();
        }
        return pass;
    }

    private static int tiempoConfigurado() {
        return (Parametros.getTiempoEspera());
    }

}

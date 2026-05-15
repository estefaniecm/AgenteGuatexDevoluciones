package com.guatex.igconfiguraciones.loggs;

import com.guatex.igconfiguraciones.util.Parametros;
import org.apache.log4j.*;

import java.io.File;
import java.io.IOException;

public class ArchivoLogs {

    private static ArchivoLogs grabaFile;
    private final Logger rootLoggerAdmin;
    private RollingFileAppender fileAppenderAdmin;

    private final String rutaAdmin = Parametros.getRutaRaiz() + "LOGS_IMPRESION_GUATEX\\";
    private final String nombreAdmin = "agent_process.log";
    private final String nameAppenderAdmin = "ap2";

    private ArchivoLogs() {
        this.rootLoggerAdmin = Logger.getLogger("log2");
        inicializa();
    }

    private void inicializa() {
        rootLoggerAdmin.setLevel(Level.INFO);
        PatternLayout layout = new PatternLayout("%d{yyyy.MM.dd HH:mm:ss.SSS}  %-5p - %m%n");

        try {
            // Verificar si el directorio de logs existe, si no, crearlo
            File logDir = new File(rutaAdmin);
            if (!logDir.exists()) {
                logDir.mkdirs();
            }
            // Configurar el appender
            fileAppenderAdmin = new RollingFileAppender(layout, rutaAdmin + nombreAdmin, true);
            fileAppenderAdmin.setName(nameAppenderAdmin);
            fileAppenderAdmin.setMaxFileSize("15MB");
            fileAppenderAdmin.setMaxBackupIndex(10);
            rootLoggerAdmin.addAppender(fileAppenderAdmin);
            System.out.println("Logger configurado en: " + rutaAdmin + nombreAdmin);
        } catch (IOException ex) {
            System.err.println("Error al configurar Log4j: " + ex.getMessage());
        }
    }

    public static ArchivoLogs getInstance() {
        if (grabaFile == null) {
            grabaFile = new ArchivoLogs();
        }
        return grabaFile;
    }

    public void grabaLogFileAdministrador(String mensaje, boolean error) {
        if (fileAppenderAdmin != null) {
            if (error) {
                rootLoggerAdmin.error(mensaje); 
            } else {
                rootLoggerAdmin.info(mensaje); 
            }
        } else {
            System.err.println("Error: No se pudo escribir el log. El appender es null.");
        }
    }
}

package com.guatex.igconfiguraciones.loggs;


import org.apache.log4j.Appender;
import org.apache.log4j.ConsoleAppender;
import org.apache.log4j.Layout;
import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.apache.log4j.PatternLayout;

public class GeneraLogs {
  private Logger log;
  
  private ConsoleAppender consoleAppender = null;
  
  private static GeneraLogs grabalog = null;
  
  private GeneraLogs() {
    inicializa();
  }
  
  private void inicializa() {
    this.log = Logger.getLogger("Class");
    this.log.setLevel(Level.toLevel("INIT"));
    PatternLayout layout3 = new PatternLayout();
    layout3.setConversionPattern("%d{dd.MM.yyyy HH:mm:ss.SSS}  %-5p - %m%n");
    try {
      this.consoleAppender = new ConsoleAppender((Layout)layout3);
    } catch (Exception e) {
      ArchivoLogs.getInstance().grabaLogFileAdministrador("------ Error de carga de configuración log4j [" + e.getMessage() + "]", true);
    } 
    this.log.addAppender((Appender)this.consoleAppender);
  }
  
  public void setLog(Logger log) {
    this.log = log;
  }
  
  public Logger getLog() {
    return this.log;
  }
  
  public static GeneraLogs getInstance() {
    if (grabalog == null)
      grabalog = new GeneraLogs(); 
    return grabalog;
  }
}

package com.guatex.igconfiguraciones.principal;

import com.guatex.igconfiguraciones.archivos.A_Impresoras;
import com.guatex.igconfiguraciones.archivos.A_Params;
import com.guatex.igconfiguraciones.archivos.A_Servicios;
import com.guatex.igconfiguraciones.archivos.A_Usuarios;
import com.guatex.igconfiguraciones.entidades.E_Impresora;
import com.guatex.igconfiguraciones.entidades.E_Servicio;
import com.guatex.igconfiguraciones.entidades.E_Usuario;
import com.guatex.igconfiguraciones.formularios.PanelAdministrador;
import com.guatex.igconfiguraciones.imagenes.RutasImagenes;
import com.guatex.igconfiguraciones.loggs.ArchivoLogs;
import com.guatex.igconfiguraciones.proceso.ImpresionesPendientes;
import com.guatex.igconfiguraciones.util.Parametros;
import java.awt.*;
import java.awt.event.*;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.nio.channels.FileChannel;
import java.nio.channels.FileLock;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.nio.channels.ClosedChannelException;
import java.nio.file.StandardOpenOption;
import java.util.ArrayList;
import java.util.Properties;
import javax.swing.JOptionPane;

/**
 *
 * @author ESTEFANIECM
 */
public class GTXConfiguracionIG {

    private static boolean visible = false;
    public static ArrayList<E_Impresora> ListaImpresoras = new ArrayList<>();
    public static ArrayList<E_Usuario> ListaUsuarios = new ArrayList<>();
    public static ArrayList<E_Servicio> ListaServicios = new ArrayList<>();
    public static Properties parametros = new Properties();
    private static int totalImp = 0;
    private static int totalUsr = 0;
    private static int totalSrv = 0;
    private static FileLock lock;
    private static FileChannel channel;
    private static final String textoIcono = "Agente Guatex";
    private static TrayIcon trayIcono;
    public static ImpresionesPendientes imp = new ImpresionesPendientes();

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        String agentePID = getCurrentPID();
        ArchivoLogs.getInstance().grabaLogFileAdministrador("---------------------------     INICIA AGENTE GUATEX    ---------------------------", false);
        
        if (!correInstanciaAgente(agentePID)) {
            if (SystemTray.isSupported()) {
                SystemTray tray = SystemTray.getSystemTray();
                Image icono = new RutasImagenes().getIcono();
                PopupMenu popupMenu = new PopupMenu();
                trayIcono = new TrayIcon(icono, textoIcono, popupMenu);
                trayIcono.setImageAutoSize(true);
                // Agrega las opciones al menú contextual
                MenuItem btnAbrir = new MenuItem("Abrir administrador de impresiones");
                MenuItem btnCerrar = new MenuItem("Cerrar");

                popupMenu.add(btnAbrir);
                popupMenu.add(btnCerrar);

                //Acciones para las opciones del menú
                //Botón abrir
                btnAbrir.addActionListener((ActionEvent e) -> {
                    if (!visible) {
                        PanelAdministrador panel = new PanelAdministrador();
                        panel.addWindowListener(new WindowListener() {
                            @Override
                            public void windowOpened(WindowEvent e) {
                                visible = true;
                                btnAbrir.setEnabled(true);
                            }

                            @Override
                            public void windowClosed(WindowEvent e) {
                                visible = false;
                            }

                            @Override
                            public void windowClosing(WindowEvent e) {
                            }

                            @Override
                            public void windowIconified(WindowEvent e) {
                            }

                            @Override
                            public void windowDeiconified(WindowEvent e) {
                            }

                            @Override
                            public void windowActivated(WindowEvent e) {
                            }

                            @Override
                            public void windowDeactivated(WindowEvent e) {
                            }
                        });
                        panel.setVisible(true);
                        btnAbrir.setEnabled(false);
                    }
                });
                btnAbrir.setFont(new Font("Arial", Font.BOLD, 12));

                btnCerrar.addActionListener((ActionEvent e) -> {
                    int respuesta = JOptionPane.showConfirmDialog(null,
                            "El servicio de impresión de guías finalizará. \n\n¿Está seguro que desea cerrar el Administrador de Impresiones?",
                            "Cerrar Agente Guatex ", JOptionPane.YES_NO_OPTION);
                    if (respuesta == JOptionPane.YES_OPTION) {
                        trayIcono.displayMessage("Administrador de Impresiones finalizado", "", TrayIcon.MessageType.NONE);
                        liberarLock();
                        finPrograma();
                        ArchivoLogs.getInstance().grabaLogFileAdministrador("---------------------------    FINALIZA AGENTE GUATEX   ---------------------------", false);
                        System.exit(0);
                    }
                });

                try {
                    tray.add(trayIcono);
                } catch (AWTException e) {
                    ArchivoLogs.getInstance().grabaLogFileAdministrador("------ Excepción - Tray - [" + e.getLocalizedMessage() + "]", true);
                    System.err.println("Error al agregar el icono al System Tray");
                }
            }
            try {
                inicioAgenteGuatex();
            } catch (Exception e) {
                ArchivoLogs.getInstance().grabaLogFileAdministrador("------ Excepción - Inicialización - [" + e.getLocalizedMessage() + "]", true);
                System.err.println("Ocurrió un error: " + e.getLocalizedMessage());
            }
            imp = new ImpresionesPendientes();
            imp.iniciar();
        }
    }//fin void main

    public static TrayIcon getTrayIcono() {
        return trayIcono;
    }

    public static String getTextoIcono() {
        return textoIcono;
    }

    public static void reinicioConfiguracionIG() {
        Parametros.setTiempoEspera(1);
        ListaImpresoras.clear();
        ListaUsuarios.clear();
        ListaServicios.clear();
        parametros.clear();
        totalImp = 0;
        totalUsr = 0;
        totalSrv = 0;
        ListaImpresoras = new ArrayList<>();
        ListaUsuarios = new ArrayList<>();
        ListaServicios = new ArrayList<>();
        parametros = new Properties();
    }

    private static void finPrograma() {
        System.out.println("-- finPrograma --");
        A_Impresoras aImpresoras = new A_Impresoras();
        boolean guardarIMP = aImpresoras.guardarImpresorasArchivo();
        if (guardarIMP) {
            if (ListaImpresoras.size() != totalImp) {
                aImpresoras.realizarBKarchivo();
            }
        }

        A_Usuarios aUsuarios = new A_Usuarios();
        boolean guardarUSR = aUsuarios.guardarUsuariosArchivo();
        if (guardarUSR) {
            if (ListaUsuarios.size() != totalUsr) {
                aUsuarios.realizarBKarchivo();
            }
        }

        A_Servicios aServicios = new A_Servicios();
        boolean guardarSRV = aServicios.guardarServiciosArchivo();
        if (guardarSRV) {
            if (ListaServicios.size() != totalSrv) {
                aServicios.realizarBKarchivo();
            }
        }
        liberarLock();
    }

    public static void inicioAgenteGuatex() {
        ArchivoLogs.getInstance().grabaLogFileAdministrador("------ Carga de configuraciones", false);
        cargarImpresoras();
        cargarUsuarios();
        cargarServicios();
        cargarParametros();
        ArchivoLogs.getInstance().grabaLogFileAdministrador("------ Finaliza carga de configuraciones", false);
    }

    private static void cargarParametros() {
        parametros = new A_Params().cargarPropiedades();
    }

    private static void cargarImpresoras() {
        ListaImpresoras.clear();
        ListaImpresoras = new A_Impresoras().impresorasRegistradas();
        totalImp = ListaImpresoras.size();
    }

    private static void cargarUsuarios() {
        ListaUsuarios.clear();
        ListaUsuarios = new A_Usuarios().usuariosRegistrados();
        totalUsr = ListaUsuarios.size();
    }

    private static void cargarServicios() {
        ListaServicios.clear();
        ListaServicios = new A_Servicios().serviciosRegistrados();
        totalSrv = ListaServicios.size();
    }

    private static boolean correInstanciaAgente(String agentePID) {
        System.out.println("---> PID Actual: [" + agentePID + "]");
        ArchivoLogs.getInstance().grabaLogFileAdministrador("------ Verificando Agente Guatex actual [" + agentePID + "]", false);
        boolean respuesta = false;
        File archivoLock = new File(Parametros.getRutaRaiz() + "instanciaIMP.lock");
        try {
            // Leer el archivo solo si no está bloqueado
            if (archivoLock.exists()) {
                System.out.println("---> Ya existe archivo .lock [instanciaIMP.lock]");
                // Intentar bloquear el archivo temporalmente para verificar si está en uso
                try (FileChannel testChannel = FileChannel.open(archivoLock.toPath(), StandardOpenOption.WRITE);
                        FileLock testLock = testChannel.tryLock()) {
                    if (testLock == null) {
                        ArchivoLogs.getInstance().grabaLogFileAdministrador("------ Archivo existente - otra instancia del Agente SIG se está ejecutando - Status Lock", true);
                        System.out.println("---> Otra instancia está corriendo (archivo bloqueado)");
                        return true;
                    }
                } catch (IOException e) {
                    ArchivoLogs.getInstance().grabaLogFileAdministrador("------ Excepción - Instancia - [" + e.getLocalizedMessage() + "]", true);
                    return true; // Considerar que ya está en ejecución
                }

                String PIDarchivo = "";
                try (BufferedReader reader = new BufferedReader(new FileReader(archivoLock))) {
                    PIDarchivo = reader.readLine();
                }

                if (PIDarchivo != null && !PIDarchivo.isEmpty()) {
                    System.out.println("---> PID último proceso: [" + PIDarchivo + "]");
                    // Verificar si el proceso con el PID leído está en ejecución
                    if (isProcessRunning(PIDarchivo)) {
                        ArchivoLogs.getInstance().grabaLogFileAdministrador("------ Agente anterior en ejecución [" + PIDarchivo + "]", false);
                        System.out.println("     Proceso [" + PIDarchivo + "] en ejecución...");
                        return true;
                    } else {
                        ArchivoLogs.getInstance().grabaLogFileAdministrador("------ Agente actual [" + agentePID + "] puede iniciar", false);
                        System.out.println("     El proceso PID [" + PIDarchivo + "] no está ejecutándose.");
                        archivoLock.delete(); // Eliminar archivo si el proceso ya no está corriendo
                    }
                } else {
                    archivoLock.delete(); // Eliminar si el archivo está vacío
                }
            }

            // Si no hay una instancia corriendo, crear archivo y guardar el PID
            archivoLock.getParentFile().mkdirs();
            archivoLock.createNewFile();

            try (PrintWriter writer = new PrintWriter(archivoLock)) {
                writer.println(agentePID);
            }

            // Ahora que el archivo está creado y escrito, bloquearlo para evitar nuevas instancias
            channel = FileChannel.open(archivoLock.toPath(), java.nio.file.StandardOpenOption.WRITE);
            lock = channel.tryLock();
            if (lock == null) {
                return true; // No se pudo bloquear, otra instancia ya está corriendo
            }

            // Añadir shutdown hook para liberar el lock al cerrar
            Runtime.getRuntime().addShutdownHook(new Thread(() -> liberarLock()));

        } catch (IOException e) {
            ArchivoLogs.getInstance().grabaLogFileAdministrador("------ Excepción - corre instancia - [" + e.getLocalizedMessage() + "]", true);
            e.printStackTrace();
            respuesta = true; // En caso de error, asumir que otra instancia está corriendo
        }

        return respuesta;
    }

    private static void liberarLock() {
        try {
            if (lock != null && lock.isValid()) {
                lock.release(); // Solo liberar si el lock sigue siendo válido
            }
            if (channel != null && channel.isOpen()) {
                channel.close(); // Cerrar el canal si aún está abierto
            }
            File archivoLock = new File(Parametros.getRutaRaiz() + "instanciaIMP.lock");
            if (archivoLock.exists()) {
                archivoLock.delete(); // Eliminar el archivo de bloqueo
            }
        } catch (ClosedChannelException e) {
            System.err.println("Canal ya cerrado: " + e.getMessage());
        } catch (IOException e) {
            ArchivoLogs.getInstance().grabaLogFileAdministrador("------ Excepción - Liberar archivo - " + e.getLocalizedMessage(), true);
            System.err.println("Error al liberar lock: " + e.getMessage());
        }
    }

    // Método para obtener el PID del proceso actual
    private static String getCurrentPID() {
        return java.lang.management.ManagementFactory.getRuntimeMXBean().getName().split("@")[0];
    }

    public static boolean isProcessRunning(String pid) {
        boolean respuesta = false;
        try {
            String line;
            // Ejecutar el comando tasklist en Windows, filtrando por el PID
            Process p = Runtime.getRuntime().exec("tasklist /FI \"PID eq " + pid + "\"");
            BufferedReader input = new BufferedReader(new InputStreamReader(p.getInputStream()));

            while ((line = input.readLine()) != null) {
                // Si la salida contiene el PID, significa que el proceso está corriendo
                if (line.contains(pid)) {
                    respuesta = true; // El proceso está corriendo
                }
            }
            input.close();
        } catch (IOException e) {
            ArchivoLogs.getInstance().grabaLogFileAdministrador("------ Excepción - ProcessRunning - " + e.getLocalizedMessage(), true);
            e.printStackTrace();
        }
        return respuesta; // El proceso no está corriendo
    }

}//fin class

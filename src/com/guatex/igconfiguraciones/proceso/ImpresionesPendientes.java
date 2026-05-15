package com.guatex.igconfiguraciones.proceso;

import com.guatex.igconfiguraciones.entidades.E_Guia;
import com.guatex.igconfiguraciones.entidades.E_GuiaHija;
import com.guatex.igconfiguraciones.entidades.E_ImpresionesUsuario;
import com.guatex.igconfiguraciones.entidades.E_Impresora;
import com.guatex.igconfiguraciones.entidades.E_Servicio;
import com.guatex.igconfiguraciones.entidades.E_Usuario;
import com.guatex.igconfiguraciones.gestiones.G_ConsultasBD;
import com.guatex.igconfiguraciones.gestiones.G_Impresora;
import com.guatex.igconfiguraciones.loggs.ArchivoLogs;
import com.guatex.igconfiguraciones.modelos.M_ZPLguia;
import com.guatex.igconfiguraciones.principal.GTXConfiguracionIG;
import com.guatex.igconfiguraciones.util.Parametros;
import java.awt.*;
import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.Set;
import javax.print.PrintService;
import java.util.Timer;
import java.util.TimerTask;

/**
 *
 * @author ESTEFANIECM
 */
public class ImpresionesPendientes {

    private static boolean running;
    private static Timer timer;
    private static int tiempoRestante = 0;
    private static final String baseTooltip = GTXConfiguracionIG.getTextoIcono();
    private Thread hiloBusquedaImpresiones;

    public void iniciar() {
        if (hiloBusquedaImpresiones != null && hiloBusquedaImpresiones.isAlive()) {
            ArchivoLogs.getInstance().grabaLogFileAdministrador("------ Excepción - Hilo actual corriendo", true);
            System.out.println("El hilo actual sigue corriendo, no se puede iniciar otro.");
            return;
        }
        hiloBusquedaImpresiones = new Thread(new busqueda());
        hiloBusquedaImpresiones.start();
    }

    private class busqueda extends Thread {

        @Override
        public void run() {
            running = true;
            while (ImpresionesPendientes.running) {
                int tiempoEspera = 0;

                try {
                    tiempoEspera = Integer.valueOf(GTXConfiguracionIG.parametros.getProperty("minutosEspera"));
                    if (tiempoEspera <= 0) {
                        tiempoEspera = new Parametros().getTiempoEspera();
                    }
                } catch (NumberFormatException e) {
                    tiempoEspera = new Parametros().getTiempoEspera();
                }
                tiempoRestante = tiempoEspera;
                iniciarTimer();
                try {
                    if (!GTXConfiguracionIG.ListaUsuarios.isEmpty()) {
                        buscarGuiasPendientes();
                    } else {
                        ArchivoLogs.getInstance().grabaLogFileAdministrador("------ No existen usuarios configurados en el sistema", false);
                        GTXConfiguracionIG.getTrayIcono().displayMessage("Configuración", "Debe agregar usuarios al sistema y configurar el servicio de impresión. "
                                + "\nPor favor contacte a Guatex.", TrayIcon.MessageType.INFO);
                    }
                } catch (Exception ex) {
                    System.err.println("Error buscarGuiasPendientes ");
                    ImpresionesPendientes.running = false;
                }
                try {
                    Thread.sleep(tiempoEspera);
                } catch (InterruptedException ex) {
                    ImpresionesPendientes.running = false;
                }
                if (timer != null) {
                    timer.cancel();
                }
            }
        }

        private void buscarGuiasPendientes() {
            ArchivoLogs.getInstance().grabaLogFileAdministrador("--------------------------- Búsqueda de guías pendientes ---------------------------", false);
            System.out.println("\n---------------------------  buscarGuiasPendientes ---------------------------");
            ArrayList<E_ImpresionesUsuario> listadoGuiasxUsuario = new ArrayList<>();
            ArrayList<E_Usuario> usuariosConfigurados = GTXConfiguracionIG.ListaUsuarios;
            ArrayList<String> impresorasINACTIVAS = new ArrayList<>();
            ArrayList<String> usuariosERROR = new ArrayList<>();
            if (!usuariosConfigurados.isEmpty()) {
                //Verifica que haya servicios de impresión configurados
                ArrayList<E_Servicio> serviciosConfigurados = GTXConfiguracionIG.ListaServicios;
                if (!serviciosConfigurados.isEmpty()) {
                    //Buscar guías pendientes por usuario
                    listadoGuiasxUsuario = new G_ConsultasBD().consultarGuiasxImprimir(usuariosConfigurados);

                    if (listadoGuiasxUsuario != null) {
                        for (E_ImpresionesUsuario ixu : listadoGuiasxUsuario) {
                            boolean servicioEncontrado = false;
                            String nombreImpresora = "";
                            String ipImpresora = "";

                            for (E_Servicio s : serviciosConfigurados) {
                                // Verifico si el usuario cuenta con un servicio configurado
                                if (s.getIdUsuario().equals(ixu.getUsuario().getIdRegistroUsuario())) {
                                    // Verifico la impresora entre las impresoras configuradas
                                    ArrayList<E_Impresora> impresorasConfiguradas = GTXConfiguracionIG.ListaImpresoras;
                                    for (E_Impresora imp : impresorasConfiguradas) {
                                        if (imp.getIdImpresora().equals(s.getIdImpresora())) {
                                            nombreImpresora = imp.getNombre();
                                            ipImpresora = imp.getIp();
                                            servicioEncontrado = true;
                                            break; // Detener el ciclo si se encuentra un servicio configurado
                                        }
                                    }
                                }
                            }
                            //Si encontró un servicio configurado para el usuario
                            if (servicioEncontrado) {

                                if (new G_Impresora().impresoraDisponible(ipImpresora)) {

                                    ArchivoLogs.getInstance().grabaLogFileAdministrador(
                                            "------ Inicia solicitud de impresión - Usuario - ["
                                            + ixu.getUsuario().toString()
                                            + "] - Impresora IP ["
                                            + ipImpresora
                                            + "]",
                                            false
                                    );

                                    System.out.println(
                                            "---> Inicia solicitud de impresión - Usuario ["
                                            + ixu.getUsuario().toString()
                                            + "] - Impresora IP ["
                                            + ipImpresora
                                            + "]"
                                    );

                                    for (E_Guia guiaxu : ixu.getGuiasImpresion()) {

                                        generaImpresionGuia(guiaxu, ipImpresora);

                                    }

                                } else {

                                    impresorasINACTIVAS.add(nombreImpresora + " - " + ipImpresora);

                                    usuariosERROR.add(ixu.getUsuario().getCodigoUsuario());

                                    ArchivoLogs.getInstance().grabaLogFileAdministrador(
                                            "------ Impresora IP ["
                                            + ipImpresora
                                            + "] no disponible para imprimir guías del usuario ["
                                            + ixu.getUsuario().toString()
                                            + "]",
                                            false
                                    );

                                    System.out.println(
                                            ">>> No se encontró disponible la impresora IP ["
                                            + ipImpresora
                                            + "] para imprimir guías del usuario ["
                                            + ixu.getUsuario().toString()
                                            + "] <<<"
                                    );
                                }
                            } else {
                                System.out.println("---> El usuario [" + ixu.getUsuario().toString() + "] NO cuenta con un servicio de impresión configurado");
                                ArchivoLogs.getInstance().grabaLogFileAdministrador("------ Usuario [" + ixu.getUsuario().toString() + "] sin servicio de impresión", false);
                                GTXConfiguracionIG.getTrayIcono().displayMessage("Servicio de impresión", "El usuario [" + ixu.getUsuario().getCodigoUsuario() + "] no cuenta con una impresora asignada para imprimir las guías pendientes. "
                                        + "Por favor, contacte a Guatex.", TrayIcon.MessageType.INFO);
                            }
                            System.out.println("---------------------------------------------------------------------------------");

                        }//fin FOR listadoGuiasxUsuario

                        if (!impresorasINACTIVAS.isEmpty()) {
                            // Usar LinkedHashSet para eliminar duplicados y preservar el orden
                            Set<String> impresorasUnicas = new LinkedHashSet<>(impresorasINACTIVAS);
                            String mensaje;
                            String impresoras = String.join(", ", impresorasUnicas);
                            String usuarios = String.join(", ", usuariosERROR);
                            if (usuariosERROR.size() > 1) {
                                mensaje = "Las guías de los usuarios [" + usuarios + "] ";
                            } else {
                                mensaje = "Las guías del usuario [" + usuarios + "] ";
                            }

                            if (impresorasUnicas.size() > 1) {
                                mensaje = mensaje + "no fueron impresas debido a que las impresoras [" + impresoras + "] no están configuradas.";
                                GTXConfiguracionIG.getTrayIcono().displayMessage("ERROR DE CONFIGURACIÓN", mensaje
                                        + "\nSi el problema persiste por favor contacte a Guatex.", TrayIcon.MessageType.ERROR);
                            } else {
                                mensaje = mensaje + "no fueron impresas debido a que la impresora [" + impresoras + "] no está configurada.";
                                GTXConfiguracionIG.getTrayIcono().displayMessage("ERROR DE CONFIGURACIÓN", mensaje
                                        + "\nSi el problema persiste por favor contacte a Guatex.", TrayIcon.MessageType.ERROR);
                            }
                            ArchivoLogs.getInstance().grabaLogFileAdministrador("------ " + mensaje, false);
                        }
                    } else {
                        ArchivoLogs.getInstance().grabaLogFileAdministrador("------ No se estableció conexión con Guatex", true);
                        GTXConfiguracionIG.getTrayIcono().displayMessage("SIN CONEXIÓN - GUATEX", "No se ha podido establecer conexión con Guatex. "
                                + "Se intentará conectar nuevamente, si el problema persiste por favor contacte a Guatex.", TrayIcon.MessageType.WARNING);
                    }
                } else {
                    ArchivoLogs.getInstance().grabaLogFileAdministrador("------ Usuarios sin impresora asignada", false);
                    GTXConfiguracionIG.getTrayIcono().displayMessage("Configuración", "Los usuarios configurados no cuentan con una impresora asignada para el servicio de impresion de guías. "
                            + "\nPor favor contacte a Guatex.", TrayIcon.MessageType.WARNING);
                }
            } else {
                GTXConfiguracionIG.getTrayIcono().displayMessage("Configuración", "Debe agregar usuarios al sistema y configurar el servicio de impresión. "
                        + "\nPor favor contacte a Guatex.", TrayIcon.MessageType.INFO);
                ArchivoLogs.getInstance().grabaLogFileAdministrador("------ Sin usuarios configurados", false);
            }

        }

        private void generaImpresionGuia(E_Guia guia, String ipImpresora) {
            M_ZPLguia zpl = new M_ZPLguia();
            int cantidadPiezas = Integer.valueOf(guia.getPiezas());
            int cantidadgHijas = guia.getGuiasHijas().size();
            int impExitosas = 1;
            if (cantidadPiezas > 1 && cantidadgHijas > 0) { //Guía con guias hijas
                StringBuilder datosGuia = zpl.generaZPL(guia, 1, guia.getNumeroGuia());
                //System.out.println(">> ZPL: \n" + datosGuia + "\n<<\n");
                if (new G_Impresora().imprimirGuia(ipImpresora, datosGuia)) {
                    impExitosas++;
                }
                int contador = 2;
                for (E_GuiaHija s : guia.getGuiasHijas()) {
                    guia.setGuiaMadre("N");
                    StringBuilder datosGuiaHija = zpl.generaZPL(guia, contador, s.getHguiaHija());
                    //System.out.println(">> ZPL: \n" + datosGuiaHija + "\n<<\n");
                    if (new G_Impresora().imprimirGuia(ipImpresora, datosGuiaHija)) {
                        impExitosas++;
                    }
                    contador++;
                }
                if (impExitosas == contador) {
                    String impreso = new G_ConsultasBD().actualizarEstadoImpresion(guia.getNumeroGuia(), ipImpresora);
                    if (!impreso.equals("OK")) {
                        System.out.println("Error de actualización ----> guía: " + guia.getNumeroGuia());
                    } else {
                        ArchivoLogs.getInstance().grabaLogFileAdministrador("------ Guía impresa [" + guia.getNumeroGuia() + "] - Piezas [" + guia.getPiezas() + "]", false);
                        System.out.println("Guía " + guia.getNumeroGuia() + " impresa ---> " + impreso);
                    }
                }
            } else { //Imprime 1 guía
                StringBuilder datosGuia = zpl.generaZPL(guia, 1, guia.getNumeroGuia());
                //System.out.println(">> ZPL: \n" + datosGuia + "\n<<\n");
                if (new G_Impresora().imprimirGuia(ipImpresora, datosGuia)) {
                    String impreso = new G_ConsultasBD().actualizarEstadoImpresion(guia.getNumeroGuia(), ipImpresora);
                    if (!impreso.equals("OK")) {
                        ArchivoLogs.getInstance().grabaLogFileAdministrador("------ Error de actualización ----> guía: " + guia.getNumeroGuia(), true);
                        System.out.println("Error de actualización ----> guía: " + guia.getNumeroGuia());
                    } else {
                        ArchivoLogs.getInstance().grabaLogFileAdministrador("------ Guía impresa [" + guia.getNumeroGuia() + "] - Piezas [" + guia.getPiezas() + "]", false);
                        System.out.println("Guía " + guia.getNumeroGuia() + " impresa ---> " + impreso);
                    }
                }
            }//fin else
        }
    }

    //Detener hilo
    public void detener() {
        running = false;
        if (timer != null) {
            timer.cancel();
        }
        if (hiloBusquedaImpresiones != null) {
            hiloBusquedaImpresiones.interrupt();
        }
    }

    private static void actualizarTrayIcon() {
        if (tiempoRestante == 0) {
            GTXConfiguracionIG.getTrayIcono().setToolTip(baseTooltip + "\nConsultando guías pendientes de imprimir...");
        } else {
            String tiempo = formatTiempoRestante(tiempoRestante);
            GTXConfiguracionIG.getTrayIcono().setToolTip(baseTooltip + "\nPróxima actualización: " + tiempo);
        }

    }

    private static void iniciarTimer() {
        timer = new Timer(true);
        timer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                if (tiempoRestante > 0) {
                    tiempoRestante -= 1000;
                    actualizarTrayIcon();
                }
            }
        }, 0, 1000);
    }

    private static String formatTiempoRestante(int tiempoRestante) {
        int segundosTotales = tiempoRestante / 1000;
        int minutos = segundosTotales / 60;
        int segundos = segundosTotales % 60;
        if (minutos > 0) {
            return String.format("%02d:%02d minutos", minutos, segundos);
        } else {
            return String.format("%02d segundos", segundos);
        }

    }

    public Thread getHiloBusquedaImpresiones() {
        return hiloBusquedaImpresiones;
    }

//    private static void mostrarNotificacion(ArrayList<E_ImpresionesUsuario> listadoGuiasxUsuario) {
//        System.out.println("---> mostrarNotificacion()");
//        
//        for (E_ImpresionesUsuario ixu : listadoGuiasxUsuario) {
//            StringBuilder mensaje = new StringBuilder();
//            mensaje.append(ixu.getUsuario().getCodigoUsuario()+" >");
//            for (E_Guia guiaxu : ixu.getGuiasImpresion()) {
//                mensaje.append("-").append(guiaxu.getNumeroGuia());
//            }
//                    GTXConfiguracionIG.getTrayIcono().displayMessage("GUÍAS POR IMPRIMIR:", mensaje.toString(), TrayIcon.MessageType.NONE);
//
//        }
//
//
//    }
    public static boolean isRunning() {
        return ImpresionesPendientes.running;
    }
}

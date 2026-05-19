package com.guatex.igconfiguraciones.gestiones;

import com.guatex.igconfiguraciones.entidades.E_Impresora;
import com.guatex.igconfiguraciones.loggs.ArchivoLogs;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.net.Socket;
import javax.print.Doc;
import javax.print.DocFlavor;
import javax.print.DocPrintJob;
import javax.print.PrintException;
import javax.print.PrintService;
import javax.print.PrintServiceLookup;
import javax.print.SimpleDoc;
import javax.print.attribute.standard.PrinterName;

/**
 *
 * @author ESTEFANIECM
 */
public class G_Impresora {

    public boolean impresoraDisponible(E_Impresora impresora) {
        if (impresora.esTipoUSB()) {
            return disponiblePorUSB(impresora.getDestino());
        } else {
            return disponiblePorIP(impresora.getDestino());
        }
    }

    public boolean imprimirGuia(E_Impresora impresora, StringBuilder ZPLguia) {
        if (impresora.esTipoUSB()) {
            return imprimirPorUSB(impresora.getDestino(), ZPLguia);
        } else {
            return imprimirPorIP(impresora.getDestino(), ZPLguia);
        }
    }

    private boolean disponiblePorIP(String ipImpresora) {
        try (Socket socket = new Socket()) {
            socket.connect(
                    new InetSocketAddress(ipImpresora, 9100),
                    3000
            );
            return true;
        } catch (Exception e) {
            System.out.println(
                    "Impresora IP no disponible ["
                    + ipImpresora
                    + "] -> "
                    + e.getMessage()
            );
            return false;
        }
    }

    private boolean imprimirPorIP(String ipImpresora, StringBuilder ZPLguia) {
        try (
                Socket socket = new Socket(ipImpresora, 9100);
                OutputStream out = socket.getOutputStream()) {
            out.write(ZPLguia.toString().getBytes());
            out.flush();
            System.out.println("IMPRESO vía IP [" + ipImpresora + "]");
            return true;
        } catch (Exception e) {
            System.err.println("Error al imprimir por IP [" + ipImpresora + "]: " + e.getMessage());
            ArchivoLogs.getInstance().grabaLogFileAdministrador(
                    "------ Excepción - Imprimir IP [" + ipImpresora + "] - " + e.getMessage(), true);
            return false;
        }
    }

    private boolean disponiblePorUSB(String nombreImpresora) {
        return buscarServicioUSB(nombreImpresora) != null;
    }

    private boolean imprimirPorUSB(String nombreImpresora, StringBuilder ZPLguia) {
        PrintService ps = buscarServicioUSB(nombreImpresora);
        if (ps == null) {
            System.err.println("Impresora USB no encontrada: [" + nombreImpresora + "]");
            ArchivoLogs.getInstance().grabaLogFileAdministrador(
                    "------ Impresora USB no encontrada [" + nombreImpresora + "]", true);
            return false;
        }
        try {
            DocPrintJob job = ps.createPrintJob();
            DocFlavor flavor = DocFlavor.BYTE_ARRAY.AUTOSENSE;
            Doc doc = new SimpleDoc(ZPLguia.toString().getBytes(), flavor, null);
            job.print(doc, null);
            System.out.println("IMPRESO vía USB [" + nombreImpresora + "]");
            return true;
        } catch (PrintException e) {
            System.err.println("Error al imprimir por USB [" + nombreImpresora + "]: " + e.getMessage());
            ArchivoLogs.getInstance().grabaLogFileAdministrador(
                    "------ Excepción - Imprimir USB [" + nombreImpresora + "] - " + e.getMessage(), true);
            return false;
        }
    }

    /**
     * Recorre las impresoras instaladas en el SO y retorna la que coincida con
     * el nombre configurado. Retorna null si no la encuentra.
     */
    private PrintService buscarServicioUSB(String nombreImpresora) {
        PrintService[] disponibles = PrintServiceLookup.lookupPrintServices(null, null);
        for (PrintService ps : disponibles) {
            String nombre = ((PrinterName) ps.getAttribute(PrinterName.class)).getValue();
            if (nombre.contains(nombreImpresora)) {
                return ps;
            }
        }
        System.out.println("No se encontró la impresora USB [" + nombreImpresora + "] "
                + "en el sistema.");
        return null;
    }
}

package com.guatex.igconfiguraciones.gestiones;

import com.guatex.igconfiguraciones.entidades.E_RespuestaImpresion;
import com.guatex.igconfiguraciones.loggs.ArchivoLogs;
import com.guatex.igconfiguraciones.proceso.PrintJobWatcher;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.net.Socket;
import javax.print.Doc;
import javax.print.DocFlavor;
import javax.print.DocPrintJob;
import javax.print.PrintException;
import javax.print.PrintService;
import javax.print.SimpleDoc;
import javax.print.attribute.Attribute;
import javax.print.attribute.PrintServiceAttributeSet;
import javax.print.attribute.standard.PrinterIsAcceptingJobs;
import javax.print.attribute.standard.PrinterState;
import javax.print.attribute.standard.PrinterStateReason;
import javax.print.attribute.standard.PrinterStateReasons;

/**
 *
 * @author ESTEFANIECM
 */
public class G_Impresora {

    public boolean impresoraDisponible(String ipImpresora) {
        try (Socket socket = new Socket()) {

            socket.connect(
                    new InetSocketAddress(ipImpresora, 9100),
                    3000
            );

            return true;

        } catch (Exception e) {

            System.out.println(
                    "Impresora no disponible ["
                    + ipImpresora
                    + "] -> "
                    + e.getMessage()
            );

            return false;
        }
    }

    public boolean imprimirGuia(String ipImpresora, StringBuilder ZPLguia) {
        boolean respuesta = false;
        try (
                Socket socket = new Socket(ipImpresora, 9100);
                OutputStream out = socket.getOutputStream()) {

            out.write(ZPLguia.toString().getBytes("UTF-8"));
            out.flush();
            System.out.println("IMPRESO");

            return true;

        } catch (Exception e) {

            System.err.println(e.getMessage());

            return false;
        }
    }

    public boolean impresoraLista(PrintService psZebra) {
        System.out.println("************************* VERIFICANDO IMPRESORA *************************");
        // Obtiene el conjunto de atributos del servicio de impresión.
        PrintServiceAttributeSet attributes = psZebra.getAttributes();

        // Imprimir todos los atributos
        System.out.println("   > Atributos de la impresora:");
        Attribute[] attrs;
        attrs = attributes.toArray();
        for (Attribute attr : attrs) {
            System.out.println("     - " + attr.getName() + ": " + attributes.get(attr.getClass()));
        }

        // Obtiene el estado actual de la impresora.
        PrinterState printerState = (PrinterState) attributes.get(PrinterState.class);
        System.out.println("   > Estado de la impresora: [" + (printerState != null ? printerState.toString() : "Desconocido") + "]");

        // Obtiene las razones del estado de la impresora.
        PrinterStateReasons printerStateReasons = (PrinterStateReasons) attributes.get(PrinterStateReasons.class);
        System.out.println("   > Razones del estado de la impresora:");
        if (printerStateReasons != null) {
            for (PrinterStateReason reason : printerStateReasons.keySet()) {
                System.out.println("     - " + reason + ": " + printerStateReasons.get(reason));
            }
        } else {
            System.out.println("     [No hay razones de estado disponibles.]");
        }

        // Verifica si la impresora está aceptando trabajos de impresión.
        PrinterIsAcceptingJobs printerAcceptingJobs = (PrinterIsAcceptingJobs) attributes.get(PrinterIsAcceptingJobs.class);
        System.out.println("   > La impresora está aceptando trabajos: [" + (printerAcceptingJobs == PrinterIsAcceptingJobs.ACCEPTING_JOBS) + "]");
        System.out.println("*************************************************************************");
        // Comprueba si la impresora está en estado "IDLE" (inactiva pero lista) y si está aceptando trabajos.
        if (printerState == PrinterState.IDLE && printerAcceptingJobs == PrinterIsAcceptingJobs.ACCEPTING_JOBS) {
            return true;
        }

        return false;
    }

}

package com.guatex.igconfiguraciones.gestiones;

import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.net.Socket;

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
        try (
                Socket socket = new Socket(ipImpresora, 9100);
                OutputStream out = socket.getOutputStream()) {

            out.write(ZPLguia.toString().getBytes("UTF-8"));
            out.flush();

            return true;

        } catch (Exception e) {
            System.out.println("Error al imprimir [" + ipImpresora + "]");
            System.err.println(e.getMessage());

            return false;
        }
    }
}

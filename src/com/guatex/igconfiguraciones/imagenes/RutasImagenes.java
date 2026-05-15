package com.guatex.igconfiguraciones.imagenes;

import java.awt.Image;
import javax.swing.ImageIcon;

/**
 *
 * @author ESTEFANIECM
 */
public class RutasImagenes {

    public Image getIcono() {
        return new ImageIcon(RutasImagenes.class.getResource("printerGTX.png")).getImage();
    }

    public Image getLoader() {
        return new ImageIcon(RutasImagenes.class.getResource("cargando.gif")).getImage();
    }

    public Image getCheck() {
        return new ImageIcon(RutasImagenes.class.getResource("check.png")).getImage();
    }

    public Image getIconoForm() {
        return new ImageIcon(RutasImagenes.class.getResource("print.png")).getImage();
    }
}

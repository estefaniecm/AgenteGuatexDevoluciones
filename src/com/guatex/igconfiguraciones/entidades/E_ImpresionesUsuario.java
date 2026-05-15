package com.guatex.igconfiguraciones.entidades;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author ESTEFANIECM
 *
 */
public class E_ImpresionesUsuario {

    private E_Usuario usuario;
    private List<E_Guia> guiasImpresion = new ArrayList<>();

    public E_Usuario getUsuario() {
        return usuario;
    }

    public void setUsuario(E_Usuario usuario) {
        this.usuario = usuario;
    }

    public List<E_Guia> getGuiasImpresion() {
        return guiasImpresion;
    }

    public void setGuiasImpresion(List<E_Guia> guiasImpresion) {
        this.guiasImpresion = guiasImpresion;
    }

    @Override
    public String toString() {
        return "E_ImpresionesUsuario{" + "usuario=" + usuario + ", guiasImpresion=" + guiasImpresion + '}';
    }

}

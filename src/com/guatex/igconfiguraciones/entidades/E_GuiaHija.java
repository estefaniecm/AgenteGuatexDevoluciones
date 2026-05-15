package com.guatex.igconfiguraciones.entidades;

/**
 *
 * @author ESTEFANIECM
 */
public class E_GuiaHija {

    private String hguiaHija = "";
    private String hnoguiaMadre = "";
    private String pFecha = "";
    private String pHora = "";
    private String pEstatus = "";
    private String hestatus = "";

    public String getHguiaHija() {
        return hguiaHija;
    }

    public void setHguiaHija(String HguiaHija) {
        this.hguiaHija =  quitaNulo(HguiaHija);
    }

    public String getHnoguiaMadre() {
        return hnoguiaMadre;
    }

    public void setHnoguiaMadre(String HnoguiaMadre) {
        this.hnoguiaMadre =  quitaNulo(HnoguiaMadre);
    }

    public String getpFecha() {
        return pFecha;
    }

    public void setpFecha(String pFecha) {
        this.pFecha =  quitaNulo(pFecha);
    }

    public String getpHora() {
        return pHora;
    }

    public void setpHora(String pHora) {
        this.pHora =  quitaNulo(pHora);
    }

    public String getpEstatus() {
        return pEstatus;
    }

    public void setpEstatus(String pEstatus) {
        this.pEstatus =  quitaNulo(pEstatus);
    }

    public String getHestatus() {
        return hestatus;
    }

    public void setHestatus(String Hestatus) {
        this.hestatus =  quitaNulo(Hestatus);
    }
    
     private String quitaNulo(String txt) {
        return txt == null ? "" : txt.trim();
    }

    @Override
    public String toString() {
        return "E_GuiaHija{" + "HguiaHija=" + hguiaHija + ", HnoguiaMadre=" + hnoguiaMadre + ", pFecha=" + pFecha + ", pHora=" + pHora + ", pEstatus=" + pEstatus + ", Hestatus=" + hestatus + '}';
    }

}

package com.guatex.igconfiguraciones.entidades;

/**
 *
 * @author ESTEFANIECM
 */
public class E_GuiaDetalle {
   
    private String linea;
    private String piezas;
    private String tipoEnvio;
    private String peso;
    private String tarifa;
    private String manual;
    private String pBultos;
    private String numeroGuia;
    
    public String getLinea() {
        return linea;
    }

    public void setLinea(String linea) {
        this.linea = quitaNulo(linea);
    }

    public String getPiezas() {
        return piezas;
    }

    public void setPiezas(String piezas) {
        this.piezas = quitaNulo(piezas);
    }

    public String getTipoEnvio() {
        return tipoEnvio;
    }

    public void setTipoEnvio(String tipoEnvio) {
        this.tipoEnvio = quitaNulo(tipoEnvio);
    }

    public String getPeso() {
        return peso;
    }

    public void setPeso(String peso) {
        this.peso = quitaNulo(peso);
    }

    public String getTarifa() {
        return tarifa;
    }

    public void setTarifa(String tarifa) {
        this.tarifa = quitaNulo(tarifa);
    }

    public String getManual() {
        return manual;
    }

    public void setManual(String manual) {
        this.manual = quitaNulo(manual);
    }

    public String getpBultos() {
        return pBultos;
    }

    public void setpBultos(String pBultos) {
        this.pBultos = quitaNulo(pBultos);
    }

    public String getNumeroGuia() {
        return numeroGuia;
    }

    public void setNumeroGuia(String numeroGuia) {
        this.numeroGuia = quitaNulo(numeroGuia);
    }

     private String quitaNulo(String txt) {
        return txt == null ? "" : txt.trim();
    }

    @Override
    public String toString() {
        return "E_GuiaDetalle{" + "linea=" + linea + ", piezas=" + piezas + ", tipoEnvio=" + tipoEnvio + ", peso=" + peso + ", tarifa=" + tarifa + ", manual=" + manual + ", pBultos=" + pBultos + ", numeroGuia=" + numeroGuia + '}';
    }
    
}

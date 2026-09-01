package com.guatex.igconfiguraciones.entidades;

/**
 *
 * @author ESTEFANICM
 */
public class E_ActualizarImpresion {

    private String noguia;
    private String ip;
    private String usuario;
    private String fechaImpresion;
    private String tipoGuia;
    private int numeroSolucion;

    public E_ActualizarImpresion() {
    }

    public E_ActualizarImpresion(String noguia, String ip, String usuario, String fechaImpresion, String tipoGuia, int numeroSolucion) {
        this.noguia = noguia;
        this.ip = ip;
        this.usuario = usuario;
        this.fechaImpresion = fechaImpresion;
        this.tipoGuia = tipoGuia;
        this.numeroSolucion = numeroSolucion;
    }

    public int getNumeroSolucion() {
        return numeroSolucion;
    }

    public void setNumeroSolucion(int numeroSolucion) {
        this.numeroSolucion = numeroSolucion;
    }

    public String getTipoGuia() {
        return tipoGuia;
    }

    public void setTipoGuia(String tipoGuia) {
        this.tipoGuia = tipoGuia;
    }

    public String getUsuario() {
        return usuario;
    }

    public void setUsuario(String usuario) {
        this.usuario = usuario;
    }

    public String getFechaImpresion() {
        return fechaImpresion;
    }

    public void setFechaImpresion(String fechaImpresion) {
        this.fechaImpresion = fechaImpresion;
    }

    public String getNoguia() {
        return noguia;
    }

    public void setNoguia(String noguia) {
        this.noguia = noguia;
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }
}

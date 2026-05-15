package com.guatex.igconfiguraciones.entidades;

import java.util.UUID;

/**
 *
 * @author ESTEFANIECM
 */
public class E_Servicio {

    private String idServicio;
    private String idImpresora;
    private String idUsuario;
    private String fechaServicio;

    public String getIdServicio() {
        return idServicio;
    }

    public void setIdServicio(String idServicio) {
        this.idServicio = idServicio;
    }

    public String getIdImpresora() {
        return idImpresora;
    }

    public void setIdImpresora(String idImpresora) {
        this.idImpresora = idImpresora;
    }

    public String getIdUsuario() {
        return idUsuario;
    }

    public void setIdUsuario(String idUsuario) {
        this.idUsuario = idUsuario;
    }

    public String getFechaServicio() {
        return fechaServicio;
    }

    public void setFechaServicio(String fechaServicio) {
        this.fechaServicio = fechaServicio;
    }

    public static String generaIDservicio() {
        return UUID.randomUUID().toString();
    }

    public String servicioTexto() {
        return idServicio + "," + idImpresora + "," + idUsuario + "," + fechaServicio;
    }

    public E_Servicio getServicio(String linea) {
        String[] datos = linea.split(",");
        E_Servicio s = new E_Servicio();
        if (datos.length == 4) {
            s.setIdServicio(datos[0]);
            s.setIdImpresora(datos[1]);
            s.setIdUsuario(datos[2]);
            s.setFechaServicio(datos[3]);
        }
        return s;
    }

    @Override
    public String toString() {
        return "E_Servicio{" + "idServicio=" + idServicio + ", idImpresora=" + idImpresora + ", idUsuario=" + idUsuario + ", fechaServicio=" + fechaServicio + '}';
    }

}

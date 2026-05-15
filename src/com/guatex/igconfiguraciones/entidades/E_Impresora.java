package com.guatex.igconfiguraciones.entidades;

import java.util.UUID;

/**
 *
 * @author ESTEFANIECM
 */
public class E_Impresora {

    private String idImpresora = "";
    private String nombre = "";
    private String ip = "";
    private String tipo = "";

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public String getIdImpresora() {
        return idImpresora;
    }

    public void setIdImpresora(String idImpresora) {
        this.idImpresora = idImpresora;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    public static String generaIDimpresora() {
        return UUID.randomUUID().toString();
    }

    public String impresoraTexto() {
        return idImpresora + "," + nombre + "," + ip;
    }

    public E_Impresora getImpresora(String linea) {
        System.out.println("linea impresora: "+linea);
        String[] datos = linea.split(",");
        E_Impresora i = new E_Impresora();
        if (datos.length == 3) {
            i.setIdImpresora(datos[0]);
            i.setNombre(datos[1]);
            i.setIp(datos[2]);
        }
        return i;
    }

    @Override
    public String toString() {
        return nombre + " - " + ip;
    }

}

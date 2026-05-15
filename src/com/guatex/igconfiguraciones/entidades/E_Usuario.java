package com.guatex.igconfiguraciones.entidades;

import java.util.UUID;

/**
 *
 * @author ESTEFANIECM
 */
public class E_Usuario {
    private String id = "";
    private String usuario = "";

    public String getIdRegistroUsuario() {
        return id;
    }

    public void setIdRegistroUsuario(String idRegistroUsuario) {
        this.id = idRegistroUsuario;
    }
    
    public String getCodigoUsuario() {
        return usuario;
    }

    public void setCodigoUsuario(String codigoUsuario) {
        this.usuario = codigoUsuario;
    }
    public String usuarioTexto() {
        return id + "," + usuario;
    }
    
     public static String generaIDregistro() {
        return UUID.randomUUID().toString();
    }

    public E_Usuario getUsuario(String linea) {
        System.out.println("linea: "+linea);
        String[] datos = linea.split(",");
        System.out.println("datos length: "+datos.length);
        E_Usuario u = new E_Usuario();
        if (datos.length == 2) {
            u.setIdRegistroUsuario(datos[0]);
            u.setCodigoUsuario(datos[1]);
        }
        return u;
    }

    @Override
    public String toString() {
        return usuario;
    }

}

package com.guatex.igconfiguraciones.entidades;

/**
 *
 * @author ESTEFANIECM
 */
public class E_RespuestaImpresion {
    private boolean exito;
    private String mensaje;

    public boolean isExito() {
        return exito;
    }

    public void setExito(boolean exito) {
        this.exito = exito;
    }

    public String getMensaje() {
        return mensaje;
    }

    public void setMensaje(String mensaje) {
        this.mensaje = mensaje;
    }

    @Override
    public String toString() {
        return "E_RespuestaImpresion{" + "exito=" + exito + ", mensaje=" + mensaje + '}';
    }
    
    
}

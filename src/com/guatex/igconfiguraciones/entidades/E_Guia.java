package com.guatex.igconfiguraciones.entidades;

import java.util.List;

/**
 *
 * @author ESTEFANIECM
 */
public class E_Guia {

    private String numeroGuia = "";
    private String nombreRemitente = "";
    private String recoleccionEntrega = "";
    private String direccionRemitente = "";
    private String telefonoRemitente = "";
    private String puntoOrigen = "";
    private String puntoDestino = "";
    private String muniRemitente = "";
    private String descripcionEnvio = "";
    private String codigoCredito = "";
    private String piezas = "";
    private String peso = "";
    private String seguro = "";
    private String formaPago = "";
    private String valorDeclarado = "";
    private String tipoCobro = "";
    private String tarifa = "";
    private String codValorCobrar = "";
    private String seabrepaquete = "";
    private String cobex = "";
    private String fecha = "";
    private String nombreDestinatario = "";
    private String direccionDestinatario = "";
    private String telefonoDestinatario = "";
    private String muniDestinatario = "";
    private String coberturaExtra = "";
    private String guiaMadre = "";
    private String llaveCliente = "";
    private String recogeOficina = "";
    private List<E_GuiaDetalle> lineasDetalle;
    private List<E_GuiaHija> guiasHijas;

    private String tipoGuia = "";
    private String razonNoEntrega = "";
    private String solucionTipo = "";
    private String solucionUbicacionActual = "";
    private String solucionDireccion = "";
    private String solucionTelefono = "";
    private String solucionDetalle = "";
    private String solucionUsuarioRegistro = "";
    private String solucionFechaRegistro = "";
    private int solucionReimpresion = 0;
    private int solucionNumero = 0;

    public int getSolucionReimpresion() {
        return solucionReimpresion;
    }

    public void setSolucionReimpresion(int solucionReimpresion) {
        this.solucionReimpresion = solucionReimpresion;
    }

    public int getSolucionNumero() {
        return solucionNumero;
    }

    public void setSolucionNumero(int solucionNumero) {
        this.solucionNumero = solucionNumero;
    }

    public String getSolucionUbicacionActual() {
        return solucionUbicacionActual;
    }

    public void setSolucionUbicacionActual(String solucionUbicacionActual) {
        this.solucionUbicacionActual = solucionUbicacionActual;
    }

    public String getSolucionDireccion() {
        return solucionDireccion;
    }

    public void setSolucionDireccion(String solucionDireccion) {
        this.solucionDireccion = solucionDireccion;
    }

    public String getSolucionTelefono() {
        return solucionTelefono;
    }

    public void setSolucionTelefono(String solucionTelefono) {
        this.solucionTelefono = solucionTelefono;
    }

    public String getSolucionUsuarioRegistro() {
        return solucionUsuarioRegistro;
    }

    public void setSolucionUsuarioRegistro(String solucionUsuarioRegistro) {
        this.solucionUsuarioRegistro = solucionUsuarioRegistro;
    }

    public String getSolucionFechaRegistro() {
        return solucionFechaRegistro;
    }

    public void setSolucionFechaRegistro(String solucionFechaRegistro) {
        this.solucionFechaRegistro = solucionFechaRegistro;
    }

    public String getUbicacionActual() {
        return solucionUbicacionActual;
    }

    public void setUbicacionActual(String ubicacionActual) {
        this.solucionUbicacionActual = ubicacionActual;
    }

    public String getTipoGuia() {
        return tipoGuia;
    }

    public void setTipoGuia(String tipoGuia) {
        this.tipoGuia = tipoGuia;
    }

    public String getRazonNoEntrega() {
        return razonNoEntrega;
    }

    public void setRazonNoEntrega(String razonNoEntrega) {
        this.razonNoEntrega = razonNoEntrega;
    }

    public String getSolucionTipo() {
        return solucionTipo;
    }

    public void setSolucionTipo(String solucionTipo) {
        this.solucionTipo = solucionTipo;
    }

    public String getSolucionDetalle() {
        return solucionDetalle;
    }

    public void setSolucionDetalle(String solucionDetalle) {
        this.solucionDetalle = solucionDetalle;
    }

    public String getRecogeOficina() {
        return recogeOficina;
    }

    public void setRecogeOficina(String recogeOficina) {
        this.recogeOficina = quitaNulo(recogeOficina);
    }

    public String getLlaveCliente() {
        return llaveCliente;
    }

    public void setLlaveCliente(String llaveCliente) {
        this.llaveCliente = quitaNulo(llaveCliente);
    }

    public String getGuiaMadre() {
        return guiaMadre;
    }

    public void setGuiaMadre(String guiaMadre) {
        this.guiaMadre = quitaNulo(guiaMadre);
    }

    public String getPuntoDestino() {
        return puntoDestino;
    }

    public void setPuntoDestino(String puntoDestino) {
        this.puntoDestino = quitaNulo(puntoDestino);
    }

    public String getPuntoOrigen() {
        return puntoOrigen;
    }

    public void setPuntoOrigen(String puntoOrigen) {
        this.puntoOrigen = quitaNulo(puntoOrigen);
    }

    public String getTarifa() {
        return tarifa;
    }

    public void setTarifa(String tarifa) {
        this.tarifa = quitaNulo(tarifa);
    }

    public String getFormaPago() {
        return formaPago;
    }

    public void setFormaPago(String formaPago) {
        this.formaPago = quitaNulo(formaPago);
    }

    public String getRecoleccionEntrega() {
        return recoleccionEntrega;
    }

    public void setRecoleccionEntrega(String recoleccionEntrega) {
        this.recoleccionEntrega = quitaNulo(recoleccionEntrega);
    }

    public String getNumeroGuia() {
        return numeroGuia;
    }

    public void setNumeroGuia(String numeroGuia) {
        this.numeroGuia = quitaNulo(numeroGuia);
    }

    public String getNombreRemitente() {
        return nombreRemitente;
    }

    public void setNombreRemitente(String nombreRemitente) {
        this.nombreRemitente = quitaNulo(nombreRemitente).toUpperCase();
    }

    public String getDireccionRemitente() {
        return direccionRemitente;
    }

    public void setDireccionRemitente(String direccionRemitente) {
        this.direccionRemitente = spaces(quitaNulo(direccionRemitente).toUpperCase());
    }

    public String getTelefonoRemitente() {
        return telefonoRemitente;
    }

    public void setTelefonoRemitente(String telefonoRemitente) {
        this.telefonoRemitente = quitaNulo(telefonoRemitente);
    }

    public String getMuniRemitente() {
        return muniRemitente.isEmpty() ? "." : muniRemitente;
    }

    public void setMuniRemitente(String muniRemitente) {
        this.muniRemitente = quitaNulo(muniRemitente).toUpperCase();
    }

    public String getDescripcionEnvio() {
        return descripcionEnvio;
    }

    public void setDescripcionEnvio(String descripcionEnvio) {
        this.descripcionEnvio = quitaNulo(descripcionEnvio);
    }

    public String getCodigoCredito() {
        return codigoCredito;
    }

    public void setCodigoCredito(String codigoCredito) {
        this.codigoCredito = quitaNulo(codigoCredito);
    }

    public String getPiezas() {
        return piezas;
    }

    public void setPiezas(String piezas) {
        this.piezas = quitaNulo(piezas);
    }

    public String getPeso() {
        return peso;
    }

    public void setPeso(String peso) {
        this.peso = quitaNulo(peso);
    }

    public String getSeguro() {
        if (this.seguro.isEmpty()) {
            this.seguro = "0.00";
        }
        return seguro;
    }

    public void setSeguro(String seguro) {
        this.seguro = quitaNulo(seguro);
    }

    public String getValorDeclarado() {
        if (this.valorDeclarado.isEmpty()) {
            this.valorDeclarado = "0.00";
        }
        return valorDeclarado;
    }

    public void setValorDeclarado(String valorDeclarado) {
        this.valorDeclarado = quitaNulo(valorDeclarado);
    }

    public String getTipoCobro() {
        return tipoCobro;
    }

    public void setTipoCobro(String tipoCobro) {
        this.tipoCobro = quitaNulo(tipoCobro);
    }

    public List<E_GuiaDetalle> getLineasDetalle() {
        return lineasDetalle;
    }

    public void setLineasDetalle(List<E_GuiaDetalle> lineasDetalle) {
        this.lineasDetalle = lineasDetalle;
    }

    public List<E_GuiaHija> getGuiasHijas() {
        return guiasHijas;
    }

    public void setGuiasHijas(List<E_GuiaHija> guiasHijas) {
        this.guiasHijas = guiasHijas;
    }

    public String getCodValorCobrar() {
        if (this.codValorCobrar.isEmpty()) {
            this.codValorCobrar = "0.00";
        }
        return codValorCobrar;
    }

    public void setCodValorCobrar(String codValorCobrar) {
        this.codValorCobrar = quitaNulo(codValorCobrar);
    }

    public String getSeabrepaquete() {
        if (this.seabrepaquete.isEmpty()) {
            this.seabrepaquete = "N";
        }
        return seabrepaquete;
    }

    public void setSeabrepaquete(String seabrepaquete) {
        this.seabrepaquete = quitaNulo(seabrepaquete);
    }

    public String getCobex() {
        return cobex;
    }

    public void setCobex(String cobex) {
        this.cobex = quitaNulo(cobex);
    }

    public String getFecha() {
        return fecha;
    }

    public void setFecha(String fecha) {
        this.fecha = quitaNulo(fecha);
    }

    public String getNombreDestinatario() {
        return nombreDestinatario;
    }

    public void setNombreDestinatario(String nombreDestinatario) {
        this.nombreDestinatario = quitaNulo(nombreDestinatario).toUpperCase();
    }

    public String getDireccionDestinatario() {
        return direccionDestinatario;
    }

    public void setDireccionDestinatario(String direccionDestinatario) {
        this.direccionDestinatario = spaces(quitaNulo(direccionDestinatario).toUpperCase());
    }

    public String getTelefonoDestinatario() {
        return telefonoDestinatario;
    }

    public void setTelefonoDestinatario(String telefonoDestinatario) {
        this.telefonoDestinatario = quitaNulo(telefonoDestinatario);
    }

    public String getMuniDestinatario() {
        return muniDestinatario.isEmpty() ? "." : muniDestinatario;
    }

    public void setMuniDestinatario(String muniDestinatario) {
        this.muniDestinatario = quitaNulo(muniDestinatario);
    }

    public String getCoberturaExtra() {
        return coberturaExtra;
    }

    public void setCoberturaExtra(String coberturaExtra) {
        this.coberturaExtra = quitaNulo(coberturaExtra);
    }

    private String quitaNulo(String txt) {
        return txt == null ? "" : txt.trim();
    }

    private String spaces(String txt) {
        return (txt.replaceAll("\\s+", " ").replaceAll("\\.NULL\\.", "")).trim();
    }

    @Override
    public String toString() {
        return "E_Guia{" + "numeroGuia=" + numeroGuia + ", nombreRemitente=" + nombreRemitente + ", recoleccionEntrega=" + recoleccionEntrega + ", direccionRemitente=" + direccionRemitente + ", telefonoRemitente=" + telefonoRemitente + ", puntoOrigen=" + puntoOrigen + ", puntoDestino=" + puntoDestino + ", muniRemitente=" + muniRemitente + ", descripcionEnvio=" + descripcionEnvio + ", codigoCredito=" + codigoCredito + ", piezas=" + piezas + ", peso=" + peso + ", seguro=" + seguro + ", formaPago=" + formaPago + ", valorDeclarado=" + valorDeclarado + ", tipoCobro=" + tipoCobro + ", tarifa=" + tarifa + ", lineasDetalle=" + lineasDetalle + ", guiasHijas=" + guiasHijas + ", codValorCobrar=" + codValorCobrar + ", seabrepaquete=" + seabrepaquete + ", cobex=" + cobex + ", fecha=" + fecha + ", nombreDestinatario=" + nombreDestinatario + ", direccionDestinatario=" + direccionDestinatario + ", telefonoDestinatario=" + telefonoDestinatario + ", muniDestinatario=" + muniDestinatario + ", coberturaExtra=" + coberturaExtra + ", guiaMadre=" + guiaMadre + ", llaveCliente=" + llaveCliente + ", recogeOficina=" + recogeOficina + '}';
    }

}

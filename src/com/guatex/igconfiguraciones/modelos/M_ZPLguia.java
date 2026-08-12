package com.guatex.igconfiguraciones.modelos;

import com.guatex.igconfiguraciones.entidades.E_Guia;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 *
 * @author ESTEFANIECM
 */
public class M_ZPLguia {

    private final int inicioTexto = 160;

    public StringBuilder generaZPLDevolucion(E_Guia guia, int contadorPiezas, String guiaImprimir) {
        StringBuilder ZPLGuiaImprimir = new StringBuilder();
        try {
            ZPLGuiaImprimir.append("^XA");
            ZPLGuiaImprimir.append(configuracionImpresion());
            ZPLGuiaImprimir.append(encabezadoEtiqueta(guia, guiaImprimir));

            ZPLGuiaImprimir.append(nombreDestinatarioOriginal(guia));
            ZPLGuiaImprimir.append(direccionDestinatarioOriginal(guia));
            ZPLGuiaImprimir.append(telefonoDestinatarioOriginal(guia));

            ZPLGuiaImprimir.append(nombreDestinatarioFinal(guia));
            ZPLGuiaImprimir.append(direccionDestinatarioFinal(guia));
            ZPLGuiaImprimir.append(telefonoDestinatarioFinal(guia));

            ZPLGuiaImprimir.append(noGuiaImagen(guia, guiaImprimir));
            ZPLGuiaImprimir.append(puntoDestinoFinalDEV(guia));
            ZPLGuiaImprimir.append(informacionGuia(guia));
            ZPLGuiaImprimir.append(QRpiezas(guia, contadorPiezas));
            ZPLGuiaImprimir.append("^XZ");
        } catch (Exception e) {
            System.out.println("----------");
            e.printStackTrace();
            System.out.println("----------");
            System.out.println(e.getLocalizedMessage());
        }
        System.out.println(":D ZPL: " + ZPLGuiaImprimir.toString());
        return ZPLGuiaImprimir;
    }

    private StringBuilder configuracionImpresion() {
        StringBuilder config = new StringBuilder();
        config.append("^SZ2");//Tipo de ZPL 2=ZPLII
        config.append("^PW609^LL1250");//Ancho de etiqueta valor en Pixeles 609 = 3", ^LL Largo de etiqueta valor en Pixeles 1218 = 6"
        config.append("^PON");//Orientacion de Impresion N=Normal I=Invertido
        config.append("^PR6,6");//Velocidad de impresion  3
        config.append("^PMN");//Print Mirror image  N=no Y=yes
        config.append("^MNY");//"^XA"/*Tipo de deteccion de medios Y= No continuo Web Sensing*/
        config.append("^LS-20");//con este comando se ha variado el margen izquierdo para este tipo de etiqueta
        config.append("^MTD");//Tipo de Medio D=Directo T=Transferencia
        config.append("^MMT,N");//MMT=Metodo de prepelar  N= No
        config.append("^MPE");//Modo de Proteccion E= Encendido todo
        config.append("^FS");
        config.append("^JUS");
        config.append("^LRN");
        config.append("^CI28");
        config.append("^FS");
        config.append("^FO1,1,0");
        config.append("^A0,N,30,30");
        config.append("^FD");
        config.append("^FS");
        config.append("^FT1,").append(inicioTexto);//Texto inicia en posición 1pulgada desde el borde izq y 160 puntos desde el borde superior 
        return config;
    }

    private StringBuilder encabezadoEtiqueta(E_Guia guia, String guiaImprimir) {
        StringBuilder header = new StringBuilder();
        header.append("^BY3");
        header.append("^A0N,40,30");
        header.append("^BC,100,N,N,N,A");
        header.append("^A2N,40,30");
        header.append("^FD").append(guiaImprimir);
        header.append("^FS");

        String textoLinea;
        if (guia.getGuiaMadre().equals("S")) {
            textoLinea = guia.getPuntoOrigen() + " " + guia.getNumeroGuia();
        } else {
            textoLinea = guia.getPuntoOrigen() + " " + guiaImprimir + " - " + guia.getNumeroGuia();
        }
        header.append("^FO3,").append(inicioTexto + 10);
        header.append(GeneradorZPL.generarCampoGF(textoLinea, 23));
        header.append("^FS");

        return header;
    }

    private StringBuilder nombreDestinatarioOriginal(E_Guia guia) {

        StringBuilder Nremitente = new StringBuilder();
        StringBuilder nombreR = new StringBuilder();

        nombreR.append("Destinatario original: ").append(guia.getNombreRemitente());

        int longitud = nombreR.length();

        if (longitud <= 65) {

            Nremitente.append("^FO0,")
                    .append(inicioTexto + 45)
                    .append(",0");

            Nremitente.append("^A0N,20,17");

            Nremitente.append("^FH\\");
            Nremitente.append("^FD")
                    .append(nombreR);

            Nremitente.append("^FS");

        } else {

            int maxLineLength1 = 65;
            int maxLineLength2 = 62;

            int start = 0;
            int end = 0;

            end = start + maxLineLength1;

            if (end > longitud) {
                end = longitud;
            }

            Nremitente.append("^FO0,")
                    .append(inicioTexto + 45)
                    .append(",0");

            Nremitente.append("^A0N,20,17");

            Nremitente.append("^FH\\");
            Nremitente.append("^FD")
                    .append(nombreR.substring(start, end).trim());

            Nremitente.append("^FS");

            start = end;

            if (longitud > start) {

                end = start + maxLineLength2;

                if (end > longitud) {
                    end = longitud;
                }

                Nremitente.append("^FO0,")
                        .append(inicioTexto + 67)
                        .append(",0");

                // segunda línea aún más compacta
                Nremitente.append("^A0N,20,17");

                Nremitente.append("^FH\\");
                Nremitente.append("^FD")
                        .append(nombreR.substring(start, end).trim());

                Nremitente.append("^FS");
            }
        }

        return Nremitente;
    }

    private StringBuilder direccionDestinatarioOriginal(E_Guia guia) {

        StringBuilder Dremitente = new StringBuilder();

        String direccion
                = "Dirección original: "
                + guia.getDireccionRemitente()
                + " "
                + guia.getMuniRemitente();

        int longitud = direccion.length();

        if (longitud <= 65) {

            Dremitente.append("^FO0,")
                    .append(inicioTexto + 95)
                    .append(",0");

            Dremitente.append("^A0N,20,17");

            Dremitente.append("^FH\\");
            Dremitente.append("^FD")
                    .append(direccion.trim());

            Dremitente.append("^FS");

        } else {

            int maxLineLength = 65;

            int start = 0;

            int end = Math.min(start + maxLineLength, longitud);

            Dremitente.append("^FO0,")
                    .append(inicioTexto + 95)
                    .append(",0");

            Dremitente.append("^A0N,20,17");

            Dremitente.append("^FH\\");
            Dremitente.append("^FD")
                    .append(direccion.substring(start, end).trim());

            Dremitente.append("^FS");

            start = end;

            if (longitud > start) {

                end = Math.min(start + maxLineLength, longitud);

                Dremitente.append("^FO0,")
                        .append(inicioTexto + 117)
                        .append(",0");

                Dremitente.append("^A0N,20,17");

                Dremitente.append("^FH\\");
                Dremitente.append("^FD")
                        .append(direccion.substring(start, end).trim());

                Dremitente.append("^FS");
            }

            if (longitud > end) {

                Dremitente.append("^FO0,")
                        .append(inicioTexto + 139)
                        .append(",0");

                Dremitente.append("^A0N,20,17");

                Dremitente.append("^FH\\");
                Dremitente.append("^FD")
                        .append(direccion.substring(end).trim());

                Dremitente.append("^FS");
            }
        }

        return Dremitente;
    }

    private StringBuilder telefonoDestinatarioOriginal(E_Guia guia) {
        StringBuilder Tdestinatario = new StringBuilder();

        Tdestinatario.append("^FO0,").append(inicioTexto + 167).append(",0");
        Tdestinatario.append("^A0,20,17");
        Tdestinatario.append("^FH\\");
        Tdestinatario.append("^FD").append("Teléfono: ").append(guia.getTelefonoRemitente());
        Tdestinatario.append("^FS");

        return Tdestinatario;
    }

    private StringBuilder nombreDestinatarioFinal(E_Guia guia) {
        StringBuilder Ndestinatario = new StringBuilder();
        StringBuilder nombreD = new StringBuilder();
        nombreD.append("Destinatario final: ").append(guia.getNombreDestinatario());
        int longitud = nombreD.length();
        double CODvalorCobrar = Double.parseDouble(guia.getCodValorCobrar());

        if (CODvalorCobrar > 0) {
            if (longitud <= 40) {
                Ndestinatario.append("^FO0,").append(inicioTexto + 195).append(",0");
                Ndestinatario.append("^A0N,N,25,25");
                Ndestinatario.append("^FH\\");
                Ndestinatario.append("^FD").append(nombreD);
                Ndestinatario.append("^FS");
            } else {
                int maxLineLength1 = 40;
                int maxLineLength2 = 38;
                int start = 0;
                int end = 0;

                // Primera parte (0 a 40)
                end = start + maxLineLength1;
                if (end > longitud) {
                    end = longitud;
                }
                Ndestinatario.append("^FO0,").append(inicioTexto + 195).append(",0");
                Ndestinatario.append("^A0N,N,25,25");
                Ndestinatario.append("^FH\\");
                Ndestinatario.append("^FD").append(nombreD.substring(start, end).trim());
                Ndestinatario.append("^FS");

                start = end;

                // Segunda parte (40 a 80)
                if (longitud > start) {
                    end = start + maxLineLength2;
                    if (end > longitud) {
                        end = longitud;
                    }
                    Ndestinatario.append("^FO0,").append(inicioTexto + 220).append(",0");
                    Ndestinatario.append("^A0N,N,25,25");
                    Ndestinatario.append("^FH\\");
                    Ndestinatario.append("^FD").append(nombreD.substring(start, end).trim());
                    Ndestinatario.append("^FS");

                    start = end;
                }
            }

        } else {
            if (longitud <= 45) {
                Ndestinatario.append("^FO0,").append(inicioTexto + 195).append(",0");
                Ndestinatario.append("^A0N,N,25,25");
                Ndestinatario.append("^FH\\");
                Ndestinatario.append("^FD").append(nombreD);
                Ndestinatario.append("^FS");
            } else {
                int maxLineLength1 = 45;
                int maxLineLength2 = 43;
                int start = 0;
                int end = 0;

                // Primera parte 
                end = start + maxLineLength1;
                if (end > longitud) {
                    end = longitud;
                }
                Ndestinatario.append("^FO0,").append(inicioTexto + 195).append(",0");
                Ndestinatario.append("^A0N,N,25,25");
                Ndestinatario.append("^FH\\");
                Ndestinatario.append("^FD").append(nombreD.substring(start, end).trim());
                Ndestinatario.append("^FS");

                start = end;

                // Segunda parte 
                if (longitud > start) {
                    end = start + maxLineLength2;
                    if (end > longitud) {
                        end = longitud;
                    }
                    Ndestinatario.append("^FO0,").append(inicioTexto + 220).append(",0");//340
                    Ndestinatario.append("^A0N,N,25,25");
                    Ndestinatario.append("^FH\\");
                    Ndestinatario.append("^FD").append(nombreD.substring(start, end).trim());
                    Ndestinatario.append("^FS");

                    start = end;
                }
            }

        }
        return Ndestinatario;
    }

    private StringBuilder direccionDestinatarioFinal(E_Guia guia) {
        StringBuilder Ddestinatario = new StringBuilder();
        StringBuilder direccionD = new StringBuilder();
        String dD = "Dirección final: " + guia.getDireccionDestinatario() + " " + guia.getMuniDestinatario();
        int longdD = dD.length();

        // Limita la longitud de la dirección inicial si excede los 130 caracteres
        if (longdD > 130) {
            int longitudMCNPORI = guia.getMuniDestinatario().length();
            dD = dD.substring(0, 130);
            dD = dD.substring(0, (longdD - longitudMCNPORI));
            dD = dD + " " + guia.getMuniRemitente();
        }

        direccionD.append(dD);
        int longitud = direccionD.length();
        double CODvalorCobrar = Double.parseDouble(guia.getCodValorCobrar());

        // Si CODvalorCobrar es mayor que 0
        if (CODvalorCobrar > 0) {
            if (longitud <= 40) {
                Ddestinatario.append("^FO0,").append(inicioTexto + 250).append(",0");
                Ddestinatario.append("^A0N,N,25,25");
                Ddestinatario.append("^FH\\");
                Ddestinatario.append("^FD").append(direccionD);
                Ddestinatario.append("^FS");
            } else {
                int maxLineLength = 40;
                int start = 0;

                // Procesa la primera parte (0 a 40)
                int end = Math.min(start + maxLineLength, longitud);
                Ddestinatario.append("^FO0,").append(inicioTexto + 250).append(",0");
                Ddestinatario.append("^A0N,N,25,25");
                Ddestinatario.append("^FH\\");
                Ddestinatario.append("^FD").append(direccionD.substring(start, end).trim());
                Ddestinatario.append("^FS");

                start = end;

                // Procesa la segunda parte (40 a 80)
                if (longitud > start) {
                    end = Math.min(start + maxLineLength, longitud);
                    Ddestinatario.append("^FO0,").append(inicioTexto + 275).append(",0");
                    Ddestinatario.append("^A0N,N,25,25");
                    Ddestinatario.append("^FH\\");
                    Ddestinatario.append("^FD").append(direccionD.substring(start, end).trim());
                    Ddestinatario.append("^FS");

                    start = end;
                }

                // Procesa la tercera parte (80 a 120)
                if (longitud > start) {
                    end = Math.min(start + maxLineLength, longitud);
                    Ddestinatario.append("^FO0,").append(inicioTexto + 300).append(",0");//415
                    Ddestinatario.append("^A0N,N,25,25");
                    Ddestinatario.append("^FH\\");
                    Ddestinatario.append("^FD").append(direccionD.substring(start, end).trim());
                    Ddestinatario.append("^FS");
                }
            }

            // Si CODvalorCobrar es 0 o menor
        } else {
            if (longitud <= 45) {
                Ddestinatario.append("^FO0,").append(inicioTexto + 250).append(",0");//365
                Ddestinatario.append("^A0N,N,25,25");
                Ddestinatario.append("^FH\\");
                Ddestinatario.append("^FD").append(direccionD);
                Ddestinatario.append("^FS");
            } else {
                int maxLineLength = 45;
                int start = 0;

                // Procesa la primera parte (0 a 45)
                int end = Math.min(start + maxLineLength, longitud);
                Ddestinatario.append("^FO0,").append(inicioTexto + 250).append(",0");//365
                Ddestinatario.append("^A0N,N,25,25");
                Ddestinatario.append("^FH\\");
                Ddestinatario.append("^FD").append(direccionD.substring(start, end).trim());
                Ddestinatario.append("^FS");

                start = end;

                // Procesa la segunda parte (45 a 90)
                if (longitud > start) {
                    end = Math.min(start + maxLineLength, longitud);
                    Ddestinatario.append("^FO0,").append(inicioTexto + 275).append(",0");//390
                    Ddestinatario.append("^A0N,N,25,25");
                    Ddestinatario.append("^FH\\");
                    Ddestinatario.append("^FD").append(direccionD.substring(start, end).trim());
                    Ddestinatario.append("^FS");

                    start = end;
                }

                // Procesa la tercera parte (90 a 135)
                if (longitud > start) {
                    end = Math.min(start + maxLineLength, longitud);
                    Ddestinatario.append("^FO0,").append(inicioTexto + 300).append(",0");//415
                    Ddestinatario.append("^A0N,N,25,25");
                    Ddestinatario.append("^FH\\");
                    Ddestinatario.append("^FD").append(direccionD.substring(start, end).trim());
                    Ddestinatario.append("^FS");
                }
            }
        }
        return Ddestinatario;
    }

    private StringBuilder telefonoDestinatarioFinal(E_Guia guia) {
        StringBuilder Tremitente = new StringBuilder();
        Tremitente.append("^FO0,").append(inicioTexto + 330).append(",0");
        Tremitente.append("^A0,N,25,25");
        Tremitente.append("^FH\\");
        Tremitente.append("^FD").append("Teléfono: ").append(guia.getTelefonoDestinatario());
        Tremitente.append("^FS");
        return Tremitente;
    }

    private StringBuilder noGuiaImagen(E_Guia guia, String guiaImprimir) {
        StringBuilder noGuiaIMG = new StringBuilder();

        String textoNoGuia;
        if (guia.getGuiaMadre().equals("S")) {
            textoNoGuia = guia.getNumeroGuia();
        } else {
            textoNoGuia = guiaImprimir;
        }

        noGuiaIMG.append("^FO10,").append(inicioTexto + 375);
        noGuiaIMG.append(GeneradorZPL.generarCampoGF(textoNoGuia, 40));
        noGuiaIMG.append("^FS");

        noGuiaIMG.append("^FO370,").append(inicioTexto + 315).append(",0");
        noGuiaIMG.append("^IME:IMG.GRF,1,1");
        noGuiaIMG.append("^FS");

        return noGuiaIMG;
    }

    private StringBuilder puntoDestinoFinalDEV(E_Guia guia) {

        StringBuilder ptoDes = new StringBuilder();

        ptoDes.append("^FO160,590");
        ptoDes.append("^GB245,10,130");
        ptoDes.append("^FS");

        ptoDes.append("^LRY");
        ptoDes.append("^FO180,610,0");
        ptoDes.append("^A0,N,125,70");
        ptoDes.append("^FDDEV");
        ptoDes.append("^FS");

        ptoDes.append("^FO85,730,0");
        ptoDes.append("^A0,N,225,70");
        ptoDes.append("^FD").append(guia.getPuntoDestino());

        ptoDes.append("^FS");
        ptoDes.append("^FS");

        return ptoDes;
    }

    private StringBuilder informacionGuia(E_Guia guia) {
        StringBuilder infoGuia = new StringBuilder();
        StringBuilder info = new StringBuilder();
        info.append("No. Piezas: ").append(guia.getPiezas());
        info.append("  Peso: ").append(guia.getPeso());
        if (!guia.getSeguro().equals("0.00")) {
            info.append("  Seg: ").append(guia.getSeguro());
        }
        if (!guia.getValorDeclarado().equals("0.00")) {
            info.append("  V.Dec: ").append(guia.getValorDeclarado());
        }
        info.append("  Forma de pago: ").append(guia.getFormaPago());
        info.append("  Codigo de cobro: ").append(guia.getCodigoCredito());
        info.append("  Fecha: ").append(guia.getFecha());
        if (!guia.getFormaPago().equals("CREDITO") && guia.getGuiaMadre().equals("S")) {
            info.append("  Tarifa: ").append(guia.getTarifa());
        }
        int longitud = info.length();

        double CODvalorCobrar = Double.parseDouble(guia.getCodValorCobrar());
        if (CODvalorCobrar > 0) {
            if (longitud > 41 && longitud < 98) {
                // Buscar el último espacio antes de cortar en 51 caracteres
                int end1 = info.lastIndexOf(" ", 41);
                if (end1 == -1) {
                    end1 = 41; // Si no hay espacio, corta en 51 caracteres
                }
                // Primera parte (0 a 51)
                infoGuia.append("^FO0,").append(inicioTexto + 755).append(",0");
                infoGuia.append("^A0,N,23,23");
                infoGuia.append("^FD").append(info.substring(0, end1).trim());
                infoGuia.append("^FS");

                // Segunda parte (desde end1 hasta el final)
                if (longitud > end1) {
                    int end2 = info.lastIndexOf(" ", longitud); // Se asegura de no cortar la última palabra
                    if (end2 == -1) {
                        end2 = longitud;
                    }
                    infoGuia.append("^FO0,").append(inicioTexto + 785).append(",0");
                    infoGuia.append("^A0,N,23,23");
                    infoGuia.append("^FD").append(info.substring(end1).trim());
                    infoGuia.append("^FS");
                }

            } else if (longitud > 97) {//107
                // Buscar el último espacio antes de cortar en 51 caracteres
                int end1 = info.lastIndexOf(" ", 51);
                if (end1 == -1) {
                    end1 = 51; // Si no hay espacio, corta en 51 caracteres
                }
                // Primera parte (0 a 51)
                infoGuia.append("^FO0,").append(inicioTexto + 755).append(",0");
                infoGuia.append("^A0,N,23,23");
                infoGuia.append("^FD").append(info.substring(0, end1).trim());
                infoGuia.append("^FS");

                // Buscar el último espacio antes de cortar en 103 caracteres
                int end2 = info.lastIndexOf(" ", 90);//100
                if (end2 == -1) {
                    end2 = 90; // Si no hay espacio, corta en 103 caracteres
                }
                // Segunda parte (51 a 103)
                if (longitud > end1) {
                    infoGuia.append("^FO0,").append(inicioTexto + 785).append(",0");
                    infoGuia.append("^A0,N,23,23");
                    infoGuia.append("^FD").append(info.substring(end1, end2).trim());
                    infoGuia.append("^FS");
                }

                // Tercera parte (103 hasta el final)
                if (longitud > end2) {
                    int end3 = info.lastIndexOf(" ", longitud); // Se asegura de no cortar la última palabra
                    if (end3 == -1) {
                        end3 = longitud;
                    }

                    infoGuia.append("^FO0,").append(inicioTexto + 815).append(",0");
                    infoGuia.append("^A0,N,23,23");
                    infoGuia.append("^FD").append(info.substring(end2).trim());
                    infoGuia.append("^FS");
                }

            } else if (longitud <= 41) {
                infoGuia.append("^FO0,").append(inicioTexto + 755).append(",0");
                infoGuia.append("^A0,N,23,23");
                infoGuia.append("^FD").append(info);
                infoGuia.append("^FS");
            }

        } else {

            if (longitud > 51 && longitud < 108) {
                // Buscar el último espacio antes de cortar en 51 caracteres
                int end1 = info.lastIndexOf(" ", 51);
                if (end1 == -1) {
                    end1 = 51; // Si no hay espacio, corta en 51 caracteres
                }
                infoGuia.append("^FO0,").append(inicioTexto + 755).append(",0");
                infoGuia.append("^A0,N,24,24");
                infoGuia.append("^FD").append(info.substring(0, end1).trim());
                infoGuia.append("^FS");

                // Segunda parte desde el final de la primera hasta el final
                if (longitud > end1) {
                    infoGuia.append("^FO0,").append(inicioTexto + 785).append(",0");
                    infoGuia.append("^A0,N,24,24");
                    infoGuia.append("^FD").append(info.substring(end1).trim());
                    infoGuia.append("^FS");
                }

            } else if (longitud > 107) {
                // Buscar el último espacio antes de cortar en 54 caracteres
                int end1 = info.lastIndexOf(" ", 54);
                if (end1 == -1) {
                    end1 = 54; // Si no hay espacio, corta en 54 caracteres
                }
                infoGuia.append("^FO0,").append(inicioTexto + 755).append(",0");
                infoGuia.append("^A0,N,24,24");
                infoGuia.append("^FD").append(info.substring(0, end1).trim());
                infoGuia.append("^FS");

                // Buscar el último espacio antes de cortar en 110 caracteres
                int end2 = info.lastIndexOf(" ", 110);
                if (end2 == -1) {
                    end2 = 110; // Si no hay espacio, corta en 110 caracteres
                }
                if (longitud > end1) {
                    infoGuia.append("^FO0,").append(inicioTexto + 785).append(",0");
                    infoGuia.append("^A0,N,24,24");
                    infoGuia.append("^FD").append(info.substring(end1, end2).trim());
                    infoGuia.append("^FS");
                }

                // Tercera parte desde el final de la segunda hasta el final
                if (longitud > end2) {
                    infoGuia.append("^FO0,").append(inicioTexto + 815).append(",0");
                    infoGuia.append("^A0,N,24,24");
                    infoGuia.append("^FD").append(info.substring(end2).trim());
                    infoGuia.append("^FS\n");
                }

            } else if (longitud <= 51) {
                infoGuia.append("^FO0,").append(inicioTexto + 755).append(",0");
                infoGuia.append("^A0,N,24,24");
                infoGuia.append("^FD").append(info);
                infoGuia.append("^FS");
            }
        }
        return infoGuia;
    }

    private StringBuilder QRpiezas(E_Guia guia, int contadorPiezas) {
        StringBuilder QRPiezas = new StringBuilder();

        // Rectángulo
        QRPiezas.append("^FO0,").append(inicioTexto + 850).append(",0");
        QRPiezas.append("^GB435,32,30");
        QRPiezas.append("^FS");

        // Tracking - Términos y Condiciones
        QRPiezas.append("^FO10,").append(inicioTexto + 855).append(",0");
        QRPiezas.append("^A0,N,22,22");
        QRPiezas.append("^FH\\");
        QRPiezas.append("^FDTracking | Condiciones Generales de Servicio\n");
        QRPiezas.append("^FS");

        // QR
        QRPiezas.append("^FO0,").append(inicioTexto + 890).append(",0");   // antes 850
        QRPiezas.append("^BQ,2,4");
        QRPiezas.append("^FDQA,").append("https://servicios.guatex.gt/Guatex/rastreoTracking?tipo=G&dato=" + guia.getNumeroGuia());
        QRPiezas.append("^FS");

        if (Integer.valueOf((guia.getPiezas())) > 99) {
            QRPiezas.append("^FO200,").append(inicioTexto + 935).append(",0"); // antes 935
            QRPiezas.append("^A0,N,100,70");
            QRPiezas.append("^FD").append(contadorPiezas).append("/").append(guia.getPiezas());
            QRPiezas.append("^FS");
        } else {
            QRPiezas.append("^FO230,").append(inicioTexto + 935).append(",0"); // antes 935
            QRPiezas.append("^A0,N,150,70");
            QRPiezas.append("^FD").append(contadorPiezas).append("/").append(guia.getPiezas());
            QRPiezas.append("^FS");
        }

        return QRPiezas;
    }

    public StringBuilder generaZPLSolucion(E_Guia guia, String usuario, LocalDateTime fechaHoraImpresion) {
        StringBuilder zpl = new StringBuilder();

        String textoSolucion = "SOLUCION";
        String fechaImpresionFormateada = fechaHoraImpresion.format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm"));

        zpl.append("^XA");
        zpl.append("^SZ2^PW609^LL1250^PON^PR6,6^PMN");
        zpl.append("^MNY^LS-10^MTD^MMT,N^MPE^FS");
        zpl.append("^JUS^LRN^CI28^FS");

        zpl.append("^FO350,95^IME:IMG.GRF,1,1^FS");

        zpl.append("^FO16,110^A0,N,25,25^FDNúmero de guia^FS");
        zpl.append("^FO15,145").append(GeneradorZPL.generarCampoGF(guia.getNumeroGuia(), 40)).append("^FS");

        zpl.append("^FO15,220^A0,N,24,24^FR^FDDATOS DE LA GUIA^FS");
        zpl.append("^FO0,245^GB590,2,2^FS");

        zpl.append("^FO15,265^A0,N,20,20^FDRazó de No Entrega^FS");
        zpl.append("^FO15,292^A0,N,18,18^FB565,3,0,L,0^FD").append(guia.getRazonNoEntrega()).append("^FS");

        zpl.append("^FO15,350^A0,N,20,20^FDTipo de solución^FS");
        zpl.append("^FO15,377^A0,N,18,18^FB565,3,0,L,0^FD").append(guia.getSolucionTipo()).append("^FS");

        zpl.append("^FO15,435^A0,N,20,20^FDUbicación actual^FS");
        zpl.append("^FO15,462^A0,N,18,18^FB565,3,0,L,0^FD").append(guia.getUbicacionActual()).append("^FS");

        zpl.append("^FO15,520^A0,N,24,24^FDDirección^FS");
        zpl.append("^FO15,547^A0,N,22,22^FB565,3,0,L,0^FD").append(guia.getSolucionDireccion()).append("^FS");

        zpl.append("^FO15,615^A0,N,24,24^FDTeléfono^FS");
        zpl.append("^FO15,642^A0,N,22,22^FB565,2,0,L,0^FD").append(guia.getSolucionTelefono()).append("^FS");

        zpl.append("^FO15,690^A0,N,24,24^FDDetalle de la solución^FS");
        zpl.append("^FO15,717^A0,N,22,22^FB565,11,0,L,0^FD").append(guia.getSolucionDetalle()).append("^FS");

        zpl.append("^FO0,970^GB590,2,2^FS");

        zpl.append("^FO15,980^A0,N,16,16^FDUsuario: ").append(guia.getSolucionUsuarioRegistro()).append("^FS");
        zpl.append("^FO260,980^A0,N,16,16^FDFecha y hora de registro: ").append(guia.getSolucionFechaRegistro()).append("^FS");

        zpl.append("^FO15,1000^A0,N,16,16^FDImpreso: ").append(usuario).append("^FS");
        zpl.append("^FO260,1000^A0,N,16,16^FDFecha y hora de impresión: ").append(fechaImpresionFormateada).append("^FS");

        zpl.append("^FO15,1040^A0,N,15,15^FB565,2,0,C,0^FDETIQUETA DE APOYO OPERATIVO INTERNO. NO SUSTITUYE LA GUIA ORIGINAL DEL ENVIO.^FS");

        zpl.append("^FO20,1080^GB550,100,3^FS");
        zpl.append("^FO26,1086^GB538,88,2^FS");
        zpl.append("^FO40,1110^AAN,N,30,30^FB530,1,0,C,0^FD").append(textoSolucion).append("^FS");

        zpl.append("^XZ");
        System.out.println("ZPL: " + zpl.toString());
        return zpl;
    }

}

package com.guatex.igconfiguraciones.modelos;

import com.guatex.igconfiguraciones.entidades.E_Guia;

/**
 *
 * @author ESTEFANIECM
 */
public class M_ZPLguia {

    private final int inicioTexto = 160;

    public StringBuilder generaZPL(E_Guia guia, int contadorPiezas, String guiaImprimir) {
        StringBuilder ZPLGuiaImprimir = new StringBuilder();
        try {
            ZPLGuiaImprimir.append("^XA\n");
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

        return ZPLGuiaImprimir;
    }

    private StringBuilder configuracionImpresion() {
        StringBuilder config = new StringBuilder();
        config.append("^SZ2");//Tipo de ZPL 2=ZPLII
        config.append("^PW609^LL1256");//Ancho de etiqueta valor en Pixeles 609 = 3", ^LL Largo de etiqueta valor en Pixeles 1218 = 6"
        config.append("^PON");//Orientacion de Impresion N=Normal I=Invertido
        config.append("^PR6,6");//Velocidad de impresion  3
        config.append("^PMN\n");//Print Mirror image  N=no Y=yes
        config.append("^MNY");//"^XA"/*Tipo de deteccion de medios Y= No continuo Web Sensing*/
        config.append("^LS-20");//con este comando se ha variado el margen izquierdo para este tipo de etiqueta
        config.append("^MTD");//Tipo de Medio D=Directo T=Transferencia
        config.append("^MMT,N");//MMT=Metodo de prepelar  N= No
        config.append("^MPE");//Modo de Proteccion E= Encendido todo
        config.append("^FS\n");
        config.append("^JUS");
        config.append("^LRN");
        config.append("^CI28");//CI0
        config.append("^FS\n");
        config.append("^FO1,1,0");
        config.append("^A0,N,30,30");
        config.append("^FD");
        config.append("^FS\n");
        config.append("^FT1,").append(inicioTexto);//Texto inicia en posición 1pulgada desde el borde izq y 160 puntos desde el borde superior 
        return config;
    }

    private StringBuilder encabezadoEtiqueta(E_Guia guia, String guiaImprimir) {
        StringBuilder header = new StringBuilder();
        header.append("^BY3");
        header.append("^A0N,40,30");
        header.append("^BC,100,N,N,N,A");
        header.append("^A2N,40,30");//A0N
        header.append("^FD").append(guiaImprimir);
        header.append("^FS\n");

        header.append("^FO3,").append(inicioTexto + 10).append(",0");//+15
        header.append("^AA,N,15,15");

        if (guia.getGuiaMadre().equals("S")) {
            header.append("^FD").append(guia.getPuntoOrigen()).append(" ").append(guia.getNumeroGuia());
        } else if (guia.getGuiaMadre().equals("N")) {
            header.append("^FD").append(guia.getPuntoOrigen()).append(" ").append(guiaImprimir).append(" - ").append(guia.getNumeroGuia());
        }
        header.append("^FS\n");
        return header;
    }

    private StringBuilder bloqueDestinatarioOriginal(E_Guia guia) {

        StringBuilder bloque = new StringBuilder();

        String nombre = "Destinatario original: " + guia.getNombreDestinatario();
        String direccion = "Dirección original: "
                + guia.getDireccionDestinatario()
                + " "
                + guia.getMuniDestinatario();

        String telefono = "Teléfono: " + guia.getTelefonoDestinatario();

        bloque.append("^FO0,")
                .append(inicioTexto + 45)
                .append("\n");

        bloque.append("^A0N,20,17\n");

        // ancho 550, max 5 líneas, separación 4
        bloque.append("^FB550,5,4,L,0\n");

        bloque.append("^FD")
                .append(nombre)
                .append("\\&")
                .append(direccion)
                .append("\\&")
                .append(telefono);

        bloque.append("^FS\n");

        return bloque;
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

            Nremitente.append("^FS\n");

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

            Nremitente.append("^FS\n");

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

                Nremitente.append("^FS\n");
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

            Dremitente.append("^FS\n");

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

            Dremitente.append("^FS\n");

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

                Dremitente.append("^FS\n");
            }

            if (longitud > end) {

                Dremitente.append("^FO0,")
                        .append(inicioTexto + 139)
                        .append(",0");

                Dremitente.append("^A0N,20,17");

                Dremitente.append("^FH\\");
                Dremitente.append("^FD")
                        .append(direccion.substring(end).trim());

                Dremitente.append("^FS\n");
            }
        }

        return Dremitente;
    }

    private StringBuilder telefonoDestinatarioOriginal(E_Guia guia) {
        StringBuilder Tdestinatario = new StringBuilder();

        Tdestinatario.append("^FO0,").append(inicioTexto + 145).append(",0");
        Tdestinatario.append("^A0,20,17\n");
        Tdestinatario.append("^FH\\");
        Tdestinatario.append("^FD").append("Teléfono: ").append(guia.getTelefonoRemitente());
        Tdestinatario.append("^FS\n");

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
                Ndestinatario.append("^FO0,").append(inicioTexto + 173).append(",0");
                Ndestinatario.append("^A0N,N,25,25");
                Ndestinatario.append("^FH\\");
                Ndestinatario.append("^FD").append(nombreD);
                Ndestinatario.append("^FS\n");
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
                Ndestinatario.append("^FO0,").append(inicioTexto + 173).append(",0");
                Ndestinatario.append("^A0N,N,25,25");
                Ndestinatario.append("^FH\\");
                Ndestinatario.append("^FD").append(nombreD.substring(start, end).trim());
                Ndestinatario.append("^FS\n");

                start = end;

                // Segunda parte (40 a 80)
                if (longitud > start) {
                    end = start + maxLineLength2;
                    if (end > longitud) {
                        end = longitud;
                    }
                    Ndestinatario.append("^FO0,").append(inicioTexto + 198).append(",0");
                    Ndestinatario.append("^A0N,N,25,25");
                    Ndestinatario.append("^FH\\");
                    Ndestinatario.append("^FD").append(nombreD.substring(start, end).trim());
                    Ndestinatario.append("^FS\n");

                    start = end;
                }
            }

        } else {
            if (longitud <= 45) {
                Ndestinatario.append("^FO0,").append(inicioTexto + 173).append(",0");
                Ndestinatario.append("^A0N,N,25,25");
                Ndestinatario.append("^FH\\");
                Ndestinatario.append("^FD").append(nombreD);
                Ndestinatario.append("^FS\n");
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
                Ndestinatario.append("^FO0,").append(inicioTexto + 173).append(",0");
                Ndestinatario.append("^A0N,N,25,25");
                Ndestinatario.append("^FH\\");
                Ndestinatario.append("^FD").append(nombreD.substring(start, end).trim());
                Ndestinatario.append("^FS\n");

                start = end;

                // Segunda parte 
                if (longitud > start) {
                    end = start + maxLineLength2;
                    if (end > longitud) {
                        end = longitud;
                    }
                    Ndestinatario.append("^FO0,").append(inicioTexto + 198).append(",0");//340
                    Ndestinatario.append("^A0N,N,25,25");
                    Ndestinatario.append("^FH\\");
                    Ndestinatario.append("^FD").append(nombreD.substring(start, end).trim());
                    Ndestinatario.append("^FS\n");

                    start = end;
                }
            }

        }
        return Ndestinatario;
    }

    private StringBuilder direccionDestinatarioFinal(E_Guia guia) {
        StringBuilder Ddestinatario = new StringBuilder();
        StringBuilder direccionD = new StringBuilder();
        String dD = "Dirección final: " + guia.getDireccionDestinatario()+ " " + guia.getMuniDestinatario();
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
                Ddestinatario.append("^FO0,").append(inicioTexto + 233).append(",0");
                Ddestinatario.append("^A0N,N,25,25");
                Ddestinatario.append("^FH\\");
                Ddestinatario.append("^FD").append(direccionD);
                Ddestinatario.append("^FS\n");
            } else {
                int maxLineLength = 40;
                int start = 0;

                // Procesa la primera parte (0 a 40)
                int end = Math.min(start + maxLineLength, longitud);
                Ddestinatario.append("^FO0,").append(inicioTexto + 233).append(",0");
                Ddestinatario.append("^A0N,N,25,25");
                Ddestinatario.append("^FH\\");
                Ddestinatario.append("^FD").append(direccionD.substring(start, end).trim());
                Ddestinatario.append("^FS\n");

                start = end;

                // Procesa la segunda parte (40 a 80)
                if (longitud > start) {
                    end = Math.min(start + maxLineLength, longitud);
                    Ddestinatario.append("^FO0,").append(inicioTexto + 258).append(",0");
                    Ddestinatario.append("^A0N,N,25,25");
                    Ddestinatario.append("^FH\\");
                    Ddestinatario.append("^FD").append(direccionD.substring(start, end).trim());
                    Ddestinatario.append("^FS\n");

                    start = end;
                }

                // Procesa la tercera parte (80 a 120)
                if (longitud > start) {
                    end = Math.min(start + maxLineLength, longitud);
                    Ddestinatario.append("^FO0,").append(inicioTexto + 283).append(",0");//415
                    Ddestinatario.append("^A0N,N,25,25");
                    Ddestinatario.append("^FH\\");
                    Ddestinatario.append("^FD").append(direccionD.substring(start, end).trim());
                    Ddestinatario.append("^FS\n");
                }
            }

            // Si CODvalorCobrar es 0 o menor
        } else {
            if (longitud <= 45) {
                Ddestinatario.append("^FO0,").append(inicioTexto + 233).append(",0");//365
                Ddestinatario.append("^A0N,N,25,25");
                Ddestinatario.append("^FH\\");
                Ddestinatario.append("^FD").append(direccionD);
                Ddestinatario.append("^FS\n");
            } else {
                int maxLineLength = 45;
                int start = 0;

                // Procesa la primera parte (0 a 45)
                int end = Math.min(start + maxLineLength, longitud);
                Ddestinatario.append("^FO0,").append(inicioTexto + 233).append(",0");//365
                Ddestinatario.append("^A0N,N,25,25");
                Ddestinatario.append("^FH\\");
                Ddestinatario.append("^FD").append(direccionD.substring(start, end).trim());
                Ddestinatario.append("^FS\n");

                start = end;

                // Procesa la segunda parte (45 a 90)
                if (longitud > start) {
                    end = Math.min(start + maxLineLength, longitud);
                    Ddestinatario.append("^FO0,").append(inicioTexto + 258).append(",0");//390
                    Ddestinatario.append("^A0N,N,25,25");
                    Ddestinatario.append("^FH\\");
                    Ddestinatario.append("^FD").append(direccionD.substring(start, end).trim());
                    Ddestinatario.append("^FS\n");

                    start = end;
                }

                // Procesa la tercera parte (90 a 135)
                if (longitud > start) {
                    end = Math.min(start + maxLineLength, longitud);
                    Ddestinatario.append("^FO0,").append(inicioTexto + 283).append(",0");//415
                    Ddestinatario.append("^A0N,N,25,25");
                    Ddestinatario.append("^FH\\");
                    Ddestinatario.append("^FD").append(direccionD.substring(start, end).trim());
                    Ddestinatario.append("^FS\n");
                }
            }
        }
        return Ddestinatario;
    }

    private StringBuilder telefonoDestinatarioFinal(E_Guia guia) {
        StringBuilder Tremitente = new StringBuilder();
        Tremitente.append("^FO0,").append(inicioTexto + 318).append(",0");
        Tremitente.append("^A0,N,25,25");
        Tremitente.append("^FH\\");
        Tremitente.append("^FD").append("Teléfono: ").append(guia.getTelefonoDestinatario());
        Tremitente.append("^FS\n");
        return Tremitente;
    }

    private StringBuilder noGuiaImagen(E_Guia guia, String guiaImprimir) {
        StringBuilder noGuiaIMG = new StringBuilder();
        noGuiaIMG.append("^FO10,").append(inicioTexto + 352).append(",0");
        noGuiaIMG.append("^AA,N,25,10");
        if (guia.getGuiaMadre().equals("S")) {
            noGuiaIMG.append("^FD").append(guia.getNumeroGuia());
        } else if (guia.getGuiaMadre().equals("N")) {
            noGuiaIMG.append("^FD").append(guiaImprimir);
        }
        noGuiaIMG.append("^FS\n");
        noGuiaIMG.append("^FO340,").append(inicioTexto + 190).append(",0");
        noGuiaIMG.append("^IME:IMG.GRF,1,1");
        noGuiaIMG.append("^FS\n");
        return noGuiaIMG;
    }

    private StringBuilder puntoDestinoFinalDEV(E_Guia guia) {

        StringBuilder ptoDes = new StringBuilder();

        ptoDes.append("^FO160,565");
        ptoDes.append("^GB245,10,130");
        ptoDes.append("^FS\n");

        ptoDes.append("^LRY");
        ptoDes.append("^FO180,580,0");
        ptoDes.append("^A0,N,125,70");
        ptoDes.append("^FDDEV");
        ptoDes.append("^FS\n");

        ptoDes.append("^FO85,710,0");
        ptoDes.append("^A0,N,225,70");
        ptoDes.append("^FD").append(guia.getPuntoDestino());

        ptoDes.append("^FS\n");

        ptoDes.append("^FS\n");

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
            if (longitud > 41 && longitud < 98) {//if (longitud > 51 && longitud < 108) {
                // Buscar el último espacio antes de cortar en 51 caracteres
                int end1 = info.lastIndexOf(" ", 41);
                if (end1 == -1) {
                    end1 = 41; // Si no hay espacio, corta en 51 caracteres
                }
                // Primera parte (0 a 51)
                infoGuia.append("^FO0,").append(inicioTexto + 770).append(",0");
                infoGuia.append("^A0,N,23,23");
                infoGuia.append("^FD").append(info.substring(0, end1).trim());
                infoGuia.append("^FS\n");

                // Segunda parte (desde end1 hasta el final)
                if (longitud > end1) {
                    int end2 = info.lastIndexOf(" ", longitud); // Se asegura de no cortar la última palabra
                    if (end2 == -1) {
                        end2 = longitud;
                    }
                    infoGuia.append("^FO0,").append(inicioTexto + 800).append(",0");
                    infoGuia.append("^A0,N,23,23");
                    infoGuia.append("^FD").append(info.substring(end1).trim());
                    infoGuia.append("^FS\n");
                }

            } else if (longitud > 97) {//107
                // Buscar el último espacio antes de cortar en 51 caracteres
                int end1 = info.lastIndexOf(" ", 51);
                if (end1 == -1) {
                    end1 = 51; // Si no hay espacio, corta en 51 caracteres
                }
                // Primera parte (0 a 51)
                infoGuia.append("^FO0,").append(inicioTexto + 770).append(",0");
                infoGuia.append("^A0,N,23,23");
                infoGuia.append("^FD").append(info.substring(0, end1).trim());
                infoGuia.append("^FS\n");

                // Buscar el último espacio antes de cortar en 103 caracteres
                int end2 = info.lastIndexOf(" ", 90);//100
                if (end2 == -1) {
                    end2 = 90; // Si no hay espacio, corta en 103 caracteres
                }
                // Segunda parte (51 a 103)
                if (longitud > end1) {
                    infoGuia.append("^FO0,").append(inicioTexto + 800).append(",0");
                    infoGuia.append("^A0,N,23,23");
                    infoGuia.append("^FD").append(info.substring(end1, end2).trim());
                    infoGuia.append("^FS\n");
                }

                // Tercera parte (103 hasta el final)
                if (longitud > end2) {
                    int end3 = info.lastIndexOf(" ", longitud); // Se asegura de no cortar la última palabra
                    if (end3 == -1) {
                        end3 = longitud;
                    }

                    infoGuia.append("^FO0,").append(inicioTexto + 830).append(",0");
                    infoGuia.append("^A0,N,23,23");
                    infoGuia.append("^FD").append(info.substring(end2).trim());
                    infoGuia.append("^FS\n");
                }

            } else if (longitud <= 41) {
                infoGuia.append("^FO0,").append(inicioTexto + 770).append(",0");
                infoGuia.append("^A0,N,23,23");
                infoGuia.append("^FD").append(info);
                infoGuia.append("^FS\n");
            }

        } else {

            if (longitud > 51 && longitud < 108) {
                // Buscar el último espacio antes de cortar en 51 caracteres
                int end1 = info.lastIndexOf(" ", 51);
                if (end1 == -1) {
                    end1 = 51; // Si no hay espacio, corta en 51 caracteres
                }
                infoGuia.append("^FO0,").append(inicioTexto + 770).append(",0");
                infoGuia.append("^A0,N,24,24");
                infoGuia.append("^FD").append(info.substring(0, end1).trim());
                infoGuia.append("^FS\n");

                // Segunda parte desde el final de la primera hasta el final
                if (longitud > end1) {
                    infoGuia.append("^FO0,").append(inicioTexto + 800).append(",0");
                    infoGuia.append("^A0,N,24,24");
                    infoGuia.append("^FD").append(info.substring(end1).trim());
                    infoGuia.append("^FS\n");
                }

            } else if (longitud > 107) {
                // Buscar el último espacio antes de cortar en 54 caracteres
                int end1 = info.lastIndexOf(" ", 54);
                if (end1 == -1) {
                    end1 = 54; // Si no hay espacio, corta en 54 caracteres
                }
                infoGuia.append("^FO0,").append(inicioTexto + 770).append(",0");
                infoGuia.append("^A0,N,24,24");
                infoGuia.append("^FD").append(info.substring(0, end1).trim());
                infoGuia.append("^FS\n");

                // Buscar el último espacio antes de cortar en 110 caracteres
                int end2 = info.lastIndexOf(" ", 110);
                if (end2 == -1) {
                    end2 = 110; // Si no hay espacio, corta en 110 caracteres
                }
                if (longitud > end1) {
                    infoGuia.append("^FO0,").append(inicioTexto + 800).append(",0");
                    infoGuia.append("^A0,N,24,24");
                    infoGuia.append("^FD").append(info.substring(end1, end2).trim());
                    infoGuia.append("^FS\n");
                }

                // Tercera parte desde el final de la segunda hasta el final
                if (longitud > end2) {
                    infoGuia.append("^FO0,").append(inicioTexto + 830).append(",0");
                    infoGuia.append("^A0,N,24,24");
                    infoGuia.append("^FD").append(info.substring(end2).trim());
                    infoGuia.append("^FS\n");
                }

            } else if (longitud <= 51) {
                infoGuia.append("^FO0,").append(inicioTexto + 770).append(",0");
                infoGuia.append("^A0,N,24,24");
                infoGuia.append("^FD").append(info);
                infoGuia.append("^FS\n");
            }
        }
        return infoGuia;
    }

    private StringBuilder QRpiezas(E_Guia guia, int contadorPiezas) {
        StringBuilder QRPiezas = new StringBuilder();
        //Rectángulo
        QRPiezas.append("^FO0,").append(inicioTexto + (860)).append(",0");//inicio rectángulo
        QRPiezas.append("^GB435,32,30");//ancho, altura, grosor, color (0=negro 1=blanco), redondeo en las esquinas (0=recto)
        QRPiezas.append("^FS\n");//finaliza rectángulo
        //Tracking - Términos y Condiciones
        QRPiezas.append("^FO10,").append(inicioTexto + (865)).append(",0");//x, y, 0
        QRPiezas.append("^A0,N,22,22");//Tamaño de la letra
        QRPiezas.append("^FH\\");
        QRPiezas.append("^FDTracking | Condiciones Generales de Servicio\n");
        QRPiezas.append("^FS\n");//finaliza texto
        //QR - > redirecciona https://servicios.guatex.gt/Guatex/rastreoTracking?tipo=G&dato=noguia
        QRPiezas.append("^FO0,").append(inicioTexto + (900)).append(",0");
        QRPiezas.append("^BQ,2,5");
        QRPiezas.append("^FDQA,").append("https://servicios.guatex.gt/Guatex/rastreoTracking?tipo=G&dato="+ guia.getNumeroGuia());
        QRPiezas.append("^FS\n");
        if (Integer.valueOf((guia.getPiezas())) > 99) {
            QRPiezas.append("^FO200,").append(inicioTexto + 940).append(",0");
            QRPiezas.append("^A0,N,100,70");
            QRPiezas.append("^FD").append(contadorPiezas).append("/").append(guia.getPiezas());
            QRPiezas.append("^FS\n");
        } else {
            QRPiezas.append("^FO230,").append(inicioTexto + 940).append(",0");
            QRPiezas.append("^A0,N,150,70");
            QRPiezas.append("^FD").append(contadorPiezas).append("/").append(guia.getPiezas());
            QRPiezas.append("^FS\n");
        }
        return QRPiezas;
    }

}

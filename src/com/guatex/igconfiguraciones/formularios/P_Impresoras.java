package com.guatex.igconfiguraciones.formularios;

import com.guatex.igconfiguraciones.archivos.A_Impresoras;
import com.guatex.igconfiguraciones.entidades.E_Impresora;
import com.guatex.igconfiguraciones.entidades.E_Servicio;
import com.guatex.igconfiguraciones.loggs.ArchivoLogs;
import com.guatex.igconfiguraciones.principal.GTXConfiguracionIG;
import java.util.Collections;
import javax.swing.AbstractButton;
import javax.swing.ButtonGroup;
import javax.swing.DefaultListModel;
import javax.swing.JOptionPane;
import javax.swing.SwingConstants;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;

/**
 *
 * @author ESTEFANIECM
 */
public final class P_Impresoras extends javax.swing.JPanel {

    /**
     * Creates new form P_Impresoras
     */
    public P_Impresoras() {
        initComponents();
        iniciarPanel();
    }

    DefaultListModel modelo;
    DefaultTableCellRenderer tcr = new DefaultTableCellRenderer();
    A_Impresoras opciones = new A_Impresoras();

    private void iniciarPanel() {
        ((DefaultTableCellRenderer) this.tbImpresoras.getTableHeader().getDefaultRenderer())
                .setHorizontalAlignment(SwingConstants.CENTER);

        this.tbImpresoras.getModel().addTableModelListener(new TableModelListener() {
            @Override
            public void tableChanged(TableModelEvent e) {
                if (e.getColumn() == 3) {
                    validarSeleccionEliminar();
                }
            }
        });

        lblDestino.setVisible(false);
        txtDestinoImpresora.setVisible(false);

        cargarTablaImpresoras();
    }

    private void cargarTablaImpresoras() {
        DefaultTableModel modeloTabla = (DefaultTableModel) tbImpresoras.getModel();
        modeloTabla.setRowCount(0);
        tcr.setHorizontalAlignment(SwingConstants.CENTER);
        for (E_Impresora impresora : GTXConfiguracionIG.ListaImpresoras) {
            modeloTabla.addRow(new Object[]{impresora.getNombre(), impresora.getTipo(), impresora.getDestino(), false, impresora.getIdImpresora()});
        }
        for (int i = 0; i < tbImpresoras.getColumnCount(); i++) {
            tbImpresoras.getColumnModel().getColumn(i).setCellRenderer(tcr);
        }
        tbImpresoras.getColumnModel().getColumn(3).setCellRenderer(tbImpresoras.getDefaultRenderer(Boolean.class));
        tbImpresoras.getColumnModel().getColumn(3).setCellEditor(tbImpresoras.getDefaultEditor(Boolean.class));

        tbImpresoras.getColumnModel().getColumn(4).setMinWidth(0);
        tbImpresoras.getColumnModel().getColumn(4).setMaxWidth(0);
        tbImpresoras.getColumnModel().getColumn(4).setWidth(0);
    }

    private void validarSeleccionEliminar() {
        DefaultTableModel modeloTabla = (DefaultTableModel) tbImpresoras.getModel();
        int filas = modeloTabla.getRowCount();
        int impresoraEliminar = 0;
        // Buscar casilla seleccionada seleccionadas
        for (int i = 0; i < filas; i++) {
            Boolean isSelected = (Boolean) modeloTabla.getValueAt(i, 3);
            if (isSelected != null && isSelected) {
                impresoraEliminar = i;
            }
        }
        String nombreImpresora = (String) modeloTabla.getValueAt(impresoraEliminar, 0);
        String idImpresora = (String) modeloTabla.getValueAt(impresoraEliminar, 4);

        if (!existeImpresoraEnServicios(idImpresora)) {
            int respuesta = JOptionPane.showConfirmDialog(null,
                    "¿Está seguro de eliminar la impresora '" + nombreImpresora + "'?",
                    "Confirmar eliminación", JOptionPane.YES_NO_OPTION);
            if (respuesta == JOptionPane.YES_OPTION) {
                GTXConfiguracionIG.ListaImpresoras.remove(impresoraEliminar);
                boolean actualizaArchivo = new A_Impresoras().guardarImpresorasArchivo();
                if (actualizaArchivo) {
                    ArchivoLogs.getInstance().grabaLogFileAdministrador("------ La impresora [" + nombreImpresora + "] fue eliminada.", false);
                    opciones.realizarBKarchivo();
                    cargarTablaImpresoras();
                }
            } else {
                cargarTablaImpresoras();
            }
        } else {
            int respuesta = JOptionPane.showConfirmDialog(null,
                    "No es posible eliminar la impresora '" + nombreImpresora + "' porque tiene servicios asociados. \nConsulte 'Mantenimiento de Servicios'.", "Alerta", JOptionPane.DEFAULT_OPTION,
                    JOptionPane.WARNING_MESSAGE);
            if (respuesta == JOptionPane.OK_OPTION || respuesta == JOptionPane.CLOSED_OPTION) {
                cargarTablaImpresoras();
            }
        }
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        grupoConexion = new javax.swing.ButtonGroup();
        jPanel1 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        jSeparator1 = new javax.swing.JSeparator();
        jLabel2 = new javax.swing.JLabel();
        txtNombreImpresora = new javax.swing.JTextField();
        lblDestino = new javax.swing.JLabel();
        btnMIGuardar = new javax.swing.JButton();
        jSeparator2 = new javax.swing.JSeparator();
        jLabel4 = new javax.swing.JLabel();
        jScrollPane1 = new javax.swing.JScrollPane();
        tbImpresoras = new javax.swing.JTable();
        txtDestinoImpresora = new javax.swing.JTextField();
        jLabel5 = new javax.swing.JLabel();
        btnRed = new javax.swing.JRadioButton();
        btnUSB = new javax.swing.JRadioButton();

        setBackground(new java.awt.Color(238, 238, 238));
        setMinimumSize(new java.awt.Dimension(510, 525));
        setPreferredSize(new java.awt.Dimension(510, 525));

        jPanel1.setBackground(new java.awt.Color(238, 238, 238));
        jPanel1.setMinimumSize(new java.awt.Dimension(500, 498));

        jLabel1.setBackground(new java.awt.Color(238, 238, 238));
        jLabel1.setFont(new java.awt.Font("Verdana", 1, 16)); // NOI18N
        jLabel1.setForeground(new java.awt.Color(51, 51, 51));
        jLabel1.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/guatex/igconfiguraciones/imagenes/addprinter.png"))); // NOI18N
        jLabel1.setText("Agregar impresora  ");
        jLabel1.setHorizontalTextPosition(javax.swing.SwingConstants.LEFT);
        jLabel1.setVerticalTextPosition(javax.swing.SwingConstants.TOP);

        jLabel2.setBackground(new java.awt.Color(238, 238, 238));
        jLabel2.setFont(new java.awt.Font("Verdana", 1, 12)); // NOI18N
        jLabel2.setForeground(new java.awt.Color(51, 51, 51));
        jLabel2.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        jLabel2.setText("Descripción impresora:");
        jLabel2.setHorizontalTextPosition(javax.swing.SwingConstants.LEFT);
        jLabel2.setVerticalTextPosition(javax.swing.SwingConstants.TOP);

        txtNombreImpresora.setFont(new java.awt.Font("Verdana", 0, 12)); // NOI18N

        lblDestino.setBackground(new java.awt.Color(238, 238, 238));
        lblDestino.setFont(new java.awt.Font("Verdana", 1, 12)); // NOI18N
        lblDestino.setForeground(new java.awt.Color(51, 51, 51));
        lblDestino.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        lblDestino.setText("Dirección IP:");
        lblDestino.setHorizontalTextPosition(javax.swing.SwingConstants.LEFT);
        lblDestino.setVerticalTextPosition(javax.swing.SwingConstants.TOP);

        btnMIGuardar.setBackground(new java.awt.Color(255, 255, 255));
        btnMIGuardar.setFont(new java.awt.Font("Verdana", 1, 12)); // NOI18N
        btnMIGuardar.setForeground(new java.awt.Color(51, 51, 51));
        btnMIGuardar.setText("Guardar");
        btnMIGuardar.setBorder(new javax.swing.border.SoftBevelBorder(javax.swing.border.BevelBorder.RAISED));
        btnMIGuardar.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btnMIGuardar.setDefaultCapable(false);
        btnMIGuardar.setFocusPainted(false);
        btnMIGuardar.setFocusable(false);
        btnMIGuardar.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        btnMIGuardar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnMIGuardarActionPerformed(evt);
            }
        });

        jLabel4.setBackground(new java.awt.Color(238, 238, 238));
        jLabel4.setFont(new java.awt.Font("Verdana", 1, 16)); // NOI18N
        jLabel4.setForeground(new java.awt.Color(51, 51, 51));
        jLabel4.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel4.setText("Impresoras registradas:");
        jLabel4.setHorizontalTextPosition(javax.swing.SwingConstants.LEFT);
        jLabel4.setVerticalTextPosition(javax.swing.SwingConstants.TOP);

        tbImpresoras.setFont(new java.awt.Font("Verdana", 0, 11)); // NOI18N
        tbImpresoras.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Nombre impresora", "Tipo", "Destino impresión", "Eliminar impresora", "id"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.Boolean.class, java.lang.String.class
            };
            boolean[] canEdit = new boolean [] {
                false, false, false, true, false
            };

            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        tbImpresoras.setAutoResizeMode(javax.swing.JTable.AUTO_RESIZE_LAST_COLUMN);
        tbImpresoras.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        tbImpresoras.setRowHeight(30);
        tbImpresoras.setSelectionBackground(new java.awt.Color(204, 204, 204));
        tbImpresoras.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tbImpresorasMouseClicked(evt);
            }
        });
        jScrollPane1.setViewportView(tbImpresoras);
        if (tbImpresoras.getColumnModel().getColumnCount() > 0) {
            tbImpresoras.getColumnModel().getColumn(0).setResizable(false);
            tbImpresoras.getColumnModel().getColumn(1).setResizable(false);
            tbImpresoras.getColumnModel().getColumn(2).setResizable(false);
            tbImpresoras.getColumnModel().getColumn(3).setResizable(false);
            tbImpresoras.getColumnModel().getColumn(4).setMinWidth(0);
            tbImpresoras.getColumnModel().getColumn(4).setPreferredWidth(0);
            tbImpresoras.getColumnModel().getColumn(4).setMaxWidth(0);
        }

        txtDestinoImpresora.setFont(new java.awt.Font("Verdana", 0, 12)); // NOI18N

        jLabel5.setBackground(new java.awt.Color(238, 238, 238));
        jLabel5.setFont(new java.awt.Font("Verdana", 1, 12)); // NOI18N
        jLabel5.setForeground(new java.awt.Color(51, 51, 51));
        jLabel5.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        jLabel5.setText("Tipo de conexión:");
        jLabel5.setHorizontalTextPosition(javax.swing.SwingConstants.LEFT);
        jLabel5.setVerticalTextPosition(javax.swing.SwingConstants.TOP);

        grupoConexion.add(btnRed);
        btnRed.setFont(new java.awt.Font("Verdana", 0, 12)); // NOI18N
        btnRed.setText("Red");
        btnRed.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btnRed.setFocusPainted(false);
        btnRed.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnRedActionPerformed(evt);
            }
        });

        grupoConexion.add(btnUSB);
        btnUSB.setFont(new java.awt.Font("Verdana", 0, 12)); // NOI18N
        btnUSB.setText("USB");
        btnUSB.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btnUSB.setFocusPainted(false);
        btnUSB.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnUSBActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGap(19, 19, 19)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                            .addComponent(jLabel5, javax.swing.GroupLayout.DEFAULT_SIZE, 168, Short.MAX_VALUE)
                            .addComponent(lblDestino, javax.swing.GroupLayout.DEFAULT_SIZE, 168, Short.MAX_VALUE)
                            .addComponent(jLabel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addGap(4, 4, 4)
                                .addComponent(txtNombreImpresora))
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(btnRed, javax.swing.GroupLayout.PREFERRED_SIZE, 70, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(btnUSB, javax.swing.GroupLayout.PREFERRED_SIZE, 70, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(0, 0, Short.MAX_VALUE))
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(txtDestinoImpresora))))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                        .addGap(0, 0, Short.MAX_VALUE)
                        .addComponent(jSeparator2, javax.swing.GroupLayout.PREFERRED_SIZE, 490, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(btnMIGuardar, javax.swing.GroupLayout.PREFERRED_SIZE, 123, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jSeparator1, javax.swing.GroupLayout.PREFERRED_SIZE, 490, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addGap(10, 10, 10)
                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jLabel1)
                                    .addComponent(jLabel4)
                                    .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 470, javax.swing.GroupLayout.PREFERRED_SIZE))))
                        .addGap(0, 0, Short.MAX_VALUE)))
                .addContainerGap())
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addComponent(jLabel1)
                .addGap(6, 6, 6)
                .addComponent(jSeparator1, javax.swing.GroupLayout.PREFERRED_SIZE, 10, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(10, 10, 10)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel2, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txtNombreImpresora, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel5, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(btnRed, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(btnUSB, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(lblDestino, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txtDestinoImpresora, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addComponent(btnMIGuardar, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(jSeparator2, javax.swing.GroupLayout.PREFERRED_SIZE, 10, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(jLabel4)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 205, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(15, 15, 15))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, 490, Short.MAX_VALUE)
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );
    }// </editor-fold>//GEN-END:initComponents

    private void btnMIGuardarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnMIGuardarActionPerformed
        String id = E_Impresora.generaIDimpresora();
        String nombre = txtNombreImpresora.getText().trim();
        String destino = txtDestinoImpresora.getText().trim();

        String tipo = getTipoConexion(grupoConexion);
        if (nombre.isEmpty() || destino.isEmpty()) {
            JOptionPane.showMessageDialog(this,
                    "Debe ingresar el nombre descriptivo y "
                    + (tipo.equals("Red") ? "la dirección IP." : "el nombre exacto de la impresora USB."),
                    "Alerta", JOptionPane.ERROR_MESSAGE);
            return;
        }

        if ("Red".equals(tipo) && !esIPvalida(destino)) {
            JOptionPane.showMessageDialog(this,
                    "La dirección IP ingresada no tiene un formato válido.",
                    "Alerta", JOptionPane.ERROR_MESSAGE);
            return;
        }

        E_Impresora impresora = new E_Impresora();
        impresora.setIdImpresora(id);
        impresora.setNombre(nombre);
        impresora.setTipo(tipo);
        impresora.setDestino(destino);

        boolean agregado = AgregaImpresora(impresora);
        if (agregado) {
            boolean actualizado = opciones.guardarImpresorasArchivo();
            if (actualizado) {
                ArchivoLogs.getInstance().grabaLogFileAdministrador(
                        "------ La impresora [" + impresora.getNombre()
                        + "] tipo [" + tipo + "] fue agregada.", false);
                JOptionPane.showMessageDialog(this,
                        "Impresora agregada exitosamente",
                        "Impresoras", JOptionPane.INFORMATION_MESSAGE);
                opciones.realizarBKarchivo();
                limpiarCampos();
            }
        } else {
            JOptionPane.showMessageDialog(this,
                    "La impresora con destino '" + destino + "' ya existe.",
                    "Impresoras", JOptionPane.ERROR_MESSAGE);
        }

    }//GEN-LAST:event_btnMIGuardarActionPerformed

    private boolean esIPvalida(String ip) {
        try {
            String[] partes = ip.split("\\.");
            if (partes.length != 4) {
                return false;
            }
            for (String p : partes) {
                int val = Integer.parseInt(p);
                if (val < 0 || val > 255) {
                    return false;
                }
            }
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    private void tbImpresorasMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tbImpresorasMouseClicked

    }//GEN-LAST:event_tbImpresorasMouseClicked

    private void btnRedActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnRedActionPerformed
        // TODO add your handling code here:
        lblDestino.setText("Dirección IP:");
        lblDestino.setVisible(true);
        txtDestinoImpresora.setText("");
        txtDestinoImpresora.setToolTipText("Ej: 192.168.1.30");
        txtDestinoImpresora.setVisible(true);
    }//GEN-LAST:event_btnRedActionPerformed

    private void btnUSBActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnUSBActionPerformed
        lblDestino.setText("Nombre impresora:");
        lblDestino.setVisible(true);
        txtDestinoImpresora.setText("");
        txtDestinoImpresora.setToolTipText("Nombre exacto de la impresora instalada en el sistema");
        txtDestinoImpresora.setVisible(true);
    }//GEN-LAST:event_btnUSBActionPerformed

    private String getTipoConexion(ButtonGroup buttonGroup) {
        for (AbstractButton boton : Collections.list(buttonGroup.getElements())) {
            if (boton.isSelected()) {
                return boton.getText().equalsIgnoreCase("Red") ? "IP" : "USB";
            }
        }
        return null;
    }

    private void limpiarCampos() {
        txtNombreImpresora.setText("");
        txtDestinoImpresora.setText("");
        txtDestinoImpresora.setVisible(false);
        grupoConexion.clearSelection();
        lblDestino.setText("");
        lblDestino.setVisible(false);
        txtDestinoImpresora.setToolTipText("");
        cargarTablaImpresoras();
    }

    private boolean AgregaImpresora(E_Impresora nueva) {
        for (E_Impresora existente : GTXConfiguracionIG.ListaImpresoras) {
            if (existente.getDestino().equalsIgnoreCase(nueva.getDestino())
                    && existente.getTipo().equalsIgnoreCase(nueva.getTipo())) {
                return false;
            }
        }
        GTXConfiguracionIG.ListaImpresoras.add(nueva);
        return true;
    }

    private boolean existeImpresoraEnServicios(String idImpresora) {
        for (E_Servicio s : GTXConfiguracionIG.ListaServicios) {
            if (s.getIdImpresora().equals(idImpresora)) {
                return true;
            }
        }
        return false;
    }

    private boolean VentanaVisible() {
        return super.isVisible();
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnMIGuardar;
    private javax.swing.JRadioButton btnRed;
    private javax.swing.JRadioButton btnUSB;
    private javax.swing.ButtonGroup grupoConexion;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JSeparator jSeparator1;
    private javax.swing.JSeparator jSeparator2;
    private javax.swing.JLabel lblDestino;
    private javax.swing.JTable tbImpresoras;
    private javax.swing.JTextField txtDestinoImpresora;
    private javax.swing.JTextField txtNombreImpresora;
    // End of variables declaration//GEN-END:variables
}

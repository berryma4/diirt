/*
 * Copyright 2008-2010 Brookhaven National Laboratory
 * All rights reserved. Use is subject to license terms.
 */

/*
 * MockWaterfallPlot.java
 *
 * Created on Jan 10, 2011, 3:59:13 PM
 */

package org.epics.pvmanager.test;

import org.epics.pvmanager.CompositeDataSource;
import org.epics.pvmanager.jca.JCADataSource;
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferByte;
import javax.swing.ImageIcon;
import org.epics.pvmanager.PV;
import org.epics.pvmanager.PVManager;
import org.epics.pvmanager.PVValueChangeListener;
import org.epics.pvmanager.ThreadSwitch;
import org.epics.pvmanager.data.Util;
import org.epics.pvmanager.data.VImage;
import org.epics.pvmanager.sim.SimulationDataSource;
import static org.epics.pvmanager.data.ExpressionLanguage.*;
import static org.epics.pvmanager.extra.ExpressionLanguage.*;

/**
 *
 * @author carcassi
 */
public class MockWaterfallPlot extends javax.swing.JFrame {

    /** Creates new form MockWaterfallPlot */
    public MockWaterfallPlot() {
        PVManager.setDefaultThread(ThreadSwitch.onSwingEDT());
        CompositeDataSource dataSource = new CompositeDataSource();
        dataSource.putDataSource("sim", SimulationDataSource.simulatedData());
        dataSource.putDataSource("epics", new JCADataSource());
        dataSource.setDefaultDataSource("sim");
        PVManager.setDefaultDataSource(dataSource);
        initComponents();
    }

    private PV<VImage> pv;

    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        plotLabel = new javax.swing.JLabel();
        jLabel1 = new javax.swing.JLabel();
        pvName = new javax.swing.JTextField();
        lastError = new javax.swing.JTextField();
        jSeparator1 = new javax.swing.JSeparator();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        plotLabel.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);

        jLabel1.setText("PV Name:");

        pvName.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                pvNameActionPerformed(evt);
            }
        });

        lastError.setEditable(false);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel1)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(pvName, javax.swing.GroupLayout.DEFAULT_SIZE, 342, Short.MAX_VALUE)
                .addContainerGap())
            .addComponent(jSeparator1, javax.swing.GroupLayout.DEFAULT_SIZE, 435, Short.MAX_VALUE)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(plotLabel, javax.swing.GroupLayout.DEFAULT_SIZE, 411, Short.MAX_VALUE)
                .addContainerGap())
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(lastError, javax.swing.GroupLayout.DEFAULT_SIZE, 411, Short.MAX_VALUE)
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel1)
                    .addComponent(pvName, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jSeparator1, javax.swing.GroupLayout.PREFERRED_SIZE, 10, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(plotLabel, javax.swing.GroupLayout.DEFAULT_SIZE, 220, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(lastError, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void pvNameActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_pvNameActionPerformed
        if (pv != null)
            pv.close();

        pv = PVManager.read(waterfallPlotOf(vDoubleArray(pvName.getText()))).andNotify(ThreadSwitch.onSwingEDT())
                .atHz(20);
        pv.addPVValueChangeListener(new PVValueChangeListener() {

            @Override
            public void pvValueChanged() {
                setLastError(pv.lastException());
                if (pv.getValue() != null) {
                    BufferedImage image = Util.toImage(pv.getValue());
                    plotLabel.setIcon(new ImageIcon(image));
                }
            }
        });
    }//GEN-LAST:event_pvNameActionPerformed


    private void setLastError(Exception ex) {
        if (ex != null)
            lastError.setText(ex.getMessage());
        else
            lastError.setText("");
    }


    final BufferedImage finalBuffer = new BufferedImage(100, 100, BufferedImage.TYPE_3BYTE_BGR);

    /**
    * @param args the command line arguments
    */
    public static void main(String args[]) {
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new MockWaterfallPlot().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLabel jLabel1;
    private javax.swing.JSeparator jSeparator1;
    private javax.swing.JTextField lastError;
    private javax.swing.JLabel plotLabel;
    private javax.swing.JTextField pvName;
    // End of variables declaration//GEN-END:variables

}

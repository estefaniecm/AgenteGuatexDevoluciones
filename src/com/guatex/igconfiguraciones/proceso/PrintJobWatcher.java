package com.guatex.igconfiguraciones.proceso;

import javax.print.DocPrintJob;
import javax.print.event.PrintJobAdapter;
import javax.print.event.PrintJobEvent;


/**
 *
 * @author PJUNIOR-1
 */
public class PrintJobWatcher {
    private boolean done = false;

    public PrintJobWatcher(DocPrintJob job) {
        job.addPrintJobListener(new PrintJobAdapter() {
            public void printJobCanceled(PrintJobEvent pje) {
                allDone();
            }
            public void printJobCompleted(PrintJobEvent pje) {
                allDone();
            }
            public void printJobFailed(PrintJobEvent pje) {
                allDone();
            }
            public void printJobNoMoreEvents(PrintJobEvent pje) {
                allDone();
            }
            void allDone() {
                synchronized (PrintJobWatcher.this) {
                    done = true;
                    PrintJobWatcher.this.notify();
                }
            }
        });
    }

    public synchronized void waitForDone() {
        try {
            while (!done) {
                wait();
            }
        } catch (InterruptedException e) {
            // manejar la excepción de interrupción si es necesario
            System.err.println("El hilo fue interrumpido: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
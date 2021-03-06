/*
    Jabber client.
    Copyright (C) 2010  Florencio Cañizal Calles

    This program is free software: you can redistribute it and/or modify
    it under the terms of the GNU General Public License as published by
    the Free Software Foundation, either version 3 of the License, or
    (at your option) any later version.

    This program is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
    GNU General Public License for more details.

    You should have received a copy of the GNU General Public License
    along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package org.jajim.interfaz.utilidades;

import org.jajim.controladores.TransferenciaFicherosControlador;
import org.jajim.interfaz.ventanas.VentanaGestorDeTransferencias;
import javax.swing.JProgressBar;
import javax.swing.SwingUtilities;
import javax.swing.SwingWorker;

/**
 * @author Florencio Cañizal Calles
 * @version 1.0.1
 * Hilo que actualiza la barra de tarea a medida que avanza una transferencia.
 */
public class BarraProgresoSwingWorker extends SwingWorker{

    private VentanaGestorDeTransferencias vgt;
    private JProgressBar barra;
    private TransferenciaFicherosControlador tfc;
    private String idTransferencia;
    private int progreso;
    private int tipo;
    private boolean cancelada;

    /**
     * Constructor de la clase. Inicializa las variables necesarias.
     * @param barra La barra de progreso que se debe actualizar.
     */
    public BarraProgresoSwingWorker(VentanaGestorDeTransferencias vgt,JProgressBar barra,TransferenciaFicherosControlador tfc,String idTransferencia,int tipo){
        super();
        this.vgt = vgt;
        this.barra = barra;
        this.tfc = tfc;
        this.idTransferencia = idTransferencia;
        this.tipo = tipo;
        this.cancelada = false;
    }

    /**
     * Método de la interfaz SwingWorker, se ejecuta en un hilo en segunod plano.
     * Actualiza le progerso de la barra de descarga.
     * @return No devuelve nada
     * @throws java.lang.Exception Si se produce en error durante el procesmiento.
     */
    @Override
    protected Object doInBackground() throws Exception {

        progreso = 0;

        // Bucle que actualiza la barra
        do{
            // Solicitar el progreso de la transferencia
            double p = tfc.getProgresoTransferencia(idTransferencia);
            
            // Si se retorna -1 es que se ha rechazado la transferencia.
            if(p == -1){
                this.setProgress(100);
                cancelada = true;
                break;
            }

            p *= 100;
            progreso = (int) p;
            SwingUtilities.invokeLater(new Runnable(){
                public void run(){
                    barra.setValue(progreso);
                }
            });
            this.setProgress(progreso);
            Thread.sleep(1000);
        }while(progreso != 100 && !this.isCancelled());

        return new Object();
    }

    /**
     * Método de la interfaz SwingWorker, se ejecuta cuando se acaba de realizar
     * la tarea. Actualiza la interfaz con el nuevo fichero recibido y cierrar la
     * transferencia.
     */
    @Override
    protected void done(){

        if(!this.isCancelled()){
            // Cerrar la transferencia
            String[] datos = null;
            if(!cancelada)
                datos = tfc.cerrarTransferencia(idTransferencia);

            // Finalizar la misma a nivel de interfaz
            vgt.finalizarTransferencia(idTransferencia,tipo,datos);
        }
    }

    /**
     * Devuelve el valor del campo idTransferencias.
     * @return El valor del campo idTansferencia.
     */
    public String getIdTransferencia(){
        return idTransferencia;
    }
}

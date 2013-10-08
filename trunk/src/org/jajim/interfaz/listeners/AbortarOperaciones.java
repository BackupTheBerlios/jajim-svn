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

package org.jajim.interfaz.listeners;

import java.util.ResourceBundle;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import org.jajim.controladores.ConexionControlador;
import org.jajim.interfaz.ventanas.VentanaConversacion;
import org.jajim.interfaz.ventanas.VentanaGestorDeTransferencias;
import org.jajim.interfaz.ventanas.VentanaPrincipal;
import org.jajim.main.Main;

/**
 * @author Florencio Cañizal Calles
 * @version 1.2
 * Clase que se encarga de abortar la conexión, las conversaciones y las transfe
 * rencias.
 */
public class AbortarOperaciones implements Runnable{

    private ResourceBundle texto = ResourceBundle.getBundle("resources.Idioma",Main.loc);

    private JFrame principal;
    private VentanaGestorDeTransferencias vgt;

    /**
     * Constructor de la clase. Inicializa las variables adecuadas.+
     * @param principal Ventana principal en este instante.
     */
    public AbortarOperaciones(JFrame principal, VentanaGestorDeTransferencias vgt){
        this.principal = principal;
        this.vgt = vgt;
    }

    /**
     * Cancela la conexión en curso, si es que había alguna activa.
     * @return Un valor booleano que indica si se realizó la operación o se abor
     * tó.
     */
    public boolean abortarConexion(){

        // Si el usuario está conectado mostrar mensaje para abortar la conexión
        ConexionControlador cnc = ConexionControlador.getInstancia();

        if(cnc.isConectado()){
            int resultado = JOptionPane.showConfirmDialog(principal,texto.getString("abortar_conexion_dialogo_principal"),texto.getString("abortar_conexion_dialogo_title"),JOptionPane.YES_NO_OPTION);
            
            // Si se selecciona no se aborta la operación. Si se selecciona si se
            // procede a abortar la conexión para poder realizar la operación.
            if(resultado == JOptionPane.NO_OPTION) {
                return false;
            }
            else if(resultado == JOptionPane.YES_OPTION){

                // Abortar conversaciones
                this.abortarConversaciones();
                this.abortarTransferencias();

                // Abortar la conexión
                Thread hilo = new Thread(this);
                hilo.start();
                VentanaPrincipal.getInstancia().conexionCancelada();

                return true;
            }
            else {
                return false;
            }
        }

        return true;
    }

    /**
     * Cancela todas las conversaciones activas.
     */
    public void abortarConversaciones(){

        // Comprobar si ha conversaciones.
        if(VentanaConversacion.hayConversaciones()){

            // Mostrar mensaje informando de que se van a cerrar todas las conversaciones
            JOptionPane.showMessageDialog(principal,texto.getString("abortar_conversaciones_dialogo_principal"),texto.getString("abortar_conversaciones_dialogo_title"),JOptionPane.INFORMATION_MESSAGE);

            // Cerrar la conversacion
            VentanaConversacion.cerrarConversaciones();
        }
    }

    /**
     * Aborta las conversaciones que tiene con este contacto.
     * @param contacto El contacto con el que se debe mantener la conversación
     */
    public void abortarConversaciones(String contacto){

        if(VentanaConversacion.hayConversaciones()){

            // Mostrar mensaje informando de que se van a cerrar todas las conversaciones
            JOptionPane.showMessageDialog(principal,texto.getString("abortar_conversaciones_dialogo_contacto_principal"),texto.getString("abortar_conversaciones_dialogo_title"),JOptionPane.INFORMATION_MESSAGE);

            // Cerrar las conversaciones del contacto
            VentanaConversacion.cerrarConversaciones(contacto);
        }
    }

    /**
     * Cancela todas las transferencias activas.
     */
    public void abortarTransferencias(){

        // Comprobar si hay transferencias
        if(vgt.getEstado() == VentanaGestorDeTransferencias.CON_TRANSFERENCIAS){

            // Mostrar mensaje informando de que se van a cerrar todas las transferencias
            JOptionPane.showMessageDialog(principal,texto.getString("abortar_transferencias_dialogo_principal"),texto.getString("abortar_transferencias_dialogo_title"),JOptionPane.INFORMATION_MESSAGE);

            // Cancelar las transferencias
            vgt.abortarTransferencias();
        }
    }

    /**
     * Cancela aquellas transferencias activas establecidas con el contacto.
     * @param contacto El contacto para el que se van a abortar las transferencias.
     * @param dialogo Valor booleano que indica si es necesario mostrar el cuadro
     * de diálogo en el que se informa de que se van a cerrar las transferencias
     * o no.
     */
    public void abortarTransferencias(String contacto, boolean dialogo){

        // Comprobar si hay transferencias
        if(vgt.getEstado() == VentanaGestorDeTransferencias.CON_TRANSFERENCIAS){

            // Mostrar mensaje informando de que se van a cerrar todas las transferencias
            if(dialogo) {
                JOptionPane.showMessageDialog(principal,texto.getString("abortar_transferencias_dialogo_contacto_principal"),texto.getString("abortar_transferencias_dialogo_title"),JOptionPane.INFORMATION_MESSAGE);
            }

            // Cancelar las transferencias
            vgt.abortarTransferencias(contacto);
        }
    }

    /**
     * Desconecta al usuario de la conexión. Se crea un hilo porque dicha opera
     * ción puede tardar, en caso de tener transferencias pendientes.
     */
    @Override
    public void run(){
        ConexionControlador cnc = ConexionControlador.getInstancia();
        cnc.desconectar();
    }
}

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

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import org.jajim.controladores.ConexionControlador;
import org.jajim.interfaz.ventanas.VentanaPrincipal;

/**
 * @author Florencio Cañizal Calles
 * @version 1.1
 * Clase oyente que se activa para realizar las operaciones relativas a la
 * desconexión del sistema.
 */
public class DesconectarActionListener implements ActionListener,Runnable{

    // Variables importantes
    private VentanaPrincipal vp;

    /**
     * Constructor de la clase. Inicializa las variables importantes.
     * @param vp Ventana principal de la aplicación.
     */
    public DesconectarActionListener(VentanaPrincipal vp){
        this.vp = vp;
    }

    /**
     * Método que se ejecuta cuando se selecciona la opción de desconectarse del
     * servidor. Cancela las conversaciones y las transferencias y cierra la cone
     * xión.
     * @param e El evento que genera la ejecución del método.
     */
    @Override
    public void actionPerformed(ActionEvent e){

        // Abortar las conversaciones y las trasferencias
        AbortarOperaciones ao = new AbortarOperaciones(vp,vp,vp.getVgt());
        ao.abortarConversaciones();
        ao.abortarTransferencias();

        // Desconectar la cuenta
        Thread t = new Thread(this);
        t.start();
        vp.conexionCancelada();
    }

    /**
     * Se ocupa de desconectar al usuario. Se hace en otro hilo, para que la in
     * terfaz se actualice rápidamente.
     */
    @Override
    public void run(){
        ConexionControlador cnc = ConexionControlador.getInstancia();
        cnc.desconectar();
    }
}

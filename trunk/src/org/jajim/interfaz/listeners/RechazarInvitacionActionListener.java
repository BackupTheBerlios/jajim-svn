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

import org.jajim.controladores.ConversacionControlador;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JDialog;

/**
 * @author Florencio Cañizal Calles
 * @version 1.1
 * Clase oyente que escucha los eventos de rechazo de invitación a una conversa
 * ción, provenientes del formulario en el que se ofrece la posibilidad de aceptar
 * o rechazar y del formulario en el que se introduce el nick para conversar.
 */
public class RechazarInvitacionActionListener implements ActionListener{

    private JDialog dialogo;
    private String room;
    private String contacto;

    /**
     * Constructor de la clase. Inicializa las variables necesarias.
     * @param dialogo El formulario de aceptación o rechazo de la invitación.
     * @param contacto El contacto que envió la invitación.
     * @param room La sala donde tiene lugar la conversación.
     */
    public RechazarInvitacionActionListener(JDialog dialogo,String contacto,String room){
        this.dialogo = dialogo;
        this.contacto = contacto;
        this.room = room;
    }

    /**
     * Método que se ejecuta cuando el usuario selecciona la opción Rechazar del
     * formulario que proporciona las opciones a elegir con respecto a la invita
     * ción o cuando selecciona la opción Cancelar del formulario para la intro
     * ducción del nick a utilizar en la conversación. Utiliza al controlador de
     * las vonversaciones para llevar a cabo la operación..
     * @param e El evento que produce la ejecución del método.
     */
    @Override
    public void actionPerformed(ActionEvent e) {
        
        // LLamar al controlador para que realice la operación.
        ConversacionControlador.rechazarInvitacion(contacto,room);
        dialogo.dispose();
    }
}

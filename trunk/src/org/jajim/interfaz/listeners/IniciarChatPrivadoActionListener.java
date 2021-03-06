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
import javax.swing.JFrame;
import org.jajim.interfaz.utilidades.PanelContactos;
import org.jajim.interfaz.ventanas.VentanaConversacion;
import org.jajim.interfaz.ventanas.VentanaConversacionChatPrivado;

/**
 * @author Florencio Cañizal Calles
 * @version 1.2 Oyente que escucha eventos procedentes de la opción de iniciar chat privados del sistema.
 */
public class IniciarChatPrivadoActionListener implements ActionListener {

    /**
     * Constructor de la clase. Inicializa las variables necesarias.
     */
    public IniciarChatPrivadoActionListener() {
    }

    /**
     * Método que se ejecuta cuando se selecciona la opción de comenzar un chat privado. Lanza la ventana de
     * conversaciones para poder comenzar con el chat.
     * <p>
     * @param e El evento que produce la ejecución del método.
     */
    @Override
    public void actionPerformed(ActionEvent e) {

        // Cerrar el menú popup de los contactos
        PanelContactos.getInstancia().cerrarPopupContactos();

        // Recuperar el alias del contacto y lanzar una ventana de conversación
        String alias = e.getActionCommand();

        // Si no hay ya un chat privado con ese usuario se comienza uno nuevo.
        if (!VentanaConversacion.hayChatPrivado(alias)) {
            new VentanaConversacionChatPrivado(alias);
        }
        else {
            // Recuperar la ventana del chat privado
            VentanaConversacionChatPrivado vccp = VentanaConversacion.getChatPrivado(alias);

            if (vccp.getEstado() == VentanaConversacionChatPrivado.OCULTA) {
                // Si la ventana está oculta se pone en estado normal y se activa su visualización
                vccp.setEstado(VentanaConversacionChatPrivado.ACTIVA);
                vccp.setVisible(true);
            }

            if (vccp.getEstado() == VentanaConversacionChatPrivado.MINIMIZADA) {
                // Si la ventana está minimizada se pone en estado normal.
                vccp.setEstado(VentanaConversacionChatPrivado.ACTIVA);
                vccp.setState(JFrame.NORMAL);
            }
        }
    }
}

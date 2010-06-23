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

import org.jajim.interfaz.utilidades.PanelContactos;
import org.jajim.interfaz.ventanas.VentanaConversacion;
import org.jajim.modelo.conversaciones.TiposDeChatEnumeracion;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * @author Florencio Cañizal Calles
 * @version 1.0.1
 * Oyente que escucha eventos procedentes de la opción de iniciar chat privados
 * del sistema.
 */
public class IniciarChatPrivadoActionListener implements ActionListener{

    private PanelContactos pc;

    /**
     * Constructor de la clase. Inicializa las variables necesarias.
     * @param pc El panel de contactos de la aplicación.
     */
    public IniciarChatPrivadoActionListener(PanelContactos pc){
        this.pc = pc;
    }

    /**
     * Método que se ejecuta cuando se selecciona la opción de comenzar un chat
     * privado. Lanza la ventana de conversaciones para poder comenzar con el chat.
     * @param e El evento que produce la ejecución del método.
     */
    @Override
    public void actionPerformed(ActionEvent e) {

        // Cerrar el menú popup de los contactos
        pc.cerrarPopupContactos();

        // Recuperar el alias del contacto y lanzar una ventana de conversación
        String alias = e.getActionCommand();

        if(!VentanaConversacion.hayChatPrivado(alias))
            new VentanaConversacion(pc.getVp(),alias,TiposDeChatEnumeracion.chatPrivado);
    }
}

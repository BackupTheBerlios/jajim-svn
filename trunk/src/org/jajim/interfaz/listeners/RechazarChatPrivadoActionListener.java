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

import org.jajim.interfaz.dialogos.AceptarORechazarChatPrivadoFormulario;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import org.jajim.controladores.ConversacionControladorChatPrivado;

/**
 * @author Florencio Cañizal Calles
 * @version 1.1
 * Clase oyente que escucha los eventos de rechazo de chat privado, procedentes
 * del formulario habilitado para dicha función.
 */
public class RechazarChatPrivadoActionListener implements ActionListener{

    private AceptarORechazarChatPrivadoFormulario acpf;
    private String idChat;

    /**
     * Constuctor de la clase. Inicializa las variables necesarias.
     * @param acpf El formulario donde se sugiere aceptar o rechazar la petción
     * de chat.
     * @param idChat El identificador del chat.
     */
    public RechazarChatPrivadoActionListener(AceptarORechazarChatPrivadoFormulario acpf,String idChat){
        this.acpf = acpf;
        this.idChat = idChat;
    }

    /**
     * Método que se ejecuta cuando se selecciona la opción de rechazar chat en
     * el formulario disponible para gestionar esta tarea. Rechaza la petición de
     * chat en colaboración con el controlador de las conversaciones.
     * @param e El evento que produce la ejecución del método.
     */
    @Override
    public void actionPerformed(ActionEvent e) {

        // LLamar al controlador de las conversaciones para que rechace el chat
        ConversacionControladorChatPrivado.rechazarChatPrivado(idChat);

        // Cerrar el formulario
        acpf.dispose();
    }
}

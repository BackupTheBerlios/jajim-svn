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

package org.jajim.controladores;

import java.util.Observer;
import org.jajim.excepciones.ImposibleEnviarMensajeException;
import org.jajim.modelo.conversaciones.ChatListener;
import org.jajim.modelo.conversaciones.InvitacionListener;
import org.jivesoftware.smack.ChatManager;
import org.jivesoftware.smack.XMPPConnection;
import org.jivesoftware.smackx.muc.MultiUserChat;

/**
 * @author Florencio Cañizal Calles
 * @version 1.1
 * Clase que gestiona todas aquellas operaciones relacionadas con las conversacio
 * nes.
 */
public abstract class ConversacionControladorNuevo {

    protected String usuario;
    protected String[] preferencias;
    protected static ChatListener cl;
    private static InvitacionListener il;
    protected static ChatManager cm;

    /**
     * Constructor de la clase. Inicia la conversación con el usuario.
     * @param usuario el usuario con el que se va a iniciar la conversación.
     */
    public ConversacionControladorNuevo(String usuario){
        this.usuario = usuario;
    }

    /**
     * Envía un mensaje a través de la conversación.
     * @param mensaje El contenido del mensaje que se desea enviar.
     * @throws ImposibleEnviarMensajeException Si no se puede enviar el mensaje
     * a los participantes en la conversación.
     */
    public abstract void enviarMensaje(String mensaje) throws ImposibleEnviarMensajeException;

    /**
     * Retorna la lista de participantes del chat.
     * @return La lista de participantes del chat.
     */
    public abstract String[] getParticipantes();

    /**
     * Método que actualiza las preferencias en el controlador para que éste pueda
     * enviarlas en el mensaje correspondiente.
     * @param preferencias Las nuevas preferencias de mensajes.
     */
    public void actualizarPreferencias(String[] preferencias){
        this.preferencias = preferencias;
    }

    /**
     * Asocia un oyente de chat a la conexión
     * @param xc La conexión activa.
     * @param o El oyente que será notificado de los eventos de chat.
     */
    public static void crearListener(XMPPConnection xc,Observer o){

        // Crear el chat listener y asociarlo a la conexión
        cl = new ChatListener(o);
        cm = xc.getChatManager();
        cm.addChatListener(cl);

        // Crear el listener de las invitaciones y asignarlo
        il = new InvitacionListener(o);
        MultiUserChat.addInvitationListener(xc,il);
    }
}

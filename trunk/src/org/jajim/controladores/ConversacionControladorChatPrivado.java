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
import org.jajim.modelo.conversaciones.MensajesChatPrivadoListener;
import org.jajim.utilidades.log.ManejadorDeLogs;
import org.jivesoftware.smack.Chat;
import org.jivesoftware.smack.XMPPException;
import org.jivesoftware.smack.packet.Message;

/**
 * @author Florencio Cañizal Calles
 * @version 1.1
 * Clase que gestiona todas aquellas operaciones relacionadas con las conversacio
 * nes llevadas a cabo en un chat privado.
 */
public class ConversacionControladorChatPrivado extends ConversacionControlador{

    private Chat conversacionPrivada;
    private MensajesChatPrivadoListener mcpl;

    /**
     * Constructor de la clase. Inicializa las variables necesarias.
     * @param usuario El usuario con el que se va mantener el chat privado.
     * @param observadorDeMensajes El observador que será notificado de los mensa
     * jes que lleguen al chat privado.
     */
    public ConversacionControladorChatPrivado(String usuario,Observer observadorDeMensajes){

        // Inicialización de variables
        super(usuario);
        mcpl = new MensajesChatPrivadoListener(observadorDeMensajes);
    }

    /**
     * Crea un nuevo chat privado con un usuario.
     */
    public void crearChatPrivado(){
        conversacionPrivada = cm.createChat(usuario,mcpl);
    }

    /**
     * Acepta una petición de chat recibida.
     * @param El identificador de la petición de chat.
     */
    public void aceptarChatPrivado(String idChat){

        conversacionPrivada = cl.getChat(idChat);
        conversacionPrivada.addMessageListener(mcpl);
        Message m = cl.getPrimerMensaje(idChat);
        if(m != null)
            mcpl.processMessage(null,m);
    }

    /**
     * Envia un mensaje a través del chat privado
     * @param mensaje El mensaje que se desea enviar.
     * @throws ImposibleEnviarMensajeException Si no se puede enviar el mensaje
     * al otro usuario de la conversación.
     */
    @Override
    public void enviarMensaje(String mensaje) throws ImposibleEnviarMensajeException{

        // Construir el mensaje
        Message m = new Message();
        m.setBody(mensaje);
        m.setProperty("fuente",preferencias[0]);
        m.setProperty("tamaño",preferencias[1]);
        m.setProperty("colorRojo",preferencias[2]);
        m.setProperty("colorVerde",preferencias[3]);
        m.setProperty("colorAzul",preferencias[4]);
        m.setProperty("negrita",preferencias[5]);
        m.setProperty("cursiva",preferencias[6]);

        // Utilizar el Chat para mandar el mensaje
        try{
            conversacionPrivada.sendMessage(m);
        }catch(XMPPException xe){
            // En caso de que se produzca un error se escribe en el fichero
            // de log y se lanza una excepción
            ManejadorDeLogs mdl = ManejadorDeLogs.getManejadorDeLogs();
            mdl.escribir("No se puede enviar el mensaje: " + mensaje + "- Conversación: " + conversacionPrivada.getParticipant());
            throw new ImposibleEnviarMensajeException();
        }
    }

    /**
     * Cierra un chat privado de manera correcta en el sistema.
     */
    @Override
    public void cerrarConversacion(){

        try{
            this.enviarMensaje("Exit");
            cl.eliminarChat(conversacionPrivada);
        }catch(ImposibleEnviarMensajeException ieme){}
    }

    /**
     * Retorna la lista de participantes del chat.
     * @return La lista de participantes del chat.
     */
    @Override
    public String[] getParticipantes(){

        String[] participantes = null;

        // Llama a la conversación para recuperar los participantes
        String participante = conversacionPrivada.getParticipant();
        participantes = new String[1];
        participantes[0] = participante;

        return participantes;
    }

    /**
     * Rechaza una de las peticiones de chat privado.
     * @param idChat El identificador del chat privado.
     */
    public static void rechazarChatPrivado(String idChat){

        // Recuperar el chat
        Chat chat = cl.getChat(idChat);

        // Enviar un mensaje rechazando la conversación.
        try{
            chat.sendMessage("Exit");
        }catch(XMPPException xe){}

        // Eliminar el chat
        cl.eliminarChat(chat);
    }
}

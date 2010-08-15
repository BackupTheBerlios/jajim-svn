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

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Observer;
import org.jajim.excepciones.FicheroNoEncontradoException;
import org.jajim.excepciones.ImposibleEnviarMensajeException;
import org.jajim.excepciones.ImposibleGuardarConversacionException;
import org.jajim.modelo.conversaciones.ChatListener;
import org.jajim.modelo.conversaciones.InvitacionListener;
import org.jajim.utilidades.log.ManejadorDeLogs;
import org.jivesoftware.smack.ChatManager;
import org.jivesoftware.smack.XMPPConnection;
import org.jivesoftware.smackx.muc.MultiUserChat;

/**
 * @author Florencio Cañizal Calles
 * @version 1.1
 * Clase que gestiona todas aquellas operaciones relacionadas con las conversacio
 * nes.
 */
public abstract class ConversacionControlador {

    protected String usuario;
    protected String[] preferencias;
    protected static ChatListener cl;
    protected static InvitacionListener il;
    protected static ChatManager cm;

    /**
     * Constructor de la clase. Inicia la conversación con el usuario.
     * @param usuario el usuario con el que se va a iniciar la conversación.
     */
    public ConversacionControlador(String usuario){
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
     * Cierra una conversación de manera correcta en el sistema.
     */
    public abstract void cerrarConversacion();

    /**
     * Guarda el contenido de la conversacion actual.
     * @param nombre El nombre del fichero en el que se va a guardar.
     * @param ruta La ruta de localización del fichero.
     * @param texto El contenido de la conversación.
     * @throws FicheroNoEncontradoException Si no se puede crear el fichero en el
     * que se va a guardar la conversación
     * @throws ImposibleGuardarConversacionException Si no se puede guardar la con
     * versación en el fichero.
     */
    public void guardarConversacion(String nombre,String ruta,String texto) throws FicheroNoEncontradoException,ImposibleGuardarConversacionException{

        // Crear un archivo file
        String f = ruta + File.separator + nombre + ".html";
        File fichero = new File(f);

        // Escribir en el fichero
        try{
            FileOutputStream fos = new FileOutputStream(fichero);
            fos.write(texto.getBytes());
            fos.close();
        }catch(FileNotFoundException fnfe){
            // En caso de que se produzca un error se escribe en el fichero
            // de log y se lanza una excepción
            ManejadorDeLogs mdl = ManejadorDeLogs.getManejadorDeLogs();
            mdl.escribir("No se encuentra el fichero: " + f);
            throw new FicheroNoEncontradoException();
        }catch(IOException ioe){
            // En caso de que se produzca un error se escribe en el fichero
            // de log y se lanza una excepción
            ManejadorDeLogs mdl = ManejadorDeLogs.getManejadorDeLogs();
            mdl.escribir("No puede escribir en el fichero: " + f);
            throw new ImposibleGuardarConversacionException();
        }
    }

    /**
     * Asocia un oyente de chat a la conexión
     * @param o El oyente que será notificado de los eventos de chat.
     */
    public static void crearListener(Observer o){

        // Recuperar la conexión
        ConexionControlador cnc = ConexionControlador.getInstancia();
        XMPPConnection xc = cnc.getXc();

        // Crear el chat listener y asociarlo a la conexión
        cl = new ChatListener(o,xc);
        cm = xc.getChatManager();
        cm.addChatListener(cl);

        // Crear el listener de las invitaciones y asignarlo
        il = new InvitacionListener(o);
        MultiUserChat.addInvitationListener(xc,il);
    }

    /**
     * Elimina el oyente de chat de la conexión.
     */
    public static void eliminarListener(){

        // Recuperar la conexión
        ConexionControlador cnc = ConexionControlador.getInstancia();
        XMPPConnection xc = cnc.getXc();

        // Eliminar el chat listener y resetearlo.
        cm.removeChatListener(cl);
        cm = null;
        cl = null;
        MultiUserChat.removeInvitationListener(xc,il);
        il = null;
    }
}

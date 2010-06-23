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

package org.jajim.modelo.conversaciones;

import org.jajim.modelo.conexiones.EventosDeConexionEnumeracion;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Observable;
import java.util.Observer;
import java.util.Set;
import org.jivesoftware.smack.Chat;
import org.jivesoftware.smack.ChatManagerListener;

/**
 * @author Florencio Cañizal Calles
 * @version 1.0.1
 * Clase oyente que reacciona a los eventos de chat recibidos a través de la conexión.
 */
public class ChatListener extends Observable implements ChatManagerListener{

    private Map<String,Chat> listaDeChats;
    private String idChat;

    /**
     * Constructor de la clase. Inicializa las variables necesarias.
     * @param o El observador que será notificado de los eventos de creación de
     * chat.
     */
    public ChatListener(Observer o){
        this.addObserver(o);
        listaDeChats = new HashMap<String,Chat>();
    }

    /**
     * Método que se ejecuta cada vez que se crea un chat.
     * @param chat El chat que se ha creado.
     * @param createdLocally Valor booleano que indica si se ha creado localmente
     * o por otro usuario.
     */
    @Override
    public void chatCreated(Chat chat,boolean createdLocally) {
        
        // Si el chat fue creado por otro usuario, se almacena el chat y se notifi
        // ca al oyente
        if(!createdLocally){
            // Comprobar si ya había un chat creado con el usuario.
            Collection<Chat> chats = listaDeChats.values();
            Iterator<Chat> iterator = chats.iterator();
            while(iterator.hasNext()){
                Chat c = iterator.next();
                if(c.getParticipant().compareTo(chat.getParticipant()) == 0)
                    return;
            }
            // Si no es chat multiusuario
            if(chat.getParticipant().contains("/")){
                listaDeChats.put(chat.getThreadID(),chat);
                idChat = chat.getThreadID();
                this.setChanged();
                this.notifyObservers(EventosDeConexionEnumeracion.peticionDeChat);
            }
        }
    }

    /**
     * Método que elimina el chat de la lista de chats mantenidos.
     * @param chatEliminar El chat que se desea eliminar.
     */
    public void eliminarChat(Chat chatEliminar){

        // Eliminar el chat
        Set<Map.Entry<String,Chat>> conjunto = listaDeChats.entrySet();
        Iterator<Map.Entry<String,Chat>> iterator = conjunto.iterator();

        String claveEliminar =  null;
        while(iterator.hasNext()){
            Map.Entry<String,Chat> entrada = iterator.next();
            Chat c = entrada.getValue();
            if(c.equals(chatEliminar)){
                claveEliminar = entrada.getKey();
                break;
            }
        }

        listaDeChats.remove(claveEliminar);
    }

    /**
     * Devuelve el identificador del chat.
     * @return El identificador del chat.
     */
    public String getIdChat(){
        return idChat;
    }

    /**
     * Devuelve el contacto que inició el chat cuyo id es el especificado.
     * @param id El identificador del chat cuyo contacto se quiere recuperar.
     * @return El contacto del chat.
     */
    public String getContacto(String id){

        // Recuperar el chat
        Chat c = listaDeChats.get(id);

        // Retorna el contacto
        return c.getParticipant();
    }

    /**
     * Retorna el chat cuyo identificador se corresponde con el proporcionado
     * @param idChat El identificador del chat.
     * @return El chat adecuado.
     */
    public Chat getChat(String idChat){

        // Recuperar el chat y retornarlo
        Chat chat = listaDeChats.get(idChat);

        return chat;
    }
}

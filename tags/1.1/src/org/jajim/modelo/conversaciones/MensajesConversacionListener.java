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

import org.jajim.utilidades.estructuras.ColaSincronizadaConSobreescritura;
import org.jivesoftware.smack.PacketListener;
import org.jivesoftware.smack.packet.Message;
import org.jivesoftware.smack.packet.Packet;
import org.jivesoftware.smack.util.StringUtils;

/**
 * @author Florencio Cañizal Calles
 * @version 1.1
 * Cachea los mensajes recibidos en chats privados para recuperar, el primer mensaje
 * de un chat privado.
 */
public class MensajesConversacionListener implements PacketListener{

    private ColaSincronizadaConSobreescritura<Message> cscs;

    /**
     * Constructor de la clase. Inicializa las variables adecuadas.
     */
    public MensajesConversacionListener(){
        cscs = new ColaSincronizadaConSobreescritura<>(5);
    }

    /**
     * Método que se ejecuta cada vez que se recibe un mensaje de conversación.
     * Recupera el contenido del mensaje y lo guarda en una pila.
     * @param arg0 El paquete que se acaba de recibir.
     */
    @Override
    public void processPacket(Packet arg0) {

        Message m = null;
        
        // Recuperar el mensaje
        if(arg0 instanceof Message) {
            m = (Message) arg0;
        }
        else {
            return;
        }

        // Comprobar si el mensaje es de chat privado
        String servicio = StringUtils.parseServer(m.getFrom());
        int posicion = servicio.indexOf('.');
        String s = servicio.substring(0,posicion);

        if(s.compareTo("conf") == 0 || s.compareTo("conference") == 0 || s.compareTo("chat") == 0 || s.compareTo("muc") == 0) {
        }
        else{
            cscs.offer(m);
        }
    }

    /**
     * Retorna el mensaje que ha dado lugar al chat privado.
     * @param idChat El identificador del chat.
     * @return El mensaje que da lugar al chat privado.
     */
    public Message getPrimerMensaje(String idChat){
        
        Message m = null;
        boolean finalizado = false;

        // Sacar los mensajes almacenados en la cola hasta dar con el apropiado
        while(!finalizado){
            if(!cscs.isEmpty()){
                m = cscs.poll();
                if(m.getThread() != null) {
                    if(idChat.compareTo(m.getThread()) == 0){
                        return m;
                    }
                }
            }else{
                finalizado = true;
            }
        }

        return null;
    }
}

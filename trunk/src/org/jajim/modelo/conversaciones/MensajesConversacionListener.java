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

import java.util.Collection;
import org.jivesoftware.smack.PacketListener;
import org.jivesoftware.smack.XMPPConnection;
import org.jivesoftware.smack.XMPPException;
import org.jivesoftware.smack.packet.Message;
import org.jivesoftware.smack.packet.Packet;
import org.jivesoftware.smack.util.StringUtils;
import org.jivesoftware.smackx.muc.HostedRoom;
import org.jivesoftware.smackx.muc.MultiUserChat;

/**
 * @author Florencio Cañizal Calles
 * @version 1.1
 * Cachea los mensajes recibidos en chats privados para recuperar, el primer mensaje
 * de un chat privado.
 */
public class MensajesConversacionListener implements PacketListener{

    /**
     * Constructor de la clase. Inicializa las variables adecuadas.
     */
    public MensajesConversacionListener(){
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
        if(arg0 instanceof Message)
            m = (Message) arg0;
        else
            return;

        // Comprobar si el mensaje es de chat privado
        String servicio = StringUtils.parseServer(m.getFrom());
        int posicion = servicio.indexOf('.');
        String s = servicio.substring(0,posicion);

        System.out.println("----- MENSAJE -----");
        if(s.compareTo("conf") == 0 || s.compareTo("conference") == 0 || s.compareTo("chat") == 0 || s.compareTo("muc") == 0)
            System.out.println("Mensaje de chat multiusuario");
        else
            System.out.println("Mensaje de chat privado");

        // DEBUG
        System.out.println("El servicio es: " + s);
        System.out.println("Cuerpo: " + m.getBody());
        System.out.println("Desde: " + m.getFrom());
        System.out.println("Identificador: " + m.getPacketID());
        System.out.println("Tema: " + m.getSubject());
        System.out.println("Hilo: " + m.getThread());
        System.out.println("Para: " + m.getTo());
        System.out.println("Xmlns: " + m.getXmlns());
        System.out.println("-------------------");

    }
}

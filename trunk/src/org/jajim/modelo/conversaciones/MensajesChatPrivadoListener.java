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

import java.util.Observable;
import java.util.Observer;
import org.jivesoftware.smack.Chat;
import org.jivesoftware.smack.MessageListener;
import org.jivesoftware.smack.packet.Message;

/**
 * @author Florencio Cañizal Calles
 * @version 1.2 Captura los mensajes recibidos por otros miembros de la conversación y los pa sa a la interfaz.
 */
public class MensajesChatPrivadoListener extends Observable implements MessageListener {

    /**
     * Constructor de la clase. Inicializa las variables adecuadas.
     * <p>
     * @param observador El observador de la interfaz que recibirá los mensajes recibidos.
     */
    public MensajesChatPrivadoListener(Observer observador) {
        this.addObserver(observador);
    }

    /**
     * Método que se ejecuta cuando llega un mensaje de un contacto de la conver sación.
     * <p>
     * @param chat    Chat para el que se ha recibido el mensaje.
     * @param mensaje Mensaje que se ha recibido.
     */
    @Override
    public void processMessage(Chat chat, Message mensaje) {

        // Extraer la información del mensaje
        String[] contenido;
        if (mensaje.getProperty("fuente") == null) {
            contenido = new String[2];
            contenido[0] = mensaje.getFrom();
            contenido[1] = mensaje.getBody();
        }
        else {
            contenido = new String[9];
            contenido[0] = mensaje.getFrom();
            contenido[1] = mensaje.getBody();
            contenido[2] = (String) mensaje.getProperty("fuente");
            contenido[3] = (String) mensaje.getProperty("tamaño");
            contenido[4] = (String) mensaje.getProperty("colorRojo");
            contenido[5] = (String) mensaje.getProperty("colorVerde");
            contenido[6] = (String) mensaje.getProperty("colorAzul");
            contenido[7] = (String) mensaje.getProperty("negrita");
            contenido[8] = (String) mensaje.getProperty("cursiva");
        }

        // Pasar la información a la interfaz
        this.setChanged();
        this.notifyObservers(contenido);
    }
}

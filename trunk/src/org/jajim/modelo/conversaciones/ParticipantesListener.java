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
import org.jivesoftware.smack.PacketListener;
import org.jivesoftware.smack.packet.Packet;
import org.jivesoftware.smack.util.StringUtils;

/**
 * @author Florencio Cañizal Calles
 * @version 1.2 Clase oyente que escucha los eventos de adición a un chat multiusurario proce dentes del servidor.
 */
public class ParticipantesListener extends Observable implements PacketListener {

    private String nickPropio;
    private String nick;
    private String usuario;

    /**
     * Constructor de la clase. Iniciliza las variables necesarias.
     * <p>
     * @param observador El observador que será notificado de los eventos produci dos.
     */
    public ParticipantesListener(Observer observador) {
        this.addObserver(observador);
    }

    /**
     * Método que se ejecuta cuando se recibe un paquete que informa de que un usuario se ha conectado a nuestro chat
     * multiusuario.
     * <p>
     * @param packet El paquete que confirma el evento.
     */
    @Override
    public void processPacket(Packet packet) {

        // Recuperar el nick del usuario
        usuario = packet.getFrom();
        nick = StringUtils.parseResource(usuario);

        // Si es nuestro nick abortar la operación.
        if (nick.compareTo(nickPropio) == 0) {
            return;
        }

        // Si el paquete no es de desconexión.
        if (packet.toString().compareTo("unavailable") != 0) {
            // Notificar a los oyentes que un usuario se ha añadido a la sala.
            this.setChanged();
            this.notifyObservers(EventosConversacionEnumeracion.participanteAñadido);
        }
        else {
            // Notificar a los oyentes que un usuario se ha desconectado de la sala.
            this.setChanged();
            this.notifyObservers(EventosConversacionEnumeracion.participanteDesconectado);
        }
    }

    /**
     * Método que actualiza el valor del atributo nickPropio.
     * <p>
     * @param nickPropio El nuevo valor para el atributo.
     */
    public void setNickPropio(String nickPropio) {
        this.nickPropio = nickPropio;
    }

    /**
     * Retorna el valor del atributo nick.
     * <p>
     * @return El valor del atributo nick.
     */
    public String getNick() {
        return nick;
    }

    /**
     * Retorna el valor del atributo usuario.
     * <p>
     * @return El valor del atributo usuario.
     */
    public String getUsuario() {
        return usuario;
    }
}

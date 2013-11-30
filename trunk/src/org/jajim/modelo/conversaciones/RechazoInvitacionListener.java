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
import org.jivesoftware.smack.util.StringUtils;
import org.jivesoftware.smackx.muc.InvitationRejectionListener;

/**
 * @author Florencio Cañizal Calles
 * @version 1.2 Clase oyente que escucha los eventos de rechaza de invitacion a un chat multi usurario procedentes del
 * servidor.
 */
public class RechazoInvitacionListener extends Observable implements InvitationRejectionListener {

    private String invitado;

    /**
     * Constructor de la clase. Inicializa las variables necesarias.
     * <p>
     * @param observador El observador de la interfaz que será notificado de los rechazos de invitación recibidos.
     */
    public RechazoInvitacionListener(Observer observador) {
        this.addObserver(observador);
    }

    /**
     * Método que se ejecuta cada vez que se recibe una notificación de rechazo de invitación, procedente del servidor.
     * <p>
     * @param invitado El invitado que rechaza la invitación.
     * @param razon    La razón por la que la rechaza.
     */
    @Override
    public void invitationDeclined(String invitado, String razon) {

        // Guardar el usuario que ha rechazado la invitción.
        this.invitado = StringUtils.parseBareAddress(invitado);

        // Notificarlo al observador de la interfaz
        this.setChanged();
        this.notifyObservers(EventosConversacionEnumeracion.invitacionRechazada);
    }

    /**
     * Retorna el valor del atributo invitado.
     * <p>
     * @return El valor del atributo invitado.
     */
    public String getInvitado() {
        return this.invitado;
    }
}

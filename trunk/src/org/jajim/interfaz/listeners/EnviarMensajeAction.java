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

import java.awt.event.ActionEvent;
import java.beans.PropertyChangeListener;
import java.util.Observable;
import java.util.Observer;
import javax.swing.Action;
import org.jajim.controladores.ConversacionControlador;
import org.jajim.excepciones.ImposibleEnviarMensajeException;
import org.jajim.interfaz.dialogos.MensajeError;
import org.jajim.interfaz.ventanas.VentanaConversacion;

/**
 * @author Florencio Cañizal Calles
 * @version 1.2 Se ocupa de enviar un gestionar el envía de un mensaje de texto en una conver sación.
 */
public class EnviarMensajeAction extends Observable implements Action {

    private final VentanaConversacion vc;

    /**
     * Constructor de la clase. Inicializa las variables necesarias.
     * <p>
     * @param vc         La ventana de la conversación.
     * @param observador El observador al que se le notificarán el envío de mensajes
     */
    public EnviarMensajeAction(VentanaConversacion vc, Observer observador) {
        this.vc = vc;
        this.addObserver(observador);
    }

    @Override
    public Object getValue(String key) {
        return new Object();
    }

    @Override
    public void putValue(String key, Object value) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void setEnabled(boolean b) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public boolean isEnabled() {
        return true;
    }

    @Override
    public void addPropertyChangeListener(PropertyChangeListener listener) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void removePropertyChangeListener(PropertyChangeListener listener) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        // Recuperar el mensaje obtenido
        String mensaje = vc.getMensaje();

        // Publicar el mensaje a la interfaz
        String[] contenido = new String[2];
        contenido[0] = "Usuario";
        contenido[1] = mensaje;
        this.setChanged();
        this.notifyObservers(contenido);

        // Llamar al controlador para enviar el mensaje al resto de miembros de
        // la conversación
        ConversacionControlador cvc = vc.getCvc();
        try {
            cvc.enviarMensaje(mensaje);
        }
        catch (ImposibleEnviarMensajeException ieme) {
            // Informar del error al usuario.
            new MensajeError(vc, "imposible_enviar_mensaje_error");
        }
    }
}

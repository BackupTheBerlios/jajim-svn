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

import org.jajim.excepciones.ImposibleEnviarMensajeException;
import org.jajim.interfaz.dialogos.MensajeError;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Observable;
import java.util.Observer;
import org.jajim.controladores.ConversacionControlador;
import org.jajim.interfaz.ventanas.VentanaConversacion;

/**
 * @author Florencio Cañizal Calles
 * @version 1.1
 * Clase que escucha las peticiones de envío de mensajes y las lleva a cabo.
 */
public class EnviarMensajeActionListener extends Observable implements ActionListener{

    private VentanaConversacion vc;

     /**
     * Constructor de la clase. Inicializa las variables necesarias.
     * @param vc La ventana de la conversación
     * @param observador El panel de la conversación.
     */
    public EnviarMensajeActionListener(VentanaConversacion vc,Observer observador){
        this.vc = vc;
        this.addObserver(observador);
    }

    /**
     * Operación que se ejecuta cuando se selecciona la opción de enviar mensaje
     * de la herramienta. Actualiza la interfaz con el nuevo mensaje y delega en
     * el controlador el envío del mensaje a todos los miembros de la conversaci
     * ón.
     * @param e Evento que generó la ejecución del método.
     */
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
        try{
            cvc.enviarMensaje(mensaje);
        }catch(ImposibleEnviarMensajeException ieme){
            // Informar del error al usuario.
            new MensajeError(vc,"imposible_enviar_mensaje_error");
        }
    }
}

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

import java.util.HashMap;
import java.util.Map;
import java.util.Observable;
import java.util.Observer;
import org.jajim.modelo.conexiones.EventosDeConexionEnumeracion;
import org.jivesoftware.smack.Connection;
import org.jivesoftware.smack.packet.Message;
import org.jivesoftware.smackx.muc.InvitationListener;

/**
 * @author Florencio Cañizal Calles
 * @version 1.1
 * Oyente que se activa cuando se recibe una invitación de participación en un
 * chat multiusuario.
 */
public class InvitacionListener extends Observable implements InvitationListener{

    private Map<String,String[]> listaDeInvitaciones;
    private String idInvitacion;
    private int generador;

    /**
     * Constructor de la clase. Iniciliza las variables necesarias.
     * @param o El observador que será notificado de los eventos de invitación.
     */
    public InvitacionListener(Observer o){
        this.addObserver(o);
        listaDeInvitaciones = new HashMap<>();
        generador = 0;
    }

    /**
     * Método que se ejecuta cada vez que se recibe una petición de invitación.
     * @param conn La conexión actual.
     * @param room La sala en la que se desarrolla la conversación.
     * @param inviter El usuario que nos ha invitado.
     * @param reason La razón de la invitación.
     * @param password La contraseña de la sala.
     * @param message Un mensaje.
     */
    @Override
    public void invitationReceived(Connection conn,String room,String inviter,String reason,String password,Message message) {

        // Almacenar la invitación y notificarselo a los oyentes.
        idInvitacion = String.valueOf(generador);
        String[] guardar = new String[2];
        guardar[0] = room;
        guardar[1] = inviter;
        listaDeInvitaciones.put(String.valueOf(generador),guardar);
        generador++;
        this.setChanged();
        this.notifyObservers(EventosDeConexionEnumeracion.invitacionAChat);
    }

    /**
     * Retorna el identificador de la última invitación recibida.
     * @return El identificador de la última invitación recibida.
     */
    public String getIdInvitacion(){
        return idInvitacion;
    }

    /**
     * Retorna la información vinculada a la invitación solicitada.
     * @param idInvitacion La invitación de la que se quiere recuperar la sala.
     * @return La información vinculada a la invitación solicitada.
     */
    public String[] getInformacion(String idInvitacion){

        // Recuperar la sala y eliminar la invitación de la lista.
        String[] informacion = listaDeInvitaciones.get(idInvitacion);
        listaDeInvitaciones.remove(idInvitacion);

        return informacion;
    }
}

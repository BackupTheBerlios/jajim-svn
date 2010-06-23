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

package org.jajim.modelo.conexiones;

import java.util.Observable;
import org.jivesoftware.smack.PacketListener;
import org.jivesoftware.smack.Roster;
import org.jivesoftware.smack.XMPPConnection;
import org.jivesoftware.smack.filter.PacketTypeFilter;
import org.jivesoftware.smack.packet.Packet;
import org.jivesoftware.smack.packet.Presence;

/**
 * @author Florencio Cañizal Calles
 * @version 1.0.1
 * Clase que escucha los paquetes de presencia que llegan al cliente. Envía a la
 * interfaz la información necesaria en caso de tener que actuar
 */
public class PaquetePresenciaListener extends Observable implements PacketListener{

    private XMPPConnection xc;
    private String origen;
    private Roster contactos;

    public PaquetePresenciaListener(XMPPConnection xc){

        // Inicio a esta clase como oyente de paquetes de presencia
        this.xc = xc;
        PacketTypeFilter filter = new PacketTypeFilter(Presence.class);
        xc.addPacketListener(this,filter);
        contactos = xc.getRoster();
    }

    /**
     * Método que se ejecuta cuando llega un paquete de presencia. En caso de ser
     * relevante a nivel de interfaz se pasa el mensaje a ella.
     * @param paquete Paquete que se ha recibido en el sistema.
     */
    @Override
    public void processPacket(Packet paquete) {

        Presence presencia = (Presence) paquete;

        // Se recibe una petición de suscripción y se pasa a la interfaz para que
        // se procese
        if(presencia.getType() == Presence.Type.subscribe){
            origen = presencia.getFrom();
            // Si ya estaba suscrito ignorar la petición
            if(contactos.getEntry(origen) != null){
                Presence respuesta = new Presence(Presence.Type.subscribed);
                respuesta.setTo(origen);
                respuesta.setProperty("inscrito",true);
                xc.sendPacket(respuesta);
                return;
            }
            this.setChanged();
            this.notifyObservers(EventosDeConexionEnumeracion.peticionDeSuscripcion);
        }
        // Se recibe una confirmación de suscripción y se pasa a la interfaz para
        // que se informe al usuario
        else if(presencia.getType() == Presence.Type.subscribed){
            Boolean b = (Boolean) presencia.getProperty("inscrito");
            if(b == null){
                origen = presencia.getFrom();
                this.setChanged();
                this.notifyObservers(EventosDeConexionEnumeracion.confirmacionDeSuscripcion);
            }
        }
        // Si se recibe un mensaje de unsuscribed quiere decir que se ha rechazado
        // la petición de contacto.
        else if(presencia.getType() == Presence.Type.unsubscribed){
            origen = presencia.getFrom();
            this.setChanged();
            this.notifyObservers(EventosDeConexionEnumeracion.denegacionDeSuscripcion);
        }
    }

    /**
     * Deja de escuchar la conexión.
     */
    public void desconectar(){
        // Elimina los oyentes de la clase y deja de escuchar la conexión.
        this.deleteObservers();
        xc.removePacketListener(this);
    }

    /**
     * Retorna el origen del mensaje de presencia.
     * @return Una cadena con el origen del mensaje de presencia.
     */
    public String getOrigen(){
        return origen;
    }
}

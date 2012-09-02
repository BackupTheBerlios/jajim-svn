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
import org.jivesoftware.smack.packet.Message;
import org.jivesoftware.smack.packet.Packet;

/**
 * @author Florencio Cañizal Calles
 * @version 1.1
 * Oyente que escucha los mensajes procendentes de un determinado chat multiusua
 * rio.
 */
public class MensajesChatMultiusuarioListener extends Observable implements PacketListener{

    private String alias;

    /**
     * Constructor de la clase. Inicializa las variables necesarias.
     * @param observador El obsevador que será notificado de la llegada de mensa
     * jes.
     */
    public MensajesChatMultiusuarioListener(Observer observador){
        this.addObserver(observador);
    }

    /**
     * Método de la interfaz PacketListener. Se ejecuta cuando se recibe un mensa
     * je de chat multiusuario.
     * @param arg0 El paquete recibido.
     */
    @Override
    public void processPacket(Packet arg0) {

        // Hacer el casting y extraer los valores
        Message mensaje = (Message) arg0;
        String[] contenido;
        if(mensaje.getProperty("fuente") == null){
            contenido = new String[2];
            contenido[0] = mensaje.getFrom();
            contenido[1] = mensaje.getBody();
        }
        else{
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

        // No publicar los mensajes provenientes del propio usuario
        if(contenido[0].contains(alias)) {
            return;
        }

        // Pasar la información a la interfaz
        this.setChanged();
        this.notifyObservers(contenido);
    }

    /**
     * Actualiza el valor del atributo alias.
     * @param alias El valor del atributo alias.
     */
    public void setAlias(String alias){
        this.alias = alias;
    }
}

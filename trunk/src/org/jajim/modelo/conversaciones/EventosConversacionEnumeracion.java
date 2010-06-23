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

/**
 * @author Florencio Cañizal Calles
 * @version 1.0.1
 * Clase enumeración que recoge los tipos de eventos importantes que se pueden
 * recibir a través de una conversación.
 */
public class EventosConversacionEnumeracion {

    // Evento
    private String evento;

    // Tipos de eventos
    public static EventosConversacionEnumeracion participanteAñadido = new EventosConversacionEnumeracion("Se ha añadido un participante");
    public static EventosConversacionEnumeracion invitacionRechazada = new EventosConversacionEnumeracion("Se ha rechazado la invitación");
    public static EventosConversacionEnumeracion participanteDesconectado = new EventosConversacionEnumeracion("Un participante ha abandonado la conversación");

    /**
     * Constructor de la clase. Inicializa las variables necesarias.
     * @param evento El evento que se ha originado.
     */
    private EventosConversacionEnumeracion(String evento){
        this.evento = evento;
    }

    /**
     * Determina si dos instancias de EventosConversacionEnumeracion son iguales.
     * @param object Una instancia de EventosConversacionEnumeracion.
     * @return 0 si son iguales, 1 si el parámeto es menor y -1 si es mayor.
     */
    public int compareTo(Object object){

        if(object instanceof EventosConversacionEnumeracion){

            // Se comparan las instancias y se retornan los valores adecuados.
            EventosConversacionEnumeracion instance = (EventosConversacionEnumeracion) object;

            if(this.evento.compareTo(instance.evento) > 0)
                return 1;
            else if(this.evento.compareTo(instance.evento) < 0)
                return -1;
            else
                return 0;
        }
        else
            throw new IllegalArgumentException();
    }
}

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
 * Clase enumeración que recoge los tipos de chat disponibles en el sistema.
 */
public class TiposDeChatEnumeracion{

    // chat
    private String chat;

    // Tipos de chat
    public static final TiposDeChatEnumeracion chatPrivado = new TiposDeChatEnumeracion("Chat privado");
    public static final TiposDeChatEnumeracion chatMultiUsuario = new TiposDeChatEnumeracion("Chat multiusuario");

    /**
     * Constructor de la clase. Inicializa las variables necesarias.
     * @param chat El chat que se va a disponer.
     */
    private TiposDeChatEnumeracion(String chat){
        this.chat = chat;
    }

    /**
     * Determina si dos instancias de TiposDeChatEnumeracion son iguales.
     * @param object Una instancia de TiposDeChatEnumeracion.
     * @return 0 si son iguales, 1 si el parámeto es menor y -1 si es mayor.
     */
    public int compareTo(Object object){

        if (object instanceof TiposDeChatEnumeracion){

            // Se comparan las instancia y se retorna los valores adecuados
            TiposDeChatEnumeracion instance = (TiposDeChatEnumeracion) object;

            if (this.chat.compareTo(instance.chat) > 0 )
                return 1;
            else if (this.chat.compareTo(instance.chat) < 0)
                return -1;
            else
                return 0;
        }
        else
            throw new IllegalArgumentException();
    }
}

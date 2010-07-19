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

package org.jajim.excepciones;

/**
 * @author Florencio Cañizal Calles
 * @version 1.0.1
 * Clase que representa una excepción que se produce cuando no existe una ruta
 * donde se desea guardar un fichero.
 */
public class RutaNoDisponibleException extends Exception{

    /**
     * Constructor de la clase.
     */
    public RutaNoDisponibleException(){}

    /**
     * Constructor de la clase.
     * @param message Mensaje que se desea propagar a los receptores de la excep
     * ción.
     */
    public RutaNoDisponibleException(String message){
        super(message);
    }
}

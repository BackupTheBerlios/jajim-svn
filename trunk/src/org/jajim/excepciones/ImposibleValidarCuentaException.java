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
 * @version 1.2 Clase que representa una excepción que se produce cuando no se puede validar la existencia de una cuenta
 * introducida por el usuario.
 */
public class ImposibleValidarCuentaException extends Exception {

    /**
     * Constructor de la clase.
     */
    public ImposibleValidarCuentaException() {
    }

    /**
     * Construtor de la clase.
     * <p>
     * @param message Mensaje que se propaga a los receptores de la excepción.
     */
    public ImposibleValidarCuentaException(String message) {
        super(message);
    }
}

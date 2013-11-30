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

/**
 * @author Florencio Cañizal Calles
 * @version 1.2 Clase enumeración que recoge los tipos de eventos importantes que se pueden recibir a través de una
 * conexión.
 */
public class EventosDeConexionEnumeracion {

    // Evento
    private final String evento;

    // Tipos de eventos
    public static final EventosDeConexionEnumeracion peticionDeSuscripcion = new EventosDeConexionEnumeracion(
        "Petición de suscripción");
    public static final EventosDeConexionEnumeracion confirmacionDeSuscripcion = new EventosDeConexionEnumeracion(
        "Confirmación de suscripción");
    public static final EventosDeConexionEnumeracion denegacionDeSuscripcion = new EventosDeConexionEnumeracion(
        "Denegación de suscripción");
    public static final EventosDeConexionEnumeracion peticionDeChat = new EventosDeConexionEnumeracion(
        "Petición de chat");
    public static final EventosDeConexionEnumeracion invitacionAChat = new EventosDeConexionEnumeracion(
        "Invitacion a chat");
    public static final EventosDeConexionEnumeracion peticionDeTransferencia = new EventosDeConexionEnumeracion(
        "Petición de transferencia");
    public static final EventosDeConexionEnumeracion usuarioConectado = new EventosDeConexionEnumeracion(
        "Usuario conectado");
    public static final EventosDeConexionEnumeracion usuarioDesconectado = new EventosDeConexionEnumeracion(
        "Usuario desconectado");

    /**
     * Constructor de la clase. Inicializa el evento de la misma.
     * <p>
     * @param evento El evento de la clase.
     */
    private EventosDeConexionEnumeracion(String evento) {
        this.evento = evento;
    }

    /**
     * Determina si dos instancias de EventosDeConexionEnumeracion son iguales.
     * <p>
     * @param object Una instancia de EventosDeConexionEnumeracion.
     * @return 0 si son iguales, 1 si el parámeto es menor y -1 si es mayor.
     */
    public int compareTo(Object object) {

        if (object instanceof EventosDeConexionEnumeracion) {

            // Se comparan las instancia y se retorna los valores adecuados
            EventosDeConexionEnumeracion instance = (EventosDeConexionEnumeracion) object;

            if (this.evento.compareTo(instance.evento) > 0) {
                return 1;
            }
            else if (this.evento.compareTo(instance.evento) < 0) {
                return -1;
            }
            else {
                return 0;
            }
        }
        else {
            throw new IllegalArgumentException();
        }
    }
}

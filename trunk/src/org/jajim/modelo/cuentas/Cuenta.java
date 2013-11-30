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
package org.jajim.modelo.cuentas;

/**
 * @author Florencio Cañizal Calles
 * @version 1.2 Esta clase guarda la información de alguna de las cuentas utilizadas por el usuario.
 */
public class Cuenta {

    private String identificador;
    private String servidor;
    private String contrasena;
    private boolean cifrada;

    /**
     * Constructor de la cuenta
     */
    public Cuenta() {
    }

    /**
     * Constructor con los parámetros adecuados para inicializar los campos de la cuenta.
     * <p>
     * @param identificador El identificador de la cuenta.
     * @param servidor      El servidor donde está registrada la cuenta.
     * @param contrasena    La contraseña de la cuenta.
     * @param cifrada       Un valor booleano que indica si la contraseña se almaceana cifrada.
     */
    public Cuenta(String identificador, String servidor, String contrasena, boolean cifrada) {
        this.identificador = identificador;
        this.servidor = servidor;
        this.contrasena = contrasena;
        this.cifrada = cifrada;
    }

    /**
     * Retorna el identificador de la cuenta.
     * <p>
     * @return El identificador de la cuenta.
     */
    public String getIdentificador() {
        return identificador;
    }

    /**
     * Modifica el valor del identificador de la cuenta
     * <p>
     * @param identificador El nuevo valor para el identificador.
     */
    public void setIdentificador(String identificador) {
        this.identificador = identificador;
    }

    /**
     * Recupera el servidor donde se encuentra almacenada la cuenta.
     * <p>
     * @return El servidor de almacenamiento.
     */
    public String getServidor() {
        return servidor;
    }

    /**
     * Cambia el valor del servidor de la cuenta.
     * <p>
     * @param servidor el nuevo valor para el servidor.
     */
    public void setServidor(String servidor) {
        this.servidor = servidor;
    }

    /**
     * Recupera el valor de la contraseña de la cuenta.
     * <p>
     * @return La contraseña de la cuenta.
     */
    public String getContrasena() {
        return contrasena;
    }

    /**
     * Modifica el valor de la contraseña de la cuenta.
     * <p>
     * @param contrasena El nuevo valor para la contraseña.
     */
    public void setContrasena(String contrasena) {
        this.contrasena = contrasena;
    }

    /**
     * Recupera la información acerca de si la contraseña se guarda cifrada o no.
     * <p>
     * @return Un valor booleano que indica si la contraseña se almacena cifrada o no.
     */
    public boolean getCifrada() {
        return cifrada;
    }

    /**
     * Modifica el campo cifrada de la cuenta.
     * <p>
     * @param cifrada Nuevo valor para el campo cifrada.
     */
    public void setCifrada(boolean cifrada) {
        this.cifrada = cifrada;
    }
}

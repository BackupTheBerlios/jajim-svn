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

import java.util.ArrayList;
import java.util.List;

/**
 * @author Florencio Cañizal Calles
 * @version 1.2 Clase que representa el contenedor de todas las cuentas del usuario del sitema.
 */
public class Cuentas {

    private final List<Cuenta> cuentas = new ArrayList<>();
    private Cuenta activa;

    /**
     * Constructor de la clase.
     */
    public Cuentas() {
    }

    /**
     * Añade una cuenta a la lista de las cuentas.
     * <p>
     * @param cuenta La nueva cuenta que se desea añadir.
     */
    public void add(Cuenta cuenta) {
        cuentas.add(cuenta);
    }

    /**
     * Devuelve la lista de cuentas del sistema.
     * <p>
     * @return La lista de cuentas.
     */
    public List<Cuenta> getCuentas() {
        return cuentas;
    }

    /**
     * Devuelve la cuenta activa del sistema
     * <p>
     * @return La cuenta activa del sistema
     */
    public Cuenta getActiva() {
        return activa;
    }

    /**
     * Asigna una nueva cuenta activa al sistema.
     * <p>
     * @param activa La nueva cuenta activa.
     */
    public void setActiva(Cuenta activa) {
        this.activa = activa;
    }

    /**
     * Crea una cuenta con la información especificada por los parámetros y la a ñade a la lista de cuentas de la
     * herramienta.
     * <p>
     * @param identificador El identificador de la cuenta.
     * @param servidor      El servidor donde se aloja la cuenta.
     * @param contraseña    La contraseña de acceso de la cuenta.
     * @param cifrada       Valor que indica si la contraseña está cifrada o no.
     * @return Verdadero en caso de ponerse como cuenta activa, falso en caso con trario.
     */
    public boolean añadirCuenta(String identificador, String servidor, String contraseña, boolean cifrada) {

        // Crear un nuevo objeto cuenta
        Cuenta c = new Cuenta(identificador, servidor, contraseña, cifrada);

        // Añadir la cuenta a la lista de cuentas
        this.add(c);

        // Si no hay ninguna cuenta como activa se asigan la nueva como cuenta ac
        // tiva
        if (activa == null) {
            activa = c;
            return true;
        }
        else {
            return false;
        }
    }

    /**
     * Crea una cuenta con la información especificada por los parámetros y la a ñade a la lista de cuentas de la
     * herramienta.
     * <p>
     * @param identificador El identificador de la cuenta.
     * @param servidor      El servidor donde se aloja la cuenta.
     * @return Verdadero en caso de ponerse como cuenta activa, falso en caso con trario.
     */
    public boolean añadirCuenta(String identificador, String servidor) {

        // Crear un nuevo objeto cuenta
        Cuenta c = new Cuenta(identificador, servidor, null, false);

        // Añadir la cuenta a la lista de cuentas
        this.add(c);

        // Si no hay ninguna cuenta como activa se asigan la nueva como cuenta ac
        // tiva
        if (activa == null) {
            activa = c;
            return true;
        }
        else {
            return false;
        }
    }

    /**
     * Devuelve verdadero en caso de existir cuentas asociadas y falso en caso con trario.
     * <p>
     * @return Valor booleano que indica la existencia o no de cuentas.
     */
    public boolean hayCuentas() {

        // Comprobar si el array list está vacío, si es así retornar falso y si
        // no verdadero
        return !cuentas.isEmpty();
    }

    /**
     * Recupera la información de las cuentas y la devuelve al solicitante.
     * <p>
     * @return Una matriz con la informaicón de las cuentas.
     */
    public String[][] getInformacionDeCuentas() {

        String[][] cuentasMatriz = new String[cuentas.size()][4];

        // Iterar sobre las cuentas y extraer la información
        for (int i = 0; i < cuentas.size(); i++) {
            Cuenta c = cuentas.get(i);
            // Conseguir identificador y servidor
            cuentasMatriz[i][0] = c.getIdentificador();
            cuentasMatriz[i][1] = c.getServidor();
            // Conseguir si tiene contraseña almacenada o no
            if (c.getContrasena() == null) {
                cuentasMatriz[i][2] = String.valueOf(false);
            }
            else {
                cuentasMatriz[i][2] = String.valueOf(true);
            }
            // Conseguir si es la cuenta activa o no
            if (c == activa) {
                cuentasMatriz[i][3] = String.valueOf(true);
            }
            else {
                cuentasMatriz[i][3] = String.valueOf(false);
            }
        }

        return cuentasMatriz;
    }

    /**
     * Método que marca a la cuenta especificada como cuenta activa.
     * <p>
     * @param identificador El identificador de la cuenta.
     * @param servidor      El servidor de la cuenta.
     */
    public void cambiarCuenta(String identificador, String servidor) {
        // Buscar la cuenta y asignarla como activa.
        Cuenta c = this.buscarCuenta(identificador, servidor);
        this.setActiva(c);
    }

    /**
     * Método que actualiza el valor de la cuenta cuyo identificador y servidor se corresponden con los suministrados.
     * <p>
     * @param identificador El identificador de la cuenta.
     * @param servidor      El servidor donde está alojada la cuenta.
     * @param contraseña    La nueva contraseña para la cuenta.
     * @param cifrada       Valor que indica si la contraseña está cifrada o no.
     */
    public void modificarContraseña(String identificador, String servidor, String contraseña, boolean cifrada) {

        // Buscar la cuenta en la lista de cuentas
        Cuenta c = buscarCuenta(identificador, servidor);

        // Cambiar la contraseña de la misma
        c.setContrasena(contraseña);
        c.setCifrada(cifrada);
    }

    /**
     * Elimina la contraseña de una cuenta representada por se identificador u el servidor donde está dada de alta.
     * <p>
     * @param identificador El identificador de la cuenta.
     * @param servidor      El servidor donde está almacenada.
     */
    public void borrarContraseña(String identificador, String servidor) {

        // Buscar la cuenta en la lista de cuentas
        Cuenta c = buscarCuenta(identificador, servidor);

        // Borrar la contraseña de la misma
        c.setContrasena(null);
    }

    /**
     * Elima la cuena del sistema, pero no del servidor asociado.
     * <p>
     * @param identificador El identificador de la cuenta.
     * @param servidor      El servidor donde se aloja la cuenta.
     */
    public void eliminarCuentaSistema(String identificador, String servidor) {

        // Buscar la cuenta en la lista de cuentas
        Cuenta c = buscarCuenta(identificador, servidor);

        // Si es la cuenta activa, borrarla y marcar a otra como activa
        if (c == activa) {
            if (cuentas.size() > 1) {
                // Marcar la primera que se encuentre
                for (int i = 0; i < cuentas.size(); i++) {
                    Cuenta aux = cuentas.get(i);
                    if (aux != c) {
                        activa = aux;
                        break;
                    }
                }
            }
            else {
                activa = null;
            }
        }

        // Eliminarla de la lista de cuentas
        cuentas.remove(c);
    }

    /**
     * Recupera una contraseña del sistema en función del identificador y servidor de la misma.
     * <p>
     * @param identificador El identificador de la cuenta.
     * @param servidor      El servidor en el que está alojada.
     * @return La contraseña de la aplicación.
     */
    public String getContraseña(String identificador, String servidor) {

        // Buscar la cuenta
        Cuenta c = buscarCuenta(identificador, servidor);

        // Devolver la contraseña
        return c.getContrasena();
    }

    /**
     * Informa de si la contraseña de la cuenta representada por los valores pro porcionados se almacena cifrada o sin
     * cifrar.
     * <p>
     * @param identificador El identificador de la cuenta.
     * @param servidor      El servidor donde está alojada la cuenta.
     * @return true si está cifrada, false en caso contrario.
     */
    public boolean isCifrada(String identificador, String servidor) {

        // Buscar la cuenta
        Cuenta c = this.buscarCuenta(identificador, servidor);

        // Devolver si está cifrada o no
        return c.getCifrada();
    }

    /**
     * Si la cuenta ya pertenece al sistema retorna verdadero, sino retorna falso.
     * <p>
     * @param identificador El identificador de la cuenta que se va a comprobar.
     * @param servidor      El servidor de la cuenta que se va a comprobar.
     * @return Verdadero si la cuenta pertenece al sistema, falso en caso contrario.
     */
    public boolean isPresente(String identificador, String servidor) {

        // Buscar la cuenta
        Cuenta c = this.buscarCuenta(identificador, servidor);

        // Devuelve si la cuenta existe en el sistema o no.
        return (c != null);
    }

    /**
     * Método que retorna la cuenta que se corresponde con el identificador y el servidor suministrados.
     * <p>
     * @param identificador El identificador de la cuenta.
     * @param servidor      El servidor de la cuenta.
     * @return La cuenta que cumple con los requisitos mencionados.
     */
    private Cuenta buscarCuenta(String identificador, String servidor) {

        Cuenta c;

        for (int i = 0; i < cuentas.size(); i++) {
            c = cuentas.get(i);
            if (c.getIdentificador().compareTo(identificador) == 0 && c.getServidor().compareTo(servidor) == 0) {
                return c;
            }
        }

        return null;
    }
}

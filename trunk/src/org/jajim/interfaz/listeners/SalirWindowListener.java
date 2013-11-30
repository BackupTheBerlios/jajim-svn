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
package org.jajim.interfaz.listeners;

import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import org.jajim.controladores.CuentaControlador;

/**
 * @author Florencio Cañizal Calles
 * @version 1.2 Realiza todas las acciones necesarias para cerrar correctamente la aplicación: guardado de cuentas de
 * usuario.
 */
public class SalirWindowListener extends WindowAdapter {

    /**
     * Constructor de la clase. Inicializa los campos de la misma.
     */
    public SalirWindowListener() {
    }

    /**
     * Método que se ejecuta cuando cierra la ventana. Guarda toda la información necesaria para la aplicación.
     * <p>
     * @param e Evento que produjo la activación del método.
     */
    @Override
    public void windowClosing(WindowEvent e) {

        // Guardar las cuentas del usuario
        CuentaControlador cc = CuentaControlador.getInstancia();
        cc.guardarCuentas();
        System.exit(0);
    }
}

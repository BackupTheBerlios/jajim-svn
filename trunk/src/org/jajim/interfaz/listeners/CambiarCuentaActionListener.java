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

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import org.jajim.controladores.CuentaControlador;
import org.jajim.interfaz.ventanas.VentanaGestorDeCuentas;
import org.jajim.interfaz.ventanas.VentanaPrincipal;

/**
 * @author Florencio Cañizal Calles
 * @version 1.2 Clase oyente que se activa cuando se selecciona la opción de activar cuenta.
 */
public class CambiarCuentaActionListener implements ActionListener {

    /**
     * Constructor de la clase. Inicializa las variables importantes.
     */
    public CambiarCuentaActionListener() {
    }

    /**
     * Método que se ejecuta cuando se selecciona la opción de activar una cuenta. Establece dicha cuenta como
     * predeterinada.
     * <p>
     * @param e El evento que produce la activación del método.
     */
    @Override
    public void actionPerformed(ActionEvent e) {

        // Recuperar la cuenta que se va a activar
        VentanaPrincipal vp = VentanaPrincipal.getInstancia();
        VentanaGestorDeCuentas vgc = VentanaGestorDeCuentas.getInstancia();
        String cuenta[] = vgc.getCuenta();

        // Si devuelve null, no hay cuentas y se cierra la ejecución del método
        if (cuenta == null) {
            return;
        }

        // Extraer los valores
        String identificador = cuenta[0];
        String servidor = cuenta[1];

        // Si ya estaba activa, se cancela la activación
        CuentaControlador cc = CuentaControlador.getInstancia();
        String comprobar = identificador + "@" + servidor;
        if (comprobar.compareTo(cc.getCuenta()) == 0) {
            return;
        }

        // Abortar la conexión si es necesario
        AbortarOperaciones ao = new AbortarOperaciones(vgc);

        if (!ao.abortarConexion()) {
            return;
        }

        // Llamar al controlador para que realice la operación
        cc.cambiarCuenta(identificador, servidor);

        // Llamar a la ventana para que actualice la información
        vp.cambiarCuenta(identificador + "@" + servidor);
    }
}

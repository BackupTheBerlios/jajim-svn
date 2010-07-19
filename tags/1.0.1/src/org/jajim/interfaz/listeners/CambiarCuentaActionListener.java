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

import org.jajim.controladores.ConexionControlador;
import org.jajim.controladores.CuentaControlador;
import org.jajim.interfaz.ventanas.VentanaGestorDeCuentas;
import org.jajim.interfaz.ventanas.VentanaPrincipal;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * @author Florencio Cañizal Calles
 * @version 1.0.1
 * Clase oyente que se activa cuando se selecciona la opción de activar cuenta.
 */
public class CambiarCuentaActionListener implements ActionListener{

    // Variables importantes
    private VentanaGestorDeCuentas vgt;
    private VentanaPrincipal vp;
    private CuentaControlador cc;
    private ConexionControlador cnc;

    /**
     * Constructor de la clase. Inicializa las variables importantes.
     * @param vgt El gestor de las cuentas del sistema.
     * @param vp Ventana principal de la aplicación.
     * @param cc Controlador de las cuentas.
     */
    public CambiarCuentaActionListener(VentanaGestorDeCuentas vgt,VentanaPrincipal vp,CuentaControlador cc,ConexionControlador cnc){
        this.vgt = vgt;
        this.vp = vp;
        this.cc = cc;
        this.cnc = cnc;
    }

    /**
     * Método que se ejecuta cuando se selecciona la opción de activar una cuenta.
     * Establece dicha cuenta como predeterinada.
     * @param e El evento que produce la activación del método.
     */
    @Override
    public void actionPerformed(ActionEvent e) {

        // Recuperar la cuenta que se va a activar
        String cuenta[] = vgt.getCuenta();

        // Si devuelve null, no hay cuentas y se cierra la ejecución del método
        if(cuenta == null)
            return;

        // Extraer los valores
        String identificador = cuenta[0];
        String servidor = cuenta[1];

        // Si ya estaba activa, se cancela la activación
        String comprobar = identificador + "@" + servidor;
        if(comprobar.compareTo(cc.getCuenta()) == 0){
            return;
        }

        // Abortar la conexión si es necesario
        AbortarOperaciones ao = new AbortarOperaciones(vgt,vp,cnc,vp.getVgt());

        if(!ao.abortarConexion())
            return;

        // Llamar al controlador para que realice la operación
        cc.cambiarCuenta(identificador,servidor);

        // Llamar a la ventana para que actualice la información
        vp.cambiarCuenta(identificador + "@" + servidor);
    }
}

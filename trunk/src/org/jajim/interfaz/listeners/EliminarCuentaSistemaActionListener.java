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
import org.jajim.controladores.ConexionControlador;
import org.jajim.controladores.CuentaControlador;
import org.jajim.interfaz.ventanas.VentanaGestorDeCuentas;
import org.jajim.interfaz.ventanas.VentanaPrincipal;

/**
 * @author Florencio Cañizal Calles
 * @version 1.2
 * Clase oyente que se ejecuta cuando se desea eliminar una cuenta del sistema.
 */
public class EliminarCuentaSistemaActionListener implements ActionListener{

    // Variables importantes
    private VentanaGestorDeCuentas vgc;

    /**
     * Constructor de la clase. Inicializa las variables adecuadas.
     * @param vgc El gestor de las cuentas del sistema.
     */
    public EliminarCuentaSistemaActionListener(VentanaGestorDeCuentas vgc){
        this.vgc = vgc;
    }

    /**
     * Método que se ejecuta cuando se selecciona la opción de eliminar cuenta
     * del sistema. Invita al controlador de las cuentas a eliminar la misma del
     * sistema.
     * @param e El evento que produce la ejecución del método.
     */
    @Override
    public void actionPerformed(ActionEvent e) {

        VentanaPrincipal vp = VentanaPrincipal.getInstancia();
        
        // Recuperar la información de la cuenta
        String[] cuenta = vgc.getCuenta();

        // Si devuelve null, no hay cuentas y se cierra la ejecución del método
        if(cuenta == null) {
            return;
        }

        // Extraer los valores
        String identificador = cuenta[0];
        String servidor = cuenta[1];

        // Comprobar si se va a eliminar la cuenta activa y estamos conectados.
        ConexionControlador cnc = ConexionControlador.getInstancia();
        CuentaControlador cc = CuentaControlador.getInstancia();
        String activa = cc.getCuenta();
        if(activa.compareTo(identificador + "@" + servidor) == 0 && cnc.isConectado()){
            // Abortar la conexión antes de borrar la cuenta
            AbortarOperaciones ao = new AbortarOperaciones(vgc, vp.getVgt());
            if(!ao.abortarConexion()) {
                return;
            }
        }

        // Llamar al controlador para que realice la operación
        cc.eliminarCuentaSistema(identificador,servidor);

        // Actualiza la lista del gestor
        vgc.añadirCuentas();
        activa = cc.getCuenta();
        vp.cambiarCuenta(activa);
    }
}

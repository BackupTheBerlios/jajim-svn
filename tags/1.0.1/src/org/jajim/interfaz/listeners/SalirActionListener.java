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

import org.jajim.controladores.CuentaControlador;
import org.jajim.interfaz.ventanas.VentanaPrincipal;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * @author Florencio Cañizal Calles
 * @version 1.0.1
 * Clase que se ejecuta cuando el usuario selecciona la opción Salir de la herra
 * mienta.
 */
public class SalirActionListener implements ActionListener{

    private VentanaPrincipal vp;

    /**
     * Constructor de la clase. Inicializa las variables necesarias.
     * @param vp La ventana principal de la aplicación.
     */
    public SalirActionListener(VentanaPrincipal vp){
        this.vp = vp;
    }

    /**
     * Método que se ejecuta cuando el usuario selecciona la opción Salir de la
     * herramienta. Realiza las operaciones necesarias.
     * @param e El evento que produce la ejecución del método.
     */
    @Override
    public void actionPerformed(ActionEvent e){

        // Recupera el gestor de cuentas y guarda las cuentas en el sistema
        CuentaControlador cc = vp.getCc();
        cc.guardarCuentas();
        System.exit(0);
    }
}

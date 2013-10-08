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
import org.jajim.interfaz.ventanas.VentanaGestorDeCuentas;
import org.jajim.interfaz.ventanas.VentanaPrincipal;

/**
 * @author Florencio Cañizal Calles
 * @version 1.2
 * Clase oyente que lanza el gestor de cuentas, cuando el usuario selecciona la
 * opción de abrirlo.
 */
public class LanzarGestorDeCuentasActionListener implements ActionListener{

    /**
     * Constructor de la clase. Inicializa las variables necesarias.
     */
    public LanzarGestorDeCuentasActionListener(){
    }

    /**
     * Método que se ejecuta cuando el usuario selecciona la opción de abrir el
     * gestor de cuentas. Se lanza la ventana que representa al mismo.
     * @param e El evento que genera la ejecución de método.
     */
    @Override
    public void actionPerformed(ActionEvent e){

        // Recuperar la ventana del gestor de cuentas
        VentanaGestorDeCuentas vgc = VentanaPrincipal.getInstancia().getVgc();

        // Hacerla visible
        vgc.hacerVisible();
    }
}

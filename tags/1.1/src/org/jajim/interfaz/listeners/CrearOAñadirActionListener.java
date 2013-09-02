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
import org.jajim.interfaz.dialogos.CrearOAñadirFormulario;
import org.jajim.interfaz.ventanas.VentanaGestorDeCuentas;
import org.jajim.interfaz.ventanas.VentanaPrincipal;

/**
 * @author Florencio Cañizal Calles
 * @version 1.1
 * Oyente que se ejecuta cuando se selecciona la opción de crear o añadir una
 * nueva cuenta al sistema.
 */
public class CrearOAñadirActionListener implements ActionListener{

    // Elementos importantes
    private VentanaPrincipal vp;
    private VentanaGestorDeCuentas vgc;

    /**
     * Constructor de la clase. Iniciliza las variables adecuadas.
     * @param vp Ventana principal de la aplicación.
     */
    public CrearOAñadirActionListener(VentanaGestorDeCuentas vgc,VentanaPrincipal vp){
        this.vp = vp;
        this.vgc = vgc;
    }

    /**
     * Método que se ejecuta cuando se debe crear o añadir una nueva cuenta al sis
     * tema. Se ejecuta el cuadro de diálogo para seleccionar la acción correspondiente.
     * @param e El evento que genera la ejecución del método.
     */
    @Override
    public void actionPerformed(ActionEvent e){
        new CrearOAñadirFormulario(vp,vgc);
    }
}

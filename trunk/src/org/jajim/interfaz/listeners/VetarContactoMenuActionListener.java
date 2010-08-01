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

import org.jajim.interfaz.dialogos.VetarContactoFormulario;
import org.jajim.interfaz.ventanas.VentanaConversacion;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import org.jajim.interfaz.ventanas.VentanaConversacionNueva;

/**
 * @author Florencio Cañizal Calles
 * @version 1.1
 * Clase oyente que escucha los eventos de rechazo de conexión provenientes del
 * menú de la conversación.
 */
public class VetarContactoMenuActionListener implements ActionListener{

    private VentanaConversacion vc;

    /**
     * Constructor de la clase. Iniciliza las variables necesarias.
     * @param vc La ventana de la conversación.
     */
    public VetarContactoMenuActionListener(VentanaConversacion vc){
        this.vc = vc;
    }

    /**
     * Constructor de la clase. Iniciliza las variables necesarias.
     * @param vc La ventana de la conversación.
     */
    public VetarContactoMenuActionListener(VentanaConversacionNueva vc){
        //this.vc = vc;
    }

    /**
     * Método que se ejecuta cuando se seleccion la  opción de vetar un contacto
     * presente en el menú de la ventana de la conversación. Lanza el formulario
     * adecuado para introducir los contactos a vetar.
     * @param e
     */
    @Override
    public void actionPerformed(ActionEvent e) {

        // Lanzar el formulario
        new VetarContactoFormulario(vc);
    }
}

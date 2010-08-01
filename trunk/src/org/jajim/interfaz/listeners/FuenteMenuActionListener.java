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

import org.jajim.interfaz.dialogos.FuenteFormulario;
import org.jajim.interfaz.ventanas.VentanaConversacion;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import org.jajim.interfaz.ventanas.VentanaConversacionNueva;

/**
 * @author Florencio Cañizal Calles
 * @version 1.1
 * Clase oyente que escucha los eventos de modificación de fuente provenientes de
 * el menú de estilo de la ventana de la conversación.
 */
public class FuenteMenuActionListener implements ActionListener{

    private VentanaConversacion vc;

    /**
     * Constructor de la clase. Inicializa las variables necesarias.
     * @param vc La ventana de la conversación actual.
     */
    public FuenteMenuActionListener(VentanaConversacion vc){
        this.vc = vc;
    }

    /**
     * Constructor de la clase. Inicializa las variables necesarias.
     * @param vc La ventana de la conversación actual.
     */
    public FuenteMenuActionListener(VentanaConversacionNueva vc){
        //this.vc = vc;
    }

    /**
     * Método que se ejecuta cuando el usuario selecciona la opción para cambiar
     * la fuente del menú de estilos de la ventana de la conversación. Lanza un
     * formulario para cambiar la fuente.
     * @param e El evento que produce la ejecución del método.
     */
    @Override
    public void actionPerformed(ActionEvent e){
        new FuenteFormulario(vc);
    }
}

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
import org.jajim.interfaz.dialogos.SolicitudDeContactoFormulario;
import org.jajim.interfaz.ventanas.VentanaPrincipal;

/**
 * @author Florencio Cañizal Calles
 * @version 1.1
 * Clase oyente que se ejecuta cuando el usuario selecciona la opción de Añadir
 * contacto. Crea un cuadro de diálogo para introducir los valores del contacto.
 */
public class AñadirContactoMenuActionListener implements ActionListener{

    private VentanaPrincipal vp;

    /**
     * Constructor de la clase. Inicializa los campos de la misma.
     * @param vp Ventana principal de la aplicación.
     */
    public AñadirContactoMenuActionListener(VentanaPrincipal vp){
        this.vp = vp;
    }

    /**
     * Método que se ejecuta cuando se selecciona la opción de añadir contacto.
     * Crear un formulario para introducir los datos.
     * @param e El evento que originó la ejecución del método.
     */
    @Override
    public void actionPerformed(ActionEvent e){
        new SolicitudDeContactoFormulario(vp);
    }
}

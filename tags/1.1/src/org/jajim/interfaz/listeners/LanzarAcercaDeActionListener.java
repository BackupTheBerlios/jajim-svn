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
import org.jajim.interfaz.dialogos.AcercaDeFormulario;
import org.jajim.interfaz.ventanas.VentanaPrincipal;

/**
 * @author Florencio Cañizal Calles
 * @version 1.1
 * Clase oyente que escucha los eventos de petición de información acerca de la
 * herramienta. Lanza el formulario apropiado para visualizar dicha información
 */
public class LanzarAcercaDeActionListener implements ActionListener{

    private VentanaPrincipal vp;

    /**
     * Constructor de la clase. Inicializa las variables necesarias.
     * @param vp La ventana principal de la aplicación.
     */
    public LanzarAcercaDeActionListener(VentanaPrincipal vp){
        this.vp = vp;
    }

    /**
     * Método de la interfaz ActionListener. Se ejecuta cuando el usuario selecci
     * ona la opción "Acerca de" de la ventana principal. Muestra un cuadro de
     * diálogo con la información de la aplicación
     * @param arg0 El evento que produce la ejecución del método.
     */
    @Override
    public void actionPerformed(ActionEvent arg0) {
        new AcercaDeFormulario(vp);
    }
}

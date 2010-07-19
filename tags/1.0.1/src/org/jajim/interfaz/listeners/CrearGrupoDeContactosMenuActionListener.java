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

import org.jajim.interfaz.dialogos.CrearGrupoDeContactosFormulario;
import org.jajim.interfaz.ventanas.VentanaPrincipal;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * @author Florencio Cañizal Calles
 * @version 1.0.1
 * Clase oyente que escucha los eventos procedentes de la opción de crear un nuevo
 * grupo activa en el menú de la aplicación.
 */
public class CrearGrupoDeContactosMenuActionListener implements ActionListener{

    private VentanaPrincipal vp;

    /**
     * Constructor de la clase. Inicializa las varibles necesarias.
     * @param vp Ventana principal de la aplicación.
     */
    public CrearGrupoDeContactosMenuActionListener(VentanaPrincipal vp){
        this.vp = vp;
    }

    /**
     * Método que se ejecuta cuando se selecciona la opción de crear un nuevo grupo
     * en el menú de la aplicación. Lanza el cuadro de diálogo de creación de grupo
     * para introducir los valores necesarios.
     * @param e El evento que produce la ejecución del método.
     */
    @Override
    public void actionPerformed(ActionEvent e) {
        new CrearGrupoDeContactosFormulario(vp);
    }
}

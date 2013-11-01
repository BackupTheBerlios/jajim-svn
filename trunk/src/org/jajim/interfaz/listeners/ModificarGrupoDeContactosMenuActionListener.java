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
import org.jajim.interfaz.dialogos.ModificarGrupoDeContactosFormulario;
import org.jajim.interfaz.utilidades.PanelContactos;

/**
 * @author Florencio Cañizal Calles
 * @version 1.2
 * Clase oyente que escucha los eventos de modificación de grupo provenientes de
 * el menú popup de los grupos.
 */
public class ModificarGrupoDeContactosMenuActionListener implements ActionListener{

    /**
     * Constructor de la clase. Inicializa las variables necesarias.
     */
    public ModificarGrupoDeContactosMenuActionListener(){
    }

    /**
     * Método que se ejecuta cuando se selecciona la opción Modificar grupo del
     * menú popup de los grupos. Lanza el cuadro de diálogo en el que el usuario
     * puede modificar el grupo.
     * @param e El evento que produce la ejecución del método.
     */
    @Override
    public void actionPerformed(ActionEvent e) {

        // Cerrar el menú popup
        PanelContactos.getInstancia().cerrarPopupGrupos();

        // Lanzar un formulario para introducir los cambios
        String grupo = e.getActionCommand();
        new ModificarGrupoDeContactosFormulario(grupo);
    }
}

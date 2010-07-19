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

import org.jajim.interfaz.dialogos.ModificarGrupoDeContactosFormulario;
import org.jajim.interfaz.utilidades.PanelContactos;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * @author Florencio Cañizal Calles
 * @version 1.0.1
 * Clase oyente que escucha los eventos de modificación de grupo provenientes de
 * el menú popup de los grupos.
 */
public class ModificarGrupoDeContactosMenuActionListener implements ActionListener{

    private PanelContactos pc;

    /**
     * Constructor de la clase. Inicializa las variables necesarias.
     * @param pc El panel de los contactos de la ventana principal.
     */
    public ModificarGrupoDeContactosMenuActionListener(PanelContactos pc){
        this.pc = pc;
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
        pc.cerrarPopupGrupos();

        // Lanzar un formulario para introducir los cambios
        String grupo = e.getActionCommand();
        new ModificarGrupoDeContactosFormulario(pc,grupo);
    }
}

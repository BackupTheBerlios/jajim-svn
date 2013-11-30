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
import org.jajim.interfaz.dialogos.AñadirContactoAGrupoFormulario;
import org.jajim.interfaz.utilidades.PanelContactos;

/**
 * @author Florencio Cañizal Calles
 * @version 1.2 Clase oyente que escucha los eventos de adición de contacto a un grupo proce+ dentes de el menu popup de
 * los contactos.
 */
public class AñadirContactoAGrupoMenuActionListener implements ActionListener {

    /**
     * Constructor de la clase. Inicializa las variables necesarias.
     */
    public AñadirContactoAGrupoMenuActionListener() {
    }

    /**
     * Método que se ejecuta cuando el usuario selecciona la opción de añadir con tacto a un grupo. Lanza un formulario
     * en el que el usuario introduce los grupos a los que desea añadir el contacto.
     * <p>
     * @param e El evento que produce la ejecución del método.
     */
    @Override
    public void actionPerformed(ActionEvent e) {

        // Cerrar el menu popup
        PanelContactos.getInstancia().cerrarPopupContactos();

        // Recuperar el contacto y lanzar el formulario de selección
        String contacto = e.getActionCommand();
        new AñadirContactoAGrupoFormulario(contacto);
    }
}

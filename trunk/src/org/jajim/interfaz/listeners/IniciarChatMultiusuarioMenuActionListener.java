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
import org.jajim.interfaz.dialogos.IniciarChatMultiusuarioFormulario;
import org.jajim.interfaz.utilidades.PanelContactos;

/**
 * @author Florencio Cañizal Calles
 * @version 1.1
 * Clase oyente que se escucha eventos procedentes de la opción de crear un nuevo
 * chat multiusuario.
 */
public class IniciarChatMultiusuarioMenuActionListener implements ActionListener{

    private PanelContactos pc;

    /**
     * Constructor de la clase. Inicializa las variables necesarias.
     * @param pc El panel de contactos de la aplicación.
     */
    public IniciarChatMultiusuarioMenuActionListener(PanelContactos pc){
        this.pc = pc;
    }

    /**
     * Método que se ejecuta cuando el usuario selecciona la opción de crear un
     * chat multiusuario de la herramienta. Lanza un formulario en el que el usua
     * rio introduce sus datos para el multichat.
     * @param e El evento que produce la ejecución del método.
     */
    @Override
    public void actionPerformed(ActionEvent e) {

        // Cerrar el menú popup del panel de contactos
        pc.cerrarPopupContactos();

        // Arrancar un formulario para que el usuario introduzca el nombre de la
        // sala y el nickname
        String contacto = e.getActionCommand();
        new IniciarChatMultiusuarioFormulario(contacto);
    }
}

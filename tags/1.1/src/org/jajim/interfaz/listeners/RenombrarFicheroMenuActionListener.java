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
import javax.swing.JTable;
import org.jajim.interfaz.dialogos.RenombrarFicheroFormulario;
import org.jajim.interfaz.ventanas.VentanaGestorDeTransferencias;

/**
 * @author Florencio Cañizal Calles
 * @version 1.1
 * Clase oyente que escucha los eventos de renombrado de fichero procedentes de la
 * barra de herramientas de la ventana de gestión de transferencias.
 */
public class RenombrarFicheroMenuActionListener implements ActionListener{

    private VentanaGestorDeTransferencias vgt;

    /**
     * Consructor de la clase. Iniciliza las variables necesarias.
     * @param vgt La ventana del gestor de transferencias.
     */
    public RenombrarFicheroMenuActionListener(VentanaGestorDeTransferencias vgt){
        this.vgt = vgt;
    }

    /**
     * Método que se ejecuta cuando el usuario selecciona la opción de Renombrar
     * fichero disponible en la barra de herramientas del gestor de transferencias.
     * Lanza un cuadro de diálogo para que el usuario introduzca el nuevo nombre
     * para el fichero.
     * @param e El evento que produce la ejecución del método.
     */
    @Override
    public void actionPerformed(ActionEvent e) {

        // Recuperar la tabla de ficheros descargados
        JTable tablaDeFicheros = vgt.getTablaDeFicheros();

        // Recuperar el fichero seleccionado, si no hay ninguno se aborta la ope
        // ración.
        int filaSeleccionada = tablaDeFicheros.getSelectedRow();
        if(filaSeleccionada == -1)
            return;

        // Lanzar el formulario
        new RenombrarFicheroFormulario(vgt,filaSeleccionada);
    }
}

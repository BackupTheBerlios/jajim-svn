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

import org.jajim.controladores.TransferenciaFicherosControlador;
import org.jajim.excepciones.ImposibleVisualizarFicheroException;
import org.jajim.interfaz.dialogos.MensajeError;
import org.jajim.interfaz.ventanas.VentanaGestorDeTransferencias;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;

/**
 * @author Florencio Cañizal Calles
 * @version 1.1
 * Clase oyente que escucha los eventos de visualización de fichero procedentes
 * de la barra de acciones del gestor de ventanas.
 */
public class VisualizarFicheroActionListener implements ActionListener{

    private VentanaGestorDeTransferencias vgt;

    /**
     * Constructor de la clase. Iniciliza las variables necesarias.
     * @param vgt La ventana del gestor de cuentas.
     */
    public VisualizarFicheroActionListener(VentanaGestorDeTransferencias vgt){
        this.vgt = vgt;
    }

    /**
     * Método que se ejecuta cuando el usuario selecciona la opción de visualizar
     * el documento, disponible en la barra de acciones del gestor de transferen
     * cias. LLama al controlador para poder visualizar el fichero.
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

        DefaultTableModel dtm = (DefaultTableModel) tablaDeFicheros.getModel();
        String nombre = (String) dtm.getValueAt(filaSeleccionada,0);
        String ruta = (String) dtm.getValueAt(filaSeleccionada,1);

        // Llamar al controlador para que realice la operación.
        TransferenciaFicherosControlador tfc = TransferenciaFicherosControlador.getInstancia();
        try{
            tfc.visualizarFichero(nombre,ruta);
        }catch(ImposibleVisualizarFicheroException ivfe){
            new MensajeError(vgt,"imposible_visualizar_fichero_error",MensajeError.ERR);
        }
    }
}

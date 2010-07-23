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
import org.jajim.excepciones.ImposibleBorrarFicheroException;
import org.jajim.interfaz.dialogos.MensajeError;
import org.jajim.interfaz.ventanas.VentanaGestorDeTransferencias;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;

/**
 * @author Florencio Cañizal Calles
 * @version 1.1
 * Clase oyente que escucha los eventos de borrado de ficheros procedentes de la
 * ventana del gestor de transferencia de ficheros.
 */
public class BorrarFicheroActionListener implements ActionListener{

    private VentanaGestorDeTransferencias vgt;

    /**
     * Constructor de la clase. Inicializa las variables necesarias.
     * @param vgt La ventana del gestor de cuentas.
     */
    public BorrarFicheroActionListener(VentanaGestorDeTransferencias vgt){
        this.vgt = vgt;
    }

    /**
     * Método que se ejecuta cuando el usuario selecciona la opción Borrar fichero
     * de la ventana del gestor de transferencias. Borra el fichero del sistema y
     * de la tabla de ficheros recibidos.
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
            tfc.borrarFichero(nombre,ruta);
        }catch(ImposibleBorrarFicheroException ibfe){
            new MensajeError(vgt,"imposible_borrar_fichero_error",MensajeError.ERR);
        }
        // Borrar el fichero de la lista de descargas
        dtm.removeRow(filaSeleccionada);
        tablaDeFicheros.setModel(dtm);
    }
}

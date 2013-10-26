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
import javax.swing.table.DefaultTableModel;
import org.jajim.controladores.TransferenciaFicherosControlador;
import org.jajim.excepciones.ImposibleReubicarFicheroException;
import org.jajim.interfaz.dialogos.MensajeError;
import org.jajim.interfaz.dialogos.ReubicarFicheroFormulario;
import org.jajim.interfaz.ventanas.VentanaGestorDeTransferencias;

/**
 * @author Florencio Cañizal Calles
 * @version 1.2
 * Clase oyente que escucha los eventos para mover un fichero procedentes del for
 * mulario habilitado para introducir la nueva ruta del fichero.
 */
public class ReubicarFicheroActionListener implements ActionListener{

    private ReubicarFicheroFormulario rff;
    private int filaSeleccionada;

    /**
     * Constructor de la clase. Iniciliza las variables necesarias.
     * @param rff El formulario habilitado para reubicar el fichero.
     * @param filaSeleccionada La fila seleccionada por el usuario.
     */
    public ReubicarFicheroActionListener(ReubicarFicheroFormulario rff, int filaSeleccionada){
        this.rff = rff;
        this.filaSeleccionada = filaSeleccionada;
    }

    /**
     * Método que se ejecuta cuando el usuario selecciona la opción Aceptar del
     * formulario de reubicación de ficheros.
     * @param e El evento que produce la ejecución del método.
     */
    @Override
    public void actionPerformed(ActionEvent e) {

        // Recuperar la tabla de ficheros descargados
        JTable tablaDeFicheros = VentanaGestorDeTransferencias.getInstancia().getTablaDeFicheros();

        // Recuperar el nombre y la ruta del fichero
        DefaultTableModel dtm = (DefaultTableModel) tablaDeFicheros.getModel();
        String nombre = (String) dtm.getValueAt(filaSeleccionada,0);
        String ruta = (String) dtm.getValueAt(filaSeleccionada,1);

        // Recuperar el nuevo nombre intorducido por el usuario.
        String[] campos = rff.getCampos();
        String nuevaRuta = campos[0];

        if(nuevaRuta.compareTo("") == 0){
            new MensajeError(rff,"campos_invalidos_error",MensajeError.WARNING);
            return;
        }

        try{
            // Llamar al controlador para que realice la operación.
            TransferenciaFicherosControlador tfc = TransferenciaFicherosControlador.getInstancia();
            tfc.reubicarFichero(nombre,ruta,nuevaRuta);

            // Cerrar el cuadro de diálogo y actualizar la interfaz
            rff.dispose();
            dtm.setValueAt(nuevaRuta,filaSeleccionada,1);
        }catch(ImposibleReubicarFicheroException irfe){
            new MensajeError(rff,"imposible_reubicar_fichero_error",MensajeError.ERR);
        }
    }
}

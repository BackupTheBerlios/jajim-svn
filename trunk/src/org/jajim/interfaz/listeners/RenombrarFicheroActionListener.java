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
import org.jajim.excepciones.ImposibleRenombrarFicheroException;
import org.jajim.interfaz.dialogos.MensajeError;
import org.jajim.interfaz.dialogos.RenombrarFicheroFormulario;
import org.jajim.interfaz.ventanas.VentanaGestorDeTransferencias;

/**
 * @author Florencio Cañizal Calles
 * @version 1.2 Clase oyente que escucha los eventos de renombrado de fichero procedentes del formulario habilitado para
 * introducir el nuevo nombre del fichero.
 */
public class RenombrarFicheroActionListener implements ActionListener {

    private final RenombrarFicheroFormulario rff;
    private final int filaSeleccionada;

    /**
     * Constuctor de la clase. Inicializa las varibles necesarias.
     * <p>
     * @param rff              El formulario en el cual se introduce el nuevo nombre.
     * @param filaSeleccionada El número de fila seleccionada por el usuario.
     */
    public RenombrarFicheroActionListener(RenombrarFicheroFormulario rff, int filaSeleccionada) {
        this.rff = rff;
        this.filaSeleccionada = filaSeleccionada;
    }

    /**
     * Método que se ejecuta cuando el usuario selecciona la opción Aceptar del formulario de renombrado de fichero.
     * Llama al constructor para llevar a ca bo la operación y actualiza la tabla de ficheros descargados.
     * <p>
     * @param e El evento que produce la ejecución del método.
     */
    @Override
    public void actionPerformed(ActionEvent e) {

        // Recuperar la tabla de ficheros descargados
        JTable tablaDeFicheros = VentanaGestorDeTransferencias.getInstancia().getTablaDeFicheros();

        // Recuperar el nombre y la ruta del fichero
        DefaultTableModel dtm = (DefaultTableModel) tablaDeFicheros.getModel();
        String nombre = (String) dtm.getValueAt(filaSeleccionada, 0);
        String ruta = (String) dtm.getValueAt(filaSeleccionada, 1);

        // Recuperar el nuevo nombre intorducido por el usuario.
        String[] campos = rff.getCampos();
        String nuevoNombre = campos[0];

        if (nuevoNombre.compareTo("") == 0) {
            new MensajeError(rff, "campos_invalidos_error", MensajeError.WARNING);
            return;
        }

        try {
            // Llamar al controlador para que realice la operación.
            TransferenciaFicherosControlador tfc = TransferenciaFicherosControlador.getInstancia();
            tfc.renombrarFichero(nombre, ruta, nuevoNombre);

            // Cerrar el cuadro de diálogo y actualizar la interfaz
            rff.dispose();
            dtm.setValueAt(nuevoNombre, filaSeleccionada, 0);
        }
        catch (ImposibleRenombrarFicheroException irfe) {
            new MensajeError(rff, "imposible_renombrar_fichero_error", MensajeError.ERR);
        }
    }
}

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
import javax.swing.JDialog;
import org.jajim.controladores.TransferenciaFicherosControlador;

/**
 * @author Florencio Cañizal Calles
 * @version 1.1
 * Clase oyente que reacciona cuando se desea rechazar la transferencia de un fi
 * chero. Delega en el controlador de las transferencias dicha operación.
 */
public class RechazarFicheroActionListener implements ActionListener{

    private JDialog aorff;
    private int idTransferencia;

    /**
     * Constructor de la clase. Inicializa las variables necesarias.
     * @param aorff El formulario desde el que se generará el rechazo de la trans
     * ferencia.
     * @param idTransferencia El identificador de la transferencia.
     */
    public RechazarFicheroActionListener(JDialog aorff,int idTransferencia){
        this.aorff = aorff;
        this.idTransferencia = idTransferencia;
    }

    /**
     * Método que se ejcuta cuando el usuario rehaza una trasnferencia de fichero.
     * Utiliza el controlador de las transferencias para llevar a cabo la operaci
     * ón.
     * @param e El evento que produce la ejecución del método.
     */
    @Override
    public void actionPerformed(ActionEvent e) {

        // LLamar al controlador para que rechace la transferencia.
        TransferenciaFicherosControlador tfc = TransferenciaFicherosControlador.getInstancia();
        tfc.rechazarFichero(idTransferencia);

        // Cerrar el cuadro de diálogo
        aorff.dispose();
    }
}

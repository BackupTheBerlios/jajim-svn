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
import org.jajim.interfaz.ventanas.VentanaGestorDeTransferencias;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * @author Florencio Cañizal Calles
 * @version 1.1
 * Clase oyente que escucha los eventos procedentes de la opción cancelar trans
 * ferencia disponible en la ventana del gestor de transferencias.
 */
public class CancelarTransferenciaActionListener implements ActionListener{

    private VentanaGestorDeTransferencias vgt;
    private String idTransferencia;

    /**
     * Constructor de la clase. Inicializa las variables necesarias.
     * @param vgt La ventana del gestor de transferencias.
     * @param idTransferencia El identificador de la transferencia que se desea
     * eliminar.
     */
    public CancelarTransferenciaActionListener(VentanaGestorDeTransferencias vgt,String idTransferencia){
        this.vgt = vgt;
        this.idTransferencia = idTransferencia;
    }

    /**
     * Método que se ejecuta cuando se selecciona la opción de cancelar transfe
     * rencia de la ventana del gestor de las mismas. Utiliza el controlador de
     * las transferencias para llevar a cabo la operación.
     * @param e El evento que produce la ejecución del método.
     */
    @Override
    public void actionPerformed(ActionEvent e) {

        // Llamar al controlador para que cancele la transferencia
        TransferenciaFicherosControlador tfc = TransferenciaFicherosControlador.getInstancia();
        tfc.cancelarTransferencia(idTransferencia);

        // Eliminar la transferencia del gestor de las mismas
        vgt.eliminarTransferencia(idTransferencia);
    }
}

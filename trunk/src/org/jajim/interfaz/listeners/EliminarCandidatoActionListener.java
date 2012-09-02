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
import org.jajim.controladores.ContactosControlador;
import org.jajim.excepciones.ImposibleEliminarContactoException;
import org.jajim.interfaz.dialogos.MensajeError;
import org.jajim.interfaz.dialogos.PeticionRechazadaFormulario;

/**
 * @author Florencio Cañizal Calles
 * @version 1.1
 * Clase oyente que se activa cuando el usuario selecciona el botón Aceptar del
 * cuadro de diálogo que informa de que una petición de contacto ha sido rechaza
 * da.
 */
public class EliminarCandidatoActionListener implements ActionListener{

    private PeticionRechazadaFormulario prf;
    private String contacto;

    /**
     * Constructor de la clase. Iniciliza las variables necesarias.
     * @param prf El formulario que informa del rechazo.
     * @param contacto El contacto que ha rechazado la petición.
     */
    public EliminarCandidatoActionListener(PeticionRechazadaFormulario prf,String contacto){
        this.prf = prf;
        this.contacto = contacto;
    }

    /**
     * Método que se ejecuta cuando el usuario selecciona el botón Aceptar del
     * cuadro de diálogo que informa de que una petición de contacto ha sido re
     * chazada. Borra el contacto de la lista de contactos.
     * @param e El evento que produce la ejecución del método.
     */
    @Override
    public void actionPerformed(ActionEvent e) {

        // Llamar al constructor para que rezalice la operación.
        try{
            ContactosControlador ctc = ContactosControlador.getInstancia();
            ctc.eliminarContacto(contacto);
        }catch(ImposibleEliminarContactoException iece){
            new MensajeError(prf,"imposible_eliminar_contacto",MensajeError.ERR);
        }
        
        prf.dispose();
    }
}

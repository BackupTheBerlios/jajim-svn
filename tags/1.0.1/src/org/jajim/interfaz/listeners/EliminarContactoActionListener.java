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

import org.jajim.controladores.ContactosControlador;
import org.jajim.excepciones.ImposibleEliminarContactoException;
import org.jajim.interfaz.dialogos.MensajeError;
import org.jajim.interfaz.utilidades.PanelContactos;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * @author Florencio Cañizal Calles
 * @version 1.0.1
 * Clase oyente que escucha las peticiones de eliminación de contactos provenien
 * tes del panel de contactos.
 */
public class EliminarContactoActionListener implements ActionListener{

    private PanelContactos pc;

    /**
     * Constructor de la clase. Iniciliza las variables adecuadas.
     * @param pc El panel de contactos de la aplicación.
     */
    public EliminarContactoActionListener(PanelContactos pc){
        this.pc = pc;
    }

    /**
     * Método que se ejecuta la opción de eliminar contacto del panel de contactos
     * . Utiliza el controlador de los mismos para llevar a cabo la operación.
     * @param e
     */
    @Override
    public void actionPerformed(ActionEvent e) {

        // Cerrar el popup y recuperar el contacto
        pc.cerrarPopupContactos();
        String contacto = e.getActionCommand();
        contacto = pc.getCtc().getContactoPorAlias(contacto);

        // Abortar las conversaciones y las transferencias mantenidas con este
        // usuario
        AbortarOperaciones ao = new AbortarOperaciones(pc.getVp(),pc.getVp(),pc.getVp().getCnc(),pc.getVp().getVgt());
        ao.abortarConversaciones(contacto);
        ao.abortarTransferencias(contacto);
        

        // Llamar al controlador de los contactos
        ContactosControlador ctc = pc.getCtc();
        try{
            ctc.eliminarContacto(contacto);
        }catch(ImposibleEliminarContactoException iece){
            new MensajeError(pc.getVp(),"imposible_eliminar_contacto",MensajeError.ERR);
        }
    }
}

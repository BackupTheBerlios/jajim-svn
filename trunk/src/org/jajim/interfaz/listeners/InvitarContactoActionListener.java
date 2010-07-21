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
import org.jajim.controladores.ConversacionControlador;
import org.jajim.interfaz.dialogos.InvitarContactoFormulario;
import org.jajim.interfaz.dialogos.MensajeError;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * @author Florencio Cañizal Calles
 * @version 1.1
 * Clase oyente que atiende las invitaciones a otros contactos para añadirse a la
 * conversación. Dichas peticiones provienen del formulario de invitar contactos.
 */
public class InvitarContactoActionListener implements ActionListener{

    private InvitarContactoFormulario icf;
    private ConversacionControlador cvc;

    /**
     * Constructor de la clase. Inicializa las varibles necesarias.
     * @param icf El formulario de invitación de contactos.
     * @param cvc El controlador de la conversación.
     */
    public InvitarContactoActionListener(InvitarContactoFormulario icf,ConversacionControlador cvc){
        this.icf = icf;
        this.cvc = cvc;
    }

    /**
     * Método que se ejecuta cuando se selecciona la opción de invitar a los con
     * tacto del formulario correspondiente. Invita a los contactos a la conver
     * sación en colaboración con el controlador de la conversación.
     * @param e El evento que produce la ejecución del método.
     */
    @Override
    public void actionPerformed(ActionEvent e) {

        // Recuperar los campos del formulario
        String[] campos = icf.getCampos();

        // Comprobar que se pasan unos parámetros correctos
        if(campos.length == 0){
            new MensajeError(icf,"campos_invalidos_error",MensajeError.WARNING);
            return;
        }

        // Llamar al controlador para que realice la operación
        cvc.invitarContactos(campos);

        // Cerrar el formulario
        icf.dispose();
    }
}

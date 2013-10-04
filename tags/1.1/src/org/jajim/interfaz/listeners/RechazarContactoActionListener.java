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
import org.jajim.controladores.ContactosControlador;

/**
 * @author Florencio Cañizal Calles
 * @version 1.1
 * Clase oyente que actúa cuando el usuario selecciona la opción de Rechazar pe
 * tición de contacto del sistema.
 */
public class RechazarContactoActionListener implements ActionListener{

    private JDialog aorcf;
    private String contacto;

    /**
     * Constructor de la clase. Inicializa las variables necesarias.
     * @param aorcf Formulario desde el que se rechaza el contacto.
     */
    public RechazarContactoActionListener(JDialog aorcf,String contacto){
        this.aorcf = aorcf;
        this.contacto = contacto;
    }

    /**
     * Método que se ejecuta cuando el usuario selecciona la opción de "Rechazar"
     * contacto. Lleva a cabo dicha operación.
     * @param e El evento que produce la ejecución del método.
     */
    @Override
    public void actionPerformed(ActionEvent e) {

        // Llamar al controlador de contactos para que realice la operación
        ContactosControlador ctc = ContactosControlador.getInstancia();
        ctc.rechazarContacto(contacto);
        aorcf.dispose();
    }
}
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
import org.jajim.excepciones.ImposibleRecuperarAyudaException;
import org.jajim.interfaz.dialogos.MensajeError;
import org.jajim.interfaz.ventanas.VentanaAyuda;
import org.jajim.interfaz.ventanas.VentanaPrincipal;

/**
 * @author Florencio Cañizal Calles
 * @version 1.2
 * Clase oyente que reacciona a los eventos de solicitud de ayuda procedentes de
 * la ventana principal de la aplicación.
 */
public class LanzarAyudaActionListener implements ActionListener{

    /**
     * Constructor de la clase. Inicializa las variables necesarias.
     */
    public LanzarAyudaActionListener(){
    }

    /**
     * Método de la intefaz ActionListener. Se ejecuta cuando el usuario selecciona
     * la opción "Contenido de ayuda" de la ventana principal. Abre una ventana
     * con el contenido de la ayuda.
     * @param e El evento que produce la ejecución del método.
     */
    @Override
    public void actionPerformed(ActionEvent e) {
        
        try{
            new VentanaAyuda();
        }catch(ImposibleRecuperarAyudaException irae){
            new MensajeError(VentanaPrincipal.getInstancia(), "imposible_recuperar_ayuda_error",MensajeError.ERR);
        }
    }

}

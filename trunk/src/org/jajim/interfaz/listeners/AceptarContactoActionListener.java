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
import java.util.List;
import org.jajim.controladores.ContactosControlador;
import org.jajim.excepciones.ImposibleAñadirContactoException;
import org.jajim.interfaz.dialogos.AceptarContactoFormulario;
import org.jajim.interfaz.dialogos.MensajeError;

/**
 * @author Florencio Cañizal Calles
 * @version 1.1
 * Clase oyente que se ejecuta cuando se selecciona la opción aceptar del cuadro
 * de diálogo de aceptar contacto. Utiliza los controladores para dar de alta al
 * contacto.
 */
public class AceptarContactoActionListener implements ActionListener{

    private AceptarContactoFormulario acf;

    /**
     * Constructor de la clase. Inicializa las variables necesarias.
     * @param ctc Controlador de los contactos.
     */
    public AceptarContactoActionListener(AceptarContactoFormulario acf){
        this.acf = acf;
    }

    /**
     * Método que se ejecuta cuando se selecciona la opción aceptar del cuadro de
     * diálogo de aceptar contacto. Utiliza los controladores para dar de alta al
     * contacto.
     * @param e Evento que produce la ejecución del método.
     */
    @Override
    public void actionPerformed(ActionEvent e){

        // Conseguir la información introducida por el usuario y el contacto que
        // se va añadir
        List<String> campos = acf.getCampos();
        String contacto = campos.get(0);
        String alias = campos.get(1);
        String grupo = campos.get(2);

        // Comprabar si los campos introducidos son correctos.
        if(contacto.compareTo("") == 0 || alias.compareTo("") == 0){
            new MensajeError(acf,"campos_invalidos_error",MensajeError.WARNING);

        }

        // Llamar al controlador de contactos para que realice las operaciones ne
        // cesarias.
        try{
            ContactosControlador ctc = ContactosControlador.getInstancia();
            ctc.aceptarContacto(contacto,alias,grupo);
            // Cerrar el cuadro de diálogo
            acf.dispose();
        }catch(ImposibleAñadirContactoException iace){
            new MensajeError(acf,"imposible_añadir_contacto_error",MensajeError.ERR);
            acf.dispose();
        }
    }
}

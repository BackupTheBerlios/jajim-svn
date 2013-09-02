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
import org.jajim.interfaz.dialogos.MensajeError;
import org.jajim.interfaz.dialogos.ModificarGrupoDeContactosFormulario;

/**
 * @author Florencio Cañizal Calles
 * @version 1.1
 * Clase oyente que escucha las peticiones de modificación de grupo provenientes
 * del formulario de modificación de grupo.
 */
public class ModificarGrupoDeContactosActionListener implements ActionListener{

    private ModificarGrupoDeContactosFormulario mgdcf;
    private String grupo;

    /**
     * Constructor de la clase. Inicializa las variables necesarias.
     * @param mgdcf El formulario de modificación de grupo.
     * @param grupo El grupo sobre el que se van a realizar los cambios.
     */
    public ModificarGrupoDeContactosActionListener(ModificarGrupoDeContactosFormulario mgdcf,String grupo){
        this.mgdcf = mgdcf;
        this.grupo = grupo;
    }

    /**
     * Método que se ejecuta cuando se selecciona el botón Aceptar del formulario
     * de modificación de grupos. Intenta modificar el nombre del grupo en cola
     * boración con el controlador de los contactos.
     * @param e Evento que produce la ejecución del método.
     */
    @Override
    public void actionPerformed(ActionEvent e) {

        // Recuperar los campos introducidos en el formulario
        String[] campos = mgdcf.getCampos();
        String nombre = campos[0];

        // Comprobar si se da un nombre de grupo válido
        if(nombre.compareTo("") == 0){
            new MensajeError(mgdcf,"campos_invalidos_error",MensajeError.WARNING);
            return;
        }

        // Llamar al controlador de los contactos para que realice la operación
        ContactosControlador ctc = ContactosControlador.getInstancia();
        ctc.modificarGrupoDeContactos(grupo,nombre);

        mgdcf.dispose();
    }
}

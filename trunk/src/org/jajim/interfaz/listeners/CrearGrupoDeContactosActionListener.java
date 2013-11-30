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
import java.util.Arrays;
import org.jajim.controladores.ContactosControlador;
import org.jajim.excepciones.ImposibleAñadirContactoAGrupoException;
import org.jajim.interfaz.dialogos.CrearGrupoDeContactosFormulario;
import org.jajim.interfaz.dialogos.MensajeError;

/**
 * @author Florencio Cañizal Calles
 * @version 1.2 Clase oyente que reacciona a los eventos de creación de grupo de contactos pro venientes del formulario
 * activo para dicho propósito.
 */
public class CrearGrupoDeContactosActionListener implements ActionListener {

    private final CrearGrupoDeContactosFormulario cgdcf;

    /**
     * Constructor de la clase. Inicializa las variables necesarias.
     * <p>
     * @param cgdcf El formulario de creación de grupo de contactos.
     */
    public CrearGrupoDeContactosActionListener(CrearGrupoDeContactosFormulario cgdcf) {
        this.cgdcf = cgdcf;
    }

    /**
     * Método que se ejecuta cuando el usuario selecciona la opción aceptar del formulario de creación de contactos.
     * Intenta crear el grupo en colaboración con el controlador de contactos.
     * <p>
     * @param e El evento que produce la ejecución del método.
     */
    @Override
    public void actionPerformed(ActionEvent e) {

        // Recuperar los campos seleccionados y extraer los valores
        String[] campos = cgdcf.getCampos();
        String nombre = campos[0];
        String[] contactos = Arrays.copyOfRange(campos, 1, campos.length);

        // Comprobar si se han introducido los campos correctamente
        if (nombre.compareTo("") == 0 || contactos.length == 0) {
            new MensajeError(cgdcf, "campos_invalidos_error", MensajeError.WARNING);
            return;
        }

        // LLamar al controlador para que realice la operación
        try {
            ContactosControlador ctc = ContactosControlador.getInstancia();
            ctc.crearGrupoDeContactos(nombre, contactos);
        }
        catch (ImposibleAñadirContactoAGrupoException iacage) {
            new MensajeError(cgdcf, "imposible_añadir_contacto_a_grupo", MensajeError.ERR);
            return;
        }

        // Cerrar el formulario
        cgdcf.dispose();
    }
}

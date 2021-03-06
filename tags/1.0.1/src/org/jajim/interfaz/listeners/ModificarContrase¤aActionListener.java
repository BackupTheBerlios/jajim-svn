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

import org.jajim.controladores.ConexionControlador;
import org.jajim.controladores.CuentaControlador;
import org.jajim.excepciones.ImposibleModificarContraseñaException;
import org.jajim.interfaz.dialogos.MensajeError;
import org.jajim.interfaz.dialogos.ModificarContraseñaFormulario;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * @author Florencio Cañizal Calles
 * @version 1.0.1
 * Clase oyente que interactúa con el controlador de cuentas para modificar la
 * contraseña de una de las cuentas del sistema.
 */
public class ModificarContraseñaActionListener implements ActionListener {

    // Varriables
    private ModificarContraseñaFormulario mcf;
    private CuentaControlador cc;
    private ConexionControlador cnc;

    /**
     * Constructor de la clase. Iniciliza las variables necesarias.
     * @param mcf El cuadro de diálogo donde se incluyen las contraseñas.
     * @param cc El controlador de las cuentas.
     * @param cnc El controlador de la connexión.
     */
    public ModificarContraseñaActionListener(ModificarContraseñaFormulario mcf, CuentaControlador cc, ConexionControlador cnc) {
        this.mcf = mcf;
        this.cc = cc;
        this.cnc = cnc;
    }

    /**
     * Método que se ejecuta cuando se desea modificar la contraseña de una cuen
     * ta. Delega la responsabilidad en el controlador de las mismas.
     * @param e
     */
    @Override
    public void actionPerformed(ActionEvent e) {

        // Recuperar los campos del formulario.
        String[] campos = mcf.getCampos();
        String contraseña = campos[0];
        String confirmarContraseña = campos[1];
        boolean guardarContraseña = Boolean.parseBoolean(campos[2]);

        // Si no se introducen valores adecuados para los formularios sacar un
        // error
        if(contraseña.compareTo("") == 0 || confirmarContraseña.compareTo("") == 0){
            new MensajeError(mcf,"campos_invalidos_error",MensajeError.WARNING);
            return;
        }

        // Verificar que se ha conseguido un valor de contraseña correcta
        if (contraseña.compareTo(confirmarContraseña) != 0) {
            new MensajeError(mcf,"confirmacion_no_valida_error",MensajeError.WARNING);
            return;
        }

        try{
            // Llamar al controlador para que realice la operación
            cc.modificarContraseña(cnc,contraseña,guardarContraseña);
            // Cerrar el cuadro de diálogo
            mcf.dispose();
        }catch(ImposibleModificarContraseñaException imce){
            new MensajeError(mcf,"imposible_modificar_contraseña_error",MensajeError.ERR);
        }
    }
}

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
import org.jajim.controladores.ConversacionControlador;
import org.jajim.controladores.ConversacionControladorChatMultiusuario;
import org.jajim.excepciones.ImposibleVetarContactosException;
import org.jajim.interfaz.dialogos.MensajeError;
import org.jajim.interfaz.dialogos.VetarContactoFormulario;

/**
 * @author Florencio Cañizal Calles
 * @version 1.2 Clase oyente que escucha los eventos para la vetación de contactos procedentes del formulario habilitado
 * para tal función.
 */
public class VetarContactoActionListener implements ActionListener {

    private final VetarContactoFormulario vcf;
    private final ConversacionControlador cvc;

    /**
     * Constructor de la clase. Inicializa las variables necesarias.
     * <p>
     * @param vcf El formulario donde se seleccionan los contactos a vetar.
     * @param cvc El controlador de la conversación.
     */
    public VetarContactoActionListener(VetarContactoFormulario vcf, ConversacionControlador cvc) {
        this.vcf = vcf;
        this.cvc = cvc;
    }

    /**
     * Método que se ejecuta cuando el usuario selecciona la opción de vetar el contacto del formulario adecuado. Veta a
     * los contactos de la conversación en colaboración con el controlador de la conversación.
     * <p>
     * @param e El evento que produce la ejecución del método.
     */
    @Override
    public void actionPerformed(ActionEvent e) {

        // Recuperar los campos del formulario
        String[] campos = vcf.getCampos();

        // Comprobar que los datos del formulario son correctos.
        if (campos.length == 0) {
            new MensajeError(vcf, "campos_invalidos_error", MensajeError.WARNING);
            return;
        }

        // Llamar al controlador para que realice la operación
        try {
            if (cvc instanceof ConversacionControladorChatMultiusuario) {
                ConversacionControladorChatMultiusuario cccm = (ConversacionControladorChatMultiusuario) cvc;
                cccm.vetarContactos(campos);
            }
        }
        catch (ImposibleVetarContactosException ivce) {
            new MensajeError(vcf, "imposible_vetar_contactos_error", MensajeError.ERR);
        }

        // Cerrar el cuadro de diálog
        vcf.dispose();
    }
}

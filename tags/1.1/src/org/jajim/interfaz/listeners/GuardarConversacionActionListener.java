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
import org.jajim.excepciones.FicheroNoEncontradoException;
import org.jajim.excepciones.ImposibleGuardarConversacionException;
import org.jajim.interfaz.dialogos.GuardarConversacionFormulario;
import org.jajim.interfaz.dialogos.MensajeError;
import org.jajim.interfaz.ventanas.VentanaConversacion;

/**
 * @author Florencio Cañizal Calles
 * @version 1.1
 * Clase oyente que escucha los eventos de guardado de conversación procedentes
 * del formulario habilitado a tal efecto.
 */
public class GuardarConversacionActionListener implements ActionListener{

    private GuardarConversacionFormulario gcf;
    private VentanaConversacion vc;
    private ConversacionControlador cvc;

    /**
     * Constructor de la clase. Inicializa las variables necesarias.
     * @param gcf El formulario de guardado de conversaciones.
     * @param vc La ventana de la conversación.
     * @param cvc El controlador de la conversación.
     */
    public GuardarConversacionActionListener(GuardarConversacionFormulario gcf,VentanaConversacion vc,ConversacionControlador cvc){
        this.gcf = gcf;
        this.vc = vc;
        this.cvc = cvc;
    }

    /**
     * Método que se ejecuta cuando el usuario selecciona la opción de guardar con
     * versación del formulario apropiado. Intenta guardar la conversación en cola
     * boración con el controlador de las conversaciones.
     * @param e El evento que produce la ejecución del método.
     */
    @Override
    public void actionPerformed(ActionEvent e) {

        // Recuperar los campos introducidos en el formulario
        String[] campos = gcf.getCampos();
        String ruta = campos[0];
        String nombre = campos[1];

        // Comprobar si los campos son correctos
        if(ruta.compareTo("") == 0 || nombre.compareTo("") == 0){
            new MensajeError(gcf,"campos_invalidos_error",MensajeError.WARNING);
            return;
        }

        // Recuperar el texto de la conversación
        String texto = vc.getConversacion();

        // Llamar al controlador para que realice la operación
        try{
            cvc.guardarConversacion(nombre,ruta,texto);
            // Cerrar el formulario
            gcf.dispose();
        }catch(FicheroNoEncontradoException fnee){
            new MensajeError(gcf,"fichero_no_encontrado_error",MensajeError.ERR);
        }catch(ImposibleGuardarConversacionException igce){
            new MensajeError(gcf,"imposible_guardar_conversacion_error",MensajeError.ERR);
        }
    }
}

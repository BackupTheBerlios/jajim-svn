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

import org.jajim.interfaz.dialogos.AceptarORechazarChatPrivadoFormulario;
import org.jajim.interfaz.ventanas.VentanaPrincipal;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import org.jajim.interfaz.ventanas.VentanaConversacionChatPrivado;

/**
 * @author Florencio Cañizal Calles
 * @version 1.1
 * Clase oyente que escucha los eventos de aceptar conversación provenientes del
 * formulario correspondiente.
 */
public class AceptarChatPrivadoActionListener implements ActionListener{

    private AceptarORechazarChatPrivadoFormulario acf;
    private VentanaPrincipal vp;
    private String idChat;
    private String alias;

    /**
     * Constructor de la clase. Inicializa las variables necesarias.
     * @param acf El formulario de aceptación de conversaciones.
     * @param vp La ventana principal de la aplicación.
     * @param idChat El identificador del chat que se va a iniciar.
     * @param alias El alias del contacto con el que se quiere establecer una con
     * veración.
     */
    public AceptarChatPrivadoActionListener(AceptarORechazarChatPrivadoFormulario acf,VentanaPrincipal vp,String idChat,String alias){
        this.acf = acf;
        this.vp = vp;
        this.idChat = idChat;
        this.alias = alias;
    }

    /**
     * Método que se ejecuta cuando el usuario selecciona la opción de aceptar con
     * tacto del formulario provisto para dicha función. Lanza una nueva conversa
     * ción con el contacto apropiado.
     * @param e El evento que produce la ejecución del método.
     */
    @Override
    public void actionPerformed(ActionEvent e) {

        // Cerrar el cuadro de diálogo
        acf.dispose();
        // LLamar al constructor de las ventanas de la conversación para crear
        // una nueva
        new VentanaConversacionChatPrivado(vp,alias,idChat);
    }
}

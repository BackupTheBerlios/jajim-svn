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

import org.jajim.interfaz.dialogos.IntroducirNickFormulario;
import org.jajim.interfaz.dialogos.MensajeError;
import org.jajim.interfaz.ventanas.VentanaPrincipal;
import org.jajim.modelo.conversaciones.TiposDeChatEnumeracion;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import org.jajim.interfaz.ventanas.VentanaConversacionChatMultiusuario;

/**
 * @author Florencio Cañizal Calles
 * @version 1.0.1
 * Clase oyente que escucha los eventos de aceptación de invitación provenientes
 * de el formulario que permite intorducir el nick del usuario.
 *
 */
public class AceptarInvitacionActionListener implements ActionListener{

    private IntroducirNickFormulario inf;
    private VentanaPrincipal vp;
    private String alias;
    private String idInvitacion;
    private String room;

    /**
     * Constructor de la clase. Inicializa las variables necesarias.
     * @param inf El formulario en el que se introduce el nick.
     * @param vp La ventana principal de la aplicación.
     * @param alias El alias del contacto que nos ha invitado.
     * @param idInvitacion El identificador de la invitación.
     * @param room La sala en la que se desarrolla la invitación.
     */
    public AceptarInvitacionActionListener(IntroducirNickFormulario inf,VentanaPrincipal vp,String alias,String idInvitacion,String room){
        this.inf = inf;
        this.vp = vp;
        this.alias = alias;
        this.idInvitacion = idInvitacion;
        this.room = room;
    }

    /**
     * Método que se ejecuta cuando el usuario selecciona la opción Aceptar del
     * formulario de introducción de nick. Acepta la invitación de conversación
     * pendiente.
     * @param e El evento que produce la ejecución del método.
     */
    @Override
    public void actionPerformed(ActionEvent e) {

        // Recuperar la información del formulario
        String[] campos = inf.getCampos();
        String nickname = campos[0];

        // Comprobar que los campos introducidos son los correctos.
        if(nickname.compareTo("") == 0){
            new MensajeError(inf,"campos_invalidos_error",MensajeError.WARNING);
            return;
        }

        // Lanzar la conversación y cerrar el formulario
        inf.dispose();
        new VentanaConversacionChatMultiusuario(vp,alias,room,nickname,idInvitacion);
    }
}

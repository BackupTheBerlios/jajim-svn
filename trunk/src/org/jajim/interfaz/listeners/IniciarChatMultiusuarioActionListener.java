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
import org.jajim.interfaz.dialogos.IniciarChatMultiusuarioFormulario;
import org.jajim.interfaz.dialogos.MensajeError;
import org.jajim.interfaz.ventanas.VentanaConversacionChatMultiusuario;

/**
 * @author Florencio Cañizal Calles
 * @version 1.2 Clase oyente que escucha los eventos de iniciación de chat multiusuario procen dentes del formulario
 * habilitado a tal efecto.
 */
public class IniciarChatMultiusuarioActionListener implements ActionListener {

    private final IniciarChatMultiusuarioFormulario icmf;
    private final String alias;

    /**
     * Constructo de la clase. Inicializa las variables adecuadas.
     * <p>
     * @param icmf  El formulario donde se introducen los datos.
     * @param alias El alias del ontacto con el que se a realizar el chat.
     */
    public IniciarChatMultiusuarioActionListener(IniciarChatMultiusuarioFormulario icmf, String alias) {
        this.icmf = icmf;
        this.alias = alias;
    }

    /**
     * Método que se ejecuta cuando el usuario selecciona la opción Aceptar del formulario de iniciación de chat
     * multiusuario. Crea un nuevo chat multiusua rio.
     * <p>
     * @param e El evento que produce la ejecución del método.
     */
    @Override
    public void actionPerformed(ActionEvent e) {

        // Recuperar los campos del formulario
        String[] campos = icmf.getCampos();
        String room = campos[0];
        String nick = campos[1];

        // Comprobar si se ha poroducido algún error.
        if (room.compareTo("") == 0 || nick.compareTo("") == 0) {
            new MensajeError(icmf, "campos_invalidos_error", MensajeError.WARNING);
            return;
        }

        // Cerrar el cuadro de diálogo y lanzar una nueva conversación
        icmf.dispose();
        new VentanaConversacionChatMultiusuario(alias, room, nick);
    }
}

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
import javax.swing.JButton;
import org.jajim.controladores.PreferenciasControlador;
import org.jajim.interfaz.ventanas.VentanaConversacion;

/**
 * @author Florencio Cañizal Calles
 * @version 1.1
 * Clase que escucha los eventos de activació o desactivación de cursiva proceden
 * tes de la ventana de la conversación.
 */
public class CursivaMenuActionListener implements ActionListener{

    private VentanaConversacion vc;

    /**
     * Constrcutor de la clase. Inicializa las variables necesarias.
     * @param vc La ventana donde tiene lugar la conversación.
     */
    public CursivaMenuActionListener(VentanaConversacion vc){
        this.vc = vc;
    }

    /**
     * Método que se ejecuta cuando el usuario activa o desactiva la opción cursi
     * va de los mensajes. Actualiza las preferencias.
     * @param e El evento que produce la ejecución del método.
     */
    @Override
    public void actionPerformed(ActionEvent e) {

        // Recuperar el botón
        JButton cursiva = (JButton) e.getSource();

        // Actualizar las preferencias y el estado del botón.
        PreferenciasControlador pfc = PreferenciasControlador.getInstancia();
        if(cursiva.isSelected()){
            cursiva.setSelected(false);
            pfc.setCursiva(false);
        }
        else{
            cursiva.setSelected(true);
            pfc.setCursiva(true);
        }

        // Notificar el cambio a los interesados
        vc.actualizarPreferenciasMensajes();
    }
}

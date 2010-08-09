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

import org.jajim.controladores.PreferenciasControlador;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JButton;
import org.jajim.interfaz.ventanas.VentanaConversacion;

/**
 * @author Florencio Cañizal Calles
 * @version 1.1
 * Clase que escucha los eventos de modficación de letra en negrita procedentes
 * de la ventana de la conversación.
 */
public class NegritaMenuActionListener implements ActionListener{

    private VentanaConversacion vc;

    /**
     * Constructor de la clase. Inicializa las variables necesarias.
     * @param vc La ventana de la conversación.
     */
    public NegritaMenuActionListener(VentanaConversacion vc){
        this.vc = vc;
    }

    /**
     * Método que se ejecuta cuando el usuario selecciona el botón que activa o
     * desactiva la opción de texto en negrita. Actualiza las preferencias.
     * @param e El evento que produce la ejecución del método.
     */
    @Override
    public void actionPerformed(ActionEvent e) {

        // Recuperar el botón
        JButton negrita = (JButton) e.getSource();

        // Actualizar las preferencias y el estado del botón.
        PreferenciasControlador pfc = PreferenciasControlador.getInstancia();
        if(negrita.isSelected()){
            negrita.setSelected(false);
            pfc.setNegrita(false);
        }
        else{
            negrita.setSelected(true);
            pfc.setNegrita(true);
        }

        // Notificar el cambio a los interesados
        vc.actualizarPreferenciasMensajes();
    }
}

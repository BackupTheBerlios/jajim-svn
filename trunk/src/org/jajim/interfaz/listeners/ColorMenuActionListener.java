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

import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ResourceBundle;
import javax.swing.JColorChooser;
import org.jajim.controladores.PreferenciasControlador;
import org.jajim.interfaz.ventanas.VentanaConversacion;
import org.jajim.main.Main;

/**
 * @author Florencio Cañizal Calles
 * @version 1.2 Clase que escucha los eventos de modificación del color de los mensajes prove nientes de la ventana de
 * la conversación.
 */
public class ColorMenuActionListener implements ActionListener {

    private final ResourceBundle texto = ResourceBundle.getBundle("resources.Idioma", Main.loc);
    private final VentanaConversacion vc;

    /**
     * Constructor de la clase. Inicializa las variables necesarias.
     * <p>
     * @param vc La ventana de la conversación.
     */
    public ColorMenuActionListener(VentanaConversacion vc) {
        this.vc = vc;
    }

    /**
     * Método que se ejecuta cuando el usuario selecciona la opción de modificar el color de los mensajes, disponible en
     * la ventana de la conversación. Lan za un cuadro de diálogo de selección de color, actualiza las preferencias y
     * informa a la ventana de que debe recargar éstas.
     * <p>
     * @param e El evento que produce la ejecución del método.
     */
    @Override
    public void actionPerformed(ActionEvent e) {

        // Recuperar el color de las preferencias
        PreferenciasControlador pfc = PreferenciasControlador.getInstancia();
        Color colorAntiguo = new Color(pfc.getColorRojo(), pfc.getColorVerde(), pfc.getColorAzul());

        // Mostrar el cuadro de diálogo en el que elegir los colores
        Color color = JColorChooser.showDialog(vc, texto.getString("color_formulario_title"), colorAntiguo);

        if (color != null) {
            // Guardar las preferencias
            pfc.setColorRojo(color.getRed());
            pfc.setColorVerde(color.getGreen());
            pfc.setColorAzul(color.getBlue());

            // Notificar la modificación a los interesados
            vc.actualizarPreferenciasMensajes();
        }
    }
}

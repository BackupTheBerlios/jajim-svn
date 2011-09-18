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

package org.jajim.interfaz.utilidades;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Toolkit;
import java.awt.event.MouseEvent;
import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JWindow;
import javax.swing.Timer;
import javax.swing.border.TitledBorder;
import org.jajim.interfaz.ventanas.VentanaConversacion;
import org.jajim.interfaz.ventanas.VentanaConversacionChatPrivado;

/**
 * @author Florencio Cañizal Calles
 * @version 1.1
 * Ventana pequeña que se muestra en la esquina inferior izquierda de la pantalla
 * para notificar eventos importantes para el sistema. Trabaja con los eventos que
 * se producen en la conexión del sistema a la red.
 */
public class VentanaPopupConversacion extends VentanaPopup{

    private VentanaConversacion vc;

    /**
     * Constructor de la clase. Inicializa las variables necesarias.
     * @param informacion La información que se va a mostrar en la ventana popup.
     * @param vccp La ventana del chat privado vinculada al evento popup.
     */
    public VentanaPopupConversacion(String informacion, VentanaConversacion vc){

        super(informacion);
        // Iniciar
        this.vc = vc;
    }

    /**
     * Método que construye la ventana y la muestra al usuario.
     */
    @Override
    public void mostrarVentana(){

         // Obtener las dimensiones de la pantalla
        Toolkit tk = Toolkit.getDefaultToolkit();
        Dimension tamaño = tk.getScreenSize();

        String mensaje = null;
        // Recuperar el contenido del mensaje y determinar su tamaño (Eliminar el estilo)
        int inicio = informacion.indexOf('>');
        int fin = informacion.indexOf("</");
        String contenido = informacion.substring(inicio + 1, fin);

        // Calcular la longitud del contenido, si es superior a 40, se recorta y se añaden puntos suspensivos
        if(contenido.length() > 40){
            int espacio = contenido.indexOf(' ', 40);
            contenido = contenido.substring(0, espacio);
            contenido += "...";

            // Poner los estilos y el contenido
            informacion = informacion.substring(0, inicio + 1) + contenido + informacion.substring(fin);
        }
        mensaje = "<html><div align=\"center\">" + informacion + "</div></html>";

        // Crear la ventana
        window = new JWindow();

        // Crear la etiqueta del mensaje
        etiquetaInformacion = new JLabel(mensaje);
        etiquetaInformacion.setBorder(BorderFactory.createTitledBorder(BorderFactory.createLineBorder(Color.BLACK),"JAJIM 1.1",TitledBorder.LEFT,TitledBorder.TOP));
        etiquetaInformacion.setHorizontalAlignment(JLabel.CENTER);
        window.add(etiquetaInformacion);

        // Establecer las propiedades de la ventana
        window.addMouseListener(this);
        window.setAlwaysOnTop(true);

        window.setSize(175,100);
        window.setLocation((int)tamaño.getWidth() - (175),(int)tamaño.getHeight() - (100 + 30));
        window.setVisible(true);

        // Tiempo
        Timer timer = new Timer(15000,this);
        timer.start();
    }

    /**
     * Metodo de la interfaz MouseListener. Se ejecuta cuando se hace click en la
     * ventana. Actualiza el estado de la ventana y cierra la misma.
     * @param e El evento que produce la ejecución del método.
     */
    @Override
    public void mouseClicked(MouseEvent e){
        super.mouseClicked(e);

        // Si se trata de un evento sobre un chat privado, se cambia el estado del chat y se restaura
        if(vc instanceof VentanaConversacionChatPrivado){
            VentanaConversacionChatPrivado vccp = (VentanaConversacionChatPrivado) vc;
            vccp.setEstado(VentanaConversacionChatPrivado.NORMAL);
        }
        vc.setVisible(true);
    }
}

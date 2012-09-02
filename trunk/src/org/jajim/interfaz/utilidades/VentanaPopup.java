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

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.ResourceBundle;
import javax.swing.JLabel;
import javax.swing.JWindow;
import org.jajim.main.Main;

/**
 * @author Florencio Cañizal Calles
 * @version 1.1
 * Ventana pequeña que se muestra en la esquina inferior izquierda de la pantalla
 * para notificar eventos importantes para el sistema.
 */
public abstract class VentanaPopup implements MouseListener, ActionListener{

    protected ResourceBundle texto = ResourceBundle.getBundle("resources.Idioma",Main.loc);

    // Variables necesarias
    protected int estado;
    protected static int DESPLEGADO = 0;
    protected static int SELECCIONADO = 1;
    protected static int FINALIZADO = 2;

    protected JWindow window;
    protected JLabel etiquetaInformacion;

    protected String informacion;

    /**
     * Constructor de la clase. Iniciliza las variables necesarias.
     * @param informacion Información adicional de presentación.
     */
    public VentanaPopup(String informacion){
        estado = VentanaPopup.DESPLEGADO;
        this.informacion = informacion;
    }

    /**
     * Método que construye la ventana y la muestra al usuario.
     */
    public abstract void mostrarVentana();

    /**
     * Metodo de la interfaz MouseListener. Se ejecuta cuando se hace click en la
     * ventana. Actualiza el estado de la ventana y cierra la misma.
     * @param e El evento que produce la ejecución del método.
     */
    @Override
    public void mouseClicked(MouseEvent e){
        this.estado = VentanaPopup.SELECCIONADO;
        window.dispose();
    }

    /**
     * Metodo de la interfaz MouseListener. Se ejecuta cuando se pulsa el
     * ratón en la ventana.
     * @param e El evento que produce la ejecución del método.
     */
    @Override
    public void mousePressed(MouseEvent e){}

    /**
     * Metodo de la interfaz MouseListener. Se ejecuta cuando se suelta el
     * ratón en la ventana.
     * @param e El evento que produce la ejecución del método.
     */
    @Override
    public void mouseReleased(MouseEvent e) {}

    /**
     * Metodo de la interfaz MouseListener. Se ejecuta cuando se introduce el
     * ratón en la ventana.
     * @param e El evento que produce la ejecución del método.
     */
    @Override
    public void mouseEntered(MouseEvent e){}

    /**
     * Metodo de la interfaz MouseListener. Se ejecuta cuando se saca el
     * ratón en la ventana.
     * @param e El evento que produce la ejecución del método.
     */
    @Override
    public void mouseExited(MouseEvent e){}

    /**
     * Método de la interfaz ActionListener. Se ejecuta cuando pasan 15 segundos
     * sin que se pinche la ventana. Actualiza el estado de la ventana y la cierra.
     * @param e El evento que produce la ejecución del método.
     */
    @Override
    public void actionPerformed(ActionEvent e) {
        this.estado = VentanaPopup.FINALIZADO;
        window.dispose();
    }

    /**
     * Método que espera hasta que se seleccione la ventana popup o hasta que se
     * pase su tiempo de activación.
     * @return true si se ha seleccionado la ventana o false si se termina su
     * tiempo de activación.
     */
    public boolean pollSeleccionado(){

        boolean seleccionado = false;

        // Esperar por la selección o finalización de la ventana.
        while(this.estado == VentanaPopup.DESPLEGADO){
            try{
                Thread.sleep(300);
            }catch(Exception e){}
        }

        // Si la ventana se selecciona devolver true
        if(this.estado == VentanaPopup.SELECCIONADO) {
            seleccionado = true;
        }

        return seleccionado;
    }
}

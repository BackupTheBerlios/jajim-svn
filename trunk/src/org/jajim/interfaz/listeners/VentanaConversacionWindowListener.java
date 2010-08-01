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
import org.jajim.interfaz.ventanas.VentanaConversacion;
import java.awt.Dimension;
import java.awt.Point;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import javax.swing.JFrame;
import org.jajim.interfaz.ventanas.VentanaConversacionNueva;

/**
 * @author Florencio Cañizal Calles
 * @version 1.1
 * Clase que escucha los eventos que proceden de una de las ventanas de una con
 * versación.
 */
public class VentanaConversacionWindowListener extends WindowAdapter{

    private VentanaConversacion vc;

    /**
     * Constructor de la clase. Inicializa las variables necesarias.
     * @param vc Ventana de una conversación.
     */
    public VentanaConversacionWindowListener(VentanaConversacion vc){
        this.vc = vc;
    }

    /**
     * Constructor de la clase. Inicializa las variables necesarias.
     * @param vc Ventana de una conversación.
     */
    public VentanaConversacionWindowListener(VentanaConversacionNueva vc){
        //this.vc = vc;
    }

    /**
     * Método que se ejecuta cuando se cierra la ventana de conversación a la que
     * está asociada el listener.
     * @param e El evento que poduce la ejecución del método.
     */
    @Override
    public void windowClosing(WindowEvent e){

        // Guardar las preferencias de la ventana
        PreferenciasControlador pfc = PreferenciasControlador.getInstancia();
        if(vc.getExtendedState() == JFrame.MAXIMIZED_BOTH)
            pfc.setVentanaConversacionMaximizada(true);
        else{
            pfc.setVentanaConversacionMaximizada(false);
            Point p = vc.getLocation();
            pfc.setVentanaConversacionX(p.x);
            pfc.setVentanaConversacionY(p.y);
            Dimension d = vc.getSize();
            pfc.setVentanaConversacionAncho(d.width);
            pfc.setVentanaConversacionLargo(d.height);
        }

        // Borrar la conversación de la lista de conversaciones
        VentanaConversacion.eliminarConversacion(vc);
    }
}

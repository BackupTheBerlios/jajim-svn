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

import java.awt.Dimension;
import java.awt.Point;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import javax.swing.JFrame;
import org.jajim.controladores.CuentaControlador;
import org.jajim.controladores.PreferenciasControlador;
import org.jajim.interfaz.utilidades.PanelContactos;
import org.jajim.interfaz.ventanas.VentanaGestorDeCuentas;
import org.jajim.interfaz.ventanas.VentanaGestorDeTransferencias;
import org.jajim.interfaz.ventanas.VentanaPrincipal;

/**
 * @author Florencio Cañizal Calles
 * @version 1.2
 * Realiza todas las acciones necesarias para cerrar correctamente la aplicación:
 * guardado de cuentas de usuario.
 */
public class VentanaPrincipalWindowListener extends WindowAdapter{

    /**
     * Constructor de la clase. Inicializa los campos de la misma.
     */
    public VentanaPrincipalWindowListener(){
    }

    /**
     * Método que se ejecuta cuando cierra la ventana. Guarda toda la información
     * necesaria para la aplicación.
     * @param e Evento que produjo la activación del método.
     */
    @Override
    public void windowClosing(WindowEvent e){

        // Guardar las cuentas del usuario
        CuentaControlador cc = CuentaControlador.getInstancia();
        cc.guardarCuentas();

        // Guardar las preferencias
        // Ventana principal
        VentanaPrincipal vp = VentanaPrincipal.getInstancia();
        PreferenciasControlador pfc = PreferenciasControlador.getInstancia();
        if(vp.getExtendedState() == JFrame.MAXIMIZED_BOTH) {
            pfc.setVentanaPrincipalMaximizada(true);
        }
        else {
            pfc.setVentanaPrincipalMaximizada(false);
        }
        Point p = vp.getLocation();
        pfc.setVentanaPrincipalX(p.x);
        pfc.setVentanaPrincipalY(p.y);
        Dimension d = vp.getSize();
        pfc.setVentanaPrincipalAncho(d.width);
        pfc.setVentanaPrincipalLargo(d.height);
        // Gestor de cuentas
        VentanaGestorDeCuentas vgc = vp.getVgc();
        p = vgc.getLocation();
        pfc.setGestorDeCuentasX(p.x);
        pfc.setGestorDeCuentasY(p.y);
        // Gestor de transferencias
        VentanaGestorDeTransferencias vgt = vp.getVgt();
        p = vgt.getLocation();
        pfc.setGestorDeTransferenciasX(p.x);
        pfc.setGestorDeTransferenciasY(p.y);
        pfc.guardarPreferencias();
        System.exit(0);
    }

    /**
     * Método que se ejecuta cuando se minimiza una ventana. La oculta para que
     * sólo sea accesible desde la barra de herramientas.
     * @param e Evento que produjo la ejecución del método.
     */
    @Override
    public void windowIconified(WindowEvent e){

        // Recuperar la ventana y hacerla invisible si se puede
        VentanaPrincipal vp = VentanaPrincipal.getInstancia();
        if(vp.isOcultable()) {
            vp.setVisible(false);
        }
    }

    /**
     * Método que se ejecuta cuando se desactiva la ventana. Intenta cerrar los
     * menús popup si estaban abiertos.
     * @param e El evento que produce la ejecución del método.
     */
    @Override
    public void windowDeactivated(WindowEvent e){
        
        // Recuperar el panel de contacto y ocultar los menús popup
        VentanaPrincipal vp = VentanaPrincipal.getInstancia();
        PanelContactos pc = vp.getPc();
        pc.cerrarPopupContactos();
        pc.cerrarPopupGrupos();
    }
}

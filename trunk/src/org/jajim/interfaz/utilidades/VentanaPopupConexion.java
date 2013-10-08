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
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JWindow;
import javax.swing.Timer;
import javax.swing.border.TitledBorder;
import org.jajim.interfaz.ventanas.VentanaPrincipal;
import org.jajim.modelo.conexiones.EventosDeConexionEnumeracion;
import org.jivesoftware.smack.util.StringUtils;

/**
 * @author Florencio Cañizal Calles
 * @version 1.2
 * Ventana pequeña que se muestra en la esquina inferior izquierda de la pantalla
 * para notificar eventos importantes para el sistema. Trabaja con los eventos que
 * se producen en la conexión del sistema a la red.
 */
public class VentanaPopupConexion extends VentanaPopup{

    private EventosDeConexionEnumeracion edce;

    /**
     * Constructor de la clase. Iniciliza las variables necesarias.
     * @param edce El tipo de evento que se debe mostrar.
     * @param informacion Información adicional de presentación.
     */
    public VentanaPopupConexion(EventosDeConexionEnumeracion edce, String informacion){

        super(informacion);
        // Iniciar
        this.edce = edce;
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

        if(edce == EventosDeConexionEnumeracion.peticionDeSuscripcion)
        {
            mensaje = "<html><div align=\"center\">" + informacion + texto.getString("peticion_de_suscripcion_evento");
        }
        else if(edce == EventosDeConexionEnumeracion.confirmacionDeSuscripcion){
            mensaje = "<html><div align=\"center\">" + informacion + texto.getString("confirmacion_de_suscripcion_evento");
        }
        else if(edce == EventosDeConexionEnumeracion.denegacionDeSuscripcion){
            mensaje = "<html><div align=\"center\">" + informacion + texto.getString("denegacion_de_suscripcion_evento");
        }
        else if(edce == EventosDeConexionEnumeracion.peticionDeChat){
            informacion = StringUtils.parseBareAddress(informacion);
            mensaje = "<html><div align=\"center\">" + informacion + texto.getString("peticion_de_chat_evento");
        }
        else if(edce == EventosDeConexionEnumeracion.invitacionAChat){
            int posicion = informacion.indexOf("&");
            String usuario = informacion.substring(0,posicion);
            usuario = StringUtils.parseBareAddress(usuario);
            String sala = informacion.substring(posicion + 1);
            mensaje = "<html><div align=\"center\">" + usuario + texto.getString("invitacion_a_chat_evento") + " " + sala + ".</div></html>";
        }
        else if(edce == EventosDeConexionEnumeracion.peticionDeTransferencia){
            informacion = StringUtils.parseBareAddress(informacion);
            mensaje = "<html><div align=\"center\">" + informacion + texto.getString("peticion_de_transferencia_evento");
        }
        else if(edce == EventosDeConexionEnumeracion.usuarioConectado){
        mensaje = "<html><div align=\"center\">" + informacion + texto.getString("usuario_conectado_evento");
        }

        // Crear la ventana
        window = new JWindow();

        // Crear la etiqueta del mensaje
        etiquetaInformacion = new JLabel(mensaje);
        etiquetaInformacion.setBorder(BorderFactory.createTitledBorder(BorderFactory.createLineBorder(Color.BLACK),"JAJIM 1.2",TitledBorder.LEFT,TitledBorder.TOP));
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
        
        VentanaPrincipal vp = VentanaPrincipal.getInstancia();
        super.mouseClicked(e);

        // Si se trata de un evento sobre la ventana principal, se hace visible y se restaura
        if(!vp.isVisible()){
          vp.setVisible(true);
          vp.setState(JFrame.NORMAL);
        }
    }
}

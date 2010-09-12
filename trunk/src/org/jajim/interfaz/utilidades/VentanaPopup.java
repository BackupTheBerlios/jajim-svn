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

import org.jajim.interfaz.ventanas.VentanaPrincipal;
import org.jajim.main.Main;
import org.jajim.modelo.conexiones.EventosDeConexionEnumeracion;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.ResourceBundle;
import javax.swing.BorderFactory;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JWindow;
import javax.swing.Timer;
import javax.swing.border.TitledBorder;
import org.jivesoftware.smack.util.StringUtils;

/**
 * @author Florencio Cañizal Calles
 * @version 1.1
 * Ventana pequeña que se muestra en la esquina inferior izquierda de la pantalla
 * para notificar eventos importantes para el sistema.
 */
public class VentanaPopup implements MouseListener,ActionListener{

    private ResourceBundle texto = ResourceBundle.getBundle("resources.Idioma",Main.loc);

    // Variables necesarias
    private int estado;
    private static int DESPLEGADO = 0;
    private static int SELECCIONADO = 1;
    private static int FINALIZADO = 2;

    private JWindow window;
    private JLabel etiquetaInformacion;

    private VentanaPrincipal vp;
    private EventosDeConexionEnumeracion edce;
    private String informacion;

    /**
     * Constructor de la clase. Iniciliza las variables necesarias.
     * @param edce El tipo de evento que se debe mostrar.
     * @param informacion Información adicional de presentación.
     * @param vp La ventana principal de la aplicación.
     */
    public VentanaPopup(EventosDeConexionEnumeracion edce,String informacion,VentanaPrincipal vp){

        // Iniciar
        estado = VentanaPopup.DESPLEGADO;
        this.vp = vp;
        this.edce = edce;
        this.informacion = informacion;
    }

    /**
     * Método que construye la ventana y la muestra al usuario.
     */
    public void mostrarVentana(){

        // Obtener las dimensiones de la pantalla
        Toolkit tk = Toolkit.getDefaultToolkit();
        Dimension tamaño = tk.getScreenSize();

        String mensaje = null;
        // Recuperar el tipo de mensaje a desplegar
        if(edce == EventosDeConexionEnumeracion.peticionDeSuscripcion){
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
        this.estado = VentanaPopup.SELECCIONADO;
        window.dispose();
        // Comprobar el estado de la ventana
        if(!vp.isVisible()){
          vp.setVisible(true);
          vp.setState(JFrame.NORMAL);
        }
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
        if(this.estado == VentanaPopup.SELECCIONADO)
            seleccionado = true;

        return seleccionado;
    }
}

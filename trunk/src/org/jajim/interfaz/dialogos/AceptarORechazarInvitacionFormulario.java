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

package org.jajim.interfaz.dialogos;

import org.jajim.controladores.ContactosControlador;
import org.jajim.interfaz.listeners.RechazarInvitacionActionListener;
import org.jajim.interfaz.ventanas.VentanaPrincipal;
import org.jajim.main.Main;
import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ResourceBundle;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;

/**
 * @author Florencio Cañizal Calles
 * @version 1.1
 * Clase formulario, crea la interfaz para que el usuario pueda aceptar o rechazar
 * uan invitación a un chat multiusuario.
 */
public class AceptarORechazarInvitacionFormulario extends JDialog implements ActionListener{

    private ResourceBundle texto = ResourceBundle.getBundle("resources.Idioma",Main.loc);

    // Cadenas constantes
    private final String principal = texto.getString("aceptar_o_rechazar_invitacion_formulario_cadena_principal");

    private final String aceptar = texto.getString("aceptar_invitacion_formulario_cadena");
    private final String rechazar = texto.getString("rechazar_invitacion_formulario_cadena");

    // Componentes de la interfaz
    private JLabel cadenaPrincipal;
    private JButton botonAceptar;
    private JButton botonRechazar;

    // Elementos importantes
    private VentanaPrincipal vp;
    private String idInvitacion;
    private String room;
    private String alias;

    /**
     * Constructor de la clase.Iniciliza las variables necesarias y crea la inter
     * faz de usuario.
     * @param vp La ventana principal de la aplicación.
     * @param idInvitacion El identificador de la invitación.
     * @param room La sala a la que se nos invita.
     * @param contacto El usuario que realiza la invitación.
     */
    public AceptarORechazarInvitacionFormulario(VentanaPrincipal vp,String idInvitacion,String room,String contacto){

        // Inicialización de variables
        super(vp,true);
        this.vp = vp;
        this.idInvitacion = idInvitacion;
        this.room = room;

        // Creación de la interfaz
        Container cp = this.getContentPane();

        // Añadir etiqueta principal
        ContactosControlador ctc = ContactosControlador.getInstancia();
        int posicion = contacto.indexOf("/");
        if(posicion != -1)
            contacto = contacto.substring(0,posicion);
        alias = ctc.getAliasPorContacto(contacto);
        cadenaPrincipal = new JLabel(alias + " " + principal + " " + room);
        cadenaPrincipal.setHorizontalAlignment(JLabel.CENTER);
        cadenaPrincipal.setBorder(BorderFactory.createEmptyBorder(15,10,15,10));
        cp.add(BorderLayout.CENTER,cadenaPrincipal);

        // Añadir los botones
        JPanel botones = new JPanel(new FlowLayout(FlowLayout.CENTER));
        botones.setBorder(BorderFactory.createEmptyBorder(0,10,6,10));
        botonAceptar = new JButton(aceptar);
        botonAceptar.addActionListener(this);
        botonRechazar = new JButton(rechazar);
        botonRechazar.addActionListener(new RechazarInvitacionActionListener(this,contacto,room));
        botones.add(botonAceptar);
        botones.add(botonRechazar);
        cp.add(BorderLayout.SOUTH,botones);

        // Opciones del cuadro de diálogo
        this.setTitle(texto.getString("aceptar_o_rechazar_invitacion_formulario_title"));
        this.setSize(350,125);
        this.setResizable(false);
        this.setLocationRelativeTo(vp);
        this.setDefaultCloseOperation(JDialog.DO_NOTHING_ON_CLOSE);
        this.setVisible(true);
    }

    /**
     * Método que se ejecuta cuando el usuario selecciona la opción Aceptar del
     * formulario. Lanza un formulario para que el usuario introduzca el nick que
     * va a usar.
     * @param e El evento que produce la ejecución del método.
     */
    @Override
    public void actionPerformed(ActionEvent e) {

        // Cerrar el formulario y lanzar el formulario para la introducción del
        // nick
        this.dispose();
        new IntroducirNickFormulario(vp,idInvitacion,room,alias);
    }
}

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

import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.FlowLayout;
import java.util.ResourceBundle;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import org.jajim.controladores.ContactosControlador;
import org.jajim.interfaz.listeners.AceptarChatPrivadoActionListener;
import org.jajim.interfaz.listeners.RechazarChatPrivadoActionListener;
import org.jajim.interfaz.ventanas.VentanaPrincipal;
import org.jajim.main.Main;

/**
 * @author Florencio Cañizal Calles
 * @version 1.2 Formulario que informa al usuario de que se ha solicitado chatear con él. El usuario puede aceptar o
 * rechazar la solicitud.
 */
public class AceptarORechazarChatPrivadoFormulario extends JDialog {

    private final ResourceBundle texto = ResourceBundle.getBundle("resources.Idioma", Main.loc);

    // Cadenas constantes
    private final String principal = texto.getString("aceptar_conversacion_formulario_cadena_principal");

    private final String aceptar = texto.getString("aceptar_conversacion_formulario_cadena");
    private final String rechazar = texto.getString("rechazar_conversacion_formulario_cadena");

    // Componentes de la interfaz
    private final JLabel cadenaPrincipal;
    private final JButton botonAceptar;
    private final JButton botonRechazar;

    /**
     * Constructor de la clase. Inicializa la variables necesarias. Crea la inter faz de usuario.
     * <p>
     * @param idChat   El identificador del chat.
     * @param contacto El contacto que ha realizado la petición.
     */
    public AceptarORechazarChatPrivadoFormulario(String idChat, String contacto) {

        // Inicialización de variables
        super(VentanaPrincipal.getInstancia(), true);
        VentanaPrincipal vp = VentanaPrincipal.getInstancia();
        int posicion = contacto.indexOf("/");
        contacto = contacto.substring(0, posicion);

        // Creación de la interfaz
        Container cp = this.getContentPane();

        // Añadir etiqueta principal
        String alias = ContactosControlador.getInstancia().getAliasPorContacto(contacto);
        cadenaPrincipal = new JLabel(alias + " " + principal);
        cadenaPrincipal.setHorizontalAlignment(JLabel.CENTER);
        cadenaPrincipal.setBorder(BorderFactory.createEmptyBorder(15, 10, 15, 10));
        cp.add(BorderLayout.CENTER, cadenaPrincipal);

        // Añadir los botones
        JPanel botones = new JPanel(new FlowLayout(FlowLayout.CENTER));
        botones.setBorder(BorderFactory.createEmptyBorder(0, 10, 6, 10));
        botonAceptar = new JButton(aceptar);
        botonAceptar.addActionListener(new AceptarChatPrivadoActionListener(this, idChat, alias));
        botonRechazar = new JButton(rechazar);
        botonRechazar.addActionListener(new RechazarChatPrivadoActionListener(this, idChat));
        botones.add(botonAceptar);
        botones.add(botonRechazar);
        cp.add(BorderLayout.SOUTH, botones);

        // Opciones del cuadro de diálogo
        this.setTitle(texto.getString("aceptar_conversacion_formulario_cadena_title"));
        this.setSize(290, 125);
        this.setResizable(false);
        this.setLocationRelativeTo(vp);
        this.setDefaultCloseOperation(JDialog.DO_NOTHING_ON_CLOSE);
        this.setVisible(true);
    }
}

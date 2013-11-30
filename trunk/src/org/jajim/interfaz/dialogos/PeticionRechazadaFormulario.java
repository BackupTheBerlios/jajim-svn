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
import org.jajim.interfaz.listeners.EliminarCandidatoActionListener;
import org.jajim.interfaz.ventanas.VentanaPrincipal;
import org.jajim.main.Main;

/**
 * @author Florencio Cañizal Calles
 * @version 1.2 Clase que representa un formulario en el que se informa al usuario de que una petición de contacto que
 * ha solicitdo ha sido rechazada.
 */
public class PeticionRechazadaFormulario extends JDialog {

    private final ResourceBundle texto = ResourceBundle.getBundle("resources.Idioma", Main.loc);

    // Cadenas constantes
    private final String principal = texto.getString("peticion_rechazada_formulario_cadena_principal");

    private final String aceptar = texto.getString("ok");

    // Componentes de la interfaz
    private final JLabel cadenaPrincipal;
    private final JButton botonAceptar;

    /**
     * Constructor de la clase. Inicializa las variables adecuadas. Crea la inter faz del cuadro de diálogo.
     * <p>
     * @param contacto El contacto que ha rechazado la petición.
     */
    public PeticionRechazadaFormulario(String contacto) {

        // Inicialización de variables
        super(VentanaPrincipal.getInstancia(), true);
        VentanaPrincipal vp = VentanaPrincipal.getInstancia();

        // Creación de la interfaz
        Container cp = this.getContentPane();

        // Añadir etiqueta principal
        cadenaPrincipal = new JLabel(contacto + " " + principal);
        cadenaPrincipal.setHorizontalAlignment(JLabel.CENTER);
        cadenaPrincipal.setBorder(BorderFactory.createEmptyBorder(15, 10, 15, 10));
        cp.add(BorderLayout.CENTER, cadenaPrincipal);

        // Añadir los botones
        JPanel botones = new JPanel(new FlowLayout(FlowLayout.CENTER));
        botones.setBorder(BorderFactory.createEmptyBorder(0, 10, 6, 10));
        botonAceptar = new JButton(aceptar);
        botonAceptar.addActionListener(new EliminarCandidatoActionListener(this, contacto));
        botones.add(botonAceptar);
        cp.add(BorderLayout.SOUTH, botones);

        // Opciones del cuadro de diálogo
        this.setTitle(texto.getString("peticion_rechazada_formulario_title"));
        this.setSize(350, 125);
        this.setResizable(false);
        this.setLocationRelativeTo(vp);
        this.setDefaultCloseOperation(JDialog.DO_NOTHING_ON_CLOSE);
        this.setVisible(true);
    }
}

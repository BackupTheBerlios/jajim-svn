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
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ResourceBundle;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import org.jajim.interfaz.ventanas.VentanaPrincipal;
import org.jajim.main.Main;

/**
 * @author Florencio Cañizal Calles
 * @version 1.2 Clase diálogo que informa al usuario de que se ha acetado una petición de con tacto iniciada por él.
 */
public class ConfirmarContactoFormulario extends JDialog implements ActionListener {

    private final ResourceBundle texto = ResourceBundle.getBundle("resources.Idioma", Main.loc);

    // Cadenas constantes
    private final String OK = texto.getString("ok");
    private final String mensajeConfirmacion = texto.getString("confirmar_contacto_formulario_cadena_principal");

    // Componentes de la interfaz
    private final JLabel confirmacion;
    private final JButton botonAceptar;

    /**
     * Constructor de la clase. Inicializa la interfaz.
     * <p>
     * @param contacto El contacto que ha aceptado nuestra petición de contacto.
     */
    public ConfirmarContactoFormulario(String contacto) {

        // Inicialización
        super(VentanaPrincipal.getInstancia(), true);
        VentanaPrincipal vp = VentanaPrincipal.getInstancia();

        // Creación de la interfaz
        Container cp = this.getContentPane();

        // Creación de la etiqueta principal
        confirmacion = new JLabel(contacto + " " + mensajeConfirmacion);
        confirmacion.setHorizontalAlignment(JLabel.CENTER);
        confirmacion.setBorder(BorderFactory.createEmptyBorder(15, 10, 15, 10));
        cp.add(BorderLayout.CENTER, confirmacion);

        // Crear el botón de Aceptar
        JPanel botones = new JPanel(new FlowLayout(FlowLayout.CENTER));
        botones.setBorder(BorderFactory.createEmptyBorder(0, 10, 6, 10));
        botonAceptar = new JButton(OK);
        botonAceptar.addActionListener(this);
        botones.add(botonAceptar);
        cp.add(BorderLayout.SOUTH, botones);

        // Opciones del cuadro de diálogo
        this.setTitle(texto.getString("confirmar_contacto_formulario_title"));
        this.setSize(350, 125);
        this.setResizable(false);
        this.setLocationRelativeTo(vp);
        this.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
        this.setVisible(true);
    }

    /**
     * Método que se ejecuta cuando se selecciona el botón Aceptar del cuadro de diálogo. Cierra la ventana del mismo.
     * <p>
     * @param e Evento que produce la ejecución del método
     */
    @Override
    public void actionPerformed(ActionEvent e) {
        this.dispose();
    }
}

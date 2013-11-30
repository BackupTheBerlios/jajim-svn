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
import java.awt.Frame;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ResourceBundle;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import org.jajim.main.Main;

/**
 * @author Florencio Cañizal Calles
 * @version 1.2 Clase que formulario en la que el usuario puede introducir la contraseña de la cuenta en caso de ser
 * necesaria.
 */
public class IntroducirContraseñaFormulario extends JDialog implements ActionListener {

    private final ResourceBundle texto = ResourceBundle.getBundle("resources.Idioma", Main.loc);

    // Cadena constantes
    private final String principal = texto.getString("introducir_contraseña_formulario_principal");
    private final String etiqueta = texto.getString("introducir_contraseña_formulario_contraseña");
    private final String OK = texto.getString("ok");

    // Componentes de la interfaz
    private final JLabel cadenaPrincipal;
    private final JPasswordField contraseña;
    private final JButton aceptar;

    /**
     * Construtor de la clase. Iniciliza las variables necesarias. Crea la inter faz.
     * <p>
     * @param padre La ventana padre del cuador de diálogo.
     */
    public IntroducirContraseñaFormulario(Frame padre) {

        // padre y modal
        super(padre, true);

        // Creación de la interfaz
        Container cp = this.getContentPane();

        // Añadir la cadena principal al cuadro de diálogo
        cadenaPrincipal = new JLabel(principal);
        cadenaPrincipal.setHorizontalAlignment(JLabel.CENTER);
        cadenaPrincipal.setBorder(BorderFactory.createEmptyBorder(15, 10, 15, 10));
        cp.add(BorderLayout.NORTH, cadenaPrincipal);

        // Añadir el formulario para introducir la contraseña
        JPanel formulario = new JPanel(new GridLayout(1, 1));
        formulario.setBorder(BorderFactory.createEmptyBorder(0, 30, 15, 30));
        contraseña = new JPasswordField();
        formulario.add(contraseña);
        cp.add(BorderLayout.CENTER, formulario);

        // Añadir los botones
        JPanel botones = new JPanel();
        botones.setLayout(new FlowLayout(FlowLayout.CENTER));
        botones.setBorder(BorderFactory.createEmptyBorder(0, 10, 6, 10));
        aceptar = new JButton(OK);
        aceptar.addActionListener(this);
        botones.add(aceptar);
        cp.add(BorderLayout.SOUTH, botones);

        // Opciones del cuadro de diálogo
        this.setTitle(texto.getString("introducir_contraseña_formulario_title"));
        this.setSize(300, 145);
        this.setResizable(false);
        this.setLocationRelativeTo(padre);
        this.setDefaultCloseOperation(JDialog.DO_NOTHING_ON_CLOSE);
        this.setVisible(true);
    }

    /**
     * Cierra el cuadro de diálogo.
     * <p>
     * @param e
     */
    @Override
    public void actionPerformed(ActionEvent e) {
        this.dispose();
    }

    /**
     * Recupera la contraseña introducida en el formulario.
     * <p>
     * @return La contraseña introducida en el formulario.
     */
    public String getContraseña() {
        return new String(contraseña.getPassword());
    }
}

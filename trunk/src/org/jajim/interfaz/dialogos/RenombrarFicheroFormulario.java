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
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ResourceBundle;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import org.jajim.interfaz.listeners.RenombrarFicheroActionListener;
import org.jajim.interfaz.ventanas.VentanaGestorDeTransferencias;
import org.jajim.main.Main;

/**
 * @author Florencio Cañizal Calles
 * @version 1.2 Clase formulario que permite al usuario introducir un nuevo nombre para el fi chero.
 */
public class RenombrarFicheroFormulario extends JDialog implements ActionListener {

    private final ResourceBundle texto = ResourceBundle.getBundle("resources.Idioma", Main.loc);

    // Cadenas constantes
    private final String principal = texto.getString("renombrar_fichero_formulario_principal");

    private final String[] etiquetas = {
        texto.getString("renombrar_fichero_formulario_nombre")
    };

    private final String OK = texto.getString("ok");
    private final String cancelar = texto.getString("cancelar");

    // Componentes de la interfaz
    private final JLabel cadenaPrincipal;
    private final JLabel[] grupoDeEtiquetas = new JLabel[etiquetas.length];
    private final JTextField[] grupoDeCampos = new JTextField[etiquetas.length];
    private final JButton botonAceptar;
    private final JButton botonCancelar;

    /**
     * Constructor de la clase. Inicializa las variables necesarias y crea la in terfaz de usuario.
     * <p>
     * @param filaSeleccionada La fila que tiene seleccionada el usuario.
     */
    public RenombrarFicheroFormulario(int filaSeleccionada) {

        // Inicialización
        super(VentanaGestorDeTransferencias.getInstancia(), true);
        VentanaGestorDeTransferencias vgt = VentanaGestorDeTransferencias.getInstancia();

        // Creación de la interfaz
        Container cp = this.getContentPane();

        // Añadir la cadena principal al cuadro de diálogo
        cadenaPrincipal = new JLabel(principal);
        cadenaPrincipal.setHorizontalAlignment(JLabel.CENTER);
        cadenaPrincipal.setBorder(BorderFactory.createEmptyBorder(15, 10, 15, 10));
        cp.add(BorderLayout.NORTH, cadenaPrincipal);

        // Crear el formulario
        JPanel formulario = new JPanel(new GridLayout(etiquetas.length, 2));
        formulario.setBorder(BorderFactory.createEmptyBorder(0, 10, 15, 10));
        for (int i = 0; i < etiquetas.length; i++) {
            // Crear la etiqueta y añadirla
            grupoDeEtiquetas[i] = new JLabel(etiquetas[i]);
            formulario.add(grupoDeEtiquetas[i]);
            // Crear el campo de texto y añadirlo
            grupoDeCampos[i] = new JTextField();
            formulario.add(grupoDeCampos[i]);
        }
        cp.add(BorderLayout.CENTER, formulario);

        // Añadir los botones
        JPanel botones = new JPanel();
        botones.setLayout(new FlowLayout(FlowLayout.RIGHT));
        botones.setBorder(BorderFactory.createEmptyBorder(0, 10, 6, 10));
        botonAceptar = new JButton(OK);
        botonAceptar.addActionListener(new RenombrarFicheroActionListener(this, filaSeleccionada));
        botones.add(botonAceptar);
        botonCancelar = new JButton(cancelar);
        botonCancelar.addActionListener(this);
        botones.add(botonCancelar);
        cp.add(BorderLayout.SOUTH, botones);

        // Opciones del cuadro de diálogo
        this.setTitle(texto.getString("renombrar_fichero_formulario_title"));
        this.setSize(290, 148);
        this.setResizable(false);
        this.setLocationRelativeTo(vgt);
        this.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
        this.setVisible(true);
    }

    /**
     * Método que se ejecuta cuando el usuario selecciona la opción Cancelar del formulario. Cierra la ventana.
     * <p>
     * @param e El evento que produce la ejecución del método.
     */
    @Override
    public void actionPerformed(ActionEvent e) {
        this.dispose();
    }

    /**
     * Retorna el valor de los campos introducidos por el usuario.
     * <p>
     * @return El valor de los campos introducidos por el usuario.
     */
    public String[] getCampos() {

        String[] campos = new String[etiquetas.length];

        for (int i = 0; i < campos.length; i++) {
            campos[i] = grupoDeCampos[i].getText();
        }

        return campos;
    }
}

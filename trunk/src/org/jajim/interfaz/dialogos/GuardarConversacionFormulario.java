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
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import org.jajim.interfaz.listeners.GuardarConversacionActionListener;
import org.jajim.interfaz.ventanas.VentanaConversacion;
import org.jajim.main.Main;

/**
 * @author Florencio Cañizal Calles
 * @version 1.2 Clase formulario en la que se introducen los datos para poder guardar una con versación.
 */
public class GuardarConversacionFormulario extends JDialog implements ActionListener {

    private final ResourceBundle texto = ResourceBundle.getBundle("resources.Idioma", Main.loc);

    // Cadenas constantes
    private final String cadenaPrincipal = texto.getString("guardar_conversacion_formulario_principal");

    private final String[] etiquetas = {
        texto.getString("guardar_conversacion_formulario_ruta"),
        texto.getString("guardar_conversacion_formulario_nombre")
    };

    private final String OK = texto.getString("ok");
    private final String cancelar = texto.getString("cancelar");

    // Componentes de la interfaz
    private final JLabel principal;
    private final JLabel[] cadenasEtiquetas = new JLabel[etiquetas.length];
    private final JTextField[] camposDeTexto = new JTextField[etiquetas.length];
    private JButton botonRuta;
    private final JButton botonAceptar;
    private final JButton botonCancelar;

    /**
     * Constructor de la clase. Inicializa las variables necesarias. Crea la inter faz de usuario.
     * <p>
     * @param vc La ventana de la conversación.
     */
    public GuardarConversacionFormulario(VentanaConversacion vc) {

        // Inicialización de variables
        super(vc, true);

        // Creación de la interfaz
        Container cp = this.getContentPane();

        // Añadir etiqueta principal
        principal = new JLabel(cadenaPrincipal);
        principal.setHorizontalAlignment(JLabel.CENTER);
        principal.setBorder(BorderFactory.createEmptyBorder(15, 10, 15, 10));
        cp.add(BorderLayout.NORTH, principal);

        // Añadir el panel del formulario
        JPanel panelFormulario = new JPanel(new GridLayout(etiquetas.length, 2, 5, 10));
        panelFormulario.setBorder(BorderFactory.createEmptyBorder(0, 10, 15, 10));
        for (int i = 0; i < etiquetas.length; i++) {
            cadenasEtiquetas[i] = new JLabel(etiquetas[i]);
            panelFormulario.add(cadenasEtiquetas[i]);
            if (i == 0) {
                JPanel textoYBoton = new JPanel(new BorderLayout());
                camposDeTexto[i] = new JTextField();
                botonRuta = new JButton(new ImageIcon(ClassLoader.getSystemResource("icons/ruta.png")));
                botonRuta.setActionCommand("ruta");
                botonRuta.addActionListener(this);
                textoYBoton.add(BorderLayout.CENTER, camposDeTexto[i]);
                textoYBoton.add(BorderLayout.EAST, botonRuta);
                panelFormulario.add(textoYBoton);
            }
            else {
                camposDeTexto[i] = new JTextField();
                panelFormulario.add(camposDeTexto[i]);
            }
        }
        cp.add(BorderLayout.CENTER, panelFormulario);

        // Añadir los botones
        JPanel botones = new JPanel();
        botones.setLayout(new FlowLayout(FlowLayout.RIGHT));
        botones.setBorder(BorderFactory.createEmptyBorder(0, 10, 6, 10));
        botonAceptar = new JButton(OK);
        botonAceptar.addActionListener(new GuardarConversacionActionListener(this, vc, vc.getCvc()));
        botonCancelar = new JButton(cancelar);
        botonCancelar.setActionCommand("cancelar");
        botonCancelar.addActionListener(this);
        botones.add(botonAceptar);
        botones.add(botonCancelar);
        cp.add(BorderLayout.SOUTH, botones);

        // Opciones del cuadro de diálogo
        this.setTitle(texto.getString("guardar_conversacion_formulario_title"));
        this.setSize(300, 180);
        this.setResizable(false);
        this.setLocationRelativeTo(vc);
        this.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
        this.setVisible(true);
    }

    /**
     * Método que se ejecuta cuando el usuario selecciona el botón Ruta del formu lario o el botón Cancelar. En el
     * primer caso abre un cuadro de diálogo pa ra introducir la ruta. En el segundo cierra el formulario.
     * <p>
     * @param e El evento que produce la ejecución del método.
     */
    @Override
    public void actionPerformed(ActionEvent e) {

        // Recuperar la acción a realizar
        String accion = e.getActionCommand();

        if (accion.compareTo("ruta") == 0) {
            // Lanzar el formulario con la ruta
            JFileChooser selector = new JFileChooser();
            selector.updateUI();
            selector.setDialogType(JFileChooser.SAVE_DIALOG);
            selector.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
            int valorDeRetorno = selector.showOpenDialog(this);
            if (valorDeRetorno == JFileChooser.APPROVE_OPTION) {
                camposDeTexto[0].setText(selector.getSelectedFile().getAbsolutePath());
            }
        }
        else {
            // Cerrar el formulario
            this.dispose();
        }
    }

    /**
     * Retorna el valor de los campos introducidos por el usuario.
     * <p>
     * @return El valor de los campos introducidos por el usuario.
     */
    public String[] getCampos() {

        String[] campos = new String[camposDeTexto.length];

        // Extraer los campos del formulario
        for (int i = 0; i < campos.length; i++) {
            campos[i] = camposDeTexto[i].getText();
        }

        return campos;
    }
}

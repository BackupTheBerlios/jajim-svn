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

import org.jajim.interfaz.listeners.ModificarContraseñaActionListener;
import org.jajim.interfaz.ventanas.VentanaPrincipal;
import org.jajim.main.Main;
import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ResourceBundle;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JPasswordField;

/**
 * @author Florencio Cañizal Calles
 * @version 1.0.1
 * Cuadro de diálogo para introducir el núevo valor de la contraseña.
 */
public class ModificarContraseñaFormulario extends JDialog implements ActionListener{

    private ResourceBundle texto = ResourceBundle.getBundle("resources.Idioma",Main.loc);

    // Cadena constantes
    private final String principal = texto.getString("modificar_contraseña_formulario_principal");

    private final String[] etiquetas = {
        texto.getString("modificar_contraseña_formulario_contraseña"),
        texto.getString("modificar_contraseña_formulario_repetir_contraseña"),
        texto.getString("modificar_contraseña_formulario_guardar_contraseña")
    };

    private final String OK = texto.getString("ok");
    private final String cancelar = texto.getString("cancelar");

    // Componentes
    private JLabel cadenaPrincipal;
    private JLabel[] camposEtiquetas = new JLabel[etiquetas.length];
    private JPasswordField[] camposContraseñas = new JPasswordField[etiquetas.length];
    private JCheckBox guardar;
    private JButton botonAceptar;
    private JButton botonCancelar;

    // Ventana principal
    private VentanaPrincipal vp;

    /**
     * Constructor de la clase. Inicializa las variables necesarias. Crea la in
     * terfaz del cuadro de diálogo.
     * @param vp
     */
    public ModificarContraseñaFormulario(VentanaPrincipal vp){

        // Inicialización
        super(vp,true);
        this.vp = vp;

        // Creación de la interfaz
        Container cp = this.getContentPane();

        // Añadir la cadena principal al cuadro de diálogo
        cadenaPrincipal = new JLabel(principal);
        cadenaPrincipal.setHorizontalAlignment(JLabel.CENTER);
        cadenaPrincipal.setBorder(BorderFactory.createEmptyBorder(15,10,15,10));
        cp.add(BorderLayout.NORTH,cadenaPrincipal);

        // Crear el formulario
        JPanel formulario = new JPanel(new GridLayout(etiquetas.length,2,5,10));
        formulario.setBorder(BorderFactory.createEmptyBorder(0,10,15,10));
        for(int i = 0;i < etiquetas.length;i++){
            // Crear la etiqueta y añadirla
            camposEtiquetas[i] = new JLabel(etiquetas[i]);
            formulario.add(camposEtiquetas[i]);

            switch(i){
                case 0:
                case 1:
                    // Crear el campo de la contraseña y añadirlo
                    camposContraseñas[i] = new JPasswordField();
                    formulario.add(camposContraseñas[i]);
                    break;
                case 2:
                    // Crear el checkbox y añadirlo al panel
                    guardar = new JCheckBox();
                    formulario.add(guardar);
                    break;
            }
        }
        cp.add(BorderLayout.CENTER,formulario);

        // Añadir los botones
        JPanel botones = new JPanel();
        botones.setLayout(new FlowLayout(FlowLayout.RIGHT));
        botones.setBorder(BorderFactory.createEmptyBorder(0,10,6,10));
        botonAceptar = new JButton(OK);
        botonAceptar.addActionListener(new ModificarContraseñaActionListener(this));
        botones.add(botonAceptar);
        botonCancelar = new JButton(cancelar);
        botonCancelar.addActionListener(this);
        botones.add(botonCancelar);
        cp.add(BorderLayout.SOUTH,botones);

        // Opciones del cuadro de diálogo
        this.setTitle(texto.getString("modificar_contraseña_formulario_title"));
        this.setSize(320,210);
        this.setResizable(false);
        this.setLocationRelativeTo(vp);
        this.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
        this.setVisible(true);
    }

    /**
     * Método que se ejecuta cuando los usuarios seleccionan la opción Cancelar
     * del cuadro de diálogo.
     * @param e El evento que produce la ejecución del método.
     */
    @Override
    public void actionPerformed(ActionEvent e){
        // Cerrar el cuadro de diálogo
        this.dispose();
    }

    /**
     * Retorna el valor de los campos introducidos por el usuario.
     * @return El valor de los campos introducidos por el usuario.
     */
    public String[] getCampos(){

        // Declaración del array
        String[] campos = new String[3];

        // Recuerar los valores y asignarlos
        for(int i = 0;i < campos.length;i++)
            switch(i){
                case 0:
                case 1:
                    // Recuperar el valor de los password
                    campos[i] = new String(camposContraseñas[i].getPassword());
                    break;
                case 2:
                    campos[i] = String.valueOf(guardar.isSelected());
            }

        return campos;
    }
}

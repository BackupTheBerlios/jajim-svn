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
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;
import org.jajim.main.Main;

/**
 * @author Florencio Cañizal Calles
 * @version 1.2
 * Clase que crea un interfaz general para poder introducir los datos de una
 * cuenta. Se registra como oyente de eventos, aunque la información especifica
 * para cada evento la recoge cada una de las clases derivadas.
 */
public class CuentaFormulario extends JDialog implements ActionListener{
    
    private ResourceBundle texto = ResourceBundle.getBundle("resources.Idioma",Main.loc);

    // Cadenas constantes
    private final String principal = texto.getString("cuenta_formulario_principal");

    private final String[] etiquetas = {
        texto.getString("cuenta_formulario_identificador"),
        texto.getString("cuenta_formulario_servidor"),
        texto.getString("cuenta_formulario_contraseña"),
        texto.getString("cuenta_formulario_confirmar_contraseña"),
        texto.getString("cuenta_formulario_guardar")
    };

    private final String OK = texto.getString("ok");
    private final String cancelar = texto.getString("cancelar");

    // Componentes de la interfaz
    private JLabel cadenaPrincipal;
    private JLabel[] grupoDeEtiquetas = new JLabel[etiquetas.length];
    private JTextField[] grupoDeCampos = new JTextField[etiquetas.length - 3];
    private JPasswordField[] contraseña = new JPasswordField[etiquetas.length - 3];
    private JCheckBox guardar;
    protected JButton botonAceptar;
    private JButton botonCancelar;

    /**
     * Constructor de la clase. Crea la interfaz genérica para la introducción de
     * los datos de una cuenta.
     * @param frame Ventana de la dependerá el formulario.
     */
    public CuentaFormulario(JFrame frame){

        // Inicialización
        super(frame, true);

        // Creación de la interfaz
        Container cp = this.getContentPane();

        // Creación del mensaje principal
        cadenaPrincipal = new JLabel(principal);
        cadenaPrincipal.setHorizontalAlignment(JLabel.CENTER);
        cadenaPrincipal.setBorder(BorderFactory.createEmptyBorder(15,10,15,10));
        cp.add(BorderLayout.NORTH,cadenaPrincipal);

        // Creación del formulario
        JPanel formulario = new JPanel();
        formulario.setBorder(BorderFactory.createEmptyBorder(0,10,15,10));
        formulario.setLayout(new GridLayout(etiquetas.length,2,5,10));
        for(int i = 0;i < etiquetas.length;i++){
            grupoDeEtiquetas[i] = new JLabel(etiquetas[i]);
            formulario.add(grupoDeEtiquetas[i]);
            switch(i){
                case 0:
                case 1:
                    grupoDeCampos[i] = new JTextField();
                    formulario.add(grupoDeCampos[i]);
                    break;
                case 2:
                case 3:
                    contraseña[i - 2] = new JPasswordField();
                    formulario.add(contraseña[i - 2]);
                    break;
                case 4:
                    guardar = new JCheckBox();
                    guardar.setSelected(false);
                    formulario.add(guardar);
            }
        }
        cp.add(BorderLayout.CENTER,formulario);

        // Crear los botones y añadirle el oyente
        JPanel botones = new JPanel();
        botones.setLayout(new FlowLayout(FlowLayout.RIGHT));
        botones.setBorder(BorderFactory.createEmptyBorder(0,10,6,10));
        botonAceptar = new JButton(OK);
        botonCancelar = new JButton(cancelar);
        botonCancelar.addActionListener(this);
        botones.add(botonAceptar);
        botones.add(botonCancelar);
        cp.add(BorderLayout.SOUTH,botones);

        // Opciones del cuadro de diálogo
        this.setSize(340,270);
        this.setResizable(false);
        this.setLocationRelativeTo(frame);
        this.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
    }

    /**
     * Método que se ejecuta despueés de la recpeción de un evento para los que
     * estaba registrado. Cierra el cuadro de diálogo
     * @param e Evento que ha producido la ejecución del método.
     */
    @Override
    public void actionPerformed(ActionEvent e){
        this.dispose();
    }

    /**
     * Método utilizado para obtener los campos introducidos por el usuario en el
     * formulario.
     * @return Lista con los campos introducidos por el usuario en el formulario
     */
    public List<String> getCampos(){

        List<String> campos = new ArrayList<>();

        // Extraer los campos del formulario y añadirlos a la lista
        for(int i = 0;i < grupoDeCampos.length;i++) {
            campos.add(grupoDeCampos[i].getText());
        }

        for(int i = 0;i < contraseña.length;i++) {
            campos.add(new String(contraseña[i].getPassword()));
        }
        boolean g = guardar.isSelected();
        campos.add(String.valueOf(g));

        return campos;
    }
}

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
import org.jajim.interfaz.listeners.RechazarContactoActionListener;
import org.jajim.interfaz.ventanas.VentanaPrincipal;
import org.jajim.main.Main;

/**
 * @author Florencio Cañizal Calles
 * @version 1.1
 * Clase diálogo que pregunta al usuario si quiere aceptar o rechazar la petición
 * de contacto de un usuario.
 */
public class AceptarORechazarContactoFormulario extends JDialog implements ActionListener{

    private ResourceBundle texto = ResourceBundle.getBundle("resources.Idioma",Main.loc);

    // Cadenas constantes
    private final String principal = texto.getString("aceptar_o_rechazar_contacto_formulario_cadena_principal");

    private final String aceptar = texto.getString("aceptar_contacto_formulario_cadena");
    private final String rechazar = texto.getString("rechazar_contacto_formulario_cadena");

    // Componentes de la interfaz
    private JLabel cadenaPrincipal;
    private JButton botonAceptar;
    private JButton botonRechazar;

    // Ventana principal de la aplicación
    private VentanaPrincipal vp;
    private String contacto;

    /**
     * Constructor de la clase. Inicializa las variables adecuadas. Crea la inter
     * faz. Registra los oyentes adecuados.
     * @param vp Ventana principal de la aplicación.
     * @param contacto El usuario que desea establecer contacto con nuestro usua
     * rio
     */
    public AceptarORechazarContactoFormulario(VentanaPrincipal vp,String contacto){

        // Inicialización de variables
        super(vp,true);
        this.vp = vp;
        this.contacto = contacto;

        // Creación de la interfaz
        Container cp = this.getContentPane();

        // Añadir etiqueta principal
        cadenaPrincipal = new JLabel(contacto + " " + principal);
        cadenaPrincipal.setHorizontalAlignment(JLabel.CENTER);
        cadenaPrincipal.setBorder(BorderFactory.createEmptyBorder(15,10,15,10));
        cp.add(BorderLayout.CENTER,cadenaPrincipal);

        // Añadir los botones
        JPanel botones = new JPanel(new FlowLayout(FlowLayout.CENTER));
        botones.setBorder(BorderFactory.createEmptyBorder(0,10,6,10));
        botonAceptar = new JButton(aceptar);
        botonAceptar.addActionListener(this);
        botonRechazar = new JButton(rechazar);
        botonRechazar.addActionListener(new RechazarContactoActionListener(this,contacto));
        botones.add(botonAceptar);
        botones.add(botonRechazar);
        cp.add(BorderLayout.SOUTH,botones);

        // Opciones del cuadro de diálogo
        this.setTitle(texto.getString("aceptar_o_rechazar_contacto_formulario_title"));
        this.setSize(350,125);
        this.setResizable(false);
        this.setLocationRelativeTo(vp);
        this.setDefaultCloseOperation(JDialog.DO_NOTHING_ON_CLOSE);
        this.setVisible(true);
    }

    /**
     * Método que se ejecuta cuando se selecciona el botón de aceptar contacto.
     * Inicia el cuadro de diálogo de asignación de contacto.
     * @param e El evento que dispara la ejecución del método.
     */
    @Override
    public void actionPerformed(ActionEvent e){

        // Llamar al cuadro de diálogo para que el usuario introduzca los datos
        // que se quieren dar al contacto.
        this.dispose();
        new AceptarContactoFormulario(vp,contacto);
    }
}

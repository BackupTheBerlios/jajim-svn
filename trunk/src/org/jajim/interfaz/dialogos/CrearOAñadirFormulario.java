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
import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import org.jajim.interfaz.ventanas.VentanaGestorDeCuentas;
import org.jajim.interfaz.ventanas.VentanaPrincipal;
import org.jajim.main.Main;

/**
 * @author Florencio Cañizal Calles
 * @version 1.2
 * Clase que crea un formulario en el que se le dan, al usuario, las opciones de
 * crear una nueva cuenta o añadir una existente.
 */
public class CrearOAñadirFormulario extends JDialog implements ActionListener{

    private ResourceBundle texto = ResourceBundle.getBundle("resources.Idioma",Main.loc);

    // Cadenas constantes
    private final String principal = texto.getString("crear_o_añadir_formulario_cadena_principal");

    private final String[] etiquetas = {
        texto.getString("crear_o_añadir_formulario_primera_opción"),
        texto.getString("crear_o_añadir_formulario_segunda_opción")
    };

    private final String OK = texto.getString("ok");
    private final String cancelar = texto.getString("cancelar");

    // Componentes de la interfaz
    private JLabel cadenaPrincipal;
    private ButtonGroup grupoDeBotones;
    private JRadioButton[] opciones = new JRadioButton[etiquetas.length];
    private JButton botonAceptar;
    private JButton botonCancelar;

    // Atributos interesantes
    private VentanaGestorDeCuentas vgc;


    /**
     * Inicializa la interfaz del formulario y controla la selección de los boto
     * nes del mismo.
     */
    public CrearOAñadirFormulario(){

        // Inicialización
        super(VentanaPrincipal.getInstancia(), true);
        this.vgc = null;
        this.setLocationRelativeTo(VentanaPrincipal.getInstancia());

        // Crear la interfaz
        this.inicilizarInterfaz();
    }

    /**
     * Inicializa la interfaz del formulario y controla la selección de los boto
     * nes del mismo.
     * @param vgc El gestor de las cuentas del sistema.
     */
    public CrearOAñadirFormulario(VentanaGestorDeCuentas vgc){
        
        // Inicailización
        super(vgc,true);
        this.vgc = vgc;
        this.setLocationRelativeTo(vgc);

        // Crear la interfaz
        this.inicilizarInterfaz();
    }

    /**
     * Método de creación obligatoria por implementar la clase la interfaz Action
     * Listener. Se ejecuta cuando se pulse culquiera de los botones del diálogo.
     * Si se pulsa el botón de aceptar se realiza el siguiente paso a la hora de
     * registrar la cuenta, si se pulsa el botón de cancelar se aborta la operación.
     * @param e Evento que ha originado la activación del método.
     */
    @Override
    public void actionPerformed(ActionEvent e){
        if(e.getActionCommand().compareTo("Cancelar") == 0){
            this.dispose();
        }
        else if(opciones[0].isSelected()){
            this.dispose();
            if(vgc == null) {
                new CrearCuentaFormulario();
            }
            else {
                new CrearCuentaFormulario(vgc);
            }
        }else if(opciones[1].isSelected()){
            this.dispose();
            if(vgc == null) {
                new AñadirCuentaFormulario();
            }
            else {
                new AñadirCuentaFormulario(vgc);
            }
        }
    }

    /**
     * Método que crea la interfaz del cuadro de diálogo.
     */
    private void inicilizarInterfaz(){
        // Creación de la interfaz
        Container cp = this.getContentPane();

        // Añadir la etiqueta principal
        cadenaPrincipal = new JLabel(principal);
        cadenaPrincipal.setHorizontalAlignment(JLabel.CENTER);
        cadenaPrincipal.setBorder(BorderFactory.createEmptyBorder(15,10,15,10));
        cp.add(BorderLayout.NORTH,cadenaPrincipal);

        // Añadir los radio buttons y las etiquetas
        JPanel central = new JPanel();
        central.setLayout(new GridLayout(etiquetas.length,1));
        central.setBorder(BorderFactory.createEmptyBorder(0,10,15,10));
        grupoDeBotones = new ButtonGroup();
        boolean marcado = true;
        for(int i = 0;i < etiquetas.length;i++){

            opciones[i] = new JRadioButton(etiquetas[i],marcado);
            grupoDeBotones.add(opciones[i]);
            central.add(opciones[i]);

            if(marcado) {
                marcado = false;
            }
        }
        cp.add(BorderLayout.CENTER,central);

        // Añadir los botones y asignarles un oyente
        JPanel botones = new JPanel();
        botones.setLayout(new FlowLayout(FlowLayout.RIGHT));
        botones.setBorder(BorderFactory.createEmptyBorder(0,10,6,10));
        botonAceptar = new JButton(OK);
        botonAceptar.setActionCommand("Aceptar");
        botonAceptar.addActionListener(this);
        botones.add(botonAceptar);
        botonCancelar = new JButton(cancelar);
        botonCancelar.setActionCommand("Cancelar");
        botonCancelar.addActionListener(this);
        botones.add(botonCancelar);
        cp.add(BorderLayout.SOUTH,botones);


        // Opciones del cuadro de diálogo
        this.setTitle(texto.getString("crear_o_añadir_formulario_title"));
        this.setSize(450,180);
        this.setResizable(false);
        if(vgc != null) {
            this.setLocationRelativeTo(vgc);
        }
        else {
            this.setLocationRelativeTo(VentanaPrincipal.getInstancia());
        }
        this.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
        this.setVisible(true);
    }
}

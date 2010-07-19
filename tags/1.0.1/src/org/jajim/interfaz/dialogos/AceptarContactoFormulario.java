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

import org.jajim.interfaz.listeners.AceptarContactoActionListener;
import org.jajim.interfaz.listeners.RechazarContactoActionListener;
import org.jajim.interfaz.ventanas.VentanaPrincipal;
import org.jajim.main.Main;
import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

/**
 * @author Florencio Cañizal Calles
 * @version 1.0.1
 * Clase diálogo que muestra los campos que el usuario puede introducir a la hora
 * de aceptar un usuario propuesto.
 */
public class AceptarContactoFormulario extends JDialog{

    private ResourceBundle texto = ResourceBundle.getBundle("resources.Idioma",Main.loc);

    // Cadenas constantes
    private final String principal = texto.getString("aceptar_contacto_formulario_principal");

    private final String[] etiquetas = {
        texto.getString("aceptar_contacto_formulario_alias"),
        texto.getString("aceptar_contacto_formulario_grupo")
    };
    
    private final String OK = texto.getString("ok");
    private final String cancelar = texto.getString("cancelar");

    // Componentes de la interfaz
    private JLabel cadenaPrincipal;
    private JLabel[] grupoDeEtiquetas = new JLabel[etiquetas.length];
    private JTextField[] grupoDeCampos = new JTextField[etiquetas.length - 1];
    private JComboBox grupos;
    private JButton botonAceptar;
    private JButton botonCancelar;

    // Variables importantes
    private String contacto;

    /**
     * Constructor de la clase. Inicializa las variables y crea la interfaz del 
     * cuadro de diálogo.
     * @param vp Ventana principal de la aplicación.
     * @param contacto El contacto que se va a aceptar.
     */
    public AceptarContactoFormulario(VentanaPrincipal vp,String contacto){

        // Inicialización de variables
        super(vp,true);
        this.contacto = contacto;

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
                    grupoDeCampos[i] = new JTextField();
                    formulario.add(grupoDeCampos[i]);
                    break;
                case 1:
                    String[] nombresGrupos = vp.getCtc().getGrupos();
                    grupos = new JComboBox(nombresGrupos);
                    grupos.setEditable(false);
                    formulario.add(grupos);
            }
        }
        cp.add(BorderLayout.CENTER,formulario);

        // Crear los botones y añadirle el oyente
        JPanel botones = new JPanel();
        botones.setBorder(BorderFactory.createEmptyBorder(0,10,6,10));
        botones.setLayout(new FlowLayout(FlowLayout.RIGHT));
        botonAceptar = new JButton(OK);
        botonAceptar.addActionListener(new AceptarContactoActionListener(this,vp.getCtc(),vp.getCnc()));
        botonCancelar = new JButton(cancelar);
        botonCancelar.addActionListener(new RechazarContactoActionListener(this,vp.getCtc(),vp.getCnc(),contacto));
        botones.add(botonAceptar);
        botones.add(botonCancelar);
        cp.add(BorderLayout.SOUTH,botones);

        // Opciones del cuadro de diálogo
        this.setSize(290,185);
        this.setResizable(false);
        this.setLocationRelativeTo(vp);
        this.setDefaultCloseOperation(JDialog.DO_NOTHING_ON_CLOSE);
        this.setTitle(texto.getString("aceptar_contacto_formulario_title") + " " + contacto);
        this.setVisible(true);
    }

    /**
     * Devuelve el contacto que se va a añadir y los campos rellenados por el usuario.
     * @return Una lista con los campos introducidos en el formulario.
     */
    public List<String> getCampos(){

        // Crear la lista de campos
        List<String> campos = new ArrayList<String>();

        // Rellenar la lista con los valores adecuados
        campos.add(contacto);
        campos.add(grupoDeCampos[0].getText());
        if(grupos.getSelectedIndex() != -1)
            campos.add(grupos.getSelectedItem().toString());
        else
            campos.add("");

        return campos;
    }
}

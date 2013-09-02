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
import java.awt.Color;
import java.awt.Container;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ResourceBundle;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import org.jajim.interfaz.ventanas.VentanaPrincipal;
import org.jajim.main.Main;

/**
 * @author Florencio Cañizal Calles
 * @version 1.1
 * Clase formulario que muestra información acerca de la herramienta.
 */
public class AcercaDeFormulario extends JDialog implements ActionListener{

    private ResourceBundle texto = ResourceBundle.getBundle("resources.Idioma",Main.loc);

    // Cadenas constantes
    private final String[] etiquetas = {
        texto.getString("acerca_de_formulario_version"),
        texto.getString("acerca_de_formulario_autor"),
        texto.getString("acerca_de_formulario_pagina")
    };
    private final String[] valores = {
        "   1.1",
        "   Florencio Cañizal Calles",
        "   http://developer.berlios.de/projects/jajim/"
    };
    private final String licencia = "Jabber client." +
                                    "\nCopyright (C) 2010  Florencio Cañizal Calles" +
                                    "\n\nThis program is free software: you can redistribute it and/or modify" +
                                    "\nit under the terms of the GNU General Public License as published by" +
                                    "\nthe Free Software Foundation, either version 3 of the License, or" +
                                    "\n(at your option) any later version." +
                                    "\n\nThis program is distributed in the hope that it will be useful," +
                                    "\nbut WITHOUT ANY WARRANTY; without even the implied warranty of" +
                                    "\nMERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the" +
                                    "\nGNU General Public License for more details." +
                                    "\n\nYou should have received a copy of the GNU General Public License" +
                                    "\nalong with this program.  If not, see <http://www.gnu.org/licenses/>.";
    
    private final String aceptar = texto.getString("ok");

    // Componentes de la interfaz
    private JLabel[] grupoDeEtiquetas = new JLabel[etiquetas.length];
    private JLabel[] grupoDeValores = new JLabel[valores.length];
    private JTextArea areaLicencia;
    private JButton botonOK;

    /**
     * Constructor de la clase. Inicializa las variables necesarias y crea la
     * interfaz de usuario.
     * @param vp La ventana principal de la aplicación.
     */
    public AcercaDeFormulario(VentanaPrincipal vp){

        // Inicialización de variables
        super(vp,true);

        // Creación de la interfaz
        Container cp = this.getContentPane();

        // Añadir información de la herramienta
        JPanel panelInformacion = new JPanel(new GridLayout(etiquetas.length,1,5,10));
        panelInformacion.setBorder(BorderFactory.createEmptyBorder(10,30,20,30));
        for(int i = 0;i < etiquetas.length;i++){
            JPanel jp = new JPanel(new BorderLayout());
            grupoDeEtiquetas[i] = new JLabel(etiquetas[i]);
            grupoDeEtiquetas[i].setFont(new Font("serif",Font.BOLD,12));
            grupoDeValores[i] = new JLabel(valores[i]);
            jp.add(BorderLayout.WEST,grupoDeEtiquetas[i]);
            jp.add(BorderLayout.CENTER,grupoDeValores[i]);
            panelInformacion.add(jp);
        }
        cp.add(BorderLayout.NORTH,panelInformacion);

        // Añadir licencia
        JPanel panelLicencia = new JPanel(new BorderLayout());
        panelLicencia.setBorder(BorderFactory.createTitledBorder(BorderFactory.createCompoundBorder(BorderFactory.createEmptyBorder(0,10,10,10),BorderFactory.createLineBorder(Color.gray,1)),"GNU PUBLIC LICENSE"));
        areaLicencia = new JTextArea(licencia);
        areaLicencia.setEditable(false);
        panelLicencia.add(BorderLayout.CENTER,areaLicencia);
        cp.add(BorderLayout.CENTER,panelLicencia);

        // Botones
        JPanel botones = new JPanel(new FlowLayout(FlowLayout.CENTER));
        botones.setBorder(BorderFactory.createEmptyBorder(0,0,10,0));
        botonOK = new JButton(aceptar);
        botonOK.addActionListener(this);
        botones.add(botonOK);
        cp.add(BorderLayout.SOUTH,botones);

        // Opciones del cuadro de diálogo
        this.setSize(450,440);
        this.setResizable(false);
        this.setLocationRelativeTo(vp);
        this.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
        this.setTitle(texto.getString("acerca_de_formulario_title"));
        Image image = Toolkit.getDefaultToolkit().getImage(ClassLoader.getSystemResource("icons/acerca_de.png"));
        this.setIconImage(image);
        this.setVisible(true);
    }

    /**
     * Método de la interaz ActionListener. Se ejecuta cuando el usuario selecciona
     * la opción "Aceptar" del formulario. Cierra el cuadro de diálogo.
     * @param e El evento que produce la ejecución del método.
     */
    @Override
    public void actionPerformed(ActionEvent e) {
        this.dispose();
    }
}

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

import org.jajim.interfaz.listeners.ModificarGrupoDeContactosActionListener;
import org.jajim.interfaz.utilidades.PanelContactos;
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
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

/**
 * @author Florencio Cañizal Calles
 * @version 1.1
 * Clase formulario que recoge los datos que del grupo que el usuario desea modi
 * ficar.
 */
public class ModificarGrupoDeContactosFormulario extends JDialog implements ActionListener{

    private ResourceBundle texto = ResourceBundle.getBundle("resources.Idioma",Main.loc);

    // Cadena constantes
    private final String principal = texto.getString("modificar_grupo_de_contactos_formulario_principal");

    private final String[] etiquetas = {
        texto.getString("modificar_grupo_de_contactos_formulario_nombre")
    };

    private final String OK = texto.getString("ok");
    private final String cancelar = texto.getString("cancelar");

    // Componentes
    private JLabel cadenaPrincipal;
    private JLabel[] camposEtiquetas = new JLabel[etiquetas.length];
    private JTextField[] camposTexto = new JTextField[etiquetas.length];
    private JButton botonAceptar;
    private JButton botonCancelar;

    /**
     * Constructor de la clase. Inicializa las variables necesarias. Crea la inter
     * faz del formulario.
     * @param pc El panel de contactos de la ventana principal.
     * @param grupo El grupo en el que se van a realizar los cambios.
     */
    public ModificarGrupoDeContactosFormulario(PanelContactos pc,String grupo){

        // Inicialización
        super(pc.getVp(),true);

        // Creación de la interfaz
        Container cp = this.getContentPane();

        // Añadir la cadena principal al cuadro de diálogo
        cadenaPrincipal = new JLabel(principal);
        cadenaPrincipal.setHorizontalAlignment(JLabel.CENTER);
        cadenaPrincipal.setBorder(BorderFactory.createEmptyBorder(15,10,15,10));
        cp.add(BorderLayout.NORTH,cadenaPrincipal);

        // Crear el formulario
        JPanel formulario = new JPanel(new GridLayout(etiquetas.length,2));
        formulario.setBorder(BorderFactory.createEmptyBorder(0,10,15,10));
        for(int i = 0;i < etiquetas.length;i++){
            // Crear la etiqueta y añadirla
            camposEtiquetas[i] = new JLabel(etiquetas[i]);
            formulario.add(camposEtiquetas[i]);
            // Crear el campo de texto y añadirlo
            camposTexto[i] = new JTextField();
            formulario.add(camposTexto[i]);
        }
        cp.add(BorderLayout.CENTER,formulario);

        // Añadir los botones
        JPanel botones = new JPanel();
        botones.setLayout(new FlowLayout(FlowLayout.RIGHT));
        botones.setBorder(BorderFactory.createEmptyBorder(0,10,6,10));
        botonAceptar = new JButton(OK);
        botonAceptar.addActionListener(new ModificarGrupoDeContactosActionListener(this,grupo));
        botones.add(botonAceptar);
        botonCancelar = new JButton(cancelar);
        botonCancelar.addActionListener(this);
        botones.add(botonCancelar);
        cp.add(BorderLayout.SOUTH,botones);

        // Opciones del cuadro de diálogo
        this.setTitle(texto.getString("modificar_grupo_de_contactos_formulario_title"));
        this.setSize(290,148);
        this.setResizable(false);
        this.setLocationRelativeTo(pc.getVp());
        this.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
        this.setVisible(true);
    }

    /**
     * Método que se ejecuta cuando se selecciona el botón Cancelar del cuadro de
     * diálogo. Cierra el formulario.
     * @param e El evento que produce la ejecución del método.
     */
    @Override
    public void actionPerformed(ActionEvent e) {
        this.dispose();
    }

    /**
     * Método que retorna el valor de los campos introducidos por el usuario.
     * @return El valor de los campos introducidos por el usuario.
     */
    public String[] getCampos(){

        String[] campos = new String[camposTexto.length];

        // Recuperar el valor de los campos y retornarlo.
        for(int i = 0;i < camposTexto.length;i++){
            campos[i] = camposTexto[i].getText();
        }

        return campos;
    }
}

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
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.ListSelectionModel;
import org.jajim.controladores.ContactosControlador;
import org.jajim.interfaz.listeners.AñadirContactoAGrupoActionListener;
import org.jajim.interfaz.ventanas.VentanaPrincipal;
import org.jajim.main.Main;

/**
 * @author Florencio Cañizal Calles
 * @version 1.2 Clase que representa el formulario en el que el usuario introduce los grupos a los que se va a añadir un
 * determinado contacto.
 */
public class AñadirContactoAGrupoFormulario extends JDialog implements ActionListener {

    private final ResourceBundle texto = ResourceBundle.getBundle("resources.Idioma", Main.loc);

    // Cadenas constantes
    private final String principal = texto.getString("añadir_contacto_a_grupo_formulario_principal");

    private final String[] etiquetas = {
        texto.getString("añadir_contacto_a_grupo_formulario_grupos")
    };

    private final String OK = texto.getString("ok");
    private final String cancelar = texto.getString("cancelar");

    // Componentes de la interfaz
    private final JLabel cadenaPrincipal;
    private final JLabel[] grupoDeEtiquetas = new JLabel[etiquetas.length];
    private JList listaDeGrupos;
    private final JButton botonAceptar;
    private final JButton botonCancelar;

    /**
     * Constructor de la clase. Inicializa las variables necesarias. Crea la inter faz de usuario.
     * <p>
     * @param contacto El contacto que se va a añadir al conjunto de grupos.
     */
    public AñadirContactoAGrupoFormulario(String contacto) {

        // Inicialización de variables
        super(VentanaPrincipal.getInstancia(), true);

        // Creación de la interfaz
        Container cp = this.getContentPane();

        // Creación del mensaje principal
        cadenaPrincipal = new JLabel(principal);
        cadenaPrincipal.setHorizontalAlignment(JLabel.CENTER);
        cadenaPrincipal.setBorder(BorderFactory.createEmptyBorder(15, 10, 15, 10));
        cp.add(BorderLayout.NORTH, cadenaPrincipal);

        // Creación del formulario
        boolean sinGrupos = false;
        JPanel formulario = new JPanel();
        formulario.setBorder(BorderFactory.createEmptyBorder(0, 10, 15, 10));
        formulario.setLayout(new GridLayout(etiquetas.length, 2, 5, 10));
        for (int i = 0; i < etiquetas.length; i++) {
            grupoDeEtiquetas[i] = new JLabel(etiquetas[i]);
            grupoDeEtiquetas[i].setHorizontalAlignment(JLabel.CENTER);
            formulario.add(grupoDeEtiquetas[i]);
            String[] grupos = ContactosControlador.getInstancia().getGrupos(contacto);
            // Si no se recupera ningún grupo se añade un texto con dicha información
            if (grupos.length == 0) {
                grupos = new String[1];
                grupos[0] = texto.getString("añadir_contacto_a_grupo_formulario_sin_grupos");
                sinGrupos = true;
            }
            // Si hay grupos y uno es el grupo por defecto, asignarle un nombre
            // correcto
            else {
                for (int j = 0; j < grupos.length; j++) {
                    if (grupos[j].compareTo("") == 0) {
                        grupos[j] = texto.getString("sin_nombre");
                    }
                }
            }
            listaDeGrupos = new JList(grupos);
            listaDeGrupos.setVisibleRowCount(5);
            listaDeGrupos.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
            JScrollPane scrollPane = new JScrollPane(listaDeGrupos);
            formulario.add(scrollPane);
        }
        cp.add(BorderLayout.CENTER, formulario);

        // Crear los botones y añadirle el oyente
        JPanel botones = new JPanel();
        botones.setBorder(BorderFactory.createEmptyBorder(0, 10, 6, 10));
        botones.setLayout(new FlowLayout(FlowLayout.RIGHT));
        botonAceptar = new JButton(OK);
        if (sinGrupos) {
            botonAceptar.setEnabled(false);
        }
        botonAceptar.addActionListener(new AñadirContactoAGrupoActionListener(this, contacto));
        botonCancelar = new JButton(cancelar);
        botonCancelar.addActionListener(this);
        botones.add(botonAceptar);
        botones.add(botonCancelar);
        cp.add(BorderLayout.SOUTH, botones);

        // Opciones del cuadro de diálogo
        this.setSize(350, 210);
        this.setResizable(false);
        this.setLocationRelativeTo(VentanaPrincipal.getInstancia());
        this.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
        this.setTitle(texto.getString("añadir_contacto_a_grupo_formulario_title"));
        this.setVisible(true);
    }

    /**
     * Método que se ejecuta cuando el usuario selecciona la opción cancelar del formulario. Cierra el formulario.
     * <p>
     * @param e Evento que produce la ejecución del método.
     */
    @Override
    public void actionPerformed(ActionEvent e) {
        // Cerrar el cuadro de diálogo
        this.dispose();
    }

    /**
     * Método que retorna el valor de los campos introducidos por el usuario.
     * <p>
     * @return El valor de los campos introducidos por el usuario.
     */
    public String[] getCampos() {

        String[] campos = null;

        // Extraer los valores del formulario
        Object[] valores = listaDeGrupos.getSelectedValues();
        campos = new String[valores.length];
        for (int i = 0; i < valores.length; i++) {
            campos[i] = (String) valores[i];
        }

        return campos;
    }
}

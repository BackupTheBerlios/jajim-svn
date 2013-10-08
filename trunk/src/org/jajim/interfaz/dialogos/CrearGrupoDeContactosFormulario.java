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
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import org.jajim.controladores.ContactosControlador;
import org.jajim.interfaz.listeners.CrearGrupoDeContactosActionListener;
import org.jajim.interfaz.ventanas.VentanaPrincipal;
import org.jajim.main.Main;

/**
 * @author Florencio Cañizal Calles
 * @version 1.2
 * Clase formulario que permite al usuario introducir los datos necesarios para
 * crear un nuevo grupo de contactos.
 */
public class CrearGrupoDeContactosFormulario extends JDialog implements ActionListener{

    private ResourceBundle texto = ResourceBundle.getBundle("resources.Idioma",Main.loc);

    // Cadenas constantes
    private final String principal = texto.getString("crear_grupo_de_contactos_formulario_principal");

    private final String[] etiquetas = {
        texto.getString("crear_grupo_de_contactos_formulario_nombre"),
        texto.getString("crear_grupo_de_contactos_formulario_contactos")
    };

    private final String OK = texto.getString("ok");
    private final String cancelar = texto.getString("cancelar");

    // Componentes de la interfaz
    private JLabel cadenaPrincipal;
    private JLabel[] grupoDeEtiquetas = new JLabel[etiquetas.length];
    private JTextField[] grupoDeCampos = new JTextField[etiquetas.length - 1];
    private JList listaDeContactos;
    private JButton botonAceptar;
    private JButton botonCancelar;

    /**
     * Constructor de la clase. Inicializa las variables necesarias.
     */
    public CrearGrupoDeContactosFormulario(){

        // Inicialización
        super(VentanaPrincipal.getInstancia(), true);
        this.inicializar();
    }

    /**
     * Crea la interfaz de usuario. La ventana principal de la aplicación.
     */
    private void inicializar(){
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
        formulario.setLayout(new BorderLayout());
        JPanel nombre = new JPanel(new GridLayout(1,2,5,10));
        nombre.setBorder(BorderFactory.createEmptyBorder(0,0,5,0));
        JPanel lista = new JPanel(new GridLayout(1,2,5,10));
        for(int i = 0;i < etiquetas.length;i++){
            grupoDeEtiquetas[i] = new JLabel(etiquetas[i]);
            switch(i){
                case 0:
                    nombre.add(grupoDeEtiquetas[i]);
                    grupoDeCampos[i] = new JTextField();
                    nombre.add(grupoDeCampos[i]);
                    break;
                case 1:
                    lista.add(grupoDeEtiquetas[i]);
                    String[] contactos = ContactosControlador.getInstancia().getContactosPorNombre();
                    listaDeContactos = new JList(contactos);
                    listaDeContactos.setVisibleRowCount(5);
                    listaDeContactos.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
                    JScrollPane scrollPane = new JScrollPane(listaDeContactos);
                    lista.add(scrollPane);
                    break;
            }
        }
        formulario.add(BorderLayout.NORTH,nombre);
        formulario.add(BorderLayout.CENTER,lista);
        cp.add(BorderLayout.CENTER,formulario);

        // Crear los botones y añadirle el oyente
        JPanel botones = new JPanel();
        botones.setLayout(new FlowLayout(FlowLayout.RIGHT));
        botones.setBorder(BorderFactory.createEmptyBorder(0,10,6,10));
        botonAceptar = new JButton(OK);
        botonAceptar.addActionListener(new CrearGrupoDeContactosActionListener(this));
        botonCancelar = new JButton(cancelar);
        botonCancelar.addActionListener(this);
        botones.add(botonAceptar);
        botones.add(botonCancelar);
        cp.add(BorderLayout.SOUTH,botones);

        // Opciones del cuadro de diálogo
        this.setSize(320,270);
        this.setResizable(false);
        this.setLocationRelativeTo(VentanaPrincipal.getInstancia());
        this.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
        this.setTitle(texto.getString("crear_grupo_de_contactos_formulario_title"));
        this.setVisible(true);
    }

    /**
     * Método que se ejecuta cuando el usuario pulsa el botón Cancelar del cuadro
     * de diálogo. Cierra el mismo.
     * @param e
     */
    @Override
    public void actionPerformed(ActionEvent e) {
        this.dispose();
    }

    /**
     * Devuelve el valor de los campos introducidos por el usuario.
     * @return El valor de los campos introducidos por el usuario.
     */
    public String[] getCampos(){

        String[] campos = new String[grupoDeCampos.length + listaDeContactos.getSelectedValues().length];

        // Extraer la información de los campos
        for(int i = 0;i < grupoDeCampos.length;i++){
            campos[i] = grupoDeCampos[i].getText();
        }

        Object[] listaValores = listaDeContactos.getSelectedValues();

        for(int i = 0;i < listaValores.length;i++){
            campos[i + grupoDeCampos.length] = (String) listaValores[i];
        }

        return campos;
    }
}

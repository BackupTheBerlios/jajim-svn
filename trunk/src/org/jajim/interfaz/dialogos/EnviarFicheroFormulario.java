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

import org.jajim.controladores.ConversacionControlador;
import org.jajim.interfaz.listeners.EnviarFicheroActionListener;
import org.jajim.interfaz.ventanas.VentanaConversacion;
import org.jajim.main.Main;
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
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JDialog;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import org.jivesoftware.smack.util.StringUtils;

/**
 * @author Florencio Cañizal Calles
 * @version 1.1
 * Clase diálog que muestra un formulario al usuario para que rellene la informa
 * cion necesaria para enviar un fichero.
 */
public class EnviarFicheroFormulario extends JDialog implements ActionListener{

    private ResourceBundle texto = ResourceBundle.getBundle("resources.Idioma",Main.loc);

    // Cadenas constantes
    private final String principal = texto.getString("enviar_fichero_formulario_principal");

    private final String[] etiquetas = {
        texto.getString("enviar_fichero_formulario_contactos"),
        texto.getString("enviar_fichero_formulario_fichero"),
        texto.getString("enviar_fichero_formulario_descripcion")
    };

    private final String OK = texto.getString("ok");
    private final String cancelar = texto.getString("cancelar");

    // Componentes de la interfaz
    private JLabel cadenaPrincipal;
    private JLabel etiquetaContactos;
    private JLabel etiquetaFichero;
    private JLabel etiquetaDescripcion;
    private JCheckBox[] contactos;
    private JTextField fichero;
    private JTextArea descripcion;
    private JButton botonFichero;
    private JButton botonAceptar;
    private JButton botonCancelar;

    /**
     * Constructor de la clase. Inicializa la interfaz del cuadro de diálogo.
     * @param vc Ventana de la conversación
     */
    public EnviarFicheroFormulario(VentanaConversacion vc){

        // Inicialización de variables
        super(vc,true);

        // Creación de la interfaz
        Container cp = this.getContentPane();

        // Creación del mensaje principal
        cadenaPrincipal = new JLabel(principal);
        cadenaPrincipal.setHorizontalAlignment(JLabel.CENTER);
        cadenaPrincipal.setBorder(BorderFactory.createEmptyBorder(15,30,15,30));
        cp.add(BorderLayout.NORTH,cadenaPrincipal);

        // Creación del formulario
        JPanel formulario = new JPanel();
        formulario.setLayout(new BorderLayout());
        formulario.setBorder(BorderFactory.createEmptyBorder(0,10,15,10));

        JPanel panelContactos = new JPanel();
        ConversacionControlador cvc = vc.getCvc();
        String[] participantes = null;
        participantes = cvc.getParticipantes();
        panelContactos.setLayout(new GridLayout(participantes.length + 1,1));
        etiquetaContactos = new JLabel(etiquetas[0]);
        panelContactos.add(etiquetaContactos);
        contactos = new JCheckBox[participantes.length];
        for(int i = 0;i < participantes.length;i++){
            participantes[i] = StringUtils.parseBareAddress(participantes[i]);
            contactos[i] = new JCheckBox(participantes[i]);
            panelContactos.add(contactos[i]);
        }
        formulario.add(BorderLayout.NORTH,panelContactos);

        JPanel panelFichero = new JPanel();
        panelFichero.setLayout(new GridLayout(1,2,5,10));
        panelFichero.setBorder(BorderFactory.createEmptyBorder(10,0,10,0));
        etiquetaFichero = new JLabel(etiquetas[1]);
        panelFichero.add(etiquetaFichero);
        JPanel textoYBoton = new JPanel(new BorderLayout());
        fichero = new JTextField();
        textoYBoton.add(BorderLayout.CENTER,fichero);
        botonFichero = new JButton(new ImageIcon(ClassLoader.getSystemResource("icons/ruta.png")));
        botonFichero.setActionCommand("seleccionar fichero");
        botonFichero.addActionListener(this);
        textoYBoton.add(BorderLayout.EAST,botonFichero);
        panelFichero.add(textoYBoton);
        formulario.add(BorderLayout.CENTER,panelFichero);

        JPanel panelDescripcion = new JPanel(new GridLayout(1,2,5,10));
        etiquetaDescripcion = new JLabel(etiquetas[2]);
        panelDescripcion.add(etiquetaDescripcion);
        descripcion = new JTextArea();
        descripcion.setLineWrap(true);
        descripcion.setRows(2);
        descripcion.setWrapStyleWord(true);
        JScrollPane scrollPane = new JScrollPane(descripcion);
        panelDescripcion.add(scrollPane);
        formulario.add(BorderLayout.SOUTH,panelDescripcion);

        cp.add(BorderLayout.CENTER,formulario);

        // Crear los botones y añadirle el oyente
        JPanel botones = new JPanel();
        botones.setLayout(new FlowLayout(FlowLayout.RIGHT));
        botones.setBorder(BorderFactory.createEmptyBorder(0,10,6,10));
        botonAceptar = new JButton(OK);
        botonAceptar.addActionListener(new EnviarFicheroActionListener(this,vc.getTfc(),vc.getCvc(),vc.getVgt()));
        botonCancelar = new JButton(cancelar);
        botonCancelar.setActionCommand("cancelar");
        botonCancelar.addActionListener(this);
        botones.add(botonAceptar);
        botones.add(botonCancelar);
        cp.add(BorderLayout.SOUTH,botones);

        // Opciones del cuadro de diálogo
        this.pack();
        this.setResizable(false);
        this.setLocationRelativeTo(vc);
        this.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
        this.setTitle(texto.getString("enviar_fichero_formulario_title"));
        this.setVisible(true);
    }

    /**
     * Método que se ejecuta cuando se produce uno de los eventos a los que está
     * registrado la clase.
     * @param e Evento que produce la ejecución del método.
     */
    @Override
    public void actionPerformed(ActionEvent e) {

        // Comprobar que ha producido la activación del método
        if(e.getActionCommand().compareTo("seleccionar fichero") == 0){
            JFileChooser selector = new JFileChooser();
            selector.updateUI();
            selector.setDialogType(JFileChooser.OPEN_DIALOG);
            int valorDeRetorno = selector.showOpenDialog(this);
            if(valorDeRetorno == JFileChooser.APPROVE_OPTION)
                fichero.setText(selector.getSelectedFile().getPath());
        }else if(e.getActionCommand().compareTo("cancelar") == 0){
            this.dispose();
        }
    }

    public List<String> getCampos(){

        // Recuperar los valores introducidos en el formulario y guardarlos en la
        // lista
        List<String> campos = new ArrayList<String>();
        campos.add(fichero.getText());
        campos.add(descripcion.getText());
        for(int i = 0;i < contactos.length;i++){
            if(contactos[i].isSelected())
                campos.add(contactos[i].getText());
        }

        return campos;
    }
}

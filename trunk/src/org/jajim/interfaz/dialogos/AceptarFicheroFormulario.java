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
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import org.jajim.interfaz.listeners.AceptarFicheroActionListener;
import org.jajim.interfaz.listeners.RechazarFicheroActionListener;
import org.jajim.interfaz.ventanas.VentanaPrincipal;
import org.jajim.main.Main;

/**
 * @author Florencio Cañizal Calles
 * @version 1.2
 * Clase diálogo que muestra recoge los datos necesarios para poder guardar el
 * fichero.
 */
public class AceptarFicheroFormulario extends JDialog implements ActionListener{

    private ResourceBundle texto = ResourceBundle.getBundle("resources.Idioma",Main.loc);

    // Cadenas constantes
    private final String cadenaPrincipal = texto.getString("aceptar_fichero_formulario_cadena_principal");
    private final String[] etiquetas = {
        texto.getString("aceptar_fichero_formulario_ruta")
    };

    private final String OK = texto.getString("ok");
    private final String cancelar = texto.getString("cancelar");

    // Componentes de la interfaz
    private JLabel principal;
    private JLabel[] cadenasEtiquetas = new JLabel[etiquetas.length];
    private JTextField[] camposDeTexto = new JTextField[etiquetas.length];
    private JButton botonRuta;
    private JButton botonAceptar;
    private JButton botonCancelar;

    // Variable importantes
    private int idTransferencia;
    
    /**
     * Constructor de la clase. Inicializa las variables necesarias y crea la in
     * terfaz del formulario.
     * @param idTransferencia Identificador de la transferencia que se quiere lle
     * var a cabo.
     */
    public AceptarFicheroFormulario(int idTransferencia){

        // Inicialización de variables
        super(VentanaPrincipal.getInstancia(),true);
        VentanaPrincipal vp = VentanaPrincipal.getInstancia();
        this.idTransferencia = idTransferencia;

        // Creación de la interfaz
        Container cp = this.getContentPane();

        // Añadir etiqueta principal
        principal = new JLabel(cadenaPrincipal);
        principal.setHorizontalAlignment(JLabel.CENTER);
        principal.setBorder(BorderFactory.createEmptyBorder(15,10,15,10));
        cp.add(BorderLayout.NORTH,principal);

        // Añadir el panel del formulario
        JPanel panelFormulario = new JPanel(new GridLayout(etiquetas.length,2,5,10));
        panelFormulario.setBorder(BorderFactory.createEmptyBorder(0,10,15,10));
        for(int i = 0;i < etiquetas.length;i++){
            cadenasEtiquetas[i] = new JLabel(etiquetas[i]);
            panelFormulario.add(cadenasEtiquetas[i]);
            if(i == 0){
                JPanel textoYBoton = new JPanel(new BorderLayout());
                camposDeTexto[i] = new JTextField();
                botonRuta = new JButton(new ImageIcon(ClassLoader.getSystemResource("icons/ruta.png")));
                botonRuta.addActionListener(this);
                textoYBoton.add(BorderLayout.CENTER,camposDeTexto[i]);
                textoYBoton.add(BorderLayout.EAST,botonRuta);
                panelFormulario.add(textoYBoton);
            }
        }
        cp.add(BorderLayout.CENTER,panelFormulario);

        // Añadir los botones
        JPanel botones = new JPanel();
        botones.setLayout(new FlowLayout(FlowLayout.RIGHT));
        botones.setBorder(BorderFactory.createEmptyBorder(0,10,6,10));
        botonAceptar = new JButton(OK);
        botonAceptar.addActionListener(new AceptarFicheroActionListener(this, vp.getVgt()));
        botonCancelar = new JButton(cancelar);
        botonCancelar.addActionListener(new RechazarFicheroActionListener(this,idTransferencia));
        botones.add(botonAceptar);
        botones.add(botonCancelar);
        cp.add(BorderLayout.SOUTH,botones);

        // Opciones del cuadro de diálogo
        this.setTitle(texto.getString("aceptar_fichero_formulario_title"));
        this.setSize(260,150);
        this.setResizable(false);
        this.setLocationRelativeTo(vp);
        this.setDefaultCloseOperation(JDialog.DO_NOTHING_ON_CLOSE);
        this.setVisible(true);
    }

    /**
     * Método que devuelve valores importantes extraídos del formulario.
     * @return Un array con los valores extraídos del formulario.
     */
    public String[] getCampos(){

        // Crear el array a devolver
        String[] campos = new String[2];
        
        // Asignar los valores adecuados al array
        campos[0] = String.valueOf(idTransferencia);
        campos[1] = camposDeTexto[0].getText();

        return campos;
    }

    /**
     * Método que se ejecuta cuando se activa uno de los eventos para los que es
     * tá registrada la clase.
     * @param e Evento que produce la ejecución del método.
     */
    @Override
    public void actionPerformed(ActionEvent e) {

        // Crear el selector de rutas
        JFileChooser selector = new JFileChooser();
        selector.updateUI();
        selector.setDialogType(JFileChooser.SAVE_DIALOG);
        selector.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
        int valorDeRetorno = selector.showOpenDialog(this);
        if(valorDeRetorno == JFileChooser.APPROVE_OPTION) {
            camposDeTexto[0].setText(selector.getSelectedFile().getAbsolutePath());
        }
    }
}

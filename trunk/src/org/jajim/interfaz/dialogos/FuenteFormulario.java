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

import org.jajim.controladores.PreferenciasControlador;
import org.jajim.interfaz.listeners.FuenteActionListener;
import org.jajim.main.Main;
import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.Dimension;
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
import javax.swing.JTextPane;
import javax.swing.ListSelectionModel;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import org.jajim.interfaz.ventanas.VentanaConversacion;

/**
 * @author Florencio Cañizal Calles
 * @version 1.1
 * Clase formulario que permite cambiar el estilo de la fuente que se está utili
 * zando en una conversación.
 */
public class FuenteFormulario extends JDialog implements ListSelectionListener,ActionListener{

    private ResourceBundle texto = ResourceBundle.getBundle("resources.Idioma",Main.loc);

     // Cadenas constantes
    private final String principal = texto.getString("fuente_formulario_principal");

    private final String[] etiquetas = {
        texto.getString("fuente_formulario_fuente"),
        texto.getString("fuente_formulario_tamaño"),
        texto.getString("fuente_formulario_prueba")
    };

    private final String OK = texto.getString("ok");
    private final String cancelar = texto.getString("cancelar");

    private final String[] tiposFuente = {
        "arial",
        "courier",
        "courier new",
        "geneva",
        "georgia",
        "helvetica",
        "roman",
        "sans-serif",
        "serif",
        "times",
        "times new roman",
        "verdana"
    };

    private final String[] tiposTamaño = {"6","7","8","9","10","11","12","13","14","15","16","17","18","19","20"};

    private final String textoPrueba = texto.getString("fuente_formulario_texto_prueba");

    // Componentes de la interfaz
    private JLabel cadenaPrincipal;
    private JLabel[] grupoDeEtiquetas = new JLabel[etiquetas.length - 1];
    private JList[] grupoDeCampos = new JList[etiquetas.length - 1];
    private JTextPane prueba;
    private JButton botonAceptar;
    private JButton botonCancelar;

    /**
     * Constructor de la clase. Inicializa la variables necesarias y crea la inter
     * faz de usuario.
     * @param vc La ventana de la conversación.
     */
    public FuenteFormulario(VentanaConversacion vc){

        // Inicialización de variables
        super(vc,true);

        // Creación de la interfaz
        Container cp = this.getContentPane();
        JPanel panelCentral = new JPanel(new BorderLayout());
        cp.add(BorderLayout.CENTER,panelCentral);

        // Creación del mensaje principal
        cadenaPrincipal = new JLabel(principal);
        cadenaPrincipal.setHorizontalAlignment(JLabel.CENTER);
        cadenaPrincipal.setBorder(BorderFactory.createEmptyBorder(15,10,15,10));
        cp.add(BorderLayout.NORTH,cadenaPrincipal);

        // Creación del formulario
        JPanel panelEstilos = new JPanel();
        panelEstilos.setLayout(new FlowLayout());
        for(int i = 0;i < (etiquetas.length - 1);i++){
            JPanel panel = new JPanel(new BorderLayout());
            grupoDeEtiquetas[i] = new JLabel(etiquetas[i]);
            grupoDeEtiquetas[i].setBorder(BorderFactory.createEmptyBorder(0,10,0,10));
            panel.add(BorderLayout.WEST,grupoDeEtiquetas[i]);
            switch(i){
                case 0:
                    grupoDeCampos[i] = new JList(tiposFuente);
                    break;
                case 1:
                    grupoDeCampos[i] = new JList(tiposTamaño);
                    break;
            }
            grupoDeCampos[i].setVisibleRowCount(5);
            grupoDeCampos[i].setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
            grupoDeCampos[i].addListSelectionListener(this);
            JScrollPane scrollPane = new JScrollPane(grupoDeCampos[i]);
            panel.add(BorderLayout.CENTER,scrollPane);
            panelEstilos.add(panel);
        }
        panelCentral.add(BorderLayout.CENTER,panelEstilos);

        JPanel panelPrueba = new JPanel(new GridLayout(1,1));
        prueba = new JTextPane();
        Dimension textoDimension = new Dimension(120,60);
        prueba.setSize(textoDimension);
        prueba.setMinimumSize(textoDimension);
        prueba.setMaximumSize(textoDimension);
        prueba.setPreferredSize(textoDimension);
        prueba.setEditable(false);
        prueba.setContentType("text/html");
        panelPrueba.add(prueba);

        panelCentral.add(BorderLayout.SOUTH,panelPrueba);

        // Seleccionar los valores utilizados en este momento
        PreferenciasControlador pfc = PreferenciasControlador.getInstancia();
        String fuenteUtilizada = pfc.getFuente();
        int tamañoUtilizado = pfc.getTamaño();

        int fu = 0;
        for(int i = 0;i < tiposFuente.length;i++){
            if(fuenteUtilizada.compareTo(tiposFuente[i]) == 0){
                fu = i;
                break;
            }
        }

        grupoDeCampos[0].setSelectedIndex(fu);
        grupoDeCampos[1].setSelectedIndex(tamañoUtilizado - 5);

        // Crear los botones y añadirle el oyente
        JPanel botones = new JPanel();
        botones.setBorder(BorderFactory.createEmptyBorder(15,10,6,10));
        botones.setLayout(new FlowLayout(FlowLayout.RIGHT));
        botonAceptar = new JButton(OK);
        botonAceptar.addActionListener(new FuenteActionListener(this,vc));
        botonCancelar = new JButton(cancelar);
        botonCancelar.addActionListener(this);
        botones.add(botonAceptar);
        botones.add(botonCancelar);
        cp.add(BorderLayout.SOUTH,botones);

        // Opciones del cuadro de diálogo
        this.setSize(340,310);
        this.setResizable(false);
        this.setLocationRelativeTo(vc);
        this.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
        this.setTitle(texto.getString("fuente_formulario_title"));
        this.setVisible(true);
    }

    /**
     * Método que se ejecuta cuando el usuario selecciona un elemento de cualquie
     * ra de las listas. Actualiza el valor del cuadro de muestra.
     * @param e El evento que produce la ejecución del método.
     */
    @Override
    public void valueChanged(ListSelectionEvent e) {
        
        // Recuperar los valores de las listas y actualizar el texto.
        String fuente = (String) grupoDeCampos[0].getSelectedValue();
        String tamaño = (String) grupoDeCampos[1].getSelectedValue();

        String cadena = "<center><font style=\"font-family:" + fuente + ";font-size:" + tamaño +  "\">" + textoPrueba + "</font></center>";
        prueba.setText(cadena);
    }

    /**
     * Método que se ejecuta cuando el usuario selecciona la opción "Cancelar"
     * del formulario. Cierra el formulario.
     * @param e El evento que produce la ejecución del método.
     */
    @Override
    public void actionPerformed(ActionEvent e) {
        this.dispose();
    }

    /**
     * Devuelve un array con el valor de los campos introducidos por el usuario.
     * @return Un array con el valor de los campos introducidos por el usuario
     */
    public String[] getCampos(){

        String[] campos = new String[grupoDeCampos.length];

        // Extraer los campos del formulario
        for(int i = 0;i < campos.length;i++){
            campos[i] = (String) grupoDeCampos[i].getSelectedValue();
        }

        return campos;
    }
}

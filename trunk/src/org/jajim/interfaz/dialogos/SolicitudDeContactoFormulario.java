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

import org.jajim.interfaz.listeners.SolicitudDeContactoActionListener;
import org.jajim.interfaz.ventanas.VentanaPrincipal;
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
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import org.jajim.controladores.ContactosControlador;

/**
 * @author Florencio Cañizal Calles
 * @version 1.1
 * Clase diálogo que muestra un formulario para que el usuario introduzca los da
 * tos del contacto que quiere establecer. Controla la selección del botón Cance
 * lar cerrando el cuadro de diálogo en caso de que se produzca.
 */
public class SolicitudDeContactoFormulario extends JDialog implements ActionListener{

    private ResourceBundle texto = ResourceBundle.getBundle("resources.Idioma",Main.loc);

    // Cadenas constantes
    private final String principal = texto.getString("solicitud_contacto_formulario_principal");

    private final String[] etiquetas = {
        texto.getString("solicitud_contacto_formulario_identificador"),
        texto.getString("solicitud_contacto_formulario_servidor"),
        texto.getString("solicitud_contacto_formulario_alias"),
        texto.getString("solicitud_contacto_formulario_grupo")
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

    private VentanaPrincipal vp;
    private String identificador;
    private String servidor;

    /**
     * Constructor de la clase. Crea la interfaz del cuadro de diálogo.
     * @param vp Ventana principal de la aplicación.
     */
    public SolicitudDeContactoFormulario(VentanaPrincipal vp){

        // Inicialización
        super(vp,true);
        this.vp = vp;
        this.identificador = null;
        this.servidor = null;

        this.setLocationRelativeTo(vp);

        this.inicializar();
    }

    /**
     * Constructor de la clase. Inicializa las variables necesarias.
     * @param bcf El formulario de busqueda de contactos.
     * @param identificador El identificador de la cuenta que se quiere establecer
     * como contacto.
     * @param servidor El servidor de la cuenta que se quiere establecer como contacto.
     */
    public SolicitudDeContactoFormulario(BuscarContactoFormulario bcf,VentanaPrincipal vp,String identificador,String servidor){

        // Inicialización
        super(bcf,true);
        this.vp = vp;
        this.identificador = identificador;
        this.servidor = servidor;

        this.setLocationRelativeTo(bcf);

        this.inicializar();
    }

    /**
     * Inicializa la interfaz de la aplicación.
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
        formulario.setLayout(new GridLayout(etiquetas.length,2,5,10));
        for(int i = 0;i < etiquetas.length;i++){
            grupoDeEtiquetas[i] = new JLabel(etiquetas[i]);
            formulario.add(grupoDeEtiquetas[i]);
            switch(i){
                case 0:
                case 1:
                case 2:
                    grupoDeCampos[i] = new JTextField();
                    if(identificador != null && servidor != null){
                        if(i == 0){
                            grupoDeCampos[i].setText(identificador);
                            grupoDeCampos[i].setEnabled(false);
                        }
                        if(i == 1){
                            grupoDeCampos[i].setText(servidor);
                            grupoDeCampos[i].setEnabled(false);
                        }
                    }
                    formulario.add(grupoDeCampos[i]);
                    break;
                case 3:
                    String[] nombresGrupos = ContactosControlador.getInstancia().getGrupos();
                    grupos = new JComboBox(nombresGrupos);
                    formulario.add(grupos);
                    break;
            }
        }
        cp.add(BorderLayout.CENTER,formulario);

        // Crear los botones y añadirle el oyente
        JPanel botones = new JPanel();
         botones.setLayout(new FlowLayout(FlowLayout.RIGHT));
        botones.setBorder(BorderFactory.createEmptyBorder(0,10,6,10));
        botonAceptar = new JButton(OK);
        botonAceptar.addActionListener(new SolicitudDeContactoActionListener(this));
        botonCancelar = new JButton(cancelar);
        botonCancelar.addActionListener(this);
        botonCancelar.setActionCommand("Cancelar");
        botones.add(botonAceptar);
        botones.add(botonCancelar);
        cp.add(BorderLayout.SOUTH,botones);

        // Opciones del cuadro de diálogo
        this.setSize(290,255);
        this.setResizable(false);
        this.setLocationRelativeTo(vp);
        this.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
        this.setTitle(texto.getString("solicitud_contacto_formulario_title"));
        this.setVisible(true);
    }

    /**
     * Método que se ejecuta despueés de la recpeción de un evento para los que
     * estaba registrado. Cierra el cuadro de diálogo.
     * @param e Evento que ha producido la ejecución del método.
     */
    @Override
    public void actionPerformed(ActionEvent e){
        this.dispose();
    }

    /**
     * Devuelve el valor de los campos introducidos en el formulario.
     * @return Lista con el valor de los campos del formulario.
     */
    public List<String> getCampos(){

        List<String> campos = new ArrayList<String>();

        // Extraer los campos del formulario y añadirlos a la lista
        for(int i = 0;i < grupoDeCampos.length;i++){
            campos.add(grupoDeCampos[i].getText());
        }

        if(grupos.getSelectedIndex() != -1)
            campos.add(grupos.getSelectedItem().toString());
        else
            campos.add("");
        
        return campos;
    }
}

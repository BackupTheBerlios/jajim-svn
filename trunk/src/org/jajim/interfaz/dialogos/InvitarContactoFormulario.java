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

import org.jajim.interfaz.listeners.InvitarContactoActionListener;
import org.jajim.interfaz.ventanas.VentanaConversacion;
import org.jajim.main.Main;
import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
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

/**
 * @author Florencio Cañizal Calles
 * @version 1.1
 * Formulario en el que el usuario introduce los contactos a los que desea invitar
 * a la conversación.
 */
public class InvitarContactoFormulario extends JDialog implements ActionListener{

    private ResourceBundle texto = ResourceBundle.getBundle("resources.Idioma",Main.loc);

    // Cadenas constantes
    private final String principal = texto.getString("invitar_contacto_formulario_principal");

    private final String[] etiquetas = {
        texto.getString("invitar_contacto_formulario_contactos")
    };

    private final String OK = texto.getString("ok");
    private final String cancelar = texto.getString("cancelar");

    // Componentes de la interfaz
    private JLabel cadenaPrincipal;
    private JLabel[] grupoDeEtiquetas = new JLabel[etiquetas.length];
    private JList listaDeContactos;
    private JButton botonAceptar;
    private JButton botonCancelar;

    /**
     * Constructor de la clase. Inicializa la variables necesarias.
     * @param vc La ventana de la conversación a la que se quiere invitar al con
     * tacto.
     */
    public InvitarContactoFormulario(VentanaConversacion vc){

        // Inicialización de variables
        super(vc,true);

         // Creación de la interfaz
        Container cp = this.getContentPane();

        // Creación del mensaje principal
        cadenaPrincipal = new JLabel(principal);
        cadenaPrincipal.setHorizontalAlignment(JLabel.CENTER);
        cadenaPrincipal.setBorder(BorderFactory.createEmptyBorder(15,10,15,10));
        cp.add(BorderLayout.NORTH,cadenaPrincipal);

        // Creación del formulario
        boolean sinContactos = false;
        JPanel formulario = new JPanel();
        formulario.setBorder(BorderFactory.createEmptyBorder(0,10,15,10));
        formulario.setLayout(new GridLayout(etiquetas.length,2,5,10));
        for(int i = 0;i < etiquetas.length;i++){
            grupoDeEtiquetas[i] = new JLabel(etiquetas[i]);
            grupoDeEtiquetas[i].setHorizontalAlignment(JLabel.CENTER);
            formulario.add(grupoDeEtiquetas[i]);
            String[] participantes = null;
            try{
                participantes = vc.getParticipantes();
            }catch(Exception e){}
            String[] contactos = ContactosControlador.getInstancia().getContactosPorNombre();
            List<String> aux = Arrays.asList(contactos);
            List<String> listaContactos = new ArrayList<String>();
            listaContactos.addAll(aux);

            int k = 0;
            for(int j = 0;j < participantes.length;j++){
                if(listaContactos.contains(participantes[j])){
                    int indice = listaContactos.indexOf(participantes[j]);
                    listaContactos.remove(indice);
                }
            }
            String[] sinParticipar = listaContactos.toArray(new String[0]);
            
            if(sinParticipar.length == 0){
                sinParticipar = new String[1];
                sinParticipar[0] = texto.getString("invitar_contacto_formulario_sin_contactos");
                sinContactos = true;
            }
            listaDeContactos = new JList(sinParticipar);
            listaDeContactos.setVisibleRowCount(5);
            listaDeContactos.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
            JScrollPane scrollPane = new JScrollPane(listaDeContactos);
            formulario.add(scrollPane);
        }
        cp.add(BorderLayout.CENTER,formulario);

        // Crear los botones y añadirle el oyente
        JPanel botones = new JPanel();
        botones.setLayout(new FlowLayout(FlowLayout.RIGHT));
        botones.setBorder(BorderFactory.createEmptyBorder(0,10,6,10));
        botonAceptar = new JButton(OK);
        if(sinContactos)
            botonAceptar.setEnabled(false);
        botonAceptar.addActionListener(new InvitarContactoActionListener(this,vc.getCvc()));
        botonCancelar = new JButton(cancelar);
        botonCancelar.addActionListener(this);
        botones.add(botonAceptar);
        botones.add(botonCancelar);
        cp.add(BorderLayout.SOUTH,botones);

        // Opciones del cuadro de diálogo
        this.setSize(350,210);
        this.setResizable(false);
        this.setLocationRelativeTo(vc);
        this.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
        this.setTitle(texto.getString("invitar_contacto_formulario_title"));
        this.setVisible(true);
    }

    /**
     * Método que se ejecuta cuando el usuario selecciona el botón cancelar del
     * formulario. Cierra el mismo.
     * @param e El evento que produce la ejecución del método.
     */
    @Override
    public void actionPerformed(ActionEvent e) {
        this.dispose();
    }

    /**
     * Retorna el valor de los campos introducidos por el usuario.
     * @return El valor de los campos introducidos por el usuario.
     */
    public String[] getCampos(){

        String[] campos = null;

        // Extraer los valores del formulario
        Object[] valores = listaDeContactos.getSelectedValues();
        campos = new String[valores.length];
        for(int i = 0;i < valores.length;i++)
            campos[i] = (String) valores[i];

        return campos;
    }
}

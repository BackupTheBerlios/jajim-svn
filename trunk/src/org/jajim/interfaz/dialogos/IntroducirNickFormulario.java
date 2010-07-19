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

import org.jajim.interfaz.listeners.AceptarInvitacionActionListener;
import org.jajim.interfaz.listeners.RechazarInvitacionActionListener;
import org.jajim.interfaz.ventanas.VentanaPrincipal;
import org.jajim.main.Main;
import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.FlowLayout;
import java.awt.GridLayout;
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
 * Clase formulario que permite al usuario introducir el nick que va a utilizar
 * en una conversación.
 */
public class IntroducirNickFormulario extends JDialog{

    private ResourceBundle texto = ResourceBundle.getBundle("resources.Idioma",Main.loc);

    // Cadenas constantes
    private final String principal = texto.getString("introducir_nick_formulario_principal");

    private final String[] etiquetas = {
        texto.getString("introducir_nick_formulario_nick")
    };

    private final String OK = texto.getString("ok");
    private final String cancelar = texto.getString("cancelar");

    // Componentes de la interfaz
    private JLabel cadenaPrincipal;
    private JLabel[] grupoDeEtiquetas = new JLabel[etiquetas.length];
    private JTextField[] grupoDeCampos = new JTextField[etiquetas.length];
    private JButton botonAceptar;
    private JButton botonCancelar;

    /**
     * Constructor de la clase. Inicializa las variables necesarias y crea la in
     * terfaz de usuario.
     * @param vp La ventana principal de la aplicación.
     * @param idInvitacion El identificador de la invitación recibida.
     * @param room La sala en la que está teniendo lugar la conversación.
     * @param alias El alias del contacto que realizó la invitación.
     */
    public IntroducirNickFormulario(VentanaPrincipal vp,String idInvitacion,String room,String alias){

        // Inicialización de variables
        super(vp,true);

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
            grupoDeCampos[i] = new JTextField();
            formulario.add(grupoDeCampos[i]);
        }
        cp.add(BorderLayout.CENTER,formulario);

        // Crear los botones y añadirle el oyente
        JPanel botones = new JPanel();
        botones.setBorder(BorderFactory.createEmptyBorder(0,10,6,10));
        botones.setLayout(new FlowLayout(FlowLayout.RIGHT));
        botonAceptar = new JButton(OK);
        botonAceptar.addActionListener(new AceptarInvitacionActionListener(this,vp,alias,idInvitacion,room));
        botonCancelar = new JButton(cancelar);
        botonCancelar.addActionListener(new RechazarInvitacionActionListener(this,vp.getCtc().getContactoPorAlias(alias),room));
        botones.add(botonAceptar);
        botones.add(botonCancelar);
        cp.add(BorderLayout.SOUTH,botones);

        // Opciones del cuadro de diálogo
        this.setSize(280,147);
        this.setResizable(false);
        this.setLocationRelativeTo(vp);
        this.setDefaultCloseOperation(JDialog.DO_NOTHING_ON_CLOSE);
        this.setTitle(texto.getString("introducir_nick_formulario_title"));
        this.setVisible(true);
    }

    /**
     * Retorna el valor de los campos introducidos por el usuario.
     * @return El valor de los campos introducidos por el usuario.
     */
    public String[] getCampos(){

        String[] campos = new String[etiquetas.length];

        // Recupera el valor de los campos del formulario.
        for(int i = 0;i < campos.length;i++)
            campos[i] = grupoDeCampos[i].getText();

        return campos;
    }
}

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
import java.awt.Dialog;
import java.awt.FlowLayout;
import java.awt.Window;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ResourceBundle;
import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import org.jajim.main.Main;

/**
 * @author Florencio Cañizal Calles
 * @version 1.1
 * Cuadro de diálogo que muestra los errores producidos al usuario.
 */
public class MensajeError extends JDialog implements ActionListener{

    private ResourceBundle texto = ResourceBundle.getBundle("resources.Idioma",Main.loc);

    // Tipos de mensajes de error
    public final static int ERR = 0;
    public final static int WARNING = 1;

    private int tipo = 0;

    // Cadenas constantes
    private final String OK = texto.getString("ok");

    // Iconos
    private final String[] iconos = {
        "icons/error.png",
        "icons/warning.png"
    };

    // Componentes de la interfaz
    private JLabel icono;
    private JLabel mensajeDeError;
    private JButton botonAceptar;

    /**
     * Constructor de la clase. Inicializa la interfaz.
     * @param jd La ventana de la que depende el cuadro de diálogo.
     * @param mensaje El mensaje que se desea pasar al usuario.
     */
    public MensajeError(Window jd,String mensaje){

        // Inicialización
        super(jd,Dialog.DEFAULT_MODALITY_TYPE);

        // Creación de la interfaz
        Container cp = this.getContentPane();

        // Creación de la etiqueta principal
        mensajeDeError = new JLabel(texto.getString(mensaje));
        mensajeDeError.setHorizontalAlignment(JLabel.CENTER);
        mensajeDeError.setBorder(BorderFactory.createEmptyBorder(15,10,15,10));
        cp.add(BorderLayout.CENTER,mensajeDeError);

        // Crear el botón de Aceptar
        JPanel botones = new JPanel();
        botones.setLayout(new FlowLayout(FlowLayout.CENTER));
        botones.setBorder(BorderFactory.createEmptyBorder(0,10,6,10));
        botonAceptar = new JButton(OK);
        botonAceptar.addActionListener(this);
        botones.add(botonAceptar);
        cp.add(BorderLayout.SOUTH,botones);

        // Opciones del cuadro de diálogo
        this.setTitle(texto.getString("mensaje_error"));
        this.setSize(350,160);
        this.setResizable(false);
        this.setLocationRelativeTo(jd);
        this.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
    }

    /**
     * Constructor de la clase. Iniciliza la interfaz.
     * @param jd La ventana a la que está asociada el mensaje.
     * @param mensaje Una cadena con el mensaje a mostrar.
     * @param tipo El tipo de mensaje de error a mostrar.
     */
    public MensajeError(Window jd,String mensaje,int tipo){

        this(jd,mensaje);
        this.tipo = tipo;

        // Creación de la interfaz
        Container cp = this.getContentPane();

        // Creación del icono en función del tipo de mensaje de error
        icono = new JLabel(new ImageIcon(ClassLoader.getSystemResource(iconos[tipo])));
        cp.add(BorderLayout.NORTH,icono);

        this.setVisible(true);
    }

    /**
     * Método que se ejecuta cuando el usuario selecciona el botón del cuadro de
     * diálogo.
     * @param e Evento que generó la ejecución del método.
     */
    @Override
    public void actionPerformed(ActionEvent e){
        this.dispose();
    }
}

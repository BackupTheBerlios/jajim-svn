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

import org.jajim.interfaz.listeners.RechazarFicheroActionListener;
import org.jajim.interfaz.ventanas.VentanaPrincipal;
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

/**
 * @author Florencio Cañizal Calles
 * @version 1.1
 * Clase formulario que informa al usuario de la llegada de una petición de trans
 * ferencia e invita a éste a aceptarla o rechazarla.
 */
public class AceptarORechazarFicheroFormulario extends JDialog implements ActionListener{

    private ResourceBundle texto = ResourceBundle.getBundle("resources.Idioma",Main.loc);

    // Cadenas constantes
    private final String cadenaPrincipal = texto.getString("aceptar_o_rechazar_fichero_formulario_cadena_principal");

    private final String[] etiquetas = {
        texto.getString("aceptar_o_rechazar_fichero_formulario_emisor"),
        texto.getString("aceptar_o_rechazar_fichero_formulario_nombre"),
        texto.getString("aceptar_o_rechazar_fichero_formulario_tamano"),
        texto.getString("aceptar_o_rechazar_fichero_formulario_descripcion")
    };

    private final String aceptar = texto.getString("aceptar_fichero_formulario_cadena");
    private final String rechazar = texto.getString("rechazar_fichero_formulario_cadena");

    // Componentes de la interfaz
    private JLabel principal;
    private JLabel[][] cadenasEtiquetas = new JLabel[etiquetas.length][2];
    private JButton botonAceptar;
    private JButton botonRechazar;

    // Variables importantes
    private int idTransferencia;
    private VentanaPrincipal vp;

    /**
     * Constructor de la clase. Inicializa las variables necesarias. Crea la inter
     * faz del cuadro de diálogo.
     * @param vp Ventana principal de la aplicación.
     * @param idTransferencia Identificador de la transferencia.
     * @param informacion Información relevante de la transferencia.
     */
    public AceptarORechazarFicheroFormulario(VentanaPrincipal vp,int idTransferencia,String[] informacion){

        // Inicialización de variables
        super(vp,true);
        this.idTransferencia = idTransferencia;
        this.vp = vp;

        // Creación de la interfaz
        Container cp = this.getContentPane();

        // Añadir etiqueta principal
        principal = new JLabel(cadenaPrincipal);
        principal.setHorizontalAlignment(JLabel.CENTER);
        principal.setBorder(BorderFactory.createEmptyBorder(15,10,15,10));
        cp.add(BorderLayout.NORTH,principal);

        // Añadir la información del fichero
        JPanel panelInformacion = new JPanel(new GridLayout(etiquetas.length,2,5,10));
        panelInformacion.setBorder(BorderFactory.createEmptyBorder(0,10,15,10));
        for(int i = 0;i < etiquetas.length;i++){
            for(int j = 0;j < 2;j++){
                if(j == 0)
                    cadenasEtiquetas[i][j] = new JLabel(etiquetas[i]);
                else
                    cadenasEtiquetas[i][j] = new JLabel(informacion[i]);
                panelInformacion.add(cadenasEtiquetas[i][j]);
            }
        }
        cp.add(BorderLayout.CENTER,panelInformacion);

        // Añadir los botones
        JPanel botones = new JPanel(new FlowLayout(FlowLayout.CENTER));
        botones.setBorder(BorderFactory.createEmptyBorder(0,10,6,10));
        botonAceptar = new JButton(aceptar);
        botonAceptar.addActionListener(this);
        botonRechazar = new JButton(rechazar);
        botonRechazar.addActionListener(new RechazarFicheroActionListener(this,idTransferencia));
        botones.add(botonAceptar);
        botones.add(botonRechazar);
        cp.add(BorderLayout.SOUTH,botones);

        // Opciones del cuadro de diálogo
        this.setTitle(texto.getString("aceptar_o_rechazar_fichero_formulario_title"));
        this.pack();
        this.setResizable(false);
        this.setLocationRelativeTo(vp);
        this.setDefaultCloseOperation(JDialog.DO_NOTHING_ON_CLOSE);
        this.setVisible(true);
    }

    /**
     * Método que se ejecuta cuando se acepta la transferencia del fichero. Abre
     * un formulario para introducir los campos necesarios.
     * @param e El evento que produjo la activación del método.
     */
    @Override
    public void actionPerformed(ActionEvent e) {

        // Cerrar el formulario y abrir el formulario para introducir los datos.
        this.dispose();
        new AceptarFicheroFormulario(vp,idTransferencia);
    }
}

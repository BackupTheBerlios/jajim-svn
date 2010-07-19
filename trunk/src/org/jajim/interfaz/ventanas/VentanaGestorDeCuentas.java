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

package org.jajim.interfaz.ventanas;

import org.jajim.controladores.CuentaControlador;
import org.jajim.controladores.PreferenciasControlador;
import org.jajim.interfaz.listeners.CambiarCuentaActionListener;
import org.jajim.interfaz.listeners.CrearOAñadirActionListener;
import org.jajim.interfaz.listeners.EliminarCuentaServidorActionListener;
import org.jajim.interfaz.listeners.EliminarCuentaSistemaActionListener;
import org.jajim.main.Main;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ResourceBundle;
import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

/**
 * @author Florencio Cañizal Calles
 * @version 1.1
 * La interfaz a partir de la cual se le permite al usuario gestionar las cuentas
 * dadas de alta en el sistema.
 */
public class VentanaGestorDeCuentas extends JFrame implements ListSelectionListener,ActionListener{

    private ResourceBundle texto = ResourceBundle.getBundle("resources.Idioma",Main.loc);

    private final String[] titulos = {
        texto.getString("gestor_de_cuentas_lista_principal"),
        texto.getString("gestor_de_cuentas_informacion_principal")
    };

    private final String[] botones = {
        texto.getString("gestor_de_cuentas_boton_añadir"),
        texto.getString("gestor_de_cuentas_boton_activar"),
        texto.getString("gestor_de_cuentas_boton_eliminar_sistema"),
        texto.getString("gestor_de_cuentas_boton_eliminar_servidor")
    };

    private final String[] etiquetas = {
        texto.getString("gestor_de_cuentas_identificador"),
        texto.getString("gestor_de_cuentas_servidor"),
        texto.getString("gestor_de_cuentas_guardada_contraseña")
    };

    private final String[] iconos = {
        "icons/añadir_cuenta.png",
        "icons/activar_cuenta.png",
        "icons/eliminar_cuenta_del_sistema.png",
        "icons/eliminar_cuenta_del_servidor.png"
    };

    private final String cerrar = texto.getString("cerrar");

    // Elementos de la interfaz
    private JLabel[] etiquetasTitulos = new JLabel[titulos.length];
    private JList listaDeCuentas;
    private JButton[] botonesCuentas = new JButton[botones.length];
    private JLabel[] etiquetasInformacion = new JLabel[etiquetas.length];
    private JLabel[] informacion = new JLabel[etiquetas.length];
    private JButton botonCerrar;

    // Controladores utilizados
    private CuentaControlador cc;

    // Ventana principal
    private VentanaPrincipal vp;

    // Listeners
    private ActionListener[] listeners = new ActionListener[botones.length];

    // Información de presentación
    private int activa = 0;
    private String[][] cuentas;

    /**
     * Constructor de la clase. Inicializa las variables necesarias. Crea la inter
     * faz de usuario.
     * @param cc El controlador de las cuentas.
     * @param vp La ventana principal de la aplicación.
     */
    public VentanaGestorDeCuentas(VentanaPrincipal vp,CuentaControlador cc){

        // Inicialización
        this.cc = cc;
        this.vp = vp;
        listeners[0] = new CrearOAñadirActionListener(this,vp,cc);
        listeners[1] = new CambiarCuentaActionListener(this,vp);
        listeners[2] = new EliminarCuentaSistemaActionListener(this,vp);
        listeners[3] = new EliminarCuentaServidorActionListener(this,vp);

        // Creación de la interfaz
        Container cp = this.getContentPane();

        // Crear el panel principal
        JPanel principal = new JPanel(new GridLayout(1,titulos.length,20,20));
        principal.setBorder(BorderFactory.createEmptyBorder(15,10,0,10));

        for(int i = 0;i < titulos.length;i++){
            // Añadir el título
            etiquetasTitulos[i] = new JLabel(titulos[i]);
            etiquetasTitulos[i].setHorizontalAlignment(JLabel.CENTER);
            etiquetasTitulos[i].setFont(new Font(Font.DIALOG,Font.BOLD,15));
            etiquetasTitulos[i].setForeground(Color.GRAY);

            switch(i){

                case 0:
                    // Añadir el título, la lista y los botones
                    JPanel listaYBotones = new JPanel(new GridBagLayout());
                    // Atirbutos de la disposición de la lista de cuentas
                    GridBagConstraints constraints = new GridBagConstraints();
                    constraints.gridx = 0;
                    constraints.gridy = 0;
                    constraints.gridheight = 1;
                    constraints.gridwidth = 1;
                    constraints.weightx = 1.0;
                    listaYBotones.add(etiquetasTitulos[i],constraints);
                    listaDeCuentas = new JList();
                    listaDeCuentas.addListSelectionListener(this);
                    JScrollPane jsp = new JScrollPane(listaDeCuentas);
                    jsp.setBorder(BorderFactory.createEmptyBorder(10,0,10,0));
                    constraints.gridy = 1;
                    constraints.weighty = 1.0;
                    constraints.fill = GridBagConstraints.BOTH;
                    listaYBotones.add(jsp,constraints);
                    constraints.weighty = 0.0;
                    constraints.fill = GridBagConstraints.NONE;
                    for(int j = 0;j < botones.length;j++){
                        JPanel jp = new JPanel();
                        botonesCuentas[j] = new JButton(botones[j],new ImageIcon(ClassLoader.getSystemResource(iconos[j])));
                        botonesCuentas[j].addActionListener(listeners[j]);
                        botonesCuentas[j].setPreferredSize(new Dimension(151,24));
                        botonesCuentas[j].setMinimumSize(new Dimension(151,24));
                        jp.add(botonesCuentas[j]);
                        constraints.gridy = j + 2;
                        listaYBotones.add(jp,constraints);
                    }
                    principal.add(listaYBotones);
                    break;

                case 1:
                    // Añadir la información de la cuenta
                    JPanel central = new JPanel(new BorderLayout());
                    central.setBorder(BorderFactory.createLineBorder(Color.GRAY,2));
                    central.setBackground(Color.WHITE);
                    etiquetasTitulos[i].setBorder(BorderFactory.createEmptyBorder(15,0,0,0));
                    central.add(BorderLayout.NORTH,etiquetasTitulos[i]);
                    JPanel cuentaInformacion = new JPanel(new GridLayout(etiquetas.length,etiquetas.length));
                    cuentaInformacion.setBackground(Color.WHITE);
                    cuentaInformacion.setBorder(BorderFactory.createEmptyBorder(10,15,80,0));
                    for(int j = 0;j < etiquetas.length;j++){
                        for(int k = 0;k < 2;k++){
                            if(k == 0){
                                etiquetasInformacion[j] = new JLabel(etiquetas[j]);
                                cuentaInformacion.add(etiquetasInformacion[j]);
                            }
                            else{
                                informacion[j] = new JLabel();
                                cuentaInformacion.add(informacion[j]);
                            }
                        }
                    }
                    central.add(BorderLayout.CENTER,cuentaInformacion);
                    principal.add(central);
                    break;
            }
        }
        cp.add(BorderLayout.CENTER,principal);

        // Crear el botón de cerrar
        JPanel panelBotones = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        panelBotones.setBorder(BorderFactory.createEmptyBorder(10,0,7,10));
        botonCerrar = new JButton(cerrar);
        botonCerrar.addActionListener(this);
        panelBotones.add(botonCerrar);
        cp.add(BorderLayout.SOUTH,panelBotones);

        // Recuperar la información de las cuentas y añadirla al gestor
        añadirCuentas();
        rellenarInformacion();

        // Iniciación de la interfaz
        Image image = Toolkit.getDefaultToolkit().getImage(ClassLoader.getSystemResource("icons/gestor_de_cuentas.png"));
        this.setIconImage(image);
        this.setTitle(texto.getString("gestor_de_cuentas_title"));
        PreferenciasControlador pfc = vp.getPfc();
        this.setLocation(pfc.getGestorDeCuentasX(),pfc.getGestorDeCuentasY());
        this.setSize(580,370);
        this.setResizable(false);
        this.setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
    }

    /**
     * Actualiza el gestor de diálogo con la información actual de las cuentas.
     */
    public void añadirCuentas(){

        // Recuperar la información de las cuentas
        cuentas = cc.getInformacionDeCuentas();

        // Asignar la información del JList
        String[] informacionLista = new String[cuentas.length];
        for(int i = 0;i < informacionLista.length;i++){
            informacionLista[i] = cuentas[i][0] + "@" + cuentas[i][1];
            if(Boolean.parseBoolean(cuentas[i][3]) == true)
                activa = i;
        }

        // Forzar a rellenar la información, cuando se añade una cuenta y no exis
        // tía ninguna previa, pues el valueChanged no funciona en esa situación
        if(listaDeCuentas.getSelectedIndex() == -1)
            rellenarInformacion();

        listaDeCuentas.setListData(informacionLista);
        listaDeCuentas.setSelectedIndex(activa);
    }

    /**
     * Método que se ejecuta cuando se selecciona una cuenta de la lista de cuentas.
     * Muestra la información de la misma.
     * @param e El evento que produce la ejecución del método.
     */
    @Override
    public void valueChanged(ListSelectionEvent e) {

        if(activa != listaDeCuentas.getSelectedIndex()){
            if(listaDeCuentas.getSelectedIndex() != -1)
                activa = listaDeCuentas.getSelectedIndex();
            rellenarInformacion();
        }
    }

    /**
     * Método que se ejecuta cuando se selecciona el botón cerrar de la ventana.
     * Oculta la ventana al usuario.
     * @param e El evento que genera la ejecución del método.
     */
    @Override
    public void actionPerformed(ActionEvent e){
        // Ocultar la ventana
        this.setVisible(false);
    }

    /**
     * Escribe la información de la cuenta apropiada en las etiquetas.
     */
    private void rellenarInformacion(){

        // Si no hay cuentas en el sistema eliminar la información del gestor y
        // salir
        if(cuentas.length == 0){
            for(int i = 0;i < informacion.length;i++){
                informacion[i].setText("");
            }
            return;
        }

        // Rellenar las etiquetas
        for(int i = 0;i < informacion.length;i++){
            switch(i){
                case 0:
                case 1:
                    informacion[i].setText(cuentas[activa][i]);
                    break;
                case 2:
                    informacion[i].setText(texto.getString(cuentas[activa][i]));
            }
        }
    }

    /**
     * Retorna el identificador y el servidor de la cuenta seleccionada en este
     * momento.
     * @return El identificador y el servidor de la cuenta seleccionada.
     */
    public String[] getCuenta(){

        // Si no hay cuentas devolver null
        if(cuentas.length == 0)
            return null;

        // Retornar el identificador y el servidor de la cuenta seleccionada.
        String[] cuenta = new String[2];
        cuenta[0] = cuentas[activa][0];
        cuenta[1] = cuentas[activa][1];

        return cuenta;
    }

    /**
     * Muestra la ventana del gestor de cuentas en pantalla.
     */
    public void hacerVisible(){
        // Colocar la ventana y hacerla visible
        this.setLocation(this.getX(),this.getY());
        this.setVisible(true);
    }
}
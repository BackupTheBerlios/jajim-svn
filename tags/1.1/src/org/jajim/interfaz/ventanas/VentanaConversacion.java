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

import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.ResourceBundle;
import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JToolBar;
import javax.swing.KeyStroke;
import javax.swing.text.Keymap;
import org.jajim.controladores.ContactosControlador;
import org.jajim.controladores.ConversacionControlador;
import org.jajim.controladores.PreferenciasControlador;
import org.jajim.interfaz.listeners.ColorMenuActionListener;
import org.jajim.interfaz.listeners.CursivaMenuActionListener;
import org.jajim.interfaz.listeners.EnviarFicheroMenuActionListener;
import org.jajim.interfaz.listeners.EnviarMensajeAction;
import org.jajim.interfaz.listeners.EnviarMensajeActionListener;
import org.jajim.interfaz.listeners.FuenteMenuActionListener;
import org.jajim.interfaz.listeners.GuardarConversacionMenuActionListener;
import org.jajim.interfaz.listeners.InvitarContactoMenuActionListener;
import org.jajim.interfaz.listeners.NegritaMenuActionListener;
import org.jajim.interfaz.listeners.VentanaConversacionWindowListener;
import org.jajim.interfaz.listeners.VetarContactoMenuActionListener;
import org.jajim.interfaz.utilidades.PanelConversacion;
import org.jajim.main.Main;

/**
 * @author Florencio Cañizal Calles
 * @version 1.1
 * Clase que representa una ventana de una conversación. Inicializa la interfaz
 * necesaria para que el usuario dialogue con un contacto.
 */
public class VentanaConversacion extends JFrame{

    private ResourceBundle texto = ResourceBundle.getBundle("resources.Idioma",Main.loc);

    // Cadenas constantes para impresión
    protected final String principal = texto.getString("conversacion_etiqueta_principal");
    private final String enviar = texto.getString("conversacion_enviar");

    private final String [] menusCad = {
        texto.getString("acciones_menu")
    };
    private final String [][] itemsDeMenuCad = {
        {
            texto.getString("enviar_fichero_item_menu"),
            texto.getString("guardar_conversacion_item"),
            texto.getString("invitar_contacto_item_menu"),
            texto.getString("vetar_contacto_item_menu")
        }
    };

    private final String[] itemsDeMenuEstilo = {
        texto.getString("fuente_item_estilo"),
        texto.getString("color_item_estilo"),
        texto.getString("negrita_item_estilo"),
        texto.getString("cursiva_item_estilo")
    };

    // Iconos
    private final String[] iconosBarraHerramientas = {
        "icons/enviar_fichero.png",
        "icons/guardar_conversacion.png",
        "icons/invitar_contacto.png",
        "icons/vetar_contacto.png"
    };

    private final String[] iconosEstilo = {
        "icons/fuente.png",
        "icons/color.png",
        "icons/negrita.png",
        "icons/cursiva.png"
    };

    // Aceleradores de teclado
    private final int[] aceleradoresDeTeclado = {
        KeyEvent.VK_E,
        KeyEvent.VK_G,
        KeyEvent.VK_I,
        KeyEvent.VK_V
    };

    // ActionListeners de los menus
    private final ActionListener actionListenersMenu [][] = {
        {
            new EnviarFicheroMenuActionListener(this),
            new GuardarConversacionMenuActionListener(this),
            new InvitarContactoMenuActionListener(this),
            new VetarContactoMenuActionListener(this)
        }
    };

    private final ActionListener actionListenersEstilos[] = {
        new FuenteMenuActionListener(this),
        new ColorMenuActionListener(this),
        new NegritaMenuActionListener(this),
        new CursivaMenuActionListener(this)
    };

    // Controladores utilizados
    protected ConversacionControlador cvc;

    // Panel de conversaciones
    protected PanelConversacion conversacion;

    // Gestor de transferencias
    private VentanaGestorDeTransferencias vgt;

    // Componentes de la interfaz
    // Menú
    private JMenuBar barraMenu;
    private JMenu menus[] = new JMenu[menusCad.length];
    protected JMenuItem[][] itemsDeMenu = new JMenuItem[menusCad.length][];

    // Barra de herramientas
    private JToolBar barraDeHerramientas;
    protected JButton[] botonesBarraDeHerramientas = new JButton[itemsDeMenuCad[0].length];

    // Componentes de la interfaz
    protected JLabel etiquetaPrincipal;
    protected JTextArea nuevoMensaje;
    private JButton botonEnviar;

    // Barra de estilos
    private JToolBar barraDeEstilos;
    private JButton[] botonesBarraDeEstilos = new JButton[itemsDeMenuEstilo.length];

    // El usuario actual
    protected String usuarioActual;

    // Conjunto de conversaciones abiertas
    private static List<VentanaConversacion> conversaciones;

    /**
     * Constructor privado. Inicializa las variables importantes. Crea la interfaz
     * de usuario.
     * @param vp Ventana principal de la aplicación.
     * @param alias Alias del contacto con el que se va a establecer la conversacion.
     */
    public VentanaConversacion(VentanaPrincipal vp,String alias){

        // Creación del menú de la aplicación
        barraMenu = new JMenuBar();

        for(int i = 0;i < menus.length;i++){

            // Crear un nuevo menu y añadirlo a la barra
            menus[i] = new JMenu(menusCad[i]);
            barraMenu.add(menus[i]);

            // Crear los items de menú y añadirlos al menú
            itemsDeMenu[i] = new JMenuItem[itemsDeMenuCad[i].length];
            for(int j = 0;j < itemsDeMenu[i].length;j++){

                // Añadir botones
                itemsDeMenu[i][j] = new JMenuItem(itemsDeMenuCad[i][j],new ImageIcon(ClassLoader.getSystemResource(iconosBarraHerramientas[j])));
                itemsDeMenu[i][j].setAccelerator(KeyStroke.getKeyStroke(aceleradoresDeTeclado[j],ActionEvent.CTRL_MASK));
                itemsDeMenu[i][j].addActionListener(actionListenersMenu[i][j]);
                menus[i].add(itemsDeMenu[i][j]);

                // Añadir separadores
                if(j == 1) {
                    menus[i].addSeparator();
                }
            }
        }

        this.setJMenuBar(barraMenu);

        // Inicialización de variables
        vgt = vp.getVgt();

        // Creación de la interfaz
        Container cp = this.getContentPane();

        // Crear la barra de menú
        JPanel barraDeHerramientasYTitulo = new JPanel(new BorderLayout());
        barraDeHerramientas = new JToolBar();
        barraDeHerramientas.setFloatable(false);

        for(int i = 0;i < botonesBarraDeHerramientas.length;i++){

            botonesBarraDeHerramientas[i] = new JButton(new ImageIcon(ClassLoader.getSystemResource(iconosBarraHerramientas[i])));
            botonesBarraDeHerramientas[i].setToolTipText(itemsDeMenuCad[0][i]);
            botonesBarraDeHerramientas[i].addActionListener(actionListenersMenu[0][i]);
            barraDeHerramientas.add(botonesBarraDeHerramientas[i]);
            if(i == 1 || i == 2){
                barraDeHerramientas.addSeparator();
            }
        }
        barraDeHerramientasYTitulo.add(BorderLayout.NORTH,barraDeHerramientas);

        // Creación de la etiqueta principal de la ventana
        etiquetaPrincipal = new JLabel(principal + " - " + alias);
        etiquetaPrincipal.setBorder(BorderFactory.createEmptyBorder(15,10,15,10));
        barraDeHerramientasYTitulo.add(BorderLayout.CENTER,etiquetaPrincipal);

        cp.add(BorderLayout.NORTH,barraDeHerramientasYTitulo);

        // Creación del panel en el que se visualizan los mensajes de la conver
        // sación
        conversacion = new PanelConversacion(this);

        // Creación del panel donde se introducen los mensajes a enviar
        JPanel barraYMensajes = new JPanel(new BorderLayout());
        barraYMensajes.setBorder(BorderFactory.createEmptyBorder(8,10,15,10));
        JPanel panelBarra = new JPanel(new GridLayout(1,1));
        panelBarra.setBorder(BorderFactory.createEmptyBorder(0,0,5,75));
        barraDeEstilos = new JToolBar();
        barraDeEstilos.setFloatable(false);
        for(int i = 0;i < botonesBarraDeEstilos.length;i++){
            botonesBarraDeEstilos[i] = new JButton(new ImageIcon(ClassLoader.getSystemResource(iconosEstilo[i])));
            botonesBarraDeEstilos[i].addActionListener(actionListenersEstilos[i]);
            if(i == 2){
                if(PreferenciasControlador.getInstancia().isNegrita()) {
                    botonesBarraDeEstilos[i].setSelected(true);
                }
            }
            if(i == 3){
                if(PreferenciasControlador.getInstancia().isCursiva()) {
                    botonesBarraDeEstilos[i].setSelected(true);
                }
            }
            botonesBarraDeEstilos[i].setToolTipText(itemsDeMenuEstilo[i]);
            barraDeEstilos.add(botonesBarraDeEstilos[i]);
            if(i != (botonesBarraDeEstilos.length - 1)) {
                barraDeEstilos.addSeparator();
            }
        }
        panelBarra.add(barraDeEstilos);
        barraYMensajes.add(BorderLayout.NORTH,panelBarra);

        JPanel mensajes = new JPanel(new BorderLayout(5,5));
        mensajes.setSize(100,70);
        mensajes.setPreferredSize(new Dimension(100,50));
        nuevoMensaje = new JTextArea();
        nuevoMensaje.setLineWrap(true);
        nuevoMensaje.setWrapStyleWord(true);
        Keymap keymap = JTextArea.addKeymap("MiKeymap",nuevoMensaje.getKeymap());
        KeyStroke key = KeyStroke.getKeyStroke(KeyEvent.VK_ENTER,0);
        keymap.addActionForKeyStroke(key,new EnviarMensajeAction(this,conversacion));
        nuevoMensaje.setKeymap(keymap);
        JScrollPane scrollPane = new JScrollPane(nuevoMensaje);
        mensajes.add(BorderLayout.CENTER,scrollPane);
        botonEnviar = new JButton(enviar,new ImageIcon(ClassLoader.getSystemResource("icons/enviar_mensaje.png")));
        botonEnviar.addActionListener(new EnviarMensajeActionListener(this,conversacion));
        mensajes.add(BorderLayout.EAST,botonEnviar);
        barraYMensajes.add(BorderLayout.CENTER,mensajes);
        cp.add(BorderLayout.SOUTH,barraYMensajes);

        // Añadir la conversación a la lista
        VentanaConversacion.añadirConversacion(this);

        // Iniciación de la interfaz
        Image image = Toolkit.getDefaultToolkit().getImage(ClassLoader.getSystemResource("icons/conversacion.png"));
        this.setTitle(alias + " - JAJIM_1.1");
        this.setIconImage(image);
        if(PreferenciasControlador.getInstancia().isVentanaConversacionMaximizada()) {
            this.setExtendedState(JFrame.MAXIMIZED_BOTH);
        }
        else{
            this.setLocation(PreferenciasControlador.getInstancia().getVentanaConversacionX(),PreferenciasControlador.getInstancia().getVentanaConversacionY());
            this.setSize(PreferenciasControlador.getInstancia().getVentanaConversacionAncho(),PreferenciasControlador.getInstancia().getVentanaConversacionLargo());
        }
        this.addWindowListener(new VentanaConversacionWindowListener(this));
        this.setVisible(true);
    }

    /**
     * Recupera el mensaje introducido en el campo y resetea el campo.
     * @return El mensaje introducido en el campo.
     */
    public String getMensaje(){

        // Recuperar el mensaje y borrar el contenido de la etiqueta
        String mensaje = nuevoMensaje.getText();
        nuevoMensaje.setText("");

        return mensaje;
    }

    /**
     * Gestiona los cambios de preferencias en el estilo de los mensajes del usua
     * rio.
     */
    public void actualizarPreferenciasMensajes(){

        // Recuperar las preferencias y notificarselas a la clase
        PreferenciasControlador pfc = PreferenciasControlador.getInstancia();
        String fuente = pfc.getFuente();
        int tamaño = pfc.getTamaño();
        int rojo = pfc.getColorRojo();
        int verde = pfc.getColorVerde();
        int azul = pfc.getColorAzul();
        boolean negrita = pfc.isNegrita();
        boolean cursiva = pfc.isCursiva();

        String[] preferencias = new String[7];
        preferencias[0] = fuente;
        preferencias[1] = String.valueOf(tamaño);
        preferencias[2] = String.valueOf(rojo);
        preferencias[3] = String.valueOf(verde);
        preferencias[4] = String.valueOf(azul);
        preferencias[5] = String.valueOf(negrita);
        preferencias[6] = String.valueOf(cursiva);

        // Actualizar preferencias en los puntos necesarios
        conversacion.actualizarPreferencias(usuarioActual,preferencias);
        cvc.actualizarPreferencias(preferencias);
    }

    /**
     * Método que devuelve el controlador de la conversación.
     * @return El controlador de la conversación.
     */
    public ConversacionControlador getCvc(){
        return cvc;
    }

    /**
     * Método que devuelve la ventana del gestor de transferencias.
     * @return Ventana del gestor de transferencias.
     */
    public VentanaGestorDeTransferencias getVgt(){
        return vgt;
    }

    /**
     * Método que devuelve el panel que gestiona la conversación.
     * @return El panel de la conversación.
     */
    public String getConversacion(){
        return conversacion.getConversacion().toString();
    }

    /**
     * Añade una conversación a la lista de conversaciones gestionada por la clase.
     * @param vc La conversación que se va a añadir.
     */
    private static void añadirConversacion(VentanaConversacion vc){

        // Si no se dispone de una inicialización de la variable, se inicializa.
        if(conversaciones == null) {
            conversaciones = Collections.synchronizedList(new ArrayList<VentanaConversacion>());
        }

        conversaciones.add(vc);
    }

    /**
     * Elimina la conversación de la lista de conversaciones.
     * @param vc La conversación que se debe eliminar.
     */
    public static void eliminarConversacion(VentanaConversacion vc){

        // Si hay conversaciones en el sistema
        if(conversaciones != null){
            // Hacer las operaciones necesarias a nivel de protocolo
            vc.getCvc().cerrarConversacion();
            vc.dispose();
            conversaciones.remove(vc);
        }
    }

    /**
     * Develve un valor booleano que indica la existencia o no de conversaciones
     * abiertas.
     * @return true si hay conversaciones y false en caso contrario.
     */
    public static boolean hayConversaciones(){

        // Si la lista es nula o tiene tamaño 0, se devuelve false
        if(conversaciones == null || conversaciones.isEmpty()) {
            return false;
        }
        else {
            return true;
        }
    }

    /**
     * Cierra todas las conversaciones que tiene en curso el sistema.
     */
    public static void cerrarConversaciones(){

        // Recorrer la lista de conversaciones cerrandolas
        for(int i = 0;i < conversaciones.size();i++){
            VentanaConversacion vc = conversaciones.get(i);
            vc.getCvc().cerrarConversacion();
            vc.dispose();
        }

        // Eliminar las conversaciones de la lista
        conversaciones.clear();

        // Resetear el oyente.
        ConversacionControlador.eliminarListener();
    }

    /**
     * Cierra las conversaciones en las que el único participante es el contacto.
     * @param contacto El contacto para el que se van a cerrar las coversaciones.
     */
    public static void cerrarConversaciones(String contacto){

        // Recorre la lista de conversaciones y cierra aquellas en las que el con
        // tacto es el único usuario
        for(int i = 0;i < conversaciones.size();i++){
            VentanaConversacion vc = conversaciones.get(i);
            String[] participantes = null;
            participantes = vc.getCvc().getParticipantes();
            if(participantes.length == 1 && participantes[0].compareTo(contacto) == 0){
                VentanaConversacion.eliminarConversacion(vc);
                i--;
            }
        }
    }

    /**
     * Método que informa de si se mantiene un chat privado con un usuario cuyo
     * alias es el proporcionado.
     * @param alias El alias del contacto.
     * @return True si hay chat privado, falso en caso contrario.
     */
    public static boolean hayChatPrivado(String alias){

        boolean chatPrivado = false;
        String contacto = null;

        // Recuperar el contacto por el alias
        if(conversaciones != null && conversaciones.size() > 0){
            contacto = ContactosControlador.getInstancia().getContactoPorAlias(alias);
        }
        else {
            return chatPrivado;
        }

        // Mirar en todas las conversaciones haber si una es un chat privado con
        // el usuario
        for(VentanaConversacion vc : conversaciones){
            String[] participantes = vc.getCvc().getParticipantes();
            if(participantes.length == 1 && vc instanceof VentanaConversacionChatPrivado && contacto.compareTo(participantes[0]) == 0){
                chatPrivado = true;
                break;
            }
        }

        return chatPrivado;
    }

    /**
     * Método que retorna la ventana de un chat privado que se mantiene con el
     * alias especificado. Se retorna null en caso de que no se disponga de ninguna
     * conversación con ese usuario.
     * @param alias El alias del contacto del que se quiere recuperar el chat.
     * @return La ventana del chat privado en el que se mantiene la conversación
     * con el usuario o null.
     */
    public static VentanaConversacionChatPrivado getChatPrivado(String alias){

        String contacto = null;

        // Recuperar el contacto por el alias
        if(conversaciones != null && conversaciones.size() > 0){
            contacto = ContactosControlador.getInstancia().getContactoPorAlias(alias);
        }
        else {
            return null;
        }

        // Mirar en todas las conversaciones haber si una es un chat privado con
        // el usuario adecuado y retornar ésta
        for(VentanaConversacion vc : conversaciones){
            String[] participantes = vc.getCvc().getParticipantes();
            if(participantes.length == 1 && vc instanceof VentanaConversacionChatPrivado && contacto.compareTo(participantes[0]) == 0){
                return (VentanaConversacionChatPrivado) vc;
            }
        }

        return null;
    }
}

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

import java.awt.AWTException;
import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.Image;
import java.awt.Menu;
import java.awt.MenuItem;
import java.awt.PopupMenu;
import java.awt.SystemTray;
import java.awt.Toolkit;
import java.awt.TrayIcon;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.util.ResourceBundle;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JToolBar;
import javax.swing.KeyStroke;
import org.jajim.controladores.ContactosControlador;
import org.jajim.controladores.CuentaControlador;
import org.jajim.controladores.PreferenciasControlador;
import org.jajim.controladores.TransferenciaFicherosControlador;
import org.jajim.interfaz.dialogos.CrearOAñadirFormulario;
import org.jajim.interfaz.listeners.AñadirContactoMenuActionListener;
import org.jajim.interfaz.listeners.BuscarContactoMenuActionListener;
import org.jajim.interfaz.listeners.CambiarEstadoActionListener;
import org.jajim.interfaz.listeners.ConectarActionListener;
import org.jajim.interfaz.listeners.CrearGrupoDeContactosMenuActionListener;
import org.jajim.interfaz.listeners.DesconectarActionListener;
import org.jajim.interfaz.listeners.LanzarAcercaDeActionListener;
import org.jajim.interfaz.listeners.LanzarAyudaActionListener;
import org.jajim.interfaz.listeners.LanzarGestorDeCuentasActionListener;
import org.jajim.interfaz.listeners.LanzarGestorDeTransferenciasActionListener;
import org.jajim.interfaz.listeners.ModificarContraseñaMenuActionListener;
import org.jajim.interfaz.listeners.SalirActionListener;
import org.jajim.interfaz.listeners.VentanaPrincipalWindowListener;
import org.jajim.interfaz.listeners.VisualizarVentanaActionListener;
import org.jajim.interfaz.utilidades.ComboBoxRenderer;
import org.jajim.interfaz.utilidades.PanelContactos;
import org.jajim.main.Main;

/**
 * @author Florencio Cañizal Calles
 * @version 1.2 Representa la ventana principal de la aplicación. Inicializa la interfaz y to dos aquellos controladores
 * necesarios para la llevar a cabo la funcionalidad de la aplicación. También, muestra el roster del usuario en caso de
 * que esté conectado.
 */
public class VentanaPrincipal extends JFrame {

    private static VentanaPrincipal instancia;
    private final ResourceBundle texto = ResourceBundle.getBundle("resources.Idioma", Main.loc);

    // Cadenas constantes para impresión
    private final String[] menusCad = {
        texto.getString("acciones_menu"),
        texto.getString("gestores_menu"),
        texto.getString("ayuda_menu")
    };

    private final String[][] itemsDeMenuCad = {
        {
            texto.getString("conectar_item_menu"),
            texto.getString("desconectar_item_menu"),
            texto.getString("modificar_contraseña_item_menu"),
            texto.getString("crear_grupo_item_menu"),
            texto.getString("añadir_contacto_item_menu"),
            texto.getString("buscar_contacto_item_menu")
        },
        {
            texto.getString("ventana_gestor_de_cuentas_item_menu"),
            texto.getString("ventana_gestor_de_transferencias")
        },
        {
            texto.getString("ayuda_item_menu"),
            texto.getString("acerca_de_item_menu")
        }
    };

    private final String[] itemsDeMenuPopupCad = {
        texto.getString("restaurar_item_menu"),
        texto.getString("salir_item_menu")
    };

    private final String[][] iconos = {
        {
            "icons/conectar.png",
            "icons/desconectar.png",
            "icons/modificar_contraseña.png",
            "icons/crear_grupo.png",
            "icons/añadir_usuario.png",
            "icons/buscar_contacto.png"
        },
        {
            "icons/gestor_de_cuentas.png",
            "icons/gestor_de_transferencias.png"
        },
        {
            "icons/ayuda.png",
            "icons/acerca_de.png"
        }
    };

    private final String[] estadosUsuario = {
        texto.getString("en_linea_estado"),
        texto.getString("ausente_estado"),
        texto.getString("libre_para_hablar_estado"),
        texto.getString("ocupado_estado"),
        texto.getString("no_disponible_estado"),
        texto.getString("desconectado_estado")
    };

    private final int[][] aceleradoresDeTeclado = {
        {
            KeyEvent.VK_C,
            KeyEvent.VK_D,
            KeyEvent.VK_M,
            KeyEvent.VK_G,
            KeyEvent.VK_A,
            KeyEvent.VK_B
        },
        {
            KeyEvent.VK_C,
            KeyEvent.VK_T
        },
        {
            KeyEvent.VK_F1,
            KeyEvent.VK_F2
        }
    };

    // ActionListeners de los menus
    private final ActionListener actionListenersMenu[][] = {
        {
            new ConectarActionListener(),
            new DesconectarActionListener(),
            new ModificarContraseñaMenuActionListener(),
            new CrearGrupoDeContactosMenuActionListener(),
            new AñadirContactoMenuActionListener(),
            new BuscarContactoMenuActionListener()
        },
        {
            new LanzarGestorDeCuentasActionListener(),
            new LanzarGestorDeTransferenciasActionListener()
        },
        {
            new LanzarAyudaActionListener(),
            new LanzarAcercaDeActionListener()
        }
    };

    private final ActionListener actionListenersMenuPopup[] = {
        new VisualizarVentanaActionListener(),
        new SalirActionListener()
    };

    // Matriz que determina si los botones están activados
    private final boolean sinConexion[][] = {
        {true, false, false, false, false, false},
        {true, true},
        {true, true}
    };

    private final boolean conConexion[][] = {
        {false, true, true, true, true, true},
        {true, true},
        {true, true}
    };

    // Matriz que determina donde colocar separadores y donde no
    private final boolean separadores[][] = {
        {false, true, true, false, false, false},
        {false, false},
        {false, false}
    };

    // Barra de herramientas
    private final JToolBar barraDeHerramientas;
    private final JButton[] botonesBarraDeHerramientas = new JButton[itemsDeMenuCad[0].length];

    // Componentes de la interfaz
    // Menú
    private final JMenuBar barraMenu;
    private final JMenu menus[] = new JMenu[menusCad.length];
    private final JMenuItem[][] itemsDeMenu = new JMenuItem[menusCad.length][];
    private final JComboBox estado;

    // Icono de la barra de herramientas y su menú pop-up
    private TrayIcon iconoBarraHerramientas;
    private PopupMenu popupMenu;
    private final MenuItem[] itemsPopup = new MenuItem[itemsDeMenuPopupCad.length];
    private Menu menuEstado;
    private final MenuItem[] itemsEstado = new MenuItem[estadosUsuario.length];

    // Control de la ocultación de la ventana por el uso de la barra de herramientas
    private final int ocultable;
    public static int OCULTABLE = 0;
    public static int NO_OCULTABLE = 1;

    /**
     * Constructor de la clase. Crea la interfaz básica e inicializa las variables necesarias.
     */
    public VentanaPrincipal() {

        // Creación del menú de la aplicación
        barraMenu = new JMenuBar();

        for (int i = 0; i < menus.length; i++) {

            // Crear un nuevo menu y añadirlo a la barra
            menus[i] = new JMenu(menusCad[i]);
            barraMenu.add(menus[i]);

            // Crear los items de menú y añadirlos al menú
            itemsDeMenu[i] = new JMenuItem[itemsDeMenuCad[i].length];
            for (int j = 0; j < itemsDeMenu[i].length; j++) {
                itemsDeMenu[i][j] = new JMenuItem(itemsDeMenuCad[i][j], new ImageIcon(ClassLoader.getSystemResource(
                    iconos[i][j])));
                itemsDeMenu[i][j].addActionListener(actionListenersMenu[i][j]);

                // Añadir aceleradores de teclado
                if (i == 0) {
                    itemsDeMenu[i][j].setAccelerator(KeyStroke.getKeyStroke(aceleradoresDeTeclado[i][j],
                        ActionEvent.CTRL_MASK));
                }
                else if (i == 1) {
                    itemsDeMenu[i][j].setAccelerator(KeyStroke.getKeyStroke(aceleradoresDeTeclado[i][j],
                        ActionEvent.CTRL_MASK | ActionEvent.SHIFT_MASK));
                }
                else {
                    itemsDeMenu[i][j].setAccelerator(KeyStroke.getKeyStroke(aceleradoresDeTeclado[i][j], 0));
                }

                itemsDeMenu[i][j].setEnabled(sinConexion[i][j]);
                menus[i].add(itemsDeMenu[i][j]);
                // Añadir el separador
                if (separadores[i][j]) {
                    menus[i].addSeparator();
                }
            }
        }

        this.setJMenuBar(barraMenu);

        // Creación de la interfaz
        Container cp = this.getContentPane();

        // Crear la barra de menú
        barraDeHerramientas = new JToolBar();
        barraDeHerramientas.setFloatable(false);

        for (int i = 0; i < botonesBarraDeHerramientas.length; i++) {

            botonesBarraDeHerramientas[i] = new JButton(new ImageIcon(ClassLoader.getSystemResource(iconos[0][i])));
            botonesBarraDeHerramientas[i].setToolTipText(itemsDeMenuCad[0][i]);
            botonesBarraDeHerramientas[i].addActionListener(actionListenersMenu[0][i]);
            botonesBarraDeHerramientas[i].setEnabled(sinConexion[0][i]);
            barraDeHerramientas.add(botonesBarraDeHerramientas[i]);
            if (i == 1 || i == 2) {
                barraDeHerramientas.addSeparator();
            }
        }

        cp.add(BorderLayout.PAGE_START, barraDeHerramientas);

        // Crear panel de contactos
        PanelContactos.getInstancia(this.getContentPane());

        // Crear el combo box
        Integer[] intArray = new Integer[ComboBoxRenderer.getLongitud()];
        for (int i = 0; i < intArray.length; i++) {
            intArray[i] = new Integer(i);
        }
        estado = new JComboBox(intArray);
        ComboBoxRenderer cbr = new ComboBoxRenderer();
        estado.setRenderer(cbr);
        estado.setSelectedIndex(ComboBoxRenderer.getLongitud() - 1);
        estado.setEnabled(false);
        cp.add(BorderLayout.SOUTH, estado);

        // Iniciación de la interfaz
        Image image = Toolkit.getDefaultToolkit().getImage(ClassLoader.getSystemResource("icons/jabber.png"));
        this.setTitle("JAJIM 1.2");
        this.setIconImage(image);
        if (!PreferenciasControlador.getInstancia().isVentanaPrincipalMaximizada()) {
            this.setLocation(PreferenciasControlador.getInstancia().getVentanaPrincipalX(), PreferenciasControlador.
                getInstancia().getVentanaPrincipalY());
            this.setSize(PreferenciasControlador.getInstancia().getVentanaPrincipalAncho(), PreferenciasControlador.
                getInstancia().getVentanaPrincipalLargo());
        }
        else {
            this.setExtendedState(JFrame.MAXIMIZED_BOTH);
        }

        this.setVisible(true);

        // Iniciación del icono en la barra de herramientas
        if (SystemTray.isSupported()) {

            // Activarla como ocultable
            this.ocultable = VentanaPrincipal.OCULTABLE;

            // Añadir el icono al barra
            SystemTray barraHerramientas = SystemTray.getSystemTray();
            iconoBarraHerramientas = new TrayIcon(image, "JAJIM_1.2");
            iconoBarraHerramientas.setImageAutoSize(true);
            try {
                barraHerramientas.add(iconoBarraHerramientas);
            }
            catch (AWTException ae) {
                iconoBarraHerramientas = null;
            }

            if (iconoBarraHerramientas != null) {
                // Asignarle un oyente
                iconoBarraHerramientas.addActionListener(new VisualizarVentanaActionListener());
                // Asignarle un menú popup
                popupMenu = new PopupMenu();

                menuEstado = new Menu(texto.getString("estado_menu"));
                menuEstado.setEnabled(false);
                for (int i = 0; i < itemsEstado.length; i++) {
                    itemsEstado[i] = new MenuItem(estadosUsuario[i]);
                    menuEstado.add(itemsEstado[i]);
                    itemsEstado[i].addActionListener(new CambiarEstadoActionListener());
                    itemsEstado[i].setActionCommand(i + "");
                }
                popupMenu.add(menuEstado);

                for (int i = 0; i < itemsDeMenuPopupCad.length; i++) {
                    itemsPopup[i] = new MenuItem(itemsDeMenuPopupCad[i]);
                    itemsPopup[i].addActionListener(actionListenersMenuPopup[i]);
                    popupMenu.add(itemsPopup[i]);
                    iconoBarraHerramientas.setPopupMenu(popupMenu);
                }
            }
        }
        else {
            this.ocultable = VentanaPrincipal.NO_OCULTABLE;
            iconoBarraHerramientas = null;
        }

        // Iniciación de los controladores
        CuentaControlador cc = CuentaControlador.getInstancia();
        if (cc.getCuenta() == null) {
            new CrearOAñadirFormulario(this);
        }

        // Poner la cuenta activa en el panel
        String idCuenta = cc.getCuenta();
        PanelContactos.getInstancia().cambiarCuenta(idCuenta);

        // Añadir el listener del combo
        estado.addActionListener(new CambiarEstadoActionListener());

        // Añadir un oyente cuando se cierre la ventana, para guardar la informa
        // ción necesaria para la aplicación.
        this.addWindowListener(new VentanaPrincipalWindowListener());
    }

    /**
     * Modifica la cadena que contiene el nombre de la cuenta activa.
     * <p>
     * @param idCuenta Nuevo nombre para la cuenta activa.
     */
    public void cambiarCuenta(String idCuenta) {
        PanelContactos.getInstancia().cambiarCuenta(idCuenta);
    }

    /**
     * Realiza la operaciones necesarias una vez que la conexión se ha establecido
     */
    public void conexionEstablecida() {

        // Activar el combo
        estado.setEnabled(true);
        estado.setSelectedIndex(0);

        // Activar el menú de la barra de herramientas
        if (this.isOcultable()) {
            menuEstado.setEnabled(true);
        }

        // Activar y desactivar los botones adecuados
        for (int i = 0; i < menus.length; i++) {
            for (int j = 0; j < itemsDeMenu[i].length; j++) {
                itemsDeMenu[i][j].setEnabled(conConexion[i][j]);
                if (i == 0) {
                    botonesBarraDeHerramientas[j].setEnabled(conConexion[i][j]);
                }
            }
        }
    }

    /**
     * Realiza las operaciones necesarias una vez que la conexión se ha cancelado.
     */
    public void conexionCancelada() {

        // Desvincular al controlador de contactos de la conexión
        ContactosControlador ctc = ContactosControlador.getInstancia();
        ctc.desconectar();

        // Desvincular al controlador de transferencias de la conexión.
        TransferenciaFicherosControlador.getInstancia().desconectar();

        // Desctivar el roster
        PanelContactos.getInstancia().conexionCancelada();

        // Desactivar el combo. Tener cuidado de no mandar mensajes de presencia
        // de más
        CambiarEstadoActionListener ceal = (CambiarEstadoActionListener) estado.getActionListeners()[0];
        ceal.setEstadoActual(5);
        estado.setSelectedIndex(ComboBoxRenderer.getLongitud() - 1);
        ceal.setEstadoActual(0);
        estado.setEnabled(false);

        // Desactivar el menú de la barra de herramientas
        if (this.isOcultable()) {
            menuEstado.setEnabled(false);
        }

        // Activar y desactivar los botones adecuados
        for (int i = 0; i < menus.length; i++) {
            for (int j = 0; j < itemsDeMenu[i].length; j++) {
                itemsDeMenu[i][j].setEnabled(sinConexion[i][j]);
                if (i == 0 && j < 3) {
                    botonesBarraDeHerramientas[j].setEnabled(sinConexion[i][j]);
                }
            }
        }
    }

    /**
     * Retorna verdadero si la ventana es ocultable por el hecho de existir barra de herramientas y falso en caso
     * contrario.
     * <p>
     * @return verdadero si la ventana es ocultable por el hecho de existir barra de herramientas y falso en caso
     *         contrario.
     */
    public boolean isOcultable() {
        return ocultable == VentanaPrincipal.OCULTABLE;
    }

    /**
     * Método estático utilizado para implementar el Singleton.
     * <p>
     * @return Retorna la única instancia que hay de la ventana principal.
     */
    public static VentanaPrincipal getInstancia() {

        // Si la instancia es nula, crea una nueva. Si no retorna la ya existente
        if (instancia == null) {
            instancia = new VentanaPrincipal();
        }

        return instancia;
    }

    /**
     * Devuelve el ComboBox de los estados.
     * <p>
     * @return El ComboBox de los estados.
     */
    public JComboBox getComboBox() {
        return estado;
    }
}

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
package org.jajim.interfaz.utilidades;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Container;
import java.awt.Font;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.Enumeration;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Observable;
import java.util.Observer;
import java.util.ResourceBundle;
import java.util.Set;
import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JTree;
import javax.swing.SwingUtilities;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreeNode;
import javax.swing.tree.TreePath;
import org.jajim.interfaz.listeners.AñadirContactoAGrupoMenuActionListener;
import org.jajim.interfaz.listeners.EliminarContactoActionListener;
import org.jajim.interfaz.listeners.EliminarContactoDeGrupoActionListener;
import org.jajim.interfaz.listeners.EliminarGrupoDeContactosActionListener;
import org.jajim.interfaz.listeners.IniciarChatMultiusuarioMenuActionListener;
import org.jajim.interfaz.listeners.IniciarChatPrivadoActionListener;
import org.jajim.interfaz.listeners.ModificarGrupoDeContactosMenuActionListener;
import org.jajim.main.Main;
import org.jajim.modelo.conexiones.EventosDeConexionEnumeracion;

/**
 * @author Florencio Cañizal Calles
 * @version 1.2 Clase que maneja los contactos a nivel de interfaz. Implementa el oyente del roster para realizar los
 * cambios oportunos.
 */
public class PanelContactos extends MouseAdapter implements Observer {

    private final ResourceBundle texto = ResourceBundle.getBundle("resources.Idioma", Main.loc);
    private static PanelContactos instancia;

    // Cadenas contactos
    private final String[] cadenasContactos = {
        texto.getString("chat_privado_item_menu"),
        texto.getString("chat_multiusuario_item_menu"),
        texto.getString("eliminar_contacto_item_menu"),
        texto.getString("añadir_contacto_a_grupo_item_menu"),
        texto.getString("eliminar_contacto_de_grupo_item_menu")
    };

    private final String[] cadenasGrupos = {
        texto.getString("modificar_grupo_item_menu"),
        texto.getString("eliminar_grupo_item_menu")
    };

    private final String[] iconosContactos = {
        "icons/chat_privado.png",
        "icons/chat_multiusuario.png",
        "icons/eliminar_contacto.png",
        "icons/añadir_contacto_a_grupo.png",
        "icons/eliminar_contacto_de_grupo.png"
    };

    private final String[] iconosGrupos = {
        "icons/modificar_grupo.png",
        "icons/eliminar_grupo.png"
    };

    // Listeners
    private final ActionListener[] contactosListeners = {
        new IniciarChatPrivadoActionListener(),
        new IniciarChatMultiusuarioMenuActionListener(),
        new EliminarContactoActionListener(),
        new AñadirContactoAGrupoMenuActionListener(),
        new EliminarContactoDeGrupoActionListener()
    };

    private final ActionListener[] gruposListeners = {
        new ModificarGrupoDeContactosMenuActionListener(),
        new EliminarGrupoDeContactosActionListener()
    };

    // Panel principal
    private final JPanel panelContactos;
    private final JLabel cuenta;
    private final JTree arbolContactos;
    private final JPopupMenu menuContactos;
    private final JMenuItem[] itemsContactos = new JMenuItem[cadenasContactos.length];
    private final JPopupMenu menuGrupos;
    private final JMenuItem[] itemsGrupos = new JMenuItem[cadenasGrupos.length];

    /**
     * Constructor de la clase inicializa los elementos de la interfaz necesarios.
     * <p>
     * @param cp El contenedor en el que se va a colocar el panel de contactos.
     */
    public PanelContactos(Container cp) {

        // Creación del panel central
        panelContactos = new JPanel(new BorderLayout());
        panelContactos.setBackground(Color.WHITE);
        cp.add(BorderLayout.CENTER, panelContactos);

        cuenta = new JLabel();
        cuenta.setHorizontalAlignment(JLabel.CENTER);
        cuenta.setFont(new Font(Font.DIALOG, Font.BOLD, 12));
        panelContactos.add(BorderLayout.NORTH, cuenta);

        Object nada[] = new Object[0];
        arbolContactos = new JTree(nada);
        arbolContactos.setBorder(BorderFactory.createEmptyBorder(15, 10, 0, 0));
        arbolContactos.setCellRenderer(new TreeRenderer());
        arbolContactos.addMouseListener(this);
        panelContactos.add(BorderLayout.CENTER, arbolContactos);

        // Crear menú Popup de contactos
        menuContactos = new JPopupMenu();
        menuContactos.setBorderPainted(true);
        for (int i = 0; i < itemsContactos.length; i++) {
            itemsContactos[i] = new JMenuItem(cadenasContactos[i], new ImageIcon(ClassLoader.getSystemResource(
                iconosContactos[i])));
            itemsContactos[i].addActionListener(contactosListeners[i]);
            menuContactos.add(itemsContactos[i]);
        }

        // Crear menú Popup de grupos
        menuGrupos = new JPopupMenu();
        menuContactos.setBorderPainted(true);
        for (int i = 0; i < itemsGrupos.length; i++) {
            itemsGrupos[i] = new JMenuItem(cadenasGrupos[i], new ImageIcon(ClassLoader.
                getSystemResource(iconosGrupos[i])));
            itemsGrupos[i].addActionListener(gruposListeners[i]);
            menuGrupos.add(itemsGrupos[i]);
        }
    }

    /**
     * Modifica la cadena que contiene el nombre de la cuenta activa.
     * <p>
     * @param idCuenta Nuevo nombre para la cuenta activa.
     */
    public void cambiarCuenta(String idCuenta) {
        // Si no se pasa null como argumentos se pone el nombre de la cuenta, en
        // caso contratio se informa de que no hay cuentas disponibles
        if (idCuenta != null) {
            cuenta.setText(idCuenta);
        }
        else {
            cuenta.setText(texto.getString("sin_cuentas_etiqueta"));
        }
    }

    /**
     * Resetea el árbol de contactos para dejarlo a nulo.
     */
    public void conexionCancelada() {
        arbolContactos.setModel(null);
        arbolContactos.repaint();
    }

    /**
     * Oculta el menú popup de los contactos.
     */
    public void cerrarPopupContactos() {
        menuContactos.setVisible(false);
    }

    /**
     * Oculta el menú popup de los grupos.
     */
    public void cerrarPopupGrupos() {
        menuGrupos.setVisible(false);
    }

    /**
     * Método que se ejecuta cada vez que un objeto observable modifica sus datos.
     * <p>
     * @param o   El objeto observable que ha modificado sus datos.
     * @param arg Un parámetro pasado por el objeto observable.
     */
    @Override
    public void update(Observable o, Object arg) {

        if (!(arg instanceof EventosDeConexionEnumeracion)) {

            // Obtener como tenía el usuario los contactos ubicados
            TreePath tp = arbolContactos.getPathForRow(0);
            Enumeration<TreePath> enumeration = null;
            if (tp != null) {
                enumeration = arbolContactos.getExpandedDescendants(tp);
            }

            // Obtener la matriz de contactos
            Map<String, List<String>> gruposContactos = (Map<String, List<String>>) arg;

            // Crear el árbol a partir de los contactos
            DefaultMutableTreeNode root = new DefaultMutableTreeNode();
            DefaultTreeModel modelo = new DefaultTreeModel(root);
            DefaultMutableTreeNode contactos = new DefaultMutableTreeNode(texto.getString("contactos_etiqueta"));
            modelo.insertNodeInto(contactos, root, 0);

            // Recorrer los contactos y añadirlos al árbol
            Set<Entry<String, List<String>>> entradas = gruposContactos.entrySet();
            int i = 0;
            for (Entry<String, List<String>> e : entradas) {
                DefaultMutableTreeNode grupo;
                if (e.getKey().compareTo("") != 0) {
                    grupo = new DefaultMutableTreeNode(e.getKey());
                }
                else {
                    grupo = new DefaultMutableTreeNode(texto.getString("sin_nombre"));
                }

                modelo.insertNodeInto(grupo, contactos, i);
                int j = 0;
                for (String s : e.getValue()) {
                    DefaultMutableTreeNode contact = new DefaultMutableTreeNode(s);
                    modelo.insertNodeInto(contact, grupo, j);
                    j++;
                }
                i++;
            }

            // Los cambios realizados en la interfaz se realizan en el hilo de
            // java swing de este modo se evitan comportamientos anormales.
            final DefaultTreeModel modeloFinal = modelo;
            final Enumeration<TreePath> enumerationFinal = enumeration;
            SwingUtilities.invokeLater(new Runnable() {
                @Override
                public void run() {
                    arbolContactos.setModel(modeloFinal);

                    // Dejar el árbol igual que estaba
                    boolean desplegado = false;
                    if (enumerationFinal != null) {
                        while (enumerationFinal.hasMoreElements()) {
                            TreePath pathAntiguo = enumerationFinal.nextElement();
                            if (pathAntiguo.getPathCount() < 3) {
                                continue;
                            }
                            TreePath expandir = find(arbolContactos, pathAntiguo.getPath());
                            if (expandir == null) {
                                continue;
                            }
                            expandAll(arbolContactos, expandir);
                            while (expandir.getPathCount() > 1) {
                                expandir = expandir.getParentPath();
                                arbolContactos.expandPath(expandir);
                            }
                            desplegado = true;
                        }
                    }

                    // Expandir los grupos si no hay nada expandido o sólo ellos están expandidos.
                    if (enumerationFinal == null || !desplegado) {
                        TreeNode nodo = (TreeNode) arbolContactos.getModel().getRoot();
                        TreeNode nodoContactos = nodo.getChildAt(0);
                        TreePath pathContactos = new TreePath(nodo);
                        pathContactos = pathContactos.pathByAddingChild(nodoContactos);
                        arbolContactos.expandPath(pathContactos);
                    }
                }
            });
        }
    }

    /**
     * Expande todos los nodos descendientes de un nodo de un árbol.
     * <p>
     * @param tree   El árbol de cual se quieren expandir los nodos.
     * @param parent El padre de los nodos que se quieren expandir.
     */
    private void expandAll(JTree tree, TreePath parent) {
        // Recuperar los hijos del padre e intentar expandirlos.
        TreeNode node = (TreeNode) parent.getLastPathComponent();
        if (node.getChildCount() >= 0) {
            for (Enumeration e = node.children(); e.hasMoreElements();) {
                TreeNode n = (TreeNode) e.nextElement();
                TreePath path = parent.pathByAddingChild(n);
                expandAll(tree, path);
            }
        }

        // Expandir el nodo padre
        tree.expandPath(parent);
    }

    /**
     * Devuelve el TreePath del árbol equivalente con el array de objetos.
     * <p>
     * @param tree  El árbol del que se quiere obtener el TreePath.
     * @param nodes El array que representa el TreePath que se quiere obtener.
     * @return El TreePath correspondiente o null si no se localiza éste.
     */
    private TreePath find(JTree tree, Object[] nodes) {

        // Consigue la longitud del path
        int longitud = nodes.length;

        // Consigue el nodo principal del arbol
        TreeNode nodo = (TreeNode) tree.getModel().getRoot();
        TreePath path = new TreePath((TreeNode) tree.getModel().getRoot());

        // Bucle de búsqueda
        int i = 1;
        while (i < longitud) {
            for (int j = 0; j < nodo.getChildCount(); j++) {
                TreeNode nodoAux = nodo.getChildAt(j);
                if (nodoAux.toString().compareTo(nodes[i].toString()) == 0) {
                    nodo = nodoAux;
                    path = path.pathByAddingChild(nodo);
                    break;
                }
                else if (j == nodo.getChildCount() - 1) {
                    return null;
                }
            }
            i++;
        }

        return path;
    }

    /**
     * Método que se ejecuta cada vez que el usuario hace click con el ratón en el árbol de los contactos.
     * <p>
     * @param e El evento que produce la ejecución del método.
     */
    @Override
    public void mouseClicked(MouseEvent e) {

        // Si es no es instancia del menú popup
        if (e.getSource() instanceof JTree) {
            if (e.getButton() == MouseEvent.BUTTON3) {

                JTree jt = (JTree) e.getSource();
                TreePath tp = jt.getClosestPathForLocation(e.getX(), e.getY());

                // Si se selecciona un grupo se visualiza el menu de grupos y se
                // actualizan sus listeners
                if (tp.getPathCount() == 3) {

                    menuGrupos.setLocation(e.getXOnScreen(), e.getYOnScreen());
                    menuGrupos.setVisible(true);
                    String grupo = tp.getLastPathComponent().toString();
                    for (int k = 0; k < gruposListeners.length; k++) {
                        itemsGrupos[k].setActionCommand(grupo);
                    }
                }

                // Si se selecciona un grupo se visualiza el menu de contactos
                // actualizan sus listeners
                if (tp.getPathCount() == 4) {

                    menuContactos.setLocation(e.getXOnScreen(), e.getYOnScreen());
                    menuContactos.setVisible(true);
                    String cadenaContacto = tp.getLastPathComponent().toString();
                    int posicion = cadenaContacto.indexOf("(");
                    String s = cadenaContacto.substring(0, posicion);
                    String cadenaGrupo = tp.getPathComponent(2).toString();
                    // Si no se está conectado se deshabilita los botones de chat.
                    if (cadenaContacto.contains("unavailable")) {
                        itemsContactos[0].setEnabled(false);
                        itemsContactos[1].setEnabled(false);
                    }
                    else {
                        itemsContactos[0].setEnabled(true);
                        itemsContactos[1].setEnabled(true);
                    }
                    for (int k = 0; k < contactosListeners.length; k++) {
                        itemsContactos[k].setActionCommand(s);
                        itemsContactos[k].setName(cadenaGrupo);
                    }
                }
            }
            else {
            }
        }
    }

    /**
     * Método que se ejecuta cuando el usuario introduce el ratón en el árbol de contactos.
     * <p>
     * @param e El evento que produce la ejecución del método.
     */
    @Override
    public void mouseEntered(MouseEvent e) {

        // Cerrar los menús popup si estaban abiertos
        if (e.getSource() instanceof JTree) {
            if (menuContactos.isVisible()) {
                menuContactos.setVisible(false);
            }
            if (menuGrupos.isVisible()) {
                menuGrupos.setVisible(false);
            }
        }
    }

    /**
     * Método estático utilizado para implementar el Singleton.
     * <p>
     * @return Retorna la única instancia que hay del oyente de la conexión.
     */
    public static PanelContactos getInstancia() {
        return instancia;
    }

    /**
     * Método estático utilizado para implementar el Singleton.
     * <p>
     * @param cp El contenedor en el que se va a poner el panel de contactos.
     * @return Retorna la única instancia que hay del oyente de la conexión.
     */
    public static PanelContactos getInstancia(Container cp) {

        // Si la instancia es nula, crea una nueva. Si no retorna la ya existente
        if (instancia == null) {
            instancia = new PanelContactos(cp);
        }

        return instancia;
    }
}

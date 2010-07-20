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

import org.jajim.interfaz.listeners.EliminarGrupoDeContactosActionListener;
import org.jajim.controladores.ContactosControlador;
import org.jajim.interfaz.listeners.AñadirContactoAGrupoMenuActionListener;
import org.jajim.interfaz.listeners.EliminarContactoActionListener;
import org.jajim.interfaz.listeners.EliminarContactoDeGrupoActionListener;
import org.jajim.interfaz.listeners.IniciarChatMultiusuarioMenuActionListener;
import org.jajim.interfaz.listeners.IniciarChatPrivadoActionListener;
import org.jajim.interfaz.listeners.ModificarGrupoDeContactosMenuActionListener;
import org.jajim.interfaz.ventanas.VentanaPrincipal;
import org.jajim.main.Main;
import org.jajim.modelo.conexiones.EventosDeConexionEnumeracion;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Container;
import java.awt.Font;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.Enumeration;
import java.util.Observable;
import java.util.Observer;
import java.util.ResourceBundle;
import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JTree;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreeNode;
import javax.swing.tree.TreePath;

/**
 * @author Florencio Cañizal Calles
 * @version 1.1
 * Clase que maneja los contactos a nivel de interfaz. Implementa el oyente del
 * roster para realizar los cambios oportunos.
 */
public class PanelContactos extends MouseAdapter implements Observer{

    private ResourceBundle texto = ResourceBundle.getBundle("resources.Idioma",Main.loc);

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
        new IniciarChatPrivadoActionListener(this),
        new IniciarChatMultiusuarioMenuActionListener(this),
        new EliminarContactoActionListener(this),
        new AñadirContactoAGrupoMenuActionListener(this),
        new EliminarContactoDeGrupoActionListener(this)
    };

    private final ActionListener[] gruposListeners = {
        new ModificarGrupoDeContactosMenuActionListener(this),
        new EliminarGrupoDeContactosActionListener(this)
    };

    private VentanaPrincipal vp;
    
    // Panel principal
    private JPanel panelContactos;
    private JLabel cuenta;
    private JTree arbolContactos;
    private JPopupMenu menuContactos;
    private JMenuItem[] itemsContactos = new JMenuItem[cadenasContactos.length];
    private JPopupMenu menuGrupos;
    private JMenuItem[] itemsGrupos = new JMenuItem[cadenasGrupos.length];
    
    /**
     * Constructor de la clase inicializa los elementos de la interfaz necesarios.
     * @param vp Ventana principal de la aplicación.
     */
    public PanelContactos(VentanaPrincipal vp){

        // Inicialización de variables
        this.vp = vp;

        // Creación de la interfaz
        Container cp = vp.getContentPane();

        // Creación del panel central
        panelContactos = new JPanel(new BorderLayout());
        panelContactos.setBackground(Color.WHITE);
        cp.add(BorderLayout.CENTER,panelContactos);

        cuenta = new JLabel();
        cuenta.setHorizontalAlignment(JLabel.CENTER);
        cuenta.setFont(new Font(Font.DIALOG,Font.BOLD,12));
        panelContactos.add(BorderLayout.NORTH,cuenta);

        Object nada[] = new Object[0];
        arbolContactos = new JTree(nada);
        arbolContactos.setBorder(BorderFactory.createEmptyBorder(15,10,0,0));
        arbolContactos.setCellRenderer(new TreeRenderer());
        arbolContactos.addMouseListener(this);
        panelContactos.add(BorderLayout.CENTER,arbolContactos);

        // Crear menú Popup de contactos
        menuContactos = new JPopupMenu();
        menuContactos.setBorderPainted(true);
        for(int i = 0;i < itemsContactos.length;i++){
            itemsContactos[i] = new JMenuItem(cadenasContactos[i],new ImageIcon(ClassLoader.getSystemResource(iconosContactos[i])));
            itemsContactos[i].addActionListener(contactosListeners[i]);
            menuContactos.add(itemsContactos[i]);
        }

        // Crear menú Popup de grupos
        menuGrupos = new JPopupMenu();
        menuContactos.setBorderPainted(true);
        for(int i = 0;i < itemsGrupos.length;i++){
            itemsGrupos[i] = new JMenuItem(cadenasGrupos[i],new ImageIcon(ClassLoader.getSystemResource(iconosGrupos[i])));
            itemsGrupos[i].addActionListener(gruposListeners[i]);
            menuGrupos.add(itemsGrupos[i]);
        }
    }

    /**
     * Modifica la cadena que contiene el nombre de la cuenta activa.
     * @param idCuenta Nuevo nombre para la cuenta activa.
     */
    public void cambiarCuenta(String idCuenta){
        // Si no se pasa null como argumentos se pone el nombre de la cuenta, en
        // caso contratio se informa de que no hay cuentas disponibles
        if(idCuenta != null)
            cuenta.setText(idCuenta);
        else
            cuenta.setText(texto.getString("sin_cuentas_etiqueta"));
    }

    /**
     * Resetea el árbol de contactos para dejarlo a nulo.
     */
    public void conexionCancelada(){
        arbolContactos.setModel(null);
        arbolContactos.repaint();
    }

    /**
     * Oculta el menú popup de los contactos.
     */
    public void cerrarPopupContactos(){
        menuContactos.setVisible(false);
    }

    /**
     * Oculta el menú popup de los grupos.
     */
    public void cerrarPopupGrupos(){
        menuGrupos.setVisible(false);
    }

    /**
     * Método que se ejecuta cada vez que un objeto observable modifica sus datos.
     * @param o El objeto observable que ha modificado sus datos.
     * @param arg Un parámetro pasado por el objeto observable.
     */
    @Override
    public void update(Observable o, Object arg) {

        if(!(arg instanceof EventosDeConexionEnumeracion)){

            // Obtener como tenía el usuario los contactos ubicados
            TreePath tp = arbolContactos.getPathForRow(0);
            Enumeration<TreePath> enumeration = null;
            if(tp != null)
                enumeration = arbolContactos.getExpandedDescendants(tp);

            // Obtener la matriz de contactos
            String[][] matrizContactos = (String[][]) arg;

            // Crear el árbol a partir de los contactos
            DefaultMutableTreeNode root = new DefaultMutableTreeNode();
            DefaultTreeModel modelo = new DefaultTreeModel(root);
            DefaultMutableTreeNode contactos = new DefaultMutableTreeNode(texto.getString("contactos_etiqueta"));
            modelo.insertNodeInto(contactos,root,0);

            // Bucle que añade los contactos al árbol
            for(int i = 0;i < matrizContactos.length;i++){
                DefaultMutableTreeNode grupo;
                if(matrizContactos[i][0].compareTo("") != 0)
                    grupo = new DefaultMutableTreeNode(matrizContactos[i][0]);
                else
                    grupo = new DefaultMutableTreeNode(texto.getString("sin_nombre"));

                modelo.insertNodeInto(grupo,contactos,i);
                for(int j = 1;j < matrizContactos[i].length;j++){
                     DefaultMutableTreeNode contact = new DefaultMutableTreeNode(matrizContactos[i][j]);
                     modelo.insertNodeInto(contact,grupo,j - 1);
                }
            }
            arbolContactos.setModel(modelo);

            // Dejar el árbol igual que estaba
            boolean desplegado = false;
            if(enumeration != null){
                while(enumeration.hasMoreElements()){
                    TreePath pathAntiguo = enumeration.nextElement();
                    if(pathAntiguo.getPathCount() < 3)
                        continue;
                    TreePath expandir = find(arbolContactos,pathAntiguo.getPath());
                    if(expandir == null)
                        continue;
                    this.expandAll(arbolContactos,expandir);
                    while(expandir.getPathCount() > 1){
                        expandir = expandir.getParentPath();
                        arbolContactos.expandPath(expandir);
                    }
                    desplegado = true;
                }
            }

            // Expandir los grupos si no hay nada expandido o sólo ellos están expandidos.
            if(enumeration == null || !desplegado){
                TreeNode nodo = (TreeNode)arbolContactos.getModel().getRoot();
                TreeNode nodoContactos = nodo.getChildAt(0);
                TreePath pathContactos = new TreePath(nodo);
                pathContactos = pathContactos.pathByAddingChild(nodoContactos);
                arbolContactos.expandPath(pathContactos);
            }
        }
    }

    /**
     * Expande todos los nodos descendientes de un nodo de un árbol.
     * @param tree El árbol de cual se quieren expandir los nodos.
     * @param parent El padre de los nodos que se quieren expandir.
     */
    private void expandAll(JTree tree, TreePath parent) {
        // Recuperar los hijos del padre e intentar expandirlos.
        TreeNode node = (TreeNode)parent.getLastPathComponent();
        if (node.getChildCount() >= 0) {
            for (Enumeration e=node.children(); e.hasMoreElements(); ) {
                TreeNode n = (TreeNode)e.nextElement();
                TreePath path = parent.pathByAddingChild(n);
                expandAll(tree, path);
            }
        }

        // Expandir el nodo padre
        tree.expandPath(parent);
    }

    /**
     * Devuelve el TreePath del árbol equivalente con el array de objetos.
     * @param tree El árbol del que se quiere obtener el TreePath.
     * @param nodes El array que representa el TreePath que se quiere obtener.
     * @return El TreePath correspondiente o null si no se localiza éste.
     */
    private TreePath find(JTree tree, Object[] nodes) {

        // Consigue la longitud del path
        int longitud = nodes.length;

        // Consigue el nodo principal del arbol
        TreeNode nodo = (TreeNode)tree.getModel().getRoot();
        TreePath path = new TreePath((TreeNode)tree.getModel().getRoot());

        // Bucle de búsqueda
        int i = 1;
        while(i < longitud){
            for(int j = 0;j < nodo.getChildCount();j++){
                TreeNode nodoAux = nodo.getChildAt(j);
                if(nodoAux.toString().compareTo(nodes[i].toString()) == 0){
                    nodo = nodoAux;
                    path = path.pathByAddingChild(nodo);
                    break;
                }
                else if(j == nodo.getChildCount() - 1)
                    return null;
            }
            i++;
        }

        return path;
    }


    /**
     * Método que se ejecuta cada vez que el usuario hace click con el ratón en
     * el árbol de los contactos.
     * @param e El evento que produce la ejecución del método.
     */
    @Override
    public void mouseClicked(MouseEvent e){
        
        // Si es no es instancia del menú popup
        if(e.getSource() instanceof JTree){
            if(e.getButton() == MouseEvent.BUTTON3){

                JTree jt = (JTree) e.getSource();
                TreePath tp = jt.getClosestPathForLocation(e.getX(),e.getY());

                // Si se selecciona un grupo se visualiza el menu de grupos y se
                // actualizan sus listeners
                if(tp.getPathCount() == 3){

                    menuGrupos.setLocation(e.getXOnScreen(),e.getYOnScreen());
                    menuGrupos.setVisible(true);
                    String grupo = tp.getLastPathComponent().toString();
                    for(int k = 0;k < gruposListeners.length;k++){
                        itemsGrupos[k].setActionCommand(grupo);
                    }
                }

                // Si se selecciona un grupo se visualiza el menu de contactos
                // actualizan sus listeners
                if(tp.getPathCount() == 4){

                    menuContactos.setLocation(e.getXOnScreen(),e.getYOnScreen());
                    menuContactos.setVisible(true);
                    String cadenaContacto = tp.getLastPathComponent().toString();
                    int posicion = cadenaContacto.indexOf("(");
                    String s = cadenaContacto.substring(0,posicion);
                    String cadenaGrupo = tp.getPathComponent(2).toString();
                    // Si no se está conectado se deshabilita los botones de chat.
                    if(cadenaContacto.contains("unavailable")){
                        itemsContactos[0].setEnabled(false);
                        itemsContactos[1].setEnabled(false);
                    }
                    else{
                        itemsContactos[0].setEnabled(true);
                        itemsContactos[1].setEnabled(true);
                    }
                    for(int k = 0;k < contactosListeners.length;k++){
                        itemsContactos[k].setActionCommand(s);
                        itemsContactos[k].setName(cadenaGrupo);
                    }
                }
            }
            else{
                return;
            }
        }
    }

    /**
     * Método que se ejecuta cuando el usuario introduce el ratón en el árbol de
     * contactos.
     * @param e El evento que produce la ejecución del método.
     */
    @Override
    public void mouseEntered(MouseEvent e){

        // Cerrar los menús popup si estaban abiertos
        if(e.getSource() instanceof JTree){
            if(menuContactos.isVisible())
                menuContactos.setVisible(false);
            if(menuGrupos.isVisible())
                menuGrupos.setVisible(false);
        }
    }

    /**
     * Retorna la ventana princiapal de la aplicación.
     * @return La ventana principal de la aplicación.
     */
    public VentanaPrincipal getVp(){
        return vp;
    }
}

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

import org.jajim.controladores.PreferenciasControlador;
import org.jajim.controladores.TransferenciaFicherosControlador;
import org.jajim.interfaz.listeners.BorrarFicheroActionListener;
import org.jajim.interfaz.listeners.CancelarTransferenciaActionListener;
import org.jajim.interfaz.listeners.RenombrarFicheroMenuActionListener;
import org.jajim.interfaz.listeners.ReubicarFicheroMenuActionListener;
import org.jajim.interfaz.listeners.VisualizarFicheroActionListener;
import org.jajim.interfaz.utilidades.BarraProgresoSwingWorker;
import org.jajim.main.Main;
import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.FlowLayout;
import java.awt.Dimension;
import java.awt.Image;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;
import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JTable;
import javax.swing.JToolBar;
import javax.swing.ListSelectionModel;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumn;

/**
 * @author Florencio Cañizal Calles
 * @version 1.1
 * Supone la interfaz del gestor de transferencias de la aplicación.
 */
public class VentanaGestorDeTransferencias extends JFrame implements ActionListener{

    private ResourceBundle texto = ResourceBundle.getBundle("resources.Idioma",Main.loc);

    // Cadenas constantes
    private final String[] pestañas = {
        texto.getString("gestor_de_transferencias_pestaña_descargados"),
        texto.getString("gestor_de_transferencias_pestaña_desgargando")
    };

    private final String sinTransferenciasPendientes = texto.getString("gestor_de_transferencias_sin_transferencias_pendientes");
    private final String cerrar = texto.getString("cerrar");

    private final String[] iconos  = {
        "icons/visualizar_fichero.png",
        "icons/borrar_fichero.png",
        "icons/renombrar_fichero.png",
        "icons/reubicar_fichero.png"
    };

    private final String[] textoIconos = {
        texto.getString("gestor_de_transferencias_iconos_visualizar_fichero"),
        texto.getString("gestor_de_transferencias_iconos_borrar_fichero"),
        texto.getString("gestor_de_transferencias_iconos_renombrar_fichero"),
        texto.getString("gestor_de_transferencias_iconos_reubicar_fichero")
    };

    private final String[] titulosTabla = {
        texto.getString("gestor_de_transferencias_tabla_nombre"),
        texto.getString("gestor_de_transferencias_tabla_ruta")
    };

    private final ActionListener[] actionListenersBarraDeAcciones = {
        new VisualizarFicheroActionListener(this),
        new BorrarFicheroActionListener(this),
        new RenombrarFicheroMenuActionListener(this),
        new ReubicarFicheroMenuActionListener(this)
    };

    // Elementos de la interfaz
    private JTabbedPane tablonDePestañas;
    private JPanel descargadosPanel;
    private JPanel descargandoPanel;
    private JLabel etiquetaSinTransferenciasPendientes = new JLabel(sinTransferenciasPendientes);
    private JToolBar barraDeAcciones;
    private JButton[] botonesBarraDeHerramientas = new JButton[iconos.length];
    private JTable tablaDeFicheros;
    private JButton botonCerrar;

    // Controladores utilizados
    private TransferenciaFicherosControlador tfc;

    // Tipos de transferencias
    public final static int EMISOR = 0;
    public final static int RECEPTOR = 1;

    // Estado del gestor
    public final static int SIN_TRANSFERENCIAS = 0;
    public final static int CON_TRANSFERENCIAS = 1;
    private int estado;

    // Lista de hilos de actualización de la barra
    private List<BarraProgresoSwingWorker> workers;
    private Map<String,JPanel> paneles;

    /**
     * Constructor de la clase. Inicializa las variables necesarias. Crea la inter
     * faz de usuario.
     * @param vp La ventana principal de la aplicación.
     * @param tfc El controlador de las transferencias.
     */
    public VentanaGestorDeTransferencias(VentanaPrincipal vp,TransferenciaFicherosControlador tfc){

        // Inicializar variables
        this.tfc = tfc;
        estado = VentanaGestorDeTransferencias.SIN_TRANSFERENCIAS;
        workers = new ArrayList<BarraProgresoSwingWorker>();
        paneles = new HashMap<String,JPanel>();

        // Creación de la interfaz
        Container cp = this.getContentPane();

        // Añadir las pestañas
        tablonDePestañas = new JTabbedPane();
        tablonDePestañas.setBorder(BorderFactory.createEmptyBorder(15,10,15,10));
        for(int i = 0;i < pestañas.length;i++){
            if(i == 0)
                iniciarPestañaFicheros();
            else
                iniciarPestañaTransferencias();
        }
        cp.add(BorderLayout.CENTER,tablonDePestañas);

        // Añadir botón
        JPanel panelBotones = new JPanel(new FlowLayout(FlowLayout.CENTER));
        panelBotones.setBorder(BorderFactory.createEmptyBorder(0,0,7,10));
        botonCerrar = new JButton(cerrar);
        botonCerrar.addActionListener(this);
        panelBotones.add(botonCerrar);
        cp.add(BorderLayout.SOUTH,panelBotones);

        // Iniciación de la interfaz
        Image image = Toolkit.getDefaultToolkit().getImage(ClassLoader.getSystemResource("icons/gestor_de_transferencias.png"));
        this.setIconImage(image);
        this.setTitle(texto.getString("gestor_de_transferencias_title"));
        this.setResizable(false);
        PreferenciasControlador pfc = PreferenciasControlador.getInstancia();
        this.setLocation(pfc.getGestorDeTransferenciasX(),pfc.getGestorDeTransferenciasY());
        this.setSize(500,400);
        this.setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
    }

    /**
     * Inicializa la interfaz de la pestaña que se ocupa de manejar los ficheros
     * ya transferidos.;
     */
    private void iniciarPestañaFicheros(){

        descargadosPanel = new JPanel(new BorderLayout());
        descargadosPanel.setBorder(BorderFactory.createEmptyBorder(5,10,5,10));
        
        // Añadir la barra de acciones
        barraDeAcciones = new JToolBar();
        barraDeAcciones.setLayout(new FlowLayout(FlowLayout.CENTER));
        for(int i = 0;i < iconos.length;i++){
            botonesBarraDeHerramientas[i] = new JButton(new ImageIcon(ClassLoader.getSystemResource(iconos[i])));
            botonesBarraDeHerramientas[i].addActionListener(actionListenersBarraDeAcciones[i]);
            botonesBarraDeHerramientas[i].setToolTipText(textoIconos[i]);
            barraDeAcciones.add(botonesBarraDeHerramientas[i]);
        }
        barraDeAcciones.setFloatable(false);
        barraDeAcciones.setBorderPainted(true);
        descargadosPanel.add(BorderLayout.NORTH,barraDeAcciones);

        // Añadir la tabla
        JPanel panelDeTabla = new JPanel(new BorderLayout());
        panelDeTabla.setBorder(BorderFactory.createEmptyBorder(5,0,0,0));
        DefaultTableModel dtm = new DefaultTableModel(titulosTabla,0);
        tablaDeFicheros = new JTable(dtm){
            @Override
            public boolean isCellEditable(int row,int column){
                return false;
            }
        };
        
        tablaDeFicheros.setPreferredScrollableViewportSize(new Dimension(450,100));
        tablaDeFicheros.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        tablaDeFicheros.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
        TableColumn  columna = null;
        columna = tablaDeFicheros.getColumnModel().getColumn(0);
        columna.setPreferredWidth(150);
        columna = tablaDeFicheros.getColumnModel().getColumn(1);
        columna.setPreferredWidth(290);
        JScrollPane scrollPane = new JScrollPane(tablaDeFicheros);
        panelDeTabla.add(BorderLayout.CENTER,scrollPane);
        descargadosPanel.add(BorderLayout.CENTER,panelDeTabla);
        tablonDePestañas.addTab(pestañas[0],descargadosPanel);
    }

    /**
     * Inicializa la interfaz de la pestaña que se ocupa de manejar las transfe
     * rencias pendientes.
     */
    private void iniciarPestañaTransferencias(){
        // Añadirlo al panel
        descargandoPanel = new JPanel();
        descargandoPanel.setLayout(new BoxLayout(descargandoPanel,BoxLayout.Y_AXIS));
        descargandoPanel.setBorder(BorderFactory.createEmptyBorder(5,10,5,10));
        descargandoPanel.add(etiquetaSinTransferenciasPendientes);
        tablonDePestañas.addTab(pestañas[1],descargandoPanel);
    }

    /**
     * Añade una barra de progreso que especifica como va la transferencia del fi
     * chero.
     * @param nombre Nombre del fichero transferido.
     * @param idTransferencia El identificador de la transferencia en curso.
     * @param tipo El papel que se juega en la transferencia (Emisor o receptor)
     */
    public void añadirTransferenciaDeFichero(String nombre,String idTransferencia,int tipo){

        if(estado == VentanaGestorDeTransferencias.SIN_TRANSFERENCIAS){
            estado = VentanaGestorDeTransferencias.CON_TRANSFERENCIAS;
            descargandoPanel = new JPanel();
            descargandoPanel.setLayout(new BoxLayout(descargandoPanel,BoxLayout.Y_AXIS));
            tablonDePestañas.remove(1);
            tablonDePestañas.addTab(pestañas[1],descargandoPanel);
        }

        // Cear un panel de con el nombre del fichero, la barra de progreso y un
        // botón de cancelación.
        JPanel transferencia = new JPanel(new BorderLayout());
        transferencia.setBorder(BorderFactory.createEmptyBorder(0,0,5,0));
        Dimension dimension = this.getSize();
        transferencia.setSize(dimension.width,30);
        transferencia.setPreferredSize(new Dimension(dimension.width,30));
        transferencia.setMaximumSize(new Dimension(dimension.width,30));
        JLabel nombreFichero = new JLabel(nombre + ":");
        nombreFichero.setSize((dimension.width) / 5 + 10,30);
        nombreFichero.setPreferredSize(new Dimension((dimension.width) / 5 + 10,30));
        nombreFichero.setMaximumSize(new Dimension((dimension.width) / 5 + 10,30));
        transferencia.add(BorderLayout.WEST,nombreFichero);
        JPanel panelBarra = new JPanel(new BorderLayout());
        panelBarra.setBorder(BorderFactory.createEmptyBorder(0,0,0,5));
        JProgressBar barraDeProgreso = new JProgressBar(0,100);        
        BarraProgresoSwingWorker bpsw = new BarraProgresoSwingWorker(this,barraDeProgreso,tfc,idTransferencia,tipo);
        panelBarra.add(BorderLayout.CENTER,barraDeProgreso);
        transferencia.add(BorderLayout.CENTER,panelBarra);
        JButton botonCancelar = new JButton(new ImageIcon(ClassLoader.getSystemResource("icons/cancelar_transferencia.png")));
        botonCancelar.addActionListener(new CancelarTransferenciaActionListener(this,tfc,idTransferencia));
        transferencia.add(BorderLayout.EAST,botonCancelar);

        // Añadir al cuadro de transferencias
        descargandoPanel.add(transferencia);

        // Hacer visible el cuadro de diálogo
        tablonDePestañas.setSelectedIndex(1);
        this.hacerVisible();
        bpsw.execute();
        workers.add(bpsw);
        paneles.put(idTransferencia,transferencia);
    }

    /**
     * Elimina la transferencia, cuyo identificador se corresponde con el suminis
     * trado, de la lista de transferencias pendientes del gestor.
     * @param idTransferencia El identificador de la transferencia que se va a
     * eliminar.
     */
    public void eliminarTransferencia(String idTransferencia){

        // Recorrer la lista de barras de progeso para detener la adecuada
        for(BarraProgresoSwingWorker bpsw : workers){
            if(idTransferencia.compareTo(bpsw.getIdTransferencia()) == 0){
                bpsw.cancel(true);
                workers.remove(bpsw);
                break;
            }
        }

        // Eliminar el panel de las transferencias
        if(paneles.size() == 1){
            tablonDePestañas.remove(1);
            this.iniciarPestañaTransferencias();
            tablonDePestañas.setSelectedIndex(1);
            tablonDePestañas.repaint();
            paneles.clear();
            estado = VentanaGestorDeTransferencias.SIN_TRANSFERENCIAS;
        }
        else{
            JPanel panel = paneles.get(idTransferencia);
            paneles.remove(idTransferencia);
            descargandoPanel.remove(panel);
            descargandoPanel.repaint();
        }
    }

    /**
     * Método que actualiza la interfaz una vez que la transferencia la terminado
     * correctamente. Borra la transferencia del panel de transferencias en descar
     * ga y actuliza la tabla de transferencias descargadas, si el tipo de trans
     * ferencia es de recepción.
     * @param idTransferencia El identificador de la transferencia que se ha de
     * finalizar.
     * @param tipo El tipo de transferencia.
     * @param datos Los datos interesantes correspnodientes a la misma.
     */
    public synchronized void finalizarTransferencia(String idTransferencia,int tipo,String[] datos){

        // Eliminar la transferencia
        this.eliminarTransferencia(idTransferencia);

        // Si el tipo de transferencia es de recepción añadir los datos a la pesta
        // ña de ficheros descargados
        if(tipo == VentanaGestorDeTransferencias.RECEPTOR && datos != null)
            this.añadirFicheroDescargado(datos);
    }

    /**
     * Método que añade un fichero a la tabla de ficheros descargados.
     * @param datos Los datos del nuevo fichero descargado.
     */
    private void añadirFicheroDescargado(String[] datos){

        // Recuperar el modelo de la tabla
        DefaultTableModel tm = (DefaultTableModel) tablaDeFicheros.getModel();
        tm.addRow(datos);
    }

    /**
     * Cancela las transferencias en curso del sistema.
     */
    public void abortarTransferencias(){

        // Cancelar todas los hilos de las barras de progreso
        for(BarraProgresoSwingWorker bpsw : workers){
            bpsw.cancel(true);
            workers.remove(bpsw);
        }

        // Limpiar la interfaz
        tablonDePestañas.remove(1);
        this.iniciarPestañaTransferencias();
        tablonDePestañas.setSelectedIndex(1);
        tablonDePestañas.repaint();
        estado = VentanaGestorDeTransferencias.SIN_TRANSFERENCIAS;
        paneles.clear();
        
        // LLamar al controlador para que realice la operación
        tfc.abortarTransferencias();
    }

    /**
     * Cancela aquellas transferencias mantenidas con el contacto seleccionado.
     * @param contacto El contacto cuyas transferencias serán canceladas.
     */
    public void abortarTransferencias(String contacto){

        // Recuperar las transferencias iniciadas por el contacto
        String[] identificadores = tfc.getIdentificadoresTransferencias(contacto);
    
        // Cancelar todos los hilos que manejen dichas tranferencias
        for(int i = 0;i < identificadores.length;i++){
            for(int j = 0;j < workers.size();j++){
                BarraProgresoSwingWorker bpsw = workers.get(j);
                if(identificadores[i].compareTo(bpsw.getIdTransferencia()) == 0){
                    // Cancelar el hilo
                    bpsw.cancel(true);
                    workers.remove(bpsw);
                    j--;
                    // Cancelar la transferencia
                    tfc.cancelarTransferencia(identificadores[i]);
                    // Eliminar el panel de la interfaz
                    if(paneles.size() == 1){
                        // Limpiar la interfaz
                        tablonDePestañas.remove(1);
                        this.iniciarPestañaTransferencias();
                        tablonDePestañas.setSelectedIndex(1);
                        tablonDePestañas.repaint();
                        estado = VentanaGestorDeTransferencias.SIN_TRANSFERENCIAS;
                        paneles.remove(identificadores[i]);
                    }
                    else{
                        JPanel panel = paneles.get(identificadores[i]);
                        paneles.remove(identificadores[i]);
                        descargandoPanel.remove(panel);
                        descargandoPanel.repaint();
                    }
                }
                if(workers.size() == 0)
                    break;
            }
        }
    }

    /**
     * Muestra la ventana del gestor de cuentas en pantalla.
     */
    public void hacerVisible(){
        // Colocar la ventana y hacerla visible
        this.setLocation(this.getX(),this.getY());
        this.setVisible(true);
    }

    /**
     * Devuelve el estado en el que se encuentra el gestor.
     * @return El estado en que se encuentra el gestor.
     */
    public int getEstado(){
        return estado;
    }

    /**
     * Devuelve la tabla que contiene los ficheros descargados.
     * @return Tabla con los ficheros descargados.
     */
    public JTable getTablaDeFicheros(){
        return this.tablaDeFicheros;
    }

    /**
     * Retorna el controlador de las transferencias de ficheros.
     * @return El controlador de las transferencias de ficheros.
     */
    public TransferenciaFicherosControlador getTfc(){
        return tfc;
    }

    /**
     * Método que se ejecuta cuando el usuario selecciona la opción Cerrar del
     * gestor. Oculta la ventana del gestor de transferencias.
     * @param e El evento que produce la ejecucion del método.
     */
    @Override
    public void actionPerformed(ActionEvent e) {
        this.dispose();
    }
}

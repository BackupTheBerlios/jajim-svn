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

import org.jajim.interfaz.ventanas.VentanaPrincipal;
import org.jajim.interfaz.listeners.BuscarContactoActionListener;
import org.jajim.main.Main;
import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ResourceBundle;
import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableModel;

/**
 * @author Florencio Cañizal Calles
 * @version 1.0.1
 * Formulario en el que el usuario puede realizar búsquedas de contactos.
 */
public class BuscarContactoFormulario extends JDialog implements ActionListener{

    private ResourceBundle texto = ResourceBundle.getBundle("resources.Idioma",Main.loc);

    // Cadenas constantes
    private final String principal = texto.getString("buscar_contacto_formulario_principal");

    private final String[] etiquetas = {
        texto.getString("buscar_contacto_formulario_cadena"),
        texto.getString("buscar_contacto_formulario_servidor")
    };

    private final String resultados = texto.getString("buscar_contacto_formulario_resultados");

    private final String buscar = texto.getString("buscar_contacto_formulario_buscar");
    private final String cerrar = texto.getString("cerrar");

    // Iconos
    private final String icono = "icons/añadir_usuario.png";

    // Componentes de la interfaz
    private JLabel cadenaPrincipal;
    private JLabel[] grupoDeEtiquetas = new JLabel[etiquetas.length];
    private JTextField[] grupoDeCampos = new JTextField[etiquetas.length];
    private JLabel cadenaResultados;
    private JButton botonAñadirContacto;
    private JTable conjuntoResultados;
    private JButton botonBuscar;
    private JButton botonCerrar;

    private VentanaPrincipal vp;

    /**
     * Constructor de la clase. Inicializa las variables necesarias. Crea la in
     * terfaz de usuario.
     * @param vp Ventana principal de la aplicación.
     */
    public BuscarContactoFormulario(VentanaPrincipal vp){

        // Inicialización de variables
        super(vp,true);
        this.vp = vp;

        // Creación de la interfaz
        Container cp = this.getContentPane();

        // Creación de la zona de introducción de datos
        JPanel datos = new JPanel(new GridLayout(2,1,5,10));
        cadenaPrincipal = new JLabel(principal);
        cadenaPrincipal.setHorizontalAlignment(JLabel.CENTER);
        cadenaPrincipal.setBorder(BorderFactory.createEmptyBorder(15,10,0,10));
        datos.add(cadenaPrincipal);

        JPanel formulario = new JPanel(new GridLayout(etiquetas.length,2,5,10));
        formulario.setBorder(BorderFactory.createEmptyBorder(0,10,15,10));
        for(int i = 0;i < etiquetas.length;i++){
            grupoDeEtiquetas[i] = new JLabel(etiquetas[i]);
            formulario.add(grupoDeEtiquetas[i]);
            grupoDeCampos[i] = new JTextField();
            formulario.add(grupoDeCampos[i]);
        }
        datos.add(formulario);
        cp.add(BorderLayout.NORTH,datos);

        // Creación de la zona de resultados
        JPanel zonaResultados = new JPanel(new BorderLayout());
        JPanel resultadoYBoton = new JPanel(new GridLayout(1,2,5,10));
        cadenaResultados = new JLabel(resultados);
        resultadoYBoton.add(cadenaResultados);
        JPanel panelBoton = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        botonAñadirContacto = new JButton(new ImageIcon(ClassLoader.getSystemResource(icono)));
        botonAñadirContacto.setActionCommand("Añadir");
        botonAñadirContacto.addActionListener(this);
        botonAñadirContacto.setEnabled(false);
        panelBoton.add(botonAñadirContacto);
        resultadoYBoton.add(panelBoton);
        zonaResultados.add(BorderLayout.NORTH,resultadoYBoton);
        conjuntoResultados = new JTable();
        conjuntoResultados.setPreferredScrollableViewportSize(new Dimension(450,100));
        conjuntoResultados.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        JScrollPane scrollPane = new JScrollPane(conjuntoResultados);
        zonaResultados.add(BorderLayout.CENTER,scrollPane);
        zonaResultados.setBorder(BorderFactory.createEmptyBorder(0,10,15,10));
        cp.add(BorderLayout.CENTER,zonaResultados);

        // Botones
        JPanel botones = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        botones.setBorder(BorderFactory.createEmptyBorder(0,10,6,10));
        botonBuscar = new JButton(buscar);
        botonBuscar.addActionListener(new BuscarContactoActionListener(this,vp.getCtc()));
        botones.add(botonBuscar);
        botonCerrar = new JButton(cerrar);
        botonCerrar.setActionCommand("Cerrar");
        botonCerrar.addActionListener(this);
        botones.add(botonCerrar);
        cp.add(BorderLayout.SOUTH,botones);

        // Opciones del cuadro de diálogo
        this.setSize(390,400);
        this.setResizable(true);
        this.setLocationRelativeTo(vp);
        this.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
        this.setTitle(texto.getString("buscar_contacto_formulario_title"));
        this.setVisible(true);
    }

    /**
     * Se ejecuta cuando el usuario selecciona el botón Cerrar del cuadro del for
     * mulario. Cierra el mismo o lanza el formulario de soicitud de contacto.
     * @param e Evento que produce la ejecución del método.
     */
    @Override
    public void actionPerformed(ActionEvent e){

        // Cerrar el formulario
        if(e.getActionCommand().compareTo("Cerrar") == 0)
            this.dispose();
        // Lanzar el formulario de solicitud de contacto
        else{
            // Extraer los valores de la tabla
            int[] seleccionadas = conjuntoResultados.getSelectedRows();
            int columnaSeleccionada = seleccionadas[0];
            TableModel tm = conjuntoResultados.getModel();
            String identificador = null;
            String servidor = null;
            for(int i = 0;i < tm.getColumnCount();i++){
                String auxiliar = tm.getValueAt(columnaSeleccionada,i).toString();
                if(auxiliar.contains("@")){
                    int posicion = auxiliar.indexOf("@");
                    identificador = auxiliar.substring(0,posicion);
                    servidor = auxiliar.substring(posicion + 1);
                    break;
                }
            }
            new SolicitudDeContactoFormulario(this,vp,identificador,servidor);
        }
    }

    /**
     * Retorna los campos introducidos en el formulario por el usuario.
     * @return Los campos introducidos en el formulario.
     */
    public String[] getCampos(){

        String[] campos = new String[grupoDeCampos.length];

        for(int i = 0;i < grupoDeCampos.length;i++){
            campos[i] = grupoDeCampos[i].getText();
        }

        return campos;
    }

    /**
     * Método que se ejecuta una vez que se ha conseguido un resultado. Actualiza
     * la tabla de resultados.
     * @param resultado el resultado obetenido en la búsqueda.
     */
    public void añadirResultados(String[][] resultado){

        // Extraer los resultados de forma adecuada
        String[] titulos = new String[resultado[0].length];
        titulos = resultado[0];
        
        String[][] contenido = new String[(resultado.length) - 1][];
        for(int i = 1;i < resultado.length;i++)
            contenido[i - 1] = resultado[i];

        // Añadir los resultados a la tabla
        DefaultTableModel dtm = new DefaultTableModel(contenido,titulos){
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        conjuntoResultados.setModel(dtm);

        // Habilitar el botón de añadir contacto
        botonAñadirContacto.setEnabled(true);
    }
}

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

import org.jajim.main.Main;
import java.awt.Component;
import java.util.ResourceBundle;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.ListCellRenderer;

/**
 * @author Florencio Cañizal Calles
 * @version 1.0.1
 * Clase necesaria para poder visualizar un icono y un texto a la vez en un JCom
 * boBox.
 */
public class ComboBoxRenderer extends JLabel implements ListCellRenderer{
    
    // Variables necesarias
    private static ResourceBundle texto = ResourceBundle.getBundle("resources.Idioma",Main.loc);

    private static final String[] estadosUsuario = {
        texto.getString("en_linea_estado"),
        texto.getString("ausente_estado"),
        texto.getString("libre_para_hablar_estado"),
        texto.getString("ocupado_estado"),
        texto.getString("no_disponible_estado"),
        texto.getString("desconectado_estado")
    };

    private static final String[] iconosEstados = {
        "icons/en_linea.png",
        "icons/ausente.png",
        "icons/libre_para_hablar.PNG",
        "icons/ocupado.png",
        "icons/no_disponible.png",
        "icons/desconectado.png"
    };

    /**
     * Constructor de la clase.
     */
    public ComboBoxRenderer(){
        this.setOpaque(true);
        this.setHorizontalAlignment(LEFT);
        this.setVerticalAlignment(CENTER);
    }

    /**
     * Método que devuelve el elemento que debe ser visualizado en el JComboBox.
     * @param list La lista que estamos pintando.
     * @param value el valor retornado por list.getModel().getElementAt(index).
     * @param index El índice de la celda.
     * @param isSelected Verdadero si la celda especificada estaba seleccionada.
     * @param cellHasFocus Verdadero si la celda tenía el foco.
     * @return Un componente cuyo método paint() renderizará el valor especificado.
     */
    @Override
    public Component getListCellRendererComponent(JList list,Object value,int index,boolean isSelected,boolean cellHasFocus){

        // Recuperar el índice seleccionado
        int indiceSeleccionado = ((Integer)value).intValue();

        if(isSelected){
            setBackground(list.getSelectionBackground());
            setForeground(list.getSelectionForeground());
        }
        else{
            setBackground(list.getBackground());
            setForeground(list.getForeground());
        }

        // Poner el icono y el texto
        ImageIcon icono = new ImageIcon(ClassLoader.getSystemResource(iconosEstados[indiceSeleccionado]));
        String cadena = estadosUsuario[indiceSeleccionado];
        this.setIcon(icono);
        this.setText(cadena);

        return this;
    }

    /**
     * Retorna el número de estados del JComboBox.
     * @return El número de estados del JComboBox.
     */
    public static int getLongitud(){
        return estadosUsuario.length;
    }
}

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

import java.awt.Color;
import java.awt.Component;
import java.awt.Font;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JTree;
import javax.swing.tree.TreeCellRenderer;

/**
 * @author Florencio Cañizal Calles
 * @version 1.1
 * Clase necesaria para poder visualizar un icono y un texto a la vez en un JTree.
 */
public class TreeRenderer extends JLabel implements TreeCellRenderer{

    private final ImageIcon[] iconos = {
        new ImageIcon(ClassLoader.getSystemResource("icons/en_linea.png")),
        new ImageIcon(ClassLoader.getSystemResource("icons/ausente.png")),
        new ImageIcon(ClassLoader.getSystemResource("icons/libre_para_hablar.PNG")),
        new ImageIcon(ClassLoader.getSystemResource("icons/ocupado.png")),
        new ImageIcon(ClassLoader.getSystemResource("icons/no_disponible.png")),
        new ImageIcon(ClassLoader.getSystemResource("icons/desconectado.png"))
    };

    private Font fuenteTitulo;
    private Font fuenteNormal;
    private Color colorTitulo;
    private Color colorNormal;

    /**
     * Constructor de la clase. Inicializa las variables necesarias.
     */
    public TreeRenderer(){
        fuenteTitulo = new Font(Font.DIALOG,Font.BOLD,11);
        fuenteNormal = this.getFont();
        //colorTitulo = Color.GRAY;
        colorTitulo = new Color(8,53,54,231);
        colorNormal = this.getForeground();
    }

    /**
     * Método que devuelve el elemento que debe ser visualizado en el JTree.
     * @param tree El árbol que está siend configurado.
     * @param value El valor del objeto que está siendo dibujado.
     * @param selected Determina si el objeto está seleccionado o no.
     * @param expanded Determina si el objeto está expandido o no.
     * @param leaf Determina si el nodo representa una hoja o no.
     * @param row La actual localización si se arrastra el elemento.
     * @param hasFocus Determina si el elemento tiene el foco.
     * @return Un componente que se utiliza para renderizar el árbol.
     */
    @Override
    public Component getTreeCellRendererComponent(JTree tree, Object value, boolean selected, boolean expanded, boolean leaf, int row, boolean hasFocus) {

        // Recuperar el nodo y el texto del mismo
        String textoNodo = tree.convertValueToText(value,selected,expanded,leaf,row,hasFocus);

        if(leaf && row != -1 && row != 0){
            // Determinar el icono a poner en la JLabel
            int icono = -1;
            if(textoNodo.contains("unavailable")) {
                icono = 5;
            }
            else if(textoNodo.contains("available")) {
                icono = 0;
            }
            else if(textoNodo.contains("away")) {
                icono = 1;
            }
            else if(textoNodo.contains("chat")) {
                icono = 2;
            }
            else if(textoNodo.contains("dnd")) {
                icono = 3;
            }
            else if(textoNodo.contains("xa")) {
                icono = 4;
            }

            // Recuperar el texto que hay que poner en el JLabel
            int posicion = textoNodo.indexOf("(");
            String texto = textoNodo.substring(0,posicion);

            // Añadir el icono y el texto al JLabel y retornarlo
            this.setIcon(iconos[icono]);
            this.setFont(fuenteNormal);
            this.setForeground(colorNormal);
            this.setText(texto);
        }
        else{
            this.setFont(fuenteTitulo);
            this.setForeground(colorTitulo);
            this.setText(textoNodo);
            this.setIcon(null);
        }

        return this;
    }
}

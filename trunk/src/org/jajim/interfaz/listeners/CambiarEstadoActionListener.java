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

package org.jajim.interfaz.listeners;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JComboBox;
import org.jajim.controladores.ConexionControlador;
import org.jajim.interfaz.ventanas.VentanaPrincipal;

/**
 * @author Florencio Cañizal Calles
 * @version 1.2
 * Clase oyente que se ejecuta cuando se modifica el estado de una conexión. En
 * vía su nuevo estado al resto de contactos.
 */
public class CambiarEstadoActionListener implements ActionListener{

    // Variables importantes
    private int estadoActual = 0;
    private String[] valoresEstado ={
        "available",
        "away",
        "chat",
        "dnd",
        "xa"
    };

    /**
     * Constructor de la clase. Inicializa las variables necesarias.
     */
    public CambiarEstadoActionListener(){
    }

    /**
     * Método que se ejecuta cuando se selecciona un nuevo estado del combo de es
     * tados.
     * @param e El evento que produce la ejecución del método.
     */
    @Override
    public void actionPerformed(ActionEvent e){

        VentanaPrincipal vp = VentanaPrincipal.getInstancia();
        int a;
        ConexionControlador cnc = ConexionControlador.getInstancia();

        // Recuperar el valor seleccionado
        int seleccionado = -1;
        JComboBox estado = null;
        Object obj = e.getSource();
        if(obj instanceof JComboBox){
            estado = (JComboBox) obj;
            seleccionado = estado.getSelectedIndex();
        }
        else{
            seleccionado = Integer.parseInt(e.getActionCommand());
            estado = vp.getComboBox();
            estado.setSelectedIndex(seleccionado);
        }

        // Si estaba ya seleccionado se sale del método. Si no se actualiza la
        // variable
        if(estadoActual == seleccionado) {
            return;
        }
        else{
            a = estadoActual;
            estadoActual = seleccionado;
        }

        // Si el estado seleccionado no es desconectado se manda el nuevo estado.
        if(estadoActual < valoresEstado.length){
            cnc.cambiarEstado(valoresEstado[estadoActual]);
        }
        // Hay que desconectar al usuario de la sesión.
        else{
            // Abortar la conexión antes de borrar la cuenta
            AbortarOperaciones ao = new AbortarOperaciones(vp);
            // Si el usuario decide no abortar, dejar el combo como estaba.
            if(!ao.abortarConexion()){
                estadoActual = a;
                estado.setSelectedIndex(estadoActual);
            }
                
        }
    }

    /**
     * Actualiza el valor de la variable estadoActual
     * @param estadoActual El nuevo valor para la variable.
     */
    public void setEstadoActual(int estadoActual){
        this.estadoActual = estadoActual;
    }
}

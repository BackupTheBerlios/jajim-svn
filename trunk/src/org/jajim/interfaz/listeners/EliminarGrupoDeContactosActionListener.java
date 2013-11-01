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
import org.jajim.controladores.ContactosControlador;
import org.jajim.excepciones.ImposibleAñadirContactoAGrupoException;
import org.jajim.excepciones.ImposibleEliminarContactoDeGrupoException;
import org.jajim.excepciones.ImposibleEliminarGrupoPorDefectoException;
import org.jajim.interfaz.dialogos.MensajeError;
import org.jajim.interfaz.utilidades.PanelContactos;
import org.jajim.interfaz.ventanas.VentanaPrincipal;

/**
 * @author Florencio Cañizal Calles
 * @version 1.2
 * Clase oyente que escucha los eventos de eliminación de grupo provenientes del
 * panel de contactos.
 */
public class EliminarGrupoDeContactosActionListener implements ActionListener{

    /**
     * Constructor de la clase. Inicializa las variables necesarias.
     */
    public EliminarGrupoDeContactosActionListener(){
    }

    /**
     * Método que se ejecuta cuando el usuario selecciona la opción Eliminar grupo
     * del panel de contactos. Intenta llevar a cabo la operación en colaboración
     * con el controlador de contactos.
     * @param e El evento que produce la ejecución del método.
     */
    @Override
    public void actionPerformed(ActionEvent e) {

        // Cerrar el menú popup
        PanelContactos.getInstancia().cerrarPopupGrupos();

        // Recuperar el nombre del grupo
        String nombre = e.getActionCommand();

        if(nombre.compareTo("Unnamed") == 0 || nombre.compareTo("Sin nombre") == 0) {
            nombre = "";
        }

        // LLamar al controlador de los contactos para que realice la operación
        ContactosControlador ctc = ContactosControlador.getInstancia();
        try{
            ctc.eliminarGrupoDeContactos(nombre);
        }catch(ImposibleAñadirContactoAGrupoException iacage){
            new MensajeError(VentanaPrincipal.getInstancia(), "imposible_añadir_contacto_a_grupo",MensajeError.ERR);
        }catch(ImposibleEliminarContactoDeGrupoException iecdge){
            new MensajeError(VentanaPrincipal.getInstancia(), "imposible_eliminar_contacto_de_grupo",MensajeError.ERR);
        }catch(ImposibleEliminarGrupoPorDefectoException iegpde){
            new MensajeError(VentanaPrincipal.getInstancia(), "imposible_eliminar_grupo_por_defecto_error",MensajeError.WARNING);
        }
    }
}

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
import javax.swing.JMenuItem;
import org.jajim.controladores.ContactosControlador;
import org.jajim.excepciones.ImposibleAñadirContactoAGrupoException;
import org.jajim.excepciones.ImposibleEliminarContactoDeGrupoException;
import org.jajim.interfaz.dialogos.MensajeError;
import org.jajim.interfaz.utilidades.PanelContactos;
import org.jajim.interfaz.ventanas.VentanaPrincipal;

/**
 * @author Florencio Cañizal Calles
 * @version 1.2
 * Oyente que escucha los eventos de eliminación de un contacto de un grupo prove
 * nientes de el menú popup del panel de contactos.
 */
public class EliminarContactoDeGrupoActionListener implements ActionListener{

    private PanelContactos pc;

    /**
     * Constructor de la clase. Inicializa las variables necesarias.
     * @param pc El panel de contactos de la aplicación principal.
     */
    public EliminarContactoDeGrupoActionListener(PanelContactos pc){
        this.pc = pc;
    }

    /**
     * Método que se ejecuta cuando se selecciona la opción de eliminar contacto
     * de grupo de el menú popup del panel de contactos. Elimina el contacto del
     * grupo en colaboración con el controlador de contactos.
     * @param e
     */
    @Override
    public void actionPerformed(ActionEvent e) {

        // Cerrar el menú popup
        pc.cerrarPopupContactos();

        // Recuperar el contacto y el grupo
        String contacto = e.getActionCommand();
        JMenuItem jmi = (JMenuItem) e.getSource();
        String grupo = jmi.getName();

        // Llamar al controlador para que realice la operación
        ContactosControlador ctc = ContactosControlador.getInstancia();
        try{
            ctc.eliminarContactoDeGrupo(contacto,grupo);
        }catch(ImposibleAñadirContactoAGrupoException iacage){
            new MensajeError(VentanaPrincipal.getInstancia(), "imposible_añadir_contacto_a_grupo",MensajeError.ERR);
        }catch(ImposibleEliminarContactoDeGrupoException iecdge){
            new MensajeError(VentanaPrincipal.getInstancia(), "imposible_eliminar_contacto_de_grupo",MensajeError.ERR);
        }
    }
}

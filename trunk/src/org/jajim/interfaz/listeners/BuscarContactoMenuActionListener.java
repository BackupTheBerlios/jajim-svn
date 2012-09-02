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
import org.jajim.interfaz.dialogos.BuscarContactoFormulario;
import org.jajim.interfaz.ventanas.VentanaPrincipal;

/**
 * @author Florencio Cañizal Calles
 * @version 1.1
 * Clase que escucha los eventos de búsqueda de usuarios procedentes del menú de
 * la ventana principal.
 */
public class BuscarContactoMenuActionListener implements ActionListener{

    private VentanaPrincipal vp;

    /**
     * Constructor de la clase. Inicializa las variables adecuadas.
     * @param vp La ventana principal de la aplicación.
     */
    public BuscarContactoMenuActionListener(VentanaPrincipal vp){
        this.vp = vp;
    }

    /**
     * Se ejecuta cuando se selecciona la opción Buscar contacto del menú princi
     * pal de la aplicación. Lanza el cuadro de diálogo de búsqueda.
     * @param e
     */
    @Override
    public void actionPerformed(ActionEvent e) {
        new BuscarContactoFormulario(vp);
    }
}

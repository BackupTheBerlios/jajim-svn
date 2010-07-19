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

import org.jajim.controladores.PreferenciasControlador;
import org.jajim.interfaz.dialogos.FuenteFormulario;
import org.jajim.interfaz.ventanas.VentanaConversacion;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * @author Florencio Cañizal Calles
 * @version 1.0.1
 * Clase que escucha los eventos de modificación de fuente provenientes del for
 * mulario de las fuentes.
 */
public class FuenteActionListener implements ActionListener{

    private FuenteFormulario ff;
    private VentanaConversacion vc;
    private PreferenciasControlador pfc;

    /**
     * Constructor del clase. Inicializa las variables necesarias.
     * @param ff El formulario donde se introducen las fuentes.
     * @param vc La ventana de la conversación.
     * @param pfc El controlador de las preferencias.
     */
    public FuenteActionListener(FuenteFormulario ff,VentanaConversacion vc,PreferenciasControlador pfc){
        this.ff = ff;
        this.vc = vc;
        this.pfc = pfc;
    }

    /**
     * Método que se ejecuta cuando el usuario selecciona la opción "Aceptar" del
     * formulario de modificación de fuente. Añade la nueva fuente a las preferen
     * cias y notifica el cambio a la ventana de la conversación.
     * @param e El evento que produce la ejecución del método.
     */
    @Override
    public void actionPerformed(ActionEvent e) {

        // Recuperar los datos introducidos
        String campos[] = ff.getCampos();
        String fuente = campos[0];
        String tamaño = campos[1];

        // Actualizar las preferencias
        pfc.setFuente(fuente);
        pfc.setTamaño(Integer.parseInt(tamaño));

        // LLamar a la ventana para que vuelva a cargar las preferencias de usua
        // rio
        vc.actualizarPreferenciasMensajes();

        ff.dispose();
    }
}

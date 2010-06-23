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

import org.jajim.controladores.ContactosControlador;
import org.jajim.excepciones.ImposibleSolicitarContactoException;
import org.jajim.interfaz.dialogos.MensajeError;
import org.jajim.interfaz.dialogos.SolicitudDeContactoFormulario;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

/**
 * @author Florencio Cañizal Calles
 * @version 1.0.1
 * Clase que se activa cuando se selecciona la opción de añadir contacto. Dialoga
 * con el controlador para que lleve a cabo dicha operación.
 */
public class SolicitudDeContactoActionListener implements ActionListener{

    private SolicitudDeContactoFormulario sdcf;
    private ContactosControlador cc;

    /**
     * Constructor de la clase. Inicializa los campos.
     * @param sdcf El formulario de solicitud de contacto.
     * @param cc El controlador de las cuentas.
     */
    public SolicitudDeContactoActionListener(SolicitudDeContactoFormulario sdcf,ContactosControlador cc){
        this.sdcf = sdcf;
        this.cc = cc;
    }

    /**
     * Método que se ejecuta cuando se selecciona el botón Aceptar del cuadro de
     * diálogo de añadir contacto. Llama al controlador para que realice las ope
     * raciones adecuadas.
     * @param e
     */
    @Override
    public void actionPerformed(ActionEvent e){

        // Recupero los campos del formulario
        List<String> campos = sdcf.getCampos();
        String identificador = campos.get(0);
        String servidor = campos.get(1);
        String alias = campos.get(2);
        String grupo = campos.get(3);

        // Comprobar que el valor de los campos introducidos es correcto
        if(identificador.compareTo("") == 0 || servidor.compareTo("") == 0 || alias.compareTo("") == 0){
            new MensajeError(sdcf,"campos_invalidos_error",MensajeError.WARNING);
            return;
        }

        // Solicitar contacto al controlador
        try{
            cc.solicitarContacto(identificador,servidor,alias,grupo);
            sdcf.dispose();
        }catch(ImposibleSolicitarContactoException isce){
            new MensajeError(sdcf,"imposible_solicitar_contacto_error",MensajeError.ERR);
        }
    }
}

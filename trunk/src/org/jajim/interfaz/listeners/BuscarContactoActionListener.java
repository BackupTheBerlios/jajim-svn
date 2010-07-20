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

import org.jajim.interfaz.dialogos.BuscarContactoFormulario;
import org.jajim.controladores.ContactosControlador;
import org.jajim.excepciones.ImposibleLoginException;
import org.jajim.excepciones.ImposibleRealizarBusquedaException;
import org.jajim.excepciones.ServicioDeBusquedaNoEncontradoException;
import org.jajim.excepciones.ServidorNoEncontradoException;
import org.jajim.interfaz.dialogos.MensajeError;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * @author Florencio Cañizal Calles
 * @version 1.1
 * Clase oyente que escucha los eventos de búsqueda de contactos procedentes del
 * formulario creado para dicha tarea.
 */
public class BuscarContactoActionListener implements ActionListener{

    private BuscarContactoFormulario bcf;

    /**
     * Constructor de la clase. Inicializa las variables necesarias.
     * @param bcf El formulario de búsqueda de contactos..
     */
    public BuscarContactoActionListener(BuscarContactoFormulario bcf){
        this.bcf = bcf;
    }

    /**
     * Método que se activa cuando el usuario selecciona la opción de buscar con
     * tacto en el formulario de búsqueda. Intenta buscar el contacto.
     * @param e El evento que produce la ejecución del método.
     */
    @Override
    public void actionPerformed(ActionEvent e) {

        // Recuperar los campos del formulario
        String[] campos = bcf.getCampos();
        String cadena = campos[0];
        String servidor = campos[1];

        // Comprobar que se introducen valores correctos en el formulario.
        if(cadena.compareTo("") == 0 || servidor.compareTo("") == 0){
            new MensajeError(bcf,"campos_invalidos_error",MensajeError.WARNING);
            return;
        }

        // Llamar al controlador de contactos para que realice la operación
        String[][] resultados = null;
        try{
            ContactosControlador ctc = ContactosControlador.getInstancia();
            resultados = ctc.buscarContacto(cadena,servidor);
        }catch(ServidorNoEncontradoException snee){
            new MensajeError(bcf,"servidor_no_encontrado_error",MensajeError.ERR);
            return;
        }catch(ImposibleLoginException ile){
            new MensajeError(bcf,"no_login_error",MensajeError.ERR);
            return;
        }catch(ServicioDeBusquedaNoEncontradoException sdbnee){
            new MensajeError(bcf,"servicio_busqueda_no_encontrado",MensajeError.ERR);
            return;
        }catch(ImposibleRealizarBusquedaException irbe){
            new MensajeError(bcf,"imposible_realizar_busqueda",MensajeError.ERR);
            return;
        }

        bcf.añadirResultados(resultados);
    }
}

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

package org.jajim.modelo.conexiones;

import java.util.Observer;
import org.jivesoftware.smack.XMPPConnection;

/**
 * @author Florencio Cañizal Calles
 * @version 1.0.1
 * Clase que crea e inicializa todos los oyentes de paquetes necesarios para el
 * correcto funcionamiento de la herramienta.
 */
public class ConjuntoDeOyentes {

    private PaquetePresenciaListener ppl;

    /**
     * Constructor de la clase inicializa los oyentes de la conexión.
     * @param observador Observador que va a ser informado de los eventos adecuados
     * @param xc Conexión sobre la que se han de registrar los eventos.
     */
    public ConjuntoDeOyentes(Observer observador,XMPPConnection xc){

        // Inicializo el oyente de presencias
        this.ppl = new PaquetePresenciaListener(xc);
        ppl.addObserver(observador);
    }

    /**
     * Desvicula a los oyentes de la conexión
     */
    public void eliminarOyentesConexion(){
        ppl.desconectar();
    }
}

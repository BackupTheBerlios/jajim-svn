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

import java.util.HashMap;
import java.util.Map;
import org.jajim.excepciones.ServidorNoEncontradoException;
import org.jajim.utilidades.log.ManejadorDeLogs;
import org.jivesoftware.smack.ConnectionConfiguration;
import org.jivesoftware.smack.XMPPConnection;

/**
 * @author Florencio Cañizal Calles
 * @version 1.1
 * Clase que maneja la creación y cacheado de las diferentes conexiones utilizadas
 * por el sistema. Para sus acceso se utiliza el patrón Singleton.
 */
public class FactoriaDeConexiones {

    private static FactoriaDeConexiones instancia;
    Map<String,XMPPConnection> conexiones;

    /**
     * Constructor de la clase. Inicia la cache de conexiones.
     */
    private FactoriaDeConexiones(){
        conexiones = new HashMap<>();
    }

    /**
     * Método estático que permite recuperar una factoría de conexiones. Se usa
     * para implementar el patrón Singlenton.
     * @return Una factoría de conexiones.
     */
    public static FactoriaDeConexiones getInstancia(){

        // Si no se ha creado una instancia de la factoría se crea una nueva.
        if(instancia == null) {
            instancia = new FactoriaDeConexiones();
        }

        return instancia;
    }

    /**
     * Obsoleto hasta que Smack solucione los problemas al cerrar una conexión y
     * volver a abrirla.
     * Método que devuelve una conexión con el servidor especificado.
     * @param servidor El servidor con el que se quiere realizar la conexión.
     * @return Una conexión xmpp.
     * @throws ServidorNoEncontradoException Si no encuentra el servidor del que
     * se quiere recuperar la conexión.
     */
    @Deprecated
    public XMPPConnection getConexion(String servidor) throws ServidorNoEncontradoException{

        // Se busca en la caché una conexión con dicho servidor, si no esxiste se
        // crea una nueva.
        XMPPConnection xc;
        if(conexiones.containsKey(servidor)){
            
            xc = conexiones.get(servidor);
            // Comprobar si se está loggeado para dar otra conexión, si no se pue
            // den dar problemas
            if(xc.isAuthenticated()){
                ConnectionConfiguration config = new ConnectionConfiguration(servidor,5222);
                xc = new XMPPConnection(config);
                try{
                    xc.connect();
                }catch(Exception e){
                    // En caso de que se produzca un error se escribe en el fichero
                    // de log y se lanza una excepción
                    ManejadorDeLogs mdl = ManejadorDeLogs.getManejadorDeLogs();
                    mdl.escribir("No se puede conectar con el servidor: " + servidor);
                    throw new ServidorNoEncontradoException();
                }
                return xc;
            }

            if(!xc.isConnected()){
                try{
                    xc.connect();
                }catch(Exception e){
                    // En caso de que se produzca un error se escribe en el fichero
                    // de log y se lanza una excepción
                    ManejadorDeLogs mdl = ManejadorDeLogs.getManejadorDeLogs();
                    mdl.escribir("No se puede conectar con el servidor: " + servidor);
                    throw new ServidorNoEncontradoException();
                }
            }
        }
        else{
            ConnectionConfiguration config = new ConnectionConfiguration(servidor,5222);
            xc = new XMPPConnection(config);
            try{
                xc.connect();
            }catch(Exception e){
                // En caso de que se produzca un error se escribe en el fichero
                // de log y se lanza una excepción
                ManejadorDeLogs mdl = ManejadorDeLogs.getManejadorDeLogs();
                mdl.escribir("No se puede conectar con el servidor: " + servidor);
                throw new ServidorNoEncontradoException();
            }
            conexiones.put(servidor,xc);
        }

        return xc;
    }

    /**
     * Retorna siempre una conexión nueva. Es útil en algunos casos de uso que
     * necesitan desconectarse antes de realizar la operación y la conexión con
     * el servidor almacenada en la caché puede estar corrrupta.
     * @param servidor El servidor con el que nos queremos conectar
     * @return La conexión solicitada.
     * @throws ServidorNoEncontradoException Si no encuentra el servidor del que
     * se quiere recuperar la conexión.
     */
    public XMPPConnection getConexionNueva(String servidor) throws ServidorNoEncontradoException{

        XMPPConnection xc;
        // Devolver siempre una nueva
        ConnectionConfiguration config = new ConnectionConfiguration(servidor,5222);
        xc = new XMPPConnection(config);
        try{
            xc.connect();
        }catch(Exception e){
            // En caso de que se produzca un error se escribe en el fichero
            // de log y se lanza una excepción
            ManejadorDeLogs mdl = ManejadorDeLogs.getManejadorDeLogs();
            mdl.escribir("No se puede conectar con el servidor: " + servidor);
            throw new ServidorNoEncontradoException();
        }
        return xc;
    }
}

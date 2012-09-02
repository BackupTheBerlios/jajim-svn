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

package org.jajim.utilidades.log;

import java.io.File;
import java.io.IOException;
import java.util.logging.FileHandler;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * @author Florencio Cañizal Calles
 * @version 1.1
 * Clase que realiza las operaciones de escritura y mantenimiento del fichero de
 * log. Implementa el patrón Singleton de modo que una sóla instancia permanece
 * en el sistema a la vez.
 */
public class ManejadorDeLogs {

    private static ManejadorDeLogs instancia;
    private static final Logger logger = Logger.getLogger("com.jajim");

    /**
     * Constructor de la clase. Inicializa la clase que maneja los ficheros de log.
     */
    private ManejadorDeLogs(){

        // Instanciación un objeto Logger e inicialización del mismo.
        try{
            String ruta = System.getProperty("user.home");
            String ficheroLog = ruta + File.separator + ".JAJIM" + File.separator + "log.txt";
            FileHandler fh = new FileHandler(ficheroLog);
            logger.addHandler(fh);
            logger.setLevel(Level.ALL);
        }catch(IOException | SecurityException e){
            System.out.println("ERROR: " + e.toString());
        }
    }
    
    /**
     * Método estático que devuelve un manejador de logs.
     * @return Una instancia de ManejadorDeLogs.
     */
    public static ManejadorDeLogs getManejadorDeLogs(){

        // Si no existe una instancia del objeto crea una nueva.
        if(instancia == null) {
            instancia = new ManejadorDeLogs();
        }

        return instancia;
    }

    /**
     * Escribe el mensaje suministrado en el fichero de log.
     * @param mensaje El mensaje que se quiere escribir en el fichero.
     */
    public void escribir(String mensaje){
        logger.log(Level.SEVERE,mensaje);
    }
}

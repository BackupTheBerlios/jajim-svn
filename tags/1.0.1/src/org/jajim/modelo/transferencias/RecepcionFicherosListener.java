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

package org.jajim.modelo.transferencias;

import org.jajim.modelo.conexiones.EventosDeConexionEnumeracion;
import java.util.HashMap;
import java.util.Map;
import java.util.Observable;
import java.util.Observer;
import org.jivesoftware.smackx.filetransfer.FileTransferListener;
import org.jivesoftware.smackx.filetransfer.FileTransferRequest;

/**
 * @author Florencio Cañizal Calles
 * @version 1.0.1
 * Clase que registra las peticiones de transferencia de ficheros provenientes de
 * otros contactos.
 */
public class RecepcionFicherosListener extends Observable implements FileTransferListener{

    private int ID;
    private Map<Integer,FileTransferRequest> peticiones;

    /**
     * Constructor de la clase. Inicializa las variables necesarias.
     * @param observer El oyente de la clase.
     */
    public RecepcionFicherosListener(Observer observer){
        ID = 0;
        peticiones = new HashMap<Integer,FileTransferRequest>();
        this.addObserver(observer);
    }

    /**
     * Método que se ejecuta cuando se recibe una petición de transferencia. Re
     * gistra la petición e informa a los oyentes del evento.
     * @param request Petición de tranferencia.
     */
    @Override
    public void fileTransferRequest(FileTransferRequest request) {

        // Registrar la petición de tansferencia
        peticiones.put(new Integer(ID),request);
        ID++;

        // Informar a los oyentes del evento
        this.setChanged();
        this.notifyObservers(EventosDeConexionEnumeracion.peticionDeTransferencia);
    }

    /**
     * Deja de escuchar por las posibles transferencias de ficheros.
     */
    public void desconectar(){
        this.deleteObservers();
    }

    /**
     * Retorna la informacion importante de una determinada transferencia.
     * @param idTransferencia Identificador de la transferencia.
     * @return Información de la transferencia.
     */
    public String[] getInformacion(int idTransferencia){

        String[] informacion = new String[4];

        // Recuperar la transferencia
        FileTransferRequest ftr = peticiones.get(new Integer(idTransferencia));

        // Extraer la infromación de la transferencia
        String emisor = ftr.getRequestor();
        int posicion = emisor.lastIndexOf("/");
        informacion[0] = emisor.substring(0,posicion);
        informacion[1] = ftr.getFileName();
        informacion[2] = String.valueOf(ftr.getFileSize());
        informacion[3] = ftr.getDescription();

        return informacion;
    }

    /**
     * Retorna el último identificador de una petición recibida.
     * @return El identificador de la petición.
     */
    public int getIdentificador(){
        return (ID - 1);
    }

    /**
     * Retorna la petición de transferencia recibida cuyo identificador se corres
     * ponde con el aportado como parámetro.
     * @param idTransferencia el identificador de la transferencia.
     * @return La petición de transferencia de fichero.
     */
    public FileTransferRequest getPeticion(int idTransferencia){

        // Recupera la petición del mapa y retornarla
        FileTransferRequest ftr = peticiones.get(new Integer(idTransferencia));
        peticiones.remove(new Integer(idTransferencia));

        return ftr;
    }
}

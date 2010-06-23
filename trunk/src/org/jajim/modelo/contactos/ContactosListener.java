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

package org.jajim.modelo.contactos;

import org.jajim.modelo.conexiones.EventosDeConexionEnumeracion;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Observable;
import org.jivesoftware.smack.Roster;
import org.jivesoftware.smack.RosterEntry;
import org.jivesoftware.smack.RosterGroup;
import org.jivesoftware.smack.RosterListener;
import org.jivesoftware.smack.packet.PacketExtension;
import org.jivesoftware.smack.packet.Presence;
import org.jivesoftware.smack.util.StringUtils;

/**
 * @author Florencio Cañizal Calles
 * @version 1.0.1
 * Clase que maneja los eventos recibidos por producirse cambios en el roster del
 * usuario.
 */
public class ContactosListener extends Observable implements RosterListener{

    private Roster contactos;
    private String aliasConectado;

    /**
     * Constructor de la clase.
     */
    public ContactosListener(){}

    /**
     * Actualiza el valor de la variable contactos
     * @param contactos Los nuevos contactos del usuario.
     */
    public void setContactos(Roster contactos){
        this.contactos = contactos;
        contactos.addRosterListener(this);
        this.actualizarContactos();
    }

    /**
     * Crea una representación matricial de los contactos y los envía a los oyen
     * tes.
     */
    private void actualizarContactos(){

        String matrizContactos[][];
        int i = 0;
        int j = 0;

        Collection<RosterGroup> grupos = contactos.getGroups();

        matrizContactos = new String[grupos.size()][];
        for(RosterGroup rg: grupos){
            Collection<RosterEntry> entradas = rg.getEntries();
            matrizContactos[i] = new String[entradas.size() + 1];
            matrizContactos[i][j++] = rg.getName();
            for(RosterEntry re: entradas){
                String alias = re.getName();
                String presencia = "";
                Presence presence = contactos.getPresence(re.getUser());
                if(presence.isAvailable()){
                    if(presence.getMode() == null)
                        presencia = "available";
                    else
                        presencia = presence.getMode().toString();
                }
                else
                    presencia = "unavailable";
                matrizContactos[i][j] = alias + "(" + presencia + ")";
                j++;
            }
            i++;
            j = 0;
        }

        // Mirar a ver si están todas las entradas en la matriz, se lleva a cabo
        // porque algunos servidores no asignan un grupo por defecto para un con
        // tacto al que no se ha asignado un grupo y puede ser que este no se mu
        // estre.

        // Contar si se han asignado todas las entradas
        int asignadas = 0;
        for(int k = 0;k < matrizContactos.length;k++)
            asignadas += matrizContactos[k].length - 1;

        if(asignadas < contactos.getEntries().size()){

            List<String> sinAsignar = new ArrayList<String>();
            sinAsignar.add("");
            
            // Comprobar que entradas están en la matriz, las que no estén asig
            // narlas a la lista
            Collection<RosterEntry> rec = contactos.getEntries();
            for(RosterEntry re: rec){
                String presencia = "";
                Presence presence = contactos.getPresence(re.getUser());
                if(presence.isAvailable()){
                    if(presence.getMode() == null)
                        presencia = "available";
                    else
                        presencia = presence.getMode().toString();
                }
                else
                    presencia = "unavailable";
                String comparar = re.getName() + "(" + presencia + ")";
                boolean encontrado = false;
                for(int k = 0;k < matrizContactos.length;k++){
                    for(int l = 1;l < matrizContactos[k].length;l++){
                        if(comparar.compareTo(matrizContactos[k][l]) == 0){
                            encontrado = true;
                            break;
                        }
                    }
                }
                if(encontrado)
                    encontrado = false;
                else
                    sinAsignar.add(comparar);
            }
            // Extraer las no asignadas y crear una nueva matriz de contactos con
            // la información de la otra y las entradas no asignadas anteriormente
            // en un grupo con nombre vacio.
            String[][] nuevaMatriz = new String[matrizContactos.length + 1][];
            for(int k = 0;k < matrizContactos.length;k++)
                nuevaMatriz[k] = matrizContactos[k];
            nuevaMatriz[nuevaMatriz.length - 1] = sinAsignar.toArray(new String [0]);
            matrizContactos = nuevaMatriz;
        }

        this.setChanged();
        this.notifyObservers(matrizContactos);
    }

    /**
     * Deja de escuchar el roster
     */
    public void desconectar(){
        // Borrar al objeto como oyente del roster y borrar los objetos que escuchan
        // de él.
        this.deleteObservers();
        contactos.removeRosterListener(this);
        contactos = null;
    }

    /**
     * Se ejecuta cuando se añade una entrada al roster.
     * @param arg0 Colección de entradas añadidas.
     */
    @Override
    public void entriesAdded(Collection<String> arg0) {
        this.actualizarContactos();
    }

    /**
     * Se ejecuta cuando se actualiza la entrada de un roster.
     * @param arg0 Colección de entradas actualizadas.
     */
    @Override
    public void entriesUpdated(Collection<String> arg0) {
        this.actualizarContactos();
    }

    /**
     * Se ejecuta cuando se borra la entrada de un roster.
     * @param arg0 Colección con las entradas borradas.
     */
    @Override
    public void entriesDeleted(Collection<String> arg0) {
        this.actualizarContactos();
    }

    /**
     * Se ejecuta cuando se actualiza la presencia de un usuario.
     * @param arg0 La nueva presencia del usuario.
     */
    @Override
    public void presenceChanged(Presence arg0) {
        this.actualizarContactos();

        Collection<PacketExtension> col = arg0.getExtensions();
        if(arg0.getPriority() == Integer.MIN_VALUE && arg0.isAvailable() && col.size() == 0){
            String contacto = arg0.getFrom();
            contacto = StringUtils.parseBareAddress(contacto);
            RosterEntry re = contactos.getEntry(contacto);
            aliasConectado = re.getName();
            this.setChanged();
            this.notifyObservers(EventosDeConexionEnumeracion.usuarioConectado);
        }
    }

    /**
     * Retorna el valor del atributo aliasConectado.
     * @return El valor del atributo aliasConectado.
     */
    public String getAliasConectado(){
        return aliasConectado;
    }
}

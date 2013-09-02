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

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Observable;
import java.util.TreeMap;
import org.jajim.modelo.conexiones.EventosDeConexionEnumeracion;
import org.jivesoftware.smack.Roster;
import org.jivesoftware.smack.RosterEntry;
import org.jivesoftware.smack.RosterGroup;
import org.jivesoftware.smack.RosterListener;
import org.jivesoftware.smack.packet.PacketExtension;
import org.jivesoftware.smack.packet.Presence;
import org.jivesoftware.smack.util.StringUtils;

/**
 * @author Florencio Cañizal Calles
 * @version 1.1
 * Clase que maneja los eventos recibidos por producirse cambios en el roster del
 * usuario.
 */
public class ContactosListener extends Observable implements RosterListener{

    private Roster contactos;
    private String aliasModificado;
    private String contactoModificado;

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

        Map<String, List<String>> gruposContactos = new TreeMap<>();
        
        // Obtener todos los contactos disponibles
        // Iterar sobre ellos y extraer el grupo al que pertenece y asociarlo
        Collection<RosterEntry> rec =  contactos.getEntries();
        for(RosterEntry re : rec){
            
            // Obtener el nombre y la disponibilidad del usuario
            String nombre = re.getName();
            String presencia = "";
            Presence presence = contactos.getPresence(re.getUser());
            if(presence.isAvailable()){
                if(presence.getMode() == null) {
                    presencia = "available";
                }
                else {
                    presencia = presence.getMode().toString();
                }
            }
            else {
                presencia = "unavailable";
            }
            nombre += "(" + presencia + ")";
            
            // Recuperar los grupos a los que pertenece y asociarlo
            Collection<RosterGroup> rgc = re.getGroups();
            if(rgc.isEmpty()){
                // Si no tiene grupos asociados añadir al grupo por defecto
                if(!gruposContactos.containsKey("")){
                    gruposContactos.put("", new ArrayList<String>());
                }
                gruposContactos.get("").add(nombre);
            }
            else{
                for(RosterGroup rg : rgc){
                    String grupo = rg.getName();
                    if(!gruposContactos.containsKey(grupo)){
                        gruposContactos.put(grupo, new ArrayList<String>());
                    }
                    gruposContactos.get(grupo).add(nombre);
                }
            }
        }

        this.setChanged();
        this.notifyObservers(gruposContactos);
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
        if(arg0.getPriority() == Integer.MIN_VALUE && col.isEmpty()){
            String contacto = arg0.getFrom();
            contacto = StringUtils.parseBareAddress(contacto);
            RosterEntry re = contactos.getEntry(contacto);
            this.setChanged();
            // Si el mensaje es de contacto disponible se notifica al observador
            if(arg0.isAvailable()){
                aliasModificado = re.getName();
                this.notifyObservers(EventosDeConexionEnumeracion.usuarioConectado);
            }
            // Si el mensaje es de contacto desconectado se notifica al observador
            else{
                aliasModificado = re.getName();
                this.notifyObservers(EventosDeConexionEnumeracion.usuarioDesconectado);
            }
        }
    }

    /**
     * Retorna el valor del atributo aliasModificado.
     * @return El valor del atributo aliasModificado.
     */
    public String getAliasModificado(){
        return aliasModificado;
    }

    /**
     * Retorna el valor del atributo contactoModificado.
     * @return El valor del atributo contactoModificado.
     */
    public String getContactoModificado(){
        return contactoModificado;
    }
}

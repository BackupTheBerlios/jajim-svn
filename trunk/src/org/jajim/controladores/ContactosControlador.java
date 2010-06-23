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

package org.jajim.controladores;

import org.jajim.excepciones.ImposibleAñadirContactoAGrupoException;
import org.jajim.excepciones.ImposibleAñadirContactoException;
import org.jajim.excepciones.ImposibleEliminarContactoDeGrupoException;
import org.jajim.excepciones.ImposibleEliminarContactoException;
import org.jajim.excepciones.ImposibleEliminarGrupoPorDefectoException;
import org.jajim.excepciones.ImposibleLoginException;
import org.jajim.excepciones.ImposibleRealizarBusquedaException;
import org.jajim.excepciones.ImposibleSolicitarContactoException;
import org.jajim.excepciones.ServicioDeBusquedaNoEncontradoException;
import org.jajim.excepciones.ServidorNoEncontradoException;
import org.jajim.modelo.conexiones.FactoriaDeConexiones;
import org.jajim.modelo.contactos.ContactosListener;
import org.jajim.utilidades.log.ManejadorDeLogs;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.Observer;
import org.jivesoftware.smack.Roster;
import org.jivesoftware.smack.RosterEntry;
import org.jivesoftware.smack.RosterGroup;
import org.jivesoftware.smack.XMPPConnection;
import org.jivesoftware.smack.XMPPException;
import org.jivesoftware.smack.packet.Presence;
import org.jivesoftware.smackx.Form;
import org.jivesoftware.smackx.FormField;
import org.jivesoftware.smackx.ReportedData;
import org.jivesoftware.smackx.ReportedData.Column;
import org.jivesoftware.smackx.ReportedData.Row;
import org.jivesoftware.smackx.search.UserSearchManager;

/**
 * @author Florencio Cañizal Calles
 * @version 1.0.1
 * Clase que controla las operaciones realizadas sobre los contactos de una cuen
 * ta.
 */
public class ContactosControlador {

    private Roster contactos;
    private ContactosListener cl;

    /**
     * Constructor de la clase.
     */
    public ContactosControlador(){
        cl = new ContactosListener();
        Roster.setDefaultSubscriptionMode(Roster.SubscriptionMode.manual);
    }

    /**
     * Realiza las operaciones necesarias para realizar un contacto con el usua
     * rio especificado.
     * @param identificador El identificador del usuario.
     * @param servidor El servidor donde se encuentra registrado.
     * @param alias El alias que se le quiere dar al usuario.
     * @param grupo El grupo al que se quiere que pertenezca.
     * @throws ImposibleSolicitarContactoException Si el sistema no puede solicitar
     * el contacto con otro usuario.
     */
    public void solicitarContacto(String identificador,String servidor,String alias,String grupo) throws ImposibleSolicitarContactoException{

        // Llama al roster para que realiza la operación
        String usuario = identificador + "@" + servidor;
        String grupos[] = new String[1];
        grupos[0] = grupo;

        // Si no se ha seleccionado ningún grupo, asignarlo a grupo por defecto
        if(contactos.getGroup(grupo) == null){
            try{
                this.crearGrupoDeContactos(grupo,null);
            }catch(ImposibleAñadirContactoAGrupoException iacage){}
        }

        try{
            contactos.createEntry(usuario,alias,grupos);
        }catch(XMPPException xe){
            // En caso de que se produzca un error se escribe en el fichero
            // de log y se lanza una excepción
            ManejadorDeLogs mdl = ManejadorDeLogs.getManejadorDeLogs();
            mdl.escribir("No se puede establecer contacto con el siguiente usuario: " + usuario);
            throw new ImposibleSolicitarContactoException();
        }
    }

    /**
     * Método que hace las operaciones necesarias para aceptar un contacto que ha
     * sido solicitado por otro usuario.
     * @param cnc Controlador de la conexión.
     * @param contacto Cadena con el contacto que ha solicitado la conexión.
     * @param alias El alias que se le desea dar al contacto.
     * @param grupo El grupo al que va a pertenecer el contacto.
     * @throws ImposibleAñadirContactoException Si no se puede añadir el contacto
     * a nuestra lista de contactos.
     */
    public void aceptarContacto(ConexionControlador cnc,String contacto,String alias,String grupo) throws ImposibleAñadirContactoException{

        // Crear una entrada en el roster
        String grupos[] = new String[1];
        grupos[0] = grupo;

        // Si no se ha seleccionado ningún grupo, asignarlo a grupo por defecto
        if(contactos.getGroup(grupo) == null){
            try{
                this.crearGrupoDeContactos(grupo,null);
            }catch(ImposibleAñadirContactoAGrupoException iacage){}
        }

        try{
            contactos.createEntry(contacto,alias,grupos);
        }catch(XMPPException xe){
            // En caso de que se produzca un error se escribe en el fichero
            // de log y se lanza una excepción
            ManejadorDeLogs mdl = ManejadorDeLogs.getManejadorDeLogs();
            mdl.escribir("No se puede establecer contacto con el siguiente contacto: " + contacto);
            throw new ImposibleAñadirContactoException();
        }

        // Enviar un mensaje que informa de que se ha aceptado la petición
        Presence presencia = new Presence(Presence.Type.subscribed);
        presencia.setTo(contacto);
        cnc.enviarPaquete(presencia);
    }

    /**
     * Método que rechaza una petición de contacto recibida por el contacto pasa
     * do como parámetro.
     * @param cnc El controlador de la conexión.
     * @param contacto El contracto que solicitó la petción de contacto.
     */
    public void rechazarContacto(ConexionControlador cnc,String contacto){

        // Crear un paquete de presencia de tipo rechazo
        Presence presenciaUnsubscribed = new Presence(Presence.Type.unsubscribed);
        presenciaUnsubscribed.setTo(contacto);

        // Enviar el paquete por la conexión
        cnc.enviarPaquete(presenciaUnsubscribed);

        // Dormir un poco
        try{
            Thread.sleep(500);
        }catch(Exception e){}

        // Se elimina el contacto si este se ha añadido por error, se realiza por
        // problemas con servidores tipo openfire
        if(contactos.getEntry(contacto) != null){
            try{
                this.eliminarContacto(contacto);
            }catch(ImposibleEliminarContactoException iece){}
        }
    }

    /**
     * Elimina el contacto de la lista de contactos.
     * @param contacto El contacto que se desea eliminar.
     * @throws ImposibleEliminarContactoException Si no se puede eliminar el con
     * tacto de nuestra lista de contactos.
     */
    public void eliminarContacto(String contacto) throws ImposibleEliminarContactoException{

        // Recuperar la entrada del roster y eliminarla
        try{
            RosterEntry entry = contactos.getEntry(contacto);

            // Si no se puede encontrar la entrada es que se suministró por nombre
            // y hay que buscarla.
            if(entry == null){
                Collection<RosterEntry> cre = contactos.getEntries();
                for(RosterEntry re : cre){
                    if(contacto.compareTo(re.getName()) == 0){
                        entry = re;
                        break;
                    }
                }
            }

            contactos.removeEntry(entry);
        }catch(Exception e){
            // En caso de que se produzca un error se escribe en el fichero
            // de log y se lanza una excepción
            ManejadorDeLogs mdl = ManejadorDeLogs.getManejadorDeLogs();
            mdl.escribir("No se puede eliminar el contacto establecido con el siguiente usuario: " + contacto);
            throw new ImposibleEliminarContactoException();
        }
    }

    /**
     * Retorna aquellos usuarios del servidor, cuyo nombre se pasa como parámetro
     * , que tienen alguna coincidencia con la cadena suministrada.
     * @param cadena La cadena que se debe buscar.
     * @param servidor El servidor donde se encuentra alojado el contacto.
     * @return Matriz con los resultados obtenidos.
     * @throws ServidorNoEncontradoException Si no se puede loclizar el servidor
     * con el que se iba a realizar la conexión
     * @throws ImposibleLoginException Si no se puede hacer login con el servidor.
     * @throws ServicioDeBusquedaNoEncontradoException Si no se puede localizar el
     * servicio de búsqueda en el servidor.
     * @throws ImposibleLoginException Si no se puede llevar a cabo la búsqueda.
     */
    public String[][] buscarContacto(String cadena,String servidor) throws ServidorNoEncontradoException,ImposibleLoginException,ServicioDeBusquedaNoEncontradoException,ImposibleRealizarBusquedaException{

        String[][] resultado = null;

        // Recuperar una conexión al servidor
        XMPPConnection xc = null;
        try{
            xc = FactoriaDeConexiones.getInstancia().getConexion(servidor);
        }catch(ServidorNoEncontradoException snee){
            throw snee;
        }

        // Realizar una búsqueda de los servicios ofrecidos por el servidor
        try{
            xc.loginAnonymously();
        }catch(XMPPException e){
            // En caso de que se produzca un error se escribe en el fichero
            // de log y se lanza una excepción
            ManejadorDeLogs mdl = ManejadorDeLogs.getManejadorDeLogs();
            mdl.escribir("No se puede loggear con el servidor " + servidor + " para realizar una búsqueda");
            throw new ImposibleLoginException();
        }

        UserSearchManager userSearch = new UserSearchManager(xc);
        String servicio = null;
        try{
            Collection servicios = userSearch.getSearchServices();
            Iterator iterator = servicios.iterator();
            while(iterator.hasNext()){
                String s = (String) iterator.next();
                if(s.contains("search")){
                    servicio = s;
                    break;
                }
            }
        }catch(XMPPException e){
            ManejadorDeLogs mdl = ManejadorDeLogs.getManejadorDeLogs();
            mdl.escribir("No se puede localizar el servicio de búsqueda en el servidor: " + servidor);
            throw new ServicioDeBusquedaNoEncontradoException();
        }

        // Si no se encuentra el servicio lanzar una excepción
        if(servicio == null){
            ManejadorDeLogs mdl = ManejadorDeLogs.getManejadorDeLogs();
            mdl.escribir("No se puede localizar el servicio de búsqueda en el servidor: " + servidor);
            throw new ServicioDeBusquedaNoEncontradoException();
        }

        // Recuperar que tipo de atributo puedo solicitar y crear la consulta
        ReportedData datos = null;
        try{
            Form searchForm = userSearch.getSearchForm(servicio);
            Form answerForm = searchForm.createAnswerForm();
            answerForm.setAnswer("search",cadena);
            Iterator<FormField> iterator = searchForm.getFields();
            while(iterator.hasNext()){
                FormField ff = iterator.next();
                if(ff.getType().compareTo("boolean") == 0){
                    answerForm.setAnswer(ff.getVariable(),true);
                }
            }
            datos = userSearch.getSearchResults(answerForm,servicio);
        }catch(XMPPException e){
            ManejadorDeLogs mdl = ManejadorDeLogs.getManejadorDeLogs();
            mdl.escribir("No se puede realizar la búsqueda en el servidor: " + servidor);
            throw new ImposibleRealizarBusquedaException();
        }

        // Extraer los resultados
        Iterator<Column> columns = datos.getColumns();
        ArrayList<String> columnas = new ArrayList<String>();
        while(columns.hasNext()){
            String s = columns.next().getVariable();
            columnas.add(s);
        }

        Iterator<Row> rows = datos.getRows();
        ArrayList<String> filas = new ArrayList<String>();
        while(rows.hasNext()){
            Row row = rows.next();

            Iterator[] iteradores = new Iterator[columnas.size()];
            for(int i = 0;i < iteradores.length;i++){
                iteradores[i] = row.getValues(columnas.get(i));
            }

            while(iteradores[0].hasNext()){
                for(int i = 0;i < iteradores.length;i++){
                    filas.add((String)iteradores[i].next());
                }
            }
        }

        resultado = new String[(filas.size() / columnas.size()) + 1][columnas.size()];

        for(int i = 0;i < columnas.size();i++){
            resultado[0][i] = columnas.get(i);
        }

        int contadorFilas = 0;
        for(int i = 1;i < resultado.length;i++){
            for(int j = 0;j < resultado[0].length;j++){
                resultado[i][j] = filas.get(contadorFilas);
                contadorFilas++;
            }
        }

        return resultado;
    }

    /**
     * Crea un nuevo grupo de contactos y le añade los contactos especificados.
     * @param nombre El nombre del nuevo grupo de contactos.
     * @param contactosDeGrupo Los contactos que se van a añadir al grupo.
     * @throws ImposibleAñadirContactoAGrupoException Si no se puede añadir el con
     * tacto al grupo.
     */
    public void crearGrupoDeContactos(String nombre,String[] contactosDeGrupo) throws ImposibleAñadirContactoAGrupoException{

        // Crear grupo de contactos
        contactos.createGroup(nombre);

        // Asignar cada contacto al grupo nuevo
        if(contactosDeGrupo != null){
            for(String contacto: contactosDeGrupo){
                try{
                    añadirContactoAGrupo(contacto,nombre);
                }catch(ImposibleAñadirContactoAGrupoException iacage){
                    throw iacage;
                }
            }
        }
    }

    /**
     * Elimina un grupo de contactos de la lista de contactos. Los contactos del
     * grupo se añaden al grupo por defecto, si sólo pertenecían a este grupo.
     * @param nombre El nombre del grupo que se desea eliminar.
     * @throws ImposibleAñadirContactoAGrupoException Si no se puede añadir el con
     * tacto al grupo.
     * @throws ImposibleEliminarContactoDeGrupoException Si no se puede eliminar
     * el contacto del grupo.
     * @throws ImposibleEliminarGrupoPorDefectoException Si no se puede eliminar
     * el grupo por defecto.
     */
    public void eliminarGrupoDeContactos(String nombre) throws ImposibleAñadirContactoAGrupoException,ImposibleEliminarContactoDeGrupoException,ImposibleEliminarGrupoPorDefectoException{

        // Para borrar el grupo de contactos hay que eliminar todos los contactos
        // de que dispone
        // Recuperar el grupo y todos los contactos de los que dispone.
        RosterGroup grupo = contactos.getGroup(nombre);
        Collection<RosterEntry> conjuntoContactos = grupo.getEntries();

        // Si se quiere eliminar el grupo por defecto, comprobar que todos los ele
        // mentos del mismo están en otro grupo, si no es así cancelar la operación.
        if(nombre.compareTo("") == 0){
            for(RosterEntry re : conjuntoContactos){
                if(re.getGroups().size() == 1){
                    // Lanzar excepcion
                    throw new ImposibleEliminarGrupoPorDefectoException();
                }
            }
        }

        // Para cada entrada eliminarla del grupo y añadirla al grupo por defecto
        // si no pertenece a ningún otro grupo
        for(RosterEntry re : conjuntoContactos){

            // Recuperar el número de grupos al que pertenece
            Collection<RosterGroup> conjuntoGrupos = re.getGroups();
            int numeroDeGrupos = conjuntoGrupos.size();

            // Si sólo pertenece a un grupo añadirlo al grupo por defecto
            if(numeroDeGrupos == 1){
                RosterGroup grupoPorDefecto = contactos.getGroup("");
                // Si el grupo por defecto no existe crear uno
                if(grupoPorDefecto == null){
                    grupoPorDefecto = contactos.createGroup("");
                }
                try{
                    this.añadirContactoAGrupo(re.getUser(),"");
                }catch(ImposibleAñadirContactoAGrupoException iacage){
                    throw iacage;
                }
            }

            // Eliminar el contacto del grupo
            try{
                grupo.removeEntry(re);
            }catch(XMPPException e){
                ManejadorDeLogs mdl = ManejadorDeLogs.getManejadorDeLogs();
                mdl.escribir("No se puede eliminar el contacto: " + re.getUser() + ", del grupo: " + nombre);
                throw new ImposibleEliminarContactoDeGrupoException();
            }
        }
    }

    /**
     * Modifica el nombre de un grupo de contacto en el servidor.
     * @param grupo El nombre del grupo que se va a modificar.
     * @param nombre El nuevo nombre para el grupo.
     */
    public void modificarGrupoDeContactos(String grupo,String nombre){

        // Recuperar el grupo y cambiar su nombre
        RosterGroup rg = contactos.getGroup(grupo);
        rg.setName(nombre);
    }

    /**
     * Mueve un contacto del grupo que lo contenía al grupo especificado.
     * @param contacto Nombre del contacto que se desea mover.
     * @param grupo Nombre del grupo al que se desea mover.
     * @throws ImposibleAñadirContactoAGrupoException Si no se puede añadir el con
     * tacto al grupo.
     */
    public void añadirContactoAGrupo(String contacto,String grupo) throws ImposibleAñadirContactoAGrupoException{

        // Recuperar el contacto y el grupo al que se va a añadir
        RosterEntry re = contactos.getEntry(contacto);
        if(re == null)
            re = contactos.getEntry(this.getContactoPorAlias(contacto));
        RosterGroup grupoAAñadir = contactos.getGroup(grupo);

        try{
            grupoAAñadir.addEntry(re);
        }catch(XMPPException e){
            ManejadorDeLogs mdl = ManejadorDeLogs.getManejadorDeLogs();
            mdl.escribir("No se puede añadir el contacto: " + contacto + ", al grupo: " + grupo);
            throw new ImposibleAñadirContactoAGrupoException();
        }
    }

    /**
     * Elimina el contacto especificado del grupo pasado como parámetro.
     * @param contacto El contacto que se desea eliminar.
     * @param grupo El grupo del que se va a eliminar.
     * @throws ImposibleAñadirContactoAGrupoException Si no se puede añadir el con
     * tacto al grupo.
     * @throws ImposibleEliminarContactoDeGrupoException Si no se puede eliminar
     * el contacto del grupo.
     */
    public void eliminarContactoDeGrupo(String contacto,String grupo) throws ImposibleAñadirContactoAGrupoException,ImposibleEliminarContactoDeGrupoException{
        
        // Recuperar el contacto
        RosterEntry re = contactos.getEntry(this.getContactoPorAlias(contacto));
        
        // Si la entrada sólo pertenece a este grupo añadirla al grupo por defecto
        if(re.getGroups().size() == 1){
        
            RosterGroup porDefecto = contactos.getGroup("");
            if(porDefecto == null)
               porDefecto = contactos.createGroup("");
            
            try{
                this.añadirContactoAGrupo(contacto,"");
            }catch(ImposibleAñadirContactoAGrupoException iacage){
                throw iacage;
            }
        }

        // Recuperar el grupo y borrar el contacto
        RosterGroup rg = contactos.getGroup(grupo);
        try{
            rg.removeEntry(re);
        }catch(Exception e){
            ManejadorDeLogs mdl = ManejadorDeLogs.getManejadorDeLogs();
            mdl.escribir("No se puede eliminar el contacto: " + re.getUser() + ", del grupo: " + grupo);
            throw new ImposibleEliminarContactoDeGrupoException();
        }
    }

    /**
     * Limpiar la información necesaria en el controlador para poder hacer otra
     * conexión.
     */
    public void desconectar(){

        // Dejar de escuchar el roster
        cl.desconectar();
        contactos = null;
    }

    /**
     * Devuelve una array con el nombre de los grupos de la cuenta.
     * @return Array con el nombre de los grupos de la cuenta.
     */
    public String[] getGrupos(){

        // Recuperar la lista de grupos del roster
        Collection<RosterGroup> coleccion = contactos.getGroups();
        String[] grupos = new String[coleccion.size()];
        int i = 0;

        for(RosterGroup rg: coleccion){
            grupos[i] = rg.getName();
            i++;
        }

        return grupos;
    }

    /**
     * Retorna un array con los grupos a los que pertenece este contacto.
     * @param contacto El contacto del que se quieren recuperar los grupos a los
     * que no pertnence.
     * @return Un array con los grupos a los que no pertenece el contacto.
     */
    public String[] getGrupos(String contacto){

        // Conseguir los grupos totales y los grupos a los que pertenece el usuario
        Collection<RosterGroup> gruposTotales = contactos.getGroups();
        RosterEntry re = contactos.getEntry(this.getContactoPorAlias(contacto));
        Collection<RosterGroup> gruposDeContactos = re.getGroups();

        // Recuperar la lista de grupos que no están en las dos colecciones y re
        // tornarla
        Collection<RosterGroup> gruposT = new ArrayList<RosterGroup>();
        gruposT.addAll(gruposTotales);
        gruposT.removeAll(gruposDeContactos);
        String[] grupos = new String[gruposT.size()];
        Object[] sinContacto = gruposT.toArray();
        for(int i = 0;i < sinContacto.length;i++){
            RosterGroup rg = (RosterGroup) sinContacto[i];
            grupos[i] = rg.getName();
        }

        return grupos;
    }

    /**
     * Devuelve un array con el nombre de los contactos de la cuenta.
     * @return Array con el nombre de los contactos de la cuenta.
     */
    public String[] getContactosPorNombre(){

        // Recuperar la lista de contactos del roster.
        Collection<RosterEntry> coleccion = contactos.getEntries();
        String[] nombresContactos = new String[coleccion.size()];
        int i = 0;

        for(RosterEntry re: coleccion){
            nombresContactos[i] = re.getUser();
            i++;
        }

        return nombresContactos;
    }

    /**
     * Devuelve el "identificador@servidor" de un contacto a través del alias del
     * mismo.
     * @param alias el alias del contacto.
     * @return La cadena mencionada.
     */
    public String getContactoPorAlias(String alias){

        Collection<RosterEntry> coleccion = contactos.getEntries();

        for(RosterEntry re: coleccion){
           if(re.getName().compareTo(alias) == 0)
               return re.getUser();
        }

        return null;
    }

    /**
     * Recupera el alias de un determinado contacto.
     * @param contacto El nombre del contacto.
     * @return El alias del contacto.
     */
    public String getAliasPorContacto(String contacto){

        // Recuperar la entrada con el contactoy devolverla
        RosterEntry re = contactos.getEntry(contacto);

        return re.getName();
    }

    /**
     * Devuelve el nombre completo del usuario: identificador'@'servidor/recurso.
     * @param contacto El usuario del que se quiere recuperar el JID.
     * @return El JID del usuario.
     */
    public String getJID(String contacto){
        Presence presence = null;
        try{
            Thread.sleep(200);
 	        presence = contactos.getPresence(contacto);
        }catch(Exception e){}

        return presence.getFrom();
    }


    /**
     * Establece el oyente al que comunicarle los eventos del roster.
     * @param pc Oyente que está interesado en los eventos del roster.
     * @param oc Oyente de la conexión para recibir notificaciones de conexiones.
     */
    public void setListeners(Observer pc,Observer oc){
        cl.addObserver(pc);
        cl.addObserver(oc);
    }

    /**
     * Devuelve la lista de grupos y contactos del usuario.
     * @return La lista de grupos y contactos.
     */
    public Roster getContactos() {
        return contactos;
    }

    /**
     * Establece una nueva lista de grupos y contactos.
     * @param contactos La nueva lista de grupos y contactos.
     */
    public void setContactos(Roster contactos) {
        this.contactos = contactos;
        cl.setContactos(contactos);
    }
}

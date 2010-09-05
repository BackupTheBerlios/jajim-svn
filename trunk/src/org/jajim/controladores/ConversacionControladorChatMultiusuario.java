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

import org.jajim.excepciones.ImposibleCrearChatMultiusuarioException;
import org.jajim.excepciones.ImposibleEnviarMensajeException;
import org.jajim.excepciones.ImposibleUnirseALaSalaException;
import org.jajim.excepciones.ServicioDeChatMultiusuarioNoEncontradoException;
import org.jajim.modelo.conversaciones.MensajesChatMultiusuarioListener;
import org.jajim.modelo.conversaciones.ParticipantesListener;
import org.jajim.modelo.conversaciones.RechazoInvitacionListener;
import org.jajim.utilidades.log.ManejadorDeLogs;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Observer;
import org.jajim.excepciones.ImposibleRecuperarParticipanteException;
import org.jajim.excepciones.ImposibleVetarContactosException;
import org.jivesoftware.smack.XMPPConnection;
import org.jivesoftware.smack.XMPPException;
import org.jivesoftware.smack.packet.Message;
import org.jivesoftware.smack.util.StringUtils;
import org.jivesoftware.smackx.Form;
import org.jivesoftware.smackx.FormField;
import org.jivesoftware.smackx.ServiceDiscoveryManager;
import org.jivesoftware.smackx.muc.MultiUserChat;
import org.jivesoftware.smackx.muc.Occupant;
import org.jivesoftware.smackx.packet.DiscoverItems;

/**
 * @author Florencio Cañizal Calles
 * @version 1.1
 * Clase que gestiona todas aquellas operaciones relacionadas con las conversacio
 * nes llevadas a cabo en un chat multiusuario.
 */
public class ConversacionControladorChatMultiusuario extends ConversacionControlador{

    private MultiUserChat conversacionMultiusuario;
    private MensajesChatMultiusuarioListener mcml;
    private ParticipantesListener pl;
    private RechazoInvitacionListener ril;

    /**
     * Constructor de la clase. Inicializa las variables necesarias.
     * @param usuario El usuario que encadena la creación del chat multiusuario.
     * @param observadorDeConversacion El observador de los mensajes ocurridos 
     * durante la conversación.
     * @param observadorDeMensajes El observador de los mensajes intercambiados
     * durante la conversacion.
     */
    public ConversacionControladorChatMultiusuario(String usuario,Observer observadorDeConversacion,Observer observadorDeMensajes){

        // Inicialización de variables
        super(usuario);
        mcml = new MensajesChatMultiusuarioListener(observadorDeMensajes);
        pl = new ParticipantesListener(observadorDeConversacion);
        ril = new RechazoInvitacionListener(observadorDeConversacion);
    }

    /**
     * Método que crea un nuevo chat multiusuario.
     * @param room La sala en la que se va a desarrollar el chat.
     * @param nick El nick que va a utilizar el usuario.
     * @throws ServicioDeChatMultiusuarioNoEncontradoException Si no se puede
     * localizar el servicio de chat multiusuario en el servidor.
     * @throws ImposibleCrearChatMultiusuarioException Si no se puede crear un
     * nuevo chat multiusuario.
     */
    public void crearChatMultiusuario(String room,String nick)throws ServicioDeChatMultiusuarioNoEncontradoException,ImposibleCrearChatMultiusuarioException{

        // Obtener un ServiceDiscoveryManager asociado a la conexión.
        ConexionControlador cnc = ConexionControlador.getInstancia();
        ServiceDiscoveryManager discoManager = ServiceDiscoveryManager.getInstanceFor(cnc.getXc());

        // Descubrir los servicios asociados al servidor
        DiscoverItems discoItems = null;
        try{
            discoItems = discoManager.discoverItems(cnc.getXc().getHost());
        }catch(XMPPException e){
            ManejadorDeLogs mdl = ManejadorDeLogs.getManejadorDeLogs();
            mdl.escribir("No se puede localizar el servicio de conversación multiusuario en el servidor: " + cnc.getXc().getHost());
            throw new ServicioDeChatMultiusuarioNoEncontradoException();
        }

        // Buscar el servicio de salas multiusuario
        Iterator it = discoItems.getItems();
        String servicio = null;
        while (it.hasNext()) {
            DiscoverItems.Item item = (DiscoverItems.Item) it.next();
            String s = item.getEntityID();
            if(s.contains("conf") || s.contains("chat") || s.contains("muc")){
                servicio = s;
                break;
            }
        }

        // Si no se encuentra el servicio, se lanza una excepción
        if(servicio == null){
            ManejadorDeLogs mdl = ManejadorDeLogs.getManejadorDeLogs();
            mdl.escribir("No se puede localizar el servicio de conversación multiusuario en el servidor: " + cnc.getXc().getHost());
            throw new ServicioDeChatMultiusuarioNoEncontradoException();
        }

        // Crear un chat multiusuario instantáneo
        conversacionMultiusuario = new MultiUserChat(cnc.getXc(),room + "@" + servicio);

        try{
            conversacionMultiusuario.create(nick);
            Form form = conversacionMultiusuario.getConfigurationForm();
            Form submitForm = form.createAnswerForm();
            for (Iterator fields = form.getFields(); fields.hasNext();) {
                FormField field = (FormField) fields.next();
                if (!FormField.TYPE_HIDDEN.equals(field.getType()) && field.getVariable() != null) {
                  submitForm.setDefaultAnswer(field.getVariable());
                }
            }
            submitForm.setAnswer("muc#roomconfig_allowinvites",true);
            List<String> whoIs = new ArrayList<String>();
            whoIs.add("anyone");
            submitForm.setAnswer("muc#roomconfig_whois",whoIs);
            conversacionMultiusuario.sendConfigurationForm(submitForm);
        }catch(XMPPException e){
            ManejadorDeLogs mdl = ManejadorDeLogs.getManejadorDeLogs();
            mdl.escribir("No se puede crear una sala multiusuario");
            throw new ImposibleCrearChatMultiusuarioException();
        }

        // Registrar oyentes
        pl.setNickPropio(nick);
        conversacionMultiusuario.addParticipantListener(pl);
        conversacionMultiusuario.addInvitationRejectionListener(ril);

        // Invitar al usuario al chat
        mcml.setAlias(nick);
        conversacionMultiusuario.addMessageListener(mcml);
        conversacionMultiusuario.invite(ContactosControlador.getInstancia().getJID(usuario),"");
    }

    /**
     * Método que permite unirse a un chat multiusuario.
     * @param room La sala a la que se va a unir al usuario.
     * @param nick El nick que va a utilizar durante la conversación.
     * @throws ImposibleUnirseALaSalaException Si no se puede unir a la sala.
     */
    public void unirseChatMultiusuario(String room,String nick) throws ImposibleUnirseALaSalaException{

        // Crear un chat multiusuario
        ConexionControlador cnc = ConexionControlador.getInstancia();
        XMPPConnection xc = cnc.getXc();
        conversacionMultiusuario = new MultiUserChat(xc,room);

        // Unir al usuario a la conversación
        try{
            conversacionMultiusuario.join(nick);
        }catch(XMPPException xe){
            ManejadorDeLogs mdl = ManejadorDeLogs.getManejadorDeLogs();
            mdl.escribir("No se puede inir a la sala: " + room);
            throw new ImposibleUnirseALaSalaException();
        }

        mcml.setAlias(nick);
        conversacionMultiusuario.addMessageListener(mcml);

        // Registrar oyentes
        pl.setNickPropio(nick);
        conversacionMultiusuario.addParticipantListener(pl);
        conversacionMultiusuario.addInvitationRejectionListener(ril);
    }

    /**
     * Envia un mensaje a través del chat privado
     * @param mensaje El mensaje que se desea enviar.
     * @throws ImposibleEnviarMensajeException Si no se puede enviar el mensaje
     * al otro usuario de la conversación.
     */
    @Override
    public void enviarMensaje(String mensaje) throws ImposibleEnviarMensajeException{

        // Construir el mensaje
        Message m = new Message();
        m.setBody(mensaje);
        m.setProperty("fuente",preferencias[0]);
        m.setProperty("tamaño",preferencias[1]);
        m.setProperty("colorRojo",preferencias[2]);
        m.setProperty("colorVerde",preferencias[3]);
        m.setProperty("colorAzul",preferencias[4]);
        m.setProperty("negrita",preferencias[5]);
        m.setProperty("cursiva",preferencias[6]);

        // Utilizar el Chat para mandar el mensaje
        try{
            m.setTo(conversacionMultiusuario.getRoom());
            m.setType(Message.Type.groupchat);
            conversacionMultiusuario.sendMessage(m);
        }catch(XMPPException xe){
            // En caso de que se produzca un error se escribe en el fichero
            // de log y se lanza una excepción
            ManejadorDeLogs mdl = ManejadorDeLogs.getManejadorDeLogs();
            mdl.escribir("No se puede enviar el mensaje: " + mensaje + "- Conversación: " + conversacionMultiusuario.getRoom());
            throw new ImposibleEnviarMensajeException();
        }
    }

    /**
     * Invita a la conversación a un conjunto de contactos
     * @param contactos Array con los contactos que se van a invitar a la conver
     * sación.
     */
    public void invitarContactos(String[] contactos){

        // Inivitar uno a uno los contactos a la conversación
        for(int i = 0;i < contactos.length;i++){
            conversacionMultiusuario.invite(ContactosControlador.getInstancia().getJID(contactos[i]),"");
        }
    }

    /**
     * Vetar a los usuario seleccionados para que no puedan pariticipar en la con
     * versación.
     * @param contactos Array con los contactos que se desea vetar.
     * @throws ImposibleVetarContactosException Si la operación de vetado de con
     * tactos no se puede llevar a cabo.
     */
    public void vetarContactos(String[] contactos) throws ImposibleVetarContactosException{

        // Vetar los contactos para la conversación
        Collection<String> jids = Arrays.asList(contactos);
        try{
            conversacionMultiusuario.banUsers(jids);
        }catch(XMPPException e){
            // En caso de que se produzca un error se escribe en el fichero
            // de log y se lanza una excepción
            ManejadorDeLogs mdl = ManejadorDeLogs.getManejadorDeLogs();
            mdl.escribir("No se puede vetar a los usuarios");
            throw new ImposibleVetarContactosException();
        }
    }

    /**
     * Cierra un chat multiusuario de manera correcta en el sistema.
     */
    @Override
    public void cerrarConversacion(){
        if(conversacionMultiusuario != null)
            conversacionMultiusuario.leave();
    }

    /**
     * Retorna la lista de participantes del chat.
     * @return La lista de participantes del chat.
     */
    @Override
    public String[] getParticipantes(){

        String[] participantes = null;

        // Llama a la conversación para recuperar los participantes
        Iterator<String> listaDeOcupantes = conversacionMultiusuario.getOccupants();
        participantes = new String[conversacionMultiusuario.getOccupantsCount() - 1];
        int i = 0;
        while(listaDeOcupantes.hasNext()){
            String nick = StringUtils.parseResource(listaDeOcupantes.next());
            if(nick.compareTo(conversacionMultiusuario.getNickname()) != 0){
                participantes[i] = nick;
                i++;
            }
        }

        return participantes;
    }

    /**
     * Retorna el JID de los participantes de un chat multiusuario.
     */
    public String[] getParticipantesComoJID(){

        String[] participantes = new String[conversacionMultiusuario.getOccupantsCount()];

        Iterator<String> iterator = conversacionMultiusuario.getOccupants();
        int i = 0;
        while(iterator.hasNext()){
            Occupant o = conversacionMultiusuario.getOccupant(iterator.next());
            participantes[i] = StringUtils.parseBareAddress(o.getJid());
            i++;
        }

        return participantes;
    }

    /**
     * Método que retorna los participantes de una conversación con su nombre com
     * pleto. Sólo válido para conversaciones multiusuario.
     * @return El nombre completo de los participantes.
     */
    public String[] getParticipantesComoRecurso(){

        String[] participantes = null;

        // Conseguir los ocupantes y extraer su información.
        Iterator<String> listaDeOcupantes = conversacionMultiusuario.getOccupants();
        participantes = new String[conversacionMultiusuario.getOccupantsCount()];
        int i = 0;
        while(listaDeOcupantes.hasNext()){
            String nick = listaDeOcupantes.next();
            participantes[i] = nick;
            i++;
        }

        return participantes;
    }

    /**
     * Devuelve el jid del participante cuyo nick se corresponde con el suministra
     * do.
     * @param nick El nick del participante del que se quiere recuperar el nick.
     * @return Retorna una cadena con el JID del usuario.
     * @throws ImposibleRecuperarParticipanteException Si no se pued recuperar el
     * JID del participante.
     */
    public String getJIDParticipante(String nick) throws ImposibleRecuperarParticipanteException{

        String JID = null;

        // Recuperar el JID del participante para devolverlo.
        Occupant o = conversacionMultiusuario.getOccupant(conversacionMultiusuario.getRoom() + "/" + nick);
        JID = o.getJid();

        // Si no se ha podido recuperar lanzar una excepción.
        if(JID == null){
            ManejadorDeLogs mdl = ManejadorDeLogs.getManejadorDeLogs();
            mdl.escribir("No se puede recuperar el JID de el usuario: " + nick);
            throw new ImposibleRecuperarParticipanteException();
        }

        return JID;
    }

    /**
     * Método estático que se ejecuta para rechazar una invitación a un chat mul
     * tiusuario generada por un contacto.
     * @param cnc El controlador de la conexión actual.
     * @param contacto El contacto que nos envió la invitación.
     * @param room La sala en la que tiene lugar la conversación.
     */
    public static void rechazarInvitacion(String contacto,String room){

        // Recuperar la conexión y rechazar la invitación.
        ConexionControlador cnc = ConexionControlador.getInstancia();
        XMPPConnection xc = cnc.getXc();
        MultiUserChat.decline(xc,room,contacto,"");
    }
}

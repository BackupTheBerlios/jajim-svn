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

import org.jajim.excepciones.FicheroNoEncontradoException;
import org.jajim.excepciones.ImposibleCrearChatMultiusuarioException;
import org.jajim.excepciones.ImposibleEnviarMensajeException;
import org.jajim.excepciones.ImposibleGuardarConversacionException;
import org.jajim.excepciones.ImposibleRecuperarParticipanteException;
import org.jajim.excepciones.ImposibleUnirseALaSalaException;
import org.jajim.excepciones.ImposibleVetarContactosException;
import org.jajim.excepciones.ServicioDeChatMultiusuarioNoEncontradoException;
import org.jajim.modelo.conversaciones.ChatListener;
import org.jajim.modelo.conversaciones.InvitacionListener;
import org.jajim.modelo.conversaciones.MensajesChatMultiusuarioListener;
import org.jajim.modelo.conversaciones.MensajesChatPrivadoListener;
import org.jajim.modelo.conversaciones.ParticipantesListener;
import org.jajim.modelo.conversaciones.RechazoInvitacionListener;
import org.jajim.modelo.conversaciones.TiposDeChatEnumeracion;
import org.jajim.utilidades.log.ManejadorDeLogs;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Observer;
import org.jivesoftware.smack.Chat;
import org.jivesoftware.smack.ChatManager;
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
 * @version 1.0.1
 * Clase que gestiona todas aquellas operaciones relacinadas con las conversacio
 * nes.
 */
public class ConversacionControlador {

    private TiposDeChatEnumeracion tipo;
    private String usuario;
    private Chat conversacionPrivada;
    private MultiUserChat conversacionMultiusuario;
    private MensajesChatPrivadoListener mcpl;
    private MensajesChatMultiusuarioListener mcml;
    private ParticipantesListener pl;
    private RechazoInvitacionListener ril;
    private String[] preferencias;
    private static ChatListener cl;
    private static InvitacionListener il;
    private static ChatManager cm;

    /**
     * Constructor de la clase. Inicia la conversación con el usuario.
     * @param observadorDeConversacion Es notificado de los eventos producidos en
     * la vonversación.
     * @param usuario el usuario con el que se va a iniciar la conversación.
     * @param observadorDeMensajes El observador de la interfaz de los mensajes
     * que llegan.
     */
    public ConversacionControlador(Observer observadorDeConversacion,String usuario,Observer observadorDeMensajes){
        this.usuario = usuario;
        mcpl = new MensajesChatPrivadoListener(observadorDeMensajes);
        mcml = new MensajesChatMultiusuarioListener(observadorDeMensajes);
        pl = new ParticipantesListener(observadorDeConversacion);
        ril = new RechazoInvitacionListener(observadorDeConversacion);
    }

    /**
     * Inicia la conversación.
     * @param tipo El tipo de conversación que se va a iniciar.
     */
    public void iniciarConversacion(TiposDeChatEnumeracion tipo){
        // Crear la conversación privada
        this.tipo = tipo;
        if(cm == null)
            System.out.println("No se dispone de un chat manager");
        conversacionPrivada = cm.createChat(usuario,mcpl);
    }

    /**
     * Inicia una conversación multichat.
     * @param ctc el controlador de contactos.
     * @param room El nombre de la sala.
     * @param nickname El nick que se va a utilizar en la conversación.
     * @param tipo El tipo de conversación.
     * @throws ServicioDeChatMultiusuarioNoEncontradoException Si no se puede en
     * contrar el servicio de chats multiusuario.
     * @throws ImposibleCrearChatMultiusuarioException Si no se puede crear el chat.
     */
    public void iniciarConversacion(ContactosControlador ctc,String room,String nickname,TiposDeChatEnumeracion tipo) throws ServicioDeChatMultiusuarioNoEncontradoException,ImposibleCrearChatMultiusuarioException{

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
        this.tipo = tipo;
        conversacionMultiusuario = new MultiUserChat(cnc.getXc(),room + "@" + servicio);
        
        try{
            conversacionMultiusuario.create(nickname);
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
        pl.setNickPropio(nickname);
        conversacionMultiusuario.addParticipantListener(pl);
        conversacionMultiusuario.addInvitationRejectionListener(ril);

        // Invitar al usuario al chat
        mcml.setAlias(nickname);
        conversacionMultiusuario.addMessageListener(mcml);
        conversacionMultiusuario.invite(ctc.getJID(usuario),"");
    }

    /**
     * Recupera una solicitud de chat privado.
     * @param idChat El identificador de la solicitud.
     * @param tipo El tipo de chat que se va a mantener.
     */
    public void iniciarConversacion(String idChat,TiposDeChatEnumeracion tipo){

        // Asignar el tipo de chat y recuperar el recibido
        this.tipo = tipo;
        conversacionPrivada = cl.getChat(idChat);
        conversacionPrivada.addMessageListener(mcpl);
    }

    /**
     * Acepta el chat multiusuario y prepara todo lo necesario para que el usuario
     * puede conversar.
     * @param idInvitacion El identificador de la invitación recibida.
     * @param room La sala en la que tiene lugar la conversación.
     * @param nickname El nick que va a utilizar el usuairo.
     * @param tipo El tipo de chat que se va a mantener.
     * @throws ImposibleUnirseALaSalaException Si no se puede añadir a la sala del
     * chat multiusuario.
     */
    public void iniciarConversacion(String idInvitacion,String room,String nickname,TiposDeChatEnumeracion tipo) throws ImposibleUnirseALaSalaException{

        this.tipo = tipo;

        // Crear un chat multiusuario
        ConexionControlador cnc = ConexionControlador.getInstancia();
        XMPPConnection xc = cnc.getXc();
        conversacionMultiusuario = new MultiUserChat(xc,room);

        // Unir al usuario a la conversación
        try{
            conversacionMultiusuario.join(nickname);
        }catch(XMPPException xe){
            ManejadorDeLogs mdl = ManejadorDeLogs.getManejadorDeLogs();
            mdl.escribir("No se puede inir a la sala: " + room);
            throw new ImposibleUnirseALaSalaException();
        }

        mcml.setAlias(nickname);
        conversacionMultiusuario.addMessageListener(mcml);

        // Registrar oyentes
        pl.setNickPropio(nickname);
        conversacionMultiusuario.addParticipantListener(pl);
        conversacionMultiusuario.addInvitationRejectionListener(ril);
    }

    /**
     * Método que envía el mensaje suministrado a todos los miembros de la conver
     * sación.
     * @param mensaje El mensaje que se desea enviar.
     * @throws ImposibleEnviarMensajeException Si no se puede enviar el mensaje
     * a los participantes en la conversación.
     */
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
            if(tipo == TiposDeChatEnumeracion.chatPrivado)
                conversacionPrivada.sendMessage(m);
            else{
                m.setTo(conversacionMultiusuario.getRoom());
                m.setType(Message.Type.groupchat);
                conversacionMultiusuario.sendMessage(m);
            }
        }catch(XMPPException xe){
            // En caso de que se produzca un error se escribe en el fichero
            // de log y se lanza una excepción
            ManejadorDeLogs mdl = ManejadorDeLogs.getManejadorDeLogs();
            mdl.escribir("No se puede enviar el mensaje: " + mensaje + "- Conversación: " + conversacionPrivada.getParticipant());
            throw new ImposibleEnviarMensajeException();
        }
    }

    /**
     * Invita a la conversación a un conjunto de contactos
     * @param contactos Array con los contactos que se van a invitar a la conver
     * sación.
     */
    public void invitarContactos(ContactosControlador ctc,String[] contactos){

        // Inivitar uno a uno los contactos a la conversación
        for(int i = 0;i < contactos.length;i++){
            conversacionMultiusuario.invite(ctc.getJID(contactos[i]),"");
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
     * Guarda el contenido de la conversacion actual.
     * @param nombre El nombre del fichero en el que se va a guardar.
     * @param ruta La ruta de localización del fichero.
     * @param texto El contenido de la conversación.
     * @throws FicheroNoEncontradoException Si no se puede crear el fichero en el
     * que se va a guardar la conversación
     * @throws ImposibleGuardarConversacionException Si no se puede guardar la con
     * versación en el fichero.
     */
    public void guardarConversacion(String nombre,String ruta,String texto) throws FicheroNoEncontradoException,ImposibleGuardarConversacionException{

        // Crear un archivo file
        String f = ruta + File.separator + nombre + ".html";
        File fichero = new File(f);

        // Escribir en el fichero
        try{
            FileOutputStream fos = new FileOutputStream(fichero);
            fos.write(texto.getBytes());
            fos.close();
        }catch(FileNotFoundException fnfe){
            // En caso de que se produzca un error se escribe en el fichero
            // de log y se lanza una excepción
            ManejadorDeLogs mdl = ManejadorDeLogs.getManejadorDeLogs();
            mdl.escribir("No se encuentra el fichero: " + f);
            throw new FicheroNoEncontradoException();
        }catch(IOException ioe){
            // En caso de que se produzca un error se escribe en el fichero
            // de log y se lanza una excepción
            ManejadorDeLogs mdl = ManejadorDeLogs.getManejadorDeLogs();
            mdl.escribir("No puede escribir en el fichero: " + f);
            throw new ImposibleGuardarConversacionException();
        }
    }

    /**
     * Método que retorna los identificadores de las personas involoucradas en
     * la actual conversación. Si el chat el privado devuelve el nombre completo
     * del usuario, si es multiusuario el nick de los participantes.
     * @return Un array con los participantes de la conversación.
     */
    public String[] getParticipantes(){

        String[] participantes = null;

        // Si es un chat privado se extrae su único ocupante
        if(tipo == TiposDeChatEnumeracion.chatPrivado){
            String participante = conversacionPrivada.getParticipant();
            participantes = new String[1];
            participantes[0] = participante;
        }
        // Si es un chat multiusuario se extraen todos sus ocupantes
        else{
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
        if(tipo == TiposDeChatEnumeracion.chatMultiUsuario){
            Iterator<String> listaDeOcupantes = conversacionMultiusuario.getOccupants();
            participantes = new String[conversacionMultiusuario.getOccupantsCount()];
            int i = 0;
            while(listaDeOcupantes.hasNext()){
                String nick = listaDeOcupantes.next();
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
        if(tipo == TiposDeChatEnumeracion.chatMultiUsuario){
            Occupant o = conversacionMultiusuario.getOccupant(conversacionMultiusuario.getRoom() + "/" + nick);
            JID = o.getJid();
        }

        // Si no se ha podido recuperar lanzar una excepción.
        if(JID == null){
            ManejadorDeLogs mdl = ManejadorDeLogs.getManejadorDeLogs();
            mdl.escribir("No se puede recuperar el JID de el usuario: " + nick);
            throw new ImposibleRecuperarParticipanteException();
        }

        return JID;
    }

    /**
     * Cierra una conversación de manera correcta en el sistema.
     */
    public void cerrarConversacion(){

        if(tipo == TiposDeChatEnumeracion.chatPrivado){
            try{
                this.enviarMensaje("Exit");
                cl.eliminarChat(conversacionPrivada);
            }catch(ImposibleEnviarMensajeException ieme){}
        }
        else
            conversacionMultiusuario.leave();
    }

    /**
     * Método que actualiza las preferencias en el controlador para que éste pueda
     * enviarlas en el mensaje correspondiente.
     * @param preferencias Las nuevas preferencias de mensajes.
     */
    public void actualizarPreferencias(String[] preferencias){
        this.preferencias = preferencias;
    }

    /**
     * Retorna el usuario con el que se inició la conversación.
     * @return El usuario con el que se inició la conversación.
     */
    public String getUsuario(){
        return usuario;
    }

    /**
     * Devuelve el tipo de conversación que se está manteniendo.
     * @return El tipo de conversación que se está manteniendo.
     */
    public TiposDeChatEnumeracion getTipo(){
        return tipo;
    }

    /**
     * Asocia un oyente de chat a la conexión
     * @param xc La conexión activa.
     * @param o El oyente que será notificado de los eventos de chat.
     */
    public static void crearListener(XMPPConnection xc,Observer o){

        // Crear el chat listener y asociarlo a la conexión
        cl = new ChatListener(o);
        cm = xc.getChatManager();
        cm.addChatListener(cl);

        // Crear el listener de las invitaciones y asignarlo
        il = new InvitacionListener(o);
        MultiUserChat.addInvitationListener(xc,il);
    }

    /**
     * Elimina el oyente de chat de la conexión.
     * @param xc La conexión activa actualmente.
     */
    public static void eliminarListener(XMPPConnection xc){

        // Eliminar el chat listener y resetearlo.
        cm.removeChatListener(cl);
        cm = null;
        cl = null;
        MultiUserChat.removeInvitationListener(xc,il);
        il = null;
    }

    /**
     * Rechaza una de las peticiones de chat privado.
     * @param idChat El identificador del chat privado.
     */
    public static void rechazarChatPrivado(String idChat){
    
        // Recuperar el chat
        Chat chat = cl.getChat(idChat);

        // Enviar un mensaje rechazando la conversación.
        try{
            chat.sendMessage("Exit");
        }catch(XMPPException xe){}

        // Eliminar el chat
        cl.eliminarChat(chat);
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

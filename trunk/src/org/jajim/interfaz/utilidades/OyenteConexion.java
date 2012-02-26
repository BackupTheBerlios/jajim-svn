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

package org.jajim.interfaz.utilidades;

import org.jajim.interfaz.dialogos.AceptarORechazarChatPrivadoFormulario;
import org.jajim.interfaz.dialogos.AceptarORechazarContactoFormulario;
import org.jajim.interfaz.dialogos.AceptarORechazarFicheroFormulario;
import org.jajim.interfaz.dialogos.AceptarORechazarInvitacionFormulario;
import org.jajim.interfaz.dialogos.PeticionRechazadaFormulario;
import org.jajim.interfaz.ventanas.VentanaPrincipal;
import org.jajim.modelo.conexiones.EventosDeConexionEnumeracion;
import org.jajim.modelo.conexiones.PaquetePresenciaListener;
import org.jajim.modelo.contactos.ContactosListener;
import org.jajim.modelo.conversaciones.ChatListener;
import org.jajim.modelo.conversaciones.InvitacionListener;
import org.jajim.modelo.transferencias.RecepcionFicherosListener;
import java.util.Observable;
import java.util.Observer;
import javax.swing.SwingUtilities;
import org.jajim.controladores.ContactosControlador;
import org.jajim.controladores.ConversacionControladorChatMultiusuario;
import org.jajim.controladores.ConversacionControladorChatPrivado;
import org.jajim.controladores.TransferenciaFicherosControlador;
import org.jajim.interfaz.listeners.AbortarOperaciones;
import org.jajim.interfaz.ventanas.VentanaConversacion;
import org.jajim.interfaz.ventanas.VentanaConversacionChatPrivado;

/**
 * @author Florencio Cañizal Calles
 * @version 1.1
 * Clase que recibe los eventos que importantes que se reciben a través de la co
 * nexión.
 */
public class OyenteConexion implements Observer{

    private VentanaPrincipal vp;
    private String contacto;
    private int idTransferencia;
    private String[] informacion;

    /**
     * Constructor de la clase. Inicializa las variables necesarias.
     * @param vp La ventana principal de la aplicación.
     */
    public OyenteConexion(VentanaPrincipal vp){
        this.vp = vp;
    }

    /**
     * Se ejecuta cuando se produce un evento que interese al usuario.
     * @param o La clase que generó el evento.
     * @param arg El tipo de evento que se ha generado.
     */
    @Override
    public void update(Observable o, Object arg) {

        if(arg instanceof EventosDeConexionEnumeracion){

            final EventosDeConexionEnumeracion edce = (EventosDeConexionEnumeracion) arg;

            // Comprobar el tipo de evento recibido y actuar en consecuencia
            if(edce == EventosDeConexionEnumeracion.peticionDeSuscripcion){
                PaquetePresenciaListener ppl = (PaquetePresenciaListener) o;
                contacto = ppl.getOrigen();
                final VentanaPopup vpp = new VentanaPopupConexion(edce,contacto,vp);
                SwingUtilities.invokeLater(new Runnable(){
                    public void run(){
                        // Mostrar ventana popup
                        vpp.mostrarVentana();
                    }
                });
                if(vpp.pollSeleccionado()){
                    SwingUtilities.invokeLater(new Runnable(){
                        public void run() {
                            new AceptarORechazarContactoFormulario(vp,contacto);
                        }
                    });
                }
            }
            else if(edce == EventosDeConexionEnumeracion.confirmacionDeSuscripcion){
                PaquetePresenciaListener ppl = (PaquetePresenciaListener) o;
                contacto = ppl.getOrigen();
                final VentanaPopup vpp = new VentanaPopupConexion(edce,contacto,vp);
                SwingUtilities.invokeLater(new Runnable() {
                    public void run() {
                        vpp.mostrarVentana();
                    }
                });
            }
            else if(edce == EventosDeConexionEnumeracion.denegacionDeSuscripcion){
                // Recuperar el origen del mensaje y notificar que se va a borrar el
                // contacto del grupo
                PaquetePresenciaListener ppl = (PaquetePresenciaListener) o;
                contacto = ppl.getOrigen();
                final VentanaPopup vpp = new VentanaPopupConexion(edce,contacto,vp);
                SwingUtilities.invokeLater(new Runnable() {
                    public void run() {
                        vpp.mostrarVentana();
                    }
                });
                if(vpp.pollSeleccionado()){
                    SwingUtilities.invokeLater(new Runnable(){
                        public void run() {
                            new PeticionRechazadaFormulario(vp,contacto);
                        }
                    });
                }
                else{
                    try{
                        ContactosControlador.getInstancia().eliminarContacto(contacto);
                    }catch(Exception e){}
                }
            }
            else if(edce == EventosDeConexionEnumeracion.peticionDeChat){
                // Recuperar el contacto que solicita la petición de chat y el propio
                // Chat, notificar al usuario del suceso.
                ChatListener cl = (ChatListener) o;
                final String idChat = cl.getIdChat();
                contacto = cl.getContacto(idChat);
                final VentanaPopup vpp = new VentanaPopupConexion(edce,contacto,vp);
                SwingUtilities.invokeLater(new Runnable(){
                    public void run(){
                        vpp.mostrarVentana();
                    }
                });
                if(vpp.pollSeleccionado()){
                    SwingUtilities.invokeLater(new Runnable(){
                        public void run(){
                            new AceptarORechazarChatPrivadoFormulario(vp,idChat,contacto);
                        }
                    });
                }
                else{
                    try{
                        ConversacionControladorChatPrivado.rechazarChatPrivado(idChat);
                    }catch(Exception e){}
                }
            }
            else if(edce == EventosDeConexionEnumeracion.invitacionAChat){
                InvitacionListener il = (InvitacionListener) o;
                final String idInvitacion = il.getIdInvitacion();
                informacion = il.getInformacion(idInvitacion);
                final String room = informacion[0];
                contacto = informacion[1];
                final VentanaPopup vpp = new VentanaPopupConexion(edce,contacto + "&" + room,vp);
                SwingUtilities.invokeLater(new Runnable(){
                    public void run(){
                        vpp.mostrarVentana();

                    }
                });
                if(vpp.pollSeleccionado()){
                    SwingUtilities.invokeLater(new Runnable(){
                        public void run(){
                            new AceptarORechazarInvitacionFormulario(vp,idInvitacion,room,contacto);
                        }
                    });
                }
                else{
                    try{
                        ConversacionControladorChatMultiusuario.rechazarInvitacion(contacto,room);
                    }catch(Exception e){}
                }
            }
            else if(edce == EventosDeConexionEnumeracion.peticionDeTransferencia){
                RecepcionFicherosListener rfl = (RecepcionFicherosListener) o;
                idTransferencia = rfl.getIdentificador();
                informacion = rfl.getInformacion(idTransferencia);
                final VentanaPopup vpp = new VentanaPopupConexion(edce,informacion[0],vp);
                SwingUtilities.invokeLater(new Runnable(){
                    public void run(){
                        vpp.mostrarVentana();
                    }
                });
                if(vpp.pollSeleccionado()){
                    SwingUtilities.invokeLater(new Runnable(){
                        public void run(){
                            new AceptarORechazarFicheroFormulario(vp,idTransferencia,informacion);
                        }
                    });
                }
                else{
                    try{
                        TransferenciaFicherosControlador.getInstancia().rechazarFichero(idTransferencia);
                    }catch(Exception e){}
                }
            }
            // Si lo que se recibe es un evento de que un usuario se ha conectado
            // se notifica al usuario, y en el caso de que hubiera una conversación
            // pendiente con el mismo se muestra en ésta.
            else if(edce == EventosDeConexionEnumeracion.usuarioConectado){
                ContactosListener cl = (ContactosListener) o;
                final String alias = cl.getAliasModificado();
                final VentanaPopup vpp = new VentanaPopupConexion(edce,alias,vp);
                SwingUtilities.invokeLater(new Runnable(){
                    public void run(){
                        // Notificar al usuario
                        vpp.mostrarVentana();

                        // Se notifica la conexión si se tenía un chat con el usuario
                        if(VentanaConversacion.hayChatPrivado(alias)){
                            VentanaConversacionChatPrivado vccp = VentanaConversacion.getChatPrivado(alias);
                            vccp.notificarEvento(edce, alias);
                        }
                    }
                });
            }
            // Si lo que se recibe es un evento de que un usuario se ha desconectado 
            // se notifica en las conversaciones y las transferencias que se mantienen
            // con el mismo
            else if(edce == EventosDeConexionEnumeracion.usuarioDesconectado){
                ContactosListener cl = (ContactosListener) o;
                final String alias = cl.getAliasModificado();
                final VentanaPrincipal vp = this.vp;
                SwingUtilities.invokeLater(new Runnable(){
                    @Override
                    public void run(){

                        // Se notifica la desconexión si se tenía algún chat con
                        // el usuario
                        if(VentanaConversacion.hayChatPrivado(alias)){
                            VentanaConversacionChatPrivado vccp = VentanaConversacion.getChatPrivado(alias);
                            vccp.notificarEvento(edce, alias);
                        }

                        // Se abortan las transferencias
                        AbortarOperaciones ao = new AbortarOperaciones(vp, vp, vp.getVgt());
                        ao.abortarTransferencias(contacto,false);
                    }
                });
            }
        }
    }
}

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

package org.jajim.interfaz.ventanas;

import org.jajim.controladores.ContactosControlador;
import org.jajim.controladores.ConversacionControladorChatMultiusuario;
import org.jajim.excepciones.ImposibleCrearChatMultiusuarioException;
import org.jajim.excepciones.ImposibleUnirseALaSalaException;
import org.jajim.excepciones.ServicioDeChatMultiusuarioNoEncontradoException;
import org.jajim.interfaz.dialogos.MensajeError;
import org.jivesoftware.smack.util.StringUtils;

/**
 * @author Florencio Cañizal Calles
 * @version 1.1
 * Clase que representa una ventana de un chat multiusuario. Inicializa la interfaz
 * necesaria para que el usuario dialogue con un contacto.
 */
public class VentanaConversacionChatMultiusuario extends VentanaConversacion{

    private final boolean activadosMultiusuario[][] = {
        {true,true,true,true}
    };

    /**
     * Constructor de la clase. Inicializa las variables necesarias.
     * @param vp La ventana principal de la aplicación.
     * @param alias El alias del usuario con el que se desea crea un chat multiusuario.
     * @param room La sala en la que va a tener lugar la charla.
     * @param nick El nick que va a utilizar nuestro usuario.
     */
    public VentanaConversacionChatMultiusuario(VentanaPrincipal vp,String alias,String room,String nick){

        // LLamar al constructor del padre
        super(vp,alias);
        
        String usuario = ContactosControlador.getInstancia().getContactoPorAlias(alias);
        cvc = new ConversacionControladorChatMultiusuario(usuario,this,conversacion);

        // Crear un chat multiusuario.
        try{
            if(cvc instanceof ConversacionControladorChatMultiusuario){
                ConversacionControladorChatMultiusuario cccm = (ConversacionControladorChatMultiusuario) cvc;
                cccm.crearChatMultiusuario(room,nick);
            }

        }catch(ServicioDeChatMultiusuarioNoEncontradoException sdcmne){
            new MensajeError(this,"servicio_chat_multiusuairo_no_encontrado",MensajeError.ERR);
            this.dispose();
            VentanaConversacion.eliminarConversacion(this);
            return;
        }catch(ImposibleCrearChatMultiusuarioException iccme){
            new MensajeError(this,"imposible_crear_chat_multiusuario_error",MensajeError.ERR);
            this.dispose();
            VentanaConversacion.eliminarConversacion(this);
            return;
        }

        etiquetaPrincipal.setText(principal + " - ");

        // Asignar nombres
        conversacion.añadirUsuario("Usuario",nick);
        usuarioActual = nick;

        // Habilitar o deshabilitar los botones
        for(int i = 0;i < itemsDeMenu.length;i++){
            for(int j = 0;j < itemsDeMenu[i].length;j++){
                itemsDeMenu[i][j].setEnabled(activadosMultiusuario[i][j]);
                botonesBarraDeHerramientas[j].setEnabled(activadosMultiusuario[0][j]);
            }
        }

        // Actualizar preferencias
        this.actualizarPreferenciasMensajes();

        // Establecer el foco en el mensaje de texto
        nuevoMensaje.requestFocusInWindow();
    }

    /**
     * Constructor de la clase inicializa las variables necesarias.
     * @param vp La ventana principal de la aplicación
     * @param alias El alias del contacto que nos invita
     * @param room La sala en la que tiene lugar la charla
     * @param nick El nick que vamos a utilizar durante el desarrollo de la misma
     * @param idInvitacion El identificador de la invitación recibida
     */
    public VentanaConversacionChatMultiusuario(VentanaPrincipal vp,String alias,String room,String nick,String idInvitacion){

        // LLamar al constructor del padre
        super(vp,alias);
        String usuario = ContactosControlador.getInstancia().getContactoPorAlias(alias);
        cvc = new ConversacionControladorChatMultiusuario(usuario,this,conversacion);

        // Iniciar la conversación
        try{
            if(cvc instanceof ConversacionControladorChatMultiusuario){
                ConversacionControladorChatMultiusuario cccm = (ConversacionControladorChatMultiusuario) cvc;
                cccm.unirseChatMultiusuario(room, nick);
            }
        }catch(ImposibleUnirseALaSalaException iualse){
            new MensajeError(this,"imposible_unirse_a_la_sala_error",MensajeError.ERR);
            this.dispose();
            VentanaConversacion.eliminarConversacion(this);
            return;
        }

        // Asignar nombres
        etiquetaPrincipal.setText(principal + " - ");
        boolean primero = true;
        conversacion.añadirUsuario("Usuario",nick);
        usuarioActual = nick;
        String[] participantes = null;
        
        if(cvc instanceof ConversacionControladorChatMultiusuario){
            ConversacionControladorChatMultiusuario cccm = (ConversacionControladorChatMultiusuario) cvc;
            participantes = cccm.getParticipantesComoRecurso();
        }

        for(String participante : participantes){

            String n = StringUtils.parseResource(participante);
            if(n == null)
                continue;

            // Si no es el usuario actual
            if(n.compareTo(nick) != 0){
                // Añadir el nick del participante a la etiqueta principal
                if(primero){
                    etiquetaPrincipal.setText(etiquetaPrincipal.getText() + n);
                    primero = false;
                }
                else
                    etiquetaPrincipal.setText(etiquetaPrincipal.getText() + ", " + n);
                // Añadir el usuario y el nick a el panel de la conversación
                // El formato es participante: misala83@conf.jabberes.org/fppfc83
                //               n : fppfc83
                conversacion.añadirUsuario(participante,n);
            }
        }

        // Habilitar o deshabilitar los botones
        for(int i = 0;i < itemsDeMenu.length;i++){
            for(int j = 0;j < itemsDeMenu[i].length;j++){
                itemsDeMenu[i][j].setEnabled(activadosMultiusuario[i][j]);
                botonesBarraDeHerramientas[j].setEnabled(activadosMultiusuario[0][j]);
            }
        }

        // Actualizar preferencias
        this.actualizarPreferenciasMensajes();

        // Establecer el foco en el mensaje de texto
        nuevoMensaje.requestFocusInWindow();
    }
}

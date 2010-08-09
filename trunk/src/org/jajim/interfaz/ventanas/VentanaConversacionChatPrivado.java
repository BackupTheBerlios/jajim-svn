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
import org.jajim.controladores.ConversacionControladorChatPrivado;
import org.jajim.controladores.CuentaControlador;

/**
 * @author Florencio Cañizal Calles
 * @version 1.1
 * Clase que representa una ventana de un chat privado. Inicializa la interfaz
 * necesaria para que el usuario dialogue con un contacto.
 */
public class VentanaConversacionChatPrivado extends VentanaConversacion{

    // Matriz que determina si los botones están activados
    private final boolean activadosPrivado[][] = {
        {true,true,false,false}
    };

    /**
     * Constructor de la clase. Inicializa las variables necesarias.
     * @param vp La ventana principal de la aplicación.
     * @param alias El alias del contacto con el que se quiere iniciar el chat.
     */
    public VentanaConversacionChatPrivado(VentanaPrincipal vp,String alias){

        // LLamar al constructor del padre
        super(vp,alias);

        // Crear el controlador de la conversacion
        String usuario = ContactosControlador.getInstancia().getContactoPorAlias(alias);
        cvc = new ConversacionControladorChatPrivado(usuario,conversacion);

        // LLamar al controlador para crear un nuevo chat
        if(cvc instanceof ConversacionControladorChatPrivado){
            ConversacionControladorChatPrivado cccp = (ConversacionControladorChatPrivado) cvc;
            cccp.crearChatPrivado();
        }

        // Asignar nombres
        String identificador = CuentaControlador.getInstancia().getIdentificador();
        conversacion.añadirUsuario("Usuario",identificador);
        usuarioActual = identificador;
        conversacion.añadirUsuario(ContactosControlador.getInstancia().getContactoPorAlias(alias),alias);

        // Habilitar o deshabilitar los botones
        for(int i = 0;i < itemsDeMenu.length;i++){
            for(int j = 0;j < itemsDeMenu[i].length;j++){
                itemsDeMenu[i][j].setEnabled(activadosPrivado[i][j]);
                botonesBarraDeHerramientas[j].setEnabled(activadosPrivado[0][j]);
            }
        }

        // Actualizar preferencias
        this.actualizarPreferenciasMensajes();

        // Establecer el foco en el mensaje de texto
        nuevoMensaje.requestFocusInWindow();
    }

    /**
     * Constructor de la clase. Inicializa las variables necesarias.
     * @param vp La ventana principal de la aplicación
     * @param alias El alias del contacto
     * @param idChat El identificador de la petición de chat recibida
     */
    public VentanaConversacionChatPrivado(VentanaPrincipal vp,String alias,String idChat){

        // LLamar al constructor del padre
        super(vp,alias);

        // Crear el controlador de la conversacion
        String usuario = ContactosControlador.getInstancia().getContactoPorAlias(alias);
        cvc = new ConversacionControladorChatPrivado(usuario,conversacion);

        // LLamar al controlador para crear un nuevo chat
        if(cvc instanceof ConversacionControladorChatPrivado){
            ConversacionControladorChatPrivado cccp = (ConversacionControladorChatPrivado) cvc;
            cccp.aceptarChatPrivado(idChat);
        }

        // Asignar nombres
        String identificador = CuentaControlador.getInstancia().getIdentificador();
        conversacion.añadirUsuario("Usuario",identificador);
        usuarioActual = identificador;
        conversacion.añadirUsuario(ContactosControlador.getInstancia().getContactoPorAlias(alias),alias);

        // Habilitar o deshabilitar los botones
        for(int i = 0;i < itemsDeMenu.length;i++){
            for(int j = 0;j < itemsDeMenu[i].length;j++){
                itemsDeMenu[i][j].setEnabled(activadosPrivado[i][j]);
                botonesBarraDeHerramientas[j].setEnabled(activadosPrivado[0][j]);
            }
        }

        // Actualizar preferencias
        this.actualizarPreferenciasMensajes();

        // Establecer el foco en el mensaje de texto
        nuevoMensaje.requestFocusInWindow();
    }
}

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

import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import org.jajim.controladores.ContactosControlador;
import org.jajim.controladores.ConversacionControladorChatPrivado;
import org.jajim.controladores.CuentaControlador;
import org.jajim.modelo.conexiones.EventosDeConexionEnumeracion;

/**
 * @author Florencio Cañizal Calles
 * @version 1.2 Clase que representa una ventana de un chat privado. Inicializa la interfaz necesaria para que el
 * usuario dialogue con un contacto.
 */
public class VentanaConversacionChatPrivado extends VentanaConversacion {

    // Matriz que determina si los botones están activados
    private final boolean activadosPrivado[][] = {
        {true, true, false, false}
    };

    // Gestión de estados de la herramienta
    public static int ACTIVA = 0;
    public static int OCULTA = 1;
    public static int MINIMIZADA = 2;

    private int estado;

    /**
     * Constructor de la clase. Inicializa las variables necesarias.
     * <p>
     * @param alias El alias del contacto con el que se quiere iniciar el chat.
     */
    public VentanaConversacionChatPrivado(String alias) {

        // LLamar al constructor del padre
        super(alias);

        // Crear el controlador de la conversacion
        String usuario = ContactosControlador.getInstancia().getContactoPorAlias(alias);
        cvc = new ConversacionControladorChatPrivado(usuario, conversacion);

        // LLamar al controlador para crear un nuevo chat
        if (cvc instanceof ConversacionControladorChatPrivado) {
            ConversacionControladorChatPrivado cccp = (ConversacionControladorChatPrivado) cvc;
            cccp.crearChatPrivado();
        }

        // Asignar nombres
        String identificador = CuentaControlador.getInstancia().getIdentificador();
        conversacion.añadirUsuario("Usuario", identificador);
        usuarioActual = identificador;
        conversacion.añadirUsuario(ContactosControlador.getInstancia().getContactoPorAlias(alias), alias);

        // Habilitar o deshabilitar los botones
        for (int i = 0; i < itemsDeMenu.length; i++) {
            for (int j = 0; j < itemsDeMenu[i].length; j++) {
                itemsDeMenu[i][j].setEnabled(activadosPrivado[i][j]);
                botonesBarraDeHerramientas[j].setEnabled(activadosPrivado[0][j]);
            }
        }

        // Actualizar preferencias
        this.actualizarPreferenciasMensajes();

        // Establecer el foco en el mensaje de texto
        nuevoMensaje.requestFocusInWindow();

        // Establecer el estado por defecto
        this.estado = VentanaConversacionChatPrivado.ACTIVA;

        // Activar el listener
        this.crearWindowListener();
    }

    /**
     * Constructor de la clase. Inicializa las variables necesarias.
     * <p>
     * @param alias  El alias del contacto
     * @param idChat El identificador de la petición de chat recibida
     */
    public VentanaConversacionChatPrivado(String alias, String idChat) {

        // LLamar al constructor del padre
        super(alias);

        // Crear el controlador de la conversacion
        String usuario = ContactosControlador.getInstancia().getContactoPorAlias(alias);
        cvc = new ConversacionControladorChatPrivado(usuario, conversacion);

        // Asignar nombres
        String identificador = CuentaControlador.getInstancia().getIdentificador();
        conversacion.añadirUsuario("Usuario", identificador);
        usuarioActual = identificador;
        conversacion.añadirUsuario(ContactosControlador.getInstancia().getContactoPorAlias(alias), alias);

        // LLamar al controlador para crear un nuevo chat
        if (cvc instanceof ConversacionControladorChatPrivado) {
            ConversacionControladorChatPrivado cccp = (ConversacionControladorChatPrivado) cvc;
            cccp.aceptarChatPrivado(idChat);
        }

        // Habilitar o deshabilitar los botones
        for (int i = 0; i < itemsDeMenu.length; i++) {
            for (int j = 0; j < itemsDeMenu[i].length; j++) {
                itemsDeMenu[i][j].setEnabled(activadosPrivado[i][j]);
                botonesBarraDeHerramientas[j].setEnabled(activadosPrivado[0][j]);
            }
        }

        // Actualizar preferencias
        this.actualizarPreferenciasMensajes();

        // Establecer el foco en el mensaje de texto
        nuevoMensaje.requestFocusInWindow();

        // Establecer el estado por defecto
        this.estado = VentanaConversacionChatPrivado.ACTIVA;

        // Activar el listener
        this.crearWindowListener();
    }

    /**
     * Crea un oyente de eventos para la ventana.
     */
    private void crearWindowListener() {
        // Añadi el listener a la ventana.
        this.addWindowListener(new WindowListener() {
            @Override
            public void windowIconified(WindowEvent e) {
                // Si se minimiza la ventana se marca como minimizada.
                VentanaConversacionChatPrivado vccp = (VentanaConversacionChatPrivado) e.getWindow();
                vccp.setEstado(VentanaConversacionChatPrivado.MINIMIZADA);
            }

            @Override
            public void windowOpened(WindowEvent e) {
            }

            @Override
            public void windowClosing(WindowEvent e) {
            }

            @Override
            public void windowClosed(WindowEvent e) {
            }

            @Override
            public void windowDeiconified(WindowEvent e) {
            }

            @Override
            public void windowActivated(WindowEvent e) {
            }

            @Override
            public void windowDeactivated(WindowEvent e) {
            }
        });

    }

    /**
     * Método que se utiliza para notificar al chat privado eventos relacionados con la conexión.
     * <p>
     * @param edce      El tipo de evento de conexión.
     * @param propiedad Algún tipo de propiedad que es útil mostrar a la hora de notificar el evento.
     */
    public void notificarEvento(EventosDeConexionEnumeracion edce, String propiedad) {
        // Delegar la información del evento en el panel de contactos.
        conversacion.notificarEventoConexion(edce, propiedad);
    }

    /**
     * Actualiza el valor del atributo estado.
     * <p>
     * @param estado El nuevo valor para el atributo estado.
     */
    public void setEstado(int estado) {
        this.estado = estado;
    }

    /**
     * Retorna el estado de la ventana.
     * <p>
     * @return El estado de la ventana.
     */
    public int getEstado() {
        return this.estado;
    }
}

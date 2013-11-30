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

import java.util.Observer;
import org.jajim.excepciones.ContraseñaNoDisponibleException;
import org.jajim.excepciones.ImposibleLoginException;
import org.jajim.excepciones.NoHayCuentaException;
import org.jajim.excepciones.ServidorNoEncontradoException;
import org.jajim.modelo.conexiones.ConjuntoDeOyentes;
import org.jajim.modelo.conexiones.FactoriaDeConexiones;
import org.jajim.utilidades.log.ManejadorDeLogs;
import org.jivesoftware.smack.Roster;
import org.jivesoftware.smack.SASLAuthentication;
import org.jivesoftware.smack.XMPPConnection;
import org.jivesoftware.smack.XMPPException;
import org.jivesoftware.smack.packet.Packet;
import org.jivesoftware.smack.packet.Presence;

/**
 * @author Florencio Cañizal Calles
 * @version 1.2 Clase controladora. Utiliza las clases del modelo para proveer todo aquella fun cionalidad asociada a la
 * conexiones y desconexiones con un servidor.
 */
public class ConexionControlador {

    private static ConexionControlador instancia;
    private XMPPConnection xc;
    private ConjuntoDeOyentes cdo;

    /**
     * Constructor de la clase.
     */
    private ConexionControlador() {
    }

    /**
     * Conecta al usuario con su cuenta jabber activa.
     * <p>
     * @param observador El observador que va a recibir los eventos importantes de la conexión.
     * @return La lista de contactos del usuario.
     * <p>
     * @throws ServidorNoEncontradoException   Si no se puede loclizar el servidor con el que se iba a realizar la
     *                                         conexión
     * @throws ContraseñaNoDisponibleException Si no se puede recuperar la contra seña de la cuenta.
     * @throws ImposibleLoginException         Si no se puede hacer login con el servidor.
     * @throws NoHayCuentaException            Si no hay cuentas disponibles en el sistema.
     */
    public Roster conectar(Observer observador) throws ServidorNoEncontradoException, ContraseñaNoDisponibleException,
        ImposibleLoginException, NoHayCuentaException {

        // Comprobar si hay cuenta activa
        CuentaControlador cc = CuentaControlador.getInstancia();

        if (cc.getCuenta() == null) {
            throw new NoHayCuentaException();
        }

        // Recuperar los datos del servidor para conseguir una conexión
        String servidor = cc.getServidor();
        try {
            // Siempre que se conecta crea una cuenta nueva, así se evita problemas
            // de conexiones no reconocidas
            xc = FactoriaDeConexiones.getInstancia().getConexionNueva(servidor);
        }
        catch (ServidorNoEncontradoException snee) {
            throw snee;
        }

        String identificador = cc.getIdentificador();
        try {
            // Recuperar el identificador y la contraseña de la cuenta activa para rea
            // lizar la conexión.      
            String contraseña = cc.getContraseña();

            SASLAuthentication.supportSASLMechanism("PLAIN", 0);
            xc.login(identificador, contraseña);
            iniciarOyentesConexion(observador);
        }
        catch (XMPPException xe) {
            // En caso de que se produzca un error se escribe en el fichero
            // de log y se lanza una excepción
            ManejadorDeLogs mdl = ManejadorDeLogs.getManejadorDeLogs();
            mdl.escribir("No se puede establecer login con la siguiente cuenta - Servidor: " + servidor
                + " - Identificador de cuenta: " + identificador);
            throw new ImposibleLoginException();
        }
        catch (ContraseñaNoDisponibleException cnde) {
            throw cnde;
        }

        return xc.getRoster();
    }

    /**
     * Conecta al usuario con su cuenta jabber activa.
     * <p>
     * @param contraseña La contraseña introducida por el usuario en el formulario.
     * @param observador El observador que va a recibir los eventos importantes de la conexión.
     * @return La lista de contactos del usuario.
     * <p>
     * @throws ServidorNoEncontradoException Si no se puede loclizar el servidor con el que se iba a realizar la
     *                                       conexión
     * @throws ImposibleLoginException       Si no se puede hacer login con el servidor.
     */
    public Roster conectar(String contraseña, Observer observador) throws ServidorNoEncontradoException,
        ImposibleLoginException {

        // Recuperar los datos del servidor para conseguir una conexión
        CuentaControlador cc = CuentaControlador.getInstancia();

        String servidor = cc.getServidor();
        try {
            xc = FactoriaDeConexiones.getInstancia().getConexionNueva(servidor);
        }
        catch (ServidorNoEncontradoException snee) {
            throw snee;
        }

        // Recuperar el identificador
        String identificador = cc.getIdentificador();

        try {
            SASLAuthentication.supportSASLMechanism("PLAIN", 0);
            xc.login(identificador, contraseña);
            iniciarOyentesConexion(observador);
        }
        catch (XMPPException xe) {
            // En caso de que se produzca un error se escribe en el fichero
            // de log y se lanza una excepción
            xc.disconnect();
            ManejadorDeLogs mdl = ManejadorDeLogs.getManejadorDeLogs();
            mdl.escribir("No se puede establecer login con la siguiente cuenta - Servidor: " + servidor
                + " - Identificador de cuenta: " + identificador);
            throw new ImposibleLoginException();
        }

        return xc.getRoster();
    }

    /**
     * Envía el paquete especificado por la conexión activa.
     * <p>
     * @param paquete El paquete que se desea enviar.
     */
    public void enviarPaquete(Packet paquete) {

        // Enviar el paquete
        xc.sendPacket(paquete);
    }

    /**
     * Método que determina si el usuario está loggeado con su cuenta o no.
     * <p>
     * @return Devuelve true en caso de que esté conectado y false en caso contra rio.
     */
    public boolean isConectado() {
        if (xc == null) {
            return false;
        }
        else {
            return xc.isAuthenticated();
        }
    }

    /**
     * Desconecta al usuario de la actual conexión.
     */
    public void desconectar() {
        // Eliminar oyentes, desconectar al usuario y resetear conexión
        eliminarOyentesConexion();
        xc.disconnect();
        xc = null;
    }

    /**
     * Cambio el estado en el que se encuentra el usuario con respecto a la cone xión.
     * <p>
     * @param estado Cadena que especifica el nuevo estado del usuario.
     */
    public void cambiarEstado(String estado) {

        // Crear una presencia con ese estado
        Presence p = new Presence(Presence.Type.available, null, 0, Presence.Mode.valueOf(estado));

        // Enviar el paquete al usuario
        this.enviarPaquete(p);
    }

    /**
     * Inicia los oyentes de la conexión. Capturan paquetes importantes para la funcionalidad de la aplicación.
     * <p>
     * @param observador El observador que será informado de la llegada de los pa quetes importantes.
     */
    private void iniciarOyentesConexion(Observer observador) {
        cdo = new ConjuntoDeOyentes(observador, xc);
    }

    /**
     * Elimina los oyentes de la conexión cuando esta debe de ser cancelada.
     */
    private void eliminarOyentesConexion() {
        cdo.eliminarOyentesConexion();
        cdo = null;
    }

    /**
     * Método que retorna la conexión actual del sistema.
     * <p>
     * @return La conexión actual del sistema.
     */
    public XMPPConnection getXc() {
        return xc;
    }

    /**
     * Método para llevar a cabo el patrón Singleton.
     * <p>
     * @return Retorna la única instancia del controlador de las conexiones.
     */
    public static synchronized ConexionControlador getInstancia() {

        if (instancia == null) {
            instancia = new ConexionControlador();
        }

        return instancia;
    }
}

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

import com.thoughtworks.xstream.XStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import org.jajim.excepciones.ContraseñaNoDisponibleException;
import org.jajim.excepciones.CuentaExistenteException;
import org.jajim.excepciones.ImposibleCifrarDescifrarException;
import org.jajim.excepciones.ImposibleCifrarException;
import org.jajim.excepciones.ImposibleCrearCuentaException;
import org.jajim.excepciones.ImposibleDescifrarException;
import org.jajim.excepciones.ImposibleEliminarCuentaException;
import org.jajim.excepciones.ImposibleLoginException;
import org.jajim.excepciones.ImposibleModificarContraseñaException;
import org.jajim.excepciones.ImposibleValidarCuentaException;
import org.jajim.excepciones.ServidorNoEncontradoException;
import org.jajim.modelo.conexiones.FactoriaDeConexiones;
import org.jajim.modelo.cuentas.Cuentas;
import org.jajim.utilidades.cifrado.Cifrador;
import org.jajim.utilidades.log.ManejadorDeLogs;
import org.jivesoftware.smack.AccountManager;
import org.jivesoftware.smack.SASLAuthentication;
import org.jivesoftware.smack.XMPPConnection;
import org.jivesoftware.smack.XMPPException;

/**
 * @author Florencio Cañizal Calles
 * @version 1.2 Clase que controla la ejecución de los casos de uso relacionados con las cuen tas de los usuarios.
 */
public class CuentaControlador {

    private static CuentaControlador instancia;
    private String ficheroCuentas = null;
    @SuppressWarnings("FieldMayBeFinal")
    private Cuentas cs;

    /**
     * Constructor de la clase. Intenta cargar el fichero que almacena las cuentas del usuario. Si no encuentra el
     * fichero con las cuentas lanza una excepción.
     */
    private CuentaControlador() {

        // Intentar recuperar el fichero de cuentas
        String ruta = System.getProperty("user.home");
        ficheroCuentas = ruta + File.separator + ".JAJIM" + File.separator + "cuentas.xml";
        File f = new File(ficheroCuentas);

        // Si el fichero se crea la carpeta para el mismo y una lista de cuentas vacías
        if (!f.exists()) {
            cs = new Cuentas();
            String carpeta = ruta + File.separator + ".JAJIM";
            File fc = new File(carpeta);
            fc.mkdir();
        }
        else {
            try {
                // Si el fichero existe se carga en memoria su contenido
                XStream xs = new XStream();
                cs = (Cuentas) xs.fromXML(new FileInputStream(f));
            }
            catch (FileNotFoundException e) {
                // En caso de que se produzca un error se escribe en el fichero
                // de log y se crea una lista de cuentas vacías
                ManejadorDeLogs mdl = ManejadorDeLogs.getManejadorDeLogs();
                mdl.escribir("No se puede abrir el fichero de las cuentas del usuario");
                // Crear las cuentas
                cs = new Cuentas();
            }
        }
    }

    /**
     * Método que gestiona la creación de una nueva cuenta en el servidor seleccio nado por el usuario.
     * <p>
     * @param identificador     El identificador de la nueva cuenta.
     * @param servidor          El servidor en que se desea crear la cuenta.
     * @param contraseña        La contraseña de la nueva cuenta.
     * @param guardarContraseña Valor booleano que especifica si se guarda la con traseña en el sistema o no.
     * @return Verdadero en caso de ponerse como cuenta activa, falso en caso con trario.
     * <p>
     * @throws ServidorNoEncontradoException Si no se puede loclizar el servidor con el que se iba a realizar la
     *                                       conexión
     * @throws ImposibleCrearCuentaException Si no se puede crear la cuenta en el servidor.
     * @throws CuentaExistenteException      Si la cuenta existe.
     */
    public boolean crearCuenta(String identificador, String servidor, String contraseña, boolean guardarContraseña)
        throws ServidorNoEncontradoException, ImposibleCrearCuentaException, CuentaExistenteException {

        // Conseguir un manejador de cuentas
        XMPPConnection xc;
        boolean activa = false;

        // Comprobar que no existe una cuenta igual en el sistema
        if (cs.isPresente(identificador, servidor)) {
            // En caso de que se produzca un error se escribe en el fichero
            // de log y se lanza una excepción
            ManejadorDeLogs mdl = ManejadorDeLogs.getManejadorDeLogs();
            mdl.escribir("La siguiente cuenta ya está almacenada en el sistema - Servidor: " + servidor
                + " - Identificador de cuenta: " + identificador);
            throw new CuentaExistenteException();
        }

        try {
            xc = FactoriaDeConexiones.getInstancia().getConexion(servidor);
        }
        catch (ServidorNoEncontradoException e) {
            throw e;
        }

        AccountManager ac = new AccountManager(xc);

        try {
            // Crear la cuenta en el servidor
            ac.createAccount(identificador, contraseña);

            // Guardar la cuenta en el sistema.
            if (guardarContraseña) {
                Cifrador c = new Cifrador();
                contraseña = c.cifrar(contraseña);
                activa = cs.añadirCuenta(identificador, servidor, contraseña, true);
            }
            else {
                activa = cs.añadirCuenta(identificador, servidor);
            }
        }
        catch (XMPPException xe) {
            // En caso de que se produzca un error se escribe en el fichero
            // de log y se lanza una excepción
            ManejadorDeLogs mdl = ManejadorDeLogs.getManejadorDeLogs();
            mdl.
                escribir("No se puede crear una cuenta en: " + servidor + " - Identificador de cuenta: " + identificador);
            throw new ImposibleCrearCuentaException();
        }
        catch (ImposibleCifrarDescifrarException | ImposibleCifrarException icce) {
            // En caso de error de cifrado se añade la contraseña sin cifrar
            activa = cs.añadirCuenta(identificador, servidor, contraseña, false);
        }

        return activa;
    }

    /**
     * Método que gestiona la adición de una cuenta ya existente al sistema.
     * <p>
     * @param identificador     El identificador de la cuenta.
     * @param servidor          El servidor en el que se encuentra alojada la cuenta.
     * @param contraseña        La contraseña de la cuenta.
     * @param guardarContraseña Valor booleano que especifica si el usuario desea guardar la contraseña en el sistema.
     * @return Verdadero en caso de ponerse como cuenta activa, falso en caso con trario.
     * <p>
     * @throws ServidorNoEncontradoException   Si no se puede loclizar el servidor con el que se iba a realizar la
     *                                         conexión
     * @throws ImposibleValidarCuentaException Si no se puede encontrar la cuenta en el servidor.
     * @throws CuentaExistenteException        Si la cuenta existe.
     */
    public boolean añadirCuenta(String identificador, String servidor, String contraseña, boolean guardarContraseña)
        throws ServidorNoEncontradoException, ImposibleValidarCuentaException, CuentaExistenteException {

        XMPPConnection xc = null;
        boolean activa = false;

        // Comprobar que no existe una cuenta igual en el sistema
        if (cs.isPresente(identificador, servidor)) {
            // En caso de que se produzca un error se escribe en el fichero
            // de log y se lanza una excepción
            ManejadorDeLogs mdl = ManejadorDeLogs.getManejadorDeLogs();
            mdl.escribir("La siguiente cuenta ya está almacenada en el sistema - Servidor: " + servidor
                + " - Identificador de cuenta: " + identificador);
            throw new CuentaExistenteException();
        }

        // Conseguir una conexión al servidor
        try {
            xc = FactoriaDeConexiones.getInstancia().getConexion(servidor);
        }
        catch (ServidorNoEncontradoException snee) {
            throw snee;
        }

        try {
            // Conectarse al servidor para comprobar que la cuenta es válida.
            SASLAuthentication.supportSASLMechanism("PLAIN", 0);
            xc.login(identificador, contraseña);

            // Si la conexión se puede establecer, se desconecta del servidor y
            // se añade la cuenta a la lista de cuentas del sistema.
            if (xc.isAuthenticated()) {

                // Desconectar del sistema
                xc.disconnect();

                // Guardar la cuenta en el sistema.
                if (guardarContraseña) {
                    Cifrador c = new Cifrador();
                    contraseña = c.cifrar(contraseña);
                    activa = cs.añadirCuenta(identificador, servidor, contraseña, true);
                }
                else {
                    activa = cs.añadirCuenta(identificador, servidor);
                }
            }
        }
        catch (XMPPException xe) {
            // En caso de que se produzca un error se escribe en el fichero
            // de log y se lanza una excepción
            xc.disconnect();
            ManejadorDeLogs mdl = ManejadorDeLogs.getManejadorDeLogs();
            mdl.escribir("No se puede verificar la siguiente cuenta - Servidor: " + servidor
                + " - Identificador de cuenta: " + identificador);
            throw new ImposibleValidarCuentaException();
        }
        catch (ImposibleCifrarDescifrarException | ImposibleCifrarException icce) {
            // En caso de error de cifrado se añade la contraseña sin cifrar
            activa = cs.añadirCuenta(identificador, servidor, contraseña, false);
        }

        return activa;
    }

    /**
     * Guarda las cuentas almacenadas en el sistema en el fichero dedicado a ellas.
     */
    public void guardarCuentas() {

        // Conseguir el path al fichero en el que se almacenarán las cuentas
        if (ficheroCuentas == null) {
            String ruta = System.getProperty("user.home");
            ficheroCuentas = ruta + File.separator + ".JAJIM" + File.separator + "cuentas.xml";
        }
        File f = new File(ficheroCuentas);

        // Guardar las cuentas en el fichero
        try {
            XStream xs = new XStream();
            xs.toXML(cs, new FileOutputStream(f));
        }
        catch (FileNotFoundException fnfe) {
            // En caso de que se produzca un error se escribe en el fichero
            // de log y se lanza una excepción
            ManejadorDeLogs mdl = ManejadorDeLogs.getManejadorDeLogs();
            mdl.escribir("No se puede guardar las cuentas: Fichero especificado no encontrado");
        }
    }

    /**
     * Método que marca a la cuenta especificada como cuenta activa.
     * <p>
     * @param identificador El identificador de la cuenta.
     * @param servidor      El servidor de la cuenta.
     */
    public void cambiarCuenta(String identificador, String servidor) {

        // Delegar en la clase de las cuentas
        cs.cambiarCuenta(identificador, servidor);
    }

    /**
     * Elima la cuena del sistema, pero no del servidor asociado.
     * <p>
     * @param identificador El identificador de la cuenta.
     * @param servidor      El servidor donde se aloja la cuenta.
     */
    public void eliminarCuentaSistema(String identificador, String servidor) {

        // Delegar en la clase de las cuentas
        cs.eliminarCuentaSistema(identificador, servidor);
    }

    /**
     * Elimina una cuenta del servidor donde se encontraba alojada y del sistema.
     * <p>
     * @param identificador El identificador de la cuenta.
     * @param servidor      El servidor donde estaba almacenada.
     * @throws ContraseñaNoDisponibleException  Si no se puede recuperar la contra seña de la cuenta.
     * @throws ServidorNoEncontradoException    Si no se puede loclizar el servidor en le que está almacenada la cuenta.
     * @throws ImposibleLoginException          Si no se puede hacer login con el servidor.
     * @throws ImposibleEliminarCuentaException Si no se puede borrar la cuenta del servidor.
     */
    public void eliminarCuentaServidor(String identificador, String servidor) throws ContraseñaNoDisponibleException,
        ServidorNoEncontradoException, ImposibleLoginException, ImposibleEliminarCuentaException {

        // Recuperar la contraseña de la cuenta
        boolean cifrada = cs.isCifrada(identificador, servidor);
        String contraseña = cs.getContraseña(identificador, servidor);

        // Si no se disponde de una contraseña almacenada se lanza la excepción
        // para que el sistema solicite la contraseña al usuario.
        if (contraseña == null) {
            throw new ContraseñaNoDisponibleException();
        }

        // Si la contraseña se guarda cifrada descifrarla
        if (cifrada) {
            try {
                Cifrador c = new Cifrador();
                contraseña = c.descifrar(contraseña);
            }
            catch (ImposibleCifrarDescifrarException | ImposibleDescifrarException idce) {
                throw new ContraseñaNoDisponibleException();
            }
        }

        // Recupera la conexión y hacer login
        FactoriaDeConexiones factoria = FactoriaDeConexiones.getInstancia();
        XMPPConnection xc = null;
        try {
            xc = factoria.getConexionNueva(servidor);

            SASLAuthentication.supportSASLMechanism("PLAIN", 0);
            xc.login(identificador, contraseña);
        }
        catch (ServidorNoEncontradoException snee) {
            throw new ServidorNoEncontradoException();
        }
        catch (XMPPException xe) {
            // En caso de que se produzca un error se escribe en el fichero
            // de log y se lanza una excepción
            ManejadorDeLogs mdl = ManejadorDeLogs.getManejadorDeLogs();
            mdl.escribir("No se puede puede hacer login en la siguiente cuenta: " + identificador + "@" + servidor);
            throw new ImposibleLoginException();
        }

        // Eliminar la cuenta del servidor y del sistema
        try {
            AccountManager ac = new AccountManager(xc);
            ac.deleteAccount();
            cs.eliminarCuentaSistema(identificador, servidor);
            xc.disconnect();
        }
        catch (XMPPException xe) {
            // En caso de que se produzca un error se escribe en el fichero
            // de log y se lanza una excepción
            ManejadorDeLogs mdl = ManejadorDeLogs.getManejadorDeLogs();
            mdl.escribir("No se puede eliminar la siguiente cuenta: " + identificador + "@" + servidor);
            xc.disconnect();
            throw new ImposibleEliminarCuentaException();
        }
    }

    /**
     *
     * Elimina una cuenta del servidor donde se encontraba alojada y del sistema.
     * <p>
     * @param identificador El identificador de la cuenta.
     * @param servidor      El servidor donde estaba almacenada.
     * @param contraseña    La contraseña de la cuenta que se va a eliminar.
     * @throws ServidorNoEncontradoException    Si no se puede loclizar el servidor en le que está almacenada la cuenta.
     * @throws ImposibleLoginException          Si no se puede hacer login con el servidor.
     * @throws ImposibleEliminarCuentaException Si no se puede borrar la cuenta del servidor.
     */
    public void eliminarCuentaServidor(String identificador, String servidor, String contraseña) throws
        ServidorNoEncontradoException, ImposibleLoginException, ImposibleEliminarCuentaException {

        // Recupera la conexión y hacer login
        FactoriaDeConexiones factoria = FactoriaDeConexiones.getInstancia();
        XMPPConnection xc = null;
        try {
            xc = factoria.getConexion(servidor);
            xc.login(identificador, contraseña);
        }
        catch (ServidorNoEncontradoException snee) {
            throw new ServidorNoEncontradoException();
        }
        catch (XMPPException xe) {
            // En caso de que se produzca un error se escribe en el fichero
            // de log y se lanza una excepción
            ManejadorDeLogs mdl = ManejadorDeLogs.getManejadorDeLogs();
            mdl.escribir("No se puede puede hacer login en la siguiente cuenta: " + identificador + "@" + servidor);
            throw new ImposibleLoginException();
        }

        // Eliminar la cuenta del servidor y del sistema
        try {
            AccountManager ac = new AccountManager(xc);
            ac.deleteAccount();
            cs.eliminarCuentaSistema(identificador, servidor);
            xc.disconnect();
        }
        catch (XMPPException xe) {
            // En caso de que se produzca un error se escribe en el fichero
            // de log y se lanza una excepción
            ManejadorDeLogs mdl = ManejadorDeLogs.getManejadorDeLogs();
            mdl.escribir("No se puede eliminar la siguiente cuenta: " + identificador + "@" + servidor);
            xc.disconnect();
            throw new ImposibleEliminarCuentaException();
        }
    }

    /**
     * Modifica la contraseña en el servidor y en el sistema.
     * <p>
     * @param contraseña        La nueva contraseña a establecer.
     * @param guardarContraseña Valor booleano que indica si se debe guardar la contraseña o no.
     * @throws ImposibleModificarContraseñaException Si no se puede cambiar la con traseña de la cuenta.
     */
    public void modificarContraseña(String contraseña, boolean guardarContraseña) throws
        ImposibleModificarContraseñaException {

        // Recuperar conexión y crear un manejador de cuentas
        ConexionControlador cnc = ConexionControlador.getInstancia();
        XMPPConnection xc = cnc.getXc();
        AccountManager am = new AccountManager(xc);

        try {
            // Cambiar la contraseña en el servidor
            am.changePassword(contraseña);
        }
        catch (XMPPException xe) {
            // En caso de que se produzca un error se escribe en el fichero
            // de log y se lanza una excepción
            ManejadorDeLogs mdl = ManejadorDeLogs.getManejadorDeLogs();
            mdl.escribir("No se puede modifcar la contraseña de la cuenta actual");
            // Lanzar la excepción
            throw new ImposibleModificarContraseñaException();
        }

        // Recuperar el identificador y el servidor de la cuenta
        String user = xc.getUser();
        int arrroba = user.indexOf("@");
        String identificador = user.substring(0, arrroba);
        String servidor = xc.getHost();

        if (guardarContraseña) {
            try {
                // Cifrar la contraseña
                Cifrador c = new Cifrador();
                contraseña = c.cifrar(contraseña);

                // Guardar la contraseña en el sistema
                cs.modificarContraseña(identificador, servidor, contraseña, true);
            }
            catch (ImposibleCifrarDescifrarException | ImposibleCifrarException ice) {
                // si se produce un error de cifrado, guardar la contraseña sin
                // cifrar
                cs.modificarContraseña(identificador, servidor, contraseña, false);
            }
        }
        else {
            cs.borrarContraseña(identificador, servidor);
        }
    }

    /**
     * Devuelve el servidor en el que se encuentra alojada la cuenta activa.
     * <p>
     * @return El servidor de la cuenta activa.
     */
    public String getServidor() {
        return cs.getActiva().getServidor();
    }

    /**
     * Devuelve el identificador de la cuenta activa.
     * <p>
     * @return El identificador de la cuenta activa.
     */
    public String getIdentificador() {
        return cs.getActiva().getIdentificador();
    }

    /**
     * Devuelve la contraseña de la cuenta activa.
     * <p>
     * @return La contraseña de la cuenta activa.
     * <p>
     * @throws ContraseñaNoDisponibleException
     */
    public String getContraseña() throws ContraseñaNoDisponibleException {

        String contraseña = null;
        contraseña = cs.getActiva().getContrasena();
        boolean cifrada = cs.getActiva().getCifrada();

        // Si no está almacena la contraseña, lanzar una excepción para que el
        // sistema se la solicite al usuario
        if (contraseña == null) {
            throw new ContraseñaNoDisponibleException();
        }

        // Si la contraseña se almacena cifrada, descifrala
        if (cifrada) {
            try {
                Cifrador c = new Cifrador();
                contraseña = c.descifrar(contraseña);
            }
            catch (ImposibleCifrarDescifrarException | ImposibleDescifrarException idce) {
                throw new ContraseñaNoDisponibleException();
            }
        }

        return contraseña;
    }

    /**
     * Devuelve una cadena con la cuenta del usuario. Se forma de la siguiente ma nera: "identificador"@"servidor".
     * <p>
     * @return La cuenta del usuario.
     */
    public String getCuenta() {
        if (cs.getActiva() != null) {
            return this.getIdentificador() + "@" + this.getServidor();
        }
        else {
            return null;
        }
    }

    /**
     * Recupera la información de las cuentas y la devuelve al solicitante.
     * <p>
     * @return Una matriz con la informaicón de las cuentas.
     */
    public String[][] getInformacionDeCuentas() {

        return cs.getInformacionDeCuentas();
    }

    /**
     * Método estático utilizado para implementar el Singleton.
     * <p>
     * @return Retorna la única instancia que hay del controlador de cuentas en el sistema.
     */
    public static CuentaControlador getInstancia() {

        // Si la instancia es nula, crea una nueva. Si no retorna la ya existente
        if (instancia == null) {
            instancia = new CuentaControlador();
        }

        return instancia;
    }
}

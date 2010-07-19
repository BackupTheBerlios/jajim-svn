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
import org.jajim.excepciones.ImposibleBorrarFicheroException;
import org.jajim.excepciones.ImposibleEnviarFicheroException;
import org.jajim.excepciones.ImposibleRecibirFicheroException;
import org.jajim.excepciones.ImposibleRecuperarParticipanteException;
import org.jajim.excepciones.ImposibleRenombrarFicheroException;
import org.jajim.excepciones.ImposibleReubicarFicheroException;
import org.jajim.excepciones.ImposibleVisualizarFicheroException;
import org.jajim.excepciones.RutaNoDisponibleException;
import org.jajim.modelo.transferencias.RecepcionFicherosListener;
import org.jajim.utilidades.log.ManejadorDeLogs;
import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Observer;
import java.util.Set;
import org.jivesoftware.smack.XMPPConnection;
import org.jivesoftware.smack.XMPPException;
import org.jivesoftware.smackx.filetransfer.FileTransfer;
import org.jivesoftware.smackx.filetransfer.FileTransferManager;
import org.jivesoftware.smackx.filetransfer.FileTransferRequest;
import org.jivesoftware.smackx.filetransfer.IncomingFileTransfer;
import org.jivesoftware.smackx.filetransfer.OutgoingFileTransfer;

/**
 * @author Florencio Cañizal Calles
 * @version 1.0.1
 * Clase controlador que gestiona todas las operaciones vinculadas a la transfe
 * rencia de ficheros.
 */
public class TransferenciaFicherosControlador {

    private FileTransferManager ftm;
    private RecepcionFicherosListener rfl;
    private Map<String,FileTransfer> enCurso;
    private Map<String,String> rutas;

    /**
     * Constructor de la clase. Inicializa las variables necesarias.
     */
    public TransferenciaFicherosControlador(){
        enCurso = Collections.synchronizedMap(new HashMap<String,FileTransfer>());
        rutas = Collections.synchronizedMap(new HashMap<String,String>());
    }

    /**
     * Crea un manager de transferencia de ficheros a partir de una conexión.
     * @param xc Conexión que se ha establecido.
     */
    public void crearManager(XMPPConnection xc,Observer observer){
        ftm = new FileTransferManager(xc);
        rfl = new RecepcionFicherosListener(observer);
        ftm.addFileTransferListener(rfl);
    }

    /**
     * Envía el fichero ubicado en la ruta especificada a todos los contactos de
     * la lista.
     * @param contactos Lista de contactos a los que enviar el fichero.
     * @param ruta La ubicación en el sistema del fichero que se quiere enviar.
     * @param descripcion La descripcion del fichero.
     * @return El identificador de la transferencia.
     * @throws FicheroNoEncontradoException Si no se encuentra el fichero que se
     * desea eliminar
     * @throws ImposibleEnviarFicheroException Si no se puede enviar el fichero
     * a los contactos
     * @throws ImposibleRecuperarParticipanteException Si no se puede recuperar
     * el JID de los participantes de la conversación a los que se ca a mandar el
     * fichero.
     */
    public String enviarFichero(ContactosControlador ctc,ConversacionControlador cvc,String[] contactos,String ruta,String descripcion) throws FicheroNoEncontradoException,ImposibleEnviarFicheroException,ImposibleRecuperarParticipanteException{

        String id = null;

        // Crear un fichero
        File fichero = new File(ruta);

        // Comprobar si el fichero existe, de no ser así se lanzará una excepción
        if(!fichero.exists() || !fichero.isFile()){
            // En caso de que se produzca un error se escribe en el fichero
            // de log y se lanza una excepción
            ManejadorDeLogs mdl = ManejadorDeLogs.getManejadorDeLogs();
            mdl.escribir("No existe el fichero: " + ruta);
            throw new FicheroNoEncontradoException();
        }

        // Enviar el fichero a cada uno de los contactos
        OutgoingFileTransfer oft = null;
        for(String s : contactos){
            
            String JID = null;
            try{
                // Recuperar el JID del usuario para poder enviar el mensaje.
                if(!s.contains("."))
                    JID = cvc.getJIDParticipante(s);
                else
                    JID = ctc.getJID(s);
                oft = ftm.createOutgoingFileTransfer(JID);

                oft.sendFile(fichero,descripcion);
                id = oft.getStreamID();
            }catch(XMPPException xe){
                // En caso de que se produzca un error se escribe en el fichero
                // de log y se lanza una excepción
                ManejadorDeLogs mdl = ManejadorDeLogs.getManejadorDeLogs();
                mdl.escribir("No se puede enviar el fichero: " + ruta + "- Contacto: " + s);
                throw new ImposibleEnviarFicheroException();
            }catch(ImposibleRecuperarParticipanteException irp){
                throw irp;
            }
        }

        // Guardar la última transferncia de fichero en el map
        enCurso.put(id,oft);

        return id;
    }

    /**
     * Acepta una de las peticiones de transferencia recibidas por el sistema.
     * @param idTransferencia El identificador de la petición de transferencia.
     * @param ruta La ruta en que se debe guardar el fichero que acompaña la trans
     * ferencia.
     * @return El identificador del stream y el nombre del fichero.
     * @throws RutaNoDisponibleException Si la ruta proporcionada no es válida.
     * @throws ImposibleRecibirFicheroException Si no se puede recibir el fichero.
     */
    public String[] aceptarFichero(int idTransferencia,String ruta) throws RutaNoDisponibleException,ImposibleRecibirFicheroException{

        String[] retornar = new String[2];

        // Comprobar si la ruta es válida
        File valida = new File(ruta);
        if(!valida.isDirectory())
            throw new RutaNoDisponibleException();

        // Recuperar la petición de transferencia de fichero
        FileTransferRequest ftr = rfl.getPeticion(idTransferencia);

        // Aceptar la petición
        IncomingFileTransfer ift = ftr.accept();

        // Descargar el fichero
        File f = new File(ruta + File.separator + ftr.getFileName());
        try{
            ift.recieveFile(f);
        }catch(XMPPException xe){
            throw new ImposibleRecibirFicheroException();
        }

        // Conseguir los valores a devolver
        retornar[0] = ftr.getFileName();
        retornar[1] = ftr.getStreamID();

        // Guardar la ruta
        rutas.put(retornar[1],ruta);

        // Guardar la transferencia en la lista de transferencias en curso.
        enCurso.put(retornar[1],ift);

        return retornar;
    }

    /**
     * Rechaza la peticicón de transferencia cuyo identificador se corresponde con
     * el suministrado.
     * @param idTransferencia El identificador de la transferencia.
     */
    public void rechazarFichero(int idTransferencia){

        // Recuperar la petición de transferencia de fichero
        FileTransferRequest ftr = rfl.getPeticion(idTransferencia);

        // Rechazar la peticicón
        ftr.reject();
    }

    /**
     * Cancela una de las tranferencias en curso.
     * @param idTransferencia El identificador de la transferencia que se desea
     * cancelar.
     */
    public void cancelarTransferencia(String idTransferencia){

        // Recuperar la transferencia y cancelarla
        FileTransfer ft = enCurso.get(idTransferencia);
        ft.cancel();
        enCurso.remove(ft);

        // Eliminar la parte del fichero descargada si es el receptor
        final String nombre = ft.getFileName();
        ft = null;
        final String ruta = rutas.get(idTransferencia);
        if(ruta != null){
            rutas.remove(idTransferencia);
            // Se borra en otro hilo porque hay que esperar que finalice la can
            // celación correctamente antes de poder borrar el fichero.
            final TransferenciaFicherosControlador tfc = this;
            Runnable r = new Runnable(){
                public void run(){
                    try{
                        Thread.sleep(3000);
                        tfc.borrarFichero(nombre,ruta);
                    }catch(Exception e){}
                }
            };
            new Thread(r,"").start();
        }
    }

    /**
     * Abre la herramienta adecuada para visualizar el fichero.
     * @param nombre El nombre del fichero.
     * @param ruta La ruta en la que está  localizado.
     * @throws ImposibleVisualizarFicheroException Si no se encuentra la herrami
     * enta adecuada para abrir el fichero.
     */
    public void visualizarFichero(String nombre,String ruta) throws ImposibleVisualizarFicheroException{

        // Construir la URL del fichero
        String fichero = ruta + File.separator + nombre;
        File f = new File(fichero);
        String url = f.getAbsolutePath();

        // Comprobar que sistema operativo se está ejecutando y actuar en conse
        // cuencia
        try{
            if(System.getProperty("os.name").toUpperCase().indexOf("95") != -1)
                Runtime.getRuntime().exec(new String[]{"command.com","/C","start",url});
            else if(System.getProperty("os.name").toUpperCase().indexOf("WINDOWS") != -1)
                Runtime.getRuntime().exec( new String[]{"cmd.exe", "/C", "start","\"dummy\"","\"" + url + "\""} );
            else if(System.getProperty("os.name").toUpperCase().indexOf("MAC") != -1)
                Runtime.getRuntime().exec(new String[]{"open",url});
            else if(System.getProperty("os.name").toUpperCase().indexOf("LINUX") != -1){
                String desktop = this.getLinuxDesktop();
                if(desktop.equals("kde"))
                    Runtime.getRuntime().exec(new String[]{"kfmclient","exec",url});
                else if(desktop.equals("gnome"))
                    Runtime.getRuntime().exec(new String[]{"gnome-open",url});
                else
                    throw new ImposibleVisualizarFicheroException();
            }
        }catch(IOException ie){
            // En caso de que se produzca un error se escribe en el fichero
            // de log y se lanza una excepción
            ManejadorDeLogs mdl = ManejadorDeLogs.getManejadorDeLogs();
            mdl.escribir("No se puede abrir el fichero: " + url);
            throw new ImposibleVisualizarFicheroException();
        }
    }

    /**
     * Pregunta en sistemas linux si existe una determinada variable.
     * @param envvar La variable que se va a consultar.
     * @return Cadena vacia si no se encuentra la variable y su valor si se encuen
     * tra.
     */
    private String getEnv(String envvar){

        // Lanzar un proceso que pregunta pou una determinada variable, recuperar
        // la salida y delvolver una cadena vacia áquella no se encuentra o su re
        // sultado si se encuentra.
        try{
            String[] cmd = { "/bin/sh", "-c", "echo $" +  envvar};
            Process p = Runtime.getRuntime().exec(cmd);
            BufferedReader br = new BufferedReader(new InputStreamReader(p.getInputStream()));
            String value = br.readLine();
            if(value == null)
                return "";
            else
                return value.trim();
        }
        catch(Exception error){
          return "";
        }
    }

    /**
     * Devuelve una cadena con el tipo de escritorio en el que nos encontramos si
     * estamos en un sistema operativo tipo linux.
     * @return Cadena con el nombre del escritorio o cadena vacia si no se encuen
     * tra un escritorio válido.
     */
    private String getLinuxDesktop(){

        // Preguntar por variables que sólo están disponibles en determinados tipos
        // de escritorio.
        String linuxDesktop = null;
        if(!getEnv("KDE_FULL_SESSION").equals("") || !getEnv("KDE_MULTIHEAD").equals("")){
            linuxDesktop="kde";
        }
        else if(!getEnv("GNOME_DESKTOP_SESSION_ID").equals("") || !getEnv("GNOME_KEYRING_SOCKET").equals("")){
            linuxDesktop="gnome";
        }
        else linuxDesktop="";

        return linuxDesktop;
    }

    /**
     * Borra del sistema el fichero cuyo nombre y ruta se corresponden con los
     * proporcionados.
     * @param nombre El nombre del fichero.
     * @param ruta La ubicación donde se encuentra almacenado.
     * @throws ImposibleBorrarFicheroException Si no se puede borrar el fichero
     * del sistema.
     */
    public void borrarFichero(String nombre,String ruta) throws ImposibleBorrarFicheroException{

        String fichero = ruta + File.separator + nombre;
        File f = new File(fichero);
        boolean ficheroBorrado = f.delete();

        if(!ficheroBorrado){
            // En caso de que se produzca un error se escribe en el fichero
            // de log y se lanza una excepción
            ManejadorDeLogs mdl = ManejadorDeLogs.getManejadorDeLogs();
            mdl.escribir("No se puede borrar el fichero: " + f.getAbsolutePath());
            throw new ImposibleBorrarFicheroException();
        }
    }

    /**
     * Cambia el nombre del fichero cuyos valores se corresponden con los propor
     * cionados.
     * @param nombre El nombre que tiene el fichero.
     * @param ruta La ruta en la que se encuentra localizado.
     * @param nuevoNombre El nuevo nombre del fichero.
     * @throws ImposibleRenombrarFicheroException Si no se puede cambiar el nombre
     * del fichero.
     */
    public void renombrarFichero(String nombre,String ruta,String nuevoNombre) throws ImposibleRenombrarFicheroException{

        // Conseguir los paths de los ficheros viejos y nuevos
        File ficheroViejo = new File(ruta + File.separator + nombre);
        File ficheroNuevo = new File(ruta + File.separator + nuevoNombre);

        // Cambiar el nombre del fichero
        boolean correcto = ficheroViejo.renameTo(ficheroNuevo);

        // Si no se ha podido renombrar el fichero devolver una excepción
        if(!correcto){
            // En caso de que se produzca un error se escribe en el fichero
            // de log y se lanza una excepción
            ManejadorDeLogs mdl = ManejadorDeLogs.getManejadorDeLogs();
            mdl.escribir("No se puede renombrar el fichero: " + ficheroViejo.getAbsolutePath());
            throw new ImposibleRenombrarFicheroException();
        }
    }

    /**
     * Método que mueve el fichero que se corresponde con los valores proporciona
     * dos a la nueva ruta especificada.
     * @param nombre El nombre del fichero.
     * @param ruta Su ruta actual.
     * @param nuevaRuta La nueva ruta para el fichero.
     * @throws ImposibleReubicarFicheroException Si no se puede mover el fichero.
     */
    public void reubicarFichero(String nombre,String ruta,String nuevaRuta) throws ImposibleReubicarFicheroException{

        // Conseguir los paths de los ficheros viejos y nuevos
        File ficheroViejo = new File(ruta + File.separator + nombre);
        File ficheroNuevo = new File(nuevaRuta + File.separator + nombre);

        // Cambiar el nombre del fichero
        boolean correcto = ficheroViejo.renameTo(ficheroNuevo);

        // Si no se ha podido reubicar el fichero devolver una excepción
        if(!correcto){
            // En caso de que se produzca un error se escribe en el fichero
            // de log y se lanza una excepción
            ManejadorDeLogs mdl = ManejadorDeLogs.getManejadorDeLogs();
            mdl.escribir("No se puede renombrar el fichero: " + ficheroViejo.getAbsolutePath());
            throw new ImposibleReubicarFicheroException();
        }
    }

    /**
     * Elimina la transferencia de la lista de tansferencias en curso.
     * @param idTransferencia El identificador de la transferencia que se va a
     * eliminar.
     * @return Retorna un array con el nombre y la ruta del fichero de la tansfe
     * rencia.
     */
    public synchronized String[] cerrarTransferencia(String idTransferencia){

        // Recuparar el nombre y la ruta del fichero
        String[] datos = new String[2];
        FileTransfer ft = enCurso.get(idTransferencia);
        datos[0] = ft.getFileName();
        datos[1] = rutas.get(idTransferencia);
        rutas.remove(idTransferencia);

        // Borrar la transferencia de la lista de transferencias en curso.
        enCurso.remove(idTransferencia);

        return datos;
    }

    /**
     * Cancela todas las transferencias en curso y las elimina de la lista.
     */
    public void abortarTransferencias(){

        // Recuperar la colección de transferencias
        Collection<FileTransfer> collection = enCurso.values();

        // Cancelarlas una a una
        Iterator<FileTransfer> iterator = collection.iterator();
        while(iterator.hasNext()){

            FileTransfer ft = iterator.next();
            ft.cancel();

            // Esperar hasta que la transferencia esté cancelada
            boolean t = true;
            while(t){
                if(ft.getStatus() == FileTransfer.Status.cancelled)
                    t = false;
                try{Thread.sleep(500);}catch(Exception e){}
            }
        }

        // Eliminar las transferencias de la lista de las mismas
        enCurso.clear();
    }

    /**
     * Limpiar la información necesaria en el controlador para poder hacer otra
     * conexión.
     */
    public void desconectar(){
        // Desconectar al oyente y poner el manager y el oyente a null
        rfl.desconectar();
        rfl = null;
        ftm = null;
        enCurso.clear();
    }

    /**
     * Retorna el progreso de una determinada transferencia. Es un valor entre 0
     * y 1.
     * @param idTransferencia el identificador de la transferencia.
     * @return El progreso de la transferencia.
     */
    public synchronized double getProgresoTransferencia(String idTransferencia){

        // Recuperar la transferencia
        FileTransfer ft = enCurso.get(idTransferencia);

        double progreso = ft.getProgress();

        if(ft.getStatus() == FileTransfer.Status.refused || (ft.getStatus() == FileTransfer.Status.complete && (progreso * 100) != 100)){
            // Retornar -1 para informar de que se ha cancelado la transferencia
            progreso = -1;
            // Limpiar las variables necesarias
            enCurso.remove(idTransferencia);
            String nombre = ft.getFileName();
            String ruta = rutas.get(idTransferencia);
            if(ruta != null){
                rutas.remove(idTransferencia);
                try{
                    this.borrarFichero(nombre,ruta);
                }catch(ImposibleBorrarFicheroException ibfe){}
            }
        }

        // Retornar su progreso
        return progreso;
    }

    /**
     * Método que retorna un array con los identificadores de las transferencias
     * mantenidas con el contacto especificado.
     * @param contacto El contacto del que se quieren recuperar las transferencias.
     * @return Un array con los identificadores de las transferencias.
     */
    public String[] getIdentificadoresTransferencias(String contacto){
        
        String[] identificadores = null;
        List<String> auxiliar = new ArrayList<String>();

        // Recorrer la lista de transferencias y almacenar el identificador
        Set<Entry<String,FileTransfer>> conjunto = enCurso.entrySet();
        Iterator <Entry<String,FileTransfer>> iterator = conjunto.iterator();
        while(iterator.hasNext()){
            Entry<String,FileTransfer> entrada = iterator.next();
            FileTransfer ft = entrada.getValue();
            String par = ft.getPeer();
            int posicion = par.indexOf("/");
            par = par.substring(0,posicion);
            if(par.compareTo(contacto) == 0){
                auxiliar.add(entrada.getKey());
            }
        }

        identificadores = auxiliar.toArray(new String[0]);

        return identificadores;
    }
}

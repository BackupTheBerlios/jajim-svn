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

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Container;
import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;
import java.util.Observable;
import java.util.Observer;
import java.util.ResourceBundle;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;
import javax.swing.BorderFactory;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextPane;
import javax.swing.SwingUtilities;
import org.jajim.interfaz.ventanas.VentanaConversacion;
import org.jajim.interfaz.ventanas.VentanaConversacionChatPrivado;
import org.jajim.main.Main;
import org.jajim.modelo.conexiones.EventosDeConexionEnumeracion;
import org.jajim.modelo.conversaciones.EventosConversacionEnumeracion;
import org.jajim.utilidades.log.ManejadorDeLogs;
import org.jivesoftware.smack.util.StringUtils;

/**
 * @author Florencio Cañizal Calles
 * @version 1.1
 * Clase que presenta el editor en el que se muestra los mensajes intercambiados
 * entre los miembros de la conversación. Se ocupa de actualizar los mensajes de
 * la misma.
 */
public class PanelConversacion implements Observer{

    private ResourceBundle texto = ResourceBundle.getBundle("resources.Idioma",Main.loc);

    // Ventana principal
    private VentanaConversacion vc;

    // Presentación
    private JTextPane contenidoConversacion;

    // Manejo de contenido
    private final String cabecera = "<html><body>";
    private final String pie = "</body></html>";
    private StringBuffer mensajes;
    private StringBuffer total;
    private Map<String,String> usuarios;
    private Map<EventosConversacionEnumeracion, String> eventosConversacion;
    private Map<EventosDeConexionEnumeracion, String> eventosConexion;
    private Map<String,String[]> estilos;

    // Gestión de estilos por defecto
    private String[][] estilosPorDefecto = {
        {"arial","10","256","0","0","false","false"},
        {"courier","11","0","256","0","true","false"},
        {"courier new","12","0","0","256","false","true"},
        {"geneva","10","128","128","0","true","true"},
        {"georgia","11","128","0","128","false","false"}
    };
    private int actualEstiloPorDefecto = 0;

    // Gestión de ventana minimizada - Sonido
    private Clip sonido;

    /**
     * Constructor de la clase. Inicializa las variables necesarias y crea la
     * presentación.
     */
    public PanelConversacion(VentanaConversacion vc){

        // Inicialización de variables
        this.vc = vc;

        // Presentación de la ventana de conversaciones
        JPanel panelConversacion = new JPanel(new BorderLayout());
        panelConversacion.setBorder(BorderFactory.createEmptyBorder(0,10,15,10));
        contenidoConversacion = new JTextPane();
        contenidoConversacion.setEditable(false);
        contenidoConversacion.setContentType("text/html");
        contenidoConversacion.setBackground(Color.WHITE);
        JScrollPane scrollPane = new JScrollPane(contenidoConversacion);
        panelConversacion.add(BorderLayout.CENTER,scrollPane);
        Container cp = vc.getContentPane();
        cp.add(BorderLayout.CENTER,panelConversacion);

        // Iniciar cadenas
        mensajes = new StringBuffer();
        total = new StringBuffer();

        // Iniciar contenedor de usuarios
        usuarios = new HashMap<>();

        // Iniciar las cadenas de los eventos de la conversación
        eventosConversacion = new HashMap<>();
        eventosConversacion.put(EventosConversacionEnumeracion.participanteAñadido,texto.getString("añadir_participante_evento"));
        eventosConversacion.put(EventosConversacionEnumeracion.invitacionRechazada,texto.getString("rechazo_invitacion_evento"));
        eventosConversacion.put(EventosConversacionEnumeracion.participanteDesconectado,texto.getString("participante_desconectado_evento"));

        // Iniciar las cadenas de los eventos de la conexión
        eventosConexion = new HashMap<>();
        eventosConexion.put(EventosDeConexionEnumeracion.usuarioConectado, texto.getString("usuario_conectado_evento_conversacion"));
        eventosConexion.put(EventosDeConexionEnumeracion.usuarioDesconectado, texto.getString("usuario_desconectado_evento_conversacion"));
        
        // Iniciar el mapa de preferencias
        estilos = new HashMap<>();

        // Iniciar los sonidos
        try{
            sonido = AudioSystem.getClip();
            InputStream audioSrc = this.getClass().getResourceAsStream("/sounds/button-9.wav");
            InputStream bufferedIn = new BufferedInputStream(audioSrc);
            sonido.open(AudioSystem.getAudioInputStream(bufferedIn));
        }catch(LineUnavailableException | UnsupportedAudioFileException | IOException e){
            // En caso de que se produzca un error se escribe en el fichero
            // de log.
            ManejadorDeLogs mdl = ManejadorDeLogs.getManejadorDeLogs();
            mdl.escribir("No se puede iniciar el sistema de sonido");
        }
    }

    @Override
    @SuppressWarnings("SleepWhileHoldingLock")
    public void update(Observable o, Object arg) {

        boolean isPrimerMensaje = false;

        // Se comprueba si es el primer mensaje para evitar que salte una ventana
        // popup con el mensaje nuevo (Ya se ha informado al usuario del mensaje
        // con la notificación de chat).

        if(total.length() == 0) {
            isPrimerMensaje = true;
        }

        // Extraer la información
        String[] informacion = (String[]) arg;
        String usuario = informacion[0];
        String contenido = informacion[1];

        // Evitar los mensajes informando de que la sala es anónima
        if(contenido.compareTo("Sala no anónima") == 0) {
            return;
        }

        // Recuperar el usuario y la hora
        if(vc instanceof VentanaConversacionChatPrivado) {
            usuario = StringUtils.parseBareAddress(usuario);
        }

        usuario = usuarios.get(usuario);
        StringBuffer hora = this.getHora();
        
        // Recuperar el estilo
        String[] estilo;
        if(informacion.length == 2){
            estilo = estilos.get(usuario);
            if(estilo == null){
                estilo = estilosPorDefecto[actualEstiloPorDefecto];
                estilos.put(usuario,estilo);
                actualEstiloPorDefecto++;
                if(actualEstiloPorDefecto > 4) {
                    actualEstiloPorDefecto = 0;
                }
            }
        }
        else{
            estilo = new String[7];
            estilo[0] = informacion[2];
            estilo[1] = informacion[3];
            estilo[2] = informacion[4];
            estilo[3] = informacion[5];
            estilo[4] = informacion[6];
            estilo[5] = informacion[7];
            estilo[6] = informacion[8];
        }
        StringBuffer mensaje = this.getMensaje(usuario,contenido,estilo);
        mensajes.append(hora);
        mensajes.append(mensaje);
        total.delete(0,total.length());
        total.append(cabecera);
        total.append(mensajes);
        total.append(pie);

        // Escribir el texto en el panel
        SwingUtilities.invokeLater(new Runnable(){
            @Override
            public void run(){
                contenidoConversacion.setText(total.toString());
            }
        });
        
        // Activar la ventana si no tiene el foco
        if(!vc.isFocused() && !isPrimerMensaje){

            // Reproducir un sonido
            sonido.start();

            // Esperar a que finalice el sonido
            do{
                try{
                    Thread.sleep(100);
                }catch(Exception e){}
            }while(sonido.isRunning());

            // Resetear el sonido para que suene otra vez si es necesario
            sonido.setFramePosition(0);

            // Activar la ventana
            final VentanaPopup vpp = new VentanaPopupConversacion(mensaje.toString(), vc);
            SwingUtilities.invokeLater(new Runnable() {
                @Override
                public void run() {
                    // La activación de la ventana del chat privado se hace clase de la ventana popup
                    vpp.mostrarVentana();
                }
            });
        }

        // Si es un chat privado y la ventana está oculta se muestra un mensaje popup
        if(vc instanceof VentanaConversacionChatPrivado){
            VentanaConversacionChatPrivado vccp = (VentanaConversacionChatPrivado) vc;
            if(vccp.getEstado() == VentanaConversacionChatPrivado.OCULTA){
                final VentanaPopup vpp = new VentanaPopupConversacion(mensaje.toString(), vccp);
                SwingUtilities.invokeLater(new Runnable() {
                    @Override
                    public void run() {
                        // La activación de la ventana del chat privado se hace clase de la ventana popup
                        vpp.mostrarVentana();
                    }
                });
            }
        }
    }

    /**
     * Devuelve la hora actual del sistema.
     * @return La hora aactual del sistema.
     */
    private StringBuffer getHora(){

        // Recuperar el calendario y conseguir la hora
        Calendar calendario = Calendar.getInstance();
        StringBuffer hora = new StringBuffer("[");

        if(calendario.get(Calendar.HOUR_OF_DAY) < 10) {
            hora.append("0").append(calendario.get(Calendar.HOUR_OF_DAY)).append(":");
        }
        else {
            hora.append(calendario.get(Calendar.HOUR_OF_DAY)).append(":");
        }

        if(calendario.get(Calendar.MINUTE) < 10) {
            hora.append("0").append(calendario.get(Calendar.MINUTE)).append(":");
        }
        else {
            hora.append(calendario.get(Calendar.MINUTE)).append(":");
        }

        if(calendario.get(Calendar.SECOND) < 10) {
            hora.append("0").append(calendario.get(Calendar.SECOND)).append("]");
        }
        else {
            hora.append(calendario.get(Calendar.SECOND)).append("]");
        }

        return hora;
    }

    /**
     * Retorna el mensaje formateado.
     * @param usuario El usuario que ha escrito el mensaje.
     * @parama contenido El contenido del mensaje.
     * @param estilo El estilo que se debe dar al mensaje
     * @return El mensaje formateado.
     */
    private StringBuffer getMensaje(String usuario,String contenido,String[] estilo){

        // Crear la estructura del mensaje.
        String fuente = estilo[0];
        String tamaño = estilo[1];
        String rojo = estilo[2];
        String verde = estilo[3];
        String azul = estilo[4];
        boolean negrita = Boolean.parseBoolean(estilo[5]);
        boolean cursiva = Boolean.parseBoolean(estilo[6]);

        final String inicioMensaje = "<font style=\"font-family:" + fuente + ";font-size:" + tamaño + ";color:rgb(" + rojo + "," + verde + "," + azul + ")" + ((negrita) ? ";font-weight:bold" : "") + ((cursiva) ? ";font-style:italic" : "") + "\">";
        final String finMensaje = "</font><br/>";

        StringBuffer mensaje = new StringBuffer(usuario);
        mensaje.append(": ");
        mensaje.append(inicioMensaje);
        mensaje.append(contenido);
        mensaje.append(finMensaje);

        return mensaje;
    }

    /**
     * Retorna el contenido de la conversación con formato.
     * @return El contenido de la conversación con formato.
     */
    public StringBuilder getConversacion(){

        // Crear una cabecera que contenga content-type apropiado
        String cabeceraGuardado = "<html><body><head><meta http-equiv=\"Content-Type\" content=\"text/html;charset=iso-8859-1\" /></head>";

        // Construir la conversación con la nueva cabecera
        StringBuilder conversacion = new StringBuilder(cabeceraGuardado);
        conversacion.append(mensajes);
        conversacion.append(pie);
        
        return conversacion;
    }

    /**
     * Añade un usuario a la lista de usuarios de la conversación.
     * @param nombreCompleto El nombre completo del usuario.
     * @param nick El nick que va a utilizar en la conversación.
     */
    public void añadirUsuario(String nombreCompleto,String nick){
        usuarios.put(nombreCompleto,nick);
    }

    /**
     * Elimina el usuario de la lista de usuario de la conversación.
     * @param nombreCompleto El nombre completo del usuario.
     */
    public void eliminarUsuario(String nombreCompleto){
        usuarios.remove(nombreCompleto);
    }

    /**
     * Muestra un mensaje informando al usuario del evento de conversación
     * producido.
     * @param ece El evento del que se va avisar.
     * @param propiedad Una propiedad interesante para el evento.
     */
    public void notificarEventoConversacion(EventosConversacionEnumeracion ece,String propiedad){
    
        // Comprobar el tipo de evento ya actuar en consecuencia
        String evento = propiedad + " " + eventosConversacion.get(ece);
        StringBuffer eventoConFormato = this.getEvento(evento);

        this.añadirEvento(eventoConFormato);
    }

    /**
     * Muestra un mensaje informando del evento de conexión producido.
     * @param edce El evento del que se va a avisar.
     * @param propiedad Una propiedad interesante para el evento.
     */
    public void notificarEventoConexion(EventosDeConexionEnumeracion edce, String propiedad){

        // Comprobar el tipo de evento ya actuar en consecuencia
        String evento = propiedad + " " + eventosConexion.get(edce);
        StringBuffer eventoConFormato = this.getEvento(evento);

        this.añadirEvento(eventoConFormato);
    }

    /**
     * Añade un evento a la conversación que se está manteniendo.
     * @param eventoConFormato Cadena que incluye tanto información del evento,
     * como el formato que este tiene.
     */
    private void añadirEvento(StringBuffer eventoConFormato){

        // Añadir el evento a la conversación
        mensajes.append(eventoConFormato);
        total.delete(0,total.length());
        total.append(cabecera);
        total.append(mensajes);
        total.append(pie);

        // Lanzar la actualización del contenido de la aplicación en el hilo de
        // Swing de ese modo se evitan problemas cuando el evento proceda de otro
        // hilo
        SwingUtilities.invokeLater(new Runnable(){
            @Override
            public void run(){
                contenidoConversacion.setText(total.toString());
            }
        });
    }

    /**
     * Devulve el evento suministrado con el formato de presentación correcto.
     * @param evento El evento que se desea mostrar.
     * @return El evento con el formato adecuado.
     */
    private StringBuffer getEvento(String evento){

        final String inicioEvento = "<font face='arial' size='2' color='green'>";
        final String finalEvento = "</font><br/>";

        // Crear el evento con formato y asignarle su valor
        StringBuffer eventoConFormato = new StringBuffer();
        eventoConFormato.append(inicioEvento);
        eventoConFormato.append(evento);
        eventoConFormato.append(finalEvento);

        return eventoConFormato;
    }

    /**
     * Método que actualiza las preferencias del usuario actual de la conversaci
     * ón.
     * @param usuarioActual El alias que utiliza el usuario actual.
     * @param preferencias Las nuevas preferencias para el usuario.
     */
    public void actualizarPreferencias(String usuarioActual,String[] preferencias){
        estilos.put(usuarioActual,preferencias);
    }
}

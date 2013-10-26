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

package org.jajim.interfaz.listeners;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import org.jajim.controladores.ConexionControlador;
import org.jajim.controladores.ContactosControlador;
import org.jajim.controladores.ConversacionControlador;
import org.jajim.controladores.TransferenciaFicherosControlador;
import org.jajim.excepciones.ContraseñaNoDisponibleException;
import org.jajim.excepciones.ImposibleLoginException;
import org.jajim.excepciones.NoHayCuentaException;
import org.jajim.excepciones.ServidorNoEncontradoException;
import org.jajim.interfaz.dialogos.IntroducirContraseñaFormulario;
import org.jajim.interfaz.dialogos.MensajeError;
import org.jajim.interfaz.utilidades.OyenteConexion;
import org.jajim.interfaz.ventanas.VentanaPrincipal;
import org.jivesoftware.smack.Roster;

/**
 * @author Florencio Cañizal Calles
 * @version 1.2
 * Clase que oyente que se activa cuando el usuario selecciona alguna de las opcio
 * nes para conectarse con el servidor. Gestiona el intercambio de información en
 * tre la interfaz y el controlador.
 */
public class ConectarActionListener implements ActionListener{

    /**
     * Constructor de la calse. Inicializa los campos.
     */
    public ConectarActionListener(){
    }

    /**
     * Método que se ejecuta cuando el usuario selecciona una de las opciones de
     * conectar con el servidor.
     * @param e El evento que produce la ejecución del método.
     */
    @Override
    public void actionPerformed(ActionEvent e){

        // Recupero los controladores necesarios y el oyente de la conexión
        VentanaPrincipal vp = VentanaPrincipal.getInstancia();
        ConexionControlador ccn = ConexionControlador.getInstancia();
        ContactosControlador ctc = ContactosControlador.getInstancia();
        TransferenciaFicherosControlador tfc = TransferenciaFicherosControlador.getInstancia();
        OyenteConexion oc = OyenteConexion.getInstancia();

        // Invocar al controlador de las coenxiones para que realice la operación
        Roster r = null;
        try{

            // Si no se ha introducido la contraseña se busca en la información
            // de las cuentas. Si se ha introducido se pasa al controlador
            r = ccn.conectar(oc);

            // Asignar el roster al controlador de los contactos
            vp.conexionEstablecida();
            ctc.setListeners(vp.getPc(), oc);
            ctc.setContactos(r);
            ConversacionControlador.crearListener(oc);
            tfc.crearManager(oc);
        }catch(ServidorNoEncontradoException snee){
            new MensajeError(vp,"servidor_no_encontrado_error",MensajeError.ERR);
        }catch(ContraseñaNoDisponibleException cnde){
            // Crear un cuadro de diálogo para que el usuario introduzca la contra
            // seña y reintentarlo de nuevo
            String contraseña = "";
            do{
                IntroducirContraseñaFormulario ic = new IntroducirContraseñaFormulario(vp);
                contraseña = ic.getContraseña();
                // Verificar que se ha introducido una contraseña válida
                if(contraseña.compareTo("") == 0){
                    new MensajeError(vp,"campos_invalidos_error",MensajeError.WARNING);
                }
            }while(contraseña.compareTo("") == 0);

            try{
                r = ccn.conectar(contraseña, oc);
                // Asignar el roster al controlador de los contactos
                vp.conexionEstablecida();
                ctc.setListeners(vp.getPc(), oc);
                ctc.setContactos(r);
                ConversacionControlador.crearListener(oc);
                tfc.crearManager(oc);
            }catch(ServidorNoEncontradoException snee){
                new MensajeError(vp,"servidor_no_encontrado_error",MensajeError.ERR);
            }catch(ImposibleLoginException ile){
                new MensajeError(vp,"no_login_error",MensajeError.ERR);
            }
        }catch(ImposibleLoginException ile){
            new MensajeError(vp,"no_login_error",MensajeError.ERR);
        }catch(NoHayCuentaException nhce){
            new MensajeError(vp,"no_hay_cuenta_error",MensajeError.ERR);
        }
    }
}

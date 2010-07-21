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

import org.jajim.controladores.ContactosControlador;
import org.jajim.controladores.ConversacionControlador;
import org.jajim.excepciones.ImposibleRecuperarParticipanteException;
import org.jajim.controladores.TransferenciaFicherosControlador;
import org.jajim.excepciones.FicheroNoEncontradoException;
import org.jajim.excepciones.ImposibleEnviarFicheroException;
import org.jajim.interfaz.dialogos.EnviarFicheroFormulario;
import org.jajim.interfaz.dialogos.MensajeError;
import org.jajim.interfaz.ventanas.VentanaGestorDeTransferencias;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.util.List;

/**
 * @author Florencio Cañizal Calles
 * @version 1.1
 * Clase oyente que interactua con el controlador de las transferencias para en
 * viar un fichero cuando se activa el botón Aceptar del formulario de envío de
 * ficheros.
 */
public class EnviarFicheroActionListener implements ActionListener{

    private EnviarFicheroFormulario eff;
    private TransferenciaFicherosControlador tfc;
    private ConversacionControlador cvc;
    private VentanaGestorDeTransferencias vgt;

    /**
     * Constructor de la clase. Inicializa las variables necesarias.
     * @param eff El formulario de envío de ficheros.
     * @param tfc El controlador de transferencia de ficheros.
     * @param vgt La ventana del gestor de transferencias.
     */
    public EnviarFicheroActionListener(EnviarFicheroFormulario eff,TransferenciaFicherosControlador tfc,ConversacionControlador cvc,VentanaGestorDeTransferencias vgt){
        this.eff = eff;
        this.tfc = tfc;
        this.cvc = cvc;
        this.vgt = vgt;
    }

    /**
     * Método que se ejecuta al pulsar el botón de Aceptar del formulario de en
     * vío de ficheros. Interactúa con el controlador correspondiente para reali
     * zar el envío del fichero.
     * @param e El evento que genera la ejecución del método.
     */
    @Override
    public void actionPerformed(ActionEvent e) {

        // Recuperar los campos del formulario
        List<String> campos = eff.getCampos();

        String ruta = campos.get(0);
        String descripcion = campos.get(1);
        String[] contactos = new String[campos.size() - 2];

        for(int i = 2;i < campos.size();i++){
            contactos[i - 2] = campos.get(i);
        }

        // Comprobar que los campos introducidos sean correctos
        if(ruta.compareTo("") == 0 || contactos.length == 0){
            new MensajeError(eff,"campos_invalidos_error",MensajeError.WARNING);
            return;
        }

        // Llamar al controlador para que realice la operación
        try{
            String id = tfc.enviarFichero(cvc,contactos,ruta,descripcion);
            int posicion = ruta.lastIndexOf(File.separator);
            String nombre = ruta.substring(posicion + 1);
            vgt.añadirTransferenciaDeFichero(nombre,id,VentanaGestorDeTransferencias.EMISOR);
            //Cerrar el cuadro de diálogo
            eff.dispose();
        }catch(FicheroNoEncontradoException fnee){
            new MensajeError(eff,"fichero_no_encontrado_error",MensajeError.WARNING);
        }catch(ImposibleEnviarFicheroException iefe){
            new MensajeError(eff,"imposible_enviar_fichero_error",MensajeError.ERR);
            //Cerrar el cuadro de diálogo
            eff.dispose();
        }catch(ImposibleRecuperarParticipanteException irpe){
            new MensajeError(eff,"imposible_recuperar_participantes_error",MensajeError.ERR);
            //Cerrar el cuadro de diálogo
            eff.dispose();
        }
    }
}

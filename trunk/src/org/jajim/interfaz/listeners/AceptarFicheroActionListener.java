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
import org.jajim.controladores.TransferenciaFicherosControlador;
import org.jajim.excepciones.ImposibleRecibirFicheroException;
import org.jajim.excepciones.RutaNoDisponibleException;
import org.jajim.interfaz.dialogos.AceptarFicheroFormulario;
import org.jajim.interfaz.dialogos.MensajeError;
import org.jajim.interfaz.ventanas.VentanaGestorDeTransferencias;

/**
 * @author Florencio Cañizal Calles
 * @version 1.1
 * Clase oyente que se activa cuando el usuario decide aceptar una tansferencia
 * de fichero.
 */
public class AceptarFicheroActionListener implements ActionListener{

    private AceptarFicheroFormulario aff;
    private VentanaGestorDeTransferencias vgt;

    /**
     * Constructor de la clase. Inicializa las variables necesarias.
     * @param aff El formulario de aceptación del fichero.
     * @param vgt La ventana del gestor de cuentas.
     */
    public AceptarFicheroActionListener(AceptarFicheroFormulario aff,VentanaGestorDeTransferencias vgt){
        this.aff = aff;
        this.vgt = vgt;
    }

    /**
     * Método que se ejecuta cuando el usuario selecciona la opción de aceptar
     * fichero. Extrae la información necesaria y llama al controlador de trans
     * ferencias para llevar a cabo la operación.
     * @param e Evento que produce la activación del método.
     */
    @Override
    public void actionPerformed(ActionEvent e){

        // Recuperar los campos del formulario
        String[] campos = aff.getCampos();
        int idTransferencia = Integer.parseInt(campos[0]);
        String ruta = campos[1];

        if(ruta.compareTo("") == 0){
            new MensajeError(aff,"campos_invalidos_error",MensajeError.WARNING);
            return;
        }

        // LLamar al controlador de transferencia para que lleve a cabo la opera
        //ción
        try{
            TransferenciaFicherosControlador tfc = TransferenciaFicherosControlador.getInstancia();
            String[] valores = tfc.aceptarFichero(idTransferencia,ruta);
            aff.dispose();
            vgt.añadirTransferenciaDeFichero(valores[0],valores[1],VentanaGestorDeTransferencias.RECEPTOR);
        }catch(RutaNoDisponibleException rnde){
            new MensajeError(aff,"ruta_no_disponible_error",MensajeError.WARNING);
        }catch(ImposibleRecibirFicheroException irfe){
            new MensajeError(aff,"imposible_recibir_fichero_error",MensajeError.ERR);
            aff.dispose();
        }
    }
}

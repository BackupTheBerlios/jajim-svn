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

import org.jajim.controladores.CuentaControlador;
import org.jajim.excepciones.ImposibleCrearCuentaException;
import org.jajim.excepciones.ServidorNoEncontradoException;
import org.jajim.interfaz.dialogos.CrearCuentaFormulario;
import org.jajim.interfaz.dialogos.MensajeError;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;
import org.jajim.excepciones.CuentaExistenteException;

/**
 * @author Florencio Cañizal Calles
 * @version 1.1
 * Clase que gestiona como se lleva a cabo la creación de la cuenta.
 */
public class CrearCuentaActionListener implements ActionListener{

    private CrearCuentaFormulario ccf;

    /**
     * Constructor de la clase. Inicialia los elementos necesarios
     * @param ccf Cuadro de diálogo desde el que se invocó el oyente.
     */
    public CrearCuentaActionListener(CrearCuentaFormulario ccf){
        this.ccf = ccf;
    }

    /**
     * Método que se ejecuta cuando se produce el evento para el que ha sido re
     * gistrado. Crea una cuenta de usuario en un servidor.
     * @param e Evento que ha producido la ejecución del método.
     */
    @Override
    public void actionPerformed(ActionEvent e){
        
        // Recuperar el valor de los campos del formulario
        List<String> campos = ccf.getCampos();
        String identificador = campos.get(0);
        String servidor = campos.get(1);
        String contraseña = campos.get(2);
        String confirmarContraseña = campos.get(3);
        boolean guardarContraseña = Boolean.parseBoolean(campos.get(4));

        if(identificador.compareTo("") == 0 || servidor.compareTo("") == 0 || contraseña.compareTo("") == 0 || confirmarContraseña.compareTo("") == 0){
            new MensajeError(ccf,"campos_invalidos_error",MensajeError.WARNING);
            return;
        }

        // Comprobar si la contraseña y su confirmación coinciden
        if(contraseña.compareTo(confirmarContraseña) != 0){
            // Mostrar mensaje de error y salir
            new MensajeError(ccf,"confirmacion_no_valida_error",MensajeError.WARNING);
            return;
        }

        // LLamar al controlador de las cuentas para que gestione la realización
        // de la operación.
        try{
            CuentaControlador cc = CuentaControlador.getInstancia();
            boolean activa = cc.crearCuenta(identificador, servidor, contraseña, guardarContraseña);
            // Si la cuenta se establece como la activa se modifica la etiqueta
            // de la ventana principal
            if(activa)
                ccf.getVp().cambiarCuenta(identificador + "@" + servidor);
            if(ccf.getVgc() != null)
                ccf.getVgc().añadirCuentas();
            ccf.dispose();
        }catch(ServidorNoEncontradoException snee){
            new MensajeError(ccf,"servidor_no_encontrado_error",MensajeError.ERR);
        }catch(ImposibleCrearCuentaException icce){
            new MensajeError(ccf,"imposible_crear_cuenta_error",MensajeError.ERR);
        }catch(CuentaExistenteException cee){
            new MensajeError(ccf, "cuenta_existente_error", MensajeError.WARNING);
        }
    }
}

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
import java.util.List;
import org.jajim.controladores.CuentaControlador;
import org.jajim.excepciones.CuentaExistenteException;
import org.jajim.excepciones.ImposibleValidarCuentaException;
import org.jajim.excepciones.ServidorNoEncontradoException;
import org.jajim.interfaz.dialogos.AñadirCuentaFormulario;
import org.jajim.interfaz.dialogos.MensajeError;
import org.jajim.interfaz.ventanas.VentanaPrincipal;

/**
 * @author Florencio Cañizal Calles
 * @version 1.2
 * Clase que gestiona la acción de añadir una cuenta ya existente en el sistema.
 */
public class AñadirCuentaActionListener implements ActionListener{

    private AñadirCuentaFormulario acf;

    /**
     * Constructor de la clase. Iniciliza los elementos necesarios
     * @param acf El formulario de adición de cuentas
     */
    public AñadirCuentaActionListener(AñadirCuentaFormulario acf){
        this.acf = acf;
    }

    /**
     * Método que se ejecuta cuando se produce el evento que invita a añadir la
     * nueva cuenta.
     * @param e Evento que provoca la ejecución del método.
     */
    @Override
    public void actionPerformed(ActionEvent e){

        // Recuperar el valor de los campos del formulario
        List<String> campos = acf.getCampos();
        String identificador = campos.get(0);
        String servidor = campos.get(1);
        String contraseña = campos.get(2);
        String confirmarContraseña = campos.get(3);
        boolean guardarContraseña = Boolean.parseBoolean(campos.get(4));

        if(identificador.compareTo("") == 0 || servidor.compareTo("") == 0 || contraseña.compareTo("") == 0 || confirmarContraseña.compareTo("") == 0){
            new MensajeError(acf,"campos_invalidos_error",MensajeError.WARNING);
            return;
        }

        // Comprobar si la contraseña y su confirmación coinciden
        if(contraseña.compareTo(confirmarContraseña) != 0){
            // Mostrar mensaje de error y salir
            new MensajeError(acf,"confirmacion_no_valida_error",MensajeError.WARNING);
            return;
        }


        // Llamar al controlador para que gestione la operación de añadir la cuen
        // ta.
        try{
            CuentaControlador cc = CuentaControlador.getInstancia();
            boolean activa = cc.añadirCuenta(identificador,servidor,contraseña,guardarContraseña);
            // Si la cuenta se establece como la activa se modifica la etiqueta
            // de la ventana principal
            if(activa) {
                VentanaPrincipal.getInstancia().cambiarCuenta(identificador + "@" + servidor);
            }
            if(acf.getVgc() != null){
                acf.getVgc().añadirCuentas();
            }
            acf.dispose();
        }catch(ServidorNoEncontradoException snee){
            new MensajeError(acf,"servidor_no_encontrado_error",MensajeError.ERR);
        }catch(ImposibleValidarCuentaException ivce){
            new MensajeError(acf,"cuenta_no_valida",MensajeError.ERR);
        }catch(CuentaExistenteException cee){
            new MensajeError(acf, "cuenta_existente_error", MensajeError.WARNING);
        }
    }
}

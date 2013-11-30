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
import java.util.ResourceBundle;
import org.jajim.controladores.ConexionControlador;
import org.jajim.controladores.CuentaControlador;
import org.jajim.excepciones.ContraseñaNoDisponibleException;
import org.jajim.excepciones.ImposibleEliminarCuentaException;
import org.jajim.excepciones.ImposibleLoginException;
import org.jajim.excepciones.ServidorNoEncontradoException;
import org.jajim.interfaz.dialogos.IntroducirContraseñaFormulario;
import org.jajim.interfaz.dialogos.MensajeError;
import org.jajim.interfaz.ventanas.VentanaGestorDeCuentas;
import org.jajim.interfaz.ventanas.VentanaPrincipal;
import org.jajim.main.Main;

/**
 * @author Florencio Cañizal Calles
 * @version 1.2 Oyente que recibe la petición de eliminar la cuenta seleccionada del servidor en el que se encuentra
 * almacenada.
 */
public class EliminarCuentaServidorActionListener implements ActionListener {

    private final ResourceBundle texto = ResourceBundle.getBundle("resources.Idioma", Main.loc);

    /**
     * Constructor de la clase. Iniciliza las variables necesarias.
     */
    public EliminarCuentaServidorActionListener() {
    }

    /**
     * Método que se ejecuta cuando se selecciona la opción de eliminar cuenta del servidor. Interactúa con el
     * controlador de las cuentas para llevar a cabo la operación.
     * <p>
     * @param e El evento que produce la ejecución del método.
     */
    @Override
    public void actionPerformed(ActionEvent e) {

        VentanaPrincipal vp = VentanaPrincipal.getInstancia();
        VentanaGestorDeCuentas vgc = VentanaGestorDeCuentas.getInstancia();

        // Recuperar la información de la cuenta
        String[] cuenta = vgc.getCuenta();

        // Si devuelve null, no hay cuentas y se cierra la ejecución del método
        if (cuenta == null) {
            return;
        }

        // Extraer los valores
        String identificador = cuenta[0];
        String servidor = cuenta[1];

        // Comprobar si se va a eliminar la cuenta activa y estamos conectados.
        CuentaControlador cc = CuentaControlador.getInstancia();
        String activa = cc.getCuenta();
        ConexionControlador cnc = ConexionControlador.getInstancia();
        if (activa.compareTo(identificador + "@" + servidor) == 0 && cnc.isConectado()) {
            // Abortar la conexión antes de borrar la cuenta
            AbortarOperaciones ao = new AbortarOperaciones(vgc);
            if (!ao.abortarConexion()) {
                return;
            }
        }

        // Llamar al controlador para que realice la operación
        try {
            cc.eliminarCuentaServidor(identificador, servidor);
        }
        catch (ContraseñaNoDisponibleException cnde) {
            // Crear un cuadro de diálogo para que el usuario introduzca la contra
            // seña y reintentarlo de nuevo
            IntroducirContraseñaFormulario ic = new IntroducirContraseñaFormulario(vgc);
            String contraseña = ic.getContraseña();

            try {
                cc.eliminarCuentaServidor(identificador, servidor, contraseña);
            }
            catch (ServidorNoEncontradoException snee) {
                new MensajeError(vgc, "servidor_no_encontrado_error", MensajeError.ERR);
            }
            catch (ImposibleLoginException ile) {
                new MensajeError(vgc, "no_login_error", MensajeError.ERR);
            }
            catch (ImposibleEliminarCuentaException iecs) {
                new MensajeError(vgc, "imposible_eliminar_cuenta_error", MensajeError.ERR);
            }
        }
        catch (ServidorNoEncontradoException snee) {
            new MensajeError(vgc, "servidor_no_encontrado_error", MensajeError.ERR);
        }
        catch (ImposibleLoginException ile) {
            new MensajeError(vgc, "no_login_error", MensajeError.ERR);
        }
        catch (ImposibleEliminarCuentaException iecs) {
            new MensajeError(vgc, "imposible_eliminar_cuenta_error", MensajeError.ERR);
        }

        // Actualiza la lista del gestor
        vgc.añadirCuentas();
        activa = cc.getCuenta();
        vp.cambiarCuenta(activa);
    }
}

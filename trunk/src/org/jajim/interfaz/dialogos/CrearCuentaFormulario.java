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

package org.jajim.interfaz.dialogos;

import java.util.ResourceBundle;
import javax.swing.JFrame;
import org.jajim.interfaz.listeners.CrearCuentaActionListener;
import org.jajim.main.Main;

/**
 * @author Florencio Cañizal Calles
 * @version 1.2
 * La clase hereda de CuentaFormulario, añadiendo las operaciones especiales para
 * poder crear una nueva cuenta en un servidor.
 */
public class CrearCuentaFormulario extends CuentaFormulario{

    private ResourceBundle texto = ResourceBundle.getBundle("resources.Idioma",Main.loc);

    /**
     * Constructor de la clase. Gestiona la funcionalidad propia del formulario
     * de creación de cuentas.
     * @param ventana La ventana que abre el formulario.
     */
    public CrearCuentaFormulario(JFrame ventana){

        // Inicializar el diálogo
        super(ventana);
        this.inicializar();
    }

    /**
     * Inicializa los elementos de la interfaz necesarios
     */
    private void inicializar(){
        // Asignar el título
        this.setTitle(texto.getString("crear_cuenta_formulario"));

        // Asignar un oyente al botón de Aceptar
        botonAceptar.addActionListener(new CrearCuentaActionListener(this));
        this.setVisible(true);
    }
}

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
import org.jajim.interfaz.listeners.AñadirCuentaActionListener;
import org.jajim.interfaz.ventanas.VentanaGestorDeCuentas;
import org.jajim.interfaz.ventanas.VentanaPrincipal;
import org.jajim.main.Main;

/**
 * @author Florencio Cañizal Calles
 * @version 1.1
 * La clase hereda de CuentaFormulario, añadiendo las operaciones especiales para
 * poder añadir una nueva cuenta al sistema. Dicha cuenta ya se encuentra registra
 * da en un servidor
 */
public class AñadirCuentaFormulario extends CuentaFormulario{

    private ResourceBundle texto = ResourceBundle.getBundle("resources.Idioma",Main.loc);

    private VentanaPrincipal vp;
    private VentanaGestorDeCuentas vgc;

    /**
     * Constructor de la clase. Gestiona la funcionalidad propia del formulario de
     * adición de una nueva cuenta.
     * @param vp Ventana principal de la que depende el formulario.
     */
    public AñadirCuentaFormulario(VentanaPrincipal vp){

        // Inicialización
        super(vp);
        this.vp = vp;
        this.vgc = null;
        this.inicializar();
    }
    
    /**
     * Constructor de la clase. Gestiona la funcionalidad propia del formulario de
     * adición de una nueva cuenta.
     * @param vp Ventana principal de la que depende el formulario.
     */
    public AñadirCuentaFormulario(VentanaPrincipal vp,VentanaGestorDeCuentas vgc){

        // Inicialización
        super(vgc);
        this.vp = vp;
        this.vgc = vgc;
        this.inicializar();
    }

    /**
     * Inicializa los elementos de la interfaz necesarios
     */
    private void inicializar(){
        
        // Asignar el título a la ventana
        this.setTitle(texto.getString("añadir_cuenta_formulario"));

        // Asignar un oyente al botón de Aceptar
        botonAceptar.addActionListener(new AñadirCuentaActionListener(this));
        this.setVisible(true);
    }

    /**
     * Devuelve la ventana principal de la aplicación para propósitos de presenta
     * ción.
     * @return La ventana principal de la aplicación.
     */
    public VentanaPrincipal getVp(){
        return vp;
    }

    /**
     * Devuelve el gestor de cuentas de la aplicación para propósitos de presenta
     * ción.
     * @return El gestor de cuentas de la aplicación.
     */
    public VentanaGestorDeCuentas getVgc(){
        return vgc;
    }
}

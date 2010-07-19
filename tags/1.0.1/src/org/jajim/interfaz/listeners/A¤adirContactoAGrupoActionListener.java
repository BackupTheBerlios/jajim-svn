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
import org.jajim.excepciones.ImposibleAñadirContactoAGrupoException;
import org.jajim.interfaz.dialogos.AñadirContactoAGrupoFormulario;
import org.jajim.interfaz.dialogos.MensajeError;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * @author Florencio Cañizal Calles
 * @version 1.0.1
 * Clase oyente que escucha los eventos de adición de usuarios a grupos proveni
 * entes del formulario correspondiente.
 */
public class AñadirContactoAGrupoActionListener implements ActionListener{

    private AñadirContactoAGrupoFormulario acagf;
    private ContactosControlador ctc;
    private String contacto;

    /**
     * Constructor de la clase. Inicializa las variables necesarias.
     * @param acagf El formulario de adición de contactos.
     * @param ctc El controlador de contactos.
     * @param contacto El contacto que se va a añadir a los grupos correspondien
     * tes.
     */
    public AñadirContactoAGrupoActionListener(AñadirContactoAGrupoFormulario acagf,ContactosControlador ctc,String contacto){
        this.acagf = acagf;
        this.ctc = ctc;
        this.contacto = contacto;
    }

    /**
     * Método que se ejecuta cuando se selecciona la opción Aceptar del formulario
     * de adición de contactos. Intenta añadir el contacto a la lista de grupos
     * proporcionada en colaboración con el controlador de contactos.
     * @param e El evento que produce la ejecución del método.
     */
    @Override
    public void actionPerformed(ActionEvent e) {

        // Recuperar los datos del formulario e imprimir error si son incorrectos
        String[] grupos = acagf.getCampos();

        // Comprobar si los campos son inválidos
        if(grupos == null || grupos.length == 0){
            new MensajeError(acagf,"campos_invalidos_error",MensajeError.WARNING);
            return;
        }

        // Añadir el contacto a cada uno de los grupos especificados
        for(int i = 0;i < grupos.length;i++){
            try{
                ctc.añadirContactoAGrupo(contacto,grupos[i]);
            }catch(ImposibleAñadirContactoAGrupoException iacage){
                new MensajeError(acagf,"imposible_añadir_contacto_a_grupo",MensajeError.ERR);
            }
        }

        acagf.dispose();
    }
}

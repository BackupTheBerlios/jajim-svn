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

package org.jajim.interfaz.ventanas;

import java.awt.Image;
import java.awt.Toolkit;
import java.io.IOException;
import java.io.InputStream;
import java.util.ResourceBundle;
import javax.swing.JFrame;
import javax.swing.JScrollPane;
import javax.swing.JTextPane;
import javax.swing.event.HyperlinkEvent;
import javax.swing.event.HyperlinkListener;
import org.jajim.excepciones.ImposibleRecuperarAyudaException;
import org.jajim.main.Main;

/**
 * @author Florencio Cañizal Calles
 * @version 1.1
 * La ventana con la ayuda de la aplicación.
 */
public class VentanaAyuda extends JFrame implements HyperlinkListener{

    private ResourceBundle texto = ResourceBundle.getBundle("resources.Idioma",Main.loc);

    private JTextPane contenidoAyuda;

    /**
     * Constructor de la clase. Inicializa las variables necesarias y crea la inter
     * faz de usuario.
     * @param vp La ventana principal de la aplicación.
     */
    public VentanaAyuda(VentanaPrincipal vp) throws ImposibleRecuperarAyudaException{

        // Iniciar la interfaz
        contenidoAyuda = new JTextPane();
        contenidoAyuda.setEditable(false);
        contenidoAyuda.setContentType("text/html");
        contenidoAyuda.addHyperlinkListener(this);
        JScrollPane jsp = new JScrollPane(contenidoAyuda);
        InputStream is = null;

        if(Main.loc.toString().contains("es")){
            is = this.getClass().getResourceAsStream("/ayuda/espanol/introduccion.htm");
        }
        else{
            is = this.getClass().getResourceAsStream("/ayuda/ingles/introduction.htm");
        }

        String totalFichero = "";
        byte[] bufferDeLectura = new byte[1024];
        try{
            while(is.read(bufferDeLectura) != -1){
                totalFichero = totalFichero + new String(bufferDeLectura);
            }
        }catch(IOException e){
            throw new ImposibleRecuperarAyudaException();
        }

        contenidoAyuda.setText(totalFichero);

        this.add(jsp);

        Image image = Toolkit.getDefaultToolkit().getImage(ClassLoader.getSystemResource("icons/ayuda.png"));
        this.setTitle(texto.getString("ventana_ayuda_title"));
        this.setIconImage(image);
        this.setSize(800,600);
        this.setVisible(true);
        this.setLocationRelativeTo(vp);
        this.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
    }

    @Override
    public void hyperlinkUpdate(HyperlinkEvent e) {
        if(e.getEventType() == HyperlinkEvent.EventType.ACTIVATED){
            try{
                 InputStream is;
                if(Main.loc.toString().contains("es")) {
                    is = this.getClass().getResourceAsStream("/ayuda/espanol/" + e.getDescription());
                }
                else {
                    is = this.getClass().getResourceAsStream("/ayuda/ingles/" + e.getDescription());
                }
                String totalFichero = "";
                byte[] bufferDeLectura = new byte[1024];
                while(is.read(bufferDeLectura) != -1){
                    totalFichero = totalFichero + new String(bufferDeLectura);
                }
                contenidoAyuda.setText(totalFichero);
            }catch(IOException ioe){
            }
        }
    }
}

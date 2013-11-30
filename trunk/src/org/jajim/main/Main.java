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
package org.jajim.main;

import java.util.Locale;
import java.util.ResourceBundle;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;
import org.jajim.interfaz.ventanas.VentanaPrincipal;
import org.pushingpixels.substance.api.SubstanceLookAndFeel;
import org.pushingpixels.substance.api.skin.SubstanceMistSilverLookAndFeel;
import org.pushingpixels.substance.internal.fonts.FontPolicies;

/**
 * @author Florencio Cañizal Calles
 * @version 1.2 Clase principal inicializa los objetos necesarios para la ejecución de la apli cación.
 */
public class Main {

    public static Locale loc = Locale.getDefault();

    /**
     * Método principal de la clase. Es el primer método que se ejecuta de la apli cación.
     * <p>
     * @param args Argumentos pasados por la línea de comandos.
     */
    public static void main(String[] args) {
        //XMPPConnection.DEBUG_ENABLED = true;

        // Si no existe un archivo de idioma, utilizar el de inglés
        if (loc.toString().contains("es")) {
            loc = new Locale("es");
        }
        else {
            loc = new Locale("en");
        }

        try {
            UIManager.setLookAndFeel(new SubstanceMistSilverLookAndFeel());
            SubstanceLookAndFeel.setFontPolicy(FontPolicies.getLooks1xPlasticPolicy());
        }
        catch (UnsupportedLookAndFeelException e) {
        }

        // Cargar algunas etiquetas para los JFileChooser porque no tienen locali
        // zación.
        ResourceBundle texto = ResourceBundle.getBundle("resources.Idioma", Main.loc);

        UIManager.put("FileChooser.saveButtonText", texto.getString("FileChooser.saveButtonText"));
        UIManager.put("FileChooser.openButtonText", texto.getString("FileChooser.openButtonText"));
        UIManager.put("FileChooser.cancelButtonText", texto.getString("FileChooser.cancelButtonText"));
        UIManager.put("FileChooser.lookInLabelText", texto.getString("FileChooser.lookInLabelText"));
        UIManager.put("FileChooser.upFolderToolTipText", texto.getString("FileChooser.upFolderToolTipText"));
        UIManager.put("FileChooser.homeFolderToolTipText", texto.getString("FileChooser.homeFolderToolTipText"));
        UIManager.put("FileChooser.newFolderToolTipText", texto.getString("FileChooser.newFolderToolTipText"));
        UIManager.put("FileChooser.listViewButtonToolTipText", texto.getString("FileChooser.listViewButtonToolTipText"));
        UIManager.put("FileChooser.detailsViewButtonToolTipText", texto.getString(
            "FileChooser.detailsViewButtonToolTipText"));
        UIManager.put("FileChooser.filesOfTypeLabelText", texto.getString("FileChooser.filesOfTypeLabelText"));
        UIManager.put("FileChooser.fileNameLabelText", texto.getString("FileChooser.fileNameLabelText"));

        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                VentanaPrincipal.getInstancia();
            }
        });
    }
}

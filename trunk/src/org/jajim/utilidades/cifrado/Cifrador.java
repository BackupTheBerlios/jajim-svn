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

package org.jajim.utilidades.cifrado;

import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.DESKeySpec;
import org.jajim.excepciones.ImposibleCifrarDescifrarException;
import org.jajim.excepciones.ImposibleCifrarException;
import org.jajim.excepciones.ImposibleDescifrarException;
import org.jajim.utilidades.log.ManejadorDeLogs;
import sun.misc.BASE64Decoder;
import sun.misc.BASE64Encoder;

/**
 * @author Florencio Cañizal Calles
 * @version 1.1
 * Clase que cifra y descrifa cadenas de caracteres.
 */
public class Cifrador{

    private SecretKey sk;
    private Cipher cifrado;

    /**
     * Constructor de la clase. Inicializa el cifrador.
     * @throws ImposibleCifrarDescifrarException Si no se puede instanciar un ci
     * frador adecuado.
     */
    public Cifrador() throws ImposibleCifrarDescifrarException{

        try{
            // Inicialización de la clave
            SecretKeyFactory skf = SecretKeyFactory.getInstance("DES");
            String clave = "cjliamve";
            DESKeySpec kspec = new DESKeySpec(clave.getBytes());
            sk = skf.generateSecret(kspec);
            // Inicialización del cifrador
            cifrado = Cipher.getInstance("DES");
        }catch(NoSuchAlgorithmException | InvalidKeyException | InvalidKeySpecException | NoSuchPaddingException e){
            // En caso de que se produzca un error se escribe en el fichero
            // de log y se lanza una excepción
            ManejadorDeLogs mdl = ManejadorDeLogs.getManejadorDeLogs();
            mdl.escribir("No se puede crear un cifrador de DES");
            throw new ImposibleCifrarDescifrarException();
        }
    }

    /**
     * Cifra la cadena que se le suministra como parámetro.
     * @param cadena Cadena que se desea cifrar.
     * @return Resultado de la cadena cifrada.
     * @throws ImposibleCifrarException Si no se puede cifrar la cadena.
     */
    public String cifrar(String cadena) throws ImposibleCifrarException{

        byte[] bloque_cifrado = null;
        String c = null;

        try{
            // Arrancar en modo cifrar
            cifrado.init(Cipher.ENCRYPT_MODE,sk);
            // Cifrar
            bloque_cifrado = cifrado.doFinal(cadena.getBytes(),0,cadena.getBytes().length);
            c = new BASE64Encoder().encode(bloque_cifrado);
        }catch(InvalidKeyException | IllegalBlockSizeException | BadPaddingException e){
            // En caso de que se produzca un error se escribe en el fichero
            // de log y se lanza una excepción
            ManejadorDeLogs mdl = ManejadorDeLogs.getManejadorDeLogs();
            mdl.escribir("No se puede cifrar la cadena");
            throw new ImposibleCifrarException();
        }


        return c;
    }

    /**
     * Descrifra la cadena que se le suministra como parámetro.
     * @param cadena Cadena que se desea descrifrar.
     * @return Resultado con la cadena descifrada.
     * @throws Si no se puede descifrar la cadena.
     */
    public String descifrar(String cadena) throws ImposibleDescifrarException{

        byte[] bloque_descifrado = null;
        String c = null;

        try{
            // Arrancar en modo descifrar
            cifrado.init(Cipher.DECRYPT_MODE,sk);
            // Descifrar
            bloque_descifrado = cifrado.doFinal(new BASE64Decoder().decodeBuffer(cadena));
            c = new String(bloque_descifrado,"ISO-8859-1");
        }catch(InvalidKeyException | IOException | IllegalBlockSizeException | BadPaddingException e){
            // En caso de que se produzca un error se escribe en el fichero
            // de log y se lanza una excepción
            ManejadorDeLogs mdl = ManejadorDeLogs.getManejadorDeLogs();
            mdl.escribir("No se puede descifrar la cadena");
            throw new ImposibleDescifrarException();
        }

        return c;
    }
}

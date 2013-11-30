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
package org.jajim.controladores;

import java.util.prefs.Preferences;

/**
 * @author Florencio Cañizal Calles
 * @version 1.2 Clase que gestiona el guardado y utilización de las preferencias del usuario. Mantiene información de la
 * posición de todas las ventanas y del tamaño de la ventana principal y de la ventana de la conversación. También
 * contiene informa ción acerca del estilo de los mensajes utilizados por el usuario.
 */
public class PreferenciasControlador {

    // Instancia de la clase
    private static PreferenciasControlador instancia;

    // Valores a almacenar
    // Ventana principal
    private boolean ventanaPrincipalMaximizada;
    private int ventanaPrincipalX;
    private int ventanaPrincipalY;
    private int ventanaPrincipalAncho;
    private int ventanaPrincipalLargo;
    // Gestor de cuentas
    private int gestorDeCuentasX;
    private int gestorDeCuentasY;
    // Gestor de transferencias
    private int gestorDeTransferenciasX;
    private int gestorDeTransferenciasY;
    // Ventana de la conversación
    private boolean ventanaConversacionMaximizada;
    private int ventanaConversacionX;
    private int ventanaConversacionY;
    private int ventanaConversacionAncho;
    private int ventanaConversacionLargo;
    // Estilo de mensajes
    private String fuente;
    private int tamaño;
    private boolean negrita;
    private boolean cursiva;
    private int colorRojo;
    private int colorVerde;
    private int colorAzul;

    // Preferencias
    private Preferences p;

    /**
     * Constructor de la clase. Inicializa las variables necesarias.
     */
    private PreferenciasControlador() {

        // Recuperar las preferencias de aquellas alamacenadas en el sistema
        p = Preferences.userNodeForPackage(this.getClass());
        p = p.node("/JAJIM");

        // Cargar las preferencias del usuario
        ventanaPrincipalMaximizada = p.getBoolean("ventanaPrincipalMaximizada", false);
        ventanaPrincipalX = p.getInt("ventanaPrincipalX", 60);
        ventanaPrincipalY = p.getInt("ventanaPrincipalY", 60);
        ventanaPrincipalAncho = p.getInt("ventanaPrincipalAncho", 200);
        ventanaPrincipalLargo = p.getInt("ventanaPrincipalLargo", 420);
        gestorDeCuentasX = p.getInt("gestorDeCuentasX", 80);
        gestorDeCuentasY = p.getInt("gestorDeCuentasY", 80);
        gestorDeTransferenciasX = p.getInt("gestorDeTransferenciasX", 80);
        gestorDeTransferenciasY = p.getInt("gestorDeTransferenciasY", 80);
        ventanaConversacionMaximizada = p.getBoolean("ventanaConversacionMaximizada", false);
        ventanaConversacionX = p.getInt("ventanaConversacionX", 80);
        ventanaConversacionY = p.getInt("ventanaConversacionY", 80);
        ventanaConversacionAncho = p.getInt("ventanaConversacionAncho", 400);
        ventanaConversacionLargo = p.getInt("ventanaConversacionLargo", 550);
        fuente = p.get("fuente", "serif");
        tamaño = p.getInt("tamaño", 10);
        negrita = p.getBoolean("negrita", false);
        cursiva = p.getBoolean("cursiva", false);
        colorRojo = p.getInt("colorRojo", 0);
        colorVerde = p.getInt("colorVerde", 0);
        colorAzul = p.getInt("colorAzul", 0);
    }

    /**
     * Método que guarda las preferencias del sistema.
     */
    public void guardarPreferencias() {

        // Guardar las preferencias en el sistema
        p.putBoolean("ventanaPrincipalMaximizada", ventanaPrincipalMaximizada);
        p.putInt("ventanaPrincipalX", ventanaPrincipalX);
        p.putInt("ventanaPrincipalY", ventanaPrincipalY);
        p.putInt("ventanaPrincipalAncho", ventanaPrincipalAncho);
        p.putInt("ventanaPrincipalLargo", ventanaPrincipalLargo);
        p.putInt("gestorDeCuentasX", gestorDeCuentasX);
        p.putInt("gestorDeCuentasY", gestorDeCuentasY);
        p.putInt("gestorDeTransferenciasX", gestorDeTransferenciasX);
        p.putInt("gestorDeTransferenciasY", gestorDeTransferenciasY);
        p.putBoolean("ventanaConversacionMaximizada", ventanaConversacionMaximizada);
        p.putInt("ventanaConversacionX", ventanaConversacionX);
        p.putInt("ventanaConversacionY", ventanaConversacionY);
        p.putInt("ventanaConversacionAncho", ventanaConversacionAncho);
        p.putInt("ventanaConversacionLargo", ventanaConversacionLargo);
        p.put("fuente", fuente);
        p.putInt("tamaño", tamaño);
        p.putBoolean("negrita", negrita);
        p.putBoolean("cursiva", cursiva);
        p.putInt("colorRojo", colorRojo);
        p.putInt("colorVerde", colorVerde);
        p.putInt("colorAzul", colorAzul);
    }

    /**
     * Retorna el valor del atributo ventanaPrincipalMaximizada.
     * <p>
     * @return El valor del atributo ventanaPrincipalMaximizada.
     */
    public boolean isVentanaPrincipalMaximizada() {
        return ventanaPrincipalMaximizada;
    }

    /**
     * Actualiza el valor del atributo ventanaPrincipalMaximizada.
     * <p>
     * @param ventanaPrincipalMaximizada El nuevo valor de ventanaPrincipalMaximizada.
     */
    public void setVentanaPrincipalMaximizada(boolean ventanaPrincipalMaximizada) {
        this.ventanaPrincipalMaximizada = ventanaPrincipalMaximizada;
    }

    /**
     * Retorna el valor del atributo ventanaPrincipalX.
     * <p>
     * @return El valor del atributo ventanaPrincipalX.
     */
    public int getVentanaPrincipalX() {
        return ventanaPrincipalX;
    }

    /**
     * Actualiza el valor del atributo ventanaPrincipalX.
     * <p>
     * @param ventanaPrincipalX El nuevo valor de ventanaPrincipalX.
     */
    public void setVentanaPrincipalX(int ventanaPrincipalX) {
        this.ventanaPrincipalX = ventanaPrincipalX;
    }

    /**
     * Retorna el valor del atributo ventanaPrincipalY.
     * <p>
     * @return El valor del atributo ventanaPrincipalY.
     */
    public int getVentanaPrincipalY() {
        return ventanaPrincipalY;
    }

    /**
     * Actualiza el valor del atributo ventanaPrincipalY.
     * <p>
     * @param ventanaPrincipalY El nuevo valor de ventanaPrincipalY.
     */
    public void setVentanaPrincipalY(int ventanaPrincipalY) {
        this.ventanaPrincipalY = ventanaPrincipalY;
    }

    /**
     * Retorna el valor del atributo ventanaPrincipalAncho.
     * <p>
     * @return El valor del atributo ventanaPrincipalAncho.
     */
    public int getVentanaPrincipalAncho() {
        return ventanaPrincipalAncho;
    }

    /**
     * Actualiza el valor del atributo ventanaPrincipalAncho.
     * <p>
     * @param ventanaPrincipalAncho El nuevo valor de ventanaPrincipalAncho.
     */
    public void setVentanaPrincipalAncho(int ventanaPrincipalAncho) {
        this.ventanaPrincipalAncho = ventanaPrincipalAncho;
    }

    /**
     * Retorna el valor del atributo ventanaPrincipalLargo.
     * <p>
     * @return El valor del atributo ventanaPrincipalLargo.
     */
    public int getVentanaPrincipalLargo() {
        return ventanaPrincipalLargo;
    }

    /**
     * Actualiza el valor del atributo ventanaPrincipalLargo.
     * <p>
     * @param ventanaPrincipalLargo El nuevo valor de ventanaPrincipalLargo.
     */
    public void setVentanaPrincipalLargo(int ventanaPrincipalLargo) {
        this.ventanaPrincipalLargo = ventanaPrincipalLargo;
    }

    /**
     * Retorna el valor del atributo gestorDeCuentasX.
     * <p>
     * @return El valor del atributo gestorDeCuentasX.
     */
    public int getGestorDeCuentasX() {
        return gestorDeCuentasX;
    }

    /**
     * Actualiza el valor del atributo gestorDeCuentasX.
     * <p>
     * @param gestorDeCuentasX El nuevo valor de gestorDeCuentasX.
     */
    public void setGestorDeCuentasX(int gestorDeCuentasX) {
        this.gestorDeCuentasX = gestorDeCuentasX;
    }

    /**
     * Retorna el valor del atributo gestorDeCuentasY.
     * <p>
     * @return El valor del atributo gestorDeCuentasY.
     */
    public int getGestorDeCuentasY() {
        return gestorDeCuentasY;
    }

    /**
     * Actualiza el valor del atributo gestorDeCuentasY.
     * <p>
     * @param gestorDeCuentasY El nuevo valor de gestorDeCuentasY.
     */
    public void setGestorDeCuentasY(int gestorDeCuentasY) {
        this.gestorDeCuentasY = gestorDeCuentasY;
    }

    /**
     * Retorna el valor del atributo gestorDeTransferenciasX.
     * <p>
     * @return El valor del atributo gestorDeTransferenciasX.
     */
    public int getGestorDeTransferenciasX() {
        return gestorDeTransferenciasX;
    }

    /**
     * Actualiza el valor del atributo gestorDeTransferenciasX.
     * <p>
     * @param gestorDeTransferenciasX El nuevo valor de gestorDeTransferenciasX.
     */
    public void setGestorDeTransferenciasX(int gestorDeTransferenciasX) {
        this.gestorDeTransferenciasX = gestorDeTransferenciasX;
    }

    /**
     * Retorna el valor del atributo gestorDeTransferenciasY.
     * <p>
     * @return El valor del atributo gestorDeTransferenciasY.
     */
    public int getGestorDeTransferenciasY() {
        return gestorDeTransferenciasY;
    }

    /**
     * Actualiza el valor del atributo gestorDeTransferenciasY.
     * <p>
     * @param gestorDeTransferenciasY El nuevo valor de gestorDeTransferenciasY.
     */
    public void setGestorDeTransferenciasY(int gestorDeTransferenciasY) {
        this.gestorDeTransferenciasY = gestorDeTransferenciasY;
    }

    /**
     * Retorna el valor del atributo ventanaConversacionMaximizada.
     * <p>
     * @return El valor del atributo ventanaConversacionMaximizada.
     */
    public boolean isVentanaConversacionMaximizada() {
        return ventanaConversacionMaximizada;
    }

    /**
     * Actualiza el valor del atributo ventanaConversacionMaximizada.
     * <p>
     * @param ventanaConversacionMaximizada El nuevo valor de ventanaConversacionMaximizada.
     */
    public void setVentanaConversacionMaximizada(boolean ventanaConversacionMaximizada) {
        this.ventanaConversacionMaximizada = ventanaConversacionMaximizada;
    }

    /**
     * Retorna el valor del atributo ventanaConversacionX.
     * <p>
     * @return El valor del atributo ventanaConversacionX.
     */
    public int getVentanaConversacionX() {
        return ventanaConversacionX;
    }

    /**
     * Actualiza el valor del atributo ventanaConversacionX.
     * <p>
     * @param ventanaConversacionX El nuevo valor de ventanaConversacionX.
     */
    public void setVentanaConversacionX(int ventanaConversacionX) {
        this.ventanaConversacionX = ventanaConversacionX;
    }

    /**
     * Retorna el valor del atributo ventanaConversacionY.
     * <p>
     * @return El valor del atributo ventanaConversacionY.
     */
    public int getVentanaConversacionY() {
        return ventanaConversacionY;
    }

    /**
     * Actualiza el valor del atributo ventanaConversacionY.
     * <p>
     * @param ventanaConversacionY El nuevo valor de ventanaConversacionY.
     */
    public void setVentanaConversacionY(int ventanaConversacionY) {
        this.ventanaConversacionY = ventanaConversacionY;
    }

    /**
     * Retorna el valor del atributo ventanaConversacionAncho.
     * <p>
     * @return El valor del atributo ventanaConversacionAncho.
     */
    public int getVentanaConversacionAncho() {
        return ventanaConversacionAncho;
    }

    /**
     * Actualiza el valor del atributo ventanaConversacionAncho.
     * <p>
     * @param ventanaConversacionAncho El nuevo valor de ventanaConversacionAncho.
     */
    public void setVentanaConversacionAncho(int ventanaConversacionAncho) {
        this.ventanaConversacionAncho = ventanaConversacionAncho;
    }

    /**
     * Retorna el valor del atributo ventanaConversacionLargo.
     * <p>
     * @return El valor del atributo ventanaConversacionLargo.
     */
    public int getVentanaConversacionLargo() {
        return ventanaConversacionLargo;
    }

    /**
     * Actualiza el valor del atributo ventanaConversacionLargo.
     * <p>
     * @param ventanaConversacionLargo El nuevo valor de ventanaConversacionLargo.
     */
    public void setVentanaConversacionLargo(int ventanaConversacionLargo) {
        this.ventanaConversacionLargo = ventanaConversacionLargo;
    }

    /**
     * Retorna el valor del atributo fuente.
     * <p>
     * @return El valor del atributo fuente.
     */
    public String getFuente() {
        return fuente;
    }

    /**
     * Actualiza el valor del atributo fuente.
     * <p>
     * @param fuente El nuevo valor de fuente.
     */
    public void setFuente(String fuente) {
        this.fuente = fuente;
    }

    /**
     * Retorna el valor del atributo tamaño.
     * <p>
     * @return El valor del atributo tamaño.
     */
    public int getTamaño() {
        return tamaño;
    }

    /**
     * Actualiza el valor del atributo tamaño.
     * <p>
     * @param tamaño El nuevo valor de tamaño.
     */
    public void setTamaño(int tamaño) {
        this.tamaño = tamaño;
    }

    /**
     * Retorna el valor del atributo negrita.
     * <p>
     * @return El valor del atributo negrita.
     */
    public boolean isNegrita() {
        return negrita;
    }

    /**
     * Actualiza el valor del atributo negrita.
     * <p>
     * @param negrita El nuevo valor de negrita.
     */
    public void setNegrita(boolean negrita) {
        this.negrita = negrita;
    }

    /**
     * Retorna el valor del atributo cursiva.
     * <p>
     * @return El valor del atributo cursiva.
     */
    public boolean isCursiva() {
        return cursiva;
    }

    /**
     * Actualiza el valor del atributo cursiva.
     * <p>
     * @param cursiva El nuevo valor de cursiva.
     */
    public void setCursiva(boolean cursiva) {
        this.cursiva = cursiva;
    }

    /**
     * Retorna el valor del atributo colorRojo.
     * <p>
     * @return El valor del atributo colorRojo.
     */
    public int getColorRojo() {
        return colorRojo;
    }

    /**
     * Actualiza el valor del atributo colorRojo.
     * <p>
     * @param colorRojo El nuevo valor de colorRojo.
     */
    public void setColorRojo(int colorRojo) {
        this.colorRojo = colorRojo;
    }

    /**
     * Retorna el valor del atributo colorVerde.
     * <p>
     * @return El valor del atributo colorVerde.
     */
    public int getColorVerde() {
        return colorVerde;
    }

    /**
     * Actualiza el valor del atributo colorVerde.
     * <p>
     * @param colorVerde El nuevo valor de colorVerde.
     */
    public void setColorVerde(int colorVerde) {
        this.colorVerde = colorVerde;
    }

    /**
     * Retorna el valor del atributo colorAzul.
     * <p>
     * @return El valor del atributo colorAzul.
     */
    public int getColorAzul() {
        return colorAzul;
    }

    /**
     * Actualiza el valor del atributo colorAzul.
     * <p>
     * @param colorAzul El nuevo valor de colorAzul.
     */
    public void setColorAzul(int colorAzul) {
        this.colorAzul = colorAzul;
    }

    /**
     * Método para implementar el patrón Singleton
     * <p>
     * @return Retorna la única instancia del controlador de preferencias.
     */
    public static PreferenciasControlador getInstancia() {

        if (instancia == null) {
            instancia = new PreferenciasControlador();
        }

        return instancia;
    }
}

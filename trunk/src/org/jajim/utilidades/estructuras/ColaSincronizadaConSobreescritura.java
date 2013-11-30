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
package org.jajim.utilidades.estructuras;

import java.util.AbstractQueue;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

/**
 * @author Florencio Cañizal Calles
 * @version 1.2 Clase que maneja una cola a la que pueden acceder varios hilos y que sobreescribe un elemento cuando se
 * desea añadir uno nuevo y no hay espacio.
 * @param <E> Tipo de dato que manejará la cola sincronizada.
 */
public class ColaSincronizadaConSobreescritura<E> extends AbstractQueue<E> {

    private final Object[] cola;
    private int head;
    private int tail;
    private int elementos;

    /**
     * Constructor de la clase. Inicializa las variables necesarias.
     * <p>
     * @param tam El tamaño de la cola.
     */
    public ColaSincronizadaConSobreescritura(int tam) {
        cola = new Object[tam];
        head = 0;
        tail = 0;
        elementos = 0;
    }

    /**
     * Añade un elemento a la cola. Si la cola está llena elimina el elemento más antiguo.
     * <p>
     * @param e El elemento que se desea añadir a la cola.
     * @return Devuelve siempre true.
     */
    @Override
    public synchronized boolean offer(E e) {

        if (tail == head && elementos != 0) {
            // Si la cola está llena eliminar el elemento más antiguo y poner el
            // nuevo
            cola[head] = e;
            head = (head + 1) % cola.length;
            tail = (tail + 1) % cola.length;
        }
        else {
            cola[head] = e;
            head = (head + 1) % cola.length;
        }

        // Aumentar el número de elementos de la cola
        if (elementos != cola.length) {
            elementos++;
        }

        return true;
    }

    /**
     * Elimina el elemento más antiguo de la cola, si la cola está vacía retorna null.
     * <p>
     * @return El elemento más antiguo de la cola.
     */
    @Override
    public synchronized E poll() {

        // Si la cola está vacía devuelve null
        if (this.isEmpty()) {
            return null;
        }
        else {
            // Recupera el elemento más antiguo y retorna
            E elemento = (E) cola[tail];
            tail = (tail + 1) % cola.length;
            elementos--;
            return elemento;
        }
    }

    /**
     * Retorna el elemento más antiguo de la cola, retorna null si la cola está vacía.
     * <p>
     * @return El elemento más antiguo de la cola.
     */
    @Override
    public synchronized E peek() {

        // Si la cola está vacía devuelve null
        if (this.isEmpty()) {
            return null;
        }
        else {
            E elemento = (E) cola[tail];
            return elemento;
        }
    }

    /**
     * Retorna el tamaño de la cola.
     * <p>
     * @return El tamaño de la cola.
     */
    @Override
    public synchronized int size() {
        return elementos;
    }

    /**
     * Devuelve un iterador que actua sobre la cola.
     * <p>
     * @return Un iterador que actúa sobre la cola.
     */
    @Override
    public synchronized Iterator<E> iterator() {

        // Conseguimos una cola a partir del array y retornamos un iterador a la
        // misma.
        List<E> lista = (List<E>) Arrays.asList(cola);
        return lista.iterator();
    }
}

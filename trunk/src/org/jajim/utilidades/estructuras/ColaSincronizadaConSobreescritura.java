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
 * @version 1.1
 * Clase que maneja una cola a la que pueden acceder varios hilos y que sobreescribe
 * un elemento cuando se desea añadir uno nuevo y no hay espacio.
 */
public class ColaSincronizadaConSobreescritura<E> extends AbstractQueue<E>{

    private Object[] cola;
    private int head;
    private int tail;
    
    /**
     * Constructor de la clase. Inicializa las variables necesarias.
     * @param tam El tamaño de la cola.
     */
    public ColaSincronizadaConSobreescritura(int tam){
        cola = new Object[tam];
        head = 0;
        tail = 0;
    }

    /**
     * Añade un elemento a la cola. Si la cola está llena elimina el elemento más
     * antiguo.
     * @param e El elemento que se desea añadir a la cola.
     * @return Devuelve siempre true.
     */
    @Override
    public boolean offer(E e){

        // Comprobar si la cola está llena
        if((head + 1) % cola.length == tail){
            // Si está llena, borrar el elemento más antiguo y guardar el nuevo
            head = (head + 1) % cola.length;
            cola[head] = e;
            tail = (tail + 1) % cola.length;
        }else{
            // Si no está llena se guarda el elemento en la siguiente posición disponible
            head = (head + 1) % cola.length;
            cola[head] = e;
        }

        return true;
    }

    /**
     * Elimina el elemento más antiguo de la cola, si la cola está vacía retorna
     * null.
     * @return El elemento más antiguo de la cola.
     */
    @Override
    public E poll(){

        // Si la cola está vacía devuelve null
        if(this.isEmpty())
            return null;
        else{
            // Recupera el elemento más antiguo y retorna
            E elemento = (E) cola[tail];
            tail = (tail + 1) % cola.length;
            return elemento;
        }
    }

    /**
     * Retorna el elemento más antiguo de la cola, retorna null si la cola está
     * vacía.
     * @return El elemento más antiguo de la cola.
     */
    @Override
    public E peek(){

        // Si la cola está vacía devuelve null
        if(this.isEmpty())
            return null;
        else{
            E elemento = (E) cola[tail];
            return elemento;
        }
    }

    /**
     * Retorna el tamaño de la cola.
     * @return El tamaño de la cola.
     */
    @Override
    public int size(){

        if(tail < head)
            return (head - tail) + 1;
        else
            return (cola.length - (tail - head)) + 1;
    }

    /**
     * Devuelve un iterador que actua sobre la cola.
     * @return Un iterador que actúa sobre la cola.
     */
    @Override
    public Iterator<E> iterator(){

        // Conseguimos una cola a partir del array y retornamos un iterador a la
        // misma.
        List<E> lista = (List<E>) Arrays.asList(cola);
        return lista.iterator();
    }
}

package com.example.TDAs;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

public class TNodoGenericoImpl<T extends Comparable<T>> implements TNodoGenerico<T> {

    private final T dato;
    private final List<TNodoGenerico<T>> hijos;

    public TNodoGenericoImpl(T dato) {
        this.dato = dato;
        this.hijos = new ArrayList<>();
    }

    @Override
    public T getDato() {
        return dato;
    }

    @Override
    public boolean agregarHijo(T padre, T hijo) {
        // Si este nodo es el padre buscado, se agrega el nuevo hijo
        if (this.dato.compareTo(padre) == 0) {
            this.hijos.add(new TNodoGenericoImpl<>(hijo));
            return true;
        }

        // Si no es el padre, se delega la búsqueda a los hijos
        for (TNodoGenerico<T> h : hijos) {
            if (h.agregarHijo(padre, hijo)) {
                return true;
            }
        }
        return false;
    }

    @Override
    public TNodoGenerico<T> eliminar(Comparable<T> criterio) {
        for (int i = 0; i < hijos.size(); i++) {
            TNodoGenerico<T> h = hijos.get(i);
            if (criterio.compareTo(h.getDato()) == 0) {
                // Elimina el nodo hijo y todo su subárbol implícitamente
                return hijos.remove(i);
            }
            TNodoGenerico<T> eliminado = h.eliminar(criterio);
            if (eliminado != null) {
                return eliminado;
            }
        }
        return null;
    }

    @Override
    public TNodoGenerico<T> buscar(Comparable<T> criterio) {
        if (criterio.compareTo(this.dato) == 0) {
            return this;
        }

        for (TNodoGenerico<T> h : hijos) {
            TNodoGenerico<T> hallado = h.buscar(criterio);
            if (hallado != null) {
                return hallado;
            }
        }
        return null;
    }

    @Override
    public TNodoGenerico<T> obtenerPadre(Comparable<T> criterio) {
        for (TNodoGenerico<T> h : hijos) {
            if (criterio.compareTo(h.getDato()) == 0) {
                return this; // Este nodo es el padre del elemento buscado
            }
            TNodoGenerico<T> padreEncontrado = h.obtenerPadre(criterio);
            if (padreEncontrado != null) {
                return padreEncontrado;
            }
        }
        return null;
    }

    @Override
    public void preOrden(Consumer<TNodoGenerico<T>> consumidor) {
        consumidor.accept(this);
        for (TNodoGenerico<T> h : hijos) {
            h.preOrden(consumidor);
        }
    }

    @Override
    public void inOrden(Consumer<TNodoGenerico<T>> consumidor) {
        if (!hijos.isEmpty()) {
            hijos.get(0).inOrden(consumidor); // Visita el primer hijo
        }
        consumidor.accept(this); // Visita la raíz/nodo actual
        for (int i = 1; i < hijos.size(); i++) {
            hijos.get(i).inOrden(consumidor); // Visita el resto de los hijos
        }
    }

    @Override
    public void postOrden(Consumer<TNodoGenerico<T>> consumidor) {
        for (TNodoGenerico<T> h : hijos) {
            h.postOrden(consumidor);
        }
        consumidor.accept(this);
    }

    @Override
    public int altura() {
        int maxAltHijos = -1; // Si no tiene hijos, la altura respecto a sus subárboles es 0
        for (TNodoGenerico<T> h : hijos) {
            maxAltHijos = Math.max(maxAltHijos, h.altura());
        }
        return maxAltHijos + 1;
    }

    @Override
    public int grado() {
        return hijos.size();
    }

    @Override
    public void vaciar() {
        for (TNodoGenerico<T> h : hijos) {
            h.vaciar();
        }
        hijos.clear();
    }

    @Override
    public List<T> obtenerHijos() {
        List<T> datosHijos = new ArrayList<>();
        for (TNodoGenerico<T> h : hijos) {
            datosHijos.add(h.getDato());
        }
        return datosHijos;
    }
}
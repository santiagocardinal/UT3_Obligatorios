package com.example.TDAs;

import java.util.function.Consumer;

public class TArbolGenericoImpl<T extends Comparable<T>> implements TArbolGenerico<T> {

    private TNodoGenerico<T> raiz;

    public TArbolGenericoImpl() {
        this.raiz = null;
    }

    public TArbolGenericoImpl(T datoRaiz) {
        this.raiz = new TNodoGenericoImpl<>(datoRaiz);
    }

    @Override
    public boolean agregarHijo(Comparable<T> padre, T hijo) {
        // Caso de árbol vacío: se asigna el primer elemento como raíz
        if (raiz == null) {
            raiz = new TNodoGenericoImpl<>(hijo);
            return true;
        }

        // Si el padre coincide con la raíz
        if (padre.compareTo(raiz.getDato()) == 0) {
            return raiz.agregarHijo(raiz.getDato(), hijo);
        }

        // Búsqueda en el subárbol
        TNodoGenerico<T> nodoPadre = raiz.buscar(padre);
        if (nodoPadre != null) {
            return nodoPadre.agregarHijo(nodoPadre.getDato(), hijo);
        }
        return false;
    }

    @Override
    public void eliminar(Comparable<T> criterio) {
        if (raiz == null) return;

        // Si se desea eliminar la raíz completa
        if (criterio.compareTo(raiz.getDato()) == 0) {
            raiz.vaciar();
            raiz = null;
        } else {
            raiz.eliminar(criterio);
        }
    }

    @Override
    public T obtenerPadre(Comparable<T> criterio) {
        if (raiz == null) return null;
        
        TNodoGenerico<T> padre = raiz.obtenerPadre(criterio);
        return (padre != null) ? padre.getDato() : null;
    }

    @Override
    public T buscar(Comparable<T> criterio) {
        if (raiz == null) return null;

        TNodoGenerico<T> nodo = raiz.buscar(criterio);
        return (nodo != null) ? nodo.getDato() : null;
    }

    @Override
    public void preOrden(Consumer<T> consumidor) {
        if (raiz != null) {
            raiz.preOrden(nodo -> consumidor.accept(nodo.getDato()));
        }
    }

    @Override
    public void inOrden(Consumer<T> consumidor) {
        if (raiz != null) {
            raiz.inOrden(nodo -> consumidor.accept(nodo.getDato()));
        }
    }

    @Override
    public void postOrden(Consumer<T> consumidor) {
        if (raiz != null) {
            raiz.postOrden(nodo -> consumidor.accept(nodo.getDato()));
        }
    }

    @Override
    public void vaciar() {
        if (raiz != null) {
            raiz.vaciar();
            raiz = null;
        }
    }

    @Override
    public int grado(Comparable<T> nodo) {
        if (raiz == null) return 0;

        TNodoGenerico<T> buscado = raiz.buscar(nodo);
        return (buscado != null) ? buscado.grado() : 0;
    }

    @Override
    public int altura(Comparable<T> nodo) {
        if (raiz == null) return -1;

        TNodoGenerico<T> buscado = raiz.buscar(nodo);
        return (buscado != null) ? buscado.altura() : -1;
    }

    public TNodoGenerico<T> getRaiz() {
        return raiz;
    }
}
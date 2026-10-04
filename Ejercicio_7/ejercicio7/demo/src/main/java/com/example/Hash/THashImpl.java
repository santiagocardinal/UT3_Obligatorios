package com.example.Hash;

import java.util.ArrayList;
import java.util.List;

public class THashImpl<K, V> extends THash<K, V> {

    public THashImpl(int elementosEsperados) 
    {
        super(elementosEsperados);
    }

    @Override
    protected int functionHashing(K clave) 
    {
        if (clave == null) return 0;
        //se hizo esto para evitar valores negativos y aplicamos el módulo según el tamaño de la tabla
        return (clave.hashCode() & 0x7FFFFFFF) % hashTable.length;
    }

    @Override
    public boolean insertar(K clave, V valor, Report report) 
    {
        if (clave == null) 
        {
            return false;
        }
        int posicionInicial = functionHashing(clave);
        int posicionFinal = posicionInicial;
        int primerLibre = -1;
        int comparaciones = 0;

        do {
            TNodoHash<K, V> nodo = hashTable[posicionFinal];
            comparaciones++;
            if (nodo == null) {
                if (primerLibre == -1) primerLibre = posicionFinal;
                break; 
            }
            if (nodo.isLoteLibre()) {
                if (primerLibre == -1) primerLibre = posicionFinal;
            } 
            else if (nodo.getClave().equals(clave)) {
                report.setCantidadComparaciones(comparaciones);
                return false; 
            }

            posicionFinal = (posicionFinal + 1) % hashTable.length;
        } while (posicionFinal != posicionInicial);

        if (primerLibre != -1) {
            hashTable[primerLibre] = new TNodoHash<>(clave, valor);
            report.setCantidadComparaciones(comparaciones);
            return true;
        }

        report.setCantidadComparaciones(comparaciones);
        return false; 
    }

    @Override
    public V buscar(K clave, Report report) {
        if (clave == null) return null;

        int posicionInicial = functionHashing(clave);
        int posicionFinal = posicionInicial;
        int comparaciones = 0;

        do {
            TNodoHash<K, V> nodo = hashTable[posicionFinal];
            comparaciones++;
            if (nodo == null) {
                report.setCantidadComparaciones(comparaciones);
                return null;
            }
            if (!nodo.isLoteLibre() && nodo.getClave().equals(clave)) {
                report.setCantidadComparaciones(comparaciones);
                return nodo.getValor();
            }

            posicionFinal = (posicionFinal + 1) % hashTable.length;
        } while (posicionFinal != posicionInicial);

        report.setCantidadComparaciones(comparaciones);
        return null;
    }

    @Override
    public boolean delete(K clave, Report report) {
        if (clave == null) return false;

        int posicionInicial = functionHashing(clave);
        int posicionFinal = posicionInicial;
        int comparaciones = 0;

        do {
            TNodoHash<K, V> nodo = hashTable[posicionFinal];
            comparaciones++;

            if (nodo == null) {
                report.setCantidadComparaciones(comparaciones);
                return false;
            }

            if (!nodo.isLoteLibre() && nodo.getClave().equals(clave)) {
                nodo.setLoteLibre(true); 
                report.setCantidadComparaciones(comparaciones);
                return true;
            }

            posicionFinal = (posicionFinal + 1) % hashTable.length;
        } while (posicionFinal != posicionInicial);

        report.setCantidadComparaciones(comparaciones);
        return false;
    }

    @Override
    public boolean esVacio() {
        for (TNodoHash<K, V> nodo : hashTable) {
            if (nodo != null && !nodo.isLoteLibre()) {
                return false;
            }
        }
        return true;
    }

    @Override
    public void vaciar() {
        for (int i = 0; i < hashTable.length; i++) {
            hashTable[i] = null;
        }
    }

    @Override
    protected int calcularCapacidadOptima(int elementosEsperados) {
        int capacidadMinima = (int) (elementosEsperados / 0.70) + 1;
        return siguientePrimo(capacidadMinima);
    }

    private int siguientePrimo(int n) {
        while (!esPrimo(n)) {
            n++;
        }
        return n;
    }

    private boolean esPrimo(int n) {
        if (n <= 1) return false;
        for (int i = 2; i * i <= n; i++) {
            if (n % i == 0) return false;
        }
        return true;
    }

    @Override
    protected boolean redimensionar() {
        return false;
    }

    @Override
    public Iterable<Entry<K, V>> entries() {
        List<Entry<K, V>> lista = new ArrayList<>();
        for (TNodoHash<K, V> nodo : hashTable) {
            if (nodo != null && !nodo.isLoteLibre()) {
                lista.add(nodo.getEntry());
            }
        }
        return lista;
    }

    @Override
    public Iterable<K> keys() {
        List<K> lista = new ArrayList<>();
        for (TNodoHash<K, V> nodo : hashTable) {
            if (nodo != null && !nodo.isLoteLibre()) {
                lista.add(nodo.getClave());
            }
        }
        return lista;
    }

    @Override
    public Iterable<V> values() {
        List<V> lista = new ArrayList<>();
        for (TNodoHash<K, V> nodo : hashTable) {
            if (nodo != null && !nodo.isLoteLibre()) {
                lista.add(nodo.getValor());
            }
        }
        return lista;
    }
}
package com.example;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TNodoTrieHashMap<T> {

    private final char caracter;
    private final Map<Character, TNodoTrieHashMap<T>> hijos;
    private boolean esPalabra;
    private T dato;

    public TNodoTrieHashMap(char caracter) {
        this.caracter = caracter;
        this.hijos = new HashMap<>();
        this.esPalabra = false;
        this.dato = null;
    }

    public void insertar(String palabra, T dato) {
        TNodoTrieHashMap<T> nodoActual = this;
        for (int i = 0; i < palabra.length(); i++) {
            char c = palabra.charAt(i);
            nodoActual = nodoActual.hijos.computeIfAbsent(c, k -> new TNodoTrieHashMap<>(k));
        }
        nodoActual.esPalabra = true;
        nodoActual.dato = dato;
    }

    public TNodoTrieHashMap<T> buscar(String palabra) {
        TNodoTrieHashMap<T> nodoActual = this;
        for (int i = 0; i < palabra.length(); i++) {
            char c = palabra.charAt(i);
            nodoActual = nodoActual.hijos.get(c);
            if (nodoActual == null) {
                return null; 
            }
        }
        return nodoActual.esPalabra ? nodoActual : null;
    }

    public TNodoTrieHashMap<T> buscarNodoPrefijo(String prefijo) {
        TNodoTrieHashMap<T> nodoActual = this;
        for (int i = 0; i < prefijo.length(); i++) {
            char c = prefijo.charAt(i);
            nodoActual = nodoActual.hijos.get(c);
            if (nodoActual == null) {
                return null; 
            }
        }
        return nodoActual; 
    }

    public void predecir(String prefijoActual, List<String> resultados) {
        if (this.esPalabra) {
            resultados.add(prefijoActual);
        }
        for (TNodoTrieHashMap<T> hijo : hijos.values()) {
            hijo.predecir(prefijoActual + hijo.caracter, resultados);
        }
    }

    public char getCaracter() 
    { 
        return caracter; 
    }
    public boolean isEsPalabra() 
    { 
        return esPalabra; 
    }
    public T getDato() 
    { 
        return dato; 
    }
    public Map<Character, TNodoTrieHashMap<T>> getHijos() 
    { 
        return hijos; 
    }
}
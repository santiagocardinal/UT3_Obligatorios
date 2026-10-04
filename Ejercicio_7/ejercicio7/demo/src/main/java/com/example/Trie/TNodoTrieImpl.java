package com.example.Trie;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

public class TNodoTrieImpl<T> implements TNodoTrie<T> 
{

    private final char caracter;
    private final List<TNodoTrieImpl<T>> hijos;
    private boolean esPalabra;
    private T dato;

    public TNodoTrieImpl(char caracter) 
    {
        this.caracter = caracter;
        this.hijos = new ArrayList<>();
        this.esPalabra = false;
        this.dato = null;
    }

    public char getCaracter() {
        return this.caracter;
    }

    public TNodoTrieImpl<T> obtenerHijo(char caracter) 
    {
        for (TNodoTrieImpl<T> hijo : hijos) 
        {
            if (hijo.getCaracter() == caracter) 
            {
                return hijo;
            }
        }
        return null;
    }

    @Override
    public boolean esPalabra() 
    {
        return this.esPalabra;
    }

    @Override
    public T getDato() 
    {
        return this.esPalabra ? this.dato : null;
    }

    @Override
    public void recorrer(Consumer<Entry<T>> consumer) 
    {
        recorrerAux("", consumer);
    }

    private void recorrerAux(String prefijoActual, Consumer<Entry<T>> consumer) 
    {
        if (this.esPalabra())
        {
            consumer.accept(new Entry<>(this.getDato(), true, prefijoActual));
        }
        for (TNodoTrieImpl<T> hijo : hijos) 
        {
            hijo.recorrerAux(prefijoActual + hijo.getCaracter(), consumer);
        }
    }

    @Override
    public Entry<T> buscar(String palabra) 
    {
        TNodoTrieImpl<T> nodoActual = this;

        for (char character : palabra.toCharArray()) 
        {
            nodoActual = nodoActual.obtenerHijo(character);
            if (nodoActual == null) 
            {
                return null;
            }
        }
        return new Entry<>(nodoActual.getDato(), nodoActual.esPalabra(), palabra);
    }

    @Override
    public boolean insertar(String palabra, T dato) 
    {
        TNodoTrieImpl<T> nodoActual = this;

        for (char character : palabra.toCharArray()) 
        {
            TNodoTrieImpl<T> hijo = nodoActual.obtenerHijo(character);
            if (hijo == null) {
                hijo = new TNodoTrieImpl<>(character);
                nodoActual.hijos.add(hijo);
            }
            nodoActual = hijo;
        }

        boolean yaEraPalabra = nodoActual.esPalabra;
        nodoActual.esPalabra = true;
        nodoActual.dato = dato;

        return !yaEraPalabra;
    }

    @Override
    public List<Entry<T>> predecir(String prefijo) 
    {
        List<Entry<T>> resultados = new ArrayList<>();
        TNodoTrieImpl<T> nodoActual = this;

        for (char character : prefijo.toCharArray()) 
        {
            nodoActual = nodoActual.obtenerHijo(character);
            if (nodoActual == null) 
            {
                return resultados; 
            }
        }
        nodoActual.recorrerAux(prefijo, resultados::add);
        return resultados;
    }
}
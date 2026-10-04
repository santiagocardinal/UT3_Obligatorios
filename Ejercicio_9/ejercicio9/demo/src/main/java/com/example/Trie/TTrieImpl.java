package com.example.Trie;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

public class TTrieImpl<T> implements TTrie<T> 
{ 

    private TNodoTrieImpl<T> raiz;

    public TTrieImpl() 
    {
        raiz = new TNodoTrieImpl<T>('\0');
    }

    @Override
    public void recorrer(Consumer<Entry<T>> consumer) 
    {
        if (raiz != null) 
        {
            raiz.recorrer(consumer);
        }
    }

    @Override
    public Entry<T> buscar(String palabra) 
    {
        if (raiz == null) 
        {
            return null;
        }
        return raiz.buscar(palabra);
    }

    @Override
    public boolean insertar(String palabra, T dato) 
    {
        if (raiz == null) 
        {
            raiz = new TNodoTrieImpl<T>('\0');
        }
        return raiz.insertar(palabra, dato);
    }

    @Override
    public List<Entry<T>> predecir(String prefijo) 
    {
        if (raiz == null) 
        {
            return new ArrayList<>();
        }
        return raiz.predecir(prefijo);
    }
}
package com.example;

import java.util.List;

public class MainEjercicio12 {

    public static void main(String[] args) {
        TTrieHashMap<Integer> trieAutocompletar = new TTrieHashMap<>();

        String[] diccionario = {"algoritmo", "algoritmos", "algo", "alguien", "arbol", "estrucutra"};
        for (int i = 0; i < diccionario.length; i++) {
            trieAutocompletar.insertar(diccionario[i], i);
        }
        String prefijo = "alg";
        List<String> sugerencias = trieAutocompletar.predecir(prefijo);

        System.out.println("=== PRUEBA DE AUTOCOMPLETAR ===");
        System.out.println("Prefijo ingresado: \"" + prefijo + "\"");
        System.out.println("Sugerencias predichas: " + sugerencias);

        String texto = "el algoritmo de hashing y el algoritmo de trie";
        TTrieHashMap.buscarPatronEnTexto(texto, "algoritmo");
    }
}
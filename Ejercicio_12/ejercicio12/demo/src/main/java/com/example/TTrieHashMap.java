package com.example;

import java.util.ArrayList;
import java.util.List;

public class TTrieHashMap<T> {

    private final TNodoTrieHashMap<T> raiz;

    public TTrieHashMap() {
        this.raiz = new TNodoTrieHashMap<>('\0');
    }

    public void insertar(String palabra, T dato) {
        if (palabra != null && !palabra.isEmpty()) {
            raiz.insertar(palabra.toLowerCase(), dato);
        }
    }

    public T buscar(String palabra) {
        if (palabra == null || palabra.isEmpty()) return null;
        TNodoTrieHashMap<T> nodo = raiz.buscar(palabra.toLowerCase());
        return (nodo != null) ? nodo.getDato() : null;
    }

    public List<String> predecir(String prefijo) {
        List<String> resultados = new ArrayList<>();
        if (prefijo == null || prefijo.isEmpty()) return resultados;

        String prefijoLimpio = prefijo.toLowerCase();
        TNodoTrieHashMap<T> nodoPrefijo = raiz.buscarNodoPrefijo(prefijoLimpio);

        if (nodoPrefijo != null) {
            nodoPrefijo.predecir(prefijoLimpio, resultados);
        }
        return resultados;
    }


    public static void buscarPatronEnTexto(String texto, String patron) {
        TTrieHashMap<List<Integer>> trieSufijos = new TTrieHashMap<>();
        String textoLimpio = texto.toLowerCase();
        String patronLimpio = patron.toLowerCase();


        for (int i = 0; i < textoLimpio.length(); i++) {
            String sufijo = textoLimpio.substring(i);
            
            TNodoTrieHashMap<List<Integer>> nodo = trieSufijos.raiz.buscarNodoPrefijo(sufijo);
            if (nodo != null && nodo.getDato() != null) {
                nodo.getDato().add(i);
            } else {
                List<Integer> posiciones = new ArrayList<>();
                posiciones.add(i);
                trieSufijos.insertar(sufijo, posiciones);
            }
        }

        TNodoTrieHashMap<List<Integer>> nodoPatron = trieSufijos.raiz.buscarNodoPrefijo(patronLimpio);

        System.out.println("\n=== BÚSQUEDA DE PATRÓN ===");
        System.out.println("Texto: \"" + texto + "\"");
        System.out.println("Patrón buscado: \"" + patron + "\"");

        if (nodoPatron != null) {
            List<String> predicciones = trieSufijos.predecir(patronLimpio);
            System.out.println("El patrón fue encontrado en el texto.");
            System.out.println("Ocurrencias / Coincidencias encontradas: " + predicciones.size());
        } else {
            System.out.println("El patrón NO se encuentra en el texto.");
        }
    }
}
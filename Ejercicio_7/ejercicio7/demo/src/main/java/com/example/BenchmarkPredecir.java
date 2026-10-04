package com.example;

import com.example.Trie.TTrieImpl;
import com.example.Trie.Entry;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;

public class BenchmarkPredecir {

    private static final int NUM_REPETICIONES = 20;
    private static final String PREFIJO = "cas";

    public static void main(String[] args) {
        List<String> palabrasCarga = cargarArchivo("listado-general-desordenado.txt");

        System.out.println("=== COMPARACIÓN DE PREDECIR / AUTOCOMPLETAR (Prefijo: \"cas\") ===");
        System.out.printf("%-15s | %-15s | %-15s%n", "Estructura", "Memoria (MB)", "Tiempo (ms)");
        System.out.println("-------------------------------------------------------");

        medirPredecirTrie(palabrasCarga);
        medirPredecirLinkedList(palabrasCarga);
        medirPredecirHashMap(palabrasCarga);
    }

    private static List<String> cargarArchivo(String ruta) {
        List<String> lineas = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new FileReader(ruta))) {
            String l;
            while ((l = br.readLine()) != null) {
                if (!l.trim().isEmpty()) lineas.add(l.trim().toLowerCase());
            }
        } catch (IOException ignored) {}
        return lineas;
    }

    private static void medirPredecirTrie(List<String> carga) {
        System.gc();
        long memInicial = Runtime.getRuntime().totalMemory() - Runtime.getRuntime().freeMemory();

        TTrieImpl<Integer> trie = new TTrieImpl<>();
        for (int i = 0; i < carga.size(); i++) trie.insertar(carga.get(i), i);

        long memFinal = Runtime.getRuntime().totalMemory() - Runtime.getRuntime().freeMemory();
        long memMB = Math.max(0, (memFinal - memInicial) / (1024 * 1024));

        long inicio = System.nanoTime();
        for (int i = 0; i < NUM_REPETICIONES; i++) {
            List<Entry<Integer>> resultado = trie.predecir(PREFIJO); 
        }
        long ms = (System.nanoTime() - inicio) / 1_000_000;

        System.out.printf("%-15s | %-15d | %-15d%n", "Trie", memMB, ms);
    }

    private static void medirPredecirLinkedList(List<String> carga) {
        System.gc();
        long memInicial = Runtime.getRuntime().totalMemory() - Runtime.getRuntime().freeMemory();

        List<String> lista = new LinkedList<>(carga);

        long memFinal = Runtime.getRuntime().totalMemory() - Runtime.getRuntime().freeMemory();
        long memMB = Math.max(0, (memFinal - memInicial) / (1024 * 1024));

        long inicio = System.nanoTime();
        for (int i = 0; i < NUM_REPETICIONES; i++) {
            List<String> res = new ArrayList<>();
            for (String p : lista) {
                if (p.startsWith(PREFIJO)) res.add(p); 
            }
        }
        long ms = (System.nanoTime() - inicio) / 1_000_000;

        System.out.printf("%-15s | %-15d | %-15d%n", "LinkedList", memMB, ms);
    }

    private static void medirPredecirHashMap(List<String> carga) {
        System.gc();
        long memInicial = Runtime.getRuntime().totalMemory() - Runtime.getRuntime().freeMemory();

        Map<String, Integer> map = new HashMap<>();
        for (int i = 0; i < carga.size(); i++) map.put(carga.get(i), i);

        long memFinal = Runtime.getRuntime().totalMemory() - Runtime.getRuntime().freeMemory();
        long memMB = Math.max(0, (memFinal - memInicial) / (1024 * 1024));

        long inicio = System.nanoTime();
        for (int i = 0; i < NUM_REPETICIONES; i++) {
            List<String> res = new ArrayList<>();
            for (String p : map.keySet()) {
                if (p.startsWith(PREFIJO)) res.add(p); 
            }
        }
        long ms = (System.nanoTime() - inicio) / 1_000_000;

        System.out.printf("%-15s | %-15d | %-15d%n", "HashMap", memMB, ms);
    }
}
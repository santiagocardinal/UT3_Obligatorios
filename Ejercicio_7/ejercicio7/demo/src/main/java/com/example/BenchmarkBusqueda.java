package com.example;

import com.example.Trie.TTrieImpl;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;

public class BenchmarkBusqueda {

    private static final int NUM_REPETICIONES = 20;

    public static void main(String[] args) {
        String archivoCarga = "listado-general-desordenado.txt";
        String archivoBuscar = "listado-general-palabrasBuscar.txt";

        List<String> palabrasCarga = cargarArchivo(archivoCarga);
        List<String> palabrasBuscar = cargarArchivo(archivoBuscar);

        System.out.println("=== COMPARACIÓN DE BÚSQUEDA EXACTA (20 Pasadas) ===");
        System.out.printf("%-15s | %-15s | %-15s%n", "Estructura", "Memoria (MB)", "Tiempo (ms)");
        System.out.println("-------------------------------------------------------");

        medirLinkedList(palabrasCarga, palabrasBuscar);
        medirArrayList(palabrasCarga, palabrasBuscar);
        medirTTrie(palabrasCarga, palabrasBuscar);
        medirHashMap(palabrasCarga, palabrasBuscar);
        medirTreeMap(palabrasCarga, palabrasBuscar);
    }

    private static List<String> cargarArchivo(String ruta) {
        List<String> lineas = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new FileReader(ruta))) {
            String l;
            while ((l = br.readLine()) != null) {
                if (!l.trim().isEmpty()) lineas.add(l.trim().toLowerCase());
            }
        } catch (IOException e) {
            System.err.println("Error al leer: " + ruta);
        }
        return lineas;
    }

    private static void limpiarMemoria() {
        System.gc();
        try { Thread.sleep(100); } catch (InterruptedException ignored) {}
    }

    private static void medirLinkedList(List<String> carga, List<String> buscar) {
        limpiarMemoria();
        long memInicial = Runtime.getRuntime().totalMemory() - Runtime.getRuntime().freeMemory();

        List<String> estructura = new LinkedList<>(carga);

        long memFinal = Runtime.getRuntime().totalMemory() - Runtime.getRuntime().freeMemory();
        long memMB = Math.max(0, (memFinal - memInicial) / (1024 * 1024));

        long inicio = System.nanoTime();
        for (int i = 0; i < NUM_REPETICIONES; i++) {
            for (String p : buscar) {
                estructura.contains(p); 
            }
        }
        long ms = (System.nanoTime() - inicio) / 1_000_000;

        System.out.printf("%-15s | %-15d | %-15d%n", "LinkedList", memMB, ms);
    }

    private static void medirArrayList(List<String> carga, List<String> buscar) {
        limpiarMemoria();
        long memInicial = Runtime.getRuntime().totalMemory() - Runtime.getRuntime().freeMemory();

        List<String> estructura = new ArrayList<>(carga);

        long memFinal = Runtime.getRuntime().totalMemory() - Runtime.getRuntime().freeMemory();
        long memMB = Math.max(0, (memFinal - memInicial) / (1024 * 1024));

        long inicio = System.nanoTime();
        for (int i = 0; i < NUM_REPETICIONES; i++) {
            for (String p : buscar) {
                estructura.contains(p); 
            }
        }
        long ms = (System.nanoTime() - inicio) / 1_000_000;

        System.out.printf("%-15s | %-15d | %-15d%n", "ArrayList", memMB, ms);
    }

    private static void medirTTrie(List<String> carga, List<String> buscar) {
        limpiarMemoria();
        long memInicial = Runtime.getRuntime().totalMemory() - Runtime.getRuntime().freeMemory();

        TTrieImpl<Integer> trie = new TTrieImpl<>();
        for (int i = 0; i < carga.size(); i++) trie.insertar(carga.get(i), i);

        long memFinal = Runtime.getRuntime().totalMemory() - Runtime.getRuntime().freeMemory();
        long memMB = Math.max(0, (memFinal - memInicial) / (1024 * 1024));

        long inicio = System.nanoTime();
        for (int i = 0; i < NUM_REPETICIONES; i++) {
            for (String p : buscar) {
                trie.buscar(p);
            }
        }
        long ms = (System.nanoTime() - inicio) / 1_000_000;

        System.out.printf("%-15s | %-15d | %-15d%n", "TTrie", memMB, ms);
    }

    private static void medirHashMap(List<String> carga, List<String> buscar) {
        limpiarMemoria();
        long memInicial = Runtime.getRuntime().totalMemory() - Runtime.getRuntime().freeMemory();

        Map<String, Integer> map = new HashMap<>();
        for (int i = 0; i < carga.size(); i++) map.put(carga.get(i), i);

        long memFinal = Runtime.getRuntime().totalMemory() - Runtime.getRuntime().freeMemory();
        long memMB = Math.max(0, (memFinal - memInicial) / (1024 * 1024));

        long inicio = System.nanoTime();
        for (int i = 0; i < NUM_REPETICIONES; i++) {
            for (String p : buscar) {
                map.containsKey(p); 
            }
        }
        long ms = (System.nanoTime() - inicio) / 1_000_000;

        System.out.printf("%-15s | %-15d | %-15d%n", "HashMap", memMB, ms);
    }

    private static void medirTreeMap(List<String> carga, List<String> buscar) {
        limpiarMemoria();
        long memInicial = Runtime.getRuntime().totalMemory() - Runtime.getRuntime().freeMemory();

        Map<String, Integer> map = new TreeMap<>();
        for (int i = 0; i < carga.size(); i++) map.put(carga.get(i), i);

        long memFinal = Runtime.getRuntime().totalMemory() - Runtime.getRuntime().freeMemory();
        long memMB = Math.max(0, (memFinal - memInicial) / (1024 * 1024));

        long inicio = System.nanoTime();
        for (int i = 0; i < NUM_REPETICIONES; i++) {
            for (String p : buscar) {
                map.containsKey(p); 
            }
        }
        long ms = (System.nanoTime() - inicio) / 1_000_000;

        System.out.printf("%-15s | %-15d | %-15d%n", "TreeMap", memMB, ms);
    }
}
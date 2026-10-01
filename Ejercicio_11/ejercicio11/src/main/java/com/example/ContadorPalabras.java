package com.example;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.PriorityQueue;
import java.util.stream.Stream;


 /**
 * Conteo: HashMap -> O(1) promedio por palabra, O(n) en total.
 * Top N:  PriorityQueue (min-heap) de tamaño N -> O(m log N), m = palabras distintas.
 */
public class ContadorPalabras {

    /** Separa por cualquier secuencia de caracteres que no sean letras (conserva tildes y ñ). */
    private static final String SEPARADOR = "[^\\p{L}]+";

    /**
     * Orden "de menor a mayor importancia": menor frecuencia primero y,
     * ante empate, la palabra alfabéticamente mayor primero.
     * Es el orden del min-heap: la raíz es siempre la candidata a descartar.
     */
    private static final Comparator<Map.Entry<String, Integer>> MENOS_FRECUENTE_PRIMERO =
            Map.Entry.<String, Integer>comparingByValue()
                    .thenComparing(Map.Entry.<String, Integer>comparingByKey().reversed());

    private final Map<String, Integer> frecuencias = new HashMap<>();

    /** Lee el archivo línea por línea y cuenta sus palabras. */
    public void procesarArchivo(Path archivo) throws IOException {
        try (Stream<String> lineas = Files.lines(archivo, StandardCharsets.UTF_8)) {
            lineas.forEach(this::procesarLinea);
        }
    }

    public void procesarLinea(String linea) {
        for (String palabra : linea.toLowerCase().split(SEPARADOR)) {
            if (!palabra.isEmpty()) {
                // Si no existe la inserta con 1; si existe, suma 1 al valor actual.
                frecuencias.merge(palabra, 1, Integer::sum);
            }
        }
    }

    /** Devuelve la frecuencia de una palabra (0 si no aparece). */
    public int frecuencia(String palabra) {
        return frecuencias.getOrDefault(palabra.toLowerCase(), 0);
    }

    public Map<String, Integer> getFrecuencias() {
        return Collections.unmodifiableMap(frecuencias);
    }

    public int cantidadPalabrasDistintas() {
        return frecuencias.size();
    }

    public long totalPalabras() {
        long total = 0;
        for (int f : frecuencias.values()) {
            total += f;
        }
        return total;
    }

    /**
     * Devuelve las n palabras más frecuentes, ordenadas de mayor a menor frecuencia
     */
    public List<Map.Entry<String, Integer>> masFrecuentes(int n) {
        if (n <= 0) {
            return new ArrayList<>();
        }
        PriorityQueue<Map.Entry<String, Integer>> heap = new PriorityQueue<>(MENOS_FRECUENTE_PRIMERO);

        for (Map.Entry<String, Integer> entrada : frecuencias.entrySet()) {
            heap.offer(Map.entry(entrada.getKey(), entrada.getValue()));
            if (heap.size() > n) {
                heap.poll(); // descarta la menos frecuente: el heap nunca supera n elementos
            }
        }

        List<Map.Entry<String, Integer>> resultado = new ArrayList<>(heap);
        resultado.sort(MENOS_FRECUENTE_PRIMERO.reversed());
        return resultado;
    }
}
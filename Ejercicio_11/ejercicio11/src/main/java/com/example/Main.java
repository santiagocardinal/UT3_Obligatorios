package com.example;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Map;

public class Main {

    private static final int TOP = 10;
    private static final int ANCHO_BARRA_TEXTO = 50;

    public static void main(String[] args) {
        Path archivo = Paths.get(args.length > 0 ? args[0] : "libro.txt");

        if (!Files.exists(archivo)) {
            System.err.println("No se encontró el archivo: " + archivo.toAbsolutePath());
            return;
        }

        ContadorPalabras contador = new ContadorPalabras();
        long inicio = System.nanoTime();
        try {
            contador.procesarArchivo(archivo);
        } catch (IOException e) {
            System.err.println("Error al leer el archivo: " + e.getMessage());
            return;
        }
        List<Map.Entry<String, Integer>> top = contador.masFrecuentes(TOP);
        long ms = (System.nanoTime() - inicio) / 1_000_000;

        System.out.println("Archivo:            " + archivo.getFileName());
        System.out.println("Palabras totales:   " + contador.totalPalabras());
        System.out.println("Palabras distintas: " + contador.cantidadPalabrasDistintas());
        System.out.println("Tiempo:             " + ms + " ms");
        System.out.println();
        System.out.println("Las " + TOP + " palabras más frecuentes:");
        imprimirGraficoTexto(top);
    }

    private static void imprimirGraficoTexto(List<Map.Entry<String, Integer>> top) {
        if (top.isEmpty()) {
            System.out.println("(el archivo no tiene palabras)");
            return;
        }
        int maximo = top.get(0).getValue();
        int posicion = 1;
        for (Map.Entry<String, Integer> e : top) {
            int largo = Math.max(1, (int) Math.round((double) e.getValue() / maximo * ANCHO_BARRA_TEXTO));
            System.out.printf("%2d. %-15s %7d %s%n", posicion++, e.getKey(), e.getValue(), "#".repeat(largo));
        }
    }
}
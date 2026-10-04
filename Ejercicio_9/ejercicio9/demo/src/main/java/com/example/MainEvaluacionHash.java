package com.example;

import com.example.Hash.THashImpl;
import com.example.Hash.Report;

public class MainEvaluacionHash {

    private static final int TAMANIO_TABLA = 10007; 
    
    private static final double[] FACTORES_DE_CARGA = {
        0.70, 0.75, 0.80, 0.85, 0.90, 0.91, 0.92, 0.93, 0.94, 0.95, 0.96, 0.97, 0.98, 0.99
    };

    public static void main(String[] args) {
        System.out.printf("%-15s %-25s %-28s %-28s%n", 
            "Factor Carga %", "Prom. Comp. Insercion", "Prom. Comp. Busq. Exitosa", "Prom. Comp. Busq. Inexistosa");
        System.out.println("--------------------------------------------------------------------------------------------------");

        for (double factorCarga : FACTORES_DE_CARGA) {
            evaluarFactorDeCarga(factorCarga);
        }
    }

    private static void evaluarFactorDeCarga(double factorCarga) {
        THashImpl<String, String> tabla = new THashImpl<>(TAMANIO_TABLA);
        int elementosAInsertar = (int) (TAMANIO_TABLA * factorCarga);

        Report report = new Report();
        long totalCompInsercion = 0;

        for (int i = 0; i < elementosAInsertar; i++) {
            String clave = "clave_" + i;
            tabla.insertar(clave, "valor_" + i, report);
            totalCompInsercion += report.getCantidadComparaciones();
        }
        double promInsercion = (double) totalCompInsercion / elementosAInsertar;

        long totalCompExitosa = 0;
        int muestrasExitosa = Math.min(1000, elementosAInsertar);
        
        for (int i = 0; i < muestrasExitosa; i++) {
            String claveExistente = "clave_" + i;
            tabla.buscar(claveExistente, report);
            totalCompExitosa += report.getCantidadComparaciones();
        }
        double promBusquedaExitosa = (double) totalCompExitosa / muestrasExitosa;


        long totalCompInexistosa = 0;
        int muestrasInexistosa = 1000;

        for (int i = 0; i < muestrasInexistosa; i++) {
            String claveInexistente = "inexistente_" + i;
            tabla.buscar(claveInexistente, report);
            totalCompInexistosa += report.getCantidadComparaciones();
        }
        double promBusquedaInexistosa = (double) totalCompInexistosa / muestrasInexistosa;

        System.out.printf("%-15.0f%% %-25.2f %-28.2f %-28.2f%n", 
            factorCarga * 100, promInsercion, promBusquedaExitosa, promBusquedaInexistosa);
    }
}

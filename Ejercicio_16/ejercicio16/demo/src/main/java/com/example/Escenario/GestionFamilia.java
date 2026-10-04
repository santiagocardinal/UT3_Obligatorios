package com.example.Escenario;

import java.util.ArrayList;
import java.util.List;
import com.example.TDAs.TNodoGenerico;
import com.example.TDAs.TArbolGenerico;

public class GestionFamilia 
{

    private final TArbolGenerico<Persona> arbol;

    public List<Persona> obtenerPersonasPorGeneracion(int generacionBuscada) 
    {
        List<Persona> resultado = new ArrayList<>();
        TNodoGenerico<Persona> raiz = arbol.getRaiz();
        
        if (raiz != null) 
        {
            recorrerPorGeneracion(raiz, 0, generacionBuscada, resultado);
        }
        return resultado;
    }

    private void recorrerPorGeneracion(TNodoGenerico<Persona> nodoActual, int nivelActual, int generacionBuscada, List<Persona> resultado) {
        if (nodoActual == null) return;
        if (nivelActual == generacionBuscada) 
        {
            resultado.add(nodoActual.getDato());
            return;
        }
    }
}
package com.example.Escenario;

import java.util.ArrayList;
import java.util.List;
import com.example.TDAs.TNodoGenerico;

import main.java.com.example.Escenario.Persona;

import com.example.TDAs.TArbolGenerico;

public class ArbolGenealogico {

    private final TArbolGenerico<Persona> arbol;

    public ArbolGenealogico(TArbolGenerico<Persona> arbol) 
    {
        this.arbol = arbol;
    }

    public List<Persona> listarDescendientes(Comparable<Persona> criterioPersona) 
    {
        List<Persona> descendientes = new ArrayList<>();
        TNodoGenerico<Persona> nodoPersona = arbol.buscar(criterioPersona);

        if (nodoPersona != null) 
        {
            nodoPersona.preOrden(nodo -> descendientes.add(nodo.getDato()));
            if (!descendientes.isEmpty()) {
                descendientes.remove(0); 
            }
        }
        return descendientes;
    }

    public boolean esDescendiente(Comparable<Persona> personaA, Persona personaB) 
    {
        List<Persona> descendientesDeA = listarDescendientes(personaA);
        return descendientesDeA.contains(personaB);
    }

    public int obtenerAlturaTotal() 
    {
        if (arbol.getRaiz() == null) 
        {
            return 0;
        }
        return arbol.altura(arbol.getRaiz().getDato());
    }

    public int contarTotalPersonas() 
    {
        int[] contador = new int[1]; 
        
        arbol.preOrden(persona -> {contador[0]++;});
        return contador[0];
    }

    public Persona ancestroComunMasCercano(Persona p1, Persona p2) 
    {
        List<Persona> camino1 = obtenerCaminoDesdeRaiz(p1);
        List<Persona> camino2 = obtenerCaminoDesdeRaiz(p2);

        if (camino1.isEmpty() || camino2.isEmpty()) 
        {
            return null; 
        }

        Persona ultimoAncestroComun = null;
        int minLargo = Math.min(camino1.size(), camino2.size());
        for (int i = 0; i < minLargo; i++) 
        {
            if (camino1.get(i).equals(camino2.get(i))) 
            {
                ultimoAncestroComun = camino1.get(i);
            } 
            else 
            {
                break; 
            }
        }
        return ultimoAncestroComun;
    }
}
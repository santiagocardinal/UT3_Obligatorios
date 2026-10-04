package com.example.Escenario;

public class Persona implements Comparable<Persona> 
{
    private String nombre;
    private int añoNacimiento;

    public Persona(String nombre, int añoNacimiento) 
    {
        this.nombre = nombre;
        this.añoNacimiento = añoNacimiento;
    }

    public String getNombre()
    {
        return nombre;
    }

    public int getAñoNacimiento()
    {
        return añoNacimiento;
    }

    @Override
    public int compareTo(Persona otra) 
    {
        return this.nombre.compareToIgnoreCase(otra.nombre);
    }
}

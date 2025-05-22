package com.example.pastiapp;

public class MedicamentoAgendado {
    private String nombre;
    private int frecuencia;
    private int duracion;
    private boolean porHoras;

    public MedicamentoAgendado(String nombre, int frecuencia, int duracion, boolean porHoras) {
        this.nombre = nombre;
        this.frecuencia = frecuencia;
        this.duracion = duracion;
        this.porHoras = porHoras;
    }

    public String getNombre() { return nombre; }
    public int getFrecuencia() { return frecuencia; }
    public int getDuracion() { return duracion; }
    public boolean isPorHoras() { return porHoras; }
}

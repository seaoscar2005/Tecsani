package com.example.pastiapp;

public class Medicamento {
    private String nombre;
    private String dosis;
    private String unidad;
    private String fecha;
    private String tipo;

    public Medicamento(String nombre, String dosis, String unidad, String fecha, String tipo) {
        this.nombre = nombre;
        this.dosis = dosis;
        this.unidad = unidad;
        this.fecha = fecha;
        this.tipo = tipo;
    }

    public String getNombre() { return nombre; }
    public String getDosis() { return dosis; }
    public String getUnidad() { return unidad; }
    public String getFecha() { return fecha; }
    public String getTipo() { return tipo; }
}

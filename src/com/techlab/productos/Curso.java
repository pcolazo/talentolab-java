package com.techlab.productos;

// Herencia: un Curso es un Producto con un nivel (Inicial / Intermedio / Avanzado)
public class Curso extends Producto {
    private final String nivel;

    public Curso(String nombre, double precio, int stock, String nivel) {
        super(nombre, precio, stock);
        this.nivel = nivel;
    }

    public String getNivel() {
        return nivel;
    }

    @Override
    public String getCategoria() {
        return nivel;
    }
}

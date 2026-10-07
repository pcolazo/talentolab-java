package com.techlab.productos;

import com.techlab.excepciones.StockInsuficienteException;

public class Producto {
    // Variable de clase: genera IDs autoincrementales (1, 2, 3...)
    private static int contadorId = 0;

    private final int id;
    private String nombre;
    private double precio;
    private int stock;

    public Producto(String nombre, double precio, int stock) {
        // Se valida ANTES de asignar el ID, así un producto inválido no "gasta" un número
        validarNombre(nombre);
        validarPrecio(precio);
        validarStock(stock);
        this.id = ++contadorId;
        this.nombre = nombre.trim();
        this.precio = precio;
        this.stock = stock;
    }

    public int getId() {
        return id;
    }

    public String getNombre() {
        return nombre;
    }

    public double getPrecio() {
        return precio;
    }

    public int getStock() {
        return stock;
    }

    public void setNombre(String nombre) {
        validarNombre(nombre);
        this.nombre = nombre.trim();
    }

    public void setPrecio(double precio) {
        validarPrecio(precio);
        this.precio = precio;
    }

    public void setStock(int stock) {
        validarStock(stock);
        this.stock = stock;
    }

    // Polimorfismo: las subclases (ej. Curso) pueden especializar la categoría
    public String getCategoria() {
        return "General";
    }

    // Descuenta stock al confirmarse un pedido
    public void descontarStock(int cantidad) throws StockInsuficienteException {
        if (cantidad <= 0) {
            throw new IllegalArgumentException("La cantidad a descontar debe ser mayor a 0.");
        }
        if (cantidad > stock) {
            throw new StockInsuficienteException(nombre, stock, cantidad);
        }
        stock -= cantidad;
    }

    private static void validarNombre(String nombre) {
        if (nombre == null || nombre.trim().isEmpty()) {
            throw new IllegalArgumentException("El nombre no puede estar vacío.");
        }
    }

    private static void validarPrecio(double precio) {
        // "!(precio >= 0)" también rechaza NaN; además se descarta el infinito
        if (!(precio >= 0) || Double.isInfinite(precio)) {
            throw new IllegalArgumentException("El precio debe ser un número mayor o igual a 0.");
        }
    }

    private static void validarStock(int stock) {
        if (stock < 0) {
            throw new IllegalArgumentException("El stock no puede ser negativo.");
        }
    }
}

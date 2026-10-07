package com.techlab.servicios;

import com.techlab.excepciones.ProductoNoEncontradoException;
import com.techlab.productos.Curso;
import com.techlab.productos.Producto;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

// Encapsula la lógica de agregar / listar / buscar / eliminar productos
public class ProductoService {
    private final ArrayList<Producto> productos = new ArrayList<>();

    // Sobrecarga 1: crea el producto a partir de sus datos
    public Producto agregar(String nombre, double precio, int stock) {
        Producto producto = new Producto(nombre, precio, stock);
        productos.add(producto);
        return producto;
    }

    // Sobrecarga 2: agrega un producto ya creado (por ejemplo, un Curso)
    public void agregar(Producto producto) {
        productos.add(producto);
    }

    public List<Producto> listar() {
        return Collections.unmodifiableList(productos);
    }

    public Producto buscarPorId(int id) throws ProductoNoEncontradoException {
        for (Producto p : productos) {
            if (p.getId() == id) {
                return p;
            }
        }
        throw new ProductoNoEncontradoException("No existe un producto con ID " + id + ".");
    }

    // Busca por coincidencia parcial, sin distinguir mayúsculas
    public List<Producto> buscarPorNombre(String texto) throws ProductoNoEncontradoException {
        if (texto == null || texto.trim().isEmpty()) {
            throw new IllegalArgumentException("Debe ingresar un texto para buscar.");
        }
        String buscado = texto.trim().toLowerCase();
        ArrayList<Producto> coincidencias = new ArrayList<>();
        for (Producto p : productos) {
            if (p.getNombre().toLowerCase().contains(buscado)) {
                coincidencias.add(p);
            }
        }
        if (coincidencias.isEmpty()) {
            throw new ProductoNoEncontradoException("No se encontraron productos que contengan '" + texto.trim() + "'.");
        }
        return coincidencias;
    }

    public void eliminar(int id) throws ProductoNoEncontradoException {
        Producto producto = buscarPorId(id);
        productos.remove(producto);
    }

    // Catálogo inicial: los mismos 10 cursos del e-commerce anterior (productos.json)
    public void cargarCatalogoInicial() {
        agregar(new Curso("Curso de HTML y CSS Inicial", 180000, 10, "Inicial"));
        agregar(new Curso("Curso de JavaScript Inicial", 129990, 5, "Inicial"));
        agregar(new Curso("Curso de React Inicial", 110000, 15, "Inicial"));
        agregar(new Curso("Curso de HTML y CSS Intermedio", 280000, 20, "Intermedio"));
        agregar(new Curso("Curso de JavaScript Intermedio", 249990, 10, "Intermedio"));
        agregar(new Curso("Curso de React Intermedio", 220000, 30, "Intermedio"));
        agregar(new Curso("Curso de HTML y CSS Avanzado", 280000, 40, "Avanzado"));
        agregar(new Curso("Curso de JavaScript Avanzado", 369990, 15, "Avanzado"));
        agregar(new Curso("Curso de React Avanzado", 440000, 45, "Avanzado"));
        agregar(new Curso("Curso Full Stack Avanzado", 880000, 100, "Avanzado"));
    }
}

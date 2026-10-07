package com.techlab.pedidos;

import com.techlab.excepciones.StockInsuficienteException;
import com.techlab.productos.Producto;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Pedido {
    private static int contadorId = 0;

    private int id; // 0 hasta que el pedido se confirma
    private final ArrayList<LineaPedido> lineas = new ArrayList<>();
    private boolean confirmado = false;

    // Agrega un producto al pedido validando que alcance el stock.
    // Si el producto ya estaba en el pedido, suma la cantidad a su línea.
    public void agregarLinea(Producto producto, int cantidad) throws StockInsuficienteException {
        if (confirmado) {
            throw new IllegalStateException("El pedido ya fue confirmado.");
        }
        if (cantidad <= 0) {
            throw new IllegalArgumentException("La cantidad debe ser mayor a 0.");
        }
        LineaPedido existente = buscarLinea(producto);
        int totalSolicitado = (existente == null ? 0 : existente.getCantidad()) + cantidad;
        if (totalSolicitado > producto.getStock()) {
            throw new StockInsuficienteException(producto.getNombre(), producto.getStock(), totalSolicitado);
        }
        if (existente == null) {
            lineas.add(new LineaPedido(producto, cantidad));
        } else {
            existente.sumarCantidad(cantidad);
        }
    }

    public double calcularTotal() {
        double total = 0;
        for (LineaPedido linea : lineas) {
            total += linea.getSubtotal();
        }
        return total;
    }

    // Confirma el pedido: primero verifica TODO el stock y recién después lo descuenta,
    // así nunca queda un pedido a medio descontar.
    public void confirmar() throws StockInsuficienteException {
        if (confirmado) {
            throw new IllegalStateException("El pedido ya fue confirmado.");
        }
        if (lineas.isEmpty()) {
            throw new IllegalStateException("No se puede confirmar un pedido vacío.");
        }
        for (LineaPedido linea : lineas) {
            Producto p = linea.getProducto();
            if (linea.getCantidad() > p.getStock()) {
                throw new StockInsuficienteException(p.getNombre(), p.getStock(), linea.getCantidad());
            }
        }
        for (LineaPedido linea : lineas) {
            linea.getProducto().descontarStock(linea.getCantidad());
        }
        this.id = ++contadorId;
        this.confirmado = true;
    }

    public int getId() {
        return id;
    }

    public boolean isConfirmado() {
        return confirmado;
    }

    public boolean estaVacio() {
        return lineas.isEmpty();
    }

    public List<LineaPedido> getLineas() {
        return Collections.unmodifiableList(lineas);
    }

    private LineaPedido buscarLinea(Producto producto) {
        for (LineaPedido linea : lineas) {
            if (linea.getProducto() == producto) {
                return linea;
            }
        }
        return null;
    }
}

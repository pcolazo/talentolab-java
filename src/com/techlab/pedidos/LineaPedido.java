package com.techlab.pedidos;

import com.techlab.productos.Producto;

// Una línea del pedido: un producto, la cantidad pedida y el precio al momento de pedir
public class LineaPedido {
    private final Producto producto;
    private int cantidad;
    private final double precioUnitario;

    public LineaPedido(Producto producto, int cantidad) {
        if (cantidad <= 0) {
            throw new IllegalArgumentException("La cantidad debe ser mayor a 0.");
        }
        this.producto = producto;
        this.cantidad = cantidad;
        // Se guarda el precio del momento: si después cambia, el pedido ya hecho no se altera
        this.precioUnitario = producto.getPrecio();
    }

    public Producto getProducto() {
        return producto;
    }

    public int getCantidad() {
        return cantidad;
    }

    public double getPrecioUnitario() {
        return precioUnitario;
    }

    public double getSubtotal() {
        return precioUnitario * cantidad;
    }

    // Visibilidad de paquete: solo Pedido puede modificar la cantidad de una línea
    void sumarCantidad(int cantidadAdicional) {
        this.cantidad += cantidadAdicional;
    }
}

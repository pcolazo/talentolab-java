package com.techlab;

import com.techlab.excepciones.ProductoNoEncontradoException;
import com.techlab.excepciones.StockInsuficienteException;
import com.techlab.pedidos.LineaPedido;
import com.techlab.pedidos.Pedido;
import com.techlab.productos.Producto;
import com.techlab.servicios.PedidoService;
import com.techlab.servicios.ProductoService;
import com.techlab.util.Consola;
import com.techlab.util.Formato;

import java.util.List;
import java.util.NoSuchElementException;

// Clase principal: muestra el menú y delega el trabajo en los servicios
public class Main {
    private static final int OPCION_SALIR = 7;

    private static final ProductoService productoService = new ProductoService();
    private static final PedidoService pedidoService = new PedidoService();
    private static final Consola consola = new Consola();

    public static void main(String[] args) {
        productoService.cargarCatalogoInicial();
        int opcion = 0;
        try {
            do {
                mostrarMenu();
                opcion = consola.leerEntero("Elija una opción: ");
                ejecutarOpcion(opcion);
            } while (opcion != OPCION_SALIR);
        } catch (NoSuchElementException e) {
            // La entrada se cerró (por ejemplo, Ctrl+D): se sale ordenadamente
            System.out.println();
            System.out.println("Entrada finalizada.");
        }
        System.out.println("¡Hasta luego!");
    }

    private static void mostrarMenu() {
        System.out.println();
        System.out.println("=================================== SISTEMA DE GESTIÓN - TECHLAB ==================================");
        System.out.println();
        System.out.println("1) Agregar producto");
        System.out.println("2) Listar productos");
        System.out.println("3) Buscar/Actualizar producto");
        System.out.println("4) Eliminar producto");
        System.out.println("5) Crear un pedido");
        System.out.println("6) Listar pedidos");
        System.out.println("7) Salir");
        System.out.println();
    }

    private static void ejecutarOpcion(int opcion) {
        System.out.println();
        switch (opcion) {
            case 1 -> agregarProducto();
            case 2 -> listarProductos();
            case 3 -> buscarActualizarProducto();
            case 4 -> eliminarProducto();
            case 5 -> crearPedido();
            case 6 -> listarPedidos();
            case OPCION_SALIR -> { }
            default -> System.out.println("Opción inválida: elija un número del 1 al 7.");
        }
    }

    // ---------------------------------------------------------------- Productos

    private static void agregarProducto() {
        String nombre = consola.leerTexto("Nombre del producto: ");
        double precio = consola.leerDecimal("Precio: ");
        int stock = consola.leerEntero("Cantidad en stock: ");
        try {
            Producto producto = productoService.agregar(nombre, precio, stock);
            System.out.println("Producto agregado con ID " + producto.getId() + ".");
        } catch (IllegalArgumentException e) {
            System.out.println("No se pudo agregar el producto: " + e.getMessage());
        }
    }

    private static void listarProductos() {
        if (productoService.listar().isEmpty()) {
            System.out.println("No hay productos registrados.");
            return;
        }
        imprimirTabla(productoService.listar());
    }

    private static void buscarActualizarProducto() {
        Producto producto = seleccionarProducto();
        if (producto == null) {
            return;
        }
        System.out.println();
        System.out.println("Producto encontrado:");
        imprimirTabla(List.of(producto));
        if (consola.confirmar("¿Desea actualizar este producto?")) {
            actualizarProducto(producto);
        }
    }

    // Busca por ID o por nombre; devuelve el producto elegido o null si no hubo resultado
    private static Producto seleccionarProducto() {
        int modo = consola.leerEntero("Buscar por (1) ID o (2) nombre: ");
        try {
            if (modo == 1) {
                return productoService.buscarPorId(consola.leerEntero("ID del producto: "));
            }
            if (modo == 2) {
                String texto = consola.leerTexto("Nombre (o parte del nombre): ");
                return elegirEntreCoincidencias(productoService.buscarPorNombre(texto));
            }
            System.out.println("Opción inválida: elija 1 o 2.");
        } catch (ProductoNoEncontradoException | IllegalArgumentException e) {
            System.out.println(e.getMessage());
        }
        return null;
    }

    private static Producto elegirEntreCoincidencias(List<Producto> coincidencias) {
        if (coincidencias.size() == 1) {
            return coincidencias.get(0);
        }
        System.out.println("Se encontraron " + coincidencias.size() + " productos:");
        imprimirTabla(coincidencias);
        int id = consola.leerEntero("ID del producto elegido: ");
        for (Producto p : coincidencias) {
            if (p.getId() == id) {
                return p;
            }
        }
        System.out.println("El ID " + id + " no está entre los resultados.");
        return null;
    }

    private static void actualizarProducto(Producto producto) {
        int campo = consola.leerEntero("¿Qué desea actualizar? (1) Precio (2) Stock: ");
        try {
            if (campo == 1) {
                producto.setPrecio(consola.leerDecimal("Nuevo precio: "));
            } else if (campo == 2) {
                producto.setStock(consola.leerEntero("Nuevo stock: "));
            } else {
                System.out.println("Opción inválida: elija 1 o 2.");
                return;
            }
            System.out.println("Producto actualizado:");
            imprimirTabla(List.of(producto));
        } catch (IllegalArgumentException e) {
            System.out.println("No se actualizó el producto: " + e.getMessage());
        }
    }

    private static void eliminarProducto() {
        int id = consola.leerEntero("ID del producto a eliminar: ");
        try {
            Producto producto = productoService.buscarPorId(id);
            imprimirTabla(List.of(producto));
            if (consola.confirmar("¿Está seguro de que desea eliminarlo?")) {
                productoService.eliminar(id);
                System.out.println("Producto eliminado.");
            } else {
                System.out.println("Eliminación cancelada.");
            }
        } catch (ProductoNoEncontradoException e) {
            System.out.println(e.getMessage());
        }
    }

    // ------------------------------------------------------------------ Pedidos

    private static void crearPedido() {
        if (productoService.listar().isEmpty()) {
            System.out.println("No hay productos disponibles para armar un pedido.");
            return;
        }
        listarProductos();
        System.out.println();
        int cantidadItems = consola.leerEntero("¿Cuántos productos distintos va a agregar al pedido? ");
        Pedido pedido = new Pedido();
        for (int i = 1; i <= cantidadItems; i++) {
            System.out.println();
            System.out.println("Ítem " + i + " de " + cantidadItems);
            agregarItemAlPedido(pedido);
        }
        finalizarPedido(pedido);
    }

    // Pide ID y cantidad; si hay un error permite reintentar o saltear el ítem
    private static void agregarItemAlPedido(Pedido pedido) {
        boolean terminado = false;
        while (!terminado) {
            try {
                Producto producto = productoService.buscarPorId(consola.leerEntero("ID del producto: "));
                int cantidad = consola.leerEntero("Cantidad: ");
                pedido.agregarLinea(producto, cantidad);
                System.out.println("Agregado: " + cantidad + " x " + producto.getNombre());
                terminado = true;
            } catch (ProductoNoEncontradoException | StockInsuficienteException | IllegalArgumentException e) {
                System.out.println("Error: " + e.getMessage());
                terminado = !consola.confirmar("¿Desea reintentar este ítem?");
            }
        }
    }

    private static void finalizarPedido(Pedido pedido) {
        System.out.println();
        if (pedido.estaVacio()) {
            System.out.println("El pedido quedó vacío: se cancela.");
            return;
        }
        System.out.println("Resumen del pedido:");
        imprimirPedido(pedido);
        if (!consola.confirmar("¿Confirmar el pedido?")) {
            System.out.println("Pedido cancelado. El stock no fue modificado.");
            return;
        }
        try {
            pedidoService.registrar(pedido);
            System.out.println("Pedido #" + pedido.getId() + " confirmado. Total: "
                    + Formato.monto(pedido.calcularTotal()));
        } catch (StockInsuficienteException e) {
            System.out.println("No se pudo confirmar el pedido: " + e.getMessage());
        }
    }

    private static void listarPedidos() {
        if (pedidoService.listar().isEmpty()) {
            System.out.println("Todavía no se realizaron pedidos.");
            return;
        }
        for (Pedido pedido : pedidoService.listar()) {
            imprimirPedido(pedido);
            System.out.println();
        }
    }

    // ------------------------------------------------------------- Presentación

    private static void imprimirTabla(List<Producto> productos) {
        System.out.printf("%-4s %-34s %-12s %16s %6s%n", "ID", "Nombre", "Categoría", "Precio", "Stock");
        for (Producto p : productos) {
            System.out.printf("%-4d %-34s %-12s %16s %6d%n",
                    p.getId(), p.getNombre(), p.getCategoria(), Formato.monto(p.getPrecio()), p.getStock());
        }
    }

    private static void imprimirPedido(Pedido pedido) {
        System.out.println(pedido.isConfirmado() ? "Pedido #" + pedido.getId() : "Pedido (sin confirmar)");
        for (LineaPedido linea : pedido.getLineas()) {
            System.out.println("   " + linea.getCantidad() + " x " + linea.getProducto().getNombre()
                    + " @ " + Formato.monto(linea.getPrecioUnitario())
                    + " = " + Formato.monto(linea.getSubtotal()));
        }
        System.out.println("   TOTAL: " + Formato.monto(pedido.calcularTotal()));
    }
}

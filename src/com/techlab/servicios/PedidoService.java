package com.techlab.servicios;

import com.techlab.excepciones.StockInsuficienteException;
import com.techlab.pedidos.Pedido;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

// Guarda los pedidos confirmados
public class PedidoService {
    private final ArrayList<Pedido> pedidos = new ArrayList<>();

    // Confirma el pedido (descuenta stock) y, solo si salió bien, lo registra
    public void registrar(Pedido pedido) throws StockInsuficienteException {
        pedido.confirmar();
        pedidos.add(pedido);
    }

    public List<Pedido> listar() {
        return Collections.unmodifiableList(pedidos);
    }
}

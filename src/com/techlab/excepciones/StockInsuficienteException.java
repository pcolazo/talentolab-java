package com.techlab.excepciones;

// Excepción verificada: se lanza cuando se pide más cantidad que el stock disponible
public class StockInsuficienteException extends Exception {
    private final int stockDisponible;
    private final int cantidadSolicitada;

    public StockInsuficienteException(String nombreProducto, int stockDisponible, int cantidadSolicitada) {
        super("Stock insuficiente para '" + nombreProducto + "': hay " + stockDisponible
                + " unidades disponibles y se solicitaron " + cantidadSolicitada + ".");
        this.stockDisponible = stockDisponible;
        this.cantidadSolicitada = cantidadSolicitada;
    }

    public int getStockDisponible() {
        return stockDisponible;
    }

    public int getCantidadSolicitada() {
        return cantidadSolicitada;
    }
}

package com.techlab.excepciones;

// Excepción verificada: se lanza cuando se busca un producto que no existe
public class ProductoNoEncontradoException extends Exception {
    public ProductoNoEncontradoException(String mensaje) {
        super(mensaje);
    }
}

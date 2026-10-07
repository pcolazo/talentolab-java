package com.techlab.util;

import java.util.Scanner;

// Lectura de datos por consola con validación: nunca se cae por un dato mal escrito
public class Consola {
    private final Scanner scanner = new Scanner(System.in);

    public String leerTexto(String mensaje) {
        System.out.print(mensaje);
        return scanner.nextLine();
    }

    public int leerEntero(String mensaje) {
        while (true) {
            String texto = leerTexto(mensaje);
            try {
                return Integer.parseInt(texto.trim());
            } catch (NumberFormatException e) {
                System.out.println("Valor inválido: ingrese un número entero (ej: 5).");
            }
        }
    }

    public double leerDecimal(String mensaje) {
        while (true) {
            String texto = leerTexto(mensaje);
            try {
                // Acepta coma o punto decimal: "1500,50" o "1500.50"
                return Double.parseDouble(texto.trim().replace(',', '.'));
            } catch (NumberFormatException e) {
                System.out.println("Valor inválido: ingrese un número (ej: 1500.50).");
            }
        }
    }

    public boolean confirmar(String mensaje) {
        while (true) {
            String respuesta = leerTexto(mensaje + " (s/n): ").trim().toLowerCase();
            if (respuesta.equals("s") || respuesta.equals("si") || respuesta.equals("sí")) {
                return true;
            }
            if (respuesta.equals("n") || respuesta.equals("no")) {
                return false;
            }
            System.out.println("Respuesta inválida: escriba 's' o 'n'.");
        }
    }
}

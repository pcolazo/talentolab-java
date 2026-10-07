package com.techlab.util;

import java.util.Locale;

public class Formato {
    private static final Locale ARGENTINA = Locale.of("es", "AR");

    // Ej: 180000 -> "$180.000,00"
    public static String monto(double valor) {
        return String.format(ARGENTINA, "$%,.2f", valor);
    }
}

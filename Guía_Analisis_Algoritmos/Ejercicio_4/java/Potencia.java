/*
 * ============================================================================
 * PROMPT INICIAL UTILIZADO PARA GENERAR ESTA SOLUCIÓN
 * ============================================================================
 *
 * Quiero implementar en Java una función recursiva que calcule una potencia sin
 * usar `Math.pow`.
 *
 * Una potencia puede verse como multiplicaciones sucesivas: elevar `base` al
 * exponente `n` significa multiplicar `base` por sí misma `n` veces.
 *
 * El caso base es cuando el exponente vale `0`, porque cualquier número elevado
 * a cero devuelve `1`. Para este ejercicio, considera también que `0^0` devuelve
 * `1`, siguiendo esta regla.
 *
 * El caso recursivo es `base * potencia(base, exponente - 1)`: en cada llamada se
 * reduce el exponente en uno hasta llegar a cero. Si el exponente es negativo, la
 * función debe rechazarlo con un error descriptivo, ya que no alcanzaría el caso
 * base.
 *
 * La función debe devolver el resultado de la potencia, sin utilizar `Math.pow`.
 * Incluye comentarios que expliquen el caso base, la reducción del exponente y
 * cómo se resuelven las llamadas al regresar del caso base. Aclara también que
 * los tipos numéricos de Java tienen límites y que un resultado grande podría
 * desbordarlos.
 *
 * Prueba estos casos:
 *   - potencia(2, 0),  resultado esperado: 1
 *   - potencia(5, 1),  resultado esperado: 5
 *   - potencia(2, 4),  resultado esperado: 16
 *   - potencia(0, 5),  resultado esperado: 0
 *   - potencia(-2, 3), resultado esperado: -8
 *   - potencia(2, -1), debe rechazar el exponente negativo
 * ============================================================================
 */

/**
 * Potencia entera calculada de forma recursiva, sin emplear {@code Math.pow}.
 *
 * <p>La potencia se define de forma recurrente como
 * {@code potencia(base, n) = base * potencia(base, n - 1)}, con el caso base
 * {@code potencia(base, 0) = 1}. Es decir, elevar {@code base} al exponente
 * {@code n} equivale a multiplicar {@code base} por sí misma {@code n} veces.</p>
 *
 * <p>Por convención, {@code 0^0 = 1}, de modo que {@code potencia(0, 0)} también
 * devuelve {@code 1}.</p>
 *
 * @author Agente Desarrollador
 */
public final class Potencia {

    /**
     * Profundidad máxima de recursión admitida. El exponente es quien controla el
     * número de llamadas recursivas, por lo que se rechazan los exponentes que
     * superarían este límite. Así un eventual {@link StackOverflowError} se
     * transforma en un error descriptivo y determinista.
     *
     * <p>Esto es especialmente importante para bases especiales como {@code 0},
     * {@code 1} o {@code -1}, cuyo resultado no desborda pero cuya recursión sí
     * podría agotar la pila con exponentes muy grandes.</p>
     */
    private static final int MAX_EXPONENTE = 10_000;

    /** Impide instanciar una clase que solo agrupa métodos estáticos. */
    private Potencia() {
    }

    // ------------------------------------------------------------------------
    // API PÚBLICA
    // ------------------------------------------------------------------------

    /**
     * Calcula, de forma recursiva, {@code base} elevado a {@code exponente}.
     *
     * @param base       número que se multiplica por sí mismo
     * @param exponente  número de veces que se multiplica la base; debe ser no
     *                   negativo
     * @return el resultado de {@code base ^ exponente}
     * @throws IllegalArgumentException si {@code exponente} es negativo o supera
     *         el límite de profundidad recursiva admitido
     * @throws ArithmeticException      si el resultado no cabe en un {@code long}
     *         (desbordamiento numérico)
     */
    public static long potencia(int base, int exponente) {
        // La validación se hace UNA sola vez, en el método público. Así la
        // recursión interna no repite comprobaciones en cada nivel de la pila.
        validar(exponente);
        try {
            return potenciaRecursiva(base, exponente);
        } catch (ArithmeticException desbordamiento) {
            // Se traduce el fallo genérico de la JVM en un mensaje que explica
            // exactamente qué operación desbordó y por qué.
            throw new ArithmeticException(
                    "Desbordamiento al calcular " + base + "^" + exponente
                            + ": el resultado excede el rango del tipo long ["
                            + Long.MIN_VALUE + ", " + Long.MAX_VALUE + "].");
        }
    }

    /**
     * Convierte un texto a entero, rechazando de forma descriptiva las entradas
     * que no sean números enteros válidos.
     *
     * @param texto cadena a convertir
     * @return el entero representado por {@code texto}
     * @throws IllegalArgumentException si el texto es nulo, está vacío o no es un
     *         número entero válido
     */
    public static int parsear(String texto) {
        if (texto == null || texto.trim().isEmpty()) {
            throw new IllegalArgumentException("La entrada no puede ser nula ni vacía.");
        }
        try {
            return Integer.parseInt(texto.trim());
        } catch (NumberFormatException excepcion) {
            throw new IllegalArgumentException(
                    "La entrada no es un número entero válido: \"" + texto + "\"");
        }
    }

    // ------------------------------------------------------------------------
    // NÚCLEO RECURSIVO Y VALIDACIÓN
    // ------------------------------------------------------------------------

    /**
     * Núcleo recursivo. Se asume que {@code exponente} ya fue validado por
     * {@link #potencia(int, int)}.
     *
     * @param base      base de la potencia
     * @param exponente exponente no negativo ya validado
     * @return {@code base ^ exponente}
     */
    private static long potenciaRecursiva(int base, int exponente) {
        // --------------------------------------------------------------------
        // CASO BASE: exponente == 0.
        // Cualquier número elevado a cero vale 1 (incluida la convención
        // 0^0 = 1). Se devuelve 1 SIN volver a llamar a potenciaRecursiva(...):
        // aquí se detiene la recursión.
        // --------------------------------------------------------------------
        if (exponente == 0) {
            return 1L;
        }

        // --------------------------------------------------------------------
        // CASO RECURSIVO: potencia(base, n) = base * potencia(base, n - 1).
        // En cada llamada el exponente se REDUCE en uno (n -> n - 1), por lo que
        // tarde o temprano se alcanza el caso base (n == 0).
        //
        // LA PILA DE LLAMADAS: cada invocación queda "esperando" el resultado de
        // potenciaRecursiva(base, n - 1) antes de poder multiplicar. Al llegar al
        // caso base se comienza a DESENROLLAR la pila, multiplicando de abajo
        // hacia arriba.
        //
        // Ejemplo para potencia(2, 4):
        //   potencia(2, 4) -> 2 * potencia(2, 3)   [pendiente en la pila]
        //   potencia(2, 3) -> 2 * potencia(2, 2)   [pendiente en la pila]
        //   potencia(2, 2) -> 2 * potencia(2, 1)   [pendiente en la pila]
        //   potencia(2, 1) -> 2 * potencia(2, 0)   [pendiente en la pila]
        //   potencia(2, 0) -> return 1             [CASO BASE: se detiene]
        //   ...y ahora la pila se resuelve al regresar:
        //   potencia(2, 1) = 2 * 1 = 2
        //   potencia(2, 2) = 2 * 2 = 4
        //   potencia(2, 3) = 2 * 4 = 8
        //   potencia(2, 4) = 2 * 8 = 16
        //
        // DESBORDAMIENTO: los tipos numéricos de Java tienen un rango finito. Un
        // long almacena, como máximo, 9_223_372_036_854_775_807. Potencias grandes
        // (por ejemplo 2^63) superan ese límite y "darían la vuelta" en silencio.
        // Para evitar resultados engañosos se usa Math.multiplyExact, que es
        // equivalente a 'base * potenciaRecursiva(...)' pero lanza
        // ArithmeticException si el producto no cabe en un long.
        // --------------------------------------------------------------------
        return Math.multiplyExact((long) base, potenciaRecursiva(base, exponente - 1));
    }

    /**
     * Valida el exponente recibido por {@link #potencia(int, int)}.
     *
     * @param exponente valor a validar
     * @throws IllegalArgumentException si {@code exponente} es negativo o excede
     *         el límite de profundidad recursiva
     */
    private static void validar(int exponente) {
        // Dominio válido: exponentes no negativos. Un exponente negativo jamás
        // alcanzaría el caso base (la reducción n -> n - 1 se alejaría de cero)
        // y además introduciría fracciones, que no caben en un resultado entero.
        if (exponente < 0) {
            throw new IllegalArgumentException(
                    "El exponente no puede ser negativo: " + exponente
                            + ". Un exponente negativo no alcanzaría el caso base y el "
                            + "resultado no es un entero.");
        }

        // Límite explícito de profundidad para no provocar un StackOverflowError.
        if (exponente > MAX_EXPONENTE) {
            throw new IllegalArgumentException(
                    "exponente = " + exponente + " excede la profundidad recursiva máxima "
                            + "permitida (" + MAX_EXPONENTE + ").");
        }
    }

    // ------------------------------------------------------------------------
    // CASOS DE PRUEBA
    // ------------------------------------------------------------------------

    /**
     * Método de entrada con los casos de prueba solicitados en el enunciado.
     *
     * @param args argumentos opcionales; si se pasan dos valores se interpretan
     *             como base y exponente respectivamente
     */
    public static void main(String[] args) {
        System.out.println("== Casos de prueba solicitados ==");
        probar(2, 0, 1L);
        probar(5, 1, 5L);
        probar(2, 4, 16L);
        probar(0, 5, 0L);
        probar(-2, 3, -8L);

        System.out.println();
        System.out.println("== Exponente negativo (debe rechazarse) ==");
        probarArgumentoInvalido(2, -1);

        System.out.println();
        System.out.println("== Casos adicionales ==");
        probar(0, 0, 1L);   // convención 0^0 = 1
        probar(7, 0, 1L);   // todo número elevado a cero es 1
        probar(-2, 2, 4L);  // base negativa con exponente par -> positivo
        probar(2, 10, 1_024L);
        probar(3, 5, 243L);

        System.out.println();
        System.out.println("== Límite del tipo long (desbordamiento) ==");
        probar(2, 62, 4_611_686_018_427_387_904L); // 2^62: cabe en long
        probarDesbordamiento(2, 63);               // 2^63: NO cabe en long

        // Si se reciben dos argumentos por línea de comandos, se procesan.
        if (args.length >= 2) {
            System.out.println();
            System.out.println("== Entrada por linea de comandos ==");
            probarEntradaUsuario(args[0], args[1]);
        }
    }

    /**
     * Ejecuta un caso de prueba y muestra si el resultado coincide con lo esperado.
     *
     * @param base      base de la potencia
     * @param exponente exponente de la potencia
     * @param esperado  resultado esperado
     */
    private static void probar(int base, int exponente, long esperado) {
        long obtenido = potencia(base, exponente);
        boolean correcto = obtenido == esperado;
        System.out.printf("potencia(%d, %d) = %-20d | esperado = %-20d | %s%n",
                base, exponente, obtenido, esperado, correcto ? "OK" : "FALLO");
    }

    /**
     * Verifica que un exponente inválido sea rechazado con la excepción esperada.
     *
     * @param base      base de la potencia
     * @param exponente exponente inválido (negativo)
     */
    private static void probarArgumentoInvalido(int base, int exponente) {
        try {
            potencia(base, exponente);
            System.out.printf("potencia(%d, %d) -> FALLO (no se lanzo ninguna excepcion)%n",
                    base, exponente);
        } catch (IllegalArgumentException excepcion) {
            System.out.printf("potencia(%d, %d) -> OK (rechazado: %s)%n",
                    base, exponente, excepcion.getMessage());
        }
    }

    /**
     * Verifica que un resultado que supera el rango de {@code long} sea detectado.
     *
     * @param base      base de la potencia
     * @param exponente exponente que provoca desbordamiento
     */
    private static void probarDesbordamiento(int base, int exponente) {
        try {
            long obtenido = potencia(base, exponente);
            System.out.printf("potencia(%d, %d) -> FALLO (se obtuvo %d sin detectar desbordamiento)%n",
                    base, exponente, obtenido);
        } catch (ArithmeticException excepcion) {
            System.out.printf("potencia(%d, %d) -> OK (desbordamiento detectado: %s)%n",
                    base, exponente, excepcion.getMessage());
        }
    }

    /**
     * Procesa una entrada de usuario recibida como texto (base y exponente).
     *
     * @param textoBase      base en formato texto
     * @param textoExponente exponente en formato texto
     */
    private static void probarEntradaUsuario(String textoBase, String textoExponente) {
        try {
            int base = parsear(textoBase);
            int exponente = parsear(textoExponente);
            long resultado = potencia(base, exponente);
            System.out.printf("Entrada \"%s\", \"%s\": potencia(%d, %d) = %d%n",
                    textoBase, textoExponente, base, exponente, resultado);
        } catch (IllegalArgumentException | ArithmeticException excepcion) {
            System.out.printf("Entrada \"%s\", \"%s\": ERROR -> %s%n",
                    textoBase, textoExponente, excepcion.getMessage());
        }
    }
}

/*
 * ============================================================================
 * NOTAS DE IMPLEMENTACIÓN (aclaraciones al prompt inicial)
 * ============================================================================
 * 1. Se usó el tipo de retorno 'long' en lugar de 'int' para ampliar el rango
 *    representable (2^31 en vez de 2^15) y reducir la probabilidad de
 *    desbordamiento con exponentes moderados.
 * 2. La validación se concentra en el método público 'potencia', de modo que la
 *    recursión interna no repita comprobaciones en cada nivel.
 * 3. Se definió un límite explícito de profundidad (MAX_EXPONENTE) para
 *    transformar un eventual StackOverflowError en un error descriptivo. Es
 *    clave para bases 0, 1 y -1, cuyo resultado nunca desborda pero cuya
 *    recursión sí podría agotar la pila.
 * 4. El producto del paso recursivo se calcula con Math.multiplyExact (que NO es
 *    Math.pow): es equivalente a 'base * potenciaRecursiva(...)', pero detecta el
 *    desbordamiento de 'long' y lo informa con un mensaje claro en lugar de
 *    devolver un valor erróneo en silencio.
 * 5. Los tipos numéricos de Java tienen límites finitos: 'long' admite como
 *    máximo 9_223_372_036_854_775_807. Por eso 2^62 se calcula bien, pero 2^63
 *    no cabe y se reporta como desbordamiento.
 * ============================================================================
 */

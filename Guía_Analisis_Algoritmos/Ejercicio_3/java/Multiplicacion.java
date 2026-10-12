/*
 * ============================================================================
 * PROMPT INICIAL UTILIZADO PARA GENERAR ESTA SOLUCIÓN
 * ============================================================================
 *
 * Quiero implementar en Java una función recursiva que multiplique dos números
 * enteros no negativos sin usar el operador `*` en la implementación.
 *
 * Este problema puede resolverse recursivamente porque una multiplicación puede
 * representarse como sumas repetidas: multiplicar `a` por `b` equivale a sumar
 * `a` un total de `b` veces.
 *
 * El caso base ocurre cuando alguno de los factores vale `0`, porque cualquier
 * número multiplicado por cero es cero. En ese caso, la función debe devolver 0.
 *
 * El caso recursivo es `a + multiplicar(a, b - 1)`: en cada llamada se reduce el
 * segundo parámetro, `b`, hasta llegar a cero.
 *
 * La función debe devolver el producto de los dos factores, calculado únicamente
 * mediante sumas. Debe rechazar los valores negativos y explicar cómo se resuelve
 * la pila de llamadas al regresar del caso base.
 *
 * También quiero probarla con estos casos:
 *   - multiplicar(0, 5), resultado esperado: 0
 *   - multiplicar(5, 0), resultado esperado: 0
 *   - multiplicar(1, 4), resultado esperado: 4
 *   - multiplicar(3, 4), resultado esperado: 12
 *   - multiplicar(6, 2), resultado esperado: 12
 * ============================================================================
 */

/**
 * Producto de dos enteros no negativos calculado de forma recursiva mediante
 * sumas repetidas, sin emplear el operador {@code *}.
 *
 * <p>La multiplicación se define de forma recurrente como
 * {@code a * b = a + a * (b - 1)}, con el caso base {@code a * 0 = 0}. Es decir,
 * multiplicar {@code a} por {@code b} equivale a sumar {@code a} un total de
 * {@code b} veces.</p>
 *
 * <p>Como la multiplicación es conmutativa, el método público reduce el
 * <em>factor que controla la recursión</em> al menor de los dos operandos. Así la
 * profundidad de la pila es {@code min(a, b)} en lugar de {@code b}, lo que
 * permite manejar productos grandes sin agotar la pila en la mayoría de los
 * casos. El núcleo recursivo conserva exactamente la forma pedida
 * {@code sumando + multiplicarRecursivo(sumando, veces - 1)}.</p>
 *
 * @author Agente Desarrollador
 */
public final class Multiplicacion {

    /**
     * Profundidad máxima de recursión admitida. Cada llamada recursiva agrega un
     * nivel a la pila del programa; para transformar un eventual
     * {@link StackOverflowError} en un error descriptivo y determinista se
     * rechazan las entradas cuyo factor de control (el menor de los operandos)
     * superaría este límite.
     *
     * <p>El valor es conservador: con el tamaño de pila por defecto de la JVM la
     * recursión tolera bastantes más niveles, pero se fija un margen amplio para
     * que el comportamiento sea seguro en distintos entornos.</p>
     */
    private static final int MAX_PROFUNDIDAD_RECURSIVA = 10_000;

    /** Impide instanciar una clase que solo agrupa métodos estáticos. */
    private Multiplicacion() {
    }

    // ------------------------------------------------------------------------
    // API PÚBLICA
    // ------------------------------------------------------------------------

    /**
     * Calcula, de forma recursiva y usando únicamente sumas, el producto de
     * {@code a} por {@code b}.
     *
     * @param a primer factor, entero no negativo
     * @param b segundo factor, entero no negativo
     * @return el producto {@code a * b}
     * @throws IllegalArgumentException si alguno de los factores es negativo o si
     *         el factor que controla la recursión excede el límite de profundidad
     */
    public static long multiplicar(int a, int b) {
        // La validación se ejecuta UNA sola vez, en el método público. Así la
        // recursión interna no repite comprobaciones en cada nivel de la pila.
        validarFactores(a, b);

        // --------------------------------------------------------------------
        // CASO BASE (a nivel de API): si CUALQUIERA de los factores vale 0, el
        // producto es 0. Se devuelve sin realizar ninguna llamada recursiva.
        // Esto cubre los casos multiplicar(0, 5) y multiplicar(5, 0).
        // --------------------------------------------------------------------
        if (a == 0 || b == 0) {
            return 0L;
        }

        // --------------------------------------------------------------------
        // OPTIMIZACIÓN (conmutatividad): la multiplicación es conmutativa, así
        // que "sumar a, b veces" produce el mismo resultado que "sumar b, a
        // veces". Elegimos como 'veces' (factor de control) el MENOR de los dos
        // operandos para que la profundidad de la pila sea min(a, b).
        //
        // No es un cambio de algoritmo: el núcleo recursivo sigue siendo
        // "sumando + multiplicarRecursivo(sumando, veces - 1)". Solo se decide
        // qué operando hace de sumando y cuál de contador.
        // --------------------------------------------------------------------
        int veces = Math.min(a, b);
        long sumando = Math.max(a, b);

        // Validación explícita de profundidad con el valor ya acotado.
        if (veces > MAX_PROFUNDIDAD_RECURSIVA) {
            throw new IllegalArgumentException(
                    "min(" + a + ", " + b + ") = " + veces
                            + " excede la profundidad recursiva máxima permitida ("
                            + MAX_PROFUNDIDAD_RECURSIVA + ").");
        }

        return multiplicarRecursivo(sumando, veces);
    }

    /**
     * Referencia independiente: calcula el mismo producto con un bucle iterativo
     * de sumas repetidas (tampoco usa el operador {@code *}). Se emplea para
     * verificar el resultado de {@link #multiplicar(int, int)}.
     *
     * @param a primer factor, entero no negativo
     * @param b segundo factor, entero no negativo
     * @return el producto {@code a * b}
     * @throws IllegalArgumentException si alguno de los factores es negativo
     */
    public static long multiplicarIterativo(int a, int b) {
        validarFactores(a, b);

        int veces = Math.min(a, b);
        long sumando = Math.max(a, b);

        long producto = 0L;
        for (int i = 0; i < veces; i++) {
            producto += sumando;
        }
        return producto;
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
     * Núcleo recursivo. Se asume que {@code sumando} y {@code veces} ya fueron
     * validados por {@link #multiplicar(int, int)}.
     *
     * @param sumando cantidad que se acumula repetidamente (equivale al factor
     *                que NO se decrementa)
     * @param veces   número de veces que resta sumar {@code sumando}
     * @return el producto {@code sumando * veces}
     */
    private static long multiplicarRecursivo(long sumando, int veces) {
        // --------------------------------------------------------------------
        // CASO BASE: veces == 0.
        // Sumar "cero veces" cualquier cantidad da 0. Se devuelve 0 SIN volver a
        // llamar a multiplicarRecursivo(...): aquí se detiene la recursión.
        // --------------------------------------------------------------------
        if (veces == 0) {
            return 0L;
        }

        // --------------------------------------------------------------------
        // CASO RECURSIVO: multiplicarRecursivo(sumando, veces) =
        //                 sumando + multiplicarRecursivo(sumando, veces - 1).
        // En cada llamada el problema se REDUCE en uno (veces -> veces - 1), por
        // lo que tarde o temprano se alcanza el caso base (veces == 0).
        //
        // Nótese que aquí NO aparece el operador '*': todo se resuelve con '+' y
        // la resta que reduce el contador.
        //
        // LA PILA DE LLAMADAS: cada invocación queda "esperando" el resultado de
        // multiplicarRecursivo(sumando, veces - 1) antes de poder sumar. Al
        // llegar al caso base se comienza a DESENROLLAR la pila, sumando de
        // abajo hacia arriba.
        //
        // Ejemplo para multiplicar(3, 4) -> sumando = 4, veces = 3:
        //   multiplicarRecursivo(4, 3) -> 4 + multRec(4, 2)   [pendiente]
        //   multiplicarRecursivo(4, 2) -> 4 + multRec(4, 1)   [pendiente]
        //   multiplicarRecursivo(4, 1) -> 4 + multRec(4, 0)   [pendiente]
        //   multiplicarRecursivo(4, 0) -> return 0            [CASO BASE]
        //   ...y ahora la pila se resuelve al regresar (unwinding):
        //   multiplicarRecursivo(4, 1) = 4 + 0 = 4
        //   multiplicarRecursivo(4, 2) = 4 + 4 = 8
        //   multiplicarRecursivo(4, 3) = 4 + 8 = 12
        // --------------------------------------------------------------------
        return sumando + multiplicarRecursivo(sumando, veces - 1);
    }

    /**
     * Valida que ambos factores sean enteros no negativos.
     *
     * @param a primer factor
     * @param b segundo factor
     * @throws IllegalArgumentException si alguno es negativo
     */
    private static void validarFactores(int a, int b) {
        if (a < 0 || b < 0) {
            throw new IllegalArgumentException(
                    "La multiplicación solo está definida para enteros no negativos, "
                            + "pero se recibió (" + a + ", " + b + ").");
        }

        // NOTA SOBRE EL RANGO NUMÉRICO:
        // Con parámetros 'int', el producto máximo posible es
        // (2^31 - 1)^2 ≈ 4.61e18, que cabe holgadamente en un 'long' (≈9.22e18).
        // Por eso, al devolver 'long', el resultado nunca desborda y no se
        // requiere una validación adicional de rango para el producto.
    }

    // ------------------------------------------------------------------------
    // CASOS DE PRUEBA
    // ------------------------------------------------------------------------

    /**
     * Método de entrada con los casos de prueba solicitados en el prompt.
     *
     * @param args argumentos opcionales; si se pasan dos valores se interpretan
     *             como los factores {@code a} y {@code b} a multiplicar
     */
    public static void main(String[] args) {
        System.out.println("== Casos de prueba solicitados ==");
        probar(0, 5, 0L);
        probar(5, 0, 0L);
        probar(1, 4, 4L);
        probar(3, 4, 12L);
        probar(6, 2, 12L);

        System.out.println();
        System.out.println("== Coincidencia con la version iterativa (solo sumas) ==");
        probarCoincidenciaIterativa(0, 5);
        probarCoincidenciaIterativa(5, 0);
        probarCoincidenciaIterativa(1, 4);
        probarCoincidenciaIterativa(3, 4);
        probarCoincidenciaIterativa(6, 2);
        probarCoincidenciaIterativa(7, 8);
        probarCoincidenciaIterativa(123, 45);

        System.out.println();
        System.out.println("== Entradas invalidas ==");
        probarArgumentoInvalido(-1, 4);
        probarArgumentoInvalido(4, -1);
        probarArgumentoInvalido(-3, -3);
        probarNoEntero("3.5");
        probarNoEntero("abc");
        probarNoEntero("");

        System.out.println();
        System.out.println("== Limite de profundidad (factor de control = min(a, b)) ==");
        probarLimiteProfundidad(1, MAX_PROFUNDIDAD_RECURSIVA);
        probarArgumentoInvalido(MAX_PROFUNDIDAD_RECURSIVA + 1, MAX_PROFUNDIDAD_RECURSIVA + 1);

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
     * @param a        primer factor
     * @param b        segundo factor
     * @param esperado resultado esperado
     */
    private static void probar(int a, int b, long esperado) {
        long obtenido = multiplicar(a, b);
        boolean correcto = obtenido == esperado;
        System.out.printf("multiplicar(%d, %d) = %-6d | esperado = %-6d | %s%n",
                a, b, obtenido, esperado, correcto ? "OK" : "FALLO");
    }

    /**
     * Comprueba que el resultado recursivo coincida con el iterativo.
     *
     * @param a primer factor
     * @param b segundo factor
     */
    private static void probarCoincidenciaIterativa(int a, int b) {
        long recursivo = multiplicar(a, b);
        long iterativo = multiplicarIterativo(a, b);
        boolean correcto = recursivo == iterativo;
        System.out.printf("a = %-6d b = %-6d | recursivo = %-10d | iterativo = %-10d | %s%n",
                a, b, recursivo, iterativo, correcto ? "OK" : "FALLO");
    }

    /**
     * Verifica que un par de factores inválidos sea rechazado.
     *
     * @param a primer factor (al menos uno inválido)
     * @param b segundo factor
     */
    private static void probarArgumentoInvalido(int a, int b) {
        try {
            multiplicar(a, b);
            System.out.printf("multiplicar(%d, %d) -> FALLO (no se lanzo ninguna excepcion)%n",
                    a, b);
        } catch (IllegalArgumentException excepcion) {
            System.out.printf("multiplicar(%d, %d) -> OK (rechazado: %s)%n",
                    a, b, excepcion.getMessage());
        }
    }

    /**
     * Verifica que una entrada no entera sea rechazada al parsearla.
     *
     * @param texto entrada no entera
     */
    private static void probarNoEntero(String texto) {
        try {
            int valor = parsear(texto);
            System.out.printf("parsear(\"%s\") -> FALLO (se obtuvo %d)%n", texto, valor);
        } catch (IllegalArgumentException excepcion) {
            System.out.printf("parsear(\"%s\") -> OK (rechazado: %s)%n",
                    texto, excepcion.getMessage());
        }
    }

    /**
     * Verifica que un valor en el límite exacto de profundidad se calcule bien.
     *
     * @param a primer factor
     * @param b segundo factor (en el límite de profundidad)
     */
    private static void probarLimiteProfundidad(int a, int b) {
        long obtenido = multiplicar(a, b);
        long iterativo = multiplicarIterativo(a, b);
        boolean correcto = obtenido == iterativo;
        System.out.printf("multiplicar(%d, %d) [limite de profundidad] -> %d | iterativo = %d | %s%n",
                a, b, obtenido, iterativo, correcto ? "OK" : "FALLO");
    }

    /**
     * Procesa dos entradas de usuario recibidas como texto.
     *
     * @param textoA primer factor como texto
     * @param textoB segundo factor como texto
     */
    private static void probarEntradaUsuario(String textoA, String textoB) {
        try {
            int a = parsear(textoA);
            int b = parsear(textoB);
            long resultado = multiplicar(a, b);
            System.out.printf("Entrada (\"%s\", \"%s\"): multiplicar(%d, %d) = %d%n",
                    textoA, textoB, a, b, resultado);
        } catch (IllegalArgumentException excepcion) {
            System.out.printf("Entrada (\"%s\", \"%s\"): ERROR -> %s%n",
                    textoA, textoB, excepcion.getMessage());
        }
    }
}

/*
 * ============================================================================
 * NOTAS DE IMPLEMENTACIÓN (aclaraciones al prompt inicial)
 * ============================================================================
 * 1. El operador '*' NO se usa en ningún punto de la implementación: tanto el
 *    núcleo recursivo como la referencia iterativa se construyen solo con sumas.
 * 2. Se usó el tipo de retorno 'long' (y no 'int') porque el producto de dos
 *    enteros 'int' puede llegar a ≈4.61e18, valor que no cabe en 'int' pero sí
 *    en 'long'. Así el resultado es siempre exacto para cualquier entrada 'int'.
 * 3. La validación se concentra en el método público 'multiplicar', de modo que
 *    la recursión interna no repita comprobaciones en cada nivel.
 * 4. Por conmutatividad, se elige el MENOR de los factores como contador de la
 *    recursión, reduciendo la profundidad de la pila a min(a, b) sin alterar la
 *    forma recursiva pedida: sumando + multiplicarRecursivo(sumando, veces - 1).
 * 5. Se definió un límite explícito de profundidad (MAX_PROFUNDIDAD_RECURSIVA)
 *    para transformar un eventual StackOverflowError en un error descriptivo.
 * 6. Las entradas no enteras se rechazan en 'parsear' mediante
 *    IllegalArgumentException, que es la práctica estándar en Java.
 * ============================================================================
 */

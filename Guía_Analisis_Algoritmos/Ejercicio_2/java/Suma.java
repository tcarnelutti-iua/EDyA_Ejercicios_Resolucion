/*
 * ============================================================================
 * PROMPT INICIAL UTILIZADO PARA GENERAR ESTA SOLUCIÓN
 * ============================================================================
 *
 * Quiero implementar en Java una función recursiva que calcule la suma de los
 * números enteros desde n hasta 1.
 *
 * Este problema puede resolverse recursivamente porque la suma de n hasta 1 se
 * puede expresar en términos de la suma de (n - 1) hasta 1:
 *
 *          suma(n) = n + suma(n - 1)
 *
 * Los casos base son n == 1 (devuelve 1) y n == 0 (devuelve 0 sin realizar otra
 * llamada recursiva).
 *
 * El caso recursivo es n + suma(n - 1), porque en cada llamada el valor de n
 * disminuye en uno hasta llegar al caso base.
 *
 * La función debe devolver la suma de los enteros desde n hasta 1. Debe rechazar
 * los valores negativos y las entradas que no sean enteras, e incluir comentarios
 * que expliquen el caso base, el caso recursivo y cómo la pila de llamadas se
 * resuelve al regresar del caso base.
 *
 * También quiero comprobar los resultados con la fórmula de Gauss n(n + 1) / 2
 * y probarla con estos casos:
 *   - n = 0,  resultado esperado: 0
 *   - n = 1,  resultado esperado: 1
 *   - n = 5,  resultado esperado: 15
 *   - n = 10, resultado esperado: 55
 * ============================================================================
 */

/**
 * Cálculo recursivo de la suma de los enteros desde {@code n} hasta {@code 1}.
 *
 * <p>La suma se define de forma recurrente como
 * {@code suma(n) = n + suma(n - 1)}, con los casos base {@code suma(0) = 0} y
 * {@code suma(1) = 1}.</p>
 *
 * <p>El resultado también puede calcularse de forma cerrada mediante la fórmula
 * de Gauss {@code n(n + 1) / 2}, que se usa como referencia independiente para
 * verificar la implementación recursiva.</p>
 *
 * @author Agente Desarrollador
 */
public final class Suma {

    /**
     * Profundidad máxima de recursión admitida. Cada llamada recursiva agrega un
     * nivel a la pila del programa; para evitar un desbordamiento de pila
     * ({@link StackOverflowError}) de forma determinista se rechazan las entradas
     * que superarían este límite.
     *
     * <p>El valor es conservador: en la JVM con el tamaño de pila por defecto la
     * recursión tolera bastantes más niveles, pero se fija un margen amplio para
     * que el comportamiento sea seguro en distintos entornos.</p>
     */
    private static final int MAX_PROFUNDIDAD_RECURSIVA = 10_000;

    /**
     * Mayor argumento para el que {@code n(n + 1) / 2} todavía cabe en un
     * {@code long} sin desbordar. Se obtiene resolviendo {@code n(n+1)/2 <= Long.MAX_VALUE}.
     *
     * <p>Este límite se usa en {@link #sumaGauss(long)}, cuyo parámetro es
     * {@code long} y por tanto sí puede recibir valores fuera del rango de
     * {@code int}.</p>
     */
    private static final long MAX_ARGUMENTO_POR_RANGO = 4_294_967_295L;

    /** Impide instanciar una clase que solo agrupa métodos estáticos. */
    private Suma() {
    }

    // ------------------------------------------------------------------------
    // API PÚBLICA
    // ------------------------------------------------------------------------

    /**
     * Calcula, de forma recursiva, la suma de los enteros desde {@code n} hasta
     * {@code 1}.
     *
     * @param n número entero no negativo hasta el que se suma
     * @return la suma {@code n + (n - 1) + ... + 2 + 1}
     * @throws IllegalArgumentException si {@code n} es negativo o si excede el
     *         límite de profundidad recursiva admitido
     */
    public static long suma(int n) {
        // La validación se hace UNA sola vez, en el método público. Así la
        // recursión interna no repite comprobaciones en cada nivel de la pila.
        validar(n);
        return sumaRecursiva(n);
    }

    /**
     * Calcula la misma suma mediante la fórmula cerrada de Gauss
     * {@code n(n + 1) / 2}. Se emplea como referencia independiente para
     * verificar el resultado de {@link #suma(int)}.
     *
     * @param n número entero no negativo hasta el que se suma
     * @return la suma {@code n + (n - 1) + ... + 2 + 1} calculada con la fórmula
     * @throws IllegalArgumentException si {@code n} es negativo o excede el rango
     *         representable en {@code long}
     */
    public static long sumaGauss(long n) {
        if (n < 0) {
            throw new IllegalArgumentException(
                    "La suma no está definida para números negativos: " + n);
        }
        if (n > MAX_ARGUMENTO_POR_RANGO) {
            throw new IllegalArgumentException(
                    "n = " + n + " desborda el tipo long al aplicar la fórmula de Gauss "
                            + "(máximo permitido: " + MAX_ARGUMENTO_POR_RANGO + ")");
        }

        // CUIDADO CON EL DESBORDAMIENTO INTERMEDIO:
        // Calcular "n * (n + 1) / 2" de izquierda a derecha puede desbordar 'long'
        // al evaluar n * (n + 1), aunque el resultado final sí quepa. Para evitarlo
        // se divide por 2 el factor que sea par ANTES de multiplicar.
        if (n % 2 == 0) {
            return (n / 2) * (n + 1);
        }
        return n * ((n + 1) / 2);
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
     * Núcleo recursivo. Se asume que {@code n} ya fue validado por {@link #suma(int)}.
     *
     * @param n entero no negativo ya validado
     * @return la suma desde {@code n} hasta {@code 1}
     */
    private static long sumaRecursiva(int n) {
        // --------------------------------------------------------------------
        // CASO BASE 1: n == 0.
        // Por convención la suma de un rango vacío es 0. Se devuelve 0 SIN
        // volver a llamar a sumaRecursiva(...): la recursión se detiene aquí.
        // --------------------------------------------------------------------
        if (n == 0) {
            return 0L;
        }

        // --------------------------------------------------------------------
        // CASO BASE 2: n == 1.
        // La suma de 1 hasta 1 es 1. También se devuelve sin llamada recursiva.
        // --------------------------------------------------------------------
        if (n == 1) {
            return 1L;
        }

        // --------------------------------------------------------------------
        // CASO RECURSIVO: suma(n) = n + suma(n - 1).
        // En cada llamada el problema se REDUCE en uno (n -> n - 1), por lo que
        // tarde o temprano se alcanza uno de los casos base.
        //
        // LA PILA DE LLAMADAS: cada invocación queda "esperando" el resultado de
        // sumaRecursiva(n - 1) antes de poder sumar. Al llegar al caso base se
        // comienza a DESENROLLAR la pila, sumando de abajo hacia arriba.
        //
        // Ejemplo para suma(5):
        //   suma(5) -> 5 + suma(4)   [pendiente en la pila]
        //   suma(4) -> 4 + suma(3)   [pendiente en la pila]
        //   suma(3) -> 3 + suma(2)   [pendiente en la pila]
        //   suma(2) -> 2 + suma(1)   [pendiente en la pila]
        //   suma(1) -> return 1      [CASO BASE: se detiene la recursión]
        //   ...y ahora la pila se resuelve al regresar:
        //   suma(2) = 2 + 1 = 3
        //   suma(3) = 3 + 3 = 6
        //   suma(4) = 4 + 6 = 10
        //   suma(5) = 5 + 10 = 15
        // --------------------------------------------------------------------
        return n + sumaRecursiva(n - 1);
    }

    /**
     * Valida el argumento de {@link #suma(int)}.
     *
     * @param n valor a validar
     * @throws IllegalArgumentException si {@code n} es negativo o excede el
     *         límite de profundidad recursiva
     */
    private static void validar(int n) {
        // Dominio válido: enteros no negativos.
        if (n < 0) {
            throw new IllegalArgumentException(
                    "La suma no está definida para números negativos: " + n);
        }

        // Límite explícito de profundidad para no provocar un StackOverflowError.
        if (n > MAX_PROFUNDIDAD_RECURSIVA) {
            throw new IllegalArgumentException(
                    "n = " + n + " excede la profundidad recursiva máxima permitida ("
                            + MAX_PROFUNDIDAD_RECURSIVA + ").");
        }

        // NOTA SOBRE EL RANGO NUMÉRICO:
        // Como 'n' es un int, su valor máximo (2_147_483_647) produce una suma de
        // aproximadamente 2.3e18, que cabe holgadamente en un 'long' (9.2e18).
        // Por eso, con este límite de profundidad, el resultado recursivo nunca
        // desborda 'long' y no se requiere una validación adicional de rango aquí.
        // El único punto con riesgo de desbordamiento intermedio es el cálculo de
        // la fórmula de Gauss, que ya se protege en sumaGauss(long).
    }

    // ------------------------------------------------------------------------
    // CASOS DE PRUEBA
    // ------------------------------------------------------------------------

    /**
     * Método de entrada con los casos de prueba solicitados en el análisis.
     *
     * @param args argumentos opcionales; si se pasa un valor se interpreta como
     *             el número {@code n} a sumar
     */
    public static void main(String[] args) {
        System.out.println("== Casos de prueba basicos ==");
        probar(0, 0L);
        probar(1, 1L);
        probar(5, 15L);
        probar(10, 55L);

        System.out.println();
        System.out.println("== Coincidencia con la formula de Gauss ==");
        probarCoincidenciaGauss(0);
        probarCoincidenciaGauss(1);
        probarCoincidenciaGauss(5);
        probarCoincidenciaGauss(10);
        probarCoincidenciaGauss(100);
        probarCoincidenciaGauss(1_000);

        System.out.println();
        System.out.println("== Entradas invalidas ==");
        probarArgumentoInvalido(-1);
        probarArgumentoInvalido(-100);
        probarNoEntero("3.5");
        probarNoEntero("abc");
        probarNoEntero("");

        System.out.println();
        System.out.println("== Limites de profundidad y rango ==");
        probarLimiteProfundidad(MAX_PROFUNDIDAD_RECURSIVA);
        probarArgumentoInvalido(MAX_PROFUNDIDAD_RECURSIVA + 1);
        probarRangoGauss(MAX_ARGUMENTO_POR_RANGO);
        probarArgumentoInvalidoGauss(MAX_ARGUMENTO_POR_RANGO + 1);

        // Si se recibe un argumento por línea de comandos, se procesa como entrada.
        if (args.length > 0) {
            System.out.println();
            System.out.println("== Entrada por linea de comandos ==");
            probarEntradaUsuario(args[0]);
        }
    }

    /**
     * Ejecuta un caso de prueba y muestra si el resultado coincide con lo esperado.
     *
     * @param n        entrada de la función
     * @param esperado resultado esperado
     */
    private static void probar(int n, long esperado) {
        long obtenido = suma(n);
        boolean correcto = obtenido == esperado;
        System.out.printf("suma(%d) = %-8d | esperado = %-8d | %s%n",
                n, obtenido, esperado, correcto ? "OK" : "FALLO");
    }

    /**
     * Comprueba que el resultado recursivo coincida con la fórmula de Gauss.
     *
     * @param n entrada a verificar
     */
    private static void probarCoincidenciaGauss(int n) {
        long recursivo = suma(n);
        long gauss = sumaGauss(n);
        boolean correcto = recursivo == gauss;
        System.out.printf("n = %-6d | recursivo = %-10d | Gauss = %-10d | %s%n",
                n, recursivo, gauss, correcto ? "OK" : "FALLO");
    }

    /**
     * Verifica que un argumento inválido sea rechazado con la excepción esperada.
     *
     * @param n argumento inválido (negativo o fuera de profundidad)
     */
    private static void probarArgumentoInvalido(int n) {
        try {
            suma(n);
            System.out.printf("suma(%d) -> FALLO (no se lanzo ninguna excepcion)%n", n);
        } catch (IllegalArgumentException excepcion) {
            System.out.printf("suma(%d) -> OK (rechazado: %s)%n", n, excepcion.getMessage());
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
     * @param n valor límite de profundidad
     */
    private static void probarLimiteProfundidad(int n) {
        long obtenido = suma(n);
        long gauss = sumaGauss(n);
        boolean correcto = obtenido == gauss;
        System.out.printf("suma(%d) [limite de profundidad] -> %d | Gauss = %d | %s%n",
                n, obtenido, gauss, correcto ? "OK" : "FALLO");
    }

    /**
     * Verifica que la fórmula de Gauss sea correcta en el límite del rango.
     *
     * @param n valor límite del rango de {@code long}
     */
    private static void probarRangoGauss(long n) {
        long obtenido = sumaGauss(n);
        boolean correcto = obtenido == 9_223_372_034_707_292_160L;
        System.out.printf("sumaGauss(%d) [limite de rango] = %d | %s%n",
                n, obtenido, correcto ? "OK" : "FALLO");
    }

    /**
     * Verifica que un valor fuera del rango de {@code long} sea rechazado.
     *
     * @param n valor fuera del rango
     */
    private static void probarArgumentoInvalidoGauss(long n) {
        try {
            sumaGauss(n);
            System.out.printf("sumaGauss(%d) -> FALLO (no se lanzo ninguna excepcion)%n", n);
        } catch (IllegalArgumentException excepcion) {
            System.out.printf("sumaGauss(%d) -> OK (rechazado: %s)%n",
                    n, excepcion.getMessage());
        }
    }

    /**
     * Procesa una entrada de usuario recibida como texto.
     *
     * @param texto entrada del usuario
     */
    private static void probarEntradaUsuario(String texto) {
        try {
            int n = parsear(texto);
            long resultado = suma(n);
            System.out.printf("Entrada \"%s\": suma(%d) = %d%n", texto, n, resultado);
        } catch (IllegalArgumentException excepcion) {
            System.out.printf("Entrada \"%s\": ERROR -> %s%n", texto, excepcion.getMessage());
        }
    }
}

/*
 * ============================================================================
 * NOTAS DE IMPLEMENTACIÓN (aclaraciones al prompt inicial)
 * ============================================================================
 * 1. Se usó el tipo de retorno 'long' en lugar de 'int' para que la suma no
 *    desborde con entradas grandes (con 'int' la suma desbordaría a partir de
 *    n = 65536).
 * 2. La validación se concentra en el método público 'suma', de modo que la
 *    recursión interna no repita comprobaciones en cada nivel.
 * 3. Se definió un límite explícito de profundidad (MAX_PROFUNDIDAD_RECURSIVA)
 *    para transformar un eventual StackOverflowError en un error descriptivo.
 * 4. La fórmula de Gauss divide por 2 el factor par antes de multiplicar, para
 *    evitar un desbordamiento intermedio de 'long' aunque el resultado quepa.
 * 5. Las entradas no enteras se rechazan en 'parsear' mediante
 *    IllegalArgumentException, que es la práctica estándar en Java.
 * ============================================================================
 */

/*
 * ============================================================================
 * PROMPT INICIAL UTILIZADO PARA GENERAR ESTA SOLUCIÓN
 * ============================================================================
 *
 * Quiero implementar en Java una función recursiva que sume los dígitos de un
 * número entero positivo.
 *
 * En cada llamada, el último dígito se obtiene con n % 10, y el resto del
 * número se obtiene con división entera: n / 10. Por ejemplo, para 305, el
 * último dígito es 5 y el número reducido es 30.
 *
 * El caso base ocurre cuando n < 10: como la entrada debe ser positiva, ese
 * número es un solo dígito y la función lo devuelve directamente.
 *
 * En el caso recursivo, la función devuelve (n % 10) + sumaDigitos(n / 10):
 * combina el último dígito con la suma de los dígitos restantes. Rechaza los
 * valores menores o iguales a cero con un mensaje descriptivo.
 *
 * Prueba estos casos y verifica los resultados:
 *   - sumaDigitos(1),   resultado esperado: 1
 *   - sumaDigitos(7),   resultado esperado: 7
 *   - sumaDigitos(10),  resultado esperado: 1
 *   - sumaDigitos(305), resultado esperado: 8
 *   - sumaDigitos(999), resultado esperado: 27
 *   - sumaDigitos(0),   debe rechazar la entrada.
 * ============================================================================
 */

/**
 * Suma recursiva de los dígitos de un número entero positivo.
 *
 * <p>La suma se define de forma recurrente como
 * {@code sumaDigitos(n) = (n % 10) + sumaDigitos(n / 10)}, con el caso base
 * {@code sumaDigitos(n) = n} cuando {@code n < 10}.</p>
 *
 * <p>El operador módulo ({@code n % 10}) aísla el último dígito y la división
 * entera ({@code n / 10}) descarta ese último dígito, de modo que cada llamada
 * reduce el problema hasta alcanzar el caso base.</p>
 *
 * @author Agente Desarrollador
 */
public final class SumaDigitos {

    /** Impide instanciar una clase que solo agrupa métodos estáticos. */
    private SumaDigitos() {
    }

    // ------------------------------------------------------------------------
    // API PÚBLICA
    // ------------------------------------------------------------------------

    /**
     * Suma, de forma recursiva, los dígitos de {@code n}.
     *
     * <p>Ejemplos:
     * {@code sumaDigitos(1)} devuelve {@code 1};
     * {@code sumaDigitos(305)} devuelve {@code 8};
     * {@code sumaDigitos(999)} devuelve {@code 27}.</p>
     *
     * @param n número entero positivo cuyos dígitos se desean sumar
     * @return la suma de los dígitos de {@code n}
     * @throws IllegalArgumentException si {@code n} es menor o igual que cero
     */
    public static int sumaDigitos(int n) {
        // La validación se hace UNA sola vez, en el método público, de modo que
        // la recursión interna no repita comprobaciones en cada nivel de la pila.
        validar(n);
        return sumaDigitosRecursivo(n);
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
     * Núcleo recursivo. Se asume que {@code n} ya fue validado por
     * {@link #sumaDigitos(int)}, por lo que aquí {@code n >= 1}.
     *
     * @param n entero positivo ya validado
     * @return la suma de los dígitos de {@code n}
     */
    private static int sumaDigitosRecursivo(int n) {
        // --------------------------------------------------------------------
        // CASO BASE: n < 10.
        // Como n es positivo (1 <= n <= 9), es un número de UN solo dígito y su
        // suma de dígitos es él mismo. Devolvemos n SIN volver a llamar a
        // sumaDigitosRecursivo(...): este es el punto donde la recursión se
        // detiene.
        // --------------------------------------------------------------------
        if (n < 10) {
            return n;
        }

        // --------------------------------------------------------------------
        // CASO RECURSIVO: (n % 10) + sumaDigitosRecursivo(n / 10).
        //   - n % 10 AÍSLA el último dígito (305 % 10 = 5).
        //   - n / 10 DESCARTA el último dígito (305 / 10 = 30).
        // Así el problema se REDUCE en cada llamada hasta alcanzar el caso base.
        //
        // LA PILA DE LLAMADAS: cada invocación queda "esperando" el resultado de
        // sumaDigitosRecursivo(n / 10) antes de poder sumar el dígito que aisló.
        // Al llegar al caso base se comienza a DESENROLLAR la pila, acumulando
        // de abajo hacia arriba (dígito por dígito).
        //
        // Ejemplo para sumaDigitos(305):
        //   sumaDigitos(305) -> 5 + sumaDigitos(30)  [5 pendiente en la pila]
        //   sumaDigitos(30)  -> 0 + sumaDigitos(3)   [0 pendiente en la pila]
        //   sumaDigitos(3)   -> return 3             [CASO BASE: se detiene]
        //   ...y ahora la pila se resuelve al regresar:
        //   sumaDigitos(30)  = 0 + 3 = 3
        //   sumaDigitos(305) = 5 + 3 = 8
        // --------------------------------------------------------------------
        return (n % 10) + sumaDigitosRecursivo(n / 10);
    }

    /**
     * Valida el argumento de {@link #sumaDigitos(int)}.
     *
     * @param n valor a validar
     * @throws IllegalArgumentException si {@code n} es menor o igual que cero
     */
    private static void validar(int n) {
        // Dominio válido: enteros POSITIVOS (n >= 1). El 0 y los negativos no
        // tienen una suma de dígitos definida en esta consigna; además, en el
        // caso del 0 la división sucesiva entre 10 nunca lo reduciría (0 / 10 = 0)
        // y la recursión no alcanzaría el caso base. Se rechazan explícitamente.
        if (n <= 0) {
            throw new IllegalArgumentException(
                    "Solo se suman los dígitos de números enteros positivos (> 0): " + n);
        }
    }

    // ------------------------------------------------------------------------
    // CASOS DE PRUEBA
    // ------------------------------------------------------------------------

    /**
     * Método de entrada con los casos de prueba solicitados en el análisis.
     *
     * @param args argumentos opcionales; si se pasa un valor se interpreta como
     *             el número {@code n} a procesar
     */
    public static void main(String[] args) {
        System.out.println("== Casos de prueba basicos ==");
        probar(1, 1);
        probar(7, 7);
        probar(10, 1);
        probar(305, 8);
        probar(999, 27);

        System.out.println();
        System.out.println("== Entradas invalidas ==");
        probarArgumentoInvalido(0);
        probarArgumentoInvalido(-1);
        probarArgumentoInvalido(-305);
        probarNoEntero("3.5");
        probarNoEntero("abc");
        probarNoEntero("");

        System.out.println();
        System.out.println("== Caso limite de mayor magnitud ==");
        // Integer.MAX_VALUE = 2147483647. La suma de sus dígitos es
        // 2+1+4+7+4+8+3+6+4+7 = 46. La profundidad máxima de recursión para un
        // 'int' es 10, por lo que no hay riesgo de desbordar la pila.
        probar(Integer.MAX_VALUE, 46);

        // Si se recibe un argumento por línea de comandos, se procesa como entrada.
        if (args.length > 0) {
            System.out.println();
            System.out.println("== Entrada por linea de comandos ==");
            probarEntradaUsuario(args[0]);
        }
    }

    /**
     * Ejecuta un caso de prueba comparando el resultado con lo esperado.
     *
     * @param n        entrada de la función
     * @param esperado resultado esperado
     */
    private static void probar(int n, int esperado) {
        int obtenido = sumaDigitos(n);
        boolean correcto = obtenido == esperado;
        System.out.printf("sumaDigitos(%d) = %-3d | esperado = %-3d | %s%n",
                n, obtenido, esperado, correcto ? "OK" : "FALLO");
    }

    /**
     * Verifica que un argumento inválido sea rechazado con la excepción esperada.
     *
     * @param n argumento inválido (menor o igual que cero)
     */
    private static void probarArgumentoInvalido(int n) {
        try {
            sumaDigitos(n);
            System.out.printf("sumaDigitos(%d) -> FALLO (no se lanzo ninguna excepcion)%n", n);
        } catch (IllegalArgumentException excepcion) {
            System.out.printf("sumaDigitos(%d) -> OK (rechazado: %s)%n", n, excepcion.getMessage());
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
     * Procesa una entrada de usuario recibida como texto.
     *
     * @param texto entrada del usuario
     */
    private static void probarEntradaUsuario(String texto) {
        try {
            int n = parsear(texto);
            System.out.printf("Entrada \"%s\": sumaDigitos = %d%n", texto, sumaDigitos(n));
        } catch (IllegalArgumentException excepcion) {
            System.out.printf("Entrada \"%s\": ERROR -> %s%n", texto, excepcion.getMessage());
        }
    }
}

/*
 * ============================================================================
 * PROMPT INICIAL UTILIZADO PARA GENERAR ESTA SOLUCIÓN
 * ============================================================================
 *
 * Quiero implementar en Java una función recursiva que calcule el factorial de
 * un número.
 *
 * Este problema puede resolverse recursivamente porque el factorial de un número
 * se puede expresar en términos del factorial de un número menor:
 *
 *          n! = n × (n - 1)!
 *
 * El caso base es n == 0 o n == 1, porque 0! y 1! valen 1. En ambos casos la
 * función debe devolver 1 sin realizar otra llamada recursiva.
 *
 * El caso recursivo es n × factorial(n - 1), porque en cada llamada el valor de n
 * disminuye en uno hasta llegar al caso base.
 *
 * La función debe devolver el factorial de n. Debe rechazar los valores negativos
 * e incluir comentarios que expliquen el caso base, el caso recursivo y cómo la
 * pila de llamadas se resuelve al regresar del caso base.
 *
 * También quiero probarla con estos casos:
 *   - n = 0, resultado esperado: 1
 *   - n = 1, resultado esperado: 1
 *   - n = 5, resultado esperado: 120
 * ============================================================================
 */

/**
 * Cálculo recursivo del factorial de un número entero no negativo.
 *
 * <p>El factorial se define de forma recurrente como
 * {@code n! = n * (n - 1)!}, con {@code 0! = 1! = 1}.</p>
 *
 * @author Agente Desarrollador
 */
public final class Factorial {

    /**
     * Mayor argumento para el que el resultado todavía cabe en un {@code long}
     * sin desbordar. 20! es el factorial más grande representable en 64 bits
     * con signo (21! ya excede {@link Long#MAX_VALUE}).
     */
    private static final int MAX_ARGUMENTO = 20;

    /** Impide instanciar una clase que solo agrupa métodos estáticos. */
    private Factorial() {
    }

    /**
     * Calcula el factorial de {@code n} de manera recursiva.
     *
     * @param n número entero no negativo cuyo factorial se desea calcular
     * @return el factorial de {@code n} ({@code n!})
     * @throws IllegalArgumentException si {@code n} es negativo o si excede
     *         {@value #MAX_ARGUMENTO} (límite del tipo {@code long})
     */
    public static long calcular(int n) {
        // --------------------------------------------------------------------
        // VALIDACIÓN: el factorial solo está definido para enteros >= 0.
        // Rechazamos los valores negativos (caso límite pedido en la consigna).
        // --------------------------------------------------------------------
        if (n < 0) {
            throw new IllegalArgumentException(
                    "El factorial no está definido para números negativos: " + n);
        }

        // Validación adicional de caso límite: evita un desbordamiento silencioso
        // de 'long' (sería un resultado incorrecto sin lanzar error).
        if (n > MAX_ARGUMENTO) {
            throw new IllegalArgumentException(
                    "n = " + n + " desborda el tipo long (máximo permitido: " + MAX_ARGUMENTO + ")");
        }

        // --------------------------------------------------------------------
        // CASO BASE: n == 0 o n == 1.
        // Por definición 0! = 1 y 1! = 1, así que devolvemos 1 SIN volver a
        // llamar a calcular(...). Este es el punto donde la recursión se detiene.
        // --------------------------------------------------------------------
        if (n <= 1) {
            return 1L;
        }

        // --------------------------------------------------------------------
        // CASO RECURSIVO: n! = n × (n - 1)!.
        // En cada llamada el problema se REDUCE en uno (n -> n - 1), por lo que
        // tarde o temprano se alcanza el caso base (n == 1).
        //
        // LA PILA DE LLAMADAS: cada invocación queda "esperando" el resultado de
        // calcular(n - 1) antes de poder multiplicar. Al llegar al caso base se
        // comienza a DESENROLLAR la pila, multiplicando de abajo hacia arriba.
        //
        // Ejemplo para calcular(4):
        //   calcular(4) -> 4 * calcular(3)   [pendiente en la pila]
        //   calcular(3) -> 3 * calcular(2)   [pendiente en la pila]
        //   calcular(2) -> 2 * calcular(1)   [pendiente en la pila]
        //   calcular(1) -> return 1          [CASO BASE: se detiene la recursión]
        //   ...y ahora la pila se resuelve al regresar:
        //   calcular(2) = 2 * 1 = 2
        //   calcular(3) = 3 * 2 = 6
        //   calcular(4) = 4 * 6 = 24
        // --------------------------------------------------------------------
        return n * calcular(n - 1);
    }

    /**
     * Método de entrada con los casos de prueba solicitados en el prompt.
     *
     * @param args no se utilizan
     */
    public static void main(String[] args) {
        probar(0, 1L);
        probar(1, 1L);
        probar(5, 120L);

        // Caso límite adicional: los valores negativos deben ser rechazados.
        probarArgumentoInvalido(-1);
    }

    /**
     * Ejecuta un caso de prueba y muestra si el resultado coincide con lo esperado.
     *
     * @param n            entrada de la función
     * @param esperado     resultado esperado
     */
    private static void probar(int n, long esperado) {
        long obtenido = calcular(n);
        boolean correcto = obtenido == esperado;
        System.out.printf("factorial(%d) = %-6d | esperado = %-6d | %s%n",
                n, obtenido, esperado, correcto ? "OK" : "FALLO");
    }

    /**
     * Verifica que un argumento inválido sea rechazado con la excepción esperada.
     *
     * @param n argumento inválido (negativo)
     */
    private static void probarArgumentoInvalido(int n) {
        try {
            calcular(n);
            System.out.printf("factorial(%d) -> FALLO (no se lanzó ninguna excepción)%n", n);
        } catch (IllegalArgumentException excepcion) {
            System.out.printf("factorial(%d) -> OK (rechazado: %s)%n", n, excepcion.getMessage());
        }
    }
}

/*
 * ============================================================================
 * NOTAS DE IMPLEMENTACIÓN (aclaraciones al prompt inicial)
 * ============================================================================
 * 1. Se usó el tipo de retorno 'long' en lugar de 'int' para ampliar el rango
 *    de resultados válidos (hasta 20!), ya que 13! en adelante desbordaría 'int'.
 * 2. Se agregó una validación extra del caso límite n > 20 que lanza una
 *    excepción en lugar de devolver un resultado incorrecto por desbordamiento.
 * 3. Se conservaron las validaciones de valores negativos exigidas por el prompt
 *    mediante IllegalArgumentException, que es la práctica estándar en Java.
 * ============================================================================
 */

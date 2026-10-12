/*
 * ============================================================================
 * PROMPT INICIAL UTILIZADO PARA GENERAR ESTA SOLUCIÓN
 * ============================================================================
 *
 * Quiero implementar en Java una función recursiva que cuente los dígitos de un
 * número entero positivo.
 *
 * El problema puede reducirse usando división entera entre 10, que elimina el
 * último dígito del número. Por ejemplo, 305 / 10 da 30, y 30 / 10 da 3.
 *
 * El caso base es cuando n < 10: como la entrada es positiva, ese número está
 * entre 1 y 9 y tiene un solo dígito. En ese caso, la función debe devolver 1.
 *
 * En cada caso recursivo, la función debe devolver 1 + contarDigitos(n / 10):
 * suma un dígito por la llamada actual y continúa con el número reducido hasta
 * alcanzar el caso base. Rechaza las entradas menores o iguales a cero con un
 * mensaje descriptivo.
 *
 * Prueba la función con estos casos:
 *   - contarDigitos(1),     resultado esperado: 1
 *   - contarDigitos(9),     resultado esperado: 1
 *   - contarDigitos(10),    resultado esperado: 2
 *   - contarDigitos(305),   resultado esperado: 3
 *   - contarDigitos(12345), resultado esperado: 5
 *   - contarDigitos(0),     debe rechazar la entrada.
 * ============================================================================
 */

/**
 * Conteo recursivo de los dígitos de un número entero positivo.
 *
 * <p>La cantidad de dígitos se define de forma recurrente como
 * {@code contarDigitos(n) = 1 + contarDigitos(n / 10)}, con el caso base
 * {@code contarDigitos(n) = 1} cuando {@code n < 10}.</p>
 *
 * <p>La división entera entre {@code 10} elimina el último dígito del número, de
 * modo que cada llamada reduce el problema hasta alcanzar el caso base.</p>
 *
 * @author Agente Desarrollador
 */
public final class ContarDigitos {

    /** Impide instanciar una clase que solo agrupa métodos estáticos. */
    private ContarDigitos() {
    }

    // ------------------------------------------------------------------------
    // API PÚBLICA
    // ------------------------------------------------------------------------

    /**
     * Cuenta, de forma recursiva, cuántos dígitos tiene {@code n}.
     *
     * <p>Ejemplos:
     * {@code contarDigitos(1)} devuelve {@code 1};
     * {@code contarDigitos(305)} devuelve {@code 3};
     * {@code contarDigitos(12345)} devuelve {@code 5}.</p>
     *
     * @param n número entero positivo cuyos dígitos se desean contar
     * @return la cantidad de dígitos de {@code n}
     * @throws IllegalArgumentException si {@code n} es menor o igual que cero
     */
    public static int contarDigitos(int n) {
        // La validación se hace UNA sola vez, en el método público, de modo que
        // la recursión interna no repita comprobaciones en cada nivel de la pila.
        validar(n);
        return contarDigitosRecursivo(n);
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
     * {@link #contarDigitos(int)}, por lo que aquí {@code n >= 1}.
     *
     * @param n entero positivo ya validado
     * @return la cantidad de dígitos de {@code n}
     */
    private static int contarDigitosRecursivo(int n) {
        // --------------------------------------------------------------------
        // CASO BASE: n < 10.
        // Como n es positivo (1 <= n <= 9), es un número de UN solo dígito.
        // Devolvemos 1 SIN volver a llamar a contarDigitosRecursivo(...): este
        // es el punto donde la recursión se detiene.
        // --------------------------------------------------------------------
        if (n < 10) {
            return 1;
        }

        // --------------------------------------------------------------------
        // CASO RECURSIVO: 1 + contarDigitosRecursivo(n / 10).
        // La división entera entre 10 ELIMINA el último dígito (305 / 10 = 30,
        // 30 / 10 = 3), por lo que el problema se REDUCE en cada llamada hasta
        // alcanzar el caso base.
        //
        // LA PILA DE LLAMADAS: cada invocación queda "esperando" el resultado de
        // contarDigitosRecursivo(n / 10) antes de poder sumar su 1. Al llegar al
        // caso base se comienza a DESENROLLAR la pila, sumando de abajo hacia
        // arriba (una unidad por cada dígito eliminado).
        //
        // Ejemplo para contarDigitos(305):
        //   contarDigitos(305) -> 1 + contarDigitos(30)  [pendiente en la pila]
        //   contarDigitos(30)  -> 1 + contarDigitos(3)   [pendiente en la pila]
        //   contarDigitos(3)   -> return 1               [CASO BASE: se detiene]
        //   ...y ahora la pila se resuelve al regresar:
        //   contarDigitos(30)  = 1 + 1 = 2
        //   contarDigitos(305) = 1 + 2 = 3
        // --------------------------------------------------------------------
        return 1 + contarDigitosRecursivo(n / 10);
    }

    /**
     * Valida el argumento de {@link #contarDigitos(int)}.
     *
     * @param n valor a validar
     * @throws IllegalArgumentException si {@code n} es menor o igual que cero
     */
    private static void validar(int n) {
        // Dominio válido: enteros POSITIVOS (n >= 1). El 0 y los negativos no
        // tienen una cantidad de dígitos definida en esta consigna; además, en el
        // caso del 0 la división sucesiva entre 10 nunca lo reduciría (0 / 10 = 0)
        // y la recursión no alcanzaría el caso base. Se rechazan explícitamente.
        if (n <= 0) {
            throw new IllegalArgumentException(
                    "Solo se cuentan los dígitos de números enteros positivos (> 0): " + n);
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
        probar(9, 1);
        probar(10, 2);
        probar(305, 3);
        probar(12345, 5);

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
        // Integer.MAX_VALUE = 2147483647 tiene 10 dígitos. La profundidad máxima
        // de recursión para un 'int' es 10, por lo que no hay riesgo de desbordar
        // la pila.
        probar(Integer.MAX_VALUE, 10);

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
        int obtenido = contarDigitos(n);
        boolean correcto = obtenido == esperado;
        System.out.printf("contarDigitos(%d) = %-3d | esperado = %-3d | %s%n",
                n, obtenido, esperado, correcto ? "OK" : "FALLO");
    }

    /**
     * Verifica que un argumento inválido sea rechazado con la excepción esperada.
     *
     * @param n argumento inválido (menor o igual que cero)
     */
    private static void probarArgumentoInvalido(int n) {
        try {
            contarDigitos(n);
            System.out.printf("contarDigitos(%d) -> FALLO (no se lanzo ninguna excepcion)%n", n);
        } catch (IllegalArgumentException excepcion) {
            System.out.printf("contarDigitos(%d) -> OK (rechazado: %s)%n", n, excepcion.getMessage());
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
            System.out.printf("Entrada \"%s\": contarDigitos = %d%n", texto, contarDigitos(n));
        } catch (IllegalArgumentException excepcion) {
            System.out.printf("Entrada \"%s\": ERROR -> %s%n", texto, excepcion.getMessage());
        }
    }
}

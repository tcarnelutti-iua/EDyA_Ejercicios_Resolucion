/*
 * ============================================================================
 * PROMPT INICIAL UTILIZADO PARA GENERAR ESTA SOLUCIÓN
 * ============================================================================
 *
 * Quiero implementar en Java una función recursiva que invierta un String,
 * sin usar ciclos.
 *
 * El caso base ocurre cuando la cadena está vacía o tiene un solo carácter: en
 * ambos casos, la función la devuelve tal como está, porque ya está invertida.
 *
 * En cada llamada, separa el primer carácter del resto de la cadena. Reduce el
 * problema llamando recursivamente a la función con el resto, obtenido mediante
 * substring(1). Luego reconstruye el resultado agregando el primer carácter al
 * final de la cadena invertida: invertir(resto) + primerCaracter.
 *
 * La función debe devolver una nueva cadena con los caracteres en orden
 * inverso. Rechaza null con un error descriptivo y conserva las mayúsculas y
 * minúsculas.
 *
 * Prueba estos casos:
 *   - invertir(""),          resultado esperado: ""
 *   - invertir("a"),         resultado esperado: "a"
 *   - invertir("casa"),      resultado esperado: "asac"
 *   - invertir("Java"),      resultado esperado: "avaJ"
 *   - invertir("reconocer"), resultado esperado: "reconocer"
 * ============================================================================
 */

/**
 * Inversión recursiva de una cadena de texto.
 *
 * <p>La inversión se define de forma recurrente como
 * {@code invertir(s) = invertir(s.substring(1)) + s.charAt(0)}, con el caso base
 * {@code invertir(s) = s} cuando {@code s} tiene longitud 0 o 1.</p>
 *
 * <p>En cada llamada se separa el primer carácter ({@code s.charAt(0)}) del
 * resto ({@code s.substring(1)}). El problema se reduce sobre el resto hasta
 * alcanzar el caso base y, al regresar, el primer carácter se agrega al final
 * de la cadena ya invertida, de modo que termina ocupando la última posición.</p>
 *
 * @author Agente Desarrollador
 */
public final class InvertirCadena {

    /** Impide instanciar una clase que solo agrupa métodos estáticos. */
    private InvertirCadena() {
    }

    // ------------------------------------------------------------------------
    // API PÚBLICA
    // ------------------------------------------------------------------------

    /**
     * Invierte, de forma recursiva, la cadena {@code texto}.
     *
     * <p>Ejemplos:
     * {@code invertir("casa")} devuelve {@code "asac"};
     * {@code invertir("Java")} devuelve {@code "avaJ"};
     * {@code invertir("reconocer")} devuelve {@code "reconocer"}.</p>
     *
     * <p>La comparación de caracteres no se altera: las mayúsculas y minúsculas
     * se conservan tal cual.</p>
     *
     * @param texto cadena que se desea invertir (puede estar vacía)
     * @return una nueva cadena con los caracteres de {@code texto} en orden inverso
     * @throws IllegalArgumentException si {@code texto} es {@code null}
     */
    public static String invertir(String texto) {
        // La validación se hace UNA sola vez, en el método público, de modo que
        // la recursión interna no repita comprobaciones en cada nivel de la pila.
        validar(texto);
        return invertirRecursivo(texto);
    }

    // ------------------------------------------------------------------------
    // NÚCLEO RECURSIVO Y VALIDACIÓN
    // ------------------------------------------------------------------------

    /**
     * Núcleo recursivo. Se asume que {@code texto} ya fue validado por
     * {@link #invertir(String)}, por lo que aquí {@code texto != null}.
     *
     * @param texto cadena ya validada (no nula)
     * @return una nueva cadena con los caracteres de {@code texto} en orden inverso
     */
    private static String invertirRecursivo(String texto) {
        // --------------------------------------------------------------------
        // CASO BASE: longitud 0 (cadena vacía) o 1 (un solo carácter).
        // Una cadena vacía y una de un único carácter YA están invertidas, así
        // que se devuelven tal cual SIN volver a llamar a invertirRecursivo(...).
        // Este es el punto donde la recursión se detiene.
        // --------------------------------------------------------------------
        if (texto.length() <= 1) {
            return texto;
        }

        // --------------------------------------------------------------------
        // CASO RECURSIVO: invertirRecursivo(resto) + primerCaracter.
        //   - texto.charAt(0)    AÍSLA el primer carácter ("casa" -> 'c').
        //   - texto.substring(1) DESCARTA ese primer carácter ("casa" -> "asa").
        // Así el problema se REDUCE en cada llamada (una letra menos) hasta
        // alcanzar el caso base.
        //
        // LA PILA DE LLAMADAS: cada invocación queda "esperando" el resultado de
        // invertirRecursivo(resto) antes de poder concatenar el carácter que
        // aisló. Al llegar al caso base se comienza a DESENROLLAR la pila y el
        // primer carácter se coloca al FINAL del resultado ya invertido; por eso
        // el último carácter del original queda primero.
        //
        // Ejemplo para invertir("casa"):
        //   invertir("casa") -> invertir("asa") + 'c'   ['c' pendiente en la pila]
        //   invertir("asa")  -> invertir("sa")  + 'a'   ['a' pendiente en la pila]
        //   invertir("sa")   -> invertir("a")   + 's'   ['s' pendiente en la pila]
        //   invertir("a")    -> return "a"              [CASO BASE: se detiene]
        //   ...y ahora la pila se resuelve al regresar:
        //   invertir("sa")   = "a" + 's' = "as"
        //   invertir("asa")  = "as" + 'a' = "asa"
        //   invertir("casa") = "asa" + 'c' = "asac"
        // --------------------------------------------------------------------
        char primerCaracter = texto.charAt(0);
        String resto = texto.substring(1);
        return invertirRecursivo(resto) + primerCaracter;
    }

    /**
     * Valida el argumento de {@link #invertir(String)}.
     *
     * @param texto valor a validar
     * @throws IllegalArgumentException si {@code texto} es {@code null}
     */
    private static void validar(String texto) {
        // El dominio exige una cadena no nula. La cadena VACÍA sí es válida y
        // constituye el caso base (se devuelve sin cambios). El null no puede
        // procesarse recursivamente (no existe longitud ni caracteres que
        // separar), por lo que se rechaza con un mensaje descriptivo.
        if (texto == null) {
            throw new IllegalArgumentException(
                    "La cadena a invertir no puede ser nula (null).");
        }
    }

    // ------------------------------------------------------------------------
    // CASOS DE PRUEBA
    // ------------------------------------------------------------------------

    /**
     * Método de entrada con los casos de prueba solicitados en el análisis.
     *
     * @param args argumentos opcionales; si se pasa un valor se interpreta como
     *             la cadena a invertir
     */
    public static void main(String[] args) {
        System.out.println("== Casos de prueba basicos ==");
        probar("", "");
        probar("a", "a");
        probar("casa", "asac");
        probar("Java", "avaJ");
        probar("reconocer", "reconocer");

        System.out.println();
        System.out.println("== Casos adicionales (mayusculas/minusculas y espacios) ==");
        probar("HoLa MuNdO", "OdNuM aLoH");
        probar("12345", "54321");
        probar("  ab  ", "  ba  ");

        System.out.println();
        System.out.println("== Entradas invalidas ==");
        probarNulo(null);

        // Si se recibe un argumento por línea de comandos, se procesa como entrada.
        if (args.length > 0) {
            System.out.println();
            System.out.println("== Entrada por linea de comandos ==");
            probar(args[0], invertir(args[0]));
        }
    }

    /**
     * Ejecuta un caso de prueba comparando el resultado con lo esperado.
     *
     * @param entrada  cadena de entrada de la función
     * @param esperado cadena esperada como resultado
     */
    private static void probar(String entrada, String esperado) {
        String obtenido = invertir(entrada);
        boolean correcto = obtenido.equals(esperado);
        System.out.printf("invertir(\"%s\") = \"%s\" | esperado = \"%s\" | %s%n",
                entrada, obtenido, esperado, correcto ? "OK" : "FALLO");
    }

    /**
     * Verifica que una entrada nula sea rechazada con la excepción esperada.
     *
     * @param entrada valor nulo
     */
    private static void probarNulo(String entrada) {
        try {
            invertir(entrada);
            System.out.println("invertir(null) -> FALLO (no se lanzo ninguna excepcion)");
        } catch (IllegalArgumentException excepcion) {
            System.out.printf("invertir(null) -> OK (rechazado: %s)%n", excepcion.getMessage());
        }
    }
}

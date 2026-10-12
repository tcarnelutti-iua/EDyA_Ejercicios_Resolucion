/*
 * ============================================================================
 * PROMPT INICIAL UTILIZADO PARA GENERAR ESTA SOLUCIÓN
 * ============================================================================
 *
 * Quiero implementar en Java el algoritmo Bubble Sort para ordenar un vector de
 * números enteros de menor a mayor.
 *
 * Bubble Sort es adecuado principalmente con fines didácticos o para conjuntos
 * pequeños: su funcionamiento es simple y permite observar cómo las
 * comparaciones e intercambios van ordenando los elementos. Sin embargo, no es
 * conveniente para conjuntos grandes porque, en promedio y en el peor caso, su
 * tiempo de ejecución es O(n²).
 *
 * Implementá el algoritmo ordenando el vector recibido in-place. En cada pasada,
 * compará elementos adyacentes e intercambialos si están en el orden incorrecto.
 * Usá una bandera para detectar si una pasada completa no hizo intercambios y
 * finalizar anticipadamente cuando el vector ya esté ordenado.
 *
 * El programa debe informar el vector ordenado, la cantidad de comparaciones y
 * la cantidad de intercambios realizados. Incrementá cada contador exactamente
 * cuando ocurra la operación correspondiente.
 *
 * Comentá el código y analizá las complejidades: mejor caso O(n) con
 * finalización anticipada, promedio y peor caso O(n²), y espacio auxiliar O(1).
 *
 * Probá vectores desordenados, ya ordenados, en orden inverso, con elementos
 * repetidos, vacíos y de un solo elemento. Verificá tanto el resultado ordenado
 * como los contadores.
 * ============================================================================
 */

/**
 * Ordenamiento de un vector de enteros mediante el algoritmo Bubble Sort,
 * con finalización anticipada.
 *
 * <p>El algoritmo ordena <b>in-place</b> (sobre el mismo vector recibido, sin
 * crear copias) comparando pares de elementos adyacentes e intercambiándolos
 * cuando están en el orden incorrecto (el primero mayor que el segundo). Tras
 * cada pasada, el mayor de los elementos aún no procesados queda "burbujeando"
 * hasta su posición definitiva al final del tramo examinado. El tramo a
 * comparar se reduce en una posición en cada pasada.</p>
 *
 * <p><b>Finalización anticipada:</b> se usa una bandera ({@code huboIntercambio})
 * que se reinicia en cada pasada. Si una pasada completa termina sin realizar
 * ningún intercambio, el vector ya está ordenado y el algoritmo se detiene. Esto
 * mejora el mejor caso (vector ya ordenado) y también casos parcialmente
 * ordenados.</p>
 *
 * <p><b>Complejidad:</b></p>
 * <ul>
 *   <li>Temporal, mejor caso: {@code O(n)} — con el vector ya ordenado se hace
 *       una única pasada (n-1 comparaciones) y se corta por la bandera.</li>
 *   <li>Temporal, caso promedio y peor caso: {@code O(n²)} — se realizan del
 *       orden de n(n-1)/2 comparaciones (vector en orden inverso).</li>
 *   <li>Espacial auxiliar: {@code O(1)} — solo se usan unas pocas variables
 *       (contadores, índices, la bandera y una variable temporal para el
 *       intercambio), independientemente del tamaño del vector.</li>
 * </ul>
 *
 * <p><b>Estabilidad:</b> la comparación es estricta ({@code vector[j] > vector[j+1]}),
 * de modo que los elementos iguales nunca se intercambian entre sí. Por eso
 * Bubble Sort, en esta versión, es un algoritmo <i>estable</i>.</p>
 *
 * @author Agente Desarrollador
 */
public final class BubbleSort {

    /** Impide instanciar una clase que solo agrupa métodos estáticos. */
    private BubbleSort() {
    }

    // ------------------------------------------------------------------------
    // TIPOS AUXILIARES
    // ------------------------------------------------------------------------

    /**
     * Resultado de una ejecución del algoritmo: cantidad de comparaciones y de
     * intercambios efectivamente realizados.
     *
     * <p>Se modela como una clase inmutable para exponer las métricas de forma
     * clara y segura (solo lectura) hacia quien invoca el ordenamiento.</p>
     */
    public static final class Estadisticas {

        private final long comparaciones;
        private final long intercambios;

        /**
         * Construye el resultado con los contadores indicados.
         *
         * @param comparaciones cantidad de comparaciones entre elementos adyacentes
         * @param intercambios  cantidad de intercambios efectivamente realizados
         */
        private Estadisticas(long comparaciones, long intercambios) {
            this.comparaciones = comparaciones;
            this.intercambios = intercambios;
        }

        /**
         * @return cantidad total de comparaciones realizadas
         */
        public long getComparaciones() {
            return comparaciones;
        }

        /**
         * @return cantidad total de intercambios realizados
         */
        public long getIntercambios() {
            return intercambios;
        }
    }

    // ------------------------------------------------------------------------
    // API PÚBLICA
    // ------------------------------------------------------------------------

    /**
     * Ordena <b>in-place</b> (de menor a mayor) el vector {@code vector} usando
     * Bubble Sort con finalización anticipada y devuelve las estadísticas de la
     * ejecución.
     *
     * <p>Al finalizar, el vector recibido queda ordenado de forma ascendente; el
     * valor de retorno solo aporta los contadores de comparaciones e
     * intercambios.</p>
     *
     * @param vector vector de enteros a ordenar; no puede ser {@code null}
     * @return estadísticas con la cantidad de comparaciones e intercambios
     * @throws IllegalArgumentException si {@code vector} es {@code null}
     */
    public static Estadisticas ordenar(int[] vector) {
        // --------------------------------------------------------------------
        // VALIDACIÓN DE ENTRADA
        // Una referencia nula no puede recorrerse ni ordenarse. Se rechaza con
        // un mensaje descriptivo (caso límite). Un vector vacío o de un solo
        // elemento SÍ es válido: simplemente no requiere ninguna operación.
        // --------------------------------------------------------------------
        if (vector == null) {
            throw new IllegalArgumentException(
                    "El vector no puede ser null.");
        }

        // Contadores de las operaciones observadas. Se usa 'long' para evitar
        // desbordamiento en vectores grandes (n(n-1)/2 puede superar el rango
        // de un 'int' cuando n es elevado).
        long comparaciones = 0;
        long intercambios = 0;

        int n = vector.length;

        // --------------------------------------------------------------------
        // PASADAS EXTERNAS
        // Son necesarias, como máximo, n-1 pasadas. En cada una, el mayor
        // elemento del tramo no ordenado "burbujea" hasta su posición final.
        //
        // Si el vector tiene 0 o 1 elemento, 'n - 1' es <= 0 y el bucle no se
        // ejecuta: no hay nada que ordenar (0 comparaciones, 0 intercambios).
        // --------------------------------------------------------------------
        for (int i = 0; i < n - 1; i++) {

            // Bandera de la pasada: se asume que no habrá intercambios y se
            // activa si ocurre al menos uno. Si termina en 'false', el vector
            // ya está ordenado y se finaliza anticipadamente.
            boolean huboIntercambio = false;

            // -----------------------------------------------------------------
            // RECORRIDO INTERNO (comparación de adyacentes)
            // Se recorre el tramo aún no garantizado como ordenado. Tras 'i'
            // pasadas, los 'i' mayores ya están al final, por eso el límite es
            // 'n - 1 - i'.
            // -----------------------------------------------------------------
            for (int j = 0; j < n - 1 - i; j++) {

                // Cada par adyacente examinado cuenta como UNA comparación,
                // sin importar el resultado de la comparación.
                comparaciones++;

                // Orden ascendente: si el de la izquierda es mayor que el de la
                // derecha, están en orden incorrecto y se intercambian.
                // Al usar '>' (estricto), los elementos iguales no se tocan:
                // el algoritmo es estable.
                if (vector[j] > vector[j + 1]) {

                    // Intercambio in-place mediante variable temporal.
                    // Espacio auxiliar O(1): solo esta variable, sin estructuras
                    // adicionales dependientes de n.
                    int temporal = vector[j];
                    vector[j] = vector[j + 1];
                    vector[j + 1] = temporal;

                    // Se contabiliza el INTERCAMBIO efectivamente realizado y se
                    // marca la bandera para saber que esta pasada sí movió algo.
                    intercambios++;
                    huboIntercambio = true;
                }
            }

            // -----------------------------------------------------------------
            // FINALIZACIÓN ANTICIPADA
            // Si la pasada completa no realizó ningún intercambio, el vector ya
            // está ordenado y no tiene sentido seguir: se corta el bucle.
            // Este es el motivo por el que el mejor caso (vector ya ordenado)
            // cuesta solo O(n): una única pasada de n-1 comparaciones.
            // -----------------------------------------------------------------
            if (!huboIntercambio) {
                break;
            }
        }

        return new Estadisticas(comparaciones, intercambios);
    }

    // ------------------------------------------------------------------------
    // CASOS DE PRUEBA
    // ------------------------------------------------------------------------

    /**
     * Método de entrada con los casos de prueba solicitados en el prompt.
     *
     * <p>Cada caso verifica dos cosas: que el vector quede ordenado de forma
     * ascendente y que los contadores coincidan con los valores esperados.</p>
     *
     * @param args no se utilizan
     */
    public static void main(String[] args) {
        System.out.println("== Casos de prueba solicitados ==");

        // 1) Vector desordenado (caso general).
        //    Entrada [5, 2, 9, 1, 6] -> esperado [1, 2, 5, 6, 9].
        //    Comparaciones = 10, intercambios = 5 (cortó en la 4ª pasada).
        probar(new int[] {5, 2, 9, 1, 6}, new int[] {1, 2, 5, 6, 9}, 10, 5);

        // 2) Vector ya ordenado (mejor caso).
        //    Una sola pasada: n-1 = 4 comparaciones y 0 intercambios -> O(n).
        probar(new int[] {1, 2, 3, 4, 5}, new int[] {1, 2, 3, 4, 5}, 4, 0);

        // 3) Vector en orden inverso (peor caso).
        //    n = 5 -> comparaciones = n(n-1)/2 = 10 e intercambios = 10.
        probar(new int[] {5, 4, 3, 2, 1}, new int[] {1, 2, 3, 4, 5}, 10, 10);

        // 4) Vector con elementos repetidos.
        //    [3, 1, 3, 1, 2] -> [1, 1, 2, 3, 3]; comparaciones = 9, intercambios = 5.
        probar(new int[] {3, 1, 3, 1, 2}, new int[] {1, 1, 2, 3, 3}, 9, 5);

        // 5) Vector vacío: nada que ordenar -> 0 comparaciones, 0 intercambios.
        probar(new int[] {}, new int[] {}, 0, 0);

        // 6) Vector de un solo elemento: nada que ordenar -> 0 y 0.
        probar(new int[] {42}, new int[] {42}, 0, 0);

        System.out.println();
        System.out.println("== Casos límite adicionales ==");

        // Todos los elementos iguales: una pasada sin intercambios -> n-1 comparaciones.
        probar(new int[] {7, 7, 7}, new int[] {7, 7, 7}, 2, 0);

        // Dos elementos desordenados.
        probar(new int[] {2, 1}, new int[] {1, 2}, 1, 1);

        // Valores negativos y repetidos en los extremos del tipo int.
        probar(new int[] {Integer.MAX_VALUE, Integer.MIN_VALUE, 0, Integer.MIN_VALUE},
                new int[] {Integer.MIN_VALUE, Integer.MIN_VALUE, 0, Integer.MAX_VALUE},
                6, 4);

        // Validación de entrada nula: debe rechazarse con excepción descriptiva.
        probarVectorNulo();

        System.out.println();
        System.out.println("Todas las verificaciones finalizaron. " +
                "Revisá que no aparezca la palabra FALLO.");
    }

    /**
     * Ejecuta un caso de prueba: ordena una <b>copia</b> del vector de entrada
     * (para no perder el original) y comprueba el resultado ordenado y los
     * contadores.
     *
     * @param entrada          vector original a ordenar
     * @param esperado         vector esperado tras el ordenamiento
     * @param comparacionesEsperadas comparaciones que debería reportar el algoritmo
     * @param intercambiosEsperados  intercambios que debería reportar el algoritmo
     */
    private static void probar(int[] entrada,
                               int[] esperado,
                               long comparacionesEsperadas,
                               long intercambiosEsperados) {
        // Se clona para conservar 'entrada' y poder mostrarla tal cual en la salida.
        int[] vector = entrada.clone();

        // Se ordena in-place: 'vector' es mutado por el algoritmo.
        Estadisticas estadisticas = ordenar(vector);

        boolean ordenCorrecto = java.util.Arrays.equals(vector, esperado);
        boolean contadoresCorrectos =
                estadisticas.getComparaciones() == comparacionesEsperadas
                        && estadisticas.getIntercambios() == intercambiosEsperados;
        boolean correcto = ordenCorrecto && contadoresCorrectos;

        System.out.printf(
                "%-26s -> %-26s | comparaciones = %-3d (esp. %-3d) | intercambios = %-3d (esp. %-3d) | %s%n",
                formatear(entrada),
                formatear(vector),
                estadisticas.getComparaciones(),
                comparacionesEsperadas,
                estadisticas.getIntercambios(),
                intercambiosEsperados,
                correcto ? "OK" : "FALLO");
    }

    /**
     * Verifica que un vector {@code null} sea rechazado con la excepción
     * documentada.
     */
    private static void probarVectorNulo() {
        try {
            ordenar(null);
            System.out.println("ordenar(null) -> FALLO (no se lanzó ninguna excepción)");
        } catch (IllegalArgumentException excepcion) {
            System.out.printf("ordenar(null) -> OK (rechazado: %s)%n",
                    excepcion.getMessage());
        }
    }

    /**
     * Construye una representación legible de un vector para la salida de pruebas.
     *
     * @param vector vector a representar
     * @return cadena con los elementos entre corchetes (ej.: {@code [1, 2, 3]})
     */
    private static String formatear(int[] vector) {
        StringBuilder texto = new StringBuilder("[");
        for (int i = 0; i < vector.length; i++) {
            if (i > 0) {
                texto.append(", ");
            }
            texto.append(vector[i]);
        }
        return texto.append("]").toString();
    }
}

/*
 * ============================================================================
 * NOTAS DE IMPLEMENTACIÓN
 * ============================================================================
 * 1. El algoritmo ordena in-place: no se crean copias ni estructuras auxiliares
 *    dependientes de n. El único almacenamiento adicional es una variable
 *    temporal para el intercambio, por lo que el espacio auxiliar es O(1).
 *
 * 2. Los contadores se incrementan EXACTAMENTE cuando ocurre la operación:
 *    - 'comparaciones' se incrementa una vez por cada par adyacente evaluado,
 *      justo antes de decidir si hay que intercambiar.
 *    - 'intercambios' se incrementa únicamente cuando la condición de desorden
 *      es verdadera y se realiza el intercambio.
 *
 * 3. La finalización anticipada usa la bandera 'huboIntercambio'. Si una pasada
 *    completa no intercambia nada, el vector ya está ordenado y se corta el
 *    bucle. Gracias a esto el mejor caso (vector ya ordenado) es O(n).
 *
 * 4. El tramo interno decrece: 'j < n - 1 - i'. Tras 'i' pasadas, los 'i'
 *    mayores ya están en su lugar definitivo, así que no hace falta volver a
 *    compararlos.
 *
 * 5. Análisis de complejidad:
 *      - Mejor caso (ya ordenado): n-1 comparaciones, 0 intercambios -> O(n).
 *      - Peor caso (orden inverso): n(n-1)/2 comparaciones e intercambios -> O(n²).
 *      - Caso promedio: O(n²), porque la cantidad de inversiones promedia
 *        n(n-1)/4.
 *      - Espacio auxiliar: O(1).
 *
 * 6. Estabilidad: al comparar con '>' estricto, dos elementos iguales nunca se
 *    intercambian; el orden relativo de claves iguales se preserva.
 *
 * 7. Casos límite:
 *      - null -> IllegalArgumentException (no hay nada que ordenar/acceder).
 *      - vacío -> vector válido; 0 pasadas, 0 comparaciones, 0 intercambios.
 *      - un elemento -> vector válido; 0 pasadas, 0 comparaciones, 0 intercambios.
 *
 * 8. Se usa 'long' en los contadores para evitar desbordamiento en vectores
 *    grandes, donde n(n-1)/2 puede exceder el rango de 'int'.
 * ============================================================================
 */

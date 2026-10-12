/*
 * ============================================================================
 * PROMPT INICIAL UTILIZADO PARA GENERAR ESTA SOLUCIÓN
 * ============================================================================
 *
 * Quiero implementar en Java un algoritmo que determine si dos vectores de
 * enteros son iguales. Dos vectores se consideran iguales si tienen la misma
 * longitud y sus elementos coinciden en las mismas posiciones.
 *
 * Compará primero las longitudes. Si son distintas, devolvé `false` sin
 * recorrerlos. Si coinciden, compará los elementos posición por posición y
 * finalizá inmediatamente al detectar una diferencia. Devolvé `true` solo si
 * todos los elementos coinciden.
 *
 * Analizá la complejidad temporal:
 *   - Mejor caso: O(1), si las longitudes son distintas o la primera posición
 *     contiene una diferencia.
 *   - Peor caso: O(n), si los vectores son iguales o la diferencia aparece en
 *     la última posición.
 *   - Caso promedio: O(n), suponiendo que la primera diferencia puede aparecer
 *     con igual probabilidad en cualquiera de las posiciones; se examina
 *     aproximadamente la mitad de los elementos en promedio.
 *
 * La complejidad espacial auxiliar debe ser O(1). Definí también el
 * comportamiento para vectores vacíos y nulos; dos vectores vacíos deben
 * considerarse iguales, y un vector `null` debe manejarse de forma explícita y
 * documentada.
 *
 * Comentá el código para explicar la comparación y la finalización anticipada.
 * Incluí pruebas con vectores iguales, longitudes distintas, una diferencia en
 * la primera posición, una diferencia en la última posición y dos vectores
 * vacíos.
 * ============================================================================
 */

/**
 * Comparación de igualdad entre dos vectores de enteros.
 *
 * <p>Dos vectores son iguales si y solo si tienen la misma longitud y, para
 * cada índice {@code i} válido, sus elementos coinciden
 * ({@code a[i] == b[i]}). La única operación expuesta es
 * {@link #sonIguales(int[], int[])}.</p>
 *
 * <p><b>Estrategia:</b> primero se descartan los casos triviales (referencias
 * {@code null} y longitudes distintas) sin recorrer ningún elemento. Solo si
 * las longitudes coinciden se recorre posición por posición, cortando el
 * recorrido en cuanto aparece la primera diferencia.</p>
 *
 * <p><b>Complejidad temporal:</b> mejor caso {@code O(1)} (longitudes distintas
 * o diferencia en la primera posición); peor caso {@code O(n)} (vectores
 * iguales o diferencia en la última posición); caso promedio {@code O(n)}
 * (bajo el supuesto de que la primera diferencia es equiprobable en cualquier
 * posición, se examina en promedio la mitad de los elementos).</p>
 *
 * <p><b>Complejidad espacial auxiliar:</b> {@code O(1)} (solo se usa el índice
 * del recorrido; no se reserva memoria proporcional al tamaño de la entrada).</p>
 *
 * @author Agente Desarrollador
 */
public final class VectoresIguales {

    /** Impide instanciar una clase que solo agrupa métodos estáticos. */
    private VectoresIguales() {
    }

    // ------------------------------------------------------------------------
    // COMPARACIÓN DE IGUALDAD
    // ------------------------------------------------------------------------

    /**
     * Determina si los vectores {@code vectorA} y {@code vectorB} son iguales.
     *
     * <p>La igualdad exige misma longitud y elementos iguales en cada posición.
     * La comparación se resuelve en tres pasos, del más barato al más costoso:</p>
     * <ol>
     *   <li><b>Referencias {@code null}:</b> si alguna es {@code null} se
     *       resuelve por identidad de referencia. Dos vectores {@code null}
     *       son considerados iguales (misma "ausencia" de vector); si
     *       exactamente uno es {@code null}, son distintos.</li>
     *   <li><b>Longitudes:</b> si difieren, se devuelve {@code false} de
     *       inmediato, sin inspeccionar ningún elemento ({@code O(1)}).</li>
     *   <li><b>Elementos:</b> con longitudes iguales, se comparan las posiciones
     *       de izquierda a derecha. Ante la primera diferencia se devuelve
     *       {@code false} (finalización anticipada). Si el recorrido termina sin
     *       diferencias, se devuelve {@code true}.</li>
     * </ol>
     *
     * <p><b>Vectores vacíos:</b> dos vectores de longitud {@code 0} son iguales
     * (ambos carecen de elementos y no hay ninguna posición que pueda
     * diferenciarlos); el bucle no ejecuta ninguna iteración y se devuelve
     * {@code true}. Un vector vacío frente a uno no vacío es distinto, porque
     * sus longitudes difieren.</p>
     *
     * <p><b>Comportamiento ante {@code null} (definido explícitamente):</b>
     * {@code sonIguales(null, null)} devuelve {@code true}; si exactamente uno
     * de los dos argumentos es {@code null}, devuelve {@code false}. Se optó por
     * esta semántica —idéntica a la de {@code java.util.Arrays.equals(int[],
     * int[])}— en lugar de lanzar una excepción, porque la pregunta "¿son
     * iguales?" siempre tiene una respuesta booleana bien definida, incluso
     * cuando los vectores no existen.</p>
     *
     * @param vectorA primer vector a comparar; puede ser {@code null}
     * @param vectorB segundo vector a comparar; puede ser {@code null}
     * @return {@code true} si ambos vectores son iguales (misma longitud y
     *         mismos elementos posición por posición), o si ambos son
     *         {@code null}; {@code false} en cualquier otro caso
     */
    public static boolean sonIguales(int[] vectorA, int[] vectorB) {
        // --------------------------------------------------------------------
        // PASO 1: MANEJO EXPLÍCITO DE null
        // Si alguna referencia es null, no se puede acceder a .length; se
        // resuelve por identidad. 'vectorA == vectorB' es true únicamente si
        // ambos son null (o apuntan al mismo objeto, caso en el que el
        // resultado también sería correcto: un objeto es igual a sí mismo).
        // Así, (null, null) -> true  y  (null, x) / (x, null) -> false.
        // --------------------------------------------------------------------
        if (vectorA == null || vectorB == null) {
            return vectorA == vectorB;
        }

        // --------------------------------------------------------------------
        // PASO 2: COMPARACIÓN DE LONGITUDES (CORTA CIRCUITO)
        // Si las longitudes son distintas, los vectores no pueden ser iguales:
        // se devuelve false SIN recorrer ninguna posición. Esta es la vía más
        // económica hacia el mejor caso O(1), pues cuesta una sola comparación.
        // --------------------------------------------------------------------
        if (vectorA.length != vectorB.length) {
            return false;
        }

        // --------------------------------------------------------------------
        // PASO 3: COMPARACIÓN POSICIÓN POR POSICIÓN (CON FINALIZACIÓN
        // ANTICIPADA)
        //
        // Con longitudes iguales, se comparan los elementos de izquierda a
        // derecha. En cuanto se halla una diferencia, los vectores ya no pueden
        // ser iguales y se devuelve false inmediatamente: no tiene sentido
        // seguir mirando las posiciones restantes.
        //
        // INVARIANTE: antes de examinar la posición 'i', todos los elementos en
        // los índices 0..i-1 coinciden. Al salir del bucle (i == n) sin haber
        // retornado, todos los elementos coinciden y el resultado es true.
        //
        // La comparación 'vectorA[i] != vectorB[i]' es una comparación por valor
        // entre primitivos int: es exacta para todo el rango, incluidos
        // Integer.MIN_VALUE y Integer.MAX_VALUE.
        //
        // COSTO: se examinan a lo sumo n posiciones. Mejor caso O(1) (diferencia
        // en la primera posición), peor caso O(n) (iguales o diferencia en la
        // última posición) y, en promedio, ~n/2 comparaciones -> O(n). Solo se
        // usa el índice 'i' como variable auxiliar -> espacio O(1).
        // --------------------------------------------------------------------
        for (int i = 0; i < vectorA.length; i++) {
            if (vectorA[i] != vectorB[i]) {
                return false; // Diferencia detectada: se corta el recorrido ya.
            }
        }

        // Se compararon todas las posiciones y ninguna difirió.
        return true;
    }

    // ------------------------------------------------------------------------
    // CASOS DE PRUEBA
    // ------------------------------------------------------------------------

    /**
     * Método de entrada: ejecuta {@link #sonIguales(int[], int[])} sobre un
     * conjunto de casos representativos y verifica cada resultado contra el
     * valor esperado.
     *
     * @param args no se utilizan
     */
    public static void main(String[] args) {
        System.out.println("== Igualdad entre dos vectores de enteros ==");
        System.out.println();

        // Caso 1: vectores iguales -> true (peor caso O(n): se comparan todas).
        probar("Iguales", new int[] {1, 2, 3, 4, 5}, new int[] {1, 2, 3, 4, 5}, true);
        probar("Iguales (1 elemento)", new int[] {7}, new int[] {7}, true);

        // Caso 2: longitudes distintas -> false sin recorrer (mejor caso O(1)).
        probar("Longitudes distintas (A más corto)", new int[] {1, 2, 3}, new int[] {1, 2, 3, 4}, false);
        probar("Longitudes distintas (A más largo)", new int[] {1, 2, 3, 4}, new int[] {1, 2, 3}, false);

        // Caso 3: diferencia en la PRIMERA posición -> false inmediato (O(1)).
        probar("Diferencia en la primera", new int[] {9, 2, 3, 4, 5}, new int[] {1, 2, 3, 4, 5}, false);

        // Caso 4: diferencia en la ÚLTIMA posición -> false tras recorrer todo.
        probar("Diferencia en la última", new int[] {1, 2, 3, 4, 9}, new int[] {1, 2, 3, 4, 5}, false);

        // Caso 5: DOS vectores VACÍOS -> iguales (true); el bucle no itera.
        probar("Dos vectores vacíos", new int[] {}, new int[] {}, true);

        // Caso 6: vacío frente a no vacío -> false (longitudes distintas).
        probar("Vacío vs no vacío", new int[] {}, new int[] {1}, false);

        System.out.println();
        System.out.println("== Casos límite: vectores null (comportamiento definido) ==");

        // Ambos null -> se consideran iguales.
        probar("Ambos null", null, null, true);

        // Exactamente un null -> distintos.
        probar("A null, B no null", null, new int[] {1, 2}, false);
        probar("A no null, B null", new int[] {1, 2}, null, false);

        // null frente a vacío -> distintos (uno existe, el otro no).
        probar("null vs vacío", null, new int[] {}, false);

        // Caso límite adicional: valores extremos de int (comparación exacta).
        probar("Extremos de int (iguales)",
                new int[] {Integer.MIN_VALUE, 0, Integer.MAX_VALUE},
                new int[] {Integer.MIN_VALUE, 0, Integer.MAX_VALUE}, true);
        probar("Extremos de int (diferencia en medio)",
                new int[] {Integer.MIN_VALUE, 1, Integer.MAX_VALUE},
                new int[] {Integer.MIN_VALUE, 0, Integer.MAX_VALUE}, false);
    }

    /**
     * Ejecuta un caso de prueba, muestra el resultado obtenido y lo contrasta
     * con el esperado.
     *
     * @param nombre   descripción del caso para la salida
     * @param vectorA  primer vector de entrada (puede ser {@code null})
     * @param vectorB  segundo vector de entrada (puede ser {@code null})
     * @param esperado resultado esperado
     */
    private static void probar(String nombre, int[] vectorA, int[] vectorB, boolean esperado) {
        boolean obtenido = sonIguales(vectorA, vectorB);
        boolean correcto = obtenido == esperado;

        System.out.printf("%-38s %-16s == %-16s | obtenido = %-5b | esperado = %-5b | %s%n",
                nombre, formatear(vectorA), formatear(vectorB), obtenido, esperado,
                correcto ? "OK" : "FALLO");
    }

    /**
     * Construye una representación legible de un vector para la salida de
     * pruebas, contemplando el caso {@code null}.
     *
     * @param vector vector a representar (puede ser {@code null})
     * @return cadena con los elementos entre corchetes, o {@code "null"}
     */
    private static String formatear(int[] vector) {
        if (vector == null) {
            return "null";
        }
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
 * NOTAS DE IMPLEMENTACIÓN (aclaraciones al prompt inicial)
 * ============================================================================
 * 1. ORDEN DE LAS COMPROBACIONES: se priorizan las verificaciones de costo
 *    constante (null y longitud) antes de tocar cualquier elemento. Así, los
 *    casos "fáciles" se resuelven en O(1) y solo los vectores de igual longitud
 *    llegan al recorrido O(n).
 *
 * 2. FINALIZACIÓN ANTICIPADA: el bucle retorna false en la primera diferencia
 *    encontrada. Esto explica el mejor caso O(1) (diferencia en la posición 0) y
 *    evita trabajo innecesario cuando los vectores difieren temprano.
 *
 * 3. PEOR CASO O(n): se alcanza cuando los vectores son iguales (hay que
 *    confirmar las n posiciones) o cuando la única diferencia está en la última
 *    posición.
 *
 * 4. CASO PROMEDIO O(n): si la primera diferencia puede aparecer con igual
 *    probabilidad en cualquiera de las n posiciones, el número esperado de
 *    comparaciones es aproximadamente n/2, que sigue siendo lineal. (El caso
 *    "sin diferencias" se examina aparte, pero no cambia la cota O(n).)
 *
 * 5. ESPACIO AUXILIAR O(1): el algoritmo no crea copias ni estructuras
 *    adicionales; solo emplea el índice 'i'. No modifica los vectores de
 *    entrada (no hay efectos secundarios).
 *
 * 6. VECTORES VACÍOS: dos vectores de longitud 0 son iguales (true). Es la
 *    semántica natural: no hay ninguna posición que los diferencie.
 *
 * 7. VECTOR null (DECISIÓN DOCUMENTADA): se maneja explícitamente como en
 *    java.util.Arrays.equals: (null, null) -> true y (null, x)/(x, null) ->
 *    false. Se prefirió esta semántica booleana a lanzar una excepción porque
 *    la pregunta de igualdad siempre admite respuesta, y así la función es
 *    total (no falla ante ninguna entrada). Un null frente a un vector vacío
 *    se considera distinto (false): uno no existe y el otro sí, aunque no tenga
 *    elementos.
 *
 * 8. COMPARACIÓN DE VALORES: '!=' sobre primitivos int compara por valor y es
 *    exacto en todo el rango (incluidos Integer.MIN_VALUE y Integer.MAX_VALUE).
 * ============================================================================
 */

/*
 * ============================================================================
 * PROMPT INICIAL UTILIZADO PARA GENERAR ESTA SOLUCIÓN
 * ============================================================================
 *
 * Quiero implementar en Java dos soluciones diferentes para detectar si un
 * vector de enteros contiene elementos duplicados. Ambas deben recibir el mismo
 * vector y devolver `true` si algún valor aparece más de una vez, o `false` en
 * caso contrario. No hace falta devolver cuáles son los duplicados.
 *
 * 1. La primera solución debe comparar los elementos usando dos ciclos anidados.
 *    Para evitar comparar un elemento consigo mismo o repetir pares, compará
 *    cada posición `i` con las posiciones posteriores `j`, comenzando en `i + 1`.
 *    Detené la búsqueda cuando encuentres un duplicado.
 * 2. La segunda solución debe usar un `HashSet<Integer>`. Al recorrer el vector,
 *    intentá agregar cada valor al conjunto; si ya estaba presente, se encontró
 *    un duplicado.
 *
 * Comentá el código y compará las implementaciones en complejidad temporal y
 * espacial:
 *   - Dos ciclos anidados: tiempo O(n²) en el peor caso y espacio auxiliar O(1).
 *   - HashSet: tiempo esperado O(n) y espacio auxiliar O(n), debido al
 *     almacenamiento de los elementos visitados.
 *
 * Aclará que el costo de `HashSet` es esperado/promedio y que ambas soluciones
 * pueden detenerse antes si encuentran un duplicado.
 *
 * Probá las dos implementaciones con los mismos casos: vector vacío, un solo
 * elemento, todos los elementos distintos, un duplicado al principio, un
 * duplicado al final y varios valores repetidos. Los resultados de ambas
 * soluciones deben coincidir.
 * ============================================================================
 */

import java.util.HashSet;
import java.util.Set;

/**
 * Detección de elementos duplicados en un vector de enteros, con dos
 * estrategias alternativas implementadas para poder compararlas.
 *
 * <p>Ambas soluciones reciben el mismo {@code int[]} y devuelven {@code true}
 * si algún valor aparece más de una vez y {@code false} en caso contrario. No
 * informan cuáles son los duplicados, solo su existencia.</p>
 *
 * <ul>
 *   <li>{@link #tieneDuplicadosFuerzaBruta(int[])}: dos ciclos anidados.
 *       Complejidad temporal {@code O(n²)} (peor caso) y espacial auxiliar
 *       {@code O(1)}.</li>
 *   <li>{@link #tieneDuplicadosConHashSet(int[])}: un {@code HashSet<Integer>}.
 *       Complejidad temporal esperada {@code O(n)} y espacial auxiliar
 *       {@code O(n)}.</li>
 * </ul>
 *
 * @author Agente Desarrollador
 */
public final class DetectarDuplicados {

    /** Impide instanciar una clase que solo agrupa métodos estáticos. */
    private DetectarDuplicados() {
    }

    // ------------------------------------------------------------------------
    // SOLUCIÓN 1: FUERZA BRUTA (DOS CICLOS ANIDADOS)
    // ------------------------------------------------------------------------

    /**
     * Indica si {@code vector} contiene algún valor repetido, comparando cada
     * posición con todas las posiciones posteriores mediante dos ciclos
     * anidados.
     *
     * <p>Para cada índice {@code i} se examinan solo los índices
     * {@code j = i + 1 .. n - 1}. Así cada par de posiciones
     * <em>(i, j) con i &lt; j</em> se compara exactamente una vez y ningún
     * elemento se compara consigo mismo. En cuanto se encuentra una igualdad se
     * devuelve {@code true} de inmediato (detección temprana).</p>
     *
     * <p>Complejidad temporal: {@code O(n²)} en el peor caso (cuando no hay
     * duplicados se realizan {@code n(n-1)/2} comparaciones). Complejidad
     * espacial auxiliar: {@code O(1)}, porque solo se usan los índices
     * {@code i} y {@code j}.</p>
     *
     * @param vector vector de enteros a examinar; no puede ser {@code null}
     * @return {@code true} si algún valor aparece más de una vez,
     *         {@code false} en caso contrario
     * @throws IllegalArgumentException si {@code vector} es {@code null}
     */
    public static boolean tieneDuplicadosFuerzaBruta(int[] vector) {
        validar(vector);

        // --------------------------------------------------------------------
        // RECORRIDO CON DOS CICLOS ANIDADOS
        //
        // Bucle externo: fija la posición 'i' cuyo valor se quiere verificar.
        // Bucle interno: compara vector[i] contra las posiciones POSTERIORES
        //   'j', que arrancan en 'i + 1'. De esta forma:
        //     - Un elemento nunca se compara consigo mismo (j > i).
        //     - Cada par (i, j) se compara una sola vez, porque el segundo
        //       índice siempre es mayor que el primero.
        //
        // Cantidad de comparaciones en el peor caso (ningún duplicado):
        //     sum_{i=0}^{n-2} (n - 1 - i) = (n-1) + (n-2) + ... + 1 = n(n-1)/2
        //   Es decir, del orden de n² -> O(n²).
        //
        // DETENCIÓN TEMPRANA: el return dentro del bucle interno corta ambos
        // ciclos apenas aparece la primera coincidencia. Si el duplicado está
        // en las primeras posiciones, el costo real es mucho menor que O(n²).
        //
        // INVARIANTE: al comenzar cada iteración 'i', ningún par (a, b) con
        // a < i fue igual, porque en las iteraciones anteriores se recorrieron
        // todas sus combinaciones sin hallar coincidencia.
        // --------------------------------------------------------------------
        for (int i = 0; i < vector.length; i++) {
            for (int j = i + 1; j < vector.length; j++) {
                if (vector[i] == vector[j]) {
                    return true; // primer duplicado encontrado: no seguir
                }
            }
        }

        // Se compararon todos los pares posibles y no hubo ninguna igualdad:
        // todos los valores son distintos.
        return false;
    }

    // ------------------------------------------------------------------------
    // SOLUCIÓN 2: HashSet<Integer>
    // ------------------------------------------------------------------------

    /**
     * Indica si {@code vector} contiene algún valor repetido, registrando en un
     * {@link HashSet} los valores ya visitados durante un único recorrido.
     *
     * <p>Para cada elemento se intenta insertar en el conjunto:
     * {@code add} devuelve {@code true} si el valor no estaba (y lo agrega), o
     * {@code false} si ya existía. Un {@code false} significa que ese valor
     * apareció antes: hay un duplicado.</p>
     *
     * <p>Complejidad temporal ESPERADA: {@code O(n)}. La operación
     * {@code add} de un {@code HashSet} es {@code O(1)} en promedio, porque el
     * conjunto está respaldado por una tabla hash. Es un costo esperado/promedio,
     * no garantizado: en el peor caso teórico (todas las claves colisionando en
     * el mismo bucket) podría degenerar a {@code O(n²)}. Complejidad espacial
     * auxiliar: {@code O(n)}, porque en el peor caso (todos los elementos
     * distintos) el conjunto almacena los {@code n} valores visitados.</p>
     *
     * <p>Las comparaciones usan {@code Integer.equals}, que compara por valor,
     * por lo que el resultado es correcto para cualquier {@code int}
     * (no solo para el rango de caché -128..127).</p>
     *
     * @param vector vector de enteros a examinar; no puede ser {@code null}
     * @return {@code true} si algún valor aparece más de una vez,
     *         {@code false} en caso contrario
     * @throws IllegalArgumentException si {@code vector} es {@code null}
     */
    public static boolean tieneDuplicadosConHashSet(int[] vector) {
        validar(vector);

        // --------------------------------------------------------------------
        // RECORRIDO ÚNICO CON CONJUNTO DE VISITADOS
        //
        // 'vistos' guarda los valores que ya aparecieron. El autoboxing (int ->
        // Integer) se aplica automáticamente al llamar a add(value).
        //
        // LÓGICA DE add(): la operación es equivalente a "si no existe, agrégalo
        // y devolvé true; si ya existe, devolvé false". Por eso alcanza con
        // negar su resultado para decidir: "si no se pudo agregar, ya estaba".
        //
        // DETENCIÓN TEMPRANA: se corta el recorrido en el primer valor repetido.
        //
        // COSTO EN EL PEOR CASO (sin duplicados): se termina de recorrer todo el
        // vector y el conjunto llega a contener los n elementos -> O(n) tiempo
        // esperado y O(n) espacio.
        // --------------------------------------------------------------------
        Set<Integer> vistos = new HashSet<>();
        for (int valor : vector) {
            if (!vistos.add(valor)) {
                return true; // 'valor' ya había aparecido antes: duplicado
            }
        }

        // Se examinaron todos los elementos sin encontrar repeticiones.
        return false;
    }

    // ------------------------------------------------------------------------
    // VALIDACIÓN DE LA ENTRADA
    // ------------------------------------------------------------------------

    /**
     * Valida que el vector de entrada no sea {@code null}.
     *
     * <p>La decisión para el vector {@code null} es rechazarlo con una
     * excepción descriptiva (no se puede responder sobre una entrada
     * inexistente). En cambio, un vector vacío no es un error: devolver
     * {@code false} (no hay valores repetidos porque no hay valores) es la
     * semántica correcta y los bucles de ambas soluciones lo resuelven sin
     * ejecutar ninguna iteración.</p>
     *
     * @param vector vector a validar
     * @throws IllegalArgumentException si {@code vector} es {@code null}
     */
    private static void validar(int[] vector) {
        if (vector == null) {
            throw new IllegalArgumentException("El vector no puede ser null.");
        }
    }

    // ------------------------------------------------------------------------
    // CASOS DE PRUEBA
    // ------------------------------------------------------------------------

    /**
     * Método de entrada: ejecuta ambas implementaciones sobre los mismos
     * vectores de prueba y verifica que sus resultados coincidan entre sí y con
     * el resultado esperado.
     *
     * @param args no se utilizan
     */
    public static void main(String[] args) {
        System.out.println("== Detección de duplicados: ambas soluciones sobre los mismos casos ==");
        System.out.println("fb   = tieneDuplicadosFuerzaBruta (dos ciclos anidados)");
        System.out.println("hash = tieneDuplicadosConHashSet (HashSet<Integer>)");
        System.out.println();

        // Caso 1: vector vacío -> ningún valor puede repetirse -> false.
        probar("Vector vacío", new int[] {}, false);

        // Caso 2: un solo elemento -> no puede haber duplicados -> false.
        probar("Un solo elemento", new int[] {42}, false);

        // Caso 3: todos los elementos distintos -> no hay duplicados -> false.
        probar("Todos distintos", new int[] {10, 20, 30, 40, 50}, false);

        // Caso 4: el duplicado aparece al principio -> true (detección temprana).
        probar("Duplicado al principio", new int[] {7, 7, 1, 2, 3}, true);

        // Caso 5: el duplicado recién aparece al final -> true. En la solución
        // de fuerza bruta obliga a comparar casi todos los pares; en la de
        // HashSet obliga a recorrer casi todo el vector.
        probar("Duplicado al final", new int[] {1, 2, 3, 7, 7}, true);

        // Caso 6: varios valores repetidos -> true.
        probar("Varios repetidos", new int[] {3, 1, 4, 1, 5, 4, 3}, true);

        // Caso límite adicional: valores negativos/positivos extremos. El
        // HashSet compara por valor (Integer.equals), por lo que distingue
        // correctamente elementos que el autoboxing agruparía en la caché.
        probar("Con extremos de int", new int[] {Integer.MIN_VALUE, 0, Integer.MAX_VALUE, Integer.MIN_VALUE}, true);

        System.out.println();
        System.out.println("== Caso límite: vector nulo (debe rechazarse en ambas) ==");
        probarVectorNulo();
    }

    /**
     * Ejecuta un caso de prueba contra las dos implementaciones y muestra si
     * ambos resultados coinciden entre sí y con el valor esperado.
     *
     * @param nombre   descripción del caso para la salida
     * @param vector   entrada de las funciones
     * @param esperado resultado esperado (igual para ambas soluciones)
     */
    private static void probar(String nombre, int[] vector, boolean esperado) {
        boolean fuerzaBruta = tieneDuplicadosFuerzaBruta(vector);
        boolean conHashSet = tieneDuplicadosConHashSet(vector);
        boolean coinciden = fuerzaBruta == conHashSet;
        boolean correcto = coinciden && fuerzaBruta == esperado;

        System.out.printf("%-24s %-28s | fb = %-5s | hash = %-5s | esperado = %-5s | %s%n",
                nombre, formatear(vector), fuerzaBruta, conHashSet, esperado,
                correcto ? "OK" : "FALLO");
    }

    /**
     * Verifica que un vector {@code null} sea rechazado con la excepción
     * documentada por ambas soluciones.
     */
    private static void probarVectorNulo() {
        // Solución de fuerza bruta.
        try {
            tieneDuplicadosFuerzaBruta(null);
            System.out.println("Fuerza bruta con null -> FALLO (no se lanzó ninguna excepción)");
        } catch (IllegalArgumentException excepcion) {
            System.out.printf("Fuerza bruta con null -> OK (rechazado: %s)%n", excepcion.getMessage());
        }

        // Solución con HashSet.
        try {
            tieneDuplicadosConHashSet(null);
            System.out.println("HashSet con null -> FALLO (no se lanzó ninguna excepción)");
        } catch (IllegalArgumentException excepcion) {
            System.out.printf("HashSet con null -> OK (rechazado: %s)%n", excepcion.getMessage());
        }
    }

    /**
     * Construye una representación legible del vector para la salida de pruebas.
     *
     * @param vector vector a representar
     * @return cadena con los elementos entre corchetes
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
 * NOTAS DE IMPLEMENTACIÓN (comparación de las dos soluciones)
 * ============================================================================
 * 1. FUERZA BRUTA: tieneDuplicadosFuerzaBruta
 *    - Compara cada par de posiciones (i, j) con i < j. En el peor caso (ningún
 *      duplicado) realiza exactamente n(n-1)/2 comparaciones.
 *    - Complejidad temporal: O(n²) en el peor caso. Puede detenerse mucho antes
 *      si el primer duplicado aparece pronto (mejor caso O(1): los dos primeros
 *      elementos son iguales).
 *    - Complejidad espacial auxiliar: O(1). Solo usa los índices i y j.
 *    - Ventaja: no consume memoria adicional. Desventaja: lenta para vectores
 *      grandes.
 *
 * 2. HashSet: tieneDuplicadosConHashSet
 *    - Registra en el conjunto cada valor; cuando add devuelve 'false', ese
 *      valor ya había sido visto -> duplicado.
 *    - Complejidad temporal ESPERADA: O(n), porque add en un HashSet es O(1) en
 *      promedio (tabla hash). IMPORTANTE: es un costo esperado/promedio, no
 *      garantizado. En el peor caso teórico, si todas las claves colisionan en
 *      el mismo bucket, la tabla degenera en una lista y el costo pasa a ser
 *      O(n²). Dualmente a la fuerza bruta, también se detiene en cuanto
 *      encuentra un duplicado.
 *    - Complejidad espacial auxiliar: O(n). En el peor caso (todos los
 *      elementos distintos) se almacenan los n valores en el conjunto.
 *    - Ventaja: lineal en el caso promedio, muy superior para vectores
 *      grandes. Desventaja: requiere memoria proporcional a la cantidad de
 *      elementos distintos.
 *
 * 3. DETENCIÓN TEMPRANA: ambas implementaciones devuelven 'true' apenas
 *    encuentran el primer duplicado, sin seguir examinando el resto del vector.
 *
 * 4. RESULTADOS COINCIDENTES: para un mismo vector ambas soluciones devuelven
 *    exactamente el mismo resultado, como se verifica caso por caso en main().
 *    La de fuerza bruta detecta la igualdad comparando valores con '=='; la del
 *    HashSet la detecta reutilizando la igualdad por valor de Integer.equals.
 *
 * 5. VECTOR VACÍO: no es un error; se devuelve 'false' porque no hay valores,
 *    y no puede haber un valor repetido. Los bucles simplemente no ejecutan
 *    ninguna iteración.
 *
 * 6. VECTOR NULO: se rechaza con IllegalArgumentException en ambas soluciones,
 *    siguiendo la convención defensiva del resto del repositorio.
 * ============================================================================
 */
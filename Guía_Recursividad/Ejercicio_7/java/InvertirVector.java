/*
 * ============================================================================
 * PROMPT INICIAL UTILIZADO PARA GENERAR ESTA SOLUCIÓN
 * ============================================================================
 *
 * Quiero implementar en Java dos versiones distintas de un algoritmo que
 * invierta el orden de los elementos de un vector de enteros.
 *
 * 1. Versión con vector auxiliar: crea y devuelve un NUEVO vector con los
 *    elementos en orden inverso, sin modificar el vector original.
 * 2. Versión in-place: invierte el MISMO vector intercambiando los elementos de
 *    los extremos hacia el centro, sin utilizar memoria adicional significativa.
 *    Esta versión modifica el vector recibido.
 *
 * Compará ambas soluciones indicando sus ventajas y desventajas:
 *   - Ambas tienen complejidad temporal O(n), porque cada elemento se copia o
 *     participa en un intercambio una cantidad constante de veces.
 *   - La versión con vector auxiliar tiene complejidad espacial O(n), pero
 *     conserva el vector original.
 *   - La versión in-place tiene complejidad espacial auxiliar O(1), pero
 *     modifica el vector recibido.
 *
 * Comentá el código para explicar cómo se construye la versión auxiliar y cómo
 * avanzan los índices de la versión in-place. Definí el comportamiento para
 * vectores vacíos y de un solo elemento. Si el vector es null, manejalo de
 * forma explícita y documentada.
 *
 * Probá ambas versiones con los mismos casos: varios elementos, un vector vacío
 * y uno de un solo elemento. Verificá que produzcan el mismo orden invertido y
 * que la versión auxiliar no modifique la entrada original.
 * ============================================================================
 */

/**
 * Inversión del orden de los elementos de un vector de enteros, en dos
 * variantes.
 *
 * <p>Se ofrecen dos soluciones con el mismo resultado (el vector con sus
 * elementos en orden inverso) pero con compromisos distintos:</p>
 * <ul>
 *   <li>{@link #invertirConVectorAuxiliar(int[])} — <b>función pura</b>: crea un
 *       vector nuevo y deja intacto el original. Complejidad espacial
 *       {@code O(n)}.</li>
 *   <li>{@link #invertirInPlace(int[])} — <b>procedimiento con efecto</b>:
 *       modifica el vector recibido intercambiando extremos hacia el centro.
 *       Complejidad espacial auxiliar {@code O(1)}.</li>
 * </ul>
 *
 * <p><b>Complejidad temporal de ambas:</b> {@code O(n)}. En la versión auxiliar
 * cada elemento se lee y se copia una vez; en la versión in-place cada elemento
 * participa en a lo sumo un intercambio. En los dos casos el trabajo es lineal
 * en la cantidad de elementos {@code n}.</p>
 *
 * <p><b>Comportamiento para vectores vacíos y de un solo elemento:</b> ambos
 * son válidos y ya están "invertidos". La versión auxiliar devuelve un vector
 * nuevo de igual longitud (vacío o de un elemento) y la versión in-place no
 * realiza ningún intercambio. No se trata como error.</p>
 *
 * <p><b>Comportamiento ante {@code null} (definido explícitamente):</b> se
 * rechaza con {@link IllegalArgumentException} descriptiva. No tiene sentido
 * invertir una estructura inexistente, y devolver {@code null} o un vector vacío
 * ocultaría el error de programación. Se sigue la convención defensiva del
 * resto del repositorio.</p>
 *
 * @author Agente Desarrollador
 */
public final class InvertirVector {

    /** Impide instanciar una clase que solo agrupa métodos estáticos. */
    private InvertirVector() {
    }

    // ------------------------------------------------------------------------
    // VERSIÓN 1: VECTOR AUXILIAR (NO MODIFICA LA ENTRADA)
    // ------------------------------------------------------------------------

    /**
     * Devuelve un <b>nuevo</b> vector con los elementos de {@code vector} en
     * orden inverso, sin modificar {@code vector}.
     *
     * <p><b>Cómo se construye:</b> se reserva un vector {@code resultado} de la
     * misma longitud y se recorre el original de izquierda a derecha. El
     * elemento de la posición {@code i} del original se escribe en la posición
     * espejo {@code n - 1 - i} del resultado. Así, el primer elemento del
     * original termina último, el segundo termina penúltimo, y el último
     * termina primero.</p>
     *
     * <pre>
     *   original : [1, 2, 3, 4, 5]      n = 5
     *   resultado: [5, 4, 3, 2, 1]
     *                ^           ^
     *     resultado[i] = original[n - 1 - i]
     * </pre>
     *
     * <p><b>Ventaja:</b> preserva el vector de entrada (función sin efectos
     * secundarios), lo que permite seguir usando el original y facilita razonar
     * sobre el código.</p>
     *
     * <p><b>Desventaja:</b> requiere {@code O(n)} de memoria adicional, lo que
     * puede ser relevante con vectores muy grandes.</p>
     *
     * <p><b>Complejidad temporal:</b> {@code O(n)} (una lectura y una escritura
     * por elemento). <b>Complejidad espacial:</b> {@code O(n)} por el vector
     * auxiliar.</p>
     *
     * <p><b>Vector vacío o de un elemento:</b> se reserva un vector nuevo de la
     * misma longitud y el bucle copia cada elemento a su posición espejo (que,
     * con 0 o 1 elementos, coincide con la posición original). El resultado es
     * una copia correcta del vector.</p>
     *
     * @param vector vector de enteros a invertir; no puede ser {@code null}
     * @return un vector nuevo con los elementos de {@code vector} en orden
     *         inverso; el argumento no se modifica
     * @throws IllegalArgumentException si {@code vector} es {@code null}
     */
    public static int[] invertirConVectorAuxiliar(int[] vector) {
        validar(vector);

        int n = vector.length;

        // --------------------------------------------------------------------
        // RESERVA DEL VECTOR AUXILIAR
        // Se crea un vector nuevo de la MISMA longitud. Es este vector el que
        // recibe el resultado; el original nunca se escribe.
        // --------------------------------------------------------------------
        int[] resultado = new int[n];

        // --------------------------------------------------------------------
        // COPIA EN ORDEN ESPEJO
        //
        // Para cada índice 'i' del original (de 0 a n-1), la posición inversa es
        // 'n - 1 - i':
        //   i = 0        -> n - 1   (el primero pasa al último lugar)
        //   i = n - 1    -> 0       (el último pasa al primer lugar)
        // Las posiciones se completan de a una, sin solapamientos, porque hay
        // una correspondencia biunívoca entre índices.
        //
        // INVARIANTE: tras la iteración del índice 'i', las posiciones
        // [0..i] de 'resultado' ya contienen los elementos invertidos del
        // prefijo [0..i] del original.
        //
        // COSTO: n iteraciones con trabajo constante cada una -> tiempo O(n).
        // El vector 'resultado' ocupa n enteros -> espacio O(n).
        // --------------------------------------------------------------------
        for (int i = 0; i < n; i++) {
            resultado[i] = vector[n - 1 - i];
        }

        return resultado;
    }

    // ------------------------------------------------------------------------
    // VERSIÓN 2: IN-PLACE (MODIFICA LA ENTRADA)
    // ------------------------------------------------------------------------

    /**
     * Invierte los elementos de {@code vector} <b>en el mismo vector</b>,
     * intercambiando los elementos de los extremos hacia el centro. Modifica el
     * vector recibido y no devuelve nada.
     *
     * <p><b>Cómo avanzan los índices:</b> se mantienen dos índices,
     * {@code izquierda} (empieza en {@code 0}) y {@code derecha} (empieza en
     * {@code n - 1}). En cada iteración se intercambian los elementos de esas
     * dos posiciones; luego {@code izquierda} avanza hacia la derecha
     * ({@code izquierda++}) y {@code derecha} retrocede hacia la izquierda
     * ({@code derecha--}). El proceso se repite mientras
     * {@code izquierda < derecha}.</p>
     *
     * <pre>
     *   [1, 2, 3, 4, 5]
     *    i           d   -> swap(0,4) -> [5, 2, 3, 4, 1]
     *       i     d      -> swap(1,3) -> [5, 4, 3, 2, 1]
     *          i,d        -> i >= d: se detiene (el centro no se toca)
     * </pre>
     *
     * <p><b>¿Por qué se detiene en {@code izquierda < derecha}?</b> Cuando los
     * índices se cruzan (o coinciden en el elemento central, si {@code n} es
     * impar), todos los pares ya fueron intercambiados. Ese elemento del centro,
     * en un vector de longitud impar, queda en su lugar y no necesita moverse;
     * en un vector de longitud par no existe ningún elemento sin pareja.</p>
     *
     * <p><b>Ventaja:</b> usa memoria auxiliar {@code O(1)} (solo dos índices y
     * una variable temporal), por lo que es apta para vectores muy grandes.</p>
     *
     * <p><b>Desventaja:</b> destruye el orden original del vector recibido. Si
     * el llamador necesita conservarlo, debe copiarlo antes.</p>
     *
     * <p><b>Complejidad temporal:</b> {@code O(n)} (se realizan
     * {@code floor(n / 2)} intercambios, cada uno de costo constante).
     * <b>Complejidad espacial auxiliar:</b> {@code O(1)}.</p>
     *
     * <p><b>Vector vacío o de un elemento:</b> {@code izquierda} arranca en
     * {@code 0} y {@code derecha} en {@code -1} (vacío) o {@code 0} (un
     * elemento), de modo que la condición {@code izquierda < derecha} es falsa
     * de entrada: no se realiza ningún intercambio y el vector queda igual (lo
     * cual es correcto, pues ya está invertido).</p>
     *
     * @param vector vector de enteros a invertir en el lugar; no puede ser
     *               {@code null} y es <b>modificado</b> por este método
     * @throws IllegalArgumentException si {@code vector} es {@code null}
     */
    public static void invertirInPlace(int[] vector) {
        validar(vector);

        // --------------------------------------------------------------------
        // DOS ÍNDICES QUE VAN AL ENCUENTRO
        //   'izquierda' comienza en el primer elemento y AVANZA.
        //   'derecha'   comienza en el último elemento y RETROCEDE.
        // La condición 'izquierda < derecha' garantiza que cada par de extremos
        // se intercambia EXACTAMENTE una vez y que nunca se toca dos veces el
        // mismo par.
        //
        // INVARIANTE: antes de cada iteración, las posiciones fuera del rango
        // [izquierda, derecha] ya están en su posición final invertida.
        // --------------------------------------------------------------------
        for (int izquierda = 0, derecha = vector.length - 1; izquierda < derecha;
                izquierda++, derecha--) {

            // Intercambio clásico con una variable temporal. El valor auxiliar
            // es de tipo int (no hay objetos), por lo que la memoria extra es
            // constante y no depende de n: espacio auxiliar O(1).
            int temporal = vector[izquierda];
            vector[izquierda] = vector[derecha];
            vector[derecha] = temporal;

            // Al terminar el cuerpo, los extremos del rango actual ya quedaron
            // invertidos; el 'for' mueve ambos índices hacia el centro.
        }

        // Al salir del bucle se cumple 'izquierda >= derecha': no quedan pares
        // por intercambiar y el vector está completamente invertido.
    }

    // ------------------------------------------------------------------------
    // VALIDACIÓN DE LA ENTRADA
    // ------------------------------------------------------------------------

    /**
     * Valida que el vector de entrada no sea {@code null}.
     *
     * <p>Se rechaza el vector {@code null} con una excepción descriptiva en
     * lugar de lanzar un {@link NullPointerException} accidental al acceder a
     * {@code .length}. El vector vacío, en cambio, es válido: representa la
     * inversión de "nada" y no es un error.</p>
     *
     * @param vector vector a validar
     * @throws IllegalArgumentException si {@code vector} es {@code null}
     */
    private static void validar(int[] vector) {
        if (vector == null) {
            throw new IllegalArgumentException(
                    "El vector a invertir no puede ser null.");
        }
    }

    // ------------------------------------------------------------------------
    // CASOS DE PRUEBA
    // ------------------------------------------------------------------------

    /**
     * Método de entrada: ejecuta ambas versiones sobre los mismos casos y
     * verifica que produzcan el orden invertido esperado, que coincidan entre sí
     * y que la versión auxiliar no modifique la entrada original.
     *
     * @param args no se utilizan
     */
    public static void main(String[] args) {
        System.out.println("== Inversión de un vector de enteros ==");
        System.out.println();

        // Caso principal solicitado: varios elementos.
        probar("Varios elementos", new int[] {1, 2, 3, 4, 5}, new int[] {5, 4, 3, 2, 1});

        // Caso solicitado: vector VACÍO -> queda vacío (válido, no es error).
        probar("Vector vacío", new int[] {}, new int[] {});

        // Caso solicitado: UN solo elemento -> queda igual (ya está invertido).
        probar("Un elemento", new int[] {7}, new int[] {7});

        // Casos adicionales: longitud par (sin elemento central) y valores
        // negativos/extremos, para comprobar que la comparación por valor y los
        // índices funcionan en todo el rango de int.
        probar("Longitud par", new int[] {1, 2, 3, 4}, new int[] {4, 3, 2, 1});
        probar("Dos elementos", new int[] {10, 20}, new int[] {20, 10});
        probar("Negativos y extremos",
                new int[] {Integer.MIN_VALUE, -5, 0, Integer.MAX_VALUE},
                new int[] {Integer.MAX_VALUE, 0, -5, Integer.MIN_VALUE});

        System.out.println();
        System.out.println("== Caso límite: vector nulo (debe rechazarse) ==");
        probarVectorNulo();
    }

    /**
     * Ejecuta un caso sobre las dos versiones, contrasta cada resultado con el
     * esperado y comprueba tres invariantes: (1) la versión auxiliar deja
     * intacta la entrada, (2) la versión in-place produce el resultado esperado
     * y (3) ambas coinciden en el orden invertido.
     *
     * @param nombre   descripción del caso para la salida
     * @param original vector de entrada
     * @param esperado vector con el orden inverso esperado
     */
    private static void probar(String nombre, int[] original, int[] esperado) {
        // Se guarda una copia para detectar si la versión auxiliar modifica el
        // original (no debería).
        int[] copia = original.clone();

        int[] conAuxiliar = invertirConVectorAuxiliar(original);
        boolean auxiliarNoModifica = sonIguales(original, copia);
        boolean auxiliarCorrecto = sonIguales(conAuxiliar, esperado);

        int[] inPlace = original.clone();
        invertirInPlace(inPlace);
        boolean inPlaceCorrecto = sonIguales(inPlace, esperado);

        boolean coinciden = sonIguales(conAuxiliar, inPlace);
        boolean correcto = auxiliarNoModifica && auxiliarCorrecto && inPlaceCorrecto && coinciden;

        System.out.printf("%-24s %-26s -> aux = %-26s | in-place = %-26s | esperado = %-26s | %s%n",
                nombre, formatear(original), formatear(conAuxiliar), formatear(inPlace),
                formatear(esperado), correcto ? "OK" : "FALLO");

        if (!correcto) {
            System.out.printf("    detalle: aux no modifica = %b | aux correcto = %b | in-place correcto = %b | coinciden = %b%n",
                    auxiliarNoModifica, auxiliarCorrecto, inPlaceCorrecto, coinciden);
        }
    }

    /**
     * Verifica que ambas versiones rechacen un vector {@code null} con la
     * excepción documentada.
     */
    private static void probarVectorNulo() {
        probarNuloAuxiliar();
        probarNuloInPlace();
    }

    /** Comprueba el rechazo de {@code null} en la versión con vector auxiliar. */
    private static void probarNuloAuxiliar() {
        try {
            invertirConVectorAuxiliar(null);
            System.out.println("invertirConVectorAuxiliar(null) -> FALLO (no se lanzó excepción)");
        } catch (IllegalArgumentException excepcion) {
            System.out.printf("invertirConVectorAuxiliar(null) -> OK (rechazado: %s)%n",
                    excepcion.getMessage());
        }
    }

    /** Comprueba el rechazo de {@code null} en la versión in-place. */
    private static void probarNuloInPlace() {
        try {
            invertirInPlace(null);
            System.out.println("invertirInPlace(null)     -> FALLO (no se lanzó excepción)");
        } catch (IllegalArgumentException excepcion) {
            System.out.printf("invertirInPlace(null)     -> OK (rechazado: %s)%n",
                    excepcion.getMessage());
        }
    }

    /**
     * Compara dos vectores de enteros por longitud y por elementos.
     *
     * @param a primer vector
     * @param b segundo vector
     * @return {@code true} si tienen la misma longitud y los mismos elementos
     *         posición por posición
     */
    private static boolean sonIguales(int[] a, int[] b) {
        if (a.length != b.length) {
            return false;
        }
        for (int i = 0; i < a.length; i++) {
            if (a[i] != b[i]) {
                return false;
            }
        }
        return true;
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
 * NOTAS DE IMPLEMENTACIÓN (aclaraciones al prompt inicial)
 * ============================================================================
 * 1. DOS COMPROMISOS, UN MISMO RESULTADO: con vector auxiliar se gana pureza
 *    (no hay efecto secundario) al costo de O(n) de memoria extra; in-place se
 *    gana memoria O(1) al costo de destruir el orden original. No hay una
 *    versión "mejor" en absoluto: depende de si el llamador necesita conservar
 *    la entrada.
 *
 * 2. COMPLEJIDAD TEMPORAL O(n) EN AMBAS: la auxiliar hace n lecturas y n
 *    escrituras (2n operaciones, aún lineales); la in-place hace floor(n/2)
 *    intercambios, cada uno de 3 asignaciones (3·floor(n/2), también lineales).
 *    Constantes distintas, misma clase de complejidad.
 *
 * 3. ESPACIO: auxiliar O(n) (el vector resultado); in-place O(1) (solo dos
 *    índices y una variable temporal de tipo int).
 *
 * 4. VECTORES VACÍO Y DE UN ELEMENTO: ambos quedan igual. En la auxiliar se
 *    devuelve una copia de la misma longitud; en la in-place la condición
 *    'izquierda < derecha' es falsa desde el inicio y no hay intercambios.
 *
 * 5. VECTOR null (DECISIÓN DOCUMENTADA): se rechaza con IllegalArgumentException
 *    descriptiva en ambas versiones, en vez de NPE accidental. Invertir una
 *    referencia inexistente no tiene semántica razonable.
 *
 * 6. ELEMENTO CENTRAL (n IMPAR): no se toca. En un vector de longitud impar el
 *    índice medio coincide consigo mismo ('izquierda == derecha') y el bucle
 *    termina justo antes de procesarlo, que es lo correcto: su posición no
 *    cambia al invertir.
 *
 * 7. VALORES LÍMITE: se intercambian y comparan valores int mediante '=='/copia
 *    directa, sin operaciones aritméticas sobre los elementos, por lo que no hay
 *    desbordamiento ni problemas con Integer.MIN_VALUE / Integer.MAX_VALUE.
 * ============================================================================
 */

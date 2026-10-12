/*
 * ============================================================================
 * PROMPT INICIAL UTILIZADO PARA GENERAR ESTA SOLUCIÓN
 * ============================================================================
 *
 * Quiero implementar en Java un algoritmo que busque un número entero dentro de
 * un vector desordenado.
 *
 * La estrategia adecuada es la búsqueda lineal: recorrer el vector desde la
 * primera posición comparando cada elemento con el valor buscado. Esta
 * estrategia es la correcta porque el vector no está ordenado y, por lo tanto,
 * no se puede descartar ninguna parte sin examinarla. No se debe ordenar el
 * vector antes de buscar: ordenar agrega trabajo innecesario para una consulta
 * y no mejora la búsqueda lineal.
 *
 * Complejidad temporal:
 *   - Mejor caso: el elemento está en la primera posición; se recorre una
 *     posición, O(1).
 *   - Peor caso: el elemento está en la última posición o no existe; se
 *     recorren hasta n posiciones, O(n).
 *   - Caso promedio: si el elemento existe y puede estar con igual
 *     probabilidad en cualquier posición, se recorren en promedio (n + 1) / 2
 *     posiciones, por lo que la complejidad es O(n). Si no existe, se recorren
 *     las n posiciones.
 *
 * El programa debe informar si se encontró el elemento, su índice cuando
 * corresponda y cuántas posiciones se recorrieron. Si el elemento no existe,
 * debe indicar que no fue encontrado y mostrar que se recorrieron todas las
 * posiciones. También debe definirse el comportamiento para un vector vacío.
 *
 * Probar con: un elemento en la primera posición, en una posición intermedia,
 * en la última posición, un elemento inexistente y un vector vacío.
 * ============================================================================
 */

/**
 * Búsqueda lineal (secuencial) de un número entero dentro de un vector
 * desordenado de enteros.
 *
 * <p>El algoritmo recorre el vector desde la primera posición y compara cada
 * elemento con el valor buscado. Se detiene en cuanto encuentra una coincidencia
 * (devolviendo el índice de la <b>primera</b> aparición) o cuando agota el
 * vector sin encontrarlo. Además del índice, se informa cuántas posiciones se
 * examinaron, dato que permite contrastar los distintos casos de complejidad.</p>
 *
 * <p>No se ordena el vector: como la entrada es desordenada, no es posible
 * descartar ninguna parte sin examinarla, y ordenar implicaría un costo
 * adicional ({@code O(n log n)}) que no mejora el costo de la consulta. La
 * búsqueda lineal es la estrategia natural para vectores no ordenados.</p>
 *
 * <p>Complejidad temporal: {@code O(1)} en el mejor caso (primera posición),
 * {@code O(n)} en el peor caso y en el caso promedio. Complejidad espacial
 * auxiliar: {@code O(1)}.</p>
 *
 * @author Agente Desarrollador
 */
public final class BusquedaLineal {

    /** Impide instanciar una clase que solo agrupa métodos estáticos. */
    private BusquedaLineal() {
    }

    // ------------------------------------------------------------------------
    // API PÚBLICA
    // ------------------------------------------------------------------------

    /**
     * Busca linealmente el valor {@code valorBuscado} dentro del vector
     * {@code vector}.
     *
     * <p>Recorre el vector desde el índice {@code 0}. En cada paso incrementa el
     * contador de posiciones examinadas y, si el elemento coincide con el valor
     * buscado, devuelve de inmediato un resultado con el índice de la primera
     * coincidencia. Si el recorrido termina sin coincidencias (incluido el caso
     * de un vector vacío), devuelve un resultado "no encontrado" con índice
     * {@code -1} y la cantidad total de posiciones examinadas.</p>
     *
     * @param vector       vector de enteros en el que se busca; no puede ser
     *                     {@code null}
     * @param valorBuscado valor entero que se desea localizar
     * @return un {@link ResultadoBusqueda} con la bandera de encontrado, el
     *         índice de la primera coincidencia ({@code -1} si no existe) y la
     *         cantidad de posiciones recorridas
     * @throws IllegalArgumentException si {@code vector} es {@code null}
     */
    public static ResultadoBusqueda buscar(int[] vector, int valorBuscado) {
        // --------------------------------------------------------------------
        // VALIDACIÓN DE CASOS LÍMITE
        // Una referencia nula no puede recorrerse ni consultarse; se rechaza con
        // un mensaje descriptivo. (Un vector VACÍO no es un error: simplemente
        // no contiene el elemento, comportamiento que se define más abajo.)
        // --------------------------------------------------------------------
        if (vector == null) {
            throw new IllegalArgumentException("El vector no puede ser null.");
        }

        // --------------------------------------------------------------------
        // CONTEO DE POSICIONES
        // 'posicionesRecorridas' cuenta cuántos elementos se EXAMINAN. Se
        // incrementa una vez por iteración, es decir, por cada posición que se
        // compara contra el valor buscado. Este contador es la evidencia
        // empírica de la complejidad: 1 en el mejor caso, n en el peor.
        // --------------------------------------------------------------------
        int posicionesRecorridas = 0;

        // --------------------------------------------------------------------
        // BÚSQUEDA LINEAL
        // Se recorre desde la primera posición. Cada iteración:
        //   1. incrementa el contador (se examina una posición más);
        //   2. compara el elemento actual con el valor buscado;
        //   3. si coincide, se retorna INMEDIATAMENTE el índice (primera
        //      coincidencia), sin seguir recorriendo el resto del vector.
        //
        // COMPORTAMIENTO CON VECTOR VACÍO (length == 0): la condición del for
        // (0 < 0) es falsa desde el principio, por lo que el cuerpo nunca se
        // ejecuta. Se sale del bucle con 0 posiciones recorridas y se devuelve
        // "no encontrado": no hay acceso a ninguna posición inexistente.
        // --------------------------------------------------------------------
        for (int i = 0; i < vector.length; i++) {
            posicionesRecorridas++;

            if (vector[i] == valorBuscado) {
                // Elemento hallado en la posición i (primera aparición).
                return new ResultadoBusqueda(true, i, posicionesRecorridas);
            }
        }

        // --------------------------------------------------------------------
        // NO ENCONTRADO
        // Se llegó al final del vector sin coincidencias. Se recorrieron TODAS
        // las posiciones (n). Si el vector estaba vacío, el contador queda en 0.
        // --------------------------------------------------------------------
        return new ResultadoBusqueda(false, -1, posicionesRecorridas);
    }

    // ------------------------------------------------------------------------
    // TIPO DE RESULTADO
    // ------------------------------------------------------------------------

    /**
     * Resultado de una búsqueda lineal.
     *
     * <p>Agrupa la información que el programa debe informar: si se encontró el
     * elemento, en qué índice estaba (cuando corresponde) y cuántas posiciones
     * se recorrieron durante la búsqueda.</p>
     */
    public static final class ResultadoBusqueda {

        private final boolean encontrado;
        private final int indice;
        private final int posicionesRecorridas;

        /**
         * Crea un resultado de búsqueda.
         *
         * @param encontrado          {@code true} si el valor fue localizado
         * @param indice              índice de la primera coincidencia, o
         *                            {@code -1} si no se encontró
         * @param posicionesRecorridas cantidad de posiciones examinadas
         */
        public ResultadoBusqueda(boolean encontrado, int indice, int posicionesRecorridas) {
            this.encontrado = encontrado;
            this.indice = indice;
            this.posicionesRecorridas = posicionesRecorridas;
        }

        /** @return {@code true} si el valor fue encontrado en el vector */
        public boolean isEncontrado() {
            return encontrado;
        }

        /** @return el índice de la primera coincidencia, o {@code -1} si no se encontró */
        public int getIndice() {
            return indice;
        }

        /** @return la cantidad de posiciones examinadas durante la búsqueda */
        public int getPosicionesRecorridas() {
            return posicionesRecorridas;
        }

        @Override
        public String toString() {
            if (encontrado) {
                return String.format(
                        "encontrado en el indice %d (posiciones recorridas: %d)",
                        indice, posicionesRecorridas);
            }
            return String.format(
                    "no encontrado (posiciones recorridas: %d)", posicionesRecorridas);
        }
    }

    // ------------------------------------------------------------------------
    // CASOS DE PRUEBA
    // ------------------------------------------------------------------------

    /**
     * Ejecuta los casos de prueba solicitados en el prompt.
     *
     * @param args no se utilizan
     */
    public static void main(String[] args) {
        System.out.println("== Casos de prueba solicitados ==");

        // Mejor caso: el elemento está en la PRIMERA posición -> 1 posición, O(1).
        probar(new int[] {7, 3, 9, 1, 5}, 7, true, 0, 1);

        // Caso intermedio: el elemento está en una posición del medio.
        probar(new int[] {10, 20, 30, 40, 50}, 30, true, 2, 3);

        // Peor caso (encontrado): el elemento está en la ÚLTIMA posición -> n posiciones.
        probar(new int[] {5, 6, 7, 8}, 8, true, 3, 4);

        // Peor caso (ausente): no existe -> se recorren TODAS las posiciones.
        probar(new int[] {1, 2, 3}, 99, false, -1, 3);

        // Vector vacío: no encontrado y 0 posiciones recorridas.
        probar(new int[] {}, 5, false, -1, 0);

        System.out.println();
        System.out.println("== Casos de borde adicionales ==");

        // Un solo elemento, presente.
        probar(new int[] {42}, 42, true, 0, 1);

        // Un solo elemento, ausente.
        probar(new int[] {42}, 99, false, -1, 1);

        // Valores negativos.
        probar(new int[] {-1, -2, -3}, -2, true, 1, 2);

        // Valor repetido: debe devolver el índice de la PRIMERA coincidencia.
        probar(new int[] {7, 1, 7, 3, 7}, 7, true, 0, 1);

        // Valor repetido que aparece por primera vez en el medio.
        probar(new int[] {4, 5, 5, 5}, 5, true, 1, 2);

        // Límite de tipo int: el valor mínimo está en la primera posición.
        probar(new int[] {Integer.MIN_VALUE, 0, Integer.MAX_VALUE}, Integer.MIN_VALUE, true, 0, 1);

        // Validación de entrada nula: debe rechazarse con una excepción descriptiva.
        probarVectorNulo();
    }

    /**
     * Ejecuta un caso de prueba de búsqueda con valor presente o ausente y
     * compara el resultado obtenido con el esperado.
     *
     * @param vector               entrada de la función
     * @param valorBuscado         valor a buscar
     * @param encontradoEsperado   bandera de encontrado esperada
     * @param indiceEsperado       índice esperado ({@code -1} si no se encuentra)
     * @param posicionesEsperadas  cantidad esperada de posiciones recorridas
     */
    private static void probar(int[] vector, int valorBuscado,
                               boolean encontradoEsperado, int indiceEsperado,
                               int posicionesEsperadas) {
        ResultadoBusqueda resultado = buscar(vector, valorBuscado);

        boolean correcto = resultado.isEncontrado() == encontradoEsperado
                && resultado.getIndice() == indiceEsperado
                && resultado.getPosicionesRecorridas() == posicionesEsperadas;

        System.out.printf("buscar(%-24s, %-11d) -> %-58s | %s%n",
                formatear(vector), valorBuscado, resultado.toString(),
                correcto ? "OK" : "FALLO");
    }

    /**
     * Verifica que un vector {@code null} sea rechazado con la excepción
     * documentada.
     */
    private static void probarVectorNulo() {
        try {
            buscar(null, 10);
            System.out.println("buscar(null, 10) -> FALLO (no se lanzó ninguna excepción)");
        } catch (IllegalArgumentException excepcion) {
            System.out.printf("buscar(null, 10) -> OK (rechazado: %s)%n", excepcion.getMessage());
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
 * NOTAS DE IMPLEMENTACIÓN (aclaraciones al prompt inicial)
 * ============================================================================
 * 1. Estrategia: búsqueda LINEAL sobre un vector desordenado. Se recorre desde
 *    la primera posición comparando cada elemento con el valor buscado. Al no
 *    haber orden, no se puede descartar ninguna parte sin examinarla; por eso
 *    NO se ordena el vector (ordenar costaría O(n log n) y no mejora la
 *    consulta).
 *
 * 2. Conteo de posiciones: el contador 'posicionesRecorridas' se incrementa
 *    exactamente una vez por cada elemento examinado. Esto permite verificar la
 *    complejidad:
 *      - mejor caso (primera posición):          1 posición     -> O(1)
 *      - caso promedio (posición uniforme):      (n + 1) / 2    -> O(n)
 *      - peor caso (última posición o ausente):  n posiciones   -> O(n)
 *
 * 3. Retorno inmediato: al hallar la coincidencia se retorna sin seguir
 *    recorriendo el resto del vector, garantizando el índice de la PRIMERA
 *    aparición y evitando trabajo innecesario (clave para el mejor caso O(1)).
 *
 * 4. Vector vacío: comportamiento definido como "no encontrado" con 0
 *    posiciones recorridas. Como el bucle no se ejecuta, nunca se accede a una
 *    posición inexistente (no hay ArrayIndexOutOfBoundsException).
 *
 * 5. Vector nulo: se rechaza con IllegalArgumentException porque no es una
 *    entrada procesable. A diferencia del vector vacío (que es una entrada
 *    válida sin el elemento), un null indica un error de programación.
 *
 * 6. Complejidad espacial auxiliar: O(1). Solo se emplean unas pocas variables
 *    (el índice 'i', el contador y el objeto resultado), independientemente del
 *    tamaño del vector.
 * ============================================================================
 */

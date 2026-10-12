/*
 * ============================================================================
 * PROMPT INICIAL UTILIZADO PARA GENERAR ESTA SOLUCIÓN
 * ============================================================================
 *
 * Quiero implementar en Java un algoritmo que calcule la suma de todos los
 * elementos de una matriz de números enteros.
 *
 * Recorré la matriz fila por fila: para cada fila, visitá cada columna y
 * acumulá su valor en una variable `suma`. El programa debe informar la suma y
 * contabilizar tanto las celdas visitadas como las operaciones de suma
 * realizadas. Incrementá los contadores de forma explícita durante el recorrido
 * y explicá qué mide cada uno.
 *
 * Analizá la complejidad temporal como O(filas × columnas), porque se visita
 * cada celda una vez, y la complejidad espacial auxiliar como O(1), porque solo
 * se necesitan acumuladores y contadores.
 *
 * Definí el comportamiento para una matriz vacía o sin elementos: la suma y
 * ambos contadores deben ser 0. Rechazá una matriz `null` con un error
 * descriptivo. Considerá una matriz rectangular con filas válidas.
 *
 * Comentá el código para explicar el recorrido, la acumulación y el conteo de
 * operaciones. Incluí pruebas para una matriz con varios valores, una matriz
 * con valores negativos, una matriz de una sola celda y una matriz vacía.
 * Verificá tanto la suma como los contadores esperados en cada caso.
 * ============================================================================
 */

/**
 * Suma de todos los elementos de una matriz de enteros mediante un recorrido
 * fila por fila, informando además métricas del trabajo realizado.
 *
 * <p>El recorrido visitá cada celda {@code [i][j]} exactamente una vez. Por cada
 * celda se acumula su valor en {@code suma} y se incrementan, de forma
 * explícita, dos contadores:</p>
 * <ul>
 *   <li>{@code celdasVisitadas}: cuántas posiciones de la matriz se leyeron.</li>
 *   <li>{@code operacionesSuma}: cuántas adiciones se acumularon en la suma.</li>
 * </ul>
 *
 * <p><b>Complejidad temporal:</b> {@code O(filas × columnas)}. Cada celda se
 * procesa una sola vez con trabajo constante, de modo que el costo es
 * proporcional a la cantidad total de elementos. <b>Complejidad espacial
 * auxiliar:</b> {@code O(1)}: solo se usan la suma y los contadores (más los
 * índices del bucle), con independencia del tamaño de la matriz.</p>
 *
 * @author Agente Desarrollador
 */
public final class SumaMatriz {

    /** Impide instanciar una clase que solo agrupa métodos estáticos. */
    private SumaMatriz() {
    }

    // ------------------------------------------------------------------------
    // RESULTADO DEL CÁLCULO
    // ------------------------------------------------------------------------

    /**
     * Valor inmutable con el resultado del recorrido: la suma total y las dos
     * métricas de conteo.
     *
     * <p>Agrupar los tres valores en un único objeto evita firmas confusas
     * (como arrays "mágicos" o parámetros de salida) y hace explícito qué
     * información se devuelve.</p>
     */
    public static final class ResultadoSuma {

        /** Suma de todos los elementos de la matriz. */
        private final int suma;

        /** Cantidad de celdas [i][j] leídas durante el recorrido. */
        private final int celdasVisitadas;

        /** Cantidad de adiciones acumuladas en la variable suma. */
        private final int operacionesSuma;

        /**
         * Construye el resultado del recorrido.
         *
         * @param suma            suma total de los elementos
         * @param celdasVisitadas cantidad de celdas leídas
         * @param operacionesSuma cantidad de adiciones realizadas
         */
        public ResultadoSuma(int suma, int celdasVisitadas, int operacionesSuma) {
            this.suma = suma;
            this.celdasVisitadas = celdasVisitadas;
            this.operacionesSuma = operacionesSuma;
        }

        /**
         * @return la suma total de los elementos de la matriz
         */
        public int getSuma() {
            return suma;
        }

        /**
         * @return la cantidad de celdas visitadas durante el recorrido
         */
        public int getCeldasVisitadas() {
            return celdasVisitadas;
        }

        /**
         * @return la cantidad de operaciones de suma realizadas
         */
        public int getOperacionesSuma() {
            return operacionesSuma;
        }

        @Override
        public String toString() {
            return "ResultadoSuma{suma=" + suma + ", celdasVisitadas="
                    + celdasVisitadas + ", operacionesSuma=" + operacionesSuma + '}';
        }
    }

    // ------------------------------------------------------------------------
    // ALGORITMO PRINCIPAL
    // ------------------------------------------------------------------------

    /**
     * Calcula la suma de todos los elementos de {@code matriz} y cuenta cuántas
     * celdas se visitaron y cuántas adiciones se realizaron.
     *
     * <p><b>Estrategia:</b> recorrido fila por fila. El bucle externo recorre las
     * filas ({@code i}) y el interno, las columnas ({@code j}). En cada celda se
     * ejecuta una única adición sobre el acumulador. Este orden es indiferente
     * para el resultado (la suma es conmutativa), pero "fila por fila" es la
     * forma natural de recorrer una matriz almacenada como arreglo de
     * arreglos.</p>
     *
     * <p><b>Comportamiento para una matriz vacía o sin elementos:</b> si la
     * matriz tiene 0 filas, o bien cada fila tiene 0 columnas, no hay nada que
     * sumar y se devuelve {@code suma = 0}, {@code celdasVisitadas = 0},
     * {@code operacionesSuma = 0}. No es un error.</p>
     *
     * @param matriz matriz rectangular de enteros; no puede ser {@code null} ni
     *               contener filas {@code null}; todas las filas deben tener la
     *               misma longitud
     * @return un {@link ResultadoSuma} con la suma total y los dos contadores
     * @throws IllegalArgumentException si {@code matriz} es {@code null}, si
     *         alguna fila es {@code null} o si la matriz no es rectangular
     */
    public static ResultadoSuma sumar(int[][] matriz) {
        // --------------------------------------------------------------------
        // VALIDACIÓN DE CASOS LÍMITE
        // 1) Una matriz null no tiene celdas sobre las que iterar.
        // 2) Las columnas son la longitud de la primera fila; si esa fila fuera
        //    null, no podríamos determinar siquiera el ancho de la matriz.
        // 3) Se exige una matriz RECTANGULAR (todas las filas del mismo largo):
        //    con filas de distinto tamaño el "ancho" no estaría bien definido y
        //    el análisis O(filas × columnas) dejaría de ser exacto.
        // --------------------------------------------------------------------
        if (matriz == null) {
            throw new IllegalArgumentException("La matriz no puede ser null.");
        }

        // Matriz sin filas -> no hay elementos: resultado neutro con contadores
        // en cero (comportamiento definido, NO es error).
        if (matriz.length == 0) {
            return new ResultadoSuma(0, 0, 0);
        }

        // Validación de filas y rectangularidad.
        for (int i = 0; i < matriz.length; i++) {
            if (matriz[i] == null) {
                throw new IllegalArgumentException(
                        "La fila " + i + " de la matriz es null.");
            }
            if (matriz[i].length != matriz[0].length) {
                throw new IllegalArgumentException(
                        "La matriz no es rectangular: la fila " + i + " tiene "
                                + matriz[i].length + " columnas, pero la fila 0 tiene "
                                + matriz[0].length + ".");
            }
        }

        // Variable local 'columnas': todas las filas comparten la misma longitud
        // tras validar la rectangularidad.
        int filas = matriz.length;
        int columnas = matriz[0].length;

        // Matriz con filas pero sin columnas (p. ej. new int[3][0]): tampoco hay
        // elementos que sumar. Los bucles no entrarían en el cuerpo, pero se
        // devuelve explícitamente para dejar claro el comportamiento en cero.
        if (columnas == 0) {
            return new ResultadoSuma(0, 0, 0);
        }

        // --------------------------------------------------------------------
        // ACUMULADORES Y CONTADORES (única memoria auxiliar, además de índices)
        //   suma             : acumula los valores; arranca en 0 (elemento neutro
        //                      de la suma), lo que garantiza suma = 0 sin celdas.
        //   celdasVisitadas  : cuenta CADA celda leída, es decir, cuántas
        //                      posiciones [i][j] se examinaron.
        //   operacionesSuma  : cuenta CADA ejecución de 'suma += valor', es
        //                      decir, cuántas adiciones se acumularon.
        //
        // Ambos contadores miden cosas distintas: 'celdasVisitadas' mide la
        // cobertura del recorrido y 'operacionesSuma' mide el trabajo aritmético.
        // En ESTE algoritmo coinciden numéricamente porque se acumula cada celda
        // visitada, pero se mantienen separados para reflejar que son métricas
        // conceptualmente diferentes y para poder instrumentarlas por separado.
        // --------------------------------------------------------------------
        int suma = 0;
        int celdasVisitadas = 0;
        int operacionesSuma = 0;

        // --------------------------------------------------------------------
        // RECORRIDO FILA POR FILA
        //
        // Bucle externo: avanza por las filas 'i' de 0 a filas-1.
        // Bucle interno: avanza por las columnas 'j' de 0 a columnas-1.
        //
        // INVARIANTE: al comenzar la fila 'i', 'suma' contiene la suma de todas
        // las celdas de las filas [0..i-1] y los contadores reflejan las celdas
        // procesadas en esas filas. Al terminar (i == filas), 'suma' es la suma
        // total y los contadores valen filas × columnas.
        //
        // COSTO: el bucle interno da exactamente 'columnas' iteraciones por cada
        // una de las 'filas' exteriores -> filas × columnas celdas, cada una con
        // trabajo constante -> tiempo O(filas × columnas). La memoria auxiliar
        // (suma y contadores) es constante -> espacio O(1).
        // --------------------------------------------------------------------
        for (int i = 0; i < filas; i++) {
            for (int j = 0; j < columnas; j++) {
                // (1) Se ingresa a la celda [i][j]: se cuenta la visita ANTES de
                //     usarla, de modo que la métrica refleje cada acceso.
                celdasVisitadas++;

                // (2) Acumulación explícita del valor de la celda en la suma y
                //     conteo de la operación de suma realizada.
                suma += matriz[i][j];
                operacionesSuma++;
            }
        }

        return new ResultadoSuma(suma, celdasVisitadas, operacionesSuma);
    }

    // ------------------------------------------------------------------------
    // CASOS DE PRUEBA
    // ------------------------------------------------------------------------

    /**
     * Método de entrada: ejecuta los casos solicitados y verifica tanto la suma
     * como ambos contadores esperados.
     *
     * @param args no se utilizan
     */
    public static void main(String[] args) {
        System.out.println("== Suma de los elementos de una matriz ==");
        System.out.println();

        // Caso 1: matriz con varios valores (2x3). Suma = 1+2+3+4+5+6 = 21.
        // Celdas = 6, operaciones = 6.
        probar("Varios valores (2x3)",
                new int[][] {{1, 2, 3}, {4, 5, 6}},
                21, 6, 6);

        // Caso 2: valores negativos (2x2). Suma = -1-2-3-4 = -10.
        // Celdas = 4, operaciones = 4.
        probar("Valores negativos (2x2)",
                new int[][] {{-1, -2}, {-3, -4}},
                -10, 4, 4);

        // Caso 3: una sola celda (1x1). Suma = 42.
        // Celdas = 1, operaciones = 1.
        probar("Una sola celda (1x1)",
                new int[][] {{42}},
                42, 1, 1);

        // Caso 4: matriz vacía, sin filas. Suma y contadores = 0.
        probar("Matriz vacía (0 filas)",
                new int[][] {},
                0, 0, 0);

        // Caso 5: matriz con filas pero sin columnas (p. ej. new int[3][0]).
        // No hay elementos: suma y contadores = 0.
        probar("Matriz sin columnas (3x0)",
                new int[][] {{}, {}, {}},
                0, 0, 0);

        // Caso 6 (adicional): mezcla de positivos, negativos y cero (2x3).
        // Suma = 10-5+0-3+8-7 = 3. Celdas = 6, operaciones = 6.
        probar("Positivos, negativos y cero (2x3)",
                new int[][] {{10, -5, 0}, {-3, 8, -7}},
                3, 6, 6);

        // Caso 7 (adicional): una sola fila con varias columnas (1x4).
        // Suma = 2+4+6+8 = 20. Celdas = 4, operaciones = 4.
        probar("Una fila (1x4)",
                new int[][] {{2, 4, 6, 8}},
                20, 4, 4);

        System.out.println();
        System.out.println("== Casos límite: entradas inválidas (deben rechazarse) ==");

        // Matriz null -> IllegalArgumentException descriptiva.
        probarMatrizNula();

        // Fila null -> IllegalArgumentException descriptiva.
        probarFilaNula();

        // Matriz no rectangular -> IllegalArgumentException descriptiva.
        probarNoRectangular();
    }

    /**
     * Ejecuta un caso de prueba, contrasta la suma y AMBOS contadores con lo
     * esperado e informa el resultado.
     *
     * @param nombre        descripción del caso para la salida
     * @param matriz        matriz de entrada
     * @param sumaEsperada  suma esperada
     * @param celdasEsperadas  celdas visitadas esperadas
     * @param operacionesEsperadas operaciones de suma esperadas
     */
    private static void probar(String nombre, int[][] matriz,
            int sumaEsperada, int celdasEsperadas, int operacionesEsperadas) {
        ResultadoSuma resultado = sumar(matriz);

        boolean sumaOk = resultado.getSuma() == sumaEsperada;
        boolean celdasOk = resultado.getCeldasVisitadas() == celdasEsperadas;
        boolean operacionesOk = resultado.getOperacionesSuma() == operacionesEsperadas;
        boolean correcto = sumaOk && celdasOk && operacionesOk;

        System.out.printf("%-38s %-24s -> %s | esperado {suma=%d, celdas=%d, ops=%d} | %s%n",
                nombre, formatear(matriz),
                resultado,
                sumaEsperada, celdasEsperadas, operacionesEsperadas,
                correcto ? "OK" : "FALLO");

        if (!correcto) {
            System.out.printf("    detalle: sumaOk=%b | celdasOk=%b | operacionesOk=%b%n",
                    sumaOk, celdasOk, operacionesOk);
        }
    }

    /** Verifica que una matriz {@code null} sea rechazada. */
    private static void probarMatrizNula() {
        try {
            sumar(null);
            System.out.println("sumar(null) -> FALLO (no se lanzó ninguna excepción)");
        } catch (IllegalArgumentException excepcion) {
            System.out.printf("sumar(null) -> OK (rechazado: %s)%n", excepcion.getMessage());
        }
    }

    /** Verifica que una fila {@code null} sea rechazada. */
    private static void probarFilaNula() {
        try {
            sumar(new int[][] {{1, 2}, null});
            System.out.println("sumar({{1,2}, null}) -> FALLO (no se lanzó ninguna excepción)");
        } catch (IllegalArgumentException excepcion) {
            System.out.printf("sumar({{1,2}, null}) -> OK (rechazado: %s)%n",
                    excepcion.getMessage());
        }
    }

    /** Verifica que una matriz no rectangular sea rechazada. */
    private static void probarNoRectangular() {
        try {
            sumar(new int[][] {{1, 2, 3}, {4, 5}});
            System.out.println("sumar(no rectangular) -> FALLO (no se lanzó ninguna excepción)");
        } catch (IllegalArgumentException excepcion) {
            System.out.printf("sumar(no rectangular) -> OK (rechazado: %s)%n",
                    excepcion.getMessage());
        }
    }

    /**
     * Construye una representación legible de la matriz para la salida de
     * pruebas.
     *
     * @param matriz matriz a representar
     * @return cadena con las filas entre corchetes
     */
    private static String formatear(int[][] matriz) {
        if (matriz == null) {
            return "null";
        }
        StringBuilder texto = new StringBuilder("[");
        for (int i = 0; i < matriz.length; i++) {
            if (i > 0) {
                texto.append(", ");
            }
            texto.append(formatearFila(matriz[i]));
        }
        return texto.append("]").toString();
    }

    /**
     * Representa una fila de la matriz.
     *
     * @param fila fila a representar (puede ser {@code null})
     * @return cadena con los elementos entre corchetes
     */
    private static String formatearFila(int[] fila) {
        if (fila == null) {
            return "null";
        }
        StringBuilder texto = new StringBuilder("[");
        for (int j = 0; j < fila.length; j++) {
            if (j > 0) {
                texto.append(", ");
            }
            texto.append(fila[j]);
        }
        return texto.append("]").toString();
    }
}

/*
 * ============================================================================
 * NOTAS DE IMPLEMENTACIÓN (aclaraciones al prompt inicial)
 * ============================================================================
 * 1. QUÉ MIDE CADA CONTADOR:
 *    - celdasVisitadas: cuántas posiciones [i][j] de la matriz se leyeron. Mide
 *      la cobertura del recorrido y debe valer filas × columnas.
 *    - operacionesSuma: cuántas veces se ejecutó 'suma += valor'. Mide el trabajo
 *      aritmético de acumulación.
 *    En este algoritmo ambos coinciden (se suma cada celda visitada), pero se
 *    mantienen separados porque expresan métricas distintas y podrían divergir
 *    en una variante que filtrara celdas sin sumarlas.
 *
 * 2. COMPLEJIDAD TEMPORAL O(filas × columnas): se visita cada celda una única
 *    vez con trabajo constante (dos incrementos y una adición). No se puede
 *    hacer mejor: para sumar TODOS los elementos hay que mirarlos todos, así que
 *    el costo mínimo es proporcional a la cantidad de elementos.
 *
 * 3. COMPLEJIDAD ESPACIAL AUXILIAR O(1): solo se emplean 'suma',
 *    'celdasVisitadas' y 'operacionesSuma' (y los índices 'i', 'j'), cantidades
 *    constantes que no dependen del tamaño de la matriz. La matriz de entrada no
 *    se modifica y no se reserva ninguna estructura proporcional a su tamaño.
 *
 * 4. MATRIZ VACÍA O SIN ELEMENTOS (DECISIÓN DOCUMENTADA): tanto una matriz sin
 *    filas (length == 0) como una con filas de 0 columnas (p. ej. new int[3][0])
 *    devuelven suma = 0 y ambos contadores = 0. Es la respuesta correcta: la
 *    suma de un conjunto vacío es el elemento neutro 0 y no hay nada que contar.
 *
 * 5. MATRIZ null (DECISIÓN DOCUMENTADA): se rechaza con IllegalArgumentException
 *    descriptiva, en lugar de provocar un NullPointerException accidental al
 *    consultar .length. Se sigue la convención defensiva del resto del
 *    repositorio.
 *
 * 6. MATRIZ RECTANGULAR: el enunciado asume una matriz rectangular (todas las
 *    filas del mismo largo). Para que el análisis O(filas × columnas) sea exacto
 *    y el ancho esté bien definido, se valida esa condición y se rechazan tanto
 *    las filas null como las matrices irregulares con mensajes descriptivos.
 *
 * 7. DESBORDAMIENTO (int): la suma se acumula en un 'int'. Con matrices cuyos
 *    valores sean muy grandes, la suma podría exceder el rango de int. Se
 *    mantiene 'int' por simplicidad y por coherencia con el tipo de los
 *    elementos; si se necesitara sumar matrices enormes, bastaría cambiar el
 *    acumulador y el campo 'suma' a 'long'.
 * ============================================================================
 */

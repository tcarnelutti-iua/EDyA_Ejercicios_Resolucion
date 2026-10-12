/*
 * ============================================================================
 * PROMPT INICIAL UTILIZADO PARA GENERAR ESTA SOLUCIÓN
 * ============================================================================
 *
 * Quiero implementar en Java un algoritmo que encuentre el mayor elemento de una
 * matriz rectangular de números enteros.
 *
 * Recorré la matriz fila por fila y mantené una variable con el mayor valor
 * encontrado hasta el momento. Es necesario examinar todas las celdas porque, al
 * no tener un orden que permita descartar posiciones, cualquier elemento aún no
 * visitado podría ser el máximo. Inicializá el máximo provisional con la primera
 * celda para que el algoritmo funcione correctamente también cuando todos los
 * valores sean negativos.
 *
 * Analizá la complejidad temporal como O(filas × columnas), ya que cada celda se
 * examina una vez, y la complejidad espacial auxiliar como O(1), porque solo se
 * necesita mantener el máximo provisional y los índices del recorrido.
 *
 * Implementá una función que devuelva el valor máximo. Definí el comportamiento
 * para una matriz `null`, vacía o sin elementos: como no existe un máximo en
 * esos casos, rechazalos con una excepción descriptiva. Comentá el código para
 * explicar la estrategia y por qué se deben revisar todas las celdas.
 *
 * Incluí pruebas para una matriz con valores positivos, una con valores
 * negativos, una con valores repetidos y los casos sin elementos.
 * ============================================================================
 */

/**
 * Búsqueda del mayor elemento de una matriz rectangular de enteros mediante un
 * recorrido fila por fila.
 *
 * <p><b>Estrategia:</b> se recorre toda la matriz una única vez manteniendo en
 * una variable {@code maximo} el mayor valor encontrado hasta el momento. El
 * máximo provisional se inicializa con la primera celda {@code matriz[0][0]},
 * de modo que el algoritmo es correcto incluso si <em>todos</em> los valores son
 * negativos (no se parte de un valor "neutro" como 0, que sería mayor que
 * cualquier negativo y daría un resultado erróneo).</p>
 *
 * <p><b>¿Por qué hay que revisar todas las celdas?</b> La matriz no está
 * ordenada y no ofrece ninguna propiedad que permita descartar posiciones: el
 * máximo podría estar en cualquier celda, incluida la última. Por lo tanto, toda
 * celda aún no visitada es un candidato válido y debe compararse. Esto fija un
 * piso de {@code O(filas × columnas)} comparaciones.</p>
 *
 * <p><b>Complejidad temporal:</b> {@code O(filas × columnas)}. Cada celda se
 * examina exactamente una vez con trabajo constante (una comparación y, a lo
 * sumo, una asignación). <b>Complejidad espacial auxiliar:</b> {@code O(1)}:
 * solo se emplean la variable {@code maximo} y los índices de los bucles, con
 * independencia del tamaño de la matriz.</p>
 *
 * @author Agente Desarrollador
 */
public final class MaximoMatriz {

    /** Impide instanciar una clase que solo agrupa métodos estáticos. */
    private MaximoMatriz() {
    }

    // ------------------------------------------------------------------------
    // ALGORITMO PRINCIPAL
    // ------------------------------------------------------------------------

    /**
     * Devuelve el mayor valor contenido en {@code matriz}.
     *
     * <p><b>Estrategia:</b> recorrido fila por fila. El bucle externo avanza por
     * las filas ({@code i}) y el interno por las columnas ({@code j}). En cada
     * celda se compara el valor con {@code maximo} y se actualiza si es mayor.
     * Al terminar, {@code maximo} es el mayor de todas las celdas.</p>
     *
     * <p><b>Comportamiento para entradas sin elementos:</b> si {@code matriz} es
     * {@code null}, si no tiene filas ({@code length == 0}) o si sus filas no
     * tienen columnas ({@code filas × 0}), no existe un máximo que devolver; en
     * todos esos casos se lanza {@link IllegalArgumentException} con un mensaje
     * descriptivo. (Devolver un valor centinela como {@code 0} o
     * {@code Integer.MIN_VALUE} sería incorrecto: no es un máximo real.)</p>
     *
     * @param matriz matriz rectangular de enteros; no puede ser {@code null}, ni
     *               tener 0 filas o 0 columnas, ni contener filas {@code null};
     *               todas las filas deben tener la misma longitud
     * @return el mayor valor contenido en {@code matriz}
     * @throws IllegalArgumentException si {@code matriz} es {@code null}, si no
     *         tiene elementos (sin filas o sin columnas), si alguna fila es
     *         {@code null} o si la matriz no es rectangular
     */
    public static int encontrarMaximo(int[][] matriz) {
        // --------------------------------------------------------------------
        // VALIDACIÓN DE CASOS LÍMITE
        // 1) Una matriz null no tiene celdas sobre las que iterar y no existe
        //    un máximo: se rechaza en lugar de provocar un NullPointerException
        //    accidental al consultar .length.
        // 2) Una matriz sin filas (length == 0) no contiene ningún elemento.
        // 3) Una matriz con filas pero sin columnas (p. ej. new int[3][0])
        //    tampoco contiene elementos.
        // En (2) y (3) no hay ningún candidato a máximo, por lo que se lanza
        // una excepción descriptiva en lugar de devolver un valor inventado.
        // --------------------------------------------------------------------
        if (matriz == null) {
            throw new IllegalArgumentException("La matriz no puede ser null.");
        }
        if (matriz.length == 0) {
            throw new IllegalArgumentException(
                    "La matriz está vacía (sin filas): no existe un valor máximo que devolver.");
        }

        // El ancho de la matriz está dado por la primera fila; si esa fila es
        // null no podemos determinar siquiera las columnas.
        if (matriz[0] == null) {
            throw new IllegalArgumentException(
                    "La fila 0 de la matriz es null.");
        }
        if (matriz[0].length == 0) {
            throw new IllegalArgumentException(
                    "La matriz no tiene columnas (sin elementos): "
                            + "no existe un valor máximo que devolver.");
        }

        // Validación de las filas restantes: no null y rectangularidad. Se
        // exige que todas las filas tengan el mismo largo para que el "ancho"
        // esté bien definido y el análisis O(filas × columnas) sea exacto.
        for (int i = 1; i < matriz.length; i++) {
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

        // --------------------------------------------------------------------
        // INICIALIZACIÓN DEL MÁXIMO PROVISIONAL
        // Se toma la PRIMERA celda como candidato inicial. Es seguro porque la
        // matriz ya se validó como no vacía. Arrancar con matriz[0][0] (en
        // lugar de, por ejemplo, Integer.MIN_VALUE o 0) hace que el resultado
        // sea correcto aunque TODOS los valores sean negativos y permite
        // encontrar correctamente el propio Integer.MIN_VALUE si apareciera.
        // --------------------------------------------------------------------
        int maximo = matriz[0][0];

        // --------------------------------------------------------------------
        // RECORRIDO FILA POR FILA
        //
        // Bucle externo: avanza por las filas 'i' de 0 a filas-1.
        // Bucle interno: avanza por las columnas 'j' de 0 a columnas-1.
        //
        // INVARIANTE: al comenzar cada iteración, 'maximo' es el mayor de todas
        // las celdas ya visitadas. Al terminar el doble bucle, 'maximo' es el
        // mayor de las filas × columnas celdas, es decir, el máximo global.
        //
        // Se comparan TODAS las celdas (incluida matriz[0][0], que solo se
        // vuelve a comparar consigo misma sin efecto): la matriz no está
        // ordenada y no hay forma de descartar posiciones, por lo que cualquier
        // celda no visitada aún podría ser el máximo.
        //
        // COSTO: el bucle interno da exactamente 'columnas' iteraciones por cada
        // una de las 'filas' exteriores -> filas × columnas celdas, cada una con
        // trabajo constante -> tiempo O(filas × columnas). La memoria auxiliar
        // ('maximo' e índices 'i', 'j') es constante -> espacio O(1).
        // --------------------------------------------------------------------
        for (int i = 0; i < filas; i++) {
            for (int j = 0; j < columnas; j++) {
                // Actualización del máximo: solo cuando el valor actual supera
                // al mejor encontrado hasta el momento. La comparación estricta
                // ('>') hace que, ante valores repetidos, se conserve el primero
                // hallado, lo cual no altera el resultado (el máximo es el mismo).
                if (matriz[i][j] > maximo) {
                    maximo = matriz[i][j];
                }
            }
        }

        return maximo;
    }

    // ------------------------------------------------------------------------
    // CASOS DE PRUEBA
    // ------------------------------------------------------------------------

    /**
     * Método de entrada: ejecuta los casos solicitados y verifica el resultado.
     *
     * @param args no se utilizan
     */
    public static void main(String[] args) {
        System.out.println("== Mayor elemento de una matriz rectangular ==");
        System.out.println();

        // Caso 1: valores positivos (2x3). Máximo = 9.
        probar("Valores positivos (2x3)",
                new int[][] {{3, 9, 2}, {6, 1, 8}},
                9);

        // Caso 2: TODOS los valores negativos (2x3). Máximo = -1.
        // Prueba clave de la inicialización con la primera celda: partir de 0
        // daría un resultado incorrecto.
        probar("Todos negativos (2x3)",
                new int[][] {{-5, -2, -7}, {-1, -9, -4}},
                -1);

        // Caso 3: valores repetidos (2x3). Máximo = 7 (aparece dos veces).
        probar("Valores repetidos (2x3)",
                new int[][] {{7, 3, 7}, {2, 5, 1}},
                7);

        // Caso 4: una sola celda (1x1). Máximo = -42.
        probar("Una sola celda (1x1)",
                new int[][] {{-42}},
                -42);

        // Caso 5 (adicional): positivos, negativos y cero (2x3). Máximo = 10.
        probar("Positivos, negativos y cero (2x3)",
                new int[][] {{10, -5, 0}, {-3, 8, -7}},
                10);

        // Caso 6 (adicional): una sola fila con varias columnas (1x4). Máximo = 8.
        probar("Una fila (1x4)",
                new int[][] {{2, 4, 6, 8}},
                8);

        // Caso 7 (adicional): los valores extremos del tipo int (1x2).
        // Máximo = Integer.MAX_VALUE. Verifica que no se use un centinela.
        probar("Extremos de int (1x2)",
                new int[][] {{Integer.MIN_VALUE, Integer.MAX_VALUE}},
                Integer.MAX_VALUE);

        System.out.println();
        System.out.println("== Casos sin elementos (deben rechazarse) ==");

        // Matriz null -> IllegalArgumentException descriptiva.
        probarMatrizNula();

        // Matriz sin filas (0x0) -> IllegalArgumentException descriptiva.
        probarSinFilas();

        // Matriz con filas pero sin columnas (3x0) -> IllegalArgumentException.
        probarSinColumnas();

        System.out.println();
        System.out.println("== Otras entradas inválidas (deben rechazarse) ==");

        // Fila null -> IllegalArgumentException descriptiva.
        probarFilaNula();

        // Matriz no rectangular -> IllegalArgumentException descriptiva.
        probarNoRectangular();
    }

    /**
     * Ejecuta un caso de prueba, contrasta el máximo obtenido con lo esperado e
     * informa el resultado.
     *
     * @param nombre   descripción del caso para la salida
     * @param matriz   matriz de entrada
     * @param esperado máximo esperado
     */
    private static void probar(String nombre, int[][] matriz, int esperado) {
        int obtenido = encontrarMaximo(matriz);
        boolean correcto = obtenido == esperado;
        System.out.printf("%-34s %-22s -> maximo = %-12d | esperado = %-12d | %s%n",
                nombre, formatear(matriz), obtenido, esperado,
                correcto ? "OK" : "FALLO");
    }

    /** Verifica que una matriz {@code null} sea rechazada. */
    private static void probarMatrizNula() {
        probarEntradaInvalida("encontrarMaximo(null)", null);
    }

    /** Verifica que una matriz sin filas sea rechazada. */
    private static void probarSinFilas() {
        probarEntradaInvalida("encontrarMaximo(0 filas)", new int[][] {});
    }

    /** Verifica que una matriz sin columnas sea rechazada. */
    private static void probarSinColumnas() {
        probarEntradaInvalida("encontrarMaximo(3x0)", new int[][] {{}, {}, {}});
    }

    /** Verifica que una fila {@code null} sea rechazada. */
    private static void probarFilaNula() {
        probarEntradaInvalida("encontrarMaximo(fila null)",
                new int[][] {{1, 2}, null});
    }

    /** Verifica que una matriz no rectangular sea rechazada. */
    private static void probarNoRectangular() {
        probarEntradaInvalida("encontrarMaximo(no rectangular)",
                new int[][] {{1, 2, 3}, {4, 5}});
    }

    /**
     * Ejecuta una entrada que debe ser rechazada y comprueba que se lance la
     * excepción documentada.
     *
     * @param nombre descripción del caso para la salida
     * @param matriz matriz inválida
     */
    private static void probarEntradaInvalida(String nombre, int[][] matriz) {
        try {
            int resultado = encontrarMaximo(matriz);
            System.out.printf("%-32s -> FALLO (no se lanzó excepción; devolvió %d)%n",
                    nombre, resultado);
        } catch (IllegalArgumentException excepcion) {
            System.out.printf("%-32s -> OK (rechazado: %s)%n",
                    nombre, excepcion.getMessage());
        }
    }

    /**
     * Construye una representación legible de la matriz para la salida de
     * pruebas.
     *
     * @param matriz matriz a representar (puede ser {@code null})
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
 * 1. INICIALIZACIÓN CON LA PRIMERA CELDA: 'maximo' arranca en matriz[0][0] y
 *    no en un valor neutro. Si se partiera de 0 o de Integer.MIN_VALUE, una
 *    matriz con todos los valores negativos devolvería 0 (incorrecto). Partir
 *    de la primera celda también permite hallar correctamente Integer.MIN_VALUE
 *    si fuera el único/mayor elemento.
 *
 * 2. ¿POR QUÉ SE EXAMINAN TODAS LAS CELDAS? Porque la matriz no está ordenada
 *    y no ofrece ninguna propiedad que permita descartar posiciones: cualquier
 *    celda aún no visitada podría ser el máximo. No existe un atajo análogo al
 *    de la búsqueda binaria, que requiere datos ordenados. Por eso el algoritmo
 *    es óptimo: no se puede garantizar el máximo sin mirar cada elemento al
 *    menos una vez -> piso de filas × columnas comparaciones.
 *
 * 3. COMPLEJIDAD TEMPORAL O(filas × columnas): cada celda se procesa una única
 *    vez con trabajo constante (una comparación y, a lo sumo, una asignación).
 *
 * 4. COMPLEJIDAD ESPACIAL AUXILIAR O(1): solo se emplean 'maximo' y los índices
 *    'i', 'j' (más las variables locales 'filas' y 'columnas'), cantidades
 *    constantes independientes del tamaño de la matriz. La matriz de entrada no
 *    se modifica y no se reserva ninguna estructura proporcional a su tamaño.
 *
 * 5. ENTRADAS SIN ELEMENTOS (DECISIÓN DOCUMENTADA): una matriz null, una matriz
 *    sin filas (length == 0) y una matriz con filas de 0 columnas (p. ej.
 *    new int[3][0]) se rechazan con IllegalArgumentException descriptiva. No
 *    existe un máximo en un conjunto vacío y devolver un centinela propagaría
 *    resultados incorrectos sin aviso.
 *
 * 6. MATRIZ RECTANGULAR: el enunciado asume una matriz rectangular (todas las
 *    filas del mismo largo). Se valida esa condición y se rechazan tanto las
 *    filas null como las matrices irregulares con mensajes descriptivos, para
 *    que el ancho esté bien definido y el análisis O(filas × columnas) sea exacto.
 *
 * 7. COMPARACIÓN ESTRICTA ('>'): ante valores repetidos se conserva el primero
 *    hallado. Como el máximo es un valor (no una posición), esto no cambia el
 *    resultado; usar '>=' tampoco lo cambiaría. Se usa '>' para actualizar solo
 *    cuando hay una mejora real.
 * ============================================================================
 */

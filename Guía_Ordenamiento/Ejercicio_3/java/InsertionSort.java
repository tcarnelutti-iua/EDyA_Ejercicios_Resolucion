/*
 * ============================================================================
 * PROMPT INICIAL UTILIZADO PARA GENERAR ESTA SOLUCIÓN
 * ============================================================================
 *
 * Quiero implementar en Java Insertion Sort para ordenar un arreglo de enteros
 * de menor a mayor. Usá como ejemplo principal:
 *
 *     int[] numeros = {1, 2, 3, 5, 4, 6, 7};
 *
 * Insertion Sort mantiene ordenada la parte izquierda del arreglo. En cada paso,
 * toma el siguiente elemento y lo inserta en su posición correcta dentro de esa
 * parte, desplazando hacia la derecha los elementos mayores que él. Funciona
 * bien cuando los datos están casi ordenados porque hay pocos elementos fuera de
 * lugar y, por lo tanto, se necesitan pocos desplazamientos.
 *
 * Ordená el arreglo in-place y mostrá el resultado final junto con la cantidad
 * total de desplazamientos. Contá un desplazamiento cada vez que un elemento
 * mayor se mueve una posición hacia la derecha; no cuentes la asignación final
 * del elemento que se inserta como desplazamiento.
 *
 * Comentá el código para explicar cómo se mantiene ordenada la parte izquierda
 * y cómo se inserta cada elemento. Analizá la complejidad: O(n) en el mejor
 * caso y cercana a O(n) cuando hay pocos desplazamientos; O(n²) en el peor
 * caso; espacio auxiliar O(1).
 *
 * Verificá el resultado con el ejemplo dado y comprobá que se ordene
 * correctamente. Agregá también un caso ya ordenado y uno en orden inverso para
 * contrastar la cantidad de desplazamientos.
 * ============================================================================
 */

import java.io.PrintStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Scanner;

/**
 * Ordenamiento de un arreglo de enteros mediante <b>Insertion Sort</b>
 * (ordenamiento por inserción), de menor a mayor y <i>in-place</i>.
 *
 * <p><b>Idea del algoritmo.</b> El arreglo se divide conceptualmente en dos
 * partes: la parte <i>izquierda</i> {@code arreglo[0..i-1]}, que se mantiene
 * <b>siempre ordenada</b>, y la parte <i>derecha</i> {@code arreglo[i..n-1]},
 * todavía no procesada. En cada paso se toma el primer elemento de la parte
 * derecha ({@code arreglo[i]}), se desplazan hacia la derecha los elementos de
 * la parte izquierda que son <b>mayores</b> que él y se lo coloca en el hueco
 * que queda libre. Así la frontera entre ambas partes avanza una posición y la
 * parte ordenada crece: {@code arreglo[0..i]}.</p>
 *
 * <p><b>Qué cuenta como desplazamiento.</b> Según la consigna, un
 * <i>desplazamiento</i> es cada movimiento de un elemento mayor <b>una posición
 * hacia la derecha</b> ({@code arreglo[j + 1] = arreglo[j]}). La asignación
 * final del elemento que se inserta ({@code arreglo[j + 1] = actual}) <b>no</b>
 * cuenta como desplazamiento: esa línea solo deposita el valor ya guardado en el
 * hueco libre y no mueve a ningún elemento mayor. Además se contabilizan las
 * comparaciones ({@code arreglo[j] > actual}) para apoyar el análisis de
 * complejidad.</p>
 *
 * <p><b>Complejidad.</b></p>
 * <ul>
 *   <li>Temporal, mejor caso: {@code O(n)} — arreglo ya ordenado. Cada elemento
 *       hace una única comparación y no se produce ningún desplazamiento
 *       ({@code n - 1} comparaciones en total).</li>
 *   <li>Temporal, promedio: {@code O(n²)}. Sin embargo, cuando los datos están
 *       <b>casi ordenados</b> (pocos elementos fuera de lugar) se hacen pocos
 *       desplazamientos, las comparaciones quedan cercanas a {@code O(n)} y el
 *       algoritmo se comporta casi como lineal.</li>
 *   <li>Temporal, peor caso: {@code O(n²)} — arreglo en orden inverso. Al
 *       insertar el elemento de la posición {@code i} se requieren {@code i}
 *       comparaciones e {@code i} desplazamientos; en total, {@code n(n-1)/2}
 *       de cada cosa.</li>
 *   <li>Espacial auxiliar: {@code O(1)}. Solo se usan variables locales
 *       (índices, contadores y la copia del valor a insertar); no se crean
 *       arreglos ni estructuras auxiliares.</li>
 * </ul>
 *
 * <p><b>Estabilidad.</b> La comparación es estricta ({@code arreglo[j] > actual}),
 * de modo que los elementos iguales nunca se desplazan entre sí: el orden
 * relativo de los valores repetidos se conserva.</p>
 *
 * <p>La clase separa el <i>algoritmo</i> ({@link #ordenar(int[])}) de la
 * <i>presentación</i> ({@link #imprimirResultado}), de forma que el resultado
 * pueda probarse de forma automática sin depender de la salida por consola.</p>
 *
 * @author Agente Desarrollador
 */
public final class InsertionSort {

    /** Impide instanciar una clase que solo agrupa métodos estáticos. */
    private InsertionSort() {
    }

    // ------------------------------------------------------------------------
    // TIPOS AUXILIARES
    // ------------------------------------------------------------------------

    /**
     * Resultado de una ejecución del algoritmo, inmutable y de solo lectura.
     *
     * <p>Además del arreglo final ordenado, conserva una "foto" del estado del
     * arreglo al terminar cada inserción y los desplazamientos que esa inserción
     * requirió. Esto permite mostrar el proceso (cómo crece la parte ordenada)
     * y, en las pruebas, comprobar que lo mostrado coincida con los cambios
     * realmente efectuados.</p>
     */
    public static final class Resultado {

        private final int[] original;
        private final int[] ordenado;
        private final List<int[]> estadosPorInsercion;
        private final List<Integer> desplazamientosPorInsercion;
        private final long comparaciones;
        private final long desplazamientos;

        private Resultado(int[] original,
                          int[] ordenado,
                          List<int[]> estadosPorInsercion,
                          List<Integer> desplazamientosPorInsercion,
                          long comparaciones,
                          long desplazamientos) {
            this.original = original;
            this.ordenado = ordenado;
            this.estadosPorInsercion = Collections.unmodifiableList(estadosPorInsercion);
            this.desplazamientosPorInsercion =
                    Collections.unmodifiableList(desplazamientosPorInsercion);
            this.comparaciones = comparaciones;
            this.desplazamientos = desplazamientos;
        }

        /** @return copia del arreglo tal como estaba antes de ordenar */
        public int[] getOriginal() {
            return original.clone();
        }

        /** @return copia del arreglo ya ordenado */
        public int[] getOrdenado() {
            return ordenado.clone();
        }

        /**
         * @return copias del estado del arreglo al finalizar cada inserción; la
         *         entrada {@code k} refleja la parte ordenada {@code [0..k+1]}
         */
        public List<int[]> getEstadosPorInsercion() {
            List<int[]> copias = new ArrayList<>(estadosPorInsercion.size());
            for (int[] estado : estadosPorInsercion) {
                copias.add(estado.clone());
            }
            return Collections.unmodifiableList(copias);
        }

        /** @return cantidad de desplazamientos efectuados en cada inserción */
        public List<Integer> getDesplazamientosPorInsercion() {
            return desplazamientosPorInsercion;
        }

        /** @return cantidad de inserciones realizadas (una por cada elemento desde el 1) */
        public int getCantidadInserciones() {
            return estadosPorInsercion.size();
        }

        /** @return cantidad total de comparaciones entre valores realizadas */
        public long getComparaciones() {
            return comparaciones;
        }

        /** @return cantidad total de desplazamientos (elementos mayores movidos a la derecha) */
        public long getDesplazamientos() {
            return desplazamientos;
        }
    }

    // ------------------------------------------------------------------------
    // NÚCLEO DEL ALGORITMO
    // ------------------------------------------------------------------------

    /**
     * Ordena <b>in-place</b> (de menor a mayor) el arreglo recibido aplicando
     * Insertion Sort y devuelve el detalle de cada inserción.
     *
     * <p>El arreglo pasado como argumento queda mutado (ordenado) al finalizar;
     * el valor de retorno aporta el original conservado, el estado tras cada
     * inserción, los desplazamientos por inserción y los contadores totales.</p>
     *
     * @param arreglo arreglo de enteros a ordenar; no puede ser {@code null}
     * @return resultado con el detalle del ordenamiento
     * @throws IllegalArgumentException si {@code arreglo} es {@code null}
     */
    public static Resultado ordenar(int[] arreglo) {
        // --------------------------------------------------------------------
        // VALIDACIÓN DE ENTRADA
        // Un arreglo null no puede recorrerse. Un arreglo vacío o de un solo
        // elemento SÍ es válido: no requiere ninguna inserción.
        // --------------------------------------------------------------------
        if (arreglo == null) {
            throw new IllegalArgumentException("El arreglo no puede ser null.");
        }

        int n = arreglo.length;

        // Copia del estado inicial para poder mostrarlo/verificarlo después.
        int[] original = arreglo.clone();

        // Historial de las inserciones: foto del arreglo tras insertar el
        // elemento i y desplazamientos que esa inserción demandó.
        List<int[]> estadosPorInsercion = new ArrayList<>();
        List<Integer> desplazamientosPorInsercion = new ArrayList<>();

        // Contadores con 'long' para evitar desbordamiento en arreglos grandes.
        // COMPARACIONES:   cada vez que se evalúa 'arreglo[j] > actual'.
        // DESPLAZAMIENTOS: cada elemento mayor que 'actual' que se mueve una
        //                  posición hacia la derecha (arreglo[j + 1] = arreglo[j]).
        //                  La asignación final (arreglo[j + 1] = actual) NO se
        //                  cuenta: solo deposita el valor guardado en el hueco.
        long comparaciones = 0;
        long desplazamientos = 0;

        // --------------------------------------------------------------------
        // INVARIANTE: antes de cada iteración, la parte izquierda
        // arreglo[0..i-1] está ordenada (de menor a mayor). Para i = 1 esa
        // parte es el único elemento arreglo[0], que trivialmente ya está
        // ordenado.
        //
        // En la iteración 'i' se toma el elemento arreglo[i] (el primero de la
        // parte derecha todavía no procesada) y se lo inserta en su posición
        // correcta dentro de arreglo[0..i-1]. Al terminar, la parte ordenada
        // pasa a ser arreglo[0..i] y el invariante se restablece para la
        // siguiente iteración.
        // --------------------------------------------------------------------
        for (int i = 1; i < n; i++) {

            // Se guarda el valor a insertar en una variable temporal. Es
            // necesario porque los desplazamientos posteriores pueden pisar la
            // casilla arreglo[i].
            int actual = arreglo[i];

            // 'j' recorre la parte izquierda de derecha a izquierda buscando la
            // posición correcta del valor 'actual': la primera casilla cuyo
            // predecesor ya no es mayor que él. Al terminar, la posición
            // correcta será 'j + 1'.
            int j = i - 1;

            // Desplazamientos efectuados en ESTA inserción (para el historial).
            int desplazamientosPaso = 0;

            // ---------------------------------------------------------------
            // DESPLAZAMIENTO DE LOS MAYORES
            // Mientras existan elementos mayores que 'actual' a la izquierda,
            // se los corre una posición hacia la derecha para "abrirle" el
            // hueco. La parte izquierda sigue ordenada: cada copia a la derecha
            // conserva la secuencia de los ya ordenados y el hueco queda detrás
            // del último elemento movido.
            // ---------------------------------------------------------------
            while (j >= 0) {

                // Cada mirada sobre 'arreglo[j]' versus 'actual' es una
                // COMPARACIÓN.
                comparaciones++;

                if (arreglo[j] > actual) {
                    // Ese elemento mayor se mueve exactamente una posición a la
                    // derecha: esto es un DESPLAZAMIENTO.
                    arreglo[j + 1] = arreglo[j];
                    desplazamientos++;
                    desplazamientosPaso++;

                    // Se continúa comparando con el predecesor.
                    j--;
                } else {
                    // arreglo[j] <= actual: se encontró el lugar correcto. Los
                    // elementos a la izquierda de 'j' son aún más pequeños, por
                    // lo que no tiene sentido seguir mirando: se corta el bucle.
                    break;
                }
            }

            // ---------------------------------------------------------------
            // INSERCIÓN
            // En 'arreglo[j + 1]' quedó el hueco dejado por el último elemento
            // desplazado. Se coloca allí el valor guardado. Esta asignación NO
            // cuenta como desplazamiento: no mueve ningún elemento mayor, solo
            // deposita el valor que se estaba insertando. Con esto, la parte
            // ordenada pasa a ser arreglo[0..i].
            // ---------------------------------------------------------------
            arreglo[j + 1] = actual;

            // Foto del estado tras la inserción y desplazamientos de este paso.
            estadosPorInsercion.add(arreglo.clone());
            desplazamientosPorInsercion.add(desplazamientosPaso);
        }

        return new Resultado(original, arreglo.clone(),
                estadosPorInsercion, desplazamientosPorInsercion,
                comparaciones, desplazamientos);
    }

    /**
     * Verifica que un arreglo esté ordenado de forma no decreciente
     * ({@code arreglo[i] <= arreglo[i+1]} para todo índice válido).
     *
     * @param arreglo arreglo a verificar
     * @return {@code true} si está ordenado de forma no decreciente
     * @throws IllegalArgumentException si {@code arreglo} es {@code null}
     */
    public static boolean estaOrdenadoNoDecreciente(int[] arreglo) {
        if (arreglo == null) {
            throw new IllegalArgumentException("El arreglo no puede ser null.");
        }
        for (int i = 0; i < arreglo.length - 1; i++) {
            if (arreglo[i] > arreglo[i + 1]) {
                return false;
            }
        }
        return true;
    }

    // ------------------------------------------------------------------------
    // PRESENTACIÓN
    // ------------------------------------------------------------------------

    /**
     * Muestra por la salida indicada el arreglo original, el estado tras cada
     * inserción, el arreglo ordenado, la verificación del orden y los
     * contadores totales.
     *
     * @param resultado resultado devuelto por {@link #ordenar(int[])}
     * @param salida    flujo de salida a utilizar
     */
    public static void imprimirResultado(Resultado resultado, PrintStream salida) {
        int[] original = resultado.getOriginal();
        salida.println("Arreglo original: " + formatear(original));

        List<int[]> estados = resultado.getEstadosPorInsercion();

        if (estados.isEmpty()) {
            // Solo puede ocurrir cuando hay menos de dos elementos.
            if (original.length < 2) {
                salida.println(
                        "El arreglo tiene menos de dos elementos: no hacen falta inserciones.");
            }
        } else {
            List<Integer> desplazamientosPaso = resultado.getDesplazamientosPorInsercion();
            for (int i = 0; i < estados.size(); i++) {
                // En el paso i se insertó el elemento que ocupaba la casilla
                // 'i + 1' del arreglo original: las casillas a la derecha de la
                // frontera nunca fueron tocadas por inserciones anteriores.
                int valorInsertado = original[i + 1];
                int cantidad = desplazamientosPaso.get(i);
                salida.printf("Insertar %d: %s (%d %s)%n",
                        valorInsertado,
                        formatear(estados.get(i)),
                        cantidad,
                        cantidad == 1 ? "desplazamiento" : "desplazamientos");
            }
        }

        int[] ordenado = resultado.getOrdenado();
        salida.println("Arreglo ordenado: " + formatear(ordenado));
        salida.println("Verificacion (orden no decreciente): "
                + (estaOrdenadoNoDecreciente(ordenado) ? "OK" : "FALLO"));
        salida.printf("Comparaciones: %d | Desplazamientos: %d%n",
                resultado.getComparaciones(), resultado.getDesplazamientos());
    }

    // ------------------------------------------------------------------------
    // PROGRAMA PRINCIPAL
    // ------------------------------------------------------------------------

    /**
     * Punto de entrada. Por defecto ejecuta la batería de casos de prueba (que
     * incluye el ejemplo principal del enunciado). Con el argumento
     * {@code interactivo} (o {@code -i}) lee un arreglo por consola y muestra el
     * ordenamiento paso a paso.
     *
     * @param args argumentos de línea de comandos
     */
    public static void main(String[] args) {
        PrintStream salida = System.out;
        boolean modoInteractivo = args.length > 0
                && ("interactivo".equalsIgnoreCase(args[0])
                    || "-i".equalsIgnoreCase(args[0]));

        if (modoInteractivo) {
            ejecutarInteractivo(new Scanner(System.in), salida);
        } else {
            ejecutarCasosDePrueba(salida);
        }
    }

    /**
     * Flujo interactivo: solicita la cantidad de elementos y los valores, ordena
     * y muestra el proceso.
     *
     * @param entrada lector de la entrada estándar
     * @param salida  flujo de salida
     */
    private static void ejecutarInteractivo(Scanner entrada, PrintStream salida) {
        salida.println("== Ordenamiento por Insertion Sort ==");

        int cantidad = leerCantidad(entrada, salida);
        int[] arreglo = new int[cantidad];
        for (int i = 0; i < cantidad; i++) {
            arreglo[i] = leerEntero(entrada, salida,
                    "Ingrese el elemento " + (i + 1) + " de " + cantidad + ": ");
        }

        salida.println();
        Resultado resultado = ordenar(arreglo);
        imprimirResultado(resultado, salida);
        salida.println("Estado: " + (estaOrdenadoNoDecreciente(resultado.getOrdenado())
                ? "OK" : "FALLO"));
    }

    /**
     * Lee una cantidad de elementos, rechazando valores negativos.
     *
     * @param entrada lector de entrada
     * @param salida  flujo de salida
     * @return cantidad no negativa ingresada por el usuario
     */
    private static int leerCantidad(Scanner entrada, PrintStream salida) {
        while (true) {
            int cantidad = leerEntero(entrada, salida,
                    "Ingrese la cantidad de elementos: ");
            if (cantidad < 0) {
                salida.println("La cantidad no puede ser negativa. Intente nuevamente.");
                continue;
            }
            return cantidad;
        }
    }

    /**
     * Lee un número entero, repitiendo la solicitud mientras la entrada no sea
     * un entero válido.
     *
     * @param entrada lector de entrada
     * @param salida  flujo de salida
     * @param mensaje texto a mostrar como solicitud
     * @return entero ingresado por el usuario
     */
    private static int leerEntero(Scanner entrada, PrintStream salida, String mensaje) {
        while (true) {
            salida.print(mensaje);
            salida.flush();
            if (!entrada.hasNextLine()) {
                throw new IllegalStateException("No se recibio entrada por consola.");
            }
            String linea = entrada.nextLine().trim();
            try {
                return Integer.parseInt(linea);
            } catch (NumberFormatException excepcion) {
                salida.println("Entrada invalida: \"" + linea
                        + "\". Debe ingresar un numero entero.");
            }
        }
    }

    // ------------------------------------------------------------------------
    // CASOS DE PRUEBA
    // ------------------------------------------------------------------------

    /**
     * Ejecuta los casos de prueba exigidos en el enunciado: el ejemplo principal
     * (casi ordenado), un arreglo ya ordenado (mejor caso, 0 desplazamientos) y
     * uno en orden inverso (peor caso, n(n-1)/2 desplazamientos), más algunos
     * casos límite. Para cada arreglo se verifica el orden final, la coherencia
     * del historial de inserciones y los contadores esperados.
     *
     * @param salida flujo de salida
     */
    private static void ejecutarCasosDePrueba(PrintStream salida) {
        salida.println("== Casos de prueba exigidos ==");

        // 1) EJEMPLO PRINCIPAL del enunciado: casi ordenado.
        //    Solo el 4 está fuera de lugar: provoca exactamente 1 desplazamiento
        //    (el 5 se corre a la derecha) y el resto se inserta sin mover nada.
        //    Es el caso que muestra por qué Insertion Sort es casi O(n) cuando
        //    hay pocos elementos fuera de lugar.
        verificarCaso("ejemplo principal",
                new int[] {1, 2, 3, 5, 4, 6, 7},
                new int[][] {
                        {1, 2, 3, 5, 4, 6, 7},
                        {1, 2, 3, 5, 4, 6, 7},
                        {1, 2, 3, 5, 4, 6, 7},
                        {1, 2, 3, 4, 5, 6, 7},
                        {1, 2, 3, 4, 5, 6, 7},
                        {1, 2, 3, 4, 5, 6, 7}
                },
                new int[] {0, 0, 0, 1, 0, 0},
                7, 1,
                true, salida);

        // 2) YA ORDENADO (mejor caso): cada elemento hace una única comparación
        //    y 0 desplazamientos. Complejidad O(n).
        verificarCaso("ya ordenado",
                new int[] {1, 2, 3, 4, 5, 6, 7},
                new int[][] {
                        {1, 2, 3, 4, 5, 6, 7},
                        {1, 2, 3, 4, 5, 6, 7},
                        {1, 2, 3, 4, 5, 6, 7},
                        {1, 2, 3, 4, 5, 6, 7},
                        {1, 2, 3, 4, 5, 6, 7},
                        {1, 2, 3, 4, 5, 6, 7}
                },
                new int[] {0, 0, 0, 0, 0, 0},
                6, 0,
                true, salida);

        // 3) ORDEN INVERSO (peor caso): al insertar el elemento i se desplazan
        //    TODOS los i ya ordenados. Desplazamientos totales = 1+2+...+6 = 21
        //    = n(n-1)/2. Complejidad O(n²).
        verificarCaso("orden inverso",
                new int[] {7, 6, 5, 4, 3, 2, 1},
                new int[][] {
                        {6, 7, 5, 4, 3, 2, 1},
                        {5, 6, 7, 4, 3, 2, 1},
                        {4, 5, 6, 7, 3, 2, 1},
                        {3, 4, 5, 6, 7, 2, 1},
                        {2, 3, 4, 5, 6, 7, 1},
                        {1, 2, 3, 4, 5, 6, 7}
                },
                new int[] {1, 2, 3, 4, 5, 6},
                21, 21,
                true, salida);

        salida.println();
        salida.println("== Casos limite adicionales ==");

        // 4) Arreglo vacío: no hacen falta inserciones.
        verificarCaso("vacio",
                new int[] {},
                null, null,
                0, 0,
                false, salida);

        // 5) Un solo elemento: tampoco requiere inserciones.
        verificarCaso("un elemento",
                new int[] {42},
                null, null,
                0, 0,
                false, salida);

        // 6) Todos iguales: la comparación estricta evita desplazar repetidos.
        verificarCaso("todos iguales",
                new int[] {7, 7, 7},
                null, null,
                2, 0,
                false, salida);

        // 7) Valores extremos de int y negativos repetidos.
        verificarCaso("valores extremos",
                new int[] {Integer.MAX_VALUE, Integer.MIN_VALUE, 0, Integer.MIN_VALUE},
                null, null,
                6, 4,
                false, salida);

        // 8) Entrada null: debe rechazarse con excepción descriptiva.
        verificarArregloNulo(salida);

        salida.println();
        salida.println("Todas las verificaciones finalizaron. "
                + "Revisa que no aparezca la palabra FALLO.");
    }

    /**
     * Ordena una copia del arreglo de entrada y comprueba: que la copia original
     * coincida con la entrada, que el resultado final sea exactamente el mismo
     * que produce {@code Arrays.sort} (verdad de referencia), que el historial
     * de inserciones (si se suministra) coincida con el proceso real, y que los
     * contadores de comparaciones y desplazamientos sean los esperados.
     *
     * @param nombre                        etiqueta del caso
     * @param entrada                       arreglo original (no se modifica)
     * @param pasosEsperados                estados esperados tras cada inserción;
     *                                      {@code null} para no verificarlos
     * @param desplazamientosPasoEsperados  desplazamientos esperados por inserción;
     *                                      {@code null} para no verificarlos
     * @param comparacionesEsperadas        comparaciones esperadas
     * @param desplazamientosEsperados      desplazamientos totales esperados
     * @param mostrarDetalle                si {@code true}, imprime el proceso completo
     * @param salida                        flujo de salida
     */
    private static void verificarCaso(String nombre,
                                      int[] entrada,
                                      int[][] pasosEsperados,
                                      int[] desplazamientosPasoEsperados,
                                      long comparacionesEsperadas,
                                      long desplazamientosEsperados,
                                      boolean mostrarDetalle,
                                      PrintStream salida) {
        // Se ordena una COPIA para no mutar el arreglo de prueba original.
        Resultado resultado = ordenar(entrada.clone());

        List<String> errores = new ArrayList<>();

        // El arreglo original mostrado debe coincidir con la entrada.
        if (!Arrays.equals(resultado.getOriginal(), entrada)) {
            errores.add("el arreglo original no coincide con la entrada");
        }

        // Verdad de referencia: el resultado debe ser idéntico a Arrays.sort.
        int[] referencia = entrada.clone();
        Arrays.sort(referencia);
        if (!Arrays.equals(resultado.getOrdenado(), referencia)) {
            errores.add("el arreglo final no coincide con Arrays.sort");
        }
        if (!estaOrdenadoNoDecreciente(resultado.getOrdenado())) {
            errores.add("el arreglo final no esta en orden no decreciente");
        }

        // Contadores totales esperados.
        if (resultado.getComparaciones() != comparacionesEsperadas) {
            errores.add("comparaciones = " + resultado.getComparaciones()
                    + ", esperadas " + comparacionesEsperadas);
        }
        if (resultado.getDesplazamientos() != desplazamientosEsperados) {
            errores.add("desplazamientos = " + resultado.getDesplazamientos()
                    + ", esperados " + desplazamientosEsperados);
        }

        // Consistencia interna: la suma de los desplazamientos por inserción
        // debe coincidir con el total acumulado.
        long sumaPorPasos = 0;
        for (Integer cantidad : resultado.getDesplazamientosPorInsercion()) {
            sumaPorPasos += cantidad;
        }
        if (sumaPorPasos != resultado.getDesplazamientos()) {
            errores.add("la suma de los desplazamientos por paso no coincide con el total");
        }

        // Si se suministraron los pasos esperados, se comparan uno a uno.
        if (pasosEsperados != null) {
            List<int[]> estados = resultado.getEstadosPorInsercion();
            if (estados.size() != pasosEsperados.length) {
                errores.add("se esperaban " + pasosEsperados.length
                        + " inserciones y hubo " + estados.size());
            } else {
                for (int i = 0; i < pasosEsperados.length; i++) {
                    if (!Arrays.equals(estados.get(i), pasosEsperados[i])) {
                        errores.add("insercion " + (i + 1)
                                + " = " + formatear(estados.get(i))
                                + ", esperada " + formatear(pasosEsperados[i]));
                    }
                }
            }

            if (desplazamientosPasoEsperados != null) {
                List<Integer> desplazamientosPaso =
                        resultado.getDesplazamientosPorInsercion();
                for (int i = 0; i < Math.min(desplazamientosPaso.size(),
                        desplazamientosPasoEsperados.length); i++) {
                    if (desplazamientosPaso.get(i) != desplazamientosPasoEsperados[i]) {
                        errores.add("desplazamientos de la insercion " + (i + 1)
                                + " = " + desplazamientosPaso.get(i)
                                + ", esperados " + desplazamientosPasoEsperados[i]);
                    }
                }
            }
        }

        // Salida del caso.
        salida.println("Caso: " + nombre + " -> " + formatear(entrada));
        if (mostrarDetalle) {
            imprimirResultado(resultado, salida);
        } else {
            salida.printf("  Entrada: %s | Ordenado: %s"
                            + " | Comparaciones: %d | Desplazamientos: %d%n",
                    formatear(entrada),
                    formatear(resultado.getOrdenado()),
                    resultado.getComparaciones(),
                    resultado.getDesplazamientos());
        }
        salida.printf("  Estado: %s%n",
                errores.isEmpty() ? "OK" : "FALLO -> " + String.join("; ", errores));
        salida.println();
    }

    /**
     * Comprueba que {@link #ordenar(int[])} rechace un arreglo {@code null}.
     *
     * @param salida flujo de salida
     */
    private static void verificarArregloNulo(PrintStream salida) {
        try {
            ordenar(null);
            salida.println("ordenar(null) -> FALLO (no se lanzo ninguna excepcion)");
        } catch (IllegalArgumentException excepcion) {
            salida.printf("ordenar(null) -> OK (rechazado: %s)%n",
                    excepcion.getMessage());
        }
    }

    /**
     * Construye una representación legible de un arreglo para la salida.
     *
     * @param arreglo arreglo a representar
     * @return cadena con los elementos entre corchetes (ej.: {@code [1, 2, 3]})
     */
    private static String formatear(int[] arreglo) {
        StringBuilder texto = new StringBuilder("[");
        for (int i = 0; i < arreglo.length; i++) {
            if (i > 0) {
                texto.append(", ");
            }
            texto.append(arreglo[i]);
        }
        return texto.append("]").toString();
    }
}

/*
 * ============================================================================
 * NOTAS DE IMPLEMENTACIÓN
 * ============================================================================
 * 1. Separación de responsabilidades: 'ordenar' resuelve el algoritmo y
 *    devuelve un objeto 'Resultado' con toda la información; 'imprimirResultado'
 *    se encarga únicamente de la presentación. Esto permite probar el algoritmo
 *    sin depender de la salida por consola.
 *
 * 2. In-place y espacio auxiliar O(1): el ordenamiento ocurre dentro del propio
 *    arreglo. Solo se usan índices, contadores y una variable temporal para el
 *    valor que se inserta ('actual'). Las casillas a la derecha de la frontera
 *    nunca se tocan antes de su turno, por eso el valor insertado en el paso i
 *    siempre es el 'original[i]' (se aprovecha en la presentación).
 *
 * 3. Invariante central: al comenzar la iteración 'i', la parte izquierda
 *    arreglo[0..i-1] está ordenada. El paso 'i' toma arreglo[i], desplaza a la
 *    derecha los mayores y lo coloca en su posición, dejando arreglo[0..i]
 *    ordenada. Con i recorriendo 1..n-1, al final todo el arreglo queda
 *    ordenado.
 *
 * 4. Qué cuenta cada contador:
 *      - 'comparaciones' : cada evaluación de 'arreglo[j] > actual' en el while.
 *      - 'desplazamientos': cada movimiento de un elemento mayor UNA posición a
 *        la derecha ('arreglo[j + 1] = arreglo[j]'). La asignación final
 *        ('arreglo[j + 1] = actual') NO cuenta: deposita el valor guardado.
 *    Son 'long' para evitar desbordamiento cuando n es grande.
 *
 * 5. Complejidad:
 *      - Mejor caso (ya ordenado): O(n) — n-1 comparaciones y 0 desplazamientos.
 *      - Casi ordenado: pocos elementos fuera de lugar ⇒ pocos desplazamientos,
 *        el tiempo queda cercano a O(n) (ventaja práctica de Insertion Sort).
 *      - Peor caso (orden inverso): O(n²) — n(n-1)/2 comparaciones y la misma
 *        cantidad de desplazamientos.
 *      - Espacio auxiliar: O(1).
 *
 * 6. Estabilidad: la comparación estricta ('arreglo[j] > actual') garantiza que
 *    los elementos iguales nunca se desplazan entre sí.
 *
 * 7. Casos límite cubiertos: arreglo null (IllegalArgumentException), vacío y
 *    de un elemento (sin inserciones), ya ordenado, orden inverso, repetidos y
 *    valores extremos de int.
 * ============================================================================
 */
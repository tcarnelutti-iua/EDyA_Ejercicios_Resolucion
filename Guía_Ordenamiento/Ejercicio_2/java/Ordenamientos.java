/*
 * ============================================================================
 * PROMPT INICIAL UTILIZADO PARA GENERAR ESTA SOLUCIÓN
 * ============================================================================
 *
 * Quiero implementar en Java dos métodos que ordenen un arreglo de enteros y
 * permitan comparar sus operaciones:
 *
 *     bubbleSort(int[] array)
 *     selectionSort(int[] array)
 *
 * Ambos algoritmos deben recibir una copia del mismo arreglo original, porque
 * modifican el arreglo durante el ordenamiento. El programa debe mostrar el
 * arreglo ordenado y la cantidad de comparaciones e intercambios de cada método.
 *
 * En Bubble Sort, comparar elementos vecinos e intercambiarlos cuando estén en
 * el orden incorrecto. En cada pasada, el mayor elemento del tramo no ordenado
 * se desplaza hacia el final.
 *
 * En Selection Sort, recorrer el tramo no ordenado para buscar directamente el
 * menor elemento y luego intercambiarlo con el primero de ese tramo. A
 * diferencia de Bubble Sort, no intercambia cada par de vecinos que está
 * desordenado: como máximo realiza un intercambio por pasada.
 *
 * Contabilizar como comparación cada comparación entre valores y como
 * intercambio solo cada cambio efectivo de posiciones. Explicar con comentarios
 * qué mide cada contador y cómo difieren las estrategias. Ambos tienen
 * complejidad temporal O(n²); Selection Sort realiza como máximo n - 1
 * intercambios, mientras que Bubble Sort puede realizar muchos más.
 *
 * Verificar que los dos métodos ordenen correctamente copias del mismo arreglo.
 * Probar un arreglo desordenado, uno ya ordenado, uno en orden inverso, uno con
 * valores repetidos y uno vacío. Mostrar los resultados y contadores de cada
 * algoritmo para compararlos.
 * ============================================================================
 */

import java.io.PrintStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

/**
 * Compara dos algoritmos clásicos de ordenamiento sobre un mismo arreglo de
 * enteros: <b>Bubble Sort</b> (ordenamiento de burbuja) y <b>Selection Sort</b>
 * (ordenamiento por selección).
 *
 * <p>Ambos métodos ordenan <i>in-place</i> (mueven los elementos dentro del
 * propio arreglo) y devuelven un objeto {@link Resultado} con el arreglo ya
 * ordenado y los contadores de operaciones. Como los métodos mutan el arreglo
 * recibido, para comparar con justicia se les debe entregar a cada uno una
 * <b>copia</b> del mismo arreglo original: así parten del mismo estado inicial
 * y sus resultados son comparables.</p>
 *
 * <p><b>Diferencia central entre las estrategias.</b></p>
 * <ul>
 *   <li><b>Bubble Sort</b> intercambia <i>vecinos</i>: en cada pasada compara
 *       pares contiguos y hace un intercambio cada vez que encuentra una pareja
 *       desordenada. Por eso puede acumular muchos intercambios (del orden de
 *       n² en el peor caso). A cambio, un arreglo ya ordenado se detecta en una
 *       sola pasada sin cambios.</li>
 *   <li><b>Selection Sort</b> <i>selecciona</i>: en cada pasada recorre todo el
 *       tramo no ordenado buscando el menor y hace <b>un único intercambio</b>
 *       (como máximo) para colocarlo al frente del tramo. Por eso nunca supera
 *       los {@code n - 1} intercambios, aunque siempre realiza la misma cantidad
 *       de comparaciones, {@code n(n-1)/2}, independientemente del desorden
 *       inicial.</li>
 * </ul>
 *
 * <p><b>Qué mide cada contador.</b> Una <i>comparación</i> cuenta cada vez que
 * el algoritmo mira dos valores y decide cuál es mayor/menor; un <i>intercambio</i>
 * cuenta solo cada cambio efectivo de posiciones (si dos elementos ya están en
 * el orden correcto, no hay intercambio). Esta distinción es la que explica por
 * qué ambos hacen la misma cantidad de comparaciones en un arreglo ya ordenado
 * (Bubble solo hasta que detecta que no hay cambios; Selection siempre todas),
 * pero difieren radicalmente en los intercambios.</p>
 *
 * <p><b>Complejidad.</b> Ambos son {@code O(n²)} en tiempo (comparaciones). La
 * diferencia está en los movimientos: Bubble Sort puede hacer {@code O(n²)}
 * intercambios, Selection Sort hace a lo sumo {@code n - 1}. El espacio
 * auxiliar de ambos es {@code O(1)}.</p>
 *
 * @author Agente Desarrollador
 */
public final class Ordenamientos {

    /** Impide instanciar una clase que solo agrupa métodos estáticos. */
    private Ordenamientos() {
    }

    // ------------------------------------------------------------------------
    // TIPOS AUXILIARES
    // ------------------------------------------------------------------------

    /**
     * Resultado inmutable de una ejecución de ordenamiento.
     *
     * <p>Conserva el arreglo original (para poder mostrarlo), el arreglo
     * ordenado, la cantidad de pasadas realizadas y los contadores de
     * comparaciones e intercambios. Todos los arreglos se devuelven como copias
     * para no exponer el estado interno.</p>
     */
    public static final class Resultado {

        private final String algoritmo;
        private final int[] original;
        private final int[] ordenado;
        private final int pasadas;
        private final long comparaciones;
        private final long intercambios;

        private Resultado(String algoritmo,
                          int[] original,
                          int[] ordenado,
                          int pasadas,
                          long comparaciones,
                          long intercambios) {
            this.algoritmo = algoritmo;
            this.original = original;
            this.ordenado = ordenado;
            this.pasadas = pasadas;
            this.comparaciones = comparaciones;
            this.intercambios = intercambios;
        }

        /** @return nombre del algoritmo que produjo el resultado */
        public String getAlgoritmo() {
            return algoritmo;
        }

        /** @return copia del arreglo tal como estaba antes de ordenar */
        public int[] getOriginal() {
            return original.clone();
        }

        /** @return copia del arreglo ya ordenado */
        public int[] getOrdenado() {
            return ordenado.clone();
        }

        /** @return cantidad de pasadas efectivamente realizadas */
        public int getPasadas() {
            return pasadas;
        }

        /** @return total de comparaciones entre valores realizadas */
        public long getComparaciones() {
            return comparaciones;
        }

        /** @return total de intercambios efectivos realizados */
        public long getIntercambios() {
            return intercambios;
        }
    }

    // ------------------------------------------------------------------------
    // ALGORITMO 1: BUBBLE SORT
    // ------------------------------------------------------------------------

    /**
     * Ordena <b>in-place</b> (de menor a mayor) el arreglo recibido aplicando
     * <b>Bubble Sort</b> y devuelve el resultado con los contadores.
     *
     * <p>En cada pasada se recorren los pares de elementos <i>vecinos</i> y se
     * intercambian cuando están en el orden incorrecto. Así, el mayor elemento
     * del tramo todavía no ordenado "burbujea" hasta el final de ese tramo y
     * queda en su posición definitiva, razón por la cual el tramo a comparar se
     * reduce en uno en cada nueva pasada.</p>
     *
     * <p><b>Finalización anticipada.</b> Si una pasada completa termina sin
     * ningún intercambio, el arreglo ya estaba ordenado y se corta el proceso.
     * Esto hace que el mejor caso (arreglo ya ordenado) sea {@code O(n)}.</p>
     *
     * @param array arreglo de enteros a ordenar; no puede ser {@code null}
     * @return resultado con el arreglo ordenado y los contadores
     * @throws IllegalArgumentException si {@code array} es {@code null}
     */
    public static Resultado bubbleSort(int[] array) {
        // --------------------------------------------------------------------
        // VALIDACION DE ENTRADA
        // Un arreglo null no puede recorrerse. Un arreglo vacio o de un solo
        // elemento SI es valido: no requiere ninguna pasada ni operacion.
        // --------------------------------------------------------------------
        if (array == null) {
            throw new IllegalArgumentException("El arreglo no puede ser null.");
        }

        int n = array.length;

        // Copia del estado inicial para poder mostrarlo/verificarlo despues.
        int[] original = array.clone();

        // Contadores con 'long' para evitar desbordamiento en arreglos grandes.
        // COMPARACIONES: cada vez que se mira un par de vecinos y se decide.
        // INTERCAMBIOS: solo cuando el par estaba desordenado y se permuta.
        long comparaciones = 0;
        long intercambios = 0;
        int pasadas = 0;

        // --------------------------------------------------------------------
        // CICLO EXTERNO: PASADAS (como maximo n - 1)
        // Al final de cada pasada, al menos el mayor del tramo comparado queda
        // en su posicion definitiva; con n - 1 colocados, el restante ya esta
        // en su lugar por descarte.
        // --------------------------------------------------------------------
        for (int pasada = 0; pasada < n - 1; pasada++) {

            // Bandera para detectar si la pasada efectuo algun intercambio.
            boolean huboCambio = false;

            // ---------------------------------------------------------------
            // CICLO INTERNO: COMPARACION DE VECINOS
            // El tramo se reduce una unidad por pasada: los ultimos 'pasada'
            // elementos ya estan ordenados al final.
            // ---------------------------------------------------------------
            int limite = n - 1 - pasada;
            for (int j = 0; j < limite; j++) {

                // Cada par adyacente examinado cuenta como una COMPARACION.
                comparaciones++;

                // Orden ascendente: si el izquierdo es mayor que el derecho,
                // estan en orden incorrecto y se intercambian.
                if (array[j] > array[j + 1]) {
                    int temporal = array[j];
                    array[j] = array[j + 1];
                    array[j + 1] = temporal;

                    // Solo cuenta como INTERCAMBIO si el cambio fue efectivo.
                    intercambios++;
                    huboCambio = true;
                }
            }

            pasadas++;

            // FINALIZACION ANTICIPADA: si no hubo cambios, ya esta ordenado.
            if (!huboCambio) {
                break;
            }
        }

        return new Resultado("Bubble Sort", original, array.clone(),
                pasadas, comparaciones, intercambios);
    }

    // ------------------------------------------------------------------------
    // ALGORITMO 2: SELECTION SORT
    // ------------------------------------------------------------------------

    /**
     * Ordena <b>in-place</b> (de menor a mayor) el arreglo recibido aplicando
     * <b>Selection Sort</b> y devuelve el resultado con los contadores.
     *
     * <p>En cada pasada se recorre el tramo no ordenado buscando el <i>menor</i>
     * elemento y luego se lo intercambia con el primero de ese tramo. A
     * diferencia de Bubble Sort, aquí no se intercambian vecinos desordenados:
     * se hacen como máximo {@code n - 1} intercambios, uno por pasada (y ninguno
     * si el mínimo ya estaba en su lugar).</p>
     *
     * <p><b>Observación.</b> Selection Sort siempre realiza las mismas
     * {@code n(n-1)/2} comparaciones, sin importar cómo venga el arreglo: recorre
     * todo el tramo en cada pasada para hallar el mínimo. Su ventaja está en la
     * economía de movimientos, no en las comparaciones.</p>
     *
     * @param array arreglo de enteros a ordenar; no puede ser {@code null}
     * @return resultado con el arreglo ordenado y los contadores
     * @throws IllegalArgumentException si {@code array} es {@code null}
     */
    public static Resultado selectionSort(int[] array) {
        // Misma validacion de entrada que en Bubble Sort.
        if (array == null) {
            throw new IllegalArgumentException("El arreglo no puede ser null.");
        }

        int n = array.length;
        int[] original = array.clone();

        // COMPARACIONES: cada vez que se compara un candidato con el minimo
        //                actual del tramo no ordenado.
        // INTERCAMBIOS:  solo cuando el minimo encontrado no estaba ya en la
        //                posicion 'i'; es decir, como maximo uno por pasada.
        long comparaciones = 0;
        long intercambios = 0;
        int pasadas = 0;

        // --------------------------------------------------------------------
        // CICLO EXTERNO: PASADAS (de 0 a n - 2)
        // En la pasada 'i' se coloca el menor del tramo [i, n-1] en la
        // posicion 'i'. Al terminar, el ultimo elemento ya queda ordenado.
        // --------------------------------------------------------------------
        for (int i = 0; i < n - 1; i++) {

            // Se asume, inicialmente, que el primero del tramo es el minimo.
            int indiceMinimo = i;

            // ---------------------------------------------------------------
            // CICLO INTERNO: BUSQUEDA DEL MINIMO
            // Recorre el resto del tramo comparando cada valor con el minimo
            // provisional y actualizandolo cuando encuentra uno menor.
            // ---------------------------------------------------------------
            for (int j = i + 1; j < n; j++) {

                // Cada valor del tramo se compara con el minimo provisional.
                comparaciones++;

                if (array[j] < array[indiceMinimo]) {
                    indiceMinimo = j;
                }
            }

            // INTERCAMBIO UNICO POR PASADA: solo si el minimo no estaba ya en
            // la posicion 'i'. Si el minimo es el propio array[i], no se toca
            // el arreglo (y por eso un arreglo ya ordenado hace 0 intercambios).
            if (indiceMinimo != i) {
                int temporal = array[i];
                array[i] = array[indiceMinimo];
                array[indiceMinimo] = temporal;
                intercambios++;
            }

            pasadas++;
        }

        return new Resultado("Selection Sort", original, array.clone(),
                pasadas, comparaciones, intercambios);
    }

    // ------------------------------------------------------------------------
    // VERIFICACION
    // ------------------------------------------------------------------------

    /**
     * Verifica que un arreglo esté ordenado de forma no decreciente
     * ({@code arreglo[i] <= arreglo[i+1]} para todo índice válido).
     *
     * @param array arreglo a verificar
     * @return {@code true} si está ordenado de forma no decreciente
     * @throws IllegalArgumentException si {@code array} es {@code null}
     */
    public static boolean estaOrdenadoNoDecreciente(int[] array) {
        if (array == null) {
            throw new IllegalArgumentException("El arreglo no puede ser null.");
        }
        for (int i = 0; i < array.length - 1; i++) {
            if (array[i] > array[i + 1]) {
                return false;
            }
        }
        return true;
    }

    // ------------------------------------------------------------------------
    // PRESENTACION
    // ------------------------------------------------------------------------

    /**
     * Imprime, por la salida indicada, el detalle de un resultado: el arreglo
     * original, el arreglo ordenado, la verificación del orden y los contadores.
     *
     * @param resultado resultado devuelto por {@code bubbleSort} o {@code selectionSort}
     * @param salida    flujo de salida
     */
    public static void imprimirResultado(Resultado resultado, PrintStream salida) {
        int[] ordenado = resultado.getOrdenado();
        salida.printf("%-15s %s%n", resultado.getAlgoritmo() + ":",
                formatear(ordenado));
        salida.printf("%-15s Comparaciones: %d | Intercambios: %d | Pasadas: %d"
                        + " | Orden: %s%n",
                "",
                resultado.getComparaciones(),
                resultado.getIntercambios(),
                resultado.getPasadas(),
                estaOrdenadoNoDecreciente(ordenado) ? "OK" : "FALLO");
    }

    // ------------------------------------------------------------------------
    // PROGRAMA PRINCIPAL
    // ------------------------------------------------------------------------

    /**
     * Punto de entrada.
     *
     * <p>Sin argumentos (o con {@code pruebas}/{@code --test}) ejecuta la
     * comparación de Bubble Sort y Selection Sort para los casos exigidos. Con
     * el argumento {@code interactivo} lee un arreglo por consola y muestra la
     * comparación de ambos algoritmos sobre copias de ese arreglo.</p>
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
            ejecutarComparacion(salida);
        }
    }

    /**
     * Flujo interactivo: solicita la cantidad de elementos y los valores, y
     * compara ambos algoritmos sobre copias del mismo arreglo.
     *
     * @param entrada lector de la entrada estándar
     * @param salida  flujo de salida
     */
    private static void ejecutarInteractivo(Scanner entrada, PrintStream salida) {
        salida.println("== Comparacion de ordenamientos ==");

        int cantidad = leerCantidad(entrada, salida);
        int[] arreglo = new int[cantidad];
        for (int i = 0; i < cantidad; i++) {
            arreglo[i] = leerEntero(entrada, salida,
                    "Ingrese el elemento " + (i + 1) + " de " + cantidad + ": ");
        }

        salida.println();
        imprimirComparacion("entrada del usuario", arreglo, salida);
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
    // COMPARACION Y CASOS DE PRUEBA
    // ------------------------------------------------------------------------

    /**
     * Ejecuta la comparación de Bubble Sort y Selection Sort sobre los casos
     * exigidos: desordenado, ya ordenado, orden inverso, con repetidos y vacío,
     * más algunos casos límite. Para cada caso se verifica que ambos métodos
     * ordenen correctamente copias del mismo arreglo y que los contadores sean
     * los esperados.
     *
     * @param salida flujo de salida
     */
    private static void ejecutarComparacion(PrintStream salida) {
        salida.println("== Comparacion: Bubble Sort vs Selection Sort ==");
        salida.println();

        // Caso 1: desordenado (caso general).
        verificarCaso("desordenado", new int[] {5, 2, 9, 1, 6},
                10, 5, 10, 3, salida);

        // Caso 2: ya ordenado (Bubble lo detecta en una pasada; Selection
        //         igual recorre todo pero no mueve nada).
        verificarCaso("ya ordenado", new int[] {1, 2, 3, 4, 5},
                4, 0, 10, 0, salida);

        // Caso 3: orden inverso (peor caso de Bubble en intercambios).
        verificarCaso("orden inverso", new int[] {5, 4, 3, 2, 1},
                10, 10, 10, 2, salida);

        // Caso 4: con valores repetidos.
        verificarCaso("con repetidos", new int[] {3, 1, 3, 1, 2},
                9, 5, 10, 3, salida);

        // Caso 5: vacio (no hacen falta pasadas).
        verificarCaso("vacio", new int[] {},
                0, 0, 0, 0, salida);

        salida.println();
        salida.println("== Casos limite adicionales ==");

        // Un solo elemento.
        verificarCaso("un elemento", new int[] {42},
                0, 0, 0, 0, salida);

        // Todos iguales (no hay desorden que corregir).
        verificarCaso("todos iguales", new int[] {7, 7, 7},
                2, 0, 3, 0, salida);

        // Valores extremos de int y negativos repetidos.
        verificarCaso("valores extremos",
                new int[] {Integer.MAX_VALUE, Integer.MIN_VALUE, 0, Integer.MIN_VALUE},
                6, 4, 6, 2, salida);

        // Entrada null: ambos metodos deben rechazarla.
        verificarArregloNulo(salida);

        salida.println();
        salida.println("Todas las verificaciones finalizaron. "
                + "Revisa que no aparezca la palabra FALLO.");
    }

    /**
     * Ejecuta ambos algoritmos sobre copias del mismo arreglo, imprime sus
     * resultados y verifica orden final y contadores esperados.
     *
     * @param nombre                etiqueta del caso
     * @param entrada               arreglo original (no se modifica)
     * @param compBubbleEsperadas   comparaciones esperadas de Bubble Sort
     * @param cambBubbleEsperados   intercambios esperados de Bubble Sort
     * @param compSelecEsperadas    comparaciones esperadas de Selection Sort
     * @param cambSelecEsperados    intercambios esperados de Selection Sort
     * @param salida                flujo de salida
     */
    private static void verificarCaso(String nombre,
                                      int[] entrada,
                                      long compBubbleEsperadas,
                                      long cambBubbleEsperados,
                                      long compSelecEsperadas,
                                      long cambSelecEsperados,
                                      PrintStream salida) {
        // Se entrega a cada algoritmo una COPIA del mismo arreglo original.
        Resultado bubble = bubbleSort(entrada.clone());
        Resultado selection = selectionSort(entrada.clone());

        List<String> errores = new ArrayList<>();

        // Ambos resultados deben partir del mismo original.
        if (!java.util.Arrays.equals(bubble.getOriginal(), entrada)
                || !java.util.Arrays.equals(selection.getOriginal(), entrada)) {
            errores.add("el arreglo original no coincide con la entrada");
        }

        // Ambos arreglos finales deben estar en orden no decreciente.
        if (!estaOrdenadoNoDecreciente(bubble.getOrdenado())) {
            errores.add("Bubble Sort no dejo el arreglo ordenado");
        }
        if (!estaOrdenadoNoDecreciente(selection.getOrdenado())) {
            errores.add("Selection Sort no dejo el arreglo ordenado");
        }

        // Ambos deben producir el MISMO arreglo ordenado (verdad de referencia).
        if (!java.util.Arrays.equals(bubble.getOrdenado(), selection.getOrdenado())) {
            errores.add("los dos algoritmos produjeron ordenamientos distintos");
        }

        // Contadores esperados.
        if (bubble.getComparaciones() != compBubbleEsperadas) {
            errores.add("Bubble comparaciones = " + bubble.getComparaciones()
                    + ", esperadas " + compBubbleEsperadas);
        }
        if (bubble.getIntercambios() != cambBubbleEsperados) {
            errores.add("Bubble intercambios = " + bubble.getIntercambios()
                    + ", esperados " + cambBubbleEsperados);
        }
        if (selection.getComparaciones() != compSelecEsperadas) {
            errores.add("Selection comparaciones = " + selection.getComparaciones()
                    + ", esperadas " + compSelecEsperadas);
        }
        if (selection.getIntercambios() != cambSelecEsperados) {
            errores.add("Selection intercambios = " + selection.getIntercambios()
                    + ", esperados " + cambSelecEsperados);
        }

        // Salida del caso: entrada, resultados de cada algoritmo y veredicto.
        salida.println("Caso: " + nombre);
        salida.println("  Entrada: " + formatear(entrada));
        imprimirResultado(bubble, salida);
        imprimirResultado(selection, salida);
        salida.println("  Estado: " + (errores.isEmpty()
                ? "OK"
                : "FALLO -> " + String.join("; ", errores)));
        salida.println();
    }

    /**
     * Variante de {@link #verificarCaso} para el modo interactivo: no conoce los
     * contadores esperados, solo imprime los resultados comparados.
     *
     * @param nombre  etiqueta del caso
     * @param entrada arreglo original (no se modifica)
     * @param salida  flujo de salida
     */
    private static void imprimirComparacion(String nombre,
                                            int[] entrada,
                                            PrintStream salida) {
        Resultado bubble = bubbleSort(entrada.clone());
        Resultado selection = selectionSort(entrada.clone());

        salida.println("Caso: " + nombre);
        salida.println("  Entrada: " + formatear(entrada));
        imprimirResultado(bubble, salida);
        imprimirResultado(selection, salida);
        salida.println("  Estado: " + ((estaOrdenadoNoDecreciente(bubble.getOrdenado())
                && estaOrdenadoNoDecreciente(selection.getOrdenado())) ? "OK" : "FALLO"));
        salida.println();
    }

    /**
     * Comprueba que ambos métodos rechacen un arreglo {@code null}.
     *
     * @param salida flujo de salida
     */
    private static void verificarArregloNulo(PrintStream salida) {
        verificarNulo("bubbleSort(null)", true, salida);
        verificarNulo("selectionSort(null)", false, salida);
    }

    private static void verificarNulo(String etiqueta,
                                      boolean esBubble,
                                      PrintStream salida) {
        try {
            if (esBubble) {
                bubbleSort(null);
            } else {
                selectionSort(null);
            }
            salida.println(etiqueta + " -> FALLO (no se lanzo ninguna excepcion)");
        } catch (IllegalArgumentException excepcion) {
            salida.printf("%s -> OK (rechazado: %s)%n",
                    etiqueta, excepcion.getMessage());
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
 * 1. Separación de responsabilidades: 'bubbleSort' y 'selectionSort' resuelven
 *    el algoritmo y devuelven un objeto 'Resultado'; 'imprimirResultado' y
 *    'verificarCaso' se encargan de la presentación y verificación. Así el
 *    algoritmo se puede probar sin depender de la salida por consola.
 *
 * 2. Ambos métodos ordenan in-place. Por eso, para comparar de forma justa, se
 *    les pasa a cada uno una copia del mismo arreglo original (entrada.clone()).
 *
 * 3. Qué mide cada contador:
 *      - 'comparaciones': cada vez que el algoritmo examina dos valores.
 *      - 'intercambios' : solo cada cambio efectivo de posiciones.
 *    Son 'long' para evitar desbordamiento cuando n es grande.
 *
 * 4. Diferencia clave entre estrategias:
 *      - Bubble Sort intercambia vecinos desordenados: puede acumular O(n²)
 *        intercambios (peor caso: arreglo en orden inverso).
 *      - Selection Sort busca el mínimo y hace un único intercambio por pasada:
 *        a lo sumo n - 1 intercambios, y 0 si el arreglo ya está ordenado.
 *    Ambos son O(n²) en comparaciones. Bubble Sort puede cortar antes si
 *    detecta que una pasada no produjo cambios (mejor caso O(n)).
 *
 * 5. Casos límite cubiertos: arreglo null (IllegalArgumentException), vacío y
 *    de un elemento (sin pasadas), ya ordenado, orden inverso, repetidos y
 *    valores extremos de int.
 * ============================================================================
 */

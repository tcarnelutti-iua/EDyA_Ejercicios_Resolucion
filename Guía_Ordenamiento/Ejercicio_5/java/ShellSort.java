/*
 * ============================================================================
 * PROMPT INICIAL UTILIZADO PARA GENERAR ESTA SOLUCIÓN
 * ============================================================================
 *
 * Quiero implementar en Java ShellSort para ordenar un arreglo de números
 * enteros de menor a mayor. Usá la secuencia de gaps que comienza en n / 2 y se
 * divide por dos en cada etapa hasta llegar a 1.
 *
 * En cada etapa, mostrá el valor del gap. Aplicá una inserción por saltos:
 * compará elementos separados por ese gap y desplazá los mayores hasta ubicar
 * cada elemento en su posición dentro de esa secuencia. Después de cada
 * inserción, mostrá el estado actual del arreglo e indicá el gap utilizado. Al
 * llegar a gap = 1, el último paso termina de ordenar el arreglo.
 *
 * Explicá por qué ShellSort puede mejorar a Insertion Sort en arreglos grandes o
 * desordenados: los saltos permiten mover elementos grandes distancias en pocas
 * operaciones, mientras que Insertion Sort solo desplaza elementos adyacentes.
 * Aclará que las etapas con gaps grandes preparan el arreglo, pero es la etapa
 * final con gap 1 la que garantiza el orden completo.
 *
 * Incluí comentarios que expliquen la secuencia de gaps, la inserción por saltos
 * y cómo se reduce el gap. Indicá que la variante solicitada trabaja in-place y
 * usa espacio auxiliar O(1). Para la secuencia de gaps elegida, analizá la
 * complejidad y aclará que depende de la secuencia utilizada.
 *
 * Probá con un arreglo desordenado, uno ya ordenado, uno en orden inverso, uno
 * con valores repetidos y uno vacío. Mostrá también el arreglo original y
 * verificá que el resultado final esté ordenado de menor a mayor.
 * ============================================================================
 */

import java.io.PrintStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Scanner;

/**
 * Ordenamiento de un arreglo de enteros mediante <b>ShellSort</b> (ordenamiento
 * por incrementos decrecientes), de menor a mayor y <i>in-place</i>.
 *
 * <p><b>Idea del algoritmo.</b> ShellSort es una generalización de Insertion
 * Sort. En lugar de ordenar comparando <b>elementos adyacentes</b>, lo hace por
 * etapas sobre subsecuencias de elementos separados una distancia
 * {@code gap}: {@code arreglo[h], arreglo[h+gap], arreglo[h+2*gap], ...}. Dentro
 * de cada subsecuencia se aplica una <i>inserción por saltos</i> (el mismo
 * Insertion Sort pero moviéndose de a {@code gap} posiciones). Al reducir el gap
 * en cada etapa y terminar en {@code gap = 1}, la última pasada es un Insertion
 * Sort clásico que deja el arreglo completamente ordenado.</p>
 *
 * <p><b>Secuencia de gaps.</b> La variante solicitada usa la secuencia original
 * de Shell: se arranca en {@code gap = n / 2} y en cada etapa se divide por dos
 * hasta llegar a {@code 1}:</p>
 *
 * <pre>
 *     gap = n/2, n/4, n/8, ..., 2, 1
 * </pre>
 *
 * <p>Los gaps grandes hacen que los elementos viajen <b>muchas posiciones en un
 * solo movimiento</b>, dejando el arreglo "casi ordenado"; los gaps chicos
 * corrigen los detalles finos. El gap {@code 1} es el único que, por sí solo,
 * <b>garantiza</b> el orden total.</p>
 *
 * <p><b>Inserción por saltos.</b> Para cada gap y cada índice {@code i} a partir
 * de {@code gap}, se toma {@code actual = arreglo[i]} y se lo compara con
 * {@code arreglo[i - gap]}, {@code arreglo[i - 2*gap]}, ... Mientras esos
 * elementos sean <b>mayores</b> que {@code actual}, se los desplaza {@code gap}
 * posiciones a la derecha y se continúa. Cuando aparece un elemento menor o igual
 * (o se llega al borde de la subsecuencia), {@code actual} se coloca en el hueco
 * libre. Es exactamente Insertion Sort, pero con paso {@code gap} en lugar de
 * paso {@code 1}.</p>
 *
 * <p><b>Por qué mejora a Insertion Sort.</b> Insertion Sort solo mueve un
 * elemento una posición por operación: llevar un valor pequeño desde el final
 * hasta el principio exige {@code n - 1} desplazamientos adyacentes y, en total,
 * {@code O(n²)}. ShellSort, con un gap grande, mueve ese mismo valor muchas
 * posiciones de una vez (por ejemplo, de a {@code n/2}), de modo que se necesitan
 * <b>muchos menos movimientos</b> para acercarlo a su lugar. Aun así, las etapas
 * con gaps grandes <b>solo preparan</b> el arreglo (lo dejan casi ordenado); es la
 * etapa final con <b>gap 1</b> la que termina de garantizar el orden completo.</p>
 *
 * <p><b>In-place y espacio auxiliar.</b> Todo el ordenamiento ocurre dentro del
 * propio arreglo. Solo se usan índices, contadores y una variable temporal para
 * el valor insertado: <b>espacio auxiliar {@code O(1)}</b> (sin contar los
 * objetos usados para registrar el proceso).</p>
 *
 * <p><b>Complejidad.</b> Depende fuertemente de la secuencia de gaps elegida:</p>
 * <ul>
 *   <li>Con la secuencia de Shell ({@code n/2, n/4, ..., 1}) el <b>peor caso</b>
 *       es {@code O(n²)} (existen arreglos que la fuerzan) y el <b>promedio</b>
 *       ronda empíricamente {@code O(n^1.3)}–{@code O(n^1.5)}. Aun así, en la
 *       práctica es sensiblemente más rápido que Insertion Sort en arreglos
 *       grandes o desordenados.</li>
 *   <li>Otras secuencias mejoran la cota: la de Knuth ({@code 3h + 1}) y la de
 *       Hibbard ({@code 2^k - 1}) dan {@code O(n^1.5)}; la de Sedgewick,
 *       {@code O(n^1.33)}.</li>
 *   <li>Mejor caso: arreglo ya ordenado, {@code O(n)} con la mayoría de las
 *       secuencias (una comparación por inserción).</li>
 *   <li>Espacio auxiliar: {@code O(1)}.</li>
 * </ul>
 *
 * <p><b>Estabilidad.</b> ShellSort <b>no es estable</b>: al comparar elementos
 * separados por un gap, dos valores iguales pueden quedar en subsecuencias
 * distintas y ser reordenados entre sí. Esto se documenta para no confundirlo con
 * Insertion Sort (que sí es estable).</p>
 *
 * <p>La clase separa el <i>algoritmo</i> ({@link #ordenar(int[])}) de la
 * <i>presentación</i> ({@link #imprimirResultado}), de forma que el resultado
 * pueda probarse automáticamente sin depender de la salida por consola.</p>
 *
 * @author Agente Desarrollador
 */
public final class ShellSort {

    /** Impide instanciar una clase que solo agrupa métodos estáticos. */
    private ShellSort() {
    }

    // ------------------------------------------------------------------------
    // TIPOS AUXILIARES
    // ------------------------------------------------------------------------

    /**
     * Detalle de una única inserción por saltos. Es inmutable y de solo lectura.
     *
     * <p>Guarda el gap con el que se trabajó, la posición y el valor que se
     * insertó, cuántos desplazamientos demandó y una "foto" del arreglo al
     * terminar esa inserción. Con esto se puede mostrar el proceso y, en las
     * pruebas, comprobar que lo mostrado coincida con los cambios efectuados.</p>
     */
    public static final class Paso {

        private final int gap;
        private final int indice;
        private final int valorInsertado;
        private final int desplazamientos;
        private final int[] estado;

        private Paso(int gap, int indice, int valorInsertado,
                     int desplazamientos, int[] estado) {
            this.gap = gap;
            this.indice = indice;
            this.valorInsertado = valorInsertado;
            this.desplazamientos = desplazamientos;
            this.estado = estado;
        }

        /** @return gap de la etapa en la que se hizo esta inserción */
        public int getGap() {
            return gap;
        }

        /** @return índice del arreglo donde estaba el valor que se insertó */
        public int getIndice() {
            return indice;
        }

        /** @return valor que se insertó (el {@code arreglo[indice]} previo) */
        public int getValorInsertado() {
            return valorInsertado;
        }

        /** @return cantidad de desplazamientos de esta inserción */
        public int getDesplazamientos() {
            return desplazamientos;
        }

        /** @return copia del estado del arreglo al terminar la inserción */
        public int[] getEstado() {
            return estado.clone();
        }
    }

    /**
     * Resultado de una ejecución del algoritmo, inmutable y de solo lectura.
     *
     * <p>Además del arreglo final ordenado, conserva la secuencia de gaps usada,
     * el detalle de cada inserción ({@link Paso}) y los contadores totales.</p>
     */
    public static final class Resultado {

        private final int[] original;
        private final int[] ordenado;
        private final List<Integer> gaps;
        private final List<Paso> pasos;
        private final long comparaciones;
        private final long desplazamientos;

        private Resultado(int[] original,
                          int[] ordenado,
                          List<Integer> gaps,
                          List<Paso> pasos,
                          long comparaciones,
                          long desplazamientos) {
            this.original = original;
            this.ordenado = ordenado;
            this.gaps = Collections.unmodifiableList(gaps);
            this.pasos = Collections.unmodifiableList(pasos);
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

        /** @return secuencia de gaps usada, de mayor a menor y terminando en 1 */
        public List<Integer> getGaps() {
            return gaps;
        }

        /** @return detalle de cada inserción, en orden de ejecución */
        public List<Paso> getPasos() {
            return pasos;
        }

        /** @return cantidad de inserciones realizadas (una por cada paso) */
        public int getCantidadInserciones() {
            return pasos.size();
        }

        /** @return cantidad total de comparaciones entre valores realizadas */
        public long getComparaciones() {
            return comparaciones;
        }

        /** @return cantidad total de desplazamientos (elementos movidos a la derecha) */
        public long getDesplazamientos() {
            return desplazamientos;
        }
    }

    // ------------------------------------------------------------------------
    // NÚCLEO DEL ALGORITMO
    // ------------------------------------------------------------------------

    /**
     * Ordena <b>in-place</b> (de menor a mayor) el arreglo recibido aplicando
     * ShellSort con la secuencia de gaps {@code n/2, n/4, ..., 1}, y devuelve el
     * detalle de cada inserción.
     *
     * <p>El arreglo pasado como argumento queda mutado (ordenado) al finalizar;
     * el valor de retorno aporta el original conservado, la secuencia de gaps, el
     * detalle de cada paso y los contadores totales.</p>
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

        // Secuencia de gaps y detalle de cada inserción.
        List<Integer> gaps = new ArrayList<>();
        List<Paso> pasos = new ArrayList<>();

        // Contadores con 'long' para evitar desbordamiento en arreglos grandes.
        // COMPARACIONES:   cada vez que se evalúa 'arreglo[j - gap] > actual'.
        // DESPLAZAMIENTOS: cada elemento mayor que 'actual' que se mueve 'gap'
        //                  posiciones hacia la derecha (arreglo[j] = arreglo[j - gap]).
        //                  La asignación final (arreglo[j] = actual) NO se cuenta.
        long comparaciones = 0;
        long desplazamientos = 0;

        // --------------------------------------------------------------------
        // SECUENCIA DE GAPS (secuencia original de Shell)
        //   gap = n/2, n/4, n/8, ..., 2, 1
        // Se REDUCE el gap dividiéndolo por dos en cada etapa. Para n < 2,
        // n/2 == 0 y el bucle no se ejecuta (no hacen falta inserciones).
        //
        // INVARIANTE: al comenzar una etapa con un gap dado, cada subsecuencia
        // de índices separados 'gap' posiciones ya está ordenada por las etapas
        // anteriores. Tras aplicar la inserción por saltos en toda la etapa, ese
        // invariante se refuerza para el gap menor siguiente.
        // --------------------------------------------------------------------
        for (int gap = n / 2; gap > 0; gap /= 2) {
            gaps.add(gap);

            // 'i' recorre desde 'gap' hasta n-1: cada valor se inserta dentro de
            // su subsecuencia (los primeros 'gap' elementos de cada subsecuencia
            // son, por definición, el inicio y no tienen predecesor separado un
            // gap dentro del arreglo).
            for (int i = gap; i < n; i++) {

                // Se guarda el valor a insertar antes de desplazar nada, porque
                // los movimientos pueden pisar la casilla arreglo[i].
                int actual = arreglo[i];

                // 'j' avanza hacia atrás de a 'gap' buscando la posición correcta.
                int j = i;

                // Desplazamientos de ESTA inserción (para el historial).
                int desplazamientosPaso = 0;

                // -----------------------------------------------------------
                // INSERCIÓN POR SALTOS
                // Se comparan elementos separados exactamente 'gap' posiciones.
                // Mientras el elemento anterior de la subsecuencia sea MAYOR que
                // 'actual', se lo corre 'gap' posiciones a la derecha para abrir
                // el hueco. Al comparar siempre contra 'actual' (y no contra el
                // vecino ya movido), la subsecuencia mantiene su orden relativo.
                // -----------------------------------------------------------
                while (j >= gap) {

                    // Cada mirada sobre 'arreglo[j - gap]' versus 'actual' es una
                    // COMPARACIÓN (incluye la que falla y corta el bucle).
                    comparaciones++;

                    if (arreglo[j - gap] > actual) {
                        // Ese elemento mayor se mueve 'gap' posiciones a la
                        // derecha: esto es un DESPLAZAMIENTO. Nótese que con
                        // gap grande un solo movimiento recorre gran distancia;
                        // esa es la ventaja frente a Insertion Sort.
                        arreglo[j] = arreglo[j - gap];
                        desplazamientos++;
                        desplazamientosPaso++;

                        j -= gap;
                    } else {
                        // arreglo[j - gap] <= actual: se encontró el lugar. Como
                        // la subsecuencia ya estaba ordenada, los elementos más a
                        // la izquierda son aún menores: se corta el bucle.
                        break;
                    }
                }

                // -----------------------------------------------------------
                // INSERCIÓN
                // En 'arreglo[j]' quedó el hueco libre. Se coloca allí el valor
                // guardado. Esta asignación NO cuenta como desplazamiento: no
                // mueve ningún elemento mayor, solo deposita el valor que se
                // estaba insertando.
                // -----------------------------------------------------------
                arreglo[j] = actual;

                // Foto del estado tras la inserción y desplazamientos del paso.
                pasos.add(new Paso(gap, i, actual, desplazamientosPaso,
                        arreglo.clone()));
            }
        }

        // Con la secuencia de Shell el último gap recorrido es siempre 1 (cuando
        // n >= 2), por lo que la etapa final equivale a un Insertion Sort y deja
        // el arreglo totalmente ordenado. Para n < 2 no hubo etapas y el arreglo
        // ya estaba ordenado de forma trivial.
        return new Resultado(original, arreglo.clone(), gaps, pasos,
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
     * Muestra por la salida indicada el arreglo original, la secuencia de gaps,
     * el estado tras cada inserción agrupado por gap, el arreglo ordenado, la
     * verificación del orden y los contadores totales.
     *
     * @param resultado resultado devuelto por {@link #ordenar(int[])}
     * @param salida    flujo de salida a utilizar
     */
    public static void imprimirResultado(Resultado resultado, PrintStream salida) {
        salida.println("Arreglo original: " + formatear(resultado.getOriginal()));
        salida.println("Secuencia de gaps: " + formatearLista(resultado.getGaps()));

        List<Paso> pasos = resultado.getPasos();

        if (pasos.isEmpty()) {
            // Solo puede ocurrir cuando hay menos de dos elementos.
            if (resultado.getOriginal().length < 2) {
                salida.println("El arreglo tiene menos de dos elementos: "
                        + "no hacen falta inserciones.");
            }
        } else {
            int gapMostrado = Integer.MIN_VALUE;
            for (Paso paso : pasos) {
                // Cada vez que cambia el gap se abre una nueva etapa.
                if (paso.getGap() != gapMostrado) {
                    gapMostrado = paso.getGap();
                    salida.println();
                    salida.printf("== Gap = %d ==%n", gapMostrado);
                }
                salida.printf("  Insertar %d (indice %d) -> %s  (gap = %d, %d %s)%n",
                        paso.getValorInsertado(),
                        paso.getIndice(),
                        formatear(paso.getEstado()),
                        paso.getGap(),
                        paso.getDesplazamientos(),
                        paso.getDesplazamientos() == 1
                                ? "desplazamiento" : "desplazamientos");
            }
        }

        int[] ordenado = resultado.getOrdenado();
        salida.println();
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
     * Punto de entrada. Por defecto ejecuta la batería de casos de prueba
     * exigidos en el enunciado. Con el argumento {@code interactivo} (o
     * {@code -i}) lee un arreglo por consola y muestra el ordenamiento paso a
     * paso.
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
        salida.println("== Ordenamiento por ShellSort ==");

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
     * Ejecuta los casos de prueba exigidos en el enunciado: un arreglo
     * desordenado (ejemplo principal), uno ya ordenado, uno en orden inverso y
     * uno con valores repetidos, más casos límite (vacío, un elemento, valores
     * extremos y entrada {@code null}). Para cada arreglo se verifica el orden
     * final contra {@link Arrays#sort(int[])}, la secuencia de gaps, la
     * coherencia del proceso y, cuando corresponde, los contadores esperados.
     *
     * @param salida flujo de salida
     */
    private static void ejecutarCasosDePrueba(PrintStream salida) {
        salida.println("== Casos de prueba exigidos ==");

        // 1) DESORDENADO (ejemplo principal). n = 8 => gaps 4, 2, 1.
        //    Se muestra el proceso completo y se comprueba contra estados
        //    esperados, la secuencia de gaps y los contadores exactos.
        verificarCaso("desordenado (ejemplo principal)",
                new int[] {8, 3, 7, 4, 2, 6, 5, 1},
                new int[] {4, 2, 1},
                new int[][] {
                        {2, 3, 7, 4, 8, 6, 5, 1},
                        {2, 3, 7, 4, 8, 6, 5, 1},
                        {2, 3, 5, 4, 8, 6, 7, 1},
                        {2, 3, 5, 1, 8, 6, 7, 4},
                        {2, 3, 5, 1, 8, 6, 7, 4},
                        {2, 1, 5, 3, 8, 6, 7, 4},
                        {2, 1, 5, 3, 8, 6, 7, 4},
                        {2, 1, 5, 3, 8, 6, 7, 4},
                        {2, 1, 5, 3, 7, 6, 8, 4},
                        {2, 1, 5, 3, 7, 4, 8, 6},
                        {1, 2, 5, 3, 7, 4, 8, 6},
                        {1, 2, 5, 3, 7, 4, 8, 6},
                        {1, 2, 3, 5, 7, 4, 8, 6},
                        {1, 2, 3, 5, 7, 4, 8, 6},
                        {1, 2, 3, 4, 5, 7, 8, 6},
                        {1, 2, 3, 4, 5, 7, 8, 6},
                        {1, 2, 3, 4, 5, 6, 7, 8}
                },
                24L, 12L,
                true, salida);

        // 2) YA ORDENADO (mejor caso con esta secuencia): cada inserción hace una
        //    sola comparación y 0 desplazamientos. Comparaciones = (n-4)+(n-2)+(n-1)
        //    = 4+6+7 = 17. No se muestra el detalle para no repetir estados.
        verificarCaso("ya ordenado",
                new int[] {1, 2, 3, 4, 5, 6, 7, 8},
                new int[] {4, 2, 1},
                null,
                17L, 0L,
                false, salida);

        // 3) ORDEN INVERSO (peor caso típico). Se verifica contra Arrays.sort;
        //    el conteo exacto no se fija aquí (depende del patrón), pero se
        //    comprueba la coherencia de los contadores y los estados.
        verificarCaso("orden inverso",
                new int[] {8, 7, 6, 5, 4, 3, 2, 1},
                new int[] {4, 2, 1},
                null,
                null, null,
                false, salida);

        // 4) VALORES REPETIDOS. ShellSort no es estable, pero el resultado debe
        //    coincidir con Arrays.sort (misma multiset de valores ordenados).
        verificarCaso("valores repetidos",
                new int[] {5, 1, 5, 3, 3, 1, 5, 3},
                new int[] {4, 2, 1},
                null,
                null, null,
                false, salida);

        salida.println();
        salida.println("== Casos limite adicionales ==");

        // 5) Arreglo vacío: no hacen falta inserciones y no hay gaps.
        verificarCaso("vacio",
                new int[] {},
                new int[] {},
                null,
                0L, 0L,
                false, salida);

        // 6) Un solo elemento: tampoco requiere inserciones.
        verificarCaso("un elemento",
                new int[] {42},
                new int[] {},
                null,
                0L, 0L,
                false, salida);

        // 7) Valores extremos de int y negativos repetidos.
        verificarCaso("valores extremos",
                new int[] {Integer.MAX_VALUE, Integer.MIN_VALUE, 0, Integer.MIN_VALUE},
                new int[] {2, 1},
                null,
                null, null,
                false, salida);

        // 8) Entrada null: debe rechazarse con excepción descriptiva.
        verificarArregloNulo(salida);

        salida.println();
        salida.println("Todas las verificaciones finalizaron. "
                + "Revisa que no aparezca la palabra FALLO.");
    }

    /**
     * Ordena una copia del arreglo de entrada y comprueba: que el original
     * conservado coincida con la entrada, que el resultado final sea exactamente
     * el mismo que produce {@code Arrays.sort} (verdad de referencia), que la
     * secuencia de gaps sea la esperada, que el historial de estados (si se
     * suministra) coincida con el proceso real, que los contadores de
     * desplazamientos por paso sumen el total y, cuando se indiquen, que los
     * contadores coincidan con lo esperado.
     *
     * @param nombre                   etiqueta del caso
     * @param entrada                  arreglo original (no se modifica)
     * @param gapsEsperados            secuencia de gaps esperada
     * @param estadosEsperados         estados esperados tras cada inserción;
     *                                 {@code null} para no verificarlos
     * @param comparacionesEsperadas   comparaciones esperadas; {@code null} para
     *                                 no verificarlas
     * @param desplazamientosEsperados desplazamientos totales esperados;
     *                                 {@code null} para no verificarlos
     * @param mostrarDetalle           si {@code true}, imprime el proceso completo
     * @param salida                   flujo de salida
     */
    private static void verificarCaso(String nombre,
                                      int[] entrada,
                                      int[] gapsEsperados,
                                      int[][] estadosEsperados,
                                      Long comparacionesEsperadas,
                                      Long desplazamientosEsperados,
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

        // Secuencia de gaps esperada.
        if (gapsEsperados != null) {
            List<Integer> gaps = resultado.getGaps();
            if (gaps.size() != gapsEsperados.length) {
                errores.add("se esperaban " + gapsEsperados.length
                        + " gaps y hubo " + gaps.size());
            } else {
                for (int i = 0; i < gapsEsperados.length; i++) {
                    if (gaps.get(i) != gapsEsperados[i]) {
                        errores.add("gap " + (i + 1) + " = " + gaps.get(i)
                                + ", esperado " + gapsEsperados[i]);
                    }
                }
            }
        }

        // Consistencia interna: la suma de los desplazamientos por paso debe
        // coincidir con el total acumulado.
        long sumaPorPasos = 0;
        for (Paso paso : resultado.getPasos()) {
            sumaPorPasos += paso.getDesplazamientos();
        }
        if (sumaPorPasos != resultado.getDesplazamientos()) {
            errores.add("la suma de los desplazamientos por paso no coincide con el total");
        }

        // Si se suministraron los estados esperados, se comparan uno a uno.
        if (estadosEsperados != null) {
            List<Paso> pasos = resultado.getPasos();
            if (pasos.size() != estadosEsperados.length) {
                errores.add("se esperaban " + estadosEsperados.length
                        + " inserciones y hubo " + pasos.size());
            } else {
                for (int i = 0; i < estadosEsperados.length; i++) {
                    if (!Arrays.equals(pasos.get(i).getEstado(), estadosEsperados[i])) {
                        errores.add("insercion " + (i + 1) + " = "
                                + formatear(pasos.get(i).getEstado())
                                + ", esperada " + formatear(estadosEsperados[i]));
                    }
                }
            }
        }

        // Contadores totales esperados (opcionales).
        if (comparacionesEsperadas != null
                && resultado.getComparaciones() != comparacionesEsperadas) {
            errores.add("comparaciones = " + resultado.getComparaciones()
                    + ", esperadas " + comparacionesEsperadas);
        }
        if (desplazamientosEsperados != null
                && resultado.getDesplazamientos() != desplazamientosEsperados) {
            errores.add("desplazamientos = " + resultado.getDesplazamientos()
                    + ", esperados " + desplazamientosEsperados);
        }

        // Salida del caso.
        salida.println("Caso: " + nombre + " -> " + formatear(entrada));
        if (mostrarDetalle) {
            imprimirResultado(resultado, salida);
        } else {
            salida.printf("  Entrada: %s | Ordenado: %s"
                            + " | Gaps: %s | Inserciones: %d"
                            + " | Comparaciones: %d | Desplazamientos: %d%n",
                    formatear(entrada),
                    formatear(resultado.getOrdenado()),
                    formatearLista(resultado.getGaps()),
                    resultado.getCantidadInserciones(),
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

    // ------------------------------------------------------------------------
    // UTILIDADES DE FORMATO
    // ------------------------------------------------------------------------

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

    /**
     * Construye una representación legible de una lista de enteros.
     *
     * @param valores lista de enteros a representar
     * @return cadena con los valores entre corchetes (ej.: {@code [4, 2, 1]})
     */
    private static String formatearLista(List<Integer> valores) {
        StringBuilder texto = new StringBuilder("[");
        for (int i = 0; i < valores.size(); i++) {
            if (i > 0) {
                texto.append(", ");
            }
            texto.append(valores.get(i));
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
 * 2. Secuencia de gaps: secuencia original de Shell, gap = n/2, n/4, ..., 1.
 *    Se reduce el gap dividiéndolo por dos hasta llegar a 1. Para n < 2 el
 *    bucle no se ejecuta.
 *
 * 3. Inserción por saltos: para cada gap y cada índice i >= gap se toma
 *    actual = arreglo[i] y se desplazan 'gap' posiciones a la derecha los
 *    elementos de la subsecuencia que sean mayores que 'actual'. Cuando aparece
 *    uno menor o igual (o se llega al borde), se coloca 'actual' en el hueco.
 *    Es Insertion Sort con paso 'gap' en lugar de paso 1.
 *
 * 4. Por qué mejora a Insertion Sort: con gaps grandes un elemento viaja muchas
 *    posiciones en un solo movimiento, en lugar de una posición por operación.
 *    Las etapas de gap grande solo preparan el arreglo; la etapa con gap 1 es la
 *    que garantiza el orden total (equivale a un Insertion Sort final).
 *
 * 5. In-place y espacio auxiliar O(1): el ordenamiento ocurre dentro del propio
 *    arreglo. Solo se usan índices, contadores y una variable temporal para el
 *    valor que se inserta ('actual'); no se crean arreglos auxiliares para
 *    ordenar (los objetos 'Paso'/'Resultado' son solo para mostrar el proceso).
 *
 * 6. Qué cuenta cada contador:
 *      - 'comparaciones' : cada evaluación de 'arreglo[j - gap] > actual',
 *        incluida la que falla y corta el bucle.
 *      - 'desplazamientos': cada movimiento de un elemento mayor 'gap'
 *        posiciones a la derecha ('arreglo[j] = arreglo[j - gap]'). La
 *        asignación final ('arreglo[j] = actual') NO cuenta.
 *    Son 'long' para evitar desbordamiento cuando n es grande.
 *
 * 7. Complejidad: con la secuencia de Shell, peor caso O(n²) y promedio
 *    aproximadamente O(n^1.3)–O(n^1.5); mejor caso O(n). La cota depende de la
 *    secuencia de gaps (Knuth/Hibbard O(n^1.5), Sedgewick O(n^1.33)). Espacio
 *    auxiliar O(1).
 *
 * 8. Estabilidad: ShellSort NO es estable, porque compara elementos separados
 *    por un gap y puede reordenar valores iguales que caen en subsecuencias
 *    distintas. El resultado sigue siendo correcto (coincide con Arrays.sort),
 *    solo cambia el orden relativo de los repetidos.
 *
 * 9. Casos límite cubiertos: arreglo null (IllegalArgumentException), vacío y
 *    de un elemento (sin gaps ni inserciones), desordenado, ya ordenado, orden
 *    inverso, repetidos y valores extremos de int.
 * ============================================================================
 */

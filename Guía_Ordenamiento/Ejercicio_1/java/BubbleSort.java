/*
 * ============================================================================
 * PROMPT INICIAL UTILIZADO PARA GENERAR ESTA SOLUCIÓN
 * ============================================================================
 *
 * Quiero implementar en Java un programa que ordene un arreglo de números
 * enteros de menor a mayor usando Bubble Sort.
 *
 * El programa debe leer la cantidad de elementos y luego los valores del
 * arreglo. Antes de ordenar, debe mostrar el arreglo original. Después, debe
 * mostrar el estado del arreglo al finalizar cada pasada, indicando el número
 * de pasada.
 *
 * En Bubble Sort se comparan elementos adyacentes y se intercambian cuando
 * están en el orden incorrecto. En cada pasada, el mayor elemento del tramo
 * todavía no ordenado se desplaza hacia el final, como si "burbujease" hasta su
 * posición definitiva. Por eso, en las pasadas siguientes ya no es necesario
 * volver a comparar los elementos que quedaron ordenados al final.
 *
 * Incluí comentarios que expliquen el propósito de cada ciclo, cuándo se
 * realiza un intercambio y por qué el mayor elemento termina al final de cada
 * pasada. Realizá hasta n - 1 pasadas, reduciendo el tramo comparado en cada
 * una. Si el arreglo tiene menos de dos elementos, indicá que no hacen falta
 * pasadas.
 *
 * Verificá el resultado comprobando que los elementos estén en orden no
 * decreciente después de ordenar. Probá, como mínimo, un arreglo desordenado,
 * uno ya ordenado, uno en orden inverso, uno con valores repetidos y uno vacío.
 * Para cada prueba, comprobá también que las pasadas mostradas correspondan a
 * los cambios realizados.
 * ============================================================================
 */

import java.io.PrintStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Scanner;

/**
 * Ordenamiento de un arreglo de enteros mediante el algoritmo <b>Bubble Sort</b>
 * (ordenamiento de burbuja), mostrando el estado del arreglo al finalizar cada
 * pasada.
 *
 * <p><b>Idea del algoritmo.</b> Se recorren los pares de elementos adyacentes y
 * se intercambian cuando están en el orden incorrecto (el de la izquierda es
 * mayor que el de la derecha). En cada pasada, el mayor elemento del tramo
 * todavía no ordenado se desplaza, posición a posición, hasta el final de ese
 * tramo: es el elemento que "burbujea" hasta su lugar definitivo. Por eso, en
 * las pasadas siguientes el tramo a comparar se reduce en uno, ya que el último
 * elemento de la pasada anterior ya está garantizado en su posición final.</p>
 *
 * <p><b>Finalización anticipada.</b> Se usa la bandera {@code huboCambio}. Si
 * una pasada completa termina sin realizar ningún intercambio, el arreglo ya
 * está ordenado y no tiene sentido seguir haciendo pasadas: el bucle se corta.
 * De este modo, un arreglo ya ordenado se resuelve en una única pasada.</p>
 *
 * <p><b>Complejidad.</b></p>
 * <ul>
 *   <li>Temporal, mejor caso: {@code O(n)} — arreglo ya ordenado (una pasada).</li>
 *   <li>Temporal, promedio y peor caso: {@code O(n²)} — del orden de n(n-1)/2
 *       comparaciones.</li>
 *   <li>Espacial auxiliar: {@code O(1)} — solo unas pocas variables temporales.</li>
 * </ul>
 *
 * <p><b>Estabilidad.</b> La comparación es estricta ({@code arreglo[j] > arreglo[j+1]}),
 * de modo que los elementos iguales nunca se intercambian entre sí; el orden
 * relativo de los elementos repetidos se conserva.</p>
 *
 * <p>Esta clase separa el <i>algoritmo</i> ({@link #ordenar(int[])}) de la
 * <i>presentación</i> ({@link #imprimirResultado}), de forma que el resultado
 * pueda probarse de forma automática sin depender de la salida por consola.</p>
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
     * Resultado de una ejecución del algoritmo, inmutable y de solo lectura.
     *
     * <p>Además del arreglo final ordenado, conserva el estado del arreglo al
     * terminar cada pasada y si esa pasada produjo algún cambio. Esto permite
     * mostrar las pasadas y, en las pruebas, comprobar que lo mostrado coincida
     * con los cambios realmente efectuados.</p>
     */
    public static final class Resultado {

        private final int[] original;
        private final int[] ordenado;
        private final List<int[]> estadosPorPasada;
        private final List<Boolean> cambiosPorPasada;
        private final long comparaciones;
        private final long intercambios;

        private Resultado(int[] original,
                          int[] ordenado,
                          List<int[]> estadosPorPasada,
                          List<Boolean> cambiosPorPasada,
                          long comparaciones,
                          long intercambios) {
            this.original = original;
            this.ordenado = ordenado;
            this.estadosPorPasada = Collections.unmodifiableList(estadosPorPasada);
            this.cambiosPorPasada = Collections.unmodifiableList(cambiosPorPasada);
            this.comparaciones = comparaciones;
            this.intercambios = intercambios;
        }

        /** @return copia del arreglo tal como estaba antes de ordenar */
        public int[] getOriginal() {
            return original.clone();
        }

        /** @return copia del arreglo ya ordenado */
        public int[] getOrdenado() {
            return ordenado.clone();
        }

        /** @return copia del estado del arreglo al final de cada pasada */
        public List<int[]> getEstadosPorPasada() {
            List<int[]> copias = new ArrayList<>(estadosPorPasada.size());
            for (int[] estado : estadosPorPasada) {
                copias.add(estado.clone());
            }
            return Collections.unmodifiableList(copias);
        }

        /** @return marca de cambio correspondiente a cada pasada realizada */
        public List<Boolean> getCambiosPorPasada() {
            return cambiosPorPasada;
        }

        /** @return cantidad de pasadas efectivamente realizadas */
        public int getCantidadPasadas() {
            return estadosPorPasada.size();
        }

        /** @return cantidad total de comparaciones entre elementos adyacentes */
        public long getComparaciones() {
            return comparaciones;
        }

        /** @return cantidad total de intercambios realizados */
        public long getIntercambios() {
            return intercambios;
        }
    }

    // ------------------------------------------------------------------------
    // NÚCLEO DEL ALGORITMO
    // ------------------------------------------------------------------------

    /**
     * Ordena <b>in-place</b> (de menor a mayor) el arreglo recibido aplicando
     * Bubble Sort y devuelve el detalle de lo ocurrido en cada pasada.
     *
     * <p>El arreglo pasado como argumento queda mutado (ordenado) al finalizar;
     * el valor de retorno aporta el original conservado, el estado tras cada
     * pasada, los indicadores de cambio y los contadores.</p>
     *
     * @param arreglo arreglo de enteros a ordenar; no puede ser {@code null}
     * @return resultado con el detalle del ordenamiento
     * @throws IllegalArgumentException si {@code arreglo} es {@code null}
     */
    public static Resultado ordenar(int[] arreglo) {
        // --------------------------------------------------------------------
        // VALIDACIÓN DE ENTRADA
        // Un arreglo null no puede recorrerse. Un arreglo vacío o de un solo
        // elemento SÍ es válido: no requiere ninguna pasada.
        // --------------------------------------------------------------------
        if (arreglo == null) {
            throw new IllegalArgumentException("El arreglo no puede ser null.");
        }

        int n = arreglo.length;

        // Copia del estado inicial para poder mostrarlo/verificarlo después.
        int[] original = arreglo.clone();

        // Historial de los estados y de los cambios a lo largo de las pasadas.
        List<int[]> estadosPorPasada = new ArrayList<>();
        List<Boolean> cambiosPorPasada = new ArrayList<>();

        // Contadores con 'long' para evitar desbordamiento en arreglos grandes.
        long comparaciones = 0;
        long intercambios = 0;

        // --------------------------------------------------------------------
        // CICLO EXTERNO: PASADAS
        // Se hacen como máximo n-1 pasadas. Si n < 2, el límite 'n - 1' es
        // negativo o cero y el bucle no se ejecuta: no hacen falta pasadas.
        //
        // ¿Por qué como máximo n-1? Porque al final de cada pasada, al menos el
        // mayor de los elementos del tramo comparado queda en su posición
        // definitiva. Con n-1 elementos colocados, el restante ya está en su
        // lugar por descarte.
        // --------------------------------------------------------------------
        for (int pasada = 0; pasada < n - 1; pasada++) {

            // Bandera con la que se detecta si la pasada efectuó algún cambio.
            boolean huboCambio = false;

            // ---------------------------------------------------------------
            // CICLO INTERNO: COMPARACIÓN DE ADYACENTES
            // El tramo a comparar se reduce en cada pasada: tras 'pasada'
            // pasadas ya hay 'pasada' elementos ordenados al final del arreglo,
            // por eso se recorre hasta 'n - 1 - pasada'.
            // ---------------------------------------------------------------
            int limite = n - 1 - pasada;
            for (int j = 0; j < limite; j++) {

                // Cada par adyacente examinado cuenta como una comparación.
                comparaciones++;

                // Orden ascendente: si el de la izquierda es mayor que el de la
                // derecha, están en orden incorrecto y se intercambian.
                if (arreglo[j] > arreglo[j + 1]) {

                    // Intercambio in-place mediante variable temporal (O(1)).
                    int temporal = arreglo[j];
                    arreglo[j] = arreglo[j + 1];
                    arreglo[j + 1] = temporal;

                    // Se contabiliza el intercambio y se marca que esta pasada
                    // movió elementos.
                    intercambios++;
                    huboCambio = true;
                }
            }

            // Al terminar el recorrido interno, el mayor elemento del tramo
            // "burbujeó" hasta el final del mismo y quedó en su posición
            // definitiva. Se guarda una foto del estado para poder mostrarlo.
            estadosPorPasada.add(arreglo.clone());
            cambiosPorPasada.add(huboCambio);

            // -----------------------------------------------------------------
            // FINALIZACIÓN ANTICIPADA
            // Si la pasada no hizo ningún intercambio, el arreglo ya estaba
            // ordenado y no tiene sentido seguir con más pasadas. Sin este
            // corte, un arreglo ya ordenado recorrería n-1 pasadas vacías.
            // -----------------------------------------------------------------
            if (!huboCambio) {
                break;
            }
        }

        return new Resultado(original, arreglo.clone(),
                estadosPorPasada, cambiosPorPasada,
                comparaciones, intercambios);
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
     * Muestra por la salida indicada el arreglo original, el estado al final de
     * cada pasada, el arreglo ordenado y la verificación del orden.
     *
     * @param resultado resultado devuelto por {@link #ordenar(int[])}
     * @param salida    flujo de salida a utilizar
     */
    public static void imprimirResultado(Resultado resultado, PrintStream salida) {
        int[] original = resultado.getOriginal();
        salida.println("Arreglo original: " + formatear(original));

        int pasadas = resultado.getCantidadPasadas();

        if (pasadas == 0) {
            // Solo puede ocurrir cuando hay menos de dos elementos.
            if (original.length < 2) {
                salida.println(
                        "El arreglo tiene menos de dos elementos: no hacen falta pasadas.");
            }
        } else {
            List<int[]> estados = resultado.getEstadosPorPasada();
            List<Boolean> cambios = resultado.getCambiosPorPasada();
            for (int i = 0; i < estados.size(); i++) {
                String nota = cambios.get(i)
                        ? ""
                        : "  (sin cambios: el arreglo ya estaba ordenado)";
                salida.printf("Pasada %d: %s%s%n",
                        i + 1, formatear(estados.get(i)), nota);
            }
        }

        int[] ordenado = resultado.getOrdenado();
        salida.println("Arreglo ordenado: " + formatear(ordenado));
        salida.println("Verificacion (orden no decreciente): "
                + (estaOrdenadoNoDecreciente(ordenado) ? "OK" : "FALLO"));
        salida.printf("Pasadas: %d | Comparaciones: %d | Intercambios: %d%n",
                pasadas, resultado.getComparaciones(), resultado.getIntercambios());
    }

    // ------------------------------------------------------------------------
    // PROGRAMA PRINCIPAL
    // ------------------------------------------------------------------------

    /**
     * Punto de entrada. Por defecto lee los datos desde la entrada estándar
     * (cantidad de elementos y luego los valores). Si se pasa el argumento
     * {@code pruebas} (o {@code --test}) ejecuta en su lugar la batería de casos
     * de prueba.
     *
     * @param args argumentos de línea de comandos
     */
    public static void main(String[] args) {
        PrintStream salida = System.out;
        boolean modoPruebas = args.length > 0
                && ("pruebas".equalsIgnoreCase(args[0])
                    || "--test".equalsIgnoreCase(args[0]));

        if (modoPruebas) {
            ejecutarCasosDePrueba(salida);
        } else {
            ejecutarInteractivo(new Scanner(System.in), salida);
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
        salida.println("== Ordenamiento por Bubble Sort ==");

        int cantidad = leerCantidad(entrada, salida);
        int[] arreglo = new int[cantidad];
        for (int i = 0; i < cantidad; i++) {
            arreglo[i] = leerEntero(entrada, salida,
                    "Ingrese el elemento " + (i + 1) + " de " + cantidad + ": ");
        }

        salida.println();
        imprimirResultado(ordenar(arreglo), salida);
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
     * Ejecuta los casos de prueba exigidos en el enunciado. Para cada arreglo se
     * verifica: que quede en orden no decreciente, que las pasadas mostradas
     * coincidan con los cambios realmente realizados y que los contadores de
     * comparaciones e intercambios sean los esperados.
     *
     * @param salida flujo de salida
     */
    private static void ejecutarCasosDePrueba(PrintStream salida) {
        salida.println("== Casos de prueba exigidos ==");

        // 1) Arreglo desordenado (caso general).
        //    Pasadas: [2,5,1,6,9], [2,1,5,6,9], [1,2,5,6,9], [1,2,5,6,9].
        //    La ultima pasada no cambia nada y confirma que ya esta ordenado.
        verificarCaso("desordenado",
                new int[] {5, 2, 9, 1, 6},
                new int[][] {
                        {2, 5, 1, 6, 9},
                        {2, 1, 5, 6, 9},
                        {1, 2, 5, 6, 9},
                        {1, 2, 5, 6, 9}
                },
                10, 5, salida);

        // 2) Arreglo ya ordenado (mejor caso): una sola pasada sin cambios.
        verificarCaso("ya ordenado",
                new int[] {1, 2, 3, 4, 5},
                new int[][] {{1, 2, 3, 4, 5}},
                4, 0, salida);

        // 3) Arreglo en orden inverso (peor caso): n-1 pasadas.
        verificarCaso("orden inverso",
                new int[] {5, 4, 3, 2, 1},
                new int[][] {
                        {4, 3, 2, 1, 5},
                        {3, 2, 1, 4, 5},
                        {2, 1, 3, 4, 5},
                        {1, 2, 3, 4, 5}
                },
                10, 10, salida);

        // 4) Arreglo con valores repetidos (verifica estabilidad).
        verificarCaso("con repetidos",
                new int[] {3, 1, 3, 1, 2},
                new int[][] {
                        {1, 3, 1, 2, 3},
                        {1, 1, 2, 3, 3},
                        {1, 1, 2, 3, 3}
                },
                9, 5, salida);

        // 5) Arreglo vacio: no hacen falta pasadas.
        verificarCaso("vacio",
                new int[] {},
                new int[][] {},
                0, 0, salida);

        salida.println();
        salida.println("== Casos limite adicionales ==");

        // Un solo elemento: no hacen falta pasadas.
        verificarCaso("un elemento",
                new int[] {42},
                new int[][] {},
                0, 0, salida);

        // Todos iguales: una pasada sin cambios (no hay desorden).
        verificarCaso("todos iguales",
                new int[] {7, 7, 7},
                new int[][] {{7, 7, 7}},
                2, 0, salida);

        // Valores extremos de int y negativos repetidos.
        verificarCaso("valores extremos",
                new int[] {Integer.MAX_VALUE, Integer.MIN_VALUE, 0, Integer.MIN_VALUE},
                new int[][] {
                        {Integer.MIN_VALUE, 0, Integer.MIN_VALUE, Integer.MAX_VALUE},
                        {Integer.MIN_VALUE, Integer.MIN_VALUE, 0, Integer.MAX_VALUE},
                        {Integer.MIN_VALUE, Integer.MIN_VALUE, 0, Integer.MAX_VALUE}
                },
                6, 4, salida);

        // Entrada null: debe rechazarse con excepcion descriptiva.
        verificarArregloNulo(salida);

        salida.println();
        salida.println("Todas las verificaciones finalizaron. "
                + "Revisa que no aparezca la palabra FALLO.");
    }

    /**
     * Ordena una copia del arreglo de entrada y comprueba: el orden final, la
     * cantidad y el contenido de las pasadas, la coherencia entre pasadas y
     * cambios, y los contadores esperados.
     *
     * @param nombre                etiqueta del caso
     * @param entrada               arreglo original (no se modifica)
     * @param pasadasEsperadas      estado esperado al final de cada pasada
     * @param comparacionesEsperadas comparaciones esperadas
     * @param intercambiosEsperados  intercambios esperados
     * @param salida                flujo de salida
     */
    private static void verificarCaso(String nombre,
                                      int[] entrada,
                                      int[][] pasadasEsperadas,
                                      long comparacionesEsperadas,
                                      long intercambiosEsperados,
                                      PrintStream salida) {
        int[] copia = entrada.clone();
        Resultado resultado = ordenar(copia);

        List<String> errores = new ArrayList<>();

        // El arreglo original mostrado debe coincidir con la entrada.
        if (!java.util.Arrays.equals(resultado.getOriginal(), entrada)) {
            errores.add("el arreglo original no coincide con la entrada");
        }

        // El arreglo final debe estar en orden no decreciente.
        if (!estaOrdenadoNoDecreciente(resultado.getOrdenado())) {
            errores.add("el arreglo final no esta en orden no decreciente");
        }

        // Cantidad de pasadas.
        if (resultado.getCantidadPasadas() != pasadasEsperadas.length) {
            errores.add("se esperaban " + pasadasEsperadas.length
                    + " pasadas y hubo " + resultado.getCantidadPasadas());
        }

        // Contenido de cada pasada.
        List<int[]> estados = resultado.getEstadosPorPasada();
        int comparables = Math.min(estados.size(), pasadasEsperadas.length);
        for (int i = 0; i < comparables; i++) {
            if (!java.util.Arrays.equals(estados.get(i), pasadasEsperadas[i])) {
                errores.add("pasada " + (i + 1) + " = " + formatear(estados.get(i))
                        + ", esperada " + formatear(pasadasEsperadas[i]));
            }
        }

        // Coherencia entre las pasadas mostradas y los cambios realizados: la
        // marca de cambio de cada pasada debe reflejar si el estado cambio, y
        // toda pasada sin cambios debe ser la ultima (se corto por anticipado).
        List<Boolean> cambios = resultado.getCambiosPorPasada();
        for (int i = 0; i < estados.size(); i++) {
            int[] anterior = (i == 0) ? resultado.getOriginal() : estados.get(i - 1);
            boolean cambioReal = !java.util.Arrays.equals(anterior, estados.get(i));
            if (cambioReal != cambios.get(i)) {
                errores.add("pasada " + (i + 1)
                        + ": la marca de cambio no coincide con los cambios reales");
            }
            if (i < estados.size() - 1 && !cambios.get(i)) {
                errores.add("pasada " + (i + 1)
                        + " sin cambios, pero no fue la ultima pasada");
            }
        }

        // Contadores.
        if (resultado.getComparaciones() != comparacionesEsperadas) {
            errores.add("comparaciones = " + resultado.getComparaciones()
                    + ", esperadas " + comparacionesEsperadas);
        }
        if (resultado.getIntercambios() != intercambiosEsperados) {
            errores.add("intercambios = " + resultado.getIntercambios()
                    + ", esperados " + intercambiosEsperados);
        }

        salida.printf("%-16s entrada=%-24s ordenado=%-24s | %s%n",
                nombre,
                formatear(entrada),
                formatear(resultado.getOrdenado()),
                errores.isEmpty() ? "OK" : "FALLO -> " + String.join("; ", errores));
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
 * 2. El arreglo se ordena in-place. El espacio auxiliar es O(1): solo se usan
 *    índices, contadores y una variable temporal para el intercambio.
 *
 * 3. El ciclo externo ejecuta como máximo n-1 pasadas. El ciclo interno recorre
 *    hasta 'n - 1 - pasada', porque al final de cada pasada el mayor del tramo
 *    queda en su posición definitiva al final y ya no hace falta compararlo.
 *
 * 4. La bandera 'huboCambio' habilita la finalización anticipada: si una pasada
 *    no intercambia nada, el arreglo ya está ordenado y el bucle se corta (mejor
 *    caso O(n)).
 *
 * 5. Se guarda una foto del arreglo al final de cada pasada y una marca de si
 *    hubo cambios. Con eso, las pruebas comprueban que lo mostrado corresponda
 *    realmente a los cambios efectuados.
 *
 * 6. Los contadores son 'long' para evitar desbordamiento en arreglos grandes,
 *    donde n(n-1)/2 puede superar el rango de 'int'.
 *
 * 7. Casos límite: un arreglo null lanza IllegalArgumentException; los arreglos
 *    vacíos y de un elemento se aceptan y no requieren pasadas.
 * ============================================================================
 */

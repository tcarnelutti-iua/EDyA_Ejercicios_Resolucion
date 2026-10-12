/*
 * ============================================================================
 * PROMPT INICIAL UTILIZADO PARA GENERAR ESTA SOLUCIÓN
 * ============================================================================
 *
 * Quiero implementar en Java un programa que ordene alfabéticamente un arreglo
 * de nombres usando Selection Sort. Usá este arreglo como ejemplo:
 *
 *     String[] nombres = {"Lucia", "Ana", "Pedro", "Juan"};
 *
 * Selection Sort debe recorrer la parte todavía desordenada del arreglo, buscar
 * el nombre que va primero alfabéticamente y colocarlo en la posición actual
 * mediante un intercambio. En cada pasada, una posición queda ordenada.
 *
 * Como los elementos son textos y no números, no se pueden comparar con
 * operadores como '<' o '>'. Usá 'compareToIgnoreCase' para comparar nombres sin
 * distinguir mayúsculas y minúsculas. Explicá que el resultado de
 * 'compareToIgnoreCase' es negativo si el nombre que lo invoca va antes, cero si
 * ambos textos son iguales ignorando mayúsculas y minúsculas, y positivo si va
 * después.
 *
 * Mostrá el arreglo original y el arreglo ordenado. Incluí comentarios que
 * expliquen cómo se busca el menor nombre de cada pasada, cómo funciona la
 * comparación de textos y cuándo se intercambian elementos.
 *
 * Verificá que el resultado esté ordenado alfabéticamente y que conserve todos
 * los nombres originales. Probá también un arreglo ya ordenado, uno en orden
 * inverso, uno vacío, uno de un solo nombre y otro con nombres repetidos.
 * ============================================================================
 */

import java.io.PrintStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

/**
 * Ordenamiento alfabético de un arreglo de nombres ({@code String[]}) mediante
 * el algoritmo <b>Selection Sort</b> (ordenamiento por selección).
 *
 * <p><b>Idea del algoritmo.</b> El arreglo se divide conceptualmente en dos
 * partes: la parte <i>izquierda</i> {@code nombres[0..i-1]}, que ya quedó
 * ordenada, y la parte <i>derecha</i> {@code nombres[i..n-1]}, todavía
 * desordenada. En cada pasada se <b>busca</b> el nombre que va primero
 * alfabéticamente dentro del tramo desordenado y se lo <b>intercambia</b> con el
 * primero de ese tramo ({@code nombres[i]}). Como el menor del tramo pasa a
 * ocupar la posición {@code i}, al terminar cada pasada una posición más queda
 * definitivamente ordenada.</p>
 *
 * <p><b>Comparar textos: {@code compareToIgnoreCase}.</b> Los nombres son
 * {@code String}, así que no se pueden comparar con {@code <} ni {@code >}. Se
 * usa {@code a.compareToIgnoreCase(b)}, que devuelve un número entero según el
 * orden lexicográfico ignorando mayúsculas y minúsculas:</p>
 * <ul>
 *   <li><b>Negativo</b> ({@code < 0}): el nombre {@code a} va <i>antes</i> que
 *       {@code b} (por ejemplo {@code "Ana".compareToIgnoreCase("Juan") < 0}).</li>
 *   <li><b>Cero</b> ({@code == 0}): {@code a} y {@code b} son <i>iguales</i>
 *       ignorando mayúsculas y minúsculas (por ejemplo
 *       {@code "ana".compareToIgnoreCase("ANA") == 0}).</li>
 *   <li><b>Positivo</b> ({@code > 0}): el nombre {@code a} va <i>después</i> que
 *       {@code b} (por ejemplo {@code "Pedro".compareToIgnoreCase("Ana") > 0}).</li>
 * </ul>
 * <p>Como el algoritmo solo necesita saber si el candidato va "antes" que el
 * mínimo provisional, la condición de actualización es
 * {@code candidato.compareToIgnoreCase(minimo) < 0}. El método se invoca sobre
 * el elemento a comparar y recibe como argumento al que se compara, de modo que
 * "negativo" significa "el candidato va primero".</p>
 *
 * <p><b>Intercambio por pasada.</b> A diferencia de Bubble Sort, aquí no se
 * intercambian vecinos: se realiza <b>como máximo un intercambio por pasada</b>
 * (y ninguno si el mínimo ya estaba en la posición {@code i}). En total, a lo
 * sumo {@code n - 1} intercambios.</p>
 *
 * <p><b>Complejidad.</b></p>
 * <ul>
 *   <li>Temporal: {@code O(n²)} — se realizan siempre las mismas
 *       {@code n(n-1)/2} comparaciones, sin importar cómo venga el arreglo.</li>
 *   <li>Espacial auxiliar: {@code O(1)} — solo se usan índices, contadores y una
 *       variable temporal para el intercambio.</li>
 * </ul>
 *
 * <p><b>Estabilidad.</b> Como la comparación es estricta
 * ({@code compareToIgnoreCase(...) < 0}), un candidato que empata (cero) no
 * reemplaza al mínimo provisional ya encontrado; así los nombres repetidos no se
 * reordenan innecesariamente dentro de las posiciones alcanzadas.</p>
 *
 * <p>Esta clase separa el <i>algoritmo</i> ({@link #ordenar(String[])}) de la
 * <i>presentación</i> ({@link #imprimirResultado}), de forma que el resultado
 * pueda probarse de forma automática sin depender de la salida por consola.</p>
 *
 * @author Agente Desarrollador
 */
public final class SelectionSortNombres {

    /** Impide instanciar una clase que solo agrupa métodos estáticos. */
    private SelectionSortNombres() {
    }

    // ------------------------------------------------------------------------
    // TIPOS AUXILIARES
    // ------------------------------------------------------------------------

    /**
     * Resultado de una ejecución del algoritmo, inmutable y de solo lectura.
     *
     * <p>Además del arreglo final ordenado, conserva el arreglo original (para
     * poder mostrarlo), el estado del arreglo al terminar cada pasada y si esa
     * pasada produjo un intercambio. Esto permite mostrar el proceso y, en las
     * pruebas, comprobar que lo mostrado coincida con los cambios realmente
     * efectuados.</p>
     */
    public static final class Resultado {

        private final String[] original;
        private final String[] ordenado;
        private final List<String[]> estadosPorPasada;
        private final List<Boolean> cambiosPorPasada;
        private final long comparaciones;
        private final long intercambios;

        private Resultado(String[] original,
                          String[] ordenado,
                          List<String[]> estadosPorPasada,
                          List<Boolean> cambiosPorPasada,
                          long comparaciones,
                          long intercambios) {
            this.original = original;
            this.ordenado = ordenado;
            this.estadosPorPasada = estadosPorPasada;
            this.cambiosPorPasada = cambiosPorPasada;
            this.comparaciones = comparaciones;
            this.intercambios = intercambios;
        }

        /** @return copia del arreglo tal como estaba antes de ordenar */
        public String[] getOriginal() {
            return original.clone();
        }

        /** @return copia del arreglo ya ordenado alfabéticamente */
        public String[] getOrdenado() {
            return ordenado.clone();
        }

        /** @return copias del estado del arreglo al final de cada pasada */
        public List<String[]> getEstadosPorPasada() {
            List<String[]> copias = new ArrayList<>(estadosPorPasada.size());
            for (String[] estado : estadosPorPasada) {
                copias.add(estado.clone());
            }
            return copias;
        }

        /** @return marca de intercambio (o no) correspondiente a cada pasada */
        public List<Boolean> getCambiosPorPasada() {
            return cambiosPorPasada;
        }

        /** @return cantidad de pasadas efectivamente realizadas */
        public int getCantidadPasadas() {
            return estadosPorPasada.size();
        }

        /** @return cantidad total de comparaciones entre nombres */
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
     * Ordena <b>in-place</b>, alfabéticamente y sin distinguir mayúsculas de
     * minúsculas, el arreglo de nombres recibido aplicando Selection Sort.
     *
     * <p>El arreglo pasado como argumento queda mutado (ordenado) al finalizar;
     * el valor de retorno aporta el original conservado, el estado tras cada
     * pasada, los indicadores de intercambio y los contadores.</p>
     *
     * @param nombres arreglo de nombres a ordenar; no puede ser {@code null} ni
     *                contener elementos {@code null}
     * @return resultado con el detalle del ordenamiento
     * @throws IllegalArgumentException si {@code nombres} es {@code null} o si
     *         contiene algún nombre {@code null}
     */
    public static Resultado ordenar(String[] nombres) {
        // --------------------------------------------------------------------
        // VALIDACIÓN DE ENTRADA
        // Un arreglo null no puede recorrerse. Tampoco se admite un nombre
        // null dentro del arreglo: no tiene sentido alfabético y rompería la
        // comparación. Un arreglo vacío o de un solo nombre SÍ es válido: no
        // requiere ninguna pasada.
        // --------------------------------------------------------------------
        validar(nombres);

        int n = nombres.length;

        // Copia del estado inicial para poder mostrarlo/verificarlo después.
        String[] original = nombres.clone();

        // Historial de los estados y de los intercambios a lo largo de las pasadas.
        List<String[]> estadosPorPasada = new ArrayList<>();
        List<Boolean> cambiosPorPasada = new ArrayList<>();

        // Contadores con 'long' para evitar desbordamiento en arreglos grandes.
        // COMPARACIONES: cada vez que un candidato se compara con el mínimo
        //                provisional del tramo desordenado.
        // INTERCAMBIOS:  solo cuando el mínimo encontrado no estaba ya en la
        //                posición 'i'; es decir, como máximo uno por pasada.
        long comparaciones = 0;
        long intercambios = 0;

        // --------------------------------------------------------------------
        // CICLO EXTERNO: PASADAS (de 0 a n - 2)
        // En la pasada 'i' se coloca el nombre que va primero alfabéticamente
        // dentro del tramo [i, n-1] en la posición 'i'. Como al final de cada
        // pasada una posición más queda ordenada, bastan n - 1 pasadas: el
        // último elemento queda ubicado por descarte.
        // Si n < 2, el límite 'n - 1' es cero o negativo y no hay pasadas.
        // --------------------------------------------------------------------
        for (int i = 0; i < n - 1; i++) {

            // -----------------------------------------------------------------
            // BÚSQUEDA DEL MENOR (alfabéticamente primero) DEL TRAMO DESORDENADO
            // Se asume, inicialmente, que el primero del tramo es el mínimo.
            // Luego se recorre el resto comparando cada candidato con el mínimo
            // provisional y actualizándolo cuando el candidato va ANTES.
            // -----------------------------------------------------------------
            int indiceMinimo = i;

            for (int j = i + 1; j < n; j++) {

                // Cada comparación de un candidato contra el mínimo provisional
                // cuenta como una COMPARACIÓN.
                comparaciones++;

                // COMPARACIÓN DE TEXTOS:
                // 'nombres[j].compareToIgnoreCase(nombres[indiceMinimo])' da:
                //   < 0  -> nombres[j] va antes  -> es el nuevo mínimo
                //   == 0 -> son iguales ignorando mayúsculas/minúsculas
                //   > 0  -> nombres[j] va después -> se conserva el mínimo actual
                // Solo se actualiza con '< 0' (estricto); ante un empate se deja
                // al que ya estaba, lo que evita reordenar repetidos.
                if (nombres[j].compareToIgnoreCase(nombres[indiceMinimo]) < 0) {
                    indiceMinimo = j;
                }
            }

            // -----------------------------------------------------------------
            // INTERCAMBIO ÚNICO POR PASADA
            // Se coloca el mínimo hallado en la posición 'i'. Solo se intercambia
            // si el mínimo no estaba ya allí ('indiceMinimo != i'); si ya estaba
            // en su lugar, no se toca el arreglo y no se cuenta intercambio.
            // -----------------------------------------------------------------
            boolean huboIntercambio = false;
            if (indiceMinimo != i) {
                // Intercambio in-place mediante variable temporal (O(1)).
                String temporal = nombres[i];
                nombres[i] = nombres[indiceMinimo];
                nombres[indiceMinimo] = temporal;

                intercambios++;
                huboIntercambio = true;
            }

            // Al terminar la pasada, la posición 'i' quedó ordenada. Se guarda
            // una foto del estado para poder mostrarlo y verificar coherencia.
            estadosPorPasada.add(nombres.clone());
            cambiosPorPasada.add(huboIntercambio);
        }

        return new Resultado(original, nombres.clone(),
                estadosPorPasada, cambiosPorPasada,
                comparaciones, intercambios);
    }

    /**
     * Valida las precondiciones del arreglo de nombres.
     *
     * @param nombres arreglo a validar
     * @throws IllegalArgumentException si es {@code null} o contiene {@code null}
     */
    private static void validar(String[] nombres) {
        if (nombres == null) {
            throw new IllegalArgumentException("El arreglo de nombres no puede ser null.");
        }
        for (int i = 0; i < nombres.length; i++) {
            if (nombres[i] == null) {
                throw new IllegalArgumentException(
                        "El nombre en la posicion " + i + " no puede ser null.");
            }
        }
    }

    // ------------------------------------------------------------------------
    // VERIFICACIÓN
    // ------------------------------------------------------------------------

    /**
     * Verifica que un arreglo de nombres esté ordenado alfabéticamente sin
     * distinguir mayúsculas y minúsculas, es decir que para todo índice válido
     * {@code nombres[i].compareToIgnoreCase(nombres[i+1]) <= 0}.
     *
     * @param nombres arreglo a verificar
     * @return {@code true} si está ordenado alfabéticamente
     * @throws IllegalArgumentException si {@code nombres} es {@code null}
     */
    public static boolean estaOrdenadoAlfabeticamente(String[] nombres) {
        if (nombres == null) {
            throw new IllegalArgumentException("El arreglo de nombres no puede ser null.");
        }
        for (int i = 0; i < nombres.length - 1; i++) {
            // Si un nombre va DESPUÉS que el siguiente, el arreglo no está ordenado.
            if (nombres[i].compareToIgnoreCase(nombres[i + 1]) > 0) {
                return false;
            }
        }
        return true;
    }

    /**
     * Comprueba que el arreglo ordenado conserve <b>exactamente</b> los mismos
     * nombres que el original (misma cantidad de cada texto, respetando
     * mayúsculas y minúsculas). Es decir, que no se haya perdido, agregado ni
     * alterado ningún nombre durante el ordenamiento.
     *
     * @param original  arreglo de nombres antes de ordenar
     * @param resultado arreglo de nombres después de ordenar
     * @return {@code true} si ambos contienen los mismos nombres con la misma
     *         multiplicidad
     * @throws IllegalArgumentException si alguno de los arreglos es {@code null}
     */
    public static boolean conservaTodosLosNombres(String[] original, String[] resultado) {
        if (original == null || resultado == null) {
            throw new IllegalArgumentException("Los arreglos no pueden ser null.");
        }
        if (original.length != resultado.length) {
            return false;
        }

        // Se cuenta cuántas veces aparece cada nombre exacto en el original y se
        // "descuenta" al recorrer el resultado. Si algún nombre no aparece o ya
        // se agotó, el resultado no conserva los nombres originales.
        Map<String, Integer> conteo = new HashMap<>();
        for (String nombre : original) {
            conteo.merge(nombre, 1, Integer::sum);
        }
        for (String nombre : resultado) {
            Integer restante = conteo.get(nombre);
            if (restante == null || restante == 0) {
                return false;
            }
            conteo.put(nombre, restante - 1);
        }
        return true;
    }

    // ------------------------------------------------------------------------
    // PRESENTACIÓN
    // ------------------------------------------------------------------------

    /**
     * Muestra por la salida indicada el arreglo original, el estado al final de
     * cada pasada, el arreglo ordenado y la verificación del orden y de la
     * conservación de los nombres.
     *
     * @param resultado resultado devuelto por {@link #ordenar(String[])}
     * @param salida    flujo de salida a utilizar
     */
    public static void imprimirResultado(Resultado resultado, PrintStream salida) {
        String[] original = resultado.getOriginal();
        salida.println("Arreglo original: " + formatear(original));

        int pasadas = resultado.getCantidadPasadas();
        if (pasadas == 0) {
            // Solo puede ocurrir cuando hay menos de dos nombres.
            if (original.length < 2) {
                salida.println(
                        "El arreglo tiene menos de dos nombres: no hacen falta pasadas.");
            }
        } else {
            List<String[]> estados = resultado.getEstadosPorPasada();
            List<Boolean> cambios = resultado.getCambiosPorPasada();
            for (int i = 0; i < estados.size(); i++) {
                String nota = cambios.get(i)
                        ? ""
                        : "  (sin intercambio: el minimo ya estaba en su lugar)";
                salida.printf("Pasada %d: %s%s%n",
                        i + 1, formatear(estados.get(i)), nota);
            }
        }

        String[] ordenado = resultado.getOrdenado();
        salida.println("Arreglo ordenado: " + formatear(ordenado));
        salida.println("Verificacion (orden alfabetico, ignorando mayusculas): "
                + (estaOrdenadoAlfabeticamente(ordenado) ? "OK" : "FALLO"));
        salida.println("Verificacion (conserva todos los nombres originales): "
                + (conservaTodosLosNombres(original, ordenado) ? "OK" : "FALLO"));
        salida.printf("Pasadas: %d | Comparaciones: %d | Intercambios: %d%n",
                pasadas, resultado.getComparaciones(), resultado.getIntercambios());
    }

    // ------------------------------------------------------------------------
    // PROGRAMA PRINCIPAL
    // ------------------------------------------------------------------------

    /**
     * Punto de entrada. Por defecto ejecuta la batería de casos de prueba (que
     * incluye el ejemplo principal del enunciado). Con el argumento
     * {@code interactivo} (o {@code -i}) lee nombres por consola y muestra el
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
     * Flujo interactivo: solicita la cantidad de nombres y los valores, ordena y
     * muestra el proceso.
     *
     * @param entrada lector de la entrada estándar
     * @param salida  flujo de salida
     */
    private static void ejecutarInteractivo(Scanner entrada, PrintStream salida) {
        salida.println("== Ordenamiento alfabetico por Selection Sort ==");

        int cantidad = leerCantidad(entrada, salida);
        String[] nombres = new String[cantidad];
        for (int i = 0; i < cantidad; i++) {
            nombres[i] = leerNombre(entrada, salida,
                    "Ingrese el nombre " + (i + 1) + " de " + cantidad + ": ");
        }

        salida.println();
        Resultado resultado = ordenar(nombres);
        imprimirResultado(resultado, salida);
        salida.println("Estado: "
                + (estaOrdenadoAlfabeticamente(resultado.getOrdenado()) ? "OK" : "FALLO"));
    }

    /**
     * Lee una cantidad de nombres, rechazando valores negativos.
     *
     * @param entrada lector de entrada
     * @param salida  flujo de salida
     * @return cantidad no negativa ingresada por el usuario
     */
    private static int leerCantidad(Scanner entrada, PrintStream salida) {
        while (true) {
            int cantidad = leerEntero(entrada, salida,
                    "Ingrese la cantidad de nombres: ");
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

    /**
     * Lee un nombre no vacío, repitiendo la solicitud mientras la entrada esté
     * formada solo por espacios.
     *
     * @param entrada lector de entrada
     * @param salida  flujo de salida
     * @param mensaje texto a mostrar como solicitud
     * @return nombre ingresado por el usuario
     */
    private static String leerNombre(Scanner entrada, PrintStream salida, String mensaje) {
        while (true) {
            salida.print(mensaje);
            salida.flush();
            if (!entrada.hasNextLine()) {
                throw new IllegalStateException("No se recibio entrada por consola.");
            }
            String linea = entrada.nextLine().trim();
            if (linea.isEmpty()) {
                salida.println("El nombre no puede estar vacio. Intente nuevamente.");
                continue;
            }
            return linea;
        }
    }

    // ------------------------------------------------------------------------
    // CASOS DE PRUEBA
    // ------------------------------------------------------------------------

    /**
     * Ejecuta los casos de prueba exigidos en el enunciado. Para cada arreglo se
     * verifica: el ejemplo principal, un arreglo ya ordenado, uno en orden
     * inverso, uno vacío, uno de un solo nombre y uno con nombres repetidos;
     * además, algunos casos límite. En cada caso se comprueba el orden final, la
     * conservación de todos los nombres, la coherencia del historial de pasadas
     * y los contadores esperados.
     *
     * @param salida flujo de salida
     */
    private static void ejecutarCasosDePrueba(PrintStream salida) {
        salida.println("== Casos de prueba exigidos ==");

        // 1) EJEMPLO PRINCIPAL del enunciado (desordenado).
        //    Orden alfabetico esperado: Ana, Juan, Lucia, Pedro.
        //    6 = n(n-1)/2 comparaciones; 3 intercambios (uno por pasada).
        verificarCaso("ejemplo principal",
                new String[] {"Lucia", "Ana", "Pedro", "Juan"},
                new String[][] {
                        {"Ana", "Lucia", "Pedro", "Juan"},
                        {"Ana", "Juan", "Pedro", "Lucia"},
                        {"Ana", "Juan", "Lucia", "Pedro"}
                },
                new boolean[] {true, true, true},
                6, 3, salida);

        // 2) YA ORDENADO: Selection Sort igual recorre todo (mismas 6
        //    comparaciones) pero no realiza ningun intercambio (0).
        verificarCaso("ya ordenado",
                new String[] {"Ana", "Juan", "Lucia", "Pedro"},
                new String[][] {
                        {"Ana", "Juan", "Lucia", "Pedro"},
                        {"Ana", "Juan", "Lucia", "Pedro"},
                        {"Ana", "Juan", "Lucia", "Pedro"}
                },
                new boolean[] {false, false, false},
                6, 0, salida);

        // 3) ORDEN INVERSO: minimo en el extremo opuesto cada vez. La ultima
        //    pasada no necesita intercambio porque el minimo ya esta ubicado.
        verificarCaso("orden inverso",
                new String[] {"Pedro", "Lucia", "Juan", "Ana"},
                new String[][] {
                        {"Ana", "Lucia", "Juan", "Pedro"},
                        {"Ana", "Juan", "Lucia", "Pedro"},
                        {"Ana", "Juan", "Lucia", "Pedro"}
                },
                new boolean[] {true, true, false},
                6, 2, salida);

        // 4) NOMBRES REPETIDOS: se conservan todas las apariciones.
        //    Orden esperado: Ana, Ana, Juan, Lucia, Lucia.
        //    10 = n(n-1)/2 comparaciones; 3 intercambios.
        verificarCaso("nombres repetidos",
                new String[] {"Lucia", "Ana", "Ana", "Juan", "Lucia"},
                new String[][] {
                        {"Ana", "Lucia", "Ana", "Juan", "Lucia"},
                        {"Ana", "Ana", "Lucia", "Juan", "Lucia"},
                        {"Ana", "Ana", "Juan", "Lucia", "Lucia"},
                        {"Ana", "Ana", "Juan", "Lucia", "Lucia"}
                },
                new boolean[] {true, true, true, false},
                10, 3, salida);

        // 5) ARREGLO VACIO: no hacen falta pasadas.
        verificarCaso("vacio",
                new String[] {},
                new String[][] {},
                new boolean[] {},
                0, 0, salida);

        // 6) UN SOLO NOMBRE: no hacen falta pasadas.
        verificarCaso("un nombre",
                new String[] {"Ana"},
                new String[][] {},
                new boolean[] {},
                0, 0, salida);

        salida.println();
        salida.println("== Casos limite adicionales ==");

        // Mayusculas y minusculas mezcladas: compareToIgnoreCase devuelve 0 para
        // "ANA" vs "ana", por lo que se demuestra que la comparacion ignora el
        // caso. Orden esperado: ANA, ana, lucia, Pedro.
        verificarCaso("mayusculas/minusculas",
                new String[] {"lucia", "ANA", "Pedro", "ana"},
                new String[][] {
                        {"ANA", "lucia", "Pedro", "ana"},
                        {"ANA", "ana", "Pedro", "lucia"},
                        {"ANA", "ana", "lucia", "Pedro"}
                },
                new boolean[] {true, true, true},
                6, 3, salida);

        // Nombres de una sola letra y repetidos extremos: se conservan todos.
        verificarCaso("letras sueltas",
                new String[] {"b", "a", "b", "a"},
                new String[][] {
                        {"a", "b", "b", "a"},
                        {"a", "a", "b", "b"},
                        {"a", "a", "b", "b"}
                },
                new boolean[] {true, true, false},
                6, 2, salida);

        // Entrada null: debe rechazarse con excepcion descriptiva.
        verificarArregloNulo(salida);

        // Nombre null dentro del arreglo: debe rechazarse.
        verificarNombreNulo(salida);

        salida.println();
        salida.println("Todas las verificaciones finalizaron. "
                + "Revisa que no aparezca la palabra FALLO.");
    }

    /**
     * Ordena una copia del arreglo de entrada y comprueba: que el original
     * coincida con la entrada, que el resultado quede ordenado alfabéticamente,
     * que conserve todos los nombres (misma multiplicidad), que el historial de
     * pasadas y de intercambios sea coherente, y que los contadores sean los
     * esperados.
     *
     * @param nombre                etiqueta del caso
     * @param entrada               arreglo original (no se modifica)
     * @param pasadasEsperadas      estado esperado al final de cada pasada
     * @param intercambiosEsperados marca esperada de intercambio por pasada
     * @param comparacionesEsperadas comparaciones esperadas
     * @param intercambiosTotal     total de intercambios esperados
     * @param salida                flujo de salida
     */
    private static void verificarCaso(String nombre,
                                      String[] entrada,
                                      String[][] pasadasEsperadas,
                                      boolean[] intercambiosEsperados,
                                      long comparacionesEsperadas,
                                      long intercambiosTotal,
                                      PrintStream salida) {
        // Se ordena una COPIA para no mutar el arreglo de prueba original.
        String[] copia = entrada.clone();
        Resultado resultado = ordenar(copia);

        List<String> errores = new ArrayList<>();

        // El arreglo original mostrado debe coincidir con la entrada.
        if (!java.util.Arrays.equals(resultado.getOriginal(), entrada)) {
            errores.add("el arreglo original no coincide con la entrada");
        }

        // El arreglo final debe quedar ordenado alfabeticamente.
        if (!estaOrdenadoAlfabeticamente(resultado.getOrdenado())) {
            errores.add("el arreglo final no esta ordenado alfabeticamente");
        }

        // Debe conservar exactamente los mismos nombres que la entrada.
        if (!conservaTodosLosNombres(entrada, resultado.getOrdenado())) {
            errores.add("el arreglo final no conserva todos los nombres originales");
        }

        // Cantidad de pasadas.
        if (resultado.getCantidadPasadas() != pasadasEsperadas.length) {
            errores.add("se esperaban " + pasadasEsperadas.length
                    + " pasadas y hubo " + resultado.getCantidadPasadas());
        }

        // Contenido de cada pasada.
        List<String[]> estados = resultado.getEstadosPorPasada();
        int comparables = Math.min(estados.size(), pasadasEsperadas.length);
        for (int i = 0; i < comparables; i++) {
            if (!java.util.Arrays.equals(estados.get(i), pasadasEsperadas[i])) {
                errores.add("pasada " + (i + 1) + " = " + formatear(estados.get(i))
                        + ", esperada " + formatear(pasadasEsperadas[i]));
            }
        }

        // Coherencia entre las pasadas y los intercambios: la marca de cada
        // pasada debe reflejar si el estado cambio, y una pasada sin intercambio
        // debe ser coherente con el estado (no se exige que sea la ultima).
        List<Boolean> cambios = resultado.getCambiosPorPasada();
        for (int i = 0; i < estados.size(); i++) {
            String[] anterior = (i == 0) ? resultado.getOriginal() : estados.get(i - 1);
            boolean cambioReal = !java.util.Arrays.equals(anterior, estados.get(i));
            if (cambioReal != cambios.get(i)) {
                errores.add("pasada " + (i + 1)
                        + ": la marca de intercambio no coincide con los cambios reales");
            }
        }

        // Marcas de intercambio esperadas.
        if (intercambiosEsperados != null) {
            for (int i = 0; i < Math.min(cambios.size(), intercambiosEsperados.length); i++) {
                if (cambios.get(i) != intercambiosEsperados[i]) {
                    errores.add("pasada " + (i + 1) + ": intercambio esperado "
                            + intercambiosEsperados[i] + " y se obtuvo " + cambios.get(i));
                }
            }
        }

        // Contadores.
        if (resultado.getComparaciones() != comparacionesEsperadas) {
            errores.add("comparaciones = " + resultado.getComparaciones()
                    + ", esperadas " + comparacionesEsperadas);
        }
        if (resultado.getIntercambios() != intercambiosTotal) {
            errores.add("intercambios = " + resultado.getIntercambios()
                    + ", esperados " + intercambiosTotal);
        }

        // Salida del caso.
        salida.printf("%-22s entrada=%-30s ordenado=%-30s | %s%n",
                nombre,
                formatear(entrada),
                formatear(resultado.getOrdenado()),
                errores.isEmpty() ? "OK" : "FALLO -> " + String.join("; ", errores));
    }

    /**
     * Comprueba que {@link #ordenar(String[])} rechace un arreglo {@code null}.
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
     * Comprueba que {@link #ordenar(String[])} rechace un arreglo con un nombre
     * {@code null} intercalado.
     *
     * @param salida flujo de salida
     */
    private static void verificarNombreNulo(PrintStream salida) {
        try {
            ordenar(new String[] {"Ana", null, "Pedro"});
            salida.println("ordenar([Ana, null, Pedro]) -> FALLO "
                    + "(no se lanzo ninguna excepcion)");
        } catch (IllegalArgumentException excepcion) {
            salida.printf("ordenar([Ana, null, Pedro]) -> OK (rechazado: %s)%n",
                    excepcion.getMessage());
        }
    }

    /**
     * Construye una representación legible de un arreglo de nombres para la
     * salida.
     *
     * @param nombres arreglo a representar
     * @return cadena con los elementos entre corchetes (ej.: {@code [Ana, Juan]})
     */
    private static String formatear(String[] nombres) {
        StringBuilder texto = new StringBuilder("[");
        for (int i = 0; i < nombres.length; i++) {
            if (i > 0) {
                texto.append(", ");
            }
            texto.append(nombres[i]);
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
 * 2. Selection Sort sobre textos: como los nombres son String, no se comparan
 *    con '<' ni '>'. Se usa 'a.compareToIgnoreCase(b)', que ignora mayúsculas y
 *    minúsculas y devuelve negativo si 'a' va antes, cero si son iguales y
 *    positivo si 'a' va después. La actualización del mínimo usa '< 0'.
 *
 * 3. El arreglo se ordena in-place. El espacio auxiliar es O(1): solo se usan
 *    índices, contadores y una variable temporal para el intercambio.
 *
 * 4. El ciclo externo ejecuta como máximo n-1 pasadas. En cada pasada se busca el
 *    menor del tramo [i, n-1] y se lo lleva a la posición 'i' con, a lo sumo, un
 *    intercambio. A diferencia de Bubble Sort, nunca hay más de n-1
 *    intercambios, pero siempre se hacen n(n-1)/2 comparaciones.
 *
 * 5. Verificación doble: 'estaOrdenadoAlfabeticamente' comprueba el orden y
 *    'conservaTodosLosNombres' comprueba (con un mapa de conteos) que el
 *    resultado sea una permutación exacta del original, sin perder ni alterar
 *    nombres.
 *
 * 6. Los contadores son 'long' para evitar desbordamiento en arreglos grandes,
 *    donde n(n-1)/2 puede superar el rango de 'int'.
 *
 * 7. Casos límite: un arreglo null o con nombres null lanza
 *    IllegalArgumentException con mensaje descriptivo; los arreglos vacíos y de
 *    un solo nombre se aceptan y no requieren pasadas. Se prueban además el
 *    ejemplo principal, ya ordenado, orden inverso, repetidos y mezcla de
 *    mayúsculas/minúsculas.
 * ============================================================================
 */

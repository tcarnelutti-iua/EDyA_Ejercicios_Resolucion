/*
 * ============================================================================
 * PROMPT INICIAL UTILIZADO PARA GENERAR ESTA SOLUCIÓN
 * ============================================================================
 *
 * Quiero implementar en Java Quicksort para ordenar un arreglo de números
 * enteros de menor a mayor, usando siempre el primer elemento de cada
 * subarreglo como pivote.
 *
 * Aplicá el enfoque de divide y vencerás: elegí el pivote, particioná el
 * subarreglo colocando a su izquierda los valores menores o iguales y a su
 * derecha los valores mayores, y ubicá el pivote en su posición definitiva.
 * Después, ordená recursivamente los subarreglos izquierdo y derecho. El caso
 * base ocurre cuando un subarreglo tiene cero o un elemento, porque ya está
 * ordenado.
 *
 * En cada partición, mostrá el pivote elegido y el contenido de los subarreglos
 * izquierdo y derecho resultantes. Al finalizar, mostrá el arreglo ordenado.
 * Incluí comentarios que expliquen la partición, las llamadas recursivas y cómo
 * cada llamada trabaja sobre subproblemas más pequeños.
 *
 * Analizá la complejidad: en promedio, el tiempo es O(n log n); en el peor caso
 * es O(n²), por ejemplo, con un arreglo ya ordenado cuando siempre se elige el
 * primer elemento como pivote. Indicá que el espacio de la pila recursiva es
 * O(log n) en promedio y puede llegar a O(n) en el peor caso.
 *
 * Probá un arreglo desordenado, uno ya ordenado, uno en orden inverso, uno con
 * valores repetidos y uno vacío. Verificá que el resultado final esté ordenado y
 * que la partición trate correctamente los valores iguales al pivote.
 * ============================================================================
 */

import java.io.PrintStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Scanner;

/**
 * Ordenamiento de un arreglo de enteros mediante <b>Quicksort</b> (ordenamiento
 * rápido), de menor a mayor, eligiendo <b>siempre el primer elemento de cada
 * subarreglo como pivote</b> y trabajando <i>in-place</i>.
 *
 * <p><b>Idea del algoritmo (divide y vencerás).</b> Quicksort resuelve el
 * problema de ordenar un rango {@code [inicio, fin]} en tres pasos:</p>
 * <ol>
 *   <li><b>Dividir.</b> Se elige un <i>pivote</i> (aquí, {@code arreglo[inicio]})
 *       y se <i>particiona</i> el rango: a la izquierda quedan los valores
 *       {@code <= pivote} y a la derecha los valores {@code > pivote}. El pivote
 *       termina en su <b>posición definitiva</b>.</li>
 *   <li><b>Vencer (recursión).</b> Se ordenan recursivamente los subarreglos
 *       izquierdo y derecho. Cada llamada trabaja sobre un subproblema
 *       <b>estrictamente más pequeño</b>, porque el pivote ya está colocado y no
 *       vuelve a participar.</li>
 *   <li><b>Combinar.</b> No hace falta: como la partición deja todo lo menor a
 *       la izquierda y todo lo mayor a la derecha, cuando los subarreglos están
 *       ordenados, el arreglo completo ya está ordenado.</li>
 * </ol>
 *
 * <p><b>Caso base.</b> Si el rango tiene <b>cero o un elemento</b>
 * ({@code inicio >= fin}), ya está ordenado y la recursión se detiene. Este caso
 * es el que garantiza que las llamadas recursivas terminen.</p>
 *
 * <p><b>Particionar con el primer elemento como pivote.</b> Se guarda
 * {@code pivote = arreglo[inicio]}. Con dos índices, {@code i} avanza desde
 * {@code inicio + 1} hacia la derecha saltando los valores {@code <= pivote}, y
 * {@code j} retrocede desde {@code fin} hacia la izquierda saltando los valores
 * {@code > pivote}. Cuando ambos se detienen y todavía {@code i < j}, hay un valor
 * grande "atrapado" a la izquierda y uno pequeño a la derecha: se intercambian y
 * se continúa. Cuando los índices se cruzan ({@code i > j}), {@code j} señala el
 * último valor {@code <= pivote}: se intercambia el pivote con esa posición y el
 * pivote queda ubicado definitivamente. Los valores <b>iguales al pivote</b>
 * quedan a su izquierda (comparación {@code <=}), tal como pide el enunciado.</p>
 *
 * <p><b>Complejidad temporal.</b> La partición de un rango de tamaño {@code m}
 * cuesta {@code O(m)} comparaciones. La eficiencia depende del <i>balance</i> de
 * las particiones:</p>
 * <ul>
 *   <li><b>Promedio: {@code O(n log n)}.</b> Si el pivote cae cerca del centro,
 *       el rango se parte en dos mitades de tamaño similar y la profundidad de la
 *       recursión es {@code log n}, con {@code O(n)} trabajo por nivel.</li>
 *   <li><b>Peor caso: {@code O(n²)}.</b> Si cada partición deja un subarreglo de
 *       tamaño {@code 0} y otro de tamaño {@code m - 1}, se hacen {@code n} niveles
 *       con {@code O(n)} trabajo cada uno. Esto ocurre, por ejemplo, con un arreglo
 *       <b>ya ordenado</b> (o en orden inverso) al elegir siempre el primer
 *       elemento: el pivote es el menor (o el mayor) y la partición es totalmente
 *       desbalanceada.</li>
 * </ul>
 *
 * <p><b>Complejidad espacial (pila recursiva).</b> Quicksort ordena in-place y
 * solo necesita la pila de llamadas: {@code O(log n)} en promedio (recursión
 * balanceada) y hasta {@code O(n)} en el peor caso (recursión en cadena). No se
 * reservan arreglos auxiliares para ordenar; las copias que se guardan son solo
 * para mostrar el proceso.</p>
 *
 * <p><b>Estabilidad.</b> Quicksort no es estable: durante la partición los
 * valores iguales al pivote pueden cambiar de orden relativo.</p>
 *
 * <p>La clase separa el <i>algoritmo</i> ({@link #ordenar(int[])}) de la
 * <i>presentación</i> ({@link #imprimirResultado}), de forma que el resultado
 * pueda probarse automáticamente sin depender de la salida por consola.</p>
 *
 * @author Agente Desarrollador
 */
public final class QuickSort {

    /** Impide instanciar una clase que solo agrupa métodos estáticos. */
    private QuickSort() {
    }

    // ------------------------------------------------------------------------
    // TIPOS AUXILIARES
    // ------------------------------------------------------------------------

    /**
     * Detalle de una única partición. Es inmutable y de solo lectura.
     *
     * <p>Guarda el rango particionado, el pivote elegido, la posición definitiva
     * que alcanzó, el contenido de los subarreglos izquierdo y derecho
     * resultantes, una "foto" del arreglo al terminar la partición, la
     * profundidad de recursión y los contadores de esa partición. Con esto se
     * puede mostrar el proceso y, en las pruebas, comprobar que lo mostrado
     * coincida con los cambios efectuados.</p>
     */
    public static final class Paso {

        private final int inicio;
        private final int fin;
        private final int pivote;
        private final int posicionPivote;
        private final int profundidad;
        private final int[] subarregloParticionado;
        private final int[] subarregloIzquierdo;
        private final int[] subarregloDerecho;
        private final int[] estado;
        private final int comparacionesDeLaParticion;
        private final int intercambiosDeLaParticion;

        private Paso(int inicio,
                     int fin,
                     int pivote,
                     int posicionPivote,
                     int profundidad,
                     int[] subarregloParticionado,
                     int[] subarregloIzquierdo,
                     int[] subarregloDerecho,
                     int[] estado,
                     int comparacionesDeLaParticion,
                     int intercambiosDeLaParticion) {
            this.inicio = inicio;
            this.fin = fin;
            this.pivote = pivote;
            this.posicionPivote = posicionPivote;
            this.profundidad = profundidad;
            this.subarregloParticionado = subarregloParticionado;
            this.subarregloIzquierdo = subarregloIzquierdo;
            this.subarregloDerecho = subarregloDerecho;
            this.estado = estado;
            this.comparacionesDeLaParticion = comparacionesDeLaParticion;
            this.intercambiosDeLaParticion = intercambiosDeLaParticion;
        }

        /** @return índice inicial del rango particionado */
        public int getInicio() {
            return inicio;
        }

        /** @return índice final (inclusive) del rango particionado */
        public int getFin() {
            return fin;
        }

        /** @return valor del pivote elegido (el primer elemento del subarreglo) */
        public int getPivote() {
            return pivote;
        }

        /** @return posición definitiva que alcanzó el pivote */
        public int getPosicionPivote() {
            return posicionPivote;
        }

        /** @return profundidad de recursión de esta partición (la raíz es 0) */
        public int getProfundidad() {
            return profundidad;
        }

        /** @return copia del subarreglo tal como estaba antes de particionar */
        public int[] getSubarregloParticionado() {
            return subarregloParticionado.clone();
        }

        /** @return copia del subarreglo izquierdo resultante (valores &lt;= pivote) */
        public int[] getSubarregloIzquierdo() {
            return subarregloIzquierdo.clone();
        }

        /** @return copia del subarreglo derecho resultante (valores &gt; pivote) */
        public int[] getSubarregloDerecho() {
            return subarregloDerecho.clone();
        }

        /** @return copia del estado del arreglo completo tras la partición */
        public int[] getEstado() {
            return estado.clone();
        }

        /** @return comparaciones realizadas en esta partición */
        public int getComparacionesDeLaParticion() {
            return comparacionesDeLaParticion;
        }

        /** @return intercambios realizados en esta partición */
        public int getIntercambiosDeLaParticion() {
            return intercambiosDeLaParticion;
        }
    }

    /**
     * Resultado de una ejecución del algoritmo, inmutable y de solo lectura.
     *
     * <p>Además del arreglo final ordenado, conserva el detalle de cada partición
     * ({@link Paso}), los contadores totales y la máxima profundidad de recursión
     * alcanzada (útil para razonar sobre el espacio de la pila).</p>
     */
    public static final class Resultado {

        private final int[] original;
        private final int[] ordenado;
        private final List<Paso> particiones;
        private final long comparaciones;
        private final long intercambios;
        private final int profundidadMaxima;

        private Resultado(int[] original,
                          int[] ordenado,
                          List<Paso> particiones,
                          long comparaciones,
                          long intercambios,
                          int profundidadMaxima) {
            this.original = original;
            this.ordenado = ordenado;
            this.particiones = Collections.unmodifiableList(particiones);
            this.comparaciones = comparaciones;
            this.intercambios = intercambios;
            this.profundidadMaxima = profundidadMaxima;
        }

        /** @return copia del arreglo tal como estaba antes de ordenar */
        public int[] getOriginal() {
            return original.clone();
        }

        /** @return copia del arreglo ya ordenado */
        public int[] getOrdenado() {
            return ordenado.clone();
        }

        /** @return detalle de cada partición, en orden de ejecución (preorden) */
        public List<Paso> getParticiones() {
            return particiones;
        }

        /** @return cantidad de particiones realizadas */
        public int getCantidadParticiones() {
            return particiones.size();
        }

        /** @return cantidad total de comparaciones entre valores realizadas */
        public long getComparaciones() {
            return comparaciones;
        }

        /** @return cantidad total de intercambios realizados */
        public long getIntercambios() {
            return intercambios;
        }

        /** @return máxima profundidad de recursión alcanzada */
        public int getProfundidadMaxima() {
            return profundidadMaxima;
        }
    }

    // ------------------------------------------------------------------------
    // ESTADO MUTABLE DEL PROCESO
    // ------------------------------------------------------------------------

    /**
     * Acumulador interno de una ejecución: el arreglo que se está ordenando, el
     * historial de particiones y los contadores. Se usa una clase auxiliar
     * mutable porque los métodos estáticos recursivos no pueden devolver varios
     * valores a la vez de forma cómoda.
     */
    private static final class Contexto {

        private final int[] arreglo;
        private final List<Paso> particiones = new ArrayList<>();
        private long comparaciones = 0;
        private long intercambios = 0;
        private int profundidadMaxima = 0;

        private Contexto(int[] arreglo) {
            this.arreglo = arreglo;
        }
    }

    // ------------------------------------------------------------------------
    // NÚCLEO DEL ALGORITMO
    // ------------------------------------------------------------------------

    /**
     * Ordena <b>in-place</b> (de menor a mayor) el arreglo recibido aplicando
     * Quicksort con pivote igual al primer elemento de cada subarreglo, y
     * devuelve el detalle de cada partición.
     *
     * <p>El arreglo pasado como argumento queda mutado (ordenado) al finalizar;
     * el valor de retorno aporta el original conservado, el detalle de las
     * particiones, los contadores y la profundidad máxima de recursión.</p>
     *
     * @param arreglo arreglo de enteros a ordenar; no puede ser {@code null}
     * @return resultado con el detalle del ordenamiento
     * @throws IllegalArgumentException si {@code arreglo} es {@code null}
     */
    public static Resultado ordenar(int[] arreglo) {
        // --------------------------------------------------------------------
        // VALIDACIÓN DE ENTRADA
        // Un arreglo null no puede recorrerse. Un arreglo vacío o de un solo
        // elemento SÍ es válido: cae en el caso base y no requiere particiones.
        // --------------------------------------------------------------------
        if (arreglo == null) {
            throw new IllegalArgumentException("El arreglo no puede ser null.");
        }

        // Copia del estado inicial para poder mostrarlo/verificarlo después.
        int[] original = arreglo.clone();

        Contexto contexto = new Contexto(arreglo);

        // Lanzamos la recursión sobre el rango completo [0, n-1].
        // Para n == 0 el rango es [0, -1] => caso base inmediato.
        quicksort(contexto, 0, arreglo.length - 1, 0);

        return new Resultado(original, arreglo.clone(), contexto.particiones,
                contexto.comparaciones, contexto.intercambios,
                contexto.profundidadMaxima);
    }

    /**
     * Ordena recursivamente el rango {@code [inicio, fin]} del arreglo aplicando
     * el esquema divide y vencerás.
     *
     * <p><b>Divide:</b> particiona el rango alrededor del pivote (primer
     * elemento). <b>Vence:</b> ordena recursivamente los subarreglos izquierdo y
     * derecho. <b>Caso base:</b> si el rango tiene cero o un elemento
     * ({@code inicio >= fin}), ya está ordenado y no se hace nada.</p>
     *
     * <p>El pivote queda en su posición definitiva tras la partición, por lo que
     * <b>no vuelve a incluirse</b> en ninguna llamada recursiva: los subproblemas
     * son estrictamente más pequeños y la recursión termina.</p>
     *
     * @param contexto    acumulador del proceso
     * @param inicio      índice inicial del rango (inclusive)
     * @param fin         índice final del rango (inclusive)
     * @param profundidad profundidad de recursión (la llamada raíz es 0)
     */
    private static void quicksort(Contexto contexto, int inicio, int fin, int profundidad) {
        // Se registra la máxima profundidad alcanzada para poder razonar sobre
        // el tamaño de la pila recursiva (O(log n) promedio, O(n) peor caso).
        if (profundidad > contexto.profundidadMaxima) {
            contexto.profundidadMaxima = profundidad;
        }

        // --------------------------------------------------------------------
        // CASO BASE: cero o un elemento => el rango ya está ordenado.
        // Nota: 'inicio > fin' ocurre cuando el pivote era el único elemento del
        // subarreglo y uno de los dos lados quedó vacío.
        // --------------------------------------------------------------------
        if (inicio >= fin) {
            return;
        }

        // --------------------------------------------------------------------
        // DIVIDIR: particionar el rango. El pivote queda en su lugar definitivo
        // y se obtiene su índice final 'posicionPivote'.
        // --------------------------------------------------------------------
        int posicionPivote = particionar(contexto, inicio, fin, profundidad);

        // --------------------------------------------------------------------
        // VENCER: ordenar recursivamente los dos subproblemas. Se excluye al
        // pivote (que ya está en su posición) y por eso ambos rangos son más
        // pequeños que el actual.
        // --------------------------------------------------------------------
        quicksort(contexto, inicio, posicionPivote - 1, profundidad + 1);
        quicksort(contexto, posicionPivote + 1, fin, profundidad + 1);

        // COMBINAR: no hay nada que hacer. Al quedar el pivote en su lugar, con
        // ambos lados ordenados el rango completo queda ordenado.
    }

    /**
     * Particiona el rango {@code [inicio, fin]} usando {@code arreglo[inicio]}
     * como pivote.
     *
     * <p>Invariante del bucle principal: en todo momento, los valores
     * {@code [inicio + 1, i - 1]} son {@code <= pivote} y los valores
     * {@code [j + 1, fin]} son {@code > pivote}. Los índices {@code i} y {@code j}
     * se mueven el uno hacia el otro hasta cruzarse. Al cruzar, {@code j} es la
     * posición del último valor {@code <= pivote}, de modo que se coloca allí el
     * pivote y queda en su posición definitiva.</p>
     *
     * <p>Los valores <b>iguales al pivote</b> se tratan como "menores o iguales"
     * y quedan a la izquierda.</p>
     *
     * @param contexto    acumulador del proceso
     * @param inicio      índice inicial del rango (donde está el pivote)
     * @param fin         índice final del rango
     * @param profundidad profundidad de recursión de esta partición
     * @return índice definitivo del pivote
     */
    private static int particionar(Contexto contexto, int inicio, int fin, int profundidad) {
        int[] arreglo = contexto.arreglo;

        // El pivote es siempre el PRIMER elemento del subarreglo.
        int pivote = arreglo[inicio];

        // Foto del subarreglo antes de modificar nada (para la presentación).
        int[] subarregloParticionado = copiarRango(arreglo, inicio, fin);

        // Contadores locales de esta partición (además de los globales).
        int comparacionesParticion = 0;
        int intercambiosParticion = 0;

        // 'i' busca desde la izquierda un valor > pivote.
        // 'j' busca desde la derecha un valor <= pivote.
        int i = inicio + 1;
        int j = fin;

        // --------------------------------------------------------------------
        // BARRIDO CRUZADO
        // --------------------------------------------------------------------
        while (i <= j) {

            // Avanzar 'i' mientras los valores sean menores o iguales al pivote:
            // esos ya están del lado correcto (izquierda).
            while (i <= j) {
                contexto.comparaciones++;
                comparacionesParticion++;
                if (arreglo[i] <= pivote) {
                    i++;
                } else {
                    break;
                }
            }

            // Retroceder 'j' mientras los valores sean mayores al pivote: esos
            // ya están del lado correcto (derecha).
            while (i <= j) {
                contexto.comparaciones++;
                comparacionesParticion++;
                if (arreglo[j] > pivote) {
                    j--;
                } else {
                    break;
                }
            }

            // Si no se cruzaron, 'arreglo[i]' es mayor al pivote y 'arreglo[j]'
            // es menor o igual: están en el lado equivocado, se intercambian.
            if (i < j) {
                intercambiar(arreglo, i, j);
                contexto.intercambios++;
                intercambiosParticion++;
                i++;
                j--;
            }
        }

        // --------------------------------------------------------------------
        // UBICAR EL PIVOTE EN SU POSICIÓN DEFINITIVA
        // Al salir del bucle, 'j' es el último índice con valor <= pivote. Se
        // intercambia el pivote (que estaba en 'inicio') con 'arreglo[j]'. Si
        // 'j == inicio' el pivote ya estaba bien ubicado y no hay nada que hacer.
        // --------------------------------------------------------------------
        if (j != inicio) {
            intercambiar(arreglo, inicio, j);
            contexto.intercambios++;
            intercambiosParticion++;
        }

        // Subarreglos resultantes: izquierda = [inicio, j-1] (<= pivote),
        // derecha = [j+1, fin] (> pivote). Alguno puede quedar vacío.
        int[] subarregloIzquierdo = copiarRango(arreglo, inicio, j - 1);
        int[] subarregloDerecho = copiarRango(arreglo, j + 1, fin);

        // Se registra la partición para poder mostrarla y verificarla.
        contexto.particiones.add(new Paso(inicio, fin, pivote, j, profundidad,
                subarregloParticionado, subarregloIzquierdo, subarregloDerecho,
                arreglo.clone(), comparacionesParticion, intercambiosParticion));

        return j;
    }

    /**
     * Intercambia in-place los elementos de las posiciones {@code i} y {@code j}
     * usando una variable temporal ({@code O(1)} de espacio auxiliar).
     *
     * @param arreglo arreglo sobre el que operar
     * @param i       primera posición
     * @param j       segunda posición
     */
    private static void intercambiar(int[] arreglo, int i, int j) {
        int temporal = arreglo[i];
        arreglo[i] = arreglo[j];
        arreglo[j] = temporal;
    }

    /**
     * Copia el rango cerrado {@code [desde, hasta]} del arreglo. Si el rango es
     * vacío ({@code desde > hasta}) devuelve un arreglo sin elementos.
     *
     * @param arreglo arreglo de origen
     * @param desde   índice inicial (inclusive)
     * @param hasta   índice final (inclusive)
     * @return copia del rango solicitado
     */
    private static int[] copiarRango(int[] arreglo, int desde, int hasta) {
        if (desde > hasta) {
            return new int[0];
        }
        int longitud = hasta - desde + 1;
        int[] copia = new int[longitud];
        System.arraycopy(arreglo, desde, copia, 0, longitud);
        return copia;
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
     * Muestra por la salida indicada el arreglo original, el detalle de cada
     * partición (pivote, posición definitiva y subarreglos izquierdo y derecho
     * resultantes), el arreglo ordenado, la verificación del orden y los
     * contadores totales junto con la profundidad máxima de recursión.
     *
     * @param resultado resultado devuelto por {@link #ordenar(int[])}
     * @param salida    flujo de salida a utilizar
     */
    public static void imprimirResultado(Resultado resultado, PrintStream salida) {
        salida.println("Arreglo original: " + formatear(resultado.getOriginal()));

        List<Paso> particiones = resultado.getParticiones();

        if (particiones.isEmpty()) {
            // Ocurre cuando el arreglo tiene menos de dos elementos.
            salida.println("El arreglo tiene menos de dos elementos: "
                    + "ya esta ordenado (caso base), no hacen falta particiones.");
        } else {
            salida.println();
            salida.println("Particiones (en orden de ejecucion):");
            int numero = 1;
            for (Paso paso : particiones) {
                String sangria = repetir("  ", paso.getProfundidad());
                salida.printf("%sParticion #%d (profundidad %d, indices %d..%d):%n",
                        sangria, numero, paso.getProfundidad(),
                        paso.getInicio(), paso.getFin());
                salida.printf("%s  Subarreglo a particionar: %s%n",
                        sangria, formatear(paso.getSubarregloParticionado()));
                salida.printf("%s  Pivote elegido: %d (primer elemento del subarreglo)%n",
                        sangria, paso.getPivote());
                salida.printf("%s  Posicion final del pivote: %d%n",
                        sangria, paso.getPosicionPivote());
                salida.printf("%s  Subarreglo izquierdo (<= pivote): %s%n",
                        sangria, formatear(paso.getSubarregloIzquierdo()));
                salida.printf("%s  Subarreglo derecho (> pivote): %s%n",
                        sangria, formatear(paso.getSubarregloDerecho()));
                salida.printf("%s  Estado del arreglo: %s  (comparaciones: %d, intercambios: %d)%n",
                        sangria, formatear(paso.getEstado()),
                        paso.getComparacionesDeLaParticion(),
                        paso.getIntercambiosDeLaParticion());
                numero++;
            }
        }

        int[] ordenado = resultado.getOrdenado();
        salida.println();
        salida.println("Arreglo ordenado: " + formatear(ordenado));
        salida.println("Verificacion (orden no decreciente): "
                + (estaOrdenadoNoDecreciente(ordenado) ? "OK" : "FALLO"));
        salida.printf("Particiones: %d | Comparaciones: %d | Intercambios: %d "
                        + "| Profundidad maxima de recursion: %d%n",
                resultado.getCantidadParticiones(), resultado.getComparaciones(),
                resultado.getIntercambios(), resultado.getProfundidadMaxima());
    }

    /**
     * Construye una cadena repitiendo un texto una cantidad de veces (para
     * indentar las particiones según su profundidad de recursión).
     *
     * @param texto  texto a repetir
     * @param veces  cantidad de repeticiones
     * @return el texto repetido
     */
    private static String repetir(String texto, int veces) {
        StringBuilder resultado = new StringBuilder();
        for (int i = 0; i < veces; i++) {
            resultado.append(texto);
        }
        return resultado.toString();
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
        salida.println("== Ordenamiento por Quicksort (pivote = primer elemento) ==");

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
     * Ejecuta los casos de prueba exigidos en el enunciado: un arreglo
     * desordenado (con el proceso completo), uno ya ordenado, uno en orden
     * inverso, uno con valores repetidos y uno vacío, más casos límite (un
     * elemento, dos elementos, todos iguales, valores extremos y entrada
     * {@code null}).
     *
     * <p>Para cada caso se verifica: que el original se conserve, que el
     * resultado coincida exactamente con {@link Arrays#sort(int[])} (verdad de
     * referencia), que cada partición haya ubicado correctamente el pivote y haya
     * separado los valores {@code <= pivote} de los {@code > pivote}, que los
     * subarreglos mostrados coincidan con el estado real y que la suma de los
     * contadores por partición coincida con los totales.</p>
     *
     * @param salida flujo de salida
     */
    private static void ejecutarCasosDePrueba(PrintStream salida) {
        salida.println("== Casos de prueba exigidos ==");

        // 1) DESORDENADO (ejemplo principal). n = 8. Se muestra el proceso
        //    completo: pivote, posición definitiva y subarreglos resultantes.
        verificarCaso("desordenado (ejemplo principal)",
                new int[] {8, 3, 7, 4, 2, 6, 5, 1},
                true, salida);

        // 2) YA ORDENADO (peor caso con pivote = primer elemento): el pivote es
        //    el menor de cada rango, la partición es totalmente desbalanceada y
        //    se hacen n-1 particiones en cadena (O(n²)).
        verificarCaso("ya ordenado (peor caso)",
                new int[] {1, 2, 3, 4, 5, 6, 7, 8},
                false, salida);

        // 3) ORDEN INVERSO (peor caso): el pivote es el mayor de cada rango, la
        //    partición vuelve a ser desbalanceada (O(n²)).
        verificarCaso("orden inverso (peor caso)",
                new int[] {8, 7, 6, 5, 4, 3, 2, 1},
                false, salida);

        // 4) VALORES REPETIDOS: comprueba que los iguales al pivote queden a la
        //    izquierda (comparación <=). El resultado debe coincidir con
        //    Arrays.sort (Quicksort no es estable).
        verificarCaso("valores repetidos",
                new int[] {5, 1, 5, 3, 3, 1, 5, 3},
                false, salida);

        // 5) VACÍO: caso base trivial, sin particiones.
        verificarCaso("vacio",
                new int[] {},
                false, salida);

        salida.println();
        salida.println("== Casos limite adicionales ==");

        // 6) Un solo elemento: caso base, sin particiones.
        verificarCaso("un elemento",
                new int[] {42},
                false, salida);

        // 7) Dos elementos desordenados: una única partición.
        verificarCaso("dos elementos",
                new int[] {2, 1},
                false, salida);

        // 8) Todos iguales: todos van a un lado (<= pivote); caso que evidencia
        //    la degradación con muchos repetidos.
        verificarCaso("todos iguales",
                new int[] {7, 7, 7, 7},
                false, salida);

        // 9) Valores extremos de int y negativos repetidos.
        verificarCaso("valores extremos",
                new int[] {Integer.MAX_VALUE, Integer.MIN_VALUE, 0, Integer.MIN_VALUE},
                false, salida);

        // 10) Entrada null: debe rechazarse con excepción descriptiva.
        verificarArregloNulo(salida);

        salida.println();
        salida.println("Todas las verificaciones finalizaron. "
                + "Revisa que no aparezca la palabra FALLO.");
    }

    /**
     * Ordena una copia del arreglo de entrada y comprueba la corrección del
     * resultado y de cada partición registrada.
     *
     * @param nombre         etiqueta del caso
     * @param entrada        arreglo original (no se modifica)
     * @param mostrarDetalle si {@code true}, imprime el proceso completo
     * @param salida         flujo de salida
     */
    private static void verificarCaso(String nombre,
                                      int[] entrada,
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

        // Se valida cada partición registrada.
        List<Paso> particiones = resultado.getParticiones();
        for (int indice = 0; indice < particiones.size(); indice++) {
            validarParticion(particiones.get(indice), indice + 1, errores);
        }

        // Consistencia de contadores: la suma por partición debe dar el total.
        long comparacionesPorParticion = 0;
        long intercambiosPorParticion = 0;
        for (Paso paso : particiones) {
            comparacionesPorParticion += paso.getComparacionesDeLaParticion();
            intercambiosPorParticion += paso.getIntercambiosDeLaParticion();
        }
        if (comparacionesPorParticion != resultado.getComparaciones()) {
            errores.add("la suma de comparaciones por particion no coincide con el total");
        }
        if (intercambiosPorParticion != resultado.getIntercambios()) {
            errores.add("la suma de intercambios por particion no coincide con el total");
        }

        // Salida del caso.
        salida.println("Caso: " + nombre + " -> " + formatear(entrada));
        if (mostrarDetalle) {
            imprimirResultado(resultado, salida);
        } else {
            salida.printf("  Ordenado: %s | Particiones: %d | Comparaciones: %d"
                            + " | Intercambios: %d | Profundidad maxima: %d%n",
                    formatear(resultado.getOrdenado()),
                    resultado.getCantidadParticiones(),
                    resultado.getComparaciones(),
                    resultado.getIntercambios(),
                    resultado.getProfundidadMaxima());
        }
        salida.printf("  Estado: %s%n",
                errores.isEmpty() ? "OK" : "FALLO -> " + String.join("; ", errores));
        salida.println();
    }

    /**
     * Verifica la coherencia de una partición registrada contra el estado del
     * arreglo en ese momento.
     *
     * <p>Comprueba que: el pivote esté en su posición definitiva, que todos los
     * valores del subarreglo izquierdo sean {@code <= pivote}, que todos los del
     * subarreglo derecho sean {@code > pivote}, y que ambos subarreglos coincidan
     * exactamente con los rangos correspondientes del estado guardado.</p>
     *
     * @param paso    partición a validar
     * @param numero  número de partición (para el mensaje de error)
     * @param errores lista de errores a la que agregar los hallazgos
     */
    private static void validarParticion(Paso paso, int numero, List<String> errores) {
        int[] estado = paso.getEstado();
        int posicion = paso.getPosicionPivote();
        int pivote = paso.getPivote();

        // El pivote debe estar ubicado en la posición registrada.
        if (estado[posicion] != pivote) {
            errores.add("particion " + numero + ": el pivote no esta en su posicion final");
        }

        int[] izquierdo = paso.getSubarregloIzquierdo();
        for (int valor : izquierdo) {
            if (valor > pivote) {
                errores.add("particion " + numero
                        + ": el subarreglo izquierdo tiene un valor mayor al pivote");
                break;
            }
        }

        int[] derecho = paso.getSubarregloDerecho();
        for (int valor : derecho) {
            if (valor <= pivote) {
                errores.add("particion " + numero
                        + ": el subarreglo derecho tiene un valor menor o igual al pivote");
                break;
            }
        }

        // Los subarreglos mostrados deben coincidir con el estado real.
        int[] izquierdoReal = copiarRango(estado, paso.getInicio(), posicion - 1);
        int[] derechoReal = copiarRango(estado, posicion + 1, paso.getFin());
        if (!Arrays.equals(izquierdo, izquierdoReal)) {
            errores.add("particion " + numero
                    + ": el subarreglo izquierdo mostrado no coincide con el estado");
        }
        if (!Arrays.equals(derecho, derechoReal)) {
            errores.add("particion " + numero
                    + ": el subarreglo derecho mostrado no coincide con el estado");
        }
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
}

/*
 * ============================================================================
 * NOTAS DE IMPLEMENTACIÓN
 * ============================================================================
 * 1. Separación de responsabilidades: 'ordenar' resuelve el algoritmo y devuelve
 *    un objeto 'Resultado' con toda la información; 'imprimirResultado' se
 *    encarga únicamente de la presentación. Esto permite probar el algoritmo sin
 *    depender de la salida por consola.
 *
 * 2. Divide y vencerás: 'quicksort' divide particionando alrededor del pivote,
 *    vence con dos llamadas recursivas sobre los subarreglos izquierdo y derecho
 *    y no necesita combinar, porque el pivote ya queda en su posición definitiva.
 *
 * 3. Pivote = primer elemento. La partición avanza 'i' desde inicio+1 saltando
 *    los valores <= pivote y retrocede 'j' desde fin saltando los valores >
 *    pivote. Cuando se cruzan, 'j' es el último valor <= pivote y allí se ubica
 *    el pivote. Los valores iguales al pivote quedan a la izquierda.
 *
 * 4. Caso base: 'inicio >= fin' (cero o un elemento). Es lo que garantiza que la
 *    recursión termine, ya que cada llamada excluye el pivote y trabaja sobre un
 *    subproblema estrictamente más pequeño.
 *
 * 5. In-place y espacio auxiliar: el ordenamiento ocurre dentro del propio
 *    arreglo; solo se usa una variable temporal para los intercambios y la pila
 *    de recursión. Las copias de subarreglos son solo para mostrar/verificar.
 *
 * 6. Complejidad temporal: O(n log n) en promedio (particiones balanceadas) y
 *    O(n²) en el peor caso (particiones desbalanceadas). Un arreglo ya ordenado o
 *    en orden inverso, con pivote = primer elemento, cae en el peor caso.
 *
 * 7. Complejidad espacial: O(log n) en promedio y O(n) en el peor caso, por la
 *    pila de recursión. Se reporta la 'profundidadMaxima' alcanzada para poder
 *    contrastarlo con los casos de prueba.
 *
 * 8. Estabilidad: Quicksort NO es estable; el resultado sigue siendo correcto
 *    (coincide con Arrays.sort), solo cambia el orden relativo de los repetidos.
 *
 * 9. Casos límite cubiertos: arreglo null (IllegalArgumentException), vacío, un
 *    elemento, dos elementos, desordenado, ya ordenado, orden inverso, repetidos,
 *    todos iguales y valores extremos de int.
 * ============================================================================
 */

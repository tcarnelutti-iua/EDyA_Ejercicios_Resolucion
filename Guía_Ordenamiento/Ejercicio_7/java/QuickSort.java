/*
 * ============================================================================
 * PROMPT INICIAL UTILIZADO PARA GENERAR ESTA SOLUCIÓN
 * ============================================================================
 *
 * Quiero implementar en Java Quicksort y analizar su peor caso usando un
 * arreglo ya ordenado de menor a mayor:
 *
 *     int[] numeros = {1, 2, 3, 4, 5, 6, 7};
 *
 * Elegí siempre el primer elemento de cada subarreglo como pivote. Después de
 * particionar, llamá recursivamente a Quicksort para ordenar las partes
 * izquierda y derecha. Incluí comentarios que expliquen el caso base y cómo la
 * partición genera subproblemas.
 *
 * El programa debe mostrar el arreglo original, cada pivote elegido y los
 * subarreglos resultantes. Al final, debe mostrar el arreglo ordenado y la
 * cantidad total de llamadas recursivas realizadas. Contá cada llamada que el
 * método se hace a sí mismo, incluso si recibe un subarreglo vacío o de un
 * elemento; no cuentes la llamada inicial desde `main`.
 *
 * Explicá por qué, en un arreglo ya ordenado, el primer elemento como pivote
 * puede producir particiones desequilibradas: una queda vacía y la otra
 * contiene los elementos restantes. Así, la profundidad de la recursión crece
 * linealmente y el tiempo puede llegar a O(n²), con espacio de pila O(n) en el
 * peor caso.
 *
 * Verificá que el arreglo termine ordenado y que el contador coincida con las
 * llamadas recursivas efectivamente realizadas.
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
 * <p>Esta variante está enfocada en el <b>análisis del peor caso</b>: se usa un
 * arreglo ya ordenado ({@code {1, 2, 3, 4, 5, 6, 7}}) para evidenciar que, al
 * tomar el primer elemento como pivote, las particiones quedan totalmente
 * desbalanceadas y la recursión se convierte en una cadena de profundidad
 * {@code n}. Además, se cuenta con precisión cuántas veces el método
 * {@code quicksort} se llama a sí mismo (incluidas las llamadas sobre
 * subarreglos vacíos o de un elemento, que caen en el caso base).</p>
 *
 * <p><b>Idea del algoritmo (divide y vencerás).</b> Quicksort resuelve el
 * problema de ordenar un rango {@code [inicio, fin]} en dos pasos:</p>
 * <ol>
 *   <li><b>Dividir.</b> Se elige un <i>pivote</i> (aquí, {@code arreglo[inicio]})
 *       y se <i>particiona</i> el rango: a la izquierda quedan los valores
 *       {@code <= pivote} y a la derecha los valores {@code > pivote}. El pivote
 *       termina en su <b>posición definitiva</b>.</li>
 *   <li><b>Vencer (recursión).</b> Se ordenan recursivamente los subarreglos
 *       izquierdo y derecho. Cada llamada trabaja sobre un subproblema
 *       <b>estrictamente más pequeño</b>, porque el pivote ya está colocado y no
 *       vuelve a participar. No hay paso de "combinar": cuando ambos lados están
 *       ordenados, el rango completo ya lo está.</li>
 * </ol>
 *
 * <p><b>Caso base.</b> Si el rango tiene <b>cero o un elemento</b>
 * ({@code inicio >= fin}), ya está ordenado y la recursión se detiene. Este caso
 * es el que garantiza que las llamadas recursivas terminen.</p>
 *
 * <p><b>Cómo la partición genera los subproblemas.</b> Después de particionar,
 * el pivote queda en su índice definitivo {@code p}. Los dos subproblemas son
 * los rangos {@code [inicio, p-1]} (valores {@code <= pivote}) y
 * {@code [p+1, fin]} (valores {@code > pivote}). Ambos son <b>estrictamente más
 * chicos</b> que el rango actual y pueden quedar <b>vacíos</b>; cuando eso pasa,
 * la llamada recursiva correspondiente cae directamente en el caso base.</p>
 *
 * <p><b>Particionar con el primer elemento como pivote.</b> Se guarda
 * {@code pivote = arreglo[inicio]}. Con dos índices, {@code i} avanza desde
 * {@code inicio + 1} hacia la derecha saltando los valores {@code <= pivote}, y
 * {@code j} retrocede desde {@code fin} hacia la izquierda saltando los valores
 * {@code > pivote}. Cuando ambos se detienen y todavía {@code i < j}, se
 * intercambian y se continúa. Cuando los índices se cruzan ({@code i > j}),
 * {@code j} señala el último valor {@code <= pivote}: se intercambia el pivote
 * con esa posición y queda ubicado definitivamente.</p>
 *
 * <p><b>Por qué un arreglo ya ordenado produce particiones desequilibradas.</b>
 * Si el arreglo ya está ordenado de menor a mayor, el primer elemento del rango
 * es el <b>menor</b> de todos. Como pivote, entonces, <b>ningún</b> valor queda
 * a su izquierda: el subarreglo izquierdo es <b>vacío</b> y el derecho recibe
 * los {@code m - 1} elementos restantes. Es decir, cada partición sobre un rango
 * de tamaño {@code m} produce subproblemas de tamaños {@code 0} y {@code m - 1}.
 * La recursión deja de parecerse a un árbol balanceado y se vuelve una
 * <b>cadena</b>:</p>
 *
 * <pre>
 *     T(m) = T(m-1) + O(m)   ->   T(n) = (n-1) + (n-2) + ... + 1 = O(n²)
 * </pre>
 *
 * <p>Hay {@code n - 1} particiones encadenadas y cada una recorre su rango en
 * {@code O(m)} comparaciones, por lo que el tiempo total es <b>O(n²)</b>. La
 * profundidad de la recursión crece <b>linealmente</b> ({@code n} niveles) y, en
 * consecuencia, la <b>pila de llamadas ocupa {@code O(n)}</b> en el peor caso
 * (contra {@code O(log n)} cuando las particiones son balanceadas).</p>
 *
 * <p><b>Conteo de llamadas recursivas.</b> Se cuenta cada vez que
 * {@code quicksort} se invoca a sí mismo, <b>incluidas</b> las llamadas sobre
 * rangos vacíos o de un elemento (que retornan de inmediato por el caso base).
 * La <b>llamada inicial desde {@code main}</b> <b>no</b> se cuenta: es la raíz
 * de la recursión, no una auto-invocación. Como cada llamada que efectivamente
 * particiona genera exactamente <b>dos</b> auto-invocaciones, el total de
 * llamadas recursivas coincide con {@code 2 * cantidadDeParticiones}; esa
 * igualdad se usa como verificación cruzada.</p>
 *
 * <p><b>Complejidad.</b> En promedio, {@code O(n log n)}; en el peor caso (como
 * este arreglo ordenado), {@code O(n²)}. El espacio de la pila recursiva es
 * {@code O(log n)} en promedio y {@code O(n)} en el peor caso. Quicksort no es
 * estable.</p>
 *
 * <p>La clase separa el <i>algoritmo</i> ({@link #ordenar(int[])}) de la
 * <i>presentación</i> ({@link #imprimirResultado}), de modo que el resultado
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
     * <p>Guarda el rango particionado, el pivote elegido, su posición
     * definitiva, el contenido de los subarreglos izquierdo y derecho
     * resultantes, una "foto" del arreglo al terminar la partición y la
     * profundidad de recursión. Con esto se puede mostrar el proceso y, en las
     * pruebas, comprobar que lo mostrado coincida con los cambios efectuados.</p>
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
     * ({@link Paso}), los contadores totales, la máxima profundidad de recursión
     * alcanzada y el <b>total de llamadas recursivas</b> (sin contar la llamada
     * inicial desde {@code main}).</p>
     */
    public static final class Resultado {

        private final int[] original;
        private final int[] ordenado;
        private final List<Paso> particiones;
        private final long comparaciones;
        private final long intercambios;
        private final int profundidadMaxima;
        private final long llamadasRecursivas;
        private final long invocacionesTotales;

        private Resultado(int[] original,
                          int[] ordenado,
                          List<Paso> particiones,
                          long comparaciones,
                          long intercambios,
                          int profundidadMaxima,
                          long llamadasRecursivas,
                          long invocacionesTotales) {
            this.original = original;
            this.ordenado = ordenado;
            this.particiones = Collections.unmodifiableList(particiones);
            this.comparaciones = comparaciones;
            this.intercambios = intercambios;
            this.profundidadMaxima = profundidadMaxima;
            this.llamadasRecursivas = llamadasRecursivas;
            this.invocacionesTotales = invocacionesTotales;
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

        /** @return máxima profundidad de recursión alcanzada (raíz = 0) */
        public int getProfundidadMaxima() {
            return profundidadMaxima;
        }

        /**
         * @return total de veces que {@code quicksort} se llamó a sí mismo,
         *         incluidas las llamadas sobre subarreglos vacíos o de un
         *         elemento; no incluye la llamada inicial desde {@code main}
         */
        public long getLlamadasRecursivas() {
            return llamadasRecursivas;
        }

        /**
         * @return total de invocaciones de {@code quicksort}, incluyendo la
         *         llamada inicial (raíz). Debería ser siempre
         *         {@code llamadasRecursivas + 1}
         */
        public long getInvocacionesTotales() {
            return invocacionesTotales;
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

        /**
         * Cuenta las auto-invocaciones de {@code quicksort}: cada llamada que el
         * método hace a sí mismo, incluso sobre subarreglos vacíos o de un
         * elemento. NO incluye la llamada inicial desde {@code main}.
         */
        private long llamadasRecursivas = 0;

        /**
         * Cuenta TODAS las invocaciones de {@code quicksort}, incluida la raíz.
         * Sirve para verificar la consistencia del contador de llamadas
         * recursivas: {@code invocacionesTotales == llamadasRecursivas + 1}.
         */
        private long invocacionesTotales = 0;

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
     * devuelve el detalle de cada partición junto con el conteo de llamadas
     * recursivas.
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

        // --------------------------------------------------------------------
        // LLAMADA INICIAL (RAÍZ de la recursión).
        // Sale desde 'main' con 'contar = false': esta llamada NO se cuenta como
        // llamada recursiva. A partir de aquí, todas las auto-invocaciones sí se
        // cuentan (se pasan con 'contar = true').
        // Para n == 0 el rango es [0, -1] => caso base inmediato.
        // --------------------------------------------------------------------
        quicksort(contexto, 0, arreglo.length - 1, 0, false);

        return new Resultado(original, arreglo.clone(), contexto.particiones,
                contexto.comparaciones, contexto.intercambios,
                contexto.profundidadMaxima, contexto.llamadasRecursivas,
                contexto.invocacionesTotales);
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
     * @param contar      {@code true} si esta invocación es una auto-invocación
     *                    (se cuenta); {@code false} solo para la llamada raíz
     *                    desde {@code main}
     */
    private static void quicksort(Contexto contexto, int inicio, int fin,
                                  int profundidad, boolean contar) {
        // Se cuentan TODAS las invocaciones (para poder verificar la
        // consistencia), pero solo las auto-invocaciones cuentan como
        // "llamadas recursivas".
        contexto.invocacionesTotales++;
        if (contar) {
            // Se cuenta la llamada ANTES de evaluar el caso base: el enunciado
            // pide contar cada llamada del método a sí mismo, incluso cuando
            // recibe un subarreglo vacío o de un elemento.
            contexto.llamadasRecursivas++;
        }

        // Se registra la máxima profundidad alcanzada para razonar sobre el
        // tamaño de la pila recursiva (O(log n) promedio, O(n) peor caso).
        if (profundidad > contexto.profundidadMaxima) {
            contexto.profundidadMaxima = profundidad;
        }

        // --------------------------------------------------------------------
        // CASO BASE: cero o un elemento => el rango ya está ordenado.
        // Nota: 'inicio > fin' ocurre cuando el pivote era el único elemento del
        // subarreglo y uno de los dos lados quedó vacío. Estas llamadas igual se
        // cuentan si fueron auto-invocaciones.
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
        // VENCER: ordenar recursivamente los dos subproblemas generados por la
        // partición. Se excluye al pivote (que ya está en su posición), por lo
        // que ambos rangos son estrictamente más pequeños que el actual:
        //   - izquierdo: [inicio, posicionPivote - 1]  (valores <= pivote)
        //   - derecho:   [posicionPivote + 1, fin]     (valores > pivote)
        // Cada una de estas dos llamadas se cuenta como llamada recursiva,
        // aunque el rango esté vacío (caso base inmediato).
        // --------------------------------------------------------------------
        quicksort(contexto, inicio, posicionPivote - 1, profundidad + 1, true);
        quicksort(contexto, posicionPivote + 1, fin, profundidad + 1, true);

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
        // En un arreglo ya ordenado SIEMPRE ocurre 'j == inicio', por lo que la
        // partición deja el lado izquierdo vacío.
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
     * resultantes), el arreglo ordenado, la verificación del orden, los
     * contadores totales y la cantidad de llamadas recursivas.
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
        salida.printf("Llamadas recursivas (sin contar la inicial desde main): %d%n",
                resultado.getLlamadasRecursivas());
    }

    /**
     * Explica, con los datos reales de una ejecución, por qué un arreglo ya
     * ordenado con pivote = primer elemento produce el peor caso.
     *
     * @param entrada   arreglo original
     * @param resultado resultado de ordenar una copia de {@code entrada}
     * @param salida    flujo de salida
     */
    private static void imprimirAnalisisPeorCaso(int[] entrada,
                                                 Resultado resultado,
                                                 PrintStream salida) {
        int n = entrada.length;

        salida.println();
        salida.println("== Analisis del peor caso (arreglo ya ordenado) ==");
        salida.println("Como el arreglo ya esta ordenado de menor a mayor, el primer");
        salida.println("elemento de cada subarreglo es el MENOR. Como se usa como pivote,");
        salida.println("ningun valor queda a su izquierda: el subarreglo izquierdo es vacio");
        salida.println("y el derecho recibe los elementos restantes. Cada particion de un");
        salida.println("rango de tamano m produce entonces subproblemas de tamanos 0 y m-1.");
        salida.println();
        salida.printf("n = %d%n", n);
        salida.printf("Particiones (niveles de la cadena): %d%n",
                resultado.getCantidadParticiones());
        salida.printf("Profundidad maxima de recursion (pila): %d%n",
                resultado.getProfundidadMaxima());
        salida.printf("Llamadas recursivas totales: %d%n",
                resultado.getLlamadasRecursivas());
        salida.println();
        salida.println("Tiempo: las particiones encadenadas cuestan (n-1)+(n-2)+...+1");
        salida.println("= n(n-1)/2, es decir O(n^2) (peor caso).");
        salida.println("Espacio: la pila de recursion guarda n marcos, es decir O(n) (peor caso).");
    }

    /**
     * Construye una cadena repitiendo un texto una cantidad de veces (para
     * indentar las particiones según su profundidad de recursión).
     *
     * @param texto texto a repetir
     * @param veces cantidad de repeticiones
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
     * Punto de entrada. Por defecto ejecuta el ejemplo del peor caso
     * ({@code {1, 2, 3, 4, 5, 6, 7}}) con su análisis y la batería de casos de
     * prueba. Con el argumento {@code interactivo} (o {@code -i}) lee un arreglo
     * por consola y muestra el ordenamiento paso a paso.
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
            ejecutarEjemploPeorCaso(salida);
            ejecutarCasosDePrueba(salida);
        }
    }

    /**
     * Ejecuta el ejemplo principal del enunciado: un arreglo ya ordenado
     * {@code {1, 2, 3, 4, 5, 6, 7}}. Muestra el proceso partición por partición,
     * el resultado y el análisis del peor caso, y verifica que el arreglo termine
     * ordenado y que el contador de llamadas recursivas sea correcto.
     *
     * @param salida flujo de salida
     */
    private static void ejecutarEjemploPeorCaso(PrintStream salida) {
        // Arreglo del enunciado: ya ordenado de menor a mayor (peor caso).
        int[] numeros = {1, 2, 3, 4, 5, 6, 7};

        salida.println("== Quicksort: peor caso con arreglo ya ordenado ==");
        salida.println();

        Resultado resultado = ordenar(numeros.clone());
        imprimirResultado(resultado, salida);
        imprimirAnalisisPeorCaso(numeros, resultado, salida);

        // --------------------------------------------------------------------
        // VERIFICACIONES
        // 1) El arreglo final debe estar ordenado (y coincidir con Arrays.sort).
        // 2) El contador de llamadas recursivas debe coincidir con las llamadas
        //    efectivamente realizadas. Como cada partición genera exactamente 2
        //    auto-invocaciones, el total debe ser 2 * cantidadDeParticiones.
        // --------------------------------------------------------------------
        int[] referencia = numeros.clone();
        Arrays.sort(referencia);

        boolean ordenOk = estaOrdenadoNoDecreciente(resultado.getOrdenado())
                && Arrays.equals(resultado.getOrdenado(), referencia);

        long llamadasEsperadas = 2L * resultado.getCantidadParticiones();
        boolean contadorOk = resultado.getLlamadasRecursivas() == llamadasEsperadas
                && resultado.getInvocacionesTotales()
                        == resultado.getLlamadasRecursivas() + 1;

        salida.println();
        salida.printf("Verificacion orden final: %s%n", ordenOk ? "OK" : "FALLO");
        salida.printf("Verificacion contador de llamadas recursivas "
                        + "(esperado 2 * particiones = %d): %s%n",
                llamadasEsperadas, contadorOk ? "OK" : "FALLO");
        salida.println();
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
     * Ejecuta casos de prueba adicionales al ejemplo principal: ya ordenado,
     * orden inverso, desordenado, valores repetidos, vacío, un elemento y
     * entrada {@code null}. Para cada caso se verifica que el resultado coincida
     * con {@link Arrays#sort(int[])}, que cada partición ubique bien el pivote y
     * que el contador de llamadas recursivas sea consistente.
     *
     * @param salida flujo de salida
     */
    private static void ejecutarCasosDePrueba(PrintStream salida) {
        salida.println("== Casos de prueba adicionales ==");

        // YA ORDENADO (peor caso): n=8 => 7 particiones => 14 llamadas recursivas.
        verificarCaso("ya ordenado (peor caso)",
                new int[] {1, 2, 3, 4, 5, 6, 7, 8},
                14L, false, salida);

        // ORDEN INVERSO (tambien peor caso: el pivote es el mayor de cada rango).
        verificarCaso("orden inverso (peor caso)",
                new int[] {8, 7, 6, 5, 4, 3, 2, 1},
                null, false, salida);

        // DESORDENADO (particiones mas balanceadas).
        verificarCaso("desordenado",
                new int[] {8, 3, 7, 4, 2, 6, 5, 1},
                null, false, salida);

        // VALORES REPETIDOS (los iguales al pivote quedan a la izquierda).
        verificarCaso("valores repetidos",
                new int[] {5, 1, 5, 3, 3, 1, 5, 3},
                null, false, salida);

        // CASOS LIMITE
        verificarCaso("vacio", new int[] {}, 0L, false, salida);
        verificarCaso("un elemento", new int[] {42}, 0L, false, salida);
        // Dos elementos desordenados: 1 partición => 2 llamadas recursivas
        // (ambas sobre subarreglos de un elemento, o sea caso base).
        verificarCaso("dos elementos", new int[] {2, 1}, 2L, false, salida);
        verificarCaso("valores extremos",
                new int[] {Integer.MAX_VALUE, Integer.MIN_VALUE, 0, Integer.MIN_VALUE},
                null, false, salida);

        // Entrada null: debe rechazarse con excepción descriptiva.
        verificarArregloNulo(salida);

        salida.println();
        salida.println("Todas las verificaciones finalizaron. "
                + "Revisa que no aparezca la palabra FALLO.");
    }

    /**
     * Ordena una copia del arreglo de entrada y comprueba la corrección del
     * resultado, de cada partición registrada y del contador de llamadas
     * recursivas.
     *
     * @param nombre                 etiqueta del caso
     * @param entrada                arreglo original (no se modifica)
     * @param llamadasEsperadas      llamadas recursivas esperadas; {@code null}
     *                               para no verificar un valor exacto
     * @param mostrarDetalle         si {@code true}, imprime el proceso completo
     * @param salida                 flujo de salida
     */
    private static void verificarCaso(String nombre,
                                      int[] entrada,
                                      Long llamadasEsperadas,
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

        // --------------------------------------------------------------------
        // CONTADOR DE LLAMADAS RECURSIVAS
        // Cada llamada que particiona genera exactamente 2 auto-invocaciones, y
        // las llamadas base (vacias o de un elemento) no generan ninguna. Por lo
        // tanto, el total de llamadas recursivas debe ser 2 * particiones.
        // Además, el total de invocaciones debe ser llamadasRecursivas + 1
        // (solo la llamada raiz desde main no se cuenta).
        // --------------------------------------------------------------------
        long llamadasEsperadasEstructural = 2L * resultado.getCantidadParticiones();
        if (resultado.getLlamadasRecursivas() != llamadasEsperadasEstructural) {
            errores.add("llamadas recursivas = " + resultado.getLlamadasRecursivas()
                    + ", esperadas estructuralmente " + llamadasEsperadasEstructural);
        }
        if (resultado.getInvocacionesTotales()
                != resultado.getLlamadasRecursivas() + 1) {
            errores.add("invocaciones totales inconsistentes con las llamadas recursivas");
        }
        if (llamadasEsperadas != null
                && resultado.getLlamadasRecursivas() != llamadasEsperadas) {
            errores.add("llamadas recursivas = " + resultado.getLlamadasRecursivas()
                    + ", esperadas " + llamadasEsperadas);
        }

        // Salida del caso.
        salida.println("Caso: " + nombre + " -> " + formatear(entrada));
        if (mostrarDetalle) {
            imprimirResultado(resultado, salida);
        } else {
            salida.printf("  Ordenado: %s | Particiones: %d | Llamadas recursivas: %d "
                            + "| Comparaciones: %d | Profundidad maxima: %d%n",
                    formatear(resultado.getOrdenado()),
                    resultado.getCantidadParticiones(),
                    resultado.getLlamadasRecursivas(),
                    resultado.getComparaciones(),
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
 *    encarga de la presentación. Esto permite probar el algoritmo sin depender
 *    de la salida por consola.
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
 * 5. Conteo de llamadas recursivas: el parámetro booleano 'contar' distingue la
 *    llamada raíz (desde 'main', contar = false) de las auto-invocaciones
 *    (contar = true). Se incrementa ANTES de evaluar el caso base, de modo que
 *    también se cuentan las llamadas sobre subarreglos vacíos o de un elemento.
 *    Como cada partición genera exactamente 2 auto-invocaciones, se cumple
 *    'llamadasRecursivas == 2 * cantidadDeParticiones', igualdad que se usa como
 *    verificación cruzada. Además se lleva 'invocacionesTotales' para comprobar
 *    'invocacionesTotales == llamadasRecursivas + 1'.
 *
 * 6. Peor caso analizado: con el arreglo ya ordenado {1..7}, el pivote es siempre
 *    el menor, el subarreglo izquierdo queda vacío y el derecho recibe m-1
 *    elementos. Hay n-1 = 6 particiones encadenadas y 2*(n-1) = 12 llamadas
 *    recursivas; el tiempo es O(n²) y la pila O(n).
 *
 * 7. In-place y espacio auxiliar: el ordenamiento ocurre dentro del propio
 *    arreglo; solo se usa una variable temporal para los intercambios y la pila
 *    de recursión. Las copias de subarreglos son solo para mostrar/verificar.
 *
 * 8. Estabilidad: Quicksort NO es estable; el resultado sigue siendo correcto
 *    (coincide con Arrays.sort), solo cambia el orden relativo de los repetidos.
 *
 * 9. Casos límite cubiertos: arreglo null (IllegalArgumentException), vacío, un
 *    elemento, dos elementos, ya ordenado, orden inverso, desordenado, repetidos
 *    y valores extremos de int.
 * ============================================================================
 */

/*
 * ============================================================================
 * PROMPT INICIAL UTILIZADO PARA GENERAR ESTA SOLUCIÓN
 * ============================================================================
 *
 * Quiero implementar en Java MergeSort para ordenar un arreglo de números
 * enteros de menor a mayor y mostrar paso a paso cómo funciona.
 *
 * Usá el enfoque de divide y vencerás:
 *
 *   - Dividir: separá recursivamente el arreglo en dos subarreglos,
 *     aproximadamente del mismo tamaño. Mostrá el contenido de cada parte y su
 *     nivel de recursión.
 *   - Condición de corte: cuando un subarreglo tenga cero o un elemento,
 *     mostralo como caso base; ya está ordenado y no necesita dividirse más.
 *   - Fusionar: combiná los subarreglos ordenados en uno solo, comparando sus
 *     primeros elementos disponibles y tomando el menor en cada paso. Mostrá el
 *     resultado de cada fusión.
 *
 * Explicá claramente que dividir descompone el problema en partes más pequeñas,
 * mientras que fusionar combina las soluciones de esas partes para construir el
 * arreglo ordenado.
 *
 * Incluí comentarios que distingan las llamadas recursivas de la lógica de
 * fusión. El programa debe mostrar el arreglo original y el resultado final.
 * Analizá la complejidad temporal O(n log n) y el espacio auxiliar O(n).
 *
 * Probá un arreglo desordenado, uno ya ordenado, uno en orden inverso, uno con
 * valores repetidos, uno vacío y uno de un solo elemento. Verificá que cada
 * resultado quede ordenado y conserve los valores originales.
 * ============================================================================
 */

import java.io.PrintStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Scanner;

/**
 * Ordenamiento de un arreglo de enteros mediante <b>MergeSort</b>
 * (ordenamiento por mezcla / intercalación), de menor a mayor, siguiendo el
 * paradigma <b>divide y vencerás</b>.
 *
 * <p><b>Idea del algoritmo.</b> Para ordenar un rango {@code [inicio, fin]} se
 * aplican tres pasos:</p>
 * <ol>
 *   <li><b>Dividir.</b> Se parte el rango en dos mitades de tamaño
 *       aproximadamente igual: {@code [inicio, medio]} y {@code [medio + 1, fin]}.
 *       Dividir <b>descompone</b> un problema grande en dos problemas más
 *       pequeños, que son más fáciles de resolver.</li>
 *   <li><b>Vencer (recursión).</b> Se ordenan recursivamente cada una de las dos
 *       mitades. El problema original se reduce a dos subproblemas del mismo
 *       tipo pero de menor tamaño.</li>
 *   <li><b>Combinar (fusionar).</b> Se <b>intercalan</b> los dos subarreglos ya
 *       ordenados en uno solo también ordenado. Fusionar <b>combina las
 *       soluciones</b> de las partes para construir la solución del problema
 *       completo. A diferencia de Quicksort, aquí el trabajo importante ocurre
 *       al <i>combinar</i>: las mitades vuelven ordenadas y hay que unirlas.</li>
 * </ol>
 *
 * <p><b>Condición de corte (caso base).</b> Un subarreglo con <b>cero o un
 * elemento</b> ya está ordenado por definición: no hay nada que dividir ni que
 * fusionar. Cuando la recursión llega a ese punto, se detiene. Este caso base es
 * el que garantiza que las llamadas recursivas terminen, porque cada división
 * reduce estrictamente el tamaño del rango.</p>
 *
 * <p><b>Cómo funciona la fusión.</b> Dados dos subarreglos <b>ya ordenados</b>
 * (izquierdo y derecho), se recorren con dos índices, {@code i} e {@code j}, que
 * apuntan a sus respectivos <b>primeros elementos disponibles</b>. En cada paso
 * se comparan esos dos elementos y se copia el <b>menor</b> al resultado,
 * avanzando el índice correspondiente. Cuando uno de los dos subarreglos se
 * agota, el resto del otro (que ya está ordenado) se copia directamente. Así, la
 * fusión combina las dos soluciones parciales en una sola ordenada en tiempo
 * proporcional al tamaño total del rango.</p>
 *
 * <p><b>Estabilidad.</b> La fusión es <b>estable</b>: ante dos valores iguales
 * se toma primero el del subarreglo izquierdo (se usa {@code aux[j] < aux[i]} y
 * no {@code <=}), por lo que se preserva el orden relativo original de los
 * elementos repetidos.</p>
 *
 * <p><b>Espacio auxiliar.</b> Este MergeSort <i>no</i> ordena in-place: para
 * intercalar necesita un <b>arreglo auxiliar</b> donde copiar el rango antes de
 * fusionarlo. Se reserva <b>una sola vez</b> un arreglo auxiliar de tamaño
 * {@code n} y se reutiliza en todas las fusiones, de modo que el espacio
 * auxiliar es <b>{@code O(n)}</b> (sin contar la pila de recursión, que es
 * {@code O(log n)}).</p>
 *
 * <p><b>Complejidad temporal {@code O(n log n)}.</b> Dividir siempre por la
 * mitad produce un <b>árbol de recursión</b> de {@code log n} niveles. En cada
 * nivel, la suma de los tamaños de todos los rangos es {@code n}, y todas las
 * fusiones de ese nivel juntas recorren esos {@code n} elementos en tiempo
 * {@code O(n)}. Por lo tanto el tiempo total es {@code O(n)} por nivel por
 * {@code O(log n)} niveles = <b>{@code O(n log n)}</b>, tanto en el mejor, el
 * promedio y el peor caso. Es por eso que MergeSort es muy predecible: no tiene
 * el peor caso cuadrático de Quicksort.</p>
 *
 * <p>La clase separa el <i>algoritmo</i> ({@link #ordenar(int[])}) de la
 * <i>presentación</i> ({@link #imprimirResultado}), de modo que el resultado
 * pueda probarse automáticamente sin depender de la salida por consola.</p>
 *
 * @author Agente Desarrollador
 */
public final class MergeSort {

    /** Impide instanciar una clase que solo agrupa métodos estáticos. */
    private MergeSort() {
    }

    // ------------------------------------------------------------------------
    // TIPOS AUXILIARES
    // ------------------------------------------------------------------------

    /**
     * Detalle de una etapa del proceso, inmutable y de solo lectura.
     *
     * <p>Cada instancia describe <b>un evento</b> de la recursión, identificado
     * por su {@link Tipo} y la profundidad (nivel de recursión) a la que ocurrió.
     * Los campos aplicables dependen del tipo:</p>
     * <ul>
     *   <li>{@link Tipo#DIVIDIR}: {@link #getSubarreglo()} (el rango completo),
     *       {@link #getParteIzquierda()} y {@link #getParteDerecha()}.</li>
     *   <li>{@link Tipo#CASO_BASE}: {@link #getSubarreglo()}.</li>
     *   <li>{@link Tipo#FUSIONAR}: {@link #getIzquierda()},
     *       {@link #getDerecha()} (ya ordenados), {@link #getFusionado()} y
     *       {@link #getComparaciones()}.</li>
     * </ul>
     * <p>Los campos no aplicables a un tipo valen {@code null} y sus getters
     * correspondientes devuelven {@code null}.</p>
     */
    public static final class Paso {

        /** Tipo de evento de la recursión. */
        public enum Tipo {
            /** Se parte un rango en dos mitades. */
            DIVIDIR,
            /** Un rango de cero o un elemento: ya está ordenado. */
            CASO_BASE,
            /** Se intercalan dos subarreglos ordenados en uno. */
            FUSIONAR
        }

        private final Tipo tipo;
        private final int profundidad;
        private final int inicio;
        private final int fin;

        // Aplicables a DIVIDIR y CASO_BASE.
        private final int[] subarreglo;

        // Aplicables a DIVIDIR.
        private final int[] parteIzquierda;
        private final int[] parteDerecha;

        // Aplicables a FUSIONAR.
        private final int[] izquierda;
        private final int[] derecha;
        private final int[] fusionado;
        private final int comparaciones;

        private Paso(Tipo tipo,
                     int profundidad,
                     int inicio,
                     int fin,
                     int[] subarreglo,
                     int[] parteIzquierda,
                     int[] parteDerecha,
                     int[] izquierda,
                     int[] derecha,
                     int[] fusionado,
                     int comparaciones) {
            this.tipo = tipo;
            this.profundidad = profundidad;
            this.inicio = inicio;
            this.fin = fin;
            this.subarreglo = subarreglo;
            this.parteIzquierda = parteIzquierda;
            this.parteDerecha = parteDerecha;
            this.izquierda = izquierda;
            this.derecha = derecha;
            this.fusionado = fusionado;
            this.comparaciones = comparaciones;
        }

        /** Crea un evento de <b>división</b> de un rango en dos mitades. */
        private static Paso dividir(int profundidad, int inicio, int fin,
                                    int[] subarreglo, int[] parteIzquierda,
                                    int[] parteDerecha) {
            return new Paso(Tipo.DIVIDIR, profundidad, inicio, fin, subarreglo,
                    parteIzquierda, parteDerecha, null, null, null, 0);
        }

        /** Crea un evento de <b>caso base</b> (rango de cero o un elemento). */
        private static Paso casoBase(int profundidad, int inicio, int fin,
                                     int[] subarreglo) {
            return new Paso(Tipo.CASO_BASE, profundidad, inicio, fin, subarreglo,
                    null, null, null, null, null, 0);
        }

        /** Crea un evento de <b>fusión</b> de dos subarreglos ordenados. */
        private static Paso fusionar(int profundidad, int inicio, int fin,
                                     int[] izquierda, int[] derecha,
                                     int[] fusionado, int comparaciones) {
            return new Paso(Tipo.FUSIONAR, profundidad, inicio, fin, null, null,
                    null, izquierda, derecha, fusionado, comparaciones);
        }

        /** @return tipo de evento (DIVIDIR, CASO_BASE o FUSIONAR) */
        public Tipo getTipo() {
            return tipo;
        }

        /** @return profundidad de recursión del evento (la raíz es 0) */
        public int getProfundidad() {
            return profundidad;
        }

        /** @return índice inicial (inclusive) del rango involucrado */
        public int getInicio() {
            return inicio;
        }

        /** @return índice final (inclusive) del rango involucrado */
        public int getFin() {
            return fin;
        }

        /**
         * @return copia del subarreglo sobre el que se trabaja (DIVIDIR o
         *         CASO_BASE), o {@code null} si el evento es FUSIONAR
         */
        public int[] getSubarreglo() {
            return subarreglo == null ? null : subarreglo.clone();
        }

        /**
         * @return copia de la mitad izquierda del rango (solo DIVIDIR), o
         *         {@code null} para otros tipos
         */
        public int[] getParteIzquierda() {
            return parteIzquierda == null ? null : parteIzquierda.clone();
        }

        /**
         * @return copia de la mitad derecha del rango (solo DIVIDIR), o
         *         {@code null} para otros tipos
         */
        public int[] getParteDerecha() {
            return parteDerecha == null ? null : parteDerecha.clone();
        }

        /**
         * @return copia del subarreglo izquierdo ya ordenado (solo FUSIONAR), o
         *         {@code null} para otros tipos
         */
        public int[] getIzquierda() {
            return izquierda == null ? null : izquierda.clone();
        }

        /**
         * @return copia del subarreglo derecho ya ordenado (solo FUSIONAR), o
         *         {@code null} para otros tipos
         */
        public int[] getDerecha() {
            return derecha == null ? null : derecha.clone();
        }

        /**
         * @return copia del resultado de la fusión (solo FUSIONAR), o
         *         {@code null} para otros tipos
         */
        public int[] getFusionado() {
            return fusionado == null ? null : fusionado.clone();
        }

        /** @return comparaciones realizadas en esta fusión (solo FUSIONAR) */
        public int getComparaciones() {
            return comparaciones;
        }
    }

    /**
     * Resultado de una ejecución del algoritmo, inmutable y de solo lectura.
     *
     * <p>Además del arreglo final ordenado, conserva el arreglo original, la
     * traza completa de eventos ({@link Paso}), los contadores totales, la
     * máxima profundidad de recursión y el número de llamadas recursivas.</p>
     */
    public static final class Resultado {

        private final int[] original;
        private final int[] ordenado;
        private final List<Paso> pasos;
        private final long comparaciones;
        private final int profundidadMaxima;
        private final long llamadasRecursivas;
        private final long invocacionesTotales;

        private Resultado(int[] original,
                          int[] ordenado,
                          List<Paso> pasos,
                          long comparaciones,
                          int profundidadMaxima,
                          long llamadasRecursivas,
                          long invocacionesTotales) {
            this.original = original;
            this.ordenado = ordenado;
            this.pasos = Collections.unmodifiableList(pasos);
            this.comparaciones = comparaciones;
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

        /** @return traza completa de eventos, en orden de ejecución */
        public List<Paso> getPasos() {
            return pasos;
        }

        /** @return cantidad total de comparaciones entre valores realizadas */
        public long getComparaciones() {
            return comparaciones;
        }

        /** @return máxima profundidad de recursión alcanzada (raíz = 0) */
        public int getProfundidadMaxima() {
            return profundidadMaxima;
        }

        /**
         * @return total de veces que {@code mergeSort} se llamó a sí mismo,
         *         incluidas las llamadas que recaen en el caso base; no incluye
         *         la llamada inicial desde {@code main}
         */
        public long getLlamadasRecursivas() {
            return llamadasRecursivas;
        }

        /**
         * @return total de invocaciones de {@code mergeSort}, incluyendo la
         *         llamada inicial (raíz). Debería ser siempre
         *         {@code llamadasRecursivas + 1}
         */
        public long getInvocacionesTotales() {
            return invocacionesTotales;
        }

        /** @return cantidad de eventos de división registrados */
        public int getCantidadDivisiones() {
            return contarPorTipo(Paso.Tipo.DIVIDIR);
        }

        /** @return cantidad de eventos de caso base registrados */
        public int getCantidadCasosBase() {
            return contarPorTipo(Paso.Tipo.CASO_BASE);
        }

        /** @return cantidad de fusiones realizadas */
        public int getCantidadFusiones() {
            return contarPorTipo(Paso.Tipo.FUSIONAR);
        }

        private int contarPorTipo(Paso.Tipo buscado) {
            int total = 0;
            for (Paso paso : pasos) {
                if (paso.getTipo() == buscado) {
                    total++;
                }
            }
            return total;
        }
    }

    // ------------------------------------------------------------------------
    // ESTADO MUTABLE DEL PROCESO
    // ------------------------------------------------------------------------

    /**
     * Acumulador interno de una ejecución: el arreglo que se está ordenando, la
     * traza de eventos y los contadores. Se usa una clase auxiliar mutable
     * porque los métodos recursivos no pueden devolver varios valores a la vez
     * de forma cómoda.
     */
    private static final class Contexto {

        private final int[] arreglo;
        private final List<Paso> pasos = new ArrayList<>();
        private long comparaciones = 0;
        private int profundidadMaxima = 0;
        private long llamadasRecursivas = 0;
        private long invocacionesTotales = 0;

        private Contexto(int[] arreglo) {
            this.arreglo = arreglo;
        }
    }

    // ------------------------------------------------------------------------
    // NÚCLEO DEL ALGORITMO
    // ------------------------------------------------------------------------

    /**
     * Ordena (de menor a mayor) el arreglo recibido aplicando MergeSort y
     * devuelve la traza completa del proceso junto con el arreglo final.
     *
     * <p>El arreglo pasado como argumento queda mutado (ordenado) al finalizar;
     * el valor de retorno aporta el original conservado, la traza de eventos y
     * los contadores.</p>
     *
     * @param arreglo arreglo de enteros a ordenar; no puede ser {@code null}
     * @return resultado con el detalle del ordenamiento
     * @throws IllegalArgumentException si {@code arreglo} es {@code null}
     */
    public static Resultado ordenar(int[] arreglo) {
        // --------------------------------------------------------------------
        // VALIDACIÓN DE ENTRADA
        // Un arreglo null no puede recorrerse. Un arreglo vacío o de un solo
        // elemento SÍ es válido: cae en el caso base.
        // --------------------------------------------------------------------
        if (arreglo == null) {
            throw new IllegalArgumentException("El arreglo no puede ser null.");
        }

        // Copia del estado inicial para poder mostrarlo/verificarlo después.
        int[] original = arreglo.clone();

        // --------------------------------------------------------------------
        // ESPACIO AUXILIAR O(n)
        // La fusión necesita un arreglo auxiliar para intercalar. Se reserva UNA
        // SOLA VEZ (tamaño n) y se reutiliza en todas las fusiones: esa es la
        // fuente del espacio auxiliar O(n) del algoritmo.
        // --------------------------------------------------------------------
        int[] auxiliar = new int[arreglo.length];

        Contexto contexto = new Contexto(arreglo);

        // --------------------------------------------------------------------
        // LLAMADA INICIAL (RAÍZ de la recursión).
        // Se lanza desde 'main' con 'contar = false': esta llamada NO se cuenta
        // como llamada recursiva. Para n == 0 el rango es [0, -1], que recae de
        // inmediato en el caso base.
        // --------------------------------------------------------------------
        mergeSort(contexto, auxiliar, 0, arreglo.length - 1, 0, false);

        return new Resultado(original, arreglo.clone(), contexto.pasos,
                contexto.comparaciones, contexto.profundidadMaxima,
                contexto.llamadasRecursivas, contexto.invocacionesTotales);
    }

    /**
     * Ordena recursivamente el rango {@code [inicio, fin]} del arreglo aplicando
     * el esquema <b>divide y vencerás</b>.
     *
     * <p><b>Dividir:</b> calcula el punto medio y separa el rango en
     * {@code [inicio, medio]} y {@code [medio + 1, fin]}. <b>Vencer:</b> ordena
     * recursivamente cada mitad. <b>Combinar:</b> fusiona las dos mitades ya
     * ordenadas. <b>Caso base:</b> si el rango tiene cero o un elemento
     * ({@code inicio >= fin}), ya está ordenado y se retorna sin hacer nada.</p>
     *
     * @param contexto    acumulador del proceso
     * @param auxiliar    arreglo auxiliar reutilizable de tamaño {@code n}
     * @param inicio      índice inicial del rango (inclusive)
     * @param fin         índice final del rango (inclusive)
     * @param profundidad profundidad de recursión (la llamada raíz es 0)
     * @param contar      {@code true} si esta invocación es una auto-invocación
     *                    (se cuenta); {@code false} solo para la llamada raíz
     */
    private static void mergeSort(Contexto contexto, int[] auxiliar,
                                  int inicio, int fin, int profundidad,
                                  boolean contar) {
        // Se cuentan TODAS las invocaciones (para verificar consistencia), pero
        // solo las auto-invocaciones cuentan como "llamadas recursivas".
        contexto.invocacionesTotales++;
        if (contar) {
            contexto.llamadasRecursivas++;
        }

        // Se registra la máxima profundidad alcanzada para razonar sobre el
        // tamaño de la pila recursiva (O(log n)).
        if (profundidad > contexto.profundidadMaxima) {
            contexto.profundidadMaxima = profundidad;
        }

        // --------------------------------------------------------------------
        // CASO BASE (condición de corte)
        // Un rango con cero o un elemento ya está ordenado por definición: no se
        // divide ni se fusiona. 'inicio > fin' ocurre solo para el arreglo vacío.
        // --------------------------------------------------------------------
        if (inicio >= fin) {
            contexto.pasos.add(Paso.casoBase(profundidad, inicio, fin,
                    copiarRango(contexto.arreglo, inicio, fin)));
            return;
        }

        // --------------------------------------------------------------------
        // DIVIDIR: partir el rango en dos mitades aproximadamente iguales.
        // 'medio' queda como el último índice de la mitad izquierda. Con esta
        // fórmula, la mitad izquierda tiene ceil(m/2) elementos y la derecha
        // floor(m/2), donde m es el tamaño del rango.
        // --------------------------------------------------------------------
        int medio = inicio + (fin - inicio) / 2;

        // Se guarda una "foto" del rango y de sus dos partes antes de ordenar,
        // para poder mostrar exactamente en qué se dividió cada subproblema.
        int[] subarreglo = copiarRango(contexto.arreglo, inicio, fin);
        int[] parteIzquierda = copiarRango(contexto.arreglo, inicio, medio);
        int[] parteDerecha = copiarRango(contexto.arreglo, medio + 1, fin);
        contexto.pasos.add(Paso.dividir(profundidad, inicio, fin, subarreglo,
                parteIzquierda, parteDerecha));

        // --------------------------------------------------------------------
        // LLAMADAS RECURSIVAS: resolver cada mitad de forma independiente.
        // Cada una de estas dos llamadas se cuenta como llamada recursiva.
        // Al volver, arreglo[inicio..medio] y arreglo[medio+1..fin] ya están
        // ordenados de forma individual.
        // --------------------------------------------------------------------
        mergeSort(contexto, auxiliar, inicio, medio, profundidad + 1, true);
        mergeSort(contexto, auxiliar, medio + 1, fin, profundidad + 1, true);

        // --------------------------------------------------------------------
        // COMBINAR (FUSIONAR)
        // Este es el paso propio de MergeSort: aunque las dos mitades ya están
        // ordenadas, el rango completo todavía no lo está. La fusión intercala
        // ambas mitades en una sola secuencia ordenada.
        // --------------------------------------------------------------------
        fusionar(contexto, auxiliar, inicio, medio, fin, profundidad);
    }

    /**
     * <b>Lógica de fusión.</b> Intercala los dos subarreglos <b>ya ordenados</b>
     * {@code [inicio, medio]} y {@code [medio + 1, fin]} en una única secuencia
     * ordenada, dejando el resultado dentro del propio arreglo.
     *
     * <p>Primero copia el rango completo al arreglo auxiliar. Luego recorre el
     * rango llenándolo con el <b>menor</b> de los dos primeros elementos
     * disponibles (índices {@code i} y {@code j}). Cuando uno de los subarreglos
     * se agota, copia el resto del otro, que ya está ordenado. La comparación
     * {@code aux[j] < aux[i]} (estricta) hace que, ante valores iguales, se elija
     * primero el de la izquierda: esto mantiene el algoritmo <b>estable</b>.</p>
     *
     * @param contexto    acumulador del proceso
     * @param auxiliar    arreglo auxiliar reutilizable
     * @param inicio      índice inicial del rango (inclusive)
     * @param medio       último índice de la mitad izquierda
     * @param fin         índice final del rango (inclusive)
     * @param profundidad profundidad de recursión del rango
     */
    private static void fusionar(Contexto contexto, int[] auxiliar,
                                 int inicio, int medio, int fin,
                                 int profundidad) {
        int[] arreglo = contexto.arreglo;

        // Se guardan los dos subarreglos (ya ordenados) para poder mostrarlos.
        int[] izquierda = copiarRango(arreglo, inicio, medio);
        int[] derecha = copiarRango(arreglo, medio + 1, fin);

        // Copia del rango al auxiliar: a partir de aquí, el arreglo se puede
        // sobrescribir sin perder los valores de origen.
        for (int k = inicio; k <= fin; k++) {
            auxiliar[k] = arreglo[k];
        }

        // 'i' recorre la mitad izquierda y 'j' la derecha, cada uno sobre el
        // auxiliar. 'k' es la posición de escritura en el arreglo.
        int i = inicio;
        int j = medio + 1;
        int comparacionesFusion = 0;

        for (int k = inicio; k <= fin; k++) {
            if (i > medio) {
                // La mitad izquierda se agotó: queda la derecha, ya ordenada.
                arreglo[k] = auxiliar[j];
                j++;
            } else if (j > fin) {
                // La mitad derecha se agotó: queda la izquierda, ya ordenada.
                arreglo[k] = auxiliar[i];
                i++;
            } else {
                // Ambas mitades tienen elementos: se comparan sus primeros
                // disponibles y se toma el menor (estable ante empates).
                comparacionesFusion++;
                contexto.comparaciones++;
                if (auxiliar[j] < auxiliar[i]) {
                    arreglo[k] = auxiliar[j];
                    j++;
                } else {
                    arreglo[k] = auxiliar[i];
                    i++;
                }
            }
        }

        // Foto del resultado de la fusión.
        int[] fusionado = copiarRango(arreglo, inicio, fin);
        contexto.pasos.add(Paso.fusionar(profundidad, inicio, fin, izquierda,
                derecha, fusionado, comparacionesFusion));
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
     * Muestra por la salida indicada el arreglo original, la traza paso a paso
     * de la recursión (divisiones, casos base y fusiones, cada una indentada
     * según su nivel de recursión) y el arreglo ordenado con la verificación del
     * orden y los contadores totales.
     *
     * @param resultado resultado devuelto por {@link #ordenar(int[])}
     * @param salida    flujo de salida a utilizar
     */
    public static void imprimirResultado(Resultado resultado, PrintStream salida) {
        salida.println("Arreglo original: " + formatear(resultado.getOriginal()));
        salida.println();

        List<Paso> pasos = resultado.getPasos();
        salida.println("Traza de la recursion (cada nivel de recursion va indentado):");
        for (Paso paso : pasos) {
            String sangria = repetir("  ", paso.getProfundidad());
            switch (paso.getTipo()) {
                case DIVIDIR:
                    salida.printf("%sDividir  (nivel %d) rango [%d..%d]: %s -> "
                                    + "izquierda %s | derecha %s%n",
                            sangria, paso.getProfundidad(), paso.getInicio(),
                            paso.getFin(), formatear(paso.getSubarreglo()),
                            formatear(paso.getParteIzquierda()),
                            formatear(paso.getParteDerecha()));
                    break;
                case CASO_BASE:
                    salida.printf("%sCaso base(nivel %d) rango [%d..%d]: %s"
                                    + "  (%d elemento%s: ya esta ordenado)%n",
                            sangria, paso.getProfundidad(), paso.getInicio(),
                            paso.getFin(), formatear(paso.getSubarreglo()),
                            paso.getSubarreglo().length,
                            paso.getSubarreglo().length == 1 ? "" : "s");
                    break;
                case FUSIONAR:
                    salida.printf("%sFusionar (nivel %d) rango [%d..%d]: %s + %s "
                                    + "= %s  (comparaciones: %d)%n",
                            sangria, paso.getProfundidad(), paso.getInicio(),
                            paso.getFin(), formatear(paso.getIzquierda()),
                            formatear(paso.getDerecha()),
                            formatear(paso.getFusionado()),
                            paso.getComparaciones());
                    break;
                default:
                    break;
            }
        }

        int[] ordenado = resultado.getOrdenado();
        salida.println();
        salida.println("Arreglo ordenado: " + formatear(ordenado));
        salida.println("Verificacion (orden no decreciente): "
                + (estaOrdenadoNoDecreciente(ordenado) ? "OK" : "FALLO"));
        salida.printf("Divisiones: %d | Casos base: %d | Fusiones: %d | "
                        + "Comparaciones: %d | Profundidad maxima: %d%n",
                resultado.getCantidadDivisiones(), resultado.getCantidadCasosBase(),
                resultado.getCantidadFusiones(), resultado.getComparaciones(),
                resultado.getProfundidadMaxima());
        salida.printf("Llamadas recursivas (sin contar la inicial desde main): %d%n",
                resultado.getLlamadasRecursivas());
    }

    /**
     * Explica conceptualmente el enfoque divide y vencerás de MergeSort y analiza
     * su complejidad temporal y espacial.
     *
     * @param salida flujo de salida
     */
    private static void imprimirExplicacion(PrintStream salida) {
        salida.println("== Como funciona MergeSort (divide y venceras) ==");
        salida.println();
        salida.println("DIVIDIR: descompone el problema en partes mas pequenas.");
        salida.println("  Se parte el arreglo en dos mitades de tamano casi igual y se");
        salida.println("  repite el proceso recursivamente en cada mitad, hasta llegar a");
        salida.println("  subarreglos de cero o un elemento (caso base), que ya estan");
        salida.println("  ordenados por definicion.");
        salida.println();
        salida.println("FUSIONAR: combina las soluciones de esas partes.");
        salida.println("  Al volver de la recursion, cada mitad ya esta ordenada. La");
        salida.println("  fusion intercala las dos mitades comparando sus primeros");
        salida.println("  elementos disponibles y tomando el menor en cada paso, de modo");
        salida.println("  que construye un subarreglo ordenado cada vez mas grande hasta");
        salida.println("  reconstruir el arreglo completo ordenado.");
        salida.println();
        salida.println("En sintesis: dividir descompone el problema; fusionar recompone");
        salida.println("la solucion a partir de las soluciones de las partes.");
        salida.println();
        salida.println("Complejidad temporal: O(n log n).");
        salida.println("  Dividir siempre por la mitad produce un arbol de recursion de");
        salida.println("  log n niveles. En cada nivel, las fusiones recorren los n");
        salida.println("  elementos en total, o sea O(n) por nivel. Por lo tanto el");
        salida.println("  tiempo es O(n) * O(log n) = O(n log n), en mejor, promedio y");
        salida.println("  peor caso.");
        salida.println("Espacio auxiliar: O(n).");
        salida.println("  La fusion necesita un arreglo auxiliar de tamano n para");
        salida.println("  intercalar (se reserva una sola vez y se reutiliza). A eso se");
        salida.println("  suma la pila de recursion, que es O(log n).");
        salida.println("Estabilidad: MergeSort es estable (ante valores iguales conserva");
        salida.println("  el orden relativo original).");
        salida.println();
    }

    /**
     * Construye una cadena repitiendo un texto una cantidad de veces (para
     * indentar los eventos según su profundidad de recursión).
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
     * Punto de entrada. Por defecto muestra un ejemplo principal con la traza
     * completa, la explicación del algoritmo y la batería de casos de prueba.
     * Con el argumento {@code interactivo} (o {@code -i}) lee un arreglo por
     * consola y muestra el ordenamiento paso a paso.
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
            ejecutarEjemploPrincipal(salida);
            imprimirExplicacion(salida);
            ejecutarCasosDePrueba(salida);
        }
    }

    /**
     * Ejecuta el ejemplo principal: un arreglo desordenado, mostrando la traza
     * completa del proceso.
     *
     * @param salida flujo de salida
     */
    private static void ejecutarEjemploPrincipal(PrintStream salida) {
        int[] numeros = {8, 3, 7, 4, 2, 6, 5, 1};

        salida.println("== MergeSort: ejemplo principal (arreglo desordenado) ==");
        salida.println();

        Resultado resultado = ordenar(numeros.clone());
        imprimirResultado(resultado, salida);
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
        salida.println("== Ordenamiento por MergeSort ==");

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
     * desordenado, uno ya ordenado, uno en orden inverso y uno con valores
     * repetidos, más casos límite (vacío, un elemento, dos elementos, valores
     * extremos y entrada {@code null}).
     *
     * <p>Para cada caso se verifica que el resultado quede ordenado, que
     * <b>conserve exactamente los valores originales</b> (misma multiset) y que
     * coincida con {@link Arrays#sort(int[])}; además se valida la coherencia de
     * la traza (divisiones, casos base, fusiones) y de los contadores.</p>
     *
     * @param salida flujo de salida
     */
    private static void ejecutarCasosDePrueba(PrintStream salida) {
        salida.println("== Casos de prueba exigidos ==");

        // 1) DESORDENADO (ejemplo principal): n = 8 => 3 niveles de recursion.
        verificarCaso("desordenado", new int[] {8, 3, 7, 4, 2, 6, 5, 1},
                false, salida);

        // 2) YA ORDENADO: mismos pasos y misma complejidad (MergeSort no se
        //    degrada con entradas ordenadas, a diferencia de Quicksort).
        verificarCaso("ya ordenado", new int[] {1, 2, 3, 4, 5, 6, 7, 8},
                false, salida);

        // 3) ORDEN INVERSO.
        verificarCaso("orden inverso", new int[] {8, 7, 6, 5, 4, 3, 2, 1},
                false, salida);

        // 4) VALORES REPETIDOS: MergeSort es estable; el resultado debe
        //    conservar los valores (y coincidir con Arrays.sort).
        verificarCaso("valores repetidos", new int[] {5, 1, 5, 3, 3, 1, 5, 3},
                false, salida);

        salida.println();
        salida.println("== Casos limite adicionales ==");

        // 5) Arreglo vacío: caso base directo, sin divisiones ni fusiones.
        verificarCaso("vacio", new int[] {}, false, salida);

        // 6) Un solo elemento: caso base directo.
        verificarCaso("un elemento", new int[] {42}, false, salida);

        // 7) Dos elementos desordenados: 1 division, 2 casos base, 1 fusion.
        verificarCaso("dos elementos", new int[] {2, 1}, false, salida);

        // 8) Valores extremos de int y negativos repetidos.
        verificarCaso("valores extremos",
                new int[] {Integer.MAX_VALUE, Integer.MIN_VALUE, 0, Integer.MIN_VALUE},
                false, salida);

        // 9) Entrada null: debe rechazarse con excepción descriptiva.
        verificarArregloNulo(salida);

        salida.println();
        salida.println("Todas las verificaciones finalizaron. "
                + "Revisa que no aparezca la palabra FALLO.");
    }

    /**
     * Ordena una copia del arreglo de entrada y comprueba toda la corrección del
     * resultado: orden final, conservación de los valores originales, coherencia
     * de cada evento de la traza y consistencia de los contadores estructurales.
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
        int n = entrada.length;

        // --------------------------------------------------------------------
        // 1) CORRECCION DEL RESULTADO
        // --------------------------------------------------------------------
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
        // Conservacion de valores: como el resultado coincide con Arrays.sort, la
        // multiset de valores se preserva; se comprueba explícitamente el conteo.
        if (!mismosValores(entrada, resultado.getOrdenado())) {
            errores.add("el resultado no conserva los valores originales");
        }

        // --------------------------------------------------------------------
        // 2) COHERENCIA DE LA TRAZA
        // --------------------------------------------------------------------
        for (Paso paso : resultado.getPasos()) {
            validarPaso(paso, errores);
        }

        // --------------------------------------------------------------------
        // 3) CONTADORES ESTRUCTURALES
        // En MergeSort sobre n elementos:
        //   - fusiones = n - 1 (una por cada nodo interno del arbol) para n >= 1;
        //   - casos base = n para n >= 1 (una hoja por elemento); = 1 si n == 0;
        //   - invocaciones totales = casos base + fusiones;
        //   - llamadas recursivas = invocaciones totales - 1 (sin la raiz);
        //   - profundidad maxima = ceil(log2(n)) para n >= 1, 0 para n <= 1.
        // --------------------------------------------------------------------
        int fusionesEsperadas = Math.max(n - 1, 0);
        int casosBaseEsperados = (n == 0) ? 1 : n;
        long invocacionesEsperadas = casosBaseEsperados + fusionesEsperadas;
        long llamadasEsperadas = invocacionesEsperadas - 1;

        if (resultado.getCantidadFusiones() != fusionesEsperadas) {
            errores.add("fusiones = " + resultado.getCantidadFusiones()
                    + ", esperadas " + fusionesEsperadas);
        }
        if (resultado.getCantidadCasosBase() != casosBaseEsperados) {
            errores.add("casos base = " + resultado.getCantidadCasosBase()
                    + ", esperados " + casosBaseEsperados);
        }
        if (resultado.getInvocacionesTotales() != invocacionesEsperadas) {
            errores.add("invocaciones totales = " + resultado.getInvocacionesTotales()
                    + ", esperadas " + invocacionesEsperadas);
        }
        if (resultado.getLlamadasRecursivas() != llamadasEsperadas) {
            errores.add("llamadas recursivas = " + resultado.getLlamadasRecursivas()
                    + ", esperadas " + llamadasEsperadas);
        }
        if (resultado.getInvocacionesTotales()
                != resultado.getLlamadasRecursivas() + 1) {
            errores.add("invocaciones totales inconsistentes con las llamadas recursivas");
        }
        int profundidadEsperada = profundidadMaximaEsperada(n);
        if (resultado.getProfundidadMaxima() != profundidadEsperada) {
            errores.add("profundidad maxima = " + resultado.getProfundidadMaxima()
                    + ", esperada " + profundidadEsperada);
        }

        // --------------------------------------------------------------------
        // SALIDA DEL CASO
        // --------------------------------------------------------------------
        salida.println("Caso: " + nombre + " -> " + formatear(entrada));
        if (mostrarDetalle) {
            imprimirResultado(resultado, salida);
        } else {
            salida.printf("  Ordenado: %s | Divisiones: %d | Casos base: %d | "
                            + "Fusiones: %d | Comparaciones: %d | Profundidad: %d%n",
                    formatear(resultado.getOrdenado()),
                    resultado.getCantidadDivisiones(),
                    resultado.getCantidadCasosBase(),
                    resultado.getCantidadFusiones(),
                    resultado.getComparaciones(),
                    resultado.getProfundidadMaxima());
        }
        salida.printf("  Estado: %s%n",
                errores.isEmpty() ? "OK" : "FALLO -> " + String.join("; ", errores));
        salida.println();
    }

    /**
     * Valida la coherencia interna de un evento de la traza.
     *
     * <ul>
     *   <li><b>DIVIDIR:</b> el rango tiene al menos dos elementos; la
     *       concatenación de las dos partes reproduce el subarreglo; y los
     *       tamaños corresponden a una partición por la mitad
     *       ({@code ceil(m/2)} y {@code floor(m/2)}).</li>
     *   <li><b>CASO_BASE:</b> el subarreglo tiene a lo sumo un elemento y su
     *       longitud coincide con el rango declarado.</li>
     *   <li><b>FUSIONAR:</b> izquierda y derecha están ordenadas, el fusionado
     *       está ordenado y coincide exactamente con la intercalación de ambas
     *       mitades.</li>
     * </ul>
     *
     * @param paso    evento a validar
     * @param errores lista de errores a la que agregar los hallazgos
     */
    private static void validarPaso(Paso paso, List<String> errores) {
        String etiqueta = paso.getTipo() + " nivel " + paso.getProfundidad()
                + " rango [" + paso.getInicio() + ".." + paso.getFin() + "]";

        switch (paso.getTipo()) {
            case DIVIDIR: {
                int[] sub = paso.getSubarreglo();
                int[] izq = paso.getParteIzquierda();
                int[] der = paso.getParteDerecha();
                if (sub.length < 2) {
                    errores.add(etiqueta + ": una division debe tener 2+ elementos");
                }
                if (izq.length + der.length != sub.length) {
                    errores.add(etiqueta + ": las partes no suman el subarreglo");
                }
                if (!Arrays.equals(concatenar(izq, der), sub)) {
                    errores.add(etiqueta + ": las partes no reproducen el subarreglo");
                }
                if (izq.length != (sub.length + 1) / 2
                        || der.length != sub.length / 2) {
                    errores.add(etiqueta + ": las mitades no son de tamano equilibrado");
                }
                break;
            }
            case CASO_BASE: {
                int[] sub = paso.getSubarreglo();
                if (sub.length > 1) {
                    errores.add(etiqueta + ": un caso base no puede tener 2+ elementos");
                }
                if (sub.length != paso.getFin() - paso.getInicio() + 1) {
                    errores.add(etiqueta + ": la longitud no coincide con el rango");
                }
                break;
            }
            case FUSIONAR: {
                int[] izq = paso.getIzquierda();
                int[] der = paso.getDerecha();
                int[] fus = paso.getFusionado();
                if (izq.length + der.length != fus.length) {
                    errores.add(etiqueta + ": el fusionado no mide la suma de mitades");
                }
                if (!estaOrdenadoNoDecreciente(izq)
                        || !estaOrdenadoNoDecreciente(der)) {
                    errores.add(etiqueta + ": alguna mitad no estaba ordenada");
                }
                if (!estaOrdenadoNoDecreciente(fus)) {
                    errores.add(etiqueta + ": el resultado de la fusion no quedo ordenado");
                }
                if (!Arrays.equals(fusionarReferencia(izq, der), fus)) {
                    errores.add(etiqueta + ": la fusion no coincide con el intercalado");
                }
                break;
            }
            default:
                errores.add(etiqueta + ": tipo de evento desconocido");
                break;
        }
    }

    /**
     * Intercala dos arreglos ordenados (verdad de referencia para verificar las
     * fusiones registradas). Ante valores iguales toma primero el de la
     * izquierda, igual que el algoritmo real.
     *
     * @param izquierda subarreglo izquierdo ordenado
     * @param derecha   subarreglo derecho ordenado
     * @return fusión ordenada de ambos
     */
    private static int[] fusionarReferencia(int[] izquierda, int[] derecha) {
        int[] resultado = new int[izquierda.length + derecha.length];
        int i = 0;
        int j = 0;
        for (int k = 0; k < resultado.length; k++) {
            if (i >= izquierda.length) {
                resultado[k] = derecha[j++];
            } else if (j >= derecha.length) {
                resultado[k] = izquierda[i++];
            } else if (derecha[j] < izquierda[i]) {
                resultado[k] = derecha[j++];
            } else {
                resultado[k] = izquierda[i++];
            }
        }
        return resultado;
    }

    /**
     * Comprueba que dos arreglos contengan exactamente los mismos valores (misma
     * multiset). Como no importa el orden, se comparan sus copias ordenadas.
     *
     * @param a primer arreglo
     * @param b segundo arreglo
     * @return {@code true} si ambos tienen los mismos valores repetidos la misma
     *         cantidad de veces
     */
    private static boolean mismosValores(int[] a, int[] b) {
        if (a.length != b.length) {
            return false;
        }
        int[] copiaA = a.clone();
        int[] copiaB = b.clone();
        Arrays.sort(copiaA);
        Arrays.sort(copiaB);
        return Arrays.equals(copiaA, copiaB);
    }

    /**
     * Concatenación de dos arreglos.
     *
     * @param a primer arreglo
     * @param b segundo arreglo
     * @return nuevo arreglo con los elementos de {@code a} seguidos de {@code b}
     */
    private static int[] concatenar(int[] a, int[] b) {
        int[] resultado = new int[a.length + b.length];
        System.arraycopy(a, 0, resultado, 0, a.length);
        System.arraycopy(b, 0, resultado, a.length, b.length);
        return resultado;
    }

    /**
     * Calcula la profundidad máxima de recursión esperada para MergeSort:
     * {@code ceil(log2(n))} para {@code n >= 1} (y 0 para {@code n <= 1}).
     *
     * @param n cantidad de elementos
     * @return profundidad máxima esperada
     */
    private static int profundidadMaximaEsperada(int n) {
        int profundidad = 0;
        while ((1L << profundidad) < n) {
            profundidad++;
        }
        return profundidad;
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
        if (arreglo == null) {
            return "[]";
        }
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
 * 2. Divide y vencerás: 'mergeSort' DIVIDE el rango por la mitad, VENCE con dos
 *    llamadas recursivas (una por mitad) y COMBINA con 'fusionar'. A diferencia
 *    de Quicksort, el trabajo relevante está al combinar: las mitades vuelven
 *    ordenadas y hay que intercalarlas.
 *
 * 3. Caso base: un rango de cero o un elemento ya está ordenado. Es el corte que
 *    garantiza que la recursión termine, porque cada división reduce el tamaño.
 *
 * 4. Fusión: se copia el rango al arreglo auxiliar y se recorre el rango
 *    escribiendo en 'arreglo' el menor de los primeros elementos disponibles de
 *    cada mitad (índices i y j). Cuando una mitad se agota, se copia el resto de
 *    la otra. La comparación 'aux[j] < aux[i]' (estricta) hace la fusión estable.
 *
 * 5. Comentarios que distinguen las partes: dentro de 'mergeSort' se marcan con
 *    claridad el CASO BASE, la sección DIVIDIR, las LLAMADAS RECURSIVAS y la
 *    sección COMBINAR (FUSIONAR). La lógica de intercalado vive aparte, en
 *    'fusionar'.
 *
 * 6. Espacio auxiliar O(n): se reserva un único arreglo auxiliar de tamaño n y se
 *    reutiliza en todas las fusiones (no se crea uno por llamada). La pila de
 *    recursión añade O(log n).
 *
 * 7. Complejidad temporal O(n log n): el árbol de recursión tiene log n niveles y
 *    cada nivel procesa los n elementos en total mediante las fusiones. Vale para
 *    mejor, promedio y peor caso.
 *
 * 8. Contadores verificados en las pruebas (para n >= 1): fusiones = n - 1,
 *    casos base = n, invocaciones totales = 2n - 1, llamadas recursivas = 2n - 2
 *    y profundidad máxima = ceil(log2(n)). Para n == 0: 1 caso base, 0 fusiones,
 *    1 invocación y 0 llamadas recursivas. Para n == 1: 1 caso base, 0 fusiones.
 *
 * 9. Conservación de valores: se comprueba explícitamente (además de comparar con
 *    Arrays.sort) que la multiset de valores no cambie, cumpliendo el pedido de
 *    "que conserve los valores originales".
 *
 * 10. Casos límite cubiertos: arreglo null (IllegalArgumentException), vacío, un
 *     elemento, dos elementos, desordenado, ya ordenado, orden inverso, valores
 *     repetidos y valores extremos de int.
 * ============================================================================
 */

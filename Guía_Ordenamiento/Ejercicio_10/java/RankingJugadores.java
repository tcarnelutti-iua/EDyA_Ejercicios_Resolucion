/*
 * ============================================================================
 * PROMPT INICIAL UTILIZADO PARA GENERAR ESTA SOLUCIÓN
 * ============================================================================
 *
 * Quiero implementar en Java un programa que ordene un arreglo de jugadores por
 * puntaje, de mayor a menor. Cada jugador tiene un nombre y un puntaje entero.
 * Usá como ejemplo:
 *
 *     Jugador("Ana", 1200)
 *     Jugador("Pedro", 900)
 *     Jugador("Lucia", 1500)
 *
 * Elegí MergeSort y justificá la elección: ofrece tiempo O(n log n), por lo que
 * resulta adecuado cuando el ranking puede contener muchos jugadores. Además,
 * es estable; si dos jugadores tienen el mismo puntaje, conserva entre ellos el
 * orden original. Indicá también su desventaja: necesita O(n) de espacio
 * auxiliar.
 *
 * Implementá el ordenamiento comparando los puntajes de mayor a menor. Si hay
 * empate, mantené el orden original de esos jugadores. Evitá comparar restando
 * puntajes, para no correr riesgos de desbordamiento.
 *
 * Mostrá el arreglo original y el ranking ordenado, con nombre y puntaje de cada
 * jugador. Incluí comentarios que expliquen la estrategia de dividir y fusionar,
 * cómo se comparan los puntajes y por qué se preserva el orden en caso de
 * empate.
 *
 * Verificá el resultado con jugadores en distintos órdenes, puntajes repetidos,
 * un solo jugador y un arreglo vacío. Comprobá que el ranking quede de mayor a
 * menor y que los empates conserven su orden original.
 * ============================================================================
 */

import java.io.PrintStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Scanner;

/**
 * Ordenamiento de un ranking de jugadores por <b>puntaje descendente</b>
 * (de mayor a menor) mediante <b>MergeSort</b> (ordenamiento por mezcla),
 * aplicando el paradigma <b>divide y vencerás</b>.
 *
 * <p><b>Modelo.</b> Cada {@link Jugador} tiene un {@code nombre} y un
 * {@code puntaje} entero. El objetivo es obtener el ranking ordenado de mayor a
 * menor puntaje.</p>
 *
 * <p><b>¿Por qué MergeSort?</b></p>
 * <ul>
 *   <li><b>Tiempo {@code O(n log n)}.</b> Dividir siempre por la mitad genera un
 *       árbol de recursión de {@code log n} niveles y, en cada nivel, las
 *       fusiones recorren los {@code n} jugadores en total. Por eso el algoritmo
 *       tarda {@code O(n log n)} en el mejor, el promedio y el peor caso. Esto lo
 *       hace <b>adecuado cuando el ranking puede contener muchos jugadores</b>, a
 *       diferencia de los algoritmos cuadráticos {@code O(n²)}.</li>
 *   <li><b>Estabilidad.</b> Si dos jugadores tienen el <b>mismo puntaje</b>, el
 *       algoritmo <b>conserva entre ellos el orden original</b> del arreglo. Esto
 *       es clave en un ranking: dos jugadores empatados mantienen el orden en que
 *       se registraron.</li>
 *   <li><b>Desventaja: espacio auxiliar {@code O(n)}.</b> La fusión necesita un
 *       arreglo auxiliar del tamaño n para intercalar; no ordena in-place. (A eso
 *       se suma la pila de recursión, {@code O(log n)}.)</li>
 * </ul>
 *
 * <p><b>Idea del algoritmo.</b> Para ordenar un rango {@code [inicio, fin]}:</p>
 * <ol>
 *   <li><b>Dividir.</b> Se parte el rango en dos mitades de tamaño
 *       aproximadamente igual: {@code [inicio, medio]} y {@code [medio + 1, fin]}.
 *       Dividir <b>descompone</b> el problema en dos subproblemas más
 *       pequeños.</li>
 *   <li><b>Vencer (recursión).</b> Se ordena recursivamente cada mitad. El
 *       problema original se reduce a dos subproblemas del mismo tipo pero más
 *       chicos.</li>
 *   <li><b>Combinar (fusionar).</b> Se <b>intercalan</b> las dos mitades ya
 *       ordenadas en una sola también ordenada. En MergeSort el trabajo
 *       importante ocurre al <i>combinar</i>: las mitades vuelven ordenadas y hay
 *       que unirlas.</li>
 * </ol>
 *
 * <p><b>Condición de corte (caso base).</b> Un rango con <b>cero o un jugador</b>
 * ya está ordenado por definición: no hay nada que dividir ni que fusionar.
 * Cuando la recursión llega allí, se detiene. Este caso base garantiza que las
 * llamadas terminen, porque cada división reduce estrictamente el tamaño.</p>
 *
 * <p><b>Cómo se comparan los puntajes.</b> En cada paso de la fusión se miran el
 * primer jugador disponible de cada mitad y se copia primero el de
 * <b>mayor puntaje</b> (orden descendente). La comparación se hace con los
 * operadores relacionales de enteros {@code >} / {@code <=}
 * ({@code puntajeDerecha > puntajeIzquierda}), es decir comparando los valores
 * <b>sin restarlos</b>. <b>Nunca</b> se calcula {@code puntajeA - puntajeB}:
 * restar dos enteros puede <i>desbordar</i> (por ejemplo
 * {@code Integer.MAX_VALUE - Integer.MIN_VALUE} no cabe en un {@code int}) y dar
 * un signo equivocado. Comparar directamente evita ese riesgo por completo.</p>
 *
 * <p><b>Por qué se preserva el orden en caso de empate (estabilidad).</b> La
 * fusión usa la condición <b>estricta</b> {@code puntajeDerecha > puntajeIzquierda}
 * para elegir al de la derecha. Si los puntajes son <b>iguales</b> la condición
 * es falsa y se elige <b>siempre el de la izquierda</b>. Como la mitad izquierda
 * contiene a los jugadores que estaban <i>antes</i> que los de la derecha en el
 * arreglo original, ante un empate "gana" el que apareció primero. Esta
 * propiedad, aplicada en todas las fusiones, hace que dos jugadores con igual
 * puntaje <b>conserven su orden relativo original</b>.</p>
 *
 * <p>La clase separa el <i>algoritmo</i> ({@link #ordenar(Jugador[])}) de la
 * <i>presentación</i> ({@link #imprimirResultado}), de modo que el resultado
 * pueda probarse automáticamente sin depender de la salida por consola.</p>
 *
 * @author Agente Desarrollador
 */
public final class RankingJugadores {

    /** Impide instanciar una clase que solo agrupa métodos estáticos. */
    private RankingJugadores() {
    }

    // ------------------------------------------------------------------------
    // MODELO
    // ------------------------------------------------------------------------

    /**
     * Un jugador con <b>nombre</b> y <b>puntaje</b>. Es inmutable: una vez creado
     * no puede cambiar. Se valida que el nombre no sea {@code null} ni esté vacío
     * (o formado solo por espacios); se guarda recortado.
     *
     * <p>Se define {@link #equals(Object)} / {@link #hashCode()} por nombre y
     * puntaje para poder comparar y verificar listas de jugadores.</p>
     */
    public static final class Jugador {

        private final String nombre;
        private final int puntaje;

        /**
         * Crea un jugador.
         *
         * @param nombre  nombre del jugador; no puede ser {@code null} ni vacío
         * @param puntaje puntaje entero del jugador (puede ser negativo o cero)
         * @throws IllegalArgumentException si {@code nombre} es {@code null} o
         *         está vacío/solo espacios
         */
        public Jugador(String nombre, int puntaje) {
            if (nombre == null) {
                throw new IllegalArgumentException("El nombre del jugador no puede ser null.");
            }
            String recortado = nombre.trim();
            if (recortado.isEmpty()) {
                throw new IllegalArgumentException("El nombre del jugador no puede estar vacio.");
            }
            this.nombre = recortado;
            this.puntaje = puntaje;
        }

        /** @return nombre del jugador */
        public String getNombre() {
            return nombre;
        }

        /** @return puntaje entero del jugador */
        public int getPuntaje() {
            return puntaje;
        }

        @Override
        public boolean equals(Object objeto) {
            if (this == objeto) {
                return true;
            }
            if (!(objeto instanceof Jugador)) {
                return false;
            }
            Jugador otro = (Jugador) objeto;
            return puntaje == otro.puntaje && nombre.equals(otro.nombre);
        }

        @Override
        public int hashCode() {
            return 31 * nombre.hashCode() + puntaje;
        }

        @Override
        public String toString() {
            return nombre + " (" + puntaje + ")";
        }
    }

    /**
     * Envoltorio <b>interno</b> de un jugador que además recuerda su
     * <b>posición original</b> en el arreglo de entrada.
     *
     * <p>Sirve para verificar la <b>estabilidad</b>: aunque dos jugadores tengan
     * igual nombre y puntaje (indistinguibles por valor), sus posiciones
     * originales son distintas y permiten comprobar que un empate conserva el
     * orden relativo original.</p>
     */
    private static final class Elemento {

        private final Jugador jugador;
        private final int posicionOriginal;

        private Elemento(Jugador jugador, int posicionOriginal) {
            this.jugador = jugador;
            this.posicionOriginal = posicionOriginal;
        }
    }

    // ------------------------------------------------------------------------
    // TIPOS AUXILIARES
    // ------------------------------------------------------------------------

    /**
     * Detalle de una etapa del proceso, inmutable y de solo lectura.
     *
     * <p>Cada instancia describe un evento de la recursión identificado por su
     * {@link Tipo} y la profundidad (nivel de recursión) a la que ocurrió. Los
     * campos aplicables dependen del tipo:</p>
     * <ul>
     *   <li>{@link Tipo#DIVIDIR}: {@link #getSubarreglo()},
     *       {@link #getParteIzquierda()} y {@link #getParteDerecha()}.</li>
     *   <li>{@link Tipo#CASO_BASE}: {@link #getSubarreglo()}.</li>
     *   <li>{@link Tipo#FUSIONAR}: {@link #getIzquierda()},
     *       {@link #getDerecha()}, {@link #getFusionado()} y
     *       {@link #getComparaciones()}.</li>
     * </ul>
     */
    public static final class Paso {

        /** Tipo de evento de la recursión. */
        public enum Tipo {
            /** Se parte un rango en dos mitades. */
            DIVIDIR,
            /** Un rango de cero o un jugador: ya está ordenado. */
            CASO_BASE,
            /** Se intercalan dos mitades ordenadas en una. */
            FUSIONAR
        }

        private final Tipo tipo;
        private final int profundidad;
        private final int inicio;
        private final int fin;
        private final Jugador[] subarreglo;
        private final Jugador[] parteIzquierda;
        private final Jugador[] parteDerecha;
        private final Jugador[] izquierda;
        private final Jugador[] derecha;
        private final Jugador[] fusionado;
        private final int comparaciones;

        private Paso(Tipo tipo,
                     int profundidad,
                     int inicio,
                     int fin,
                     Jugador[] subarreglo,
                     Jugador[] parteIzquierda,
                     Jugador[] parteDerecha,
                     Jugador[] izquierda,
                     Jugador[] derecha,
                     Jugador[] fusionado,
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

        private static Paso dividir(int profundidad, int inicio, int fin,
                                    Jugador[] subarreglo, Jugador[] parteIzquierda,
                                    Jugador[] parteDerecha) {
            return new Paso(Tipo.DIVIDIR, profundidad, inicio, fin, subarreglo,
                    parteIzquierda, parteDerecha, null, null, null, 0);
        }

        private static Paso casoBase(int profundidad, int inicio, int fin,
                                     Jugador[] subarreglo) {
            return new Paso(Tipo.CASO_BASE, profundidad, inicio, fin, subarreglo,
                    null, null, null, null, null, 0);
        }

        private static Paso fusionar(int profundidad, int inicio, int fin,
                                     Jugador[] izquierda, Jugador[] derecha,
                                     Jugador[] fusionado, int comparaciones) {
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

        /** @return copia del subarreglo (DIVIDIR o CASO_BASE), o {@code null} */
        public Jugador[] getSubarreglo() {
            return subarreglo == null ? null : subarreglo.clone();
        }

        /** @return copia de la mitad izquierda del rango (solo DIVIDIR), o {@code null} */
        public Jugador[] getParteIzquierda() {
            return parteIzquierda == null ? null : parteIzquierda.clone();
        }

        /** @return copia de la mitad derecha del rango (solo DIVIDIR), o {@code null} */
        public Jugador[] getParteDerecha() {
            return parteDerecha == null ? null : parteDerecha.clone();
        }

        /** @return copia de la mitad izquierda ya ordenada (solo FUSIONAR), o {@code null} */
        public Jugador[] getIzquierda() {
            return izquierda == null ? null : izquierda.clone();
        }

        /** @return copia de la mitad derecha ya ordenada (solo FUSIONAR), o {@code null} */
        public Jugador[] getDerecha() {
            return derecha == null ? null : derecha.clone();
        }

        /** @return copia del resultado de la fusión (solo FUSIONAR), o {@code null} */
        public Jugador[] getFusionado() {
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
     * <p>Además del ranking final ordenado, conserva el arreglo original, la traza
     * completa de eventos, los contadores, la máxima profundidad de recursión y
     * las posiciones originales de los jugadores ya ordenados (para verificar la
     * estabilidad).</p>
     */
    public static final class Resultado {

        private final Jugador[] original;
        private final Jugador[] ordenado;
        private final int[] posicionesOriginales;
        private final List<Paso> pasos;
        private final long comparaciones;
        private final int profundidadMaxima;

        private Resultado(Jugador[] original,
                          Jugador[] ordenado,
                          int[] posicionesOriginales,
                          List<Paso> pasos,
                          long comparaciones,
                          int profundidadMaxima) {
            this.original = original;
            this.ordenado = ordenado;
            this.posicionesOriginales = posicionesOriginales;
            this.pasos = Collections.unmodifiableList(pasos);
            this.comparaciones = comparaciones;
            this.profundidadMaxima = profundidadMaxima;
        }

        /** @return copia del arreglo tal como estaba antes de ordenar */
        public Jugador[] getOriginal() {
            return original.clone();
        }

        /** @return copia del ranking ya ordenado (puntaje descendente) */
        public Jugador[] getOrdenado() {
            return ordenado.clone();
        }

        /** @return traza completa de eventos, en orden de ejecución */
        public List<Paso> getPasos() {
            return pasos;
        }

        /** @return cantidad total de comparaciones entre puntajes */
        public long getComparaciones() {
            return comparaciones;
        }

        /** @return máxima profundidad de recursión alcanzada (raíz = 0) */
        public int getProfundidadMaxima() {
            return profundidadMaxima;
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

        /**
         * Comprueba que el ranking sea <b>estable</b>: si dos jugadores aparecen
         * consecutivamente con el <b>mismo puntaje</b>, sus posiciones originales
         * deben ser crecientes (el que estaba antes en el arreglo original debe
         * quedar antes en el ranking). Como los empatados quedan contiguos, basta
         * revisar pares consecutivos.
         *
         * @return {@code true} si los empates conservan el orden original
         */
        public boolean esEstable() {
            for (int i = 1; i < ordenado.length; i++) {
                boolean mismoPuntaje =
                        ordenado[i - 1].getPuntaje() == ordenado[i].getPuntaje();
                if (mismoPuntaje
                        && posicionesOriginales[i - 1] > posicionesOriginales[i]) {
                    return false;
                }
            }
            return true;
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
     * Acumulador interno de una ejecución: el arreglo de elementos que se está
     * ordenando, la traza y los contadores. Se usa una clase auxiliar mutable
     * porque los métodos recursivos no pueden devolver varios valores a la vez.
     */
    private static final class Contexto {

        private final Elemento[] elementos;
        private final List<Paso> pasos = new ArrayList<>();
        private long comparaciones = 0;
        private int profundidadMaxima = 0;

        private Contexto(Elemento[] elementos) {
            this.elementos = elementos;
        }
    }

    // ------------------------------------------------------------------------
    // NÚCLEO DEL ALGORITMO
    // ------------------------------------------------------------------------

    /**
     * Ordena a los jugadores por <b>puntaje descendente</b> aplicando MergeSort y
     * devuelve la traza completa del proceso junto con el ranking final.
     *
     * <p>El arreglo pasado como argumento <b>no</b> se modifica: se trabaja sobre
     * copias internas. El valor de retorno aporta el original conservado, el
     * ranking ordenado, la traza y los contadores.</p>
     *
     * @param jugadores arreglo de jugadores a ordenar; no puede ser {@code null}
     *                  ni contener elementos {@code null}
     * @return resultado con el detalle del ordenamiento
     * @throws IllegalArgumentException si el arreglo es {@code null} o contiene
     *         algún jugador {@code null}
     */
    public static Resultado ordenar(Jugador[] jugadores) {
        // --------------------------------------------------------------------
        // VALIDACION DE ENTRADA
        // Un arreglo null no puede recorrerse. Un jugador null dentro del arreglo
        // no tiene puntaje y romperia la comparacion. Un arreglo vacio o de un
        // solo jugador SI es valido: cae en el caso base.
        // --------------------------------------------------------------------
        if (jugadores == null) {
            throw new IllegalArgumentException("El arreglo de jugadores no puede ser null.");
        }
        for (int i = 0; i < jugadores.length; i++) {
            if (jugadores[i] == null) {
                throw new IllegalArgumentException(
                        "El jugador en la posicion " + i + " no puede ser null.");
            }
        }

        // Copia del estado inicial para poder mostrarlo/verificarlo despues.
        Jugador[] original = jugadores.clone();

        // Se envuelve cada jugador con su posicion original. Esto permite
        // verificar la estabilidad: en un empate deben conservarse las
        // posiciones relativas originales.
        int n = jugadores.length;
        Elemento[] elementos = new Elemento[n];
        for (int i = 0; i < n; i++) {
            elementos[i] = new Elemento(jugadores[i], i);
        }

        // --------------------------------------------------------------------
        // ESPACIO AUXILIAR O(n)
        // La fusion necesita un arreglo auxiliar para intercalar. Se reserva UNA
        // SOLA VEZ (tamano n) y se reutiliza en todas las fusiones: esa es la
        // fuente del espacio auxiliar O(n) del algoritmo.
        // --------------------------------------------------------------------
        Elemento[] auxiliar = new Elemento[n];

        Contexto contexto = new Contexto(elementos);

        // Llamada inicial (raiz de la recursion). Para n == 0 el rango es
        // [0, -1], que recae de inmediato en el caso base.
        mergeSort(contexto, auxiliar, 0, n - 1, 0);

        // --------------------------------------------------------------------
        // CONSTRUCCION DEL RESULTADO
        // Se extraen los jugadores ordenados y, en paralelo, sus posiciones
        // originales (para poder verificar la estabilidad).
        // --------------------------------------------------------------------
        Jugador[] ordenado = new Jugador[n];
        int[] posiciones = new int[n];
        for (int i = 0; i < n; i++) {
            ordenado[i] = elementos[i].jugador;
            posiciones[i] = elementos[i].posicionOriginal;
        }

        return new Resultado(original, ordenado, posiciones, contexto.pasos,
                contexto.comparaciones, contexto.profundidadMaxima);
    }

    /**
     * Ordena recursivamente el rango {@code [inicio, fin]} aplicando el esquema
     * <b>divide y vencerás</b>.
     *
     * <p><b>Dividir:</b> calcula el punto medio y separa el rango en
     * {@code [inicio, medio]} y {@code [medio + 1, fin]}. <b>Vencer:</b> ordena
     * recursivamente cada mitad. <b>Combinar:</b> fusiona las dos mitades ya
     * ordenadas. <b>Caso base:</b> si el rango tiene cero o un elemento, ya está
     * ordenado y se retorna sin hacer nada.</p>
     *
     * @param contexto    acumulador del proceso
     * @param auxiliar    arreglo auxiliar reutilizable de tamaño {@code n}
     * @param inicio      índice inicial del rango (inclusive)
     * @param fin         índice final del rango (inclusive)
     * @param profundidad profundidad de recursión (la llamada raíz es 0)
     */
    private static void mergeSort(Contexto contexto, Elemento[] auxiliar,
                                  int inicio, int fin, int profundidad) {
        // Se registra la máxima profundidad alcanzada para razonar sobre el
        // tamaño de la pila recursiva (O(log n)).
        if (profundidad > contexto.profundidadMaxima) {
            contexto.profundidadMaxima = profundidad;
        }

        // --------------------------------------------------------------------
        // CASO BASE (condicion de corte)
        // Un rango con cero o un elemento ya esta ordenado: no se divide ni se
        // fusiona. 'inicio > fin' ocurre solo para el arreglo vacio.
        // --------------------------------------------------------------------
        if (inicio >= fin) {
            contexto.pasos.add(Paso.casoBase(profundidad, inicio, fin,
                    copiarJugadores(contexto.elementos, inicio, fin)));
            return;
        }

        // --------------------------------------------------------------------
        // DIVIDIR: partir el rango en dos mitades aproximadamente iguales.
        // 'medio' queda como el ultimo indice de la mitad izquierda. La mitad
        // izquierda tiene ceil(m/2) elementos y la derecha floor(m/2), siendo m
        // el tamano del rango. La mitad izquierda contiene los jugadores que
        // estaban ANTES: esto es lo que sostiene la estabilidad.
        // --------------------------------------------------------------------
        int medio = inicio + (fin - inicio) / 2;

        // Foto del rango y de sus dos partes antes de ordenar, para mostrar la
        // division.
        contexto.pasos.add(Paso.dividir(profundidad, inicio, fin,
                copiarJugadores(contexto.elementos, inicio, fin),
                copiarJugadores(contexto.elementos, inicio, medio),
                copiarJugadores(contexto.elementos, medio + 1, fin)));

        // --------------------------------------------------------------------
        // LLAMADAS RECURSIVAS: resolver cada mitad de forma independiente.
        // Al volver, [inicio..medio] y [medio+1..fin] ya estan ordenados cada uno.
        // --------------------------------------------------------------------
        mergeSort(contexto, auxiliar, inicio, medio, profundidad + 1);
        mergeSort(contexto, auxiliar, medio + 1, fin, profundidad + 1);

        // --------------------------------------------------------------------
        // COMBINAR (FUSIONAR)
        // Aunque las dos mitades ya estan ordenadas, el rango completo todavia no
        // lo esta: la fusion las intercala en una sola secuencia descendente.
        // --------------------------------------------------------------------
        fusionar(contexto, auxiliar, inicio, medio, fin, profundidad);
    }

    /**
     * <b>Lógica de fusión.</b> Intercala las dos mitades <b>ya ordenadas</b>
     * {@code [inicio, medio]} y {@code [medio + 1, fin]} en una única secuencia
     * con puntaje <b>descendente</b>, dejando el resultado dentro del arreglo.
     *
     * <p>Primero copia el rango al arreglo auxiliar. Luego recorre el rango
     * llenándolo con el primer jugador disponible de cada mitad, eligiendo el de
     * <b>mayor puntaje</b>. Cuando una mitad se agota, copia el resto de la otra,
     * que ya está ordenada.</p>
     *
     * <p><b>Comparación sin resta.</b> Se usa {@code puntajeDerecha > puntajeIzquierda}
     * (operador relacional de {@code int}), nunca una resta, para no arriesgar un
     * desbordamiento.</p>
     *
     * <p><b>Estabilidad.</b> La comparación es <i>estricta</i>: si los puntajes
     * son iguales la condición es falsa y se elige el de la <b>izquierda</b>, que
     * estaba antes en el arreglo original. Así los empatados conservan su orden
     * relativo.</p>
     *
     * @param contexto    acumulador del proceso
     * @param auxiliar    arreglo auxiliar reutilizable
     * @param inicio      índice inicial del rango (inclusive)
     * @param medio       último índice de la mitad izquierda
     * @param fin         índice final del rango (inclusive)
     * @param profundidad profundidad de recursión del rango
     */
    private static void fusionar(Contexto contexto, Elemento[] auxiliar,
                                 int inicio, int medio, int fin, int profundidad) {
        Elemento[] elementos = contexto.elementos;

        // Se guardan las dos mitades (ya ordenadas) para poder mostrarlas.
        Jugador[] izquierda = copiarJugadores(elementos, inicio, medio);
        Jugador[] derecha = copiarJugadores(elementos, medio + 1, fin);

        // Copia del rango al auxiliar: a partir de aqui el arreglo se puede
        // sobrescribir sin perder los valores de origen.
        for (int k = inicio; k <= fin; k++) {
            auxiliar[k] = elementos[k];
        }

        // 'i' recorre la mitad izquierda y 'j' la derecha, cada uno sobre el
        // auxiliar. 'k' es la posicion de escritura en el arreglo.
        int i = inicio;
        int j = medio + 1;
        int comparacionesFusion = 0;

        for (int k = inicio; k <= fin; k++) {
            if (i > medio) {
                // La mitad izquierda se agoto: queda la derecha, ya ordenada.
                elementos[k] = auxiliar[j];
                j++;
            } else if (j > fin) {
                // La mitad derecha se agoto: queda la izquierda, ya ordenada.
                elementos[k] = auxiliar[i];
                i++;
            } else {
                // Ambas mitades tienen elementos: se comparan sus primeros
                // disponibles y se toma el de MAYOR puntaje (descendente).
                comparacionesFusion++;
                contexto.comparaciones++;

                // Comparacion directa de enteros (NO se restan los puntajes para
                // evitar desbordamiento). Condicion estricta '>': ante un empate
                // (puntajes iguales) es falsa y se toma el de la izquierda, que
                // aparecio antes en el arreglo original => se preserva el orden.
                int puntajeIzquierda = auxiliar[i].jugador.getPuntaje();
                int puntajeDerecha = auxiliar[j].jugador.getPuntaje();
                if (puntajeDerecha > puntajeIzquierda) {
                    elementos[k] = auxiliar[j];
                    j++;
                } else {
                    elementos[k] = auxiliar[i];
                    i++;
                }
            }
        }

        // Foto del resultado de la fusion.
        contexto.pasos.add(Paso.fusionar(profundidad, inicio, fin, izquierda,
                derecha, copiarJugadores(elementos, inicio, fin),
                comparacionesFusion));
    }

    /**
     * Copia el rango cerrado {@code [desde, hasta]} como un arreglo de
     * {@link Jugador}. Si el rango es vacío ({@code desde > hasta}) devuelve un
     * arreglo sin elementos.
     *
     * @param elementos arreglo de origen (envoltorios internos)
     * @param desde     índice inicial (inclusive)
     * @param hasta     índice final (inclusive)
     * @return copia de los jugadores del rango
     */
    private static Jugador[] copiarJugadores(Elemento[] elementos,
                                             int desde, int hasta) {
        if (desde > hasta) {
            return new Jugador[0];
        }
        Jugador[] copia = new Jugador[hasta - desde + 1];
        for (int k = desde; k <= hasta; k++) {
            copia[k - desde] = elementos[k].jugador;
        }
        return copia;
    }

    // ------------------------------------------------------------------------
    // VERIFICACIÓN
    // ------------------------------------------------------------------------

    /**
     * Verifica que el ranking esté ordenado de forma <b>descendente</b>
     * ({@code jugadores[i].puntaje >= jugadores[i+1].puntaje} para todo índice
     * válido).
     *
     * @param jugadores ranking a verificar
     * @return {@code true} si está ordenado de mayor a menor puntaje
     * @throws IllegalArgumentException si {@code jugadores} es {@code null}
     */
    public static boolean estaOrdenadoDescendente(Jugador[] jugadores) {
        if (jugadores == null) {
            throw new IllegalArgumentException("El arreglo de jugadores no puede ser null.");
        }
        for (int i = 0; i < jugadores.length - 1; i++) {
            if (jugadores[i].getPuntaje() < jugadores[i + 1].getPuntaje()) {
                return false;
            }
        }
        return true;
    }

    /**
     * Comprueba que el ranking ordenado contenga <b>exactamente</b> los mismos
     * jugadores que el original, con la misma multiplicidad (ninguno perdido,
     * agregado ni alterado).
     *
     * @param original  arreglo antes de ordenar
     * @param resultado arreglo después de ordenar
     * @return {@code true} si ambos contienen los mismos jugadores con la misma
     *         multiplicidad
     * @throws IllegalArgumentException si alguno de los arreglos es {@code null}
     */
    public static boolean conservaJugadores(Jugador[] original, Jugador[] resultado) {
        if (original == null || resultado == null) {
            throw new IllegalArgumentException("Los arreglos no pueden ser null.");
        }
        if (original.length != resultado.length) {
            return false;
        }
        // Se cuenta cuantas veces aparece cada jugador exacto (nombre y puntaje)
        // en el original y se "descuenta" al recorrer el resultado. Si alguno no
        // aparece o ya se agoto, no se conservan los jugadores originales.
        java.util.Map<Jugador, Integer> conteo = new java.util.HashMap<>();
        for (Jugador jugador : original) {
            conteo.merge(jugador, 1, Integer::sum);
        }
        for (Jugador jugador : resultado) {
            Integer restante = conteo.get(jugador);
            if (restante == null || restante == 0) {
                return false;
            }
            conteo.put(jugador, restante - 1);
        }
        return true;
    }

    // ------------------------------------------------------------------------
    // PRESENTACIÓN
    // ------------------------------------------------------------------------

    /**
     * Muestra por la salida indicada el arreglo original, la traza paso a paso de
     * la recursión (divisiones, casos base y fusiones, con su nivel) y el ranking
     * ordenado con la verificación del orden descendente, la conservación de
     * jugadores y la estabilidad.
     *
     * @param resultado resultado devuelto por {@link #ordenar(Jugador[])}
     * @param salida    flujo de salida a utilizar
     */
    public static void imprimirResultado(Resultado resultado, PrintStream salida) {
        salida.println("Arreglo original: " + formatear(resultado.getOriginal()));
        salida.println();

        List<Paso> pasos = resultado.getPasos();
        salida.println("Traza de la recursion (cada nivel va indentado):");
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
                                    + "  (%d jugador%s: ya esta ordenado)%n",
                            sangria, paso.getProfundidad(), paso.getInicio(),
                            paso.getFin(), formatear(paso.getSubarreglo()),
                            paso.getSubarreglo().length,
                            paso.getSubarreglo().length == 1 ? "" : "es");
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

        Jugador[] ordenado = resultado.getOrdenado();
        salida.println();
        salida.println("Ranking ordenado (mayor a menor puntaje):");
        for (int i = 0; i < ordenado.length; i++) {
            salida.printf("  %2d. %-12s %d%n", i + 1,
                    ordenado[i].getNombre(), ordenado[i].getPuntaje());
        }
        salida.println();
        salida.println("Verificacion (orden descendente por puntaje): "
                + (estaOrdenadoDescendente(ordenado) ? "OK" : "FALLO"));
        salida.println("Verificacion (conserva todos los jugadores): "
                + (conservaJugadores(resultado.getOriginal(), ordenado) ? "OK" : "FALLO"));
        salida.println("Verificacion (estable: empates conservan su orden original): "
                + (resultado.esEstable() ? "OK" : "FALLO"));
        salida.printf("Divisiones: %d | Casos base: %d | Fusiones: %d | "
                        + "Comparaciones: %d | Profundidad maxima: %d%n",
                resultado.getCantidadDivisiones(), resultado.getCantidadCasosBase(),
                resultado.getCantidadFusiones(), resultado.getComparaciones(),
                resultado.getProfundidadMaxima());
    }

    /**
     * Explica conceptualmente la estrategia divide y vencerás de MergeSort, su
     * estabilidad, su disventaja de espacio y el cuidado con la comparación sin
     * resta.
     *
     * @param salida flujo de salida
     */
    private static void imprimirExplicacion(PrintStream salida) {
        salida.println("== Por que MergeSort y como funciona ==");
        salida.println();
        salida.println("DIVIDIR: descompone el problema en partes mas pequenas.");
        salida.println("  Se parte el arreglo en dos mitades de tamano casi igual y se");
        salida.println("  repite recursivamente en cada mitad, hasta llegar a rangos de");
        salida.println("  cero o un jugador (caso base), que ya estan ordenados.");
        salida.println();
        salida.println("FUSIONAR: combina las soluciones de esas partes.");
        salida.println("  Al volver de la recursion, cada mitad ya esta ordenada. La");
        salida.println("  fusion intercala las dos mitades mirando el primer jugador");
        salida.println("  disponible de cada una y tomando el de MAYOR puntaje, hasta");
        salida.println("  reconstruir el ranking completo de mayor a menor.");
        salida.println();
        salida.println("Comparacion de puntajes: se usan operadores relacionales");
        salida.println("  ('puntajeDerecha > puntajeIzquierda'). NO se restan puntajes,");
        salida.println("  porque la resta puede desbordar (por ejemplo MAX_VALUE menos");
        salida.println("  MIN_VALUE no entra en un int) y daria un signo equivocado.");
        salida.println();
        salida.println("Estabilidad: la comparacion es estricta ('>'). Si los puntajes");
        salida.println("  son iguales, gana SIEMPRE el de la mitad izquierda, que estaba");
        salida.println("  antes en el arreglo original; asi los empates conservan su");
        salida.println("  orden original.");
        salida.println();
        salida.println("Complejidad temporal: O(n log n).");
        salida.println("  Dividir por la mitad produce un arbol de recursion de log n");
        salida.println("  niveles; en cada nivel las fusiones recorren los n jugadores en");
        salida.println("  total, o sea O(n) por nivel. Por lo tanto el tiempo es");
        salida.println("  O(n) * O(log n) = O(n log n), en mejor, promedio y peor caso.");
        salida.println("  Es por eso que resulta adecuado para rankings con muchos");
        salida.println("  jugadores: su tiempo crece de forma casi lineal.");
        salida.println("Espacio auxiliar: O(n).");
        salida.println("  La fusion necesita un arreglo auxiliar de tamano n para");
        salida.println("  intercalar (se reserva una sola vez y se reutiliza). A eso se");
        salida.println("  suma la pila de recursion, que es O(log n). Esta es la");
        salida.println("  desventaja de MergeSort frente a algoritmos in-place.");
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

    /**
     * Construye una representación legible de un arreglo de jugadores para la
     * salida.
     *
     * @param jugadores arreglo a representar
     * @return cadena con los jugadores entre corchetes
     *         (ej.: {@code [Lucia (1500), Ana (1200), Pedro (900)]})
     */
    private static String formatear(Jugador[] jugadores) {
        StringBuilder texto = new StringBuilder("[");
        for (int i = 0; i < jugadores.length; i++) {
            if (i > 0) {
                texto.append(", ");
            }
            texto.append(jugadores[i]);
        }
        return texto.append("]").toString();
    }

    // ------------------------------------------------------------------------
    // PROGRAMA PRINCIPAL
    // ------------------------------------------------------------------------

    /**
     * Punto de entrada. Por defecto muestra el ejemplo principal con la traza
     * completa, la explicación del algoritmo y la batería de casos de prueba.
     * Con el argumento {@code interactivo} (o {@code -i}) lee jugadores por
     * consola y muestra el ranking paso a paso.
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
     * Ejecuta el ejemplo principal del enunciado (Ana, Pedro y Lucia),
     * mostrando la traza completa del proceso.
     *
     * @param salida flujo de salida
     */
    private static void ejecutarEjemploPrincipal(PrintStream salida) {
        Jugador[] jugadores = {
            new Jugador("Ana", 1200),
            new Jugador("Pedro", 900),
            new Jugador("Lucia", 1500)
        };

        salida.println("== Ranking de jugadores por MergeSort: ejemplo principal ==");
        salida.println();

        Resultado resultado = ordenar(jugadores.clone());
        imprimirResultado(resultado, salida);
        salida.println();
    }

    /**
     * Flujo interactivo: solicita la cantidad de jugadores, el nombre y el
     * puntaje de cada uno, y muestra el ranking.
     *
     * @param entrada lector de la entrada estándar
     * @param salida  flujo de salida
     */
    private static void ejecutarInteractivo(Scanner entrada, PrintStream salida) {
        salida.println("== Ranking de jugadores por MergeSort ==");

        int cantidad = leerCantidad(entrada, salida);
        Jugador[] jugadores = new Jugador[cantidad];
        for (int i = 0; i < cantidad; i++) {
            String nombre = leerNombre(entrada, salida,
                    "Ingrese el nombre del jugador " + (i + 1) + " de " + cantidad + ": ");
            int puntaje = leerEntero(entrada, salida,
                    "Ingrese el puntaje de " + nombre + ": ");
            jugadores[i] = new Jugador(nombre, puntaje);
        }

        salida.println();
        Resultado resultado = ordenar(jugadores);
        imprimirResultado(resultado, salida);
        salida.println("Estado: " + (estaOrdenadoDescendente(resultado.getOrdenado())
                ? "OK" : "FALLO"));
    }

    /**
     * Lee una cantidad de jugadores, rechazando valores negativos.
     *
     * @param entrada lector de entrada
     * @param salida  flujo de salida
     * @return cantidad no negativa ingresada por el usuario
     */
    private static int leerCantidad(Scanner entrada, PrintStream salida) {
        while (true) {
            int cantidad = leerEntero(entrada, salida,
                    "Ingrese la cantidad de jugadores: ");
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
     * Ejecuta los casos de prueba exigidos en el enunciado: jugadores en
     * distintos órdenes, un arreglo ya descendente, uno inverso, puntajes
     * repetidos (para comprobar la estabilidad), uno con todos empatados, un solo
     * jugador y un arreglo vacío; además, casos límite (puntajes extremos de
     * {@code int}, entrada {@code null} y jugador {@code null}).
     *
     * <p>Para cada caso se verifica: que el original coincida con la entrada,
     * que el ranking quede en orden <b>descendente</b>, que conserve todos los
     * jugadores, que sea <b>estable</b> y que coincida exactamente con un ranking
     * de referencia obtenido con un ordenamiento estable.</p>
     *
     * @param salida flujo de salida
     */
    private static void ejecutarCasosDePrueba(PrintStream salida) {
        salida.println("== Casos de prueba exigidos ==");

        // 1) EJEMPLO PRINCIPAL del enunciado. Ranking esperado:
        //    Lucia (1500), Ana (1200), Pedro (900).
        verificarCaso("ejemplo principal", new Jugador[] {
            new Jugador("Ana", 1200),
            new Jugador("Pedro", 900),
            new Jugador("Lucia", 1500)
        }, salida);

        // 2) OTRO ORDEN (Lucia ya al frente). Debe dar el mismo ranking.
        verificarCaso("otro orden", new Jugador[] {
            new Jugador("Lucia", 1500),
            new Jugador("Ana", 1200),
            new Jugador("Pedro", 900)
        }, salida);

        // 3) YA DESCENDENTE: MergeSort no se degrada con entradas ordenadas.
        verificarCaso("ya descendente", new Jugador[] {
            new Jugador("Lucia", 1500),
            new Jugador("Ana", 1200),
            new Jugador("Pedro", 900),
            new Jugador("Ursula", 100)
        }, salida);

        // 4) ORDEN INVERSO (ascendente): debe invertirse por completo.
        verificarCaso("orden inverso", new Jugador[] {
            new Jugador("Ursula", 100),
            new Jugador("Pedro", 900),
            new Jugador("Ana", 1200),
            new Jugador("Lucia", 1500)
        }, salida);

        // 5) PUNTAJES REPETIDOS: clave para la estabilidad. Ana y Beto empatan
        //    en 1000 (Ana estaba antes), y Caro y Dora empatan en 1500 (Caro
        //    estaba antes). El ranking estable debe respetar esos ordenes:
        //    Caro (1500), Dora (1500), Ana (1000), Beto (1000), Eze (900).
        verificarCaso("puntajes repetidos", new Jugador[] {
            new Jugador("Ana", 1000),
            new Jugador("Caro", 1500),
            new Jugador("Beto", 1000),
            new Jugador("Eze", 900),
            new Jugador("Dora", 1500)
        }, salida);

        // 6) TODOS EMPATADOS: el ranking debe ser EXACTAMENTE el orden original.
        verificarCaso("todos empatados", new Jugador[] {
            new Jugador("Zoe", 500),
            new Jugador("Mario", 500),
            new Jugador("Lara", 500),
            new Jugador("Ivan", 500)
        }, salida);

        salida.println();
        salida.println("== Casos limite adicionales ==");

        // 7) UN SOLO JUGADOR: caso base directo.
        verificarCaso("un jugador", new Jugador[] {
            new Jugador("Solo", 42)
        }, salida);

        // 8) ARREGLO VACIO: caso base directo, sin divisiones ni fusiones.
        verificarCaso("vacio", new Jugador[] {}, salida);

        // 9) PUNTAJES EXTREMOS: si se restaran los puntajes, MAX_VALUE - MIN_VALUE
        //    desbordaria y el orden saldria mal. La comparacion directa lo evita.
        verificarCaso("puntajes extremos", new Jugador[] {
            new Jugador("Min", Integer.MIN_VALUE),
            new Jugador("Max", Integer.MAX_VALUE),
            new Jugador("Cero", 0),
            new Jugador("Min2", Integer.MIN_VALUE)
        }, salida);

        // 10) Entrada null: debe rechazarse con excepcion descriptiva.
        verificarArregloNulo(salida);

        // 11) Jugador null dentro del arreglo: debe rechazarse.
        verificarJugadorNulo(salida);

        salida.println();
        salida.println("Todas las verificaciones finalizaron. "
                + "Revisa que no aparezca la palabra FALLO.");
    }

    /**
     * Ordena una copia del arreglo de entrada y comprueba toda la corrección del
     * resultado: que el original coincida con la entrada, que el ranking quede
     * descendente, que conserve los jugadores, que sea estable, y que coincida
     * con un ranking de referencia estable.
     *
     * @param nombre  etiqueta del caso
     * @param entrada arreglo original (no se modifica)
     * @param salida  flujo de salida
     */
    private static void verificarCaso(String nombre, Jugador[] entrada,
                                      PrintStream salida) {
        // Se ordena una COPIA para no mutar el arreglo de prueba original.
        Resultado resultado = ordenar(entrada.clone());

        List<String> errores = new ArrayList<>();

        // 1) El arreglo original mostrado debe coincidir con la entrada.
        if (!Arrays.equals(resultado.getOriginal(), entrada)) {
            errores.add("el arreglo original no coincide con la entrada");
        }

        // 2) El ranking debe quedar en orden descendente por puntaje.
        if (!estaOrdenadoDescendente(resultado.getOrdenado())) {
            errores.add("el ranking no esta ordenado de mayor a menor");
        }

        // 3) Debe conservar exactamente los mismos jugadores.
        if (!conservaJugadores(entrada, resultado.getOrdenado())) {
            errores.add("el ranking no conserva todos los jugadores originales");
        }

        // 4) Debe ser estable (los empates conservan el orden original).
        if (!resultado.esEstable()) {
            errores.add("la estabilidad no se conservo ante puntajes repetidos");
        }

        // 5) Verdad de referencia: ranking obtenido con un ordenamiento estable
        //    (puntaje descendente, y ante empate la posicion original creciente).
        Jugador[] referencia = rankingEsperado(entrada);
        if (!Arrays.equals(resultado.getOrdenado(), referencia)) {
            errores.add("el ranking no coincide con el orden esperado "
                    + formatear(referencia));
        }

        // --- Salida del caso ---
        salida.println("Caso: " + nombre);
        salida.println("  Original: " + formatear(entrada));
        salida.println("  Ranking : " + formatear(resultado.getOrdenado()));
        salida.printf("  Divisiones: %d | Casos base: %d | Fusiones: %d | "
                        + "Comparaciones: %d | Profundidad: %d%n",
                resultado.getCantidadDivisiones(), resultado.getCantidadCasosBase(),
                resultado.getCantidadFusiones(), resultado.getComparaciones(),
                resultado.getProfundidadMaxima());
        salida.println("  Estado: " + (errores.isEmpty() ? "OK"
                : "FALLO -> " + String.join("; ", errores)));
        salida.println();
    }

    /**
     * Calcula el ranking esperado usando un ordenamiento <b>estable</b> de
     * referencia: por puntaje descendente y, ante empate, por la posición
     * original (el que estaba antes queda antes). Sirve como verdad de referencia
     * independiente del MergeSort implementado.
     *
     * @param entrada arreglo original
     * @return nuevo arreglo con el ranking esperado
     */
    private static Jugador[] rankingEsperado(Jugador[] entrada) {
        List<Integer> indices = new ArrayList<>();
        for (int i = 0; i < entrada.length; i++) {
            indices.add(i);
        }
        // Collections.sort es estable (TimSort): al desempatar por puntaje sin
        // tocar el orden de los indices iniciales, conserva la posicion original.
        Collections.sort(indices, (a, b) -> {
            int diferencia = Integer.compare(entrada[b].getPuntaje(),
                    entrada[a].getPuntaje()); // descendente por puntaje
            if (diferencia != 0) {
                return diferencia;
            }
            return Integer.compare(a, b); // empate: posicion original creciente
        });
        Jugador[] ranking = new Jugador[entrada.length];
        for (int i = 0; i < indices.size(); i++) {
            ranking[i] = entrada[indices.get(i)];
        }
        return ranking;
    }

    /**
     * Comprueba que {@link #ordenar(Jugador[])} rechace un arreglo {@code null}.
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
     * Comprueba que {@link #ordenar(Jugador[])} rechace un arreglo con un jugador
     * {@code null} intercalado.
     *
     * @param salida flujo de salida
     */
    private static void verificarJugadorNulo(PrintStream salida) {
        try {
            ordenar(new Jugador[] {new Jugador("Ana", 1000), null,
                new Jugador("Pedro", 900)});
            salida.println("ordenar([Ana, null, Pedro]) -> FALLO "
                    + "(no se lanzo ninguna excepcion)");
        } catch (IllegalArgumentException excepcion) {
            salida.printf("ordenar([Ana, null, Pedro]) -> OK (rechazado: %s)%n",
                    excepcion.getMessage());
        }
    }
}

/*
 * ============================================================================
 * NOTAS DE IMPLEMENTACIÓN
 * ============================================================================
 * 1. MODELO: 'Jugador' es inmutable (nombre + puntaje) y valida el nombre. Se
 *    define equals/hashCode por nombre y puntaje para poder comparar y verificar
 *    listas.
 *
 * 2. ELECCIÓN DE MERGESORT: tiempo O(n log n) en mejor, promedio y peor caso
 *    (predecible y adecuado para rankings grandes) y estabilidad (los empates
 *    conservan su orden original). Desventaja: espacio auxiliar O(n) por el
 *    arreglo de fusión.
 *
 * 3. ORDEN DESCENDENTE Y ESTABILIDAD: en la fusión se toma el de MAYOR puntaje
 *    con la condición estricta 'puntajeDerecha > puntajeIzquierda'. Ante empate
 *    la condición es falsa y se toma el de la IZQUIERDA (el que estaba antes en
 *    el arreglo original), preservando el orden relativo. Para verificarlo, cada
 *    jugador se envuelve internamente con su posición original; 'esEstable()'
 *    comprueba que en cada empate las posiciones consecutivas sean crecientes.
 *
 * 4. SIN RESTA: nunca se calcula 'puntajeA - puntajeB'; se comparan los enteros
 *    directamente con '>' para evitar desbordamiento (p. ej. MAX_VALUE menos
 *    MIN_VALUE no cabe en un int). El caso 'puntajes extremos' lo demuestra.
 *
 * 5. ESPACIO AUXILIAR O(n): el arreglo auxiliar se reserva UNA sola vez y se
 *    reutiliza en todas las fusiones. A eso se suma la pila de recursión,
 *    O(log n).
 *
 * 6. SEPARACIÓN DE RESPONSABILIDADES: 'ordenar' resuelve el algoritmo y devuelve
 *    un 'Resultado' inmutable con la traza y los contadores; 'imprimirResultado'
 *    se encarga de la presentación. Así el algoritmo se puede probar sin depender
 *    de la salida por consola.
 *
 * 7. VERIFICACIÓN: cada caso comprueba el orden descendente, la conservación de
 *    los jugadores (misma multiplicidad), la estabilidad y la coincidencia con
 *    un ranking de referencia calculado con un ordenamiento estable. Además, se
 *    prueba que se rechacen 'null' y jugadores 'null'.
 * ============================================================================
 */

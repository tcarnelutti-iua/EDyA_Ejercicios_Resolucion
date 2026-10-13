/*
 * ============================================================================
 * PROMPT ORIGINAL
 * ============================================================================
 * Implementá un sistema simple de historial de navegación usando una pila
 * enlazada y guardá la solución en:
 * Guía_Pilas_colas_lista_enlazada/Ejercicio_5/HistorialNavegacion.java
 *
 * Requisitos:
 * - Definí una clase pública HistorialNavegacion.
 * - Representá las páginas mediante sus direcciones URL (String).
 * - Usá una pila enlazada simple para guardar las páginas anteriores. Mantené
 *   la página actual por separado: al visitar la primera página, esta pasa a
 *   ser la actual; al visitar otra página, apilá la actual y luego actualizala.
 * - Implementá estas operaciones:
 *     - visitar(String pagina): establece la primera página actual o navega a
 *       otra página, conservando la página actual como anterior.
 *     - volverAtras(): recupera de la pila la página anterior y la establece
 *       como actual.
 *     - consultarPaginaActual(): devuelve la página actual.
 *     - imprimirHistorial(): muestra la página actual y las páginas anteriores,
 *       desde la más reciente hasta la más antigua, sin modificar la pila.
 * - La pila representa correctamente "volver atrás" porque la última página
 *   visitada antes de la actual es la primera que debe recuperarse. Por eso,
 *   volver atrás consiste en desapilar la página anterior más reciente.
 * - Si se intenta volver atrás cuando no hay páginas anteriores, informá que no
 *   es posible y conservá la página actual.
 * - Si todavía no se visitó ninguna página, consultar la página actual debe
 *   informarlo claramente.
 * - No agregues funcionalidad de avanzar; no forma parte de este ejercicio.
 * - Visitar, volver atrás y consultar la página actual deben ser O(1). Imprimir
 *   el historial debe ser O(n).
 * - Incluí un main breve que pruebe varias visitas, consultas, impresiones y el
 *   caso en que no haya páginas anteriores.
 * - Mantené la implementación de la pila enlazada en el mismo archivo y no uses
 *   arrays ni colecciones de Java para almacenar el historial.
 *
 * Incluí al comienzo del archivo, como comentario, este prompt completo y
 * explicá que una pila aplica LIFO: la última página anterior guardada es la
 * primera que se recupera al volver atrás. Aclarà si hubo ajustes posteriores.
 * Si no modificaste los requisitos, indicá: "Ajustes posteriores: ninguno".
 *
 * Mantené la solución en el archivo indicado y evitá agregar otros archivos.
 *
 * ----------------------------------------------------------------------------
 * AJUSTES POSTERIORES
 * ----------------------------------------------------------------------------
 * Ajustes posteriores: ninguno
 *
 * ============================================================================
 * EXPLICACIÓN: POR QUÉ UNA PILA APLICA LIFO
 * ============================================================================
 * Una pila es una estructura LIFO ("last in, first out"): el último elemento
 * que se apila es el primero que se desapila. En un historial de navegación,
 * cada vez que se visita una página nueva, la página que era actual se apila
 * como "página anterior". Al volver atrás, se debe recuperar la página visitada
 * inmediatamente antes de la actual, es decir, la última que se apiló. Por eso,
 * "volver atrás" consiste simplemente en desapilar el tope: la última página
 * anterior guardada es la primera que se recupera. Ese comportamiento es
 * exactamente LIFO, que es lo que una pila garantiza.
 * ============================================================================
 */

/**
 * Historial de navegación simple basado en una <em>pila enlazada</em>.
 *
 * <p>La página actual se mantiene aparte (en {@link #paginaActual}) y las
 * páginas anteriores se guardan en una pila enlazada simple cuyo tope es
 * {@link #tope}. Al visitar otra página, la actual se apila como anterior y la
 * nueva pasa a ser la actual; al volver atrás, se desapila el tope y pasa a ser
 * la página actual.
 *
 * <p>Esta estructura respeta la semántica LIFO propia de una pila: la última
 * página anterior guardada (la visitada más recientemente antes de la actual)
 * es la primera que se recupera al volver atrás.
 *
 * <p>No se utilizan arrays ni colecciones de Java: las páginas anteriores se
 * almacenan únicamente en instancias de {@link Nodo}.
 *
 * <p>Complejidades: {@link #visitar(String)}, {@link #volverAtras()} y
 * {@link #consultarPaginaActual()} son O(1); {@link #imprimirHistorial()} es
 * O(n).
 */
public class HistorialNavegacion {

    /**
     * Nodo de la lista enlazada simple que forma la pila de páginas anteriores.
     *
     * <p>Cada nodo guarda una dirección URL y una referencia al siguiente nodo.
     * El último nodo apunta a {@code null}.
     */
    private static final class Nodo {

        /** Dirección URL almacenada en este nodo. */
        private final String url;

        /** Referencia al siguiente nodo de la pila (null si es el último). */
        private Nodo siguiente;

        /**
         * Crea un nodo con una URL y una referencia al siguiente nodo.
         *
         * @param url       dirección URL a almacenar.
         * @param siguiente nodo que le sigue en la pila.
         */
        private Nodo(String url, Nodo siguiente) {
            this.url = url;
            this.siguiente = siguiente;
        }
    }

    /** Página actualmente visitada; {@code null} si todavía no se visitó ninguna. */
    private String paginaActual;

    /**
     * Tope de la pila de páginas anteriores. Representa la página anterior más
     * reciente (la primera que se recupera al volver atrás).
     */
    private Nodo tope;

    /** Cantidad de páginas anteriores almacenadas en la pila. */
    private int tamanio;

    /**
     * Crea un historial vacío, sin página actual ni páginas anteriores.
     */
    public HistorialNavegacion() {
        this.paginaActual = null;
        this.tope = null;
        this.tamanio = 0;
    }

    /**
     * Visita una página (establece la primera página actual o navega a otra).
     *
     * <p>Si todavía no hay página actual, la URL indicada pasa a ser la actual.
     * Si ya hay una página actual, esta se apila como página anterior y la URL
     * indicada pasa a ser la nueva actual. En ambos casos la operación es O(1),
     * porque apilar inserta al inicio de la lista enlazada.
     *
     * @param pagina dirección URL de la página a visitar.
     * @throws IllegalArgumentException si {@code pagina} es {@code null} o está
     *                                  vacía (una URL no puede ser vacía).
     */
    public void visitar(String pagina) {
        if (pagina == null || pagina.trim().isEmpty()) {
            throw new IllegalArgumentException(
                    "La página a visitar no puede ser nula ni vacía.");
        }

        if (paginaActual == null) {
            // Primera visita: no hay página anterior que conservar.
            paginaActual = pagina;
        } else {
            // Se apila la actual como anterior y la nueva pasa a ser la actual.
            tope = new Nodo(paginaActual, tope);
            tamanio++;
            paginaActual = pagina;
        }
    }

    /**
     * Vuelve a la página anterior desapilando el tope de la pila.
     *
     * <p>Al ser una pila LIFO, el tope es la página anterior más reciente, que
     * es la correcta a recuperar. La operación es O(1).
     *
     * <p>Si no hay páginas anteriores, informa que no es posible volver atrás y
     * conserva la página actual sin modificarla.
     *
     * @return la nueva página actual tras volver atrás; si no fue posible,
     *         devuelve la página actual sin cambios (puede ser {@code null} si
     *         todavía no se visitó ninguna página).
     */
    public String volverAtras() {
        if (tope == null) {
            System.out.println(
                    "No es posible volver atrás: no hay páginas anteriores.");
            return paginaActual;
        }

        // La página anterior más reciente pasa a ser la actual.
        paginaActual = tope.url;
        tope = tope.siguiente; // Desapila: el tope avanza al siguiente nodo.
        tamanio--;
        return paginaActual;
    }

    /**
     * Devuelve la página actual.
     *
     * <p>Si todavía no se visitó ninguna página, lo informa claramente y
     * devuelve {@code null}. La operación es O(1).
     *
     * @return la dirección URL de la página actual, o {@code null} si no hay
     *         ninguna.
     */
    public String consultarPaginaActual() {
        if (paginaActual == null) {
            System.out.println("Todavía no se visitó ninguna página.");
            return null;
        }
        return paginaActual;
    }

    /**
     * Indica si todavía no se visitó ninguna página.
     *
     * @return {@code true} si no hay página actual; {@code false} en caso
     *         contrario.
     */
    public boolean estaVacio() {
        return paginaActual == null;
    }

    /**
     * Muestra por consola la página actual y las páginas anteriores, desde la
     * más reciente hasta la más antigua, sin modificar la pila.
     *
     * <p>Recorre la pila una sola vez, por lo que es O(n). Si todavía no se
     * visitó ninguna página, lo informa.
     */
    public void imprimirHistorial() {
        if (paginaActual == null) {
            System.out.println(
                    "No hay historial: todavía no se visitó ninguna página.");
            return;
        }

        StringBuilder salida = new StringBuilder();
        salida.append("Página actual: ").append(paginaActual);

        if (tope == null) {
            salida.append(System.lineSeparator());
            salida.append("No hay páginas anteriores.");
        } else {
            salida.append(System.lineSeparator());
            salida.append("Páginas anteriores (de la más reciente a la más antigua):");
            int posicion = 1;
            // Se recorre el tope hacia abajo sin desapilar: no modifica la pila.
            Nodo nodo = tope;
            while (nodo != null) {
                salida.append(System.lineSeparator());
                salida.append("  ").append(posicion).append(". ").append(nodo.url);
                nodo = nodo.siguiente;
                posicion++;
            }
        }

        System.out.println(salida);
    }

    /**
     * Devuelve la cantidad de páginas anteriores almacenadas.
     *
     * @return tamaño actual de la pila de páginas anteriores.
     */
    public int cantidadPaginasAnteriores() {
        return tamanio;
    }

    /**
     * Punto de entrada de prueba. Ejercita varias visitas, consultas,
     * impresiones y el caso en que no haya páginas anteriores.
     *
     * @param args argumentos de línea de comandos (no se usan).
     */
    public static void main(String[] args) {
        HistorialNavegacion historial = new HistorialNavegacion();

        System.out.println("== Historial sin páginas ==");
        historial.consultarPaginaActual();
        historial.imprimirHistorial();
        historial.volverAtras(); // No hay páginas anteriores.

        System.out.println();
        System.out.println("== Primera visita ==");
        historial.visitar("https://www.ejemplo.com/inicio");
        System.out.println("Página actual: " + historial.consultarPaginaActual());
        historial.imprimirHistorial();

        System.out.println();
        System.out.println("== Nuevas visitas ==");
        historial.visitar("https://www.ejemplo.com/noticias");
        historial.visitar("https://www.ejemplo.com/deportes");
        historial.visitar("https://www.ejemplo.com/contacto");
        System.out.println("Página actual: " + historial.consultarPaginaActual());
        System.out.println("Páginas anteriores: "
                + historial.cantidadPaginasAnteriores());
        historial.imprimirHistorial();

        System.out.println();
        System.out.println("== Volver atrás ==");
        System.out.println("volverAtras() -> " + historial.volverAtras());
        historial.imprimirHistorial();

        System.out.println();
        System.out.println("== Volver atrás hasta agotar las anteriores ==");
        System.out.println("volverAtras() -> " + historial.volverAtras());
        System.out.println("volverAtras() -> " + historial.volverAtras());
        // Ya no quedan páginas anteriores: debe informarlo y conservar la actual.
        System.out.println("volverAtras() -> " + historial.volverAtras());
        historial.imprimirHistorial();

        System.out.println();
        System.out.println("== Navegar luego de volver atrás ==");
        historial.visitar("https://www.ejemplo.com/busqueda");
        historial.imprimirHistorial();
    }
}

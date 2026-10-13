/*
 * ============================================================================
 * PROMPT ORIGINAL
 * ============================================================================
 * Implementá una cola de enteros usando una lista enlazada simple y guardá la
 * solución en:
 * Guía_Pilas_colas_lista_enlazada/Ejercicio_2/ColaEnteros.java
 *
 * Requisitos:
 * - Definí una clase pública llamada ColaEnteros.
 * - Cada nodo debe contener un entero y una referencia al siguiente nodo.
 * - Mantené referencias head y tail: head representa el frente, del que se
 *   desencola; tail representa el final, donde se encola.
 * - Encolar al final mediante tail y desencolar desde head permite realizar
 *   ambas operaciones en O(1).
 * - Explicá en un comentario por qué se necesitan head y tail: si solo se
 *   usara head, encolar al final requeriría recorrer la lista en O(n), o bien
 *   habría que cambiar el extremo desde el que se desencola y perder la
 *   eficiencia de esa operación.
 * - Implementá estas operaciones:
 *     - encolar(int valor): agrega el valor al final de la cola.
 *     - desencolar(): quita y devuelve el valor del frente.
 *     - consultarFrente(): devuelve el valor del frente sin quitarlo.
 *     - estaVacia(): indica si la cola está vacía.
 *     - contiene(int valor): indica si el valor está en la cola.
 *     - imprimir(): muestra los elementos desde el frente hasta el final, sin
 *       modificar la cola.
 * - Al encolar el primer elemento, head y tail deben apuntar al mismo nodo. Al
 *   desencolar el último, ambas referencias deben quedar vacías.
 * - Si se llama a desencolar() o consultarFrente() con la cola vacía, lanzá
 *   NoSuchElementException con un mensaje claro.
 * - contiene() debe devolver false si la cola está vacía. imprimir() debe
 *   informar que está vacía.
 * - No uses arrays ni colecciones de Java para almacenar los elementos; la
 *   estructura debe ser una lista enlazada simple.
 * - Complejidades esperadas: encolar, desencolar, consultarFrente y estaVacia
 *   en O(1); contiene e imprimir en O(n).
 * - Agregá un main breve que pruebe las operaciones principales y los casos de
 *   cola vacía.
 * - Incluí al comienzo del archivo, como comentario, este prompt completo y
 *   aclarà si hubo ajustes posteriores. Si no modificaste los requisitos,
 *   indicá: "Ajustes posteriores: ninguno".
 * - Mantené la solución en el archivo indicado y evitá agregar otros archivos.
 *
 * Ajustes posteriores: ninguno
 * ============================================================================
 */

import java.util.NoSuchElementException;

/**
 * Cola de enteros implementada sobre una lista enlazada simple con referencias
 * {@code head} y {@code tail}.
 *
 * <p>{@code head} representa el frente de la cola, es decir, el extremo por el
 * que se {@linkplain #desencolar() desencola}. {@code tail} representa el final
 * de la cola, el extremo por el que se {@linkplain #encolar(int) encola}.</p>
 *
 * <h2>¿Por qué se necesitan head y tail?</h2>
 * <p>Una cola debe respetar el orden FIFO: se quita por un extremo (el frente)
 * y se agrega por el otro (el final). Si solo se mantuviera la referencia
 * {@code head}, encolar al final obligaría a recorrer toda la lista hasta el
 * último nodo para enganchar el nuevo elemento, lo que costaría O(n). La
 * alternativa de encolar por el frente con una sola referencia cambiaría el
 * extremo de desencolado y rompería el comportamiento FIFO (o bien obligaría a
 * desencolar recorriendo toda la lista, trasladando el costo O(n) a esa
 * operación). Mantener ambas referencias permite encolar por {@code tail} y
 * desencolar por {@code head}, logrando O(1) en las dos operaciones.</p>
 *
 * <p>No se utilizan arrays ni colecciones de Java: los elementos se almacenan
 * únicamente en instancias de {@link Nodo}.</p>
 */
public class ColaEnteros {

    /**
     * Nodo de la lista enlazada simple.
     *
     * Cada nodo guarda un entero y una referencia al siguiente nodo. El último
     * nodo apunta a {@code null}.
     */
    private static final class Nodo {

        /** Valor entero almacenado en este nodo. */
        private final int valor;

        /** Referencia al siguiente nodo de la lista (null si es el último). */
        private Nodo siguiente;

        /**
         * Crea un nodo con un valor y una referencia al siguiente nodo.
         *
         * @param valor     entero a almacenar.
         * @param siguiente nodo que le sigue en la lista.
         */
        private Nodo(int valor, Nodo siguiente) {
            this.valor = valor;
            this.siguiente = siguiente;
        }
    }

    /** Primer nodo de la lista; representa el frente de la cola. */
    private Nodo head;

    /** Último nodo de la lista; representa el final de la cola. */
    private Nodo tail;

    /** Cantidad de elementos almacenados en la cola. */
    private int tamanio;

    /**
     * Crea una cola vacía.
     */
    public ColaEnteros() {
        this.head = null;
        this.tail = null;
        this.tamanio = 0;
    }

    /**
     * Encola un valor al final de la cola.
     *
     * El nuevo nodo se enlaza detrás de {@code tail} y pasa a ser el nuevo
     * final. No se recorre la lista, por lo que la operación es O(1).
     *
     * @param valor entero a encolar.
     */
    public void encolar(int valor) {
        // El nuevo nodo siempre termina la lista, por eso su "siguiente" es null.
        Nodo nuevo = new Nodo(valor, null);

        if (estaVacia()) {
            // Primer elemento: head y tail apuntan al mismo nodo.
            head = nuevo;
        } else {
            // Se engancha al final y se actualiza tail.
            tail.siguiente = nuevo;
        }
        tail = nuevo;
        tamanio++;
    }

    /**
     * Desencola y devuelve el valor del frente de la cola.
     *
     * Quita el primer nodo de la lista, operación O(1). Si era el último
     * elemento, ambas referencias quedan vacías.
     *
     * @return el valor que estaba en el frente.
     * @throws NoSuchElementException si la cola está vacía.
     */
    public int desencolar() {
        if (estaVacia()) {
            throw new NoSuchElementException(
                    "No se puede desencolar: la cola está vacía.");
        }

        int valor = head.valor;
        head = head.siguiente; // Avanza el frente al siguiente nodo.
        tamanio--;

        if (head == null) {
            // Se quitó el último elemento: la cola vuelve a estar vacía.
            tail = null;
        }
        return valor;
    }

    /**
     * Devuelve el valor del frente de la cola sin quitarlo.
     *
     * @return el valor del frente.
     * @throws NoSuchElementException si la cola está vacía.
     */
    public int consultarFrente() {
        if (estaVacia()) {
            throw new NoSuchElementException(
                    "No se puede consultar el frente: la cola está vacía.");
        }
        return head.valor;
    }

    /**
     * Indica si la cola está vacía.
     *
     * @return {@code true} si no hay elementos; {@code false} en caso contrario.
     */
    public boolean estaVacia() {
        return head == null;
    }

    /**
     * Indica si un valor está presente en la cola.
     *
     * Recorre la lista desde el frente hasta el final. Es O(n) en el peor caso.
     *
     * @param valor entero a buscar.
     * @return {@code true} si el valor está en la cola; {@code false} si no
     *         está o si la cola está vacía.
     */
    public boolean contiene(int valor) {
        Nodo actual = head;
        while (actual != null) {
            if (actual.valor == valor) {
                return true;
            }
            actual = actual.siguiente;
        }
        return false;
    }

    /**
     * Devuelve la cantidad de elementos almacenados.
     *
     * @return tamaño actual de la cola.
     */
    public int tamanio() {
        return tamanio;
    }

    /**
     * Muestra por consola los elementos desde el frente hasta el final, sin
     * modificar la cola. Si la cola está vacía, lo informa.
     *
     * Recorre la lista una sola vez, por lo que es O(n).
     */
    public void imprimir() {
        if (estaVacia()) {
            System.out.println("La cola está vacía.");
            return;
        }

        StringBuilder salida = new StringBuilder("Frente -> ");
        Nodo actual = head;
        while (actual != null) {
            salida.append(actual.valor);
            if (actual.siguiente != null) {
                salida.append(" -> ");
            }
            actual = actual.siguiente;
        }
        salida.append(" <- Final");
        System.out.println(salida.toString());
    }

    /**
     * Punto de entrada de prueba. Ejercita las operaciones principales y los
     * casos de cola vacía.
     *
     * @param args argumentos de línea de comandos (no se usan).
     */
    public static void main(String[] args) {
        ColaEnteros cola = new ColaEnteros();

        System.out.println("== Casos de cola vacía ==");
        System.out.println("estaVacia(): " + cola.estaVacia());
        System.out.println("contiene(5): " + cola.contiene(5));
        cola.imprimir();
        probarDesencolarVacia();
        probarConsultarFrenteVacia();

        System.out.println();
        System.out.println("== Operaciones principales ==");
        cola.encolar(10);
        cola.encolar(20);
        cola.encolar(30);

        System.out.println("estaVacia(): " + cola.estaVacia());
        System.out.println("tamanio(): " + cola.tamanio());
        System.out.println("consultarFrente(): " + cola.consultarFrente());
        cola.imprimir();

        System.out.println("contiene(20): " + cola.contiene(20));
        System.out.println("contiene(99): " + cola.contiene(99));

        System.out.println("desencolar(): " + cola.desencolar());
        System.out.println("desencolar(): " + cola.desencolar());
        cola.imprimir();

        System.out.println("desencolar(): " + cola.desencolar());
        System.out.println("estaVacia(): " + cola.estaVacia());
        cola.imprimir();
    }

    /**
     * Verifica que {@link #desencolar()} lance la excepción esperada con la
     * cola vacía.
     */
    private static void probarDesencolarVacia() {
        try {
            new ColaEnteros().desencolar();
            System.out.println("ERROR: debió lanzar NoSuchElementException.");
        } catch (NoSuchElementException e) {
            System.out.println("desencolar() vacía -> " + e.getMessage());
        }
    }

    /**
     * Verifica que {@link #consultarFrente()} lance la excepción esperada con
     * la cola vacía.
     */
    private static void probarConsultarFrenteVacia() {
        try {
            new ColaEnteros().consultarFrente();
            System.out.println("ERROR: debió lanzar NoSuchElementException.");
        } catch (NoSuchElementException e) {
            System.out.println("consultarFrente() vacía -> " + e.getMessage());
        }
    }
}

/*
 * ============================================================================
 * PROMPT ORIGINAL
 * ============================================================================
 * Implementá una pila de enteros usando una lista enlazada simple y guardá la
 * solución en:
 * Guía_Pilas_colas_lista_enlazada/Ejercicio_1/PilaEnteros.java
 *
 * Requisitos:
 * - Definí una clase pública llamada PilaEnteros.
 * - Cada nodo debe contener un entero y una referencia al siguiente nodo.
 * - El head de la lista debe representar el tope de la pila. Apilar y
 *   desapilar desde el head evita recorrer la lista, por lo que ambas
 *   operaciones deben ser O(1).
 * - Implementá estas operaciones:
 *     - apilar(int valor): agrega el valor al tope.
 *     - desapilar(): quita y devuelve el valor del tope.
 *     - consultarTope(): devuelve el valor del tope sin quitarlo.
 *     - estaVacia(): indica si la pila está vacía.
 *     - contiene(int valor): indica si el valor está en la pila.
 *     - imprimir(): muestra los elementos desde el tope hasta el último
 *       elemento, sin modificar la pila.
 * - Si se llama a desapilar() o consultarTope() con la pila vacía, lanzá
 *   NoSuchElementException con un mensaje claro.
 * - contiene() debe devolver false si la pila está vacía. imprimir() debe
 *   informar que está vacía.
 * - No se permiten arrays ni colecciones de Java para almacenar los
 *   elementos; la estructura debe ser una lista enlazada simple.
 * - Complejidades esperadas: apilar, desapilar, consultarTope y estaVacia en
 *   O(1); contiene e imprimir en O(n).
 * - Agregá un main breve que permita probar las operaciones principales y los
 *   casos de pila vacía.
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
 * Pila de enteros implementada sobre una lista enlazada simple.
 *
 * El {@code head} de la lista representa el tope de la pila. Esto permite que
 * las operaciones {@link #apilar(int)} y {@link #desapilar()} trabajen
 * siempre sobre el primer nodo, sin recorrer la estructura, por lo que ambas
 * son O(1).
 *
 * No se utilizan arrays ni colecciones de Java: los elementos se almacenan
 * únicamente en instancias de {@link Nodo}.
 */
public class PilaEnteros {

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

    /** Primer nodo de la lista; representa el tope de la pila. */
    private Nodo tope;

    /** Cantidad de elementos almacenados en la pila. */
    private int tamanio;

    /**
     * Crea una pila vacía.
     */
    public PilaEnteros() {
        this.tope = null;
        this.tamanio = 0;
    }

    /**
     * Apila un valor en el tope de la pila.
     *
     * El nuevo nodo se inserta al inicio de la lista, por lo que la operación
     * es O(1).
     *
     * @param valor entero a apilar.
     */
    public void apilar(int valor) {
        // El nuevo nodo pasa a ser el tope y enlaza al antiguo tope.
        Nodo nuevo = new Nodo(valor, tope);
        tope = nuevo;
        tamanio++;
    }

    /**
     * Desapila y devuelve el valor del tope de la pila.
     *
     * Quita el primer nodo de la lista, operación O(1).
     *
     * @return el valor que estaba en el tope.
     * @throws NoSuchElementException si la pila está vacía.
     */
    public int desapilar() {
        if (estaVacia()) {
            throw new NoSuchElementException(
                    "No se puede desapilar: la pila está vacía.");
        }

        int valor = tope.valor;
        tope = tope.siguiente; // Avanza el tope al siguiente nodo.
        tamanio--;
        return valor;
    }

    /**
     * Devuelve el valor del tope sin quitarlo.
     *
     * @return el valor del tope.
     * @throws NoSuchElementException si la pila está vacía.
     */
    public int consultarTope() {
        if (estaVacia()) {
            throw new NoSuchElementException(
                    "No se puede consultar el tope: la pila está vacía.");
        }
        return tope.valor;
    }

    /**
     * Indica si la pila está vacía.
     *
     * @return {@code true} si no hay elementos; {@code false} en caso contrario.
     */
    public boolean estaVacia() {
        return tope == null;
    }

    /**
     * Indica si un valor está presente en la pila.
     *
     * Recorre la lista desde el tope hasta el último nodo. Es O(n) en el peor
     * caso.
     *
     * @param valor entero a buscar.
     * @return {@code true} si el valor está en la pila; {@code false} si no
     *         está o si la pila está vacía.
     */
    public boolean contiene(int valor) {
        Nodo actual = tope;
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
     * @return tamaño actual de la pila.
     */
    public int tamanio() {
        return tamanio;
    }

    /**
     * Muestra por consola los elementos desde el tope hasta el último, sin
     * modificar la pila. Si la pila está vacía, lo informa.
     *
     * Recorre la lista una sola vez, por lo que es O(n).
     */
    public void imprimir() {
        if (estaVacia()) {
            System.out.println("La pila está vacía.");
            return;
        }

        StringBuilder salida = new StringBuilder("Tope -> ");
        Nodo actual = tope;
        while (actual != null) {
            salida.append(actual.valor);
            if (actual.siguiente != null) {
                salida.append(" -> ");
            }
            actual = actual.siguiente;
        }
        salida.append(" <- Fondo");
        System.out.println(salida.toString());
    }

    /**
     * Punto de entrada de prueba. Ejercita las operaciones principales y los
     * casos de pila vacía.
     *
     * @param args argumentos de línea de comandos (no se usan).
     */
    public static void main(String[] args) {
        PilaEnteros pila = new PilaEnteros();

        System.out.println("== Casos de pila vacía ==");
        System.out.println("estaVacia(): " + pila.estaVacia());
        System.out.println("contiene(5): " + pila.contiene(5));
        pila.imprimir();
        probarDesapilarVacia();
        probarConsultarTopeVacia();

        System.out.println();
        System.out.println("== Operaciones principales ==");
        pila.apilar(10);
        pila.apilar(20);
        pila.apilar(30);

        System.out.println("estaVacia(): " + pila.estaVacia());
        System.out.println("tamanio(): " + pila.tamanio());
        System.out.println("consultarTope(): " + pila.consultarTope());
        pila.imprimir();

        System.out.println("contiene(20): " + pila.contiene(20));
        System.out.println("contiene(99): " + pila.contiene(99));

        System.out.println("desapilar(): " + pila.desapilar());
        System.out.println("desapilar(): " + pila.desapilar());
        pila.imprimir();

        System.out.println("desapilar(): " + pila.desapilar());
        System.out.println("estaVacia(): " + pila.estaVacia());
        pila.imprimir();
    }

    /**
     * Verifica que {@link #desapilar()} lance la excepción esperada con la pila
     * vacía.
     */
    private static void probarDesapilarVacia() {
        try {
            new PilaEnteros().desapilar();
            System.out.println("ERROR: debió lanzar NoSuchElementException.");
        } catch (NoSuchElementException e) {
            System.out.println("desapilar() vacía -> " + e.getMessage());
        }
    }

    /**
     * Verifica que {@link #consultarTope()} lance la excepción esperada con la
     * pila vacía.
     */
    private static void probarConsultarTopeVacia() {
        try {
            new PilaEnteros().consultarTope();
            System.out.println("ERROR: debió lanzar NoSuchElementException.");
        } catch (NoSuchElementException e) {
            System.out.println("consultarTope() vacía -> " + e.getMessage());
        }
    }
}

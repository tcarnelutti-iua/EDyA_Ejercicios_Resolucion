/*
Prompt inicial utilizado:
Quiero implementar desde cero una lista enlazada simple de números enteros en Java, sin usar
ArrayList, LinkedList ni otras colecciones para almacenar los datos.

Cada nodo debe representar un número entero y una referencia al siguiente nodo. La lista debe
mantener una referencia head al primer nodo: si la lista está vacía, head no apunta a ningún nodo.

Necesito que permita insertar un dato al inicio y al final, imprimir los elementos en orden desde
head, consultar si está vacía y obtener la cantidad de elementos. El tamaño debe comenzar en cero e
incrementarse una sola vez por cada inserción.

Al insertar al inicio, el nuevo nodo debe quedar antes del que era primero. Al insertar al final,
debe enlazarse después del último; si la lista está vacía, debe convertirse en el primer nodo.
Considerá también la impresión y las consultas cuando la lista está vacía, y valores enteros como
cero y negativos.

Explicá brevemente cómo se actualizan las referencias y el tamaño en cada inserción. Incluí pruebas
o ejemplos que permitan comprobar la lista vacía, inserciones al inicio y al final, el orden de
impresión y el tamaño.

Ajustes realizados luego de la primera respuesta de OpenCode:
Sin cambios.
*/

/**
 * Lista enlazada simple de números enteros implementada desde cero.
 *
 * <p>No se utiliza ninguna colección de la API de Java (ArrayList, LinkedList, etc.)
 * para almacenar los datos: cada elemento vive en un {@link Nodo} propio enlazado
 * mediante referencias.</p>
 *
 * <p>La clase mantiene dos invariantes fundamentales:</p>
 * <ul>
 *   <li>{@code head} apunta al primer nodo, o es {@code null} si la lista está vacía.</li>
 *   <li>{@code size} refleja siempre la cantidad real de nodos de la lista.</li>
 * </ul>
 */
public class ListaEnlazadaSimple {

    /**
     * Nodo de la lista enlazada simple.
     *
     * <p>Es una clase interna estática porque no necesita acceder al estado de la
     * lista que la contiene; cada nodo solo conoce su dato y su sucesor.</p>
     */
    private static final class Nodo {
        /** Valor entero almacenado en este nodo. */
        private final int dato;
        /** Referencia al siguiente nodo; {@code null} indica fin de la lista. */
        private Nodo siguiente;

        /**
         * Construye un nodo con el dato indicado y sin sucesor.
         *
         * @param dato valor entero a almacenar
         */
        private Nodo(int dato) {
            this.dato = dato;
            this.siguiente = null;
        }
    }

    /** Referencia al primer nodo de la lista; {@code null} si está vacía. */
    private Nodo head;

    /** Cantidad de nodos almacenados en la lista. Empieza en cero. */
    private int size;

    /**
     * Crea una lista enlazada simple vacía.
     */
    public ListaEnlazadaSimple() {
        this.head = null;
        this.size = 0;
    }

    /**
     * Inserta un nuevo dato al inicio de la lista.
     *
     * <p>El nuevo nodo pasa a ser la cabeza y queda enlazado antes del que era el
     * primer nodo. Si la lista estaba vacía, {@code head} era {@code null}, por lo
     * que el nuevo nodo también queda como único elemento.</p>
     *
     * @param dato valor entero a insertar (puede ser cero o negativo)
     */
    public void insertarAlInicio(int dato) {
        Nodo nuevo = new Nodo(dato);
        // El nuevo nodo apunta al que era el primer nodo (puede ser null si estaba vacía).
        nuevo.siguiente = this.head;
        // Ahora la cabeza es el nuevo nodo.
        this.head = nuevo;
        // Se agregó exactamente un nodo.
        this.size++;
    }

    /**
     * Inserta un nuevo dato al final de la lista.
     *
     * <p>Si la lista está vacía, el nuevo nodo se convierte en la cabeza. Si no,
     * se recorre hasta el último nodo y se enlaza el nuevo nodo después de él.</p>
     *
     * @param dato valor entero a insertar (puede ser cero o negativo)
     */
    public void insertarAlFinal(int dato) {
        Nodo nuevo = new Nodo(dato);

        if (this.head == null) {
            // Caso lista vacía: el nuevo nodo es a la vez el primero.
            this.head = nuevo;
        } else {
            // Recorrer hasta el último nodo (aquel cuyo 'siguiente' es null).
            Nodo actual = this.head;
            while (actual.siguiente != null) {
                actual = actual.siguiente;
            }
            // Enlazar el nuevo nodo después del último.
            actual.siguiente = nuevo;
        }

        // Se agregó exactamente un nodo, tanto en el caso vacío como en el no vacío.
        this.size++;
    }

    /**
     * Indica si la lista no contiene elementos.
     *
     * @return {@code true} si la lista está vacía, {@code false} en caso contrario
     */
    public boolean estaVacia() {
        return this.head == null;
    }

    /**
     * Devuelve la cantidad de elementos almacenados en la lista.
     *
     * @return el tamaño actual de la lista (cero si está vacía)
     */
    public int getSize() {
        return this.size;
    }

    /**
     * Imprime en la consola los elementos de la lista en orden, desde {@code head}
     * hasta el último nodo.
     *
     * <p>Si la lista está vacía se informa explícitamente en lugar de no imprimir
     * nada, para que el resultado sea claro.</p>
     */
    public void imprimir() {
        if (estaVacia()) {
            System.out.println("Lista vacía []");
            return;
        }

        StringBuilder salida = new StringBuilder("Lista: [");
        Nodo actual = this.head;
        while (actual != null) {
            salida.append(actual.dato);
            if (actual.siguiente != null) {
                salida.append(" -> ");
            }
            actual = actual.siguiente;
        }
        salida.append("]  (tamaño = ").append(this.size).append(")");
        System.out.println(salida);
    }

    /**
     * Método de prueba que ejercita los casos borde: lista vacía, inserciones al
     * inicio y al final, valores cero y negativos, y verificación de orden y tamaño.
     *
     * @param args no se utilizan
     */
    public static void main(String[] args) {
        // ---- Caso 1: lista vacía ----
        ListaEnlazadaSimple lista = new ListaEnlazadaSimple();
        System.out.println("== Caso 1: lista vacía ==");
        System.out.println("estaVacia() = " + lista.estaVacia());
        System.out.println("getSize()   = " + lista.getSize());
        lista.imprimir();

        // ---- Caso 2: inserción al inicio ----
        System.out.println("\n== Caso 2: insertarAlInicio ==");
        lista.insertarAlInicio(10);
        lista.insertarAlInicio(-5);
        lista.insertarAlInicio(0); // cero es un valor válido
        lista.imprimir();          // esperado: [0 -> -5 -> 10]
        System.out.println("estaVacia() = " + lista.estaVacia());
        System.out.println("getSize()   = " + lista.getSize()); // esperado: 3

        // ---- Caso 3: inserción al final ----
        System.out.println("\n== Caso 3: insertarAlFinal ==");
        lista.insertarAlFinal(7);
        lista.insertarAlFinal(-100);
        lista.imprimir();          // esperado: [0 -> -5 -> 10 -> 7 -> -100]
        System.out.println("getSize()   = " + lista.getSize()); // esperado: 5

        // ---- Caso 4: insertar al final sobre lista vacía ----
        System.out.println("\n== Caso 4: insertarAlFinal en lista vacía ==");
        ListaEnlazadaSimple vacia = new ListaEnlazadaSimple();
        vacia.insertarAlFinal(42);
        vacia.imprimir();          // esperado: [42]
        System.out.println("estaVacia() = " + vacia.estaVacia());
        System.out.println("getSize()   = " + vacia.getSize()); // esperado: 1

        // ---- Caso 5: verificación automática de orden y tamaño ----
        System.out.println("\n== Caso 5: verificación automática ==");
        verificar(lista.getSize() == 5
                        && lista.head.dato == 0
                        && lista.head.siguiente.dato == -5
                        && lista.head.siguiente.siguiente.dato == 10
                        && lista.head.siguiente.siguiente.siguiente.dato == 7
                        && lista.head.siguiente.siguiente.siguiente.siguiente.dato == -100,
                "Orden y tamaño de la lista principal");
        verificar(vacia.getSize() == 1 && !vacia.estaVacia(),
                "Inserción al final en lista vacía");
    }

    /**
     * Utilidad sencilla para mostrar el resultado de una verificación booleana.
     *
     * @param condicion resultado de la comprobación
     * @param descripcion texto descriptivo de lo verificado
     */
    private static void verificar(boolean condicion, String descripcion) {
        System.out.println((condicion ? "[OK]   " : "[FALLA] ") + descripcion);
    }
}

/*
Prompt utilizado:
Partiendo de la lista enlazada simple de enteros del Ejercicio 1, agregar el método
boolean buscar(int dato). Debe recorrer la lista desde head, avanzar nodo por nodo
usando la referencia al siguiente y devolver true si encuentra el dato. Si llega a
null sin encontrarlo, debe devolver false.

Se mantiene toda la estructura y las operaciones existentes del Ejercicio 1
(insertarAlInicio, insertarAlFinal, estaVacia, getSize, imprimir) y solo se agrega la
búsqueda secuencial, junto con pruebas para lista vacía, valores al inicio, en el
medio, al final y ausentes.

¿Por qué no se puede acceder a un elemento por índice?
Porque los nodos no se guardan en posiciones contiguas de memoria: cada nodo solo
conoce su dato y la referencia al siguiente. La lista no ofrece acceso posicional
directo (no hay arreglo subyacente ni offsets calculables). Por eso, para buscar un
valor hay que recorrer secuencialmente desde head siguiendo las referencias, con
costo O(n) en el peor caso.
*/

/**
 * Lista enlazada simple de números enteros implementada desde cero, con búsqueda
 * secuencial de valores.
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
     * Busca un valor dentro de la lista mediante un recorrido secuencial.
     *
     * <p>No existe acceso posicional directo: como los nodos están conectados
     * únicamente por referencias al siguiente, la única forma de llegar a un
     * elemento es partir de {@code head} y avanzar nodo por nodo. Se recorre la
     * lista completa y, en cuanto se encuentra un nodo cuyo dato coincide, se
     * devuelve {@code true}. Si el recorrido termina en {@code null} (fin de la
     * lista) sin coincidencia, se devuelve {@code false}.</p>
     *
     * <p>Casos contemplados:</p>
     * <ul>
     *   <li>Lista vacía: {@code head} es {@code null}, el bucle no se ejecuta y
     *       devuelve {@code false}.</li>
     *   <li>Dato al inicio: se encuentra en la primera comparación.</li>
     *   <li>Dato en el medio o al final: se encuentra tras avanzar por las
     *       referencias {@code siguiente}.</li>
     *   <li>Dato ausente: se recorre toda la lista y se devuelve {@code false}.</li>
     * </ul>
     *
     * <p>Complejidad temporal: O(n) en el peor caso (dato ausente o al final);
     * O(1) en el mejor caso (dato en {@code head}). Complejidad espacial: O(1).</p>
     *
     * @param dato valor entero a buscar (puede ser cero o negativo)
     * @return {@code true} si el dato está presente, {@code false} en caso contrario
     */
    public boolean buscar(int dato) {
        Nodo actual = this.head;
        // Avanzar por la lista mientras haya nodos por visitar.
        while (actual != null) {
            // Comparar el dato del nodo actual con el buscado.
            if (actual.dato == dato) {
                return true;
            }
            // Pasar al siguiente nodo siguiendo la referencia.
            actual = actual.siguiente;
        }
        // Se llegó a null sin encontrar el dato: no está en la lista.
        return false;
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
     * Método de prueba que ejercita la búsqueda en todos los casos relevantes:
     * lista vacía, dato al inicio, en el medio, al final y ausente. También repite
     * los casos borde del Ejercicio 1 para confirmar que las operaciones previas
     * siguen funcionando.
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

        // ---- Caso 6: búsqueda en lista vacía ----
        System.out.println("\n== Caso 6: buscar en lista vacía ==");
        ListaEnlazadaSimple sinElementos = new ListaEnlazadaSimple();
        // Cualquier valor debe dar false porque no hay nodos.
        verificar(!sinElementos.buscar(1),
                "buscar(1) en lista vacía devuelve false");

        // ---- Caso 7: búsqueda de valores existentes (inicio, medio, final) ----
        // La lista principal es: [0 -> -5 -> 10 -> 7 -> -100]
        System.out.println("\n== Caso 7: buscar valores existentes ==");
        verificar(lista.buscar(0), "buscar(0)   -> inicio de la lista");
        verificar(lista.buscar(10), "buscar(10)  -> medio de la lista");
        verificar(lista.buscar(-100), "buscar(-100) -> final de la lista");
        verificar(lista.buscar(-5), "buscar(-5)  -> segundo elemento (negativo)");

        // ---- Caso 8: búsqueda de un valor ausente ----
        System.out.println("\n== Caso 8: buscar valor ausente ==");
        verificar(!lista.buscar(999),
                "buscar(999) en lista no vacía devuelve false");

        // ---- Caso 9: búsqueda en lista de un solo elemento ----
        System.out.println("\n== Caso 9: buscar en lista de un elemento ==");
        verificar(vacia.buscar(42) && !vacia.buscar(43),
                "Coincidencia con el único elemento; ausencia devuelve false");
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

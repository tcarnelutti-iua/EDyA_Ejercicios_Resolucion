/*
Prompt utilizado:
Partiendo de la lista enlazada simple de enteros del Ejercicio 2, agregar el método
int obtener(int posicion). Debe devolver el dato almacenado en esa posición, contando
desde cero. Si posicion < 0 o posicion >= size, debe lanzar IndexOutOfBoundsException.

Se conservan todas las operaciones existentes (insertarAlInicio, insertarAlFinal,
buscar, estaVacia, getSize, imprimir) y solo se agrega el acceso por posición,
junto con pruebas para posiciones válidas al inicio, en el medio y al final, más la
lista vacía y posiciones inválidas.

¿Por qué, aunque el método reciba una posición, no se puede acceder directamente al
elemento como en un arreglo?
Porque en una lista enlazada simple los nodos NO se guardan en posiciones contiguas
de memoria: cada nodo solo conoce su dato y la referencia (puntero) al siguiente
nodo. No hay un arreglo subyacente ni una dirección base sobre la cual calcular un
"desplazamiento" (offset). Por eso, para llegar al elemento número k no existe una
fórmula de acceso directo O(1); la única forma es partir de head y avanzar nodo por
nodo siguiendo las referencias siguiente exactamente k veces. Esto da un costo
O(k) (O(n) en el peor caso, cuando k es el último índice), a diferencia del acceso
por índice de un arreglo, que es O(1).
*/

/**
 * Lista enlazada simple de números enteros implementada desde cero, con búsqueda
 * secuencial de valores y acceso por posición.
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
     * Devuelve el dato almacenado en la posición indicada (contando desde cero).
     *
     * <p>Aunque el método reciba una posición, la lista enlazada simple NO permite
     * acceder directamente al elemento como ocurre con un arreglo. Los nodos no
     * están en posiciones contiguas de memoria y no existe una dirección base para
     * calcular un desplazamiento: cada nodo solo conoce su dato y la referencia al
     * siguiente. Por eso es obligatorio comenzar en {@code head} y avanzar nodo por
     * nodo (siguiendo {@code siguiente}) hasta haber dado exactamente
     * {@code posicion} pasos. El nodo alcanzado es el que ocupa esa posición y su
     * dato es el que se devuelve.</p>
     *
     * <p>Casos contemplados:</p>
     * <ul>
     *   <li>Posición válida al inicio ({@code posicion == 0}): no se avanza ningún
     *       paso; se devuelve el dato de {@code head}.</li>
     *   <li>Posición válida en el medio: se avanza la cantidad de pasos necesaria.</li>
     *   <li>Posición válida al final ({@code posicion == size - 1}): se recorre toda
     *       la lista hasta el último nodo.</li>
     *   <li>Lista vacía: {@code size} es {@code 0}, por lo que cualquier posición
     *       válida (0 o mayor) resulta inválida y se lanza la excepción.</li>
     *   <li>Posición inválida ({@code posicion < 0} o {@code posicion >= size}): se
     *       lanza {@link IndexOutOfBoundsException}.</li>
     * </ul>
     *
     * <p>Complejidad temporal: O(k) para obtener la posición k; O(n) en el peor caso
     * (última posición). Complejidad espacial: O(1).</p>
     *
     * @param posicion índice del elemento a obtener (base cero)
     * @return el dato entero almacenado en la posición indicada
     * @throws IndexOutOfBoundsException si {@code posicion < 0} o
     *         {@code posicion >= size}
     */
    public int obtener(int posicion) {
        // Validación previa: la posición debe pertenecer al rango [0, size - 1].
        // Al comparar contra 'size' (y no contra null) evitamos recorrer la lista
        // solo para descubrir que la posición no es válida.
        if (posicion < 0 || posicion >= this.size) {
            throw new IndexOutOfBoundsException(
                    "Posición fuera de rango: " + posicion + " (tamaño = " + this.size + ")");
        }

        // Comenzar siempre en la cabeza: es el único punto de entrada a la lista.
        Nodo actual = this.head;

        // Avanzar exactamente 'posicion' nodos siguiendo las referencias.
        // Después del bucle, 'actual' apunta al nodo que ocupa esa posición.
        for (int i = 0; i < posicion; i++) {
            actual = actual.siguiente;
        }

        // Devolver el dato del nodo alcanzado.
        return actual.dato;
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
     * Método de prueba que ejercita {@code obtener} en todos los casos relevantes:
     * posiciones válidas al inicio, en el medio y al final, lista vacía y posiciones
     * inválidas (negativa y fuera del tamaño). También repite los casos borde de los
     * ejercicios anteriores para confirmar que las operaciones previas siguen
     * funcionando.
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

        // ---- Caso 4: obtener posiciones válidas (inicio, medio y final) ----
        // La lista principal es: [0 -> -5 -> 10 -> 7 -> -100], índices 0..4.
        System.out.println("\n== Caso 4: obtener posiciones válidas ==");
        verificar(lista.obtener(0) == 0,
                "obtener(0)  -> inicio  (dato 0)");
        verificar(lista.obtener(2) == 10,
                "obtener(2)  -> medio   (dato 10)");
        verificar(lista.obtener(4) == -100,
                "obtener(4)  -> final   (dato -100)");
        verificar(lista.obtener(1) == -5,
                "obtener(1)  -> segundo (dato -5)");

        // ---- Caso 5: obtener con lista de un solo elemento ----
        System.out.println("\n== Caso 5: obtener en lista de un elemento ==");
        ListaEnlazadaSimple unico = new ListaEnlazadaSimple();
        unico.insertarAlFinal(42);
        verificar(unico.obtener(0) == 42,
                "obtener(0) en lista de un elemento devuelve 42");

        // ---- Caso 6: posiciones inválidas (negativa y >= size) ----
        System.out.println("\n== Caso 6: posiciones inválidas ==");
        verificar(lanzaExcepcion(() -> lista.obtener(-1)),
                "obtener(-1)  -> lanza IndexOutOfBoundsException");
        verificar(lanzaExcepcion(() -> lista.obtener(5)),
                "obtener(5)   -> lanza IndexOutOfBoundsException (size = 5)");
        verificar(lanzaExcepcion(() -> lista.obtener(100)),
                "obtener(100) -> lanza IndexOutOfBoundsException");

        // ---- Caso 7: obtener en lista vacía ----
        System.out.println("\n== Caso 7: obtener en lista vacía ==");
        ListaEnlazadaSimple sinElementos = new ListaEnlazadaSimple();
        verificar(lanzaExcepcion(() -> sinElementos.obtener(0)),
                "obtener(0) en lista vacía lanza IndexOutOfBoundsException");

        // ---- Caso 8: las operaciones previas siguen funcionando ----
        System.out.println("\n== Caso 8: operaciones previas ==");
        verificar(lista.getSize() == 5 && !lista.estaVacia(),
                "getSize() y estaVacia() conservados");
        verificar(lista.buscar(10) && !lista.buscar(999),
                "buscar() conservado");
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

    /**
     * Ejecuta una acción que se espera que lance {@link IndexOutOfBoundsException}
     * y devuelve {@code true} solo si efectivamente la lanza.
     *
     * @param accion acción a ejecutar
     * @return {@code true} si se capturó la excepción esperada, {@code false} en caso contrario
     */
    private static boolean lanzaExcepcion(Runnable accion) {
        try {
            accion.run();
            return false; // No lanzó excepción: la verificación falla.
        } catch (IndexOutOfBoundsException e) {
            return true;  // Se lanzó la excepción esperada.
        }
    }
}

/*
 * ============================================================================
 * PROMPT ORIGINAL
 * ============================================================================
 * Implementá una lista doblemente enlazada genérica y guardá la solución en:
 *
 * Guía_Pilas_colas_lista_enlazada/Ejercicio_10/ListaDobleEnlazada.java
 *
 * Requisitos:
 * - Definí una clase pública ListaDobleEnlazada<T>.
 * - Cada nodo debe guardar un dato de tipo T y dos referencias: anterior y
 *   siguiente.
 * - Mantené referencias al primer nodo (head) y al último (tail). La referencia
 *   anterior permite recorrer la lista hacia atrás y la siguiente permite
 *   recorrerla hacia adelante.
 * - La lista no debe ser circular.
 * - Implementá estas operaciones:
 *   - insertarInicio(T dato)
 *   - insertarFinal(T dato)
 *   - eliminar(T dato): elimina la primera coincidencia y devuelve si encontró
 *     y eliminó un elemento.
 *   - imprimirAdelante(): muestra los elementos desde head hasta tail.
 *   - imprimirAtras(): muestra los elementos desde tail hasta head.
 * - Compará los elementos con Objects.equals para que la lista funcione con
 *   distintos tipos.
 * - Al eliminar, contemplá estos casos:
 *   - la lista está vacía;
 *   - se elimina el único nodo;
 *   - se elimina el primer nodo;
 *   - se elimina el último nodo;
 *   - se elimina un nodo intermedio;
 *   - el dato no se encuentra.
 * - Al insertar el primer nodo, head y tail deben referirse al mismo nodo. Al
 *   eliminar el único nodo, ambas referencias deben quedar vacías. Al eliminar
 *   un extremo, actualizá también la referencia anterior o siguiente del nuevo
 *   extremo.
 * - Insertar al inicio y al final debe ser O(1). Eliminar requiere buscar el
 *   dato, por lo que es O(n). Imprimir en cualquiera de las direcciones es O(n).
 * - Incluí un main breve que demuestre inserciones, eliminación de un elemento y
 *   recorridos en ambas direcciones, incluyendo una lista vacía y una lista con
 *   un solo elemento.
 * - No uses arrays ni colecciones de Java para almacenar los elementos.
 *
 * Incluí al comienzo del archivo, como comentario, este prompt completo y
 * explicá las referencias anterior y siguiente de cada nodo, por qué permiten
 * recorrer en ambos sentidos y qué casos especiales se contemplan al eliminar.
 * Aclará si hubo ajustes posteriores. Si no modificaste los requisitos, indicá:
 * "Ajustes posteriores: ninguno".
 *
 * Mantené la solución en el archivo indicado y evitá agregar otros archivos.
 *
 * ----------------------------------------------------------------------------
 * AJUSTES POSTERIORES
 * ----------------------------------------------------------------------------
 * Ajustes posteriores: ninguno
 *
 * ============================================================================
 * EXPLICACIÓN BREVE DE LOS CONCEPTOS
 * ============================================================================
 * Las referencias "anterior" y "siguiente" de cada nodo
 * -----------------------------------------------------
 * Cada nodo es una unidad con dos "cables": uno que lo une al nodo que viene
 * antes (anterior) y otro que lo une al que viene después (siguiente). En una
 * lista doblemente enlazada esos dos cables existen en AMBOS sentidos, es
 * decir, son simétricos: si B.siguiente == C entonces también C.anterior == B.
 * Esa simetría es el corazón de la estructura y hay que mantenerla en cada
 * inserción y en cada eliminación.
 *
 * ¿Por qué permiten recorrer en ambos sentidos?
 * ---------------------------------------------
 * Para recorrer HACIA ADELANTE alcanza con partir de head y seguir siempre el
 * cable "siguiente" (actual = actual.siguiente) hasta llegar a null: se pasa de
 * head a tail en orden. Para recorrer HACIA ATRÁS se parte de tail y se sigue
 * el cable "anterior" (actual = actual.anterior) hasta llegar a null: se pasa
 * de tail a head en orden inverso. En una lista SIMPLEMENTE enlazada solo
 * existe el cable "siguiente", por lo que el único punto de entrada es head y
 * no se puede retroceder; por eso aquí se mantienen también "anterior" y la
 * referencia "tail". Como la lista NO es circular, el head tiene
 * anterior == null y el tail tiene siguiente == null, y esos null marcan el fin
 * del recorrido en cada dirección.
 *
 * Complejidades
 * -------------
 * - insertarInicio / insertarFinal: O(1), porque se trabaja solo con head,
 *   tail y los enlaces del nodo nuevo, sin recorrer nada. Mantener "tail"
 *   evita tener que buscar el último nodo para insertar al final.
 * - eliminar: O(n), porque hay que BUSCAR la primera coincidencia recorriendo
 *   desde head; una vez hallada, reenganchar los vecinos es O(1).
 * - imprimirAdelante / imprimirAtras: O(n), porque hay que visitar todos los
 *   nodos (en una u otra dirección).
 *
 * Casos especiales contemplados al eliminar
 * -----------------------------------------
 *  1. Lista vacía (head == null): no hay nada que buscar; se devuelve false sin
 *     tocar nada.
 *  2. Se elimina el ÚNICO nodo: el nodo es a la vez head y tail; tras sacarlo,
 *     head y tail deben quedar AMBOS en null (lista vacía otra vez).
 *  3. Se elimina el PRIMER nodo (head) habiendo más de uno: head pasa a ser
 *     head.siguiente y, como el nuevo primero ya no tiene vecino anterior, hay
 *     que poner nuevoHead.anterior = null.
 *  4. Se elimina el ÚLTIMO nodo (tail) habiendo más de uno: tail pasa a ser
 *     tail.anterior y hay que poner nuevoTail.siguiente = null.
 *  5. Se elimina un nodo INTERMEDIO: sus vecinos se enlazan entre sí
 *     (anterior.siguiente = actual.siguiente y
 *      actual.siguiente.anterior = anterior), sin tocar head ni tail.
 *  6. El dato NO se encuentra: se recorre toda la lista y se devuelve false sin
 *     modificar enlaces ni tamaño.
 * En todos los casos la comparación se hace con Objects.equals, que aplica el
 * equals del objeto (contenido) y tolera null.
 * ============================================================================
 */

import java.util.Objects;

/**
 * Lista doblemente enlazada GENÉRICA implementada desde cero.
 *
 * <p>Cada nodo almacena un dato de tipo {@code T} y dos referencias: una al nodo
 * anterior y otra al siguiente. La lista mantiene referencias al primer nodo
 * ({@code head}) y al último ({@code tail}), y NO es circular: el primer nodo
 * tiene {@code anterior == null} y el último tiene {@code siguiente == null}.</p>
 *
 * <p>Gracias a las dos referencias se puede recorrer la lista en ambos sentidos:
 * desde {@code head} siguiendo {@code siguiente} (hacia adelante) y desde
 * {@code tail} siguiendo {@code anterior} (hacia atrás).</p>
 *
 * <p>No se utiliza ninguna colección de la API de Java ({@code ArrayList},
 * {@code LinkedList}, etc.) ni arrays para guardar los elementos: cada elemento
 * vive en un {@link Nodo} propio, enlazado mediante referencias.</p>
 *
 * <p><b>Comparación de datos:</b> como {@code T} es un tipo por referencia, la
 * igualdad se resuelve con {@link Objects#equals(Object, Object)}, que aplica el
 * {@code equals} del objeto (comparación por contenido) y maneja {@code null} de
 * forma segura.</p>
 *
 * @param <T> tipo de dato almacenado en la lista
 */
public class ListaDobleEnlazada<T> {

    /**
     * Nodo de la lista doblemente enlazada, genérico.
     *
     * <p>Es una clase interna estática porque no necesita acceder al estado de la
     * lista que la contiene. Cada nodo conoce su dato ({@code T}), su antecesor y
     * su sucesor.</p>
     *
     * @param <T> tipo de dato almacenado en el nodo
     */
    private static final class Nodo<T> {

        /** Dato almacenado en este nodo, de tipo {@code T}. */
        private final T dato;

        /** Referencia al nodo anterior; {@code null} si es el primero ({@code head}). */
        private Nodo<T> anterior;

        /** Referencia al nodo siguiente; {@code null} si es el último ({@code tail}). */
        private Nodo<T> siguiente;

        /**
         * Construye un nodo con el dato indicado y sin vecinos.
         *
         * @param dato dato a almacenar (puede ser {@code null})
         */
        private Nodo(T dato) {
            this.dato = dato;
            this.anterior = null;
            this.siguiente = null;
        }
    }

    /** Referencia al primer nodo de la lista; {@code null} si está vacía. */
    private Nodo<T> head;

    /** Referencia al último nodo de la lista; {@code null} si está vacía. */
    private Nodo<T> tail;

    /** Cantidad de nodos almacenados en la lista. */
    private int size;

    /**
     * Crea una lista doblemente enlazada vacía.
     */
    public ListaDobleEnlazada() {
        this.head = null;
        this.tail = null;
        this.size = 0;
    }

    /**
     * Inserta un nuevo dato al inicio de la lista.
     *
     * <p>El nuevo nodo pasa a ser {@code head} y su sucesor es el que era el
     * primer nodo. Como el nuevo primero no tiene antecesor, su {@code anterior}
     * queda en {@code null}. Si la lista estaba vacía, el nuevo nodo es a la vez
     * {@code head} y {@code tail}.</p>
     *
     * <p>Complejidad temporal: O(1). Complejidad espacial: O(1).</p>
     *
     * @param dato dato a insertar (puede ser {@code null})
     */
    public void insertarInicio(T dato) {
        Nodo<T> nuevo = new Nodo<>(dato);

        if (this.head == null) {
            // Lista vacía: el nuevo nodo es el único; head y tail apuntan a él.
            this.head = nuevo;
            this.tail = nuevo;
        } else {
            // El nuevo nodo queda delante del que era la cabeza.
            nuevo.siguiente = this.head;   // el nuevo apunta al viejo primero
            this.head.anterior = nuevo;    // el viejo primero reconoce a su antecesor
            this.head = nuevo;             // la cabeza pasa a ser el nuevo nodo
            // nuevo.anterior ya es null (es el primer nodo), no hay que tocarlo.
        }

        this.size++;
    }

    /**
     * Inserta un nuevo dato al final de la lista.
     *
     * <p>El nuevo nodo se engancha después de {@code tail} y pasa a ser el nuevo
     * último. Como el nuevo último no tiene sucesor, su {@code siguiente} queda en
     * {@code null}. Si la lista estaba vacía, el nuevo nodo es a la vez
     * {@code head} y {@code tail}.</p>
     *
     * <p>Se usa {@code tail} para no recorrer la lista, por lo que la operación es
     * O(1) y no O(n).</p>
     *
     * <p>Complejidad temporal: O(1). Complejidad espacial: O(1).</p>
     *
     * @param dato dato a insertar (puede ser {@code null})
     */
    public void insertarFinal(T dato) {
        Nodo<T> nuevo = new Nodo<>(dato);

        if (this.tail == null) {
            // Lista vacía: el nuevo nodo es el único; head y tail apuntan a él.
            this.head = nuevo;
            this.tail = nuevo;
        } else {
            // El nuevo nodo queda detrás del que era el último.
            nuevo.anterior = this.tail;    // el nuevo reconoce a su antecesor
            this.tail.siguiente = nuevo;   // el viejo último apunta al nuevo
            this.tail = nuevo;             // la cola pasa a ser el nuevo nodo
            // nuevo.siguiente ya es null (es el último), no hay que tocarlo.
        }

        this.size++;
    }

    /**
     * Elimina la PRIMERA aparición del valor indicado en la lista.
     *
     * <p>Se recorre la lista desde {@code head} siguiendo las referencias
     * {@code siguiente}, comparando cada dato con {@link Objects#equals(Object, Object)}.
     * Al encontrar la coincidencia se reenganchan los vecinos del nodo eliminado,
     * contemplando todos los casos especiales:</p>
     * <ul>
     *   <li><b>Lista vacía:</b> se devuelve {@code false} sin modificar nada.</li>
     *   <li><b>Único nodo:</b> {@code head} y {@code tail} quedan en {@code null}.</li>
     *   <li><b>Primer nodo:</b> {@code head = head.siguiente} y el nuevo primero
     *       tiene {@code anterior = null}.</li>
     *   <li><b>Último nodo:</b> {@code tail = tail.anterior} y el nuevo último
     *       tiene {@code siguiente = null}.</li>
     *   <li><b>Nodo intermedio:</b> los vecinos se enlazan entre sí.</li>
     *   <li><b>Dato no encontrado:</b> se recorre toda la lista y se devuelve
     *       {@code false} sin cambios.</li>
     * </ul>
     *
     * <p>Se elimina únicamente la primera aparición: en cuanto se reenganchan los
     * enlaces, el método retorna, de modo que los valores repetidos posteriores
     * permanecen en la lista.</p>
     *
     * <p><b>Comparación de objetos:</b> se usa {@code Objects.equals} en lugar de
     * {@code ==} para comparar por contenido y tolerar {@code null}.</p>
     *
     * <p>Complejidad temporal: O(n) en el peor caso (recorrer y no encontrar, o
     * encontrar al final); O(1) si el dato está en la cabeza. Espacial: O(1).</p>
     *
     * @param dato dato a eliminar (puede ser {@code null})
     * @return {@code true} si se encontró y eliminó la primera aparición;
     *         {@code false} en caso contrario
     */
    public boolean eliminar(T dato) {
        // Caso 1: lista vacía (head == tail == null). Nada que buscar.
        if (this.head == null) {
            return false;
        }

        // Buscar la primera coincidencia recorriendo hacia adelante.
        Nodo<T> actual = this.head;
        while (actual != null && !Objects.equals(actual.dato, dato)) {
            actual = actual.siguiente;
        }

        // Caso 6: se llegó al final sin encontrar el dato.
        if (actual == null) {
            return false;
        }

        // Se encontró el nodo a eliminar ('actual'). Se guardan sus vecinos.
        Nodo<T> anterior = actual.anterior;
        Nodo<T> siguiente = actual.siguiente;

        if (anterior == null && siguiente == null) {
            // Caso 2: es el ÚNICO nodo. La lista vuelve a estar vacía.
            this.head = null;
            this.tail = null;
        } else if (anterior == null) {
            // Caso 3: es el PRIMER nodo (y no es el único).
            this.head = siguiente;        // el segundo pasa a ser el primero
            siguiente.anterior = null;    // el nuevo primero ya no tiene antecesor
        } else if (siguiente == null) {
            // Caso 4: es el ÚLTIMO nodo (y no es el único).
            this.tail = anterior;         // el penúltimo pasa a ser el último
            anterior.siguiente = null;    // el nuevo último ya no tiene sucesor
        } else {
            // Caso 5: nodo INTERMEDIO. Los vecinos se enlazan entre sí.
            anterior.siguiente = siguiente;
            siguiente.anterior = anterior;
        }

        // Se quitó exactamente un nodo.
        this.size--;
        return true;
    }

    /**
     * Muestra los elementos desde {@code head} hasta {@code tail} (hacia adelante).
     *
     * <p>Se parte de {@code head} y se avanza siguiendo {@code siguiente} hasta
     * llegar a {@code null}. El dato se imprime invocando su {@code toString()}.
     * Complejidad temporal: O(n).</p>
     */
    public void imprimirAdelante() {
        if (this.head == null) {
            System.out.println("Lista vacía []. Recorrido hacia adelante.");
            return;
        }

        StringBuilder salida = new StringBuilder("Adelante (head -> tail): [");
        Nodo<T> actual = this.head;
        while (actual != null) {
            salida.append(actual.dato);
            if (actual.siguiente != null) {
                salida.append(" <-> ");
            }
            actual = actual.siguiente;
        }
        salida.append("]  (tamaño = ").append(this.size).append(")");
        System.out.println(salida);
    }

    /**
     * Muestra los elementos desde {@code tail} hasta {@code head} (hacia atrás).
     *
     * <p>Se parte de {@code tail} y se retrocede siguiendo {@code anterior} hasta
     * llegar a {@code null}. Esta es la operación que distingue a la lista
     * doblemente enlazada de una simple: es posible recorrerla en sentido inverso
     * sin invertirla. Complejidad temporal: O(n).</p>
     */
    public void imprimirAtras() {
        if (this.tail == null) {
            System.out.println("Lista vacía []. Recorrido hacia atrás.");
            return;
        }

        StringBuilder salida = new StringBuilder("Atrás  (tail -> head): [");
        Nodo<T> actual = this.tail;
        while (actual != null) {
            salida.append(actual.dato);
            if (actual.anterior != null) {
                salida.append(" <-> ");
            }
            actual = actual.anterior;
        }
        salida.append("]  (tamaño = ").append(this.size).append(")");
        System.out.println(salida);
    }

    /**
     * Indica si la lista no contiene elementos.
     *
     * @return {@code true} si la lista está vacía; {@code false} en caso contrario
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
     * Punto de entrada de prueba. Demuestra inserciones, eliminación de un
     * elemento y recorridos en ambas direcciones, incluyendo una lista vacía y una
     * lista con un solo elemento.
     *
     * @param args argumentos de línea de comandos (no se usan)
     */
    public static void main(String[] args) {
        // =====================================================================
        // Caso 1: lista vacía -> recorridos e intento de eliminación
        // =====================================================================
        System.out.println("== Lista vacía ==");
        ListaDobleEnlazada<String> vacia = new ListaDobleEnlazada<>();
        System.out.println("estaVacia(): " + vacia.estaVacia());
        vacia.imprimirAdelante();
        vacia.imprimirAtras();
        System.out.println("eliminar(\"x\") en lista vacía: " + vacia.eliminar("x"));

        // =====================================================================
        // Caso 2: lista con un solo elemento -> eliminarlo deja las refs vacías
        // =====================================================================
        System.out.println("\n== Lista con un solo elemento ==");
        ListaDobleEnlazada<Integer> uno = new ListaDobleEnlazada<>();
        uno.insertarInicio(42);
        uno.imprimirAdelante();
        uno.imprimirAtras();
        System.out.println("eliminar(42): " + uno.eliminar(42));
        System.out.println("estaVacia(): " + uno.estaVacia()
                + "  size = " + uno.getSize());
        uno.imprimirAdelante();

        // =====================================================================
        // Caso 3: inserciones en ambos extremos y recorridos en ambas direcciones
        // =====================================================================
        System.out.println("\n== Lista con varios elementos ==");
        ListaDobleEnlazada<String> nombres = new ListaDobleEnlazada<>();
        nombres.insertarInicio("Beto");   // [Beto]
        nombres.insertarFinal("Carla");   // [Beto, Carla]
        nombres.insertarInicio("Ana");    // [Ana, Beto, Carla]
        nombres.insertarFinal("Diego");   // [Ana, Beto, Carla, Diego]
        nombres.imprimirAdelante();
        nombres.imprimirAtras();

        // Eliminar un elemento intermedio.
        System.out.println("eliminar(\"Beto\"): " + nombres.eliminar("Beto"));
        nombres.imprimirAdelante();
        nombres.imprimirAtras();

        // Eliminar el primer nodo.
        System.out.println("eliminar(\"Ana\"): " + nombres.eliminar("Ana"));
        nombres.imprimirAdelante();
        nombres.imprimirAtras();

        // Eliminar el último nodo.
        System.out.println("eliminar(\"Diego\"): " + nombres.eliminar("Diego"));
        nombres.imprimirAdelante();
        nombres.imprimirAtras();

        // Intentar eliminar un dato que no existe.
        System.out.println("eliminar(\"Zoe\"): " + nombres.eliminar("Zoe"));
        System.out.println("size final = " + nombres.getSize());

        // =====================================================================
        // Caso 4: igualdad por contenido con Objects.equals (String nuevo)
        // =====================================================================
        System.out.println("\n== Igualdad por contenido ==");
        ListaDobleEnlazada<String> iguales = new ListaDobleEnlazada<>();
        iguales.insertarFinal("Hola");
        iguales.insertarFinal("Chau");
        System.out.println("eliminar(new String(\"Hola\")): "
                + iguales.eliminar(new String("Hola")));
        iguales.imprimirAdelante();
    }
}

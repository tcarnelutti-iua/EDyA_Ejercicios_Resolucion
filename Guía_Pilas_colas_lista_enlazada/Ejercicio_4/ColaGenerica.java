/*
 * ============================================================================
 * PROMPT ORIGINAL
 * ============================================================================
 * Modificá la implementación de cola para que sea genérica y guardá la solución
 * en:
 * Guía_Pilas_colas_lista_enlazada/Ejercicio_4/ColaGenerica.java
 *
 * Requisitos:
 * - Definí una clase pública ColaGenerica<T>, donde T representa el tipo de
 *   dato almacenado.
 * - Implementá la cola con una lista enlazada simple y referencias head y tail:
 *   head representa el frente y tail el final.
 * - Conservá las operaciones: encolar, desencolar, consultar el frente,
 *   verificar si está vacía, buscar si contiene un valor e imprimir su
 *   contenido.
 * - El comportamiento debe ser FIFO para cualquier T: los elementos se
 *   desencolan en el mismo orden en que fueron encolados. Encolar al final
 *   mediante tail y desencolar desde head mantiene ese orden sin depender del
 *   tipo de dato.
 * - Encolar, desencolar, consultar el frente y verificar si está vacía deben ser
 *   O(1). Buscar e imprimir deben ser O(n).
 * - Al encolar el primer elemento, head y tail deben referirse al mismo nodo. Al
 *   desencolar el último, ambas referencias deben quedar vacías.
 * - Si se intenta desencolar o consultar el frente de una cola vacía, lanzá
 *   NoSuchElementException con un mensaje claro.
 * - Evitá tipos crudos y conversiones de tipo innecesarias. No uses colecciones
 *   para almacenar los elementos.
 * - Probá la cola con nombres de personas (String) y con objetos Cliente.
 *   Definí Cliente en el mismo archivo y usá varios clientes para demostrar que
 *   se respeta el orden FIFO.
 * - Incluí un main breve que muestre las operaciones principales y el orden de
 *   salida en ambas colas.
 *
 * Incluí al comienzo del archivo, como comentario, este prompt completo y
 * explicá que los genéricos permiten reutilizar la cola con distintos tipos,
 * mientras que FIFO se determina por el orden de inserción y extracción, no por
 * el tipo almacenado. Aclarà si hubo ajustes posteriores. Si no modificaste los
 * requisitos, indicá: "Ajustes posteriores: ninguno".
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
 * 1. Genéricos en Java: permiten parametrizar una clase con un tipo, como
 *    ColaGenerica<T>. El parámetro T actúa como un "tipo por definir" que el
 *    usuario concreta al crear la instancia (por ejemplo ColaGenerica<String>
 *    o ColaGenerica<Cliente>). Así, una única implementación se reutiliza con
 *    distintos tipos y el compilador sigue comprobando los tipos: encolar un
 *    String en una ColaGenerica<Integer> no compila, y desencolar() devuelve
 *    directamente T sin necesidad de conversiones (casts).
 *
 * 2. El orden FIFO (First In, First Out) no depende del tipo almacenado, sino
 *    de por dónde se inserta y por dónde se extrae. En esta implementación
 *    siempre se encola al final (por tail) y se desencola desde el frente (por
 *    head). Como los nodos se enlazan en el orden en que llegan, el primero en
 *    entrar es siempre el primero en salir, sin importar si T es String, Cliente
 *    o cualquier otro tipo. Lo único "sensible al tipo" es la comparación de
 *    valores en contiene(), que se resuelve con Objects.equals, la igualdad
 *    apropiada para objetos.
 * ============================================================================
 */

import java.util.NoSuchElementException;
import java.util.Objects;

/**
 * Cola genérica implementada sobre una lista enlazada simple con referencias
 * {@code head} y {@code tail}.
 *
 * <p>El parámetro {@code T} es el tipo de los elementos almacenados. La misma
 * clase sirve para {@code String}, {@code Integer} o cualquier clase propia como
 * {@link Cliente}.</p>
 *
 * <p>{@code head} representa el frente de la cola, es decir, el extremo por el
 * que se {@linkplain #desencolar() desencola}. {@code tail} representa el final
 * de la cola, el extremo por el que se {@linkplain #encolar(Object) encola}.</p>
 *
 * <h2>¿Por qué se necesitan head y tail?</h2>
 * <p>Una cola debe respetar el orden FIFO: se quita por un extremo (el frente) y
 * se agrega por el otro (el final). Si solo se mantuviera {@code head}, encolar
 * al final obligaría a recorrer toda la lista hasta el último nodo, lo que
 * costaría O(n) y rompería la eficiencia esperada. Mantener ambas referencias
 * permite encolar por {@code tail} y desencolar por {@code head}, logrando O(1)
 * en las dos operaciones.</p>
 *
 * <p>No se utilizan arrays ni colecciones de Java: los elementos se almacenan
 * únicamente en instancias de {@link Nodo}.</p>
 *
 * @param <T> tipo de los elementos almacenados en la cola.
 */
public class ColaGenerica<T> {

    /**
     * Nodo de la lista enlazada simple.
     *
     * <p>Cada nodo guarda un valor de tipo {@code T} y una referencia al
     * siguiente nodo. El último nodo apunta a {@code null}.</p>
     *
     * @param <T> tipo del valor almacenado en el nodo.
     */
    private static final class Nodo<T> {

        /** Valor almacenado en este nodo. */
        private final T valor;

        /** Referencia al siguiente nodo de la lista (null si es el último). */
        private Nodo<T> siguiente;

        /**
         * Crea un nodo con un valor y una referencia al siguiente nodo.
         *
         * @param valor     valor a almacenar.
         * @param siguiente nodo que le sigue en la lista.
         */
        private Nodo(T valor, Nodo<T> siguiente) {
            this.valor = valor;
            this.siguiente = siguiente;
        }
    }

    /** Primer nodo de la lista; representa el frente de la cola. */
    private Nodo<T> head;

    /** Último nodo de la lista; representa el final de la cola. */
    private Nodo<T> tail;

    /** Cantidad de elementos almacenados en la cola. */
    private int tamanio;

    /**
     * Crea una cola vacía.
     */
    public ColaGenerica() {
        this.head = null;
        this.tail = null;
        this.tamanio = 0;
    }

    /**
     * Encola un valor al final de la cola.
     *
     * <p>El nuevo nodo se enlaza detrás de {@code tail} y pasa a ser el nuevo
     * final. No se recorre la lista, por lo que la operación es O(1).</p>
     *
     * @param valor valor a encolar (puede ser {@code null}).
     */
    public void encolar(T valor) {
        // El nuevo nodo siempre termina la lista, por eso su "siguiente" es null.
        Nodo<T> nuevo = new Nodo<>(valor, null);

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
     * <p>Quita el primer nodo de la lista, operación O(1). Si era el último
     * elemento, ambas referencias quedan vacías.</p>
     *
     * @return el valor que estaba en el frente.
     * @throws NoSuchElementException si la cola está vacía.
     */
    public T desencolar() {
        if (estaVacia()) {
            throw new NoSuchElementException(
                    "No se puede desencolar: la cola está vacía.");
        }

        T valor = head.valor;
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
    public T consultarFrente() {
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
     * <p>Recorre la lista desde el frente hasta el final comparando con
     * {@link Objects#equals(Object, Object)}, la igualdad apropiada para
     * objetos. Es O(n) en el peor caso.</p>
     *
     * @param valor valor a buscar.
     * @return {@code true} si el valor está en la cola; {@code false} si no
     *         está o si la cola está vacía.
     */
    public boolean contiene(T valor) {
        Nodo<T> actual = head;
        while (actual != null) {
            if (Objects.equals(actual.valor, valor)) {
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
     * <p>Recorre la lista una sola vez, por lo que es O(n).</p>
     */
    public void imprimir() {
        if (estaVacia()) {
            System.out.println("La cola está vacía.");
            return;
        }

        StringBuilder salida = new StringBuilder("Frente -> ");
        Nodo<T> actual = head;
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
     * Clase propia de ejemplo para demostrar el uso de la cola con un tipo
     * definido por el usuario.
     *
     * <p>Sobrescribe {@code equals} y {@code hashCode} para que
     * {@link Objects#equals(Object, Object)} compare por identidad lógica (DNI)
     * y no por referencia.</p>
     */
    private static final class Cliente {

        /** Documento único que identifica al cliente. */
        private final int dni;

        /** Nombre del cliente. */
        private final String nombre;

        /**
         * Crea un cliente.
         *
         * @param dni    documento único del cliente.
         * @param nombre nombre del cliente.
         */
        private Cliente(int dni, String nombre) {
            this.dni = dni;
            this.nombre = nombre;
        }

        @Override
        public boolean equals(Object otro) {
            if (this == otro) {
                return true;
            }
            if (otro == null || getClass() != otro.getClass()) {
                return false;
            }
            Cliente cliente = (Cliente) otro;
            return dni == cliente.dni;
        }

        @Override
        public int hashCode() {
            return Integer.hashCode(dni);
        }

        @Override
        public String toString() {
            return nombre + "(DNI " + dni + ")";
        }
    }

    /**
     * Punto de entrada de prueba. Ejercita la misma cola genérica con nombres de
     * personas ({@code String}) y con objetos {@link Cliente}, mostrando el
     * orden FIFO de salida, y verifica los casos de cola vacía.
     *
     * @param args argumentos de línea de comandos (no se usan).
     */
    public static void main(String[] args) {
        probarConString();
        probarConCliente();
        probarColaVacia();
    }

    /**
     * Demuestra el uso de la cola con {@code String} (nombres de personas) y el
     * orden FIFO de salida.
     */
    private static void probarConString() {
        System.out.println("== ColaGenerica<String> (nombres) ==");
        ColaGenerica<String> cola = new ColaGenerica<>();
        cola.encolar("Ana");
        cola.encolar("Bruno");
        cola.encolar("Carla");

        System.out.println("estaVacia(): " + cola.estaVacia());
        System.out.println("tamanio(): " + cola.tamanio());
        System.out.println("consultarFrente(): " + cola.consultarFrente());
        System.out.println("contiene(\"Bruno\"): " + cola.contiene("Bruno"));
        System.out.println("contiene(\"Zoe\"): " + cola.contiene("Zoe"));
        cola.imprimir();

        System.out.print("Orden de salida (FIFO): ");
        while (!cola.estaVacia()) {
            System.out.print(cola.desencolar());
            if (!cola.estaVacia()) {
                System.out.print(", ");
            }
        }
        System.out.println();
        System.out.println("estaVacia() tras vaciar: " + cola.estaVacia());
        System.out.println();
    }

    /**
     * Demuestra el uso de la misma cola con objetos {@link Cliente}, varios
     * clientes, y el orden FIFO de salida.
     */
    private static void probarConCliente() {
        System.out.println("== ColaGenerica<Cliente> ==");
        ColaGenerica<Cliente> cola = new ColaGenerica<>();
        cola.encolar(new Cliente(101, "Lucía"));
        cola.encolar(new Cliente(102, "Martín"));
        cola.encolar(new Cliente(103, "Sofía"));
        cola.encolar(new Cliente(104, "Diego"));

        System.out.println("tamanio(): " + cola.tamanio());
        System.out.println("consultarFrente(): " + cola.consultarFrente());
        // Se busca con una instancia distinta pero lógicamente igual (mismo DNI),
        // lo que demuestra que se usa equals y no ==.
        System.out.println("contiene(Cliente DNI 103): "
                + cola.contiene(new Cliente(103, "Otro nombre")));
        System.out.println("contiene(Cliente DNI 999): "
                + cola.contiene(new Cliente(999, "Inexistente")));
        cola.imprimir();

        System.out.print("Orden de salida (FIFO): ");
        while (!cola.estaVacia()) {
            System.out.print(cola.desencolar());
            if (!cola.estaVacia()) {
                System.out.print(", ");
            }
        }
        System.out.println();
        System.out.println("estaVacia() tras vaciar: " + cola.estaVacia());
        System.out.println();
    }

    /**
     * Verifica los casos de cola vacía: {@code estaVacia}, {@code contiene},
     * {@code imprimir} y las excepciones de {@code desencolar} y
     * {@code consultarFrente}.
     */
    private static void probarColaVacia() {
        System.out.println("== Casos de cola vacía ==");
        ColaGenerica<String> cola = new ColaGenerica<>();
        System.out.println("estaVacia(): " + cola.estaVacia());
        System.out.println("contiene(\"Ana\"): " + cola.contiene("Ana"));
        cola.imprimir();

        try {
            cola.desencolar();
            System.out.println("ERROR: debió lanzar NoSuchElementException.");
        } catch (NoSuchElementException e) {
            System.out.println("desencolar() vacía -> " + e.getMessage());
        }

        try {
            cola.consultarFrente();
            System.out.println("ERROR: debió lanzar NoSuchElementException.");
        } catch (NoSuchElementException e) {
            System.out.println("consultarFrente() vacía -> " + e.getMessage());
        }
    }
}

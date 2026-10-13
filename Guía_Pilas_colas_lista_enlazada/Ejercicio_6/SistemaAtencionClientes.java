/*
 * ============================================================================
 * PROMPT ORIGINAL
 * ============================================================================
 * Implementá un sistema de atención de clientes para un banco usando una cola
 * enlazada y guardá la solución en:
 * Guía_Pilas_colas_lista_enlazada/Ejercicio_6/SistemaAtencionClientes.java
 *
 * Requisitos:
 * - Definí una clase pública SistemaAtencionClientes.
 * - Cada cliente debe tener nombre, número de turno y motivo de consulta.
 * - Definí una clase Cliente dentro del mismo archivo para representar esos datos.
 * - Implementá la cola con una lista enlazada simple y referencias head y tail.
 *   head representa al próximo cliente que será atendido y tail al último que
 *   llegó.
 * - Implementá estas operaciones:
 *   - agregarCliente(Cliente cliente): agrega al cliente al final de la fila.
 *   - atenderProximo(): quita y devuelve el cliente que está al frente.
 *   - consultarSiguiente(): devuelve quién está al frente sin quitarlo.
 *   - imprimirFila(): muestra los clientes desde el primero que llegó y todavía
 *     espera hasta el último, sin modificar la cola.
 * - Usá una cola porque el orden de atención es FIFO: el primero en llegar es el
 *   primero en ser atendido. La secuencia de nodos desde head hasta tail
 *   representa ese orden.
 * - El número de turno debe formar parte de los datos de Cliente y conservarse
 *   al agregar, consultar, atender e imprimir. Para las pruebas, creá clientes
 *   con números de turno explícitos.
 * - Si se intenta atender o consultar una cola vacía, informá claramente que no
 *   hay clientes; no provoques un error ni alteres el estado.
 * - Al agregar el primer cliente, head y tail deben referirse al mismo nodo. Al
 *   atender al último cliente, ambas referencias deben quedar vacías.
 * - Agregar un cliente, atender al próximo, consultar quién sigue y verificar si
 *   está vacía deben ser O(1). Imprimir la fila debe ser O(n).
 * - Incluí un main breve que agregue varios clientes, consulte quién sigue,
 *   atienda clientes e imprima la fila, incluyendo el caso de fila vacía.
 * - No uses arrays ni colecciones de Java para almacenar los clientes.
 *
 * Incluí al comienzo del archivo, como comentario, este prompt completo y
 * explicá por qué una cola corresponde al orden de atención y cómo head y tail
 * conservan el orden de llegada. Aclarà si hubo ajustes posteriores. Si no
 * modificaste los requisitos, indicá: "Ajustes posteriores: ninguno".
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
 * ¿Por qué una cola corresponde al orden de atención?
 * ---------------------------------------------------
 * En un banco la regla es "el primero que llega es el primero que se atiende".
 * Ese criterio se llama FIFO (First In, First Out) y es exactamente el
 * comportamiento de una cola. Un cliente se incorpora por un extremo (el final
 * de la fila) y se atiende por el otro (el frente), de modo que nadie se saltea
 * el turno: quien llegó antes sale antes. Si en lugar de una cola usáramos una
 * pila (LIFO), el último en llegar sería atendido primero, que no es lo que un
 * banco quiere.
 *
 * ¿Cómo conservan head y tail el orden de llegada?
 * ------------------------------------------------
 * Cada cliente ocupa un nodo de una lista enlazada simple. Al agregar un
 * cliente se engancha un nodo nuevo detrás del nodo apuntado por "tail" y se
 * actualiza "tail" para que apunte a ese nodo nuevo; así los nodos quedan
 * ordenados por llegada, del más antiguo al más reciente. El nodo "head"
 * siempre es el primero que llegó (el próximo a atender) y "tail" siempre es el
 * último que llegó (el final de la fila). Atender al próximo consiste en sacar
 * por head y avanzar head al siguiente nodo, respetando el orden FIFO. Mantener
 * ambas referencias permite agregar por tail y atender por head en O(1), sin
 * recorrer la lista; con una sola referencia, una de las dos operaciones
 * costaría O(n).
 *
 * El número de turno viaja dentro de cada objeto Cliente, por lo que se conserva
 * intacto al agregar, consultar, atender e imprimir.
 * ============================================================================
 */

/**
 * Sistema de atención de clientes de un banco que modela la fila como una cola
 * implementada sobre una lista enlazada simple con referencias {@code head} y
 * {@code tail}.
 *
 * <p>{@code head} representa al próximo cliente que será atendido (el frente de
 * la fila) y {@code tail} al último que llegó (el final de la fila). La
 * secuencia de nodos desde {@code head} hasta {@code tail} refleja el orden de
 * llegada, que es justamente el orden de atención (FIFO).</p>
 *
 * <h2>¿Por qué se necesitan head y tail?</h2>
 * <p>La fila debe respetar el orden FIFO: se atiende por un extremo (el frente)
 * y se agrega por el otro (el final). Si solo se mantuviera {@code head},
 * agregar al final obligaría a recorrer toda la lista hasta el último nodo, lo
 * que costaría O(n). Mantener ambas referencias permite agregar por
 * {@code tail} y atender por {@code head}, logrando O(1) en las dos
 * operaciones.</p>
 *
 * <p>No se utilizan arrays ni colecciones de Java: los clientes se almacenan
 * únicamente en instancias de {@link Nodo}.</p>
 */
public class SistemaAtencionClientes {

    /**
     * Nodo de la lista enlazada simple.
     *
     * <p>Cada nodo guarda un {@link Cliente} y una referencia al siguiente nodo.
     * El último nodo apunta a {@code null}.</p>
     */
    private static final class Nodo {

        /** Cliente almacenado en este nodo. */
        private final Cliente cliente;

        /** Referencia al siguiente nodo de la fila (null si es el último). */
        private Nodo siguiente;

        /**
         * Crea un nodo con un cliente y una referencia al siguiente nodo.
         *
         * @param cliente   cliente a almacenar.
         * @param siguiente nodo que le sigue en la fila.
         */
        private Nodo(Cliente cliente, Nodo siguiente) {
            this.cliente = cliente;
            this.siguiente = siguiente;
        }
    }

    /**
     * Datos de un cliente que llega al banco.
     *
     * <p>Incluye el número de turno, que identifica el orden de llegada y se
     * conserva en todas las operaciones del sistema.</p>
     */
    public static final class Cliente {

        /** Nombre del cliente. */
        private final String nombre;

        /** Número de turno asignado al cliente. */
        private final int numeroTurno;

        /** Motivo de la consulta del cliente. */
        private final String motivoConsulta;

        /**
         * Crea un cliente con sus datos.
         *
         * @param nombre         nombre del cliente; no puede ser nulo ni vacío.
         * @param numeroTurno    número de turno; debe ser positivo.
         * @param motivoConsulta motivo de la consulta; no puede ser nulo ni
         *                       vacío.
         * @throws IllegalArgumentException si algún dato no es válido.
         */
        public Cliente(String nombre, int numeroTurno, String motivoConsulta) {
            if (nombre == null || nombre.trim().isEmpty()) {
                throw new IllegalArgumentException(
                        "El nombre del cliente no puede ser nulo ni vacío.");
            }
            if (numeroTurno <= 0) {
                throw new IllegalArgumentException(
                        "El número de turno debe ser positivo.");
            }
            if (motivoConsulta == null || motivoConsulta.trim().isEmpty()) {
                throw new IllegalArgumentException(
                        "El motivo de consulta no puede ser nulo ni vacío.");
            }
            this.nombre = nombre.trim();
            this.numeroTurno = numeroTurno;
            this.motivoConsulta = motivoConsulta.trim();
        }

        /**
         * Devuelve el nombre del cliente.
         *
         * @return nombre del cliente.
         */
        public String getNombre() {
            return nombre;
        }

        /**
         * Devuelve el número de turno del cliente.
         *
         * @return número de turno.
         */
        public int getNumeroTurno() {
            return numeroTurno;
        }

        /**
         * Devuelve el motivo de consulta del cliente.
         *
         * @return motivo de la consulta.
         */
        public String getMotivoConsulta() {
            return motivoConsulta;
        }

        /**
         * Representación legible del cliente, incluyendo su número de turno.
         *
         * @return texto con turno, nombre y motivo.
         */
        @Override
        public String toString() {
            return "Turno " + numeroTurno + " - " + nombre
                    + " (" + motivoConsulta + ")";
        }
    }

    /** Primer nodo de la lista; próximo cliente que será atendido (frente). */
    private Nodo head;

    /** Último nodo de la lista; último cliente que llegó (final de la fila). */
    private Nodo tail;

    /** Cantidad de clientes que esperan en la fila. */
    private int tamanio;

    /**
     * Crea un sistema de atención con la fila vacía.
     */
    public SistemaAtencionClientes() {
        this.head = null;
        this.tail = null;
        this.tamanio = 0;
    }

    /**
     * Agrega un cliente al final de la fila.
     *
     * <p>El nuevo nodo se enlaza detrás de {@code tail} y pasa a ser el nuevo
     * final. No se recorre la lista, por lo que la operación es O(1).</p>
     *
     * @param cliente cliente a agregar; no puede ser nulo.
     * @throws IllegalArgumentException si el cliente es nulo.
     */
    public void agregarCliente(Cliente cliente) {
        if (cliente == null) {
            throw new IllegalArgumentException(
                    "No se puede agregar un cliente nulo a la fila.");
        }

        // El nuevo nodo siempre termina la fila, por eso su "siguiente" es null.
        Nodo nuevo = new Nodo(cliente, null);

        if (estaVacia()) {
            // Primer cliente: head y tail apuntan al mismo nodo.
            head = nuevo;
        } else {
            // Se engancha al final y se actualiza tail.
            tail.siguiente = nuevo;
        }
        tail = nuevo;
        tamanio++;
    }

    /**
     * Atiende (quita y devuelve) al cliente que está al frente de la fila.
     *
     * <p>Saca el primer nodo de la lista, operación O(1). Si era el último
     * cliente esperando, ambas referencias quedan vacías. Si la fila está vacía,
     * informa la situación y devuelve {@code null} sin alterar el estado ni
     * provocar un error.</p>
     *
     * @return el cliente atendido, o {@code null} si la fila está vacía.
     */
    public Cliente atenderProximo() {
        if (estaVacia()) {
            System.out.println("No hay clientes en la fila para atender.");
            return null;
        }

        Cliente atendido = head.cliente;
        head = head.siguiente; // Avanza el frente al siguiente nodo.
        tamanio--;

        if (head == null) {
            // Se atendió al último cliente: la fila vuelve a estar vacía.
            tail = null;
        }
        return atendido;
    }

    /**
     * Devuelve quién está al frente de la fila sin quitarlo.
     *
     * <p>Es O(1) porque solo lee el nodo apuntado por {@code head}. Si la fila
     * está vacía, informa la situación y devuelve {@code null} sin alterar el
     * estado ni provocar un error.</p>
     *
     * @return el próximo cliente a atender, o {@code null} si la fila está
     *         vacía.
     */
    public Cliente consultarSiguiente() {
        if (estaVacia()) {
            System.out.println("No hay clientes en la fila para consultar.");
            return null;
        }
        return head.cliente;
    }

    /**
     * Indica si la fila está vacía.
     *
     * @return {@code true} si no hay clientes esperando; {@code false} en caso
     *         contrario.
     */
    public boolean estaVacia() {
        return head == null;
    }

    /**
     * Devuelve la cantidad de clientes que esperan en la fila.
     *
     * @return tamaño actual de la fila.
     */
    public int tamanio() {
        return tamanio;
    }

    /**
     * Muestra la fila desde el primero que llegó y todavía espera hasta el
     * último, sin modificar la cola. Si no hay clientes, lo informa.
     *
     * <p>Recorre la lista una sola vez, por lo que es O(n).</p>
     */
    public void imprimirFila() {
        if (estaVacia()) {
            System.out.println("La fila está vacía: no hay clientes esperando.");
            return;
        }

        System.out.println("Fila de atención (primero -> último):");
        int posicion = 1;
        Nodo actual = head;
        while (actual != null) {
            System.out.println("  " + posicion + ". " + actual.cliente);
            actual = actual.siguiente;
            posicion++;
        }
    }

    /**
     * Punto de entrada de prueba. Agrega varios clientes con turnos explícitos,
     * consulta quién sigue, atiende clientes, imprime la fila y muestra el caso
     * de fila vacía.
     *
     * @param args argumentos de línea de comandos (no se usan).
     */
    public static void main(String[] args) {
        SistemaAtencionClientes banco = new SistemaAtencionClientes();

        System.out.println("== Fila vacía al inicio ==");
        System.out.println("estaVacia(): " + banco.estaVacia());
        banco.imprimirFila();
        Cliente sinNadie = banco.consultarSiguiente();
        System.out.println("consultarSiguiente(): " + sinNadie);
        Cliente nadieAtendido = banco.atenderProximo();
        System.out.println("atenderProximo(): " + nadieAtendido);
        System.out.println("estaVacia() sigue siendo: " + banco.estaVacia());

        System.out.println();
        System.out.println("== Llegan clientes ==");
        banco.agregarCliente(new Cliente("Lucía", 1, "Apertura de cuenta"));
        banco.agregarCliente(new Cliente("Martín", 2, "Depósito"));
        banco.agregarCliente(new Cliente("Sofía", 3, "Préstamo personal"));
        banco.agregarCliente(new Cliente("Diego", 4, "Consulta de saldo"));

        System.out.println("tamanio(): " + banco.tamanio());
        System.out.println("consultarSiguiente(): " + banco.consultarSiguiente());
        banco.imprimirFila();

        System.out.println();
        System.out.println("== Se atienden clientes (FIFO) ==");
        System.out.println("Atendido: " + banco.atenderProximo());
        System.out.println("Atendido: " + banco.atenderProximo());
        System.out.println("consultarSiguiente(): " + banco.consultarSiguiente());
        banco.imprimirFila();

        System.out.println();
        System.out.println("== Se vacía la fila ==");
        System.out.println("Atendido: " + banco.atenderProximo());
        System.out.println("Atendido: " + banco.atenderProximo());
        System.out.println("estaVacia(): " + banco.estaVacia());
        banco.imprimirFila();
        System.out.println("atenderProximo(): " + banco.atenderProximo());
    }
}

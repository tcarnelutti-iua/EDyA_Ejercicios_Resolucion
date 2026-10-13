/*
 * ============================================================================
 * PROMPT ORIGINAL
 * ============================================================================
 * Implementá una cola de impresión usando una cola enlazada y guardá la solución
 * en:
 * Guía_Pilas_colas_lista_enlazada/Ejercicio_9/ColaImpresion.java
 *
 * Requisitos:
 * - Definí una clase pública ColaImpresion.
 * - Cada trabajo debe contener nombre del archivo, cantidad de páginas y usuario
 *   que lo envió. Definí una clase TrabajoImpresion con esos datos dentro del
 *   mismo archivo.
 * - Implementá la cola con una lista enlazada simple y referencias head y tail.
 *   head representa el próximo trabajo y tail el último agregado.
 * - Implementá estas operaciones:
 *   - agregarTrabajo(TrabajoImpresion trabajo): agrega un trabajo al final de la
 *     cola.
 *   - imprimirProximoTrabajo(): retira el trabajo del frente y muestra sus datos
 *     para representar que fue impreso. No hace falta conectarse a una impresora
 *     real.
 *   - consultarProximoArchivo(): informa el nombre del archivo que está al
 *     frente, sin retirarlo.
 *   - mostrarPendientes(): muestra todos los trabajos pendientes desde el próximo
 *     a imprimir hasta el último agregado, sin modificar la cola.
 * - La impresora debe procesar los trabajos en FIFO: el primero enviado es el
 *   primero que se imprime. Encolar al final mediante tail y procesar desde head
 *   conserva el orden de llegada.
 * - Si no hay trabajos, imprimir el próximo debe informarlo sin modificar la cola;
 *   consultar el próximo archivo también debe informar que la cola está vacía.
 * - Al agregar el primer trabajo, head y tail deben referirse al mismo nodo. Al
 *   retirar el último, ambas referencias deben quedar vacías.
 * - Agregar un trabajo, imprimir el próximo y consultar el próximo archivo deben
 *   ser O(1). Mostrar todos los pendientes debe ser O(n).
 * - Incluí un main breve que agregue varios trabajos, consulte el próximo archivo,
 *   muestre los pendientes e imprima trabajos para demostrar el orden FIFO.
 *   Incluí también el caso de cola vacía.
 * - No uses arrays ni colecciones de Java para almacenar los trabajos.
 *
 * Incluí al comienzo del archivo, como comentario, este prompt completo y explicá
 * por qué FIFO corresponde a una cola de impresión. Aclarà si hubo ajustes
 * posteriores. Si no modificaste los requisitos, indicá:
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
 * ¿Por qué FIFO corresponde a una cola de impresión?
 * ---------------------------------------------------
 * En una impresora compartida la regla es "el primero que manda a imprimir es el
 * primero que se imprime". Ese criterio se llama FIFO (First In, First Out) y es
 * exactamente el comportamiento de una cola. Los trabajos se incorporan por un
 * extremo (el final de la cola) y se procesan por el otro (el frente), de modo
 * que ningún trabajo se saltea: quien envió antes se imprime antes, sin importar
 * cuántas páginas tenga. Si en lugar de una cola usáramos una pila (LIFO), el
 * último trabajo enviado se imprimiría primero y los trabajos viejos quedarían
 * postergados, algo que no se espera de una impresora de uso compartido.
 *
 * ¿Cómo conservan head y tail el orden de llegada?
 * ------------------------------------------------
 * Cada trabajo ocupa un nodo de una lista enlazada simple. Al agregar un trabajo
 * se engancha un nodo nuevo detrás del nodo apuntado por "tail" y se actualiza
 * "tail" para que apunte a ese nodo nuevo; así los nodos quedan ordenados por
 * llegada, del más antiguo al más reciente. El nodo "head" siempre es el primero
 * que llegó (el próximo a imprimir) y "tail" siempre es el último que llegó (el
 * final de la cola de espera). Imprimir el próximo consiste en sacar por head y
 * avanzar head al siguiente nodo, respetando el orden FIFO. Mantener ambas
 * referencias permite agregar por tail y procesar por head en O(1), sin recorrer
 * la lista; con una sola referencia, una de las dos operaciones costaría O(n).
 * ============================================================================
 */

/**
 * Cola de impresión que modela los trabajos pendientes de una impresora como una
 * cola implementada sobre una lista enlazada simple con referencias
 * {@code head} y {@code tail}.
 *
 * <p>{@code head} representa al próximo trabajo que será impreso (el frente de
 * la cola) y {@code tail} al último que se agregó (el final de la cola). La
 * secuencia de nodos desde {@code head} hasta {@code tail} refleja el orden de
 * llegada, que es el orden en que los trabajos se imprimen (FIFO).</p>
 *
 * <h2>¿Por qué se necesitan head y tail?</h2>
 * <p>La cola debe respetar el orden FIFO: se procesa por un extremo (el frente)
 * y se agrega por el otro (el final). Si solo se mantuviera {@code head}, agregar
 * al final obligaría a recorrer toda la lista hasta el último nodo, lo que
 * costaría O(n). Mantener ambas referencias permite agregar por {@code tail} y
 * procesar por {@code head}, logrando O(1) en las dos operaciones.</p>
 *
 * <p>No se utilizan arrays ni colecciones de Java: los trabajos se almacenan
 * únicamente en instancias de {@link Nodo}.</p>
 */
public class ColaImpresion {

    /**
     * Nodo de la lista enlazada simple.
     *
     * <p>Cada nodo guarda un {@link TrabajoImpresion} y una referencia al
     * siguiente nodo. El último nodo apunta a {@code null}.</p>
     */
    private static final class Nodo {

        /** Trabajo de impresión almacenado en este nodo. */
        private final TrabajoImpresion trabajo;

        /** Referencia al siguiente nodo de la cola (null si es el último). */
        private Nodo siguiente;

        /**
         * Crea un nodo con un trabajo y una referencia al siguiente nodo.
         *
         * @param trabajo   trabajo a almacenar.
         * @param siguiente nodo que le sigue en la cola.
         */
        private Nodo(TrabajoImpresion trabajo, Nodo siguiente) {
            this.trabajo = trabajo;
            this.siguiente = siguiente;
        }
    }

    /**
     * Datos de un trabajo de impresión enviado a la cola.
     *
     * <p>Incluye el nombre del archivo, la cantidad de páginas y el usuario que lo
     * envió. Toda esta información se conserva intacta en cada operación.</p>
     */
    public static final class TrabajoImpresion {

        /** Nombre del archivo a imprimir. */
        private final String nombreArchivo;

        /** Cantidad de páginas del trabajo. */
        private final int paginas;

        /** Usuario que envió el trabajo. */
        private final String usuario;

        /**
         * Crea un trabajo de impresión con sus datos.
         *
         * @param nombreArchivo nombre del archivo; no puede ser nulo ni vacío.
         * @param paginas       cantidad de páginas; debe ser positiva.
         * @param usuario       usuario que envía el trabajo; no puede ser nulo ni
         *                      vacío.
         * @throws IllegalArgumentException si algún dato no es válido.
         */
        public TrabajoImpresion(String nombreArchivo, int paginas, String usuario) {
            if (nombreArchivo == null || nombreArchivo.trim().isEmpty()) {
                throw new IllegalArgumentException(
                        "El nombre del archivo no puede ser nulo ni vacío.");
            }
            if (paginas <= 0) {
                throw new IllegalArgumentException(
                        "La cantidad de páginas debe ser positiva.");
            }
            if (usuario == null || usuario.trim().isEmpty()) {
                throw new IllegalArgumentException(
                        "El usuario no puede ser nulo ni vacío.");
            }
            this.nombreArchivo = nombreArchivo.trim();
            this.paginas = paginas;
            this.usuario = usuario.trim();
        }

        /**
         * Devuelve el nombre del archivo del trabajo.
         *
         * @return nombre del archivo.
         */
        public String getNombreArchivo() {
            return nombreArchivo;
        }

        /**
         * Devuelve la cantidad de páginas del trabajo.
         *
         * @return cantidad de páginas.
         */
        public int getPaginas() {
            return paginas;
        }

        /**
         * Devuelve el usuario que envió el trabajo.
         *
         * @return usuario que envió el trabajo.
         */
        public String getUsuario() {
            return usuario;
        }

        /**
         * Representación legible del trabajo.
         *
         * @return texto con archivo, páginas y usuario.
         */
        @Override
        public String toString() {
            return nombreArchivo + " (" + paginas + " páginas, enviado por "
                    + usuario + ")";
        }
    }

    /** Primer nodo de la lista; próximo trabajo a imprimir (frente). */
    private Nodo head;

    /** Último nodo de la lista; último trabajo agregado (final). */
    private Nodo tail;

    /** Cantidad de trabajos pendientes en la cola. */
    private int tamanio;

    /**
     * Crea una cola de impresión vacía.
     */
    public ColaImpresion() {
        this.head = null;
        this.tail = null;
        this.tamanio = 0;
    }

    /**
     * Agrega un trabajo al final de la cola de impresión.
     *
     * <p>El nuevo nodo se enlaza detrás de {@code tail} y pasa a ser el nuevo
     * final. No se recorre la lista, por lo que la operación es O(1).</p>
     *
     * @param trabajo trabajo a agregar; no puede ser nulo.
     * @throws IllegalArgumentException si el trabajo es nulo.
     */
    public void agregarTrabajo(TrabajoImpresion trabajo) {
        if (trabajo == null) {
            throw new IllegalArgumentException(
                    "No se puede agregar un trabajo nulo a la cola de impresión.");
        }

        // El nuevo nodo siempre termina la cola, por eso su "siguiente" es null.
        Nodo nuevo = new Nodo(trabajo, null);

        if (estaVacia()) {
            // Primer trabajo: head y tail apuntan al mismo nodo.
            head = nuevo;
        } else {
            // Se engancha al final y se actualiza tail.
            tail.siguiente = nuevo;
        }
        tail = nuevo;
        tamanio++;
    }

    /**
     * Imprime (retira y muestra) el trabajo que está al frente de la cola.
     *
     * <p>Saca el primer nodo de la lista, operación O(1), y muestra sus datos para
     * representar que fue impreso. Si era el último trabajo pendiente, ambas
     * referencias quedan vacías. Si la cola está vacía, informa la situación y
     * devuelve {@code null} sin alterar el estado ni provocar un error.</p>
     *
     * @return el trabajo impreso, o {@code null} si la cola está vacía.
     */
    public TrabajoImpresion imprimirProximoTrabajo() {
        if (estaVacia()) {
            System.out.println("No hay trabajos en la cola de impresión para imprimir.");
            return null;
        }

        TrabajoImpresion impreso = head.trabajo;
        head = head.siguiente; // Avanza el frente al siguiente nodo.
        tamanio--;

        if (head == null) {
            // Se imprimió el último trabajo: la cola vuelve a estar vacía.
            tail = null;
        }

        System.out.println("Imprimiendo: " + impreso);
        return impreso;
    }

    /**
     * Informa el nombre del archivo que está al frente de la cola, sin retirarlo.
     *
     * <p>Es O(1) porque solo lee el nodo apuntado por {@code head}. Si la cola está
     * vacía, informa la situación y devuelve {@code null} sin alterar el estado ni
     * provocar un error.</p>
     *
     * @return el nombre del próximo archivo a imprimir, o {@code null} si la cola
     *         está vacía.
     */
    public String consultarProximoArchivo() {
        if (estaVacia()) {
            System.out.println("La cola de impresión está vacía: no hay próximo archivo.");
            return null;
        }

        String nombre = head.trabajo.getNombreArchivo();
        System.out.println("Próximo archivo a imprimir: " + nombre);
        return nombre;
    }

    /**
     * Indica si la cola de impresión está vacía.
     *
     * @return {@code true} si no hay trabajos pendientes; {@code false} en caso
     *         contrario.
     */
    public boolean estaVacia() {
        return head == null;
    }

    /**
     * Devuelve la cantidad de trabajos pendientes en la cola.
     *
     * @return tamaño actual de la cola.
     */
    public int tamanio() {
        return tamanio;
    }

    /**
     * Muestra todos los trabajos pendientes desde el próximo a imprimir hasta el
     * último agregado, sin modificar la cola. Si no hay trabajos, lo informa.
     *
     * <p>Recorre la lista una sola vez, por lo que es O(n).</p>
     */
    public void mostrarPendientes() {
        if (estaVacia()) {
            System.out.println("La cola de impresión está vacía: no hay trabajos pendientes.");
            return;
        }

        System.out.println("Trabajos pendientes (próximo -> último):");
        int posicion = 1;
        Nodo actual = head;
        while (actual != null) {
            System.out.println("  " + posicion + ". " + actual.trabajo);
            actual = actual.siguiente;
            posicion++;
        }
    }

    /**
     * Punto de entrada de prueba. Agrega varios trabajos, consulta el próximo
     * archivo, muestra los pendientes e imprime trabajos para demostrar el orden
     * FIFO, incluyendo el caso de cola vacía.
     *
     * @param args argumentos de línea de comandos (no se usan).
     */
    public static void main(String[] args) {
        ColaImpresion impresora = new ColaImpresion();

        System.out.println("== Cola vacía al inicio ==");
        System.out.println("estaVacia(): " + impresora.estaVacia());
        impresora.mostrarPendientes();
        impresora.consultarProximoArchivo();
        impresora.imprimirProximoTrabajo();
        System.out.println("estaVacia() sigue siendo: " + impresora.estaVacia());

        System.out.println();
        System.out.println("== Se envían trabajos a la impresora ==");
        impresora.agregarTrabajo(new TrabajoImpresion("informe.pdf", 12, "Lucía"));
        impresora.agregarTrabajo(new TrabajoImpresion("tesis.docx", 120, "Martín"));
        impresora.agregarTrabajo(new TrabajoImpresion("foto.png", 1, "Sofía"));
        impresora.agregarTrabajo(new TrabajoImpresion("contrato.pdf", 8, "Diego"));

        System.out.println("tamanio(): " + impresora.tamanio());
        impresora.consultarProximoArchivo();
        impresora.mostrarPendientes();

        System.out.println();
        System.out.println("== Se imprimen trabajos (FIFO) ==");
        impresora.imprimirProximoTrabajo();
        impresora.imprimirProximoTrabajo();
        impresora.consultarProximoArchivo();
        impresora.mostrarPendientes();

        System.out.println();
        System.out.println("== Se vacía la cola ==");
        impresora.imprimirProximoTrabajo();
        impresora.imprimirProximoTrabajo();
        System.out.println("estaVacia(): " + impresora.estaVacia());
        impresora.mostrarPendientes();
        impresora.imprimirProximoTrabajo();
    }
}

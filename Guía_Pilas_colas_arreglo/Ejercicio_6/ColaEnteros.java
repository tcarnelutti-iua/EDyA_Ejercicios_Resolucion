/**
 * Cola de enteros (FIFO) de tamaño fijo implementada sobre un arreglo circular.
 *
 * <p>La capacidad se define al construir la cola y <b>no</b> se redimensiona:
 * cuando el arreglo se llena, {@link #enqueue(int)} informa el error sin
 * modificar el contenido.</p>
 *
 * <p>Al ser <b>circular</b>, las posiciones liberadas en la parte delantera al
 * desencolar se reutilizan al final del arreglo. El estado se describe con
 * tres variables:</p>
 * <ul>
 *   <li>{@code front}: índice del primer elemento, el próximo que devolverán
 *       {@link #dequeue()} o {@link #front()}.</li>
 *   <li>{@code rear}: índice del último elemento agregado.</li>
 *   <li>{@code size}: cantidad de elementos; permite distinguir sin ambigüedad
 *       la cola vacía de la llena.</li>
 * </ul>
 *
 * <p>Transiciones de índices (siempre con aritmética modular para el avance
 * circular):</p>
 * <ul>
 *   <li>Cola vacía &rarr; {@code front = 0}, {@code rear = -1}, {@code size = 0}.</li>
 *   <li>{@code enqueue} &rarr; {@code rear = (rear + 1) % capacidad}, {@code size++}.</li>
 *   <li>{@code dequeue} &rarr; se devuelve {@code datos[front]}, luego
 *       {@code front = (front + 1) % capacidad} y {@code size--}.</li>
 *   <li>Al eliminar el último elemento ({@code size == 0}) se restablece el
 *       estado canónico {@code front = 0}, {@code rear = -1}.</li>
 * </ul>
 *
 * <p>{@code isEmpty} es verdadero cuando {@code size == 0} e {@code isFull}
 * cuando {@code size == capacidad}. No se usa ninguna colección para almacenar
 * los datos: solo el arreglo interno y el contador {@code size}.</p>
 *
 * @author Agente Desarrollador
 */
public class ColaEnteros {

    /** Arreglo circular que almacena los elementos de la cola. */
    private final int[] datos;

    /** Índice del primer elemento (próximo en salir). */
    private int front;

    /** Índice del último elemento insertado; {@code -1} si está vacía. */
    private int rear;

    /** Cantidad de elementos actualmente en la cola. */
    private int size;

    /**
     * Construye una cola vacía con la capacidad indicada.
     *
     * @param capacidad número máximo de elementos que podrá almacenar (debe ser &gt; 0)
     * @throws IllegalArgumentException si {@code capacidad} es menor o igual a cero
     */
    public ColaEnteros(int capacidad) {
        // Validación de caso límite: una capacidad no positiva no tiene sentido.
        if (capacidad <= 0) {
            throw new IllegalArgumentException(
                    "La capacidad debe ser positiva. Se recibió: " + capacidad);
        }
        this.datos = new int[capacidad];
        this.front = 0;
        this.rear = -1;
        this.size = 0;
    }

    /**
     * Agrega un valor al final de la cola.
     *
     * <p>Si la cola está llena, informa el error sin modificar su contenido.</p>
     *
     * @param valor entero a encolar
     * @throws IllegalStateException si la cola está llena
     */
    public void enqueue(int valor) {
        if (isFull()) {
            throw new IllegalStateException(
                    "No se puede encolar: la cola está llena (capacidad "
                            + capacidad() + ").");
        }
        // Avance circular: al llegar al último índice se regresa a la posición 0.
        rear = (rear + 1) % capacidad();
        datos[rear] = valor;
        size++;
    }

    /**
     * Elimina y devuelve el elemento del frente de la cola.
     *
     * @return el elemento que estaba al frente
     * @throws IllegalStateException si la cola está vacía
     */
    public int dequeue() {
        if (isEmpty()) {
            throw new IllegalStateException(
                    "No se puede desencolar: la cola está vacía.");
        }
        int valorFrente = datos[front];
        // Avance circular del frente; la posición liberada podrá reutilizarse.
        front = (front + 1) % capacidad();
        size--;
        // Si se eliminó el último elemento, se restablece el estado canónico.
        if (size == 0) {
            front = 0;
            rear = -1;
        }
        return valorFrente;
    }

    /**
     * Devuelve el elemento del frente sin eliminarlo.
     *
     * @return el elemento que está al frente
     * @throws IllegalStateException si la cola está vacía
     */
    public int front() {
        if (isEmpty()) {
            throw new IllegalStateException(
                    "No se puede consultar el frente: la cola está vacía.");
        }
        return datos[front];
    }

    /**
     * Indica si la cola no tiene elementos.
     *
     * @return {@code true} si {@code size == 0}
     */
    public boolean isEmpty() {
        return size == 0;
    }

    /**
     * Indica si la cola alcanzó su capacidad máxima.
     *
     * @return {@code true} si {@code size == capacidad}
     */
    public boolean isFull() {
        return size == capacidad();
    }

    /**
     * Devuelve la cantidad de elementos almacenados.
     *
     * @return el número de elementos actuales
     */
    public int size() {
        return size;
    }

    /**
     * Devuelve la capacidad máxima del arreglo interno.
     *
     * @return la capacidad de la cola
     */
    public int capacidad() {
        return datos.length;
    }

    /**
     * Representación del contenido en orden FIFO, útil para depuración y
     * para la demostración. No modifica la cola.
     *
     * @return los elementos desde el frente hasta el último agregado
     */
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder("[frente -> fin] ");
        for (int i = 0; i < size; i++) {
            // Se recorre desde front respetando el avance circular.
            int indice = (front + i) % capacidad();
            sb.append(datos[indice]);
            if (i < size - 1) {
                sb.append(", ");
            }
        }
        return sb.toString();
    }

    // ------------------------------------------------------------------
    // Demostración ejecutable
    // ------------------------------------------------------------------

    /** Contador de comprobaciones fallidas para resumir la demostración. */
    private static int fallos = 0;

    /**
     * Comprueba una condición de la demostración e imprime el resultado.
     *
     * @param descripcion qué se está comprobando
     * @param condicion   resultado esperado
     */
    private static void verificar(String descripcion, boolean condicion) {
        if (condicion) {
            System.out.println("  [OK]    " + descripcion);
        } else {
            System.out.println("  [FALLO] " + descripcion);
            fallos++;
        }
    }

    /**
     * Ejecuta una demostración breve de la cola: orden FIFO, consulta con
     * {@code front} sin eliminar, casos de vacía y llena, y reutilización de
     * una posición liberada tras {@code dequeue}.
     *
     * @param args no se usan
     */
    public static void main(String[] args) {
        System.out.println("=== DEMOSTRACIÓN DE ColaEnteros (arreglo circular) ===\n");

        // Caso 1: cola recién creada -> vacía, no llena, tamaño 0.
        System.out.println("1. Cola recién creada (capacidad 3)");
        ColaEnteros cola = new ColaEnteros(3);
        verificar("isEmpty es verdadero", cola.isEmpty());
        verificar("isFull es falso", !cola.isFull());
        verificar("size() es 0", cola.size() == 0);

        // Caso 2 y 3: orden FIFO y consulta con front sin eliminar.
        System.out.println("\n2. Orden FIFO y front() sin eliminar");
        cola.enqueue(10);
        cola.enqueue(20);
        cola.enqueue(30);
        System.out.println("  Contenido: " + cola);
        verificar("front() devuelve 10", cola.front() == 10);
        verificar("front() no elimina (size sigue 3)", cola.size() == 3);
        verificar("dequeue() devuelve 10 (primero entrado)", cola.dequeue() == 10);
        verificar("dequeue() devuelve 20", cola.dequeue() == 20);
        verificar("front() ahora es 30", cola.front() == 30);

        // Caso de reutilización: se liberó la posición 0 y se usa al encolar.
        System.out.println("\n2b. Reutilización de posición liberada (circular)");
        cola.enqueue(40); // rear avanza circularmente y reutiliza el hueco
        verificar("tras encolar 40 el tamaño es 2", cola.size() == 2);
        verificar("contenido FIFO correcto [30, 40]", "[frente -> fin] 30, 40".equals(cola.toString()));
        verificar("front() sigue siendo 30", cola.front() == 30);

        // Caso llena: capacidad agotada y error sin modificar la cola.
        System.out.println("\n3. Cola llena");
        cola.enqueue(50); // ahora hay 3 elementos (capacidad 3)
        verificar("isFull() es verdadero", cola.isFull());
        verificar("size() == capacidad", cola.size() == cola.capacidad());
        String antes = cola.toString();
        boolean lanzoLlena = false;
        try {
            cola.enqueue(60); // no debe caber
        } catch (IllegalStateException e) {
            lanzoLlena = true;
        }
        verificar("enqueue en cola llena lanza IllegalStateException", lanzoLlena);
        verificar("la cola no cambió al intentar encolar en llena", antes.equals(cola.toString()));

        // Vaciar la cola y verificar el caso vacía.
        System.out.println("\n4. Vaciar la cola y caso vacía");
        verificar("dequeue() = 30", cola.dequeue() == 30);
        verificar("dequeue() = 40", cola.dequeue() == 40);
        verificar("dequeue() = 50", cola.dequeue() == 50);
        verificar("isEmpty() es verdadero tras vaciar", cola.isEmpty());
        verificar("size() == 0 tras vaciar", cola.size() == 0);

        boolean lanzoDequeue = false;
        try {
            cola.dequeue();
        } catch (IllegalStateException e) {
            lanzoDequeue = true;
        }
        verificar("dequeue en cola vacía lanza IllegalStateException", lanzoDequeue);

        boolean lanzoFront = false;
        try {
            cola.front();
        } catch (IllegalStateException e) {
            lanzoFront = true;
        }
        verificar("front en cola vacía lanza IllegalStateException", lanzoFront);

        // Constructor con capacidad inválida.
        System.out.println("\n5. Capacidad inválida en el constructor");
        verificarCapacidadInvalida(0);
        verificarCapacidadInvalida(-3);

        System.out.println("\n=== RESULTADO ===");
        if (fallos == 0) {
            System.out.println("Todas las comprobaciones pasaron correctamente.");
        } else {
            System.out.println("Hubo " + fallos + " comprobación(es) fallida(s).");
            throw new AssertionError("La demostración de ColaEnteros falló.");
        }
    }

    /**
     * Comprueba que crear una cola con capacidad no positiva lance
     * {@link IllegalArgumentException}.
     *
     * @param capacidad valor inválido a probar
     */
    private static void verificarCapacidadInvalida(int capacidad) {
        boolean lanzo = false;
        try {
            new ColaEnteros(capacidad);
        } catch (IllegalArgumentException e) {
            lanzo = true;
        }
        verificar("capacidad " + capacidad + " lanza IllegalArgumentException", lanzo);
    }
}

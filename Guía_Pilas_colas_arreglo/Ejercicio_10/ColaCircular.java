/**
 * Cola <b>circular</b> (FIFO) de enteros implementada sobre un arreglo de
 * capacidad fija.
 *
 * <p><b>¿Por qué es circular?</b> Los índices {@code front} y {@code rear} no se
 * detienen al llegar al final del arreglo: avanzan siempre con la operación
 * módulo</p>
 *
 * <pre>{@code indice = (indice + 1) % capacidad}</pre>
 *
 * <p>de modo que, tras la última posición ({@code capacidad - 1}), el siguiente
 * índice vuelve a {@code 0}. Así el arreglo se comporta como un anillo y las
 * posiciones liberadas al inicio pueden reutilizarse sin desperdicio.</p>
 *
 * <p>Estado interno (representación adoptada para evitar ambigüedades):</p>
 * <ul>
 *   <li>{@code front}: índice de la próxima posición desde la que se
 *       <b>retirará</b> un elemento (el más antiguo, por FIFO).</li>
 *   <li>{@code rear}: índice de la próxima posición <b>libre</b> donde se
 *       <b>agregará</b> un elemento.</li>
 *   <li>{@code size}: cantidad de elementos actuales. Es el dato que desambigua
 *       los estados: la cola está vacía cuando {@code size == 0} y llena cuando
 *       {@code size == capacidad}, aunque entonces {@code front} y {@code rear}
 *       coincidan.</li>
 *   <li>{@code datos}: arreglo que almacena los elementos. Las posiciones
 *       liberadas por {@code dequeue} se marcan con {@link #VACIO} para dejar
 *       visible que quedaron libres.</li>
 * </ul>
 *
 * @author Agente Desarrollador
 */
public class ColaCircular {

    /** Valor centinela que marca una posición liberada del arreglo. */
    private static final int VACIO = Integer.MIN_VALUE;

    /** Arreglo circular que almacena los elementos de la cola. */
    private final int[] datos;

    /** Índice de la próxima posición desde la que se retirará (próximo en salir). */
    private int front;

    /** Índice de la próxima posición libre donde se agregará. */
    private int rear;

    /** Cantidad de elementos actualmente almacenados. */
    private int size;

    /**
     * Construye una cola circular vacía con la capacidad indicada.
     *
     * @param capacidad número máximo de elementos que podrá almacenar (debe ser &gt; 0)
     * @throws IllegalArgumentException si {@code capacidad} es menor o igual a cero
     */
    public ColaCircular(int capacidad) {
        // Caso límite: una capacidad no positiva no tiene sentido.
        if (capacidad <= 0) {
            throw new IllegalArgumentException(
                    "La capacidad debe ser positiva. Se recibió: " + capacidad);
        }
        this.datos = new int[capacidad];
        // Estado canónico de cola vacía: ambos índices al inicio y size en 0.
        this.front = 0;
        this.rear = 0;
        this.size = 0;
    }

    /**
     * Agrega un valor en la posición {@code rear} y avanza {@code rear} de forma
     * circular.
     *
     * <p>Si la cola está llena ({@code size == capacidad}) se informa el error
     * sin modificar la cola. Al final, {@code rear} avanza con
     * {@code (rear + 1) % capacidad}.</p>
     *
     * @param valor entero a encolar; no puede ser {@link #VACIO}
     * @throws IllegalArgumentException si el valor es el centinela reservado
     * @throws IllegalStateException    si la cola está llena
     */
    public void enqueue(int valor) {
        // Se reserva el centinela para marcar posiciones vacías: no es un dato válido.
        if (valor == VACIO) {
            throw new IllegalArgumentException(
                    "El valor " + VACIO + " está reservado como marca de posición vacía.");
        }
        if (isFull()) {
            throw new IllegalStateException(
                    "No se puede encolar " + valor + ": la cola está llena (size == capacidad).");
        }
        // Se escribe en la próxima posición libre y se avanza de forma circular.
        datos[rear] = valor;
        rear = (rear + 1) % datos.length;
        size++;
    }

    /**
     * Elimina y devuelve el primer elemento de la cola (FIFO).
     *
     * <p>Obtiene el valor en {@code front}, avanza {@code front} de forma circular
     * con {@code (front + 1) % capacidad} y decrementa {@code size}. La posición
     * liberada se marca con {@link #VACIO}.</p>
     *
     * @return el elemento que estaba al frente
     * @throws IllegalStateException si la cola está vacía
     */
    public int dequeue() {
        if (isEmpty()) {
            throw new IllegalStateException("No se puede desencolar: la cola está vacía.");
        }
        int valorFrente = datos[front];
        // Se marca la posición como libre (queda visible como hueco reutilizable).
        datos[front] = VACIO;
        // Avance circular del frente.
        front = (front + 1) % datos.length;
        size--;
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
            throw new IllegalStateException("No se puede consultar el frente: la cola está vacía.");
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
        return size == datos.length;
    }

    /**
     * Devuelve la cantidad de elementos actualmente almacenados.
     *
     * @return el número de elementos
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
     * Devuelve el índice actual de {@code front}.
     *
     * @return el índice del próximo elemento a retirar
     */
    public int getFront() {
        return front;
    }

    /**
     * Devuelve el índice actual de {@code rear}.
     *
     * @return el índice de la próxima posición libre donde se agregará
     */
    public int getRear() {
        return rear;
    }

    /**
     * Indica si una posición del arreglo almacena actualmente un elemento.
     *
     * <p>En una cola circular, las posiciones ocupadas son las {@code size}
     * posiciones consecutivas que comienzan en {@code front}, recorriendo el
     * arreglo de forma circular.</p>
     *
     * @param indice posición a consultar
     * @return {@code true} si la posición está dentro del tramo ocupado
     */
    public boolean posicionOcupada(int indice) {
        if (indice < 0 || indice >= datos.length || isEmpty()) {
            return false;
        }
        // Distancia circular desde front hasta el índice consultado.
        int distancia = (indice - front + datos.length) % datos.length;
        return distancia < size;
    }

    /**
     * Muestra el arreglo completo marcando las posiciones vacías, junto con los
     * índices {@code front} y {@code rear}, el contador {@code size} y el estado.
     */
    public void mostrarEstado() {
        StringBuilder arreglo = new StringBuilder();
        StringBuilder indices = new StringBuilder();
        arreglo.append("Arreglo: [ ");
        for (int i = 0; i < datos.length; i++) {
            if (posicionOcupada(i)) {
                arreglo.append(datos[i]);
            } else {
                // Posición sin elemento: liberada por dequeue o aún no usada.
                arreglo.append("vacío");
            }
            if (i < datos.length - 1) {
                arreglo.append(" | ");
            }
        }
        arreglo.append(" ]");

        indices.append("Índices:   ");
        for (int i = 0; i < datos.length; i++) {
            indices.append("  ").append(i).append("  ");
            if (i < datos.length - 1) {
                indices.append(" ");
            }
        }

        System.out.println("  " + arreglo);
        System.out.println("  " + indices);
        System.out.println("  front = " + front + "   rear = " + rear
                + "   size = " + size + "/" + capacidad()
                + (isFull() ? "   [LLENA]" : isEmpty() ? "   [VACÍA]" : ""));
    }

    /**
     * Representación del contenido en orden FIFO, útil para depuración.
     *
     * @return los elementos desde el frente hasta el último agregado
     */
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder("[frente -> fin] ");
        for (int i = 0; i < size; i++) {
            int posicion = (front + i) % datos.length;
            sb.append(datos[posicion]);
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
     * Demuestra el funcionamiento circular y la reutilización de posiciones:
     * <ol>
     *   <li>Se crea una cola de capacidad 5 y se verifica el estado inicial.</li>
     *   <li>Se agregan valores y se retiran algunos, dejando huecos al inicio.</li>
     *   <li>Se agregan más valores hasta que {@code rear} cruza el final del
     *       arreglo (índice 4) y vuelve al índice 0, reutilizando las posiciones
     *       liberadas.</li>
     *   <li>Se comprueban los estados de cola llena y cola vacía, incluidos los
     *       errores informados por {@code enqueue}, {@code dequeue} y
     *       {@code front}.</li>
     * </ol>
     *
     * @param args no se usan
     */
    public static void main(String[] args) {
        System.out.println("=== DEMOSTRACIÓN DE ColaCircular (cola CIRCULAR con arreglo) ===\n");

        // Caso 1: cola recién creada.
        System.out.println("1. Cola recién creada (capacidad 5)");
        ColaCircular cola = new ColaCircular(5);
        cola.mostrarEstado();
        verificar("la cola empieza vacía", cola.isEmpty());
        verificar("no está llena", !cola.isFull());
        verificar("size() es 0", cola.size() == 0);
        verificar("front y rear coinciden en 0 (válido porque size == 0)",
                cola.getFront() == 0 && cola.getRear() == 0);

        // Caso 2: se llena parcialmente.
        System.out.println("\n2. Se agregan 10, 20 y 30");
        cola.enqueue(10);
        cola.enqueue(20);
        cola.enqueue(30);
        cola.mostrarEstado();
        verificar("front = 0 y rear = 3", cola.getFront() == 0 && cola.getRear() == 3);
        verificar("hay 3 elementos", cola.size() == 3);
        verificar("el frente es 10 (FIFO)", cola.front() == 10);

        // Caso 3: se retiran dos; se liberan las posiciones 0 y 1.
        System.out.println("\n3. Se retiran dos elementos con dequeue");
        int primero = cola.dequeue();
        int segundo = cola.dequeue();
        System.out.println("  dequeue() devolvió: " + primero + " y luego " + segundo);
        cola.mostrarEstado();
        verificar("dequeue devolvió primero 10 (FIFO)", primero == 10);
        verificar("dequeue devolvió luego 20 (FIFO)", segundo == 20);
        verificar("front avanzó a 2", cola.getFront() == 2);
        verificar("rear sigue en 3 (no se movió al retirar)", cola.getRear() == 3);
        verificar("las posiciones 0 y 1 quedaron libres",
                !cola.posicionOcupada(0) && !cola.posicionOcupada(1));
        verificar("queda 1 elemento (el 30)", cola.size() == 1);

        // Caso 4: se agregan valores reutilizando huecos y cruzando el final.
        System.out.println("\n4. Se agregan 40, 50, 60 y 70");
        System.out.println("   (rear cruza el final: 3 -> 4 -> 0 -> 1 -> 2)");
        cola.enqueue(40);
        cola.enqueue(50);
        System.out.println("  Tras agregar 40 y 50 (rear llegó a la última posición):");
        cola.mostrarEstado();
        verificar("rear se detuvo en 0 tras dar la vuelta", cola.getRear() == 0);
        cola.enqueue(60);
        cola.enqueue(70);
        System.out.println("  Tras agregar 60 y 70 (rear reutilizó las posiciones 0 y 1):");
        cola.mostrarEstado();
        verificar("el 60 se guardó en la posición 0 (reutilizada)",
                cola.posicionOcupada(0) && cola.datos[0] == 60);
        verificar("el 70 se guardó en la posición 1 (reutilizada)",
                cola.posicionOcupada(1) && cola.datos[1] == 70);
        verificar("rear volvió a 2 tras dar la vuelta", cola.getRear() == 2);
        verificar("front, que estaba en 2, sigue en 2", cola.getFront() == 2);
        verificar("la cola quedó llena", cola.isFull());
        verificar("hay 5 elementos", cola.size() == 5);
        System.out.println("  Contenido en orden FIFO: " + cola);
        verificar("el orden FIFO es [30, 40, 50, 60, 70]",
                cola.toString().equals("[frente -> fin] 30, 40, 50, 60, 70"));
        verificar("las posiciones iniciales liberadas fueron reutilizadas",
                cola.posicionOcupada(0) && cola.posicionOcupada(1));

        // Caso 5: intentar agregar a cola llena.
        System.out.println("\n5. Se intenta agregar 80 a la cola llena");
        String antes = cola.toString();
        boolean rechazado = false;
        try {
            cola.enqueue(80);
        } catch (IllegalStateException e) {
            rechazado = true;
            System.out.println("  Rechazado -> " + e.getMessage());
        }
        verificar("enqueue fue rechazado con IllegalStateException", rechazado);
        verificar("la cola no cambió al rechazar el 80", antes.equals(cola.toString()));
        cola.mostrarEstado();
        System.out.println("  Nota: front y rear coinciden (2 y 2), pero la cola NO está vacía:");
        System.out.println("  size = 5 == capacidad, por eso se distingue como LLENA.");

        // Caso 6: vaciar la cola y comprobar el estado vacío.
        System.out.println("\n6. Se vacía la cola por completo");
        int contadorRetirados = 0;
        while (!cola.isEmpty()) {
            cola.dequeue();
            contadorRetirados++;
        }
        System.out.println("  Se retiraron " + contadorRetirados + " elementos.");
        cola.mostrarEstado();
        verificar("se retiraron los 5 elementos", contadorRetirados == 5);
        verificar("la cola quedó vacía", cola.isEmpty());
        verificar("ya no está llena", !cola.isFull());
        verificar("size() es 0", cola.size() == 0);
        verificar("front y rear vuelven a coincidir (2 y 2), pero con size == 0",
                cola.getFront() == 2 && cola.getRear() == 2);

        // Caso 7: errores en cola vacía.
        System.out.println("\n7. Se comprueban los errores en cola vacía");
        boolean dequeueRechazado = false;
        try {
            cola.dequeue();
        } catch (IllegalStateException e) {
            dequeueRechazado = true;
            System.out.println("  dequeue -> " + e.getMessage());
        }
        verificar("dequeue fue rechazado con IllegalStateException", dequeueRechazado);

        boolean frontRechazado = false;
        try {
            cola.front();
        } catch (IllegalStateException e) {
            frontRechazado = true;
            System.out.println("  front   -> " + e.getMessage());
        }
        verificar("front fue rechazado con IllegalStateException", frontRechazado);

        // Caso 8: rechazo de capacidad inválida en el constructor.
        System.out.println("\n8. Se comprueba el rechazo de capacidades inválidas");
        verificar("capacidad 0 lanza IllegalArgumentException", construyeConError(0));
        verificar("capacidad negativa lanza IllegalArgumentException", construyeConError(-3));

        // Resumen de la demostración.
        System.out.println("\n=== RESULTADO ===");
        if (fallos == 0) {
            System.out.println("Todas las comprobaciones pasaron correctamente.");
        } else {
            System.out.println("Hubo " + fallos + " comprobación(es) fallida(s).");
            throw new AssertionError("La demostración de ColaCircular falló.");
        }
    }

    /**
     * Intenta construir una cola con la capacidad indicada y determina si se
     * rechazó la operación.
     *
     * @param capacidad capacidad inválida a probar
     * @return {@code true} si el constructor lanzó {@link IllegalArgumentException}
     */
    private static boolean construyeConError(int capacidad) {
        try {
            new ColaCircular(capacidad);
            return false;
        } catch (IllegalArgumentException e) {
            System.out.println("  (" + capacidad + ") -> " + e.getMessage());
            return true;
        }
    }
}

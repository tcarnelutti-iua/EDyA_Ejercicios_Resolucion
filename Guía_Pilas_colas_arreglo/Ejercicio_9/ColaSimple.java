/**
 * Cola simple <b>lineal</b> (FIFO) de enteros implementada sobre un arreglo de
 * capacidad fija.
 *
 * <p><b>¿Por qué es lineal y no circular?</b> Los dos índices de control solo
 * avanzan en un sentido:</p>
 * <ul>
 *   <li>{@code rear} avanza hacia el final del arreglo en cada
 *       {@link #enqueue(int)}; al llegar al último índice la cola se considera
 *       llena y <b>no da la vuelta</b> al inicio.</li>
 *   <li>{@code front} avanza en cada {@link #dequeue()} para señalar el nuevo
 *       primer elemento.</li>
 * </ul>
 *
 * <p>Como {@code enqueue} no regresa a la posición 0 ni desplaza (compacta) los
 * elementos, las posiciones que quedan libres <b>antes de {@code front}</b> no
 * pueden reutilizarse mientras la cola aún tenga elementos. Ese es el
 * <b>desperdicio de posiciones</b> característico de la cola lineal: puede
 * quedar espacio al inicio y, aun así, rechazarse una inserción porque
 * {@code rear} ya alcanzó el final del arreglo.</p>
 *
 * <p>Estado interno:</p>
 * <ul>
 *   <li>{@code front}: índice del primer elemento (el próximo en salir).</li>
 *   <li>{@code rear}: índice del último elemento insertado; {@code -1} si la
 *       cola está vacía.</li>
 *   <li>{@code datos}: arreglo de enteros. Las posiciones liberadas por
 *       {@code dequeue} se marcan con {@link #VACIO} para dejar visible que
 *       quedaron libres.</li>
 * </ul>
 *
 * <p>La cola está vacía cuando {@code rear < front}. Cuando se vacía por
 * completo se reinician {@code front = 0} y {@code rear = -1} para dejarla lista
 * para reutilizarse. La cola está llena cuando {@code rear == capacidad - 1},
 * es decir, cuando ya no hay espacio al final del arreglo.</p>
 *
 * @author Agente Desarrollador
 */
public class ColaSimple {

    /** Valor centinela que marca una posición liberada del arreglo. */
    private static final int VACIO = Integer.MIN_VALUE;

    /** Arreglo lineal que almacena los elementos de la cola. */
    private final int[] datos;

    /** Índice del primer elemento (próximo en salir). */
    private int front;

    /** Índice del último elemento insertado; {@code -1} si la cola está vacía. */
    private int rear;

    /**
     * Construye una cola lineal vacía con la capacidad indicada.
     *
     * @param capacidad número máximo de elementos que podrá almacenar (debe ser &gt; 0)
     * @throws IllegalArgumentException si {@code capacidad} es menor o igual a cero
     */
    public ColaSimple(int capacidad) {
        // Caso límite: una capacidad no positiva no tiene sentido.
        if (capacidad <= 0) {
            throw new IllegalArgumentException(
                    "La capacidad debe ser positiva. Se recibió: " + capacidad);
        }
        this.datos = new int[capacidad];
        // Estado canónico de cola vacía: front apunta al inicio y rear está antes.
        this.front = 0;
        this.rear = -1;
    }

    /**
     * Agrega un valor al final disponible de la cola.
     *
     * <p>Si {@code rear} ya alcanzó el último índice del arreglo, la cola se
     * considera llena y se rechaza la inserción, aunque existan huecos libres al
     * inicio (desperdicio propio de la cola lineal).</p>
     *
     * @param valor entero a encolar; no puede ser {@link #VACIO}
     * @throws IllegalArgumentException si el valor es el centinela reservado
     * @throws IllegalStateException    si ya no hay espacio al final del arreglo
     */
    public void enqueue(int valor) {
        // Se reserva el centinela para marcar posiciones vacías: no es un dato válido.
        if (valor == VACIO) {
            throw new IllegalArgumentException(
                    "El valor " + VACIO + " está reservado como marca de posición vacía.");
        }
        if (isFull()) {
            throw new IllegalStateException(
                    "No se puede encolar " + valor + ": no hay espacio al final del arreglo "
                            + "(la cola lineal está llena aunque pueda haber huecos al inicio).");
        }
        // Avance lineal: rear siempre se mueve una posición hacia el final.
        rear++;
        datos[rear] = valor;
    }

    /**
     * Elimina y devuelve el primer elemento de la cola.
     *
     * <p>La posición liberada se marca con {@link #VACIO} para dejar constancia
     * de que quedó libre, aunque en una cola lineal ese hueco no se reutilizará
     * mientras queden elementos.</p>
     *
     * @return el elemento que estaba al frente
     * @throws IllegalStateException si la cola está vacía
     */
    public int dequeue() {
        if (isEmpty()) {
            throw new IllegalStateException("No se puede desencolar: la cola está vacía.");
        }
        int valorFrente = datos[front];
        // Se marca la posición como libre (queda visible como hueco).
        datos[front] = VACIO;
        // Avance lineal del frente hacia el final.
        front++;
        // Si se sacó el último elemento, se reinicia la cola para reutilizarla.
        if (isEmpty()) {
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
            throw new IllegalStateException("No se puede consultar el frente: la cola está vacía.");
        }
        return datos[front];
    }

    /**
     * Indica si la cola no tiene elementos.
     *
     * @return {@code true} si no hay elementos
     */
    public boolean isEmpty() {
        // Con índices lineales, la cola está vacía cuando rear quedó antes que front.
        return rear < front;
    }

    /**
     * Indica si ya no hay espacio al final del arreglo.
     *
     * <p><b>Punto clave de la cola lineal:</b> estar "llena" aquí significa que
     * {@code rear} llegó al último índice, sin importar cuántas posiciones libres
     * queden antes de {@code front}.</p>
     *
     * @return {@code true} si {@code rear == capacidad - 1}
     */
    public boolean isFull() {
        return rear == datos.length - 1;
    }

    /**
     * Devuelve la cantidad de elementos actualmente almacenados.
     *
     * @return el número de elementos
     */
    public int size() {
        return isEmpty() ? 0 : rear - front + 1;
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
     * @return el índice del primer elemento (0 si la cola está vacía)
     */
    public int getFront() {
        return front;
    }

    /**
     * Devuelve el índice actual de {@code rear}.
     *
     * @return el índice del último elemento ({@code -1} si la cola está vacía)
     */
    public int getRear() {
        return rear;
    }

    /**
     * Indica si una posición del arreglo almacena actualmente un elemento.
     *
     * @param indice posición a consultar
     * @return {@code true} si la posición pertenece al rango ocupado
     */
    public boolean posicionOcupada(int indice) {
        return !isEmpty() && indice >= front && indice <= rear;
    }

    /**
     * Muestra el arreglo completo marcando las posiciones vacías, junto con los
     * índices {@code front} y {@code rear} y la cantidad de elementos.
     */
    public void mostrarEstado() {
        StringBuilder arreglo = new StringBuilder();
        StringBuilder indices = new StringBuilder();
        arreglo.append("Arreglo: [ ");
        for (int i = 0; i < datos.length; i++) {
            if (posicionOcupada(i)) {
                arreglo.append(datos[i]);
            } else {
                // Posición sin elemento: liberada por dequeue o nunca usada.
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
                + "   elementos = " + size() + "/" + capacidad()
                + (isFull() ? "   [LLENA]" : ""));
    }

    /**
     * Representación del contenido en orden FIFO, útil para depuración.
     *
     * @return los elementos desde el frente hasta el último agregado
     */
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder("[frente -> fin] ");
        for (int i = front; i <= rear; i++) {
            sb.append(datos[i]);
            if (i < rear) {
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
     * Demuestra el desperdicio de posiciones de una cola lineal:
     * <ol>
     *   <li>Capacidad 5; se agregan cinco valores hasta llegar al final.</li>
     *   <li>Se retiran dos elementos con {@code dequeue}.</li>
     *   <li>Las dos posiciones iniciales quedan libres, pero los elementos
     *       restantes siguen al final y {@code rear} no se mueve.</li>
     *   <li>Se intenta agregar otro valor y se rechaza: el hueco inicial no puede
     *       reutilizarse en una cola simple lineal.</li>
     * </ol>
     *
     * @param args no se usan
     */
    public static void main(String[] args) {
        System.out.println("=== DEMOSTRACIÓN DE ColaSimple (cola LINEAL con arreglo) ===\n");

        // Caso 1: cola recién creada.
        System.out.println("1. Cola recién creada (capacidad 5)");
        ColaSimple cola = new ColaSimple(5);
        cola.mostrarEstado();
        verificar("la cola empieza vacía", cola.isEmpty());
        verificar("no está llena", !cola.isFull());
        verificar("size() es 0", cola.size() == 0);

        // Caso 1 (cont.): llenar hasta el final del arreglo.
        System.out.println("\n2. Se agregan cinco valores: 10, 20, 30, 40, 50");
        cola.enqueue(10);
        cola.enqueue(20);
        cola.enqueue(30);
        cola.enqueue(40);
        cola.enqueue(50);
        cola.mostrarEstado();
        verificar("rear llegó al final (rear == capacidad - 1)", cola.getRear() == cola.capacidad() - 1);
        verificar("la cola quedó llena", cola.isFull());
        verificar("hay 5 elementos", cola.size() == 5);

        // Caso 2: retirar dos elementos.
        System.out.println("\n3. Se retiran dos elementos con dequeue");
        int primero = cola.dequeue();
        int segundo = cola.dequeue();
        System.out.println("  dequeue() devolvió: " + primero + " y luego " + segundo);
        cola.mostrarEstado();

        // Caso 3: las posiciones iniciales quedan libres, rear sigue al final.
        System.out.println("\n4. Análisis: huecos al inicio vs. elementos al final");
        verificar("dequeue devolvió primero 10 (FIFO)", primero == 10);
        verificar("dequeue devolvió luego 20 (FIFO)", segundo == 20);
        verificar("la posición 0 quedó libre", !cola.posicionOcupada(0));
        verificar("la posición 1 quedó libre", !cola.posicionOcupada(1));
        verificar("los 30, 40, 50 siguen en las posiciones 2, 3 y 4",
                cola.posicionOcupada(2) && cola.posicionOcupada(3) && cola.posicionOcupada(4));
        verificar("front avanzó a 2", cola.getFront() == 2);
        verificar("rear sigue en 4 (no se movió al retirar)", cola.getRear() == 4);
        verificar("quedan 3 elementos", cola.size() == 3);
        verificar("aun con 3 elementos, isFull() sigue siendo true", cola.isFull());

        // Caso 4: intentar agregar otro valor; se rechaza.
        System.out.println("\n5. Se intenta agregar el valor 60 (hay 2 huecos al inicio)");
        boolean rechazado = false;
        String antes = cola.toString();
        try {
            cola.enqueue(60);
        } catch (IllegalStateException e) {
            rechazado = true;
            System.out.println("  Rechazado -> " + e.getMessage());
        }
        verificar("enqueue fue rechazado con IllegalStateException", rechazado);
        verificar("la cola no cambió al rechazar el 60", antes.equals(cola.toString()));
        cola.mostrarEstado();
        System.out.println("  Explicación: las posiciones 0 y 1 están libres, pero rear ya está en");
        System.out.println("  el índice 4. En una cola simple lineal enqueue NO vuelve al inicio ni");
        System.out.println("  compacta el arreglo, por lo que ese espacio inicial se DESPERDICIA.");

        // Caso 5 (opcional): vaciar la cola y comprobar el reinicio.
        System.out.println("\n6. Caso adicional: al vaciarse por completo, la cola se reinicia");
        System.out.println("  (no forma parte del caso principal, que aún conserva elementos)");
        cola.dequeue(); // 30
        cola.dequeue(); // 40
        cola.dequeue(); // 50 -> queda vacía
        System.out.println("  Tras retirar 30, 40 y 50:");
        cola.mostrarEstado();
        verificar("la cola quedó vacía", cola.isEmpty());
        verificar("front se reinició a 0", cola.getFront() == 0);
        verificar("rear se reinició a -1", cola.getRear() == -1);
        cola.enqueue(70);
        System.out.println("  Tras encolar 70 en la cola reiniciada:");
        cola.mostrarEstado();
        verificar("el 70 se almacenó en la posición 0", cola.posicionOcupada(0));

        // Resumen de la demostración.
        System.out.println("\n=== RESULTADO ===");
        if (fallos == 0) {
            System.out.println("Todas las comprobaciones pasaron correctamente.");
        } else {
            System.out.println("Hubo " + fallos + " comprobación(es) fallida(s).");
            throw new AssertionError("La demostración de ColaSimple falló.");
        }
    }
}

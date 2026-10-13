import java.util.Scanner;

/**
 * Simulador de una torre de platos implementada como una pila sobre un arreglo
 * de tamaño fijo.
 *
 * <p><b>¿Por qué una pila y no una cola?</b> En una torre de platos solo se
 * puede colocar o retirar el plato que está arriba. Por eso el <i>último</i>
 * plato agregado es el <i>primero</i> en retirarse: este comportamiento se llama
 * LIFO (<i>Last In, First Out</i>) y es exactamente el de una pila. Una cola, en
 * cambio, es FIFO (<i>First In, First Out</i>) y retiraría primero el plato más
 * antiguo, lo que no representa el funcionamiento real de una torre de platos.</p>
 *
 * <p>Los platos se almacenan únicamente en un arreglo de {@code String}. No se
 * usan {@code Stack}, {@code Queue} ni ninguna colección, y la capacidad es fija:
 * cuando la torre se llena se rechaza el alta en lugar de redimensionar.</p>
 *
 * <p>El índice {@code cima} apunta al plato superior y vale {@code -1} cuando la
 * torre está vacía. El identificador de cada plato se maneja como texto para
 * admitir nombres con espacios ("Plato hondo 1") o números ("3").</p>
 *
 * @author Agente Desarrollador
 */
public class TorrePlatosSimulador {

    /**
     * Capacidad máxima de la torre. Es una constante fácil de modificar: cambia
     * este valor para admitir más o menos platos. No se redimensiona en ejecución.
     */
    private static final int CAPACIDAD_MAXIMA = 5;

    /** Arreglo que almacena los identificadores de los platos. */
    private final String[] platos;

    /** Índice del plato superior; {@code -1} representa una torre vacía. */
    private int cima;

    /**
     * Crea una torre de platos vacía con capacidad {@link #CAPACIDAD_MAXIMA}.
     */
    public TorrePlatosSimulador() {
        this.platos = new String[CAPACIDAD_MAXIMA];
        this.cima = -1; // Torre vacía.
    }

    /**
     * Punto de entrada del simulador.
     *
     * @param args argumentos de línea de comandos (no se usan)
     */
    public static void main(String[] args) {
        TorrePlatosSimulador simulador = new TorrePlatosSimulador();
        simulador.ejecutarMenu();
    }

    /**
     * Ejecuta el menú interactivo por consola hasta que el usuario decide salir.
     */
    private void ejecutarMenu() {
        Scanner scanner = new Scanner(System.in);
        boolean salir = false;

        mostrarBienvenida();
        while (!salir) {
            mostrarMenu();
            int opcion = leerOpcion(scanner);

            switch (opcion) {
                case 1 -> agregarPlato(scanner);
                case 2 -> retirarPlato();
                case 3 -> consultarPlatoSuperior();
                case 4 -> {
                    salir = true;
                    System.out.println("Fin de la simulación.");
                }
                // Cualquier entrada no numérica o fuera de rango cae aquí.
                default -> System.out.println("Opción inválida. Intenta de nuevo.");
            }
        }
        scanner.close();
    }

    /**
     * Muestra el encabezado inicial del programa.
     */
    private void mostrarBienvenida() {
        System.out.println("=== Simulador de torre de platos (pila LIFO) ===");
        System.out.println("Capacidad máxima de la torre: " + CAPACIDAD_MAXIMA + " platos.");
    }

    /**
     * Muestra las opciones disponibles y el estado actual de la torre.
     */
    private void mostrarMenu() {
        System.out.println();
        System.out.println("Estado actual: " + tamano() + "/" + CAPACIDAD_MAXIMA
                + (estaVacia() ? " (torre vacía)" : estaLlena() ? " (torre llena)" : ""));
        System.out.println("1) Agregar un plato");
        System.out.println("2) Retirar el plato superior");
        System.out.println("3) Consultar el plato superior");
        System.out.println("4) Salir");
    }

    /**
     * Lee y valida la opción elegida por el usuario.
     *
     * @param scanner fuente de entrada (System.in)
     * @return la opción como entero, o {@code -1} si la entrada no es un número
     */
    private int leerOpcion(Scanner scanner) {
        System.out.print("Elige una opción: ");
        String entrada = scanner.nextLine().trim();
        try {
            return Integer.parseInt(entrada);
        } catch (NumberFormatException e) {
            // Entrada no numérica: se devuelve un valor imposible para el menú.
            return -1;
        }
    }

    /**
     * Solicita un identificador y agrega un plato en la cima de la torre.
     *
     * @param scanner fuente de entrada para leer el identificador
     */
    private void agregarPlato(Scanner scanner) {
        System.out.print("Identificador del plato (nombre o número): ");
        String identificador = scanner.nextLine().trim();

        // Caso límite: no se admiten identificadores vacíos.
        if (identificador.isEmpty()) {
            System.out.println("El identificador no puede estar vacío.");
            return;
        }

        // Caso límite: torre llena; se rechaza el alta sin redimensionar.
        if (estaLlena()) {
            System.out.println("No se puede agregar: la torre está llena (capacidad = "
                    + CAPACIDAD_MAXIMA + ").");
            return;
        }

        // Hay espacio: se sube la cima y se guarda el plato.
        cima++;
        platos[cima] = identificador;
        System.out.println("Plato agregado en la cima: " + identificador);
    }

    /**
     * Retira y muestra el plato superior, si existe.
     */
    private void retirarPlato() {
        // Caso límite: torre vacía; se informa sin modificar el estado.
        if (estaVacia()) {
            System.out.println("No se puede retirar: la torre está vacía.");
            return;
        }

        String retirado = platos[cima];
        platos[cima] = null; // Se libera la referencia del plato retirado.
        cima--;              // La cima baja un nivel.
        System.out.println("Plato retirado: " + retirado);
    }

    /**
     * Muestra el plato superior sin retirarlo, si existe.
     */
    private void consultarPlatoSuperior() {
        // Caso límite: torre vacía; se informa sin modificar el estado.
        if (estaVacia()) {
            System.out.println("No hay plato superior: la torre está vacía.");
            return;
        }
        System.out.println("Plato superior: " + platos[cima]);
    }

    /**
     * Indica si la torre no tiene platos.
     *
     * @return {@code true} si la cima vale {@code -1}
     */
    private boolean estaVacia() {
        return cima == -1;
    }

    /**
     * Indica si la torre alcanzó su capacidad máxima.
     *
     * @return {@code true} si la cima llegó al último índice del arreglo
     */
    private boolean estaLlena() {
        return cima == platos.length - 1;
    }

    /**
     * Devuelve la cantidad actual de platos en la torre.
     *
     * @return el número de platos, calculado como {@code cima + 1}
     */
    private int tamano() {
        return cima + 1;
    }
}

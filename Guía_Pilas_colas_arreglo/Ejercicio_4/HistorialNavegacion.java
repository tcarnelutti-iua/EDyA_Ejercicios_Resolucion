import java.util.Scanner;

/**
 * Simulador simple de un historial de navegación usando una pila (LIFO).
 *
 * <p><b>Representación del historial.</b> El historial se modela con una pila de
 * {@code String} implementada sobre un arreglo de tamaño fijo. La URL que está en
 * la cima es siempre la página actual. No se usa {@code Stack} ni ninguna
 * colección: solo un arreglo de {@code String} y un índice de cima.</p>
 *
 * <p><b>¿Por qué una pila sigue LIFO?</b> LIFO significa <i>Last In, First Out</i>
 * (el último en entrar es el primero en salir). Cada vez que se visita una página
 * se apila su URL, por lo que la visita <i>más reciente</i> queda en la cima.
 * Al volver atrás, esa visita reciente debe ser la primera en quitarse
 * ({@code pop}) para dejar visible la visita inmediatamente anterior. Ese orden
 * de "lo último que hice es lo primero que deshago" es exactamente el
 * comportamiento de una pila; una cola (FIFO) sacaría primero la visita más
 * antigua, lo que no representa "volver a la página anterior".</p>
 *
 * <p>El programa permite visitar URLs, volver a la página anterior, consultar la
 * página actual y salir. No implementa navegación hacia adelante.</p>
 *
 * @author Agente Desarrollador
 */
public class HistorialNavegacion {

    /**
     * Capacidad máxima del historial (número de URLs que puede recordar).
     * Es una constante fácil de modificar; al superarla se informa que el
     * historial está lleno.
     */
    private static final int CAPACIDAD_MAXIMA = 10;

    /** Opción del menú para visitar una página. */
    private static final int OPCION_VISITAR = 1;

    /** Opción del menú para volver a la página anterior. */
    private static final int OPCION_VOLVER = 2;

    /** Opción del menú para consultar la página actual. */
    private static final int OPCION_ACTUAL = 3;

    /** Opción del menú para salir del programa. */
    private static final int OPCION_SALIR = 4;

    /**
     * Punto de entrada del programa: muestra el menú y procesa las acciones del
     * usuario hasta que decide salir.
     *
     * @param args argumentos de línea de comandos (no se usan)
     */
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        PilaUrls historial = new PilaUrls(CAPACIDAD_MAXIMA);

        mostrarEncabezado();

        boolean ejecutando = true;
        while (ejecutando) {
            mostrarMenu();
            int opcion = leerOpcion(scanner);

            switch (opcion) {
                case OPCION_VISITAR:
                    visitarPagina(scanner, historial);
                    break;
                case OPCION_VOLVER:
                    volverAtras(historial);
                    break;
                case OPCION_ACTUAL:
                    consultarPaginaActual(historial);
                    break;
                case OPCION_SALIR:
                    ejecutando = false;
                    System.out.println("Saliendo del simulador. ¡Hasta luego!");
                    break;
                default:
                    // Entrada fuera de rango: se informa sin terminar el programa.
                    System.out.println("Opción inválida. Ingresa un número entre "
                            + OPCION_VISITAR + " y " + OPCION_SALIR + ".");
                    break;
            }
            System.out.println();
        }

        scanner.close();
    }

    /** Muestra el encabezado del programa. */
    private static void mostrarEncabezado() {
        System.out.println("=== Simulador de historial de navegación (pila LIFO) ===");
        System.out.println("Capacidad máxima del historial: " + CAPACIDAD_MAXIMA + " páginas.");
        System.out.println();
    }

    /** Muestra las opciones disponibles del menú. */
    private static void mostrarMenu() {
        System.out.println("Menú:");
        System.out.println("  " + OPCION_VISITAR + ") Visitar una página");
        System.out.println("  " + OPCION_VOLVER + ") Volver a la página anterior");
        System.out.println("  " + OPCION_ACTUAL + ") Consultar página actual");
        System.out.println("  " + OPCION_SALIR + ") Salir");
        System.out.print("Elige una opción: ");
    }

    /**
     * Lee una opción del menú validando la entrada.
     *
     * <p>Si el usuario ingresa algo que no es un número entero, se descarta la
     * línea y se devuelve {@code -1}, valor que el menú trata como opción
     * inválida sin lanzar excepciones.</p>
     *
     * @param scanner lector de consola
     * @return el número ingresado, o {@code -1} si la entrada no es un entero válido
     */
    private static int leerOpcion(Scanner scanner) {
        String linea = scanner.nextLine();
        try {
            return Integer.parseInt(linea.trim());
        } catch (NumberFormatException e) {
            // Entrada no numérica: se informa como opción inválida.
            return -1;
        }
    }

    /**
     * Procesa la visita de una página: valida la URL, comprueba que haya espacio
     * y la apila como nueva página actual.
     *
     * @param scanner   lector de consola
     * @param historial pila que representa el historial
     */
    private static void visitarPagina(Scanner scanner, PilaUrls historial) {
        System.out.print("Ingresa la URL a visitar: ");
        String url = scanner.nextLine();

        // Se rechazan URLs nulas, vacías o formadas solo por espacios.
        if (url == null || url.trim().isEmpty()) {
            System.out.println("URL inválida: no puede estar vacía ni contener solo espacios.");
            return;
        }

        // Si el arreglo ya está lleno no se puede apilar otra página.
        if (historial.isFull()) {
            System.out.println("No se puede visitar la página: el historial está lleno "
                    + "(capacidad = " + CAPACIDAD_MAXIMA + ").");
            return;
        }

        String urlLimpia = url.trim();
        historial.push(urlLimpia);
        System.out.println("Página visitada: " + urlLimpia);
        System.out.println("Página actual: " + historial.peek());
    }

    /**
     * Vuelve a la página anterior desapilando la página actual.
     *
     * <p>Casos contemplados:</p>
     * <ul>
     *   <li>Historial vacío: informa que todavía no se visitó ninguna página.</li>
     *   <li>Solo hay una página: no existe una anterior, se informa y se
     *       conserva el estado.</li>
     *   <li>Hay dos o más: se hace {@code pop} de la actual y la nueva cima pasa
     *       a ser la página actual.</li>
     * </ul>
     *
     * @param historial pila que representa el historial
     */
    private static void volverAtras(PilaUrls historial) {
        if (historial.isEmpty()) {
            System.out.println("Todavía no se visitó ninguna página.");
            return;
        }

        // Con una sola página en la pila no hay página anterior a la cual volver.
        if (historial.size() == 1) {
            System.out.println("No es posible volver: no hay una página anterior.");
            return;
        }

        historial.pop(); // Sale la página actual (LIFO).
        System.out.println("Volviste a: " + historial.peek());
    }

    /**
     * Consulta la página actual sin modificar la pila.
     *
     * @param historial pila que representa el historial
     */
    private static void consultarPaginaActual(PilaUrls historial) {
        if (historial.isEmpty()) {
            System.out.println("Todavía no se visitó ninguna página.");
            return;
        }
        System.out.println("Página actual: " + historial.peek());
    }

    /**
     * Pila de {@code String} de tamaño fijo implementada sobre un arreglo.
     *
     * <p>El índice {@code cima} señala el elemento superior y se inicializa en
     * {@code -1}, valor que representa una pila vacía:</p>
     * <ul>
     *   <li>{@code isEmpty} &rarr; {@code cima == -1}</li>
     *   <li>{@code isFull} &rarr; {@code cima == datos.length - 1}</li>
     *   <li>{@code size} &rarr; {@code cima + 1}</li>
     * </ul>
     *
     * <p>Solo se almacena el arreglo interno y el índice de cima; no se usan
     * colecciones ni otras estructuras.</p>
     */
    private static class PilaUrls {

        /** Arreglo que almacena las URLs de la pila. */
        private final String[] datos;

        /** Índice de la cima; {@code -1} indica pila vacía. */
        private int cima;

        /**
         * Construye una pila vacía con la capacidad indicada.
         *
         * @param capacidad número máximo de elementos (debe ser &gt; 0)
         * @throws IllegalArgumentException si {@code capacidad} es menor o igual a cero
         */
        PilaUrls(int capacidad) {
            // Caso límite: una capacidad no positiva no tiene sentido.
            if (capacidad <= 0) {
                throw new IllegalArgumentException(
                        "La capacidad debe ser mayor que cero. Valor recibido: " + capacidad);
            }
            this.datos = new String[capacidad];
            this.cima = -1; // Pila vacía.
        }

        /**
         * Apila {@code url} en la cima: pasa a ser la página actual.
         *
         * @param url URL a apilar
         * @throws IllegalStateException si la pila está llena (overflow)
         */
        void push(String url) {
            if (isFull()) {
                throw new IllegalStateException(
                        "No se puede apilar: la pila está llena (capacidad = " + datos.length + ").");
            }
            cima++;
            datos[cima] = url;
        }

        /**
         * Desapila y devuelve la URL de la cima (la página actual).
         *
         * @return la URL que estaba en la cima
         * @throws IllegalStateException si la pila está vacía (underflow)
         */
        String pop() {
            if (isEmpty()) {
                throw new IllegalStateException("No se puede desapilar: la pila está vacía.");
            }
            String urlCima = datos[cima];
            datos[cima] = null; // Se libera la referencia; queda lógicamente fuera de la pila.
            cima--;
            return urlCima;
        }

        /**
         * Devuelve la URL de la cima sin eliminarla.
         *
         * @return la URL de la cima (página actual)
         * @throws IllegalStateException si la pila está vacía
         */
        String peek() {
            if (isEmpty()) {
                throw new IllegalStateException("No se puede consultar la cima: la pila está vacía.");
            }
            return datos[cima];
        }

        /**
         * Indica si la pila está vacía.
         *
         * @return {@code true} si no hay elementos ({@code cima == -1})
         */
        boolean isEmpty() {
            return cima == -1;
        }

        /**
         * Indica si la pila alcanzó su capacidad.
         *
         * @return {@code true} si la cima llegó al último índice del arreglo
         */
        boolean isFull() {
            return cima == datos.length - 1;
        }

        /**
         * Devuelve la cantidad de elementos almacenados.
         *
         * @return el número de elementos, calculado como {@code cima + 1}
         */
        int size() {
            return cima + 1;
        }
    }
}

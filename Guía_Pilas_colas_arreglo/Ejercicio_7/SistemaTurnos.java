import java.util.Scanner;

/**
 * Sistema de turnos para la atención de una oficina.
 *
 * <p>Se modela como una <b>cola FIFO</b> (First-In, First-Out): la primera
 * persona que llega es la primera en ser atendida. No se usa una pila LIFO
 * porque una pila atendería primero al último que llegó, invirtiendo el orden
 * de llegada y resultando injusta para un turnero.</p>
 *
 * <p>La cola se implementa sobre un <b>arreglo de capacidad fija</b> con
 * comportamiento <b>circular</b>. No se utiliza ninguna colección
 * ({@code Queue}, {@code LinkedList}, etc.) para almacenar los turnos: solo el
 * arreglo interno y tres variables de control:</p>
 * <ul>
 *   <li>{@code frente}: índice del primer turno, el próximo en salir.</li>
 *   <li>{@code fin}: índice del último turno insertado; {@code -1} si está vacía.</li>
 *   <li>{@code cantidad}: número de elementos. Permite distinguir sin ambigüedad
 *       el estado vacío ({@code cantidad == 0}) del lleno
 *       ({@code cantidad == capacidad}). Sin este contador, {@code frente == fin}
 *       sería ambiguo (podría significar vacía o con un solo elemento).</li>
 * </ul>
 *
 * <p><b>Avance circular de los índices.</b> Se usa aritmética modular para que
 * los índices "den la vuelta" y reutilicen las posiciones liberadas al frente:</p>
 * <ul>
 *   <li>{@code registrar} (encolar): {@code fin = (fin + 1) % capacidad}, luego
 *       se guarda el turno y {@code cantidad++}.</li>
 *   <li>{@code atender} (desencolar): se lee {@code turnos[frente]}, luego
 *       {@code frente = (frente + 1) % capacidad} y {@code cantidad--}.</li>
 *   <li>Al quedar vacía se restablece el estado canónico
 *       {@code frente = 0}, {@code fin = -1}.</li>
 * </ul>
 *
 * <p>El número de turno es consecutivo y <b>solo</b> se asigna y avanza cuando
 * la persona logra ingresar a la cola; si está llena, se informa el problema y
 * el contador no se incrementa.</p>
 *
 * @author Agente Desarrollador
 */
public class SistemaTurnos {

    /** Capacidad por defecto usada por el menú interactivo. */
    private static final int CAPACIDAD_POR_DEFECTO = 5;

    /** Arreglo circular que almacena los turnos en orden FIFO. */
    private final Turno[] turnos;

    /** Índice del primer turno (el próximo en ser atendido). */
    private int frente;

    /** Índice del último turno insertado; {@code -1} cuando está vacía. */
    private int fin;

    /** Cantidad de turnos actualmente en la cola. */
    private int cantidad;

    /** Próximo número de turno por asignar (consecutivo). */
    private int siguienteNumero;

    /**
     * Representa un turno con su número consecutivo y el nombre de la persona.
     */
    public static final class Turno {

        private final int numero;
        private final String nombre;

        /**
         * Crea un turno.
         *
         * @param numero número de turno asignado
         * @param nombre nombre de la persona (no vacío)
         */
        public Turno(int numero, String nombre) {
            this.numero = numero;
            this.nombre = nombre;
        }

        public int getNumero() {
            return numero;
        }

        public String getNombre() {
            return nombre;
        }

        @Override
        public String toString() {
            return "Turno #" + numero + " - " + nombre;
        }
    }

    /**
     * Construye el sistema con la capacidad indicada.
     *
     * @param capacidad número máximo de personas que pueden esperar (&gt; 0)
     * @throws IllegalArgumentException si {@code capacidad} es menor o igual a cero
     */
    public SistemaTurnos(int capacidad) {
        if (capacidad <= 0) {
            throw new IllegalArgumentException(
                    "La capacidad debe ser positiva. Se recibió: " + capacidad);
        }
        this.turnos = new Turno[capacidad];
        this.frente = 0;
        this.fin = -1;
        this.cantidad = 0;
        this.siguienteNumero = 1;
    }

    /**
     * Registra a una persona en la cola y le asigna un número de turno.
     *
     * <p>El número solo se asigna y avanza si la persona logra ingresar. Si la
     * cola está llena, devuelve {@code null} sin modificar el contador.</p>
     *
     * @param nombre nombre de la persona; no puede ser nulo ni vacío
     * @return el {@link Turno} creado, o {@code null} si la cola está llena
     * @throws IllegalArgumentException si el nombre es nulo o está en blanco
     */
    public Turno registrar(String nombre) {
        // Validación de caso límite: no se aceptan nombres vacíos.
        if (nombre == null || nombre.trim().isEmpty()) {
            throw new IllegalArgumentException("El nombre no puede estar vacío.");
        }
        // Si está llena, se informa con null y NO se incrementa el contador.
        if (isFull()) {
            return null;
        }
        String nombreLimpio = nombre.trim();
        Turno nuevoTurno = new Turno(siguienteNumero, nombreLimpio);
        // Avance circular del final: al llegar al último índice vuelve a 0.
        fin = (fin + 1) % capacidad();
        turnos[fin] = nuevoTurno;
        cantidad++;
        siguienteNumero++; // El contador avanza solo tras una inserción exitosa.
        return nuevoTurno;
    }

    /**
     * Atiende (elimina y devuelve) al primer turno de la cola.
     *
     * @return el turno atendido
     * @throws IllegalStateException si la cola está vacía
     */
    public Turno atender() {
        if (isEmpty()) {
            throw new IllegalStateException("No hay personas para atender.");
        }
        Turno atendido = turnos[frente];
        turnos[frente] = null; // Se libera la referencia de la posición.
        // Avance circular del frente; la posición liberada podrá reutilizarse.
        frente = (frente + 1) % capacidad();
        cantidad--;
        // Si quedó vacía, se restablece el estado canónico.
        if (cantidad == 0) {
            frente = 0;
            fin = -1;
        }
        return atendido;
    }

    /**
     * Devuelve el primer turno <b>sin</b> eliminarlo de la cola.
     *
     * @return el turno que está al frente
     * @throws IllegalStateException si la cola está vacía
     */
    public Turno primero() {
        if (isEmpty()) {
            throw new IllegalStateException("No hay personas para consultar.");
        }
        return turnos[frente];
    }

    /**
     * Indica si la cola no tiene turnos.
     *
     * @return {@code true} si {@code cantidad == 0}
     */
    public boolean isEmpty() {
        return cantidad == 0;
    }

    /**
     * Indica si la cola alcanzó su capacidad máxima.
     *
     * @return {@code true} si {@code cantidad == capacidad}
     */
    public boolean isFull() {
        return cantidad == capacidad();
    }

    /**
     * Devuelve la cantidad de turnos en espera.
     *
     * @return el número de elementos actuales
     */
    public int tamano() {
        return cantidad;
    }

    /**
     * Devuelve la capacidad máxima del arreglo interno.
     *
     * @return la capacidad de la cola
     */
    public int capacidad() {
        return turnos.length;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder("[frente -> fin] ");
        for (int i = 0; i < cantidad; i++) {
            // Se recorre desde frente respetando el avance circular.
            int indice = (frente + i) % capacidad();
            sb.append(turnos[indice]);
            if (i < cantidad - 1) {
                sb.append(" | ");
            }
        }
        return sb.toString();
    }

    // ------------------------------------------------------------------
    // Menú interactivo por consola
    // ------------------------------------------------------------------

    /**
     * Ejecuta el menú interactivo. Si se recibe el argumento {@code --demo} se
     * ejecuta en su lugar la demostración automática (útil para verificar sin
     * entrada manual).
     *
     * @param args {@code --demo} para correr la demostración; vacío para el menú
     */
    public static void main(String[] args) {
        if (args.length > 0 && "--demo".equalsIgnoreCase(args[0])) {
            ejecutarDemostracion();
            return;
        }
        ejecutarMenu();
    }

    /**
     * Muestra el menú y procesa las opciones hasta que el usuario sale. Las
     * entradas inválidas se informan sin terminar el programa.
     */
    private static void ejecutarMenu() {
        SistemaTurnos sistema = new SistemaTurnos(CAPACIDAD_POR_DEFECTO);
        try (Scanner scanner = new Scanner(System.in)) {
            boolean salir = false;
            while (!salir) {
                mostrarMenu(sistema);

                int opcion = leerOpcion(scanner);
                switch (opcion) {
                    case 1:
                        registrarPersona(sistema, scanner);
                        break;
                    case 2:
                        atenderSiguiente(sistema);
                        break;
                    case 3:
                        consultarPrimero(sistema);
                        break;
                    case 4:
                        salir = true;
                        System.out.println("Saliendo del sistema de turnos. ¡Hasta luego!");
                        break;
                    default:
                        System.out.println("Opción inválida. Ingrese un número entre 1 y 4.");
                }
            }
        }
    }

    /**
     * Imprime el menú y el estado actual de la cola.
     *
     * @param sistema sistema de turnos a mostrar
     */
    private static void mostrarMenu(SistemaTurnos sistema) {
        System.out.println();
        System.out.println("========= SISTEMA DE TURNOS =========");
        System.out.println("1. Registrar persona");
        System.out.println("2. Atender siguiente");
        System.out.println("3. Consultar quién está primero");
        System.out.println("4. Salir");
        System.out.println("Personas en espera: " + sistema.tamano()
                + " / " + sistema.capacidad());
        System.out.print("Seleccione una opción: ");
    }

    /**
     * Lee una opción del menú de forma segura. Acepta cualquier entrada y ante
     * un valor no numérico devuelve {@code -1} (opción inválida).
     *
     * @param scanner lector de consola
     * @return el número ingresado, o {@code -1} si no es un entero válido
     */
    private static int leerOpcion(Scanner scanner) {
        String linea = scanner.nextLine().trim();
        try {
            return Integer.parseInt(linea);
        } catch (NumberFormatException e) {
            return -1; // Se maneja como opción inválida sin abortar el programa.
        }
    }

    /**
     * Solicita un nombre y registra a la persona, manejando nombre vacío y cola
     * llena.
     *
     * @param sistema sistema de turnos
     * @param scanner lector de consola
     */
    private static void registrarPersona(SistemaTurnos sistema, Scanner scanner) {
        System.out.print("Ingrese el nombre de la persona: ");
        String nombre = scanner.nextLine();
        try {
            Turno turno = sistema.registrar(nombre);
            if (turno == null) {
                System.out.println("La cola está llena. No se pudo registrar ni "
                        + "asignar un nuevo turno.");
            } else {
                System.out.println("Registrado -> " + turno
                        + ". Personas en espera: " + sistema.tamano());
            }
        } catch (IllegalArgumentException e) {
            System.out.println("Nombre inválido: " + e.getMessage());
        }
    }

    /**
     * Atiende al siguiente turno o informa si no hay personas.
     *
     * @param sistema sistema de turnos
     */
    private static void atenderSiguiente(SistemaTurnos sistema) {
        try {
            Turno atendido = sistema.atender();
            System.out.println("Atendiendo -> " + atendido);
        } catch (IllegalStateException e) {
            System.out.println(e.getMessage());
        }
    }

    /**
     * Consulta el primer turno sin retirarlo, o informa si no hay personas.
     *
     * @param sistema sistema de turnos
     */
    private static void consultarPrimero(SistemaTurnos sistema) {
        try {
            Turno primero = sistema.primero();
            System.out.println("Primero en la fila -> " + primero
                    + " (no se retira de la cola).");
        } catch (IllegalStateException e) {
            System.out.println(e.getMessage());
        }
    }

    // ------------------------------------------------------------------
    // Demostración automática (verificación)
    // ------------------------------------------------------------------

    /** Contador de comprobaciones fallidas de la demostración. */
    private static int fallos = 0;

    /**
     * Comprueba una condición e imprime el resultado.
     *
     * @param descripcion qué se comprueba
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
     * Ejecuta una demostración representativa: registro de dos personas,
     * consulta del frente sin retirar, atención en orden de llegada, cola
     * vacía, capacidad llena, reutilización circular y rechazo de nombre vacío.
     */
    private static void ejecutarDemostracion() {
        System.out.println("=== DEMOSTRACIÓN DE SistemaTurnos (cola circular FIFO) ===\n");

        SistemaTurnos sistema = new SistemaTurnos(3);

        System.out.println("1. Estado inicial");
        verificar("la cola empieza vacía", sistema.isEmpty());
        verificar("tamano() es 0", sistema.tamano() == 0);

        System.out.println("\n2. Registro de dos personas (Ana y Luis)");
        Turno t1 = sistema.registrar("Ana");
        Turno t2 = sistema.registrar("Luis");
        verificar("Ana recibe el turno #1", t1 != null && t1.getNumero() == 1);
        verificar("Luis recibe el turno #2", t2 != null && t2.getNumero() == 2);
        verificar("hay 2 personas en espera", sistema.tamano() == 2);

        System.out.println("\n3. Consultar quién está primero (no debe retirar)");
        verificar("el primero es Ana (turno #1)",
                sistema.primero() != null && sistema.primero().getNumero() == 1);
        verificar("la consulta NO retira (sigue habiendo 2)",
                sistema.tamano() == 2);

        System.out.println("\n4. Atender a ambos en orden de llegada (FIFO)");
        Turno a1 = sistema.atender();
        verificar("se atiende primero a Ana (#1)",
                a1 != null && a1.getNumero() == 1 && "Ana".equals(a1.getNombre()));
        Turno a2 = sistema.atender();
        verificar("se atiende luego a Luis (#2)",
                a2 != null && a2.getNumero() == 2 && "Luis".equals(a2.getNombre()));
        verificar("la cola quedó vacía", sistema.isEmpty());

        System.out.println("\n5. Cola vacía: consultar y atender no deben fallar");
        boolean consultaManejo = false;
        try {
            sistema.primero();
        } catch (IllegalStateException e) {
            consultaManejo = true;
        }
        verificar("primero() en cola vacía informa el problema", consultaManejo);

        boolean atencionManejo = false;
        try {
            sistema.atender();
        } catch (IllegalStateException e) {
            atencionManejo = true;
        }
        verificar("atender() en cola vacía informa el problema", atencionManejo);

        System.out.println("\n6. Cola llena: no se asigna turno ni avanza el contador");
        SistemaTurnos lleno = new SistemaTurnos(2);
        lleno.registrar("Ana");  // turno 1
        lleno.registrar("Luis"); // turno 2
        verificar("la cola está llena", lleno.isFull());
        Turno rechazado = lleno.registrar("Carlos");
        verificar("registrar en cola llena devuelve null (no hay turno)", rechazado == null);
        verificar("el contador de turnos no avanzó (sigue esperando el #3)",
                lleno.atender() != null && lleno.atender() != null);
        // Tras liberar espacio, el siguiente turno vuelve a ser el #3.
        Turno t3 = lleno.registrar("Carlos");
        verificar("el siguiente turno tras liberar es el #3",
                t3 != null && t3.getNumero() == 3);
        Turno siguiente = lleno.atender();
        verificar("se atiende a Carlos (#3)", siguiente != null && siguiente.getNumero() == 3);

        System.out.println("\n7. Reutilización circular de posiciones liberadas");
        SistemaTurnos circular = new SistemaTurnos(3);
        circular.registrar("A"); // #1
        circular.registrar("B"); // #2
        circular.atender();      // sale A -> frente avanza
        circular.atender();      // sale B -> frente avanza
        circular.registrar("C"); // #3 reutiliza posición circular
        circular.registrar("D"); // #4
        circular.registrar("E"); // #5
        verificar("se pudieron reutilizar las posiciones (3 en espera)",
                circular.tamano() == 3);
        verificar("el frente sigue siendo C (#3)",
                circular.primero() != null && circular.primero().getNumero() == 3);
        verificar("la cola está llena con 3 elementos", circular.isFull());

        System.out.println("\n8. Rechazo de nombre vacío");
        boolean nombreRechazado = false;
        try {
            sistema.registrar("   ");
        } catch (IllegalArgumentException e) {
            nombreRechazado = true;
        }
        verificar("registrar con nombre vacío lanza IllegalArgumentException", nombreRechazado);

        System.out.println("\n=== RESULTADO ===");
        if (fallos == 0) {
            System.out.println("Todas las comprobaciones pasaron correctamente.");
        } else {
            System.out.println("Hubo " + fallos + " comprobación(es) fallida(s).");
            throw new AssertionError("La demostración de SistemaTurnos falló.");
        }
    }
}

import java.util.Scanner;

/**
 * Sistema sencillo de cola de impresión basado en una <b>cola FIFO</b>
 * (First-In, First-Out).
 *
 * <p>Una impresora atiende un trabajo por vez y debe respetar el orden de
 * llegada: el documento enviado primero se imprime primero. Esa semántica es
 * exactamente la de una cola. Una <b>pila</b> (LIFO) sería inadecuada porque
 * retira por el mismo extremo donde inserta e imprimiría primero el documento
 * agregado más recientemente, invirtiendo el orden.</p>
 *
 * <p>La cola se implementa sobre un <b>arreglo de capacidad fija</b> con
 * comportamiento <b>circular</b>. No se usa ninguna colección ({@code Queue},
 * {@code LinkedList}, etc.) para almacenar los trabajos: solo el arreglo
 * interno y tres variables de control:</p>
 * <ul>
 *   <li>{@code frente}: índice del primer documento, el próximo en imprimirse.</li>
 *   <li>{@code fin}: índice del último documento agregado; {@code -1} si vacía.</li>
 *   <li>{@code cantidad}: número de documentos. Permite distinguir sin ambigüedad
 *       el estado vacío ({@code cantidad == 0}) del lleno
 *       ({@code cantidad == capacidad}). Sin este contador {@code frente == fin}
 *       sería ambiguo (podría significar vacía o con un solo elemento).</li>
 * </ul>
 *
 * <p><b>Avance circular de los índices.</b> Se usa aritmética modular para que
 * los índices "den la vuelta" y reutilicen las posiciones liberadas al frente:</p>
 * <ul>
 *   <li>{@code agregar} (encolar): {@code fin = (fin + 1) % capacidad}, luego se
 *       guarda el documento y {@code cantidad++}.</li>
 *   <li>{@code imprimir} (desencolar): se lee {@code documentos[frente]}, luego
 *       {@code frente = (frente + 1) % capacidad} y {@code cantidad--}.</li>
 *   <li>Al quedar vacía se restablece el estado canónico
 *       {@code frente = 0}, {@code fin = -1}.</li>
 * </ul>
 *
 * @author Agente Desarrollador
 */
public class ColaImpresion {

    /** Capacidad por defecto usada por el menú interactivo. */
    private static final int CAPACIDAD_POR_DEFECTO = 5;

    /** Arreglo circular que almacena los documentos en orden FIFO. */
    private final Documento[] documentos;

    /** Índice del primer documento (el próximo en imprimirse). */
    private int frente;

    /** Índice del último documento insertado; {@code -1} cuando está vacía. */
    private int fin;

    /** Cantidad de documentos actualmente en la cola. */
    private int cantidad;

    /**
     * Representa un trabajo de impresión con su nombre y cantidad de páginas.
     */
    public static final class Documento {

        private final String nombre;
        private final int paginas;

        /**
         * Crea un documento.
         *
         * @param nombre  nombre del documento (no vacío)
         * @param paginas cantidad de páginas (debe ser positiva)
         */
        public Documento(String nombre, int paginas) {
            this.nombre = nombre;
            this.paginas = paginas;
        }

        public String getNombre() {
            return nombre;
        }

        public int getPaginas() {
            return paginas;
        }

        @Override
        public String toString() {
            return "\"" + nombre + "\" (" + paginas + " pág.)";
        }
    }

    /**
     * Construye la cola de impresión con la capacidad indicada.
     *
     * @param capacidad número máximo de documentos que pueden esperar (&gt; 0)
     * @throws IllegalArgumentException si {@code capacidad} es menor o igual a cero
     */
    public ColaImpresion(int capacidad) {
        // Validación de caso límite: una capacidad no positiva no tiene sentido.
        if (capacidad <= 0) {
            throw new IllegalArgumentException(
                    "La capacidad debe ser positiva. Se recibió: " + capacidad);
        }
        this.documentos = new Documento[capacidad];
        this.frente = 0;
        this.fin = -1;
        this.cantidad = 0;
    }

    /**
     * Agrega un documento al final de la cola.
     *
     * <p>Valida el nombre y las páginas antes de tocar el arreglo. Si la cola
     * está llena, devuelve {@code null} sin modificar los documentos ya
     * encolados.</p>
     *
     * @param nombre  nombre del documento; no puede ser nulo ni estar en blanco
     * @param paginas cantidad de páginas; debe ser positiva
     * @return el {@link Documento} creado, o {@code null} si la cola está llena
     * @throws IllegalArgumentException si el nombre está vacío o las páginas no son positivas
     */
    public Documento agregar(String nombre, int paginas) {
        // Validación de casos límite: nombre vacío o páginas no positivas.
        if (nombre == null || nombre.trim().isEmpty()) {
            throw new IllegalArgumentException("El nombre del documento no puede estar vacío.");
        }
        if (paginas <= 0) {
            throw new IllegalArgumentException(
                    "La cantidad de páginas debe ser positiva. Se recibió: " + paginas);
        }
        // Si está llena, se informa con null y NO se altera el contenido.
        if (isFull()) {
            return null;
        }
        Documento nuevo = new Documento(nombre.trim(), paginas);
        // Avance circular del final: al llegar al último índice vuelve a 0.
        fin = (fin + 1) % capacidad();
        documentos[fin] = nuevo;
        cantidad++;
        return nuevo;
    }

    /**
     * Imprime (elimina y devuelve) el documento que está al frente de la cola.
     *
     * <p>Al retirar el frente, el siguiente documento pasa a ser el que llevaba
     * más tiempo esperando.</p>
     *
     * @return el documento impreso
     * @throws IllegalStateException si la cola está vacía
     */
    public Documento imprimir() {
        if (isEmpty()) {
            throw new IllegalStateException("No hay documentos para imprimir.");
        }
        Documento impreso = documentos[frente];
        documentos[frente] = null; // Se libera la referencia de la posición.
        // Avance circular del frente; la posición liberada podrá reutilizarse.
        frente = (frente + 1) % capacidad();
        cantidad--;
        // Si quedó vacía, se restablece el estado canónico.
        if (cantidad == 0) {
            frente = 0;
            fin = -1;
        }
        return impreso;
    }

    /**
     * Devuelve el documento que será el próximo en imprimirse, <b>sin</b>
     * retirarlo de la cola.
     *
     * @return el documento que está al frente
     * @throws IllegalStateException si la cola está vacía
     */
    public Documento proximo() {
        if (isEmpty()) {
            throw new IllegalStateException("No hay documentos para consultar.");
        }
        return documentos[frente];
    }

    /**
     * Indica si la cola no tiene documentos.
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
     * Devuelve la cantidad de documentos en espera.
     *
     * @return el número de documentos actuales
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
        return documentos.length;
    }

    /**
     * Representación del contenido en orden FIFO, útil para depuración y para
     * la demostración. No modifica la cola.
     *
     * @return los documentos desde el frente hasta el último agregado
     */
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder("[frente -> fin] ");
        for (int i = 0; i < cantidad; i++) {
            // Se recorre desde frente respetando el avance circular.
            int indice = (frente + i) % capacidad();
            sb.append(documentos[indice]);
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
        ColaImpresion cola = new ColaImpresion(CAPACIDAD_POR_DEFECTO);
        try (Scanner scanner = new Scanner(System.in)) {
            boolean salir = false;
            while (!salir) {
                mostrarMenu(cola);

                int opcion = leerOpcion(scanner);
                switch (opcion) {
                    case 1:
                        agregarDocumento(cola, scanner);
                        break;
                    case 2:
                        imprimirSiguiente(cola);
                        break;
                    case 3:
                        consultarProximo(cola);
                        break;
                    case 4:
                        salir = true;
                        System.out.println("Saliendo de la cola de impresión. ¡Hasta luego!");
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
     * @param cola cola de impresión a mostrar
     */
    private static void mostrarMenu(ColaImpresion cola) {
        System.out.println();
        System.out.println("========= COLA DE IMPRESIÓN =========");
        System.out.println("1. Agregar documento");
        System.out.println("2. Imprimir siguiente documento");
        System.out.println("3. Consultar próximo documento");
        System.out.println("4. Salir");
        System.out.println("Documentos en espera: " + cola.tamano()
                + " / " + cola.capacidad());
        System.out.print("Seleccione una opción: ");
    }

    /**
     * Lee una opción del menú de forma segura. Ante un valor no numérico
     * devuelve {@code -1} (opción inválida) sin abortar el programa.
     *
     * @param scanner lector de consola
     * @return el número ingresado, o {@code -1} si no es un entero válido
     */
    private static int leerOpcion(Scanner scanner) {
        String linea = scanner.nextLine().trim();
        try {
            return Integer.parseInt(linea);
        } catch (NumberFormatException e) {
            return -1; // Se maneja como opción inválida sin terminar el programa.
        }
    }

    /**
     * Lee un entero positivo de forma segura. Repite la lectura mientras la
     * entrada no sea un entero válido.
     *
     * @param scanner      lector de consola
     * @param mensaje      texto a mostrar como solicitud
     * @param valorMinimo  valor mínimo aceptado (inclusive)
     * @return el entero válido ingresado
     */
    private static int leerEntero(Scanner scanner, String mensaje, int valorMinimo) {
        while (true) {
            System.out.print(mensaje);
            String linea = scanner.nextLine().trim();
            try {
                int valor = Integer.parseInt(linea);
                if (valor < valorMinimo) {
                    System.out.println("Valor inválido: debe ser mayor o igual a " + valorMinimo + ".");
                    continue;
                }
                return valor;
            } catch (NumberFormatException e) {
                System.out.println("Entrada inválida: ingrese un número entero.");
            }
        }
    }

    /**
     * Solicita nombre y páginas, y agrega el documento manejando nombre vacío,
     * páginas no positivas y cola llena.
     *
     * @param cola    cola de impresión
     * @param scanner lector de consola
     */
    private static void agregarDocumento(ColaImpresion cola, Scanner scanner) {
        System.out.print("Ingrese el nombre del documento: ");
        String nombre = scanner.nextLine();
        int paginas = leerEntero(scanner, "Ingrese la cantidad de páginas: ", 1);
        try {
            Documento doc = cola.agregar(nombre, paginas);
            if (doc == null) {
                System.out.println("La cola está llena. No se pudo agregar el documento "
                        + "sin alterar los ya encolados.");
            } else {
                System.out.println("Documento agregado -> " + doc
                        + ". Documentos en espera: " + cola.tamano());
            }
        } catch (IllegalArgumentException e) {
            System.out.println("Documento inválido: " + e.getMessage());
        }
    }

    /**
     * Imprime el siguiente documento o informa si no hay ninguno.
     *
     * @param cola cola de impresión
     */
    private static void imprimirSiguiente(ColaImpresion cola) {
        try {
            Documento impreso = cola.imprimir();
            System.out.println("Imprimiendo -> " + impreso);
        } catch (IllegalStateException e) {
            System.out.println(e.getMessage());
        }
    }

    /**
     * Consulta el próximo documento sin retirarlo, o informa si no hay ninguno.
     *
     * @param cola cola de impresión
     */
    private static void consultarProximo(ColaImpresion cola) {
        try {
            Documento proximo = cola.proximo();
            System.out.println("Próximo a imprimir -> " + proximo
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
     * Ejecuta una demostración representativa: orden FIFO de varios documentos,
     * consulta del próximo sin retirar, cola vacía, cola llena, reutilización
     * circular y rechazo de entradas inválidas.
     */
    private static void ejecutarDemostracion() {
        System.out.println("=== DEMOSTRACIÓN DE ColaImpresion (cola circular FIFO) ===\n");

        System.out.println("1. Estado inicial");
        ColaImpresion cola = new ColaImpresion(3);
        verificar("la cola empieza vacía", cola.isEmpty());
        verificar("tamano() es 0", cola.tamano() == 0);
        verificar("isFull() es falso", !cola.isFull());

        System.out.println("\n2. Agregar varios documentos en orden (A, B, C)");
        cola.agregar("Informe", 10);
        cola.agregar("Carta", 2);
        cola.agregar("Tesis", 120);
        verificar("hay 3 documentos en espera", cola.tamano() == 3);
        verificar("el orden FIFO se conserva",
                "[frente -> fin] \"Informe\" (10 pág.) | \"Carta\" (2 pág.) | \"Tesis\" (120 pág.)"
                        .equals(cola.toString()));

        System.out.println("\n3. Consultar el próximo (no debe retirarlo)");
        verificar("el próximo es \"Informe\"",
                cola.proximo() != null && "Informe".equals(cola.proximo().getNombre()));
        verificar("la consulta NO retira (siguen 3)", cola.tamano() == 3);

        System.out.println("\n4. Imprimir en el mismo orden de llegada (FIFO)");
        Documento d1 = cola.imprimir();
        verificar("se imprime primero \"Informe\"",
                d1 != null && "Informe".equals(d1.getNombre()) && d1.getPaginas() == 10);
        Documento d2 = cola.imprimir();
        verificar("luego se imprime \"Carta\"",
                d2 != null && "Carta".equals(d2.getNombre()));
        verificar("el próximo ahora es \"Tesis\"",
                cola.proximo() != null && "Tesis".equals(cola.proximo().getNombre()));
        Documento d3 = cola.imprimir();
        verificar("finalmente se imprime \"Tesis\"",
                d3 != null && "Tesis".equals(d3.getNombre()));
        verificar("la cola quedó vacía", cola.isEmpty());

        System.out.println("\n5. Cola vacía: consultar e imprimir no deben fallar");
        boolean consultaManejo = false;
        try {
            cola.proximo();
        } catch (IllegalStateException e) {
            consultaManejo = true;
        }
        verificar("proximo() en cola vacía informa el problema", consultaManejo);

        boolean impresionManejo = false;
        try {
            cola.imprimir();
        } catch (IllegalStateException e) {
            impresionManejo = true;
        }
        verificar("imprimir() en cola vacía informa el problema", impresionManejo);

        System.out.println("\n6. Cola llena: se rechaza el nuevo trabajo sin alterar los encolados");
        ColaImpresion llena = new ColaImpresion(2);
        llena.agregar("Doc1", 1);
        llena.agregar("Doc2", 1);
        verificar("la cola está llena", llena.isFull());
        String antes = llena.toString();
        Documento rechazado = llena.agregar("Doc3", 5);
        verificar("agregar en cola llena devuelve null (no se encola)", rechazado == null);
        verificar("la cola no cambió al intentar agregar en llena", antes.equals(llena.toString()));

        System.out.println("\n7. Reutilización circular de posiciones liberadas");
        ColaImpresion circular = new ColaImpresion(3);
        circular.agregar("A", 1);
        circular.agregar("B", 1);
        circular.imprimir(); // sale A -> frente avanza
        circular.imprimir(); // sale B -> frente avanza
        circular.agregar("C", 1); // reutiliza posición circular
        circular.agregar("D", 1);
        circular.agregar("E", 1);
        verificar("se reutilizaron las posiciones (3 en espera)", circular.tamano() == 3);
        verificar("el frente es ahora \"C\"",
                circular.proximo() != null && "C".equals(circular.proximo().getNombre()));
        verificar("la cola está llena con 3 elementos", circular.isFull());

        System.out.println("\n8. Rechazo de entradas inválidas");
        boolean nombreRechazado = false;
        try {
            cola.agregar("   ", 3);
        } catch (IllegalArgumentException e) {
            nombreRechazado = true;
        }
        verificar("nombre vacío lanza IllegalArgumentException", nombreRechazado);

        boolean paginasRechazadasCero = false;
        try {
            cola.agregar("Valido", 0);
        } catch (IllegalArgumentException e) {
            paginasRechazadasCero = true;
        }
        verificar("páginas 0 lanza IllegalArgumentException", paginasRechazadasCero);

        boolean paginasRechazadasNegativas = false;
        try {
            cola.agregar("Valido", -4);
        } catch (IllegalArgumentException e) {
            paginasRechazadasNegativas = true;
        }
        verificar("páginas negativas lanzan IllegalArgumentException", paginasRechazadasNegativas);

        boolean capacidadRechazada = false;
        try {
            new ColaImpresion(0);
        } catch (IllegalArgumentException e) {
            capacidadRechazada = true;
        }
        verificar("capacidad 0 lanza IllegalArgumentException", capacidadRechazada);

        System.out.println("\n=== RESULTADO ===");
        if (fallos == 0) {
            System.out.println("Todas las comprobaciones pasaron correctamente.");
        } else {
            System.out.println("Hubo " + fallos + " comprobación(es) fallida(s).");
            throw new AssertionError("La demostración de ColaImpresion falló.");
        }
    }
}

/*
 * ============================================================================
 * PROMPT INICIAL UTILIZADO PARA GENERAR ESTA SOLUCIÓN
 * ============================================================================
 *
 * Quiero implementar en Java una función recursiva que imprima un conteo desde
 * un entero no negativo n hasta 0.
 *
 * La función no necesariamente devuelve un valor: su objetivo es mostrar cada
 * número en pantalla. Por eso, debe tener tipo de retorno void; imprimir el
 * conteo es su efecto.
 *
 * El caso base ocurre cuando n == 0: se imprime 0 y la función termina, sin
 * hacer otra llamada recursiva.
 *
 * En el caso recursivo, se imprime el valor actual de n y se llama a la función
 * con n - 1. Así, el número disminuye en cada llamada y se acerca al caso base.
 * Si se usara n + 1, el valor aumentaría y se alejaría de 0; la recursión no
 * alcanzaría el caso base y podría terminar provocando un desbordamiento de
 * pila.
 *
 * La función debe rechazar los valores negativos con un mensaje claro, ya que al
 * disminuirlos tampoco se llegaría a 0.
 *
 * Prueba la ejecución con estos casos y verifica el orden exacto de los números
 * impresos:
 *   - contar(0):  0
 *   - contar(1):  1 0
 *   - contar(5):  5 4 3 2 1 0
 *   - contar(-1): debe rechazar la entrada.
 * ============================================================================
 */

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.nio.charset.StandardCharsets;

/**
 * Conteo recursivo desde un entero no negativo {@code n} hasta {@code 0}.
 *
 * <p>El conteo se define de forma recurrente como:
 * {@code contar(n) = imprimir(n); contar(n - 1)}, con el caso base
 * {@code contar(0) = imprimir(0)} (sin llamada recursiva adicional).</p>
 *
 * <p>La función es {@code void} porque su efecto observable es la impresión en
 * pantalla, no la devolución de un valor.</p>
 *
 * @author Agente Desarrollador
 */
public final class Contar {

    /**
     * Profundidad máxima de recursión admitida. Cada llamada recursiva agrega un
     * nivel a la pila del programa; para evitar un desbordamiento de pila
     * ({@link StackOverflowError}) de forma determinista se rechazan las entradas
     * que superarían este límite.
     *
     * <p>El valor es conservador: en la JVM con el tamaño de pila por defecto la
     * recursión tolera más niveles, pero se fija un margen amplio para que el
     * comportamiento sea seguro en distintos entornos.</p>
     */
    private static final int MAX_PROFUNDIDAD_RECURSIVA = 10_000;

    /** Impide instanciar una clase que solo agrupa métodos estáticos. */
    private Contar() {
    }

    // ------------------------------------------------------------------------
    // API PÚBLICA
    // ------------------------------------------------------------------------

    /**
     * Imprime, de forma recursiva, el conteo desde {@code n} hasta {@code 0},
     * separando los números por un espacio y terminando con un salto de línea.
     *
     * <p>Ejemplos:
     * {@code contar(0)} imprime {@code 0};
     * {@code contar(5)} imprime {@code 5 4 3 2 1 0}.</p>
     *
     * @param n número entero no negativo desde el cual se inicia el conteo
     * @throws IllegalArgumentException si {@code n} es negativo o si excede el
     *         límite de profundidad recursiva admitido
     */
    public static void contar(int n) {
        // La validación se hace UNA sola vez, en el método público. Así la
        // recursión interna no repite comprobaciones en cada nivel de la pila.
        validar(n);
        contarRecursivo(n);
    }

    /**
     * Convierte un texto a entero, rechazando de forma descriptiva las entradas
     * que no sean números enteros válidos.
     *
     * @param texto cadena a convertir
     * @return el entero representado por {@code texto}
     * @throws IllegalArgumentException si el texto es nulo, está vacío o no es un
     *         número entero válido
     */
    public static int parsear(String texto) {
        if (texto == null || texto.trim().isEmpty()) {
            throw new IllegalArgumentException("La entrada no puede ser nula ni vacía.");
        }
        try {
            return Integer.parseInt(texto.trim());
        } catch (NumberFormatException excepcion) {
            throw new IllegalArgumentException(
                    "La entrada no es un número entero válido: \"" + texto + "\"");
        }
    }

    // ------------------------------------------------------------------------
    // NÚCLEO RECURSIVO Y VALIDACIÓN
    // ------------------------------------------------------------------------

    /**
     * Núcleo recursivo. Se asume que {@code n} ya fue validado por
     * {@link #contar(int)}.
     *
     * @param n entero no negativo ya validado
     */
    private static void contarRecursivo(int n) {
        // --------------------------------------------------------------------
        // CASO BASE: n == 0.
        // Se imprime el 0 y la función termina. El 'return' impide cualquier
        // llamada recursiva adicional: la recursión se detiene aquí. Sin este
        // caso base, la recurrencia se llamaría indefinidamente y terminaría
        // provocando un StackOverflowError.
        //
        // El 0 es el último número del conteo, por eso se imprime con
        // println (agrega el salto de línea final y no deja un espacio colgante).
        // --------------------------------------------------------------------
        if (n == 0) {
            System.out.println(0);
            return;
        }

        // --------------------------------------------------------------------
        // CASO RECURSIVO: imprimir(n) y luego contarRecursivo(n - 1).
        // En cada llamada el problema se REDUCE en uno (n -> n - 1), por lo que
        // tarde o temprano se alcanza el caso base (n == 0).
        //
        // SI SE USARA n + 1 el problema CRECERÍA: el valor se alejaría de 0 y el
        // caso base nunca se alcanzaría, agotando la pila (StackOverflowError).
        //
        // ORDEN DE IMPRESIÓN: como el 'print' ocurre ANTES de la llamada
        // recursiva, los números se muestran en orden descendente (n, n-1, ...,
        // 0). No hay "desenrollado" visible de la pila como en las funciones que
        // devuelven valor: aquí el efecto se materializa en la fase de IDA,
        // mientras la pila todavía se está apilando.
        //
        // Ejemplo para contar(5):
        //   contar(5) -> imprime 5, llama a contar(4)   [5 apilado]
        //   contar(4) -> imprime 4, llama a contar(3)   [4 apilado]
        //   contar(3) -> imprime 3, llama a contar(2)   [3 apilado]
        //   contar(2) -> imprime 2, llama a contar(1)   [2 apilado]
        //   contar(1) -> imprime 1, llama a contar(0)   [1 apilado]
        //   contar(0) -> imprime 0  [CASO BASE: deja de llamarse]
        //   Salida: 5 4 3 2 1 0
        // --------------------------------------------------------------------
        System.out.print(n + " ");
        contarRecursivo(n - 1);
    }

    /**
     * Valida el argumento de {@link #contar(int)}.
     *
     * @param n valor a validar
     * @throws IllegalArgumentException si {@code n} es negativo o excede el
     *         límite de profundidad recursiva
     */
    private static void validar(int n) {
        // Dominio válido: enteros no negativos. Un n negativo NO es un conteo
        // válido: al disminuirlo (n - 1) el valor se aleja de 0 y el caso base
        // jamás se alcanzaría, degradando en un StackOverflowError.
        if (n < 0) {
            throw new IllegalArgumentException(
                    "El conteo no está definido para números negativos: " + n);
        }

        // Límite explícito de profundidad para no provocar un StackOverflowError
        // de forma silenciosa ante entradas desproporcionadamente grandes.
        if (n > MAX_PROFUNDIDAD_RECURSIVA) {
            throw new IllegalArgumentException(
                    "n = " + n + " excede la profundidad recursiva máxima permitida ("
                            + MAX_PROFUNDIDAD_RECURSIVA + ").");
        }
    }

    // ------------------------------------------------------------------------
    // CASOS DE PRUEBA
    // ------------------------------------------------------------------------

    /**
     * Método de entrada con los casos de prueba solicitados en el análisis.
     *
     * <p>Se captura la salida estándar para comprobar el ORDEN EXACTO de los
     * números impresos, ya que {@link #contar(int)} no devuelve un valor.</p>
     *
     * @param args argumentos opcionales; si se pasa un valor se interpreta como
     *             el número {@code n} a contar
     */
    public static void main(String[] args) {
        System.out.println("== Casos de prueba basicos ==");
        probar(0, "0");
        probar(1, "1 0");
        probar(5, "5 4 3 2 1 0");

        System.out.println();
        System.out.println("== Entradas invalidas ==");
        probarArgumentoInvalido(-1);
        probarArgumentoInvalido(-100);
        probarNoEntero("3.5");
        probarNoEntero("abc");
        probarNoEntero("");

        System.out.println();
        System.out.println("== Limites de profundidad ==");
        probarArgumentoInvalido(MAX_PROFUNDIDAD_RECURSIVA + 1);

        // Si se recibe un argumento por línea de comandos, se procesa como entrada.
        if (args.length > 0) {
            System.out.println();
            System.out.println("== Entrada por linea de comandos ==");
            probarEntradaUsuario(args[0]);
        }
    }

    /**
     * Ejecuta un caso de prueba comparando la salida impresa con la esperada.
     *
     * @param n        entrada de la función
     * @param esperado texto exacto que debe imprimir {@link #contar(int)}
     */
    private static void probar(int n, String esperado) {
        String obtenido = capturarSalida(n);
        boolean correcto = obtenido.equals(esperado);
        System.out.printf("contar(%d) -> \"%s\" | esperado = \"%s\" | %s%n",
                n, obtenido, esperado, correcto ? "OK" : "FALLO");
    }

    /**
     * Invoca {@link #contar(int)} capturando temporalmente la salida estándar.
     *
     * @param n entrada a contar
     * @return el texto impreso por {@code contar(n)} sin el salto de línea final
     */
    private static String capturarSalida(int n) {
        PrintStream salidaOriginal = System.out;
        ByteArrayOutputStream buffer = new ByteArrayOutputStream();
        try {
            System.setOut(new PrintStream(buffer, true, StandardCharsets.UTF_8));
            contar(n);
        } finally {
            System.setOut(salidaOriginal);
        }
        // strip() elimina el salto de línea final para comparar el contenido.
        return buffer.toString(StandardCharsets.UTF_8).strip();
    }

    /**
     * Verifica que un argumento inválido sea rechazado con la excepción esperada.
     *
     * @param n argumento inválido (negativo o fuera de profundidad)
     */
    private static void probarArgumentoInvalido(int n) {
        try {
            contar(n);
            System.out.printf("contar(%d) -> FALLO (no se lanzo ninguna excepcion)%n", n);
        } catch (IllegalArgumentException excepcion) {
            System.out.printf("contar(%d) -> OK (rechazado: %s)%n", n, excepcion.getMessage());
        }
    }

    /**
     * Verifica que una entrada no entera sea rechazada al parsearla.
     *
     * @param texto entrada no entera
     */
    private static void probarNoEntero(String texto) {
        try {
            int valor = parsear(texto);
            System.out.printf("parsear(\"%s\") -> FALLO (se obtuvo %d)%n", texto, valor);
        } catch (IllegalArgumentException excepcion) {
            System.out.printf("parsear(\"%s\") -> OK (rechazado: %s)%n",
                    texto, excepcion.getMessage());
        }
    }

    /**
     * Procesa una entrada de usuario recibida como texto.
     *
     * @param texto entrada del usuario
     */
    private static void probarEntradaUsuario(String texto) {
        try {
            int n = parsear(texto);
            System.out.printf("Entrada \"%s\": ", texto);
            contar(n);
        } catch (IllegalArgumentException excepcion) {
            System.out.printf("Entrada \"%s\": ERROR -> %s%n", texto, excepcion.getMessage());
        }
    }
}

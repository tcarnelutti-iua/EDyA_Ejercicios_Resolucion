/*
 * ============================================================================
 * PROMPT ORIGINAL
 * ============================================================================
 * Implementá un verificador de paréntesis balanceados usando una pila enlazada
 * y guardá la solución en:
 * Guía_Pilas_colas_lista_enlazada/Ejercicio_7/VerificadorParentesis.java
 *
 * Requisitos:
 * - Definí una clase pública VerificadorParentesis.
 * - Implementá una pila enlazada de caracteres para almacenar los paréntesis
 *   de apertura '('.
 * - Recorré la expresión de izquierda a derecha:
 *   - Al encontrar '(', apilalo.
 *   - Al encontrar ')', si la pila está vacía, la expresión es inválida; de lo
 *     contrario, desapilá una apertura.
 *   - Ignorá los demás caracteres.
 * - Al terminar, la expresión es válida solo si la pila está vacía. Así se
 *   detectan aperturas sin cierre y cierres sin apertura o en orden incorrecto.
 * - Implementá un método que reciba una expresión String y devuelva si sus
 *   paréntesis están balanceados. Una expresión null debe considerarse
 *   inválida.
 * - Incluí un main que pruebe los ejemplos válidos `(2 + 3) * (5 - 1)` y
 *   `((a + b) * c)`, y los inválidos `(2 + 3` y `())(`.
 * - La verificación debe tener complejidad O(n) en tiempo y O(n) en espacio en
 *   el peor caso.
 * - No uses arrays ni colecciones de Java para implementar la pila; hacelo con
 *   nodos enlazados.
 *
 * Incluí al comienzo del archivo, como comentario, este prompt completo y
 * explicá cómo la pila guarda las aperturas pendientes: cada cierre válido
 * elimina la apertura más reciente, y al final la pila debe quedar vacía.
 * Aclarà si hubo ajustes posteriores. Si no modificaste los requisitos,
 * indicá: "Ajustes posteriores: ninguno".
 *
 * Mantené la solución en el archivo indicado y evitá agregar otros archivos.
 *
 * ----------------------------------------------------------------------------
 * AJUSTES POSTERIORES
 * ----------------------------------------------------------------------------
 * Ajustes posteriores: ninguno
 *
 * ============================================================================
 * EXPLICACIÓN: CÓMO LA PILA GUARDA LAS APERTURAS PENDIENTES
 * ============================================================================
 * La pila funciona como una estructura LIFO ("last in, first out"): el último
 * elemento apilado es el primero en desapilarse. Al recorrer la expresión, cada
 * '(' que todavía no tiene su cierre se apila. Ese paréntesis queda "pendiente"
 * hasta que aparezca su ')' correspondiente.
 *
 * Cuando se encuentra un ')', la pila se consulta y se desapila su tope: se
 * elimina la apertura MÁS RECIENTE, que es la que ese cierre debe emparejar
 * (porque los paréntesis se anidan de adentro hacia afuera). Si en ese momento
 * la pila ya está vacía, significa que aparece un cierre sin una apertura que
 * le corresponda y la expresión es inválida de inmediato.
 *
 * Al terminar el recorrido, si la pila quedó vacía es porque TODAS las
 * aperturas encontraron su cierre, en el orden correcto: la expresión es
 * válida. Si todavía quedan nodos en la pila, hay aperturas sin cerrar y la
 * expresión es inválida. De este modo se detectan tanto aperturas sin cierre
 * como cierres sin apertura o en orden incorrecto (por ejemplo "())(": el
 * tercer ')' se topa con una pila vacía y falla).
 *
 * El recorrido es O(n) en tiempo: cada carácter se procesa una sola vez y las
 * operaciones de apilar/desapilar son O(1). El espacio es O(n) en el peor caso,
 * cuando la expresión contiene n aperturas y ninguna se cierra.
 * ============================================================================
 */

/**
 * Verificador de paréntesis balanceados basado en una <em>pila enlazada</em>.
 *
 * <p>Se considera únicamente el emparejamiento de paréntesis
 * {@code '('} y {@code ')'}; cualquier otro carácter de la expresión se
 * ignora. La verificación recorre la expresión una sola vez, por lo que es
 * O(n) en tiempo. En el peor caso (todas las aperturas sin cerrar) ocupa O(n)
 * de espacio.
 *
 * <p>No se utilizan arrays ni colecciones de Java: la pila se construye con
 * nodos enlazados ({@link Nodo}) cuyo {@code tope} es el nodo con la apertura
 * más reciente.
 */
public class VerificadorParentesis {

    /**
     * Nodo de la lista enlazada simple que forma la pila de aperturas.
     *
     * <p>Cada nodo guarda un carácter de apertura y una referencia al
     * siguiente nodo. El último nodo apunta a {@code null}.
     */
    private static final class Nodo {

        /** Carácter de apertura almacenado en este nodo. */
        private final char valor;

        /** Referencia al siguiente nodo de la pila (null si es el último). */
        private Nodo siguiente;

        /**
         * Crea un nodo con un carácter y una referencia al siguiente nodo.
         *
         * @param valor     carácter a almacenar.
         * @param siguiente nodo que le sigue en la pila.
         */
        private Nodo(char valor, Nodo siguiente) {
            this.valor = valor;
            this.siguiente = siguiente;
        }
    }

    /** Tope de la pila; representa la apertura más reciente (o null si no hay). */
    private Nodo tope;

    /** Cantidad de aperturas pendientes almacenadas en la pila. */
    private int tamanio;

    /**
     * Crea un verificador con la pila vacía.
     */
    public VerificadorParentesis() {
        this.tope = null;
        this.tamanio = 0;
    }

    /**
     * Indica si los paréntesis de una expresión están balanceados.
     *
     * <p>Recorre la expresión de izquierda a derecha ignorando todo carácter
     * que no sea un paréntesis: cada {@code '('} se apila y cada {@code ')'}
     * desapila la apertura más reciente. Si aparece un {@code ')'} con la pila
     * vacía, no hay apertura que emparejar y la expresión es inválida. Al
     * terminar, es válida solo si no quedaron aperturas pendientes (pila
     * vacía). La operación es O(n) en tiempo y O(n) en espacio en el peor caso.
     *
     * @param expresion expresión a verificar; puede ser {@code null}.
     * @return {@code true} si los paréntesis están balanceados; {@code false}
     *         si la expresión es {@code null}, hay un cierre sin apertura o
     *         quedan aperturas sin cerrar.
     */
    public boolean estaBalanceada(String expresion) {
        // Una expresión null no puede considerarse balanceada.
        if (expresion == null) {
            return false;
        }

        // Se reutiliza la instancia para cada verificación, partiendo vacía.
        tope = null;
        tamanio = 0;

        for (int i = 0; i < expresion.length(); i++) {
            char caracter = expresion.charAt(i);

            if (caracter == '(') {
                // Apertura pendiente: se apila en el tope.
                tope = new Nodo(caracter, tope);
                tamanio++;
            } else if (caracter == ')') {
                if (tope == null) {
                    // Cierre sin apertura que lo empareje: inválida.
                    return false;
                }
                // Se elimina la apertura más reciente (el tope de la pila).
                tope = tope.siguiente;
                tamanio--;
            }
            // Cualquier otro carácter se ignora.
        }

        // Al final, todas las aperturas deben haber sido cerradas.
        return tope == null;
    }

    /**
     * Devuelve la cantidad de aperturas que quedaron pendientes tras la última
     * verificación.
     *
     * <p>Es útil únicamente con fines de prueba o depuración; no forma parte de
     * la verificación en sí.
     *
     * @return cantidad de aperturas pendientes en la pila.
     */
    public int aperturasPendientes() {
        return tamanio;
    }

    /**
     * Punto de entrada de prueba. Verifica los ejemplos válidos
     * {@code (2 + 3) * (5 - 1)} y {@code ((a + b) * c)}, los inválidos
     * {@code (2 + 3} y {@code ()) (} y casos adicionales como {@code null}.
     *
     * @param args argumentos de línea de comandos (no se usan).
     */
    public static void main(String[] args) {
        VerificadorParentesis verificador = new VerificadorParentesis();

        System.out.println("== Casos válidos ==");
        probar(verificador, "(2 + 3) * (5 - 1)");
        probar(verificador, "((a + b) * c)");

        System.out.println();
        System.out.println("== Casos inválidos ==");
        probar(verificador, "(2 + 3");
        probar(verificador, "())(");

        System.out.println();
        System.out.println("== Casos adicionales ==");
        probar(verificador, null);          // null -> inválida
        probar(verificador, "");            // sin paréntesis -> válida
        probar(verificador, "sin parentesis"); // se ignoran los demás caracteres
        probar(verificador, ")( ");         // cierre antes de apertura -> inválida
        probar(verificador, "((()))");      // anidamiento correcto -> válida
    }

    /**
     * Verifica una expresión y muestra el resultado por consola.
     *
     * @param verificador verificador a utilizar.
     * @param expresion   expresión a verificar (puede ser {@code null}).
     */
    private static void probar(VerificadorParentesis verificador, String expresion) {
        boolean balanceada = verificador.estaBalanceada(expresion);
        System.out.println(
                "estaBalanceada(" + (expresion == null ? "null" : "\"" + expresion + "\"")
                        + ") = " + balanceada
                        + "  [aperturas pendientes: "
                        + verificador.aperturasPendientes() + "]");
    }
}

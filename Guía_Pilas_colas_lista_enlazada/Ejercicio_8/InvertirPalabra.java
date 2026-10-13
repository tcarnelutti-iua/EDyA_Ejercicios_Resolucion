/*
 * ============================================================================
 * PROMPT ORIGINAL
 * ============================================================================
 * Implementá un programa que invierta una palabra usando una pila enlazada y
 * guardá la solución en:
 * Guía_Pilas_colas_lista_enlazada/Ejercicio_8/InvertirPalabra.java
 *
 * Requisitos:
 * - Definí una clase pública InvertirPalabra.
 * - Implementá una pila enlazada de caracteres.
 * - Creá un método que reciba una palabra y devuelva sus caracteres en orden
 *   inverso.
 * - Para invertirla, recorré la palabra de izquierda a derecha y apilá cada
 *   carácter. Luego desapilá todos los caracteres y agregalos al resultado.
 * - No uses métodos de inversión ya implementados, como StringBuilder.reverse();
 *   la inversión debe realizarse mediante la pila.
 * - Una pila sigue la política LIFO: el último carácter apilado es el primero
 *   en salir. Por eso, al desapilar los caracteres se obtiene el orden inverso
 *   al original.
 * - Definí claramente el comportamiento para una palabra vacía y para null. La
 *   palabra vacía debe producir una palabra vacía; null debe considerarse
 *   inválido e informarse claramente.
 * - Incluí un main que pruebe el ejemplo `"algoritmo" → "omtirogla"` y también
 *   una palabra vacía.
 * - La inversión debe tener complejidad O(n) en tiempo y O(n) en espacio.
 * - No uses arrays ni colecciones de Java para almacenar los caracteres de la
 *   pila.
 *
 * Incluí al comienzo del archivo, como comentario, este prompt completo y
 * explicá por qué LIFO invierte el orden de los caracteres. Aclarà si hubo
 * ajustes posteriores. Si no modificaste los requisitos, indicá:
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
 * EXPLICACIÓN: POR QUÉ LIFO INVIERTE EL ORDEN DE LOS CARACTERES
 * ============================================================================
 * Una pila es una estructura LIFO ("last in, first out"): el último elemento
 * que se apila es el primero que se desapila.
 *
 * Al recorrer la palabra de izquierda a derecha y apilar cada carácter, el
 * primer carácter de la palabra queda en el FONDO de la pila y el último queda
 * en el TOPE. Es decir, el orden de apilado es el orden original de la palabra.
 *
 * Como al desapilar siempre se extrae primero el tope, los caracteres salen en
 * el orden exactamente inverso al que entraron: el último carácter de la palabra
 * es el primero en salir, y el primero es el último. Por eso, al ir agregando
 * cada carácter desapilado al resultado, se obtiene la palabra invertida.
 *
 * Ejemplo con "abc":
 *   Apilar 'a' -> tope: a
 *   Apilar 'b' -> tope: b -> a
 *   Apilar 'c' -> tope: c -> b -> a
 *   Desapilar -> 'c', luego 'b', luego 'a'  =>  "cba"
 *
 * El recorrido es O(n) en tiempo: cada carácter se apila y se desapila una sola
 * vez, y ambas operaciones son O(1). El espacio es O(n): la pila guarda un nodo
 * por cada carácter de la palabra.
 * ============================================================================
 */

/**
 * Invierte una palabra utilizando una <em>pila enlazada de caracteres</em>.
 *
 * <p>La palabra se recorre de izquierda a derecha apilando cada carácter y
 * luego se desapila por completo para construir el resultado. Gracias a la
 * política LIFO de la pila, el resultado queda en orden inverso al original.
 *
 * <p>No se utilizan arrays ni colecciones de Java para almacenar los
 * caracteres: la pila se implementa con nodos enlazados ({@link Nodo}). Tampoco
 * se usan métodos de inversión preexistentes como {@code StringBuilder.reverse()}.
 *
 * <p>La inversión es O(n) en tiempo y O(n) en espacio.
 */
public class InvertirPalabra {

    /**
     * Nodo de la lista enlazada simple que forma la pila.
     *
     * <p>Cada nodo guarda un carácter y una referencia al siguiente nodo. El
     * último nodo apunta a {@code null}.
     */
    private static final class Nodo {

        /** Carácter almacenado en este nodo. */
        private final char caracter;

        /** Referencia al siguiente nodo de la pila (null si es el fondo). */
        private Nodo siguiente;

        /**
         * Crea un nodo con un carácter y una referencia al siguiente nodo.
         *
         * @param caracter  carácter a almacenar.
         * @param siguiente nodo que le sigue en la pila.
         */
        private Nodo(char caracter, Nodo siguiente) {
            this.caracter = caracter;
            this.siguiente = siguiente;
        }
    }

    /** Tope de la pila; es {@code null} cuando la pila está vacía. */
    private Nodo tope;

    /**
     * Crea una instancia con la pila vacía.
     */
    public InvertirPalabra() {
        this.tope = null;
    }

    /**
     * Devuelve los caracteres de la palabra en orden inverso.
     *
     * <p>Recorre la palabra de izquierda a derecha apilando cada carácter y
     * luego desapila todos los caracteres, agregándolos al resultado. Debido a
     * la política LIFO, el resultado queda invertido. La operación es O(n) en
     * tiempo y O(n) en espacio.
     *
     * <p>Comportamiento según la entrada:
     * <ul>
     *   <li>Palabra vacía ({@code ""}): devuelve la palabra vacía
     *       ({@code ""}), ya que no hay caracteres que invertir.</li>
     *   <li>Palabra {@code null}: se considera inválida y se informa lanzando
     *       una {@link IllegalArgumentException} con un mensaje claro.</li>
     *   <li>Palabra de uno o más caracteres: devuelve la palabra invertida.</li>
     * </ul>
     *
     * @param palabra palabra a invertir; no puede ser {@code null}.
     * @return la palabra con sus caracteres en orden inverso; {@code ""} si la
     *         palabra de entrada es vacía.
     * @throws IllegalArgumentException si {@code palabra} es {@code null}.
     */
    public String invertir(String palabra) {
        // Una palabra null no es una entrada válida: se informa claramente.
        if (palabra == null) {
            throw new IllegalArgumentException(
                    "No se puede invertir: la palabra es null (entrada inválida).");
        }

        // Una palabra vacía se invierte en sí misma (palabra vacía).
        if (palabra.isEmpty()) {
            return "";
        }

        // Se parte siempre de una pila vacía por si se reutiliza la instancia.
        tope = null;
        int longitud = palabra.length();

        // Fase 1: apilar cada carácter de izquierda a derecha.
        for (int i = 0; i < palabra.length(); i++) {
            tope = new Nodo(palabra.charAt(i), tope);
        }

        // Se reserva la capacidad exacta del resultado (misma longitud).
        StringBuilder resultado = new StringBuilder(longitud);

        // Segunda fase: desapilar todos los caracteres y agregarlos al
        // resultado. El tope (último apilado) sale primero: orden inverso.
        while (tope != null) {
            resultado.append(tope.caracter);
            tope = tope.siguiente; // Avanza el tope al siguiente nodo.
        }

        return resultado.toString();
    }

    /**
     * Indica si la pila está vacía.
     *
     * <p>Se incluye con fines de prueba o depuración; no forma parte del
     * algoritmo de inversión.
     *
     * @return {@code true} si no hay caracteres apilados.
     */
    public boolean estaVacia() {
        return tope == null;
    }

    /**
     * Punto de entrada de prueba. Invierte el ejemplo {@code "algoritmo"} (que
     * debe dar {@code "omtirogla"}) y una palabra vacía, y además muestra el
     * comportamiento ante una entrada {@code null}.
     *
     * @param args argumentos de línea de comandos (no se usan).
     */
    public static void main(String[] args) {
        InvertirPalabra inversor = new InvertirPalabra();

        System.out.println("== Ejemplos ==");
        probar(inversor, "algoritmo");
        probar(inversor, "");

        System.out.println();
        System.out.println("== Casos adicionales ==");
        probar(inversor, "a");
        probar(inversor, "reconocer");
        probar(inversor, "Programación");

        System.out.println();
        System.out.println("== Entrada inválida ==");
        probarNull(inversor);
    }

    /**
     * Invierte una palabra y muestra el resultado por consola.
     *
     * @param inversor inversor a utilizar.
     * @param palabra  palabra a invertir (puede ser vacía).
     */
    private static void probar(InvertirPalabra inversor, String palabra) {
        String invertida = inversor.invertir(palabra);
        System.out.println(
                "invertir(\"" + palabra + "\") = \"" + invertida + "\"");
    }

    /**
     * Intenta invertir {@code null} y muestra cómo se informa el error.
     *
     * @param inversor inversor a utilizar.
     */
    private static void probarNull(InvertirPalabra inversor) {
        try {
            inversor.invertir(null);
            System.out.println("ERROR: se esperaba una excepción para null.");
        } catch (IllegalArgumentException e) {
            System.out.println("invertir(null) -> " + e.getMessage());
        }
    }
}

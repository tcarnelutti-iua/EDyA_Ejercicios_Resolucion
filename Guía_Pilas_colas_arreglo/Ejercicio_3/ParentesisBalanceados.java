import java.util.Scanner;

/**
 * Valida si los paréntesis {@code '('} y {@code ')'} de una expresión matemática
 * están balanceados.
 *
 * <p><b>¿Por qué una pila y no una cola?</b> Cada paréntesis de cierre debe
 * emparejarse con el paréntesis de apertura <i>más reciente</i> que todavía no
 * fue cerrado. Ese comportamiento es LIFO (<i>Last In, First Out</i>) y es
 * exactamente el de una pila: al apilar cada {@code '('} y desapilar con cada
 * {@code ')'}, la cima siempre contiene la apertura pendiente más próxima. Una
 * cola, al ser FIFO (<i>First In, First Out</i>), emparejaría el cierre con la
 * apertura <i>más antigua</i>, lo que rompe el anidamiento y no representa la
 * regla de los paréntesis.</p>
 *
 * <p>El análisis se limita al balance de paréntesis: cualquier otro carácter
 * (dígitos, letras, operadores, espacios) se ignora y no se valida la sintaxis
 * matemática de la expresión.</p>
 *
 * <p>La pila se implementa sobre un arreglo de {@code char} y no se usan
 * {@code Stack}, {@code Queue} ni ninguna colección. La capacidad del arreglo es
 * la longitud de la expresión, suficiente porque la cantidad de aperturas nunca
 * puede superarla.</p>
 *
 * @author Agente Desarrollador
 */
public class ParentesisBalanceados {

    /**
     * Punto de entrada del programa: lee una expresión completa desde la consola
     * y muestra si sus paréntesis están balanceados.
     *
     * @param args argumentos de línea de comandos (no se usan)
     */
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        System.out.println("=== Validación de paréntesis balanceados (pila LIFO) ===");
        System.out.print("Ingresa la expresión matemática: ");

        // nextLine() conserva la expresión completa, incluidos los espacios internos.
        String expresion = scanner.nextLine();

        boolean balanceada = estaBalanceada(expresion);

        System.out.println();
        if (balanceada) {
            System.out.println("Resultado: VÁLIDO - los paréntesis están balanceados.");
        } else {
            System.out.println("Resultado: INVÁLIDO - los paréntesis no están balanceados.");
        }

        scanner.close();
    }

    /**
     * Determina si los paréntesis de {@code expresion} están balanceados.
     *
     * <p>Recorre la expresión de izquierda a derecha apilando cada {@code '('} y
     * desapilando con cada {@code ')'}. Es válida únicamente si nunca aparece un
     * cierre sin apertura disponible y la pila queda vacía al final.</p>
     *
     * @param expresion expresión a analizar (puede contener espacios y otros caracteres)
     * @return {@code true} si los paréntesis están balanceados; {@code false} en caso contrario
     */
    public static boolean estaBalanceada(String expresion) {
        // Caso límite: una expresión nula o vacía no tiene paréntesis que balancear.
        if (expresion == null || expresion.isEmpty()) {
            return true;
        }

        // La cantidad de aperturas nunca supera la longitud de la expresión,
        // por lo que este arreglo siempre tiene capacidad suficiente.
        PilaCaracteres pila = new PilaCaracteres(expresion.length());

        for (int i = 0; i < expresion.length(); i++) {
            char caracter = expresion.charAt(i);

            if (caracter == '(') {
                // Apila la apertura: queda pendiente de un cierre.
                pila.push(caracter);
            } else if (caracter == ')') {
                // Un cierre sin apertura disponible hace inválida la expresión.
                if (pila.isEmpty()) {
                    return false;
                }
                // Empareja el cierre con la apertura más reciente (LIFO).
                pila.pop();
            }
            // Cualquier otro carácter se ignora: no afecta al balance.
        }

        // Solo es válida si no quedaron aperturas sin cerrar.
        return pila.isEmpty();
    }

    /**
     * Pila de caracteres de tamaño fijo implementada sobre un arreglo.
     *
     * <p>El índice {@code cima} señala el elemento superior y se inicializa en
     * {@code -1}, valor que representa una pila vacía:</p>
     * <ul>
     *   <li>{@code isEmpty} &rarr; {@code cima == -1}</li>
     *   <li>{@code isFull} &rarr; {@code cima == datos.length - 1}</li>
     * </ul>
     *
     * <p>Solo se almacena el arreglo interno y el índice de cima; no se usan
     * colecciones ni otras estructuras.</p>
     */
    private static class PilaCaracteres {

        /** Arreglo que almacena los caracteres de la pila. */
        private final char[] datos;

        /** Índice de la cima; {@code -1} indica pila vacía. */
        private int cima;

        /**
         * Construye una pila vacía con la capacidad indicada.
         *
         * @param capacidad número máximo de elementos (debe ser &gt; 0)
         * @throws IllegalArgumentException si {@code capacidad} es menor o igual a cero
         */
        PilaCaracteres(int capacidad) {
            // Caso límite: una capacidad no positiva no tiene sentido.
            if (capacidad <= 0) {
                throw new IllegalArgumentException(
                        "La capacidad debe ser mayor que cero. Valor recibido: " + capacidad);
            }
            this.datos = new char[capacidad];
            this.cima = -1; // Pila vacía.
        }

        /**
         * Agrega {@code caracter} en la cima de la pila.
         *
         * @param caracter carácter a apilar
         * @throws IllegalStateException si la pila está llena (no debería ocurrir,
         *                               ya que la capacidad iguala la longitud de la expresión)
         */
        void push(char caracter) {
            if (isFull()) {
                throw new IllegalStateException(
                        "No se puede apilar: la pila está llena (capacidad = " + datos.length + ").");
            }
            cima++;
            datos[cima] = caracter;
        }

        /**
         * Elimina y devuelve el carácter de la cima.
         *
         * @return el carácter que estaba en la cima
         * @throws IllegalStateException si la pila está vacía (underflow)
         */
        char pop() {
            if (isEmpty()) {
                throw new IllegalStateException("No se puede desapilar: la pila está vacía.");
            }
            char caracterCima = datos[cima];
            cima--; // El valor queda lógicamente fuera de la pila.
            return caracterCima;
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
    }
}

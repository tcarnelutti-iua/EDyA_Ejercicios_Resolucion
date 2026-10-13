/**
 * Pila de enteros de tamaño fijo implementada sobre un arreglo.
 *
 * <p>La capacidad se define al construir la pila y <b>no</b> se redimensiona
 * automáticamente: cuando el arreglo se llena, {@link #push(int)} informa el
 * error sin modificar el contenido.</p>
 *
 * <p>El índice {@code top} señala la cima de la pila y se inicializa en
 * {@code -1}, valor que representa una pila vacía:</p>
 * <ul>
 *   <li>{@code isEmpty} &rarr; {@code top == -1}</li>
 *   <li>{@code isFull} &rarr; {@code top == capacidad - 1}</li>
 *   <li>{@code size} &rarr; {@code top + 1}</li>
 * </ul>
 *
 * <p>No se utiliza ninguna otra estructura de datos para almacenar los
 * elementos: solo el arreglo interno y el índice de cima.</p>
 *
 * @author Agente Desarrollador
 */
public class PilaEnteros {

    /** Arreglo que almacena los elementos de la pila. */
    private final int[] datos;

    /** Índice de la cima; {@code -1} indica pila vacía. */
    private int top;

    /**
     * Construye una pila vacía con la capacidad indicada.
     *
     * @param capacidad número máximo de elementos que podrá almacenar (debe ser &gt; 0)
     * @throws IllegalArgumentException si {@code capacidad} es menor o igual a cero
     */
    public PilaEnteros(int capacidad) {
        // Validación de caso límite: una capacidad no positiva no tiene sentido.
        if (capacidad <= 0) {
            throw new IllegalArgumentException(
                    "La capacidad debe ser mayor que cero. Valor recibido: " + capacidad);
        }
        this.datos = new int[capacidad];
        this.top = -1; // Pila vacía.
    }

    /**
     * Agrega {@code valor} en la cima de la pila.
     *
     * @param valor entero a insertar
     * @throws IllegalStateException si la pila está llena (overflow); la pila
     *                               permanece intacta
     */
    public void push(int valor) {
        // Comprobación previa: si la cima ya está en el último índice, no hay espacio.
        if (isFull()) {
            throw new IllegalStateException(
                    "No se puede apilar: la pila está llena (capacidad = " + datos.length + ").");
        }
        // Hay espacio: se incrementa la cima y se guarda el valor.
        top++;
        datos[top] = valor;
    }

    /**
     * Elimina y devuelve el elemento de la cima.
     *
     * @return el elemento que estaba en la cima
     * @throws IllegalStateException si la pila está vacía (underflow)
     */
    public int pop() {
        // No se puede desapilar de una pila vacía.
        if (isEmpty()) {
            throw new IllegalStateException(
                    "No se puede desapilar: la pila está vacía.");
        }
        int valorCima = datos[top];
        top--; // Se reduce la cima; el valor queda lógicamente fuera de la pila.
        return valorCima;
    }

    /**
     * Devuelve el elemento de la cima sin eliminarlo.
     *
     * @return el elemento que está en la cima
     * @throws IllegalStateException si la pila está vacía
     */
    public int peek() {
        // No hay cima que consultar en una pila vacía.
        if (isEmpty()) {
            throw new IllegalStateException(
                    "No se puede consultar la cima: la pila está vacía.");
        }
        return datos[top];
    }

    /**
     * Indica si la pila está vacía.
     *
     * @return {@code true} si no hay elementos ({@code top == -1})
     */
    public boolean isEmpty() {
        return top == -1;
    }

    /**
     * Indica si la pila está llena.
     *
     * @return {@code true} si la cima alcanzó el último índice del arreglo
     */
    public boolean isFull() {
        return top == datos.length - 1;
    }

    /**
     * Devuelve la cantidad de elementos almacenados.
     *
     * @return el número de elementos, calculado como {@code top + 1}
     */
    public int size() {
        return top + 1;
    }
}

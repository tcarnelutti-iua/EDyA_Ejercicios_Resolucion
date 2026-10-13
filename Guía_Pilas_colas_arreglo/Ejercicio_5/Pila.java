public class Pila<T> {
    
    private static final String MENSAJE_PILA_VACIA = "La pila está vacía";
    private static final String MENSAJE_PILA_LLENA = "La pila está llena";
    private static final String MENSAJE_CAPACIDAD_INVALIDA = "La capacidad máxima debe ser positiva";
    
    private final Object[] elementos;
    private int tope; // Índice del elemento superior. -1 si la pila está vacía.
    
    /**
     * Constructor que crea una pila con una capacidad máxima determinada.
     * 
     * @param capacidadMaxima La capacidad máxima de la pila. Debe ser mayor a 0.
     * @throws IllegalArgumentException Si la capacidad máxima es menor o igual a 0.
     */
    public Pila(int capacidadMaxima) {
        if (capacidadMaxima <= 0) {
            throw new IllegalArgumentException(MENSAJE_CAPACIDAD_INVALIDA);
        }
        this.elementos = new Object[capacidadMaxima];
        this.tope = -1;
    }
    
    /**
     * Agrega un elemento en la cima de la pila (operación LIFO).
     * No se redimensiona el arreglo interno.
     * 
     * @param elemento El elemento a apilar.
     * @throws IllegalStateException Si la pila está llena.
     */
    public void push(T elemento) {
        if (estaLlena()) {
            throw new IllegalStateException(MENSAJE_PILA_LLENA);
        }
        tope++;
        elementos[tope] = elemento;
    }
    
    /**
     * Elimina y devuelve el elemento superior de la pila (operación LIFO).
     * Al desapilar, se limpia la posición liberada para evitar retención de referencias.
     * 
     * @return El elemento superior de la pila.
     * @throws IllegalStateException Si la pila está vacía.
     */
    @SuppressWarnings("unchecked")
    public T pop() {
        if (estaVacia()) {
            throw new IllegalStateException(MENSAJE_PILA_VACIA);
        }
        T elementoSuperior = (T) elementos[tope];
        // Limpia la posición para evitar retener referencias innecesarias
        elementos[tope] = null;
        tope--;
        return elementoSuperior;
    }
    
    /**
     * Devuelve el elemento superior de la pila sin eliminarlo.
     * 
     * @return El elemento superior de la pila.
     * @throws IllegalStateException Si la pila está vacía.
     */
    @SuppressWarnings("unchecked")
    public T peek() {
        if (estaVacia()) {
            throw new IllegalStateException(MENSAJE_PILA_VACIA);
        }
        return (T) elementos[tope];
    }
    
    /**
     * Verifica si la pila está vacía.
     * 
     * @return true si la pila no contiene elementos, false en caso contrario.
     */
    public boolean estaVacia() {
        return tope < 0;
    }
    
    /**
     * Verifica si la pila está llena.
     * 
     * @return true si la pila alcanzó su capacidad máxima, false en caso contrario.
     */
    public boolean estaLlena() {
        return tope == elementos.length - 1;
    }
    
    /**
     * Verifica si la pila está vacía (método con nombre alternativo para mayor claridad).
     * 
     * @return true si la pila está vacía, false en caso contrario.
     */
    public boolean isEmpty() {
        return estaVacia();
    }
    
    /**
     * Obtiene la cantidad de elementos actualmente en la pila.
     * 
     * @return El número de elementos en la pila.
     */
    public int size() {
        return tope + 1;
    }
    
    /**
     * Obtiene la capacidad máxima de la pila.
     * 
     * @return La capacidad máxima.
     */
    public int capacidadMaxima() {
        return elementos.length;
    }
    
    /**
     * Clase simple propia para demostrar el uso de la pila genérica.
     */
    static class Persona {
        private final String nombre;
        private final int edad;
        
        public Persona(String nombre, int edad) {
            this.nombre = nombre;
            this.edad = edad;
        }
        
        public String getNombre() {
            return nombre;
        }
        
        public int getEdad() {
            return edad;
        }
        
        @Override
        public String toString() {
            return "Persona{nombre='" + nombre + "', edad=" + edad + "}";
        }
    }
    
    public static void main(String[] args) {
        System.out.println("=== DEMOSTRACIÓN DE PILA GENÉRICA Pila<T> ===\n");
        
        // Demostración con Pila<Integer>
        System.out.println("1. Pila<Integer> (usando tipo envoltorio)");
        Pila<Integer> pilaEnteros = new Pila<>(3);
        System.out.println("  - Capacidad máxima: " + pilaEnteros.capacidadMaxima());
        System.out.println("  - ¿Vacía? " + pilaEnteros.isEmpty() + " | Tamaño: " + pilaEnteros.size());
        
        System.out.println("  - Apilando 10, 20, 30...");
        pilaEnteros.push(10); // Autoboxing: int -> Integer
        pilaEnteros.push(20);
        pilaEnteros.push(30);
        System.out.println("  - ¿Vacía? " + pilaEnteros.isEmpty() + " | ¿Llena? " + pilaEnteros.estaLlena() + " | Tamaño: " + pilaEnteros.size());
        System.out.println("  - Tope (peek): " + pilaEnteros.peek());
        System.out.println("  - Desapilando: " + pilaEnteros.pop());
        System.out.println("  - Tope después de pop: " + pilaEnteros.peek() + " | Tamaño: " + pilaEnteros.size());
        System.out.println();
        
        // Demostración con Pila<String>
        System.out.println("2. Pila<String>");
        Pila<String> pilaCadenas = new Pila<>(2);
        System.out.println("  - Apilando \"Hola\" y \"Mundo\"...");
        pilaCadenas.push("Hola");
        pilaCadenas.push("Mundo");
        System.out.println("  - ¿Llena? " + pilaCadenas.estaLlena() + " | Tamaño: " + pilaCadenas.size());
        System.out.println("  - Tope: " + pilaCadenas.peek());
        System.out.println("  - Desapilando en orden LIFO:");
        System.out.println("      " + pilaCadenas.pop());
        System.out.println("      " + pilaCadenas.pop());
        System.out.println("  - ¿Vacía después de desapilar? " + pilaCadenas.isEmpty());
        System.out.println();
        
        // Demostración con clase propia Persona
        System.out.println("3. Pila<Persona> (clase sencilla propia)");
        Pila<Persona> pilaPersonas = new Pila<>(3);
        System.out.println("  - Apilando personas...");
        pilaPersonas.push(new Persona("Ana", 25));
        pilaPersonas.push(new Persona("Luis", 30));
        pilaPersonas.push(new Persona("María", 28));
        System.out.println("  - Tamaño: " + pilaPersonas.size() + " | Tope: " + pilaPersonas.peek());
        System.out.println("  - Desapilando: " + pilaPersonas.pop());
        System.out.println("  - Tope actual: " + pilaPersonas.peek());
        System.out.println("  - Quedan " + pilaPersonas.size() + " elemento(s) en la pila");
        System.out.println("\n=== Demostración finalizada ===");
    }
}
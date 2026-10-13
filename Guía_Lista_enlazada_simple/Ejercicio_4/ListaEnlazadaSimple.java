/*
Prompt utilizado:
Partiendo de la lista enlazada simple de enteros del Ejercicio 3, agregar el método
void insertarEnPosicion(int dato, int posicion). La posición se cuenta desde cero y
las posiciones válidas van de 0 a size, inclusive. Debe permitir insertar al inicio,
en el medio y al final; si posicion < 0 o posicion > size, debe lanzar
IndexOutOfBoundsException.

Para insertar en el medio, ubicar el nodo anterior a la posición y actualizar las
referencias en este orden:
    nuevo.setSiguiente(actual.getSiguiente());
    actual.setSiguiente(nuevo);

Se conservan todas las operaciones existentes (insertarAlInicio, insertarAlFinal,
buscar, obtener, estaVacia, getSize, imprimir) y solo se agrega la inserción por
posición, junto con pruebas para lista vacía, inserciones al inicio, en el medio y al
final, y posiciones inválidas.

¿Por qué primero hay que conectar el nuevo nodo con el resto de la lista?
Porque al insertar en el medio necesitamos que el nuevo nodo "se apropie" de la
referencia al sucesor ANTES de que el nodo anterior deje de apuntar a ese sucesor.
El enlace actual.siguiente es la ÚNICA vía de acceso al resto de la lista que seguirá
después del nuevo nodo. Si modificáramos primero el enlace de 'actual'
(actual.siguiente = nuevo), perderíamos la referencia al nodo que seguía: la dirección
del sucesor original quedaría sobreescrita y ya no habría forma de recuperarla desde
'actual'. Entonces:
  - Si después hiciéramos nuevo.siguiente = actual.siguiente, como actual.siguiente ya
    apunta a 'nuevo', el nuevo nodo quedaría apuntándose a sí mismo, generando un ciclo
    con lista rota (y un bucle infinito al recorrerla).
  - Si simplemente dejáramos el enlace sin asignar o mal, perderíamos todos los nodos
    que venían después del punto de inserción, dejándolos inaccesibles.
Por eso el orden correcto es: primero nuevo.siguiente = actual.siguiente (guardar/heredar
el acceso al resto de la lista) y recién después actual.siguiente = nuevo (cortar y
reenlazar hacia el nuevo nodo). Así nunca se pierde ninguna referencia.
*/

/**
 * Lista enlazada simple de números enteros implementada desde cero, con búsqueda
 * secuencial, acceso por posición e inserción por posición.
 *
 * <p>No se utiliza ninguna colección de la API de Java (ArrayList, LinkedList, etc.)
 * para almacenar los datos: cada elemento vive en un {@link Nodo} propio enlazado
 * mediante referencias.</p>
 *
 * <p>La clase mantiene dos invariantes fundamentales:</p>
 * <ul>
 *   <li>{@code head} apunta al primer nodo, o es {@code null} si la lista está vacía.</li>
 *   <li>{@code size} refleja siempre la cantidad real de nodos de la lista.</li>
 * </ul>
 */
public class ListaEnlazadaSimple {

    /**
     * Nodo de la lista enlazada simple.
     *
     * <p>Es una clase interna estática porque no necesita acceder al estado de la
     * lista que la contiene; cada nodo solo conoce su dato y su sucesor.</p>
     */
    private static final class Nodo {
        /** Valor entero almacenado en este nodo. */
        private final int dato;
        /** Referencia al siguiente nodo; {@code null} indica fin de la lista. */
        private Nodo siguiente;

        /**
         * Construye un nodo con el dato indicado y sin sucesor.
         *
         * @param dato valor entero a almacenar
         */
        private Nodo(int dato) {
            this.dato = dato;
            this.siguiente = null;
        }
    }

    /** Referencia al primer nodo de la lista; {@code null} si está vacía. */
    private Nodo head;

    /** Cantidad de nodos almacenados en la lista. Empieza en cero. */
    private int size;

    /**
     * Crea una lista enlazada simple vacía.
     */
    public ListaEnlazadaSimple() {
        this.head = null;
        this.size = 0;
    }

    /**
     * Inserta un nuevo dato al inicio de la lista.
     *
     * <p>El nuevo nodo pasa a ser la cabeza y queda enlazado antes del que era el
     * primer nodo. Si la lista estaba vacía, {@code head} era {@code null}, por lo
     * que el nuevo nodo también queda como único elemento.</p>
     *
     * @param dato valor entero a insertar (puede ser cero o negativo)
     */
    public void insertarAlInicio(int dato) {
        Nodo nuevo = new Nodo(dato);
        // El nuevo nodo apunta al que era el primer nodo (puede ser null si estaba vacía).
        nuevo.siguiente = this.head;
        // Ahora la cabeza es el nuevo nodo.
        this.head = nuevo;
        // Se agregó exactamente un nodo.
        this.size++;
    }

    /**
     * Inserta un nuevo dato al final de la lista.
     *
     * <p>Si la lista está vacía, el nuevo nodo se convierte en la cabeza. Si no,
     * se recorre hasta el último nodo y se enlaza el nuevo nodo después de él.</p>
     *
     * @param dato valor entero a insertar (puede ser cero o negativo)
     */
    public void insertarAlFinal(int dato) {
        Nodo nuevo = new Nodo(dato);

        if (this.head == null) {
            // Caso lista vacía: el nuevo nodo es a la vez el primero.
            this.head = nuevo;
        } else {
            // Recorrer hasta el último nodo (aquel cuyo 'siguiente' es null).
            Nodo actual = this.head;
            while (actual.siguiente != null) {
                actual = actual.siguiente;
            }
            // Enlazar el nuevo nodo después del último.
            actual.siguiente = nuevo;
        }

        // Se agregó exactamente un nodo, tanto en el caso vacío como en el no vacío.
        this.size++;
    }

    /**
     * Inserta un nuevo dato en la posición indicada (contando desde cero).
     *
     * <p>Las posiciones válidas van de {@code 0} a {@code size}, ambas inclusive:</p>
     * <ul>
     *   <li>{@code posicion == 0}: inserción al inicio (el nuevo nodo pasa a ser
     *       {@code head}).</li>
     *   <li>{@code 0 < posicion < size}: inserción en el medio.</li>
     *   <li>{@code posicion == size}: inserción al final.</li>
     * </ul>
     *
     * <p>Como en una lista enlazada simple no hay acceso posicional directo, para
     * insertar en el medio o al final se recorre la lista desde {@code head} hasta
     * ubicar el nodo ANTERIOR a la posición de inserción (el que ocupa
     * {@code posicion - 1}). Ese nodo es {@code actual}, y su sucesor
     * ({@code actual.siguiente}) pasará a ser el sucesor del nuevo nodo.</p>
     *
     * <p>El orden de actualización de referencias es clave:</p>
     * <pre>
     *   nuevo.siguiente = actual.siguiente;  // 1) el nuevo hereda el resto de la lista
     *   actual.siguiente = nuevo;            // 2) recién ahora el anterior apunta al nuevo
     * </pre>
     * <p>Primero se conecta el nuevo nodo con el resto de la lista porque
     * {@code actual.siguiente} es la única referencia que permite llegar a los nodos
     * posteriores. Si se modificara primero {@code actual.siguiente = nuevo}, esa
     * referencia se sobreescribiría y se perdería el acceso a los nodos que seguían;
     * un posterior {@code nuevo.siguiente = actual.siguiente} haría que el nuevo nodo
     * se apunte a sí mismo, creando un ciclo y dejando elementos inaccesibles. Al
     * enlazar primero el nuevo nodo, ninguna referencia se pierde.</p>
     *
     * <p>El {@code size} se incrementa exactamente una vez por inserción realizada,
     * en todos los casos válidos.</p>
     *
     * <p>Complejidad temporal: O(k) para insertar en la posición k; O(n) en el peor
     * caso (posición final). Complejidad espacial: O(1).</p>
     *
     * @param dato     valor entero a insertar (puede ser cero o negativo)
     * @param posicion índice donde insertar (base cero); rango válido {@code [0, size]}
     * @throws IndexOutOfBoundsException si {@code posicion < 0} o {@code posicion > size}
     */
    public void insertarEnPosicion(int dato, int posicion) {
        // Validación previa: el rango permitido es [0, size] (ambos inclusive).
        // Se compara contra 'size' antes de recorrer, así no hay que tocar la lista
        // para descubrir que la posición es inválida.
        if (posicion < 0 || posicion > this.size) {
            throw new IndexOutOfBoundsException(
                    "Posición fuera de rango: " + posicion
                            + " (rango válido: 0.." + this.size + ")");
        }

        Nodo nuevo = new Nodo(dato);

        if (posicion == 0) {
            // Inserción al inicio: la cabeza pasa a ser el nuevo nodo.
            // Primero se enlaza el nuevo con el que era el primer nodo y luego se
            // mueve 'head'; el nuevo nodo queda antes que el resto.
            nuevo.siguiente = this.head;
            this.head = nuevo;
        } else {
            // Ubicar el nodo ANTERIOR al punto de inserción, es decir el que ocupa
            // la posición (posicion - 1). Como posicion >= 1, el recorrido siempre
            // termina sobre un nodo real (nunca queda en null).
            Nodo actual = this.head;
            for (int i = 0; i < posicion - 1; i++) {
                actual = actual.siguiente;
            }

            // Orden correcto: el nuevo nodo se conecta primero con el resto de la
            // lista (hereda el sucesor de 'actual') y recién después 'actual' apunta
            // al nuevo. Invertir estas dos líneas perdería el resto de la lista o
            // crearía un ciclo.
            nuevo.siguiente = actual.siguiente; // 1) nuevo hereda el resto
            actual.siguiente = nuevo;           // 2) el anterior enlaza al nuevo
        }

        // Se agregó exactamente un nodo, en todos los casos válidos.
        this.size++;
    }

    /**
     * Busca un valor dentro de la lista mediante un recorrido secuencial.
     *
     * <p>No existe acceso posicional directo: como los nodos están conectados
     * únicamente por referencias al siguiente, la única forma de llegar a un
     * elemento es partir de {@code head} y avanzar nodo por nodo. Se recorre la
     * lista completa y, en cuanto se encuentra un nodo cuyo dato coincide, se
     * devuelve {@code true}. Si el recorrido termina en {@code null} (fin de la
     * lista) sin coincidencia, se devuelve {@code false}.</p>
     *
     * <p>Complejidad temporal: O(n) en el peor caso (dato ausente o al final);
     * O(1) en el mejor caso (dato en {@code head}). Complejidad espacial: O(1).</p>
     *
     * @param dato valor entero a buscar (puede ser cero o negativo)
     * @return {@code true} si el dato está presente, {@code false} en caso contrario
     */
    public boolean buscar(int dato) {
        Nodo actual = this.head;
        // Avanzar por la lista mientras haya nodos por visitar.
        while (actual != null) {
            // Comparar el dato del nodo actual con el buscado.
            if (actual.dato == dato) {
                return true;
            }
            // Pasar al siguiente nodo siguiendo la referencia.
            actual = actual.siguiente;
        }
        // Se llegó a null sin encontrar el dato: no está en la lista.
        return false;
    }

    /**
     * Devuelve el dato almacenado en la posición indicada (contando desde cero).
     *
     * <p>Aunque el método reciba una posición, la lista enlazada simple NO permite
     * acceder directamente al elemento como ocurre con un arreglo. Los nodos no
     * están en posiciones contiguas de memoria y no existe una dirección base para
     * calcular un desplazamiento: cada nodo solo conoce su dato y la referencia al
     * siguiente. Por eso es obligatorio comenzar en {@code head} y avanzar nodo por
     * nodo hasta haber dado exactamente {@code posicion} pasos.</p>
     *
     * @param posicion índice del elemento a obtener (base cero)
     * @return el dato entero almacenado en la posición indicada
     * @throws IndexOutOfBoundsException si {@code posicion < 0} o
     *         {@code posicion >= size}
     */
    public int obtener(int posicion) {
        // Validación previa: la posición debe pertenecer al rango [0, size - 1].
        if (posicion < 0 || posicion >= this.size) {
            throw new IndexOutOfBoundsException(
                    "Posición fuera de rango: " + posicion + " (tamaño = " + this.size + ")");
        }

        // Comenzar siempre en la cabeza: es el único punto de entrada a la lista.
        Nodo actual = this.head;

        // Avanzar exactamente 'posicion' nodos siguiendo las referencias.
        for (int i = 0; i < posicion; i++) {
            actual = actual.siguiente;
        }

        // Devolver el dato del nodo alcanzado.
        return actual.dato;
    }

    /**
     * Indica si la lista no contiene elementos.
     *
     * @return {@code true} si la lista está vacía, {@code false} en caso contrario
     */
    public boolean estaVacia() {
        return this.head == null;
    }

    /**
     * Devuelve la cantidad de elementos almacenados en la lista.
     *
     * @return el tamaño actual de la lista (cero si está vacía)
     */
    public int getSize() {
        return this.size;
    }

    /**
     * Imprime en la consola los elementos de la lista en orden, desde {@code head}
     * hasta el último nodo.
     *
     * <p>Si la lista está vacía se informa explícitamente en lugar de no imprimir
     * nada, para que el resultado sea claro.</p>
     */
    public void imprimir() {
        if (estaVacia()) {
            System.out.println("Lista vacía []");
            return;
        }

        StringBuilder salida = new StringBuilder("Lista: [");
        Nodo actual = this.head;
        while (actual != null) {
            salida.append(actual.dato);
            if (actual.siguiente != null) {
                salida.append(" -> ");
            }
            actual = actual.siguiente;
        }
        salida.append("]  (tamaño = ").append(this.size).append(")");
        System.out.println(salida);
    }

    /**
     * Método de prueba que ejercita {@code insertarEnPosicion} en todos los casos
     * relevantes: lista vacía, inserción al inicio, en el medio y al final, y
     * posiciones inválidas. También repite los casos borde de los ejercicios
     * anteriores para confirmar que las operaciones previas siguen funcionando.
     *
     * @param args no se utilizan
     */
    public static void main(String[] args) {
        // ---- Caso 1: lista vacía ----
        ListaEnlazadaSimple lista = new ListaEnlazadaSimple();
        System.out.println("== Caso 1: lista vacía ==");
        System.out.println("estaVacia() = " + lista.estaVacia());
        System.out.println("getSize()   = " + lista.getSize());
        lista.imprimir();

        // ---- Caso 2: operaciones existentes conservadas ----
        System.out.println("\n== Caso 2: insertarAlInicio / insertarAlFinal ==");
        lista.insertarAlInicio(10);
        lista.insertarAlInicio(-5);
        lista.insertarAlInicio(0);
        lista.insertarAlFinal(7);
        lista.insertarAlFinal(-100);
        lista.imprimir();          // esperado: [0 -> -5 -> 10 -> 7 -> -100]
        System.out.println("getSize()   = " + lista.getSize()); // esperado: 5

        // ---- Caso 3: insertar en el inicio (posicion 0) ----
        System.out.println("\n== Caso 3: insertarEnPosicion(99, 0) -> inicio ==");
        lista.insertarEnPosicion(99, 0);
        lista.imprimir();          // esperado: [99 -> 0 -> -5 -> 10 -> 7 -> -100]
        verificar(lista.obtener(0) == 99 && lista.getSize() == 6,
                "Inserción al inicio: head = 99 y size = 6");

        // ---- Caso 4: insertar en el medio (posicion 3) ----
        // Lista actual: [99 -> 0 -> -5 -> 10 -> 7 -> -100]
        System.out.println("\n== Caso 4: insertarEnPosicion(55, 3) -> medio ==");
        lista.insertarEnPosicion(55, 3);
        lista.imprimir();          // esperado: [99 -> 0 -> -5 -> 55 -> 10 -> 7 -> -100]
        verificar(lista.obtener(2) == -5
                        && lista.obtener(3) == 55
                        && lista.obtener(4) == 10
                        && lista.getSize() == 7,
                "Inserción en el medio: nuevo dato entre -5 y 10, size = 7");

        // ---- Caso 5: insertar al final (posicion == size) ----
        // Lista actual: [99 -> 0 -> -5 -> 55 -> 10 -> 7 -> -100], size = 7
        System.out.println("\n== Caso 5: insertarEnPosicion(123, size) -> final ==");
        lista.insertarEnPosicion(123, lista.getSize());
        lista.imprimir();          // esperado: [... -> -100 -> 123]
        verificar(lista.obtener(lista.getSize() - 1) == 123 && lista.getSize() == 8,
                "Inserción al final: último = 123 y size = 8");

        // ---- Caso 6: insertar en posición 0 sobre lista vacía ----
        System.out.println("\n== Caso 6: insertarEnPosicion en lista vacía ==");
        ListaEnlazadaSimple vacia = new ListaEnlazadaSimple();
        vacia.insertarEnPosicion(42, 0); // única posición válida en lista vacía
        vacia.imprimir();          // esperado: [42]
        verificar(vacia.obtener(0) == 42 && vacia.getSize() == 1,
                "Lista vacía: insertar en 0 crea el único nodo, size = 1");

        // ---- Caso 7: posiciones inválidas ----
        System.out.println("\n== Caso 7: posiciones inválidas ==");
        verificar(lanzaExcepcion(() -> lista.insertarEnPosicion(1, -1)),
                "insertarEnPosicion(1, -1)  -> lanza IndexOutOfBoundsException");
        verificar(lanzaExcepcion(() -> lista.insertarEnPosicion(1, lista.getSize() + 1)),
                "insertarEnPosicion(1, size+1) -> lanza IndexOutOfBoundsException");
        verificar(lanzaExcepcion(() -> vacia.insertarEnPosicion(1, 2)),
                "insertarEnPosicion(1, 2) en lista vacía -> lanza IndexOutOfBoundsException");

        // Tras los intentos inválidos, el tamaño no debe haber cambiado.
        verificar(lista.getSize() == 8, "El tamaño no cambia ante posiciones inválidas");

        // ---- Caso 8: size se incrementa exactamente una vez por inserción ----
        System.out.println("\n== Caso 8: control de size ==");
        ListaEnlazadaSimple control = new ListaEnlazadaSimple();
        control.insertarEnPosicion(1, 0); // [1]
        control.insertarEnPosicion(2, 1); // [1, 2]
        control.insertarEnPosicion(3, 1); // [1, 3, 2]
        control.insertarEnPosicion(4, 0); // [4, 1, 3, 2]
        control.imprimir();               // esperado: [4 -> 1 -> 3 -> 2]
        verificar(control.getSize() == 4
                        && control.obtener(0) == 4
                        && control.obtener(1) == 1
                        && control.obtener(2) == 3
                        && control.obtener(3) == 2,
                "Cuatro inserciones -> size = 4 y orden correcto");

        // ---- Caso 9: las operaciones previas siguen funcionando ----
        System.out.println("\n== Caso 9: operaciones previas ==");
        verificar(!lista.estaVacia() && lista.buscar(55) && !lista.buscar(999),
                "estaVacia(), buscar() y getSize() conservados");
    }

    /**
     * Utilidad sencilla para mostrar el resultado de una verificación booleana.
     *
     * @param condicion resultado de la comprobación
     * @param descripcion texto descriptivo de lo verificado
     */
    private static void verificar(boolean condicion, String descripcion) {
        System.out.println((condicion ? "[OK]   " : "[FALLA] ") + descripcion);
    }

    /**
     * Ejecuta una acción que se espera que lance {@link IndexOutOfBoundsException}
     * y devuelve {@code true} solo si efectivamente la lanza.
     *
     * @param accion acción a ejecutar
     * @return {@code true} si se capturó la excepción esperada, {@code false} en caso contrario
     */
    private static boolean lanzaExcepcion(Runnable accion) {
        try {
            accion.run();
            return false; // No lanzó excepción: la verificación falla.
        } catch (IndexOutOfBoundsException e) {
            return true;  // Se lanzó la excepción esperada.
        }
    }
}

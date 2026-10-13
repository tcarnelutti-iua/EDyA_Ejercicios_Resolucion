/*
Prompt utilizado:
Partiendo de la lista enlazada simple de enteros de los ejercicios anteriores, agregar el
método void modificar(int posicion, int nuevoDato). Las posiciones se cuentan desde cero.
Antes de recorrer la lista, validar que posicion >= 0 y posicion < size; si no se cumple,
lanzar IndexOutOfBoundsException.

Si la posición es válida, recorrer desde head hasta el nodo correspondiente y reemplazar el
dato almacenado por nuevoDato.

Diferencia entre modificar el dato del nodo y modificar su referencia al siguiente:
- Modificar el DATO (este ejercicio) cambia únicamente el valor guardado dentro de un nodo
  ya existente, por ejemplo nodo.dato = nuevoDato. La cantidad de nodos, el orden y todos los
  enlaces (quién apunta a quién) permanecen intactos: sigue siendo exactamente la misma
  estructura de lista, solo cambió el contenido de una casilla. El size no varía.
- Modificar la REFERENCIA al siguiente (nodo.siguiente = otro) cambia la ESTRUCTURA: altera el
  recorrido, puede insertar/saltar/reordenar nodos, perder el resto de la lista o crear ciclos.
  Es lo que hacen insertar y eliminar. Aquí NO se toca ningún enlace, solo el valor.

Nota de implementación: como en el Ejercicio 5 el campo 'dato' del Nodo era 'final' (inmutable),
para permitir la modificación fue necesario quitar 'final'. El dato de un nodo pasa a ser
modificable, mientras que la lista enlazada sigue siendo la misma clase con head/size.

Se conservan todas las operaciones existentes (insertarAlInicio, insertarAlFinal,
insertarEnPosicion, eliminar, buscar, obtener, estaVacia, getSize, imprimir) y solo se agrega
modificar, junto con pruebas para: modificar el primer nodo, uno del medio, el último, posición
inválida negativa, posición inválida >= size, lista vacía y verificación de que el tamaño
permanece igual tras cada modificación.
*/

/**
 * Lista enlazada simple de números enteros implementada desde cero, con búsqueda
 * secuencial, acceso por posición, inserción por posición, eliminación de la primera
 * aparición de un valor y modificación del dato de un nodo por posición.
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
        /**
         * Valor entero almacenado en este nodo.
         *
         * <p>No es {@code final}: a partir de este ejercicio {@code modificar} puede
         * reemplazar el valor guardado en un nodo ya existente sin alterar su lugar ni
         * sus enlaces.</p>
         */
        private int dato;
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
     * Reemplaza el dato almacenado en la posición indicada (contando desde cero).
     *
     * <p>Primero valida el rango contra {@code size} <b>antes de recorrer</b>: la
     * posición debe cumplir {@code 0 <= posicion < size}. Si no, lanza
     * {@link IndexOutOfBoundsException} sin tocar la lista.</p>
     *
     * <p>Si la posición es válida, recorre desde {@code head} avanzando
     * {@code posicion} enlaces hasta dar con el nodo correspondiente y sustituye su
     * dato: {@code actual.dato = nuevoDato}.</p>
     *
     * <p><b>Diferencia clave:</b> este método cambia el VALOR de un nodo existente,
     * NO su referencia al siguiente. Por eso la cantidad de nodos, el orden y todos
     * los enlaces se mantienen exactamente iguales: solo cambia el contenido de una
     * casilla. En cambio, alterar {@code actual.siguiente} modificaría la estructura
     * (es lo que hace insertar o eliminar). En consecuencia, {@code size} nunca cambia
     * al llamar a {@code modificar}.</p>
     *
     * <p>Complejidad temporal: O(k) para modificar la posición k; O(n) en el peor caso
     * (última posición). Complejidad espacial: O(1).</p>
     *
     * @param posicion  índice del elemento a modificar (base cero)
     * @param nuevoDato valor entero que reemplazará al actual (puede ser cero o negativo)
     * @throws IndexOutOfBoundsException si {@code posicion < 0} o
     *         {@code posicion >= size}
     */
    public void modificar(int posicion, int nuevoDato) {
        // Validación previa: la posición debe pertenecer al rango [0, size - 1].
        // Se comprueba contra 'size' antes de recorrer; así, ante un índice inválido,
        // no se toca la estructura ni se producen recorridos innecesarios.
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

        // Reemplazar SOLO el dato del nodo alcanzado. No se modifican los enlaces
        // (actual.siguiente), por lo que la estructura de la lista no cambia y el
        // tamaño permanece intacto.
        actual.dato = nuevoDato;
    }

    /**
     * Elimina la PRIMERA aparición del valor indicado en la lista.
     *
     * <p>Se recorre la lista con dos referencias:</p>
     * <ul>
     *   <li>{@code actual}: el nodo que se está comparando.</li>
     *   <li>{@code anterior}: el nodo inmediatamente previo a {@code actual}
     *       ({@code null} mientras {@code actual} es la cabeza).</li>
     * </ul>
     *
     * <p>Al encontrar la coincidencia se presentan dos situaciones según dónde esté
     * el nodo a eliminar:</p>
     * <ul>
     *   <li><b>Es la cabeza</b> ({@code anterior == null}): basta con mover
     *       {@code head} al siguiente nodo, {@code this.head = actual.siguiente}.</li>
     *   <li><b>Está en el medio o al final</b>: se salta el nodo enlazando el
     *       anterior directamente con el siguiente del eliminado,
     *       {@code anterior.siguiente = actual.siguiente}. Si era el último,
     *       {@code actual.siguiente} es {@code null} y el anterior pasa a ser el
     *       nuevo último.</li>
     * </ul>
     *
     * <p>En ambos casos {@code size} se decrementa exactamente una vez, y solo cuando
     * se elimina un nodo realmente. Si la lista está vacía o el dato no aparece, se
     * devuelve {@code false} sin modificar la estructura ni el tamaño.</p>
     *
     * <p>Al reenlazar las referencias, el nodo eliminado deja de ser alcanzable desde
     * la lista. Java no libera memoria manualmente: si no queda ninguna referencia al
     * nodo, el Garbage Collector podrá recuperarlo automáticamente.</p>
     *
     * <p>Se elimina únicamente la primera aparición: en cuanto se enlaza el salto, el
     * método retorna, de modo que los valores repetidos posteriores permanecen en la
     * lista.</p>
     *
     * <p>Complejidad temporal: O(n) en el peor caso (dato ausente o en el último
     * nodo); O(1) en el mejor caso (dato en la cabeza). Complejidad espacial: O(1).</p>
     *
     * @param dato valor entero a eliminar (puede ser cero o negativo)
     * @return {@code true} si se encontró y eliminó la primera aparición;
     *         {@code false} si la lista está vacía o el dato no existe
     */
    public boolean eliminar(int dato) {
        Nodo actual = this.head;
        Nodo anterior = null;

        // Recorrer la lista llevando siempre el nodo anterior al actual.
        while (actual != null) {
            if (actual.dato == dato) {
                // Coincidencia encontrada: corresponde eliminar 'actual'.
                if (anterior == null) {
                    // El nodo a eliminar es la cabeza: la nueva cabeza es su siguiente.
                    this.head = actual.siguiente;
                } else {
                    // Nodo del medio o del final: el anterior se salta al eliminado
                    // y se enlaza directamente con el siguiente de este.
                    anterior.siguiente = actual.siguiente;
                }

                // El nodo eliminado queda inaccesible desde la lista y podrá ser
                // recuperado por el Garbage Collector si no hay otras referencias.
                this.size--;
                return true;
            }

            // Avanzar manteniendo el rastro del nodo anterior.
            anterior = actual;
            actual = actual.siguiente;
        }

        // Se llegó al final sin encontrar el dato (o la lista estaba vacía).
        return false;
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
     * Método de prueba que ejercita {@code modificar} en todos los casos relevantes:
     * modificación del primer nodo, de un nodo del medio y del último, posición
     * inválida negativa, posición inválida mayor o igual al tamaño, lista vacía, y
     * verificación de que el tamaño permanece igual tras cada modificación. También
     * repite casos borde de los ejercicios anteriores para confirmar que las
     * operaciones previas siguen funcionando.
     *
     * @param args no se utilizan
     */
    public static void main(String[] args) {
        // ---- Caso 1: modificar en lista vacía (debe lanzar excepción) ----
        System.out.println("== Caso 1: modificar en lista vacía ==");
        ListaEnlazadaSimple vacia = new ListaEnlazadaSimple();
        verificar(lanzaIndexOutOfBounds(() -> vacia.modificar(0, 99)),
                "modificar(0, 99) en lista vacía lanza IndexOutOfBoundsException");
        verificar(vacia.getSize() == 0 && vacia.estaVacia(),
                "La lista vacía sigue vacía y con size = 0");

        // ---- Caso 2: operaciones existentes conservadas ----
        System.out.println("\n== Caso 2: insertarAlInicio / insertarAlFinal ==");
        ListaEnlazadaSimple lista = new ListaEnlazadaSimple();
        lista.insertarAlInicio(10);
        lista.insertarAlInicio(-5);
        lista.insertarAlInicio(0);
        lista.insertarAlFinal(7);
        lista.insertarAlFinal(-100);
        lista.imprimir();          // esperado: [0 -> -5 -> 10 -> 7 -> -100]
        System.out.println("getSize()   = " + lista.getSize()); // esperado: 5

        // ---- Caso 3: modificar el primer nodo (head) ----
        System.out.println("\n== Caso 3: modificar el primero (posición 0) ==");
        int sizeAntes = lista.getSize();
        lista.modificar(0, 111);
        lista.imprimir();          // esperado: [111 -> -5 -> 10 -> 7 -> -100]
        verificar(lista.obtener(0) == 111, "El primer dato pasa a ser 111");
        verificar(lista.getSize() == sizeAntes, "El tamaño no cambia al modificar el primero");

        // ---- Caso 4: modificar un nodo del medio ----
        System.out.println("\n== Caso 4: modificar un nodo del medio (posición 2) ==");
        sizeAntes = lista.getSize();
        lista.modificar(2, 222);
        lista.imprimir();          // esperado: [111 -> -5 -> 222 -> 7 -> -100]
        verificar(lista.obtener(0) == 111
                        && lista.obtener(1) == -5
                        && lista.obtener(2) == 222
                        && lista.obtener(3) == 7
                        && lista.obtener(4) == -100,
                "Solo cambió el dato de la posición 2; el orden y los enlaces siguen igual");
        verificar(lista.getSize() == sizeAntes, "El tamaño no cambia al modificar un nodo del medio");

        // ---- Caso 5: modificar el último nodo ----
        System.out.println("\n== Caso 5: modificar el último (posición size-1) ==");
        sizeAntes = lista.getSize();
        lista.modificar(lista.getSize() - 1, -999);
        lista.imprimir();          // esperado: [111 -> -5 -> 222 -> 7 -> -999]
        verificar(lista.obtener(lista.getSize() - 1) == -999,
                "El último dato pasa a ser -999");
        verificar(lista.getSize() == sizeAntes, "El tamaño no cambia al modificar el último");

        // ---- Caso 6: posición negativa inválida ----
        System.out.println("\n== Caso 6: posición negativa inválida ==");
        verificar(lanzaIndexOutOfBounds(() -> lista.modificar(-1, 0)),
                "modificar(-1, 0) lanza IndexOutOfBoundsException");
        lista.imprimir();          // esperado: sin cambios

        // ---- Caso 7: posición >= size inválida ----
        System.out.println("\n== Caso 7: posición >= size inválida ==");
        int sizeActual = lista.getSize();
        verificar(lanzaIndexOutOfBounds(() -> lista.modificar(sizeActual, 0)),
                "modificar(size, 0) lanza IndexOutOfBoundsException");
        verificar(lanzaIndexOutOfBounds(() -> lista.modificar(100, 0)),
                "modificar(100, 0) lanza IndexOutOfBoundsException");
        verificar(lista.getSize() == sizeActual, "El tamaño no cambia tras intentos inválidos");

        // ---- Caso 8: lista de un solo nodo ----
        System.out.println("\n== Caso 8: lista de un solo elemento ==");
        ListaEnlazadaSimple unico = new ListaEnlazadaSimple();
        unico.insertarAlFinal(42);
        unico.modificar(0, 77);
        unico.imprimir();          // esperado: [77]
        verificar(unico.getSize() == 1 && unico.obtener(0) == 77,
                "Se modifica el único elemento y size permanece en 1");
        verificar(lanzaIndexOutOfBounds(() -> unico.modificar(1, 5)),
                "modificar(1, ...) en lista de un elemento lanza excepción");

        // ---- Caso 9: datos repetidos: modificar solo afecta a una posición ----
        System.out.println("\n== Caso 9: datos repetidos ==");
        ListaEnlazadaSimple repetidos = new ListaEnlazadaSimple();
        repetidos.insertarAlFinal(5);
        repetidos.insertarAlFinal(5);
        repetidos.insertarAlFinal(5);
        repetidos.modificar(1, 9);
        repetidos.imprimir();      // esperado: [5 -> 9 -> 5]
        verificar(repetidos.obtener(0) == 5
                        && repetidos.obtener(1) == 9
                        && repetidos.obtener(2) == 5
                        && repetidos.getSize() == 3,
                "Solo cambia el dato de la posición indicada; los demás nodos quedan igual");

        // ---- Caso 10: las operaciones previas siguen funcionando ----
        System.out.println("\n== Caso 10: operaciones previas ==");
        ListaEnlazadaSimple control = new ListaEnlazadaSimple();
        control.insertarEnPosicion(1, 0);  // [1]
        control.insertarEnPosicion(2, 1);  // [1, 2]
        control.insertarEnPosicion(3, 1);  // [1, 3, 2]
        control.insertarEnPosicion(4, 0);  // [4, 1, 3, 2]
        control.modificar(2, 30);          // [4, 1, 30, 2]
        verificar(control.obtener(0) == 4 && control.obtener(2) == 30 && control.getSize() == 4,
                "insertarEnPosicion, modificar y obtener conviven correctamente");
        verificar(control.buscar(30)
                        && control.eliminar(30)
                        && !control.buscar(30)
                        && control.getSize() == 3,
                "modificar/buscar/eliminar conviven correctamente");
    }

    /**
     * Comprueba si la acción indicada lanza una {@link IndexOutOfBoundsException}.
     *
     * <p>Se usa en las pruebas para verificar las validaciones sin que el programa
     * termine de forma abrupta por una excepción no capturada.</p>
     *
     * @param accion bloque de código a ejecutar
     * @return {@code true} si se lanzó {@code IndexOutOfBoundsException};
     *         {@code false} en caso contrario
     */
    private static boolean lanzaIndexOutOfBounds(Runnable accion) {
        try {
            accion.run();
            return false;
        } catch (IndexOutOfBoundsException e) {
            return true;
        }
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
}

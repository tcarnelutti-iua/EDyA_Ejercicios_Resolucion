/*
Prompt utilizado:
Partiendo de la lista enlazada simple de enteros de los ejercicios anteriores, agregar el
método int contarOcurrencias(int dato). El método debe recorrer TODA la lista desde head,
contar cada nodo cuyo dato sea igual al valor buscado y devolver el total.

No se debe cortar el recorrido al encontrar la primera coincidencia: el mismo valor puede
aparecer en varios nodos y hay que contarlos todos. Por ejemplo, en la lista
10 -> 20 -> 10 -> 30 -> 10 -> null, contarOcurrencias(10) debe devolver 3.
Si la lista está vacía o el valor no aparece, debe devolver 0.

¿Por qué hay que recorrer toda la lista y no detenerse en la primera coincidencia?
A diferencia de buscar() o eliminar(), que solo necesitan saber SI el valor existe (y en el
caso de eliminar, actuar sobre una única aparición), contarOcurrencias necesita la CANTIDAD
total de apariciones. Un valor puede almacenarse muchas veces en una lista enlazada; la
primera coincidencia solo aporta 1 al total. Por eso el recorrido debe continuar hasta que
la referencia 'actual' se vuelva null (fin de la lista), incrementando un contador cada vez
que el dato coincide. No se puede usar 'return' dentro del bucle: solo se retorna una vez,
al terminar el recorrido. Además, este método es de solo lectura: no modifica enlaces ni el
dato de ningún nodo, por lo que size permanece intacto.

Se conservan todas las operaciones existentes (insertarAlInicio, insertarAlFinal,
insertarEnPosicion, modificar, eliminar, buscar, obtener, estaVacia, getSize, imprimir) y
solo se agrega contarOcurrencias, junto con pruebas para: varias coincidencias, una sola
coincidencia, ninguna coincidencia, lista vacía, valores válidos como 0 y verificación de
que el tamaño de la lista no cambia tras contar.
*/

/**
 * Lista enlazada simple de números enteros implementada desde cero, con búsqueda
 * secuencial, acceso por posición, inserción por posición, eliminación de la primera
 * aparición de un valor, modificación del dato de un nodo por posición y conteo de
 * ocurrencias de un valor.
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
         * <p>No es {@code final}: {@code modificar} puede reemplazar el valor guardado
         * en un nodo ya existente sin alterar su lugar ni sus enlaces.</p>
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
     * casilla. En consecuencia, {@code size} nunca cambia al llamar a {@code modificar}.</p>
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
     * <p><b>Diferencia con {@link #contarOcurrencias(int)}:</b> {@code buscar} puede
     * cortar en la primera coincidencia porque solo responde SI el valor existe,
     * mientras que {@code contarOcurrencias} debe recorrer toda la lista para
     * responder CUÁNTAS veces aparece.</p>
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
     * Cuenta cuántas veces aparece el valor indicado en la lista.
     *
     * <p>Recorre la lista <b>completa</b> desde {@code head} hasta {@code null},
     * comparando el dato de cada nodo con el buscado y sumando 1 a un contador
     * cada vez que coinciden. El recorrido <b>no se interrumpe</b> en la primera
     * coincidencia: como un mismo valor puede repetirse en varios nodos, es
     * necesario visitarlos todos para obtener el total.</p>
     *
     * <p>El método es de <b>solo lectura</b>: no modifica enlaces ni datos, por lo
     * que la estructura y el tamaño ({@code size}) de la lista quedan intactos.</p>
     *
     * <p>Casos contemplados:</p>
     * <ul>
     *   <li>Lista vacía: el bucle no se ejecuta y se devuelve {@code 0}.</li>
     *   <li>Valor ausente: se recorre toda la lista sin coincidencias y se
     *       devuelve {@code 0}.</li>
     *   <li>Una o varias coincidencias: se devuelve la cantidad total de nodos
     *       cuyo dato es igual al valor buscado.</li>
     *   <li>El valor buscado puede ser cero o negativo.</li>
     * </ul>
     *
     * <p>Complejidad temporal: O(n) siempre, porque hay que visitar todos los nodos.
     * Complejidad espacial: O(1).</p>
     *
     * @param dato valor entero cuyas apariciones se quieren contar (puede ser cero o negativo)
     * @return cantidad de nodos cuyo dato es igual a {@code dato};
     *         {@code 0} si la lista está vacía o el valor no aparece
     */
    public int contarOcurrencias(int dato) {
        int contador = 0;
        Nodo actual = this.head;

        // Recorrer TODA la lista: no se corta al encontrar una coincidencia.
        while (actual != null) {
            if (actual.dato == dato) {
                // Cada coincidencia suma exactamente 1. No se retorna aquí: hay que
                // seguir contando posibles apariciones posteriores del mismo valor.
                contador++;
            }
            // Avanzar al siguiente nodo hasta llegar a null (fin de la lista).
            actual = actual.siguiente;
        }

        // Al terminar el recorrido, 'contador' es el total de apariciones.
        return contador;
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
     * Método de prueba que ejercita {@code contarOcurrencias} en todos los casos
     * relevantes: varias coincidencias, una sola coincidencia, ninguna coincidencia,
     * lista vacía, valor {@code 0}, y verificación de que el tamaño de la lista no
     * cambia tras contar. También repite casos borde de los ejercicios anteriores
     * para confirmar que las operaciones previas siguen funcionando.
     *
     * @param args no se utilizan
     */
    public static void main(String[] args) {
        // ---- Caso 1: contar en lista vacía ----
        System.out.println("== Caso 1: contar en lista vacía ==");
        ListaEnlazadaSimple vacia = new ListaEnlazadaSimple();
        verificar(vacia.contarOcurrencias(10) == 0,
                "contarOcurrencias(10) en lista vacía devuelve 0");
        verificar(vacia.getSize() == 0 && vacia.estaVacia(),
                "La lista vacía sigue vacía y con size = 0");

        // ---- Caso 2: caso del enunciado con varias coincidencias ----
        // Lista: 10 -> 20 -> 10 -> 30 -> 10 -> null
        System.out.println("\n== Caso 2: varias coincidencias (10 aparece 3 veces) ==");
        ListaEnlazadaSimple lista = new ListaEnlazadaSimple();
        lista.insertarAlFinal(10);
        lista.insertarAlFinal(20);
        lista.insertarAlFinal(10);
        lista.insertarAlFinal(30);
        lista.insertarAlFinal(10);
        lista.imprimir();          // esperado: [10 -> 20 -> 10 -> 30 -> 10]
        int sizeAntes = lista.getSize();
        verificar(lista.contarOcurrencias(10) == 3,
                "contarOcurrencias(10) devuelve 3 (no se corta en la primera)");
        verificar(lista.getSize() == sizeAntes,
                "El tamaño no cambia al contar (método de solo lectura)");

        // ---- Caso 3: una sola coincidencia ----
        System.out.println("\n== Caso 3: una sola coincidencia ==");
        // Sobre la misma lista: 20 aparece 1 vez, 30 aparece 1 vez.
        verificar(lista.contarOcurrencias(20) == 1,
                "contarOcurrencias(20) devuelve 1");
        verificar(lista.contarOcurrencias(30) == 1,
                "contarOcurrencias(30) devuelve 1");
        verificar(lista.getSize() == sizeAntes,
                "El tamaño sigue intacto tras contar valores con una aparición");

        // ---- Caso 4: ninguna coincidencia ----
        System.out.println("\n== Caso 4: ninguna coincidencia ==");
        verificar(lista.contarOcurrencias(999) == 0,
                "contarOcurrencias(999) devuelve 0 (valor ausente)");
        verificar(lista.contarOcurrencias(-1) == 0,
                "contarOcurrencias(-1) devuelve 0 (valor ausente)");
        lista.imprimir();          // esperado: sin cambios
        verificar(lista.getSize() == sizeAntes,
                "El tamaño no cambia al no encontrar coincidencias");

        // ---- Caso 5: contar el valor 0 (valor válido) ----
        System.out.println("\n== Caso 5: contar el valor 0 ==");
        ListaEnlazadaSimple conCeros = new ListaEnlazadaSimple();
        conCeros.insertarAlFinal(0);
        conCeros.insertarAlFinal(5);
        conCeros.insertarAlFinal(0);
        conCeros.insertarAlFinal(0);
        conCeros.imprimir();       // esperado: [0 -> 5 -> 0 -> 0]
        verificar(conCeros.contarOcurrencias(0) == 3,
                "contarOcurrencias(0) devuelve 3 (el cero es un valor válido)");
        verificar(conCeros.contarOcurrencias(5) == 1,
                "contarOcurrencias(5) devuelve 1");

        // ---- Caso 6: lista con un único nodo coincidente ----
        System.out.println("\n== Caso 6: lista de un solo elemento ==");
        ListaEnlazadaSimple unico = new ListaEnlazadaSimple();
        unico.insertarAlFinal(42);
        verificar(unico.contarOcurrencias(42) == 1,
                "contarOcurrencias(42) en lista de un nodo devuelve 1");
        verificar(unico.contarOcurrencias(7) == 0,
                "contarOcurrencias(7) en lista de un nodo devuelve 0");
        verificar(unico.getSize() == 1,
                "El tamaño permanece en 1 tras contar");

        // ---- Caso 7: todos los nodos coinciden ----
        System.out.println("\n== Caso 7: todos los nodos coinciden ==");
        ListaEnlazadaSimple todos = new ListaEnlazadaSimple();
        todos.insertarAlFinal(4);
        todos.insertarAlFinal(4);
        todos.insertarAlFinal(4);
        verificar(todos.contarOcurrencias(4) == 3 && todos.getSize() == 3,
                "contarOcurrencias(4) devuelve 3 con todos los nodos iguales");

        // ---- Caso 8: las operaciones previas siguen funcionando ----
        System.out.println("\n== Caso 8: operaciones previas ==");
        ListaEnlazadaSimple control = new ListaEnlazadaSimple();
        control.insertarEnPosicion(1, 0);  // [1]
        control.insertarEnPosicion(2, 1);  // [1, 2]
        control.insertarEnPosicion(3, 1);  // [1, 3, 2]
        control.insertarEnPosicion(4, 0);  // [4, 1, 3, 2]
        control.modificar(2, 30);          // [4, 1, 30, 2]
        control.insertarAlFinal(30);       // [4, 1, 30, 2, 30]
        control.imprimir();
        verificar(control.obtener(0) == 4 && control.obtener(2) == 30 && control.getSize() == 5,
                "insertarEnPosicion, modificar y obtener siguen funcionando");
        verificar(control.contarOcurrencias(30) == 2,
                "contarOcurrencias(30) devuelve 2 (dos nodos tras modificar e insertar)");
        verificar(control.buscar(30)
                        && control.eliminar(30)
                        && control.buscar(30)
                        && control.getSize() == 4,
                "buscar/eliminar siguen funcionando: al borrar un 30, el otro permanece");
        verificar(control.contarOcurrencias(30) == 1,
                "Tras eliminar una aparición, quedan 1 ocurrencia de 30");
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

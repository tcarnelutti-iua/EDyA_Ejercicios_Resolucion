/*
Prompt utilizado:
Partiendo de la lista enlazada simple de números enteros de los ejercicios anteriores,
transformarla en una versión GENÉRICA ListaEnlazada<T> con nodos Nodo<T>, de modo que pueda
almacenar Integer, String u objetos de una clase Alumno, sin usar colecciones de Java para
guardar los elementos. Las posiciones y el tamaño siguen siendo enteros; las operaciones que
trabajan con datos pasan a recibir o devolver T. Deben conservarse: inserciones (inicio, final,
por posición), búsquedas, obtención por posición, eliminaciones, modificación, conteo de
ocurrencias e inversión. Para comparar datos se usa una igualdad apropiada para objetos
(Objects.equals), de manera que buscar, eliminar y contar ocurrencias funcionen también con
String, Integer o Alumno. Se conservan las validaciones de posición de los ejercicios previos.

QUÉ CAMBIA con la genericidad:
- El tipo del dato en el nodo: antes era int dato; ahora es T dato (Nodo<T>).
- El tipo de los parámetros/retornos de las operaciones que tratan con DATOS:
    insertarAlInicio, insertarAlFinal, insertarEnPosicion, modificar, eliminar, buscar,
    contarOcurrencias reciben T; obtener devuelve T.
- La forma de comparar: antes se usaba el operador == (compara valores primitivos); ahora se
  usa Objects.equals(a, b), que aplica el equals del objeto (contenido) y tolera null.
- La clase y el nodo llevan parámetro de tipo: ListaEnlazada<T> y Nodo<T>.

QUÉ SE MANTIENE igual:
- head (referencia al primer nodo) y el campo size (entero).
- Los enlaces siguiente entre nodos: siguen siendo referencias Nodo<T>.
- El recorrido secuencial partiendo siempre de head: la lista enlazada simple no tiene acceso
  posicional directo.
- Las reglas para actualizar enlaces y tamaño: al insertar se enlaza primero el nuevo nodo con
  el resto y recién después se reconecta el anterior; al eliminar se salta el nodo; size sube/baja
  exactamente una vez por operación; modificar NO cambia enlaces ni size; invertir NO cambia size.
- Las validaciones de posición (0..size para insertar; 0..size-1 para modificar/obtener).

POR QUÉ LOS ALGORITMOS NO DEPENDEN DEL TIPO ALMACENADO:
- Recorrido, inserción y eliminación solo siguen y modifican REFERENCIAS entre nodos
  (actual = actual.siguiente; anterior.siguiente = nuevo; ...). Nunca miran "qué hay adentro" del
  dato, salvo para comparar por igualdad. Por eso el mismo algoritmo funciona con cualquier T.
- La genericidad hace que el compilador valide los tipos: no hay casts manuales ni riesgo de
  guardar un tipo distinto al declarado.

Se incluyen ejemplos de uso con ListaEnlazada<Integer>, ListaEnlazada<String> y
ListaEnlazada<Alumno>. Como en el proyecto no existía una clase Alumno, se incluye una versión
MÍNIMA de Alumno (solo nombre, legajo, equals/hashCode/toString) únicamente para poder compilar
y demostrar el uso; no es una implementación extensa.
*/

import java.util.Objects;

/**
 * Lista enlazada simple GENÉRICA implementada desde cero, con búsqueda secuencial, acceso por
 * posición, inserción por posición, eliminación de la primera aparición de un valor,
 * modificación del dato de un nodo por posición, conteo de ocurrencias e inversión in-place.
 *
 * <p>El parámetro de tipo {@code T} es el tipo de dato que almacena la lista. Puede instanciarse
 * con tipos envoltorio ({@code Integer}, {@code String}), o con cualquier clase propia
 * (por ejemplo {@code Alumno}). No se utiliza ninguna colección de la API de Java
 * ({@code ArrayList}, {@code LinkedList}, etc.) para guardar los elementos: cada elemento vive
 * en un {@link Nodo} propio, enlazado mediante referencias.</p>
 *
 * <p>La clase mantiene dos invariantes fundamentales:</p>
 * <ul>
 *   <li>{@code head} apunta al primer nodo, o es {@code null} si la lista está vacía.</li>
 *   <li>{@code size} refleja siempre la cantidad real de nodos de la lista.</li>
 * </ul>
 *
 * <p><b>Comparación de datos:</b> como {@code T} es un tipo por referencia, no puede compararse
 * con {@code ==} (eso compararía identidades/referencias). Se usa {@link Objects#equals(Object, Object)},
 * que aplica el {@code equals} del objeto (comparación por contenido) y además maneja {@code null}
 * sin lanzar excepción. Así, buscar/eliminar/contar funcionan con {@code Integer}, {@code String}
 * o cualquier objeto que implemente correctamente {@code equals}.</p>
 *
 * @param <T> tipo de dato almacenado en la lista
 */
public class ListaEnlazada<T> {

    /**
     * Nodo de la lista enlazada simple, genérico.
     *
     * <p>Es una clase interna estática porque no necesita acceder al estado de la lista que la
     * contiene; cada nodo solo conoce su dato (de tipo {@code T}) y su sucesor
     * ({@code Nodo<T>}).</p>
     *
     * @param <T> tipo de dato almacenado en el nodo
     */
    private static final class Nodo<T> {
        /**
         * Dato almacenado en este nodo. Su tipo es {@code T} (antes era {@code int}).
         *
         * <p>No es {@code final}: {@code modificar} puede reemplazar el dato guardado en un nodo
         * ya existente sin alterar su lugar ni sus enlaces.</p>
         */
        private T dato;
        /** Referencia al siguiente nodo; {@code null} indica fin de la lista. */
        private Nodo<T> siguiente;

        /**
         * Construye un nodo con el dato indicado y sin sucesor.
         *
         * @param dato dato a almacenar (puede ser {@code null})
         */
        private Nodo(T dato) {
            this.dato = dato;
            this.siguiente = null;
        }
    }

    /** Referencia al primer nodo de la lista; {@code null} si está vacía. Se mantiene igual. */
    private Nodo<T> head;

    /** Cantidad de nodos almacenados en la lista. Sigue siendo un entero. Empieza en cero. */
    private int size;

    /**
     * Crea una lista enlazada simple vacía.
     */
    public ListaEnlazada() {
        this.head = null;
        this.size = 0;
    }

    /**
     * Inserta un nuevo dato al inicio de la lista.
     *
     * <p>El nuevo nodo pasa a ser la cabeza y queda enlazado antes del que era el primer nodo.
     * Si la lista estaba vacía, {@code head} era {@code null}, por lo que el nuevo nodo también
     * queda como único elemento. El algoritmo es idéntico al de la versión de enteros: solo
     * cambió el tipo del dato ({@code T} en lugar de {@code int}).</p>
     *
     * @param dato dato a insertar (puede ser {@code null})
     */
    public void insertarAlInicio(T dato) {
        Nodo<T> nuevo = new Nodo<>(dato);
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
     * <p>Si la lista está vacía, el nuevo nodo se convierte en la cabeza. Si no, se recorre hasta
     * el último nodo y se enlaza el nuevo nodo después de él.</p>
     *
     * @param dato dato a insertar (puede ser {@code null})
     */
    public void insertarAlFinal(T dato) {
        Nodo<T> nuevo = new Nodo<>(dato);

        if (this.head == null) {
            // Caso lista vacía: el nuevo nodo es a la vez el primero.
            this.head = nuevo;
        } else {
            // Recorrer hasta el último nodo (aquel cuyo 'siguiente' es null).
            Nodo<T> actual = this.head;
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
     *   <li>{@code posicion == 0}: inserción al inicio (el nuevo nodo pasa a ser {@code head}).</li>
     *   <li>{@code 0 < posicion < size}: inserción en el medio.</li>
     *   <li>{@code posicion == size}: inserción al final.</li>
     * </ul>
     *
     * <p>El orden de actualización de referencias es clave y no depende del tipo {@code T}:</p>
     * <pre>
     *   nuevo.siguiente = actual.siguiente;  // 1) el nuevo hereda el resto de la lista
     *   actual.siguiente = nuevo;            // 2) recién ahora el anterior apunta al nuevo
     * </pre>
     * <p>Primero se conecta el nuevo nodo con el resto porque {@code actual.siguiente} es la única
     * referencia que permite llegar a los nodos posteriores. Al enlazar primero el nuevo nodo,
     * ninguna referencia se pierde y no se crea un ciclo.</p>
     *
     * <p>La validación de posición se conserva igual que en los ejercicios previos: se comprueba
     * contra {@code size} antes de recorrer.</p>
     *
     * <p>Complejidad temporal: O(k) para insertar en la posición k; O(n) en el peor caso (final).
     * Complejidad espacial: O(1).</p>
     *
     * @param dato     dato a insertar (puede ser {@code null})
     * @param posicion índice donde insertar (base cero); rango válido {@code [0, size]}
     * @throws IndexOutOfBoundsException si {@code posicion < 0} o {@code posicion > size}
     */
    public void insertarEnPosicion(T dato, int posicion) {
        // Validación previa: el rango permitido es [0, size] (ambos inclusive).
        if (posicion < 0 || posicion > this.size) {
            throw new IndexOutOfBoundsException(
                    "Posición fuera de rango: " + posicion
                            + " (rango válido: 0.." + this.size + ")");
        }

        Nodo<T> nuevo = new Nodo<>(dato);

        if (posicion == 0) {
            // Inserción al inicio: la cabeza pasa a ser el nuevo nodo.
            nuevo.siguiente = this.head;
            this.head = nuevo;
        } else {
            // Ubicar el nodo ANTERIOR al punto de inserción (el que ocupa posicion - 1).
            Nodo<T> actual = this.head;
            for (int i = 0; i < posicion - 1; i++) {
                actual = actual.siguiente;
            }

            // Orden correcto: el nuevo se conecta primero con el resto y luego 'actual' al nuevo.
            nuevo.siguiente = actual.siguiente; // 1) nuevo hereda el resto
            actual.siguiente = nuevo;           // 2) el anterior enlaza al nuevo
        }

        // Se agregó exactamente un nodo, en todos los casos válidos.
        this.size++;
    }

    /**
     * Reemplaza el dato almacenado en la posición indicada (contando desde cero).
     *
     * <p>Cambia únicamente el VALOR del nodo ({@code actual.dato = nuevoDato}); NO modifica su
     * referencia al siguiente. Por eso la cantidad de nodos, el orden y todos los enlaces se
     * mantienen iguales y {@code size} nunca cambia. El dato de reemplazo ahora es de tipo
     * {@code T}.</p>
     *
     * <p>La validación de posición se conserva: debe cumplir {@code 0 <= posicion < size}.</p>
     *
     * <p>Complejidad temporal: O(k) para modificar la posición k; O(n) en el peor caso.
     * Complejidad espacial: O(1).</p>
     *
     * @param posicion  índice del elemento a modificar (base cero)
     * @param nuevoDato nuevo dato de tipo {@code T} (puede ser {@code null})
     * @throws IndexOutOfBoundsException si {@code posicion < 0} o {@code posicion >= size}
     */
    public void modificar(int posicion, T nuevoDato) {
        // Validación previa: la posición debe pertenecer al rango [0, size - 1].
        if (posicion < 0 || posicion >= this.size) {
            throw new IndexOutOfBoundsException(
                    "Posición fuera de rango: " + posicion + " (tamaño = " + this.size + ")");
        }

        // Comenzar siempre en la cabeza: es el único punto de entrada a la lista.
        Nodo<T> actual = this.head;

        // Avanzar exactamente 'posicion' nodos siguiendo las referencias.
        for (int i = 0; i < posicion; i++) {
            actual = actual.siguiente;
        }

        // Reemplazar SOLO el dato del nodo alcanzado; los enlaces no se tocan.
        actual.dato = nuevoDato;
    }

    /**
     * Elimina la PRIMERA aparición del valor indicado en la lista.
     *
     * <p>Se recorre la lista con dos referencias, {@code actual} y {@code anterior}
     * ({@code null} mientras {@code actual} es la cabeza). Al encontrar la coincidencia:</p>
     * <ul>
     *   <li><b>Es la cabeza</b> ({@code anterior == null}): {@code this.head = actual.siguiente}.</li>
     *   <li><b>Está en el medio o al final</b>: {@code anterior.siguiente = actual.siguiente}.</li>
     * </ul>
     *
     * <p><b>Comparación de objetos:</b> al buscar la coincidencia se usa
     * {@link Objects#equals(Object, Object)} en lugar de {@code ==}. Para tipos por referencia
     * ({@code String}, {@code Integer}, {@code Alumno}) esto compara el contenido y no la
     * identidad, por lo que un {@code String} u objeto con el mismo valor se encuentra aunque sea
     * una instancia distinta. Además, {@code Objects.equals} maneja {@code null} sin error.</p>
     *
     * <p>Se elimina únicamente la primera aparición: en cuanto se enlaza el salto, el método
     * retorna, de modo que los valores repetidos posteriores permanecen en la lista. Si la lista
     * está vacía o el dato no aparece, se devuelve {@code false} sin modificar estructura ni
     * tamaño.</p>
     *
     * <p>Complejidad temporal: O(n) en el peor caso; O(1) en el mejor caso (dato en la cabeza).
     * Complejidad espacial: O(1).</p>
     *
     * @param dato dato a eliminar (puede ser {@code null})
     * @return {@code true} si se encontró y eliminó la primera aparición; {@code false} en caso
     *         contrario
     */
    public boolean eliminar(T dato) {
        Nodo<T> actual = this.head;
        Nodo<T> anterior = null;

        // Recorrer la lista llevando siempre el nodo anterior al actual.
        while (actual != null) {
            // Igualdad por contenido (no por referencia), segura ante null.
            if (Objects.equals(actual.dato, dato)) {
                // Coincidencia encontrada: corresponde eliminar 'actual'.
                if (anterior == null) {
                    // El nodo a eliminar es la cabeza: la nueva cabeza es su siguiente.
                    this.head = actual.siguiente;
                } else {
                    // Nodo del medio o del final: el anterior se salta al eliminado.
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
     * <p>Como los nodos están conectados únicamente por referencias al siguiente, la única forma
     * de llegar a un elemento es partir de {@code head} y avanzar nodo por nodo. Se recorre la
     * lista y, en cuanto se encuentra un nodo cuyo dato es igual al buscado, se devuelve
     * {@code true}.</p>
     *
     * <p>La igualdad se resuelve con {@link Objects#equals(Object, Object)}, de modo que funciona
     * con {@code String}, {@code Integer} y objetos como {@code Alumno}, comparando contenido.</p>
     *
     * <p><b>Diferencia con {@link #contarOcurrencias(Object)}:</b> {@code buscar} puede cortar en
     * la primera coincidencia porque solo responde SI el valor existe; {@code contarOcurrencias}
     * debe recorrer toda la lista para responder CUÁNTAS veces aparece.</p>
     *
     * <p>Complejidad temporal: O(n) en el peor caso; O(1) en el mejor. Espacial: O(1).</p>
     *
     * @param dato dato a buscar (puede ser {@code null})
     * @return {@code true} si el dato está presente; {@code false} en caso contrario
     */
    public boolean buscar(T dato) {
        Nodo<T> actual = this.head;
        // Avanzar por la lista mientras haya nodos por visitar.
        while (actual != null) {
            // Comparar por contenido, no por referencia.
            if (Objects.equals(actual.dato, dato)) {
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
     * <p>Recorre la lista <b>completa</b> desde {@code head} hasta {@code null}, comparando el
     * dato de cada nodo con el buscado mediante {@link Objects#equals(Object, Object)} y sumando 1
     * a un contador cada vez que coinciden. El recorrido <b>no se interrumpe</b> en la primera
     * coincidencia, porque un mismo valor puede repetirse en varios nodos.</p>
     *
     * <p>Es de <b>solo lectura</b>: no modifica enlaces ni datos, por lo que {@code size} queda
     * intacto.</p>
     *
     * <p>Complejidad temporal: O(n) siempre. Complejidad espacial: O(1).</p>
     *
     * @param dato dato cuyas apariciones se quieren contar (puede ser {@code null})
     * @return cantidad de nodos cuyo dato es igual a {@code dato}; {@code 0} si no aparece
     */
    public int contarOcurrencias(T dato) {
        int contador = 0;
        Nodo<T> actual = this.head;

        // Recorrer TODA la lista: no se corta al encontrar una coincidencia.
        while (actual != null) {
            if (Objects.equals(actual.dato, dato)) {
                contador++;
            }
            actual = actual.siguiente;
        }

        return contador;
    }

    /**
     * Invierte el orden de los enlaces de la lista <b>in-place</b>: no se crea otra lista ni
     * nodos nuevos, solo se reorientan las referencias {@code siguiente} de los nodos ya
     * existentes. El algoritmo es exactamente el mismo que en la versión de enteros: no mira el
     * tipo del dato, solo las referencias.
     *
     * <p>Ejemplo: {@code 10 -> 20 -> 30 -> 40 -> null} queda como
     * {@code 40 -> 30 -> 20 -> 10 -> null}.</p>
     *
     * <p>Se usan tres referencias auxiliares: {@code anterior} (empieza en {@code null}),
     * {@code actual} (empieza en {@code head}) y {@code siguiente} (guarda el resto antes de
     * tocar el enlace). El orden de las operaciones es fundamental:</p>
     * <pre>
     *   siguiente = actual.siguiente;  // 1) GUARDAR el resto ANTES de tocarlo
     *   actual.siguiente = anterior;   // 2) invertir el enlace
     *   anterior = actual;             // 3) avanzar 'anterior'
     *   actual = siguiente;            // 4) avanzar 'actual'
     * </pre>
     * <p>Al terminar, {@code this.head = anterior}. {@code size} no cambia: no se crean ni se
     * destruyen nodos.</p>
     *
     * <p>Complejidad temporal: O(n). Complejidad espacial: O(1).</p>
     */
    public void invertir() {
        Nodo<T> anterior = null;     // El primero original será el último (apuntará a null).
        Nodo<T> actual = this.head;  // Se recorre desde la cabeza.
        Nodo<T> siguiente = null;    // Aquí se guarda el resto antes de tocar el enlace.

        while (actual != null) {
            siguiente = actual.siguiente;  // 1) guardar primero el resto
            actual.siguiente = anterior;   // 2) invertir el enlace
            anterior = actual;             // 3) avanzar 'anterior'
            actual = siguiente;            // 4) avanzar 'actual'
        }

        this.head = anterior;  // nuevo primero (o null si la lista estaba vacía)
        // size no se modifica.
    }

    /**
     * Devuelve el dato almacenado en la posición indicada (contando desde cero).
     *
     * <p>La lista enlazada simple NO permite acceso posicional directo: hay que partir de
     * {@code head} y avanzar nodo por nodo hasta dar exactamente {@code posicion} pasos. El valor
     * devuelto es de tipo {@code T} (antes era {@code int}).</p>
     *
     * @param posicion índice del elemento a obtener (base cero)
     * @return el dato de tipo {@code T} almacenado en la posición indicada
     * @throws IndexOutOfBoundsException si {@code posicion < 0} o {@code posicion >= size}
     */
    public T obtener(int posicion) {
        // Validación previa: la posición debe pertenecer al rango [0, size - 1].
        if (posicion < 0 || posicion >= this.size) {
            throw new IndexOutOfBoundsException(
                    "Posición fuera de rango: " + posicion + " (tamaño = " + this.size + ")");
        }

        Nodo<T> actual = this.head;
        for (int i = 0; i < posicion; i++) {
            actual = actual.siguiente;
        }

        // Devolver el dato del nodo alcanzado (tipo T).
        return actual.dato;
    }

    /**
     * Indica si la lista no contiene elementos.
     *
     * @return {@code true} si la lista está vacía; {@code false} en caso contrario
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
     * Imprime en la consola los elementos de la lista en orden, desde {@code head} hasta el
     * último nodo.
     *
     * <p>Como el dato es de tipo {@code T}, se invoca su método {@code toString()} a través de
     * {@link StringBuilder#append(Object)}. Por eso conviene que las clases usadas como dato
     * sobrescriban {@code toString()} para una salida legible. Un dato {@code null} se imprime
     * como {@code "null"}.</p>
     */
    public void imprimir() {
        if (estaVacia()) {
            System.out.println("Lista vacía []");
            return;
        }

        StringBuilder salida = new StringBuilder("Lista: [");
        Nodo<T> actual = this.head;
        while (actual != null) {
            salida.append(actual.dato);  // usa toString() del objeto
            if (actual.siguiente != null) {
                salida.append(" -> ");
            }
            actual = actual.siguiente;
        }
        salida.append("]  (tamaño = ").append(this.size).append(")");
        System.out.println(salida);
    }

    // ---------------------------------------------------------------------------------------
    // Clase de apoyo MÍNIMA para el ejemplo con objetos.
    // No es una implementación extensa: solo lo necesario para poder almacenar y comparar.
    // ---------------------------------------------------------------------------------------

    /**
     * Modelo de dominio mínimo para demostrar el uso de {@code ListaEnlazada<Alumno>}.
     *
     * <p>Sobrescribe {@code equals}/{@code hashCode} para que dos alumnos con el mismo legajo y
     * nombre se consideren iguales. Esto es lo que permite que {@code buscar}, {@code eliminar} y
     * {@code contarOcurrencias} funcionen con objetos aunque se pasen instancias distintas.</p>
     */
    private static final class Alumno {
        private final String nombre;
        private final int legajo;

        private Alumno(String nombre, int legajo) {
            this.nombre = nombre;
            this.legajo = legajo;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) {
                return true;
            }
            if (!(o instanceof Alumno)) {
                return false;
            }
            Alumno otro = (Alumno) o;
            return legajo == otro.legajo && Objects.equals(nombre, otro.nombre);
        }

        @Override
        public int hashCode() {
            return Objects.hash(nombre, legajo);
        }

        @Override
        public String toString() {
            return nombre + "(" + legajo + ")";
        }
    }

    /**
     * Demuestra el uso de la lista genérica con {@code Integer}, {@code String} y {@code Alumno},
     * y verifica que las operaciones se comporten igual que en la versión de enteros.
     *
     * @param args no se utilizan
     */
    public static void main(String[] args) {
        // =====================================================================================
        // Ejemplo 1: ListaEnlazada<Integer>
        // =====================================================================================
        System.out.println("== Ejemplo 1: ListaEnlazada<Integer> ==");
        ListaEnlazada<Integer> enteros = new ListaEnlazada<>();
        enteros.insertarAlFinal(10);
        enteros.insertarAlFinal(20);
        enteros.insertarAlFinal(30);
        enteros.insertarAlInicio(5);          // [5, 10, 20, 30]
        enteros.insertarEnPosicion(15, 2);    // [5, 10, 15, 20, 30]
        enteros.imprimir();
        verificar(enteros.getSize() == 5, "size = 5 tras las inserciones");
        verificar(enteros.obtener(0) == 5 && enteros.obtener(4) == 30,
                "obtener devuelve Integer en los extremos");
        verificar(enteros.buscar(15) && !enteros.buscar(99),
                "buscar(15) = true y buscar(99) = false");
        enteros.modificar(2, 99);             // [5, 10, 99, 20, 30]
        verificar(enteros.obtener(2) == 99, "modificar cambia el dato en la posición 2");
        verificar(enteros.eliminar(10), "eliminar(10) devuelve true");
        verificar(!enteros.eliminar(10), "eliminar(10) otra vez devuelve false");
        enteros.insertarAlFinal(99);
        verificar(enteros.contarOcurrencias(99) == 2, "contarOcurrencias(99) = 2");
        enteros.imprimir();
        enteros.invertir();
        enteros.imprimir();
        verificar(enteros.obtener(0) == 99 && enteros.getSize() == 5 && !tieneCiclo(enteros),
                "invertir conserva size y no genera ciclos");

        // =====================================================================================
        // Ejemplo 2: ListaEnlazada<String>  (igualdad por contenido con Objects.equals)
        // =====================================================================================
        System.out.println("\n== Ejemplo 2: ListaEnlazada<String> ==");
        ListaEnlazada<String> nombres = new ListaEnlazada<>();
        nombres.insertarAlFinal("Ana");
        nombres.insertarAlFinal("Beto");
        nombres.insertarAlFinal("Ana");
        nombres.insertarAlInicio("Carla");    // [Carla, Ana, Beto, Ana]
        nombres.imprimir();
        verificar(nombres.buscar("Beto"), "buscar(\"Beto\") encuentra el String");
        verificar(nombres.contarOcurrencias("Ana") == 2,
                "contarOcurrencias(\"Ana\") = 2 (comparación por contenido)");
        // Se pasa un String NUEVO, no el mismo objeto guardado: equals lo encuentra igual.
        verificar(nombres.buscar(new String("Carla")),
                "buscar con un String nuevo de igual contenido devuelve true");
        verificar(nombres.eliminar("Ana"), "eliminar(\"Ana\") elimina la primera aparición");
        verificar(nombres.contarOcurrencias("Ana") == 1, "queda 1 ocurrencia de \"Ana\"");
        nombres.imprimir();

        // =====================================================================================
        // Ejemplo 3: ListaEnlazada<Alumno>  (objetos con equals/hashCode propios)
        // =====================================================================================
        System.out.println("\n== Ejemplo 3: ListaEnlazada<Alumno> ==");
        ListaEnlazada<Alumno> alumnos = new ListaEnlazada<>();
        alumnos.insertarAlFinal(new Alumno("Ana", 101));
        alumnos.insertarAlFinal(new Alumno("Beto", 102));
        alumnos.insertarAlInicio(new Alumno("Carla", 103));
        alumnos.imprimir();                   // usa toString() de Alumno
        verificar(alumnos.buscar(new Alumno("Beto", 102)),
                "buscar con un Alumno nuevo de igual contenido devuelve true");
        verificar(!alumnos.buscar(new Alumno("Beto", 999)),
                "un Alumno con distinto legajo no se encuentra");
        verificar(alumnos.obtener(0).equals(new Alumno("Carla", 103)),
                "obtener(0) devuelve el Alumno de la cabeza");
        verificar(alumnos.eliminar(new Alumno("Ana", 101)),
                "eliminar elimina el Alumno por igualdad de contenido");
        verificar(alumnos.getSize() == 2, "size = 2 tras eliminar");
        alumnos.invertir();
        alumnos.imprimir();
        verificar(alumnos.obtener(0).equals(new Alumno("Beto", 102)),
                "tras invertir, el primer Alumno es Beto");

        // =====================================================================================
        // Ejemplo 4: validaciones de posición conservadas
        // =====================================================================================
        System.out.println("\n== Ejemplo 4: validaciones de posición ==");
        ListaEnlazada<String> chica = new ListaEnlazada<>();
        chica.insertarAlFinal("uno");   // size = 1; posiciones válidas [0]
        verificarSeLanza(() -> chica.insertarEnPosicion("x", 2),
                "insertarEnPosicion en 2 (size=1) lanza IndexOutOfBoundsException");
        verificarSeLanza(() -> chica.insertarEnPosicion("x", -1),
                "insertarEnPosicion en -1 lanza IndexOutOfBoundsException");
        verificarSeLanza(() -> chica.obtener(1),
                "obtener(1) con size=1 lanza IndexOutOfBoundsException");
        verificarSeLanza(() -> chica.modificar(1, "x"),
                "modificar(1) con size=1 lanza IndexOutOfBoundsException");
        verificar(!chica.estaVacia() && chica.getSize() == 1,
                "tras las validaciones la lista sigue intacta (size = 1)");

        // =====================================================================================
        // Ejemplo 5: datos nulos son tratados de forma segura por Objects.equals
        // =====================================================================================
        System.out.println("\n== Ejemplo 5: valores null ==");
        ListaEnlazada<String> conNull = new ListaEnlazada<>();
        conNull.insertarAlFinal(null);
        conNull.insertarAlFinal("texto");
        verificar(conNull.buscar(null) && conNull.contarOcurrencias(null) == 1,
                "null puede almacenarse y buscarse/contarse sin excepción");
        conNull.imprimir();
    }

    // ---------------------------------------------------------------------------------------
    // Utilidades de prueba
    // ---------------------------------------------------------------------------------------

    /**
     * Cuenta los nodos alcanzables desde {@code head}. Si el recorrido supera el tamaño declarado
     * ({@code size}), se considera que hay un ciclo y se devuelve {@code -1}.
     *
     * <p>Usa el comodín {@code ListaEnlazada<?>} / {@code Nodo<?>} para poder operar sobre listas
     * de cualquier tipo sin conocer {@code T}.</p>
     *
     * @return cantidad de nodos recorridos, o {@code -1} si se detecta un ciclo
     */
    private static int contarNodos(ListaEnlazada<?> lista) {
        int contador = 0;
        Nodo<?> actual = lista.head;
        while (actual != null) {
            contador++;
            if (contador > lista.size) {
                return -1;
            }
            actual = actual.siguiente;
        }
        return contador;
    }

    /**
     * Detecta si la lista contiene un ciclo usando el algoritmo de Floyd (tortuga y liebre).
     *
     * <p>Usa comodines para operar sobre listas de cualquier tipo.</p>
     *
     * @return {@code true} si la lista tiene un ciclo; {@code false} en caso contrario
     */
    private static boolean tieneCiclo(ListaEnlazada<?> lista) {
        Nodo<?> lento = lista.head;
        Nodo<?> rapido = lista.head;
        while (rapido != null && rapido.siguiente != null) {
            lento = lento.siguiente;             // avanza 1 nodo
            rapido = rapido.siguiente.siguiente;  // avanza 2 nodos
            if (lento == rapido) {
                return true;
            }
        }
        return false;
    }

    /**
     * Utilidad para mostrar el resultado de una verificación booleana.
     *
     * @param condicion   resultado de la comprobación
     * @param descripcion texto descriptivo de lo verificado
     */
    private static void verificar(boolean condicion, String descripcion) {
        System.out.println((condicion ? "[OK]   " : "[FALLA] ") + descripcion);
    }

    /**
     * Comprueba que la acción indicada lance {@link IndexOutOfBoundsException}. Se usa para
     * verificar las validaciones de posición.
     *
     * @param accion      operación a ejecutar
     * @param descripcion texto descriptivo de lo verificado
     */
    private static void verificarSeLanza(Runnable accion, String descripcion) {
        boolean lanzo = false;
        try {
            accion.run();
        } catch (IndexOutOfBoundsException e) {
            lanzo = true;
        }
        verificar(lanzo, descripcion);
    }
}

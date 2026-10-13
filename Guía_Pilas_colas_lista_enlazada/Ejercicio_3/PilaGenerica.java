/*
 * ============================================================================
 * PROMPT ORIGINAL
 * ============================================================================
 * En este ejercicio, modificá la implementación de pila para que sea genérica y
 * guardá la solución en:
 *
 * Guía_Pilas_colas_lista_enlazada/Ejercicio_3/PilaGenerica.java
 *
 * Requisitos:
 * - Definí una clase pública PilaGenerica<T>, donde T representa el tipo de
 *   dato que almacenará la pila.
 * - Usá una lista enlazada simple y mantené el head como tope. Los nodos también
 *   deben ser genéricos.
 * - Conservá las operaciones de la pila: apilar, desapilar, consultar el tope,
 *   verificar si está vacía, buscar si contiene un valor e imprimir su
 *   contenido.
 * - No uses tipos crudos (raw types), conversiones de tipo innecesarias ni
 *   estructuras de colección para almacenar los elementos.
 * - La implementación debe poder usarse con Integer, String y una clase propia
 *   de ejemplo.
 * - Para buscar valores, usá la igualdad apropiada para objetos, por ejemplo
 *   Objects.equals.
 * - Si la pila está vacía, desapilar y consultar el tope deben lanzar
 *   NoSuchElementException con un mensaje claro.
 * - Apilar, desapilar, consultar el tope y verificar si está vacía deben ser
 *   O(1). Buscar e imprimir deben ser O(n).
 * - Incluí pruebas breves que demuestren el uso de la misma pila con Integer,
 *   String y objetos de una clase propia.
 *
 * Incluí en un comentario al comienzo del archivo este prompt completo y una
 * explicación breve de estos conceptos:
 * 1. En Java, los genéricos permiten parametrizar una clase con un tipo, como
 *    PilaGenerica<T>, para reutilizarla con distintos tipos conservando la
 *    comprobación de tipos en compilación.
 * 2. La lógica de una pila no depende del tipo almacenado: solo necesita
 *    apilar, desapilar y acceder al tope. Por eso, sus nodos guardan valores de
 *    tipo T y las operaciones mantienen el mismo comportamiento para Integer,
 *    String u objetos propios.
 *
 * Aclarà también si hubo ajustes posteriores. Si no modificaste los requisitos,
 * indicá: "Ajustes posteriores: ninguno". Mantené la solución en el archivo
 * indicado y evitá agregar otros archivos.
 *
 * ----------------------------------------------------------------------------
 * AJUSTES POSTERIORES
 * ----------------------------------------------------------------------------
 * Ajustes posteriores: ninguno
 *
 * ============================================================================
 * EXPLICACIÓN BREVE DE LOS CONCEPTOS
 * ============================================================================
 * 1. Genéricos en Java: permiten parametrizar una clase con un tipo, como
 *    PilaGenerica<T>. El parámetro T actúa como un "tipo por definir" que el
 *    usuario concreta al crear la instancia (por ejemplo PilaGenerica<Integer>
 *    o PilaGenerica<String>). Así, una única implementación se reutiliza con
 *    distintos tipos y el compilador sigue comprobando los tipos: apilar un
 *    String en una PilaGenerica<Integer> no compila, y desapilar() devuelve
 *    directamente T sin necesidad de conversiones (casts).
 *
 * 2. La lógica de una pila es independiente del tipo almacenado: solo necesita
 *    apilar, desapilar y acceder al tope. Por eso los nodos guardan valores de
 *    tipo T y las operaciones mantienen exactamente el mismo comportamiento
 *    para Integer, String u objetos propios. Lo único "sensible al tipo" es la
 *    comparación de valores en contiene(), que se resuelve con Objects.equals,
 *    la igualdad apropiada para objetos.
 * ============================================================================
 */

import java.util.NoSuchElementException;
import java.util.Objects;

/**
 * Pila genérica implementada sobre una lista enlazada simple.
 *
 * <p>El parámetro {@code T} es el tipo de los elementos almacenados. La misma
 * clase sirve para {@code Integer}, {@code String} o cualquier clase propia.
 *
 * <p>El {@code tope} (head de la lista) representa el tope de la pila. Esto
 * permite que {@link #apilar(Object)}, {@link #desapilar()},
 * {@link #consultarTope()} y {@link #estaVacia()} trabajen siempre sobre el
 * primer nodo, sin recorrer la estructura, por lo que las cuatro son O(1).
 *
 * <p>No se utilizan arrays ni colecciones de Java: los elementos se almacenan
 * únicamente en instancias de {@link Nodo}.
 *
 * @param <T> tipo de los elementos almacenados en la pila.
 */
public class PilaGenerica<T> {

    /**
     * Nodo de la lista enlazada simple.
     *
     * <p>Cada nodo guarda un valor de tipo {@code T} y una referencia al
     * siguiente nodo. El último nodo apunta a {@code null}.
     *
     * @param <T> tipo del valor almacenado en el nodo.
     */
    private static final class Nodo<T> {

        /** Valor almacenado en este nodo. */
        private final T valor;

        /** Referencia al siguiente nodo de la lista (null si es el último). */
        private Nodo<T> siguiente;

        /**
         * Crea un nodo con un valor y una referencia al siguiente nodo.
         *
         * @param valor     valor a almacenar.
         * @param siguiente nodo que le sigue en la lista.
         */
        private Nodo(T valor, Nodo<T> siguiente) {
            this.valor = valor;
            this.siguiente = siguiente;
        }
    }

    /** Primer nodo de la lista; representa el tope de la pila. */
    private Nodo<T> tope;

    /** Cantidad de elementos almacenados en la pila. */
    private int tamanio;

    /**
     * Crea una pila vacía.
     */
    public PilaGenerica() {
        this.tope = null;
        this.tamanio = 0;
    }

    /**
     * Apila un valor en el tope de la pila.
     *
     * <p>El nuevo nodo se inserta al inicio de la lista, por lo que la
     * operación es O(1).
     *
     * @param valor valor a apilar (puede ser {@code null}).
     */
    public void apilar(T valor) {
        // El nuevo nodo pasa a ser el tope y enlaza al antiguo tope.
        Nodo<T> nuevo = new Nodo<>(valor, tope);
        tope = nuevo;
        tamanio++;
    }

    /**
     * Desapila y devuelve el valor del tope de la pila.
     *
     * <p>Quita el primer nodo de la lista, operación O(1).
     *
     * @return el valor que estaba en el tope.
     * @throws NoSuchElementException si la pila está vacía.
     */
    public T desapilar() {
        if (estaVacia()) {
            throw new NoSuchElementException(
                    "No se puede desapilar: la pila está vacía.");
        }

        T valor = tope.valor;
        tope = tope.siguiente; // Avanza el tope al siguiente nodo.
        tamanio--;
        return valor;
    }

    /**
     * Devuelve el valor del tope sin quitarlo.
     *
     * @return el valor del tope.
     * @throws NoSuchElementException si la pila está vacía.
     */
    public T consultarTope() {
        if (estaVacia()) {
            throw new NoSuchElementException(
                    "No se puede consultar el tope: la pila está vacía.");
        }
        return tope.valor;
    }

    /**
     * Indica si la pila está vacía.
     *
     * @return {@code true} si no hay elementos; {@code false} en caso contrario.
     */
    public boolean estaVacia() {
        return tope == null;
    }

    /**
     * Indica si un valor está presente en la pila.
     *
     * <p>Recorre la lista desde el tope hasta el último nodo comparando con
     * {@link Objects#equals(Object, Object)}, la igualdad apropiada para
     * objetos. Es O(n) en el peor caso.
     *
     * @param valor valor a buscar.
     * @return {@code true} si el valor está en la pila; {@code false} si no
     *         está o si la pila está vacía.
     */
    public boolean contiene(T valor) {
        Nodo<T> actual = tope;
        while (actual != null) {
            if (Objects.equals(actual.valor, valor)) {
                return true;
            }
            actual = actual.siguiente;
        }
        return false;
    }

    /**
     * Devuelve la cantidad de elementos almacenados.
     *
     * @return tamaño actual de la pila.
     */
    public int tamanio() {
        return tamanio;
    }

    /**
     * Muestra por consola los elementos desde el tope hasta el último, sin
     * modificar la pila. Si la pila está vacía, lo informa.
     *
     * <p>Recorre la lista una sola vez, por lo que es O(n).
     */
    public void imprimir() {
        if (estaVacia()) {
            System.out.println("La pila está vacía.");
            return;
        }

        StringBuilder salida = new StringBuilder("Tope -> ");
        Nodo<T> actual = tope;
        while (actual != null) {
            salida.append(actual.valor);
            if (actual.siguiente != null) {
                salida.append(" -> ");
            }
            actual = actual.siguiente;
        }
        salida.append(" <- Fondo");
        System.out.println(salida.toString());
    }

    /**
     * Clase propia de ejemplo para demostrar el uso de la pila con un tipo
     * definido por el usuario.
     *
     * <p>Sobrescribe {@code equals} y {@code hashCode} para que
     * {@link Objects#equals(Object, Object)} compare por identidad lógica
     * (código) y no por referencia.
     */
    private static final class Libro {

        /** Código único del libro. */
        private final int codigo;

        /** Título del libro. */
        private final String titulo;

        /**
         * Crea un libro.
         *
         * @param codigo código único.
         * @param titulo título del libro.
         */
        private Libro(int codigo, String titulo) {
            this.codigo = codigo;
            this.titulo = titulo;
        }

        @Override
        public boolean equals(Object otro) {
            if (this == otro) {
                return true;
            }
            if (otro == null || getClass() != otro.getClass()) {
                return false;
            }
            Libro libro = (Libro) otro;
            return codigo == libro.codigo;
        }

        @Override
        public int hashCode() {
            return Integer.hashCode(codigo);
        }

        @Override
        public String toString() {
            return titulo + "(" + codigo + ")";
        }
    }

    /**
     * Punto de entrada de prueba. Ejercita la misma pila genérica con
     * {@code Integer}, {@code String} y una clase propia ({@link Libro}), y
     * verifica los casos de pila vacía.
     *
     * @param args argumentos de línea de comandos (no se usan).
     */
    public static void main(String[] args) {
        probarConInteger();
        probarConString();
        probarConClasePropia();
        probarPilaVacia();
    }

    /**
     * Demuestra el uso de la pila con {@code Integer}.
     */
    private static void probarConInteger() {
        System.out.println("== PilaGenerica<Integer> ==");
        PilaGenerica<Integer> pila = new PilaGenerica<>();
        pila.apilar(10);
        pila.apilar(20);
        pila.apilar(30);

        System.out.println("estaVacia(): " + pila.estaVacia());
        System.out.println("tamanio(): " + pila.tamanio());
        System.out.println("consultarTope(): " + pila.consultarTope());
        System.out.println("contiene(20): " + pila.contiene(20));
        System.out.println("contiene(99): " + pila.contiene(99));
        pila.imprimir();

        System.out.println("desapilar(): " + pila.desapilar());
        pila.imprimir();
        System.out.println();
    }

    /**
     * Demuestra el uso de la misma pila con {@code String}.
     */
    private static void probarConString() {
        System.out.println("== PilaGenerica<String> ==");
        PilaGenerica<String> pila = new PilaGenerica<>();
        pila.apilar("uno");
        pila.apilar("dos");
        pila.apilar("tres");

        System.out.println("consultarTope(): " + pila.consultarTope());
        System.out.println("contiene(\"dos\"): " + pila.contiene("dos"));
        System.out.println("contiene(\"nueve\"): " + pila.contiene("nueve"));
        pila.imprimir();

        System.out.println("desapilar(): " + pila.desapilar());
        pila.imprimir();
        System.out.println();
    }

    /**
     * Demuestra el uso de la misma pila con una clase propia ({@link Libro}),
     * comprobando la búsqueda por igualdad lógica.
     */
    private static void probarConClasePropia() {
        System.out.println("== PilaGenerica<Libro> ==");
        PilaGenerica<Libro> pila = new PilaGenerica<>();
        pila.apilar(new Libro(1, "Álgebra"));
        pila.apilar(new Libro(2, "Física"));
        pila.apilar(new Libro(3, "Química"));

        System.out.println("consultarTope(): " + pila.consultarTope());
        // Se busca con una instancia distinta pero lógicamente igual (mismo
        // código), lo que demuestra que se usa equals y no ==.
        System.out.println("contiene(Libro(2)): "
                + pila.contiene(new Libro(2, "Otro título")));
        System.out.println("contiene(Libro(9)): "
                + pila.contiene(new Libro(9, "Inexistente")));
        pila.imprimir();

        System.out.println("desapilar(): " + pila.desapilar());
        pila.imprimir();
        System.out.println();
    }

    /**
     * Verifica los casos de pila vacía: {@code estaVacia}, {@code contiene},
     * {@code imprimir} y las excepciones de {@code desapilar} y
     * {@code consultarTope}.
     */
    private static void probarPilaVacia() {
        System.out.println("== Casos de pila vacía ==");
        PilaGenerica<Integer> pila = new PilaGenerica<>();
        System.out.println("estaVacia(): " + pila.estaVacia());
        System.out.println("contiene(5): " + pila.contiene(5));
        pila.imprimir();

        try {
            pila.desapilar();
            System.out.println("ERROR: debió lanzar NoSuchElementException.");
        } catch (NoSuchElementException e) {
            System.out.println("desapilar() vacía -> " + e.getMessage());
        }

        try {
            pila.consultarTope();
            System.out.println("ERROR: debió lanzar NoSuchElementException.");
        } catch (NoSuchElementException e) {
            System.out.println("consultarTope() vacía -> " + e.getMessage());
        }
    }
}

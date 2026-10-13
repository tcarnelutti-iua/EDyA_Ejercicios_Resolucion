/*
Prompt utilizado:
Partiendo de la lista enlazada simple de enteros de los ejercicios anteriores, agregar el método
void eliminarEnPosicion(int posicion). Las posiciones se cuentan desde cero. Si posicion < 0
o posicion >= size, lanzar IndexOutOfBoundsException.

Para eliminar un nodo que no sea el primero, recorré la lista hasta encontrar el nodo
anterior a la posición indicada. Luego, hacé que su referencia al siguiente apunte al nodo
siguiente del que se elimina. Si se elimina la posición cero, actualizá head para que apunte
al segundo nodo. Decrementá size una sola vez después de una eliminación válida.

Conservá las operaciones existentes y considerá la lista vacía, eliminar el primer nodo, uno
del medio y el último. Verificá que el tamaño y los enlaces queden correctos y que el archivo compile.
*/

import java.util.StringJoiner;

/**
 * Lista enlazada simple de números enteros implementada desde cero, con búsqueda
 * secuencial, acceso por posición, inserción por posición, eliminación de la primera
 * aparición de un valor y eliminación por posición.
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
     * @param dato     valor entero a insertar (puede ser cero o negativo)
     * @param posicion índice donde insertar (base cero); rango válido {@code [0, size]}
     * @throws IndexOutOfBoundsException si {@code posicion < 0} o {@code posicion > size}
     */
    public void insertarEnPosicion(int dato, int posicion) {
        // Validación previa: el rango permitido es [0, size] (ambos inclusive).
        if (posicion < 0 || posicion > this.size) {
            throw new IndexOutOfBoundsException(
                    "Posición fuera de rango: " + posicion
                            + " (rango válido: 0.." + this.size + ")");
        }

        Nodo nuevo = new Nodo(dato);

        if (posicion == 0) {
            // Inserción al inicio: la cabeza pasa a ser el nuevo nodo.
            nuevo.siguiente = this.head;
            this.head = nuevo;
        } else {
            // Ubicar el nodo ANTERIOR al punto de inserción.
            Nodo actual = this.head;
            for (int i = 0; i < posicion - 1; i++) {
                actual = actual.siguiente;
            }

            // Orden correcto: primero conectar el nuevo con el resto, luego el anterior al nuevo.
            nuevo.siguiente = actual.siguiente; // 1) nuevo hereda el resto
            actual.siguiente = nuevo;           // 2) el anterior enlaza al nuevo
        }

        // Se agregó exactamente un nodo, en todos los casos válidos.
        this.size++;
    }

    /**
     * Elimina la PRIMERA aparición del valor indicado en la lista.
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
                    // Nodo del medio o del final: el anterior se salta al eliminado.
                    anterior.siguiente = actual.siguiente;
                }

                // Se decrementa el tamaño tras una eliminación válida.
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
     * Elimina el nodo ubicado en la posición indicada (contando desde cero).
     *
     * <p>Si la posición es menor que cero o mayor o igual a {@code size}, se lanza
     * {@code IndexOutOfBoundsException}. Si la posición es cero, se actualiza {@code head}
     * para que apunte al segundo nodo. En caso contrario, se recorre hasta encontrar el
     * nodo anterior a la posición indicada y se enlaza este con el siguiente del nodo
     * a eliminar.</p>
     *
     * <p>El {@code size} se decrementa exactamente una sola vez después de una
     * eliminación válida.</p>
     *
     * @param posicion índice del nodo a eliminar (base cero); rango válido {@code [0, size - 1]}
     * @throws IndexOutOfBoundsException si {@code posicion < 0} o {@code posicion >= size}
     */
    public void eliminarEnPosicion(int posicion) {
        // Validación: las posiciones válidas son [0, size - 1].
        if (posicion < 0 || posicion >= this.size) {
            throw new IndexOutOfBoundsException(
                    "Posición fuera de rango: " + posicion + " (tamaño = " + this.size + ")");
        }

        if (posicion == 0) {
            // Eliminar la cabeza: la nueva cabeza es el siguiente nodo.
            this.head = this.head.siguiente;
        } else {
            // Recorrer hasta encontrar el nodo anterior a la posición indicada.
            Nodo anterior = this.head;
            for (int i = 0; i < posicion - 1; i++) {
                anterior = anterior.siguiente;
            }
            // El nodo a eliminar es anterior.siguiente; se salta enlazando con su siguiente.
            Nodo nodoAEliminar = anterior.siguiente;
            anterior.siguiente = nodoAEliminar.siguiente;
        }

        // Decrementar size una sola vez después de una eliminación válida.
        this.size--;
    }

    /**
     * Busca un valor dentro de la lista mediante un recorrido secuencial.
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
     * @param posicion índice del elemento a obtener (base cero)
     * @return el dato entero almacenado en la posición indicada
     * @throws IndexOutOfBoundsException si {@code posicion < 0} o {@code posicion >= size}
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
     * Devuelve una representación en cadena de la lista.
     *
     * @return representación en formato {@code [a -> b -> c]} o {@code []}
     */
    @Override
    public String toString() {
        if (estaVacia()) {
            return "[]";
        }

        StringJoiner joiner = new StringJoiner(" -> ", "[", "]");
        Nodo actual = this.head;
        while (actual != null) {
            joiner.add(String.valueOf(actual.dato));
            actual = actual.siguiente;
        }
        return joiner.toString();
    }

    /**
     * Imprime en la consola los elementos de la lista en orden, desde {@code head}
     * hasta el último nodo.
     */
    public void imprimir() {
        if (estaVacia()) {
            System.out.println("Lista vacía []");
            return;
        }
        System.out.println("Lista: " + toString() + "  (tamaño = " + this.size + ")");
    }

    /**
     * Método de prueba que ejercita {@code eliminarEnPosicion} y verifica la conservación
     * de las operaciones existentes.
     *
     * @param args no se utilizan
     */
    public static void main(String[] args) {
        // ---- Caso 1: eliminarEnPosicion en lista vacía ----
        System.out.println("== Caso 1: eliminarEnPosicion en lista vacía ==");
        ListaEnlazadaSimple vacia = new ListaEnlazadaSimple();
        try {
            vacia.eliminarEnPosicion(0);
            verificar(false, "Debería lanzar IndexOutOfBoundsException");
        } catch (IndexOutOfBoundsException e) {
            verificar(true, "Lanza IndexOutOfBoundsException correctamente (pos >= size)");
        }
        verificar(vacia.getSize() == 0 && vacia.estaVacia(), "Lista vacía permanece intacta");

        // ---- Caso 2: posiciones inválidas (< 0 o >= size) ----
        System.out.println("\n== Caso 2: posiciones inválidas ==");
        ListaEnlazadaSimple lista = new ListaEnlazadaSimple();
        lista.insertarAlFinal(1);
        lista.insertarAlFinal(2);
        lista.insertarAlFinal(3); // [1, 2, 3], size = 3
        try {
            lista.eliminarEnPosicion(-1);
            verificar(false, "Debería lanzar IndexOutOfBoundsException para posicion < 0");
        } catch (IndexOutOfBoundsException e) {
            verificar(true, "Lanza IndexOutOfBoundsException para posicion < 0");
        }
        try {
            lista.eliminarEnPosicion(3); // >= size (válidas 0,1,2)
            verificar(false, "Debería lanzar IndexOutOfBoundsException para posicion >= size");
        } catch (IndexOutOfBoundsException e) {
            verificar(true, "Lanza IndexOutOfBoundsException para posicion >= size");
        }
        verificar(lista.getSize() == 3 && lista.toString().equals("[1 -> 2 -> 3]"), "Lista no se modificó");

        // ---- Caso 3: eliminar el primer nodo (posición 0) ----
        System.out.println("\n== Caso 3: eliminar primer nodo (posición 0) ==");
        // Lista: [1, 2, 3]
        lista.eliminarEnPosicion(0);
        lista.imprimir(); // esperado: [2 -> 3]
        verificar(lista.getSize() == 2 && lista.obtener(0) == 2 && lista.obtener(1) == 3,
                "head actualizado a 2; enlaces correctos");

        // ---- Caso 4: eliminar nodo del medio ----
        System.out.println("\n== Caso 4: eliminar nodo del medio ==");
        // Lista: [2, 3] - insertar para tener medio
        lista.insertarAlFinal(4); // [2, 3, 4]
        lista.imprimir();
        // Eliminar posición 1 (valor 3, que está en el medio)
        lista.eliminarEnPosicion(1);
        lista.imprimir(); // esperado: [2 -> 4]
        verificar(lista.getSize() == 2 && lista.obtener(0) == 2 && lista.obtener(1) == 4,
                "Anterior (2) apunta a siguiente (4) tras eliminar posición 1");

        // ---- Caso 5: eliminar el último nodo ----
        System.out.println("\n== Caso 5: eliminar último nodo ==");
        // Lista: [2, 4]
        lista.eliminarEnPosicion(1);
        lista.imprimir(); // esperado: [2]
        verificar(lista.getSize() == 1 && lista.obtener(0) == 2,
                "Último nodo eliminado correctamente; size decrementado");

        // ---- Caso 6: eliminar hasta lista vacía y casos límite ----
        System.out.println("\n== Caso 6: eliminar único elemento (queda vacía) ==");
        lista.eliminarEnPosicion(0);
        lista.imprimir(); // esperado: Lista vacía []
        verificar(lista.estaVacia() && lista.getSize() == 0, "Lista queda vacía tras eliminar cabeza única");

        // ---- Caso 7: verificar operaciones existentes (insertarAlInicio, insertarAlFinal, insertarEnPosicion, buscar, eliminar por valor) ----
        System.out.println("\n== Caso 7: conservar operaciones existentes ==");
        ListaEnlazadaSimple control = new ListaEnlazadaSimple();
        control.insertarAlInicio(10);
        control.insertarAlInicio(-5);
        control.insertarAlFinal(7);
        control.insertarEnPosicion(0, 1); // [-5, 0, 10, 7]
        control.imprimir();
        verificar(control.buscar(10) && control.buscar(-5) && control.buscar(7) && control.buscar(0),
                "Búsqueda funciona correctamente");
        verificar(control.eliminar(10), "eliminar(valor) elimina primera aparición");
        verificar(!control.buscar(10) && control.getSize() == 3, "eliminar(valor) actualiza size/enlaces");
        control.imprimir(); // [-5, 0, 7]

        // ---- Caso 8: más pruebas con eliminarEnPosicion ----
        System.out.println("\n== Caso 8: eliminarEnPosicion en distintos puntos ==");
        ListaEnlazadaSimple l = new ListaEnlazadaSimple();
        for (int i = 0; i < 5; i++) {
            l.insertarAlFinal(i); // [0,1,2,3,4]
        }
        l.eliminarEnPosicion(2); // elimina 2 -> [0,1,3,4]
        verificar(l.toString().equals("[0 -> 1 -> 3 -> 4]") && l.getSize() == 4, "Elimina posición intermedia correctamente");
        l.eliminarEnPosicion(0); // elimina 0 -> [1,3,4]
        verificar(l.obtener(0) == 1 && l.getSize() == 3, "Elimina cabeza correctamente");
        l.eliminarEnPosicion(2); // elimina 4 (último) -> [1,3]
        verificar(l.obtener(1) == 3 && l.getSize() == 2, "Elimina último correctamente");
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

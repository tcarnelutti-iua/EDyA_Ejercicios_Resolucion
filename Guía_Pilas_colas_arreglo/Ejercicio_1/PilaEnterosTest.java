/**
 * Pruebas de comportamiento para {@link PilaEnteros}.
 *
 * <p>Se ejecutan como una aplicación de consola (método {@code main}) y no
 * dependen de librerías externas. Cada caso verifica uno de los
 * comportamientos esperados de la pila y se muestra como {@code OK} o
 * {@code FALLO}. Al finalizar se lanza {@link AssertionError} si algún caso
 * falló, de modo que el programa termine con código de salida distinto de 0.</p>
 */
public class PilaEnterosTest {

    /** Contador de pruebas ejecutadas. */
    private static int pruebas = 0;

    /** Contador de pruebas fallidas. */
    private static int fallos = 0;

    public static void main(String[] args) {
        // 1. Al crear una pila: vacía, no llena y tamaño 0.
        ejecutar("Pila recién creada: vacía, no llena y size() == 0", () -> {
            PilaEnteros pila = new PilaEnteros(3);
            afirmar(pila.isEmpty(), "isEmpty() debería ser true");
            afirmar(!pila.isFull(), "isFull() debería ser false");
            afirmarIgual(0, pila.size(), "size() debería ser 0");
        });

        // 2. Tras insertar 10 y 20: peek() == 20 y size() == 2.
        ejecutar("Tras push(10) y push(20): peek() == 20, size() == 2", () -> {
            PilaEnteros pila = new PilaEnteros(5);
            pila.push(10);
            pila.push(20);
            afirmarIgual(20, pila.peek(), "peek() debería devolver 20");
            afirmarIgual(2, pila.size(), "size() debería ser 2");
        });

        // 3. pop() devuelve 20 y luego peek() devuelve 10.
        ejecutar("pop() devuelve 20 y después peek() devuelve 10", () -> {
            PilaEnteros pila = new PilaEnteros(5);
            pila.push(10);
            pila.push(20);
            afirmarIgual(20, pila.pop(), "pop() debería devolver 20");
            afirmarIgual(10, pila.peek(), "peek() debería devolver 10");
            afirmarIgual(1, pila.size(), "size() debería ser 1 tras un pop");
        });

        // 4. Al llenar la pila, isFull() es verdadero.
        ejecutar("Pila llena: isFull() == true", () -> {
            PilaEnteros pila = new PilaEnteros(3);
            pila.push(1);
            pila.push(2);
            pila.push(3);
            afirmar(pila.isFull(), "isFull() debería ser true al llenar la capacidad");
            afirmar(!pila.isEmpty(), "isEmpty() debería ser false con elementos");
            afirmarIgual(3, pila.size(), "size() debería ser 3");
        });

        // 5. push en pila llena informa error y no cambia el contenido.
        ejecutar("push en pila llena: error y contenido intacto", () -> {
            PilaEnteros pila = new PilaEnteros(2);
            pila.push(10);
            pila.push(20);
            boolean error = false;
            try {
                pila.push(30);
            } catch (IllegalStateException ex) {
                error = true;
            }
            afirmar(error, "push en pila llena debería lanzar IllegalStateException");
            afirmarIgual(2, pila.size(), "size() no debe cambiar tras el overflow");
            afirmarIgual(20, pila.peek(), "peek() debe seguir mostrando 20");
        });

        // 6. Al vaciar la pila: isEmpty() y size() == 0.
        ejecutar("Al vaciar la pila: isEmpty() == true y size() == 0", () -> {
            PilaEnteros pila = new PilaEnteros(2);
            pila.push(10);
            pila.push(20);
            pila.pop();
            pila.pop();
            afirmar(pila.isEmpty(), "isEmpty() debería ser true tras vaciar");
            afirmarIgual(0, pila.size(), "size() debería ser 0 tras vaciar");
        });

        // 7a. pop() en pila vacía informa error descriptivo.
        ejecutar("pop() en pila vacía: error descriptivo", () -> {
            PilaEnteros pila = new PilaEnteros(2);
            boolean error = false;
            String mensaje = "";
            try {
                pila.pop();
            } catch (IllegalStateException ex) {
                error = true;
                mensaje = ex.getMessage();
            }
            afirmar(error, "pop en pila vacía debería lanzar IllegalStateException");
            afirmar(mensaje != null && !mensaje.isEmpty(),
                    "el mensaje de error debe ser descriptivo");
        });

        // 7b. peek() en pila vacía informa error descriptivo.
        ejecutar("peek() en pila vacía: error descriptivo", () -> {
            PilaEnteros pila = new PilaEnteros(2);
            boolean error = false;
            String mensaje = "";
            try {
                pila.peek();
            } catch (IllegalStateException ex) {
                error = true;
                mensaje = ex.getMessage();
            }
            afirmar(error, "peek en pila vacía debería lanzar IllegalStateException");
            afirmar(mensaje != null && !mensaje.isEmpty(),
                    "el mensaje de error debe ser descriptivo");
        });

        // 8. Capacidad inválida: rechazada con error descriptivo.
        ejecutar("Capacidad <= 0: rechazada con IllegalArgumentException", () -> {
            boolean errorCero = false;
            boolean errorNegativa = false;
            try {
                new PilaEnteros(0);
            } catch (IllegalArgumentException ex) {
                errorCero = true;
            }
            try {
                new PilaEnteros(-5);
            } catch (IllegalArgumentException ex) {
                errorNegativa = true;
            }
            afirmar(errorCero, "capacidad 0 debería lanzar IllegalArgumentException");
            afirmar(errorNegativa, "capacidad negativa debería lanzar IllegalArgumentException");
        });

        // Resumen final.
        System.out.println("----------------------------------------");
        System.out.printf("Pruebas ejecutadas: %d | Fallos: %d%n", pruebas, fallos);
        if (fallos > 0) {
            throw new AssertionError("Se produjeron " + fallos + " fallo(s) en las pruebas.");
        }
        System.out.println("Todas las pruebas pasaron.");
    }

    // ------------------------------------------------------------------
    // Infraestructura mínima de pruebas
    // ------------------------------------------------------------------

    /**
     * Ejecuta una prueba capturando cualquier excepción para no detener el resto.
     *
     * @param nombre descripción del caso de prueba
     * @param prueba bloque de código que contiene las verificaciones
     */
    private static void ejecutar(String nombre, Runnable prueba) {
        pruebas++;
        try {
            prueba.run();
            System.out.println("OK    - " + nombre);
        } catch (Throwable t) {
            fallos++;
            System.out.println("FALLO - " + nombre + " -> " + t.getMessage());
        }
    }

    /** Verifica que una condición sea verdadera. */
    private static void afirmar(boolean condicion, String mensaje) {
        if (!condicion) {
            throw new AssertionError(mensaje);
        }
    }

    /** Verifica que dos enteros sean iguales. */
    private static void afirmarIgual(int esperado, int real, String mensaje) {
        if (esperado != real) {
            throw new AssertionError(mensaje + " (esperado=" + esperado + ", real=" + real + ")");
        }
    }
}

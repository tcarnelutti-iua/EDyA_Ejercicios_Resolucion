/*
 * ============================================================================
 * PROMPT INICIAL UTILIZADO PARA GENERAR ESTA SOLUCIÓN
 * ============================================================================
 *
 * Quiero implementar en Java un algoritmo de búsqueda binaria que encuentre un
 * número entero en un vector ordenado de menor a mayor.
 *
 * Usá búsqueda binaria porque, al estar ordenados los datos, en cada paso se
 * puede comparar el valor buscado con el elemento central y descartar la mitad
 * del intervalo que no puede contenerlo. Esto resulta más eficiente que una
 * búsqueda lineal: para un vector de tamaño n, la búsqueda binaria tiene
 * complejidad temporal O(log n), mientras que la búsqueda lineal puede requerir
 * O(n) comparaciones. Implementá una versión iterativa, con complejidad espacial
 * auxiliar O(1).
 *
 * La función debe recibir el vector y el valor buscado, y devolver el índice
 * donde se encuentra o -1 si no está. Documentá que el vector debe estar
 * ordenado de menor a mayor. Si hay valores repetidos, definí y documentá si se
 * devuelve cualquier coincidencia o la primera. Para un vector vacío, debe
 * devolverse -1.
 *
 * Comentá el código y hacé que el programa muestre paso a paso la evolución de
 * las variables de búsqueda en cada iteración: los límites inicio y fin, el
 * índice medio, el valor en esa posición y cómo se actualiza el intervalo.
 * Mostrá también el resultado final.
 *
 * Incluí pruebas para un valor en el medio, en los extremos, un valor
 * inexistente, un vector vacío y un vector con valores repetidos.
 * ============================================================================
 */

/**
 * Búsqueda binaria <b>iterativa</b> de un número entero dentro de un vector
 * ordenado de menor a mayor.
 *
 * <p><b>Requisito de la entrada:</b> el vector debe estar ordenado de forma
 * ascendente (de menor a mayor). Si no lo estuviera, el algoritmo puede
 * descartar mitades que sí contienen el valor y devolver un resultado
 * incorrecto. Este método <b>no</b> ordena ni valida el orden del vector: es
 * responsabilidad del llamador garantizar esa precondición.</p>
 *
 * <p><b>Estrategia:</b> se mantiene un intervalo de búsqueda
 * {@code [inicio, fin]}. En cada iteración se calcula el índice central
 * {@code medio} y se compara {@code vector[medio]} con el valor buscado:
 * <ul>
 *   <li>si coinciden, se devuelve {@code medio};</li>
 *   <li>si {@code vector[medio]} es menor, se descarta la mitad izquierda
 *       ({@code inicio = medio + 1});</li>
 *   <li>si es mayor, se descarta la mitad derecha ({@code fin = medio - 1}).</li>
 * </ul>
 * El proceso termina cuando se encuentra el valor o cuando el intervalo queda
 * vacío ({@code inicio > fin}), en cuyo caso se devuelve {@code -1}.</p>
 *
 * <p><b>Valores repetidos:</b> si el vector contiene el valor buscado varias
 * veces, este método devuelve el índice de <b>cualquier</b> aparición (no se
 * garantiza que sea la primera ni la última). La parada en la primera
 * coincidencia es lo que mantiene el costo en {@code O(log n)}.</p>
 *
 * <p><b>Comportamiento con vector vacío:</b> se devuelve {@code -1}. Como el
 * intervalo inicial es vacío, el bucle no se ejecuta y nunca se accede a una
 * posición inexistente.</p>
 *
 * <p><b>Complejidad temporal:</b> {@code O(log n)} en todos los casos (mejor,
 * promedio y peor), porque en cada iteración el intervalo se reduce a la mitad.
 * <b>Complejidad espacial auxiliar:</b> {@code O(1)}, ya que solo se emplean
 * unas pocas variables enteras ({@code inicio}, {@code fin}, {@code medio},
 * {@code iteracion}) independientemente del tamaño del vector.</p>
 *
 * @author Agente Desarrollador
 */
public final class BusquedaBinaria {

    /** Impide instanciar una clase que solo agrupa métodos estáticos. */
    private BusquedaBinaria() {
    }

    // ------------------------------------------------------------------------
    // API PÚBLICA
    // ------------------------------------------------------------------------

    /**
     * Busca {@code valorBuscado} en {@code vector} mediante búsqueda binaria,
     * sin imprimir la traza de ejecución.
     *
     * @param vector       vector ordenado de menor a mayor; no puede ser
     *                     {@code null}
     * @param valorBuscado valor entero que se desea localizar
     * @return el índice de una aparición de {@code valorBuscado}, o {@code -1}
     *         si no está presente (incluido el caso de un vector vacío)
     * @throws IllegalArgumentException si {@code vector} es {@code null}
     */
    public static int buscar(int[] vector, int valorBuscado) {
        return buscarInterno(vector, valorBuscado, false);
    }

    /**
     * Igual que {@link #buscar(int[], int)}, pero además imprime paso a paso la
     * evolución de las variables de búsqueda (límites {@code inicio} y
     * {@code fin}, índice {@code medio}, valor en esa posición y actualización
     * del intervalo) y el resultado final.
     *
     * @param vector       vector ordenado de menor a mayor; no puede ser
     *                     {@code null}
     * @param valorBuscado valor entero que se desea localizar
     * @return el índice de una aparición de {@code valorBuscado}, o {@code -1}
     *         si no está presente
     * @throws IllegalArgumentException si {@code vector} es {@code null}
     */
    public static int buscarConTraza(int[] vector, int valorBuscado) {
        return buscarInterno(vector, valorBuscado, true);
    }

    // ------------------------------------------------------------------------
    // IMPLEMENTACIÓN
    // ------------------------------------------------------------------------

    /**
     * Núcleo de la búsqueda binaria. Centraliza la lógica y decide si se emite
     * la traza, evitando duplicar el algoritmo entre la versión "silenciosa" y
     * la versión con seguimiento.
     *
     * @param vector       vector ordenado de menor a mayor; no puede ser
     *                     {@code null}
     * @param valorBuscado valor a localizar
     * @param mostrarTraza {@code true} para imprimir la evolución paso a paso
     * @return el índice de una aparición del valor, o {@code -1} si no está
     * @throws IllegalArgumentException si {@code vector} es {@code null}
     */
    private static int buscarInterno(int[] vector, int valorBuscado, boolean mostrarTraza) {
        // --------------------------------------------------------------------
        // VALIDACIÓN DE CASOS LÍMITE
        // Un vector nulo no es una entrada procesable (error de programación),
        // por eso se rechaza con una excepción descriptiva. Un vector VACÍO, en
        // cambio, es una entrada válida: simplemente no contiene el valor y el
        // resultado será -1 (se gestiona con la condición del bucle).
        // --------------------------------------------------------------------
        if (vector == null) {
            throw new IllegalArgumentException("El vector no puede ser null.");
        }

        // --------------------------------------------------------------------
        // INICIALIZACIÓN DEL INTERVALO
        // El intervalo de búsqueda es [inicio, fin], cerrado en ambos extremos.
        // Al comenzar, abarca todo el vector. Con un vector vacío se tiene
        // inicio = 0 y fin = -1, de modo que el intervalo ya nace vacío.
        // --------------------------------------------------------------------
        int inicio = 0;
        int fin = vector.length - 1;
        int iteracion = 0;

        if (mostrarTraza) {
            System.out.printf("  Vector (%d elementos): %s%n", vector.length, formatear(vector));
            System.out.printf("  Valor buscado: %d%n", valorBuscado);
            if (vector.length == 0) {
                System.out.println("  El vector está vacío: no hay elementos que comparar.");
            } else {
                System.out.printf("  Estado inicial: inicio=%d, fin=%d%n", inicio, fin);
            }
        }

        // --------------------------------------------------------------------
        // BUCLE PRINCIPAL
        // Condición: mientras el intervalo [inicio, fin] contenga al menos una
        // posición (inicio <= fin). Cada iteración reduce el intervalo a la
        // mitad, por lo que se realizan como máximo O(log n) iteraciones.
        // --------------------------------------------------------------------
        while (inicio <= fin) {
            // Cálculo del punto medio evitando el desbordamiento de enteros:
            // "inicio + (fin - inicio) / 2" es equivalente a "(inicio + fin) / 2"
            // pero nunca desborda, aun cuando inicio y fin sean muy grandes.
            int medio = inicio + (fin - inicio) / 2;
            int valorMedio = vector[medio];
            iteracion++;

            if (mostrarTraza) {
                System.out.printf("  Iteración %d: inicio=%d, fin=%d, medio=%d, vector[%d]=%d%n",
                        iteracion, inicio, fin, medio, medio, valorMedio);
            }

            if (valorMedio == valorBuscado) {
                // ------------------------------------------------------------
                // CASO 1: coincidencia.
                // Se devuelve el índice hallado. Si el valor está repetido, este
                // es el índice de UNA de sus apariciones (no necesariamente la
                // primera). Se detiene la búsqueda para conservar O(log n).
                // ------------------------------------------------------------
                if (mostrarTraza) {
                    System.out.printf(
                            "    -> vector[%d] == %d: ¡coincidencia! Se detiene la búsqueda.%n",
                            medio, valorBuscado);
                }
                return medio;

            } else if (valorMedio < valorBuscado) {
                // ------------------------------------------------------------
                // CASO 2: el valor central es MENOR que el buscado.
                // Por estar el vector ordenado de menor a mayor, todo el sector
                // izquierdo (índices <= medio) es <= vector[medio] < buscado, así
                // que no puede contenerlo. Se DESCARTA la mitad izquierda
                // desplazando 'inicio' a medio + 1.
                // ------------------------------------------------------------
                if (mostrarTraza) {
                    System.out.printf(
                            "    -> vector[%d] (%d) < %d: el valor, si existe, está a la derecha."
                                    + " Nuevo intervalo: inicio=%d, fin=%d.%n",
                            medio, valorMedio, valorBuscado, medio + 1, fin);
                }
                inicio = medio + 1;

            } else {
                // ------------------------------------------------------------
                // CASO 3: el valor central es MAYOR que el buscado.
                // Todo el sector derecho (índices >= medio) es >= vector[medio]
                // > buscado, así que no puede contenerlo. Se DESCARTA la mitad
                // derecha retrocediendo 'fin' a medio - 1.
                // ------------------------------------------------------------
                if (mostrarTraza) {
                    System.out.printf(
                            "    -> vector[%d] (%d) > %d: el valor, si existe, está a la izquierda."
                                    + " Nuevo intervalo: inicio=%d, fin=%d.%n",
                            medio, valorMedio, valorBuscado, inicio, medio - 1);
                }
                fin = medio - 1;
            }
        }

        // --------------------------------------------------------------------
        // NO ENCONTRADO
        // Se salió del bucle porque el intervalo quedó vacío (inicio > fin): ya
        // no quedan posiciones candidatas. Se devuelve -1. Esto cubre también el
        // caso del vector vacío (0 iteraciones).
        // --------------------------------------------------------------------
        if (mostrarTraza) {
            if (iteracion == 0) {
                System.out.println(
                        "  Resultado: no encontrado (el intervalo inicial ya estaba vacío)."
                                + " Se devuelve -1.");
            } else {
                System.out.printf("  Iteración final: inicio=%d, fin=%d -> el intervalo quedó vacío.%n",
                        inicio, fin);
                System.out.printf("  Resultado: no encontrado tras %d iteración(es). Se devuelve -1.%n",
                        iteracion);
            }
        }
        return -1;
    }

    // ------------------------------------------------------------------------
    // CASOS DE PRUEBA
    // ------------------------------------------------------------------------

    /**
     * Ejecuta los casos de prueba solicitados en el prompt.
     *
     * @param args no se utilizan
     */
    public static void main(String[] args) {
        System.out.println("============================================================");
        System.out.println(" BÚSQUEDA BINARIA (ITERATIVA) EN UN VECTOR ORDENADO");
        System.out.println("============================================================");

        int[] ordenado = {2, 4, 6, 8, 10, 12, 14};

        System.out.println();
        System.out.println("--- 1) Valor en el medio ---");
        probar(ordenado, 8, 3);

        System.out.println();
        System.out.println("--- 2) Valores en los extremos ---");
        probar(ordenado, 2, 0);    // primer elemento
        probar(ordenado, 14, 6);   // último elemento

        System.out.println();
        System.out.println("--- 3) Valores inexistentes ---");
        probar(new int[] {1, 3, 5, 7, 9}, 6, -1);   // ausente en el interior
        probar(ordenado, -1, -1);                   // menor que todos
        probar(ordenado, 100, -1);                  // mayor que todos

        System.out.println();
        System.out.println("--- 4) Vector vacío ---");
        probar(new int[] {}, 5, -1);

        System.out.println();
        System.out.println("--- 5) Vector con valores repetidos ---");
        probarValorRepetido(new int[] {1, 2, 2, 2, 3}, 2);

        System.out.println();
        System.out.println("--- Casos de borde adicionales ---");
        probar(new int[] {42}, 42, 0);                          // un solo elemento, presente
        probar(new int[] {42}, 7, -1);                          // un solo elemento, ausente
        probar(new int[] {-10, -5, 0, 5, 10}, -5, 1);           // valores negativos
        probar(new int[] {-7, -3, -1}, -3, 1);                  // todos negativos
        probar(new int[] {0, 1, 2, 3, 4}, 0, 0);                // primera posición (valor 0)
        probar(new int[] {Integer.MIN_VALUE, -1, 0, 1, Integer.MAX_VALUE},
                Integer.MAX_VALUE, 4);                          // límites del tipo int
        probarVectorNulo();

        System.out.println();
        System.out.println("Fin de las pruebas.");
    }

    /**
     * Ejecuta un caso de prueba con un índice esperado exacto, mostrando la
     * traza de la búsqueda y comparando el resultado obtenido con el esperado.
     *
     * @param vector         entrada de la función
     * @param valorBuscado   valor a buscar
     * @param indiceEsperado índice esperado, o {@code -1} si no debe encontrarse
     */
    private static void probar(int[] vector, int valorBuscado, int indiceEsperado) {
        String esperado = (indiceEsperado == -1)
                ? "no encontrado (-1)"
                : "índice " + indiceEsperado;
        System.out.printf(">>> buscar(%s, %d)  [esperado: %s]%n",
                formatear(vector), valorBuscado, esperado);

        int obtenido = buscarConTraza(vector, valorBuscado);

        boolean correcto = (obtenido == indiceEsperado);
        String resultado = (obtenido == -1)
                ? "no encontrado (-1)"
                : "índice " + obtenido;
        System.out.printf("  >> Resultado final: %s | %s%n",
                resultado, correcto ? "OK" : "FALLO");
    }

    /**
     * Ejecuta un caso de prueba con valores repetidos. Como el método devuelve
     * cualquier aparición, la verificación consiste en comprobar que el índice
     * retornado es válido y que el elemento en esa posición coincide con el
     * valor buscado.
     *
     * @param vector       entrada de la función
     * @param valorBuscado valor repetido que se desea localizar
     */
    private static void probarValorRepetido(int[] vector, int valorBuscado) {
        System.out.printf(
                ">>> buscar(%s, %d)  [se acepta cualquier aparición]%n",
                formatear(vector), valorBuscado);

        int obtenido = buscarConTraza(vector, valorBuscado);

        boolean correcto = obtenido >= 0
                && obtenido < vector.length
                && vector[obtenido] == valorBuscado;
        String resultado = (obtenido == -1)
                ? "no encontrado (-1)"
                : "índice " + obtenido;
        System.out.printf("  >> Resultado final: %s | %s%n",
                resultado, correcto ? "OK" : "FALLO");
    }

    /**
     * Verifica que un vector {@code null} sea rechazado con la excepción
     * documentada.
     */
    private static void probarVectorNulo() {
        System.out.printf(">>> buscar(null, 5)  [esperado: rechazo con excepción]%n");
        try {
            buscar(null, 5);
            System.out.println("  >> FALLO (no se lanzó ninguna excepción)");
        } catch (IllegalArgumentException excepcion) {
            System.out.printf("  >> OK (rechazado: %s)%n", excepcion.getMessage());
        }
    }

    /**
     * Construye una representación legible del vector para la salida de pruebas.
     *
     * @param vector vector a representar
     * @return cadena con los elementos entre corchetes
     */
    private static String formatear(int[] vector) {
        StringBuilder texto = new StringBuilder("[");
        for (int i = 0; i < vector.length; i++) {
            if (i > 0) {
                texto.append(", ");
            }
            texto.append(vector[i]);
        }
        return texto.append("]").toString();
    }
}

/*
 * ============================================================================
 * NOTAS DE IMPLEMENTACIÓN (aclaraciones al prompt inicial)
 * ============================================================================
 * 1. Precondición de orden: el vector DEBE estar ordenado de menor a mayor. El
 *    algoritmo no lo verifica ni lo ordena, porque comprobarlo o reordenarlo
 *    costaría O(n) o O(n log n), arruinando la ventaja de la búsqueda binaria.
 *
 * 2. Iteratividad y espacio: la versión es iterativa (un solo bucle while), por
 *    lo que no consume pila de recursión. El espacio auxiliar es O(1): solo las
 *    variables inicio, fin, medio, valorMedio e iteracion.
 *
 * 3. Complejidad temporal O(log n): en cada iteración el intervalo se reduce
 *    aproximadamente a la mitad. Aun en el peor caso (valor ausente o en un
 *    extremo) el número de iteraciones es del orden de log2(n). Para n = 1.000.000
 *    son unas 20 comparaciones, frente a hasta 1.000.000 en una búsqueda lineal.
 *
 * 4. Cálculo seguro del medio: se usa inicio + (fin - inicio) / 2 en lugar de
 *    (inicio + fin) / 2 para evitar el desbordamiento de enteros cuando los
 *    índices son muy grandes.
 *
 * 5. Valores repetidos: se devuelve CUALQUIER aparición del valor, no
 *    necesariamente la primera. Si se necesitara la primera (o la última)
 *    aparición, habría que seguir buscando dentro de la mitad correspondiente
 *    tras encontrar una coincidencia; sigue siendo O(log n), pero requiere un
 *    ajuste adicional que aquí no se implementa porque el enunciado admite
 *    cualquier coincidencia.
 *
 * 6. Vector vacío: comportamiento definido como "no encontrado" (-1). El bucle
 *    no se ejecuta porque inicio = 0 y fin = -1, de modo que nunca se accede a
 *    una posición inexistente (no hay ArrayIndexOutOfBoundsException).
 *
 * 7. Vector nulo: se rechaza con IllegalArgumentException porque no es una
 *    entrada procesable. A diferencia del vector vacío (entrada válida sin el
 *    valor), un null señala un error de programación.
 * ============================================================================
 */

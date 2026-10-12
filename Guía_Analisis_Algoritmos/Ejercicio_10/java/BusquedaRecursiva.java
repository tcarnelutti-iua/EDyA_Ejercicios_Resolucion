/*
 * ============================================================================
 * PROMPT INICIAL UTILIZADO PARA GENERAR ESTA SOLUCIÓN
 * ============================================================================
 *
 * Quiero implementar en Java una función recursiva que busque un número dentro
 * de un arreglo de enteros, sin usar ciclos (for ni while).
 *
 * La búsqueda debe comenzar en la posición 0. En cada llamada, compara el
 * elemento de la posición actual con el valor buscado. Si no coincide, avanza
 * recursivamente a la posición siguiente, indice + 1.
 *
 * Hay dos casos base:
 *   - Si el elemento actual coincide con el valor buscado, devuelve su índice.
 *   - Si el índice llega al final del arreglo, devuelve -1 para indicar que el
 *     valor no está presente.
 *
 * La función debe devolver el índice de la primera coincidencia o -1 si no
 * encuentra el número. Comprueba que no se haya llegado al final antes de
 * acceder al arreglo y rechaza null con un error descriptivo.
 *
 * Prueba estos casos:
 *   - Buscar un valor en la primera posición: devuelve 0.
 *   - Buscar un valor en el medio: devuelve su índice.
 *   - Buscar un valor en la última posición: devuelve su índice.
 *   - Buscar un valor ausente: devuelve -1.
 *   - Buscar en un arreglo vacío: devuelve -1.
 *   - Buscar un valor repetido: devuelve el índice de la primera coincidencia.
 * ============================================================================
 */


/**
 * Búsqueda recursiva de un valor en un arreglo de enteros.
 *
 * <p>La búsqueda se realiza de forma recursiva, comenzando en el índice 0.
 * En cada llamada se compara el elemento de la posición actual con el valor
 * buscado. Si coinciden, se devuelve el índice actual (primera coincidencia).
 * Si no coinciden, se continúa la búsqueda en {@code indice + 1}. Si el índice
 * alcanza o supera la longitud del arreglo, significa que se llegó al final sin
 * encontrar el valor y se devuelve {@code -1}.</p>
 *
 * <p>La verificación de que no se ha llegado al final se realiza <b>antes</b> de
 * acceder al arreglo, evitando así un {@link ArrayIndexOutOfBoundsException}.
 * Además, se rechaza la referencia {@code null} con un mensaje descriptivo.</p>
 *
 * <p>No se utilizan bucles {@code for} ni {@code while}; toda la iteración se
 * realiza mediante recursión.</p>
 *
 * @author Agente Desarrollador
 */
public final class BusquedaRecursiva {

    /** Impide instanciar una clase que solo agrupa métodos estáticos. */
    private BusquedaRecursiva() {
    }

    // ------------------------------------------------------------------------
    // API PÚBLICA
    // ------------------------------------------------------------------------

    /**
     * Busca recursivamente el valor {@code valorBuscado} dentro del arreglo
     * {@code arreglo}.
     *
     * <p>La búsqueda comienza en la posición 0 y devuelve el índice de la
     * <b>primera coincidencia</b> encontrada. Si el valor no está presente
     * en el arreglo, devuelve {@code -1}.</p>
     *
     * <p>Orden de evaluación:</p>
     * <ol>
     *   <li>Se valida que el arreglo no sea {@code null}.</li>
     *   <li>Se comprueba que el índice actual no haya llegado al final antes de
     *       acceder a los elementos del arreglo.</li>
     *   <li>Si el elemento actual coincide con {@code valorBuscado}, se devuelve
     *       su índice inmediatamente.</li>
     *   <li>Si no coincide, se busca recursivamente en {@code indice + 1}.</li>
     * </ol>
     *
     * @param arreglo       arreglo de enteros en el que se realizará la búsqueda;
     *                      no puede ser {@code null}
     * @param valorBuscado  valor a buscar dentro del arreglo
     * @return índice de la primera coincidencia, o {@code -1} si el valor no
     *         se encuentra en el arreglo
     * @throws IllegalArgumentException si {@code arreglo} es {@code null}
     */
    public static int buscar(int[] arreglo, int valorBuscado) {
        // --------------------------------------------------------------------
        // VALIDACIÓN: una referencia nula no puede recorrerse ni consultarse.
        // Se rechaza con un mensaje descriptivo (caso límite pedido).
        // --------------------------------------------------------------------
        if (arreglo == null) {
            throw new IllegalArgumentException(
                    "El arreglo no puede ser nulo (null).");
        }

        // Se delega en el núcleo recursivo comenzando en el índice 0.
        // Esta es la entrada a la recursión.
        return buscarRecursivo(arreglo, valorBuscado, 0);
    }

    // ------------------------------------------------------------------------
    // NÚCLEO RECURSIVO
    // ------------------------------------------------------------------------

    /**
     * Núcleo recursivo para la búsqueda de un valor en un arreglo.
     *
     * <p>En cada llamada se siguen estrictamente las reglas:</p>
     * <ul>
     *   <li><b>Antes de acceder al arreglo:</b> se verifica si el índice ha
     *       llegado al final. Si {@code indice >= arreglo.length}, se devuelve
     *       {@code -1} (caso base: no encontrado).</li>
     *   <li><b>Comparación:</b> si el elemento en la posición actual coincide
     *       con {@code valorBuscado}, se devuelve {@code indice} (caso base:
     *       primera coincidencia encontrada).</li>
     *   <li><b>Avance recursivo:</b> si no coincide, se llama recursivamente con
     *       {@code indice + 1}, reduciendo así el problema hacia el final del
     *       arreglo.</li>
     * </ul>
     *
     * @param arreglo       arreglo de enteros (nunca {@code null})
     * @param valorBuscado  valor a buscar
     * @param indice        índice actual desde el cual se realiza la búsqueda
     * @return índice de la primera coincidencia, o {@code -1} si no se encuentra
     */
    private static int buscarRecursivo(int[] arreglo, int valorBuscado, int indice) {
        // --------------------------------------------------------------------
        // CASO BASE 1: Se llegó al final del arreglo sin encontrar coincidencia.
        //
        // La condición se evalúa ANTES de acceder a arreglo[indice] para evitar
        // ArrayIndexOutOfBoundsException. Esto cubre también el caso de un
        // arreglo vacío: si arreglo.length == 0, la primera llamada tiene
        // indice == 0, por lo que indice >= arreglo.length (0 >= 0) es verdadero
        // y se devuelve -1 inmediatamente.
        //
        // Este es el punto donde la recursión se detiene sin encontrar el valor.
        // --------------------------------------------------------------------
        if (indice >= arreglo.length) {
            return -1;
        }

        // --------------------------------------------------------------------
        // CASO BASE 2: El elemento actual coincide con el valor buscado.
        //
        // Como buscamos la PRIMERA coincidencia, al encontrarla devolvemos el
        // índice actual sin realizar más llamadas recursivas hacia adelante.
        // Esto cumple con el requisito: "devuelve el índice de la primera
        // coincidencia".
        // --------------------------------------------------------------------
        if (arreglo[indice] == valorBuscado) {
            return indice;
        }

        // --------------------------------------------------------------------
        // CASO RECURSIVO: El elemento actual no coincide con el valor buscado.
        //
        // Se avanza recursivamente a la siguiente posición (indice + 1). En cada
        // llamada el problema se reduce acercando el índice al final del arreglo,
        // hasta que se alcance alguno de los casos base:
        //   - se encuentra una coincidencia (CASO BASE 2) -> se devuelve ese índice
        //   - se llega al final del arreglo (CASO BASE 1) -> se devuelve -1
        //
        // LA PILA DE LLAMADAS: cada invocación queda "esperando" el resultado de
        // buscarRecursivo(arreglo, valorBuscado, indice + 1). Si en alguna
        // llamada más profunda se encuentra el valor, ese índice se propaga
        // hacia arriba a través de la pila (desenrollado) hasta la llamada
        // inicial. Si ninguna llamada encuentra el valor, el -1 del CASO BASE 1
        // se propaga hacia arriba.
        //
        // Ejemplo para buscar 7 en [1, 3, 7, 9] (índices 0-3):
        //   buscarRecursivo(..., 0): arreglo[0]=1 != 7 -> llama a (1)
        //   buscarRecursivo(..., 1): arreglo[1]=3 != 7 -> llama a (2)
        //   buscarRecursivo(..., 2): arreglo[2]=7 == 7 -> CASO BASE 2: return 2
        //   ...la pila se desenrolla y devuelve 2 hacia la llamada inicial
        //
        // Ejemplo para arreglo vacío [] (longitud 0):
        //   buscarRecursivo(..., 0): indice (0) >= length (0) -> CASO BASE 1: return -1
        //
        // Ejemplo para valor repetido buscar 5 en [5, 2, 5]:
        //   índice 0: arreglo[0]==5 -> devuelve 0 INMEDIATAMENTE (primera coincidencia)
        // --------------------------------------------------------------------
        return buscarRecursivo(arreglo, valorBuscado, indice + 1);
    }

    // ------------------------------------------------------------------------
    // CASOS DE PRUEBA
    // ------------------------------------------------------------------------

    /**
     * Método de entrada con los casos de prueba solicitados en el prompt.
     *
     * @param args argumentos opcionales (no se utilizan)
     */
    public static void main(String[] args) {
        System.out.println("== Casos de prueba solicitados ==");
        
        // - Buscar un valor en la primera posición: devuelve 0.
        probar(new int[]{1, 2, 3, 4}, 1, 0);
        
        // - Buscar un valor en el medio: devuelve su índice.
        probar(new int[]{10, 20, 30, 40, 50}, 30, 2);
        
        // - Buscar un valor en la última posición: devuelve su índice.
        probar(new int[]{5, 6, 7, 8}, 8, 3);
        
        // - Buscar un valor ausente: devuelve -1.
        probar(new int[]{1, 2, 3}, 99, -1);
        
        // - Buscar en un arreglo vacío: devuelve -1.
        probar(new int[]{}, 5, -1);
        
        // - Buscar un valor repetido: devuelve el índice de la primera coincidencia.
        probar(new int[]{7, 1, 7, 3, 7}, 7, 0);

        System.out.println();
        System.out.println("== Casos de borde adicionales ==");
        probar(new int[]{42}, 42, 0);           // Un solo elemento presente
        probar(new int[]{42}, 99, -1);          // Un solo elemento ausente
        probar(new int[]{2, 4, 6, 8}, 6, 2);   // Valor en medio exacto
        probar(new int[]{-1, -2, -3}, -2, 1);  // Valores negativos
        probar(new int[]{5}, 5, 0);             // Último y único elemento

        // Validación de entrada nula: debe rechazarse con excepción descriptiva.
        probarArregloNulo();
    }

    /**
     * Ejecuta un caso de prueba y muestra si el resultado coincide con lo esperado.
     *
     * @param arreglo  entrada de la función
     * @param valor    valor buscado
     * @param esperado resultado esperado
     */
    private static void probar(int[] arreglo, int valor, int esperado) {
        int obtenido = buscar(arreglo, valor);
        boolean correcto = obtenido == esperado;
        System.out.printf("buscar(%s, %d) = %-3d | esperado = %-3d | %s%n",
                formatearArreglo(arreglo),
                valor,
                obtenido,
                esperado,
                correcto ? "OK" : "FALLO");
    }

    /**
     * Verifica que el arreglo {@code null} sea rechazado con la excepción esperada.
     */
    private static void probarArregloNulo() {
        try {
            buscar(null, 10);
            System.out.println("buscar(null, 10) -> FALLO (no se lanzó ninguna excepción)");
        } catch (IllegalArgumentException excepcion) {
            System.out.printf("buscar(null, 10) -> OK (rechazado: %s)%n",
                    excepcion.getMessage());
        }
    }

    /**
     * Formatea un arreglo de enteros para mostrarlo en la salida de pruebas.
     *
     * @param arreglo arreglo a formatear
     * @return representación textual del arreglo (ej.: [1, 2, 3] o [])
     */
    private static String formatearArreglo(int[] arreglo) {
        if (arreglo == null) {
            return "null";
        }
        if (arreglo.length == 0) {
            return "[]";
        }
        
        StringBuilder sb = new StringBuilder();
        sb.append("[");
        for (int i = 0; i < arreglo.length; i++) {
            sb.append(arreglo[i]);
            if (i < arreglo.length - 1) {
                sb.append(", ");
            }
        }
        sb.append("]");
        return sb.toString();
    }
}

/*
 * ============================================================================
 * NOTAS DE IMPLEMENTACIÓN
 * ============================================================================
 * 1. La verificación de fin de arreglo se realiza ANTES de acceder a
 *    arreglo[indice]: `if (indice >= arreglo.length) return -1;`. Esto evita
 *    ArrayIndexOutOfBoundsException y cubre correctamente el caso de arreglo
 *    vacío (length == 0, indice == 0 -> condición verdadera).
 *
 * 2. Al encontrar una coincidencia se devuelve inmediatamente el índice actual
 *    sin realizar llamadas adicionales. Esto garantiza que se devuelve la
 *    PRIMERA coincidencia, cumpliendo con lo solicitado.
 *
 * 3. No se utilizan ciclos (for/while). Toda la iteración se logra mediante
 *    recursión: en cada paso se avanza con `indice + 1`.
 *
 * 4. La validación de null se realiza una única vez en el método público
 *    `buscar()`. El núcleo recursivo asume que el arreglo ya es no nulo.
 *
 * 5. Análisis de complejidad:
 *    - Tiempo: O(n) en el peor caso (valor ausente o presente al final).
 *              O(1) en el mejor caso (valor presente en la primera posición).
 *    - Espacio: O(n) debido a la pila de llamadas recursivas. En el peor caso
 *              se realizan hasta n llamadas (una por elemento).
 *    - Profundidad de recursión: n (máximo igual a la longitud del arreglo).
 *
 * 6. Para arreglos muy grandes, la recursión podría generar
 *    StackOverflowError en Java (límite de pila). Esto es inherente a la
 *    recursión; el ejercicio solicita explícitamente usar recursión sin ciclos,
 *    por lo que esta implementación es correcta según lo pedido.
 * ============================================================================
 */

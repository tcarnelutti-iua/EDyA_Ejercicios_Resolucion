/*
 * ============================================================================
 * PROMPT INICIAL UTILIZADO PARA GENERAR ESTA SOLUCIÓN
 * ============================================================================
 *
 * Quiero implementar en Java un algoritmo que cuente cuántas veces aparece un
 * valor entero determinado dentro de un vector.
 *
 * Recorré el vector completo y compará cada elemento con el valor buscado,
 * incrementando un contador cada vez que coincidan. Es necesario examinar todas
 * las posiciones porque cualquier elemento restante podría ser otra aparición
 * del valor; por eso no se puede detener el recorrido al encontrar la primera
 * coincidencia.
 *
 * Analizá la complejidad: el algoritmo tiene tiempo O(n), ya que examina los
 * n elementos exactamente una vez, y espacio auxiliar O(1), porque solo
 * necesita un contador y unas pocas variables, independientemente del tamaño
 * del vector.
 *
 * Implementá una función que reciba el vector y el valor buscado y devuelva la
 * cantidad de apariciones. Definí el comportamiento para un vector vacío, que
 * debe devolver 0. Si el vector es null, rechazalo con una excepción
 * descriptiva.
 *
 * Comentá el código y probalo con estos casos: valor presente una vez, valor
 * presente varias veces, valor ausente, vector vacío y vector con un solo
 * elemento.
 * ============================================================================
 */

/**
 * Conteo de ocurrencias de un valor dentro de un vector de enteros.
 *
 * <p>La única operación expuesta es
 * {@link #contarOcurrencias(int[], int)}, que recorre el vector completo
 * comparando cada elemento con el valor buscado y devuelve cuántas veces
 * aparece dicho valor.</p>
 *
 * <p><b>Complejidad:</b> temporal {@code O(n)} (se examina cada elemento una
 * sola vez; {@code n} es el tamaño del vector) y espacial auxiliar {@code O(1)}
 * (solo se emplean un contador y el índice del recorrido mejorado, sin
 * estructuras adicionales proporcionales a la entrada).</p>
 *
 * @author Agente Desarrollador
 */
public final class ContarOcurrencias {

    /** Impide instanciar una clase que solo agrupa métodos estáticos. */
    private ContarOcurrencias() {
    }

    // ------------------------------------------------------------------------
    // CONTEO DE OCURRENCIAS
    // ------------------------------------------------------------------------

    /**
     * Cuenta cuántas veces aparece {@code valor} dentro de {@code vector}.
     *
     * <p>Se recorre el vector completo, de principio a fin, comparando cada
     * elemento con {@code valor} mediante {@code ==} (comparación por valor,
     * correcta para tipos primitivos {@code int}). Cada coincidencia incrementa
     * el contador y, al finalizar, se devuelve dicho contador.</p>
     *
     * <p><b>¿Por qué NO se puede detener el recorrido en la primera
     * coincidencia?</b> Porque la pregunta no es "¿existe el valor?" sino
     * "¿cuántas veces aparece?". Cualquier posición aún no visitada podría
     * contener otra aparición, por lo que hay que examinar todas las posiciones
     * para no subcontar. Esto lo diferencia de una búsqueda lineal, que sí puede
     * cortar apenas encuentra el primer match.</p>
     *
     * <p><b>Complejidad temporal:</b> {@code O(n)}. El bucle ejecuta
     * exactamente {@code n} iteraciones (una por elemento) y dentro de cada una
     * realiza un trabajo de costo constante: la complejidad no depende de los
     * valores ni de cuántas veces aparece el valor buscado.</p>
     *
     * <p><b>Complejidad espacial auxiliar:</b> {@code O(1)}. Solo se usan el
     * contador y la variable del recorrido; no se reserva memoria proporcional
     * al tamaño del vector.</p>
     *
     * <p><b>Vector vacío:</b> no es un error; el bucle no ejecuta ninguna
     * iteración y se devuelve {@code 0}, que es la cantidad correcta de
     * apariciones (no hay elementos, no puede haber coincidencias).</p>
     *
     * @param vector vector de enteros donde se busca el valor; no puede ser
     *               {@code null}
     * @param valor  valor entero cuya cantidad de apariciones se desea contar
     * @return la cantidad de posiciones de {@code vector} cuyo valor es igual a
     *         {@code valor}; {@code 0} si el vector está vacío o si el valor no
     *         aparece
     * @throws IllegalArgumentException si {@code vector} es {@code null}
     */
    public static int contarOcurrencias(int[] vector, int valor) {
        validar(vector);

        // --------------------------------------------------------------------
        // RECORRIDO COMPLETO DEL VECTOR
        //
        // 'contador' acumula cuántas coincidencias se encontraron hasta ahora.
        // Se recorre TODAS las posiciones (no hay corte anticipado): cada
        // elemento es candidato a ser una nueva aparición del valor buscado.
        //
        // INVARIANTE: al terminar la iteración del índice 'i', 'contador' es
        // igual a la cantidad de apariciones de 'valor' en vector[0..i].
        // Al salir del bucle (i = n), 'contador' es el total buscado.
        //
        // La comparación 'vector[i] == valor' es una comparación por valor entre
        // primitivos int, por lo que es exacta para cualquier entero, incluidos
        // los extremos Integer.MIN_VALUE y Integer.MAX_VALUE.
        // --------------------------------------------------------------------
        int contador = 0;
        for (int elemento : vector) {
            if (elemento == valor) {
                contador++;
            }
        }

        // Se examinaron los n elementos: 'contador' es la cantidad total de
        // apariciones (puede ser 0 si el valor no estaba presente).
        return contador;
    }

    // ------------------------------------------------------------------------
    // VALIDACIÓN DE LA ENTRADA
    // ------------------------------------------------------------------------

    /**
     * Valida que el vector de entrada no sea {@code null}.
     *
     * <p>La decisión para el vector {@code null} es rechazarlo con una
     * excepción descriptiva: no tiene sentido preguntar cuántas veces aparece un
     * valor en una estructura inexistente. En cambio, el vector vacío no es un
     * error; devolver {@code 0} es la semántica correcta.</p>
     *
     * @param vector vector a validar
     * @throws IllegalArgumentException si {@code vector} es {@code null}
     */
    private static void validar(int[] vector) {
        if (vector == null) {
            throw new IllegalArgumentException(
                    "El vector no puede ser null: no es posible contar ocurrencias.");
        }
    }

    // ------------------------------------------------------------------------
    // CASOS DE PRUEBA
    // ------------------------------------------------------------------------

    /**
     * Método de entrada: ejecuta {@link #contarOcurrencias(int[], int)} sobre
     * un conjunto de casos representativos y verifica cada resultado contra el
     * valor esperado.
     *
     * @param args no se utilizan
     */
    public static void main(String[] args) {
        System.out.println("== Conteo de ocurrencias de un valor en un vector ==");
        System.out.println();

        // Caso 1: el valor aparece UNA sola vez.
        probar("Presente una vez", new int[] {5, 2, 9, 4, 7}, 9, 1);

        // Caso 2: el valor aparece VARIAS veces (3 apariciones).
        probar("Presente varias veces", new int[] {3, 1, 3, 4, 3, 5}, 3, 3);

        // Caso 3: el valor NO está presente -> 0.
        probar("Ausente", new int[] {8, 6, 7, 5, 3, 0, 9}, 1, 0);

        // Caso 4: vector VACÍO -> 0 (no es error).
        probar("Vector vacío", new int[] {}, 42, 0);

        // Caso 5: vector con UN SOLO elemento que coincide -> 1.
        probar("Un elemento (coincide)", new int[] {7}, 7, 1);

        // Caso 6: vector con UN SOLO elemento que NO coincide -> 0.
        probar("Un elemento (no coincide)", new int[] {7}, 4, 0);

        // Caso límite adicional: todas las posiciones coinciden -> n.
        probar("Todas coinciden", new int[] {2, 2, 2, 2, 2}, 2, 5);

        // Caso límite adicional: valores extremos de int. La comparación '=='
        // entre primitivos es exacta, sin problemas de caché ni de rango.
        probar("Extremos de int", new int[] {Integer.MIN_VALUE, 0, Integer.MAX_VALUE, Integer.MIN_VALUE},
                Integer.MIN_VALUE, 2);

        System.out.println();
        System.out.println("== Caso límite: vector nulo (debe rechazarse) ==");
        probarVectorNulo();
    }

    /**
     * Ejecuta un caso de prueba, muestra el resultado obtenido y contrasta con
     * el esperado.
     *
     * @param nombre   descripción del caso para la salida
     * @param vector   vector de entrada
     * @param valor    valor buscado
     * @param esperado cantidad de apariciones esperada
     */
    private static void probar(String nombre, int[] vector, int valor, int esperado) {
        int obtenido = contarOcurrencias(vector, valor);
        boolean correcto = obtenido == esperado;

        System.out.printf("%-26s %-28s buscando %-12s | obtenido = %-3d | esperado = %-3d | %s%n",
                nombre, formatear(vector), valor, obtenido, esperado,
                correcto ? "OK" : "FALLO");
    }

    /**
     * Verifica que un vector {@code null} sea rechazado con la excepción
     * documentada.
     */
    private static void probarVectorNulo() {
        try {
            contarOcurrencias(null, 1);
            System.out.println("Vector null -> FALLO (no se lanzó ninguna excepción)");
        } catch (IllegalArgumentException excepcion) {
            System.out.printf("Vector null -> OK (rechazado: %s)%n", excepcion.getMessage());
        }
    }

    /**
     * Construye una representación legible del vector para la salida de
     * pruebas.
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
 * NOTAS DE IMPLEMENTACIÓN
 * ============================================================================
 * 1. ESTRATEGIA: recorrido lineal completo. Para cada elemento se pregunta si
 *    es igual al valor buscado; si lo es, se incrementa el contador. No se
 *    ordena ni se usa ninguna estructura auxiliar: ordenar sería O(n log n) y
 *    no aportaría nada, porque el conteo no requiere orden.
 *
 * 2. SIN CORTE ANTICIPADO: a diferencia de una búsqueda (que se detiene en la
 *    primera coincidencia), aquí hay que agotar las n posiciones. Cualquier
 *    elemento no visitado podría ser otra aparición del valor; detenerse antes
 *    produciría un subconteo. Por eso el algoritmo siempre ejecuta n
 *    iteraciones, tanto si el valor aparece muchas veces como si aparece una
 *    sola o ninguna.
 *
 * 3. COMPLEJIDAD TEMPORAL O(n): el trabajo por elemento es constante
 *    (comparación + incremento opcional), así que el costo crece linealmente
 *    con el tamaño del vector. Este costo es igual para el mejor, el peor y el
 *    caso promedio: no existe un caso que permita terminar antes.
 *
 * 4. COMPLEJIDAD ESPACIAL AUXILIAR O(1): solo se usan el contador y la
 *    variable de iteración. La memoria consumida no depende de n.
 *
 * 5. VECTOR VACÍO: el bucle no itera y se devuelve 0, que es correcto (no hay
 *    elementos, por lo tanto no hay apariciones). No se trata como error.
 *
 * 6. VECTOR NULO: se rechaza con IllegalArgumentException (mensaje descriptivo)
 *    en lugar de lanzar un NullPointerException accidental, siguiendo la
 *    convención defensiva del resto del repositorio.
 *
 * 7. COMPARACIÓN DE VALORES: se usa '==' sobre primitivos int, que compara por
 *    valor y funciona correctamente para todo el rango de int (incluidos
 *    Integer.MIN_VALUE y Integer.MAX_VALUE), sin los matices de la caché de
 *    Integer.valueOf.
 * ============================================================================
 */

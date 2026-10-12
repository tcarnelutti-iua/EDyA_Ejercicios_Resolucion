/*
 * ============================================================================
 * PROMPT INICIAL UTILIZADO PARA GENERAR ESTA SOLUCIÓN
 * ============================================================================
 *
 * Quiero implementar en Java un algoritmo que encuentre el valor mínimo de un
 * vector de números enteros.
 *
 * La estrategia más adecuada es recorrer el vector una sola vez, manteniendo en
 * una variable el menor valor encontrado hasta el momento. Se comienza con el
 * primer elemento como mínimo provisional y, por cada elemento restante, se
 * actualiza ese mínimo si se encuentra uno menor. Esta estrategia es correcta
 * porque compara todos los elementos y, al terminar el recorrido, el mínimo
 * provisional es menor o igual que cada valor del vector.
 *
 * No es necesario ordenar el vector: ordenar requiere trabajo adicional y no
 * aporta información necesaria para identificar el mínimo. Basta con examinar
 * cada elemento una vez.
 *
 * La complejidad temporal esperada es O(n), donde n es la cantidad de elementos,
 * porque se recorre el vector una sola vez. La complejidad espacial esperada es
 * O(1), porque se utilizan unas pocas variables adicionales, independientemente
 * del tamaño del vector.
 *
 * Escribí una función en Java que reciba un vector de enteros y devuelva su
 * valor mínimo. Comentá el código para explicar cómo se inicializa el mínimo
 * provisional y cómo se actualiza durante el recorrido. Incluí también una
 * explicación de por qué no hace falta ordenar el vector.
 *
 * Definí y documentá qué debe ocurrir si el vector está vacío; no debe
 * intentarse acceder a una posición inexistente.
 *
 * Probá la función con estos casos:
 *   - Un vector con varios valores positivos.
 *   - Un vector con valores positivos y negativos.
 *   - Un vector con todos sus valores iguales.
 *   - Un vector de un solo elemento.
 *   - Un vector vacío, según el comportamiento definido.
 * ============================================================================
 */

/**
 * Búsqueda del valor mínimo de un vector de números enteros mediante un único
 * recorrido lineal.
 *
 * <p>Complejidad temporal: {@code O(n)}; complejidad espacial: {@code O(1)}.</p>
 *
 * @author Agente Desarrollador
 */
public final class MinimoVector {

    /** Impide instanciar una clase que solo agrupa métodos estáticos. */
    private MinimoVector() {
    }

    /**
     * Devuelve el valor mínimo de {@code vector}.
     *
     * <p>Recorre el vector una sola vez comparando cada elemento con el mínimo
     * provisional y actualizándolo cuando encuentra un valor menor. No ordena el
     * vector, ya que ello implicaría un costo mayor ({@code O(n log n)} en el
     * mejor de los casos) sin aportar información necesaria para hallar el
     * mínimo.</p>
     *
     * @param vector vector de enteros a examinar; no puede ser {@code null}
     * @return el menor valor contenido en {@code vector}
     * @throws IllegalArgumentException si {@code vector} es {@code null} o está
     *         vacío (no existe un mínimo que se pueda devolver sin acceder a una
     *         posición inexistente)
     */
    public static int encontrarMinimo(int[] vector) {
        // --------------------------------------------------------------------
        // VALIDACIÓN DE CASOS LÍMITE
        // 1) Un vector nulo no tiene elementos sobre los que iterar.
        // 2) Un vector vacío tampoco tiene un valor mínimo posible: devolver un
        //    "0" arbitrario sería incorrecto y acceder a vector[0] sería un
        //    acceso fuera de rango (ArrayIndexOutOfBoundsException).
        // Se documenta la decisión: en ambos casos se lanza una excepción, que
        // es la forma estándar en Java de señalar una entrada no procesable.
        // --------------------------------------------------------------------
        if (vector == null) {
            throw new IllegalArgumentException("El vector no puede ser null.");
        }
        if (vector.length == 0) {
            throw new IllegalArgumentException(
                    "El vector está vacío: no existe un valor mínimo que devolver.");
        }

        // --------------------------------------------------------------------
        // INICIALIZACIÓN DEL MÍNIMO PROVISIONAL
        // Se toma el PRIMER elemento como candidato inicial. Esto es seguro
        // porque el vector ya se validó como no vacío. Arrancar con el primer
        // elemento (en lugar de, por ejemplo, Integer.MAX_VALUE) garantiza el
        // resultado correcto aun cuando todos los valores sean negativos o
        // cuando el vector contenga Integer.MIN_VALUE.
        // --------------------------------------------------------------------
        int minimo = vector[0];

        // --------------------------------------------------------------------
        // RECORRIDO Y ACTUALIZACIÓN
        // Se examina cada elemento restante (desde el índice 1). Si un elemento
        // es estrictamente menor que el mínimo provisional, este se actualiza.
        //
        // INVARIANTE DEL BUCLE: al comienzo de cada iteración 'i', 'minimo' es
        // el menor de los elementos en vector[0..i-1]. Al terminar (i == n),
        // 'minimo' es el menor de vector[0..n-1], es decir, el mínimo global.
        //
        // Se recorre el vector UNA sola vez: cada elemento se compara a lo sumo
        // una vez -> complejidad O(n). Solo se usan un par de variables
        // auxiliares ('minimo' y el índice 'i') -> complejidad espacial O(1).
        // --------------------------------------------------------------------
        for (int i = 1; i < vector.length; i++) {
            if (vector[i] < minimo) {
                minimo = vector[i];
            }
        }

        return minimo;
    }

    /*
     * ========================================================================
     * ¿POR QUÉ NO HACE FALTA ORDENAR EL VECTOR?
     * ========================================================================
     * Ordenar el vector para luego tomar la primera posición también hallaría
     * el mínimo, pero:
     *   1. Es más costoso: los algoritmos de ordenamiento eficientes son
     *      O(n log n), frente al O(n) de un único recorrido lineal.
     *   2. Trabajo innecesario: ordenar aporta información (el orden relativo de
     *      TODOS los elementos) que no se necesita para conocer solo el mínimo.
     *   3. Modifica/muta el vector: un recorrido de búsqueda del mínimo no
     *      necesita alterar la estructura de entrada.
     * En conclusión: comparar cada elemento una sola vez es suficiente y
     * óptimo (no se puede hallar el mínimo en menos de O(n), pues hay que mirar
     * todos los elementos al menos una vez).
     * ========================================================================
     */

    /**
     * Ejecuta los casos de prueba solicitados en el prompt.
     *
     * @param args no se utilizan
     */
    public static void main(String[] args) {
        // Caso 1: varios valores positivos.
        probar(new int[] {7, 3, 9, 1, 5}, 1);

        // Caso 2: valores positivos y negativos.
        probar(new int[] {4, -2, 8, -10, 0, 6}, -10);

        // Caso 3: todos los valores iguales.
        probar(new int[] {5, 5, 5, 5}, 5);

        // Caso 4: un solo elemento.
        probar(new int[] {42}, 42);

        // Caso 5: vector vacío -> debe lanzar IllegalArgumentException.
        probarVectorVacio(new int[] {});

        // Caso límite adicional: el propio valor mínimo y máximo del tipo int.
        probar(new int[] {Integer.MAX_VALUE, Integer.MIN_VALUE, 0}, Integer.MIN_VALUE);
    }

    /**
     * Ejecuta un caso de prueba y muestra si el resultado coincide con lo esperado.
     *
     * @param vector   entrada de la función
     * @param esperado resultado esperado
     */
    private static void probar(int[] vector, int esperado) {
        int obtenido = encontrarMinimo(vector);
        boolean correcto = obtenido == esperado;
        System.out.printf("minimo(%s) = %-12d | esperado = %-12d | %s%n",
                formatear(vector), obtenido, esperado, correcto ? "OK" : "FALLO");
    }

    /**
     * Verifica que un vector vacío sea rechazado con la excepción documentada.
     *
     * @param vector vector vacío (comportamiento definido: se lanza excepción)
     */
    private static void probarVectorVacio(int[] vector) {
        try {
            encontrarMinimo(vector);
            System.out.println("minimo({}) -> FALLO (no se lanzó ninguna excepción)");
        } catch (IllegalArgumentException excepcion) {
            System.out.printf("minimo({}) -> OK (rechazado: %s)%n", excepcion.getMessage());
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
 * 1. Comportamiento del vector vacío (y nulo): se lanza IllegalArgumentException
 *    con un mensaje descriptivo. Se eligió una excepción en lugar de devolver un
 *    valor centinela (como 0) porque no existe un mínimo real que devolver y un
 *    valor inventado podría propagar resultados incorrectos sin aviso.
 * 2. El mínimo se inicializa con el primer elemento del vector tras validar que
 *    no está vacío; así el algoritmo es correcto incluso si todos los valores
 *    son negativos o si aparece Integer.MIN_VALUE.
 * 3. La comparación es estricta (vector[i] < minimo): usar "<" o "<=" es
 *    indistinto para el resultado; se usa "<" para actualizar solo cuando hay
 *    una mejora real.
 * 4. La complejidad se mantiene en O(n) temporal y O(1) espacial, cumpliendo lo
 *    esperado en el análisis.
 * ============================================================================
 */

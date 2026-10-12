/*
 * Comparacion de Bubble Sort, Selection Sort e Insertion Sort
 * 
 * Comparacion de los tres algoritmos de ordenamiento.
 */
import java.util.Arrays;

/**
 * Clase principal para comparar los tres algoritmos de ordenamiento.
 */
public class SortComparison {
    
    public static class SortResult {
        private final int[] sortedArray;
        private final long comparaciones;
        private final long intercambios;
        private final long desplazamientos;
        private final String algorithmName;
        
        public SortResult(String algorithmName, int[] sortedArray, 
                         long comparaciones, long intercambios, long desplazamientos) {
            this.algorithmName = algorithmName;
            this.sortedArray = Arrays.copyOf(sortedArray, sortedArray.length);
            this.comparaciones = comparaciones;
            this.intercambios = intercambios;
            this.desplazamientos = desplazamientos;
        }
        
        public int[] getSortedArray() { return sortedArray; }
        public long getComparaciones() { return comparaciones; }
        public long getIntercambios() { return intercambios; }
        public long getDesplazamientos() { return desplazamientos; }
        public String getAlgorithmName() { return algorithmName; }
    }
    
    /**
     * Bubble Sort
     * - Comparaciones: cada comparacion entre valores que decide el orden.
     * - Intercambios: cambios efectivos de posicion.
     * - Finalizacion anticipada si una pasada completa no realiza intercambios.
     */
    public static SortResult bubbleSort(int[] array) {
        if (array == null || array.length <= 1) {
            int[] copia = array == null ? new int[0] : Arrays.copyOf(array, array.length);
            return new SortResult("Bubble Sort", copia, 0, 0, 0);
        }
        int[] arr = Arrays.copyOf(array, array.length);
        long comparaciones = 0;
        long intercambios = 0;
        int n = arr.length;
        boolean huboIntercambio;
        for (int i = 0; i < n - 1; i++) {
            huboIntercambio = false;
            for (int j = 0; j < n - 1 - i; j++) {
                comparaciones++;
                if (arr[j] > arr[j + 1]) {
                    int temp = arr[j];
                    arr[j] = arr[j + 1];
                    arr[j + 1] = temp;
                    intercambios++;
                    huboIntercambio = true;
                }
            }
            if (!huboIntercambio) break;
        }
        return new SortResult("Bubble Sort", arr, comparaciones, intercambios, 0);
    }
    
    /**
     * Selection Sort
     * - Comparaciones: cada comparacion para encontrar el minimo.
     * - Intercambios: solo si el menor encontrado esta en posicion distinta de la actual.
     */
    public static SortResult selectionSort(int[] array) {
        if (array == null || array.length <= 1) {
            int[] copia = array == null ? new int[0] : Arrays.copyOf(array, array.length);
            return new SortResult("Selection Sort", copia, 0, 0, 0);
        }
        int[] arr = Arrays.copyOf(array, array.length);
        long comparaciones = 0;
        long intercambios = 0;
        int n = arr.length;
        for (int i = 0; i < n - 1; i++) {
            int minIndex = i;
            for (int j = i + 1; j < n; j++) {
                comparaciones++;
                if (arr[j] < arr[minIndex]) {
                    minIndex = j;
                }
            }
            if (minIndex != i) {
                int temp = arr[i];
                arr[i] = arr[minIndex];
                arr[minIndex] = temp;
                intercambios++;
            }
        }
        return new SortResult("Selection Sort", arr, comparaciones, intercambios, 0);
    }
    
    /**
     * Insertion Sort
     * - Comparaciones: cada comparacion entre valores que decide el orden.
     * - Desplazamientos: veces que se mueve un elemento una posicion hacia la derecha
     *   para insertar otro (no cuenta la asignacion final del elemento insertado).
     */
    public static SortResult insertionSort(int[] array) {
        if (array == null || array.length <= 1) {
            int[] copia = array == null ? new int[0] : Arrays.copyOf(array, array.length);
            return new SortResult("Insertion Sort", copia, 0, 0, 0);
        }
        int[] arr = Arrays.copyOf(array, array.length);
        long comparaciones = 0;
        long desplazamientos = 0;
        int n = arr.length;
        for (int i = 1; i < n; i++) {
            int key = arr[i];
            int j = i - 1;
            while (j >= 0) {
                comparaciones++;
                if (arr[j] > key) {
                    arr[j + 1] = arr[j];
                    desplazamientos++;
                    j--;
                } else {
                    break;
                }
            }
            arr[j + 1] = key;
        }
        return new SortResult("Insertion Sort", arr, comparaciones, 0, desplazamientos);
    }
    
    private static boolean arraysEqual(int[] a1, int[] a2) {
        if (a1.length != a2.length) return false;
        for (int i = 0; i < a1.length; i++) {
            if (a1[i] != a2[i]) return false;
        }
        return true;
    }
    
    private static String formatArray(int[] array) {
        if (array == null) return "null";
        if (array.length == 0) return "[]";
        StringBuilder sb = new StringBuilder();
        sb.append("[");
        for (int i = 0; i < array.length; i++) {
            sb.append(array[i]);
            if (i < array.length - 1) sb.append(", ");
        }
        sb.append("]");
        return sb.toString();
    }
    
    private static void testCase(String caseName, int[] original) {
        System.out.println("===================================================");
        System.out.println("CASO: " + caseName);
        System.out.println("Arreglo original: " + formatArray(original));
        System.out.println("---------------------------------------------------");
        
        SortResult bubbleResult = bubbleSort(original);
        SortResult selectionResult = selectionSort(original);
        SortResult insertionResult = insertionSort(original);
        
        System.out.println("BUBBLE SORT");
        System.out.println("  Arreglo ordenado: " + formatArray(bubbleResult.getSortedArray()));
        System.out.println("  Comparaciones:    " + bubbleResult.getComparaciones());
        System.out.println("  Intercambios:     " + bubbleResult.getIntercambios());
        System.out.println("  Desplazamientos:  " + bubbleResult.getDesplazamientos());
        System.out.println();
        
        System.out.println("SELECTION SORT");
        System.out.println("  Arreglo ordenado: " + formatArray(selectionResult.getSortedArray()));
        System.out.println("  Comparaciones:    " + selectionResult.getComparaciones());
        System.out.println("  Intercambios:     " + selectionResult.getIntercambios());
        System.out.println("  Desplazamientos:  " + selectionResult.getDesplazamientos());
        System.out.println();
        
        System.out.println("INSERTION SORT");
        System.out.println("  Arreglo ordenado: " + formatArray(insertionResult.getSortedArray()));
        System.out.println("  Comparaciones:    " + insertionResult.getComparaciones());
        System.out.println("  Intercambios:     " + insertionResult.getIntercambios());
        System.out.println("  Desplazamientos:  " + insertionResult.getDesplazamientos());
        System.out.println();
        
        boolean allEqual = arraysEqual(bubbleResult.getSortedArray(), 
                                      selectionResult.getSortedArray()) &&
                           arraysEqual(selectionResult.getSortedArray(), 
                                      insertionResult.getSortedArray());
        
        if (allEqual) {
            System.out.println("OK: Todos producen el mismo resultado ordenado.");
        } else {
            System.out.println("ERROR: Resultados diferentes");
        }
        System.out.println();
    }
    
    public static void main(String[] args) {
        System.out.println("COMPARACION DE ALGORITMOS DE ORDENAMIENTO");
        System.out.println("Bubble Sort | Selection Sort | Insertion Sort");
        System.out.println();
        System.out.println("COMPARACION DE ESTRATEGIAS:");
        System.out.println("- Bubble Sort: intercambia vecinos cuando estan desordenados.");
        System.out.println("- Selection Sort: busca el menor del tramo restante e intercambia");
        System.out.println("  con la posicion actual (solo si esta en posicion distinta).");
        System.out.println("- Insertion Sort: desplaza elementos hacia la derecha para insertar");
        System.out.println("  cada valor en la parte ordenada (sin contar asignacion final).");
        System.out.println();
        
        testCase("Arreglo desordenado", new int[]{5, 3, 8, 4, 2});
        testCase("Ya ordenado", new int[]{1, 2, 3, 4, 5});
        testCase("En orden inverso", new int[]{5, 4, 3, 2, 1});
        testCase("Con valores repetidos", new int[]{4, 2, 4, 1, 3, 2});
        testCase("Vacio", new int[]{});
        testCase("De un solo elemento", new int[]{42});
    }
}

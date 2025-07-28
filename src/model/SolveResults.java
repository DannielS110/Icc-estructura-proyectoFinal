package model;

import java.util.ArrayList;
import java.util.List;

/**
 * Clase que contiene una colección de resultados de algoritmos de resolución
 * Permite agrupar y analizar múltiples resultados
 */
public class SolveResults {

    private List<AlgorithmResult> results;

    /**
     * Constructor por defecto
     */
    public SolveResults() {
        this.results = new ArrayList<>();
    }

    /**
     * Constructor con lista de resultados
     */
    public SolveResults(List<AlgorithmResult> results) {
        this.results = results != null ? results : new ArrayList<>();
    }

    /**
     * Agrega un resultado a la colección
     * @param result El resultado a agregar
     */
    public void addResult(AlgorithmResult result) {
        if (result != null) {
            results.add(result);
        }
    }

    /**
     * Obtiene todos los resultados
     * @return Lista de resultados
     */
    public List<AlgorithmResult> getResults() {
        return new ArrayList<>(results);
    }

    /**
     * Establece la lista de resultados
     * @param results Nueva lista de resultados
     */
    public void setResults(List<AlgorithmResult> results) {
        this.results = results != null ? new ArrayList<>(results) : new ArrayList<>();
    }

    /**
     * Obtiene el número de resultados
     * @return Número de resultados
     */
    public int getResultCount() {
        return results.size();
    }

    /**
     * Obtiene resultados por nombre de algoritmo
     * @param algorithmName Nombre del algoritmo
     * @return Lista de resultados del algoritmo especificado
     */
    public List<AlgorithmResult> getResultsByAlgorithm(String algorithmName) {
        List<AlgorithmResult> filteredResults = new ArrayList<>();
        for (AlgorithmResult result : results) {
            if (algorithmName.equals(result.getAlgorithmName())) {
                filteredResults.add(result);
            }
        }
        return filteredResults;
    }

    /**
     * Obtiene el resultado más rápido
     * @return El resultado con menor tiempo de ejecución
     */
    public AlgorithmResult getFastestResult() {
        if (results.isEmpty()) {
            return null;
        }

        AlgorithmResult fastest = results.get(0);
        for (AlgorithmResult result : results) {
            if (result.getExecutionTimeMs() < fastest.getExecutionTimeMs()) {
                fastest = result;
            }
        }
        return fastest;
    }

    /**
     * Obtiene el resultado más eficiente (menos celdas visitadas)
     * @return El resultado con menos celdas visitadas
     */
    public AlgorithmResult getMostEfficientResult() {
        if (results.isEmpty()) {
            return null;
        }

        AlgorithmResult mostEfficient = results.get(0);
        for (AlgorithmResult result : results) {
            if (result.getVisitedCells() < mostEfficient.getVisitedCells()) {
                mostEfficient = result;
            }
        }
        return mostEfficient;
    }

    /**
     * Obtiene el tiempo promedio de ejecución para un algoritmo específico
     * @param algorithmName Nombre del algoritmo
     * @return Tiempo promedio en milisegundos
     */
    public double getAverageExecutionTime(String algorithmName) {
        List<AlgorithmResult> algorithmResults = getResultsByAlgorithm(algorithmName);
        if (algorithmResults.isEmpty()) {
            return 0.0;
        }

        long totalTime = 0;
        for (AlgorithmResult result : algorithmResults) {
            totalTime += result.getExecutionTimeMs();
        }

        return (double) totalTime / algorithmResults.size();
    }

    /**
     * Obtiene el número promedio de celdas visitadas para un algoritmo específico
     * @param algorithmName Nombre del algoritmo
     * @return Número promedio de celdas visitadas
     */
    public double getAverageVisitedCells(String algorithmName) {
        List<AlgorithmResult> algorithmResults = getResultsByAlgorithm(algorithmName);
        if (algorithmResults.isEmpty()) {
            return 0.0;
        }

        int totalCells = 0;
        for (AlgorithmResult result : algorithmResults) {
            totalCells += result.getVisitedCells();
        }

        return (double) totalCells / algorithmResults.size();
    }

    /**
     * Limpia todos los resultados
     */
    public void clear() {
        results.clear();
    }

    /**
     * Verifica si hay resultados
     * @return true si hay resultados, false en caso contrario
     */
    public boolean isEmpty() {
        return results.isEmpty();
    }

    /**
     * Obtiene los nombres únicos de algoritmos
     * @return Lista de nombres de algoritmos únicos
     */
    public List<String> getUniqueAlgorithmNames() {
        List<String> names = new ArrayList<>();
        for (AlgorithmResult result : results) {
            if (!names.contains(result.getAlgorithmName())) {
                names.add(result.getAlgorithmName());
            }
        }
        return names;
    }
}

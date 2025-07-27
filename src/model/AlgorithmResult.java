package model;

import java.time.LocalDateTime;

/**
 * Clase que representa el resultado de un algoritmo de resolución de laberinto
 * Contiene información sobre el algoritmo usado, tiempo de ejecución, celdas visitadas, etc.
 */
public class AlgorithmResult {

    private int id;
    private String algorithmName;
    private int mazeRows;
    private int mazeCols;
    private long executionTimeMs;
    private int visitedCells;
    private int solutionPathLength;
    private boolean solutionFound;
    private LocalDateTime timestamp;

    /**
     * Constructor por defecto
     */
    public AlgorithmResult() {
        this.timestamp = LocalDateTime.now();
    }

    /**
     * Constructor con parámetros
     */
    public AlgorithmResult(String algorithmName, int mazeRows, int mazeCols,
                           long executionTimeMs, int visitedCells, int solutionPathLength,
                           boolean solutionFound) {
        this.algorithmName = algorithmName;
        this.mazeRows = mazeRows;
        this.mazeCols = mazeCols;
        this.executionTimeMs = executionTimeMs;
        this.visitedCells = visitedCells;
        this.solutionPathLength = solutionPathLength;
        this.solutionFound = solutionFound;
        this.timestamp = LocalDateTime.now();
    }

    // Getters y Setters
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getAlgorithmName() {
        return algorithmName;
    }

    public void setAlgorithmName(String algorithmName) {
        this.algorithmName = algorithmName;
    }

    public int getMazeRows() {
        return mazeRows;
    }

    public void setMazeRows(int mazeRows) {
        this.mazeRows = mazeRows;
    }

    public int getMazeCols() {
        return mazeCols;
    }

    public void setMazeCols(int mazeCols) {
        this.mazeCols = mazeCols;
    }

    public long getExecutionTimeMs() {
        return executionTimeMs;
    }

    public void setExecutionTimeMs(long executionTimeMs) {
        this.executionTimeMs = executionTimeMs;
    }

    public int getVisitedCells() {
        return visitedCells;
    }

    public void setVisitedCells(int visitedCells) {
        this.visitedCells = visitedCells;
    }

    public int getSolutionPathLength() {
        return solutionPathLength;
    }

    public void setSolutionPathLength(int solutionPathLength) {
        this.solutionPathLength = solutionPathLength;
    }

    public boolean isSolutionFound() {
        return solutionFound;
    }

    public void setSolutionFound(boolean solutionFound) {
        this.solutionFound = solutionFound;
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(LocalDateTime timestamp) {
        this.timestamp = timestamp;
    }

    /**
     * Obtiene el tiempo de ejecución en formato legible
     * @return String con el tiempo formateado
     */
    public String getFormattedExecutionTime() {
        if (executionTimeMs < 1000) {
            return executionTimeMs + " ms";
        } else {
            return String.format("%.2f s", executionTimeMs / 1000.0);
        }
    }

    /**
     * Obtiene el tamaño del laberinto en formato legible
     * @return String con el tamaño formateado
     */
    public String getFormattedMazeSize() {
        return mazeRows + "x" + mazeCols;
    }

    @Override
    public String toString() {
        return "AlgorithmResult{" +
                "id=" + id +
                ", algorithmName='" + algorithmName + '\'' +
                ", mazeSize=" + getFormattedMazeSize() +
                ", executionTime=" + getFormattedExecutionTime() +
                ", visitedCells=" + visitedCells +
                ", solutionPathLength=" + solutionPathLength +
                ", solutionFound=" + solutionFound +
                ", timestamp=" + timestamp +
                '}';
    }
}
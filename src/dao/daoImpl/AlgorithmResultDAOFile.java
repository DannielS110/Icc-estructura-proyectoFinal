package dao.daoImpl;

import dao.AlgorithmResultDAO;
import model.AlgorithmResult;
import model.SolveResults;

import java.io.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

/**
 * Implementación del DAO que guarda los resultados de algoritmos en archivos de texto
 * Cada resultado se guarda en una línea separada con formato CSV
 */
public class AlgorithmResultDAOFile implements AlgorithmResultDAO {
    
    private static final String DATA_FILE = "algorithm_results.csv";
    private static final String CSV_HEADER = "ID,AlgorithmName,MazeRows,MazeCols,ExecutionTimeMs,VisitedCells,SolutionPathLength,SolutionFound,Timestamp";
    private static final String CSV_SEPARATOR = ",";
    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
    
    private int nextId = 1;
    
    /**
     * Constructor que inicializa el archivo si no existe
     */
    public AlgorithmResultDAOFile() {
        initializeFile();
        loadNextId();
    }
    
    /**
     * Inicializa el archivo de datos si no existe
     */
    private void initializeFile() {
        File file = new File(DATA_FILE);
        if (!file.exists()) {
            try (PrintWriter writer = new PrintWriter(new FileWriter(file))) {
                writer.println(CSV_HEADER);
            } catch (IOException e) {
                System.err.println("Error al crear el archivo de datos: " + e.getMessage());
            }
        }
    }
    
    /**
     * Carga el siguiente ID disponible
     */
    private void loadNextId() {
        List<AlgorithmResult> allResults = findAll();
        if (!allResults.isEmpty()) {
            int maxId = 0;
            for (AlgorithmResult result : allResults) {
                if (result.getId() > maxId) {
                    maxId = result.getId();
                }
            }
            nextId = maxId + 1;
        }
    }
    
    @Override
    public boolean save(AlgorithmResult result) {
        if (result == null) {
            return false;
        }
        
        try {
            // Asignar timestamp si no tiene uno
            if (result.getTimestamp() == null) {
                result.setTimestamp(java.time.LocalDateTime.now());
            }
            
            // Asignar nuevo ID para cada ejecución
            result.setId(nextId++);
            
            // Leer todos los resultados existentes
            List<AlgorithmResult> allResults = findAll();
            
            // Agregar el nuevo resultado (cada ejecución es un registro separado)
            allResults.add(result);
            
            // Reescribir el archivo completo
            return rewriteFile(allResults);
            
        } catch (Exception e) {
            System.err.println("Error al guardar resultado: " + e.getMessage());
            return false;
        }
    }
    
    @Override
    public boolean saveAll(List<AlgorithmResult> results) {
        if (results == null || results.isEmpty()) {
            return false;
        }
        
        try {
            // Leer todos los resultados existentes
            List<AlgorithmResult> existingResults = findAll();
            
            // Asignar IDs a los nuevos resultados
            for (AlgorithmResult result : results) {
                if (result.getId() == 0) {
                    result.setId(nextId++);
                }
            }
            
            // Reescribir el archivo completo
            try (PrintWriter writer = new PrintWriter(new FileWriter(DATA_FILE))) {
                writer.println(CSV_HEADER);
                
                // Escribir resultados existentes
                for (AlgorithmResult result : existingResults) {
                    writer.println(resultToCsv(result));
                }
                
                // Escribir nuevos resultados
                for (AlgorithmResult result : results) {
                    writer.println(resultToCsv(result));
                }
            }
            
            return true;
        } catch (IOException e) {
            System.err.println("Error al guardar múltiples resultados: " + e.getMessage());
            return false;
        }
    }
    
    @Override
    public AlgorithmResult findById(int id) {
        List<AlgorithmResult> allResults = findAll();
        for (AlgorithmResult result : allResults) {
            if (result.getId() == id) {
                return result;
            }
        }
        return null;
    }
    
    @Override
    public List<AlgorithmResult> findAll() {
        List<AlgorithmResult> results = new ArrayList<>();
        File file = new File(DATA_FILE);
        
        if (!file.exists()) {
            return results;
        }
        
        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line;
            boolean firstLine = true;
            
            while ((line = reader.readLine()) != null) {
                if (firstLine) {
                    firstLine = false;
                    continue; // Saltar la línea de encabezado
                }
                
                AlgorithmResult result = csvToResult(line);
                if (result != null) {
                    results.add(result);
                }
            }
        } catch (IOException e) {
            System.err.println("Error al leer resultados: " + e.getMessage());
        }
        
        return results;
    }
    
    @Override
    public List<AlgorithmResult> findByAlgorithm(String algorithmName) {
        List<AlgorithmResult> allResults = findAll();
        List<AlgorithmResult> filteredResults = new ArrayList<>();
        
        for (AlgorithmResult result : allResults) {
            if (algorithmName.equals(result.getAlgorithmName())) {
                filteredResults.add(result);
            }
        }
        
        return filteredResults;
    }
    
    @Override
    public List<AlgorithmResult> findByMazeSize(int rows, int cols) {
        List<AlgorithmResult> allResults = findAll();
        List<AlgorithmResult> filteredResults = new ArrayList<>();
        
        for (AlgorithmResult result : allResults) {
            if (result.getMazeRows() == rows && result.getMazeCols() == cols) {
                filteredResults.add(result);
            }
        }
        
        return filteredResults;
    }
    
    @Override
    public List<AlgorithmResult> findSuccessfulResults() {
        List<AlgorithmResult> allResults = findAll();
        List<AlgorithmResult> successfulResults = new ArrayList<>();
        
        for (AlgorithmResult result : allResults) {
            if (result.isSolutionFound()) {
                successfulResults.add(result);
            }
        }
        
        return successfulResults;
    }
    
    @Override
    public List<AlgorithmResult> findFailedResults() {
        List<AlgorithmResult> allResults = findAll();
        List<AlgorithmResult> failedResults = new ArrayList<>();
        
        for (AlgorithmResult result : allResults) {
            if (!result.isSolutionFound()) {
                failedResults.add(result);
            }
        }
        
        return failedResults;
    }
    
    @Override
    public AlgorithmResult findFastestResult() {
        List<AlgorithmResult> allResults = findAll();
        if (allResults.isEmpty()) {
            return null;
        }
        
        AlgorithmResult fastest = allResults.get(0);
        for (AlgorithmResult result : allResults) {
            if (result.getExecutionTimeMs() < fastest.getExecutionTimeMs()) {
                fastest = result;
            }
        }
        
        return fastest;
    }
    
    @Override
    public AlgorithmResult findMostEfficientResult() {
        List<AlgorithmResult> allResults = findAll();
        if (allResults.isEmpty()) {
            return null;
        }
        
        AlgorithmResult mostEfficient = allResults.get(0);
        for (AlgorithmResult result : allResults) {
            if (result.getVisitedCells() < mostEfficient.getVisitedCells()) {
                mostEfficient = result;
            }
        }
        
        return mostEfficient;
    }
    
    @Override
    public boolean update(AlgorithmResult result) {
        if (result == null) {
            return false;
        }
        
        List<AlgorithmResult> allResults = findAll();
        boolean found = false;
        
        for (int i = 0; i < allResults.size(); i++) {
            if (allResults.get(i).getId() == result.getId()) {
                allResults.set(i, result);
                found = true;
                break;
            }
        }
        
        if (!found) {
            return false;
        }
        
        return rewriteFile(allResults);
    }
    
    @Override
    public boolean deleteById(int id) {
        List<AlgorithmResult> allResults = findAll();
        boolean removed = allResults.removeIf(result -> result.getId() == id);
        
        if (removed) {
            return rewriteFile(allResults);
        }
        
        return false;
    }
    
    @Override
    public boolean deleteAll() {
        try (PrintWriter writer = new PrintWriter(new FileWriter(DATA_FILE))) {
            writer.println(CSV_HEADER);
            nextId = 1;
            return true;
        } catch (IOException e) {
            System.err.println("Error al eliminar todos los resultados: " + e.getMessage());
            return false;
        }
    }
    
    @Override
    public boolean deleteByAlgorithm(String algorithmName) {
        List<AlgorithmResult> allResults = findAll();
        boolean removed = allResults.removeIf(result -> algorithmName.equals(result.getAlgorithmName()));
        
        if (removed) {
            return rewriteFile(allResults);
        }
        
        return false;
    }
    
    @Override
    public int getResultCount() {
        return findAll().size();
    }
    
    @Override
    public int getResultCountByAlgorithm(String algorithmName) {
        return findByAlgorithm(algorithmName).size();
    }
    
    @Override
    public SolveResults getAllAsSolveResults() {
        return new SolveResults(findAll());
    }
    
    @Override
    public String getStatistics() {
        List<AlgorithmResult> allResults = findAll();
        if (allResults.isEmpty()) {
            return "No hay resultados disponibles.";
        }
        
        StringBuilder stats = new StringBuilder();
        stats.append("=== ESTADÍSTICAS DE RESULTADOS ===\n");
        stats.append("Total de resultados: ").append(allResults.size()).append("\n");
        
        // Estadísticas por algoritmo
        List<String> algorithmNames = new ArrayList<>();
        for (AlgorithmResult result : allResults) {
            if (!algorithmNames.contains(result.getAlgorithmName())) {
                algorithmNames.add(result.getAlgorithmName());
            }
        }
        
        for (String algorithmName : algorithmNames) {
            List<AlgorithmResult> algorithmResults = findByAlgorithm(algorithmName);
            stats.append("\n").append(algorithmName).append(":\n");
            stats.append("  - Ejecuciones: ").append(algorithmResults.size()).append("\n");
            
            if (!algorithmResults.isEmpty()) {
                long totalTime = 0;
                int totalCells = 0;
                int successfulCount = 0;
                
                for (AlgorithmResult result : algorithmResults) {
                    totalTime += result.getExecutionTimeMs();
                    totalCells += result.getVisitedCells();
                    if (result.isSolutionFound()) {
                        successfulCount++;
                    }
                }
                
                double avgTime = (double) totalTime / algorithmResults.size();
                double avgCells = (double) totalCells / algorithmResults.size();
                double successRate = (double) successfulCount / algorithmResults.size() * 100;
                
                stats.append("  - Tiempo promedio: ").append(String.format("%.2f ms", avgTime)).append("\n");
                stats.append("  - Celdas promedio: ").append(String.format("%.1f", avgCells)).append("\n");
                stats.append("  - Tasa de éxito: ").append(String.format("%.1f%%", successRate)).append("\n");
            }
        }
        
        return stats.toString();
    }
    
    /**
     * Convierte un resultado a formato CSV
     */
    private String resultToCsv(AlgorithmResult result) {
        return String.join(CSV_SEPARATOR,
                String.valueOf(result.getId()),
                escapeCsvField(result.getAlgorithmName()),
                String.valueOf(result.getMazeRows()),
                String.valueOf(result.getMazeCols()),
                String.valueOf(result.getExecutionTimeMs()),
                String.valueOf(result.getVisitedCells()),
                String.valueOf(result.getSolutionPathLength()),
                String.valueOf(result.isSolutionFound()),
                escapeCsvField(result.getTimestamp().format(DATE_FORMATTER))
        );
    }
    
    /**
     * Convierte una línea CSV a un resultado
     */
    private AlgorithmResult csvToResult(String csvLine) {
        try {
            String[] fields = parseCsvLine(csvLine);
            if (fields.length < 9) {
                return null;
            }
            
            AlgorithmResult result = new AlgorithmResult();
            result.setId(Integer.parseInt(fields[0]));
            result.setAlgorithmName(fields[1]);
            result.setMazeRows(Integer.parseInt(fields[2]));
            result.setMazeCols(Integer.parseInt(fields[3]));
            result.setExecutionTimeMs(Long.parseLong(fields[4]));
            result.setVisitedCells(Integer.parseInt(fields[5]));
            result.setSolutionPathLength(Integer.parseInt(fields[6]));
            result.setSolutionFound(Boolean.parseBoolean(fields[7]));
            result.setTimestamp(LocalDateTime.parse(fields[8], DATE_FORMATTER));
            
            return result;
        } catch (Exception e) {
            System.err.println("Error al parsear línea CSV: " + csvLine + " - " + e.getMessage());
            return null;
        }
    }
    
    /**
     * Escapa un campo CSV
     */
    private String escapeCsvField(String field) {
        if (field == null) {
            return "";
        }
        if (field.contains(",") || field.contains("\"") || field.contains("\n")) {
            return "\"" + field.replace("\"", "\"\"") + "\"";
        }
        return field;
    }
    
    /**
     * Parsea una línea CSV considerando campos entre comillas
     */
    private String[] parseCsvLine(String line) {
        List<String> fields = new ArrayList<>();
        StringBuilder currentField = new StringBuilder();
        boolean inQuotes = false;
        
        for (int i = 0; i < line.length(); i++) {
            char c = line.charAt(i);
            
            if (c == '"') {
                if (inQuotes && i + 1 < line.length() && line.charAt(i + 1) == '"') {
                    currentField.append('"');
                    i++; // Saltar la siguiente comilla
                } else {
                    inQuotes = !inQuotes;
                }
            } else if (c == ',' && !inQuotes) {
                fields.add(currentField.toString());
                currentField = new StringBuilder();
            } else {
                currentField.append(c);
            }
        }
        
        fields.add(currentField.toString());
        return fields.toArray(new String[0]);
    }
    
    /**
     * Reescribe el archivo completo con los resultados proporcionados
     */
    private boolean rewriteFile(List<AlgorithmResult> results) {
        try (PrintWriter writer = new PrintWriter(new FileWriter(DATA_FILE))) {
            writer.println(CSV_HEADER);
            for (AlgorithmResult result : results) {
                writer.println(resultToCsv(result));
            }
            return true;
        } catch (IOException e) {
            System.err.println("Error al reescribir archivo: " + e.getMessage());
            return false;
        }
    }
} 
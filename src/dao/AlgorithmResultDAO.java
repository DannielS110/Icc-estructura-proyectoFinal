package dao;

import model.AlgorithmResult;
import model.SolveResults;

import java.util.List;

/**
 * Interfaz DAO (Data Access Object) para el acceso a datos de resultados de algoritmos
 * Define las operaciones básicas de persistencia para los resultados
 */
public interface AlgorithmResultDAO {
    
    /**
     * Guarda un resultado de algoritmo
     * @param result El resultado a guardar
     * @return true si se guardó exitosamente, false en caso contrario
     */
    boolean save(AlgorithmResult result);
    
    /**
     * Guarda múltiples resultados de algoritmos
     * @param results Lista de resultados a guardar
     * @return true si se guardaron exitosamente, false en caso contrario
     */
    boolean saveAll(List<AlgorithmResult> results);
    
    /**
     * Obtiene un resultado por su ID
     * @param id El ID del resultado
     * @return El resultado encontrado o null si no existe
     */
    AlgorithmResult findById(int id);
    
    /**
     * Obtiene todos los resultados
     * @return Lista de todos los resultados
     */
    List<AlgorithmResult> findAll();
    
    /**
     * Obtiene resultados por nombre de algoritmo
     * @param algorithmName El nombre del algoritmo
     * @return Lista de resultados del algoritmo especificado
     */
    List<AlgorithmResult> findByAlgorithm(String algorithmName);
    
    /**
     * Obtiene resultados por tamaño de laberinto
     * @param rows Número de filas
     * @param cols Número de columnas
     * @return Lista de resultados para el tamaño especificado
     */
    List<AlgorithmResult> findByMazeSize(int rows, int cols);
    
    /**
     * Obtiene resultados que encontraron solución
     * @return Lista de resultados exitosos
     */
    List<AlgorithmResult> findSuccessfulResults();
    
    /**
     * Obtiene resultados que no encontraron solución
     * @return Lista de resultados fallidos
     */
    List<AlgorithmResult> findFailedResults();
    
    /**
     * Obtiene el resultado más rápido
     * @return El resultado con menor tiempo de ejecución
     */
    AlgorithmResult findFastestResult();
    
    /**
     * Obtiene el resultado más eficiente (menos celdas visitadas)
     * @return El resultado con menos celdas visitadas
     */
    AlgorithmResult findMostEfficientResult();
    
    /**
     * Actualiza un resultado existente
     * @param result El resultado a actualizar
     * @return true si se actualizó exitosamente, false en caso contrario
     */
    boolean update(AlgorithmResult result);
    
    /**
     * Elimina un resultado por su ID
     * @param id El ID del resultado a eliminar
     * @return true si se eliminó exitosamente, false en caso contrario
     */
    boolean deleteById(int id);
    
    /**
     * Elimina todos los resultados
     * @return true si se eliminaron exitosamente, false en caso contrario
     */
    boolean deleteAll();
    
    /**
     * Elimina resultados por nombre de algoritmo
     * @param algorithmName El nombre del algoritmo
     * @return true si se eliminaron exitosamente, false en caso contrario
     */
    boolean deleteByAlgorithm(String algorithmName);
    
    /**
     * Obtiene el número total de resultados
     * @return Número total de resultados
     */
    int getResultCount();
    
    /**
     * Obtiene el número de resultados por algoritmo
     * @param algorithmName El nombre del algoritmo
     * @return Número de resultados del algoritmo especificado
     */
    int getResultCountByAlgorithm(String algorithmName);
    
    /**
     * Obtiene todos los resultados como objeto SolveResults
     * @return Objeto SolveResults con todos los resultados
     */
    SolveResults getAllAsSolveResults();
    
    /**
     * Obtiene estadísticas básicas de los resultados
     * @return String con estadísticas formateadas
     */
    String getStatistics();
} 
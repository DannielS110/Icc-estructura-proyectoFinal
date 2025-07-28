package view;

import controller.MazeController;
import model.AlgorithmResult;
import view.MinecraftTheme;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

/**
 * Ventana separada que muestra las estadísticas y resultados de los algoritmos
 */
public class StatisticsWindow extends JFrame {
    
    private JTable resultsTable;
    private DefaultTableModel tableModel;
    private MazeController mazeController;
    
    public StatisticsWindow(MazeController mazeController) {
        this.mazeController = mazeController;
        
        // Configurar la ventana
        setTitle("Estadísticas de Algoritmos - Resolvedor de Laberintos");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setSize(1000, 600);
        setLocationRelativeTo(null); // Centrar en la pantalla
        
        // Aplicar tema Minecraft a la ventana
        getContentPane().setBackground(MinecraftTheme.BACKGROUND_DARK);
        
        // Configurar el layout
        setLayout(new BorderLayout());
        
        // Inicializar componentes
        initializeTable();
        initializeButtons();
        
        // Cargar datos iniciales
        refreshTable();
    }
    
    private void initializeTable() {
        // Definir las columnas de la tabla
        String[] columnNames = {"ID", "Algoritmo", "Tamaño Laberinto", "Tiempo (ms)", "Celdas Visitadas", "Longitud Camino", "Solución Encontrada", "Fecha"};
        
        // Crear el modelo de tabla (no editable)
        tableModel = new DefaultTableModel(columnNames, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false; // Hacer la tabla no editable
            }
        };
        
        resultsTable = new JTable(tableModel);
        resultsTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        
        // Aplicar tema Minecraft a la tabla
        resultsTable.setFont(MinecraftTheme.getMinecraftFont(12f));
        resultsTable.setBackground(Color.WHITE);
        resultsTable.setForeground(Color.BLACK);
        resultsTable.setGridColor(MinecraftTheme.BORDER_BROWN);
        resultsTable.setSelectionBackground(MinecraftTheme.EMERALD_GREEN);
        resultsTable.setSelectionForeground(Color.WHITE);
        
        // Configurar el header de la tabla
        resultsTable.getTableHeader().setFont(MinecraftTheme.getMinecraftFont(12f));
        resultsTable.getTableHeader().setBackground(MinecraftTheme.PANEL_BROWN);
        resultsTable.getTableHeader().setForeground(MinecraftTheme.TEXT_WHITE);
        
        // Configurar el scroll pane
        JScrollPane scrollPane = new JScrollPane(resultsTable);
        scrollPane.setPreferredSize(new Dimension(950, 450));
        scrollPane.getViewport().setBackground(Color.WHITE);
        
        add(scrollPane, BorderLayout.CENTER);
    }
    
    private void initializeButtons() {
        JPanel buttonPanel = new JPanel(new FlowLayout());
        
        // Aplicar tema Minecraft al panel de botones
        MinecraftTheme.applyMinecraftPanelStyle(buttonPanel);
        
        // Botón para refrescar la tabla
        JButton refreshButton = new JButton("Refrescar Datos");
        MinecraftTheme.applyMinecraftButtonStyle(refreshButton);
        refreshButton.addActionListener(e -> refreshTable());
        
        // Botón para limpiar el archivo
        JButton clearButton = new JButton("Limpiar Archivo");
        MinecraftTheme.applyMinecraftButtonStyle(clearButton);
        clearButton.addActionListener(e -> clearResultsFile());
        
        // Botón para graficar tiempos
        JButton graphButton = new JButton("Graficar Tiempos");
        MinecraftTheme.applyMinecraftButtonStyle(graphButton);
        graphButton.addActionListener(e -> showTimeGraph());
        
        // Botón para cerrar la ventana
        JButton closeButton = new JButton("Cerrar");
        MinecraftTheme.applyMinecraftButtonStyle(closeButton);
        closeButton.addActionListener(e -> dispose());
        
        buttonPanel.add(refreshButton);
        buttonPanel.add(clearButton);
        buttonPanel.add(graphButton);
        buttonPanel.add(closeButton);
        
        add(buttonPanel, BorderLayout.SOUTH);
    }
    
    public void refreshTable() {
        // Limpiar la tabla
        tableModel.setRowCount(0);
        
        // Verificar que el DAO esté disponible
        if (mazeController == null || mazeController.getResultDAO() == null) {
            JOptionPane.showMessageDialog(
                this,
                "DAO no disponible.",
                "Error",
                JOptionPane.ERROR_MESSAGE
            );
            return;
        }
        
        // Obtener todos los resultados del DAO
        List<AlgorithmResult> results = mazeController.getResultDAO().findAll();
        
        if (results.isEmpty()) {
            JOptionPane.showMessageDialog(
                this,
                "No hay resultados disponibles en el archivo.",
                "Sin Datos",
                JOptionPane.INFORMATION_MESSAGE
            );
            return;
        }
        
        // Agregar cada resultado a la tabla
        for (AlgorithmResult result : results) {
            Object[] row = {
                result.getId(),
                result.getAlgorithmName(),
                result.getFormattedMazeSize(),
                result.getExecutionTimeMs(),
                result.getVisitedCells(),
                result.getSolutionPathLength(),
                result.isSolutionFound() ? "Sí" : "No",
                result.getTimestamp().format(java.time.format.DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm"))
            };
            tableModel.addRow(row);
        }
        
        // Mostrar mensaje de éxito
        setTitle("Estadísticas de Algoritmos - " + results.size() + " resultados cargados");
    }
    
    private void clearResultsFile() {
        // Verificar que el DAO esté disponible
        if (mazeController == null || mazeController.getResultDAO() == null) {
            JOptionPane.showMessageDialog(
                this,
                "DAO no disponible.",
                "Error",
                JOptionPane.ERROR_MESSAGE
            );
            return;
        }
        
        int option = JOptionPane.showConfirmDialog(
            this,
            "¿Estás seguro de que quieres limpiar todos los resultados?\nEsta acción no se puede deshacer.",
            "Confirmar Limpieza",
            JOptionPane.YES_NO_OPTION,
            JOptionPane.WARNING_MESSAGE
        );
        
        if (option == JOptionPane.YES_OPTION) {
            boolean success = mazeController.getResultDAO().deleteAll();
            if (success) {
                JOptionPane.showMessageDialog(
                    this,
                    "Archivo de resultados limpiado exitosamente.",
                    "Limpieza Completada",
                    JOptionPane.INFORMATION_MESSAGE
                );
                refreshTable();
            } else {
                JOptionPane.showMessageDialog(
                    this,
                    "Error al limpiar el archivo de resultados.",
                    "Error",
                    JOptionPane.ERROR_MESSAGE
                );
            }
        }
    }
    
    private void showTimeGraph() {
        // Verificar que el DAO esté disponible
        if (mazeController == null || mazeController.getResultDAO() == null) {
            JOptionPane.showMessageDialog(
                this,
                "DAO no disponible.",
                "Error",
                JOptionPane.ERROR_MESSAGE
            );
            return;
        }
        
        List<AlgorithmResult> results = mazeController.getResultDAO().findAll();
        
        if (results.isEmpty()) {
            JOptionPane.showMessageDialog(
                this,
                "No hay resultados disponibles para graficar.",
                "Sin Datos",
                JOptionPane.INFORMATION_MESSAGE
            );
            return;
        }
        
        try {
            // Crear y mostrar la ventana de gráficas con JFreeChart
            ChartWindow chartWindow = new ChartWindow(mazeController);
            chartWindow.setVisible(true);
        } catch (Exception e) {
            // Si hay algún error con JFreeChart, mostrar la gráfica ASCII como fallback
            JOptionPane.showMessageDialog(
                this,
                "Error al crear gráficas con JFreeChart: " + e.getMessage() + "\n\nMostrando gráfica simple...",
                "Error de Gráficas",
                JOptionPane.WARNING_MESSAGE
            );
            
            // Mostrar gráfica ASCII simple como respaldo
            showSimpleTimeGraph(results);
        }
    }
    
    private void showSimpleTimeGraph(List<AlgorithmResult> results) {
        // Crear una ventana simple con estadísticas de tiempos (fallback)
        StringBuilder stats = new StringBuilder();
        stats.append("=== GRÁFICA DE TIEMPOS DE EJECUCIÓN ===\n\n");
        
        // Agrupar por algoritmo
        java.util.Map<String, java.util.List<AlgorithmResult>> algorithmGroups = new java.util.HashMap<>();
        for (AlgorithmResult result : results) {
            algorithmGroups.computeIfAbsent(result.getAlgorithmName(), k -> new java.util.ArrayList<>()).add(result);
        }
        
        for (java.util.Map.Entry<String, java.util.List<AlgorithmResult>> entry : algorithmGroups.entrySet()) {
            String algorithmName = entry.getKey();
            List<AlgorithmResult> algorithmResults = entry.getValue();
            
            long totalTime = 0;
            long minTime = Long.MAX_VALUE;
            long maxTime = 0;
            
            for (AlgorithmResult result : algorithmResults) {
                long time = result.getExecutionTimeMs();
                totalTime += time;
                minTime = Math.min(minTime, time);
                maxTime = Math.max(maxTime, time);
            }
            
            double avgTime = (double) totalTime / algorithmResults.size();
            
            stats.append(algorithmName).append(":\n");
            stats.append("  - Ejecuciones: ").append(algorithmResults.size()).append("\n");
            stats.append("  - Tiempo promedio: ").append(String.format("%.2f ms", avgTime)).append("\n");
            stats.append("  - Tiempo mínimo: ").append(minTime).append(" ms\n");
            stats.append("  - Tiempo máximo: ").append(maxTime).append(" ms\n");
            stats.append("  - Tiempo total: ").append(totalTime).append(" ms\n\n");
            
            // Crear una barra visual simple
            int barLength = Math.min(50, (int)(avgTime / 10)); // Escalar la barra
            stats.append("  - Gráfica: ");
            for (int i = 0; i < barLength; i++) {
                stats.append("█");
            }
            stats.append(" ").append(String.format("%.0f ms", avgTime)).append("\n\n");
        }
        
        // Mostrar en un diálogo con scroll
        JTextArea textArea = new JTextArea(stats.toString());
        textArea.setEditable(false);
        textArea.setFont(new Font("Monospaced", Font.PLAIN, 12));
        
        JScrollPane scrollPane = new JScrollPane(textArea);
        scrollPane.setPreferredSize(new Dimension(500, 400));
        
        JOptionPane.showMessageDialog(
            this,
            scrollPane,
            "Gráfica de Tiempos de Ejecución (Simple)",
            JOptionPane.INFORMATION_MESSAGE
        );
    }
} 
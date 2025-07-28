package view;

import controller.MazeController;
import model.AlgorithmResult;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.data.category.DefaultCategoryDataset;
import org.jfree.data.general.DefaultPieDataset;
import view.MinecraftTheme;

import javax.swing.*;
import java.awt.*;
import java.util.List;
import java.util.Map;
import java.util.HashMap;

/**
 * Ventana que muestra gráficas de tiempos de ejecución usando JFreeChart
 */
public class ChartWindow extends JFrame {
    
    private MazeController mazeController;
    private JTabbedPane tabbedPane;
    
    public ChartWindow(MazeController mazeController) {
        this.mazeController = mazeController;
        
        // Configurar la ventana
        setTitle("Gráficas de Tiempos de Ejecución - Resolvedor de Laberintos");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setSize(1000, 700);
        setLocationRelativeTo(null);
        
        // Aplicar tema Minecraft a la ventana
        getContentPane().setBackground(MinecraftTheme.BACKGROUND_DARK);
        
        // Configurar el layout
        setLayout(new BorderLayout());
        
        // Crear el panel de pestañas
        tabbedPane = new JTabbedPane();
        
        // Aplicar tema Minecraft al tabbed pane
        tabbedPane.setBackground(MinecraftTheme.PANEL_BROWN);
        tabbedPane.setForeground(MinecraftTheme.TEXT_WHITE);
        tabbedPane.setFont(MinecraftTheme.getMinecraftFont(12f));
        
        // Crear las diferentes gráficas
        createBarChart();
        createPieChart();
        createAlgorithmComparisonChart();
        
        add(tabbedPane, BorderLayout.CENTER);
        
        // Botón para cerrar
        JButton closeButton = new JButton("Cerrar");
        MinecraftTheme.applyMinecraftButtonStyle(closeButton);
        closeButton.addActionListener(e -> dispose());
        
        JPanel buttonPanel = new JPanel();
        MinecraftTheme.applyMinecraftPanelStyle(buttonPanel);
        buttonPanel.add(closeButton);
        add(buttonPanel, BorderLayout.SOUTH);
    }
    
    private void createBarChart() {
        try {
            List<AlgorithmResult> results = mazeController.getResultDAO().findAll();
            
            if (results.isEmpty()) {
                JLabel noDataLabel = new JLabel("No hay datos disponibles para graficar");
                noDataLabel.setHorizontalAlignment(SwingConstants.CENTER);
                MinecraftTheme.applyMinecraftLabelStyleLight(noDataLabel);
                noDataLabel.setBackground(Color.WHITE);
                noDataLabel.setOpaque(true);
                tabbedPane.addTab("Gráfica de Barras", noDataLabel);
                return;
            }
            
            // Agrupar datos por algoritmo
            Map<String, Double> algorithmAverages = new HashMap<>();
            Map<String, Integer> algorithmCounts = new HashMap<>();
            
            for (AlgorithmResult result : results) {
                String algorithmName = result.getAlgorithmName();
                double currentAvg = algorithmAverages.getOrDefault(algorithmName, 0.0);
                int count = algorithmCounts.getOrDefault(algorithmName, 0);
                
                algorithmAverages.put(algorithmName, currentAvg + result.getExecutionTimeMs());
                algorithmCounts.put(algorithmName, count + 1);
            }
            
            // Calcular promedios
            DefaultCategoryDataset dataset = new DefaultCategoryDataset();
            for (Map.Entry<String, Double> entry : algorithmAverages.entrySet()) {
                String algorithmName = entry.getKey();
                int count = algorithmCounts.get(algorithmName);
                double average = entry.getValue() / count;
                
                dataset.addValue(average, "Tiempo Promedio (ms)", algorithmName);
            }
            
            JFreeChart barChart = ChartFactory.createBarChart(
                "Tiempos Promedio de Ejecución por Algoritmo",
                "Algoritmo",
                "Tiempo (ms)",
                dataset,
                PlotOrientation.VERTICAL,
                true, true, false
            );
            
            ChartPanel chartPanel = new ChartPanel(barChart);
            chartPanel.setPreferredSize(new Dimension(950, 600));
            
            tabbedPane.addTab("Gráfica de Barras", chartPanel);
            
        } catch (Exception e) {
            JLabel errorLabel = new JLabel("Error al crear gráfica de barras: " + e.getMessage());
            errorLabel.setHorizontalAlignment(SwingConstants.CENTER);
            tabbedPane.addTab("Gráfica de Barras", errorLabel);
        }
    }
    
    private void createPieChart() {
        try {
            List<AlgorithmResult> results = mazeController.getResultDAO().findAll();
            
            if (results.isEmpty()) {
                JLabel noDataLabel = new JLabel("No hay datos disponibles para graficar");
                noDataLabel.setHorizontalAlignment(SwingConstants.CENTER);
                MinecraftTheme.applyMinecraftLabelStyleLight(noDataLabel);
                noDataLabel.setBackground(Color.WHITE);
                noDataLabel.setOpaque(true);
                tabbedPane.addTab("Gráfica Circular", noDataLabel);
                return;
            }
            
            // Agrupar datos por algoritmo
            Map<String, Integer> algorithmCounts = new HashMap<>();
            
            for (AlgorithmResult result : results) {
                String algorithmName = result.getAlgorithmName();
                algorithmCounts.put(algorithmName, algorithmCounts.getOrDefault(algorithmName, 0) + 1);
            }
            
            DefaultPieDataset dataset = new DefaultPieDataset();
            for (Map.Entry<String, Integer> entry : algorithmCounts.entrySet()) {
                dataset.setValue(entry.getKey(), entry.getValue());
            }
            
            JFreeChart pieChart = ChartFactory.createPieChart(
                "Distribución de Ejecuciones por Algoritmo",
                dataset,
                true, true, false
            );
            
            ChartPanel chartPanel = new ChartPanel(pieChart);
            chartPanel.setPreferredSize(new Dimension(950, 600));
            
            tabbedPane.addTab("Gráfica Circular", chartPanel);
            
        } catch (Exception e) {
            JLabel errorLabel = new JLabel("Error al crear gráfica circular: " + e.getMessage());
            errorLabel.setHorizontalAlignment(SwingConstants.CENTER);
            tabbedPane.addTab("Gráfica Circular", errorLabel);
        }
    }
    

    
    private void createAlgorithmComparisonChart() {
        try {
            List<AlgorithmResult> results = mazeController.getResultDAO().findAll();
            
            if (results.isEmpty()) {
                JLabel noDataLabel = new JLabel("No hay datos disponibles para graficar");
                noDataLabel.setHorizontalAlignment(SwingConstants.CENTER);
                MinecraftTheme.applyMinecraftLabelStyleLight(noDataLabel);
                noDataLabel.setBackground(Color.WHITE);
                noDataLabel.setOpaque(true);
                tabbedPane.addTab("Comparación de Algoritmos", noDataLabel);
                return;
            }
            
            // Agrupar datos por algoritmo
            Map<String, Double> algorithmAverages = new HashMap<>();
            Map<String, Integer> algorithmCounts = new HashMap<>();
            Map<String, Long> algorithmMinTimes = new HashMap<>();
            Map<String, Long> algorithmMaxTimes = new HashMap<>();
            
            for (AlgorithmResult result : results) {
                String algorithmName = result.getAlgorithmName();
                long time = result.getExecutionTimeMs();
                
                // Promedio
                double currentAvg = algorithmAverages.getOrDefault(algorithmName, 0.0);
                int count = algorithmCounts.getOrDefault(algorithmName, 0);
                algorithmAverages.put(algorithmName, currentAvg + time);
                algorithmCounts.put(algorithmName, count + 1);
                
                // Mínimo y máximo
                algorithmMinTimes.put(algorithmName, Math.min(algorithmMinTimes.getOrDefault(algorithmName, Long.MAX_VALUE), time));
                algorithmMaxTimes.put(algorithmName, Math.max(algorithmMaxTimes.getOrDefault(algorithmName, 0L), time));
            }
            
            // Crear dataset con múltiples series
            DefaultCategoryDataset dataset = new DefaultCategoryDataset();
            
            for (Map.Entry<String, Double> entry : algorithmAverages.entrySet()) {
                String algorithmName = entry.getKey();
                int count = algorithmCounts.get(algorithmName);
                double average = entry.getValue() / count;
                long minTime = algorithmMinTimes.get(algorithmName);
                long maxTime = algorithmMaxTimes.get(algorithmName);
                
                dataset.addValue(average, "Promedio", algorithmName);
                dataset.addValue(minTime, "Mínimo", algorithmName);
                dataset.addValue(maxTime, "Máximo", algorithmName);
            }
            
            JFreeChart comparisonChart = ChartFactory.createBarChart(
                "Comparación Completa de Algoritmos (Promedio, Mínimo, Máximo)",
                "Algoritmo",
                "Tiempo (ms)",
                dataset,
                PlotOrientation.VERTICAL,
                true, true, false
            );
            
            ChartPanel chartPanel = new ChartPanel(comparisonChart);
            chartPanel.setPreferredSize(new Dimension(950, 600));
            
            tabbedPane.addTab("Comparación de Algoritmos", chartPanel);
            
        } catch (Exception e) {
            JLabel errorLabel = new JLabel("Error al crear gráfica de comparación: " + e.getMessage());
            errorLabel.setHorizontalAlignment(SwingConstants.CENTER);
            tabbedPane.addTab("Comparación de Algoritmos", errorLabel);
        }
    }
} 
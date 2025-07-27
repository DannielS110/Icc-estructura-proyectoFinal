package controller.listeners;

import controller.MazeController;
import view.StatisticsWindow;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * Listener para abrir la ventana de estadísticas
 */
public class StatisticsWindowListener implements ActionListener {
    
    private final MazeController mazeController;
    
    public StatisticsWindowListener(MazeController mazeController) {
        this.mazeController = mazeController;
    }
    
    @Override
    public void actionPerformed(ActionEvent e) {
        // Crear y mostrar la ventana de estadísticas
        StatisticsWindow statisticsWindow = new StatisticsWindow(mazeController);
        statisticsWindow.setVisible(true);
    }
} 
package controller.listeners;

import controller.MazeController;
import view.drawable.MazePanel;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * ActionListener para el botón de edición de paredes.
 * Alterna entre el modo de edición de paredes y el modo normal.
 */
public class WallEditButtonListener implements ActionListener {
    
    private final MazeController mazeController;
    private MazePanel mazePanel;
    
    public WallEditButtonListener(MazeController mazeController, MazePanel mazePanel) {
        this.mazeController = mazeController;
        this.mazePanel = mazePanel;
    }
    
    public void setMazePanel(MazePanel mazePanel) {
        this.mazePanel = mazePanel;
    }
    
    @Override
    public void actionPerformed(ActionEvent e) {
        boolean currentMode = mazeController.isWallEditMode();
        mazeController.setWallEditMode(!currentMode);
        
        // Actualizar el texto del botón
        if (e.getSource() instanceof javax.swing.JButton) {
            javax.swing.JButton button = (javax.swing.JButton) e.getSource();
            if (mazeController.isWallEditMode()) {
                button.setText("Salir Edición");
                button.setToolTipText("Haz clic para salir del modo de edición de paredes");
            } else {
                button.setText("Editar Paredes");
                button.setToolTipText("Haz clic para entrar al modo de edición de paredes");
            }
        }
        
        // Actualizar el estado del laberinto
        mazeController.setInstructions();
    }
} 
package controller.listeners;

import controller.MazeController;
import model.Cell;
import model.Direction;
import model.Maze;
import view.drawable.MazePanel;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

/**
 * Mouse listener para editar paredes del laberinto manualmente.
 * Permite al usuario hacer clic en las paredes para agregarlas o quitarlas.
 */
public class MazeWallEditorListener extends MouseAdapter {
    
    private final MazePanel mazePanel;
    private final MazeController mazeController;
    private final Maze maze;
    
    public MazeWallEditorListener(MazePanel mazePanel, MazeController mazeController) {
        this.mazePanel = mazePanel;
        this.mazeController = mazeController;
        this.maze = mazeController.getMaze();
    }
    
    @Override
    public void mouseClicked(MouseEvent e) {
        // Solo procesar si estamos en modo de edición
        if (!mazeController.isWallEditMode()) {
            return;
        }
        
        int mouseX = e.getX();
        int mouseY = e.getY();
        
        // Buscar la celda y dirección donde se hizo clic
        WallClickInfo wallInfo = findWallAtPosition(mouseX, mouseY);
        
        if (wallInfo != null) {
            Cell cell = wallInfo.cell;
            Direction direction = wallInfo.direction;
            
            // Obtener la celda vecina
            Cell neighborCell = getNeighborCell(cell, direction);
            
            // Alternar el estado de la pared en AMBAS celdas
            if (cell.wallMissing(direction)) {
                // Agregar pared en ambas celdas
                cell.addWall(direction);
                if (neighborCell != null) {
                    neighborCell.addWall(direction.oppositeDirection());
                }
            } else {
                // Quitar pared en ambas celdas
                cell.removeWall(direction);
                if (neighborCell != null) {
                    neighborCell.removeWall(direction.oppositeDirection());
                }
            }
            
            // Repintar el laberinto
            mazePanel.repaint();
        }
    }
    
    /**
     * Encuentra la pared en la posición del clic del mouse
     */
    private WallClickInfo findWallAtPosition(int mouseX, int mouseY) {
        int cellSize = 20; // CellDrawableConstants.CELL_SIZE
        int margin = 10;   // CellDrawableConstants.MARGIN
        
        // Ajustar coordenadas por el offset del panel
        int adjustedX = mouseX - mazePanel.getXOffset();
        int adjustedY = mouseY - mazePanel.getYOffset();
        
        // Calcular la celda base
        int col = (adjustedX - margin) / cellSize;
        int row = (adjustedY - margin) / cellSize;
        
        // Verificar límites
        if (row < 0 || row >= maze.numRows() || col < 0 || col >= maze.numCols()) {
            return null;
        }
        
        Cell cell = maze.mazeCell(row, col);
        if (cell == null) {
            return null;
        }
        
        // Calcular posición relativa dentro de la celda
        int cellX = adjustedX - margin - col * cellSize;
        int cellY = adjustedY - margin - row * cellSize;
        
        // Determinar qué pared fue clickeada
        Direction clickedDirection = determineWallDirection(cellX, cellY, cellSize);
        
        if (clickedDirection != null) {
            return new WallClickInfo(cell, clickedDirection);
        }
        
        return null;
    }
    
    /**
     * Determina qué dirección de pared fue clickeada basándose en la posición relativa
     */
    private Direction determineWallDirection(int cellX, int cellY, int cellSize) {
        int wallThickness = 2; // Grosor aproximado de las paredes
        
        // Pared superior
        if (cellY <= wallThickness) {
            return Direction.UP;
        }
        
        // Pared inferior
        if (cellY >= cellSize - wallThickness) {
            return Direction.DOWN;
        }
        
        // Pared izquierda
        if (cellX <= wallThickness) {
            return Direction.LEFT;
        }
        
        // Pared derecha
        if (cellX >= cellSize - wallThickness) {
            return Direction.RIGHT;
        }
        
        return null;
    }
    
    /**
     * Obtiene la celda vecina en la dirección especificada
     */
    private Cell getNeighborCell(Cell cell, Direction direction) {
        int newRow = cell.row() + direction.dy;
        int newCol = cell.col() + direction.dx;
        
        // Verificar límites
        if (!maze.inBounds(newRow, newCol)) {
            return null;
        }
        
        return maze.mazeCell(newRow, newCol);
    }
    
    /**
     * Clase auxiliar para almacenar información sobre el clic en una pared
     */
    private static class WallClickInfo {
        final Cell cell;
        final Direction direction;
        
        WallClickInfo(Cell cell, Direction direction) {
            this.cell = cell;
            this.direction = direction;
        }
    }
} 
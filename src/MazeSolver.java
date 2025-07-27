import controller.MazeController;
import view.SplashScreen;
import javax.swing.SwingUtilities;

/**
 * MazeSolver --- A maze solver application that automatically generates a random maze, and then proceeds to traverse
 * and solve the maze using a variety of graph traversal algorithms (BFS, DFS, A*). This generation and traversal is
 * visualized using JSwing, and allows the user to interact with the maze, enabling them to choose the size of the maze,
 * the maze start and end points, the solution algorithm, as well as vary the animation speed.
 * @author
 */
public class MazeSolver {
    public static void main(String[] args) {
        // Iniciar con la pantalla de carga
        SwingUtilities.invokeLater(() -> {
            SplashScreen splash = new SplashScreen();
            splash.setVisible(true);
        });
    }
}
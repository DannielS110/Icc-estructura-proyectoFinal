package model;

import controller.MazeController;
import model.solvers.BFS;
import model.solvers.DFS;
import model.solvers.MazeSolverRecursivo;
import model.solvers.MazeSolverRecursivoCompleto;
import model.solvers.MazeSolverRecursivoBT;

/**
 * The MazeSolverWorker factory.
 */
public class MazeSolverWorkerFactory {

    public static MazeSolverWorker initMazeSolver(SolverType solverType, Maze maze, MazeController mazeController) {
        if (maze == null) {
            return null;
        }

        MazeSolverWorker mazeSolverWorker;

        switch (solverType) {
            case BFS:
                mazeSolverWorker = new BFS(maze, mazeController);
                break;

            case DFS:
                mazeSolverWorker = new DFS(maze, mazeController);
                break;

            case RECURSIVO:
                mazeSolverWorker = new MazeSolverRecursivo(maze, mazeController);
                break;

            case RECURSIVO_COMPLETO:
                mazeSolverWorker = new MazeSolverRecursivoCompleto(maze, mazeController);
                break;

            case RECURSIVO_BT:
                mazeSolverWorker = new MazeSolverRecursivoBT(maze, mazeController);
                break;

            // Default to BFS
            default:
                mazeSolverWorker = new BFS(maze, mazeController);
                break;
        }

        return mazeSolverWorker;
    }
}
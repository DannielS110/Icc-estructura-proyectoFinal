package model;

import controller.MazeController;
import model.generators.RecursiveBacktracker;

/**
 * The MazeGeneratorWorker factory.
 */
public class MazeGeneratorWorkerFactory {
    public static MazeGeneratorWorker initMazeGenerator(GeneratorType generatorType, Maze maze,
                                                        MazeController mazeController) {
        if (maze == null || mazeController == null) {
            return null;
        }

        MazeGeneratorWorker mazeGeneratorWorker;

        switch (generatorType) {
            case RECURSIVE_BACKTRACKER:
                mazeGeneratorWorker = new RecursiveBacktracker(maze, mazeController);
                break;

            // Default to recursive backtracker
            default:
                mazeGeneratorWorker = new RecursiveBacktracker(maze, mazeController);
                break;
        }

        return mazeGeneratorWorker;
    }
}
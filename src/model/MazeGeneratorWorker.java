package model;

import controller.MazeController;

import javax.swing.*;

/**
 * An abstract class that extends the SwingWorker class. This class is extended from by all maze generators. All
 * SwingWorker classes extending this class will return a Boolean class type indicating if the maze generation was a
 * success, and publish Maze instances at each iteration of the generation to be repainted in the view.
 */
public abstract class MazeGeneratorWorker extends SwingWorker<Boolean, Maze> {
    protected final Maze maze;
    protected final MazeController mazeController;

    protected MazeGeneratorWorker(Maze maze, MazeController mazeController) {
        this.maze = maze;
        this.mazeController = mazeController;
    }
}

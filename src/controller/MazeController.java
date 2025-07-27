package controller;

import controller.listeners.*;
import dao.AlgorithmResultDAO;
import dao.daoImpl.AlgorithmResultDAOFile;
import model.*;
import view.MazeView;

import javax.swing.*;

/**
 * The controller of the maze (i.e. the controller in the MVC design pattern). This is the main orchestrator of the
 * application, initializing and controlling the maze and the maze view, handling GUI interactions, and managing the
 * SwingWorker threads for maze generation, solving, and solution path drawing.
 */
public class MazeController {
    // Model
    private final Maze maze;
    // View
    private final MazeView view;
    //// Custom Maze Dimensions
    private final MazeCustomNumRowsListener mazeCustomNumRowsListener;
    private final MazeCustomNumColsListener mazeCustomNumColsListener;
    //// Buttons
    private final MazeGeneratorListener mazeGeneratorListener;
    private final MazeSolverListener mazeSolverListener;
    private final MazeSolverSelectionRadioListener mazeSolverSelectionRadioListener;
    private final MazeResetListener mazeResetListener;
    private final StatisticsWindowListener statisticsWindowListener;
    private final WallEditButtonListener wallEditButtonListener;
    // La tabla de resultados ahora se maneja en una ventana separada
    //// Speed Slider
    private final MazeAnimationSpeedSliderListener mazeAnimationSpeedSliderListener;
    private MazeState state; // Stores the current state of the maze.
    private int animationSpeed;

    // Listeners
    private int numRows;
    private int numCols;
    private GeneratorType generatorType;
    private MazeGeneratorWorker generator;
    private SolverType solverType;
    private MazeSolverWorker solver;
    private MazeSolutionWalkerWorker solutionWalker;

    // DAO for saving algorithm results
    private final AlgorithmResultDAO resultDAO;
    private long solveStartTime;
    private int visitedCellsCount;

    public MazeController() {
        this.state = MazeState.INIT;

        this.maze = new Maze();
        this.generatorType = GeneratorType.RECURSIVE_BACKTRACKER;
        this.solverType = SolverType.BFS;

        this.mazeCustomNumRowsListener = new MazeCustomNumRowsListener(this);
        this.mazeCustomNumColsListener = new MazeCustomNumColsListener(this);

        this.mazeGeneratorListener = new MazeGeneratorListener(this);
        this.mazeSolverSelectionRadioListener = new MazeSolverSelectionRadioListener(this);
        this.mazeSolverListener = new MazeSolverListener(this);
        this.mazeResetListener = new MazeResetListener(this);
        this.statisticsWindowListener = new StatisticsWindowListener(this);
        this.wallEditButtonListener = new WallEditButtonListener(this, null); // Se inicializará después

        this.mazeAnimationSpeedSliderListener = new MazeAnimationSpeedSliderListener(this);

        this.view = new MazeView(maze, this);

        this.animationSpeed = MazeAnimationConstants.DEFAULT_ANIMATION_SLEEP;

        this.numRows = MazeConstants.DEFAULT_NUM_ROWS;
        this.numCols = MazeConstants.DEFAULT_NUM_COLS;

        // Initialize DAO for saving algorithm results
        this.resultDAO = new AlgorithmResultDAOFile();
        this.solveStartTime = 0;
        this.visitedCellsCount = 0;
    }

    public MazeState getState() {
        return state;
    }

    public void setGeneratorType(GeneratorType generatorType) {
        this.generatorType = generatorType;
    }

    public SolverType getSolverType() {
        return solverType;
    }

    public void setSolverType(SolverType solverType) {
        this.solverType = solverType;
    }

    public MazeGeneratorListener getMazeGeneratorListener() {
        return mazeGeneratorListener;
    }

    public MazeSolverSelectionRadioListener getMazeSolverSelectionRadioListener() {
        return mazeSolverSelectionRadioListener;
    }

    public MazeSolverListener getMazeSolverListener() {
        return mazeSolverListener;
    }

    public MazeResetListener getMazeResetListener() {
        return mazeResetListener;
    }

    public StatisticsWindowListener getStatisticsWindowListener() {
        return statisticsWindowListener;
    }

    public WallEditButtonListener getWallEditButtonListener() {
        return wallEditButtonListener;
    }

    public MazeCustomNumRowsListener getMazeCustomNumRowsListener() {
        return mazeCustomNumRowsListener;
    }

    public MazeCustomNumColsListener getMazeCustomNumColsListener() {
        return mazeCustomNumColsListener;
    }

    public MazeAnimationSpeedSliderListener getMazeAnimationSpeedSliderListener() {
        return mazeAnimationSpeedSliderListener;
    }

    public void setMazeNumRows(int numRows) {
        this.numRows = numRows;
    }

    public void setMazeNumCols(int numCols) {
        this.numCols = numCols;
    }

    /**
     * Returns the thread sleep time for animation based on the current animation speed (set by the speed slider) and
     * maze state. In an effort to limit the maximum speed of animation, the animation speed is scaled by a scaling
     * factor corresponding to the current maze state. As some stages are more interesting to visualize than others,
     * each stage has an individual scaling factor.
     *
     * @return the thread sleep time for animation
     */
    public long getAnimationSpeed() {
        double animationSpeedMultiplier;

        switch (state) {
            case GENERATING:
                animationSpeedMultiplier = MazeAnimationConstants.GENERATION_SLEEP_TIME_MULTIPLIER;
                break;
            case SOLVING:
                animationSpeedMultiplier = MazeAnimationConstants.SOLVE_SLEEP_TIME_MULTIPLIER;
                break;
            case SOLVED:
                animationSpeedMultiplier = MazeAnimationConstants.SOLUTION_SLEEP_TIME_MULTIPLIER;
                break;
            default:
                animationSpeedMultiplier = MazeAnimationConstants.DEFAULT_ANIMATION_SLEEP;
                break;
        }

        return (long) (animationSpeed * animationSpeedMultiplier);
    }

    public void setAnimationSpeed(int animationSpeed) {
        this.animationSpeed = animationSpeed;
    }

    public void generate() {
        initGenerate();
        generateMaze();
    }

    private void initGenerate() {
        maze.initMaze(numRows, numCols);
        view.resize();
        generator = MazeGeneratorWorkerFactory.initMazeGenerator(generatorType, maze, this);
    }

    private void generateMaze() {
        state = MazeState.GENERATING;
        setInstructions();
        generator.execute();
    }

    /**
     * Function called on maze generation success to update the maze state, and to default the starting and ending cell
     * for solving the maze.
     */
    public void generateMazeSuccess() {
        state = MazeState.GENERATED;
        maze.defaultWaypoints();
        setInstructions();
    }

    public void solve() {
        initSolve();
        solveMaze();
    }

    private void initSolve() {
        solver = MazeSolverWorkerFactory.initMazeSolver(solverType, maze, this);
    }

    private void solveMaze() {
        state = MazeState.SOLVING;
        setInstructions();

        // Record start time for measuring execution time
        solveStartTime = System.currentTimeMillis();

        solver.execute();
    }

    /**
     * Function called on maze generation success to update the maze state, and to trigger the solution path walker.
     */
    public void solveMazeSuccess() {
        state = MazeState.SOLVED;

        // Save algorithm result to DAO
        saveAlgorithmResult(true);

        walkSolutionPath();
        setInstructions();
    }

    /**
     * Executes the solution path walker to draw the solution path from the starting cell to the ending cell.
     */
    private void walkSolutionPath() {
        solutionWalker = new MazeSolutionWalkerWorker(maze, this);
        solutionWalker.execute();
    }

    /**
     * Resets the maze to its initial state, ready to generate a new maze. This includes cancelling all currently
     * running threads, resetting the maze cells, and repainting the view.
     */
    public void reset() {
        // If we were solving and didn't succeed, save the failed result
        if (state == MazeState.SOLVING && solveStartTime > 0) {
            saveAlgorithmResult(false);
        }

        resetThreads();

        state = MazeState.INIT;
        setInstructions();

        maze.resetMaze();
        view.resetView();
    }

    /**
     * Cancels any currently running SwingWorker.
     */
    private void resetThreads() {
        if (generator != null) {
            generator.cancel(true);
            generator = null;
        }

        if (solver != null) {
            solver.cancel(true);
            solver = null;
        }

        if (solutionWalker != null) {
            solutionWalker.cancel(true);
            solutionWalker = null;
        }
    }

    public void repaintMaze(Maze newMaze) {
        view.repaintMaze(newMaze);
    }

    /**
     * Updates instructions for maze on the GUI (based on the maze state) asynchronously.
     */
    public void setInstructions() {
        SwingUtilities.invokeLater(view::setInstructions);
    }

    /**
     * Saves the algorithm result to the DAO
     * @param solutionFound Whether a solution was found
     */
    private void saveAlgorithmResult(boolean solutionFound) {
        try {
            // Calculate execution time
            long executionTime = System.currentTimeMillis() - solveStartTime;

            // Count visited cells
            visitedCellsCount = countVisitedCells();

            // Get solution path length
            int solutionPathLength = getSolutionPathLength();

            // Debug logging
            System.out.println("Debug - Algoritmo: " + solverType.getName());
            System.out.println("Debug - Celdas visitadas: " + visitedCellsCount);
            System.out.println("Debug - Longitud del camino: " + solutionPathLength);
            System.out.println("Debug - Solución encontrada: " + solutionFound);

            // Create algorithm result
            AlgorithmResult result = new AlgorithmResult(
                    solverType.getName(),
                    maze.numRows(),
                    maze.numCols(),
                    executionTime,
                    visitedCellsCount,
                    solutionPathLength,
                    solutionFound
            );

            // Save to DAO
            boolean saved = resultDAO.save(result);
            if (saved) {
                System.out.println("Resultado guardado exitosamente: " + result.toString());
                // Los resultados se guardan automáticamente, la tabla se actualiza cuando se abre la ventana
            } else {
                System.err.println("Error al guardar el resultado del algoritmo");
            }
        } catch (Exception e) {
            System.err.println("Error al guardar resultado del algoritmo: " + e.getMessage());
        }
    }

    /**
     * Counts the number of visited cells in the maze
     * Includes cells marked as VISITED, VISITING, and any cell that has been explored
     * @return Number of visited cells
     */
    private int countVisitedCells() {
        int count = 0;
        for (int i = 0; i < maze.numRows(); i++) {
            for (int j = 0; j < maze.numCols(); j++) {
                Cell cell = maze.mazeCell(i, j);
                if (cell != null) {
                    // Contar celdas que han sido visitadas, están siendo visitadas, o tienen padre (han sido exploradas)
                    if (cell.visited() || cell.visiting() || cell.parent() != null) {
                        count++;
                    }
                }
            }
        }
        return count;
    }

    /**
     * Gets the length of the solution path or explored path
     * @return Length of the solution path or explored path
     */
    private int getSolutionPathLength() {
        // Si hay un objetivo (solución encontrada), contar el camino de solución
        if (maze.getGoal() != null) {
            int length = 0;
            Cell current = maze.getGoal();
            while (current != null) {
                length++;
                current = current.parent();
            }
            return length;
        }

        // Si no hay solución, contar el camino explorado más largo
        int maxPathLength = 0;
        for (int i = 0; i < maze.numRows(); i++) {
            for (int j = 0; j < maze.numCols(); j++) {
                Cell cell = maze.mazeCell(i, j);
                if (cell != null && (cell.visited() || cell.visiting() || cell.parent() != null)) {
                    // Contar el camino desde esta celda hasta la raíz
                    int pathLength = 0;
                    Cell current = cell;
                    while (current != null) {
                        pathLength++;
                        current = current.parent();
                    }
                    maxPathLength = Math.max(maxPathLength, pathLength);
                }
            }
        }
        return maxPathLength;
    }

    /**
     * Gets the DAO instance for external access
     * @return The DAO instance
     */
    public AlgorithmResultDAO getResultDAO() {
        return resultDAO;
    }

    /**
     * Gets statistics from the DAO
     * @return Statistics string
     */
    public String getAlgorithmStatistics() {
        return resultDAO.getStatistics();
    }

    public Maze getMaze() {
        return maze;
    }

    private boolean wallEditMode = false;

    public boolean isWallEditMode() {
        return wallEditMode;
    }

    public void setWallEditMode(boolean wallEditMode) {
        this.wallEditMode = wallEditMode;

        // Actualizar el estado del laberinto
        if (wallEditMode) {
            this.state = MazeState.WALL_EDIT;
        } else {
            // Volver al estado anterior (GENERATED si existe un laberinto)
            if (maze.getStartingCell() != null) {
                this.state = MazeState.GENERATED;
            } else {
                this.state = MazeState.INIT;
            }
        }

        // Actualizar las instrucciones
        setInstructions();
    }

    // Los métodos de la tabla de resultados ahora se manejan en la ventana separada
}

/*

	* Old method to force the user to select the start and end points for maze solving. In the current version,
	* the user has a choice as to whether or not to set the start and end points.

	private void setWaypoints() {
		synchronized (view) {
			while(maze.startingCell == null || maze.endingCell == null) {
				try {
					view.wait();
				} catch(InterruptedException e) {
					System.out.println(e);
				}
			}
		}
	}

*/
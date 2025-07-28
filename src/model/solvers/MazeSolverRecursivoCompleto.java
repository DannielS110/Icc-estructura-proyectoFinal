package model.solvers;

import controller.MazeController;
import model.Cell;
import model.Cell.CellVisitState;
import model.Direction;
import model.Maze;
import model.MazeSolverWorker;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CancellationException;

/**
 * A SwingWorker class (extending MazeSolverWorker) that implements a recursive maze solving algorithm.
 * This algorithm uses recursion to explore the maze in 4 directions (UP, DOWN, LEFT, RIGHT).
 * It follows the same structure as BFS/DFS but uses recursive calls instead of queues/stacks.
 */
public class MazeSolverRecursivoCompleto extends MazeSolverWorker {
	
	private boolean solutionFound;
	
	public MazeSolverRecursivoCompleto(Maze maze, MazeController mazeController) {
		super(maze, mazeController);
		this.solutionFound = false;
	}

	@Override
	protected Boolean doInBackground() throws Exception {
		Cell start = maze.getStartingCell();
		Cell end = maze.getEndingCell();
		
		if (start == null || end == null) {
			return false;
		}
		
		// Iniciar búsqueda recursiva desde la celda de inicio
		return solveRecursive(start, end);
	}

	/**
	 * Método recursivo que busca la solución en 4 direcciones
	 * @param current La celda actual
	 * @param end La celda objetivo
	 * @return true si se encontró solución desde esta posición
	 */
	private boolean solveRecursive(Cell current, Cell end) throws InterruptedException {
		// Marcar como visitada y actual
		current.setCurrent(true);
		current.setVisitState(CellVisitState.VISITED);

		// Verificar si llegamos al objetivo
		if (current == end) {
			maze.setGoal(current);
			return true;
		}

		// Obtener vecinos válidos en 4 direcciones
		List<Cell> validNeighbors = getValidNeighbors4Directions(current);

		// Explorar cada vecino válido
		for (Cell neighbor : validNeighbors) {
			neighbor.setVisitState(CellVisitState.VISITING);
			neighbor.setParent(current);
			
			// Publicar el estado actual para animación
			publish(maze);
			Thread.sleep(mazeController.getAnimationSpeed());
			
			// Llamada recursiva
			if (solveRecursive(neighbor, end)) {
				current.setCurrent(false);
				return true;
			}
		}

		// Publicar el estado actual para animación
		publish(maze);
		Thread.sleep(mazeController.getAnimationSpeed());

		current.setCurrent(false);
		return false;
	}

	/**
	 * Obtiene los vecinos válidos en 4 direcciones
	 * @param current La celda actual
	 * @return Lista de vecinos válidos
	 */
	private List<Cell> getValidNeighbors4Directions(Cell current) {
		List<Cell> validNeighbors = new ArrayList<>();
		int currRow = current.row();
		int currCol = current.col();
		
		// Direcciones: ARRIBA, ABAJO, IZQUIERDA, DERECHA
		Direction[] directions = {Direction.UP, Direction.DOWN, Direction.LEFT, Direction.RIGHT};
		
		for (Direction direction : directions) {
			int newRow = currRow + direction.dy;
			int newCol = currCol + direction.dx;

			// Verificar que la celda está dentro de los límites
			if (!maze.inBounds(newRow, newCol)) {
				continue;
			}

			Cell neighbor = maze.mazeCell(newRow, newCol);

			// Verificar que la celda no ha sido visitada y que no hay pared en esa dirección
			if (current.wallMissing(direction) && neighbor.unvisited()) {
				validNeighbors.add(neighbor);
			}
		}

		return validNeighbors;
	}

	/**
	 * Override of the SwingWorker process function, which repaints the maze at every iteration of maze solving
	 * asynchronously on the event dispatch thread.
	 */
	@Override
	protected void process(List<Maze> chunks) {
		for (Maze maze : chunks) {
			mazeController.repaintMaze(maze);
		}
	}

	/**
	 * Override of the SwingWorker done function (run after the thread is completed). If the maze was successfully
	 * solved, this will call the solveMazeSuccess method defined in the controller, which will trigger the walking of
	 * the solution path. In the case of this SwingWorker being interrupted or cancelled, it will have been done by the
	 * maze reset function in the controller, and any clean-up will be handled there. For other exceptions, these will
	 * not have been triggered by the maze reset function, so that will trigger the maze reset function to clean up.
	 */
	@Override
	protected void done() {
		Boolean status;

		try {
			status = get();

			if (status) {
				mazeController.solveMazeSuccess();
			} else {
				mazeController.reset();
			}
		} catch (CancellationException ignore) {
		} catch (Exception e) {
			mazeController.reset();
		}
	}
} 
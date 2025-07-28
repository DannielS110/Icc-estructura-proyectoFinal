package model.solvers;

import controller.MazeController;
import model.Cell;
import model.Cell.CellVisitState;
import model.Direction;
import model.Maze;
import model.MazeSolverWorker;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.util.concurrent.CancellationException;

/**
 * A SwingWorker class (extending MazeGeneratorWorker) that implements the BFS graph traversal algorithm. This algorithm
 * traverses a graph by starting at the root node (in this case the starting cell), and explores all of the cells at
 * the present depth before moving onto nodes at the next depth. This uses the opposite strategy compared to DFS. The
 * algorithm uses a Queue to store the cells to be visited at each depth.
 * <p>
 * https://en.wikipedia.org/wiki/Breadth-first_search
 */
public class BFS extends MazeSolverWorker {
	public BFS(Maze maze, MazeController mazeController) {
		super(maze, mazeController);
	}

	@Override
	protected Boolean doInBackground() throws Exception {
		Cell current;
		Cell end = maze.getEndingCell();
		Queue<Cell> searchQueue = new LinkedList<>();
		searchQueue.add(maze.getStartingCell());

		while (!searchQueue.isEmpty()) {
			current = searchQueue.remove();
			current.setCurrent(true);
			current.setVisitState(CellVisitState.VISITED);

			if (current == end) { // Check if the current cell is the goal cell (i.e. maze has been solved)
				maze.setGoal(current);
				return true;
			}

			List<Cell> unvisitedNeighbors = unvisitedNeighbors(current);

            /*
            	Add each valid unvisited neighboring cell to the Queue to be visited later
             */
			for (Cell neighbor : unvisitedNeighbors) {
				searchQueue.add(neighbor);
				neighbor.setVisitState(CellVisitState.VISITING);
				neighbor.setParent(current);
			}

			publish(maze); // Publish the current maze state to be repainted on the event dispatch thread

			Thread.sleep(mazeController.getAnimationSpeed());

			current.setCurrent(false);
		}

		return false;
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

	/**
	 * Iterates through all neighbours of the currently visited cell (up, down left, right), and returns all of the
	 * valid neighboring cells that have not already been visited.
	 *
	 * @param current The current cell
	 * @return A list of valid (i.e. in bounds) neighboring cells that have not already been visited
	 */
	private List<Cell> unvisitedNeighbors(Cell current) {
		Cell neighbor;
		List<Cell> unvisitedNeighbors = new ArrayList<>();
		int currRow = current.row();
		int currCol = current.col();
		int newRow, newCol;

		for (Direction direction : Direction.values()) {
			newCol = currCol + direction.dx;
			newRow = currRow + direction.dy;

			// Check that the cell is in the bounds of the maze
			if (!maze.inBounds(newRow, newCol)) {
				continue;
			}

			neighbor = maze.mazeCell(newRow, newCol);

			// Check that the cell hasn't already been visited and that a wall doesn't exist in that direction
			if (current.wallMissing(direction) && neighbor.unvisited()) {
				unvisitedNeighbors.add(neighbor);
			}
		}

		return unvisitedNeighbors;
	}
}

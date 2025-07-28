package model.solvers;

import controller.MazeController;
import model.Cell;
import model.Cell.CellVisitState;
import model.Direction;
import model.Maze;
import model.MazeSolverWorker;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.Stack;
import java.util.concurrent.CancellationException;

/**
 * A SwingWorker class (extending MazeGeneratorWorker) that implements the DFS graph traversal algorithm. This algorithm
 * traverses a graph by starting at the root node (in this case the starting cell), and explores as far as possible
 * along a specific branch before backtracking. This uses the opposite strategy compared to BFS. This algorithm can be
 * implemented either recursively or iteratively (I implemented it using the iterative approach). The algorithm uses a
 * Stack to keep track of all the previously visited nodes which it will pop off when backtracking.
 * <p>
 * https://en.wikipedia.org/wiki/Depth-first_search
 */
public class DFS extends MazeSolverWorker {
	public DFS(Maze maze, MazeController mazeController) {
		super(maze, mazeController);
	}

	@Override
	protected Boolean doInBackground() throws Exception {
		Stack<Cell> searchStack = new Stack<>();
		Cell root, current, unvisitedNeighbor, end;

		root = maze.getStartingCell();
		end = maze.getEndingCell();

		searchStack.push(root);
		root.setVisitState(CellVisitState.VISITING);

		while (!searchStack.isEmpty()) {
			current = searchStack.peek();
			current.setCurrent(true);

			if (current == end) { // Check if the current cell is the goal cell (i.e. maze has been solved)
				maze.setGoal(current);
				return true;
			}

			unvisitedNeighbor = unvisitedNeighbor(current);

			if (unvisitedNeighbor != null) { // There is an unvisited neighboring cells to visit from the current cell.
				searchStack.push(unvisitedNeighbor);
				unvisitedNeighbor.setVisitState(CellVisitState.VISITING);

				unvisitedNeighbor.setParent(current);
			} else {
				/*
	        		There are no unvisited neighboring cells for the current cell. Backtrack to the cell that was
	        		visited previously to find an unvisited neighboring cell from there.
	        	 */
				current.setVisitState(CellVisitState.VISITED);
				searchStack.pop();
			}

			publish(maze); // Publish the current maze state to be repainted on the event dispatch thread.

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
	 * Iterates through all neighbours of the currently visited cell (up, down left, right), and returns a randomly
	 * picked valid neighboring cell that has not already been visited.
	 *
	 * @param current The current cell
	 * @return A randomly picked valid (i.e. in bounds) neighboring cell that has not already been visited
	 */
	private Cell unvisitedNeighbor(Cell current) {
		Cell neighbor;
		List<Cell> unvisitedNeighbors = new ArrayList<>();
		int currRow = current.row();
		int currCol = current.col();
		int newRow, newCol;

		Random rand = new Random();

		for (Direction direction : Direction.values()) {
			newRow = currRow + direction.dy;
			newCol = currCol + direction.dx;

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

		if (unvisitedNeighbors.size() == 0) {
			return null;
		}

		return unvisitedNeighbors.get(rand.nextInt(unvisitedNeighbors.size()));
	}
}

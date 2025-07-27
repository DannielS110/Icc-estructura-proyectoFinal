package controller.listeners;

import controller.MazeController;
import model.MazeState;
import view.drawable.MazePanel;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

/**
 * A MouseAdapter that listens for clicks on a generated maze to set the start and end points for the maze solver.
 * Checks that the state of the maze is viable for setting the start and end points.
 */
public class MazeWaypointClickListener extends MouseAdapter {
	private final MazePanel mazePanel;
	private final MazeController mazeController;

	public MazeWaypointClickListener(MazePanel mazePanel, MazeController mazeController) {
		super();
		this.mazePanel = mazePanel;
		this.mazeController = mazeController;
	}

	@Override
	public void mouseReleased(MouseEvent mouseEvent) {
		if (canSet()) {
			mazePanel.setWaypoint(mouseEvent.getX(), mouseEvent.getY());
		}
	}

	/**
	 * Determines if setting the start and end points can be set. The waypoints can only be set if the maze is in the
	 * GENERATED state (after a maze has been generated but not being solved or already solved).
	 */
	private boolean canSet() {
		MazeState mazeState = mazeController.getState();
		return mazeState == MazeState.GENERATED;
	}

}

/*

	* Old method used in unison with method in controller to force the user to select the start and end points for maze
	* solving. In the current version, the user has a choice as to whether or not to set the start and end points.

	@Override
	public void mouseReleased(MouseEvent mouseEvent) {
		if (canSet()) {
			synchronized (view) {
				if (canSet()) {
					this.view.mazePanel.setWaypoint(e.getX(), e.getY());
				}
				view.notify();
			}
		}
	}

*/

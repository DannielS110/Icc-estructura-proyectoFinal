package controller.listeners;

import controller.MazeActionListener;
import controller.MazeController;
import model.SolverType;

import java.awt.event.ActionEvent;

/**
 * An ActionListener (extending MazeActionListener) that is registered and listens for changes in the maze solve method
 * radio, and updates the maze solve selection accordingly on change.
 */
public class MazeSolverSelectionRadioListener extends MazeActionListener {
	public MazeSolverSelectionRadioListener(MazeController mazeController) {
		super(mazeController);
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		String command = e.getActionCommand();
		SolverType solverTypeChoice = SolverType.fromString(command); // Determines which solver type has been selected.

		if (solverTypeChoice == null) {
			return;
		}

		mazeController.setSolverType(solverTypeChoice);
	}
}

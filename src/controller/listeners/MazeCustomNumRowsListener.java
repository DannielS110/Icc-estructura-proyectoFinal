package controller.listeners;

import controller.MazeChangeListener;
import controller.MazeController;

import javax.swing.*;
import javax.swing.event.ChangeEvent;

/**
 * A ChangeListener (extending MazeChangeListener) that listens for changes in the number of rows spinner and sets
 * the number of rows to be used in the next maze generation accordingly.
 */
public class MazeCustomNumRowsListener extends MazeChangeListener {
	public MazeCustomNumRowsListener(MazeController mazeController) {
		super(mazeController);
	}

	@Override
	public void stateChanged(ChangeEvent e) {
		JSpinner rowsSpinner = (JSpinner) e.getSource();
		SpinnerNumberModel rowsSpinnerModel = (SpinnerNumberModel) (rowsSpinner.getModel());
		int numRows = (int) rowsSpinnerModel.getNumber();
		mazeController.setMazeNumRows(numRows);
	}
}

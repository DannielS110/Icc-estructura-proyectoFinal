package controller.listeners;

import controller.MazeChangeListener;
import controller.MazeController;

import javax.swing.*;
import javax.swing.event.ChangeEvent;

/**
 * A ChangeListener (extending MazeChangeListener) that listens for changes from the animation speed slider, and sets
 * the animation speed accordingly.
 */
public class MazeAnimationSpeedSliderListener extends MazeChangeListener {
	public MazeAnimationSpeedSliderListener(MazeController mazeController) {
		super(mazeController);
	}

	@Override
	public void stateChanged(ChangeEvent e) {
		JSlider source = (JSlider) e.getSource();
		if (!source.getValueIsAdjusting()) { // Only set the speed once the user releases the slider.
			int animationSpeed = source.getValue();
			mazeController.setAnimationSpeed(animationSpeed);
		}
	}
}

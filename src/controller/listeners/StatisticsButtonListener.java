package controller.listeners;

import controller.MazeActionListener;
import controller.MazeController;

import javax.swing.*;
import java.awt.event.ActionEvent;

/**
 * An ActionListener (extending MazeActionListener) that is registered and listens for clicks from the 'Statistics'
 * button and shows the algorithm results statistics.
 */
public class StatisticsButtonListener extends MazeActionListener {
	
	public StatisticsButtonListener(MazeController mazeController) {
		super(mazeController);
	}

	@Override
	public void actionPerformed(ActionEvent event) {
		showStatistics();
	}

	/**
	 * Shows the algorithm statistics in a dialog
	 */
	private void showStatistics() {
		String statistics = mazeController.getAlgorithmStatistics();
		
		// Create a scrollable text area for the statistics
		JTextArea textArea = new JTextArea(statistics);
		textArea.setEditable(false);
		textArea.setLineWrap(true);
		textArea.setWrapStyleWord(true);
		textArea.setFont(new java.awt.Font("Monospaced", java.awt.Font.PLAIN, 12));
		
		// Create scroll pane
		JScrollPane scrollPane = new JScrollPane(textArea);
		scrollPane.setPreferredSize(new java.awt.Dimension(600, 400));
		
		// Show dialog
		JOptionPane.showMessageDialog(
			null,
			scrollPane,
			"Estadísticas de Algoritmos",
			JOptionPane.INFORMATION_MESSAGE
		);
	}
} 
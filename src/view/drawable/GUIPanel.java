package view.drawable;

import controller.MazeController;
import view.MinecraftTheme;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import static view.drawable.DrawableHelper.addComponent;

/**
 * A JPanel of the GUI to the right of the maze. This panel stores all the UI components that the user can use
 * to interact with the maze including the maze dimension spinners, the solution method radio, the animation speed
 * slider, and the buttons.
 */
public class GUIPanel extends JPanel implements ActionListener {
	private final MazeController mazeController;

	public GUIPanel(MazeController mazeController) {
		this.mazeController = mazeController;
		initGUIPanel();
	}

	private void initGUIPanel() {
		setLayout(new GridBagLayout());
		
		// Aplicar tema Minecraft al panel
		MinecraftTheme.applyMinecraftPanelStyle(this);

		Insets insets = new Insets(5, 0, 0, 0);

		// Maze Size Inputs
		JPanel customMazeSizeInputs = new MazeSizeInputs(mazeController.getMazeCustomNumRowsListener(),
				mazeController.getMazeCustomNumColsListener());
		addComponent(this, customMazeSizeInputs, 0, 0, 1, 1,
				GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, insets);

		// Generate Maze Button
		initButton(new JButton("Generar"), "generate", 1,
				mazeController.getMazeGeneratorListener(), insets);

		// Solve Method Radio
		JPanel solveMethodRadio = new SolveMethodRadioPanel(mazeController.getMazeSolverSelectionRadioListener(),
				mazeController.getSolverType());
		addComponent(this, solveMethodRadio, 0, 2, 1, 1,
				GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, insets);

		// Solve Maze Button
		initButton(new JButton("Resolver"), "solve", 3,
				mazeController.getMazeSolverListener(), insets);

		// Speed Slider
		JPanel mazeAnimationSpeedSliderPanel = new AnimationSliderPanel(mazeController.getMazeAnimationSpeedSliderListener());
		addComponent(this, mazeAnimationSpeedSliderPanel, 0, 4, 1, 1,
				GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, insets);

		// Reset Maze Button
		initButton(new JButton("Reiniciar"), "reset", 5,
				mazeController.getMazeResetListener(), insets);

		// Statistics Button
		initButton(new JButton("Mostrar Estadísticas"), "statistics", 6,
				mazeController.getStatisticsWindowListener(), insets);

		// Wall Edit Button
		initButton(new JButton("Editar Paredes"), "wallEdit", 7,
				mazeController.getWallEditButtonListener(), insets);

		// Exit Button
		initButton(new JButton("Salir"), "exit", 8, this, insets);
		
		// La tabla de resultados ahora se muestra en una ventana separada
	}

	/**
	 * Initializes a button and adds it to the GUI panel.
	 *
	 * @param button              New button to add to the GUI panel
	 * @param buttonActionCommand The new button's ActionCommand
	 * @param gridy               The button's y coordinate in the GridBayLayout
	 * @param actionListener      The actionListener to attach to the button
	 * @param insets              The button's insets
	 */
	private void initButton(JButton button,
							String buttonActionCommand,
							int gridy,
							ActionListener actionListener,
							Insets insets) {
		button.setActionCommand(buttonActionCommand);
		button.addActionListener(actionListener);
		
		// Aplicar estilo Minecraft al botón
		MinecraftTheme.applyMinecraftButtonStyle(button);
		
		addComponent(this, button, 0, gridy, 1, 1,
				GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, insets);
	}

	/**
	 * Override of the actionPerformed action. The only button that is listened to and handled directly by the GUI panel
	 * is the 'Exit' button, which terminates the application.
	 *
	 * @param mazeGuiButtonClick A button click ActionEvent
	 */
	@Override
	public void actionPerformed(ActionEvent mazeGuiButtonClick) {
		String buttonIntention = mazeGuiButtonClick.getActionCommand();

		if (buttonIntention.equals("exit")) {
			System.exit(0);
		}
	}
}

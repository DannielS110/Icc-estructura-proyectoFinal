package view;

import controller.MazeController;
import model.Maze;
import view.drawable.GUIPanel;
import view.drawable.InstructionsPanel;
import view.drawable.MazePanel;
import view.MinecraftTheme;

import javax.swing.*;
import java.awt.*;

/**
 * The view of the maze (i.e. the view in the MVC design pattern). This is a JPanel that contains both the maze panel
 * (the JPanel containing the maze) and the GUI panel (the panel containing all the UI elements). This class also acts
 * as an intermediary between the controller and between each of the its child panels.
 */
public class MazeView extends JFrame {
	private final MazeController mazeController;
	private final MazePanel mazePanel;
	private final GUIPanel guiPanel;
	private final InstructionsPanel instructionsPanel;

	public MazeView(Maze maze, MazeController mazeController) {
		super("Resolvedor de Laberintos - Proyecto Final");
		this.mazeController = mazeController;
		this.guiPanel = new GUIPanel(mazeController);
		this.mazePanel = new MazePanel(maze, mazeController);
		this.instructionsPanel = new InstructionsPanel();

		// Configurar el listener de edición de paredes
		setupWallEditListener();

		initDisplay();
	}

	private void setupWallEditListener() {
		// Crear el listener de edición de paredes
		controller.listeners.MazeWallEditorListener wallEditorListener = 
			new controller.listeners.MazeWallEditorListener(mazePanel, mazeController);
		
		// Agregar el listener al panel del laberinto
		mazePanel.addMouseListener(wallEditorListener);
		
		// Actualizar el listener del botón con la referencia al panel
		mazeController.getWallEditButtonListener().setMazePanel(mazePanel);
	}

	private void initDisplay() {
		setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
		setResizable(false);
		setLayout(new GridBagLayout());
		
		// Aplicar tema Minecraft a la ventana principal
		getContentPane().setBackground(MinecraftTheme.BACKGROUND_DARK);

		Insets insets = new Insets(5, 5, 5, 5);

		addComponent(mazePanel, 0, 0, 2, 1, GridBagConstraints.CENTER,
				GridBagConstraints.BOTH, insets);
		addComponent(guiPanel, 2, 0, 1, 1, GridBagConstraints.NORTH,
				GridBagConstraints.HORIZONTAL, insets);
		addComponent(instructionsPanel, 0, 1, 2, 1, GridBagConstraints.CENTER,
				GridBagConstraints.BOTH, insets);

		setInstructions();

		setVisible(true);
		pack();
		setLocationRelativeTo(null); // Centrar en la pantalla
	}

	/**
	 * Sets the maze instructions (based on maze state) and adjusts the view size to account for changes in text.
	 */
	public void setInstructions() {
		instructionsPanel.setInstructions(mazeController.getState().getInstruction());
		pack();
	}

	/**
	 * Resizes the maze panel to account for a change in the number of rows and columns.
	 */
	public void resize() {
		mazePanel.resize();
		pack();
		mazePanel.setOffset(mazePanel.getWidth(), mazePanel.getHeight());
		mazePanel.repaint();
	}

	/**
	 * Resets the view after the reset action is triggered.
	 */
	public void resetView() {
		mazePanel.resetWaypointSetterState();
		mazePanel.repaint();
	}

	public void repaintMaze(Maze maze) {
		mazePanel.repaintMaze(maze);
	}

	/**
	 * Add a component to the maze view
	 *
	 * @param component  The component to add to the view
	 * @param gridx      The component's x coordinate in the GridBayLayout
	 * @param gridy      The component's y coordinate in the GridBayLayout
	 * @param gridwidth  The GridBayLayout grid width
	 * @param gridheight The GridBayLayout grid height
	 * @param anchor     The component's anchor in the GridBayLayout
	 * @param fill       The component's fill in the GridBayLayout
	 */
	private void addComponent(Component component, int gridx, int gridy, int gridwidth, int gridheight, int anchor,
							  int fill, Insets insets) {

		GridBagConstraints gbc = new GridBagConstraints(gridx, gridy, gridwidth, gridheight, 1.0, 1.0,
				anchor, fill, insets, 0, 0);
		getContentPane().add(component, gbc);
	}
}

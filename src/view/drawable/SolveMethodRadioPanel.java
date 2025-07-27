package view.drawable;

import controller.listeners.MazeSolverSelectionRadioListener;
import model.SolverType;
import view.MinecraftTheme;

import javax.swing.*;
import java.awt.*;

import static view.drawable.DrawableHelper.addComponent;

/**
 * A JPanel of maze solver options radio
 */
class SolveMethodRadioPanel extends JPanel {
	public SolveMethodRadioPanel(MazeSolverSelectionRadioListener solverSelectionRadioListener,
								 SolverType mazeSolverType) {
		setLayout(new GridBagLayout());
		
		// Aplicar tema Minecraft al panel
		MinecraftTheme.applyMinecraftPanelStyle(this);
		
		// Crear borde con título estilo Minecraft
		javax.swing.border.TitledBorder titledBorder = BorderFactory.createTitledBorder("Método de Resolución");
		titledBorder.setTitleFont(MinecraftTheme.getMinecraftFont(14f));
		titledBorder.setTitleColor(MinecraftTheme.TEXT_GOLD);
		setBorder(titledBorder);

		Box solveMethodRadioBox = Box.createVerticalBox();
		ButtonGroup solveMethodRadioButtonGroup = new ButtonGroup();

        /*
        	Iterates through all the solver types and creates a corresponding radio option for the user to pick
         */
		for (SolverType solverType : SolverType.values()) {
			JRadioButton solverTypeOption = new JRadioButton(solverType.getName());
			
			// Aplicar estilo Minecraft al radio button
			solverTypeOption.setFont(MinecraftTheme.getMinecraftFont(12f));
			solverTypeOption.setForeground(Color.BLACK);
			solverTypeOption.setBackground(Color.WHITE);
			solverTypeOption.setOpaque(false);
			
			solverTypeOption.addActionListener(solverSelectionRadioListener);

			if (solverType == mazeSolverType) { // Defaults to the initial maze SolverType set by the controller
				solverTypeOption.setSelected(true);
			}

			solveMethodRadioBox.add(solverTypeOption);
			solveMethodRadioButtonGroup.add(solverTypeOption);
		}

		// Crear un scroll pane para asegurar que todos los botones sean visibles
		JScrollPane scrollPane = new JScrollPane(solveMethodRadioBox);
		scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
		scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
		scrollPane.setPreferredSize(new Dimension(170, 140));

		Insets insets = new Insets(2, 2, 2, 2);
		addComponent(this, scrollPane, 0, 0, 1, 1,
				GridBagConstraints.CENTER, GridBagConstraints.BOTH, insets);

		Dimension guiDimension = new Dimension(180, 180);
		setMinimumSize(guiDimension);
		setPreferredSize(guiDimension);
	}
}









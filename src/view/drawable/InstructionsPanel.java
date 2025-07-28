package view.drawable;

import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.border.CompoundBorder;
import javax.swing.border.EmptyBorder;
import javax.swing.border.EtchedBorder;
import java.awt.*;
import view.MinecraftTheme;

/**
 * A JPanel of the instructions for the maze at each stage to give the user guidance on what to do and what features
 * are available to them.
 */
public class InstructionsPanel extends JPanel {
	private final JTextArea instructions;

	public InstructionsPanel() {
		setLayout(new BorderLayout());
		Border margin = new EmptyBorder(5, 5, 5, 5);
		setBorder(new CompoundBorder(new EtchedBorder(), margin));

		// Aplicar tema Minecraft al panel
		MinecraftTheme.applyMinecraftPanelStyle(this);

		this.instructions = new JTextArea();
		initInstructionTextArea();
		add(instructions);

		setVisible(true);
	}

	private void initInstructionTextArea() {
		instructions.setBackground(MinecraftTheme.PANEL_BROWN);
		instructions.setForeground(MinecraftTheme.TEXT_WHITE);
		instructions.setFont(MinecraftTheme.getMinecraftFont(12f));
		instructions.setEditable(false);
		instructions.setCursor(null);
		instructions.setOpaque(false);
		instructions.setFocusable(false);
		instructions.setWrapStyleWord(true);
		instructions.setLineWrap(true);
	}

	public void setInstructions(String instruction) {
		instructions.setText(instruction);
	}
}

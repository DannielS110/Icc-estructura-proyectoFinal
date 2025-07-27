package view.drawable;

import controller.listeners.MazeAnimationSpeedSliderListener;
import model.MazeAnimationConstants;
import view.MinecraftTheme;

import javax.swing.*;
import java.awt.*;

import static view.drawable.DrawableHelper.addComponent;

/**
 * A JPanel of animation slider
 */
class AnimationSliderPanel extends JPanel {
	public AnimationSliderPanel(MazeAnimationSpeedSliderListener mazeAnimationSpeedSliderListener) {
		setLayout(new GridBagLayout());
		
		// Aplicar tema Minecraft al panel
		MinecraftTheme.applyMinecraftPanelStyle(this);

		JSlider mazeAnimationSpeedSlider = new JSlider(JSlider.HORIZONTAL, MazeAnimationConstants.MINIMUM_ANIMATION_SLEEP,
				MazeAnimationConstants.MAXIMUM_ANIMATION_SLEEP, MazeAnimationConstants.DEFAULT_ANIMATION_SLEEP);
		mazeAnimationSpeedSlider.setInverted(true);
		mazeAnimationSpeedSlider.addChangeListener(mazeAnimationSpeedSliderListener);
		
		// Aplicar estilo Minecraft al slider
		mazeAnimationSpeedSlider.setBackground(MinecraftTheme.PANEL_BROWN);
		mazeAnimationSpeedSlider.setForeground(MinecraftTheme.EMERALD_GREEN);

		JLabel mazeAnimationSpeedSliderLabel = new JLabel("Velocidad de Animación");
		MinecraftTheme.applyMinecraftLabelStyle(mazeAnimationSpeedSliderLabel);

		Insets insets = new Insets(0, 0, 0, 0);

		addComponent(this, mazeAnimationSpeedSliderLabel, 0, 0, 1, 1,
				GridBagConstraints.CENTER, GridBagConstraints.BOTH, insets);
		mazeAnimationSpeedSliderLabel.setLabelFor(mazeAnimationSpeedSlider);
		addComponent(this, mazeAnimationSpeedSlider, 0, 1, 1, 1,
				GridBagConstraints.CENTER, GridBagConstraints.BOTH, insets);

	}
}

package view.drawable;

import java.awt.*;

/**
 * A helper class available to all classes in the view.
 */
class DrawableHelper {
	/**
	 * Add a component to a container. This function assumes that the container has a GridBayLayout layout.
	 *
	 * @param container  The container
	 * @param component  The component to add to the container
	 * @param gridx      The component's x coordinate in the GridBayLayout
	 * @param gridy      The component's y coordinate in the GridBayLayout
	 * @param gridwidth  The GridBayLayout grid width
	 * @param gridheight The GridBayLayout grid height
	 * @param anchor     The component's anchor in the GridBayLayout
	 * @param fill       The component's fill in the GridBayLayout
	 * @param insets     The components's insets
	 */
	public static void addComponent(Container container, Component component, int gridx, int gridy, int gridwidth,
									int gridheight, int anchor, int fill, Insets insets) {
		GridBagConstraints gbc = new GridBagConstraints(gridx, gridy, gridwidth, gridheight,
				1.0, 1.0, anchor, fill, insets, 0, 0);
		container.add(component, gbc);
	}
}

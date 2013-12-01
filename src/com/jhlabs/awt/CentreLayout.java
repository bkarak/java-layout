package com.jhlabs.awt;

import java.awt.*;

public class CentreLayout implements LayoutManager, java.io.Serializable {

    /**
     * Adds the specified component to the layout. Not used by this class.
     * @param name the name of the component
     * @param comp the component to be added
     */
    public void addLayoutComponent(String name, Component comp) {
    }

    /**
     * Removes the specified component from the layout. Not used by
     * this class.
     * @param comp the component to remove
     * @see       java.awt.Container#removeAll
     */
    public void removeLayoutComponent(Component comp) {
    }

    /**
     * Returns the preferred dimensions for this layout given the components
     * in the specified target container.
     * @param target the component which needs to be laid out
     * @return    the preferred dimensions to lay out the
     *                    subcomponents of the specified container.
     * @see Container
     * @see #minimumLayoutSize
     * @see       java.awt.Container#getPreferredSize
     */
    public Dimension preferredLayoutSize(Container target) {
		return target.getPreferredSize();
    }

    /**
     * Returns the minimum dimensions needed to layout the components
     * contained in the specified target container.
     * @param target the component which needs to be laid out
     * @return    the minimum dimensions to lay out the
     *                    subcomponents of the specified container.
     * @see #preferredLayoutSize
     * @see       java.awt.Container
     * @see       java.awt.Container#doLayout
     */
    public Dimension minimumLayoutSize(Container target) {
		return target.getMinimumSize();
    }

    /**
     * Lays out the container. This method lets each component take
     * its preferred size by reshaping the components in the
     * target container in order to satisfy the constraints of
     * this <code>FlowLayout</code> object.
     * @param target the specified component being laid out.
     * @see Container
     * @see       java.awt.Container#doLayout
     */
    public void layoutContainer(Container target) {
		synchronized (target.getTreeLock()) {
			Insets insets = target.getInsets();
			Dimension size = target.getSize();
			int w = size.width - (insets.left + insets.right);
			int h = size.height - (insets.top + insets.bottom);
			int count = target.getComponentCount();

			for (int i = 0 ; i < count ; i++) {
			    Component m = target.getComponent(i);
			    if (m.isVisible()) {
					Dimension d = m.getPreferredSize();
					m.reshape((w-d.width)/2, (h-d.height)/2, d.width, d.height);
				}
			}
		}
    }

}

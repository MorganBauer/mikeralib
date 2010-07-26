package mikera.ui;

import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.image.BufferedImage;

import javax.swing.Icon;
import javax.swing.JComponent;
import javax.swing.JPanel;

public class JIcon extends JComponent {
	private Icon icon=null;
	
	public JIcon() {
		
	}
	
	public JIcon (BufferedImage b) {
		this (new BufferedImageIcon(b));
	}
	
	public JIcon(Icon icon) {
		this.setIcon(icon);
	}
	
	
	@Override public void paintComponent(Graphics g) {
		int x=(getWidth()-icon.getIconWidth())/2;
		int y=(getHeight()-icon.getIconHeight())/2;
		getIcon().paintIcon(this, g, x, y);	
	}

	/**
	 * @param icon the icon to set
	 */
	private void setIcon(Icon icon) {
		this.icon = icon;
		setPreferredSize(new Dimension(icon.getIconWidth(),icon.getIconHeight()));
	}

	/**
	 * @return the icon
	 */
	private Icon getIcon() {
		return icon;
	}
}

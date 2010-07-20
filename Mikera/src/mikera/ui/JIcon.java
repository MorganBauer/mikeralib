package mikera.ui;

import java.awt.Dimension;
import java.awt.Graphics;

import javax.swing.Icon;
import javax.swing.JComponent;
import javax.swing.JPanel;

public class JIcon extends JComponent {
	private Icon icon=null;
	
	public JIcon() {
		
	}
	
	public JIcon(Icon icon) {
		this.setIcon(icon);
	}
	
	
	@Override public void paintComponent(Graphics g) {
		getIcon().paintIcon(this, g, 0, 0);	
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

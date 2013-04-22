import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Graphics;

/**
 * 
 * @author Jian Rossi
 *
 */
public class Background {
	
	protected Component component;
	protected Dimension size;

	public Background(Component comp) {
		component = comp;
		size = comp.getSize();
	}

	public Dimension getSize() {
		return size;
	}

	public void draw(Graphics g) {
		// Fill with component color
		g.setColor(component.getBackground()); 
		g.fillRect(0, 0, size.width, size.height); 
		g.setColor(Color.black);
	}
	
} // of Background
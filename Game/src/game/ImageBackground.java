package game;
import java.awt.Component;
import java.awt.Graphics;
import java.awt.Image;

public class ImageBackground extends Background {
	
	protected Image image;

	public ImageBackground(Component comp, Image img) {
		super(comp);
		image = img;
	}

	public Image getImage() {
		return image;
	}

	public void setImage(Image img) {
		image = img;
	}

	public void draw(Graphics g) {
		// Draw background image
		g.drawImage(image, 0, 60, component);
	}
}
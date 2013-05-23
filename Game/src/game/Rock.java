package game;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Rectangle;
import java.net.URL;
import javax.swing.ImageIcon;

public class Rock extends Actor {

    private Image image;
    Rectangle objectRect;

    public Rock(int x, int y) {
        super(x, y);

        URL loc = this.getClass().getResource("/Resources/rock_image.png");
        ImageIcon iia = new ImageIcon(loc);
        image = iia.getImage();
        this.setImage(image);
        
        this.objectRect = getRect(x, y, 32,32);

        this.setRect(objectRect);

    }
    
    public Rectangle getRect(){
		return objectRect;
    }
    
    public void draw(Graphics g) {
    	g.drawImage(image, objectRect.x, objectRect.y, null);
    }
}
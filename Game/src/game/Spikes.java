package game;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Rectangle;
import java.net.URL;
import javax.swing.ImageIcon;

public class Spikes extends Actor {

    private Image image;
    Rectangle objectRect;

    public Spikes(int x, int y) {
        super(x, y);

        URL loc = this.getClass().getResource("/Resources/firstdoor.png");
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
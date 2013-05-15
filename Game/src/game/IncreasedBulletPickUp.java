
package game;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Rectangle;
import java.net.URL;
import javax.swing.ImageIcon;

public class IncreasedBulletPickUp extends Actor {

	
	Image item;
	protected Rectangle itemRect;
    public IncreasedBulletPickUp(int x, int y) {
        super(x, y);
        URL loc = this.getClass().getResource("/Resources/wall.png");
        ImageIcon iia = new ImageIcon(loc);
        item = iia.getImage();
        this.itemRect = getRect(x, y, 32,32);
        this.setImage(item);
    }
	
    public void draw(Graphics g) {
    	g.drawImage(item, itemRect.x, itemRect.y, null);
    	
    }
}

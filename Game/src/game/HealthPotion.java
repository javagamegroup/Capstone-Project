
package game;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Rectangle;
import java.net.URL;
import javax.swing.ImageIcon;

public class HealthPotion extends Actor {

	
	Image item;
	public Rectangle itemRect;
    public HealthPotion(int x, int y) {
        super(x, y);
        URL loc = this.getClass().getResource("/Resources/potion_image.png");
        ImageIcon iia = new ImageIcon(loc);
        item = iia.getImage();
        this.itemRect = getRect(x, y, 12,20);
        this.setImage(item);
    }
	
    public void draw(Graphics g) {
    	g.drawImage(item, itemRect.x, itemRect.y, null);
    }
    public Rectangle getRect(){
		return itemRect;
    }
}

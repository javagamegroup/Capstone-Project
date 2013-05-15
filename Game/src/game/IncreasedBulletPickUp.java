
package game;
import java.awt.Image;
import java.net.URL;
import javax.swing.ImageIcon;

public class IncreasedBulletPickUp extends Actor {

    public IncreasedBulletPickUp(int x, int y) {
        super(x, y);
        URL loc = this.getClass().getResource("/Resources/wall.png");
        ImageIcon iia = new ImageIcon(loc);
        Image image = iia.getImage();
        this.setImage(image);
    }

	public void move(int x, int y) {
        int nx = this.x() + x;
        int ny = this.y() + y;
    }
}

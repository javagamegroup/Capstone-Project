
import java.awt.Image;
import java.awt.Rectangle;
import java.net.URL;
import javax.swing.ImageIcon;

public class Wall extends Actor {

    private Image image;
    Rectangle objectRect;

    public Wall(int x, int y) {
        super(x, y);

        URL loc = this.getClass().getResource("Resources/wall.png");
        ImageIcon iia = new ImageIcon(loc);
        image = iia.getImage();
        this.setImage(image);
        
        this.objectRect = getRect(x, y, 32,32);

        this.setRect(objectRect);

    }
}
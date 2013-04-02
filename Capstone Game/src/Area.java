
import java.awt.Image;
import java.awt.Rectangle;
import java.net.URL;
import javax.swing.ImageIcon;

public class Area extends Actor {

    Rectangle areaRect;

	public Area(int x, int y) {
        super(x, y);

        URL loc = this.getClass().getResource("Resources/area.png");
        ImageIcon iia = new ImageIcon(loc);
        Image image = iia.getImage();
        this.setImage(image);
        this.areaRect = getRect(x, y, 20,20);

        this.setRect(areaRect);
    }
}
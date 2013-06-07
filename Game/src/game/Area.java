package game;
import java.awt.Image;
import java.awt.Rectangle;
import java.net.URL;
import javax.swing.ImageIcon;

public class Area extends Actor {

    Rectangle areaRect;
    Image imageOpenDoor;
    Image imageCloseDoor;

	public Area(int x, int y) {
        super(x, y);

        URL loc = this.getClass().getResource("/Resources/open_door.png");
        ImageIcon iia = new ImageIcon(loc);
        imageOpenDoor = iia.getImage();
        this.setImage(imageOpenDoor);
        loc = this.getClass().getResource("/Resources/lock_door.png");
        iia = new ImageIcon(loc);
        imageCloseDoor = iia.getImage();
        this.setImage(imageCloseDoor);
        this.areaRect = getRect(x, y, 32,32);

        this.setRect(areaRect);
    }
	
	public void setImage(int num){
		if(num==0){
			this.setImage(imageOpenDoor);
		}
		else
			this.setImage(imageCloseDoor);
	}
}
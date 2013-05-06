import java.awt.Component;
import java.awt.Image;
import java.awt.MediaTracker;
import java.awt.Point;
import java.util.BitSet;

import javax.swing.ImageIcon;


public class TurtleDuckCorpse extends Sprite{
	
	protected static Image[] image = new Image[4];
	
	public TurtleDuckCorpse(Component comp, Point pos) {
		super(comp, image, 0, 1, 20, pos, new Point(0, 0), 30, Sprite.BA_DIE);
	}

	public static void initResources(MediaTracker tracker, int id)
	{
		java.net.URL imgURL;
		ImageIcon turtleDuckCorpse;
		String path;
		for(int i = 0; i < 6; i++) {
			path = "Resources/turtleduckcorpse" + i + ".png"; 
			imgURL = BabyTurtleDuck.class.getResource(path);
			turtleDuckCorpse = new ImageIcon(imgURL);
			image[i] = turtleDuckCorpse.getImage();
			tracker.addImage(image[i], id);
		}
	}
	
	public BitSet update() {
		
		BitSet action = new BitSet();
		
		// Die? 
		if (frame >= 3) {
			action.set(Sprite.SA_KILL);
			return action;
		}
		
		// Increment the frame
		incFrame();
		
		return action;
	}
}

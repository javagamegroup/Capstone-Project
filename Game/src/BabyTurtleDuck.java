import java.awt.Component;
import java.awt.MediaTracker;
import java.awt.Point;
import java.util.BitSet;

import javax.swing.ImageIcon;


public class BabyTurtleDuck extends Sprite{
	
	
	public BabyTurtleDuck(Component comp, Point pos) {
		super(comp, image, 0, 1, 20, pos, new Point(0,0), 40, Sprite.BA_DIE);
	}
	
	/**
	 * This method could include anything from images to sound to music. 
	 * Important for it to be static. Means that initResources applies to all instances
	 * of the AlienDeer class, which results in all AlienDeer objects referencing the same images.
	 * Makes loading of resources smoother because we can load the resources at the beginning
	 * of the game before we even create any AlienDeer objects. 
	 * 
	 */
	public static void initResources(MediaTracker tracker, int id)
	{
		java.net.URL imgURL;
		ImageIcon babyturtleduck;
		String path;
		for(int i = 0; i < 6; i++) {
			path = "Resources/babyturtleduck" + i + ".png"; 
			imgURL = BabyTurtleDuck.class.getResource(path);
			babyturtleduck = new ImageIcon(imgURL);
			image[i] = babyturtleduck.getImage();
			tracker.addImage(image[i], id);
		}
	}
	
	public BitSet update() {
		BitSet action = new BitSet();
		
		// Die?
		if (frame >= 5) {
			action.set(Sprite.SA_KILL);
			action.set(Sprite.SA_ADDSPRITE);
			action.set(TurtleDuck.SA_ADDTURTLEDUCK);
			return action;
		}
		
		// Increment the frame
		incFrame();
		
		return action;
	}
	
	protected Sprite addSprite(BitSet action) {
		// Add turtle duck corpse
		if (action.get(TurtleDuck.SA_ADDTURTLEDUCK))
			return new TurtleDuck(component, new Point(position.x, position.y));
		return null;
	}
	
}

package collectionOfSprites;
import game.DirectionalSprite;
import game.Sprite;

import java.awt.Component;
import java.awt.Image;
import java.awt.MediaTracker;
import java.awt.Point;
import java.util.BitSet;

import javax.swing.ImageIcon;


public class BabyTurtleDuck extends DirectionalSprite{
	
	public static Image[][] image;
	private static int direction = 0;
	
	public BabyTurtleDuck(Component comp, Point pos) {
		super(comp, image, 0, 1, 20, pos, new Point(0,0), 40, Sprite.BA_DIE, direction);
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
		image = new Image[4][2];
		java.net.URL imgURL;
		ImageIcon babyturtleduck;
		String path;
		for(int i = 0; i < 4; i++) 
		{
			for(int j = 0; j < 2; j++)
			{
				path = "/Resources/babyturtleduck" + i + j + ".png"; 
				imgURL = BabyTurtleDuck.class.getResource(path);
				babyturtleduck = new ImageIcon(imgURL);
				image[i][j] = babyturtleduck.getImage();
				tracker.addImage(image[i][j], id);
			}
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

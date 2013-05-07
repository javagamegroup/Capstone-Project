package collectionOfSprites;
import game.DirectionalSprite;
import game.Sprite;

import java.awt.Component;
import java.awt.Image;
import java.awt.MediaTracker;
import java.awt.Point;
import java.util.BitSet;
import java.util.Random;

import javax.swing.ImageIcon;



public class TurtleDuck extends DirectionalSprite {

	public static final int SA_ADDTURTLEDUCK = 3,
							SA_ADDBABYTURTLEDUCK = 4,
							SA_ADDTURTLEDUCKCORPSE = 5;
	public static Image[][] image;
	protected static Random rand = new Random(System.currentTimeMillis());
	private static int direction = 0;
	
	public TurtleDuck(Component comp, Point pos)
	{
		super(comp, image, 0, 1, 2, pos, new Point(1, 1), 50, Sprite.BA_STOP, direction);
	}
	
	/**
	 * Call this method from GamePanel's initialize method. 
	 * 
	 * image[0][] represents sprite facing right
	 * image[1][] represents sprite facing down
	 * image[2][] represents sprite facing left
	 * image[3][] represents sprite facing up
	 * 
	 * the second dimension of the array holds images, that when drawn in succession,
	 * depict the sprite walking in the direction that the first dimension represents.
	 * 
	 * @param tracker - MediaTracker object
	 * @param id - position in tracker
	 */
	public static void initResources(MediaTracker tracker, int id)
	{
		image = new Image[4][2];
		java.net.URL imgURL;
		ImageIcon turtleduck;
		String path;
		for(int i = 0; i < 4; i++) {
			for(int j = 0; j < 2; j++) {
				path = "/Resources/turtleduck" + i + j + ".png"; 
				imgURL = TurtleDuck.class.getResource(path);
				turtleduck = new ImageIcon(imgURL);
				image[i][j] = turtleduck.getImage();
				tracker.addImage(image[i][j], id);
			}
		}
	}
	
	
	/**
	 * The update method first handles giving the turtleduck its ability to roam by randomly
	 * altering the direction. The superclass update method is then called so that all the default 
	 * handling can take place. The update method then randomly decides whether a new babyturtleduck
	 * object should be created. If so, the SA_ADDSPRITE and SA_ADDBABYTURTLEDUCK flags are set. 
	 * Similarly, update randomly decides whether a TurtleDuckCorpse object should be created. 
	 * If so, the SA_KILL, SA_ADDSPRITE, and SA_ADDTURTLEDUCKCORPSE flags are set. 
	 * The SA_KILL flag takes care of killing the TurtleDuck object itself, while the other two 
	 * cause the new TurtleDuckCorpse object to be created.
	 */
	public BitSet update() {
		// Randomly change direction
		if ((rand.nextInt() % 10) == 0) {
			velocity.x = velocity.y = 1;
			setDirection(direction + rand.nextInt() % 2);
		}
		
		// Call parent's update()
		BitSet action = super.update();
		
		// Give birth?
		if (rand.nextInt() % 250 == 0) {
			action.set(Sprite.SA_ADDSPRITE);
			action.set(TurtleDuck.SA_ADDBABYTURTLEDUCK);
		}
		
		// Die?
		if (rand.nextInt() % 250 == 0) {
			action.set(Sprite.SA_KILL);
			action.set(Sprite.SA_ADDSPRITE);
			action.set(TurtleDuck.SA_ADDTURTLEDUCKCORPSE);
		}
		
		incFrame();
		
		return action;
	}
	
	/**
	 * Handles adding new sprites upon actions.
	 */
	protected Sprite addSprite(BitSet action) {
		// Add baby turtle duck
		if (action.get(TurtleDuck.SA_ADDBABYTURTLEDUCK))
			return new BabyTurtleDuck(component, new Point(position.x, position.y));
		
		// Add Turtle Duck Corpse
		else if (action.get(TurtleDuck.SA_ADDTURTLEDUCKCORPSE))
			return new TurtleDuckCorpse(component, new Point(position.x, position.y));
		
		return null;
	}
	
	
	
}
	

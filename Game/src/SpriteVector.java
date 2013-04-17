import java.awt.Dimension;
import java.awt.Point;
import java.awt.Rectangle;
import java.util.BitSet;
import java.util.Random;
import java.util.Vector;


public class SpriteVector extends Vector{

	protected Background background;
	
	public SpriteVector(Background back) {
		super(50, 10);  						// Calls Vector parent class constructor and sets the default 
												// storage capacity (50) and amount to increment the storage 
												// capacity(10) if the vector needs to grow.
		background = back;
	}
	
	/**
	 * Game may use this to get the current background
	 * @return background object
	 */
	public Background getBackground() {
		  return background;
	}
	
	/**
	 * Game will use this to set the background for the current scene
	 * @param background object
	 */
	public void setBackground(Background back) {
		  background = back;
	}
	
	/**
	 *  Used by the SpriteVector class to help position new sprites
	 *  Used to find an empty physical position in which to place a new sprite 
	 *  in the sprite list. This doesn't mean the position of the sprite in the list;
	 *  it means its physical position on the screen. Very useful to randomly place 
	 *  multiple sprites on the screen. Eliminates the possibility of placing new 
	 *  sprites on top of existing sprites. 
	 * 
	 * @param sSize
	 * @return point of empty position to place sprite 
	 */
	public Point getEmptyPosition(Dimension sSize) {
		Rectangle pos = new Rectangle(0, 0, sSize.width, sSize.height); 
		Random    rand = new Random(System.currentTimeMillis()); 
		boolean   empty = false;
		int       numTries = 0;
		
		// Look for an empty position
		while (!empty && numTries++ < 50) {
			// Get a random position
		    pos.x = Math.abs(rand.nextInt() % background.getSize().width); 
		    pos.y = Math.abs(rand.nextInt() % background.getSize().height); 

		    // Iterate through sprites, checking if position is empty
		    boolean collision = false;
		    for (int i = 0; i < size(); i++) { 
		    	Rectangle testPos = ((Sprite)elementAt(i)).getPosition(); 
		    	if (pos.intersects(testPos)) {
		    		collision = true; 
		    		break;
		    	}
		    }
		    empty = !collision;
		}
	
		return new Point(pos.x, pos.y);
	} // of getEmptyPosition
	
	/**
	 * isPointInside goes through entire sprite list checking each sprite.
	 * the sprite list is searched in reverse, meaning that the last sprite is checked before the first.
	 * The sprites are searched in this order for a very important reason: Z-order. The sprites are 
	 * stored in the sprite list sorted in ascending Z-order, which specifies their depth on the screen.
	 * Therefore, sprites near the beginning of the list are sometimes concealed by sprites near the end of the list.
	 * If you want to check for a point lying within a sprite, it only makes sense to check the topmost sprites first-that is, 
	 * the sprites with larger Z-order values.
	 * 
	 * @param pt
	 * @return If the point passed in the parameter pt lies in a sprite, isPointInside returns the sprite
	 */
	Sprite isPointInside(Point pt) {
		// Iterate backward through the sprites, testing each
		for (int i = (size() - 1); i >= 0; i--) {
			Sprite s = (Sprite)elementAt(i);
			if (s.isPointInside(pt))
				return s;
			}
		
		return null;
	}
	
	/**
	 *  Iterate through sprites, calling Sprite's update method on each one.
	 */
	public void update() {
		
		Sprite s, sHit;
		Rectangle lastPos;
		for (int i = 0; i < size(); ) {
			// Update the sprite
		    s = (Sprite)elementAt(i);
		    lastPos = new Rectangle(s.getPosition().x, s.getPosition().y, s.getPosition().width, s.getPosition().height); 
		    BitSet action = s.update();

		    // Check for the SA_ADDSPRITE action
		    if (action.get(Sprite.SA_ADDSPRITE)) { 
		    	// Add the sprite
		    	Sprite sToAdd = s.addSprite(action); 
		    	if (sToAdd != null) {
		    		int iAdd = add(sToAdd); 
		    		if (iAdd >= 0 && iAdd <= i)
		    			i++; 
		    	}
		    }

		    // Check for the SA_RESTOREPOS action 
		    if (action.get(Sprite.SA_RESTOREPOS)) 
		    	s.setPosition(lastPos);

		    // Check for the SA_KILL action
		    if (action.get(Sprite.SA_KILL)) {
		    	removeElementAt(i);
		    	continue;
		    }
		    	
		    // Test for collision
		    int iHit = testCollision(s);
		    if (iHit >= 0)
		    	if (collision(i, iHit))
		    		s.setPosition(lastPos); 
		    i++;
		}
	}
	
	
	
}

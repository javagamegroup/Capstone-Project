import java.awt.Dimension;
import java.awt.Graphics;
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
	 *  It then checks for the various sprite action flags returned by the call to update.
	 *  If the SA_ADDSPRITE flag is set, the addSprite method is called on the sprite and 
	 *  the returned sprite is added to the list. If the SA_RESTOREPOS flag is set,
	 * 	the sprite position is set to the position of the sprite prior to being updated.
	 * 	If the SA_KILL flag is set, the sprite is removed from the sprite list. Finally, 
	 * 	testCollision is called to see whether a collision has occurred between sprites.
	 * 	If a collision has occurred, the old position of the collided sprite is restored 
	 * 	and the collision method is called.
	 *  
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
	
	/**
	 * The collision method is responsible for handling any actions that result from a 
	 * collision between sprites. The action in this case is to simply swap the velocities 
	 * of the collided Sprite objects, which results in a bouncing effect. This method is 
	 * where you provide specific collision actions in derived sprites. For example, 
	 * in a space game, you might want alien sprites to explode upon collision with a meteor sprite.
	 * 
	 * @param i
	 * @param iHit
	 * @return whether it was a successful collision
	 */
	protected boolean collision(int i, int iHit) {
	  
		// Swap velocities (bounce) example
		Sprite s = (Sprite)elementAt(i);
		Sprite sHit = (Sprite)elementAt(iHit);
		Point swap = s.getVelocity();
		s.setVelocity(sHit.getVelocity());
		sHit.setVelocity(swap);
		
		return true;
	}
	
	/**
	 * Used to test for collisions between a sprite and the rest of the sprites in the sprite list.
	 * The sprite to be tested is passed in the test parameter. The sprites are then iterated through and 
	 * the testCollision method in Sprite is called for each.
	 * Will not check for collision with itself. 
	 * 
	 * @param test
	 * @return If a collision is detected, the Sprite object that has been hit is returned from testCollision.
	 */
	protected int testCollision(Sprite test) {
		// Check for collision with other sprites
		Sprite  s;
		for (int i = 0; i < size(); i++)
		{
			s = (Sprite)elementAt(i);
		    if (s == test)  // don't check itself
		      continue;
		    if (test.testCollision(s))
		      return i;
		}
			return -1;
	}
	
	/**
	 * The draw method handles drawing the background, as well as drawing all the sprites.
	 * The background is drawn with a simple call to the draw method of the Background object. 
	 * The sprites are then drawn by iterating through the sprite list and calling the draw method for each.
	 * 
	 * @param g
	 */
	public void draw(Graphics g) {
		// Draw the background
		background.draw(g);

		// Iterate through sprites, drawing each
		for (int i = 0; i < size(); i++)
			((Sprite)elementAt(i)).draw(g);
	}
	
	/**
	 *  Handles adding new sprites to the sprite list.
	 *  Sprite list must always be sorted according to Z-order.
	 *  Sorting the sprite list by ascending Z-order and then drawing them 
	 *  in that order is an effective way to provide the illusion of depth. 
	 *  The add method uses a binary search to find the right spot to add new 
	 *  sprites so that the sprite list remains sorted by Z-order.
	 *  
	 * @param s
	 * @return
	 */
	public int add(Sprite s) {
		// Use a binary search to find the right location to insert the
		// new sprite (based on z-order)
		int   l = 0, r = size(), i = 0;
		int   z = s.getZOrder(), zTest = z + 1; 
		
		while (r > l) {
			i = (l + r) / 2;
			zTest = ((Sprite)elementAt(i)).getZOrder(); 
			if (z < zTest)
				r = i;
			else
				l = i + 1;
			if (z == zTest)
				break;
		}
		if (z >= zTest)
			i++;
		
		insertElementAt(s, i);
	
		return i;
	}
}

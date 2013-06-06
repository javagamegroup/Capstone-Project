package game;
import java.awt.Component;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Point;
import java.awt.Rectangle;
import java.util.BitSet;


public class Sprite {

	public static final int SA_KILL = 0,
				            SA_RESTOREPOS = 1,
				            SA_ADDSPRITE = 2;
	public static final int BA_STOP = 0,
				            BA_WRAP = 1,
				            BA_BOUNCE = 2,
				            BA_DIE = 3;
	protected Component     component;
	protected static Image[]       image; 
	protected int           frame, 
							frameInc,              // used to provide a means to change the way that the animation frames are updated. set to -1 for reverse order.
				            frameDelay, 		   // frameDelay and frameTrigger are used to provide a means of varying
				            frameTrigger;          // the speed of the frame animation.
	protected Rectangle     position,
	            			collision; 
	protected int           zOrder; 
	protected Point         velocity; 
	protected Rectangle     bounds;
	protected int           boundsAction; 
	protected boolean       hidden = false;        // determines whether or not the sprite is hidden.
	
	
	/**
	 * Use this constructor for sprites without frame animations. 
	 * 
	 * @param comp
	 * @param img sprite image
	 * @param pos position
	 * @param vel velocity
	 * @param z zOrder (layer)
	 * @param ba bounded action
	 */
	public Sprite(Component comp, Image img, Point pos, Point vel, int z, int ba) {
		  component = comp;
		  image = new Image[1];
		  image[0] = img;
		  setPosition(new Rectangle(pos.x, pos.y, img.getWidth(comp), 
		    img.getHeight(comp)));
		  setVelocity(vel);
		  frame = 0;
		  frameInc = 0;
		  frameDelay = frameTrigger = 0;
		  zOrder = z;
		  bounds = new Rectangle(0, 0, comp.getSize().width, comp.getSize().height); 
		  boundsAction = ba;
	}
	
	/**
	 * Use this contructor for sprites with 2D array of images and frame animation
	 * 
	 * @param comp
	 * @param img  -   2D array of images
	 * @param f    -   frame number to start on
	 * @param fi   -   frame incrementing
	 * @param fd   -   frame delaying
	 * @param pos
	 * @param vel
	 * @param z
	 * @param ba
	 */
	public Sprite(Component comp, Image[] img, int f, int fi, int fd, Point pos, Point vel, int z, int ba) {
		  component = comp;
		  image = img;
		  setPosition(new Rectangle(pos.x, pos.y, img[f].getWidth(comp), 
		    img[f].getHeight(comp)));
		  setVelocity(vel);
		  frame = f;
		  frameInc = fi;
		  frameDelay = frameTrigger = fd;
		  zOrder = z;
		  bounds = new Rectangle(0, 0, comp.getSize().width, comp.getSize().height); 
		  boundsAction = ba;
	}
	
	public void setImage(Image[] img)
	{
		this.image = img;
	}
	public Point getVelocity() {
		return velocity;
	}

	public void setVelocity(Point vel){ 
		velocity = vel;
	}
	
	public int getZOrder() {
		return zOrder;
	}
	
	public boolean getHidden() {
		return hidden;
	}
	
	public void setHidden(boolean hid) {
		hidden = hid;
	}
	
	public Rectangle getBounds() {
		return bounds;
	}
	
	/**
	 *  Used to increment the current animation frame. 
	 *  First checks the frameDelay and frameTrigger member variables to see whether the 
	 *  frame should actually be incremented. 
	 *  This check is what allows us to vary the frame animation speed for a sprite, which 
	 *  is done by changing the value of frameDelay. 
	 *  Larger values for frameDelay result in a slower animation speed.
	 *  The current frame is incremented by adding frameInc to frame. 
	 *  frame is then checked to make sure that its value is within the bounds of the image array,
	 *  because it is used later to index into the array when the frame image is drawn.
	 * 
	 */
	protected void incFrame() {
		if ((frameDelay > 0) && (--frameTrigger <= 0)) {
			// Reset the frame trigger
			frameTrigger = frameDelay;
	
			// Increment the frame
			frame += frameInc;
			if (frame >= image.length)
				frame = 0;
			else if (frame < 0)
				frame = image.length - 1; 
		}
	}
	
	void setPosition(Rectangle pos) {
		position = pos;
		setCollision();
	}

	public void setPosition(Point pos) {
		position.setLocation(pos.x, pos.y);
		setCollision();
	}
	
	protected Point getPointPosition() {
		return position.getLocation();
	}
	
	protected Rectangle getPosition() {
		return position;
	}
	
	protected void setCollision() {
		collision = position;
	}
	
	protected Rectangle getCollision() {
		return collision;
	}
	
	boolean isPointInside(Point pt) {
	    return position.contains(pt.x, pt.y);
	}
	
	/**
	 * Handles task of updating the animation frame and position of the sprite. 
	 * 
	 * 
	 * @return bound action (BitSet class is included in the standard Java package
	 * 
	 */
	public BitSet update() {
		BitSet action = new BitSet();

		// Increment the frame
		incFrame();

		// Update the position
		Point pos = new Point(position.x, position.y);
		pos.translate(velocity.x, velocity.y);

		// Check the bounds
		// Wrap?
		if (boundsAction == Sprite.BA_WRAP) {
			
			if ((pos.x + position.width) < bounds.x) 
				pos.x = bounds.x + bounds.width; 
			else if (pos.x > (bounds.x + bounds.width)) 
				pos.x = bounds.x - position.width; 
			
			if ((pos.y + position.height) < bounds.y) 
				pos.y = bounds.y + bounds.height; 
			else if (pos.y > (bounds.y + bounds.height)) 
				pos.y = bounds.y - position.height; 
		}
		// Bounce?
		else if (boundsAction == Sprite.BA_BOUNCE) {
			boolean bounce = false;
			Point   vel = new Point(velocity.x, velocity.y);
			if (pos.x < bounds.x) {
				bounce = true;
				pos.x = bounds.x;
				vel.x = -vel.x;
			}
			else if ((pos.x + position.width) > (bounds.x + bounds.width)) {
				bounce = true;
				pos.x = bounds.x + bounds.width - position.width;
				vel.x = -vel.x;
			}
			
			if (pos.y < bounds.y) {
				bounce = true;
				pos.y = bounds.y;
				vel.y = -vel.y;
			}
			else if ((pos.y + position.height) > (bounds.y + bounds.height)) {
				bounce = true;
				pos.y = bounds.y + bounds.height - position.height;
				vel.y = -vel.y;
			}
			
			if (bounce)
				setVelocity(vel);
		}
		// Die?
		else if (boundsAction == Sprite.BA_DIE) {
			if ((pos.x + position.width) < bounds.x || pos.x > bounds.width || 
					(pos.y + position.height) < bounds.y || pos.y > bounds.height) {
				action.set(Sprite.SA_KILL); 
				return action;
			}
		}
		// Stop (default)
		else {
			if (pos.x  < bounds.x || pos.x > (bounds.x + bounds.width - position.width)) {
				pos.x = Math.max(bounds.x, Math.min(pos.x,bounds.x + bounds.width - position.width));
				setVelocity(new Point(0, 0)); 
			}
			if (pos.y  < bounds.y || pos.y > (bounds.y + bounds.height - position.height)) {
				pos.y = Math.max(bounds.y, Math.min(pos.y,bounds.y + bounds.height - position.height));
				setVelocity(new Point(0, 0)); 
			}
		}
		setPosition(pos);

		return action;
	} // update
	
	public void draw(Graphics g) {
	// Draw the current frame
		if (!hidden)
		    g.drawImage(image[frame], position.x, position.y, component);
	}
	
	protected Sprite addSprite(BitSet action) {
		  return null;
	}
	
	protected boolean testCollision(Sprite test) {
	// Check for collision with another sprite
		if (test != this)
			return collision.intersects(test.getCollision()); 
		return false;
	}
	
}

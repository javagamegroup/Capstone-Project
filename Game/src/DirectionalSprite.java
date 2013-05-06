import java.awt.Component;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Point;
import java.awt.Rectangle;
import java.util.BitSet;


@SuppressWarnings("unused")
public class DirectionalSprite extends Sprite{

	
	/**
	 * Holds values that are used to calculate the sprite's velocity based on a given direction.
	 * When the direction of the sprite is changed, the velocity is multiplied by an X and Y component
	 * from the velDirs array.
	 */
	protected static final int[][] velDirs = {
		{0, -1}, {1, -1}, {1, 0}, {1,1},
		{0,1}, {-1, 1}, {-1, 0}, {-1, -1} };
	protected Image[][]		image;
	protected int           direction;
	
	public DirectionalSprite(Component comp, Image[] img, Point pos, Point vel, int z, int ba, int d) {
		  super(comp, img[d], pos, vel, z, ba);
		  image[0] = img;
		  setDirection(d);
	}
	
	public DirectionalSprite(Component comp, Image[][] img, int f, int fi, int fd, Point pos, Point vel, int z, int ba, int d) {
		  super(comp, img[d], f, fi, fd, pos, vel, z, ba);
		  image = img;
		  setDirection(d);
	}
	
	/** 
	 * first ensures that the direction is within the directional bounds (0 to 7)
	 * takes care to wrap the direction around if it goes beyond a boundary; this 
	 * gives the sprite the capability to rotate freely. The velocity is then modified using
	 * the velDirs directional velocity multipliers. 
	 * Finally, the new direction image is set with a call to setImage.
	 * @param dir
	 */
	public void setDirection(int dir) {
		// set the direction
		if(dir<0)
			dir=7;
		else if (dir > 7)
			dir = 0;
		direction = dir;
		
		// change the velocity 
		velocity.x *= velDirs[dir][0];
		velocity.y *= velDirs[dir][1];
		
		// Set the image
		setImage(image[dir]);
	}
	
	/** 
	 * overrides setVelocity from Sprite class because changing the velocity 
	 * should cause a change in the direction. 
	 * velocity is first assigned its new value. Direction is then altered based
	 * on the new velocity by way of a few comparisons and equations. 
	 * @param vel
	 */
	@Override
	public void setVelocity(Point vel) {
		velocity = vel;
		
		// Change the direction
		if (vel.x == 0 && vel.y == 0)
			return;
		if (vel.x == 0)
			direction = (vel.y + 1) * 2;
		else if (vel.x == 1)
			direction = vel.y + 1; 
		else if (vel.x == -1)
			direction = -vel.y + 6;
	}
	
	
}

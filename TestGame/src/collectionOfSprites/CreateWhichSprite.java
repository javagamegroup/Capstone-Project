package collectionOfSprites;

import java.awt.Component;
import java.awt.Image;
import java.awt.Point;
import java.util.Random;

import game.*;

public class CreateWhichSprite {

	private static Random rand = new Random();
	
	public CreateWhichSprite()
	{
		
	}
	private static DirectionalSprite createTurtleDuck(Point pos, int i, Image[][] turtleDuck, Component comp, int zOrder)
	{
		return new DirectionalSprite(comp, turtleDuck[i], pos, new Point(rand.nextInt() % 5, rand.nextInt() % 5), zOrder, DirectionalSprite.BA_STOP, 1);
	}
	
}

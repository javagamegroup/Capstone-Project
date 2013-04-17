

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Rectangle;
import java.util.ArrayList;


public class Event {

	
	private boolean playerEnemyCollision;
	Rectangle nonPlayerObj1;
	Rectangle nonPlayerObj2;
	ArrayList<Rectangle> Rect;
	
	
	
	public Event(boolean PCCollision, Rectangle NonPlayerObject1)
	{
		playerEnemyCollision = PCCollision;
		nonPlayerObj1 = NonPlayerObject1;
	}
	
	public Event(boolean PCCollision, ArrayList<Rectangle> Rect)
	{
		playerEnemyCollision = PCCollision;
		this.Rect = Rect;
	}
	
	public Event(boolean PCCollision, Rectangle NonPlayerObject1, Rectangle NonPlayerObject2)
	{
		playerEnemyCollision = PCCollision;
		nonPlayerObj1 = NonPlayerObject1;
		nonPlayerObj2 = NonPlayerObject2;
	}
	
	public void draw(Graphics g)
	{
		if(playerEnemyCollision == true)
		{
			g.setColor(Color.white);
			g.fillRoundRect(nonPlayerObj1.x-40, nonPlayerObj1.y-40, 130, 30, 30, 30);
			g.setColor(Color.black);
			g.drawString("HEY! I'M WALKIN HERE!", nonPlayerObj1.x-40, nonPlayerObj1.y-20);
		}
	}
	
	
}



import java.awt.Color;
import java.awt.Graphics;
import java.awt.Rectangle;


public class Event {

	
	private boolean playerEnemyCollision;
	Rectangle enemyChar;
	
	public Event(boolean PCCollision, Rectangle enemy)
	{
		playerEnemyCollision = PCCollision;
		enemyChar = enemy;
	}
	
	public void draw(Graphics g)
	{
		if(playerEnemyCollision == true)
		{
			g.setColor(Color.white);
			g.drawString("HEY! I'M WALKIN HERE!", enemyChar.x, enemyChar.y);
		}
	}
	
	
}



import java.awt.Color;
import java.awt.Graphics;


public class Event {

	
	private boolean playerEnemyCollision;
	EnemyAI enemyChar;
	
	public Event(boolean PCCollision, EnemyAI enemy)
	{
		playerEnemyCollision = PCCollision;
		enemyChar = enemy;
	}
	
	public void draw(Graphics g)
	{
		if(playerEnemyCollision == true)
		{
			g.setColor(Color.white);
			g.drawString("HEY! I'M WALKIN HERE!", enemyChar.rectx(), enemyChar.recty());
		}
	}
	
	
}

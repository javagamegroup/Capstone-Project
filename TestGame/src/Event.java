

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.event.MouseAdapter;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.PrintStream;
import java.util.ArrayList;


public class Event implements Runnable{

	private static final int ACHIEVEMENT_NOTICE_X = 150;
	private static final int ACHIEVEMENT_NOTICE_Y = 150;
	
	private boolean playerEnemyCollision;
	Rectangle nonPlayerObj1;
	Rectangle nonPlayerObj2;
	ArrayList<Rectangle> Rect;
	private PrintStream ps;
	private boolean enemyDestroyed = false;
	EnemyAI enemy;
	private int enemyHP;
	
	Rectangle achievementNotice = new Rectangle(ACHIEVEMENT_NOTICE_X, ACHIEVEMENT_NOTICE_Y, 250, 50);
	
	public Event(Rectangle NonPlayerObject1) 
	{
		nonPlayerObj1 = NonPlayerObject1;
	}
	
	public Event(ArrayList<Rectangle> Rect)
	{
		this.Rect = Rect;
	}
	
	public Event(Rectangle NonPlayerObject1, Rectangle NonPlayerObject2)
	{
		nonPlayerObj1 = NonPlayerObject1;
		nonPlayerObj2 = NonPlayerObject2;
	}
	
	public void setEnemyStatus(boolean deceased) {
		this.enemyDestroyed = deceased;
	}
	
	public Rectangle getANRect() {
		return achievementNotice;
	}
	
	public void setPlayerEnemyCollision(boolean collision) {
		this.playerEnemyCollision = collision;
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
		
		if(enemyDestroyed == true)
		{
			g.setColor(Color.white);
			g.fillRoundRect(achievementNotice.x, achievementNotice.y, 250, 50, 30, 30);
			g.setFont(new Font("Arial", Font.BOLD, 12));
			g.setColor(Color.black);
			g.drawString("ACHIEVEMENT UNLOCKED! Enemy terminated.", 150, 50);
		}
	} // of draw
	
	@Override
	public void run(){
        try{
            while(true){
                Thread.sleep(5);
            }
        }catch(Exception ex){
            System.err.println(ex.getMessage());
        }
    }
}

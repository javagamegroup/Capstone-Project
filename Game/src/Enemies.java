

import java.awt.Image;
import java.net.URL;
import java.util.ArrayList;
import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

import javax.swing.ImageIcon;

public class Enemies implements Runnable  {
	
	Player target;
	ArrayList<Wall> walls;
	EnemyAI enemy;
	private ArrayList areas;
	int bulletLife = 4;
	int bulletDamage;
	Achievements getAchieves = null;
	EnemyAI [] enemies = null;
	int numEnemies = 0;
	int num = 0;
	Event charE;
	int difficulty = 20;
	
    public Enemies(int x, int y,ArrayList<Wall> walls, ArrayList<Area> arrayList, Player play, int numEnemies) {
        this.walls = walls;
        this.areas = arrayList;
        this.numEnemies = numEnemies;
        enemies = new EnemyAI[numEnemies];
        target = play;
    }

    public void createEnemy(int x, int y,ArrayList<Wall> walls, ArrayList<Area> arrayList, Player play){
    	enemies[num] = new EnemyAI(x, y, walls, arrayList, play);
    	charE = new Event(true, enemies[num].enemyRect);
    	num ++;
    	if(num ==30) num =0;
    }
    
    public void destroyEnemy(int num){
    	enemies[num] = null;
    }
    
    public void enemyCollision(){
    	for(int j =0; j<numEnemies; j++){
			if(enemies[j] != null){
		        for (int i = 0; i < walls.size(); i++) {
		            Wall wall = (Wall) walls.get(i);
		            if (enemies[j].enemyRect.intersects(wall.objectRect)) {
		            	if(enemies[j].xDirection != 0)
		            		enemies[j].setXDirection(0);
		            	if(enemies[j].yDirection != 0)
		            		enemies[j].setYDirection(0);
		            }
		        }
			}
    	}
    	
    	for(int j =0; j<numEnemies; j++){
			if(enemies[j] != null){
		        for (int i = 0; i < areas.size(); i++) {
		            Area area = (Area) areas.get(i);
		            if (enemies[j].enemyRect.intersects(area.areaRect)) {
		            	if(enemies[j].xDirection != 0)
		            		enemies[j].setXDirection(0);
		            	if(enemies[j].yDirection != 0)
		            		enemies[j].setYDirection(0);
		            }
		        }
		    }
    	}
    	

    	for(int j =0; j<numEnemies; j++){
			if(enemies[j] != null){
				if (enemies[j].enemyRect.intersects(target.playerRect)) {
					
				}
			}
		}
    	
    	for(int j =0; j<numEnemies; j++){
			if(enemies[j] != null){
				if (enemies[j].health <=0)
					enemies[j] = null;
			}
    	}
    }
    
    public void draw(Graphics g) {
    	for(int i = 0; i<numEnemies; i++){
    		if(enemies[i]!=null)
	    		if(enemies[i].health <= 0)
	    			enemies[i] = null;
    		if(enemies[i]!=null){
	    		enemies[i].draw(g);
    		}
    	}
    }
    
    public void setDifficulty(int enemyRunSpeed) {
    	difficulty = enemyRunSpeed;
    	
    	
    }
    
    public void run(){
        try{
            while(true){
    				for(int i =0; i<numEnemies; i++){
	    				if(enemies[i] != null)
	    					enemies[i].update();
    				}
    			enemyCollision();
    			Thread.sleep(difficulty);
            }
        }catch(Exception ex){
            System.err.println(ex.getMessage());
        }
    }
}

package game;

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
	EnemyAI [] enemies = new EnemyAI[10];;
	int numEnemies = 10;
	int num = 0;
	Event charE;
	int difficulty = 20;
	boolean paused = false;
	
    public Enemies(int x, int y,Player play) {
        target = play;
    }

    public void createEnemy(int x, int y,ArrayList<Wall> walls, ArrayList<Area> arrayList, Player play){
    	enemies[num] = new EnemyAI(x, y, walls, arrayList, play);
    	charE = new Event(true, enemies[num].enemyRect);
    	num ++;
    	if(num ==10) num =0;
    }
    
    public void destroyEnemy(int num){
    	enemies[num] = null;
    }
    public void setNumEnemies(int num){
    	numEnemies = num;
    	enemies = new EnemyAI[numEnemies];
    }
    
    public void enemyCollision(){
    	for(int j =0; j<numEnemies; j++){
			if(enemies[j] != null){
		        for (int i = 0; i < World.walls.size(); i++) {
		            Wall wall = (Wall) World.walls.get(i);
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
		        for (int i = 0; i < World.getAreas().size(); i++) {
		            Area area = (Area) World.getAreas().get(i);
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
    
    public void killAllEnemies(){
    	for(int i = 0; i<numEnemies;i++){
    		enemies[i] = null;
    	}
    	num = 0;
    }
    public void paused(){
    	paused = true;
    }
    public void unpaused(){
    	paused = false;
    }
    
    public void run(){
        try{
            while(true){
            	if(paused==false){
	    				for(int i =0; i<numEnemies; i++){
		    				if(enemies[i] != null)
		    					enemies[i].update();
	    				}
	    			enemyCollision();
	    			Thread.sleep(difficulty);
            	}
    			else{Thread.sleep(7);}
            }
        }catch(Exception ex){
            System.err.println(ex.getMessage());
        }
    }
}

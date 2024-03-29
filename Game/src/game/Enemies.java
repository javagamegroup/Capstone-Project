package game;

import java.util.ArrayList;
import java.awt.Graphics;
import java.awt.Rectangle;

public class Enemies implements Runnable  {
	
	Player target;
	ArrayList<Wall> walls;
	EnemyAI enemy;
	int bulletLife = 4;
	int bulletDamage;
	Object [] enemies = new Object[10];
	int [] enemyNums = new int[10];
	int numEnemies = 10;
	int totalEnemies = 0;
	int num = 0;
	Event charE;
	int difficulty = 20;
	boolean paused = false;
	
    public Enemies(int x, int y,Player play) {
        for(int i=0; i<10; i++){
    		enemies[i] = null;
    		enemyNums[i] = -1;
    	}
        target = play;
    }

    public void createEnemy(int x, int y,ArrayList<Wall> walls, ArrayList<Area> arrayList, Player play, char z){
    	switch(z)
    	{
    	case 'a': 
    		enemies[num] = new EnemyAI(x, y, walls, arrayList, play);
    		enemyNums[num] = 0;
    		num ++;
        	totalEnemies ++;
    		break;
    	default:
    		break;
    	
    	}
    	if(num ==10) num =0;
    }
    
    public void destroyEnemy(int num){
    	enemies[num] = null;
    	enemyNums[num] = -1;
    	totalEnemies --;
    }
    public void setNumEnemies(int num){
    	numEnemies = num;
    	enemies = new EnemyAI[numEnemies];
    }
    
    public void draw(Graphics g) {
    	for(int i = 0; i<numEnemies; i++){
        	switch(enemyNums[i])
        	{
        	case 0: 
        		if(enemies[i]!=null)
    	    		if(((EnemyAI) enemies[i]).getHealth() <= 0)
    	    			destroyEnemy(i);
        		if(enemies[i]!=null){
        			((EnemyAI) enemies[i]).draw(g);
        		}
        	default:
        		break;
        	
        	}
    	}
    }
    
    public void setDifficulty(int enemyRunSpeed) {
    	difficulty = enemyRunSpeed;
    	
    	
    }
    
    public void killAllEnemies(){
    	for(int i = 0; i<numEnemies;i++){
    		enemies[i] = null;
    		enemyNums[i] = -1;
    	}
    	num = 0;
    	totalEnemies = 0;
    }
    
    public void paused(){
    	paused = true;
    }
    
    public void unpaused(){
    	paused = false;
    }
    
    public Rectangle getRect(int x){
    	switch(enemyNums[x])
    	{
    	case 0: 
    		return ((EnemyAI) enemies[x]).getRect();
    	default:
    		break;
    	
    	}
		return null;
    }
    
    public void decreaseHealth(int x, int damage){
    	switch(enemyNums[x])
    	{
    	case 0: 
    		((EnemyAI) enemies[x]).decreaseHealth(damage);
    		if(((EnemyAI) enemies[x]).getHealth() <= 0)
    			destroyEnemy(x);
    	default:
    		break;
    	
    	}
    }
    
    public int getHealth(int x){
    	switch(enemyNums[x])
    	{
    	case 0: 
    		return ((EnemyAI) enemies[x]).getHealth();
    	default:
    		break;
    	
    	}
		return -1;
    }
    
    
    
    public void run(){
        try{
            while(true){
            	if(paused==false){
	    				for(int i =0; i<numEnemies; i++){
	    					switch(enemyNums[i])
	    		        	{
	    		        	case 0: 
	    		        		if(enemies[i]!=null)
	    		        			((EnemyAI) enemies[i]).update();
	    		        	default:
	    		        		break;
	    		        	
	    		        	}
	    				}
	    			Thread.sleep(difficulty);
            	}
    			else{Thread.sleep(7);}
            }
        }catch(Exception ex){
            System.err.println(ex.getMessage());
        }
    }
}

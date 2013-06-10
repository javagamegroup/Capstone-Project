package game;

import java.awt.Image;
import java.net.URL;
import java.util.ArrayList;
import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

import javax.swing.ImageIcon;

public class NPC implements Runnable  {
	
	Player target;
	ArrayList<Wall> walls;
	private ArrayList areas;
	Achievements getAchieves = null;
	Object [] npc = new Object[10];
	int [] npcNums = new int[10];
	int numNpc = 10;
	int totalNpc = 0;
	int num = 0;
	Event charE;
	int difficulty = 30;
	boolean paused = false;
	
    public NPC(int x, int y,Player play) {
        for(int i=0; i<10; i++){
    		npc[i] = null;
    		npcNums[i] = -1;
    	}
        target = play;
    }

    public void createNPC(int x, int y,ArrayList<Wall> walls, ArrayList<Area> arrayList, Player play, char z){
    	switch(z)
    	{
    	case 'a': 
    		npc[num] = new TurtleDuck(400, 400, World.walls, World.getAreas());
    		npcNums[num] = 0;
    		num ++;
        	totalNpc ++;
    		break;
    	default:
    		break;
    	
    	}
    	if(num ==10) num =0;
    }
    
    public void destroyNPC(int num){
    	npc[num] = null;
    	npcNums[num] = -1;
    	totalNpc --;
    }
    public void setnumNpc(int num){
    	numNpc = num;
    	npc = new TurtleDuck[numNpc];
    }
    
    public void draw(Graphics g) {
    	for(int i = 0; i<numNpc; i++){
        	switch(npcNums[i])
        	{
        	case 0: 
        		if(npc[i]!=null){
        			((TurtleDuck) npc[i]).draw(g);
        		}
        	default:
        		break;
        	
        	}
    	}
    }
    
    public void setDifficulty(int enemyRunSpeed) {
    	difficulty = enemyRunSpeed;
    	
    	
    }
    
    public void killAllNPC(){
    	for(int i = 0; i<numNpc;i++){
    		npc[i] = null;
    		npcNums[i] = -1;
    	}
    	num = 0;
    	totalNpc = 0;
    }
    
    public void paused(){
    	paused = true;
    }
    
    public void unpaused(){
    	paused = false;
    }
    
    public Rectangle getRect(int x){
    	switch(npcNums[x])
    	{
    	case 0: 
    		return ((TurtleDuck) npc[x]).getRect();
    	default:
    		break;
    	
    	}
		return null;
    }
    
    public void decreaseHealth(int x, int damage){
    	switch(npcNums[x])
    	{
    	case 0: 
    		((TurtleDuck) npc[x]).decreaseHealth(damage);
    		if(((TurtleDuck) npc[x]).getHealth() <= 0)
    			destroyNPC(x);
    	default:
    		break;
    	
    	}
    }
    
    public int getHealth(int x){
    	switch(npcNums[x])
    	{
    	case 0: 
    		return ((TurtleDuck) npc[x]).getHealth();
    	default:
    		break;
    	
    	}
		return -1;
    }
    
    
    
    public void run(){
        try{
            while(true){
            	if(paused==false){
	    				for(int i =0; i<numNpc; i++){
	    					switch(npcNums[i])
	    		        	{
	    		        	case 0: 
	    		        		if(npc[i]!=null)
	    		        			((TurtleDuck) npc[i]).update();
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

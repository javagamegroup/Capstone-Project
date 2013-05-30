package game;

import java.awt.Graphics;
import java.awt.Rectangle;

public class Obstacles {
	Object [] obs= new Object [30];
	int [] obsNums = new int[30];
	int num = 0;
	
	
    public Obstacles() {
        for(int i=0; i<30; i++){
        	obs[i] = null;
        	obsNums[i] = -1;
    	}
        
    }
    
    public void createObs(int x, int y,char z){
    	switch(z)
    	{
    	case 'a': 
    		obs[num] = new Rock(x, y);
    		obsNums[num] = 0;
    		num ++;
    		break;
    	default:
    		break;
    	
    	}
    	if(num ==30) num =0;
    }
    
    public void destroyObs(int num){
    	obs[num] = null;
    	obsNums[num] = -1;
    }
    
    public void destroyAllObs(){
    	for(int i = 0; i<30;i++){
    		obs[i] = null;
    		obsNums[i] = -1;
    	}
    	num = 0;
    }
    
    public Rectangle getRect(int x){
    	switch(obsNums[x])
    	{
    	case 0: 
    		return ((Rock) obs[x]).getRect();
    	default:
    		break;
    	
    	}
		return null;
    }
    
    public void draw(Graphics g) {
    	for(int i = 0; i<30; i++){
    		if(obs[i]!=null)
        	switch(obsNums[i])
        	{
        	case 0: 
        			((Rock) obs[i]).draw(g);
        		break;
        	default:
        		break;
        	
        	}
    	}
    }
}

package game;

import java.awt.Graphics;
import java.awt.Rectangle;
import java.util.ArrayList;

public class Items {
	Object [] item= new Object [30];
	int [] itemNums = new int[30];
	int num = 0;
	
	
    public Items() {
        for(int i=0; i<30; i++){
    		item[i] = null;
    		itemNums[i] = -1;
    	}
        
    }
    
    public void createItem(int x, int y,char z){
    	switch(z)
    	{
    	case 'a': 
    		item[num] = new IncreasedBulletPickUp(x, y);
    		itemNums[num] = 0;
    		break;
    	default:
    		break;
    	
    	}
    	num ++;
    	if(num ==30) num =0;
    }
    
    public void destroyItem(int num){
    	switch(itemNums[num])
    	{
    	case 0: 
    		GamePanel.gun.increaseDamage(1);
    	default:
    		break;
    	
    	}
    	item[num] = null;
    	itemNums[num] = -1;
    }
    public void destroyAllItems(){
    	for(int i = 0; i<30;i++){
    		item[i] = null;
    		itemNums[i] = -1;
    	}
    	num = 0;
    }
    public Rectangle getRect(int x){
    	switch(itemNums[x])
    	{
    	case 0: 
    		return ((IncreasedBulletPickUp) item[x]).getRect();
    	default:
    		break;
    	
    	}
		return null;
    }
    
    public void draw(Graphics g) {
    	for(int i = 0; i<30; i++){
    		if(item[i]!=null)
        	switch(itemNums[i])
        	{
        	case 0: 
        			((IncreasedBulletPickUp) item[i]).draw(g);
        		break;
        	default:
        		break;
        	
        	}
    	}
    }
}

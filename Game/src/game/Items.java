package game;

import java.awt.Graphics;
import java.util.ArrayList;

public class Items {
	Object [] item= new Object [30];
	int num = 0;
	
	
    public Items() {
        for(int i=0; i<30; i++){
    		item[i] = null;
    	}
    }
    
    public void createItem(int x, int y,int z){
    	switch(z)
    	{
    	case 0: 
    		item[num] = new IncreasedBulletPickUp(x, y);
    		break;
    	default:
    		break;
    	
    	}
    	num ++;
    	if(num ==30) num =0;
    }
    
    public void destroyItem(int num){
    	item[num] = null;
    }
    public void destroyAllItems(){
    	for(int i = 0; i<30;i++){
    		item[i] = null;
    	}
    	num = 0;
    }
    
    public void draw(Graphics g) {
    	for(int i = 0; i<30; i++){
    		if(item[i]!=null)
    			((Items) item[i]).draw(g);
    	}
    }
}

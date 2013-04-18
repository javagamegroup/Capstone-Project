
import java.awt.*;
import java.net.URL;
import java.util.ArrayList;

import javax.swing.ImageIcon;

public class EnemyAI extends Actor implements Runnable {
    
	ImageIcon paul;
	Image enemyImage;
	Player target;
	boolean resting = false;
	private ArrayList areas;
	Rectangle enemyRect;
	ArrayList<Wall> walls;
	boolean isAlive = true;
	int health = 6;
	Event charE;
	
	int difficulty = 20;
	
    public EnemyAI(int x, int y,ArrayList<Wall> walls, ArrayList<Area> arrayList, Player play) {
        super(x, y);
        target = play;
        this.walls = walls;
        this.areas = arrayList;
        URL paulLoc = this.getClass().getResource("Resources/baggage.png");
       	paul = new ImageIcon(paulLoc);
        enemyImage = paul.getImage();
        this.enemyRect = getRect(100, 200, 20,20);
        this.setRect(enemyRect);
        charE = new Event(true, this.enemyRect);
    }
    
    //Find a path to the target
    public void findPathToTarget() {
    	if(enemyRect.x < target.playerRect.x) 
    		setXDirection(1);
    	if(enemyRect.x > target.playerRect.x) 
    		setXDirection(-1);
    	if(enemyRect.y < target.playerRect.y) 
    		setYDirection(1);
    	if(enemyRect.y > target.playerRect.y) 
    		setYDirection(-1);
    	if(enemyRect.x == target.playerRect.x) 
    		setXDirection(0);
    	if(enemyRect.x == target.playerRect.x) 
    		setXDirection(0);
    	if(enemyRect.y == target.playerRect.y) 
    		setYDirection(0);
    	if(enemyRect.y == target.playerRect.y) 
    		setYDirection(0);
    }
    //Move in that direction
    public void move(){
        incX(xDirection);
        incY(yDirection);
        
    }
    
    public void decreaseHealth(int x){
    	health -= x;
    	if(health<=0)
    		destroyEnemy();
    }
    
    public void destroyEnemy(){
    	isAlive = false;
    	this.enemyRect = null;
    }
    
    public void setCoord(int x, int y){
    	this.enemyRect = getRect(x, y, 20,20);
    	this.setRect(enemyRect);
    }
    
    public void collision(){
        for (int i = 0; i < walls.size(); i++) {
            Wall wall = (Wall) walls.get(i);
            if (this.enemyRect.intersects(wall.objectRect)) {
            	if(xDirection != 0)
            		setXDirection(0);
            	if(yDirection != 0)
            		setYDirection(0);
            }
            

        }
        
        for (int i = 0; i < areas.size(); i++) {
            Area area = (Area) areas.get(i);
            if (this.enemyRect.intersects(area.areaRect)) {
            	if(xDirection != 0)
            		setXDirection(0);
            	if(yDirection != 0)
            		setYDirection(0);
            }
        }
       
    }
    
    public void draw(Graphics g) {
    	g.drawImage(enemyImage, this.rectx(), this.recty(), null);
		if(isAlive)
			if(this.enemyRect.intersects(target.playerRect)){
				charE.draw(g);
			}
    	
    	
    }
    
    public void setDifficulty(int enemyRunSpeed) {
    	difficulty = enemyRunSpeed;
    	
    	
    }
    
    //In Run method, move in that direction and then wait
    @Override
    public void run(){
        try{
            while(true){
            	if(isAlive){
                  findPathToTarget();
                    move();
                    Thread.sleep(difficulty);
            	}
            	
            }
        }catch(Exception ex){
            System.err.println(ex.getMessage());
        }
    }
}
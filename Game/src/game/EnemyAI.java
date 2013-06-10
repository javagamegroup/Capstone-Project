package game;
import java.awt.*;
import java.util.ArrayList;

import javax.swing.ImageIcon;

public class EnemyAI extends Actor{
    
	ImageIcon paul;

	public static Image[][] enemyImage;
	private static int numPoses = 1; // size of first dimension of player image array
	private static int numFramesPerPose = 8; // size of second dimension of player image array
	
	int frameRate = 250;
	int i = 0;
	Player target;
	boolean resting = false;
	private ArrayList<Area> areas;
	Rectangle enemyRect;
	ArrayList<Wall> walls;
	boolean isAlive = true;
	int health = 6;
	Event charE;
	double spikeTime = -1;
	boolean started = false;
	
	private static int walkWhichWay = 0;
	double animationStartTime = -1;
	
	int difficulty = 20;
	
    public EnemyAI(int x, int y,ArrayList<Wall> walls, ArrayList<Area> arrayList, Player play) {
        super(x, y);
        target = play;
        this.walls = walls;
        this.areas = arrayList;
//        URL paulLoc = this.getClass().getResource("/Resources/enemy.png");
//       	paul = new ImageIcon(paulLoc);
//        enemyImage = paul.getImage();
        enemyInit();
        this.enemyRect = getRect(x, y, 44,54);
        this.setRect(enemyRect);
        charE = new Event(true, this.enemyRect);
    }
    
    public static void enemyInit()
    {
    	enemyImage = new Image[numPoses][numFramesPerPose];
    	
    	java.net.URL imgURL;
		ImageIcon enemyIcon;
		String path;
		
		for(int i = 0; i < numPoses; i++) {
			for(int j = 0; j < numFramesPerPose; j++) {
				path = "/Resources/enemy" + i + j + ".png"; 
				imgURL = Player.class.getResource(path);
				enemyIcon = new ImageIcon(imgURL);
				enemyImage[i][j] = enemyIcon.getImage();
			}
		}
		
    }
    
    //Find a path to the target
    public void findPathToTarget() {
    	if((enemyRect.x-target.playerRect.x<75&&enemyRect.x-target.playerRect.x>-75)&&(enemyRect.y-target.playerRect.y<75&&enemyRect.y-target.playerRect.y>-75)||started){
	    	started = true;
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
	    	if(GamePanel.player.playerRect.intersects(enemyRect)){setYDirection(0);setXDirection(0);}
    	}
    }
    //Move in that direction
    public void move(){
        incX(xDirection);
        incY(yDirection);
        
    }
    
    public void decreaseHealth(int x){
    	health -= x;
    }
    
    public void destroyEnemy(){
    	isAlive = false;
    	this.enemyRect = null;
    }
    
    public void setCoord(int x, int y){
    	this.enemyRect = getRect(x, y, 44,54);
    	this.setRect(enemyRect);
    }
    
    public void update(){
    	findPathToTarget();
    	collision();
        move();
    }
    
    public Rectangle getRect(){
		return enemyRect;
    }
    public int getHealth(){
		return health;
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
        
        for (int i = 0; i < GamePanel.obs.num; i++)
			if(GamePanel.obs.obs[i] != null)
				if (GamePanel.obs.getRect(i).intersects(enemyRect)) {
					if(GamePanel.obs.getType(i)==0){
		            	if(enemyRect.x<= GamePanel.obs.getRect(i).x+44 && enemyRect.x>= GamePanel.obs.getRect(i).x && xDirection <=-1){
	            			this.xDirection = 0;
	            			//this.setCoord(enemyRect.x, enemyRect.y+1);
		            	}
		            	if(enemyRect.x+44>= GamePanel.obs.getRect(i).x  && enemyRect.x+44<= GamePanel.obs.getRect(i).x+32 && xDirection >=1){
	            			this.xDirection = 0;
		            		//this.setCoord(enemyRect.x, enemyRect.y+1);
		            	}
		            	if(enemyRect.y+54>= GamePanel.obs.getRect(i).y && enemyRect.y+54<= GamePanel.obs.getRect(i).y+32 && yDirection >=1){
	            			this.yDirection = 0;
	            			//this.setCoord(enemyRect.x+1, enemyRect.y);
		            	}
		            	if(enemyRect.y<= GamePanel.obs.getRect(i).y+54 && enemyRect.y>= GamePanel.obs.getRect(i).y && yDirection <=-1){
	            			this.yDirection = 0;
		            		//this.setCoord(enemyRect.x+1, enemyRect.y);
		            	}
					}
					if(GamePanel.obs.getType(i)==1)
			        	if(System.currentTimeMillis() - spikeTime > 1000 || spikeTime == -1)
			        	{
			            	spikeTime = System.currentTimeMillis();
			            		health -= 2;
			        	}
				}
       
    }
    
    public void draw(Graphics g) {
    	switch(walkWhichWay)
    	{
    	case 0:
    		if(System.currentTimeMillis() - animationStartTime  > frameRate || animationStartTime == -1){
            	animationStartTime = System.currentTimeMillis();
            	i++;
    		}
    		
        	if(i != 7)
        	{
        		g.drawImage(enemyImage[0][i], enemyRect.x, enemyRect.y, null);
        		
        	}
        	else
        	{
        		i = 0;
        		g.drawImage(enemyImage[0][i], enemyRect.x, enemyRect.y, null);
        	}            		
            
    		break;
    	}
    }
    
    
}
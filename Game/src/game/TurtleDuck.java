package game;
import java.awt.*;
import java.net.URL;
import java.util.ArrayList;
import java.util.Random;

import game.RandomGen;

import javax.swing.ImageIcon;

import Exceptions.MinMaxException;

public class TurtleDuck extends Actor{
    
	
	public static Image[][] turtleDuckImage;
	
	private static int numPoses = 4; // size of first dimension of player image array
	private static int numFramesPerPose = 2; // size of second dimension of player image array
	int frameRate = 250;
	int i = 0;
	private static int walkWhichWay = 0;
	
	URL theDudeLoc;
	Point target = null;
	boolean resting = false;
	private ArrayList areas;
	Rectangle turtleDuckRect;
	ArrayList<Wall> walls;
	boolean isAlive = true;
	int health = 6;
	Event charE;
	boolean started = false;
	double animationStartTime = -1;
	
	Point newPoint;
	protected static RandomGen randG;
	protected Random rand = new Random();
	int[] XYPoint = new int[2];
	
	int difficulty = 20;
	boolean paused = false;
	
    public TurtleDuck(int x, int y,ArrayList<Wall> walls, ArrayList<Area> arrayList) {
        super(x, y);
        this.walls = walls;
        this.areas = arrayList;
//        theDudeLoc = this.getClass().getResource("/Resources/thealiendude.png");
//       	theDude = new ImageIcon(theDudeLoc);
//        vendorImage = theDude.getImage();
        turtleDuckInit();
        this.turtleDuckRect = getRect(x, y, 44,54);
        this.setRect(turtleDuckRect);
        charE = new Event(true, this.turtleDuckRect);
        try {
			randG = new RandomGen(32, 768, 92, 448);
		} catch (MinMaxException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    }
    
    public void turtleDuckInit()
    {
    	turtleDuckImage = new Image[numPoses][numFramesPerPose];
    	
    	java.net.URL imgURL;
		ImageIcon turtleDuckIcon;
		String path;
		
		for(int i = 0; i < numPoses; i++) {
			for(int j = 0; j < numFramesPerPose; j++) {
				path = "/Resources/turtleduck" + i + j + ".png"; 
				imgURL = Player.class.getResource(path);
				turtleDuckIcon = new ImageIcon(imgURL);
				turtleDuckImage[i][j] = turtleDuckIcon.getImage();
			}
		}
    }
    
  //Find a path to the target
    public void findPathToTarget() {
    	//if((turtleDuckRect.x-target.x<75&&turtleDuckRect.x-target.x>-75)&&(turtleDuckRect.y-target.y<75&&turtleDuckRect.y-target.y>-75)||started){
	    	started = true;
    		if(turtleDuckRect.x < target.x) {
	    		setXDirection(1); 
	    		walkWhichWay = 0;}
	    	if(turtleDuckRect.x > target.x) {
	    		setXDirection(-1);
	    		walkWhichWay = 2;}
	    	if(turtleDuckRect.y < target.y) {
	    		setYDirection(1);
	    		walkWhichWay = 1;}
	    	if(turtleDuckRect.y > target.y) {
	    		setYDirection(-1);
	    		walkWhichWay = 3; }
	    	if(turtleDuckRect.x == target.x) 
	    		setXDirection(0);
	    	if(turtleDuckRect.y == target.y) 
	    		setYDirection(0);
	    	if(turtleDuckRect.y == target.y && turtleDuckRect.x == target.x){findNewTarget();}
    	//}
    }
    
    //Move in that direction
    public void move(){
        incX(xDirection);
        incY(yDirection);
        
    }
    
    public void setStarted(boolean startedFlag)
    {
    	started = startedFlag;
    }
    
    public void setTargetLoc(Point pos)
    {
    	target = pos;
    }
    
    public void decreaseHealth(int x){
    	health -= x;
    }
    
    public void destroyvendor(){
    	isAlive = false;
    	this.turtleDuckRect = null;
    }
    
    public void setCoord(int x, int y){
    	this.turtleDuckRect = getRect(x, y, 20,20);
    	this.setRect(turtleDuckRect);
    }
    
    public Point randPoint()
	{
    	XYPoint[0] = 0;
    	XYPoint[1] = 0;
//		while( XYPoint[0] < 32 || XYPoint[0] > 768 && XYPoint[1] < 92 || XYPoint[1] > 448)
//		{
//			XYPoint[2] = rand.nextInt(768);
//			XYPoint[3] = rand.nextInt(448);
			XYPoint = randG.randomCoordinates();
		//	XYPoint[1] = randG.randomCoordinates()[1];
		//}
		newPoint = new Point(XYPoint[0], XYPoint[1]);
		return newPoint;
	}
    
    public void findNewTarget()
    {	
    	if(target == null)
    	{
    		target = new Point(400, 400);
    		newPoint = randPoint();
    	}
		if(turtleDuckRect.x == target.x && turtleDuckRect.y == target.y)
		{
    		newPoint = randPoint();
			target = newPoint;
		}
//		else
//			target = newPoint;
    }
    
    public void update(){
    	findNewTarget();
		findPathToTarget();
    	//collision();
    	move();
    }
    
    public Rectangle getRect(){
		return turtleDuckRect;
    }
    public int getHealth(){
		return health;
    }
    
    public void paused(){
    	paused = true;
    }
    
    public void unpaused(){
    	paused = false;
    }
    
    public void collision(){
    	
        for (int i = 0; i < areas.size(); i++) {
            Area area = (Area) areas.get(i);
            if (this.turtleDuckRect.intersects(area.areaRect)) {
            	if(xDirection != 0)
            		setXDirection(0);
            	if(yDirection != 0)
            		setYDirection(0);
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
    		
        	if(i != 2)
        	{
        		g.drawImage(turtleDuckImage[0][i], turtleDuckRect.x, turtleDuckRect.y, null);
        	}
        	else
        	{
        		i = 0;
        		g.drawImage(turtleDuckImage[0][i], turtleDuckRect.x, turtleDuckRect.y, null);
        	} 
    		break;
    	case 1:
    		if(System.currentTimeMillis() - animationStartTime  > frameRate || animationStartTime == -1){
            	animationStartTime = System.currentTimeMillis();
            	i++;
    		}
            	
            if(i != 2)
        	{
        		g.drawImage(turtleDuckImage[1][i], turtleDuckRect.x, turtleDuckRect.y, null);
        	}
        	else
        	{
        		i = 0;
        		g.drawImage(turtleDuckImage[1][i], turtleDuckRect.x, turtleDuckRect.y, null);
        	} 
    		break;
    	case 2:
    		if(System.currentTimeMillis() - animationStartTime  > frameRate || animationStartTime == -1){
            	animationStartTime = System.currentTimeMillis();
            	i++;
    		}	
            	
            if(i != 2)
        	{
        		g.drawImage(turtleDuckImage[2][i], turtleDuckRect.x, turtleDuckRect.y, null);
        	}
        	else
        	{
        		i = 0;
        		g.drawImage(turtleDuckImage[2][i], turtleDuckRect.x, turtleDuckRect.y, null);
        	}
    		break;
    	case 3:
    		if(System.currentTimeMillis() - animationStartTime  > frameRate || animationStartTime == -1){
            	animationStartTime = System.currentTimeMillis();
            	i++;
    		}
            	
            if(i != 2)
        	{
        		g.drawImage(turtleDuckImage[3][i], turtleDuckRect.x, turtleDuckRect.y, null);
        	}
        	else
        	{
        		i = 0;
        		g.drawImage(turtleDuckImage[3][i], turtleDuckRect.x, turtleDuckRect.y, null);
        	} 
    		break;
  //  	default:
  //  		System.out.println("walkWhichWay equals something besides 0 - 4");
    	}
    }
    
//    public void run(){
//        try{
//            while(true){
//            	if(paused==false){
//            		update();
//	                move();
//	                Thread.sleep(30);
//            	}
//                else{Thread.sleep(30);}
//            }
//        }catch(Exception ex){
//        	ex.printStackTrace();
//            System.err.println(ex.getMessage());
//        }
//    }
//    
    
}
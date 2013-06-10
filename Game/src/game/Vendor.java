package game;
import java.awt.*;
import java.net.URL;
import java.util.ArrayList;
import java.util.Random;

import game.RandomGen;

import javax.swing.ImageIcon;

import Exceptions.MinMaxException;

public class Vendor extends Actor{
    
	ImageIcon theDude;
	Image vendorImage;
	URL theDudeLoc;
	Point target;
	boolean resting = false;
	private ArrayList<Area> areas;
	Rectangle vendorRect;
	ArrayList<Wall> walls;
	boolean isAlive = true;
	int health = 6;
	Event charE;
	boolean started = false;
	
	Point newPoint;
	protected static RandomGen randG;
	protected Random rand = new Random();
	int[] XYPoint = new int[2];
	
	int difficulty = 20;
	boolean paused = false;
	
    public Vendor(int x, int y,ArrayList<Wall> walls, ArrayList<Area> arrayList) {
        super(x, y);
        this.walls = walls;
        this.areas = arrayList;
        theDudeLoc = this.getClass().getResource("/Resources/thealiendude.png");
       	theDude = new ImageIcon(theDudeLoc);
        vendorImage = theDude.getImage();
        this.vendorRect = getRect(x, y, 44,54);
        this.setRect(vendorRect);
        charE = new Event(true, this.vendorRect);
        try {
			randG = new RandomGen(32, 768, 92, 448);
		} catch (MinMaxException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    }
    
    //Find a path to the target
    public void findPathToTarget() {
    	//if((vendorRect.x-target.x<75&&vendorRect.x-target.x>-75)&&(vendorRect.y-target.y<75&&vendorRect.y-target.y>-75)||started){
	    	started = true;
    		if(vendorRect.x < target.x) 
	    		setXDirection(1);
	    	if(vendorRect.x > target.x) 
	    		setXDirection(-1);
	    	if(vendorRect.y < target.y) 
	    		setYDirection(1);
	    	if(vendorRect.y > target.y) 
	    		setYDirection(-1);
	    	if(vendorRect.x == target.x) 
	    		setXDirection(0);
	    	if(vendorRect.y == target.y) 
	    		setYDirection(0);
	    	if(vendorRect.y == target.y && vendorRect.x == target.x){findNewTarget();}
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
    	this.vendorRect = null;
    }
    
    public void setCoord(int x, int y){
    	this.vendorRect = getRect(x, y, 20,20);
    	this.setRect(vendorRect);
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
		if(vendorRect.x == target.x && vendorRect.y == target.y)
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
    	collision();
    	move();
    }
    
    public Rectangle getRect(){
		return vendorRect;
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
            if (this.vendorRect.intersects(area.areaRect)) {
            	if(xDirection != 0)
            		setXDirection(0);
            	if(yDirection != 0)
            		setYDirection(0);
            }
        }
       
    }
    
    public void draw(Graphics g) {
    	g.drawImage(vendorImage, this.rectx(), this.recty(), null);
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
    
    
}
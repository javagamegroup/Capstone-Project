package game;

import java.awt.Image;
import java.net.URL;
import java.util.ArrayList;
import java.util.Timer;
import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

import javax.swing.ImageIcon;

import Exceptions.InvalidCharacterException;

public class Player extends Actor implements Runnable  {
	
	ImageIcon tad;
	Image playerImage;
	ArrayList<Wall> walls;
	private ArrayList areas;
	Projectile bullet [] = new Projectile [30];
	int numBullets = 0;
	int fireRate = 250;
	Timer timer = new Timer();
	protected Rectangle playerRect;
	double startTime = -1;
	double bulletLife;
	int health = 5;
	public boolean paused = false;
	private boolean moveLevels = false;
	Level level = null;
	
    public Player(int x, int y, Level level) {
        super(x, y);
        this.level = level;
        bulletLife = 2500;
        URL loc = this.getClass().getResource("/Resources/sokoban.png");
        tad = new ImageIcon(loc);
        
        playerImage = tad.getImage();
        
        this.playerRect = getRect(x, y, 32,32);

        this.setRect(playerRect);
    }
    
    public void collision(){
        for (int i = 0; i < World.walls.size(); i++) {
            Wall wall = (Wall) World.walls.get(i);
            	if(playerRect.intersects(wall.objectRect) && xDirection ==1){
            		if(this.playerRect.x<735){}
            		else
            			this.xDirection = 0;
            	}
            	if(playerRect.intersects(wall.objectRect)  && xDirection ==-1){
            		if(this.playerRect.x>33){}
            		else
            			this.xDirection = 0;
            	}
            	if(playerRect.intersects(wall.objectRect) && yDirection ==1)
            		if(this.playerRect.y<507){}
            		else
            			this.yDirection = 0;
            	if(playerRect.intersects(wall.objectRect) && yDirection ==-1)
            		if(this.playerRect.y>93){}
            		else
            			this.yDirection = 0;
            	
            }
        
        for (int i = 0; i < World.getAreas().size(); i++) {
            Area area = (Area) World.getAreas().get(i);
            if (this.playerRect.intersects(area.areaRect)) 
            {
            	if(playerRect.x> 500 && xDirection ==1)
            	{
            		try {GamePanel.level = GamePanel.level.setPlayerEntrance('e',GamePanel.level);} catch (InvalidCharacterException e) {e.printStackTrace();}
            		this.setLevel(true);
            	}
            	else if(playerRect.x< 400 && xDirection ==-1)
            	{
            		try {GamePanel.level = GamePanel.level.setPlayerEntrance('w',GamePanel.level);} catch (InvalidCharacterException e) {e.printStackTrace();}
            		this.setLevel(true);
            	}
            	else if(playerRect.y> 200 && yDirection ==1)
            	{
            		try {GamePanel.level = GamePanel.level.setPlayerEntrance('s',GamePanel.level);} catch (InvalidCharacterException e) {e.printStackTrace();}
            		this.setLevel(true);
            	}
            	else if(playerRect.y< 400 && yDirection ==-1)
            	{
            		try {GamePanel.level = GamePanel.level.setPlayerEntrance('n',GamePanel.level);} catch (InvalidCharacterException e) {e.printStackTrace();}
            		this.setLevel(true);
            	}
            }
        }

       
    }

    public void move(){
    	collision();
        incX(xDirection);
        incY(yDirection);
    }
    
 
    public void setCoord(int x, int y){
    	this.playerRect = getRect(x, y, 32,32);
    	this.setRect(playerRect);
    }
    
    public void draw(Graphics g) {
    	g.drawImage(playerImage, playerRect.x, playerRect.y, null);
    	
    }
    
        public void keyPressed(KeyEvent e) {
            if (e.getKeyCode() == e.VK_A) {
                setXDirection(-1);

            } if (e.getKeyCode() == e.VK_D) {

                setXDirection(1);

            }if (e.getKeyCode() == e.VK_W) {

                setYDirection(-1);

            }if (e.getKeyCode() == e.VK_S) {

                setYDirection(1);
            }
            if (e.getKeyCode() == e.VK_UP) {
            	if(System.currentTimeMillis() - startTime  > fireRate || startTime == -1){
            	startTime = System.currentTimeMillis();
                GamePanel.gun.createBullet('y', -1,playerRect.x, playerRect.y, bulletLife );
            	}

            } if (e.getKeyCode() == e.VK_DOWN) {
            	if(System.currentTimeMillis() - startTime > fireRate || startTime == -1){
            	startTime = System.currentTimeMillis();
            	GamePanel.gun.createBullet('y', 1,playerRect.x, playerRect.y, bulletLife);
            	}

            }if (e.getKeyCode() == e.VK_LEFT) {
            	if(System.currentTimeMillis() - startTime > fireRate || startTime == -1){
            	startTime = System.currentTimeMillis();
            	GamePanel.gun.createBullet('x', -1,playerRect.x, playerRect.y, bulletLife);
            	}

            }if (e.getKeyCode() == e.VK_RIGHT) {
            	if(System.currentTimeMillis() - startTime > fireRate || startTime == -1){
            	startTime = System.currentTimeMillis();
            	GamePanel.gun.createBullet('x', 1,playerRect.x, playerRect.y, bulletLife );
            	}
            }
        }
        public void keyReleased(KeyEvent e) {
                if (e.getKeyCode() == e.VK_A) {
                    setXDirection(0);

                } if (e.getKeyCode() == e.VK_D) {

                    setXDirection(0);

                }if (e.getKeyCode() == e.VK_W) {

                    setYDirection(0);

                }if (e.getKeyCode() == e.VK_S) {

                    setYDirection(0);

                }

        }

    public void increaseHealth(){
    	health += 1;
    }
    public void decreaseHealth(){
    	health -= 1;
    }
    public void increaseBulletLife(int x){
    	bulletLife += x;
    }
    public void increaseFireRate(int x){
    	fireRate -= x;
    }
    public void paused(){
    	paused = true;
    }
    public void unpaused(){
    	paused = false;
    }
    
    public void setLevel(boolean level){
    	moveLevels = level;
    }
    
    public boolean nextLevel(){
    	return moveLevels;
    }
    
    public void run(){
        try{
            while(true){
            	if(paused==false){
	                move();
	                Thread.sleep(7);
            	}
                else{Thread.sleep(7);}
            }
        }catch(Exception ex){
        	ex.printStackTrace();
            System.err.println(ex.getMessage());
        }
    }
}

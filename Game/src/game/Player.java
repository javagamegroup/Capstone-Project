package game;

import java.awt.Image;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Timer;
import java.awt.Graphics;
import java.awt.MediaTracker;
import java.awt.Rectangle;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

import javax.swing.ImageIcon;

import Exceptions.InvalidCharacterException;

public class Player extends Actor implements Runnable  {
	
	ImageIcon   theDude, player00, player01, player02, 
				player10, player11, player12, 
				player20, player21, player22, 
				player30, player31, player32,
				player40, player41, player42;
	public static Image[][] playerImage;
	ArrayList<Wall> walls;
	private ArrayList areas;
	Projectile bullet [] = new Projectile [30];
	int numBullets = 0;
	int fireRate = 250;
	Timer timer = new Timer();
	protected Rectangle playerRect;
	double startTime = -1;
	double drawTime = -1;
	double bulletLife;
	int health = 5;
	public boolean paused = false;
	private boolean moveLevels = false;
	Level level = null;
	
	private static int walkWhichWay = 0;
	
    public Player(int x, int y, Level level) {
        super(x, y);
        this.level = level;
        bulletLife = 2500;
        playerInit();
        this.playerRect = getRect(x, y, 32,32);

        this.setRect(playerRect);
    }
    
    public String toString()
    {
    	return "walkWhichWay: " + walkWhichWay;
    }
    public static void playerInit()
    {
    	playerImage = new Image[5][3];
    	
    	java.net.URL imgURL;
		ImageIcon playerIcon;
		String path;
		
		for(int d = 0; d < 3; d++)
		{
			path = "/Resources/theDude.png"; 
			imgURL = Player.class.getResource(path);
			playerIcon = new ImageIcon(imgURL);
			playerImage[0][d] = playerIcon.getImage();
		}
		
		for(int i = 1; i < 5; i++) {
			for(int j = 0; j < 3; j++) {
				path = "/Resources/player" + i + j + ".png"; 
				imgURL = Player.class.getResource(path);
				playerIcon = new ImageIcon(imgURL);
				playerImage[i][j] = playerIcon.getImage();
			}
		}
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
            		if(System.currentTimeMillis() - drawTime  > 500 || drawTime == -1){
            			drawTime = System.currentTimeMillis();
            			try {GamePanel.level = GamePanel.level.setPlayerEntrance('e');} catch (InvalidCharacterException e) {e.printStackTrace();}
            			Main.gp.initialize();
            		}
            	}
            	else if(playerRect.x< 400 && xDirection ==-1)
            	{
            		if(System.currentTimeMillis() - drawTime  > 500 || drawTime == -1){
            			drawTime = System.currentTimeMillis();
            			try {GamePanel.level = GamePanel.level.setPlayerEntrance('w');} catch (InvalidCharacterException e) {e.printStackTrace();}
            			Main.gp.initialize();
            		}
            	}
            	else if(playerRect.y> 200 && yDirection ==1)
            	{
            		if(System.currentTimeMillis() - drawTime  > 500 || drawTime == -1){
            			drawTime = System.currentTimeMillis();
            			try {GamePanel.level = GamePanel.level.setPlayerEntrance('s');} catch (InvalidCharacterException e) {e.printStackTrace();}
            			Main.gp.initialize();
            		}
            	}
            	else if(playerRect.y< 400 && yDirection ==-1)
            	{
            		if(System.currentTimeMillis() - drawTime  > 500 || drawTime == -1){
            			drawTime = System.currentTimeMillis();
            			try {GamePanel.level = GamePanel.level.setPlayerEntrance('n');} catch (InvalidCharacterException e) {e.printStackTrace();}
            			Main.gp.initialize();
            		}
            	}
            }
        }
		if(GamePanel.item != null)
			if (GamePanel.item.itemRect.intersects(playerRect)) {
				GamePanel.item = null;
				GamePanel.gun.increaseDamage(10);
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
    	int i = 0;
    	
    	switch(walkWhichWay)
    	{
    	case 0:
    		if(i != 3)
			{
				g.drawImage(playerImage[0][i], playerRect.x, playerRect.y, null);
				i++;
			}
			else
			{
				i = 0;
				g.drawImage(playerImage[0][i], playerRect.x, playerRect.y, null);
				i++;
			}	
    		break;
    	case 1:
    		if(i != 3)
			{
				g.drawImage(playerImage[1][i], playerRect.x, playerRect.y, null);
				i++;
			}
			else
			{
				i = 0;
				g.drawImage(playerImage[1][i], playerRect.x, playerRect.y, null);
				i++;
			}	
    		break;
    	case 2:
    		if(i != 3)
			{
				g.drawImage(playerImage[2][i], playerRect.x, playerRect.y, null);
				i++;
			}
			else
			{
				i = 0;
				g.drawImage(playerImage[2][i], playerRect.x, playerRect.y, null);
				i++;
			}	
    		break;
    	case 3:
    		if(i != 3)
			{
				g.drawImage(playerImage[3][i], playerRect.x, playerRect.y, null);
				i++;
			}
			else
			{
				i = 0;
				g.drawImage(playerImage[3][i], playerRect.x, playerRect.y, null);
				i++;
			}	
    		break;
    	case 4:
			if(i != 3)
			{
				g.drawImage(playerImage[4][i], playerRect.x, playerRect.y, null);
				i++;
			}
			else
			{
				i = 0;
				g.drawImage(playerImage[4][i], playerRect.x, playerRect.y, null);
				i++;
			}	
			break;
  //  	default:
  //  		System.out.println("walkWhichWay equals something besides 0 - 4");
    	}
    }
    
        public void keyPressed(KeyEvent e) {
            if (e.getKeyCode() == e.VK_A) {
            	walkWhichWay = 3;
                setXDirection(-1);

            } if (e.getKeyCode() == e.VK_D) {
            	walkWhichWay = 1;
                setXDirection(1);

            }if (e.getKeyCode() == e.VK_W) {
            	walkWhichWay = 4;
                setYDirection(-1);

            }if (e.getKeyCode() == e.VK_S) {
            	walkWhichWay = 2;
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
                	walkWhichWay = 0;
                    setXDirection(0);

                } if (e.getKeyCode() == e.VK_D) {
                	walkWhichWay = 0;
                    setXDirection(0);

                }if (e.getKeyCode() == e.VK_W) {
                	walkWhichWay = 0;
                    setYDirection(0);

                }if (e.getKeyCode() == e.VK_S) {
                	walkWhichWay = 0;
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

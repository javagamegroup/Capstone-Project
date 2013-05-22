package game;

import java.awt.Image;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Timer;
import java.awt.Color;
import java.awt.Font;
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
	public static Image lifeIcon;
	public static Image manaIcon;
	public static Image ammoIcon;
	public static Image gunIcon;
	public static Image potionIcon;
	public static Image bootIcon;
	private int speed = 1;
	int numHealthPotions = 5;
	ArrayList<Wall> walls;
	private ArrayList areas;
	Projectile bullet [] = new Projectile [30];
	int numBullets = 0;
	int fireRate = 250;
	Timer timer = new Timer();
	protected Rectangle playerRect;
	double startTime = -1;
	double drawTime = -1;
	double enemyTime = -1;
	double spacebarTime = -1;
	double bulletLife;
	int health = 100;
	int totalHealth = 100;
	public boolean paused = false;
	private boolean moveLevels = false;
	Level level = null;
	
	private static int walkWhichWay = 0;
	
    public Player(int x, int y, Level level) {
        super(x, y);
        this.level = level;
        bulletLife = 2500;
        playerInit();
        URL loc = this.getClass().getResource("/Resources/lifebar_image.png");
        ImageIcon iia = new ImageIcon(loc);
        lifeIcon = iia.getImage();
        loc = this.getClass().getResource("/Resources/manabar_image.png");
        iia = new ImageIcon(loc);
        manaIcon = iia.getImage();
        loc = this.getClass().getResource("/Resources/ammobar_image.png");
        iia = new ImageIcon(loc);
        ammoIcon = iia.getImage();
        loc = this.getClass().getResource("/Resources/gun_image.png");
        iia = new ImageIcon(loc);
        gunIcon = iia.getImage();
        loc = this.getClass().getResource("/Resources/potion_image.png");
        iia = new ImageIcon(loc);
        potionIcon = iia.getImage();
        loc = this.getClass().getResource("/Resources/boot_image.png");
        iia = new ImageIcon(loc);
        bootIcon = iia.getImage();
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
            			try {GamePanel.map.level = GamePanel.map.level.setPlayerEntrance('e');} catch (InvalidCharacterException e) {e.printStackTrace();}
            			Main.gp.initialize();
            		}
            	}
            	else if(playerRect.x< 400 && xDirection ==-1)
            	{
            		if(System.currentTimeMillis() - drawTime  > 500 || drawTime == -1){
            			drawTime = System.currentTimeMillis();
            			try {GamePanel.map.level = GamePanel.map.level.setPlayerEntrance('w');} catch (InvalidCharacterException e) {e.printStackTrace();}
            			Main.gp.initialize();
            		}
            	}
            	else if(playerRect.y> 200 && yDirection ==1)
            	{
            		if(System.currentTimeMillis() - drawTime  > 500 || drawTime == -1){
            			drawTime = System.currentTimeMillis();
            			try {GamePanel.map.level = GamePanel.map.level.setPlayerEntrance('s');} catch (InvalidCharacterException e) {e.printStackTrace();}
            			Main.gp.initialize();
            		}
            	}
            	else if(playerRect.y< 400 && yDirection ==-1)
            	{
            		if(System.currentTimeMillis() - drawTime  > 500 || drawTime == -1){
            			drawTime = System.currentTimeMillis();
            			try {GamePanel.map.level = GamePanel.map.level.setPlayerEntrance('n');} catch (InvalidCharacterException e) {e.printStackTrace();}
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
		for(int i = 0; i<GamePanel.enemies.numEnemies;i++){
			if(GamePanel.enemies.enemies[i]!=null){
				if(playerRect.intersects(GamePanel.enemies.enemies[i].enemyRect)){
	            	if(System.currentTimeMillis() - enemyTime > 1000 || enemyTime == -1){
	                	enemyTime = System.currentTimeMillis();
	                	decreaseHealth();
	            	}
				}
			}
		}

       
    }

    public void move(){
    	collision();
        incX(xDirection);
        incY(yDirection);
        if(health<=0){
 		   GamePanel.player.paused();
 		   GamePanel.enemies.paused();
           GamePanel.gameOver = true;
 		   health = 100;
        }
        if(System.currentTimeMillis() - spacebarTime > 10000)
        	speed = 1;
    }
    
 
    public void setCoord(int x, int y){
    	this.playerRect = getRect(x, y, 32,32);
    	this.setRect(playerRect);
    }
    
    public void draw(Graphics g) {
    	int i = 0;
    	//health
    	g.drawImage(lifeIcon, 2, 2, null);
    	g.drawImage(manaIcon, 2, 22, null);
    	g.drawImage(ammoIcon, 0, 42, null);
    	g.setColor(Color.WHITE);
    	g.drawRect(19, 2, 101, 16);
    	g.setColor(Color.RED);
    	g.fillRect(20, 3, health, 15);
    	g.setColor(Color.GRAY);
    	g.fillRect(health+20, 3, 100-health, 15);
    	g.setColor(Color.BLACK);
    	g.setFont(new Font("TimesRoman", Font.BOLD, 15));
    	if(health>=100)
    		g.drawString(health+"/" + totalHealth, 47, 15);
    	else
    		g.drawString(health+"/" + totalHealth, 50, 15);
    	//mana
    	g.setColor(Color.WHITE);
    	g.drawRect(19, 22, 101, 16);
    	g.setColor(Color.BLUE);
    	g.fillRect(20, 23, 100, 15);
    	//g.setColor(Color.GRAY);
    	//g.fillRect(health+20, 23, 100-health, 15);
    	g.setColor(Color.BLACK);
    	g.setFont(new Font("TimesRoman", Font.BOLD, 15));
    	//if(health>=100)
    		g.drawString(100+"/" + totalHealth, 45, 36);
    	//else
    		//g.drawString(health+"/" + totalHealth, 50, 36);
    	//gun
    	g.setColor(Color.WHITE);
    	g.drawRect(19, 42, 101, 16);
    	g.setColor(Color.YELLOW);
    	g.fillRect(20, 43, 100, 15);
    	//g.setColor(Color.GRAY);
    	//g.fillRect(health+20, 43, 100-health, 15);
    	g.setColor(Color.BLACK);
    	g.setFont(new Font("TimesRoman", Font.BOLD, 15));
    	g.drawString(Character.toString((char) 8734), 62, 55);
    	//q&e bar item
    	g.setColor(Color.WHITE);
    	g.fill3DRect(300, 2, 40, 40, true);
    	g.fill3DRect(306, 45, 13, 13, true);
    	g.fill3DRect(321, 45, 13, 13, true);
    	g.setColor(Color.BLACK);
    	g.drawString("Q", 307, 56);
    	g.drawString("E", 322, 56);
    	g.drawImage(potionIcon, 313, 12, null);
    	g.drawString(Integer.toString(numHealthPotions), 330, 13);
    	//gun bar item
    	g.setColor(Color.WHITE);
    	g.fill3DRect(400, 2, 40, 40, true);
    	g.fill3DRect(400, 45, 12, 13, true);
    	g.fill3DRect(414, 45, 12, 6, true);
    	g.fill3DRect(414, 52, 12, 6, true);
    	g.fill3DRect(428, 45, 12, 13, true);
    	g.setColor(Color.BLACK);
    	g.drawString("<", 401, 57);
    	g.drawString(">", 430, 57);
    	g.setFont(new Font("Windsor BT", Font.PLAIN, 10));
    	g.drawString("^", 417, 53);
    	g.setFont(new Font("Windsor BT", Font.PLAIN, 8));
    	g.drawString("V", 417, 57);
    	g.setFont(new Font("TimesRoman", Font.BOLD, 15));
    	g.drawImage(gunIcon, 412, 12, null);
    	//Space bar item
    	g.setColor(Color.WHITE);
    	g.fill3DRect(500, 2, 40, 40, true);
    	g.fill3DRect(495, 45, 51, 13, true);
    	g.setColor(Color.BLACK);
    	g.drawString("SPACE", 496, 56);
    	g.drawImage(bootIcon, 505, 12, null);
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
                setXDirection(-speed);

            } if (e.getKeyCode() == e.VK_D) {
            	walkWhichWay = 1;
                setXDirection(speed);

            }if (e.getKeyCode() == e.VK_W) {
            	walkWhichWay = 4;
                setYDirection(-speed);

            }if (e.getKeyCode() == e.VK_S) {
            	walkWhichWay = 2;
                setYDirection(speed);
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
            if (e.getKeyCode() == e.VK_Q||e.getKeyCode() == e.VK_E) {
            	if(numHealthPotions>0){
            		if(health<totalHealth)
            			increaseHealth();
            		numHealthPotions --;
            	}
            }
            if (e.getKeyCode() == e.VK_SPACE) {
            	if(System.currentTimeMillis() - spacebarTime > 60000 || spacebarTime == -1){
            	spacebarTime = System.currentTimeMillis();
            	speed = 2;
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
    	health += 10;
    }
    public void decreaseHealth(){
    	health -= 10;
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

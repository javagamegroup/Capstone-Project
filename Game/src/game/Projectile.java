package game;

import java.awt.Image;
import java.net.URL;
import java.util.ArrayList;
import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

import javax.swing.ImageIcon;

public class Projectile extends Actor {
	
	ImageIcon projectile;
	Image projectileImage;
	ArrayList<Wall> walls;
	private ArrayList areas;
	long startBulletTime;
	boolean isAlive;
	double lifeSpan;
	
	protected Rectangle projectileRect;
	
    public Projectile(int x,int y, double life) {
        super(x, y);
        startBulletTime = System.currentTimeMillis();
        isAlive = true;
        lifeSpan = life;
        URL loc = this.getClass().getResource("/Resources/bullet.png");
        projectile = new ImageIcon(loc);
        
        projectileImage = projectile.getImage();
        
        this.projectileRect = getRect(x, y, 8,8);

        this.setRect(projectileRect);
    }

    public void move(){
        incX(xDirection);
        incY(yDirection);
    }
    public boolean getLifeSpan(){
    	if(System.currentTimeMillis() - startBulletTime > lifeSpan)
    		isAlive = false;
    	return isAlive;
    }
    public void draw(Graphics g) {
    	//g.drawImage(projectileImage, projectileRect.x, projectileRect.y, null);
    }
}

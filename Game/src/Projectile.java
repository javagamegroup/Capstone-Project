

import java.awt.Image;
import java.net.URL;
import java.util.ArrayList;
import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

import javax.swing.ImageIcon;

public class Projectile extends Actor implements Runnable  {
	
	ImageIcon projectile;
	Image projectileImage;
	ArrayList<Wall> walls;
	private ArrayList areas;
	
	protected Rectangle projectileRect;
	
    public Projectile(int x,int y) {
        super(x, y);
        URL loc = this.getClass().getResource("Resources/ball.png");
        projectile = new ImageIcon(loc);
        
        projectileImage = projectile.getImage();
        
        this.projectileRect = getRect(x, y, 20,20);

        this.setRect(projectileRect);
    }

    public void move(){
        incX(xDirection);
        incY(yDirection);
    }
    public void draw(Graphics g) {
    	g.drawImage(projectileImage, projectileRect.x, projectileRect.y, null);
    	
    }
    
    public void run(){
        try{
            while(true){
                move();
                Thread.sleep(7);
            }
        }catch(Exception ex){
            System.err.println(ex.getMessage());
        }
    }
}

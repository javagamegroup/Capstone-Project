

import java.awt.Image;
import java.net.URL;
import java.util.ArrayList;
import java.util.Timer;
import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

import javax.swing.ImageIcon;

public class Player extends Actor implements Runnable  {
	
	ImageIcon tad;
	Image playerImage;
	ArrayList<Wall> walls;
	private ArrayList areas;
	Projectile bullet [] = new Projectile [30];
	int numBullets = 0;
	int fireRate = 2;
	Timer timer = new Timer();
	protected Rectangle playerRect;
	
    public Player(int x, int y, ArrayList<Wall> walls2, ArrayList<Area> arrayList) {
        super(x, y);
        int i;
    	for(i=0; i<30; i++){
    		bullet[i] = null;
    	}
        this.walls = walls2;
        this.areas = arrayList;
        URL loc = this.getClass().getResource("Resources/sokoban.png");
        tad = new ImageIcon(loc);
        
        playerImage = tad.getImage();
        
        this.playerRect = getRect(x, y, 19,19);

        this.setRect(playerRect);
    }
    
    public void collision(){
        for (int i = 0; i < walls.size(); i++) {
            Wall wall = (Wall) walls.get(i);
            if (this.playerRect.intersects(wall.objectRect)) {
            	if(playerRect.x> ((Actor) walls.get(32)).x() && xDirection ==1)
            		setCoord(wall.rectx()-20,this.playerRect.y);
            	else if(playerRect.x< ((Actor) walls.get(1)).x() && xDirection ==-1)
            		setCoord(wall.rectx()+20,this.playerRect.y);
            	else if(playerRect.y> ((Actor) walls.get(49)).y() && yDirection ==1)
            		setCoord(this.playerRect.x,wall.recty()-20);
            	else if(playerRect.y< ((Actor) walls.get(35)).y() && yDirection ==-1)
            		setCoord(this.playerRect.x,wall.recty()+20);            	
            }
        }
        
        for (int i = 0; i < areas.size(); i++) {
            Area area = (Area) areas.get(i);
            if (this.playerRect.intersects(area.areaRect)) {
            	if(playerRect.x> ((Actor) areas.get(1)).x() && xDirection ==1)
            		setCoord(((Actor) areas.get(2)).x()+21,((Actor) areas.get(2)).y());
            	else if(playerRect.x< ((Actor) areas.get(0)).x() && xDirection ==-1)
            		setCoord(((Actor) areas.get(3)).x()-21,((Actor) areas.get(3)).y());
            	else if(playerRect.y> ((Actor) areas.get(4)).y() && yDirection ==1)
            		setCoord(((Actor) areas.get(0)).x(),((Actor) areas.get(0)).y()+21);
            	else if(playerRect.y< ((Actor) areas.get(2)).y() && yDirection ==-1)
            		setCoord(((Actor) areas.get(6)).x(),((Actor) areas.get(6)).y()-21);
            }
        }

       
    }

    public void move(){
    	collision();
        incX(xDirection);
        incY(yDirection);
    }
    
 
    public void setCoord(int x, int y){
    	this.playerRect = getRect(x, y, 20,20);
    	this.setRect(playerRect);
    }
    
    private void checkForCollision() {
    	
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
                GamePanel.gun.createBullet('y', -1,playerRect.x, playerRect.y );

            } if (e.getKeyCode() == e.VK_DOWN) {
            	GamePanel.gun.createBullet('y', 1,playerRect.x, playerRect.y );

            }if (e.getKeyCode() == e.VK_LEFT) {
            	GamePanel.gun.createBullet('x', -1,playerRect.x, playerRect.y );

            }if (e.getKeyCode() == e.VK_RIGHT) {
            	GamePanel.gun.createBullet('x', 1,playerRect.x, playerRect.y );
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



import java.awt.Image;
import java.net.URL;
import java.util.ArrayList;
import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

import javax.swing.ImageIcon;

public class Gun extends Actor implements Runnable  {
	
	Projectile bullet [] = new Projectile [30];
	int numBullets = 0;
	int speed = 7;
	ArrayList<Wall> walls;
	EnemyAI enemy;
	private ArrayList areas;
	int bulletLife = 4;
	int bulletDamage;
	
    public Gun(int x,int y, ArrayList<Wall> walls2, ArrayList<Area> arrayList, EnemyAI sub, int damage) {
        super(x, y);
        enemy = sub;
        bulletDamage = damage;
        this.walls = walls2;
        this.areas = arrayList;
        for(int i=0; i<30; i++){
    		bullet[i] = null;
    	}
    }

    public void createBullet(char var, int x, int playerX, int playerY, double life){
    	bullet[numBullets] = new Projectile(playerX, playerY, life);
    	if(var == 'x'){
    		bullet[numBullets].setXDirection(x);
    	}
    	if(var == 'y'){
    		bullet[numBullets].setYDirection(x);
    	}
    	numBullets ++;
    	if(numBullets ==30) numBullets =0;
    }
    
    public void destroyBullet(int x, int num){
    	bullet[num] = null;
    }
    
    public void increaseDamage(int x){
    	bulletDamage += x;
    }
    
    public void bulletCollision(){
    	for(int i =0; i<30; i++){
    		if(bullet[i] != null)
    			if(bullet[i].getLifeSpan() == false){
    				destroyBullet(100, i);
    			}
    	}
    	
    	for (int i = 0; i < walls.size(); i++) {
            Wall wall = (Wall) walls.get(i);
            for(int j =0; j<30; j++){
    			if(bullet[j] != null)
    				if (bullet[j].projectileRect.intersects(wall.objectRect)) {
    					destroyBullet(100, j);
    				}
            }
        }
    	for (int i = 0; i < areas.size(); i++) {
            Area area = (Area) areas.get(i);
            for(int j =0; j<30; j++){
    			if(bullet[j] != null)
    				if (bullet[j].projectileRect.intersects(area.areaRect)) {
    					destroyBullet(100, j);
    				}
            }
        }
    	for(int j =0; j<30; j++){
			if(bullet[j] != null)
				if(enemy.enemyRect!=null)
				if (bullet[j].projectileRect.intersects(enemy.enemyRect)) {
					destroyBullet(100, j);
					enemy.decreaseHealth(bulletDamage);
				}
        }
    }
    
    public void draw(Graphics g) {
    	for(int i = 0; i<30; i++){
    		if(bullet[i] != null){
    			g.drawImage(bullet[i].projectileImage, bullet[i].projectileRect.x, bullet[i].projectileRect.y, null);
    		}
    	}
    }
    
    public void run(){
        try{
            while(true){
    				for(int i =0; i<30; i++){
    				if(bullet[i] != null)
    					bullet[i].move();
    				}
    			bulletCollision();
                Thread.sleep(speed);
            }
        }catch(Exception ex){
            System.err.println(ex.getMessage());
        }
    }
}

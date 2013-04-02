package javagame;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

import javax.swing.ImageIcon;
import javax.swing.JFrame;

public class JavaGame extends JFrame implements Runnable{
	
	int x,y, xDirection, yDirection;
	Image dbImage;
	Graphics dbg;
	Image tad;
	
	public void run(){
		try{
			while(true){
				
				move();
				
				Thread.sleep(5);
			}
			
		} catch(Exception e){
			System.out.println("Error");
		}
	}
	
	Font font = new Font("Arial", Font.BOLD | Font.ITALIC, 30);
	public void move() {
		x += xDirection;
		y += yDirection;
		
		if(x <= 0)
			x = 0;
		if(x >= 400)
			x = 400;
		if(y <= 0)
			y = 0;
		if(y >= 400)
			y = 400;
	}
	
	public void setXDir(int xdir) {
		xDirection = xdir;
	}
	
	public void setYDir(int ydir) {
		yDirection = ydir;
	}
	public class actionListener extends KeyAdapter{
		public void keyPressed(KeyEvent e){
			
			int keyCode = e.getKeyCode();
			
			
			if(keyCode == e.VK_LEFT || keyCode == e.VK_A){ setXDir(-1);	}
			if(keyCode == e.VK_RIGHT || keyCode == e.VK_D){	setXDir(+1); }
			if(keyCode == e.VK_UP || keyCode == e.VK_W){ setYDir(-1);	 }
			if(keyCode == e.VK_DOWN || keyCode == e.VK_S){ setYDir(+1);	 }
			
		}
		public void keyReleased(KeyEvent e){
			int keyCode = e.getKeyCode();
			
			if(keyCode == e.VK_LEFT || keyCode == e.VK_A){ setXDir(0);	}
			if(keyCode == e.VK_RIGHT || keyCode == e.VK_D){	setXDir(0); }
			if(keyCode == e.VK_UP || keyCode == e.VK_W){ setYDir(0);	 }
			if(keyCode == e.VK_DOWN || keyCode == e.VK_S){ setYDir(0);	 }
		}
	}

	public JavaGame() {
		
		//Load images
		ImageIcon i = new ImageIcon("H:/College classes and material/Capstone 1/Begin/src/javagame/tad.jpg");
		this.tad = i.getImage();
		
		//Game properties 
		addKeyListener(new actionListener());
		setTitle("Java Game");
		setSize(500, 500);
		setResizable(false);
		setVisible(true);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBackground(Color.cyan);
		this.x = 150;
		this.y = 150;
	}
	
	public void paint(Graphics g){
		dbImage = createImage(getWidth(), getHeight());
		dbg = dbImage.getGraphics();
		
		paintComponent(dbg);
		g.drawImage(dbImage, 0, 0, this);
		
	}
	public void paintComponent(Graphics g){
		
		g.setFont(font);
		g.drawImage(tad, x, y, this);
		
		repaint();
	}
	
	public static void main(String[] args) {
		JavaGame jg = new JavaGame();
		//Threads
		Thread t1 = new Thread(jg);
		t1.start();
	}
}

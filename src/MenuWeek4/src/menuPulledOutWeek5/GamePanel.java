package menuPulledOutWeek5;

import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import javax.swing.JPanel;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Rectangle;

public class GamePanel extends JPanel implements Runnable{
	
	
	Thread t1 = new Thread(enemyAI);
    Thread t2 = new Thread(passiveAI);
    
	boolean hardDifficulty = false;

    boolean achievementsStarted = false;
    
    boolean startHover;
    boolean quitHover;
    boolean difficultyHover; 
    boolean achievementsHover; 
    boolean backHover;
	
	static Rectangle player = new Rectangle(30, 50, 25, 25);
    static Rectangle enemy = new Rectangle(275, 175, 16, 16);
    static Rectangle friendly = new Rectangle(300, 200, 16, 16);
    
    //Menu Buttons
    Rectangle startButton = new Rectangle(200, 150, 125, 25);
    Rectangle quitButton = new Rectangle(200, 200, 125, 25);
    Rectangle difficultyButton = new Rectangle(200, 250, 125,25);
    Rectangle achievementButton = new Rectangle(200, 300, 125, 25);
    Rectangle backButton = new Rectangle(25, 50, 75, 25);
    
    
    static EnemyAI enemyAI = new EnemyAI(enemy, player);
    static PassiveAI passiveAI = new PassiveAI(friendly);
	
	//Double buffering
	private Image dbImage;
	private Graphics dbg;
	//JPanel variables
	static final int GWIDTH = 800, GHEIGHT = 460;
	static final Dimension gameDim = new Dimension(GWIDTH, GHEIGHT);
	// Game Variables
	private Thread game;
	private volatile boolean running = false;
	int xDirection, yDirection;
	
	public GamePanel() {
		
		setPreferredSize(gameDim);
		setBackground(Color.WHITE);
		setFocusable(true);
		requestFocus();
		
		
		//Handle all key inputs from user
		addKeyListener(new KeyAdapter(){
			@Override
			public void keyPressed(KeyEvent e) {
				int keyCode = e.getKeyCode();
	            if(keyCode == e.VK_LEFT || keyCode == e.VK_A){
	                setXDirection(-1);
	            }
	            if(keyCode == e.VK_RIGHT || keyCode == e.VK_D){
	                setXDirection(1);
	            }
	            if(keyCode == e.VK_UP || keyCode == e.VK_W){
	                setYDirection(-1);
	            }
	            if(keyCode == e.VK_DOWN || keyCode == e.VK_S){
	                setYDirection(1);
	            }
			}
			@Override
			public void keyReleased(KeyEvent e){
				 int keyCode = e.getKeyCode();
		            if(keyCode == e.VK_LEFT || keyCode == e.VK_A){
		                setXDirection(0);
		            }
		            if(keyCode == e.VK_RIGHT || keyCode == e.VK_D){
		                setXDirection(0);
		            }
		            if(keyCode == e.VK_UP || keyCode == e.VK_W){
		                setYDirection(0);
		            }
		            if(keyCode == e.VK_DOWN || keyCode == e.VK_S){
		                setYDirection(0);
		            }
			}
			@Override
			public void keyTyped(KeyEvent e){
				
			}
		});
	}
	
	public void run() {
		
		try{
			while(running){
				
				gameUpdate();
				gameRender();
				paintScreen();
	        
				move();
				Thread.sleep(5);
			}
		}catch(Exception e){
            System.err.println(e.getMessage());
		 }
	}
	
	public void move(){
        player.x += xDirection;
        player.y += yDirection;
    }
	
	
	
	private void gameUpdate() {
		if(running && game != null) {
			//update game state
		}
	}
	
	private void gameRender() {
		if(dbImage == null) { // Create the buffer
			dbImage = createImage(GWIDTH, GHEIGHT);
			if(dbImage == null) {
				System.err.println("dbIimage is still null!");
				return;
			}else{
				dbg = dbImage.getGraphics();
			}
		}
		//Clear the screen
		dbg.setColor(Color.WHITE);
		dbg.fillRect(0, 0, GWIDTH, GHEIGHT);
		//Draw Game elements
		draw(dbg);
	}
	
	/**
	 * draw all game contents in this method
	 * @param g
	 */
	public void draw(Graphics g){
		
		if(!running) {
    	
			if(!achievementsStarted) {
	    		//Menu
			    g.setFont(new Font("Arial", Font.BOLD, 26));
			    g.setColor(Color.WHITE);
			    g.drawString("Pong Game!", 175, 75);
			    
			    // difficulty button
			    if(!difficultyHover)
			    	g.setColor(Color.cyan);
			    else
			    	g.setColor(Color.pink);
			    g.fillRect(difficultyButton.x, difficultyButton.y, difficultyButton.width, difficultyButton.height);
			    g.setFont(new Font("Arial", Font.BOLD, 12));
			    g.setColor(Color.gray);
			    g.drawString("Difficulty: ", difficultyButton.x+5, difficultyButton.y+17);
			   
			    if(!hardDifficulty) {
			    	g.setColor(Color.BLUE);
			    	g.drawString("Easy", difficultyButton.x+65, difficultyButton.y+17);
			    }
			    else {
			    	g.setColor(Color.RED);
			    	g.drawString("Hard", difficultyButton.x+70, difficultyButton.y+17);
			    }
			    
			    // start button
			    if(!startHover)
			    	g.setColor(Color.CYAN);
			    else
			    	g.setColor(Color.pink);
			    g.fillRect(startButton.x, startButton.y, startButton.width, startButton.height);
			    g.setFont(new Font("Arial", Font.BOLD, 12));
			    g.setColor(Color.GRAY);
			    g.drawString("Start Game", startButton.x+30, startButton.y+17);
			    
			    // quit button 
			    if(!quitHover)
			    	g.setColor(Color.CYAN);
			    else
			    	g.setColor(Color.pink);
			    g.fillRect(quitButton.x, quitButton.y, quitButton.width, quitButton.height);
			    g.setColor(Color.GRAY);
			    g.drawString("Quit Game", quitButton.x+30, quitButton.y+17);
			    
			 // achievements button
			    if(!achievementsHover)
			    	g.setColor(Color.CYAN);
			    else
			    	g.setColor(Color.pink);
			    g.fillRect(achievementButton.x, achievementButton.y, achievementButton.width, achievementButton.height);
			    g.setColor(Color.GRAY);
			    g.drawString("Achievements", achievementButton.x+20, achievementButton.y+17);
    		}
    		else {
    			// Achievement drawings
    			g.setColor(Color.DARK_GRAY);
    			g.fill3DRect(0, 0, GWIDTH, GHEIGHT, true);
    			g.setColor(Color.black);
    			g.setFont(new Font("Arial", Font.BOLD, 28));
    			g.drawString("ACHIEVEMENTS", 150, 75);	
    			g.drawOval(50, 125, 50, 50);
    			g.drawArc(50, 125, 45, 10, 45, 30);
    			g.setFont(new Font("Arial", Font.BOLD, 20));
    			g.drawString("Completed Level One            10pt", 125, 150);
    			if(!backHover)
    				g.setColor(Color.cyan);
    			else
    				g.setColor(Color.pink);
    			g.fillRect(backButton.x, backButton.y, backButton.width, backButton.height);
    			g.setFont(new Font("Arial", Font.BOLD, 12));
    			g.setColor(Color.black);
    			g.drawString("Back", backButton.x+15, backButton.y+17);
		    	
    		} // if achievement list started
		
		} // if game started
    	else{
        //Game drawings
    	
        g.setColor(Color.RED);
        g.fillRect(player.x, player.y, player.width, player.height);
        enemyAI.draw(g);
        passiveAI.draw(g);
        
    } // if game not started
        paintScreen();
	}
	
	private void paintScreen(){
		Graphics g;
		try{
			g = this.getGraphics();
			if(dbImage != null && g != null) {
				g.drawImage(dbImage, 0, 0, null);
			}
			Toolkit.getDefaultToolkit().sync(); //For some Operating Systems
			g.dispose();
		}catch (Exception e){
			System.err.println(e);
		}
	}
	
	public void addNotify() {
		super.addNotify();
		startGame();
	}
	private void startGame() {
		if(game == null || !running) {
			game = new Thread(this);
			game.start();
			running = true;
	    	t1.start();
	    	t2.start();
		}
	}
	
	public void quitGame() {
		if(running) {
			running = false;
			System.exit(0);
		}
	}
	
	private void log(String s) {
		System.out.println(s);
	}
	
    public void setYDirection(int ydir){
        yDirection = ydir;
    }
    public void setXDirection(int xdir){
        xDirection = xdir;
    }

    public class MouseHandler extends MouseAdapter {
       	
       	
           @Override
           public void mouseMoved(MouseEvent e){
           	int mx = e.getX(); 
               int my = e.getY();
               
               if(mx > startButton.x && mx < startButton.x+startButton.width &&
               		my > startButton.y && my < startButton.y+startButton.height)  
               	startHover = true;
               else
               	startHover = false;
               
               if(mx > quitButton.x && mx < quitButton.x+quitButton.width &&
               		my > quitButton.y && my < quitButton.y+quitButton.height) 
               	quitHover = true;
               else
               	quitHover = false;
               
               if(mx > difficultyButton.x && mx < difficultyButton.x+difficultyButton.width &&
               		my > difficultyButton.y && my < difficultyButton.y+difficultyButton.height)  
               	difficultyHover = true;
               else
               	difficultyHover = false;
               
               if(mx > achievementButton.x && mx < achievementButton.x+achievementButton.width &&
               		my > achievementButton.y && my < achievementButton.y+achievementButton.height)  
               	achievementsHover = true;
               else
               	achievementsHover = false;
               
               if(mx > backButton.x && mx < backButton.x+backButton.width &&
               		my > backButton.y && my < backButton.y+backButton.height)  
               	backHover = true;
               else
               	backHover = false;
               
           }
           @Override
           public void mousePressed(MouseEvent e){
               int mx = e.getX(); 
               int my = e.getY();
               
               if(mx > startButton.x && mx < startButton.x+startButton.width &&
               		my > startButton.y && my < startButton.y+startButton.height)
               	startGame();
               
               if(mx > quitButton.x && mx < quitButton.x+quitButton.width &&
               		my > quitButton.y && my < quitButton.y+quitButton.height)
               	quitGame();
               
               if(mx > difficultyButton.x && mx < difficultyButton.x+difficultyButton.width &&
               		my > difficultyButton.y && my < difficultyButton.y+difficultyButton.height)
               {
               	if(!hardDifficulty) {
               		enemyAI.setDifficulty(5);
               		hardDifficulty = true;
               	}
               	else {
               		enemyAI.setDifficulty(10);
               		hardDifficulty = false;
               		
               	}
               		
               }
               
               if(mx > backButton.x && mx < backButton.x+backButton.width &&
               		my > backButton.y && my < backButton.y+backButton.height)
               		achievementsStarted = false;
               	
               
               	
               if(mx > achievementButton.x && mx < achievementButton.x+achievementButton.width &&
                   	my > achievementButton.y && my < achievementButton.y+achievementButton.height)
               		achievementsStarted = true;
               		
           }
    
      	}
    
   }
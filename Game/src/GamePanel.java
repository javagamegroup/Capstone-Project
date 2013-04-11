

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.JPanel;

import java.awt.*;

public class GamePanel extends JPanel implements Runnable{ 

	/**
	 * 
	 */
	private static final long serialVersionUID = -7708339559886723406L;
	/**
	 *  JPanel variables
	 *  int GWIDTH, GHEIGHT
	 *  Dimension gameDim object 
	 *  	@param GWIDTH, GHEIGHT
	 *  	
	 */
	static final int GWIDTH = 800, GHEIGHT = 600;
	static final Dimension gameDim = new Dimension(GWIDTH, GHEIGHT);
	/**
	 * Game Variables
	 */
	private Thread game;
	private volatile boolean running = false;
	private long period = 6*1000000; // miliseconds -> nano
	private static final int DELAYS_BEFORE_YIELD = 10;
	
	// Double buffering
    private Image dbImage;
    private Graphics dbg;
    
    /**
     * Game Objects
     *	world and characters
     */
    static Player player = new Player(200, 200, World.walls, World.getAreas());
    static EnemyAI enemy = new EnemyAI(70, 70, World.walls, World.getAreas(), player);
    static Gun gun = new Gun(200, 200, World.walls, World.getAreas(), enemy);
    World world;
    Thread p1 = new Thread(player);
    Thread npc = new Thread(enemy);
    Thread weapon = new Thread(gun);
    
    boolean gameStarted = false;
    boolean achievementsStarted = false;
    boolean hardDifficulty = false;
    boolean startHover;
    boolean quitHover;
    boolean difficultyHover; 
    boolean achievementsHover; 
    boolean backHover;
    
    // button flags
    boolean startButtonFlag = false;
    boolean quitButtonFlag = false;
    boolean achievementButtonFlag = false;
    boolean difficultyButtonFlag = false;
    boolean ESCkeyPressed = false;
    
    //Menu Buttons
    Rectangle startButton = new Rectangle(350, 150, GWIDTH/4, GHEIGHT/12);
    Rectangle quitButton = new Rectangle(350, 450, GWIDTH/4, GHEIGHT/12);
    Rectangle difficultyButton = new Rectangle(350, 250, GWIDTH/4, GHEIGHT/12);
    Rectangle achievementButton = new Rectangle(350, 350, GWIDTH/4, GHEIGHT/12);
    Rectangle backButton = new Rectangle(25, 50, 75, 25);
	
    Color fontColor, backColor, buttonColor;
    String gameTitle;
    
    public GamePanel(String gameTitle, Color fontColor, Color backColor, Color buttonColor, boolean sbf, boolean qbf, boolean abf, boolean dbf) 
    {
    	this.gameTitle = gameTitle;
		
		this.startButtonFlag = sbf;
		this.quitButtonFlag = qbf;
		this.achievementButtonFlag = abf;
		this.difficultyButtonFlag = dbf;
		
		this.fontColor = fontColor;
		this.backColor = backColor;
		this.buttonColor = buttonColor;
    	
    	this.addKeyListener(new KeyHandler());
    	this.addMouseListener(new MouseHandler());
        this.addMouseMotionListener(new MouseHandler());
        world = new World(player, enemy);
        
        setPreferredSize(gameDim);
        setBackground(Color.BLACK);
        setFocusable(true);
        requestFocusInWindow();
        
        // Handle all key inputs from user
        //addKeyListener(new TAdapter());
    }

  
    public void run() {
		long beforeTime, afterTime, diff, sleepTime, overSleepTime = 0;
		int delays = 0;
		while(running){
			beforeTime = System.nanoTime();
			
			
			gameUpdate();
			gameRender();
			paintScreen();
			
			afterTime = System.nanoTime();
			diff = afterTime - beforeTime;
			sleepTime = (period - diff) - overSleepTime;
			// If the sleep time is between 0 and the period, we can happily sleep
			if(sleepTime < period && sleepTime > 0) {
				try {
					game.sleep(sleepTime / 1000000L);
					overSleepTime = 0;
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			// The diff was greater than the period
			else if(diff > period) {
				overSleepTime = diff - period;
			}
			// Accumulate the amount of delays, and eventually yeild. 
			else if(++delays >= DELAYS_BEFORE_YIELD){
				game.yield();
				delays = 0; 
				overSleepTime = 0;
			}
			// The loop took less time than expected, but we need to make up 
			// for overSleepTime
			else{
				overSleepTime = 0;
			
			}
			// Print out game stats
			log(
					"beforeTime: 	    " + beforeTime + "\n" +
					"afterTime:			" + afterTime + "\n" +
					"diff:				" + diff + "\n"	 +
					"sleepTime:			" + sleepTime / 1000000L + "\n" +
					"overSleepTime:		" + overSleepTime / 1000000L + "\n" +
					"delays:			" + delays + "\n"	
				
			);
			
		}
	}
    
    private void gameUpdate() {
		if(running && game != null) {
			//enemy.update();
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
		dbg.setColor(Color.BLACK);
		dbg.fillRect(0, 0, GWIDTH, GHEIGHT);
		//Draw Game elements
		draw(dbg);
	}
    
    /**
	 * draw all game contents in this method
	 * @param g
	 */
	public void draw(Graphics g){
		
		if(!gameStarted) {
    		
    		if(!achievementsStarted) {
	    		//Menu
			    g.setFont(new Font("Arial", Font.BOLD, 26));
			    g.setColor(Color.WHITE);
			    g.drawString(gameTitle, 275, 75);
			    
			    // difficulty button
			    if(difficultyButtonFlag = true) {
				    if(!difficultyHover)
				    	g.setColor(buttonColor);
				    else
				    	g.setColor(Color.pink);
				    g.fillRect(difficultyButton.x, difficultyButton.y, GWIDTH/4, GHEIGHT/14);
				    g.setFont(new Font("Arial", Font.BOLD, 12));
				    g.setColor(Color.gray);
				    g.drawString("Difficulty: ", difficultyButton.x+20, difficultyButton.y+25);
				   
				    if(!hardDifficulty) {
				    	g.setColor(Color.BLUE);
				    	g.drawString("Easy", difficultyButton.x+100, difficultyButton.y+25);
				    }
				    else {
				    	g.setColor(Color.RED);
				    	g.drawString("Hard", difficultyButton.x+105, difficultyButton.y+25);
				    }
			    }
			    
			    // start button
			    if(startButtonFlag = true){
				    if(!startHover)
				    	g.setColor(buttonColor);
				    else
				    	g.setColor(Color.pink);
				    g.fillRect(startButton.x, startButton.y, GWIDTH/4, GHEIGHT/14);
				    g.setFont(new Font("Arial", Font.BOLD, 12));
				    g.setColor(Color.GRAY);
				    g.drawString("Start Game", startButton.x+60, startButton.y+25);
				    
				    // quit button 
				    if(!quitHover)
				    	g.setColor(buttonColor);
				    else
				    	g.setColor(Color.pink);
				    g.fillRect(quitButton.x, quitButton.y, GWIDTH/4, GHEIGHT/14);
				    g.setColor(Color.GRAY);
				    g.drawString("Quit Game", quitButton.x+60, quitButton.y+25);
			    }
			    
			    // achievements button
			    if(achievementButtonFlag = true) {
				    if(!achievementsHover)
				    	g.setColor(buttonColor);
				    else
				    	g.setColor(Color.pink);
				    g.fillRect(achievementButton.x, achievementButton.y, GWIDTH/4, GHEIGHT/14);
				    g.setColor(Color.GRAY);
				    g.drawString("Achievements", achievementButton.x+50, achievementButton.y+25);
			    }
		    
    		}
    		else {
    			// Achievement drawings
    			g.setColor(backColor);
    			g.fill3DRect(0, 0, GWIDTH, GHEIGHT, true);
    			g.setColor(Color.black);
    			g.setFont(new Font("Arial", Font.BOLD, 28));
    			g.drawString("ACHIEVEMENTS", 150, 75);	
    			g.drawOval(50, 125, 50, 50);
    			g.drawArc(50, 125, 45, 10, 45, 30);
    			g.setFont(new Font("Arial", Font.BOLD, 20));
    			g.drawString("Completed Level One            10pt", 125, 150);
    			if(!backHover)
    				g.setColor(buttonColor);
    			else
    				g.setColor(Color.pink);
    			g.fillRect(backButton.x, backButton.y, backButton.width, backButton.height);
    			g.setFont(new Font("Arial", Font.BOLD, 12));
    			g.setColor(Color.black);
    			g.drawString("Back", backButton.x+15, backButton.y+17);
    		}
    	} // if game started
    	else{
    		//Game drawings
    		world.buildWorld(g);
    		player.draw(g);
    		if(enemy.health > 0){
    			enemy.draw(g);
			
    		}
    		else
    			npc.interrupt();
    		gun.draw(g);
		
		}
	
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
		if(game == null || gameStarted) {
			game = new Thread(this);
			p1.start();
			weapon.start();
			npc.start();
			game.start();
			running = true;
		}
	}
    
    public void stopGame() {
    	if(running) {
    		running = false;
    		System.exit(0);
    	}
    }
    
    
    
////////EVENT LISTENER CLASSES/////////
    public class KeyHandler extends KeyAdapter {
        @Override
        public void keyPressed(KeyEvent e){
            player.keyPressed(e);
            
            if(e.getKeyCode() == KeyEvent.VK_ESCAPE){
                gameStarted = false;
            }
        }
        @Override
        public void keyReleased(KeyEvent e){
            player.keyReleased(e);
        }
    }

    public class MouseHandler extends MouseAdapter {
    	
    	
        @Override
        public void mouseMoved(MouseEvent e){
        	int mx = e.getX(); 
            int my = e.getY();
            
            if(startButtonFlag = true) {
	            if(mx > startButton.x && mx < startButton.x+startButton.width &&
	            		my > startButton.y && my < startButton.y+startButton.height)  
	            	startHover = true;
	            else
	            	startHover = false;
            }    
	            
            if(quitButtonFlag = true) {    
	            if(mx > quitButton.x && mx < quitButton.x+quitButton.width &&
	            		my > quitButton.y && my < quitButton.y+quitButton.height) 
	            	quitHover = true;
	            else
	            	quitHover = false;
            }
            
	        if(difficultyButtonFlag = true) {   
	            if(mx > difficultyButton.x && mx < difficultyButton.x+difficultyButton.width &&
	            		my > difficultyButton.y && my < difficultyButton.y+difficultyButton.height)  
	            	difficultyHover = true;
	            else
	            	difficultyHover = false;
	        }
	        
	        if(achievementButtonFlag = true) {
	            if(mx > achievementButton.x && mx < achievementButton.x+achievementButton.width &&
	            		my > achievementButton.y && my < achievementButton.y+achievementButton.height)  
	            	achievementsHover = true;
	            else
	            	achievementsHover = false;
	        }    
	            
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
            	gameStarted = true;
            
            if(mx > quitButton.x && mx < quitButton.x+quitButton.width &&
            		my > quitButton.y && my < quitButton.y+quitButton.height)
            	stopGame();
            
            if(mx > difficultyButton.x && mx < difficultyButton.x+difficultyButton.width &&
            		my > difficultyButton.y && my < difficultyButton.y+difficultyButton.height)
            {
            	if(!hardDifficulty) {
            		enemy.setDifficulty(8);
            		hardDifficulty = true;
            	}
            	else {
            		enemy.setDifficulty(20);
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
    ///////END EVENT LISTENER CLASSES/////

    
    private void log(String s) {
		System.out.println(s);
	}



} // this is a comment from jian
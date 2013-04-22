

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.Component.*;

import javax.swing.ImageIcon;
import javax.swing.JPanel;

import java.awt.*;
import java.io.PrintStream;
import java.net.URL;
import java.util.Random;

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
    private Image dbImage, backGround;
    private Image[] sokuban = new Image[6];
    private Graphics dbg;
    
    ImageIcon backIcon, playerDefaultIcon, playerRightGunIcon, playerLeftGunIcon;
    ImageIcon playerRightWalkGunIcon, playerLeftWalkGunIcon;
    
    private Thread animate;
    private MediaTracker tracker;
    private SpriteVector sv;
    private int dalay = 83; // 12 fps
    private Random rand = new Random(System.currentTimeMillis());
    
    private static PrintStream ps;
    
    /**
     * Game Objects
     *	world and characters
     */
    static Player player = new Player(200, 200, World.walls, World.getAreas());
    static EnemyAI enemy = new EnemyAI(70, 70, World.walls, World.getAreas(), player);
    static Gun gun = new Gun(200, 200, World.walls, World.getAreas(), enemy, 2);
    World world;
    Event event = new Event(enemy.enemyRect);
    Thread p1 = new Thread(player);
    Thread npc = new Thread(enemy);
    Thread weapon = new Thread(gun);
    Thread event1 = new Thread(event);
    
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
  //  boolean achievementNoticeClicked = false;
    
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
		
    //	this.ps = ps;
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

    /**
     * loads all the images and registers them with the media tracker.
     * Tracking the images is necessary because we want to wait until all the images
     * have been loaded before we start the animation.
     */
    public void init() {
    	
    	// Image locations
    	URL back = this.getClass().getResource("Resources/Background_example_1.png");
    	URL playerAirtankDefault = this.getClass().getResource("Resources/player_V1_default_position_airtank.png");
    	URL playerRightGun = this.getClass().getResource("Resources/player_V1_rightfacing_position_gunout.png");
    	URL playerLeftGun = this.getClass().getResource("Resources/player_V1_leftfacing_position_gunout.png");
    	URL playerRightWalkGun = this.getClass().getResource("Resources/player_V1_rightfacing_walking_position_gunout.png");
    	URL playerLeftWalkGun = this.getClass().getResource("Resources/player_V1_leftfacing_walking_position_gunout.png");
    	
    	
    	backIcon = new ImageIcon(back);
    	playerDefaultIcon = new ImageIcon(playerAirtankDefault);
    	playerRightGunIcon = new ImageIcon(playerRightGun);
    	playerLeftGunIcon = new ImageIcon(playerLeftGun);
    	playerRightWalkGunIcon = new ImageIcon(playerRightWalkGun);
    	playerLeftWalkGunIcon = new ImageIcon(playerLeftWalkGun);
    	
        
        // Load and track the images
    	tracker = new MediaTracker(this);
    	
    	backGround = backIcon.getImage();
    	tracker.addImage(backGround, 0);
    	
    	sokuban[0] = playerDefaultIcon.getImage();
    	tracker.addImage(sokuban[0], 0);
    	sokuban[1] = playerRightGunIcon.getImage();
    	tracker.addImage(sokuban[1], 0);
    	sokuban[2] = playerLeftGunIcon.getImage();
    	tracker.addImage(sokuban[2], 0);
    	sokuban[3] = playerRightWalkGunIcon.getImage();
    	tracker.addImage(sokuban[3], 0);
    	sokuban[4] = playerLeftWalkGunIcon.getImage();
    	tracker.addImage(sokuban[4], 0);
    	
    	
    }
    
    public void run() {
		long beforeTime, afterTime, diff, sleepTime, overSleepTime = 0;
		int delays = 0;
		while(running){
			beforeTime = System.nanoTime();
			
			init();
			
			try{
				tracker.waitForID(0, 3000);
			}
			catch (InterruptedException e) { return;}
			
			gameUpdate();
			sv.update();
			gameRender();
			paintScreen();
			
			afterTime = System.nanoTime();
			diff = afterTime - beforeTime;
			sleepTime = (period - diff) - overSleepTime;
			// If the sleep time is between 0 and the period, we can happily sleep
			if(sleepTime < period && sleepTime > 0) {
				try {
					game.sleep(sleepTime / 1000000L);
					p1.sleep(sleepTime / 1000000L);
					event1.sleep(sleepTime / 1000000L);
					weapon.sleep(sleepTime / 1000000L);
					npc.sleep(sleepTime / 1000000L);
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
				event1.yield();
				p1.yield();
				npc.yield();
				weapon.yield();
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
			sv = new SpriteVector(new ImageBackground(this, backGround));
			for(int i = 0; i < 1; i++)
			{
				Point pos = sv.getEmptyPosition(new Dimension(
						sokuban[0].getWidth(this), sokuban[0].getHeight(this)));
				sv.add(createSprite(pos, i % 6));
			}
		}
	}
    
    /**
     * Handles creating a single sprite.
     * Takes below parameters then calculates a random velocity for the sprite using the rand member variable.
     * Each velocity component for the sprite (X and Y) is given a random value between -5 and 5. 
     * The sprite is given a Z-order value of 0.
     * 
     * @param pos - determines the sprite's initial position.
     * @param i - specifies which sprite image to use. 
     * @return
     */
    private Sprite createSprite(Point pos, int i) {
    	return new Sprite(this, sokuban[i], pos, new Point(rand.nextInt()
    			% 5, rand.nextInt() % 5), 0, Sprite.BA_STOP);
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
		draw(dbg, dbImage);
	}
    
    /**
	 * draw all game contents in this method
	 * @param g
	 */
	public void draw(Graphics g, Image offImage){
		
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
    		sv.draw(g);
    		player.draw(g);
    		event.draw(g);
    		if(enemy.health > 0){
    			enemy.draw(g);
    		}
    		else
    			npc.interrupt();
    		gun.draw(g);
		
    		g.drawImage(offImage, 0, 0, null);
		}
	
	}
    
    private void paintScreen(){
		Graphics g;
		try{
			g = this.getGraphics();
			if(dbImage != null && g != null && ((tracker.statusID(0,true) & MediaTracker.COMPLETE) != 0)) {
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
			event1.start();
			weapon.start();
			npc.start();
			game.start();
			running = true;
		}
	}
    
    public void stopGame() {
    	if(running) {
    		running = false;
    		p1.interrupt();
    		p1 = null;
    		event1.interrupt();
    		event1 = null;
    		weapon.interrupt();
    		weapon = null;
    		npc.interrupt();
    		npc = null;
    		game.interrupt();
    		game = null;
    		
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
            Rectangle achNotice = (event.getANRect());
            boolean isAlive = (enemy.getEnemyStatus());
            
            if(mx > startButton.x && mx < startButton.x+startButton.width &&
            		my > startButton.y && my < startButton.y+startButton.height)
            	gameStarted = true;
            
            if(!isAlive){
            	 if(mx > achNotice.x && mx < achNotice.x+achNotice.width &&
 	            		my > achNotice.y && my < achNotice.y+achNotice.height)
 	            	event.setEnemyStatus(false);
            }
            
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


}

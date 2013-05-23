package game;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Point;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.ImageIcon;
import javax.swing.JPanel;

import java.awt.*;
import java.io.Serializable;
import java.net.URL;
import java.util.Random;
import collectionOfSprites.*;

public class GamePanel extends JPanel implements Runnable, Serializable{ 

	private static final long serialVersionUID = -7708339559886723406L;
	static final int GWIDTH = 832, GHEIGHT = 600;
	static final Dimension gameDim = new Dimension(GWIDTH, GHEIGHT);
	/**
	 * Game Variables
	 */
	private Thread game;
	private volatile boolean running = false;
	private long period = 6*1000000; // miliseconds -> nano
	private static final int DELAYS_BEFORE_YIELD = 10;
    private Random rand = new Random();
    private double gameOverTime = -1;
	
	// Double buffering
    private Image dbImage, Imagebackground;
    private Graphics dbg;
    
    // 2D sprite arrays
    private Image[][] turtleDuck = new Image[4][2];
    private Image[][] babyTurtleDuck = new Image[4][2];
    private ImageIcon backIcon;
    
    // position to draw sprite
    private Point pos;
    
    // number of sprites you want
    private int numTDuckSprites = 0;
    private int numBTDuckSprites = 0;
    
    // MediaTracker
    private MediaTracker tracker;
   
    
    // Vars for Achievements
    static Achievements getAchieves = new Achievements();
	static String [] achivements = null;
	static String [] earnedAchivements = null;
    
    /**
     * Game Objects
     *	world and characters
     */
	static Map map;
	static World world;
	static Items item = new Items();
	static Obstacles obs = new Obstacles();
//    static IncreasedBulletPickUp item = null;
    static Player player = new Player(200, 200,map.level);
    static Enemies enemies = new Enemies(70, 70,player);
    static EnemyAI enemy = null;
//    static EnemyAI enemy = new EnemyAI(70, 70, World.walls, World.getAreas(), player);
    static Gun gun = new Gun(200, 200, World.walls, World.getAreas(), enemies, 2, getAchieves);
    private Thread p1 = new Thread(player);
    private Thread npc = new Thread(enemies);
    private Thread weapon = new Thread(gun);
    private Thread animate;
    private int num = 1;
    
    // spriteVector
    private SpriteVector sv;
    
    static boolean gameStarted = false;
    static boolean gameOver = false;
    static boolean pauseMenu = false;
    boolean achievementsStarted = false;
    boolean hardDifficulty = false;
    boolean startHover;
    boolean quitHover;
    boolean difficultyHover; 
    boolean achievementsHover; 
    boolean backHover;
    boolean resetHover;
    
    // button flags
    boolean startButtonFlag = false;
    boolean quitButtonFlag = false;
    boolean achievementButtonFlag = false;
    boolean difficultyButtonFlag = false;
    boolean ESCkeyPressed = false;
    
    //Menu Buttons
    Rectangle startButton = new Rectangle(300, 150, GWIDTH/4, GHEIGHT/12);
    Rectangle resumeButton = new Rectangle(200, 450, GWIDTH/4, GHEIGHT/12);
    Rectangle menuButton = new Rectangle(450, 450, GWIDTH/4, GHEIGHT/12);
    Rectangle quitButton = new Rectangle(300, 450, GWIDTH/4, GHEIGHT/12);
    Rectangle difficultyButton = new Rectangle(300, 250, GWIDTH/4, GHEIGHT/12);
    Rectangle achievementButton = new Rectangle(300, 350, GWIDTH/4, GHEIGHT/12);
    Rectangle backButton = new Rectangle(25, 525, 75, 25);
    Rectangle resetButton = new Rectangle(625, 525, 135, 25);
	
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
		
		map = new Map(1,10,new char['a'],1,10,new char['a']);
    	
    	this.addKeyListener(new KeyHandler());
    	this.addMouseListener(new MouseHandler());
        this.addMouseMotionListener(new MouseHandler());
        
    	world = new World(player, enemies);
        
        
        setPreferredSize(gameDim);
        setFocusable(true);
        requestFocusInWindow();
        initialize();
        
        // Handle all key inputs from user
        //addKeyListener(new TAdapter());
    }
    
    public void initialize(){
    	tracker = new MediaTracker(this);
    	
    	URL backLoc = this.getClass().getResource("/Resources/Background.png");
    	backIcon = new ImageIcon(backLoc);
    	Imagebackground = backIcon.getImage();
    	tracker.addImage(Imagebackground, 0);
		
		//TurtleDuck td = new TurtleDuck(this, pos);
    	TurtleDuck.initResources(tracker, 1);
    	
    	//BabyTurtleDuck btd = new BabyTurtleDuck(this, pos);
    	BabyTurtleDuck.initResources(tracker, 2);
    	
    	// duplicate 
    	for(int i = 0; i<4; i++){
    		for(int j=0; j<2; j++){
    			turtleDuck[i][j] = TurtleDuck.image[i][j];
    			babyTurtleDuck[i][j] = BabyTurtleDuck.image[i][j];
    		}
    	}
    	enemies.killAllEnemies();
    	world.restartLevel();
    	item.createItem(200, 200, 'a');
    	obs.createObs(400, 400, 'a');
    	player.setLevel(false);
    	
    }

  
    public void run() {
		long beforeTime, afterTime, diff, sleepTime, overSleepTime = 0;
		int delays = 0;
		
		try{
			tracker.waitForID(0);
		}
		catch (InterruptedException e) {
			return;
		}
		sv = new SpriteVector(new ImageBackground(this, Imagebackground));
		// For loops to add how many sprites we want to the spritevector
		for(int i = 0; i < numBTDuckSprites ; i++)
		{
			// get an empty position to draw sprite
			Point btdpos = sv.getEmptyPosition(new Dimension(babyTurtleDuck[0][1].getWidth(this), babyTurtleDuck[0][1].getHeight(this)));
			// add sprites to spritevector
			sv.add(createBabyTurtleDuck(btdpos, i % 6, 2));
		}
		for(int i = 0; i < numTDuckSprites ; i++)
		{
			// get an empty position to draw sprite
			Point tdpos = sv.getEmptyPosition(new Dimension(turtleDuck[0][0].getWidth(this), turtleDuck[0][0].getHeight(this)));
			// add sprites to spritevector
			sv.add(createTurtleDuck(tdpos, i % 6, 1));
		}
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
//			log(
//					"beforeTime: 	    " + beforeTime + "\n" +
//					"afterTime:			" + afterTime + "\n" +
//					"diff:				" + diff + "\n"	 +
//					"sleepTime:			" + sleepTime / 1000000L + "\n" +
//					"overSleepTime:		" + overSleepTime / 1000000L + "\n" +
//					"delays:			" + delays + "\n"	
//				
//			);
			
		}
	}
    
    private void gameUpdate() {
		if(running && game != null) {
			//enemy.update();
			sv.update();
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
			    g.drawString(gameTitle, 225, 75);
			    
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
    			int startY = 145;
    			int startX = 120;
    			int ovalX = 50;
    			int ovalY = 125;
    			g.setColor(backColor);
    			g.setColor(Color.WHITE);
    			g.setFont(new Font("Arial", Font.BOLD, 28));
    			g.drawString("ACHIEVEMENTS", 300, 75);	
    			achivements = getAchieves.getAllAchievement();
    			earnedAchivements = getAchieves.getEarnedAchievement();
    			g.setFont(new Font("Arial", Font.BOLD, 20));
    			g.setColor(Color.GRAY);
    			for(int i = 0; i<achivements.length; i ++){
    				for(int j = 0; j<earnedAchivements.length; j ++){
    					if(achivements[i].equals(earnedAchivements[j])){
    						g.setColor(Color.WHITE);
    						g.fillOval(ovalX, ovalY, 20, 20);
    						break;
    					}
    					else g.setColor(Color.GRAY);
    				}
    				g.drawString(achivements[i], startX, startY);
    				g.drawOval(ovalX, ovalY, 20, 20);
    				startY += 35;
    				ovalY += 35;
    				
    			}
    			if(!backHover)
    				g.setColor(buttonColor);
    			else
    				g.setColor(Color.pink);
    			g.fillRect(backButton.x, backButton.y, backButton.width, backButton.height);
    			g.setFont(new Font("Arial", Font.BOLD, 12));
    			g.setColor(Color.black);
    			g.drawString("Back", backButton.x+22, backButton.y+17);
    			if(!resetHover)
    				g.setColor(buttonColor);
    			else
    				g.setColor(Color.pink);
    			g.fillRect(resetButton.x, resetButton.y, resetButton.width, resetButton.height);
    			g.setFont(new Font("Arial", Font.BOLD, 12));
    			g.setColor(Color.black);
    			g.drawString("Reset Achiements", resetButton.x+15, resetButton.y+16);
    		}
    	} // if game started and Pausemenu is up
		else if(pauseMenu){//Menu
			sv.draw(g);
    		world.buildWorld(g);
    		player.draw(g);
    		enemies.draw(g);
    		gun.draw(g);
		    g.setFont(new Font("Arial", Font.BOLD, 26));
		    g.setColor(Color.GRAY);
		    g.fillRect(180, GHEIGHT/6*2, 500, 300);
		    g.setColor(Color.WHITE);
		    g.drawString("Pause Menu", 345, 225);
		    g.drawString("Bullet Damage", 190, 270);
		    if(gun.bulletDamage<10)
		    	g.drawString("  "+Integer.toString(gun.bulletDamage), 550, 270);
		    else
		    	g.drawString(Integer.toString(gun.bulletDamage), 550, 270);
		    g.drawString("Bullet Fire Rate", 190, 310);
		    if(player.fireRate<10)
		    	g.drawString("  "+Integer.toString(player.fireRate/10), 550, 310);
		    else
		    	g.drawString(Integer.toString(player.fireRate/10), 550, 310);
		    g.drawString("Player Speed", 190, 350);
		    if(player.speed<10)
		    	g.drawString("  "+Integer.toString(player.speed), 550, 350);
		    else
		    	g.drawString(Integer.toString(player.speed), 550, 350);
		    
		    // start button
		    if(startButtonFlag = true){
			    if(!startHover)
			    	g.setColor(buttonColor);
			    else
			    	g.setColor(Color.pink);
			    g.fillRect(resumeButton.x, resumeButton.y, GWIDTH/4, GHEIGHT/14);
			    g.setFont(new Font("Arial", Font.BOLD, 12));
			    g.setColor(Color.GRAY);
			    g.drawString("Resume Game", resumeButton.x+60, resumeButton.y+25);
			    
			    // quit button 
			    if(!quitHover)
			    	g.setColor(buttonColor);
			    else
			    	g.setColor(Color.pink);
			    g.fillRect(menuButton.x, menuButton.y, GWIDTH/4, GHEIGHT/14);
			    g.setColor(Color.GRAY);
			    g.drawString("Return to Main Menu", menuButton.x+40, menuButton.y+25);
		    }
			
		}
		else if(gameOver){//Menu
    		g.setColor(Color.BLACK);
    		g.fillRect(225, 95, 425, 60);
		    g.setFont(new Font("Arial", Font.BOLD, 70));
		    g.setColor(Color.WHITE);
		    g.drawString("GAME OVER", 225, 150);		    
		}
    	else{
    		//Game drawings
    		player.drawTopMenuBar(g);
        	if(enemies.totalEnemies==0){
                for (int i = 0; i < World.getAreas().size(); i++) {
                    Area area = (Area) World.getAreas().get(i);
                    area.setImage(0);
                    g.drawImage(area.getImage(), area.x(), area.y(), null);
                }
        	}
        	else{
        		for (int i = 0; i < World.getAreas().size(); i++) {
                    Area area = (Area) World.getAreas().get(i);
                    area.setImage(1);
                    g.drawImage(area.getImage(), area.x(), area.y(), null);
        		}	
        	}
    		if((tracker.statusID(0, true) & MediaTracker.ERRORED) != 0) 
    		{
    			// Error symbol
    			g.setColor(Color.red);
    			g.fillRect(0, 0, GWIDTH, GHEIGHT);
    			return;
    		}
    		if ((tracker.statusID(0, true) & MediaTracker.COMPLETE) != 0) 
    		{	
    			if(player.nextLevel())
    				initialize();
    			sv.draw(g);
    			if(item!=null)
    				item.draw(g);
    			if(obs!=null)
        			obs.draw(g);
    			world.buildWorld(g);
	    		player.draw(g);
	    		enemies.draw(g);
	    		gun.draw(g);
	    		
    		}
    		else {
    			// Draw the title message (while the images load)
    		      Font        f1 = new Font("TimesRoman", Font.BOLD, 28),
    		                  f2 = new Font("Helvetica", Font.PLAIN, 16);
    		      FontMetrics fm1 = g.getFontMetrics(f1), 
    		                  fm2 = g.getFontMetrics(f2);
    		      String      s1 = new String("Game"),
    		                  s2 = new String("Loading images...");
    		      g.setFont(f1);
    		      g.drawString(s1, (GWIDTH - fm1.stringWidth(s1)) / 2,
    		        ((GHEIGHT - fm1.getHeight()) / 2) + fm1.getAscent());
    		      g.setFont(f2);
    		      g.drawString(s2, (GWIDTH - fm2.stringWidth(s2)) / 2,
    		        GHEIGHT - fm2.getHeight() - fm2.getAscent());
    		}
	    		
    		
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
    	if(gameOver){
    		if(num%10==0){
	    		try {Thread.sleep(2000);} catch (InterruptedException e) {}
	    		gameOver = false;
	    		gameStarted = false;
	    		num ++;
    		}
    		else num++;
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
            if(achievementsStarted == true)
            	if (e.getKeyCode() == e.VK_ESCAPE) {
            		achievementsStarted = false;
            	}
      
            if(e.getKeyCode() == KeyEvent.VK_ESCAPE){
                pauseMenu = true;
            	player.paused();
            	enemies.paused();
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
            if(gameStarted ==false){
            
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
		           
		            
		            if(mx > backButton.x && mx < backButton.x+backButton.width &&
		            		my > backButton.y && my < backButton.y+backButton.height)  
		            	backHover = true;
		            else
		            	backHover = false;
		            if(mx > resetButton.x && mx < resetButton.x+resetButton.width &&
		            		my > resetButton.y && my < resetButton.y+resetButton.height)  
		            	resetHover = true;
		            else
		            	resetHover = false;
		        } 
            }
            
            if(pauseMenu ==true){
            	  if(startButtonFlag = true) {
  		            if(mx > resumeButton.x && mx < resumeButton.x+resumeButton.width &&
  		            		my > resumeButton.y && my < resumeButton.y+resumeButton.height)  
  		            	startHover = true;
  		            else
  		            	startHover = false;
  	            }
            	  if(quitButtonFlag = true) {    
  		            if(mx > menuButton.x && mx < menuButton.x+menuButton.width &&
  		            		my > menuButton.y && my < menuButton.y+menuButton.height) 
  		            	quitHover = true;
  		            else
  		            	quitHover = false;
  	            }
            }

        }
        @Override
        public void mousePressed(MouseEvent e){
            int mx = e.getX(); 
            int my = e.getY();
            if(gameStarted ==false){
            	 if(achievementsStarted == true) {
 		            if(mx > backButton.x && mx < backButton.x+backButton.width &&
 		            		my > backButton.y && my < backButton.y+backButton.height)
 		            		achievementsStarted = false;

 		            if(mx > resetButton.x && mx < resetButton.x+resetButton.width &&
 		            		my > resetButton.y && my < resetButton.y+resetButton.height)
 		            		getAchieves.resetAchieve();
 	            	
 	            }
            	 else{
  		            if(mx > achievementButton.x && mx < achievementButton.x+achievementButton.width &&
 		                	my > achievementButton.y && my < achievementButton.y+achievementButton.height)
 		            		achievementsStarted = true;
		            if(mx > startButton.x && mx < startButton.x+startButton.width &&
		            		my > startButton.y && my < startButton.y+startButton.height){
		            	gameStarted = true;
		            	initialize();
	            		   player.unpaused();
	            		   enemies.unpaused();
		            	getAchieves.storeAchievement("I'm Awesome - Start your first game");}
		            
		            if(mx > quitButton.x && mx < quitButton.x+quitButton.width &&
		            		my > quitButton.y && my < quitButton.y+quitButton.height)
		            	stopGame();
		            
		            if(mx > difficultyButton.x && mx < difficultyButton.x+difficultyButton.width &&
		            		my > difficultyButton.y && my < difficultyButton.y+difficultyButton.height)
		            {
		            	if(!hardDifficulty) {
		            		enemies.setDifficulty(8);
		            		hardDifficulty = true;
		            	}
		            	else {
		            		enemies.setDifficulty(20);
		            		hardDifficulty = false;
		            		
		            	}
		            		
		            }
            	 }
	           
	            
	        }
            if(pauseMenu ==true){
            	   if(mx > resumeButton.x && mx < resumeButton.x+resumeButton.width &&
   	            		my > resumeButton.y && my < resumeButton.y+resumeButton.height){
            		   player.unpaused();
            		   enemies.unpaused();
            		   pauseMenu = false;
            	   }
            	   if(mx > menuButton.x && mx < menuButton.x+menuButton.width &&
   	            		my > menuButton.y && my < menuButton.y+menuButton.height){
            		   gameStarted = false;
            		   pauseMenu = false;
            		   player.unpaused();
            		   enemies.unpaused();
            	   }
            }
            
        }

    }
    ///////END EVENT LISTENER CLASSES/////

    
    private void log(String s) {
		System.out.println(s);
	}

	    /************************** Create Sprite Methods ***********************/
    /************************************************************************/
    
    public DirectionalSprite createTurtleDuck(Point pos, int i, int zOrder)
    {
    	int frame = 1;
    	int frameIncrement = 2;
    	int frameDelay = -1;
		return new DirectionalSprite(this, turtleDuck, frame, frameIncrement, frameDelay, pos, new Point(rand.nextInt() % 5, rand.nextInt() % 2), zOrder, DirectionalSprite.BA_WRAP, 1);
	}
    
    public DirectionalSprite createBabyTurtleDuck(Point pos, int i, int zOrder)
    {
    	int frame = 1;
    	int frameIncrement = 2;
    	int frameDelay = -1;
    	return new DirectionalSprite(this, babyTurtleDuck, frame, frameIncrement, frameDelay, pos, new Point(rand.nextInt() % 5, rand.nextInt() % 2), zOrder, DirectionalSprite.BA_WRAP, 1);
    }

}


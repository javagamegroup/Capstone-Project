import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import javax.swing.*;

/**
 * 
 * @author Jian Rossi
 * @version 
 *
 */
 public class Menu extends JFrame {
 
    
    //Double buffering
    Image dbImage;
    Graphics dbg;
    
    //Ball objects
    static Ball b = new Ball(193, 143);
    
    Thread ball = new Thread(b);
    Thread p1 = new Thread(b.p1);
    Thread p2 = new Thread(b.p2);
    
    boolean hardDifficulty = false;
    
    boolean gameStarted = false;
    boolean achievementsStarted = false;
    
    boolean startHover;
    boolean quitHover;
    boolean difficultyHover; 
    boolean achievementsHover; 
    boolean backHover;
    
    //Menu Buttons
    Rectangle startButton = new Rectangle(200, 150, 125, 25);
    Rectangle quitButton = new Rectangle(200, 200, 125, 25);
    Rectangle difficultyButton = new Rectangle(200, 250, 125,25);
    Rectangle achievementButton = new Rectangle(200, 300, 125, 25);
    Rectangle backButton = new Rectangle(25, 50, 75, 25);
    
    //Variables for screen size
    int
    GWIDTH = 500,
    GHEIGHT = 500;
    //Dimension of GWIDTH*GHEIGHT
    Dimension screenSize = new Dimension(GWIDTH, GHEIGHT);
    
    //Create constructor to spawn window
    public Menu(){
    	
        this.setTitle("Pong Game");
        this.setSize(screenSize);
        this.setResizable(false);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setVisible(true);
        this.setBackground(Color.DARK_GRAY);
        this.addKeyListener(new KeyHandler());
        this.addMouseListener(new MouseHandler());
        this.addMouseMotionListener(new MouseHandler());
    }
    
    public void startGame() {
    	gameStarted = true;
    	ball.start();
    	p1.start();
    	p2.start();
    }
    
    public void quitGame() {
    	gameStarted = false;
//    	ball.interrupt();
//    	p1.interrupt();
//    	p2.interrupt();
    	System.exit(0);
    }
    
    public static void main(String[] args){
        Menu m = new Menu();
    }
    
    @Override
    public void paint(Graphics g){
        dbImage = createImage(getWidth(), getHeight());
        dbg = dbImage.getGraphics();
        draw(dbg);
        g.drawImage(dbImage, 0, 0, this);
    }
    public void draw(Graphics g){
    	
    	if(!gameStarted) {
    		
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
        b.draw(g);
        b.p1.draw(g);
        b.p2.draw(g);
        
        //Scores
        g.setColor(Color.WHITE);
        g.drawString(""+b.p1Score, 15, 50);
        g.drawString(""+b.p2Score, 470, 50);
        
    	} // if game not started
        
        repaint();
    }
    
    ////////EVENT LISTENER CLASSES/////////
    public class KeyHandler extends KeyAdapter {
        @Override
        public void keyPressed(KeyEvent e){
            b.p1.keyPressed(e);
            b.p2.keyPressed(e);
        }
        @Override
        public void keyReleased(KeyEvent e){
            b.p1.keyReleased(e);
            b.p2.keyReleased(e);
        }
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
            		b.setDifficulty(4);
            		hardDifficulty = true;
            	}
            	else {
            		b.setDifficulty(7);
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
    
}
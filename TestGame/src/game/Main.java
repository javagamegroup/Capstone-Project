package game;
import java.awt.Color;

import javax.swing.JFrame;


public final class Main extends JFrame {

    /**
	 * 
	 */
	private static final long serialVersionUID = 8886604927372324963L;
	
	Color drkGray = new Color(128, 128, 128);
    Color cyan = new Color(0, 245, 245);
    Color black = new Color(0, 0, 0);
    
    
    GamePanel gp;

    public Main() {
        InitUI();
    }

    public void InitUI() {
        gp = new GamePanel("Java Capstone Game Alpha!", black, black, cyan, true, true, true, true);
        add(gp);
        
        
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(GamePanel.GWIDTH+6,
                GamePanel.GHEIGHT);
        setLocationRelativeTo(null);
        setTitle("Game");
        setResizable(false);
        setVisible(true);
    }


    public static void main(String[] args) {
		Main sokoban = new Main();
    }
}
package menuPulledOutWeek5;

import javax.swing.JFrame;


public class Main extends JFrame {
	
	
	GamePanel gp;
	
	public Main(){
		this.setTitle("Chase");
		gp = new GamePanel();
		setSize(800, 460);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setVisible(true);
		setResizable(false);
	
		add(gp);
	}
	
	public static void main(String [] args) {
		Main m = new Main();
	}


   
}
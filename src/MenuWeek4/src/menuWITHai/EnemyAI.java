package menuWITHai;


import java.awt.*;
import javax.swing.ImageIcon;

public class EnemyAI implements Runnable {
    
    Rectangle AI, target;
    
    int xDirection, yDirection;
    
    boolean resting = false;
    int difficulty = 7;
    
    Image senator;
    
    public EnemyAI(Rectangle r, Rectangle t){
        AI = r;
        target = t;
        senator = new ImageIcon("C:/Users/jian/Documents/New folder/Begin/MenuWeek4/src/menuPulledOutWeek5/R-TX.png").getImage();
    }
    public void draw(Graphics g){
        g.setColor(Color.LIGHT_GRAY);
        if(AI != null)
            g.drawRect(AI.x, AI.y, AI.width, AI.height);
        g.drawImage(senator, AI.x, AI.y, null);
    }
    
    //Find a path to the target
    public void findPathToTarget() {
    	if(AI.x < target.x) 
    		setXDirection(1);
    	if(AI.x > target.x) 
    		setXDirection(-1);
    	if(AI.y < target.y) 
    		setYDirection(1);
    	if(AI.y > target.y) 
    		setYDirection(-1);
    }
    //Move in that direction
    public void detectEdges(){
        if(AI.x <= 0)
            setXDirection(1);
        if(AI.x >= 600-AI.width)
            setXDirection(-1);
        if(AI.y <= 25)
            setYDirection(1);
        if(AI.y > 400-AI.height)
            setYDirection(-1);
    }
    public void setXDirection(int dir){
        xDirection = dir;
    }
    public void setYDirection(int dir){
        yDirection = dir;
    }
    public void move(){
        AI.x += xDirection;
        AI.y += yDirection;
        if(xDirection == 1)
            senator = new ImageIcon("H:/College classes and material/Capstone 1/Game/src/pathFindingAI/R-TX.gif").getImage();
        if(xDirection == -1)
            senator = new ImageIcon("H:/College classes and material/Capstone 1/Game/src/pathFindingAI/R-TX.gif").getImage();
        if(yDirection == 1)
            senator = new ImageIcon("H:/College classes and material/Capstone 1/Game/src/pathFindingAI/R-TX.gif").getImage();
        if(yDirection == -1)
            senator = new ImageIcon("H:/College classes and material/Capstone 1/Game/src/pathFindingAI/R-TX.gif").getImage();
    }
    //In Run method, move in that direction and then wait
    @Override
    public void run(){
        try{
            while(true){
                if(!resting){
                    long start = System.currentTimeMillis();
                    long end = start + 3*500;
                    while(System.currentTimeMillis() < end){
                        findPathToTarget();
                    	move();
                        detectEdges();
                        Thread.sleep(difficulty);
                    }
                    resting = true;
                }
                else{
                    Thread.sleep(2000);
                    resting = false;
                }
            }
        }catch(Exception ex){
            System.err.println(ex.getMessage());
        }
    }
    
    public void setDifficulty(int diffEnemy) {
    	difficulty = diffEnemy;
    }
    
    public void log(String s){
        System.out.println(s);
    }
}
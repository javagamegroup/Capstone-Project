

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Rectangle;
import java.util.Random;

public class Ball implements Runnable {
    
    //Gloabal variables
    int x, y, xDirection, yDirection;
    
    int difficulty = 7;
    int paddleHeight = 50;
    
    //Score
    int p1Score, p2Score;
    
    Paddle p1 = new Paddle(15, 140,  1);
    Paddle p2 = new Paddle(470, 140, 2);
    
    Rectangle ball;
    
    public Ball(int x, int y){
        p1Score = p2Score = 0;
        this.x = x;
        this.y = y;
       
        //Set ball moving randomly
        Random r = new Random();
        int rDir = r.nextInt(1);
        if(rDir == 0)
            rDir--;
        setXDirection(rDir);
        int yrDir = r.nextInt(1);
        if(yrDir == 0)
            yrDir--;
        setYDirection(yrDir);
        //Create 'ball'
        ball = new Rectangle(this.x, this.y, 7, 7);
    }
    
    public void setXDirection(int xdir){
        xDirection = xdir;
    }
    public void setYDirection(int ydir){
        yDirection = ydir;
    }
    
    public void draw(Graphics g){
        g.setColor(Color.WHITE);
        g.fillRect(ball.x, ball.y, ball.width, ball.height);
    }
    
    public void collision(){
        if(ball.intersects(p1.paddle))
            setXDirection(+1);
        if(ball.intersects(p2.paddle))
            setXDirection(-1);
    }
    
    public void move(){
        collision();
        ball.x += xDirection;
        ball.y += yDirection;
        //Bounce the ball when edge is detected
        if(ball.x <= 0){
            setXDirection(+1);
            p2Score++;
        }
        if(ball.x >= 485){
            setXDirection(-1);
            p1Score++;
        }
        if(ball.y <= 15)
            setYDirection(+1);
        if(ball.y >= 485)
            setYDirection(-1);
    }
    
    @Override
    public void run(){
        try{
            while(true){
                move();
                Thread.sleep(difficulty);
            }
        }catch(Exception e){System.err.println(e.getMessage());}
    }
    
    public void setDifficulty(int diffBall) {
    	difficulty = diffBall;
    	
    	
    }
}
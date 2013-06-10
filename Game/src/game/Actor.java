package game;

import java.awt.Image;
import java.awt.Rectangle;

public class Actor {

    protected int xDirection, yDirection;
    private int x;
    private int y;
    private Image image;
    private Rectangle rect;

    public Actor(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public Image getImage() {
        return this.image;
    }

    public void setImage(Image img) {
        image = img;
    }
    
    public Rectangle getRect(int xPos, int yPos, int imageWidth, int imageHeight) {
    	rect = new Rectangle(xPos, yPos, imageWidth, imageHeight);
    	return this.rect;
    }

    public void setRect(Rectangle rec) {
    	rect = rec;
    }

    public int x() {
        return this.x;
    }

    public int y() {
        return this.y;
    }
    public int rectx() {
        return rect.x;
    }

    public int recty() {
        return rect.y;
    }

    public void incX(int x) {
        rect.x += x;
    }

    public void incY(int y) {
        rect.y += y;
    }
    
    public void setYDirection(int ydir){
        yDirection = ydir;
    }
    public void setXDirection(int xdir){
        xDirection = xdir;
    }
}
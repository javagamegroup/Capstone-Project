package game;

import java.awt.*;
import java.util.ArrayList;

public class World {

    private final int OFFSET = 60;
    private final int SPACE = 32;
    private final int LEFT_COLLISION = 1;
    private final int RIGHT_COLLISION = 2;
    private final int TOP_COLLISION = 3;
    private final int BOTTOM_COLLISION = 4;
    private int numEnemies = 0;
    
    static ArrayList<Wall> walls = new ArrayList<Wall>();
    private ArrayList<Baggage> baggs = new ArrayList<Baggage>();
    private static ArrayList<Area> areas = new ArrayList<Area>();
    private String[] levels = new String[4];
    
    private int w = 0;
    private int h = 0;
    String level;
    
    private boolean completed = false;
    
    private Player player;
    private Enemies enemies;
    
    public World(Player player, Enemies enemy) {
    	
    	this.player = player;
    	enemies = enemy;
    	initWorld();
    }
    
    public int getBoardWidth() {
        return this.w;
    }

    public int getBoardHeight() {
        return this.h;
    }
    
    public void buildWorld(Graphics g) {

        ArrayList<Actor> world = new ArrayList<Actor>();
        world.addAll(walls);
        world.addAll(getAreas());
        world.addAll(baggs);
        

        for (int i = 0; i < world.size(); i++) {

            Actor item = (Actor) world.get(i);

                g.drawImage(item.getImage(), item.x(), item.y(), null);
        }
    }
    
    public final void initWorld() {

        String level1 =GamePanel.level.getLevel();
        int x = 0;
        int y = OFFSET;
        
        Wall wall;
        Baggage b;
        Area a;
        for (int i = 0; i < level1.length(); i++) {

            char item = level1.charAt(i);

            if (item == '\n') {
                y += SPACE;
                if (this.w < x) {
                    this.w = x;
                }

                x = 0;
            } else if (item == '#') {
                wall = new Wall(x, y);
                walls.add(wall);
                x += SPACE;
            } else if (item == '$') {
                b = new Baggage(x, y);
                baggs.add(b);
                x += SPACE;
            } else if (item == '/') {
                a = new Area(x, y);
                getAreas().add(a);
                x += SPACE;
            }
            else if (item == 'x') {
                GamePanel.player.setCoord(x, y);
                x += SPACE;
            }
            else if (item == 'y') {
            	GamePanel.enemies.createEnemy(x, y, World.walls, World.getAreas(), player);
                x += SPACE;
            }
        else if (item == '=') {
                x += SPACE;
            }

            h = y;
        }
        
    }
    
    public void restartLevel() {

        getAreas().clear();
        baggs.clear();
        walls.clear();
        initWorld();
    }

	public static ArrayList<Area> getAreas() {
		return areas;
	}

	public void setAreas(ArrayList<Area> areas) {
		this.areas = areas;
	}

}

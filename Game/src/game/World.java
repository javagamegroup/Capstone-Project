package game;

import java.awt.*;
import java.util.ArrayList;

public class World {

    private final int OFFSET = 60;
    private final int SPACE = 32;
    static ArrayList<Wall> walls = new ArrayList<Wall>();
    private ArrayList<Baggage> baggs = new ArrayList<Baggage>();
    private static ArrayList<Area> areas = new ArrayList<Area>();
    private int w = 0;
    private int h = 0;
    String level;
    
    public World(Player player, Enemies enemy) {
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
        

        for (int i = 0; i < world.size(); i++) {

            Actor item = (Actor) world.get(i);
            	if(item!=null)
                g.drawImage(item.getImage(), item.x(), item.y(), null);
        }
    }
    
    public final void initWorld() {

        String level1 =Map.level.getLevel();
        int x = 0;
        int y = OFFSET;
        
        Wall wall;
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
            	i++;
            	item = level1.charAt(i);
            	GamePanel.item.createItem(x, y, item);
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
            	i++;
            	item = level1.charAt(i);
            	GamePanel.enemies.createEnemy(x, y, World.walls, World.getAreas(), GamePanel.player, item);
                x += SPACE;
            }
            else if (item == '&') {
            	i++;
            	item = level1.charAt(i);
            	GamePanel.npc.createNPC(x, y, World.walls, World.getAreas(), GamePanel.player, item);
                x += SPACE;
            }
            else if (item == '>') {
            	i++;
            	item = level1.charAt(i);
            	GamePanel.obs.createObs(x, y, item);;
                x += SPACE;
            }
            else{
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
		World.areas = areas;
	}

}

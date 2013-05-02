import java.awt.*;
import java.util.ArrayList;

public class World {

    private final int OFFSET = 30;
    private final int SPACE = 20;
    private final int LEFT_COLLISION = 1;
    private final int RIGHT_COLLISION = 2;
    private final int TOP_COLLISION = 3;
    private final int BOTTOM_COLLISION = 4;
    
    static ArrayList<Wall> walls = new ArrayList<Wall>();
    private ArrayList<Baggage> baggs = new ArrayList<Baggage>();
    private static ArrayList<Area> areas = new ArrayList<Area>();
    private String[] levels = new String[4];
    
    private int w = 0;
    private int h = 0;
    
    private boolean completed = false;
    
    //private Player soko;
    private EnemyAI enemy;
    
    public World(Player player, EnemyAI enemy) {
    	
    	//this.soko = player;
    	//this.enemy = enemy;
    	initWorld();
    }
    
    public int getBoardWidth() {
        return this.w;
    }

    public int getBoardHeight() {
        return this.h;
    }
    
    public void buildWorld(Graphics g) {
    	
       // g.setColor(new Color(0, 0, 0));
       // g.fillRect(0, 0, this.getBoardWidth(), this.getBoardHeight());

        ArrayList<Actor> world = new ArrayList<Actor>();
        world.addAll(walls);
        world.addAll(getAreas());
        world.addAll(baggs);
        //world.add(soko);
        //world.add(enemy);
        

        for (int i = 0; i < world.size(); i++) {

            Actor item = (Actor) world.get(i);

                g.drawImage(item.getImage(), item.x(), item.y(), null);
        }
    }
    
    public final void initWorld() {
        String level1 =
                "#########..#####################\n"
              + "#                              #\n"
              + "#                              #\n"
              + "#                              #\n"
              + "#                              #\n"
              + ".                              .\n"
              + ".                              .\n"
              + "#                              #\n"
              + "#                              #\n"
              + "#                              #\n"
              + "#                              #\n"
              + "#                              #\n"
              + "#                              #\n"
              + "#                              #\n"
              + "#                              #\n"
              + "#                              #\n"
              + "#                              #\n"
              + "#                              #\n"
              + "#                              #\n"
              + "#                              #\n"
              + "#                              #\n"
              + "#########..#####################\n";
        int x = OFFSET;
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

                x = OFFSET;
            } else if (item == '#') {
                wall = new Wall(x, y);
                walls.add(wall);
                x += SPACE;
            } else if (item == '$') {
                b = new Baggage(x, y);
                baggs.add(b);
                x += SPACE;
            } else if (item == '.') {
                a = new Area(x, y);
                getAreas().add(a);
                x += SPACE;
            } //else if (item == '@') {
            	//soko = new Player(x, y);
                //x += SPACE;
                
           // } 
        //else if (item == 'e') {
                //enemy = new EnemyAI(x, y, soko);
                //x += SPACE;
                
            //}
        else if (item == ' ') {
                x += SPACE;
            }

            h = y;
        }
        
    }

    
    
    public void isCompleted() {

        int num = baggs.size();
        int compl = 0;

        for (int i = 0; i < num; i++) {
            Baggage bag = (Baggage) baggs.get(i);
            for (int j = 0; j < num; j++) {
                Area area = (Area) getAreas().get(j);
                if (bag.x() == area.x()
                        && bag.y() == area.y()) {
                    compl += 1;
                }
            }
        }

        if (compl == num) {
            completed = true;
        }
    }
    
    public void restartLevel() {

        getAreas().clear();
        baggs.clear();
        walls.clear();
        initWorld();
        if (completed) {
            completed = false;
        }
    }

	public static ArrayList<Area> getAreas() {
		return areas;
	}

	public void setAreas(ArrayList<Area> areas) {
		this.areas = areas;
	}
}

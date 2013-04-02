public class Level
{
	int numEnemies;
	boolean enemy;
	int numDoors = 2;
	private char nDoor = '#';
	private char sDoor = '#';
	private char eDoor = '#';
	private char wDoor = '#';
	private RandomGen rand;
	private boolean finalRoom = false;
	private boolean firstRoom = false;
	private boolean startDoor = false;
	protected Level north = null;
	protected Level south = null;
	protected Level east = null;
	protected Level west = null;
	char i;
	String level = null;
	char door = '.';
	char player = 'x';
	char nplayer = '=';
	char splayer = '=';
	char eplayer = '=';
	char wplayer = '=';
	
	public Level()
	{
		numEnemies = 0;
		numDoors = 2;
		try {rand = new RandomGen(1,4);}
		catch (Exception e)
		{
			System.exit(100);
			e.printStackTrace();
		}
//		if(rand.randomInt() % 2 == 0)
//		{
//			
//		}
		this.firstRoom = true;
		//32x16
		sDoor ='/';
		this.level =
			"###############"+nDoor+nDoor+"###############"+'\n'+
			"#=============="+nplayer+"===============#"+'\n'+
			"#==============================#"+'\n'+
			"#==============================#"+'\n'+
			"#==============================#"+'\n'+
			"#==============================#"+'\n'+
			wDoor+wplayer+"============="+player+"=============="+eplayer+eDoor+'\n'+
			wDoor+"=============================="+eDoor+'\n'+
			"#==============================#"+'\n'+
			"#==============================#"+'\n'+
			"#==============================#"+'\n'+
			"#==============================#"+'\n'+
			"#==============="+splayer+"==============#"+'\n'+
			"###############"+sDoor+sDoor+"###############"+'\n';
	}
	//check min and max make sure separating is okay.
	public Level genLevel(boolean enemies, int numEnemies, int minDoors, int maxDoors, char startDir) throws PreviousRoomException, Exception
	{
		rand = new RandomGen(minDoors, maxDoors);
		this.numEnemies = numEnemies;
		this.numDoors = minDoors + rand.randomInt();
		if(numDoors == 1)
			finalRoom = true;
		return this;
		
	}
	
	public String getLevel()
	{
		return level;
	}
	
	/**
	 * @param x
	 * @param numN
	 * @param numS
	 * @param numE
	 * @param numW
	 */
	public void recursiveLevel(Level x, int numN, int numS, int numE, int numW)
	{
		if(x.north == null && numN >= 0)
		{
			x.north = new Level();
			numN--;
			while(x.north.finalRoom != true)
			{
				if(x.north.numDoors == 1)
					x.north.finalRoom = true;
			}
			x = x.north;
			recursiveLevel(x, numN, numS, numE, numW);
		}
//		do recursion south
//		do recursion east
//		do recursion west
	}
	public void print()
	{
		System.out.println(level);
	}
}
class PreviousRoomException extends Exception 
{
	private static final long serialVersionUID = 1L;
	
	public PreviousRoomException() { super(); }
	public PreviousRoomException(String message) { super(message); }
	public PreviousRoomException(String message, Throwable cause) { super(message, cause); }
	public PreviousRoomException(Throwable cause) { super(cause); }
}
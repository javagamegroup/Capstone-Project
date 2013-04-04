public class Level
{
	private int maxLevWidth = 7;
	private int maxLevHeight = 7;
	private int numEnemies;
	private boolean enemy;
	private int numDoors = 2;
	private char nDoor = '#';
	private char sDoor = '#';
	private char eDoor = '#';
	private char wDoor = '#';
	private RandomGen rand;
	protected Level[][] theLevel = new Level[maxLevHeight][maxLevWidth];
	private boolean finalRoom = false;
	private boolean firstRoom = false;
	private boolean startDoor = false;
	protected Level north = null;
	protected Level south = null;
	protected Level east = null;
	protected Level west = null;
	protected char i;
	protected String level = null;
	protected char door = '.';
	protected char player = 'x';
	protected char nplayer = '=';
	protected char splayer = '=';
	protected char eplayer = '=';
	protected char wplayer = '=';
	
//  default
//  [1]{[a][b][c][d][e][f][g]}
//  [2]{[a][b][c][d][e][f][g]}
//  [3]{[a][b][c][d][e][f][g]}
//  [4]{[a][b][c][d][e][f][g]}
//  [5]{[a][b][c][d][e][f][g]}
//  [6]{[a][b][c][d][e][f][g]}
//  [7]{[a][b][c][d][e][f][g]}
//  
//  [4][d] center room and starting room
//  
//  multidimensional arrays someArray[height][width]

	public Level()
	{
		room();	
	}
	
	public void room()//setter for the room.
	{
		for(int i=0; i<maxLevHeight; i++)
		{
			for(int j=0; j<maxLevWidth; j++)
			{
				theLevel[i][j] = null;
			}
		}
		theLevel[maxLevWidth/2+1][maxLevHeight/2 +1] = createLevel(true);
	}
	
	private Level createLevel(boolean first)//determines if this is the first room where the player enters into the level.
	{
		numEnemies = 0;
		try {rand = new RandomGen(1,4);}
		catch (Exception e)
		{
			System.exit(100);
			e.printStackTrace();
		}
		
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
		return this;
	}
	
	private Level voidFillLevel(Level x)
	{
		//insert and fill level
		return x;
	}
	
//  check min and max make sure separating is okay.
//  requires previous room direction?
//	private Level genLevel(boolean enemies, int numEnemies, int minDoors, int maxDoors, char startDir) throws PreviousRoomException, Exception
//	{
//		rand = new RandomGen(minDoors, maxDoors);
//		this.numEnemies = numEnemies;
//		this.numDoors = minDoors + rand.randomInt();
//		if(numDoors == 1)
//			finalRoom = true;
//		genLevel(true, numEnemies+rand.randomInt(),1,4);
//		return this;
//	}
	
	public String getLevel()
	{
		return level;
	}

	private void recursiveLevel(Level x, int numN, int numS, int numE, int numW)
	{
//		if(x.north == null && numN >= 0)
//		{
//			x.north = new Level();
//			numN--;
//			while(x.north.finalRoom != true)
//			{
//				if(x.north.numDoors == 1)
//					x.north.finalRoom = true;
//			}
//			x = x.north;
//			recursiveLevel(x, numN, numS, numE, numW);
//		}
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
package game;

public class Level
{
	private int xBounds = 7;
	private int yBounds = 7;
	private int numEnemies;
	private boolean enemy;
	private int numDoors = 1;
	private char nDoor = '#';
	private char sDoor = '#';
	private char eDoor = '#';
	private char wDoor = '#';
	private boolean finalRoom = false;
	private boolean firstRoom = false;
	private boolean nBoolDoor = false; //if generating set to true //
	private boolean sBoolDoor = false; //if generating set to true //
	private boolean eBoolDoor = false; //if generating set to true //
	private boolean wBoolDoor = false; //if generating set to true //
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
	protected int yloc;
	protected int xloc;
	
	//direction 0 = North
	//direction 1 = East
	//direction 2 = South
	//direction 3 = West
	
	public Level(boolean first, int startDir)//determines if this is the first room where the player enters into the level.
	{
		firstRoom = true;
		numEnemies = 0;
		RandomGen rand = null;
		try 
		{
			rand = new RandomGen(0,2);
		}
		catch (Exception e){}
	
		numDoors += rand.randomInt();
		
		int dir = rand.randomDirection();
		while(numDoors!= 0 && dir!= -1)
		{
			switch (dir)
			{
					
				case 0:
					if(dir == startDir)
						break;
					else
					{
						nDoor = '/';
						nBoolDoor = true;
						numDoors --;
					}
				case 1:
					if(dir == startDir)
						break;
					else
					{
						eBoolDoor = true;
						eDoor = '/';
						numDoors --;
					}
					break;
				case 2:
					if(dir == startDir)
					{
						sDoor = '/';
						break;
					}
					else
					{
						sBoolDoor = true;
						sDoor = '/';
						numDoors --;
					}
					break;
				case 3:
					if(dir == startDir)
						break;
					else
					{
						wDoor = '/';
						wBoolDoor = true;
						numDoors --;
					}
					break;
				default:
					break;
					
			}
			dir = rand.randomDirection();
		}
		player = 'x';
		
		//32x16
		switch(startDir)
		{
			case 0:
				nDoor ='/';
				break;
			case 1:
				eDoor ='/';
				break;
			case 2:
				sDoor ='/';
				break;
			case 3:
				wDoor ='/';
				break;
			default:
				break;
		}
		
		this.level =
			"############"+nDoor+nDoor+"############"+'\n'+
			"#==========="+nplayer+"============#"+'\n'+
			"#========================#"+'\n'+
			"#========================#"+'\n'+
			"#========================#"+'\n'+
			"#========================#"+'\n'+
			"#========================#"+'\n'+
			wDoor+wplayer+"=========="+player+"==========="+eplayer+eDoor+'\n'+
			wDoor+"========================"+eDoor+'\n'+
			"#========================#"+'\n'+
			"#========================#"+'\n'+
			"#========================#"+'\n'+
			"#========================#"+'\n'+
			"#========================#"+'\n'+
			"#============"+splayer+"===========#"+'\n'+
			"############"+sDoor+sDoor+"############"+'\n';
		
		yloc = (yBounds/2) + 1;
		xloc = (xBounds/2) + 1;
		
		//Debugging Print check//
		try {rand.setLimits(0,500);} catch (Exception e) {}
		System.out.println(rand.randomInt());
		this.print();
		//End Debugging check//
		recursiveLevel();
		
	}
	
	private Level(boolean enemies, int numEnemies, int minDoors, int maxDoors, Level previous, int startDir, int y, int x)
	{
		this.yloc = y;
		this.xloc = x;
		RandomGen rand = null;
		switch (startDir)
		{
			case 0:
				this.south = previous;
				break;
			case 1:
				this.west = previous;				
				break;
			case 2:
				this.north = previous;
				break;
			case 3:
				this.east = previous;
				break;
			default:
				break;
		}
		int tempY = -1;
		int tempX = -1;
		int tempDoors = maxDoors;
		if(yloc == yBounds)
		{
			tempY = 2;
			maxDoors--;
		}
		else if(yloc == 0)
		{
			tempY = 0;
			maxDoors--;
		}
		if(xloc == xBounds)
		{
			tempX = 1;
			maxDoors--;
		}
		else if(xloc == 0)
		{
			tempX = 3;
			maxDoors--;
		}
		try {rand = new RandomGen(minDoors, maxDoors);} catch (Exception e) {}
		this.numEnemies = numEnemies;
		this.numDoors = minDoors + rand.randomInt();
		
		int dir = rand.randomDirection();
		while(numDoors!= 0 && dir!= -1)
		{
			switch (dir)
			{
					
				case 0:
					if(dir == startDir)
					{
						nDoor = '/';
						break;
					}
					else if(dir == tempY)
						break;
					else
					{
						nDoor = '/';
						nBoolDoor = true;
						numDoors --;
					}
					break;
				case 1:
					if(dir == startDir)
					{
						eDoor = '/';
						break;
					}
					else if(dir == tempX)
						break;
					else
					{
						eBoolDoor = true;
						eDoor = '/';
						numDoors --;
					}
					break;
				case 2:
					if(dir == startDir)
					{
						sDoor = '/';
						break;
					}
					else if(dir == tempY)
						break;
					else
					{
						sBoolDoor = true;
						sDoor = '/';
						numDoors --;
					}
					break;
				case 3:
					if(dir == startDir)
					{
						wDoor = '/';
						break;
					}
					else if(dir == tempX)
						break;
					else
					{
						wDoor = '/';
						wBoolDoor = true;
						numDoors --;
					}
					break;
				default:
					break;
					
			}
			dir = rand.randomDirection();
		}
		
		switch(startDir)
		{
			case 0:
				nDoor ='/';
				break;
			case 1:
				eDoor ='/';
				break;
			case 2:
				sDoor ='/';
				break;
			case 3:
				wDoor ='/';
				break;
			default:
				break;
		}
		
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
						
		//Debugging Print check//
		try {rand. setLimits(0,750);} catch (Exception e) {}
		System.out.println(rand.randomInt());
		this.print();
		//End Debugging check//
		recursiveLevel();
	}
	
	public String getLevel()
	{
		return level;
	}
	
	//possible set incomingDirection to lower case?
	public void setPlayerEntrance(char incomingDirection, Level current) throws InvalidCharacterException
	{
		current.player = '=';
		current.nplayer = '=';
		current.splayer = '=';
		current.eplayer = '=';
		current.wplayer = '=';
		
		if(incomingDirection == 'n')
		{
			current = current.north;
			current.splayer = 'x';
		}
		else if(incomingDirection == 's')
		{
			current = current.south;
			current.nplayer = 'x';
		}
		else if(incomingDirection == 'e')
		{
			current = current.east;
			current.wplayer = 'x';
		}
		else if(incomingDirection == 'w')
		{
			current = current.west;
			current.eplayer = 'x';
		}
		else throw new InvalidCharacterException();
	}

	private void  recursiveLevel()
	{
		RandomGen numEnemies = null;
		RandomGen maxDoors = null;
		try
		{
			numEnemies = new RandomGen(0,10);
			maxDoors = new RandomGen(0,3);
		} catch (Exception e) {}
		
		
		if(nBoolDoor)
			this.north = new Level(true, numEnemies.randomInt(), 0,maxDoors.randomInt(), this, 0 , this.yloc - 1, this.xloc );
		if(eBoolDoor)
			this.east = new Level(true, numEnemies.randomInt(), 0,maxDoors.randomInt(), this, 1 , this.yloc, this.xloc + 1 );
		if(sBoolDoor)
			this.south = new Level(true, numEnemies.randomInt(), 0,maxDoors.randomInt(), this, 2 , this.yloc + 1, this.xloc );
		if(wBoolDoor)
			this.west = new Level(true, numEnemies.randomInt(), 0,maxDoors.randomInt(), this, 4 , this.yloc, this.xloc - 1);
	}

	
	public void print()
	{System.out.println(level);}

}



class PreviousLevelException extends Exception 
{
	private static final long serialVersionUID = 1L;
	
	public PreviousLevelException() { super(); }
	public PreviousLevelException(String message) { super(message); }
	public PreviousLevelException(String message, Throwable cause) { super(message, cause); }
	public PreviousLevelException(Throwable cause) { super(cause); }
}

class InvalidCharacterException extends Exception 
{
	private static final long serialVersionUID = 1L;
	
	public InvalidCharacterException() { super(); }
	public InvalidCharacterException(String message) { super(message); }
	public InvalidCharacterException(String message, Throwable cause) { super(message, cause); }
	public InvalidCharacterException(Throwable cause) { super(cause); }
}
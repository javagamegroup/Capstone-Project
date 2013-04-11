public class Level extends Room
{
	private int numEnemies;
	private boolean enemy;
	private int numDoors = 1;
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
	protected char i;
	protected String level = null;
	protected char door = '.';
	protected char player = 'x';
	protected char nplayer = '=';
	protected char splayer = '=';
	protected char eplayer = '=';
	protected char wplayer = '=';
	
	protected Level(boolean first, int dir)//determines if this is the first room where the player enters into the level.
	{
		numEnemies = 0;
		try 
		{
			rand = new RandomGen();
			rand.setLimits(2,3);
		}
		catch (Exception e){}
		
		numDoors += rand.randomInt();
		
		switch (dir)
		{
			case 0:
				player = 'x';
				break;
			case 1:
				splayer = 'x';
				break;
			case 2:
				wplayer = 'x';
				break;
			case 3:
				nplayer = 'x';
				break;
			case 4:
				eplayer = 'x';
				break;
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
		
		int midHeight = (super.maxLevHeight/2) + 1;
		int midWidth = (super.maxLevWidth/2) + 1;
		try 
		{
			rand.setLimits(0,3);
			genLevel(true, 0, 1, 4, this, rand.randomInt(), midHeight, midWidth);
		}
		catch (Exception e){}
	}
	
	private Level voidFillLevel(Level x, boolean enemies, int numEnemies, int minDoors, int maxDoors, char previousLevel)
	{
		//insert and fill level
		return x;
	}
	
//	check min and max make sure separating is okay.
//	requires previous room direction?
	private Level genLevel(boolean enemies, int numEnemies, int minDoors, int maxDoors, Level previous, int dir, int yloc, int xloc) throws PreviousLevelException, Exception
	{
//		switch (dir)
//		{
//			case 0:
//				previous.south = temp;
//				break;
//			case 1:
//				previous.west = temp;
//				break;
//			case 2:
//				previous.north = temp;
//				break;
//			case 3:
//				previous.east = temp;
//				break;
//			default:
//				throw new PreviousLevelException();
//		}
				
		rand = new RandomGen(minDoors, maxDoors);
		this.numEnemies = numEnemies;
		this.numDoors = minDoors + rand.randomInt();
		
		try{rand.setLimits(2,5);}
		catch(Exception e){}
		
		if(numDoors == 1)
		{
			finalRoom = true;
			return this;
		}
		//already an existing room terminate recursion
		try
		{
			rand.setLimits(-numEnemies,numEnemies);
			switch(dir)
			{
				case 0:
					super.theLevel[yloc + 1][xloc] = genLevel(true, rand.randomInt(), 1, 4, this, 0, yloc, xloc);// South
					super.theLevel[yloc][xloc - 1] = genLevel(true, rand.randomInt(), 1, 4, this, 1, yloc, xloc);// West
					super.theLevel[yloc][xloc +1] = genLevel(true, rand.randomInt(), 1, 4, this, 3, yloc, xloc);// East
					break;
				case 1:
					super.theLevel[yloc + 1][xloc] = genLevel(true, rand.randomInt(), 1, 4, this, 0, yloc, xloc);// South
					super.theLevel[yloc][xloc - 1] = genLevel(true, rand.randomInt(), 1, 4, this, 1, yloc, xloc);// West
					super.theLevel[yloc - 1][xloc] = genLevel(true, rand.randomInt(), 1, 4, this, 2, yloc, xloc);// North
					break;
				case 2:
					super.theLevel[yloc][xloc - 1] = genLevel(true, rand.randomInt(), 1, 4, this, 1, yloc, xloc);// West
					super.theLevel[yloc - 1][xloc] = genLevel(true, rand.randomInt(), 1, 4, this, 2, yloc, xloc);// North
					super.theLevel[yloc][xloc +1] = genLevel(true, rand.randomInt(), 1, 4, this, 3, yloc, xloc);// East
					break;
				case 3:
					super.theLevel[yloc + 1][xloc] = genLevel(true, rand.randomInt(), 1, 4, this, 0, yloc, xloc);// South
					super.theLevel[yloc - 1][xloc] = genLevel(true, rand.randomInt(), 1, 4, this, 2, yloc, xloc);// North
					super.theLevel[yloc][xloc +1] = genLevel(true, rand.randomInt(), 1, 4, this, 3, yloc, xloc);// East
					break;
				default:
					break;
			}
		}catch(IndexOutOfBoundsException e){return this;}
		
		return this;
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
		current.player = '=';
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
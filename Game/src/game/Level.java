package game;

import java.util.StringTokenizer;

import Exceptions.InvalidCharacterException;
import Exceptions.InvalidEnemyException;
import Exceptions.InvalidItemException;

public class Level
{
	private int xBounds = 7;
	private int yBounds = 7;
	
	private int minEnemies;
	private int maxEnemies;
	private int numEnemies;
	private char[] enemies = null;
	private boolean enemy;
	private int minItems;
	private int maxItems;
	private int numItems;
	private char[] items = null;
	private int minObs;
	private int maxObs;
	private int numObs;
	private char[] obs = null;
	private boolean friendlyNPC;
	private char[] npcFriendly = null;
	
	private int rows = 16;
	private int columns = 26;

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
	protected int player = 0;
	protected int nplayer = 0;
	protected int splayer = 0;
	protected int eplayer = 0;
	protected int wplayer = 0;
	protected int yloc;
	protected int xloc;
	protected String level = null;
//	26x16
//	"############"+nDoor+nDoor+"############"+'\n'+
//	"#-----------"+nplayer+"------------#"+'\n'+
//	"#------------------------#"+'\n'+
//	"#--========----========--#"+'\n'+
//	"#--====================--#"+'\n'+
//	"#--====================--#"+'\n'+
//	"#---==================---#"+'\n'+
//	wDoor+wplayer+"--========"+player+"=========--"+eplayer+eDoor+'\n'+
//	wDoor+"---==================---"+eDoor+'\n'+
//	"#---==================---#"+'\n'+
//	"#--====================--#"+'\n'+
//	"#--====================--#"+'\n'+
//	"#--========----========--#"+'\n'+
//	"#------------------------#"+'\n'+
//	"#-----------"+splayer+"------------#"+'\n'+
//	"############"+sDoor+sDoor+"############"+'\n';
//	
//	direction 0 = North
//	direction 1 = East
//	direction 2 = South
//	direction 3 = West
	
	public Level(boolean first, int startDir, int minEmemies, int maxEnemies, char[] enemies, int minItems, int maxItems, char[] items, int minObs, int maxObs, char[] obs, boolean vender, char[] npcFriendly)//determines if this is the first room where the player enters into the level.
	{
		firstRoom = true;
		this.player = 'x';
		
		this.friendlyNPC = vender;
		this.npcFriendly = new char[npcFriendly.length];
		this.npcFriendly = npcFriendly;
		
		this.numEnemies = 0;
		this.numItems = 0;
		this.minEnemies = minEnemies;
		this.maxEnemies = maxEnemies;
		this.enemies = new char[enemies.length];
		this.enemies = enemies;
		
		this.minItems = minItems;
		this.maxItems = maxItems;
		this.items = new char[items.length];
		this.items = items;
		
		this.minObs = minObs;
		this.maxObs = maxObs;
		this.obs =  new char[obs.length];
		this.obs = obs;
		
		for(int i = 0; i < items.length; i++)
		{
			this.items[i] = items[i];
		}
		
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
					break;
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
		
		//26x16
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
				"#------------------------#"+'\n'+
				"#------------------------#"+'\n'+
				"#--========----========--#"+'\n'+
				"#--====================--#"+'\n'+
				"#--====================--#"+'\n'+
				"#---==================---#"+'\n'+
				wDoor+"---=========x========---"+eDoor+'\n'+
				wDoor+"---==================---"+eDoor+'\n'+
				"#---==================---#"+'\n'+
				"#--====================--#"+'\n'+
				"#--====================--#"+'\n'+
				"#--========----========--#"+'\n'+
				"#------------------------#"+'\n'+
				"#------------------------#"+'\n'+
				"############"+sDoor+sDoor+"############"+'\n';
		
		yloc = (yBounds/2) + 1;
		xloc = (xBounds/2) + 1;
		
		//Debugging Print check//
//		try {rand.setLimits(0,500);} catch (Exception e) {}
//		System.out.println(rand.randomInt());
//		this.print();
		//End Debugging check//
		recursiveLevel();
		
	}
	
	private Level(boolean enemy, int minEnemies, int maxEnemies, char[] enemies, int minItems, int maxItems, char[] items, int minObs, int maxObs, char[] obs, boolean friendlyNPC, char[] npcFriendly, int minDoors, int maxDoors, Level previous, int startDir, int y, int x)
	{
		RandomGen ranEnemies = new RandomGen(enemies);
		RandomGen ranItems = new RandomGen(items);
		RandomGen ranObs = new RandomGen(items);
		RandomGen ranNPCs = new RandomGen(npcFriendly);
		this.friendlyNPC = friendlyNPC;
		this.npcFriendly = new char[npcFriendly.length];
		this.npcFriendly = npcFriendly;
		this.enemy = enemy;
		this.player = 'x';
		this.minEnemies = minEnemies;
		this.maxEnemies = maxEnemies;
		this.items = new char[items.length];
		this.items = items;
		this.enemies = new char[enemies.length];
		this.enemies = enemies;
		
		for(int i = 0; i < items.length; i++)
		{
			this.items[i] = items[i];
		}
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
			if(maxDoors <0)maxDoors = 0;
		}
		else if(yloc == 0)
		{
			tempY = 0;
			maxDoors--;
			if(maxDoors <0)maxDoors = 0;
		}
		if(xloc == xBounds)
		{
			tempX = 1;
			maxDoors--;
			if(maxDoors <0)maxDoors = 0;
		}
		else if(xloc == 0)
		{
			tempX = 3;
			maxDoors--;
			if(maxDoors <0)maxDoors = 0;
		}
		
		try {rand = new RandomGen(minDoors, maxDoors);} catch (Exception e) {}
		this.numDoors = minDoors + rand.randomInt();
		
		int dir = rand.randomDirection();
		while(numDoors!= 0 && dir!= -1)
		{
			switch (dir)
			{
					
				case 0:
					if(dir == startDir)
						break;
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
						break;
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
						break;
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
						break;
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
				sDoor ='/';
				break;
			case 1:
				wDoor ='/';
				break;
			case 2:
				nDoor ='/';
				break;
			case 3:
				eDoor ='/';
				break;
			default:
				break;
		}
		
		this.level =
				"############"+nDoor+nDoor+"############"+'\n'+
				"#------------------------#"+'\n'+
				"#------------------------#"+'\n'+
				"#--========----========--#"+'\n'+
				"#--====================--#"+'\n'+
				"#--====================--#"+'\n'+
				"#---==================---#"+'\n'+
				wDoor+"---==================---"+eDoor+'\n'+
				wDoor+"---==================---"+eDoor+'\n'+
				"#---==================---#"+'\n'+
				"#--====================--#"+'\n'+
				"#--====================--#"+'\n'+
				"#--========----========--#"+'\n'+
				"#------------------------#"+'\n'+
				"#------------------------#"+'\n'+
				"############"+sDoor+sDoor+"############"+'\n';

//START npcFriendly RANDOM GENERATION//
		
		int stringPos = 0;
		int validSpawn = 0;
		
		while(stringPos < this.level.length())
		{
			if(this.level.charAt(stringPos) == '=' || this.level.charAt(stringPos) == '-')
				validSpawn ++;
			stringPos++;
		}

		RandomGen probability = null;
		try 
		{
			probability = new RandomGen(0,1);
		} catch (Exception e) {}
		
		stringPos = 0;
		double probRate = ((double)1/(double)validSpawn);
		StringBuffer tempString= new StringBuffer(this.level);
		i=0;
		
		while(stringPos < tempString.length() && this.friendlyNPC == true)
		{
			switch(tempString.charAt(stringPos))
			{
				case '\n':
					break;
				case '-':
					if(probRate > probability.randomDouble())
					{
						tempString.setCharAt(stringPos, '&');
						stringPos++;
						i++;
						tempString.insert(stringPos, ranNPCs.randomChar());
						this.friendlyNPC = false;
					}
					break;
				case '=':
					if(probRate > probability.randomDouble())
					{
						tempString.setCharAt(stringPos, '&');
						stringPos++;
						tempString.insert(stringPos, ranNPCs.randomChar());
						i++;
						this.friendlyNPC = false;
					}
					break;
				 default:
					 break;
			}
			stringPos++;
		}
		this.level = tempString.toString();

//END npcFriendly RANDOM GENERATION//
				
//START ENEMY RANDOM GENERATION//
			
		try 
		{
			probability = new RandomGen(0,1);
			rand = new RandomGen(minEnemies, maxEnemies);
		} catch (Exception e) {}
		
		this.numEnemies = rand.randomInt();	
		stringPos = 0;
		validSpawn = 0;
		
		while(stringPos < this.level.length())
		{
			if(this.level.charAt(stringPos) == '=')
				validSpawn ++;
			stringPos++;
		}
		
		stringPos = 0;
		probRate = ((double)this.numEnemies/(double)validSpawn);
		tempString= new StringBuffer(this.level);
		i=0;
		
		while(stringPos < tempString.length() && i < this.numEnemies)
		{
			switch(tempString.charAt(stringPos))
			{
				case '\n':
					break;
				case '=':
					if(probRate > probability.randomDouble())
					{
						tempString.setCharAt(stringPos, 'y');
						stringPos++;
						tempString.insert(stringPos, ranEnemies.randomChar());
						i++;
					}
					break;
					 
				 default:
					 break;
			}
			stringPos++;
		}
		this.level = tempString.toString();

//END ENEMY RANDOM GENERATION//
		
//START ITEM RANDOM GENERATION//
		
		try 
		{
			probability = new RandomGen(0,1);
			rand = new RandomGen(minItems, maxItems);
		} catch (Exception e) {}
		
		numItems = rand.randomInt();
		i=0;
		validSpawn = 0;
		stringPos = 0;
		
		while(stringPos < this.level.length())
		{
			if(this.level.charAt(stringPos) == '=' || this.level.charAt(stringPos) == '-')
				validSpawn++;
			stringPos++;
		}
		
		probRate = ((double)this.numItems/(double)validSpawn);
		tempString = new StringBuffer(level);
		i = 0;
		stringPos = 0;
		
		while(stringPos < tempString.length() && i < this.numItems)
		{
			switch(tempString.charAt(stringPos))
			{
				case '\n':
					 break;
				case '-':
					if(tempString.charAt(stringPos+1) == '#')
						break;
					else if(probRate > probability.randomDouble())
					{
						tempString.setCharAt(stringPos, '$');
						stringPos++;
						tempString.insert(stringPos, ranItems.randomChar());
						i++;
					}
					break;
				case '=':
					if(probRate > probability.randomDouble())
					{
						tempString.setCharAt(stringPos, '$');
						stringPos++;
						tempString.insert(stringPos, ranItems.randomChar());
						i++;
					}
					break;
				default:
					break;
			}
			stringPos++;
		}
		this.level = tempString.toString();
//END ITEM RANDOM GENERATION//	

//START OBSTACLE RANDOM GENERATION//

		try 
		{
			probability = new RandomGen(0,1);
			rand = new RandomGen(minObs, maxObs);
		} catch (Exception e) {}
		
		this.numObs = rand.randomInt();	
		stringPos = 0;
		validSpawn = 0;
		
		while(stringPos < this.level.length())
		{
			if(this.level.charAt(stringPos) == '=' || this.level.charAt(stringPos) == '-')
				validSpawn ++;
			stringPos++;
		}
		
		stringPos = 0;
		probRate = ((double)this.numObs/(double)validSpawn);
		tempString= new StringBuffer(this.level);
		i=0;
		
		while(stringPos < tempString.length() && i < this.numObs)
		{
			switch(tempString.charAt(stringPos))
			{
				case '\n':
					break;
				case '=':
					if(probRate > probability.randomDouble())
					{
						tempString.setCharAt(stringPos, '>');
						stringPos++;
						tempString.insert(stringPos, ranObs.randomChar());
						i++;
					}
				case '-':
					if(probRate > probability.randomDouble())
					{
						tempString.setCharAt(stringPos, '>');
						stringPos++;
						tempString.insert(stringPos, ranObs.randomChar());
						i++;
					}
					break;
					 
				 default:
					 break;
			}
			stringPos++;
		}
		this.level = tempString.toString();

//END OBSTACLE RANDOM GENERATION//
		

//Debugging Print check//
//		try {rand. setLimits(0,750);} catch (Exception e) {}
//		System.out.println(rand.randomInt());
		this.print();
//End Debugging check//
		recursiveLevel();
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
			this.north = new Level(true, this.minEnemies, this.maxEnemies, this.enemies, this.minItems, this.maxItems, this.items, 
					this.minObs, this.maxObs, this.obs, this.friendlyNPC, this.npcFriendly, 0, maxDoors.randomInt(), this, 0 , this.yloc - 1, this.xloc );
		if(eBoolDoor)
			this.east = new Level(true, this.minEnemies, this.maxEnemies,  this.enemies, this.minItems, this.maxItems, this.items, 
					this.minObs, this.maxObs, this.obs, this.friendlyNPC, this.npcFriendly, 0, maxDoors.randomInt(), this, 1 , this.yloc, this.xloc + 1 );
		if(sBoolDoor)
			this.south = new Level(true, this.minEnemies, this.maxEnemies,  this.enemies, this.minItems, this.maxItems, this.items, 
					this.minObs, this.maxObs, this.obs, this.friendlyNPC, this.npcFriendly, 0, maxDoors.randomInt(), this, 2 , this.yloc + 1, this.xloc );
		if(wBoolDoor)
			this.west = new Level(true, this.minEnemies, this.maxEnemies,  this.enemies, this.minItems, this.maxItems, this.items, 
					this.minObs, this.maxObs, this.obs, this.friendlyNPC, this.npcFriendly, 0, maxDoors.randomInt(), this, 3 , this.yloc, this.xloc - 1);
	}

	public String getLevel()
	{	return this.level;	}
	
	//possible set incomingDirection to lower case?
	public Level setPlayerEntrance(char incomingDirection) throws InvalidCharacterException
	{
		StringBuffer tempString = new StringBuffer(this.level);
		int stringPos = 0;
		while(stringPos < tempString.length())
		{
			if(tempString.charAt(stringPos)=='x')
				tempString.setCharAt(stringPos, ' ');
			stringPos++;
		
		}
		this.level = tempString.toString();
		
		int i;
		int lineNo;
		int tempLine;
		if(incomingDirection == 'n')
		{
			tempString = new StringBuffer(this.north.level);
			i=0;
			lineNo = 1;
			tempLine = 0;
			splayer = 0;
			while(i < tempString.length())
			{
				if(tempString.charAt(i) == '\n')
					lineNo++;
				else
				{
					if(lineNo == 15)
						tempLine++;
					if(lineNo == 16)
						break;
				}
				i++;
			}	
			if(tempLine % 2 == 0)
				splayer = i - ((tempLine+4)/2);
			else
				splayer = i - ((tempLine - 3)/2);
			tempString.setCharAt(splayer, 'x');
			this.north.level = tempString.toString();
			return this.north;
		}
		else if(incomingDirection == 's')
		{
			tempString = new StringBuffer(this.south.level);
			i = 0;
			lineNo = 1;
			tempLine = 0;
			nplayer = 0;
			while(i < tempString.length())
			{
				if(tempString.charAt(i) == '\n')
					lineNo++;
				else
				{
					if(lineNo == 2)
						tempLine++;
					if(lineNo == 3)
						break;
				}
				i++;
			}
			if(tempLine % 2 == 0)
				nplayer = i - ((tempLine+4)/2);
			else
				nplayer = i - ((tempLine - 3)/2);
			tempString.setCharAt(nplayer, 'x');
			this.south.level = tempString.toString();
			return this.south;
		}
		else if(incomingDirection == 'e')
		{
			tempString = new StringBuffer(this.east.level);
			int numLines = 0;
			i=0;
			while(numLines < 7 && i < tempString.length())
			{
				if(tempString.charAt(i) == '\n')
					numLines++;
				i++;
			}
			wplayer = i + 1;
			tempString.setCharAt(wplayer, 'x');
			this.east.level = tempString.toString();
			return this.east;
		}
		else if(incomingDirection == 'w')
		{
			tempString = new StringBuffer(this.west.level);
			int numLines = 0;
			i=0;
			while(numLines < 8 && i < tempString.length())
			{
				if(tempString.charAt(i) == '\n')
					numLines++;
				i++;
			}
			eplayer = i - 3;
			tempString.setCharAt(eplayer, 'x');
			this.west.level = tempString.toString();
			return this.west;
		}
		else throw new InvalidCharacterException();
	}
	
	public void killItem(int itemNo) throws InvalidItemException
	{
		StringBuffer tempString = new StringBuffer(this.level);
		int charNum = -1;
		int i = 0;
		while(i < tempString.length())
		{
			if(tempString.charAt(i) == '$' || tempString.charAt(i) == '?')
				charNum++;
			if(charNum == itemNo)
			{
				tempString.setCharAt(i, '?');
				tempString.deleteCharAt(i+1);
				this.level = tempString.toString();
				this.print();
				return;
			}
			i++;
		}
		throw new InvalidItemException("Item # " + itemNo + " does not exist in the level");
	}
	
	public void killEnemy(int enemyNo) throws InvalidItemException, InvalidEnemyException
	{
		StringBuffer tempString = new StringBuffer(this.level);
		int charNum = -1;
		int i = 0;
		while(i < tempString.length())
		{
			if(tempString.charAt(i) == 'y' || tempString.charAt(i) == '@')
				charNum++;
			if(charNum == enemyNo)
			{
				tempString.setCharAt(i, '@');
				tempString.deleteCharAt(i+1);
				this.level = tempString.toString();
				this.print();
				return;
			}
			i++;
		}
		throw new InvalidEnemyException("Enemy # " + enemyNo + " does not exist in the level");
	}
	public void print()
	{System.out.println(this.level);}
	
}
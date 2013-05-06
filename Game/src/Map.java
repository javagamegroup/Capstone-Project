
public class Map 
{	
	protected int maxLevWidth = 7;
	protected int maxLevHeight = 7;
	protected Level[][] theLevel = new Level[maxLevHeight][maxLevWidth];
	public static Level level;
	
//  default
//  [1]{[a][b][c][d][e][f][g]}
//  [2]{[a][b][c][d][e][f][g]}
//  [3]{[a][b][c][d][e][f][g]}
//  [4]{[a][b][c][d][e][f][g]}
//  [5]{[a][b][c][d][e][f][g]}
//  [6]{[a][b][c][d][e][f][g]}
//  [7]{[a][b][c][d][e][f][g]}
//  
//  [4][d] center Map and starting Map
//  
//  multidimensional arrays someArray[height][width]
	
	protected Map()//setter for the Map.
	{
		for(int i=0; i<maxLevHeight; i++)
		{
			for(int j=0; j<maxLevWidth; j++)
			{
				theLevel[i][j] = null;
			}
		}
		level = new Level(true, 2);
	}
	
	protected void setMapDimensions(int width, int height)//setter for the Map.
	{
		this.maxLevHeight = height;
		this.maxLevWidth = width;
		
		for(int i=0; i<maxLevHeight; i++)
		{
			for(int j=0; j<maxLevWidth; j++)
			{
				theLevel[i][j] = null;
			}
		}
	}
	
	public int setLevel(Level level, int x, int y)
	{
		try
		{
			if(theLevel[y][x] != null)
				return -1;
			else 
				theLevel[y][x] = level;
			return 0;
		}
		catch(IndexOutOfBoundsException e){return -2;}
	}
	
	protected Level returnMap()
	{
		return theLevel[(maxLevHeight/2) + 1][(maxLevWidth/2)+1];
		
	}
}

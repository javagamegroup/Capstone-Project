
public class Room 
{	
	protected int maxLevWidth = 7;
	protected int maxLevHeight = 7;
	protected Level[][] theLevel = new Level[maxLevHeight][maxLevWidth];
	
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
	
	protected Room()//setter for the room.
	{
		for(int i=0; i<maxLevHeight; i++)
		{
			for(int j=0; j<maxLevWidth; j++)
			{
				theLevel[i][j] = null;
			}
		}
	}
	
	protected void setRoomDimensions(int width, int height)//setter for the room.
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
	
	protected Level returnRoom()
	{
		return theLevel[(maxLevHeight/2) + 1][(maxLevWidth/2)+1];
		
	}
}

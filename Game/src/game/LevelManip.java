package game;

import Exceptions.InvalidCharacterException;

public class LevelManip
{
	public void setPlayerEntrance(char incomingDirection) throws InvalidCharacterException
	{
		GamePanel.level.player = '=';
		GamePanel.level.nplayer = '=';
		GamePanel.level.splayer = '=';
		GamePanel.level.eplayer = '=';
		GamePanel.level.wplayer = '=';
		
		if(incomingDirection == 'n')
		{
			GamePanel.level = GamePanel.level.north;
			GamePanel.level.splayer = 'x';
		}
		else if(incomingDirection == 's')
		{
			GamePanel.level = GamePanel.level.south;
			GamePanel.level.nplayer = 'x';
		}
		else if(incomingDirection == 'e')
		{
			GamePanel.level = GamePanel.level.east;
			GamePanel.level.wplayer = 'x';
		}
		else if(incomingDirection == 'w')
		{
			GamePanel.level = GamePanel.level.west;
			GamePanel.level.eplayer = 'x';
		}
		else throw new InvalidCharacterException();
	}

}

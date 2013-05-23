package game;
public class LevelDriver 
{
	public static void main(String[] args)
	{
		Map theMap = new Map(1,10,new char['a'], 1, 10, new char['a']);
		System.out.println(Map.level.getLevel());
	}
}

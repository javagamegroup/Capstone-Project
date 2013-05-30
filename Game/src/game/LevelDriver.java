package game;
public class LevelDriver 
{
	public static void main(String[] args)
	{
		char[] enemies = new char[2];
		enemies[0] = 'a';
		enemies[1] = 'b';
		char[] items = new char[2];
		items[0] = 'a';
		items[1] = 'b';
		char[] obs = new char[2];
		obs[0] = 'a';
		obs[1] = 'b';
		
		Map theMap = new Map(1,10, enemies, 1, 10, items, 1, 10, obs);
//		try
//		{
//			System.out.println(Map.level.getLevel());
//			System.out.println(Map.level.north.getLevel());
//			System.out.println(Map.level.east.getLevel());
//			System.out.println(Map.level.west.getLevel());
//		}catch(Exception e){e.printStackTrace();}
	}
}

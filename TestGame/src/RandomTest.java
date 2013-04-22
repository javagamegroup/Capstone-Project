import java.lang.reflect.Array;


public class RandomTest {
	public static void main(String[] args)
	{
		try 
		{
			RandomGen x = new RandomGen(0,1);
			System.out.println(x.toString());
			
			Array[] y = new Array[10];
			RandomGen z = new RandomGen(y);
			System.out.println(z.toString());
		} 
		catch (Exception e) {e.printStackTrace();}
	}
}

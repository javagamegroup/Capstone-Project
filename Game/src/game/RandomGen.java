package game;

import java.lang.reflect.Array;
import java.util.Random;

/**@author Sean Ewing
 * This class creates a Random Generation object
 * TODO
 * impment array randomization
 * object randomization option
 * item randomization option
 **/
public class RandomGen
{
	private int ranInt;
	private double ranDouble;
	private long ranLong;
	protected int cpu;
	private Random r;
	private int min, max;
	private int x1,x2,y1,y2 = 0;
	private Array[] x;
	private int[] ranDirection;
	/**
	 * 
	 * @param x
	 */
//	public RandomGen(Array[] x)
//	{
//		r = new Random();
//		this.ranInt = 0;
//		this.ranDouble = 0;
//		this.ranLong = 0;
//		this.max = x.length;
//		this.min = 0;
//		cpu = Runtime.getRuntime().availableProcessors();
//		this.x = x;
//		ranDirection =  new int[4];
//		for(int i = 0; i < 4; i++)
//			ranDirection[i] = i;
//	}//end RandomGen(Array[] x)
//	
//	public RandomGen(int x1, int y1, int x2, int y2)
//	{
//		r = new Random();
//		
//		ranDirection =  new int[4];
//		for(int i = 0; i < 4; i++)
//			ranDirection[i] = i;
//		
//	}
	
	public RandomGen()
	{
		r = new Random();
		this.ranInt = 0;
		this.ranDouble = 0;
		this.ranLong = 0;
		this.max = 1;
		this.min = 0;
		cpu = Runtime.getRuntime().availableProcessors();
		ranDirection =  new int[4];
		for(int i = 0; i < 4; i++)
			ranDirection[i] = i;
	}// end RandomGen()
	
	public RandomGen(int min, int max) throws Exception
	{
		
		r = new Random();
		this.ranInt = 0;
		this.ranDouble = 0;
		this.ranLong = 0;
		this.max = max;
		this.min = min;
		
		if(max >= min) randomize();
		else throw new Exception("Max must be greater than Min");
		
		cpu = Runtime.getRuntime().availableProcessors();
		// seems like this ranDirection array is almost always filled twice
		ranDirection =  new int[4];
		for(int i = 0; i < 4; i++)
			ranDirection[i] = i;
	}//end RandomGen(int min, int max)
	
	public void randomize()
	{
		this.ranInt = Math.round((float)((this.max - this.min) * r.nextDouble() + this.min));
		this.ranDouble = (this.max - this.min) * r.nextDouble() + this.min;
		this.ranLong = Math.round((this.max - this.min) * r.nextDouble() + this.min);
		// filled in genRandom too
		ranDirection =  new int[4];
		for(int i = 0; i < 4; i++)
			ranDirection[i] = i;
		//array's
		//coordinates
	}//end randomize
	
	public void setLimits(int min, int max) throws Exception
	{
		if(min < max && min >= 0)
		{
			this.min = min;
			this.max = max;
		} else throw new Exception("Max must be greater than Min");
	}// end setLimits
	
	public int randomDirection()
	{
		int total = -1;
		for(int i = 0; i < 4; i++)
		{
			if(ranDirection[i] != -1)
				total = i;
		}
		if(total == -1)
			return total;
		
		int loc = this.ranInt = Math.round((float)(total * r.nextDouble()));
		int num = ranDirection[loc];
		
		if(loc == total)
			ranDirection[loc] = -1;
		else
		{
			ranDirection[loc] = ranDirection[total];
			ranDirection[total] = -1;
		}
		return num;
	}
	
	public int randomInt(){randomize(); return ranInt;}//end randomInt
	
	public double randomDouble(){randomize(); return ranDouble;}// end randomDouble
	
	public long randomLong(){randomize(); return ranLong;}//end randomLong
	
	public String toString()
	{
		String nl = System.getProperty("line.separator");
		String x = "-- Random --" + nl +
				"Cores: " + this.cpu + nl +
				"Integer: " + this.ranInt + nl +
				"Long: " + this.ranLong + nl +
				"Double: " + this.ranDouble + nl +
				"Max: " + this.max + nl +
				"Min: " + this.min + nl;
		return x;
	}//end toString
}
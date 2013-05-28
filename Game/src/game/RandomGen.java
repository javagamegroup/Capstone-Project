package game;

import java.lang.reflect.Array;
import java.util.Random;

import Exceptions.MinMaxException;

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
	private int x1, y1, x2, y2 = 0;
	private char someChar;
	private char[] xArr;
	private int[] ranDirection;
	/**
	 * 
	 * @param x
	 */
	public RandomGen(char[] x)
	{
		r = new Random();
		this.ranInt = 0;
		this.ranDouble = 0;
		this.ranLong = 0;
		this.max = x.length;
		this.min = 0;
		this.xArr = new char[x.length];
		for(int i = 0; i < x.length; i++)
			xArr[i] = x[i];
		someChar = xArr[0];
	}//end RandomGen(Array[] x)
	
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

	public RandomGen(int x1, int x2, int y1, int y2) throws MinMaxException
	{
		r = new Random();
		this.x1 = x1;
		this.y1 = y1;
		this.x2 = x2;
		this.y2 = y2;
		ranDirection =  new int[2];
		if(this.x1 > this.x2 || this.y1 > this.y2) 
			throw new MinMaxException("Max must be greater than Min");
		else
		{
			ranDirection[0] = Math.round((float)((this.x2 - this.x1) * r.nextDouble() + this.x1));
			ranDirection[1] = Math.round((float)((this.y2 - this.y1) * r.nextDouble() + this.y1));
		}
	}
	
	public RandomGen(int min, int max) throws MinMaxException
	{
		
		r = new Random();
		this.ranInt = 0;
		this.ranDouble = 0;
		this.ranLong = 0;
		this.max = max;
		this.min = min;
		ranDirection =  new int[4];
		for(int i = 0; i < 4; i++)
			ranDirection[i] = i;
		if(max >= min) randomize();
		else throw new MinMaxException("Max must be greater than Min");
		
		cpu = Runtime.getRuntime().availableProcessors();
	}//end RandomGen(int min, int max)
	
	public void randomize()
	{
		this.ranInt = Math.round((float)((this.max - this.min) * r.nextDouble() + this.min));
		this.ranDouble = (this.max - this.min) * r.nextDouble() + this.min;
		this.ranLong = Math.round((this.max - this.min) * r.nextDouble() + this.min);		
		//array's
		
	}//end randomize
	
	public void randomizeChar()
	{
		this.someChar = xArr[r.nextInt(xArr.length)];
	}//end randomizeCoord
	
	public void randomizeCoord()
	{
		this.ranDirection[0] = Math.round((float)((this.x2 - this.x1) * r.nextDouble() + this.x1));
		this.ranDirection[1] = Math.round((float)((this.y2 - this.y1) * r.nextDouble() + this.y1));
	}//end randomizeCoord
	
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
	
	public int[] randomCoordinates(){randomizeCoord(); return ranDirection;}
	
	public char randomChar(){randomizeChar(); return someChar;}
	
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
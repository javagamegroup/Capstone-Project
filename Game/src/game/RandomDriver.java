package game;

import java.lang.reflect.Array;

import Exceptions.MinMaxException;


public class RandomDriver {
	public static void main(String[] args) throws MinMaxException
	{
		RandomGen r = new RandomGen(1,10,1,20);
		int[] x = new int[2];
		for(int i = 0; i< 10; i++)
		{
			x = r.randomCoordinates();
			System.out.println("x" + i + " = " + x[0]);
			System.out.println("y" + i + " = " + x[1]);
		}

	}
}
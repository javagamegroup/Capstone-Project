package game;

import java.lang.reflect.Array;

import Exceptions.MinMaxException;


public class RandomTest {
	public static void main(String[] args) throws MinMaxException
	{
		RandomGen r = new RandomGen(1,10,1,20);
		for(int i = 0; i< 10; i++)
		{
			System.out.println("x" + i + " = " + r.randomCoordinates()[0]);
			System.out.println("y" + i + " = " + r.randomCoordinates()[1]);
		}

	}
}
package Exceptions;

public class InvalidEnemyException extends Exception
{
	private static final long serialVersionUID = 1L;
	
	public InvalidEnemyException() { super(); }
	public InvalidEnemyException(String message) { super(message); }
	public InvalidEnemyException(String message, Throwable cause) { super(message, cause); }
	public InvalidEnemyException(Throwable cause) { super(cause); }
}

package Exceptions;

public class InvalidCharacterException extends Exception
{
	private static final long serialVersionUID = 1L;
	
	public InvalidCharacterException() { super(); }
	public InvalidCharacterException(String message) { super(message); }
	public InvalidCharacterException(String message, Throwable cause) { super(message, cause); }
	public InvalidCharacterException(Throwable cause) { super(cause); }
}

package Exceptions;

public class InvalidItemException extends Exception
{
	private static final long serialVersionUID = 1L;
	
	public InvalidItemException() { super(); }
	public InvalidItemException(String message) { super(message); }
	public InvalidItemException(String message, Throwable cause) { super(message, cause); }
	public InvalidItemException(Throwable cause) { super(cause); }
}

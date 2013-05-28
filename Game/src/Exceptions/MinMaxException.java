package Exceptions;

public class MinMaxException extends Exception
{
	private static final long serialVersionUID = 1L;
	
	public MinMaxException() { super(); }
	public MinMaxException(String message) { super(message); }
	public MinMaxException(String message, Throwable cause) { super(message, cause); }
	public MinMaxException(Throwable cause) { super(cause); }
}

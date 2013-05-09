package Exceptions;

public class PreviousLevelException extends Exception 
{
	private static final long serialVersionUID = 1L;
	
	public PreviousLevelException() { super(); }
	public PreviousLevelException(String message) { super(message); }
	public PreviousLevelException(String message, Throwable cause) { super(message, cause); }
	public PreviousLevelException(Throwable cause) { super(cause); }
}

package se.jacob.exception;

public class JJGeneralException extends Exception {

	/**
	 * 
	 */
	private static final long serialVersionUID = -6782818579367441104L;

	protected String message;
	protected Exception origException;
	
	public JJGeneralException(String message, Exception ex) {
		this.message = message;
		this.origException = ex;
	}
	
	public String getMessage() {
		return message + "\n" + origException.getMessage();
	}
	
	public String toString() {
		return getMessage();
	}
}

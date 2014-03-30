package se.jacob.exception;

public class SaveFileException extends Exception {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -8839389359905818830L;
	private String message;
	private Exception origException;
	
	public SaveFileException(String message, Exception ex) {
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

package se.jacob.exception;

public class SearchFileException extends JJGeneralException {

	/**
	 * 
	 */
	private static final long serialVersionUID = -4211830051489975235L;
	
	public SearchFileException(String message, Exception ex) {
		super(message, ex);
	}
	
	public String getMessage() {
		String origMess = origException.getMessage();
		return message + (origMess == null ? "" : "\n" + origMess);
	}
}

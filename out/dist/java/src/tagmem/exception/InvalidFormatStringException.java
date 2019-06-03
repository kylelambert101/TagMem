package tagmem.exception;

public class InvalidFormatStringException extends Exception {

	/**
	 * Eclipse told me to add this serialVersionUID 
	 * :[]
	 */
	private static final long serialVersionUID = 1L;

	public InvalidFormatStringException() {
	}
	
	public InvalidFormatStringException(String message) {
		super(message);
	}
	
	public InvalidFormatStringException (Throwable cause) {
        super (cause);
    }

    public InvalidFormatStringException (String message, Throwable cause) {
        super (message, cause);
    }
}

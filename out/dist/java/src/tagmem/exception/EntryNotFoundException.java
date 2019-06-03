package tagmem.exception;

public class EntryNotFoundException extends Exception {

	/**
	 * Eclipse told me to add this serialVersionUID 
	 * :[]
	 */
	private static final long serialVersionUID = 1L;

	public EntryNotFoundException() {
	}
	
	public EntryNotFoundException(String message) {
		super(message);
	}
	
	public EntryNotFoundException (Throwable cause) {
        super (cause);
    }

    public EntryNotFoundException (String message, Throwable cause) {
        super (message, cause);
    }
}

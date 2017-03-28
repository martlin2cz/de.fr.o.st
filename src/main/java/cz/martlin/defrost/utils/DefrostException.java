package cz.martlin.defrost.utils;

/**
 * Exception occured during wherever in the de.fr.o.st application's process.
 * 
 * @author martin
 *
 */
public class DefrostException extends Exception {

	private static final long serialVersionUID = 3132075198299594902L;

	public DefrostException(String message) {
		super(message);
	}

	public DefrostException(Throwable cause) {
		super(cause);
	}

	public DefrostException(String message, Throwable cause) {
		super(message, cause);
	}

}

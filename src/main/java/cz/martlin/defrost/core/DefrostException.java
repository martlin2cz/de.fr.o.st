package cz.martlin.defrost.core;

import java.io.IOException;

/**
 * Exception occured during wherever in the process.
 * @author martin
 *
 */
public class DefrostException extends IOException {

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

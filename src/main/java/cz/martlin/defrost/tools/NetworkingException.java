package cz.martlin.defrost.tools;

import java.io.IOException;

public class NetworkingException extends IOException {

	private static final long serialVersionUID = 3132075198299594902L;

	public NetworkingException(String message) {
		super(message);
	}

	public NetworkingException(Throwable cause) {
		super(cause);
	}

	public NetworkingException(String message, Throwable cause) {
		super(message, cause);
	}

}

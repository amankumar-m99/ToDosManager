package configuration.exception;

public class InvalidDbConfigKeyException extends Exception{

	private static final long serialVersionUID = 1L;

	public InvalidDbConfigKeyException(String message) {
		super(message);
	}

}

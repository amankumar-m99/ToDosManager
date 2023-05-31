package configuration.exception;

public class MissingDbConfigKeyException extends Exception{

	private static final long serialVersionUID = 1L;

	public MissingDbConfigKeyException(String message) {
		super(message);
	}

}

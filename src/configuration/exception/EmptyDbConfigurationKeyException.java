package configuration.exception;

public class EmptyDbConfigurationKeyException extends Exception{

	private static final long serialVersionUID = 1L;

	public EmptyDbConfigurationKeyException(String message) {
		super(message);
	}
}

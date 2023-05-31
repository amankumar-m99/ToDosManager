package utility;

import java.util.Date;

public class ConsoleLogger {
	
	@SuppressWarnings("deprecation")
	public static void logQuery(String query) {
		System.out.print("["+new Date().toLocaleString()+"]");
		System.out.print("Query: ");
		System.err.println(query);
	}

	@SuppressWarnings("deprecation")
	public static void logMessage(String action) {
		System.out.print("["+new Date().toLocaleString()+"]");
		System.out.print("Message: ");
		System.out.println(action);
	}

	@SuppressWarnings("deprecation")
	public static void logException(String message) {
		System.err.print("["+new Date().toLocaleString()+"]");
		System.err.print("Exception: ");
		System.err.println(message);
	}
}

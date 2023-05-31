package configuration;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import configuration.exception.EmptyDbConfigurationKeyException;
import configuration.exception.InvalidDbConfigKeyException;
import configuration.exception.MissingDbConfigKeyException;
import utility.ConsoleLogger;

public final class DbConfiguration {

	private static DbConfiguration dbConfiguration;
	private static boolean showSql = true;

	private final Properties dbProperties;
	private final String namePropertyName = "name";
	private final String ipPropertyName = "ip";
	private final String userPropertyName = "user";
	private final String passwordPropertyName = "password";
	private final String driverClassPropertyName = "driver_class_name";
	private final String showSQLPropertyName = "show_sql";

	private String dbName;
	private String dbIp;
	private String dbUser;
	private String dbPassword;
	private String dbDriverClassName;

	public static DbConfiguration getDbConfiguration() throws EmptyDbConfigurationKeyException, InvalidDbConfigKeyException, MissingDbConfigKeyException {
		if(dbConfiguration == null)
			dbConfiguration = new DbConfiguration();
		return dbConfiguration;
	}

	private DbConfiguration() throws EmptyDbConfigurationKeyException, InvalidDbConfigKeyException, MissingDbConfigKeyException {
		dbProperties = getDbProperties();
		initAllDbDetails();
		verifyAllFieldsAssigned();		
	}

	private Properties getDbProperties() {
		Properties dbProperties = new Properties();
		try {
			InputStream input = DbConfiguration.class.getClassLoader().getResourceAsStream("configuration/dbConfig.properties");
			dbProperties.load(input);
		} catch (IOException e) {
			ConsoleLogger.logException(e.getMessage());
			dbProperties = null;
			System.exit(0);
		} catch (Exception e) {
			ConsoleLogger.logException(e.getMessage());
		}
		return dbProperties;
	}

	private void initAllDbDetails() throws EmptyDbConfigurationKeyException, InvalidDbConfigKeyException {
		for(Object keyObject : dbProperties.keySet()) {
			if(!(keyObject instanceof String))
				continue;
			String key = (String) keyObject;
			String value = dbProperties.getProperty(key);
			if(value == null)
				throw new EmptyDbConfigurationKeyException(key);
			initDbDetail(key, value);
		}
	}

	private void initDbDetail(String key, String value) throws InvalidDbConfigKeyException {
		if(key.equalsIgnoreCase(ipPropertyName))
			dbIp = value;
		else if(key.equalsIgnoreCase(namePropertyName))
			dbName = value;
		else if(key.equalsIgnoreCase(userPropertyName))
			dbUser = value;
		else if(key.equalsIgnoreCase(passwordPropertyName))
			dbPassword = value;
		else if(key.equalsIgnoreCase(driverClassPropertyName))
			dbDriverClassName = value;
		else if(key.equalsIgnoreCase(showSQLPropertyName))
			showSql = Boolean.parseBoolean(value);
		else
			throw new InvalidDbConfigKeyException(key);
	}

	private void verifyAllFieldsAssigned() throws MissingDbConfigKeyException {
		if(dbIp == null)
			throw new MissingDbConfigKeyException(dbIp);
		else if(dbName == null)
			throw new MissingDbConfigKeyException(dbName);
		else if(dbUser == null)
			throw new MissingDbConfigKeyException(dbUser);
		else if(dbPassword == null)
			throw new MissingDbConfigKeyException(dbPassword);
		else if(dbDriverClassName == null)
			throw new MissingDbConfigKeyException(dbDriverClassName);
	}

	public String getDbUrl() {
		String dbUrl = dbIp + "/" + dbName;
		return dbUrl;
	}

	public String getDbUser() {
		return dbUser;
	}

	public String getDbPassword() {
		return dbPassword;
	}

	public String getDbDriverClassName() {
		return dbDriverClassName;
	}

	public static boolean showSql() {
		return showSql;
	}
}

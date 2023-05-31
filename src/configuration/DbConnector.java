package configuration;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.concurrent.ExecutionException;

import controller.AppMainToDo;
import javafx.concurrent.Task;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.stage.Modality;
import utility.AppStaticData;
import utility.ConsoleLogger;

public final class DbConnector {

	private static DbConnector dbConnector;
	private Connection dbConnection;
	private Alert alert;

	private DbConnector() throws InterruptedException, ExecutionException {
		dbConnection = generateNewConnection();
	}

	private Connection generateNewConnection() throws InterruptedException, ExecutionException {
		alert = null;
		Task<Connection> task = getDbConnectorTask();
		task.setOnSucceeded(e->{
			AppMainToDo.getVeil().setVisible(false);
			AppMainToDo.getVeilLabel().textProperty().unbind();
			if(alert != null)
				alert.showAndWait();
		});
		AppMainToDo.getVeil().setVisible(true);
		AppMainToDo.getVeilLabel().textProperty().bind(task.messageProperty());
		Thread t = new Thread(task);
		t.start();
		t.join();
		return task.get();
	}

	public static DbConnector getDbConnector() throws InterruptedException, ExecutionException {
		if(dbConnector == null)
			dbConnector = new DbConnector();
		return dbConnector;
	}

	private Task<Connection> getDbConnectorTask() {
		Task<Connection> task = new Task<Connection>() {
			@Override
			protected Connection call() throws Exception {
				Connection connection;
				DbConfiguration dbConfiguration = DbConfiguration.getDbConfiguration();
				try {
					updateMessage("Loading JDBC driver...");
					loadDriver(dbConfiguration.getDbDriverClassName());
					updateMessage("JDBC driver loaded Successfully.");

					updateMessage("Connecting to database...");
					connection = getConnectionInitialized(dbConfiguration);
					if(connection != null)
						updateMessage("Connected to database.");
					else
						updateMessage("Could not connect to database.");

				} catch (ClassNotFoundException e) {
					connection = null;
					String error = "Error: unable to load driver class " + dbConfiguration.getDbDriverClassName();
					updateMessage(error);
					alert = getDriverLoadErrorAlert(e.getMessage());
				} catch (SQLException e) {
					connection = null;
					String error = "Error: unable to connect to Database";
					updateMessage(error);
					alert = getDbConnectionErrorAlert(e.getMessage());
				}
				return connection;
			}
		};
		return task;
	}

	private Alert getDriverLoadErrorAlert(String message) {
		Alert alert = new Alert(AlertType.ERROR);
		alert.setTitle("Driver error");
		alert.setHeaderText("Could not load Driver");
		alert.setContentText(message);
		alert.initOwner(AppStaticData.getMainStage());
		alert.initModality(Modality.APPLICATION_MODAL);
		return alert;
	}

	private Alert getDbConnectionErrorAlert(String message) {
		Alert alert = new Alert(AlertType.ERROR);
		alert.setTitle("SQL Error");
		alert.setHeaderText("Problem encountered with database");
		alert.setContentText(message);
		alert.initOwner(AppStaticData.getMainStage());
		alert.initModality(Modality.APPLICATION_MODAL);
		return alert;
	}

	public Connection getConnection() throws InterruptedException, ExecutionException {
		if(dbConnection != null)
			return dbConnection;
		dbConnection = generateNewConnection();
		return dbConnection;
	}

	private void loadDriver(String driver) throws ClassNotFoundException {
		Class.forName(driver);
	}

	private Connection getConnectionInitialized(DbConfiguration dbConfiguration) throws SQLException {
		String url = dbConfiguration.getDbUrl();
		String username = dbConfiguration.getDbUser();
		String password = dbConfiguration.getDbPassword();
		Connection conn = DriverManager.getConnection(url, username, password);
		if (conn.isClosed())
			conn = null;
		return conn;
	}

	public void closeConnection() {
		if (dbConnection != null)
			try {
				ConsoleLogger.logMessage("Closing database connection.");
				dbConnection.close();
				ConsoleLogger.logMessage("Database connection closed.");				
			} catch (SQLException e) {
				ConsoleLogger.logException("Error occured while closing database connecttion");
				ConsoleLogger.logException(e.getMessage());
			}
	}
}

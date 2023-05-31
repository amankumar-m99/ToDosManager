package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.util.Collections;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

import configuration.DbConfiguration;
import models.todo.ToDo;
import models.todo.ToDoStatus;
import utility.AppStaticData;
import utility.ConsoleLogger;
import utility.ToDoStatusConverter;

public class Dao {

	private static ToDo insertedToDo;
	private static ToDo updatedToDo;

	private static boolean showSql = DbConfiguration.showSql();

	public static boolean createTable() throws SQLException {
		Connection connection = AppStaticData.getConnection();
		if(!isConnectionAvailable(connection))
			return false;

		String query = QueryGenerator.getTableCreationQuery();
		Statement statement = connection.createStatement();
		if(showSql)
			ConsoleLogger.logQuery(query);
		statement.executeUpdate(query);
		return true;
	}

	public static boolean insertToDo(String title, String description) throws SQLException {
		int id=0;
		Connection connection = AppStaticData.getConnection();
		if(connection == null || connection.isClosed())
			return false;
		String query = QueryGenerator.getTableInsertionQuery();
		java.util.Date utilDate = new java.util.Date();
		Timestamp timestamp = new Timestamp(utilDate.getTime());
		PreparedStatement preparedStatement = connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);
		preparedStatement.setString(1, title);
		preparedStatement.setString(2, description);
		preparedStatement.setString(3, ToDoStatus.ASSIGNED.toString());
		preparedStatement.setTimestamp(4, timestamp);
		preparedStatement.setTimestamp(5, timestamp);
		if(showSql)
			ConsoleLogger.logQuery(query);
		preparedStatement.executeUpdate();
		ResultSet rs = preparedStatement.getGeneratedKeys();
		while(rs.next()) {
			id = rs.getInt(1);
		}
		insertedToDo = new ToDo(id, title, description, ToDoStatus.ASSIGNED, timestamp, timestamp);
		return true;
	}

	public static ToDo getInsertedToDo() {
		return insertedToDo;
	}

	public static boolean updateToDo(ToDo toDo, ToDoStatus status) throws SQLException {
		Connection connection = AppStaticData.getConnection();
		if(!isConnectionAvailable(connection))
			return false;

		String query = QueryGenerator.getTableUpdateQuery();
		Timestamp timestamp = new Timestamp(new Date().getTime());
		PreparedStatement preparedStatement = connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);
		preparedStatement.setString(1, toDo.getTitle());
		preparedStatement.setString(2, toDo.getDescription());
		preparedStatement.setString(3, status.toString());
		preparedStatement.setTimestamp(4, timestamp);
		preparedStatement.setInt(5, toDo.getId());
		if(showSql)
			ConsoleLogger.logQuery(query);
		preparedStatement.executeUpdate();
		toDo.setStatus(status);
		toDo.setModificationTimestamp(timestamp);
		updatedToDo = toDo;
		return true;
	}

	public static ToDo getUpdatedToDo() {
		return updatedToDo;
	}

	public static List<ToDo> getAllToDos() throws SQLException {
		Connection connection = AppStaticData.getConnection();
		if(!isConnectionAvailable(connection))
			return Collections.emptyList();
		String query = QueryGenerator.getSelectAllQuery();
		Statement statement = connection.createStatement();
		ConsoleLogger.logQuery(query);
		ResultSet resultSet = statement.executeQuery(query);
		return getToDosList(resultSet);
	}

	private static List<ToDo> getToDosList(ResultSet resultSet) throws SQLException {
		List<ToDo> toDos = new LinkedList<>();
		while(resultSet.next()) {
			int id = resultSet.getInt(1);
			String title = resultSet.getString(2);
			String description = resultSet.getString(3);
			ToDoStatus status = ToDoStatusConverter.getToDoStausFromString(resultSet.getString(4));
			Timestamp creationTimestamp = resultSet.getTimestamp(5);
			Timestamp modificationTimestamp = resultSet.getTimestamp(6);
			ToDo todo = new ToDo(id, title, description, status, creationTimestamp, modificationTimestamp);
			toDos.add(todo);
		}
		return toDos;
	}

	public static boolean deleteToDo(int id) throws SQLException {
		Connection connection = AppStaticData.getConnection();
		if(!isConnectionAvailable(connection))
			return false;
		String query = QueryGenerator.getTableDeletionQuery();
		PreparedStatement preparedStatement = connection.prepareStatement(query);
		preparedStatement.setInt(1, id);
		if(showSql)
			ConsoleLogger.logQuery(query);
		preparedStatement.executeUpdate();
		return true;
	}

	private static boolean isConnectionAvailable(Connection connection) {
		try {
			if(connection == null || connection.isClosed())
			{
				ConsoleLogger.logException("Connection is null");
				return false;
			}
		} catch (SQLException e) {
			ConsoleLogger.logException(e.getMessage());
			return false;
		}
		return true;
	}
}

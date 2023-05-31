package dao;

import java.util.ArrayList;
import java.util.List;

public class QueryGenerator {

	private static final String tableName = "todos";
	private static List<String> columnList;
	private static String tableCreationQuery;
	private static String tableInsertionQuery;
	private static String tableUpdateQuery;
	private static String tableDeleteQuery;
	
	private static List<String> generateColumnList() {
		List<String> listOfColumns = new ArrayList<>();
		listOfColumns.add("id int auto_increment");
		listOfColumns.add("title varchar(256) not null");
		listOfColumns.add("description text not null");
		listOfColumns.add("status varchar(20) not null");
		listOfColumns.add("date_of_creation TIMESTAMP");
		listOfColumns.add("date_of_modification TIMESTAMP");
		return listOfColumns;
	}

	private static String generateCreateTableQuery() {
		StringBuilder sb = new StringBuilder();
		sb.append("create table if not exists "+ tableName + "(");
		if(columnList == null)
			columnList = generateColumnList();
		for(String column : columnList) {
			sb.append(column + ", ");
		}
		sb.append("PRIMARY KEY(id));");
		return sb.toString();
	}

	private static String generateInsertionQuery() {
		StringBuilder sb = new StringBuilder();
		sb.append("insert into "+ tableName);
		sb.append("(title, description, status, date_of_creation, date_of_modification) ");
		sb.append("values(?,?,?,?,?);");
		return sb.toString();
	}

	private static String generateSelectAllQuery() {
		return String.format("Select * from %s ;", tableName);
	}

	private static String generateUpdateQuery() {
		StringBuilder sb = new StringBuilder();
		sb.append("Update "+ tableName + " set ");
		sb.append("title = ?, ");
		sb.append("description = ?, ");
		sb.append("status = ?, ");
		sb.append("date_of_modification = ? ");
		sb.append("where id=?;");
		return sb.toString();
	}

	private static String generateDeleteQuery() {
		StringBuilder sb = new StringBuilder();
		sb.append("Delete from "+ tableName);
		sb.append(" where id=?;");
		return sb.toString();
	}

	public static String getTableCreationQuery() {
		if(tableCreationQuery == null)
			tableCreationQuery = generateCreateTableQuery();
		return tableCreationQuery;
	}

	public static String getSelectAllQuery() {
		return generateSelectAllQuery();
	}

	public static String getTableInsertionQuery() {
		if(tableInsertionQuery == null)
			tableInsertionQuery = generateInsertionQuery();
		return tableInsertionQuery;
	}

	public static String getTableUpdateQuery() {
		if(tableUpdateQuery == null)
			tableUpdateQuery = generateUpdateQuery();
		return tableUpdateQuery;
	}

	public static String getTableDeletionQuery() {
		if(tableDeleteQuery == null)
			tableDeleteQuery = generateDeleteQuery();
		return tableDeleteQuery;
	}
}

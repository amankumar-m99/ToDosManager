package controller;

import java.sql.SQLException;
import java.util.Collections;
import java.util.List;

import dao.Dao;
import models.todo.ToDo;
import models.todo.ToDoStatus;

public class ToDoController {

	private static ToDoController toDoController;

	private ToDoController() {}

	public static ToDoController getToDoController() {
		if(toDoController == null)
			toDoController = new ToDoController();
		return toDoController;
	}

	/* CREATE OPERATIONS */

	public boolean createTable() {
		try {
			return Dao.createTable();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return false;
	}

	public boolean insertToDo(String title, String description) {
		try {
			return Dao.insertToDo(title, description);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return false;
	}

	public ToDo getInsertedToDo() {
		return Dao.getInsertedToDo();
	}

	/* RETRIEVE OPERATIONS */
	
	public List<ToDo> getAllToDos() {
		try {
			return Dao.getAllToDos();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return Collections.emptyList();
	}

	/* UPDATE OPERATIONS */

	public boolean updateToDo(ToDo toDo, ToDoStatus status) {
		try {
			return Dao.updateToDo(toDo, status);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return false;
	}

	public ToDo getUpdatedToDo() {
		return Dao.getUpdatedToDo();
	}

	/* DELETE OPERATIONS */

	public boolean deleteToDo(int id) {
		try {
			return Dao.deleteToDo(id);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return false;
	}
}

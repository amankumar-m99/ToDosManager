package view.todo.container;

import controller.ToDoController;
import models.todo.ToDoStatus;

public class AssignedToDos extends ToDosContainer {

	public AssignedToDos(ToDoController toDoCardController) {
		super(ToDoStatus.ASSIGNED, toDoCardController);
	}
}

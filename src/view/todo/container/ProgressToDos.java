package view.todo.container;

import controller.ToDoController;
import models.todo.ToDoStatus;

public class ProgressToDos extends ToDosContainer {

	public ProgressToDos(ToDoController toDoCardController) {
		super(ToDoStatus.PROGRESS, toDoCardController);
	}
}

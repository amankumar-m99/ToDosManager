package view.todo.container;

import controller.ToDoController;
import models.todo.ToDoStatus;

public class DoneToDos extends ToDosContainer {

	public DoneToDos(ToDoController toDoCardController) {
		super(ToDoStatus.DONE, toDoCardController);
	}
}

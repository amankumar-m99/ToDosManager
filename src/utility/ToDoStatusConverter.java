package utility;

import models.todo.ToDoStatus;

public class ToDoStatusConverter {
	public static ToDoStatus getToDoStausFromString(String toDoStatusString) {
		if(toDoStatusString.startsWith("A"))
			return ToDoStatus.ASSIGNED;
		else if(toDoStatusString.startsWith("P"))
			return ToDoStatus.PROGRESS;
		return ToDoStatus.DONE;
	}
}

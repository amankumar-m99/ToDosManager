package view.todo.container;

import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.Date;

import controller.ToDoController;
import javafx.geometry.Insets;
import javafx.scene.input.Dragboard;
import javafx.scene.input.TransferMode;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import models.todo.ToDo;
import models.todo.ToDoStatus;
import utility.AppStaticData;
import view.todo.card.ToDoCard;

public abstract class ToDosContainer extends VBox {

	private ToDoStatus status;
	private ToDoController toDoController;

	public ToDosContainer(ToDoStatus status, ToDoController toDoController) {
		this.status = status;
		this.toDoController = toDoController;
		this.setSpacing(15);
		this.setPadding(new Insets(15));
		this.setMinWidth(200);
		this.setDragDroppedEvent();
		this.setDraggedOverEvent();
		VBox.setVgrow(this, Priority.ALWAYS);
	}

	public String getHeadingText() {
		String text = "";
		switch (status) {
		case ASSIGNED: text = "Assigned ToDos"; break;
		case PROGRESS: text = "Progress ToDos"; break;
		case DONE    : text = "Done ToDos"; break;
		}
		return text;
	}

	protected void setDragDroppedEvent() {
		this.setOnDragDropped(event -> {
			ToDoCard toDoCard = AppStaticData.getDraggedToDo();
			event.setDropCompleted(true);
			event.consume();
            if (toDoCard != null) {
            	try {
					this.addToDoCardAtTop(toDoCard, AdditionSource.FRONTEND);
				} catch (SQLException e) {
					e.printStackTrace();
				}
            	AppStaticData.setDraggedToDo(null);
            }
        });
	}

	protected void setDraggedOverEvent() {
		this.setOnDragOver(event -> {
	        Dragboard db = event.getDragboard();
	        if (db.hasString()) {
	        	db.getString();
	        }
	        event.acceptTransferModes(TransferMode.COPY_OR_MOVE);
	        event.consume();
	    });
	}

	public void addToDoCardAtTop(ToDoCard toDoCard, AdditionSource source) throws SQLException {
		this.addToDoCard(0, toDoCard, source);
	}

	public void addToDoCard(int index, ToDoCard toDoCard, AdditionSource source) throws SQLException {
		if(source.equals(AdditionSource.FRONTEND)) {
			if(toDoCard.getToDo().getStatus().equals(status))
				return;
			ToDo toDo = toDoCard.getToDo();
			toDo.setStatus(status);
			toDo.setModificationTimestamp(new Timestamp(new Date().getTime()));
			if(!toDoController.updateToDo(toDo, status))
				return;
			toDoCard.setToDo(toDoController.getUpdatedToDo());
			toDoCard.refreshCardData();
		}
		this.getChildren().add(index, toDoCard);
	}
}

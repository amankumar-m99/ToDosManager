package view.todo;

import java.sql.SQLException;

import controller.ToDoController;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.ScrollPane.ScrollBarPolicy;
import javafx.scene.control.SplitPane;
import models.todo.ToDo;
import models.todo.ToDoStatus;
import view.todo.card.ToDoCard;
import view.todo.container.AdditionSource;
import view.todo.container.AssignedToDos;
import view.todo.container.DoneToDos;
import view.todo.container.ProgressToDos;
import view.todo.container.ToDosContainer;

public class ToDosView {

	private static AssignedToDos assignedToDos;
	private static ProgressToDos progressToDos;
	private static DoneToDos doneToDos;
	private SplitPane headerPane;
	private SplitPane toDosPane;
	private ScrollPane allToDosView;
	private ToDoController toDoController;

	public ToDosView(ToDoController toDoController) {
		this.toDoController = toDoController;
		assignedToDos = new AssignedToDos(toDoController);
		progressToDos = new ProgressToDos(toDoController);
		doneToDos = new DoneToDos(toDoController);
		initAllToDosView();
		initHeaderPane();
	}

	private void initAllToDosView() {
		allToDosView = new ScrollPane();
		toDosPane = new SplitPane();
		toDosPane.getItems().add(assignedToDos);
		toDosPane.getItems().add(progressToDos);
		toDosPane.getItems().add(doneToDos);
		toDosPane.prefHeightProperty().bind(allToDosView.heightProperty().subtract(5.0));
		toDosPane.prefWidthProperty().bind(allToDosView.widthProperty());
		allToDosView.setContent(toDosPane);
		allToDosView.setHbarPolicy(ScrollBarPolicy.NEVER);
	}

	private void initHeaderPane() {
		headerPane = new SplitPane();
		headerPane.getItems().add(getHeadingLabel(assignedToDos));
		headerPane.getItems().add(getHeadingLabel(progressToDos));
		headerPane.getItems().add(getHeadingLabel(doneToDos));
	}

	private Label getHeadingLabel(ToDosContainer toDosContainer) {
		Label label = new Label(toDosContainer.getHeadingText());
		label.setStyle("-fx-font-weight:bold");
		label.setAlignment(Pos.BASELINE_CENTER);
		label.minWidthProperty().bind(toDosContainer.widthProperty());
		label.prefWidthProperty().bind(toDosContainer.widthProperty());
		label.maxWidthProperty().bind(toDosContainer.widthProperty());
		return label;
	}

	public Node getHeader() {
		return headerPane;
	}

	public Node getAllToDosView() {
		return allToDosView;
	}

	public void resetDividerPositions() {
		double width = toDosPane.getWidth();
		assignedToDos.setPrefWidth(width/3);
		progressToDos.setPrefWidth(width/3);
		doneToDos.setPrefWidth(width/3);
	}

	public void addToDoCard(ToDo toDo, AdditionSource source) throws SQLException {
		ToDosContainer toDosContainer = getContainerByStatus(toDo.getStatus());
		if(toDosContainer != null) {
			toDosContainer.addToDoCard(0, new ToDoCard(toDo, toDoController), source);
		}
	}

	private ToDosContainer getContainerByStatus(ToDoStatus status) {
		switch (status) {
		case ASSIGNED: return assignedToDos;
		case PROGRESS: return progressToDos;
		case DONE: return doneToDos;
		}
		return null;
	}

	public static AssignedToDos getAssignedToDosContainer() {
		return assignedToDos;
	}

	public static ProgressToDos getProgressToDosContainer() {
		return progressToDos;
	}

	public static DoneToDos getDoneToDosContainer() {
		return doneToDos;
	}
}

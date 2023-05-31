package view.homepage;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.concurrent.ExecutionException;

import configuration.DbConnector;
import controller.ToDoController;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.geometry.Insets;
import javafx.geometry.Orientation;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Separator;
import javafx.scene.control.Tooltip;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import models.todo.ToDo;
import utility.AppStaticData;
import utility.ConsoleLogger;
import view.about.AboutDialog;
import view.todo.ToDosView;
import view.todo.container.AdditionSource;
import view.todo.form.FormMode;
import view.todo.form.ToDoForm;

public class HomePage extends BorderPane {
	private ToDosView toDosView;
	private Button connectionIndicatorButton;
	private ToDoController toDoController;
	private SimpleBooleanProperty dbConnectivity = new SimpleBooleanProperty(false);

	public HomePage(ToDoController toDoController) throws SQLException, InterruptedException, ExecutionException {
		this.toDoController = toDoController;
		loadView();
		loadDb();
	}

	private void loadView() {
		this.toDosView = new ToDosView(toDoController);
		setTop(toDosView.getHeader());
		setCenter(toDosView.getAllToDosView());
		setRight(getControlPallet());
	}

	private void loadDb() throws SQLException, InterruptedException, ExecutionException {
		if(isDbConnected()) {
			ConsoleLogger.logMessage("Database connected.");
			dbConnectivity.setValue(true);
			connectionIndicatorButton.setStyle("-fx-background-color: Green;");
			connectionIndicatorButton.getTooltip().setText("Database Connected");
			ConsoleLogger.logMessage("Creating table...");
			if(toDoController.createTable())
				ConsoleLogger.logMessage("Table created.");
			else
				ConsoleLogger.logMessage("Table could not be created.");
			ConsoleLogger.logMessage("Populating todos from database.");
			populateToDos();
			toDosView.resetDividerPositions();
			ConsoleLogger.logMessage("Todos populated.");
		}
		else {
			ConsoleLogger.logMessage("Database not connected.");
			connectionIndicatorButton.setStyle("-fx-background-color: Red;");
			connectionIndicatorButton.getTooltip().setText("Unconnected");
		}
	}

	private boolean isDbConnected() throws SQLException, InterruptedException, ExecutionException{
		Connection connection = DbConnector.getDbConnector().getConnection();
		if(connection != null) {
			AppStaticData.setConnection(connection);
			return true;
		}
		Alert a = new Alert(AlertType.ERROR);
		a.setTitle("Connection Error");
		a.setHeaderText("No Connection");
		a.setContentText("Couldn't connect to Database!");
		a.initModality(Modality.APPLICATION_MODAL);			
		a.setOnShowing(e->{
			//it was needed bcz this method is executed before the scene is set in main stage
			//it causes null pointer exception as scene is not found
			a.initOwner(AppStaticData.getMainStage());
		});
		a.showAndWait();
		return false;
	}

	private void populateToDos() throws SQLException {
		for(ToDo toDo: toDoController.getAllToDos()) {
			this.toDosView.addToDoCard(toDo, AdditionSource.BACKEND);
		}
	}

	private Node getControlPallet() {
		Pane vXPane = new Pane();
		VBox.setVgrow(vXPane, Priority.ALWAYS);
		VBox vbox = new VBox(15);
		vbox.setPadding(new Insets(10));
		vbox.getChildren().add(getRefreshButton());
		vbox.getChildren().add(getcomposeButton());
		vbox.getChildren().add(getResetDividerPostionButton());
		vbox.getChildren().add(getAboutButton());
		vbox.getChildren().add(vXPane);
		vbox.getChildren().add(getResizeControl());
		vbox.getChildren().add(new Separator(Orientation.HORIZONTAL));
		vbox.getChildren().add(getConnectionIndicator());
		return vbox;
	}

	private Node getcomposeButton() {
		Button composeButton = new Button("Compose New Todo");
		composeButton.disableProperty().bind(dbConnectivity.not());
		composeButton.setOnAction(e->{
			try {
				composeNewToDo();
			} catch (SQLException e1) {
				e1.printStackTrace();
			}
		});
		return composeButton;
	}

	private void composeNewToDo() throws SQLException {
		ToDoForm toDoComposeForm = new ToDoForm(AppStaticData.getMainStage(), Modality.APPLICATION_MODAL, FormMode.ADD, null, toDoController);
		toDoComposeForm.showAndWait();
		ToDo toDo = toDoComposeForm.getToDo();
		if(toDo == null)
			return;
		toDosView.addToDoCard(toDo, AdditionSource.BACKEND);
	}

	private Node getRefreshButton() {
		ImageView imageView = new ImageView(new Image("resources/refresh.png"));
		imageView.setPreserveRatio(true);
		imageView.setFitHeight(25);
		Label refresh = new Label("Refresh");
		refresh.setGraphic(imageView);
		refresh.setTooltip(new Tooltip("Reload Data"));
		refresh .setOnMouseClicked(e->{
			loadView();
			try {
				loadDb();
			} catch (SQLException e1) {
				e1.printStackTrace();
			} catch (InterruptedException e1) {
				e1.printStackTrace();
			} catch (ExecutionException e1) {
				e1.printStackTrace();
			}
		});
		return refresh;
	}

	private Node getAboutButton() {
		Button aboutBtn = new Button("About");
		aboutBtn.setMaxWidth(Double.MAX_VALUE);
		aboutBtn.setOnAction(e->new AboutDialog(AppStaticData.getMainStage()).showAndWait());
		return aboutBtn;
	}

	private Node getResizeControl() {
		Button minusButton = new Button("-");
		minusButton.setOnAction(e->minusClicked());
		
		Button resetButton = new Button("Reset");
		resetButton.setOnAction(e->resetClicked());

		Button plusButton = new Button("+");
		plusButton.setOnAction(e->plusClicked());
		HBox hbox = new HBox(0, minusButton, resetButton, plusButton);
		return hbox;
	}

	private void minusClicked() {
		SimpleDoubleProperty property = AppStaticData.getCardHeightProperty();
		if(property.getValue() < 100)
			return;
		property.setValue(property.getValue()-10);
	}

	private void resetClicked() {
		SimpleDoubleProperty property = AppStaticData.getCardHeightProperty();
		property.setValue(150);
	}

	private void plusClicked() {
		SimpleDoubleProperty property = AppStaticData.getCardHeightProperty();
		if(property.getValue() > 200)
			return;
		property.setValue(property.getValue()+10);
	}

 	private Node getResetDividerPostionButton() {
		Button resetDividerPostionButton = new Button("Reset Dividers Position");
		resetDividerPostionButton.setOnAction(e->toDosView.resetDividerPositions());
		return resetDividerPostionButton;
	}

	private Node getConnectionIndicator() {
		Label label = new Label("Connection Status: ");
		label.setAlignment(Pos.CENTER_LEFT);
		connectionIndicatorButton = new Button();
		connectionIndicatorButton.setTooltip(new Tooltip("Unconnected"));
		connectionIndicatorButton.setStyle("-fx-background-color: Red;");
		connectionIndicatorButton.prefHeight(label.getPrefHeight());
		connectionIndicatorButton.prefWidthProperty().bind(connectionIndicatorButton.prefHeightProperty());
		HBox hbox = new HBox(10, label, connectionIndicatorButton);
		return hbox;
	}
}

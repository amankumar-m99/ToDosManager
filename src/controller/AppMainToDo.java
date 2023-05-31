package controller;

import java.sql.SQLException;
import java.util.Optional;
import java.util.concurrent.ExecutionException;

import configuration.DbConnector;
import javafx.application.Application;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressIndicator;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.StackPane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import utility.AppStaticData;
import view.homepage.HomePage;

public class AppMainToDo extends Application{

	private HomePage homePage;
	private static Node veil;
	private static Label veilLabel;

	public static void main(String[] args) {
		launch(args);
	}

	@Override
	public void start(Stage primaryStage) throws Exception {
		AppStaticData.setMainStage(primaryStage);
		initVeil();
		Scene scene = new Scene(getInitScene(), 1000, 600);
		primaryStage.setTitle(AppStaticData.getAppName());
		primaryStage.getIcons().add(AppStaticData.getAppIcon());
		primaryStage.setScene(scene);
		primaryStage.setOnCloseRequest(e->{
			try {
				destroyApp(e);
			} catch (InterruptedException | ExecutionException e1) {
				e1.printStackTrace();
			}
		});
		primaryStage.showingProperty().addListener(new ChangeListener<Boolean>() {
		    @Override
		    public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) {
		        if (newValue) {
//		        	primaryStage.setScene(scene);
//		            observable.removeListener(this);
		        }
		    }
		});
		primaryStage.setMaximized(true);
		primaryStage.show();
	}

	private void destroyApp(WindowEvent e) throws InterruptedException, ExecutionException {
		Alert alert = new Alert(AlertType.CONFIRMATION);
		alert.initModality(Modality.APPLICATION_MODAL);
		alert.initOwner(AppStaticData.getMainStage());
		alert.setTitle("Confirm exit");
		alert.setHeaderText("Close the connection and exit ?");
		Optional<ButtonType> response = alert.showAndWait();
		if(response.isPresent() && response.get().equals(ButtonType.OK))
			DbConnector.getDbConnector().closeConnection();
		else
			e.consume();
	}

	private Parent getInitScene() throws SQLException, InterruptedException, ExecutionException {
		ToDoController toDoController = ToDoController.getToDoController();
		homePage = new HomePage(toDoController);
		StackPane stackPane = new StackPane(homePage, veil);
		return stackPane;
	}

	private static void initVeil() {
		ProgressIndicator progressIndicator = new ProgressIndicator();
//		progressIndicator.setPrefWidth(20);
		progressIndicator.setMaxWidth(100);
//		HBox progressHBox = new HBox(progressIndicator);
//		HBox.setHgrow(progressHBox, Priority.ALWAYS);

		veilLabel = new Label("Connecting...Please Wait.");
		veilLabel.setStyle("-fx-font-weight:bold;-fx-font-size:20;-fx-text-fill:white;");
//		label.setAlignment(Pos.BASELINE_CENTER);
		HBox labelHBox = new HBox(veilLabel);
		HBox.setHgrow(labelHBox, Priority.ALWAYS);

		veil = new StackPane(progressIndicator, veilLabel);
//		veil = new VBox(20, progressHBox, labelHBox);
		veil.setOnMouseClicked(e-> e.consume());
//		veil.setVisible(false);
		veil.setStyle("-fx-background-color: rgba(0,0,0,0.5);");
	}

	public static Node getVeil() {
		return veil;
	}

	public static Label getVeilLabel() {
		return veilLabel;
	}
}
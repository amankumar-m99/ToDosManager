package utility;

import java.sql.Connection;

import javafx.beans.property.SimpleDoubleProperty;
import javafx.scene.image.Image;
import javafx.scene.layout.Border;
import javafx.scene.layout.BorderStroke;
import javafx.scene.layout.BorderStrokeStyle;
import javafx.scene.layout.BorderWidths;
import javafx.scene.layout.CornerRadii;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import view.todo.card.ToDoCard;

public class AppStaticData {
	
	private static final String APP_NAME= "ToDos Manager";
	private static final Image APP_ICON = new Image("/resources/appicon.png");
	private static Stage mainStage;
	private static ToDoCard draggedToDoCard;
	private static Connection connection;
	private static Border componentBorder = new Border(new BorderStroke(Color.DARKGRAY, BorderStrokeStyle.SOLID, new CornerRadii(10, 10, 10, 10,false), new BorderWidths(2)));
	private static SimpleDoubleProperty cardHeightProperty = new SimpleDoubleProperty(150.0);

	public static Stage getMainStage() {
		return mainStage;
	}

	public static void setMainStage(Stage mainStage) {
		AppStaticData.mainStage = mainStage;
	}

	public static ToDoCard getDraggedToDo() {
		return draggedToDoCard;
	}	

	public static void setDraggedToDo(ToDoCard draggedToDoCard) {
		AppStaticData.draggedToDoCard = draggedToDoCard;
	}

	public static double getScreenHeight() {
		return java.awt.Toolkit.getDefaultToolkit().getScreenSize().getHeight();
	}
	
	public static double getScreenWidth() {
		return java.awt.Toolkit.getDefaultToolkit().getScreenSize().getWidth();
	}

	public static Image getAppIcon() {
		return APP_ICON;
	}

	public static String getAppName() {
		return APP_NAME;
	}

	public static Border getComponentBorder() {
		return componentBorder;
	}

	public static Connection getConnection() {
		return connection;
	}

	public static void setConnection(Connection connection) {
		AppStaticData.connection = connection;
	}

	public static SimpleDoubleProperty getCardHeightProperty() {
		return cardHeightProperty;
	}
}

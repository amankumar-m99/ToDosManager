package view.todo.card;

import java.sql.SQLException;
import java.util.Optional;

import controller.ToDoController;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.geometry.Side;
import javafx.scene.Cursor;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.Dialog;
import javafx.scene.control.Label;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuItem;
import javafx.scene.control.Tooltip;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.ClipboardContent;
import javafx.scene.input.ContextMenuEvent;
import javafx.scene.input.Dragboard;
import javafx.scene.input.TransferMode;
import javafx.scene.layout.Border;
import javafx.scene.layout.BorderStroke;
import javafx.scene.layout.BorderStrokeStyle;
import javafx.scene.layout.BorderWidths;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.stage.Modality;
import models.todo.ToDo;
import models.todo.ToDoStatus;
import utility.AppStaticData;
import view.todo.ToDosView;
import view.todo.container.AdditionSource;
import view.todo.container.ToDosContainer;
import view.todo.details.ToDoDetails;
import view.todo.form.FormMode;
import view.todo.form.ToDoForm;

public class ToDoCard extends VBox{
	private static Border outerBorder = new Border(new BorderStroke(Color.BLACK,BorderStrokeStyle.SOLID, new CornerRadii(10), new BorderWidths(2.5)));
	private static Border headBorder = new Border(new BorderStroke(Color.GRAY,BorderStrokeStyle.SOLID, new CornerRadii(5), new BorderWidths(0,0,1.5,0)));
	private static Border footerBorder = new Border(new BorderStroke(Color.GRAY,BorderStrokeStyle.SOLID, new CornerRadii(5), new BorderWidths(1.5,0,0,0)));

	private ToDo toDo;
	private ImageView moreImageView;
	private ToDoController toDoController;
	private Label titleLabel;
	private Label descriptionLabel;	
	private Label modificationDateLabel;	

	public ToDoCard(ToDo toDo, ToDoController toDoController) {
		this.toDo = toDo;
		this.toDoController = toDoController;
		this.titleLabel = new Label();
		this.descriptionLabel = new Label();
		this.modificationDateLabel = new Label();
		initMoreImageView();
		initCard();
		makeDraggable();
	}

	private void initMoreImageView() {
		moreImageView = new ImageView(new Image("resources/more.png"));
		moreImageView.setPreserveRatio(true);
		moreImageView.setFitHeight(20);
	}

	private void initCard() {
		this.setPadding(new Insets(8));
		this.setSpacing(5);
		this.setBorder(outerBorder);
		this.setStyle("-fx-background-color: white;");
		this.getChildren().addAll(getHeader(), getBody(), getFooter());
	}

	private Node getHeader() {
		refreshTitle();
		titleLabel.setAlignment(Pos.CENTER_LEFT);
		titleLabel.setStyle("-fx-font-weight:bold;");
		HBox hbox = new HBox(10, titleLabel);
		hbox.setPadding(new Insets(10));
		hbox.setBorder(headBorder);
		HBox.setHgrow(hbox, Priority.ALWAYS);
		hbox.setOnMouseClicked(e->editToDo());
		hbox.setOnMouseEntered(e->hbox.setCursor(Cursor.HAND));
		hbox.setOnMouseExited(e->hbox.setCursor(Cursor.DEFAULT));
		return hbox;
	}

	private Node getBody() {
		refreshDescription();
		descriptionLabel.setWrapText(true);
		HBox hbox = new HBox(descriptionLabel);
		HBox.setHgrow(hbox, Priority.ALWAYS);
		hbox.minHeightProperty().bind(AppStaticData.getCardHeightProperty());
		hbox.prefHeightProperty().bind(AppStaticData.getCardHeightProperty());
		hbox.maxHeightProperty().bind(AppStaticData.getCardHeightProperty());
		hbox.setOnMouseClicked(e->editToDo());
		hbox.setOnMouseEntered(e->hbox.setCursor(Cursor.HAND));
		hbox.setOnMouseExited(e->hbox.setCursor(Cursor.DEFAULT));
		return hbox;
	}

	private Node getFooter() {
		refreshDate();
		Pane expandPane = new Pane();
		HBox.setHgrow(expandPane, Priority.ALWAYS);
		
		Label moreMenu = new Label();
		moreMenu.setGraphic(moreImageView);
		setContextMenuFunctionalities(moreMenu);
		moreMenu.setTooltip(new Tooltip("More menu"));
		moreMenu.setOnMouseEntered(e->moreMenu.setCursor(Cursor.HAND));
		moreMenu.setOnMouseExited(e->moreMenu.setCursor(Cursor.DEFAULT));
		moreMenu.setOnMouseClicked(e-> showCardContextMenu(moreMenu, moreMenu.getContextMenu(), null));
		moreMenu.setOnContextMenuRequested(e-> showCardContextMenu(moreMenu, moreMenu.getContextMenu(), e));
		
		HBox hbox = new HBox(10, moreMenu, expandPane, modificationDateLabel);
		hbox.setPadding(new Insets(10));
		hbox.setBorder(footerBorder);
		HBox.setHgrow(hbox, Priority.ALWAYS);
		return hbox;
	}

	private void showCardContextMenu(Node node, ContextMenu contextMenu, ContextMenuEvent e) {
		if(e != null)
			e.consume();
		if(contextMenu == null)
			return;
		Menu moveMenu = (Menu) contextMenu.getItems().get(0);
		resetMoveToMenu(moveMenu);
		contextMenu.show(node, Side.TOP, -10, 0);
	}

	private void setContextMenuFunctionalities(Label moreMenu) {
		Menu moveToMenu = new Menu("Move to");
		MenuItem moveToAssigned = new MenuItem("Assigned");
		moveToAssigned.setUserData(ToDoStatus.ASSIGNED);
		moveToAssigned.setOnAction(e->moveToMenuClicked(ToDoStatus.ASSIGNED));

		MenuItem moveToProgress = new MenuItem("Progress");
		moveToProgress.setUserData(ToDoStatus.PROGRESS);
		moveToProgress.setOnAction(e->moveToMenuClicked(ToDoStatus.PROGRESS));

		MenuItem moveToDone = new MenuItem("Done");
		moveToDone.setUserData(ToDoStatus.DONE);
		moveToDone.setOnAction(e->moveToMenuClicked(ToDoStatus.DONE));

		moveToMenu.getItems().addAll(moveToAssigned, moveToProgress, moveToDone);

		MenuItem editMenuItem = new MenuItem("Edit");
		editMenuItem.setOnAction(e-> editToDo());

		MenuItem deleteMenuItem = new MenuItem("Delete");
		deleteMenuItem.setOnAction(e->{
			try {
				if(deleteToDoFromDatabase())
					deleteToDoFromContainer();
			} catch (SQLException e1) {
				e1.printStackTrace();
			}
		});

		MenuItem detailsMenuItem = new MenuItem("Details");
		detailsMenuItem.setOnAction(e->showTodoDetails());

		ContextMenu contextMenu = new ContextMenu();
		contextMenu.getItems().addAll(moveToMenu, editMenuItem, deleteMenuItem, detailsMenuItem);
		moreMenu.setContextMenu(contextMenu);
	}

	private void moveToMenuClicked(ToDoStatus status) {
		try {
			moveToContainer(status);
		} catch (SQLException e1) {
			e1.printStackTrace();
		}
	}

	private void moveToContainer(ToDoStatus status) throws SQLException {
		ToDosContainer container = null;
		switch(status) {
		case ASSIGNED: container = ToDosView.getAssignedToDosContainer(); break;
		case PROGRESS: container = ToDosView.getProgressToDosContainer(); break;
		case DONE: container = ToDosView.getDoneToDosContainer(); break;
		}
		deleteToDoFromContainer();
		container.addToDoCardAtTop(this, AdditionSource.FRONTEND);
	}

 	private void editToDo() {
		new ToDoForm(AppStaticData.getMainStage(), Modality.APPLICATION_MODAL, FormMode.EDIT, this.toDo, toDoController).showAndWait();
		refreshCardData();
	}

	private void deleteToDoFromContainer() {
		Node node = this.getParent();
		if(!(node instanceof ToDosContainer))
			return;
		ToDosContainer container = (ToDosContainer) node;
		container.getChildren().remove(this);
	}

	private boolean deleteToDoFromDatabase() throws SQLException {
		Node node = this.getParent();
		if(!(node instanceof ToDosContainer))
			return false;
		if(!confirmDeletion())
			return false;
		if(!toDoController.deleteToDo(this.toDo.getId()))
			return false;
		return true;
	}

	private boolean confirmDeletion() {
		Alert alert = new Alert(AlertType.CONFIRMATION);
		alert.setTitle("Delete ToDo ?");
		alert.setHeaderText("Delete todo '"+ this.toDo.getTitle()+"'");
		alert.setContentText("This action can't be reversed!");
		alert.initOwner(AppStaticData.getMainStage());
		Optional<ButtonType> result = alert.showAndWait();
		if(result.isPresent() && result.get().equals(ButtonType.OK))
			return true;
		return false;
	}

	private void showTodoDetails() {
		Dialog<ButtonType> detailsDialog = getDetailsDialog();
		detailsDialog.showAndWait();
	}

	private Dialog<ButtonType> getDetailsDialog() {
		return new ToDoDetails(AppStaticData.getMainStage(), Modality.APPLICATION_MODAL, toDo);
	}

	private void makeDraggable() {
		this.setOnDragDetected(event -> {
			Dragboard dragboard = this.startDragAndDrop(TransferMode.MOVE);
			dragboard.setDragView(this.snapshot(null, null));
            ClipboardContent clipboardContent = new ClipboardContent();            
            clipboardContent.putString("");
            dragboard.setContent(clipboardContent);
            AppStaticData.setDraggedToDo(this);
            event.consume();
        });
	}

	private void resetMoveToMenu(Menu menu) {
		for(MenuItem menuItem : menu.getItems()) {
			if(menuItem.getUserData().equals(toDo.getStatus()))
				menuItem.setVisible(false);
			else
				menuItem.setVisible(true);
		}
	}

	public void refreshCardData() {
		refreshTitle();
		refreshDescription();
		refreshDate();
	}

	private void refreshTitle() {
		titleLabel.setText(toDo.getTitle());
	}

	private void refreshDescription() {
		descriptionLabel.setText(toDo.getDescription());
	}

	private void refreshDate() {
		modificationDateLabel.setText(toDo.getModificationTimestampAsString());
	}

	public ToDo getToDo() {
		return toDo;
	}

	public void setToDo(ToDo toDo) {
		this.toDo = toDo;
	}
}

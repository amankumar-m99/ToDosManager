package view.todo.form;

import java.sql.SQLException;

import controller.ToDoController;
import javafx.event.ActionEvent;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Dialog;
import javafx.scene.control.DialogPane;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Window;
import models.todo.ToDo;

public class ToDoForm extends Dialog<ButtonType>{

	private ToDo toDo;
	private FormMode mode;
	private TextField titleTextField;
	private TextArea descriptionTextArea;
	private ToDoController toDoController;

	public ToDoForm(Window owner, Modality modality, FormMode mode, ToDo toDo, ToDoController toDoController){
		this.mode = mode;
		this.toDo = toDo;
		this.toDoController = toDoController;
		this.initOwner(owner);
		this.initModality(modality);
		this.setDialogPane(initDialogPane());
		this.setTitle(generateTitle());
		this.setResizable(true);
	}

	private String generateTitle() {
		String title = "";
		switch(mode) {
		case ADD: title = "Compose a new Todo"; break;
		case EDIT: title = "Edit Todo"; break;
		}
		return title;
	}

	private DialogPane initDialogPane() {
		DialogPane dialogPane = new DialogPane();
		dialogPane.setContent(getFormContent());
		dialogPane.getButtonTypes().addAll(ButtonType.NEXT, ButtonType.CLOSE);

		Button nextButton = (Button) dialogPane.lookupButton(ButtonType.NEXT);
		nextButton.addEventFilter(ActionEvent.ACTION, event->{
			try {
				if(!submit())
					event.consume();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		});
		if(toDo != null)
			nextButton.setText("Save");
		else
			nextButton.setText("Add");

		return dialogPane;
	}

	private Node getFormContent() {
		VBox vbox = new VBox(20, getTitleBox(), getDescriptionBox());
		return vbox;
	}

	private Node getTitleBox() {
		Label label = new Label("Title:");
		titleTextField = new TextField();
		if(toDo != null)
			titleTextField.setText(toDo.getTitle());
		VBox vbox = new VBox(10, label, titleTextField);
		return vbox;
	}

	private Node getDescriptionBox() {
		Label label = new Label("Description:");
		descriptionTextArea = new TextArea();
		if(toDo != null)
			descriptionTextArea.setText(toDo.getDescription());
		descriptionTextArea.setMaxHeight(Double.MAX_VALUE);
		VBox.setVgrow(descriptionTextArea, Priority.ALWAYS);
		VBox vbox = new VBox(10, label, descriptionTextArea);
		return vbox;
	}

	private boolean submit() throws SQLException{
		String formTitleText = titleTextField.getText().trim();
		String formDescriptionText = descriptionTextArea.getText().trim();

		if(!isTextAvailable(formTitleText))
			return false;

		if(!isTextAvailable(formDescriptionText))
			return false;
		Alert alert = new Alert(AlertType.INFORMATION);
		alert.initOwner(this.getOwner());
		alert.initModality(Modality.APPLICATION_MODAL);
		switch (mode) {
		case ADD:
			if(toDoController.insertToDo(formTitleText, formDescriptionText)) {
				toDo = toDoController.getInsertedToDo();
				alert.setTitle("Record saved");
				alert.setHeaderText("Record inserted successfully.");
				alert.show();
			}
			break;
		case EDIT:
			String currentTitleText = toDo.getTitle();
			String currentDescriptionText = toDo.getDescription();
			if(!currentTitleText.equals(formTitleText))
				toDo.setTitle(formTitleText);
			if(!currentDescriptionText.equals(formDescriptionText))
				toDo.setDescription(formDescriptionText);
			if(toDoController.updateToDo(toDo, toDo.getStatus())) {
				alert.setTitle("Record saved");
				alert.setHeaderText("Record saved successfully.");
				alert.show();
			}
			break;
		}
		return true;
	}

	private boolean isTextAvailable(String text) {
		if(text == null || text.trim().isEmpty())
			return false;
		return true;
	}

	public ToDo getToDo() {
		return toDo;
	}
}

package view.todo.details;

import java.util.LinkedHashMap;
import java.util.Map;

import javafx.event.ActionEvent;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Dialog;
import javafx.scene.control.DialogPane;
import javafx.scene.control.Label;
import javafx.scene.input.Clipboard;
import javafx.scene.input.ClipboardContent;
import javafx.scene.layout.GridPane;
import javafx.stage.Modality;
import javafx.stage.Window;
import models.todo.ToDo;
import models.todo.ToDoStatus;

public class ToDoDetails extends Dialog<ButtonType>{

	private ToDo toDo;
	private Map<String, String> detailsMap;

	public ToDoDetails(Window owner, Modality modality, ToDo toDo) {
		this.toDo = toDo;
		this.detailsMap = getInitDetailsMap();
		this.setTitle("ToDo Details");
		this.setDialogPane(getDetailsDialogPane(owner, modality));
		this.initOwner(owner);
		this.initModality(modality);
	}

	private Map<String, String> getInitDetailsMap() {
		int id = toDo.getId();
		ToDoStatus status = toDo.getStatus();
		String dateOfCreation = toDo.getCreationTimestampAsString();
		String dateOfModification = toDo.getModificationTimestampAsString();
		int titleLength = toDo.getTitle().length();
		int descriptionLength = toDo.getDescription().length();

		Map<String, String> map = new LinkedHashMap<>();
		map.put("ID", String.valueOf(id));
		map.put("Status", String.valueOf(status));
		map.put("Date of Creation", dateOfCreation);
		map.put("Date of Modification", dateOfModification);
		map.put("Length of title", String.valueOf(titleLength));
		map.put("Length of Description", String.valueOf(descriptionLength));

		return map;
	}

	private DialogPane getDetailsDialogPane(Window owner, Modality modality) {
		DialogPane dialogPane = new DialogPane();
		dialogPane.setContent(getDetailsNode());
		dialogPane.setPadding(new Insets(20,20,15,10));
		dialogPane.getButtonTypes().addAll(ButtonType.OK, ButtonType.CLOSE);
		Button copyButton = (Button) dialogPane.lookupButton(ButtonType.OK);
		copyButton.setText("Copy");
		copyButton.addEventFilter(ActionEvent.ACTION, event->{
			event.consume();
			Clipboard clipboard = Clipboard.getSystemClipboard();
			ClipboardContent content = new ClipboardContent();
		     content.putString(getDetailstoBeCopied	());
		     clipboard.setContent(content);
		     showCopySuccessAlert(owner, modality);
		});
		return dialogPane;
	}

	private void showCopySuccessAlert(Window owner, Modality modality) {
		Alert alert = new Alert(AlertType.INFORMATION);
		alert.initOwner(owner);
		alert.initModality(modality);
		alert.setTitle("Copied to clipboard");
		alert.setHeaderText("Text copied to clipboard.");
		alert.showAndWait();
	}

	private Node getDetailsNode() {
		GridPane gridPane = new GridPane();
		gridPane.setPadding(new Insets(15));
		gridPane.setHgap(30);
		gridPane.setVgap(20);
		detailsMap.forEach((key, value)->{
			int index = gridPane.getChildren().size()/2;
			gridPane.addRow(index, headingLabel(key), dataLabel(value));
		});
		return gridPane;
	}

	private Label headingLabel(String text) {
		Label label = new Label(text);
		label.setStyle("-fx-font-weight:bold;");
		return label;
	}

	private Label dataLabel(String text) {
		Label label = new Label(": "+ text);
		return label;
	}

	private String getDetailstoBeCopied() {
		StringBuilder stringBuilder = new StringBuilder();
		detailsMap.forEach((key, value)->{
			stringBuilder.append(key + " : " + value + System.lineSeparator());
		});
		return stringBuilder.toString();
	}
}

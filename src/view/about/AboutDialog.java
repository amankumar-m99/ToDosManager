package view.about;

import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Dialog;
import javafx.scene.control.DialogPane;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Modality;
import javafx.stage.Window;
import utility.AppStaticData;

public class AboutDialog extends Dialog<ButtonType>{
	public AboutDialog(Window owner) {
		this.initModality(Modality.APPLICATION_MODAL);
		this.initOwner(owner);
		this.setTitle("About- "+ AppStaticData.getAppName());
		this.setDialogPane(getAboutDialogPane());
	}

	private DialogPane getAboutDialogPane() {
		DialogPane dialogPane = new DialogPane();
		dialogPane.setPadding(new Insets(30,30,20,30));
		dialogPane.setContent(getDialogPaneContent());
		dialogPane.getButtonTypes().add(ButtonType.CLOSE);
		return dialogPane;
	}

	private Node getDialogPaneContent() {
        Image image = AppStaticData.getAppIcon();
        ImageView imageView = new ImageView(image);
        imageView.setFitWidth(200);
        imageView.setPreserveRatio(true);
        StackPane sp1 = new StackPane(imageView);
        
        Label compName = new Label();
        compName.setText(AppStaticData.getAppName());
        compName.setFont(Font.font ("Verdana",FontWeight.BOLD, 18));
        StackPane sp2 = new StackPane(compName);
        
        Label version = new Label();
        version.setText("App Version :");
        version.setFont(Font.font ("Verdana", FontWeight.BOLD, 13));
        Label versionVal = new Label();
        versionVal.setText("1.0.0");
        versionVal.setFont(Font.font ("Verdana", 16));
        
        Label developerInfo = new Label();
        developerInfo.setText("Developed By :");
        developerInfo.setFont(Font.font ("Verdana", FontWeight.BOLD, 13));
        Label developerName = new Label();
        developerName.setText("Aman Kumar");
        developerName.setFont(Font.font ("Verdana", FontWeight.BOLD, 15));
        
        Label email = new Label("Email :");
        email.setFont(Font.font ("Verdana", FontWeight.BOLD, 13 ));
        Hyperlink emailVal = new Hyperlink("amankumar.m99@gmail.com");
        emailVal.setFont(Font.font ("Verdana", 15));        
        
        GridPane gpan = new GridPane();
        ColumnConstraints col1 = new ColumnConstraints();
        col1.setHalignment(HPos.RIGHT);
        gpan.getColumnConstraints().add(col1);
        gpan.setHgap(20);
        gpan.setVgap(18);
        gpan.addRow(0,version,versionVal);
        gpan.addRow(1,developerInfo,developerName);
        gpan.addRow(2,email,emailVal);
        gpan.setPadding(new Insets(20,20,20,20));
        gpan.setBorder(AppStaticData.getComponentBorder());
        
        Label copyright = new Label();
        copyright.setText("Copyright by Indeed Coder \u00a92021 All Rights Reserved.");
        copyright.setFont(Font.font ("Verdana", 12));
        VBox vbx = new VBox(30);
        vbx.getChildren().addAll(sp1,sp2,gpan/*,new StackPane(copyright)*/);
        vbx.setPadding(new Insets(20,20,20,20));
        VBox.setMargin(vbx, new Insets(5,15,5,5));
        vbx.setBorder(AppStaticData.getComponentBorder());
        return vbx;
	}
}

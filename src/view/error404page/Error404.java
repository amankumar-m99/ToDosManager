package view.error404page;

import javafx.scene.Node;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

public class Error404 {
	private static String path = "resources/error-404.png";
	public Node getContent() {
		ImageView imageView = new ImageView(new Image(path));
		imageView.setPreserveRatio(true);
		return imageView;
	}
}

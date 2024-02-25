package menu;
//2nd version
import javafx.scene.control.Button;
import javafx.scene.paint.Color;

public class MenuButtons extends Button {
	
	// Constructor
	public MenuButtons(String text) {
		setText(text);
		setPrefHeight(50);
		setPrefWidth(150);
		setStyle("-fx-background-color: #00ff00;");
		setTextFill(Color.BLACK);
	}
}


/**
* Here we just create a class that extends button in javaFX which creates a default button
* The button has some extra styling which is a green background and black text
*/

package menu;
// Imports
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


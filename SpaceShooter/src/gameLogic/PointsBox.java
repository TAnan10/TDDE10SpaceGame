/**
* This class displays a white box on the top right corner of the game which shows how many
* points a players has.
*/

package gameLogic;

// Imports
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundImage;
import javafx.scene.layout.BackgroundPosition;
import javafx.scene.layout.BackgroundRepeat;

public class PointsBox extends Label {
	
	// Constructor
	public PointsBox(String text) {
		setPrefWidth(150);
		setPrefHeight(50);
		BackgroundImage backgroundImage = new BackgroundImage(new Image("menu/Images/grey_button05.png", 130, 50, false, true), BackgroundRepeat.NO_REPEAT, BackgroundRepeat.NO_REPEAT, BackgroundPosition.DEFAULT, null);
		setBackground(new Background(backgroundImage));
		setAlignment(Pos.CENTER_LEFT);
		setPadding(new Insets(10, 10, 10, 10));
		setText(text);
	}
}

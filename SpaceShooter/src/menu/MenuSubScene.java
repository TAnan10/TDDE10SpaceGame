/**
* This class has 3 background images which are used for different menus
* This class has a method for the gameOver menu which has a certain background
* A background for the scene where the player gets to choose which level they want to play
* A background for reaching a new highscore and writing your name and level
*/

package menu;

import javafx.scene.SubScene;
import javafx.scene.image.Image;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundImage;
import javafx.scene.layout.BackgroundPosition;
import javafx.scene.layout.BackgroundRepeat;
import javafx.scene.layout.BackgroundSize;

public class MenuSubScene extends SubScene {

	// Variables	
	BackgroundImage backgroundImage = new BackgroundImage(
			new Image("menu/Images/GameOverImage.jpg"),
			BackgroundRepeat.NO_REPEAT, BackgroundRepeat.NO_REPEAT,
			BackgroundPosition.DEFAULT,
			new BackgroundSize(100, 100, false, false, false, true)
			);

	BackgroundImage backgroundImage2 = new BackgroundImage(
			new Image("menu/Images/GreenBackground.png"),
			BackgroundRepeat.NO_REPEAT, BackgroundRepeat.NO_REPEAT,
			BackgroundPosition.DEFAULT,
			new BackgroundSize(100, 100, false, false, false, true)
			);

	BackgroundImage backgroundImage3 = new BackgroundImage(
			new Image("menu/Images/firework.jpg"),
			BackgroundRepeat.NO_REPEAT, BackgroundRepeat.NO_REPEAT,
			BackgroundPosition.DEFAULT,
			new BackgroundSize(100, 100, false, false, false, true)
			);

	// Constructor
	public MenuSubScene() {
		super(new AnchorPane(), 600, 400);
	}

	// Methods
	public void gameOverScene() {
		AnchorPane root2 = (AnchorPane) this.getRoot();
		root2.setBackground(new Background(backgroundImage));
		setHeight(400);
	}

	public void setUpPopup() {
		AnchorPane root = (AnchorPane) this.getRoot();
		root.setBackground(new Background(backgroundImage2));
	}

	public void createEnterNameSubSceneDesign() {
		AnchorPane pane = (AnchorPane) this.getPane();
		setHeight(400);
		setWidth(600);
		setLayoutY(400);
		pane.setBackground(new Background(backgroundImage3));
	}

	public AnchorPane getPane() {
		return (AnchorPane) this.getRoot();
	}
}







package menu;

import gameLogic.TheGame;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

public class MenuDesign {
	
	// Variables
	private static final int windowHeight = 600;
	private static final int windowWidth = 800;
	
	private Stage mainStage;
	private Scene mainScene;
	private AnchorPane mainPane;
	
	// Constructor
	public MenuDesign() {
		mainPane = new AnchorPane();
		mainScene = new Scene(mainPane, windowHeight, windowWidth);
		mainStage = new Stage();
		mainStage.setScene(mainScene);
		createBackground();
		createTitle();
		createAllButtons();
	}
	
	// Methods
	public Stage getMainStage() {
		return mainStage;
	}
	
	private void createTitle() {
		ImageView title = new ImageView("menu/Images/Menu_Title.png");
		title.setFitHeight(200);
		title.setFitWidth(350);
		title.setLayoutX(125);
		title.setLayoutY(50);
		mainPane.getChildren().add(title);
	}
	
	private void createBackground() {
		ImageView background = new ImageView("menu/Images/Menu_Background.jpg");
		mainPane.getChildren().add(background);
	}
	
	private void createAllButtons() {
		NewGameButton();
		HighScoresButton();
		HelpButton();
		QuitButton();
	}
	
	private void NewGameButton() {
		MenuButtons newGame = new MenuButtons("New Game");
		newGame.setLayoutX(220);
		newGame.setLayoutY(300);
		mainPane.getChildren().add(newGame);
		
		newGame.setOnAction(new EventHandler<ActionEvent>() {

			@Override
			public void handle(ActionEvent event) {
				TheGame gameWindow = new TheGame();
				gameWindow.createNewGame(mainStage);
			}
			
		});
	}
	
	private void HighScoresButton() {
		MenuButtons highscoresButton = new MenuButtons("Highscores");
		highscoresButton.setLayoutX(220);
		highscoresButton.setLayoutY(400);
		mainPane.getChildren().add(highscoresButton);
	}
	
	private void HelpButton() {
		MenuButtons helpButton = new MenuButtons("Help");
		helpButton.setLayoutX(220);
		helpButton.setLayoutY(500);
		mainPane.getChildren().add(helpButton);
	}
	
	private void QuitButton() {
		MenuButtons quitButton = new MenuButtons("Quit");
		quitButton.setLayoutX(220);
		quitButton.setLayoutY(600);
		mainPane.getChildren().add(quitButton);
	}
}













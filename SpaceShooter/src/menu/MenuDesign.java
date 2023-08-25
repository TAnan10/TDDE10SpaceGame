package menu;

import gameLogic.LevelOne;
import gameLogic.LevelThree;
import gameLogic.LevelTwo;
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
	private MenuSubScene levels;

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
				levels = new MenuSubScene();
				levels.setUpPopup();
				levels.setLayoutX(105);
				levels.setLayoutY(300);
				levels.setWidth(400);
				levelButtons();
				mainPane.getChildren().add(levels);
			}
		});
	}

	private void HighScoresButton() {
		MenuButtons highScoresButton = new MenuButtons("Highscores");
		highScoresButton.setLayoutX(220);
		highScoresButton.setLayoutY(400);
		mainPane.getChildren().add(highScoresButton);
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

	private void levelButtons() {
		MenuButtons level1 = new MenuButtons("Level 1 - Easy");
		level1.setLayoutX(130);
		level1.setLayoutY(60);
		level1.setOnAction(new EventHandler<ActionEvent>() {

			@Override
			public void handle(ActionEvent event) {
				LevelOne gameWindow = new LevelOne();
				gameWindow.createNewGame(mainStage);
			}

		});

		MenuButtons level2 = new MenuButtons("Level 2 - Medium");
		level2.setLayoutX(130);
		level2.setLayoutY(170);
		level2.setOnAction(new EventHandler<ActionEvent>() {

			@Override
			public void handle(ActionEvent event) {
				LevelTwo gameWindow = new LevelTwo();
				gameWindow.createNewGame(mainStage);
			}

		});

		MenuButtons level3 = new MenuButtons("Level 3 - Hard");
		level3.setLayoutX(130);
		level3.setLayoutY(280);
		level3.setOnAction(new EventHandler<ActionEvent>() {

			@Override
			public void handle(ActionEvent event) {
				LevelThree gameWindow = new LevelThree();
				gameWindow.createNewGame(mainStage);
			}
		});

		levels.getPane().getChildren().add(level1);
		levels.getPane().getChildren().add(level2);
		levels.getPane().getChildren().add(level3);
	}
}















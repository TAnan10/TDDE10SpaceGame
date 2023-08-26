package menu;

import gameLogic.LevelTwo;
import gameLogic.LevelThree;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import gameLogic.HighScores;
import gameLogic.LevelOne;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.stage.Stage;

public class MenuDesign {

	// Variables
	private static final int windowHeight = 600;
	private static final int windowWidth = 800;

	private Stage mainStage;
	private Scene mainScene;
	private AnchorPane mainPane;
	private MenuSubScene levels;
	private MenuSubScene displayHighscore;
	private MenuSubScene helpScene;
	private List<HighScores> highscores;

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

	    highScoresButton.setOnAction(new EventHandler<ActionEvent>() {
	        @Override
	        public void handle(ActionEvent event) {
	            List<HighScores> highscores = loadHighscoresFromFile();
	            displayHighscores(highscores);
	        }
	    });
	}

	private List<HighScores> loadHighscoresFromFile() {
	    List<HighScores> highscores = new ArrayList<>();
	    
	    try (BufferedReader reader = new BufferedReader(new FileReader("src/gameLogic/resources/HighScore.txt"))) {
	        String line;
	        while ((line = reader.readLine()) != null) {
	            String[] parts = line.split(",");
	            if (parts.length == 2) {
	                String name = parts[0];
	                int score = Integer.parseInt(parts[1]);
	                highscores.add(new HighScores(name, score));
	            }
	        }
	    } catch (IOException e) {
	        e.printStackTrace();
	    }
	    
	    return highscores;
	}

	private void displayHighscores(List<HighScores> highscores) {
		displayHighscore = new MenuSubScene();
		displayHighscore.setUpPopup();
		displayHighscore.setLayoutX(105);
		displayHighscore.setLayoutY(300);
		displayHighscore.setWidth(400);
		mainPane.getChildren().add(displayHighscore);
		
	    VBox highscoresBox = new VBox(10);
	    highscoresBox.setLayoutX(140);
	    
	    Text title = new Text("Highscores");
	    title.setFont(Font.font("Arial", FontWeight.BOLD, 24));
	    highscoresBox.getChildren().add(title);
	    
	    for (HighScores entry : highscores) {
	        Text highscoreText = new Text(entry.getName() + " - " + entry.getScore());
	        highscoreText.setFont(Font.font("Arial", 16));
	        highscoresBox.getChildren().add(highscoreText);
	    }
	    
	    if (displayHighscore != null) {
	        displayHighscore.getPane().getChildren().clear(); // Clear previous content
	        displayHighscore.getPane().getChildren().add(highscoresBox);
	    }
	}



	private void HelpButton() {
		MenuButtons helpButton = new MenuButtons("Help");
		helpButton.setLayoutX(220);
		helpButton.setLayoutY(500);
		mainPane.getChildren().add(helpButton);
		
		helpButton.setOnAction(new EventHandler<ActionEvent>() {

			@Override
			public void handle(ActionEvent event) {
				helpScene = new MenuSubScene();
				helpScene.setUpPopup();
				helpScene.setLayoutX(105);
				helpScene.setLayoutY(300);
				helpScene.setWidth(400);
				mainPane.getChildren().add(helpScene);
			}
		});
		
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















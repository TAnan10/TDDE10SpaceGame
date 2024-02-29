/**
* This is the main menu
* Creates the background of the menu
* Creates the title of the menu
* Creates 4 menu buttons using MenuButtons class(New Game, Highscores, Help & Quit)
*/

package menu;

// Imports
import gameLogic.LevelFive;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import gameLogic.HighScores;
import gameLogic.LevelFour;
import gameLogic.LevelSix;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;
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
	
	// Getter Method for the mainStage
	public Stage getMainStage() {
		return mainStage;
	}

	// Method for SpaceShooter Logo
	private void createTitle() {
		ImageView title = new ImageView("menu/Images/Menu_Title.png");
		title.setFitHeight(200);
		title.setFitWidth(350);
		title.setLayoutX(125);
		title.setLayoutY(50);
		mainPane.getChildren().add(title);
	}

	// Method for black background in the menu scene
	private void createBackground() {
		ImageView background = new ImageView("menu/Images/Menu_Background.jpg");
		mainPane.getChildren().add(background);
	}

	// Calls all the button methods
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
	            if (parts.length == 3) {
	                String level = parts[0];
	                String name = parts[1];
	                int score = Integer.parseInt(parts[2]);
	                highscores.add(new HighScores(level, name, score));
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
	    highscoresBox.setAlignment(Pos.CENTER);
	    highscoresBox.setLayoutX(115);
	    
	    Text title = new Text("Highscores");
	    title.setFont(Font.font("Arial", FontWeight.BOLD, 24));
	    highscoresBox.getChildren().add(title);
	    
	    for (HighScores entry : highscores) {
	        Text highscoreText = new Text(entry.getLevel() + "-" + entry.getName() + " - " + entry.getScore());
	        highscoreText.setFont(Font.font("Arial", 16));
	        highscoresBox.getChildren().add(highscoreText);
	    }
	    
	    mainButtonHighscore();
	    
	    if (displayHighscore != null) {
	        displayHighscore.getPane().getChildren().clear(); // Clear previous content
	        displayHighscore.getPane().getChildren().add(highscoresBox);
	    }
	}

	private void mainButtonHighscore() {
		MenuButtons menuButton = new MenuButtons("Main Menu");
		menuButton.setLayoutX(215);
		menuButton.setLayoutY(600);
		menuButton.setMinWidth(180);
		menuButton.setFont(Font.font("Arial", FontWeight.BOLD, 18));
		mainPane.getChildren().add(menuButton);

		menuButton.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				displayHighscore.setVisible(false);
				mainPane.getChildren().remove(menuButton);
			}
		});
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
				
				// Create a VBox to hold the help content
	            VBox helpContent = new VBox(10);
	            helpContent.setAlignment(Pos.CENTER); // Center-align the content
	            helpContent.setPadding(new Insets(20));
	            
	            Text title = new Text("Welcome to Space Invaders 2024!!!");
	            title.setFont(Font.font("Times New Roman", FontWeight.BOLD, 20));
	            helpContent.getChildren().add(title);

	            // Add a paragraph about the game
	            Text paragraph = new Text("In this game there are three levels each with a different enemy. Level one has asteroids, level two has ufo's that can shoot lasers and level three has a robot ship that you cannot catch and shoots green lasers!!! Well how can defeat these enemies and win every level? Don't worry because you will be in a powerful rocket that shoots lasers at lightning speeds! But be aware you have only 2 lives for level one, 4 lives for level two and 6 lives for level three"
	            );
	            paragraph.setFont(Font.font("Times New Roman", 14));
	            paragraph.setWrappingWidth(350);
	            paragraph.setTextAlignment(TextAlignment.CENTER);
	            helpContent.getChildren().add(paragraph);
	            
	            Text controlsInfo = new Text("To control the rocket all you need is three buttons on the keyboard. The left arrow key to move left, the right arrow key to move right and the spacebar to shoot lasers");
	            controlsInfo.setFont(Font.font("Times New Roman, 14"));
	            controlsInfo.setWrappingWidth(350);
	            controlsInfo.setTextAlignment(TextAlignment.CENTER);
	            helpContent.getChildren().add(controlsInfo);

	            Text endingText = new Text("Good luck and have fun! And remember do not die!");
	            endingText.setFont(Font.font("Times New Roman", FontWeight.BOLD, 16));
	            
	            helpContent.getChildren().add(endingText);
	            helpScene.getPane().getChildren().add(helpContent);
			}
		});
	}

	private void QuitButton() {
		MenuButtons quitButton = new MenuButtons("Quit");
		quitButton.setLayoutX(220);
		quitButton.setLayoutY(600);
		mainPane.getChildren().add(quitButton);
		
		quitButton.setOnAction(new EventHandler<ActionEvent>() {

			@Override
			public void handle(ActionEvent arg0) {
				mainStage.close();
			}
			
		});
	}

	private void levelButtons() {
		MenuButtons level1 = new MenuButtons("Level 1 - Easy");
		level1.setLayoutX(130);
		level1.setLayoutY(60);
		level1.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) { 
				LevelFour gameWindow = new LevelFour();
				gameWindow.createNewGame(mainStage);
			}

		});

		MenuButtons level2 = new MenuButtons("Level 2 - Medium");
		level2.setLayoutX(130);
		level2.setLayoutY(170);
		level2.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				LevelFive gameWindow = new LevelFive();
				gameWindow.createNewGame(mainStage);
			}
		});

		MenuButtons level3 = new MenuButtons("Level 3 - Hard");
		level3.setLayoutX(130);
		level3.setLayoutY(280);
		level3.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				LevelSix gameWindow = new LevelSix();
				gameWindow.createNewGame(mainStage);
			}
		});
		levels.getPane().getChildren().add(level1);
		levels.getPane().getChildren().add(level2);
		levels.getPane().getChildren().add(level3);
	}
}
















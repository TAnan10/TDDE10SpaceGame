package gameLogic;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import javafx.animation.AnimationTimer;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import menu.MenuButtons;
import menu.MenuSubScene;

public class LevelTwo {

	// Instance Variables
	private AnchorPane gamePane;
	private Scene gameScene;
	private Stage gameStage;
	private Stage menuStage;
	private MenuSubScene gameOver;

	private ImageView rocket;
	private double shipSpeed = 8;
	private AnimationTimer gameTimer;

	private List<Enemy> enemies;

	private boolean isLeftKeyPressed;
	private boolean isRightKeyPressed;

	private ImageView[] playersLifes;
	private int playerLife;
	private String playerName;
	private String levelNum;
	
	private boolean doubleLaserPowerUpActive = false;
	private List<ImageView> extraHearts = new ArrayList<>();
	
	private Star star;
	private PointsBox pointsBox;
	private int playerScore = 0;
	private List<HighScores> highscores = new ArrayList<>();

	public static final int Game_Width = 600;
	public static final int Game_Height = 800;

	private static final String BackgroundImage = "menu/Images/space.jpg";
	private List<ImageView> backgroundImages = new ArrayList<>();

	private List<ImageView> lasers = new ArrayList<>();
	private static final String LASER_IMAGE = "menu/Images/laserRed02.png";
	private static final int LASER_WIDTH = 15;
	private static final int LASER_HEIGHT = 40;

	// Constructor
	public LevelTwo() {
		initializeStage();
		createKeyListeners();
		initializeHighscores();
		enemies = new ArrayList<>();
	}

	// Methods
	public String getLevel() {
		return levelNum;
	}
	
	public static int getGameHeight() {
		return Game_Height;
	}
	
	public static int getGameWidth() {
		return Game_Width;
	}

	public List<ImageView> getLasers() {
		return lasers;
	}

	// Creating the game window
	private void initializeStage() {
		gamePane = new AnchorPane();
		gameScene = new Scene(gamePane, Game_Width, Game_Height);
		gameStage = new Stage();
		gameStage.setScene(gameScene);
	}

	// Create arrow keys for moving the ship left and right
	private void createKeyListeners() {
		gameScene.setOnKeyPressed(new EventHandler<KeyEvent>() {
			@Override
			public void handle(KeyEvent event) {
				if (event.getCode() == KeyCode.LEFT) {
					isLeftKeyPressed = true;
				} else if (event.getCode() == KeyCode.RIGHT) {
					isRightKeyPressed = true;
				} else if (event.getCode() == KeyCode.SPACE) {
					// Shoot lasers when the space bar is pressed
					double shipX = rocket.getLayoutX() + (rocket.getFitWidth() - LASER_WIDTH) / 2;
					shootLaser(shipX);
				}
			}
		});

		gameScene.setOnKeyReleased(new EventHandler<KeyEvent>() {
			@Override
			public void handle(KeyEvent event) {
				if (event.getCode() == KeyCode.LEFT) {
					isLeftKeyPressed = false;
				} else if (event.getCode() == KeyCode.RIGHT) {
					isRightKeyPressed = false;
				} 
			}
		});
	}

	// This method will hide the menu window and show the game scene
	public void createNewGame(Stage menuStage) {
		this.menuStage = menuStage;
		this.menuStage.hide();
		createBackground();
		createRocketShip();
		createPlayerLives();
		createStars();
		createPointsBox();
		createEnemies();
		GameLoop();
		gameStage.show();
	}

	private void GameLoop() {
		gameTimer = new AnimationTimer() {
			@Override
			public void handle(long arg0) {
				controlShipAnimation();
				backgroundAnimation();
				createStarsAnimation();
				moveEnemies();
				moveLasers();
				handleEnemyCollisions();
			}
		};
		gameTimer.start();
	}

	private void createEnemies() {
		UFOEnemy ufoEnemy1 = new UFOEnemy(0, 50, gamePane, 3, rocket, this);
		UFOEnemy ufoEnemy2 = new UFOEnemy(150, 60, gamePane, 5, rocket, this);
		UFOEnemy ufoEnemy3 = new UFOEnemy(250, 30, gamePane, 10, rocket, this);
		enemies.add(ufoEnemy1);
		enemies.add(ufoEnemy2);
		enemies.add(ufoEnemy3);
		for (Enemy enemy : enemies) {
			gamePane.getChildren().add(enemy.getEnemyImage());
		}
	}

	private void moveEnemies() {
		for (Enemy enemy : enemies) {
			enemy.move();
		}
	}

	private void handleEnemyCollisions() {
		for (Enemy enemy : enemies) {
			enemy.handleCollision(this);
		}
		starPoints();
	}

	private void createSubScene() {
		gameOver = new MenuSubScene();
		gameOver.gameOverScene();
		gamePane.getChildren().add(gameOver);
		pointsBox.toFront();
		gameOverButton();
	}
	
	private void createPointsBox() {
		pointsBox = new PointsBox("POINTS :   ");
		pointsBox.setLayoutX(460);
		pointsBox.setLayoutY(20);
		gamePane.getChildren().add(pointsBox);
	}

	private void createStars() {
		star = new Star("menu/Images/star.png");
		star.starPosition();
		gamePane.getChildren().add(star.getStarImageView());
	}

	private void createStarsAnimation()  {
		star.move();
	}
	
	public void starPoints() {
		if (star.checkCollision(rocket)) {
			playerScore += 1;
			star.starPosition();
			String textToSet = "POINTS : ";
			if (playerScore < 5) {
				textToSet = textToSet += " ";
			}
			pointsBox.setText(textToSet + playerScore);

			
			if (playerScore >= 2) { // PowerUp 1
				shipSpeed = 15;
			}

			if (playerScore >= 3) { // PowerUp 2
				doubleLaserPowerUpActive = true;
			}
			
			if (playerScore == 5) { // PowerUp 3
	            addExtraLives(2);
	        }
			
			if (playerScore == 10) {
				showVictoryScreen();
				createEnterNameSubScene();
			}
		}
	}
	
	private void updateAndSaveHighscores() {
		highscores.add(new HighScores(levelNum, playerName, playerScore));

		try (FileWriter writer = new FileWriter("src/gameLogic/resources/HighScore.txt")) {
			for (HighScores entry : highscores) {
				writer.write(entry.getLevel() + "," +  entry.getName() + "," + entry.getScore() + "\n");
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	private void gameOverButton() {
		MenuButtons over = new MenuButtons("Play Again");
		over.setLayoutX(220);
		over.setLayoutY(400);
		over.setMinWidth(180);
		over.setFont(Font.font("Arial", FontWeight.BOLD, 18));
		gamePane.getChildren().add(over);

		over.setOnAction(new EventHandler<ActionEvent>() {

			@Override
			public void handle(ActionEvent event) {
				gamePane.getChildren().remove(gameOver);
				gamePane.getChildren().remove(over);
				resetGame();
			}

		});
	}

	private void resetGame() {
		playerScore = 0;
		doubleLaserPowerUpActive = false;
		shipSpeed = 8;
		removeAllEnemies();
		createBackground();
		createRocketShip();
		createPlayerLives();
		createStars();
		createPointsBox();
		createEnemies();
		GameLoop();
	}

	private void removeAllEnemies() {
		for (Enemy enemy : enemies) {
			gamePane.getChildren().remove(enemy.getEnemyImage());
		}
		enemies.clear();
	}
	
	public void showVictoryScreen() {
		gameTimer.stop();
		
		for (ImageView laser : lasers) {
	        laser.setVisible(false);
	    }
 	    
	    MenuSubScene victorySubScene = new MenuSubScene();
	    victorySubScene.setHeight(400);
	    victorySubScene.setUpPopup();
	    gamePane.getChildren().add(victorySubScene);
	    
	    Text victoryText = new Text("Victory");
	    victoryText.setLayoutX(200);
	    victoryText.setLayoutY(200);
	    victoryText.setFont(Font.font("Arial", FontWeight.BOLD, 60));
	    victorySubScene.getPane().getChildren().add(victoryText);

	    // Create and add the main menu button
	    MenuButtons menuButton = new MenuButtons("Main Menu");
	    menuButton.setLayoutX(210);
	    menuButton.setLayoutY(280);
	    menuButton.setMinWidth(180);
	    menuButton.setFont(Font.font("Arial", FontWeight.BOLD, 18));
	    victorySubScene.getPane().getChildren().add(menuButton);
	    pointsBox.toFront();

	    menuButton.setOnAction(new EventHandler<ActionEvent>() {
	        @Override
	        public void handle(ActionEvent event) {
	        	gameStage.hide();
	            menuStage.show();
	        }
	    });
	}
	
	private void createEnterNameSubScene() {
		MenuSubScene enterNameSubScene = new MenuSubScene();
		enterNameSubScene.createEnterNameSubSceneDesign();
		
		Text Title = new Text("Congratulations!");
		Title.setLayoutX(195);
		Title.setLayoutY(50);
		Title.setFill(Color.ALICEBLUE);
		Title.setFont(Font.font("Arial", FontWeight.BOLD, 30));
		
		Text Subtitle = new Text("You Reached A New Highscore!!!");
		Subtitle.setLayoutX(110);
		Subtitle.setLayoutY(100);
		Subtitle.setFill(Color.ALICEBLUE);
		Subtitle.setFont(Font.font("Arial", FontWeight.BOLD, 25));
		
		TextField levelField = new TextField();
	    levelField.setPrefHeight(35);
	    levelField.setPrefWidth(180);
	    levelField.setLayoutX(210);
	    levelField.setLayoutY(160); // Adjust the Y position as needed
	    levelField.setPromptText("Enter your level");
		
		TextField nameField = new TextField();
		nameField.setPrefHeight(35);
		nameField.setPrefWidth(180);
		nameField.setLayoutX(210);
		nameField.setLayoutY(130);
		nameField.setPromptText("Enter your name");

		Button saveButton = new Button("Save Score");
		saveButton.setPrefHeight(50);
		saveButton.setPrefWidth(100);
		saveButton.setLayoutX(250);
		saveButton.setLayoutY(200);
		saveButton.setOnAction(event -> {
			playerName = nameField.getText();
			levelNum = levelField.getText();
			updateAndSaveHighscores();
			enterNameSubScene.setVisible(false);
		});

		enterNameSubScene.getPane().getChildren().addAll(nameField, levelField, Title, Subtitle, saveButton);
		gamePane.getChildren().add(enterNameSubScene);
	}
	
	private void initializeHighscores() {
	    // Read highScores from the text file
	    String absolutePath = "src/gameLogic/resources/HighScore.txt";
	    try (BufferedReader reader = new BufferedReader(new FileReader(absolutePath))) {
	        String line;
	        while ((line = reader.readLine()) != null) {
	            String[] parts = line.split(",");
	            if (parts.length == 3) {
	                String level = parts[0]; // Parse the level as a string
	                String name = parts[1];
	                int score = Integer.parseInt(parts[2]);
	                highscores.add(new HighScores(level, name, score)); // Pass the level
	            }
	        }
	    } catch (IOException e) {
	        e.printStackTrace();
	    }
	}

	private void createPlayerLives() {
		playerLife = 4;
		playersLifes = new ImageView[5];
		for (int i = 0; i < playersLifes.length; i++) {
			playersLifes[i] = new ImageView("menu/Images/heart.png");
			playersLifes[i].setFitWidth(20);
			playersLifes[i].setFitHeight(20);
			playersLifes[i].setLayoutX(400 + (i * 35));
			playersLifes[i].setLayoutY(100);
			gamePane.getChildren().add(playersLifes[i]);
		}
	}
	
	private void addExtraLives(int count) {
		playerLife += count;
	    int newLength = playersLifes.length + count;
	    ImageView[] newPlayersLifes = new ImageView[newLength];
	    
	    // Copy the existing hearts to the new array
	    for (int i = 0; i < playersLifes.length; i++) {
	        newPlayersLifes[i] = playersLifes[i];
	    }
	    
	    // Add new hearts to the new array
	    for (int i = playersLifes.length; i < newLength; i++) {
	        ImageView extraLife = new ImageView("menu/Images/heart.png");
	        extraLife.setFitWidth(20);
	        extraLife.setFitHeight(20);
	        extraLife.setLayoutX(460 + (i * 35));
	        extraLife.setLayoutY(100);
	        newPlayersLifes[i] = extraLife;
	        extraHearts.add(extraLife);
	        gamePane.getChildren().add(extraLife);
	    }
	    
	    playersLifes = newPlayersLifes; // Update the reference
	}

	public void removePlayerLives() {
	    playerLife--;

	    if (!extraHearts.isEmpty()) {
	        ImageView removedHeart = extraHearts.remove(extraHearts.size() - 1);
	        gamePane.getChildren().remove(removedHeart);
	    } else if (playerLife >= 0) {
	        gamePane.getChildren().remove(playersLifes[playerLife + 1]);
	    }

	    if (playerLife < 0) {
	    	for (ImageView heart : playersLifes) {
	            gamePane.getChildren().remove(heart);
	        }
	    	
	        gameTimer.stop();
	        createSubScene();
	        
	        boolean canEnterName = true;
	        for (int i = 0; i < highscores.size(); i++) {
	            if (playerScore <= highscores.get(i).getScore()) {
	                canEnterName = false;
	                break;
	            }
	        }

	        if (highscores.isEmpty() || canEnterName) {
	        	createEnterNameSubScene();
	        }
	    }
	}

	private void createBackground() {
		for (int i = 0; i < 2; i++) {
			ImageView backgroundImage = new ImageView(BackgroundImage);
			backgroundImage.setLayoutY(-i * Game_Height);
			gamePane.getChildren().add(backgroundImage);
			backgroundImages.add(backgroundImage);
		}
	}

	private void backgroundAnimation() {
		for (int i = 0; i < backgroundImages.size(); i++) {
			ImageView backgroundImage = backgroundImages.get(i);
			backgroundImage.setLayoutY(backgroundImage.getLayoutY() + 2);

			if (backgroundImage.getLayoutY() >= Game_Height) {
				backgroundImage.setLayoutY(backgroundImage.getLayoutY() - 2 * Game_Height);
			}
		}
	}

	private void createRocketShip() {
		rocket = new ImageView("menu/Images/rocketShip.png");
		rocket.setFitWidth(50);
		rocket.setFitHeight(100);
		rocket.setLayoutX(270);
		rocket.setLayoutY(Game_Height - 120);
		gamePane.getChildren().add(rocket);
	}

	private void controlShipAnimation() {
		if (isLeftKeyPressed && !isRightKeyPressed) {
			if (rocket.getLayoutX() > -20) { // This statement makes sure that the ship doesn't go too far left
				rocket.setLayoutX(rocket.getLayoutX() - shipSpeed);
			}
		}

		if (isRightKeyPressed && !isLeftKeyPressed) {
			if (rocket.getLayoutX() < 522) { // 522 is the maximum width of the scene before the ship goes out of the window. So it checks if the ship position is less than 522, if true then it will move 3 pixels to the right.
				rocket.setLayoutX(rocket.getLayoutX() + shipSpeed);
			}
		}
	}

	// Laser methods
	private void shootLaser(double x) {
		if (doubleLaserPowerUpActive) {
			// Create two lasers spaced apart
			ImageView laser1 = new ImageView(LASER_IMAGE);
			ImageView laser2 = new ImageView(LASER_IMAGE);

			double laserX1 = x - LASER_WIDTH;
			double laserX2 = x;

			createLaser(laser1, laserX1);
			createLaser(laser2, laserX2);
		} else {
			// Create a single laser
			ImageView laser = new ImageView(LASER_IMAGE);
			laser.setLayoutX(x - LASER_WIDTH / 2);
			laser.setLayoutY(675);
			laser.setFitWidth(LASER_WIDTH);
			laser.setFitHeight(LASER_HEIGHT);
			lasers.add(laser);
			gamePane.getChildren().add(laser);
		}
	}

	private void createLaser(ImageView laser, double x) {
		laser.setLayoutX(x);
		laser.setLayoutY(675);
		laser.setFitWidth(LASER_WIDTH);
		laser.setFitHeight(LASER_HEIGHT);
		lasers.add(laser);
		gamePane.getChildren().add(laser);
	}

	private void moveLasers() {
		for (int i = 0; i < lasers.size(); i++) {
			ImageView laser = lasers.get(i);
			laser.setLayoutY(laser.getLayoutY() - 10);
			if (laser.getLayoutY() > LevelTwo.Game_Height) {
				lasers.remove(i);
				gamePane.getChildren().remove(laser);
				i--;
			}
		}
	}
}









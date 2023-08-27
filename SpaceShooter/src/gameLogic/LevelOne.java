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
import javafx.geometry.Bounds;
import javafx.scene.Scene;
import javafx.scene.SubScene;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import menu.MenuButtons;
import menu.MenuSubScene;

public class LevelOne {

	private AnchorPane gamePane;
	private Scene gameScene;
	private Stage gameStage;
	private Stage menuStage;
	private MenuSubScene gameOver;

	private ImageView rocket;
	private double shipSpeed = 8;
	private AnimationTimer gameTimer;

	private boolean isLeftKeyPressed;
	private boolean isRightKeyPressed;

	private ImageView[] playersLifes;
	private int playerLife;
	private String playerName;

	private boolean doubleLaserPowerUpActive = false;

	private Star star;
	private PointsBox pointsBox;
	private int playerScore = 0;
	private List<HighScores> highscores = new ArrayList<>();

	private static final int Game_Width = 600;
	private static final int Game_Height = 800;

	private static final String BackgroundImage = "menu/Images/leveltwospace.jpg";
	private List<ImageView> backgroundImages = new ArrayList<>();

	private final static String asteroidImage = "menu/Images/Asteroid.png";
	private ImageView[] asteroids;

	private List<ImageView> lasers = new ArrayList<>();
	private static final String LASER_IMAGE = "menu/Images/laserRed02.png";
	private static final int LASER_WIDTH = 15;
	private static final int LASER_HEIGHT = 40;

	// Constructor
	public LevelOne() {
		initializeStage();
		createKeyListeners();
		initializeHighscores();
	}

	// Methods
	public static int getGameHeight() {
		return Game_Height;
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
		createAsteroids();
		createPlayerLives();
		createStars();
		createPointsBox();
		GameLoop();
		gameStage.show();
	}

	private void GameLoop() {
		gameTimer = new AnimationTimer() {
			@Override
			public void handle(long arg0) {
				controlShipAnimation();
				backgroundAnimation();
				AsteroidsAnimation();
				createStarsAnimation();
				collisionLogic();
				moveLasers();
			}
		};
		gameTimer.start();
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

	private void createAsteroids() {
		asteroids = new ImageView[ 5];
		for (int i = 0; i < asteroids.length; i++) {
			asteroids[i] = new ImageView(asteroidImage);
			asteroids[i].setFitWidth(50); 
			asteroids[i].setFitHeight(50);
			asteroids[i].setLayoutX(Math.random() * (Game_Width - asteroids[i].getFitWidth())); // Random X-coordinate
			asteroids[i].setLayoutY(-Math.random() * Game_Height); // Random Y-coordinate above the screen
			gamePane.getChildren().add(asteroids[i]);
		}
	}

	private void AsteroidsAnimation() {
		for (int i = 0; i < asteroids.length; i++) {
			ImageView asteroid = asteroids[i];
			asteroid.setLayoutY(asteroid.getLayoutY() + 5); 
			if (asteroid.getLayoutY() >= Game_Height) {
				asteroid.setLayoutY(-Math.random() * Game_Height); // Reset to a random position above the screen
				asteroid.setLayoutX(Math.random() * (Game_Width - asteroid.getFitWidth())); // Random X-coordinate
			}
		}
	}

	private void collisionLogic() {
		for (int i = 0; i < asteroids.length; i++) {
			ImageView asteroid = asteroids[i];
			if (rocket.getBoundsInParent().intersects(asteroid.getBoundsInParent())) {
				removePlayerLives();
				asteroid.setLayoutY(-Math.random() * Game_Height); // Reset to a random position above the screen
				asteroid.setLayoutX(Math.random() * (Game_Width - asteroid.getFitWidth())); // Random X-coordinate
			}

			starPoints();
		}
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

			/*
			if (playerScore >= 2) { // PowerUp 1
				shipSpeed = 15; // Increase ship speed to 12 pixels per frame
			}
*/
			if (playerScore >= 3) { // PowerUp 2
				doubleLaserPowerUpActive = true; // Activate the double laser power-up
			}
			
			if (playerScore == 2) { // PowerUp 3
	            addExtraLives(2);
	        }
		}
	}

	private void updateAndSaveHighscores() {
		highscores.add(new HighScores(playerName, playerScore));

		try (FileWriter writer = new FileWriter("src/gameLogic/resources/HighScore.txt")) {
			for (HighScores entry : highscores) {
				writer.write(entry.getName() + "," + entry.getScore() + "\n");
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void showVictoryScreen() {
		gameTimer.stop();

		if (playerScore > highscores.get(0).getScore()) {
			SubScene enterNameSubScene = createEnterNameSubScene();
			gamePane.getChildren().add(enterNameSubScene);
		}

		for (ImageView laser : lasers) {
			laser.setVisible(false);
		}

		MenuSubScene victorySubScene = new MenuSubScene();
		victorySubScene.setHeight(400);
		victorySubScene.setUpPopup();
		victorySubScene.setLayoutY(180);
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

		SubScene enterNameSubScene = createEnterNameSubScene();
		victorySubScene.getPane().getChildren().add(enterNameSubScene);

		menuButton.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				gameStage.hide();
				menuStage.show();
			}
		});
	}

	private SubScene createEnterNameSubScene() {
		MenuSubScene enterNameSubScene = new MenuSubScene();
		enterNameSubScene.setHeight(300);
		enterNameSubScene.setWidth(400);
		enterNameSubScene.setUpPopup();
		enterNameSubScene.setLayoutX((Game_Width - 400) / 2);
		enterNameSubScene.setLayoutY((Game_Height - 300) / 2);

		// Create UI elements for entering name and saving score
		TextField nameField = new TextField();
		nameField.setLayoutX(200);
		nameField.setLayoutY(60);
		nameField.setPromptText("Enter your name");

		Button saveButton = new Button("Save Score");
		saveButton.setLayoutX(150);
		saveButton.setLayoutY(80);
		saveButton.setOnAction(event -> {
			playerName = nameField.getText();
			updateAndSaveHighscores();
			enterNameSubScene.setVisible(false);
		});

		enterNameSubScene.getPane().getChildren().addAll(nameField, saveButton);
		return enterNameSubScene;
	}

	private void initializeHighscores() {
		// Read highScores from the text file
		String absolutePath = "src/gameLogic/resources/HighScore.txt";
		try (BufferedReader reader = new BufferedReader(new FileReader(absolutePath))) {
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
	}

	private void createSubScene() {
		gameOver = new MenuSubScene();
		gameOver.gameOverScene();
		gamePane.getChildren().add(gameOver);
		gameOverButton();
	}

	private void gameOverButton() {
		MenuButtons over = new MenuButtons("Play Again");
		over.setLayoutX(220);
		over.setLayoutY(480);
		over.setMinWidth(180);
		over.setFont(Font.font("Arial", FontWeight.BOLD, 18));
		gamePane.getChildren().add(over);

		over.setOnAction(new EventHandler<ActionEvent>() {

			@Override
			public void handle(ActionEvent event) {
				resetGame(); // Call the method to reset the game
				gamePane.getChildren().remove(gameOver);
				gamePane.getChildren().remove(over);
			}
		});
	}

	private void resetGame() {
		playerScore = 0;
		doubleLaserPowerUpActive = false;
		shipSpeed = 8;
		createBackground();
		createRocketShip();
		createAsteroids();
		createPlayerLives();
		createStars();
		createPointsBox();
		GameLoop();
	}

	private void createPlayerLives() {
		playerLife = 1;
		playersLifes = new ImageView[2];
		for (int i = 0; i < playersLifes.length; i++) {
			playersLifes[i] = new ImageView("menu/Images/heart.png");
			playersLifes[i].setFitWidth(20);
			playersLifes[i].setFitHeight(20);
			playersLifes[i].setLayoutX(460 + (i * 35));
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
	        gamePane.getChildren().add(extraLife);
	    }
	    
	    playersLifes = newPlayersLifes; // Update the reference
	}



	private void removePlayerLives() {
		gamePane.getChildren().remove(playersLifes[playerLife]);
		playerLife--;

		if (playerLife < 0) {
			gameTimer.stop();
			createSubScene();
			if (highscores.isEmpty() || playerScore > highscores.get(0).getScore()) {
				SubScene enterNameSubScene = createEnterNameSubScene();
				gamePane.getChildren().add(enterNameSubScene);
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

	// Move the ship left and right
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
			checkLaserMeteorCollisions(laser);

		}
	}

	private void checkLaserMeteorCollisions(ImageView laser) {
		for (int i = 0; i < asteroids.length; i++) {
			ImageView asteroid = asteroids[i];
			if (asteroid.isVisible() && areColliding(laser, asteroid)) {
				// Handle collision with brown meteor
				laser.setVisible(false);
				asteroid.setLayoutY(-Math.random() * Game_Height); // Reset to a random position above the screen
				asteroid.setLayoutX(Math.random() * (Game_Width - asteroid.getFitWidth())); // Random X-coordinate
			}
		}
	}

	private boolean areColliding(ImageView object1, ImageView object2) {
		Bounds bounds1 = object1.getBoundsInParent();
		Bounds bounds2 = object2.getBoundsInParent();
		return bounds1.intersects(bounds2);
	}
}





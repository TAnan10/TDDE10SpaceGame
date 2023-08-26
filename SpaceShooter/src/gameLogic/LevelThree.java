package gameLogic;

import java.util.ArrayList;
import java.util.List;
import javafx.animation.AnimationTimer;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Scene;
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

public class LevelThree {

	// Instance Variables
	private AnchorPane gamePane;
	private Scene gameScene;
	private Stage gameStage;
	private Stage menuStage;
	private MenuSubScene gameOver;

	private ImageView rocket;
	private AnimationTimer gameTimer;

	private List<Enemy> enemies;

	private boolean isLeftKeyPressed;
	private boolean isRightKeyPressed;

	private ImageView[] playersLifes;
	private int playerLife;

	public static final int Game_Width = 600;
	public static final int Game_Height = 800;

	private static final String BackgroundImage = "menu/Images/three.png";
	private List<ImageView> backgroundImages = new ArrayList<>();

	private List<ImageView> lasers = new ArrayList<>();
	private static final String LASER_IMAGE = "menu/Images/laserRed02.png";
	private static final int LASER_WIDTH = 10;
	private static final int LASER_HEIGHT = 20;

	// Constructor
	public LevelThree() {
		initializeStage();
		createKeyListeners();
		enemies = new ArrayList<>();
	}

	// Methods

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
		createLasers();
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
				moveEnemies();
				moveLasers();
				handleEnemyCollisions();
			}
		};
		gameTimer.start();
	}

	private void createEnemies() {
		RobotEnemy robotEnemy1 = new RobotEnemy(0, 50, gamePane, 10, rocket, this);
		enemies.add(robotEnemy1);
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
				gamePane.getChildren().remove(gameOver);
				gamePane.getChildren().remove(over);
				resetGame();
			}

		});
	}

	private void resetGame() {
		removeAllEnemies();
		createBackground();
		createRocketShip();
		createPlayerLives();
		createLasers();
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

	    menuButton.setOnAction(new EventHandler<ActionEvent>() {
	        @Override
	        public void handle(ActionEvent event) {
	        	gameStage.hide();
	            menuStage.show();
	        }
	    });
	}

	private void createPlayerLives() {
		playerLife = 6;
		playersLifes = new ImageView[7];
		for (int i = 0; i < playersLifes.length; i++) {
			playersLifes[i] = new ImageView("menu/Images/heart.png");
			playersLifes[i].setFitWidth(20);
			playersLifes[i].setFitHeight(20);
			playersLifes[i].setLayoutX(370 + (i * 40));
			playersLifes[i].setLayoutY(100);
			gamePane.getChildren().add(playersLifes[i]);
		}
	}

	public void removePlayerLives() {
	    if (playerLife >= 0) {
	        gamePane.getChildren().remove(playersLifes[playerLife]);
	        playerLife--;
	        if (playerLife < 0) {
	            gameTimer.stop();
	            createSubScene();
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
				rocket.setLayoutX(rocket.getLayoutX() - 8);
			}
		}

		if (isRightKeyPressed && !isLeftKeyPressed) {
			if (rocket.getLayoutX() < 522) { // 522 is the maximum width of the scene before the ship goes out of the window. So it checks if the ship position is less than 522, if true then it will move 3 pixels to the right.
				rocket.setLayoutX(rocket.getLayoutX() + 8);
			}
		}
	}

	// Laser methods
	private void shootLaser(double x) {
		for (int i = 0; i < lasers.size(); i++) {
			ImageView laser = lasers.get(i);
			laser.toFront();
			if (!laser.isVisible()) {
				laser.setLayoutX(x);
				laser.setLayoutY(675);
				laser.setVisible(true);
				break;
			} 
		}
	}

	private void createLasers() {
		for (int i = 0; i < 10; i++) {
			ImageView laser = new ImageView(LASER_IMAGE);
			lasers.add(laser);
			laser.setFitWidth(LASER_WIDTH);
			laser.setFitHeight(LASER_HEIGHT);
			laser.setVisible(false);
			gamePane.getChildren().add(laser);
		}
	}

	private void moveLasers() {
		for (int i = 0; i < lasers.size(); i++) {
			ImageView laser = lasers.get(i);
			if (laser.isVisible()) {
				laser.setLayoutY(laser.getLayoutY() - 5); // Adjust speed as needed
				if (laser.getLayoutY() < -LASER_HEIGHT) {
					laser.setVisible(false);
				} 
			}
		}
	}
}









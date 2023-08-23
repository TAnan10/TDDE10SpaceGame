package gameLogic;

import java.util.ArrayList;
import java.util.List;
import javafx.animation.AnimationTimer;
import javafx.event.EventHandler;
import javafx.geometry.Bounds;
import javafx.scene.Scene;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

public class TheGame {

	private AnchorPane gamePane;
	private Scene gameScene;
	private Stage gameStage;
	private Stage menuStage;

	private ImageView rocket;
	private AnimationTimer gameTimer;

	private boolean isLeftKeyPressed;
	private boolean isRightKeyPressed;

	private ImageView[] playersLifes;
	private int playerLife;

	private static final int Game_Width = 600;
	private static final int Game_Height = 800;

	private static final String BackgroundImage = "menu/Images/space.jpg";
	private List<ImageView> backgroundImages = new ArrayList<>();

	private final static String asteroidImage = "menu/Images/Asteroid-image.png";
	private ImageView[] asteroids;

	private List<ImageView> lasers = new ArrayList<>();
	private static final String LASER_IMAGE = "menu/Images/laserRed02.png";
	private static final int LASER_WIDTH = 10;
	private static final int LASER_HEIGHT = 20;

	// Constructor
	public TheGame() {
		initializeStage();
		createKeyListeners();
	}

	// Methods

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
		createLasers();
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
				collisionLogic();
				moveLasers();
			}
		};
		gameTimer.start();
	}

	private void createAsteroids() {
		asteroids = new ImageView[5];
		for (int i = 0; i < asteroids.length; i++) {
			asteroids[i] = new ImageView(asteroidImage);
			asteroids[i].setFitWidth(50); // Set appropriate dimensions
			asteroids[i].setFitHeight(50);
			asteroids[i].setLayoutX(Math.random() * (Game_Width - asteroids[i].getFitWidth())); // Random X-coordinate
			asteroids[i].setLayoutY(-Math.random() * Game_Height); // Random Y-coordinate above the screen
			gamePane.getChildren().add(asteroids[i]);
		}
	}

	private void AsteroidsAnimation() {
		for (int i = 0; i < asteroids.length; i++) {
			ImageView asteroid = asteroids[i];
			asteroid.setLayoutY(asteroid.getLayoutY() + 10); // Adjust the speed as needed
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
		}
	}

	private void createPlayerLives() {
		playerLife = 2;
		playersLifes = new ImageView[3];
		for (int i = 0; i < playersLifes.length; i++) {
			playersLifes[i] = new ImageView("menu/Images/heart.png");
			playersLifes[i].setFitWidth(20);
			playersLifes[i].setFitHeight(20);
			playersLifes[i].setLayoutX(485 + (i * 40));
			playersLifes[i].setLayoutY(100);
			gamePane.getChildren().add(playersLifes[i]);
		}
	}

	private void removePlayerLives() {
		gamePane.getChildren().remove(playersLifes[playerLife]);
		playerLife--;
		if (playerLife < 0) {
			gameStage.close();
			gameTimer.stop();
			menuStage.show();
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
				} else {
					checkLaserMeteorCollisions(laser); // Check for collisions
				}
			}
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





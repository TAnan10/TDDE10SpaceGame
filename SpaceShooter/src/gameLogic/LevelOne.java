/**
* This class has all the levelOne specfic methods and variables. In this level we create
* our asteroids, it's animation and the collision logic of the asteroids.
* Since almost the whole game is created in the abstract GameLevel class we just use the
* super keyword in the LevelOne constructor to create everything we need.
*/


package gameLogic;

// Imports
import javafx.animation.AnimationTimer;
import javafx.geometry.Bounds;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;

public class LevelOne extends GameLevel {

	// Variables
	private final static String asteroidImage = "menu/Images/Asteroid.png";
	private ImageView[] asteroids;

	// Constructor
	public LevelOne() {
		super();
	}

	// Methods

	// This method will hide the menu window and show the game scene
	public void createNewGame(Stage menuStage) {
		this.menuStage = menuStage;
		this.menuStage.hide();
		createBackground("menu/Images/leveltwospace.jpg");
		createRocketShip();
		createAsteroids();
		createPlayerLives();
		createStars();
		createPointsBox();
		GameLoop();
		gameStage.show();
	}

	@Override
	public void GameLoop() {
		gameTimer = new AnimationTimer() {
			@Override
			public void handle(long arg0) {
				controlShipAnimation();
                backgroundAnimation();
                createStarsAnimation();
				AsteroidsAnimation();
				collisionLogic();
				moveLasers();
			}
		};
		gameTimer.start();
	}

	public void resetGame() {
		super.resetGame();
		createAsteroids();
	}

	// LevelOne specfic methods
	private void createAsteroids() {
		asteroids = new ImageView[ 5];
		for (int i = 0; i < asteroids.length; i++) {
			asteroids[i] = new ImageView(asteroidImage);
			asteroids[i].setFitWidth(50); 
			asteroids[i].setFitHeight(50);
			asteroids[i].setLayoutX(Math.random() * (getGameWidth() - asteroids[i].getFitWidth())); // Random X-coordinate
			asteroids[i].setLayoutY(-Math.random() * getGameHeight()); // Random Y-coordinate above the screen
			getGamePane().getChildren().add(asteroids[i]);
		}
	}

	private void AsteroidsAnimation() {
		for (int i = 0; i < asteroids.length; i++) {
			ImageView asteroid = asteroids[i];
			asteroid.setLayoutY(asteroid.getLayoutY() + 5); 
			if (asteroid.getLayoutY() >= getGameHeight()) {
				asteroid.setLayoutY(-Math.random() * getGameHeight()); // Reset to a random position above the screen
				asteroid.setLayoutX(Math.random() * (getGameWidth() - asteroid.getFitWidth())); // Random X-coordinate
			}
		}
	}

	private void collisionLogic() {
		for (int i = 0; i < asteroids.length; i++) {
			ImageView asteroid = asteroids[i];
			if (getRocket().getBoundsInParent().intersects(asteroid.getBoundsInParent())) {
				removePlayerLives();
				asteroid.setLayoutY(-Math.random() * getGameHeight()); // Reset to a random position above the screen
				asteroid.setLayoutX(Math.random() * (getGameWidth() - asteroid.getFitWidth())); // Random X-coordinate
			}
			starPoints();
		}
	}

	public void moveLasers() {
		for (int i = 0; i < lasers.size(); i++) {
			ImageView laser = lasers.get(i);
			laser.setLayoutY(laser.getLayoutY() - 10);
			if (laser.getLayoutY() > LevelTwo.getGameHeight()) {
				lasers.remove(i);
				getGamePane().getChildren().remove(laser);
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
				asteroid.setLayoutY(-Math.random() * getGameHeight()); // Reset to a random position above the screen
				asteroid.setLayoutX(Math.random() * (getGameWidth() - asteroid.getFitWidth())); // Random X-coordinate
			}
		}
	}

	private boolean areColliding(ImageView object1, ImageView object2) {
		Bounds bounds1 = object1.getBoundsInParent();
		Bounds bounds2 = object2.getBoundsInParent();
		return bounds1.intersects(bounds2);
	}
}

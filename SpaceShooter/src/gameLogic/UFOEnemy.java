/**
* UfoEnemy extends Enemy class uses super to implement the image, speed, height
* and width. RobotEnemy also has methods on movement, shooting lasers and collision 
* logic for lasers and with player's ship. We also initialize a health for the enemy.
*/

package gameLogic;

// Imports
import java.util.ArrayList;
import java.util.List;
import javafx.geometry.Bounds;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;

public class UFOEnemy extends Enemy {

	// Variables
	private boolean movingRight = true;
	private double getX = getX();
	private double getSpeed = getSpeed();
	private AnchorPane gamePane;
	private int health = 100;

	private ImageView rocket;
	private LevelTwo level;

	private List<ImageView> ufoLasers = new ArrayList<>();
	private static final String UFO_LASER_IMAGE = "menu/Images/laserGreen04.png";  // Change this to the UFO's laser image
	private static final int UFO_LASER_WIDTH = 15;
	private static final int UFO_LASER_HEIGHT = 40;

	// Constructor
	public UFOEnemy(double x, double y, AnchorPane gamePane, int initialHealth, ImageView rocket, LevelTwo level) {
		super("menu/Images/ufo.png", x, y, 8, 0.1, 100, 100);
		this.gamePane = gamePane;
		this.health = initialHealth;
		this.rocket = rocket;
		this.level = level;
	}

	public List<ImageView> getUfoLasers() {
		return ufoLasers;
	}
	
	// Methods

	@Override
	public void move() {
		super.move();
		moveUfoLasers();

		double ufoX = getEnemyImage().getLayoutX();
		double ufoY = getEnemyImage().getLayoutY();

		if (Math.random() < 0.01) {
			shootLaser(ufoX + getEnemyImage().getFitWidth() / 2, ufoY);
		}

		if (movingRight) {
			getX += getSpeed;
			if (getX + enemyImage.getFitWidth() >= LevelTwo.getGameHeight()) {
				movingRight = false;
			}
		} else {
			getX -= getSpeed;
			if (getX <= 0) {
				movingRight = true;
			}
		}
		enemyImage.setLayoutX(getX);
	}

	private void shootLaser(double x, double y) {
		ImageView ufoLaser = new ImageView(UFO_LASER_IMAGE);
		ufoLaser.setLayoutX(x - UFO_LASER_WIDTH / 2);
		ufoLaser.setLayoutY(y + getEnemyImage().getFitHeight());
		ufoLaser.setFitWidth(UFO_LASER_WIDTH);
		ufoLaser.setFitHeight(UFO_LASER_HEIGHT);
		ufoLasers.add(ufoLaser);
		gamePane.getChildren().add(ufoLaser);
	}

	private void moveUfoLasers() {
		for (int i = 0; i < ufoLasers.size(); i++) {
			ImageView ufoLaser = ufoLasers.get(i);
			ufoLaser.setLayoutY(ufoLaser.getLayoutY() + 3); // Adjust laser speed as needed

			// Check for collision with player's ship
			if (areColliding(ufoLaser, rocket)) {
				ufoLasers.remove(i);
				gamePane.getChildren().remove(ufoLaser);
				i--;
				level.removePlayerLives();
			}

			if (ufoLaser.getLayoutY() > LevelTwo.getGameHeight()) {
				ufoLasers.remove(i);
				gamePane.getChildren().remove(ufoLaser);
				i--;
			}
		}
	}

	@Override
	public void handleCollision(LevelTwo level) {
		checkLaserUFOCollisions(level.getLasers()); // Pass lasers from LevelTwo class
	}

	private void checkLaserUFOCollisions(List<ImageView> lasers) {
		for (ImageView laser : lasers) {
			if (laser.isVisible() && areColliding(laser, getEnemyImage())) {
				laser.setVisible(false);
				decreaseHealth(); // Decrease UFO's health
				if (health <= 0) {
					destroy(); // Destroy the UFO if its health is zero or less
				}
			}
		}
	}

	private void decreaseHealth() {
		health--;
	}

	private void destroy() {
		gamePane.getChildren().remove(getEnemyImage()); // Remove UFO from the game pane
		level.showVictoryScreen();
	}

	private boolean areColliding(ImageView object1, ImageView object2) {
		Bounds bounds1 = object1.getBoundsInParent();
		Bounds bounds2 = object2.getBoundsInParent();
		return bounds1.intersects(bounds2);
	}

	@Override
	protected void handleCollision(LevelThree levelThree) {
	}
}

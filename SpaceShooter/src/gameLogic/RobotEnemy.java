/**
* RobotEnemy extends Enemy class uses super to implement the image, speed, height
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

public class RobotEnemy extends Enemy {

	// Variables
	private AnchorPane gamePane;
	private int health = 15;

	private ImageView robot;
	private LevelSix level;

	private List<ImageView> robotLasers = new ArrayList<>();
	private static final String ROBOT_LASER_IMAGE = "menu/Images/laserGreen04.png";
	private static final int ROBOT_LASER_WIDTH = 15;
	private static final int ROBOT_LASER_HEIGHT = 40;

	// Constructor
	public RobotEnemy(double x, double y, AnchorPane gamePane, int initialHealth, ImageView robot, LevelSix level) {
		super("menu/Images/robotOrange.png", x, y, 10, 0.5, 100, 100);
		this.gamePane = gamePane;
		this.health = initialHealth;
		this.robot = robot;
		this.level = level;
	}

	public List<ImageView> getUfoLasers() {
		return robotLasers;
	}

	@Override
	public void move() {
	    super.move();
	    moveRobotLasers();

	    double ufoX = getEnemyImage().getLayoutX();
	    double ufoY = getEnemyImage().getLayoutY();

	    if (Math.random() < 0.04) {
	        shootLaser(ufoX + getEnemyImage().getFitWidth() / 2, ufoY);
	    }

	    // Random movement
	    if (Math.random() < 0.01) {
	        double randomX = Math.random() * (LevelSix.getGameHeight() - enemyImage.getFitWidth());
	        double randomY = Math.random() * (LevelSix.getGameHeight() / 2); // Limit vertical range
	        moveTo(randomX, randomY);
	    }
	}
	
	public void moveTo(double x, double y) {
        enemyImage.setLayoutX(x);
        enemyImage.setLayoutY(y);
    }


	private void shootLaser(double x, double y) {
		ImageView robotLaser = new ImageView(ROBOT_LASER_IMAGE);
		robotLaser.setLayoutX(x - ROBOT_LASER_WIDTH / 2);
		robotLaser.setLayoutY(y + getEnemyImage().getFitHeight());
		robotLaser.setFitWidth(ROBOT_LASER_WIDTH);
		robotLaser.setFitHeight(ROBOT_LASER_HEIGHT);
		robotLasers.add(robotLaser);
		gamePane.getChildren().add(robotLaser);
	}

	private void moveRobotLasers() {
		for (int i = 0; i < robotLasers.size(); i++) {
			ImageView ufoLaser = robotLasers.get(i);
			ufoLaser.setLayoutY(ufoLaser.getLayoutY() + 3); // Adjust laser speed as needed

			// Check for collision with player's ship
			if (areColliding(ufoLaser, robot)) {
				robotLasers.remove(i);
				gamePane.getChildren().remove(ufoLaser);
				i--;
				level.removePlayerLives();
			}

			if (ufoLaser.getLayoutY() > LevelSix.getGameHeight()) {
				robotLasers.remove(i);
				gamePane.getChildren().remove(ufoLaser);
				i--;
			}
		}
	}

	@Override
	public void handleCollision(LevelFive level) {
		
	}

	private void checkLaserRobotCollisions(List<ImageView> lasers) {
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
	protected void handleCollision(LevelSix levelSix) {
		checkLaserRobotCollisions(level.getLasers());
	}
}

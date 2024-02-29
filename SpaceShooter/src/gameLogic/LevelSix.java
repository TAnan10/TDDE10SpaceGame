/**
* LevelSix extends GameLevel and uses super keyword to access all the methods
* to create the game as well as having it's own methods such as creating enemies
* the animation of enemies, lasers and collision logic. The enemy is from the Robot enemy
* class.
*/

package gameLogic;

// Imports
import java.util.ArrayList;
import java.util.List;
import javafx.animation.AnimationTimer;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;

public class LevelSix extends GameLevel {
	// Variablers
	private List<Enemy> enemies;

	//Constructor
	public LevelSix() {
		super();
		enemies = new ArrayList<>();
	}

	public List<ImageView> getLasers() {
		return lasers;
	}

	// Class Level specfic methods
	public void createNewGame(Stage menuStage) {
		this.menuStage = menuStage;
		this.menuStage.hide();
		createBackground("menu/Images/three.png");
		createRocketShip();
		createPlayerLives();
		createPointsBox();
		createLasers();
		createEnemies();
		GameLoop();
		gameStage.show();
	}

	public void GameLoop() {
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

	public void resetGame() {
		super.resetGame();
		removeAllEnemies();
		createEnemies();
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

	private void removeAllEnemies() {
		for (Enemy enemy : enemies) {
			gamePane.getChildren().remove(enemy.getEnemyImage());
		}
		enemies.clear();
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
				laser.setLayoutY(laser.getLayoutY() - 3); // Adjust speed as needed
				if (laser.getLayoutY() < -LASER_HEIGHT) {
					laser.setVisible(false);
				} 
			}
		}
	}
}

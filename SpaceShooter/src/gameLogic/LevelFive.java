/**
* LevelFive extends GameLevel and uses super keyword to access all the methods
* to create the game as well as having it's own methods such as creating enemies
* the animation of enemies, lasers and collision logic. The enemy is from the UFO enemy
* class.
*/

package gameLogic;

// Imports
import java.util.ArrayList;
import java.util.List;
import javafx.animation.AnimationTimer;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;

public class LevelFive extends GameLevel {
	
	// Variables
	private List<Enemy> enemies;
	
	// Constructor
	public LevelFive() {
		super();
		enemies = new ArrayList<>();
	}
	
	// Class specific methods
	
	public List<ImageView> getLasers() {
		return lasers;
	}
	
	// This method will hide the menu window and show the game scene
		public void createNewGame(Stage menuStage) {
			this.menuStage = menuStage;
			this.menuStage.hide();
			createBackground("menu/Images/space.jpg");
			createRocketShip();
			createPlayerLives();
			createStars();
			createPointsBox();
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
					createStarsAnimation();
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

		private void removeAllEnemies() {
			for (Enemy enemy : enemies) {
				gamePane.getChildren().remove(enemy.getEnemyImage());
			}
			enemies.clear();
		}
		
		private void createEnemies() {
			UFOEnemy ufoEnemy1 = new UFOEnemy(0, 50, gamePane, 13, rocket, this);
			UFOEnemy ufoEnemy2 = new UFOEnemy(150, 60, gamePane, 5, rocket, this);
			enemies.add(ufoEnemy1);
			enemies.add(ufoEnemy2);
			
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
		
		private void moveLasers() {
			for (int i = 0; i < lasers.size(); i++) {
				ImageView laser = lasers.get(i);
				laser.setLayoutY(laser.getLayoutY() - 5);
				if (laser.getLayoutY() > LevelFive.getGameHeight()) {
					lasers.remove(i);
					gamePane.getChildren().remove(laser);
					i--;
				}
			}
		}
	
}

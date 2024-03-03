/**
* Enemy is an abstract class for our subclasses of UFOEnemy and RobotEnemy
* It's used to create the image of the enemy, and to give value of it's x and y coordinate
* and speed. It also has a asbtract collision method that every enemy must create
*/

package gameLogic;

// Imports
import javafx.scene.image.ImageView;

public abstract class Enemy {

    public ImageView enemyImage;
    private double x, y;
    private double speed;
    private double verticalSpeed;
    private double width;
    private double height;    

    // Constructor
    public Enemy(String imagePath, double x, double y, double speed, double verticalSpeed, double width, double height) {
        enemyImage = new ImageView(imagePath);
        enemyImage.setLayoutX(x);
        enemyImage.setLayoutY(y);
        this.x = x;
        this.y = y;
        this.speed = speed;
        this.verticalSpeed = verticalSpeed;
        this.height = height;
        this.width = width;
        enemyImage.setFitHeight(height);
        enemyImage.setFitWidth(width);
    }
    
    // Getter Methods
    public Double getX() {
    	return x;
    }
    
    public Double getY() {
    	return y;
    }
    
    public Double getSpeed() {
    	return speed;
    }

    public ImageView getEnemyImage() {
        return enemyImage;
    }
    
    // Methods
    public void move() {
        enemyImage.setLayoutY(enemyImage.getLayoutY() + verticalSpeed);
    }
    
    // Abstract Methods
    public abstract void handleCollision(LevelTwo level);

	protected abstract void handleCollision(LevelThree levelThree);
}

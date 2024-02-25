package gameLogic;
//2nd version
import javafx.scene.image.ImageView;

public abstract class Enemy {

    public ImageView enemyImage;
    private double x, y;
    private double speed;
    private double verticalSpeed;
    private double width;
    private double height;    

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

    public void move() {
        enemyImage.setLayoutY(enemyImage.getLayoutY() + verticalSpeed);
    }

    public abstract void handleCollision(LevelFive level);

	protected abstract void handleCollision(LevelSix levelSix);
}

package gameLogic;

import javafx.geometry.Bounds;
import javafx.scene.image.ImageView;

public class Star {
	
	//Variables
	private ImageView starImageView;
	
	// Constructor
	public Star(String ImageUrl) {
		starImageView = new ImageView(ImageUrl);
	}
	
	// Methods
	
	// Getter Method for the star Image
	public ImageView getStarImageView() {
        return starImageView;
    }
	
	public void starPosition() {
		starImageView.setFitWidth(50); 
		starImageView.setFitHeight(50);
		starImageView.setLayoutX(Math.random() * (LevelOne.getGameHeight() - starImageView.getFitWidth())); // Random X-coordinate
		starImageView.setLayoutY(-Math.random() * LevelOne.getGameHeight()); // Random Y-coordinate above the screen
	}

	// Animation for moving the star
	public void move() {
        starImageView.setLayoutY(starImageView.getLayoutY() + 5);
        if (starImageView.getLayoutY() >= LevelOne.getGameHeight()) {
        	starImageView.setLayoutY(-Math.random() * LevelOne.getGameHeight()); // Reset to a random position above the screen
        	starImageView.setLayoutX(Math.random() * (LevelOne.getGameHeight() - starImageView.getFitWidth())); // Random X-coordinate
		}
    }
	
	public boolean checkCollision(ImageView playerShip) {
        Bounds starBounds = starImageView.getBoundsInParent();
        Bounds shipBounds = playerShip.getBoundsInParent();
        return starBounds.intersects(shipBounds);
    }
}

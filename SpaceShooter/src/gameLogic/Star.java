/**
* Here we create the image of the star, where the star spawns in the game, how it moves 
* and the collision between the star and the ship.
*/

package gameLogic;

// Imports
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
	
	// Randomize where the star spawns of the screen
	public void starPosition() {
		starImageView.setFitWidth(50); 
		starImageView.setFitHeight(50);
		starImageView.setLayoutX(Math.random() * (LevelOne.getGameHeight() - starImageView.getFitWidth())); 
		starImageView.setLayoutY(-Math.random() * LevelOne.getGameHeight()); 
	}

	// Animation for moving the star
	public void move() {
        starImageView.setLayoutY(starImageView.getLayoutY() + 8);
        if (starImageView.getLayoutY() >= LevelOne.getGameHeight()) {
        	starImageView.setLayoutY(-Math.random() * LevelOne.getGameHeight()); 
        	starImageView.setLayoutX(Math.random() * (LevelOne.getGameHeight() - starImageView.getFitWidth())); 
		}
    }
	
	// Collision of start and ship
	public boolean checkCollision(ImageView playerShip) {
        Bounds starBounds = starImageView.getBoundsInParent();
        Bounds shipBounds = playerShip.getBoundsInParent();
        return starBounds.intersects(shipBounds);
    }
}

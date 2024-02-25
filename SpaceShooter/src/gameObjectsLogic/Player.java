package gameObjectsLogic;

import javafx.scene.image.ImageView;

public class Player {
	
	// Variables
	private ImageView rocketImage;
	private int playerLife;
	private int playerScore;
	private boolean doubleLaserPowerUpActive;
	
	// Constructor
	public Player(ImageView rocketImage) {
		this.rocketImage = rocketImage;
		this.playerLife = 3;
		this.playerScore = 0;
		this.doubleLaserPowerUpActive = false;
	}
	
	// Methods
	
	// Getter methods for Player variables
	public int getPlayerLife() {
        return playerLife;
    }
    
    public int getPlayerScore() {
        return playerScore;
    }
    
    public boolean isDoubleLaserPowerUpActive() {
        return doubleLaserPowerUpActive;
    }

}



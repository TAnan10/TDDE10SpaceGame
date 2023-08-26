package gameLogic;

public class HighScores {
	
	// Variables
	private String name;
    private int score;
	
	// Constructor
    public HighScores(String name, int score) {
        this.name = name;
        this.score = score;
    }
	
	// Methods
    public String getName() {
        return name;
    }
    
    public int getScore() {
        return score;
    }

}

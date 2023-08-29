package gameLogic;

public class HighScores {
	
	// Variables
	private String level;
	private String name;
    private int score;
	
	// Constructor
    public HighScores(String level, String name, int score) {
    	this.level = level;
        this.name = name;
        this.score = score;
    }
	
	// Methods
    public String getLevel() {
    	return level;
    }
    
    public String getName() {
        return name;
    }
    
    public int getScore() {
        return score;
    }

}

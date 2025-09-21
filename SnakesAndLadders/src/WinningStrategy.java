/**
 * Represents different winning strategies available in the game.
 * Makes the code more human-readable and extensible.
 */
public enum WinningStrategy {
    /**
     * Player must land exactly on the final position to win.
     * If dice roll would exceed final position, player stays put.
     */
    EXACT_LANDING("Must land exactly on position 100"),
    
    /**
     * Player wins by reaching or crossing the final position.
     * Any roll that reaches or exceeds final position wins.
     */
    CROSS_FINISH_LINE("Win by reaching or crossing position 100");
    
    private final String description;
    
    WinningStrategy(String description) {
        this.description = description;
    }
    
    public String getDescription() {
        return description;
    }
}
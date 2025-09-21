/**
 * Different winning strategies for extensible game rules.
 */
public enum WinningStrategy {
    EXACT_LANDING("Must land exactly on position 100"),
    
    CROSS_FINISH_LINE("Win by reaching or crossing position 100");
    
    private final String description;
    
    WinningStrategy(String description) {
        this.description = description;
    }
    
    public String getDescription() {
        return description;
    }
}
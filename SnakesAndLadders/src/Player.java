/**
 * Represents a player with enhanced state tracking for rule-based gameplay.
 */
public class Player {
    private final String name;
    private Position currentPosition;
    private boolean hasEnteredBoard;
    private int consecutiveSixes;
    private int totalMoves;
    
    public Player(String name) {
        if (name == null || name.trim().isEmpty()) {
            throw new IllegalArgumentException("Player name cannot be null or empty");
        }
        this.name = name.trim();
        this.currentPosition = new Position(1);
        this.hasEnteredBoard = false;
        this.consecutiveSixes = 0;
        this.totalMoves = 0;
    }
    
    public String getName() {
        return name;
    }
    
    public Position getCurrentPosition() {
        return currentPosition;
    }
    
    public void setCurrentPosition(Position position) {
        if (position == null) {
            throw new IllegalArgumentException("Position cannot be null");
        }
        this.currentPosition = position;
    }
    
    /**
     * Checks if player has won based on current position.
     */
    public boolean hasWon() {
        return currentPosition.getValue() == 100;
    }
    
    /**
     * Checks if player has won based on winning strategy.
     */
    public boolean hasWon(WinningStrategy strategy) {
        switch (strategy) {
            case EXACT_LANDING:
                return currentPosition.getValue() == 100;
            case CROSS_FINISH_LINE:
                return currentPosition.getValue() >= 100;
            default:
                return hasWon();
        }
    }
    
    /**
     * Has the player entered the board yet?
     */
    public boolean hasEnteredBoard() {
        return hasEnteredBoard;
    }
    
    /**
     * Mark player as having entered the board.
     */
    public void enterBoard() {
        this.hasEnteredBoard = true;
    }
    
    /**
     * Get consecutive sixes count.
     */
    public int getConsecutiveSixes() {
        return consecutiveSixes;
    }
    
    /**
     * Record a six being rolled.
     */
    public void rollSix() {
        consecutiveSixes++;
        totalMoves++;
    }
    
    /**
     * Record a non-six being rolled.
     */
    public void rollNonSix() {
        consecutiveSixes = 0;
        totalMoves++;
    }
    
    /**
     * Reset consecutive sixes counter.
     */
    public void resetConsecutiveSixes() {
        consecutiveSixes = 0;
    }
    
    /**
     * Send player back to starting position (penalty).
     */
    public void sendToStart() {
        this.currentPosition = new Position(1);
        this.hasEnteredBoard = false;
        this.consecutiveSixes = 0;
    }
    
    /**
     * Get total moves made by this player.
     */
    public int getTotalMoves() {
        return totalMoves;
    }
    
    /**
     * Check if player is at starting position.
     */
    public boolean isAtStart() {
        return currentPosition.getValue() == 1 && !hasEnteredBoard;
    }
    
    @Override
    public String toString() {
        return "Player{name='" + name + "', position=" + currentPosition.getValue() + 
               ", entered=" + hasEnteredBoard + ", moves=" + totalMoves + "}";
    }
}
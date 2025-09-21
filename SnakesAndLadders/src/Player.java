/**
 * Represents a player in the Snakes and Ladders game.
 * Follows Single Responsibility Principle - manages player state.
 * Follows Open/Closed Principle - can be extended without modification.
 */
public class Player {
    private final String name;
    private Position currentPosition;
    
    public Player(String name) {
        if (name == null || name.trim().isEmpty()) {
            throw new IllegalArgumentException("Player name cannot be null or empty");
        }
        this.name = name.trim();
        this.currentPosition = new Position(1); // Start at position 1
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
    
    public boolean hasWon() {
        return currentPosition.getValue() == 100;
    }
    
    @Override
    public String toString() {
        return "Player{name='" + name + "', position=" + currentPosition.getValue() + "}";
    }
}
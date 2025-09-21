/**
 * Represents a position on the Snakes and Ladders board.
 * Follows Single Responsibility Principle - only manages position data.
 */
public class Position {
    private final int value;
    
    public Position(int value) {
        if (value < 1 || value > 100) {
            throw new IllegalArgumentException("Position must be between 1 and 100");
        }
        this.value = value;
    }
    
    public int getValue() {
        return value;
    }
    
    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        Position position = (Position) obj;
        return value == position.value;
    }
    
    @Override
    public int hashCode() {
        return Integer.hashCode(value);
    }
    
    @Override
    public String toString() {
        return "Position{" + value + "}";
    }
}
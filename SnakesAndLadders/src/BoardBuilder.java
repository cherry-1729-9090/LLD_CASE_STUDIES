import java.util.*;

/**
 * Builder pattern implementation for creating Board objects.
 * Follows Builder Design Pattern for complex object construction.
 * Follows Single Responsibility - only builds Board objects.
 */
public class BoardBuilder {
    int size = 100; // Default board size
    List<Snake> snakes = new ArrayList<>();
    List<Ladder> ladders = new ArrayList<>();
    
    public BoardBuilder() {
        // Default constructor
    }
    
    public BoardBuilder setSize(int size) {
        if (size < 10 || size > 1000) {
            throw new IllegalArgumentException("Board size must be between 10 and 1000");
        }
        this.size = size;
        return this;
    }
    
    public BoardBuilder addSnake(Position head, Position tail) {
        if (head == null || tail == null) {
            throw new IllegalArgumentException("Snake positions cannot be null");
        }
        validatePositionOnBoard(head);
        validatePositionOnBoard(tail);
        
        Snake snake = new Snake(head, tail);
        
        // Ensure no conflicts with existing snakes/ladders
        if (hasConflict(head)) {
            throw new IllegalArgumentException("Position " + head.getValue() + " already has a snake or ladder");
        }
        
        snakes.add(snake);
        return this;
    }
    
    public BoardBuilder addLadder(Position bottom, Position top) {
        if (bottom == null || top == null) {
            throw new IllegalArgumentException("Ladder positions cannot be null");
        }
        validatePositionOnBoard(bottom);
        validatePositionOnBoard(top);
        
        Ladder ladder = new Ladder(bottom, top);
        
        // Ensure no conflicts with existing snakes/ladders
        if (hasConflict(bottom)) {
            throw new IllegalArgumentException("Position " + bottom.getValue() + " already has a snake or ladder");
        }
        
        ladders.add(ladder);
        return this;
    }
    
    /**
     * Creates a board with default snakes and ladders for quick setup.
     */
    public BoardBuilder withDefaultSnakesAndLadders() {
        // Add some default snakes
        addSnake(new Position(99), new Position(78));
        addSnake(new Position(95), new Position(75));
        addSnake(new Position(92), new Position(88));
        addSnake(new Position(89), new Position(68));
        addSnake(new Position(74), new Position(53));
        addSnake(new Position(64), new Position(60));
        addSnake(new Position(62), new Position(19));
        addSnake(new Position(46), new Position(25));
        addSnake(new Position(37), new Position(3));
        
        // Add some default ladders
        addLadder(new Position(1), new Position(38));
        addLadder(new Position(4), new Position(14));
        addLadder(new Position(9), new Position(21));
        addLadder(new Position(16), new Position(42));
        addLadder(new Position(20), new Position(77));
        addLadder(new Position(28), new Position(84));
        addLadder(new Position(40), new Position(59));
        addLadder(new Position(51), new Position(67));
        addLadder(new Position(63), new Position(81));
        addLadder(new Position(71), new Position(91));
        
        return this;
    }
    
    public Board build() {
        return new Board(this);
    }
    
    private void validatePositionOnBoard(Position position) {
        if (position.getValue() < 1 || position.getValue() >= size) {
            throw new IllegalArgumentException("Position must be between 1 and " + (size - 1));
        }
    }
    
    private boolean hasConflict(Position position) {
        // Check for conflicts with existing snakes
        for (Snake snake : snakes) {
            if (snake.hasSnakeAt(position)) {
                return true;
            }
        }
        
        // Check for conflicts with existing ladders
        for (Ladder ladder : ladders) {
            if (ladder.hasLadderAt(position)) {
                return true;
            }
        }
        
        return false;
    }
}
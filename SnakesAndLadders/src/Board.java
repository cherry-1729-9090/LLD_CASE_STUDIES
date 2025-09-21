import java.util.*;

/**
 * Game board containing snakes and ladders with immutable state.
 */
public class Board {
    private final int size;
    private final List<Snake> snakes;
    private final List<Ladder> ladders;
    
    Board(BoardBuilder builder) {
        this.size = builder.size;
        this.snakes = Collections.unmodifiableList(new ArrayList<>(builder.snakes));
        this.ladders = Collections.unmodifiableList(new ArrayList<>(builder.ladders));
    }
    
    public int getSize() {
        return size;
    }
    
    public List<Snake> getSnakes() {
        return snakes;
    }
    
    public List<Ladder> getLadders() {
        return ladders;
    }
    
    /**
     * Gets the final position after considering snakes and ladders.
     */
    public Position getFinalPosition(Position position) {
        // Check for snake
        for (Snake snake : snakes) {
            if (snake.hasSnakeAt(position)) {
                return snake.getDestination();
            }
        }
        
        // Check for ladder
        for (Ladder ladder : ladders) {
            if (ladder.hasLadderAt(position)) {
                return ladder.getDestination();
            }
        }
        
        return position;
    }
    
    /**
     * Validates if a position is valid on this board.
     */
    public boolean isValidPosition(Position position) {
        return position.getValue() >= 1 && position.getValue() <= size;
    }
    
    @Override
    public String toString() {
        return "Board{size=" + size + ", snakes=" + snakes.size() + ", ladders=" + ladders.size() + "}";
    }
}
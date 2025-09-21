import java.util.Random;

/**
 * Standard implementation of a 6-sided dice.
 * Follows Single Responsibility Principle - only handles dice rolling.
 * Implements Strategy Pattern through Dice interface.
 */
public class StandardDice implements Dice {
    private static final int MIN_VALUE = 1;
    private static final int MAX_VALUE = 6;
    
    private final Random random;
    
    public StandardDice() {
        this.random = new Random();
    }
    
    // Constructor for testing with seed
    public StandardDice(long seed) {
        this.random = new Random(seed);
    }
    
    @Override
    public int roll() {
        return random.nextInt(MAX_VALUE - MIN_VALUE + 1) + MIN_VALUE;
    }
    
    @Override
    public int getMinValue() {
        return MIN_VALUE;
    }
    
    @Override
    public int getMaxValue() {
        return MAX_VALUE;
    }
    
    @Override
    public String toString() {
        return "StandardDice{min=" + MIN_VALUE + ", max=" + MAX_VALUE + "}";
    }
}
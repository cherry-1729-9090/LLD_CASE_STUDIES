/**
 * Interface for dice implementations.
 * Follows Dependency Inversion Principle - high-level modules depend on abstractions.
 * Follows Interface Segregation Principle - clients depend only on methods they use.
 */
public interface Dice {
    /**
     * Rolls the dice and returns the result.
     * @return the dice roll result
     */
    int roll();
    
    /**
     * Gets the minimum possible value from this dice.
     * @return minimum value
     */
    int getMinValue();
    
    /**
     * Gets the maximum possible value from this dice.
     * @return maximum value
     */
    int getMaxValue();
}
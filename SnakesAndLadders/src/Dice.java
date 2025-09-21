/**
 * Dice interface following Dependency Inversion and Interface Segregation principles.
 */
public interface Dice {
    int roll();
    
    int getMinValue();
    
    int getMaxValue();
}
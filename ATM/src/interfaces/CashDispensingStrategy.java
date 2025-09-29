package interfaces;

import java.util.Map;

// Strategy pattern interface
public interface CashDispensingStrategy {
    
    /**
     * Dispense cash using the specific algorithm
     * @param amount The amount to dispense
     * @param availableNotes Map of available note denominations to their counts
     * @return Map representing the notes to be dispensed
     */
    Map<Integer, Integer> dispense(int amount, Map<Integer, Integer> availableNotes);
}
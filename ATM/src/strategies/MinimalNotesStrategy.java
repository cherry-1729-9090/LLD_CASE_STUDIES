package strategies;

import interfaces.CashDispensingStrategy;
import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;

// Dispenses minimum number of notes using greedy algorithm
public class MinimalNotesStrategy implements CashDispensingStrategy {
    
    @Override
    public Map<Integer, Integer> dispense(int amount, Map<Integer, Integer> availableNotes) {
        System.out.println("Using MinimalNotesStrategy to dispense $" + amount);
        
        if (amount <= 0) {
            throw new IllegalArgumentException("Amount must be positive");
        }
        
        // Use TreeMap with reverse order to process highest denominations first
        TreeMap<Integer, Integer> sortedNotes = new TreeMap<>((a, b) -> b.compareTo(a));
        sortedNotes.putAll(availableNotes);
        
        Map<Integer, Integer> result = new HashMap<>();
        int remainingAmount = amount;
        
        // Greedy algorithm: start with highest denomination
        for (Map.Entry<Integer, Integer> entry : sortedNotes.entrySet()) {
            int denomination = entry.getKey();
            int availableCount = entry.getValue();
            
            if (remainingAmount >= denomination && availableCount > 0) {
                int notesNeeded = Math.min(remainingAmount / denomination, availableCount);
                
                if (notesNeeded > 0) {
                    result.put(denomination, notesNeeded);
                    remainingAmount -= (notesNeeded * denomination);
                    
                    System.out.println("Selected " + notesNeeded + " notes of $" + denomination);
                }
            }
            
            if (remainingAmount == 0) {
                break;
            }
        }
        
        // Check if we could dispense the exact amount
        if (remainingAmount > 0) {
            System.out.println("Cannot dispense exact amount $" + amount + 
                             ". Remaining: $" + remainingAmount);
            return null; // Cannot dispense exact amount
        }
        
        System.out.println("Successfully calculated dispensing for $" + amount);
        return result;
    }
}
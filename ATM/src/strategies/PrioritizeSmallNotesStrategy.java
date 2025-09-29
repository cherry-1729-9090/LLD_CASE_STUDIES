package strategies;

import interfaces.CashDispensingStrategy;
import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;

// Prioritizes smaller notes to maintain balanced inventory
public class PrioritizeSmallNotesStrategy implements CashDispensingStrategy {
    
    @Override
    public Map<Integer, Integer> dispense(int amount, Map<Integer, Integer> availableNotes) {
        System.out.println("Using PrioritizeSmallNotesStrategy to dispense $" + amount);
        
        if (amount <= 0) {
            throw new IllegalArgumentException("Amount must be positive");
        }
        
        // Use TreeMap with natural order to process smallest denominations first
        TreeMap<Integer, Integer> sortedNotes = new TreeMap<>();
        sortedNotes.putAll(availableNotes);
        
        Map<Integer, Integer> result = new HashMap<>();
        int remainingAmount = amount;
        
        // Try to use smaller denominations first, but with limits to avoid too many small notes
        for (Map.Entry<Integer, Integer> entry : sortedNotes.entrySet()) {
            int denomination = entry.getKey();
            int availableCount = entry.getValue();
            
            if (remainingAmount >= denomination && availableCount > 0) {
                int maxSmallNotes = calculateMaxSmallNotes(denomination, amount);
                int notesNeeded = Math.min(
                    Math.min(remainingAmount / denomination, availableCount),
                    maxSmallNotes
                );
                
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
        
        // If we still have remaining amount, use larger denominations
        if (remainingAmount > 0) {
            System.out.println("Still need to dispense $" + remainingAmount + 
                             ". Using larger denominations.");
            
            // Use reverse order for remaining amount
            TreeMap<Integer, Integer> reverseSortedNotes = new TreeMap<>((a, b) -> b.compareTo(a));
            reverseSortedNotes.putAll(availableNotes);
            
            for (Map.Entry<Integer, Integer> entry : reverseSortedNotes.entrySet()) {
                int denomination = entry.getKey();
                int availableCount = entry.getValue();
                int alreadyUsed = result.getOrDefault(denomination, 0);
                int stillAvailable = availableCount - alreadyUsed;
                
                if (remainingAmount >= denomination && stillAvailable > 0) {
                    int notesNeeded = Math.min(remainingAmount / denomination, stillAvailable);
                    
                    if (notesNeeded > 0) {
                        result.put(denomination, result.getOrDefault(denomination, 0) + notesNeeded);
                        remainingAmount -= (notesNeeded * denomination);
                        
                        System.out.println("Added " + notesNeeded + " more notes of $" + denomination);
                    }
                }
                
                if (remainingAmount == 0) {
                    break;
                }
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
    
    /**
     * Calculate maximum number of small notes to use to avoid giving too many small denominations
     */
    private int calculateMaxSmallNotes(int denomination, int totalAmount) {
        // Limit small notes to avoid giving too many
        if (denomination <= 20) {
            return Math.min(10, totalAmount / denomination / 2); // Max 10 small notes, or half of what's needed
        } else if (denomination <= 50) {
            return Math.min(5, totalAmount / denomination); // Max 5 medium notes
        } else {
            return totalAmount / denomination; // No limit for large notes
        }
    }
}
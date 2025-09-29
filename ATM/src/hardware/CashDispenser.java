package hardware;

import interfaces.CashDispensingStrategy;
import java.util.HashMap;
import java.util.Map;

public class CashDispenser {
    private Map<Integer, Integer> noteInventory;
    private CashDispensingStrategy dispensingStrategy;
    
    public CashDispenser(Map<Integer, Integer> initialInventory) {
        this.noteInventory = new HashMap<>(initialInventory);
        System.out.println("Cash dispenser initialized with inventory: " + noteInventory);
    }
    
    /**
     * Set the dispensing strategy
     */
    public void setStrategy(CashDispensingStrategy strategy) {
        this.dispensingStrategy = strategy;
        System.out.println("Cash dispensing strategy updated");
    }
    
    /**
     * Dispense cash using the current strategy
     */
    public void dispenseCash(int amount) {
        if (dispensingStrategy == null) {
            throw new IllegalStateException("No dispensing strategy set");
        }
        
        if (!hasSufficientCash(amount)) {
            throw new IllegalStateException("Insufficient cash available");
        }
        
        Map<Integer, Integer> notesToDispense = dispensingStrategy.dispense(amount, noteInventory);
        
        if (notesToDispense == null || notesToDispense.isEmpty()) {
            throw new IllegalStateException("Cannot dispense the requested amount with available notes");
        }
        
        // Update inventory by removing dispensed notes
        for (Map.Entry<Integer, Integer> entry : notesToDispense.entrySet()) {
            int denomination = entry.getKey();
            int count = entry.getValue();
            
            if (noteInventory.get(denomination) < count) {
                throw new IllegalStateException("Insufficient notes of denomination " + denomination);
            }
            
            noteInventory.put(denomination, noteInventory.get(denomination) - count);
        }
        
        System.out.println("Dispensing cash:");
        for (Map.Entry<Integer, Integer> entry : notesToDispense.entrySet()) {
            System.out.println("  $" + entry.getKey() + " x " + entry.getValue() + " notes");
        }
        
        System.out.println("Total amount dispensed: $" + amount);
        System.out.println("Remaining inventory: " + noteInventory);
    }
    
    /**
     * Check if sufficient cash is available
     */
    public boolean hasSufficientCash(int amount) {
        int totalAvailable = 0;
        for (Map.Entry<Integer, Integer> entry : noteInventory.entrySet()) {
            totalAvailable += entry.getKey() * entry.getValue();
        }
        return totalAvailable >= amount;
    }
    
    /**
     * Get current inventory
     */
    public Map<Integer, Integer> getNoteInventory() {
        return new HashMap<>(noteInventory);
    }
    
    /**
     * Add notes to inventory (for refilling)
     */
    public void addNotes(int denomination, int count) {
        noteInventory.put(denomination, noteInventory.getOrDefault(denomination, 0) + count);
        System.out.println("Added " + count + " notes of $" + denomination + " denomination");
    }
    
    /**
     * Get total cash available
     */
    public int getTotalCashAvailable() {
        int total = 0;
        for (Map.Entry<Integer, Integer> entry : noteInventory.entrySet()) {
            total += entry.getKey() * entry.getValue();
        }
        return total;
    }
}
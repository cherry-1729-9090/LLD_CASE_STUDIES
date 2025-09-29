package hardware;

public class DepositSlot {
    private boolean slotOpen;
    private double depositAmount;
    private long depositStartTime;
    private static final long DEPOSIT_TIMEOUT = 30000; // 30 seconds timeout
    
    public DepositSlot() {
        this.slotOpen = false;
        this.depositAmount = 0.0;
        System.out.println("Deposit slot initialized");
    }
    
    /**
     * Accept cash deposit
     */
    public void acceptCash(double amount) {
        if (!slotOpen) {
            throw new IllegalStateException("Deposit slot is not open");
        }
        
        if (amount <= 0) {
            throw new IllegalArgumentException("Deposit amount must be positive");
        }
        
        this.depositAmount = amount;
        System.out.println("Cash deposit accepted: $" + String.format("%.2f", amount));
    }
    
    /**
     * Open the deposit slot
     */
    public void openSlot() {
        if (slotOpen) {
            throw new IllegalStateException("Deposit slot is already open");
        }
        
        this.slotOpen = true;
        this.depositStartTime = System.currentTimeMillis();
        System.out.println("Deposit slot opened. Please insert cash within 30 seconds.");
    }
    
    /**
     * Close the deposit slot
     */
    public void closeSlot() {
        if (!slotOpen) {
            throw new IllegalStateException("Deposit slot is already closed");
        }
        
        this.slotOpen = false;
        System.out.println("Deposit slot closed");
    }
    
    /**
     * Check if deposit has timed out
     */
    public boolean isDepositTimedOut() {
        if (!slotOpen) {
            return false;
        }
        
        long currentTime = System.currentTimeMillis();
        return (currentTime - depositStartTime) > DEPOSIT_TIMEOUT;
    }
    
    /**
     * Get the deposited amount
     */
    public double getDepositAmount() {
        return depositAmount;
    }
    
    /**
     * Reset the deposit amount
     */
    public void resetDeposit() {
        this.depositAmount = 0.0;
    }
    
    /**
     * Check if slot is open
     */
    public boolean isSlotOpen() {
        return slotOpen;
    }
}
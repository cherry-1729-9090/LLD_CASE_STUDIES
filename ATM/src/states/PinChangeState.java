package states;

import interfaces.ATMState;
import models.*;
import core.ATM;
import java.util.Map;

/**
 * State for handling PIN change transactions.
 */
public class PinChangeState implements ATMState {
    private CardDetails cardDetails;
    
    public PinChangeState(CardDetails cardDetails, Account userAccount) {
        this.cardDetails = cardDetails;
        // userAccount not needed for PIN change operation
    }
    
    @Override
    public void performTransaction(ATM atm, Map<String, Object> transactionDetails) {
        System.out.println("Performing PIN change");
        
        // Get old and new PINs from transaction details
        // In real implementation, these would come from user input
        String oldPin = transactionDetails.containsKey("oldPin") ? 
                       (String) transactionDetails.get("oldPin") : "1234"; // Default for demo
        String newPin = transactionDetails.containsKey("newPin") ? 
                       (String) transactionDetails.get("newPin") : "5678"; // Default for demo
        
        try {
            // Change PIN through bank service
            boolean success = atm.getBankService().changePin(cardDetails, oldPin, newPin);
            
            if (success) {
                atm.getScreen().displayMessage("PIN changed successfully");
                System.out.println("PIN change completed successfully");
            } else {
                atm.getScreen().displayTransactionFailed("PIN change failed. Please verify your current PIN.");
                System.out.println("PIN change failed");
            }
            
        } catch (Exception e) {
            atm.getScreen().displayTransactionFailed("Unable to change PIN at this time");
            System.out.println("PIN change failed: " + e.getMessage());
        }
        
        // Return to transaction selection after a brief pause
        try {
            Thread.sleep(2000); // 2 second pause
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
        
        returnToTransactionSelection(atm);
    }
    
    @Override
    public void cancelTransaction(ATM atm) {
        System.out.println("PIN change cancelled");
        returnToTransactionSelection(atm);
    }
    
    private void returnToTransactionSelection(ATM atm) {
        atm.setState(new TransactionSelectionState(cardDetails));
        java.util.List<String> options = java.util.Arrays.asList(
            "Cash Withdrawal", "Balance Inquiry", "PIN Change", "Mini Statement", "Cash Deposit"
        );
        atm.getScreen().displayOptions(options);
    }
}
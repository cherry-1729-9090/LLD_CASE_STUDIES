package states;

import interfaces.ATMState;
import models.*;
import core.ATM;
import java.util.Map;

/**
 * State for handling balance inquiry transactions.
 */
public class BalanceInquiryState implements ATMState {
    private CardDetails cardDetails;
    private Account userAccount;
    
    public BalanceInquiryState(CardDetails cardDetails, Account userAccount) {
        this.cardDetails = cardDetails;
        this.userAccount = userAccount;
    }
    
    @Override
    public void performTransaction(ATM atm, Map<String, Object> transactionDetails) {
        System.out.println("Performing balance inquiry");
        
        try {
            // Get balance from bank service
            double balance = atm.getBankService().getAccountBalance(userAccount);
            
            // Display balance
            atm.getScreen().displayBalance(balance);
            
            System.out.println("Balance inquiry completed successfully");
            
        } catch (Exception e) {
            atm.getScreen().displayTransactionFailed("Unable to retrieve balance");
            System.out.println("Balance inquiry failed: " + e.getMessage());
        }
        
        // Return to transaction selection after a brief pause (simulated)
        try {
            Thread.sleep(3000); // 3 second pause to let user read balance
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
        
        returnToTransactionSelection(atm);
    }
    
    @Override
    public void cancelTransaction(ATM atm) {
        System.out.println("Balance inquiry cancelled");
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
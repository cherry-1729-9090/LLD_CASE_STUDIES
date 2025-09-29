package states;

import interfaces.ATMState;
import models.*;
import core.ATM;
import java.util.Map;
import java.util.List;

/**
 * State for handling mini statement transactions.
 */
public class MiniStatementState implements ATMState {
    private CardDetails cardDetails;
    private Account userAccount;
    
    public MiniStatementState(CardDetails cardDetails, Account userAccount) {
        this.cardDetails = cardDetails;
        this.userAccount = userAccount;
    }
    
    @Override
    public void performTransaction(ATM atm, Map<String, Object> transactionDetails) {
        System.out.println("Performing mini statement");
        
        try {
            // Get mini statement from bank service
            List<Transaction> recentTransactions = atm.getBankService().getMiniStatement(userAccount);
            
            // Print mini statement
            atm.getPrinter().printMiniStatement(recentTransactions, userAccount.getAccountNumber());
            
            atm.getScreen().displayMessage("Mini statement printed successfully");
            System.out.println("Mini statement completed successfully");
            
        } catch (Exception e) {
            atm.getScreen().displayTransactionFailed("Unable to retrieve mini statement");
            System.out.println("Mini statement failed: " + e.getMessage());
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
        System.out.println("Mini statement cancelled");
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
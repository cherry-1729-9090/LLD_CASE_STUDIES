package states;

import interfaces.ATMState;
import models.*;
import core.ATM;
import enums.TransactionStatus;
import enums.TransactionType;
import java.util.Map;
import java.util.Date;

/**
 * State for handling cash withdrawal transactions.
 */
public class CashWithdrawalState implements ATMState {
    private CardDetails cardDetails;
    private Account userAccount;
    
    public CashWithdrawalState(CardDetails cardDetails, Account userAccount) {
        this.cardDetails = cardDetails;
        this.userAccount = userAccount;
    }
    
    @Override
    public void performTransaction(ATM atm, Map<String, Object> transactionDetails) {
        System.out.println("Performing cash withdrawal");
        
        // Get amount from transaction details (in real implementation, this would come from user input)
        double amount = transactionDetails.containsKey("amount") ? 
                       (Double) transactionDetails.get("amount") : 100.0; // Default for demo
        
        // Check if ATM has sufficient cash
        if (!atm.getCashDispenser().hasSufficientCash((int) amount)) {
            atm.getScreen().displayTransactionFailed("Insufficient cash in ATM");
            returnToTransactionSelection(atm);
            return;
        }
        
        // Create transaction
        Transaction transaction = new Transaction(
            generateTransactionId(),
            userAccount,
            TransactionType.CASH_WITHDRAWAL,
            amount,
            TransactionStatus.PENDING,
            new Date()
        );
        
        // Execute transaction through bank service
        TransactionStatus status = atm.getBankService().executeTransaction(transaction);
        transaction.setStatus(status);
        
        if (status == TransactionStatus.SUCCESS) {
            // Dispense cash
            atm.getCashDispenser().dispenseCash((int) amount);
            atm.getScreen().displayTransactionSuccess();
            
            // Print receipt
            atm.getPrinter().printReceipt(transaction);
            
        } else {
            atm.getScreen().displayTransactionFailed("Transaction declined by bank");
        }
        
        returnToTransactionSelection(atm);
    }
    
    @Override
    public void cancelTransaction(ATM atm) {
        System.out.println("Cash withdrawal cancelled");
        returnToTransactionSelection(atm);
    }
    
    private void returnToTransactionSelection(ATM atm) {
        atm.setState(new TransactionSelectionState(cardDetails));
        java.util.List<String> options = java.util.Arrays.asList(
            "Cash Withdrawal", "Balance Inquiry", "PIN Change", "Mini Statement", "Cash Deposit"
        );
        atm.getScreen().displayOptions(options);
    }
    
    private String generateTransactionId() {
        return "TXN" + System.currentTimeMillis();
    }
}
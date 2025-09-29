package states;

import interfaces.ATMState;
import models.*;
import core.ATM;
import enums.TransactionStatus;
import enums.TransactionType;
import java.util.Map;
import java.util.Date;

/**
 * State for handling cash deposit transactions.
 */
public class CashDepositState implements ATMState {
    private CardDetails cardDetails;
    private Account userAccount;
    
    public CashDepositState(CardDetails cardDetails, Account userAccount) {
        this.cardDetails = cardDetails;
        this.userAccount = userAccount;
    }
    
    @Override
    public void performTransaction(ATM atm, Map<String, Object> transactionDetails) {
        System.out.println("Performing cash deposit");
        
        try {
            // Check if deposit has timed out
            if (atm.getDepositSlot().isDepositTimedOut()) {
                atm.getScreen().displayTransactionFailed("Deposit timed out");
                atm.getDepositSlot().closeSlot();
                returnToTransactionSelection(atm);
                return;
            }
            
            // Get deposit amount from slot
            double depositAmount = atm.getDepositSlot().getDepositAmount();
            
            if (depositAmount <= 0) {
                atm.getScreen().displayTransactionFailed("No cash detected");
                atm.getDepositSlot().closeSlot();
                returnToTransactionSelection(atm);
                return;
            }
            
            // Create transaction
            Transaction transaction = new Transaction(
                generateTransactionId(),
                userAccount,
                TransactionType.CASH_DEPOSIT,
                depositAmount,
                TransactionStatus.PENDING,
                new Date()
            );
            
            // Execute transaction through bank service
            TransactionStatus status = atm.getBankService().executeTransaction(transaction);
            transaction.setStatus(status);
            
            if (status == TransactionStatus.SUCCESS) {
                atm.getScreen().displayMessage("Cash deposited successfully: $" + 
                                             String.format("%.2f", depositAmount));
                
                // Print receipt
                atm.getPrinter().printReceipt(transaction);
                
                System.out.println("Cash deposit completed successfully");
            } else {
                atm.getScreen().displayTransactionFailed("Deposit transaction failed");
                System.out.println("Cash deposit failed");
            }
            
            // Close deposit slot and reset
            atm.getDepositSlot().closeSlot();
            atm.getDepositSlot().resetDeposit();
            
        } catch (Exception e) {
            atm.getScreen().displayTransactionFailed("Deposit processing error");
            System.out.println("Cash deposit failed: " + e.getMessage());
            
            // Ensure slot is closed on error
            if (atm.getDepositSlot().isSlotOpen()) {
                atm.getDepositSlot().closeSlot();
            }
        }
        
        returnToTransactionSelection(atm);
    }
    
    @Override
    public void cancelTransaction(ATM atm) {
        System.out.println("Cash deposit cancelled");
        
        // Close deposit slot if open
        if (atm.getDepositSlot().isSlotOpen()) {
            atm.getDepositSlot().closeSlot();
        }
        atm.getDepositSlot().resetDeposit();
        
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
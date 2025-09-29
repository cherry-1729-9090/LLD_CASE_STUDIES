package interfaces;

import enums.TransactionStatus;
import models.Account;
import models.CardDetails;
import models.Transaction;
import java.util.List;

public interface BankService {
    
    /**
     * Authenticate the card and PIN
     */
    boolean authenticate(CardDetails cardDetails, String pin);
    
    /**
     * Get the current account balance
     */
    double getAccountBalance(Account account);
    
    /**
     * Execute a transaction
     */
    TransactionStatus executeTransaction(Transaction transaction);
    
    /**
     * Get mini statement for the account
     */
    List<Transaction> getMiniStatement(Account account);
    
    /**
     * Change PIN for the card
     */
    boolean changePin(CardDetails cardDetails, String oldPin, String newPin);
}
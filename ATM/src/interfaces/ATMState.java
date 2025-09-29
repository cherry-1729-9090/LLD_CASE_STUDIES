package interfaces;

import enums.TransactionType;
import models.CardDetails;
import core.ATM;
import java.util.Map;

// State pattern interface
public interface ATMState {
    
    /**
     * Handle card insertion
     */
    default void insertCard(ATM atm, CardDetails cardDetails) {
        throw new UnsupportedOperationException("Insert card operation not supported in current state");
    }
    
    /**
     * Handle PIN authentication
     */
    default void authenticatePIN(ATM atm, String pin) {
        throw new UnsupportedOperationException("PIN authentication not supported in current state");
    }
    
    /**
     * Handle operation selection
     */
    default void selectOperation(ATM atm, TransactionType type) {
        throw new UnsupportedOperationException("Operation selection not supported in current state");
    }
    
    /**
     * Perform the selected transaction
     */
    default void performTransaction(ATM atm, Map<String, Object> transactionDetails) {
        throw new UnsupportedOperationException("Transaction performance not supported in current state");
    }
    
    /**
     * Cancel current transaction
     */
    default void cancelTransaction(ATM atm) {
        throw new UnsupportedOperationException("Transaction cancellation not supported in current state");
    }
    
    /**
     * Eject the card
     */
    default void ejectCard(ATM atm) {
        throw new UnsupportedOperationException("Card ejection not supported in current state");
    }
}
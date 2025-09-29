package states;

import interfaces.ATMState;
import models.CardDetails;
import models.Account;
import core.ATM;
import enums.TransactionType;

public class TransactionSelectionState implements ATMState {
    private CardDetails cardDetails;
    private Account userAccount;
    
    public TransactionSelectionState(CardDetails cardDetails) {
        this.cardDetails = cardDetails;
        // In a real implementation, we would get the account from the bank service
        // For now, create a mock account
        this.userAccount = new Account("123456789", cardDetails.getBank());
    }
    
    @Override
    public void selectOperation(ATM atm, TransactionType type) {
        System.out.println("Operation selected: " + type);
        
        switch (type) {
            case CASH_WITHDRAWAL:
                atm.setState(new CashWithdrawalState(cardDetails, userAccount));
                atm.getScreen().displayMessage("Enter withdrawal amount:");
                break;
                
            case BALANCE_INQUIRY:
                atm.setState(new BalanceInquiryState(cardDetails, userAccount));
                // Immediately perform balance inquiry
                java.util.Map<String, Object> balanceDetails = new java.util.HashMap<>();
                atm.performTransaction(balanceDetails);
                break;
                
            case PIN_CHANGE:
                atm.setState(new PinChangeState(cardDetails, userAccount));
                atm.getScreen().displayMessage("Enter your current PIN:");
                break;
                
            case MINI_STATEMENT:
                atm.setState(new MiniStatementState(cardDetails, userAccount));
                // Immediately perform mini statement
                java.util.Map<String, Object> statementDetails = new java.util.HashMap<>();
                atm.performTransaction(statementDetails);
                break;
                
            case CASH_DEPOSIT:
                atm.setState(new CashDepositState(cardDetails, userAccount));
                atm.getScreen().displayMessage("Please insert cash into the deposit slot");
                atm.getDepositSlot().openSlot();
                break;
                
            default:
                atm.getScreen().displayMessage("Invalid operation selected");
                break;
        }
    }
    
    @Override
    public void cancelTransaction(ATM atm) {
        System.out.println("Transaction cancelled in TransactionSelection state");
        atm.getCardReader().ejectCard();
        atm.setState(new IdleState());
        atm.getScreen().displayWelcome();
    }
    
    @Override
    public void ejectCard(ATM atm) {
        cancelTransaction(atm);
    }
}
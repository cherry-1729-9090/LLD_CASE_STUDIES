package states;

import interfaces.ATMState;
import models.CardDetails;
import core.ATM;

public class HasCardState implements ATMState {
    private CardDetails cardDetails;
    private int pinAttempts;
    private static final int MAX_PIN_ATTEMPTS = 3;
    
    public HasCardState(CardDetails cardDetails) {
        this.cardDetails = cardDetails;
        this.pinAttempts = 0;
    }
    
    @Override
    public void authenticatePIN(ATM atm, String pin) {
        System.out.println("Authenticating PIN in HasCard state");
        
        pinAttempts++;
        
        // Use bank service to authenticate
        boolean isAuthenticated = atm.getBankService().authenticate(cardDetails, pin);
        
        if (isAuthenticated) {
            System.out.println("PIN authentication successful");
            atm.getScreen().displayMessage("Authentication successful");
            atm.setState(new TransactionSelectionState(cardDetails));
            
            // Display transaction options
            java.util.List<String> options = java.util.Arrays.asList(
                "Cash Withdrawal",
                "Balance Inquiry", 
                "PIN Change",
                "Mini Statement",
                "Cash Deposit"
            );
            atm.getScreen().displayOptions(options);
            
        } else {
            System.out.println("PIN authentication failed. Attempt " + pinAttempts + "/" + MAX_PIN_ATTEMPTS);
            
            if (pinAttempts >= MAX_PIN_ATTEMPTS) {
                atm.getScreen().displayMessage("Maximum PIN attempts exceeded. Card will be ejected.");
                atm.getCardReader().ejectCard();
                atm.setState(new IdleState());
            } else {
                atm.getScreen().displayMessage("Invalid PIN. Please try again. Attempts remaining: " + 
                                             (MAX_PIN_ATTEMPTS - pinAttempts));
                atm.getScreen().displayPinPrompt();
            }
        }
    }
    
    @Override
    public void ejectCard(ATM atm) {
        System.out.println("Ejecting card from HasCard state");
        atm.getCardReader().ejectCard();
        atm.setState(new IdleState());
        atm.getScreen().displayWelcome();
    }
    
    @Override
    public void cancelTransaction(ATM atm) {
        System.out.println("Transaction cancelled in HasCard state");
        ejectCard(atm);
    }
}
package states;

import interfaces.ATMState;
import models.CardDetails;
import core.ATM;

public class IdleState implements ATMState {
    
    @Override
    public void insertCard(ATM atm, CardDetails cardDetails) {
        System.out.println("Card inserted in Idle state: " + cardDetails.getCardNumber());
        
        // Validate card (basic validation)
        if (cardDetails == null || cardDetails.getCardNumber() == null || cardDetails.getCardNumber().isEmpty()) {
            System.out.println("Invalid card detected");
            atm.getScreen().displayMessage("Invalid card. Please try again.");
            return;
        }
        
        // Check card expiry
        if (cardDetails.getExpiryDate().before(new java.util.Date())) {
            System.out.println("Card has expired");
            atm.getScreen().displayMessage("Card has expired. Please contact your bank.");
            atm.getCardReader().ejectCard();
            return;
        }
        
        System.out.println("Card accepted. Transitioning to HasCardState");
        atm.setState(new HasCardState(cardDetails));
        atm.getScreen().displayPinPrompt();
    }
    
    @Override
    public void ejectCard(ATM atm) {
        System.out.println("No card to eject in Idle state");
        atm.getScreen().displayMessage("Please insert your card first");
    }
}
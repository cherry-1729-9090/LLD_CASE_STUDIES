package hardware;

import java.util.List;

public class Screen {
    
    public Screen() {
        System.out.println("Screen initialized");
    }
    
    /**
     * Display a message to the user
     */
    public void displayMessage(String message) {
        System.out.println("SCREEN: " + message);
    }
    
    /**
     * Display options to the user
     */
    public void displayOptions(List<String> options) {
        System.out.println("SCREEN: Select an option:");
        for (int i = 0; i < options.size(); i++) {
            System.out.println("SCREEN: " + (i + 1) + ". " + options.get(i));
        }
    }
    
    /**
     * Display welcome message
     */
    public void displayWelcome() {
        displayMessage("Welcome to the ATM");
        displayMessage("Please insert your card");
    }
    
    /**
     * Display PIN prompt
     */
    public void displayPinPrompt() {
        displayMessage("Please enter your PIN");
    }
    
    /**
     * Display balance
     */
    public void displayBalance(double balance) {
        displayMessage("Your current balance is: $" + String.format("%.2f", balance));
    }
    
    /**
     * Display transaction successful message
     */
    public void displayTransactionSuccess() {
        displayMessage("Transaction completed successfully");
    }
    
    /**
     * Display transaction failed message
     */
    public void displayTransactionFailed(String reason) {
        displayMessage("Transaction failed: " + reason);
    }
    
    /**
     * Clear the screen
     */
    public void clearScreen() {
        System.out.println("SCREEN: [CLEARED]");
    }
}
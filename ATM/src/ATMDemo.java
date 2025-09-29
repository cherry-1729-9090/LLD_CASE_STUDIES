import core.ATM;
import models.*;
import services.BankServiceProxy;
import strategies.*;
import enums.TransactionType;
import java.util.*;

// Demo class showing ATM functionality
public class ATMDemo {
    
    public static void main(String[] args) {
        System.out.println("=== ATM System Demo ===\n");
        
        // Initialize ATM with cash inventory
        Map<Integer, Integer> initialCash = new HashMap<>();
        initialCash.put(2000, 10);  // 10 notes of $2000
        initialCash.put(500, 20);   // 20 notes of $500
        initialCash.put(100, 50);   // 50 notes of $100
        initialCash.put(50, 30);    // 30 notes of $50
        initialCash.put(20, 40);    // 40 notes of $20
        
        ATM atm = new ATM("ATM001", "Main Street Branch", initialCash);
        
        // Set up bank service (Proxy pattern)
        BankServiceProxy bankService = new BankServiceProxy("HDFC Bank");
        atm.setBankService(bankService);
        
        System.out.println("\n=== Demo Scenario 1: Successful Cash Withdrawal ===");
        simulateSuccessfulTransaction(atm);
        
        System.out.println("\n=== Demo Scenario 2: Balance Inquiry ===");
        simulateBalanceInquiry(atm);
        
        System.out.println("\n=== Demo Scenario 3: Strategy Pattern Demo ===");
        demonstrateStrategyPattern(atm);
        
        System.out.println("\n=== Demo Scenario 4: PIN Change ===");
        simulatePinChange(atm);
        
        System.out.println("\n=== Demo Scenario 5: Mini Statement ===");
        simulateMiniStatement(atm);
        
        System.out.println("\n=== ATM System Demo Complete ===");
    }
    
    private static void simulateSuccessfulTransaction(ATM atm) {
        // Create sample card and bank
        Bank bank = new Bank("HDFC001", "HDFC Bank");
        Date expiryDate = new Date(System.currentTimeMillis() + 365L * 24 * 60 * 60 * 1000); // 1 year from now
        CardDetails card = new CardDetails("1234567890123456", expiryDate, bank);
        
        // Simulate card insertion
        atm.getCardReader().insertCard(card);
        
        // Insert card (State pattern: Idle -> HasCard)
        atm.insertCard(card);
        
        // Authenticate PIN (State pattern: HasCard -> TransactionSelection)
        atm.authenticatePIN("1234");
        
        // Select withdrawal operation (State pattern: TransactionSelection -> CashWithdrawal)
        atm.selectOperation(TransactionType.CASH_WITHDRAWAL);
        
        // Perform withdrawal
        Map<String, Object> transactionDetails = new HashMap<>();
        transactionDetails.put("amount", 500.0);
        atm.performTransaction(transactionDetails);
        
        // The system should automatically return to selection after transaction
        // Cancel to complete the session and eject card
        atm.cancel();
    }
    
    private static void simulateBalanceInquiry(ATM atm) {
        // Create sample card and bank
        Bank bank = new Bank("HDFC001", "HDFC Bank");
        Date expiryDate = new Date(System.currentTimeMillis() + 365L * 24 * 60 * 60 * 1000);
        CardDetails card = new CardDetails("1234567890123456", expiryDate, bank);
        
        atm.getCardReader().insertCard(card);
        atm.insertCard(card);
        atm.authenticatePIN("1234");
        atm.selectOperation(TransactionType.BALANCE_INQUIRY);
        atm.cancel();
    }
    
    private static void demonstrateStrategyPattern(ATM atm) {
        System.out.println("Demonstrating different cash dispensing strategies:");
        
        // Test MinimalNotesStrategy
        System.out.println("\n--- Using MinimalNotesStrategy ---");
        atm.getCashDispenser().setStrategy(new MinimalNotesStrategy());
        
        Map<Integer, Integer> availableNotes = atm.getCashDispenser().getNoteInventory();
        System.out.println("Available notes: " + availableNotes);
        
        // Simulate dispensing $1200 
        int amount = 1200;
        System.out.println("Attempting to dispense $" + amount);
        atm.getCashDispenser().dispenseCash(amount);
        
        // Test PrioritizeSmallNotesStrategy
        System.out.println("\n--- Using PrioritizeSmallNotesStrategy ---");
        atm.getCashDispenser().setStrategy(new PrioritizeSmallNotesStrategy());
        
        // Add some cash back to demonstrate different strategy
        atm.getCashDispenser().addNotes(20, 20);
        atm.getCashDispenser().addNotes(50, 15);
        atm.getCashDispenser().addNotes(100, 10);
        
        System.out.println("Attempting to dispense $" + amount + " with small notes strategy");
        atm.getCashDispenser().dispenseCash(amount);
        
        // Reset to default strategy
        atm.getCashDispenser().setStrategy(new MinimalNotesStrategy());
    }
    
    private static void simulatePinChange(ATM atm) {
        Bank bank = new Bank("HDFC001", "HDFC Bank");
        Date expiryDate = new Date(System.currentTimeMillis() + 365L * 24 * 60 * 60 * 1000);
        CardDetails card = new CardDetails("1234567890123456", expiryDate, bank);
        
        atm.getCardReader().insertCard(card);
        atm.insertCard(card);
        atm.authenticatePIN("1234");
        atm.selectOperation(TransactionType.PIN_CHANGE);
        
        Map<String, Object> pinChangeDetails = new HashMap<>();
        pinChangeDetails.put("oldPin", "1234");
        pinChangeDetails.put("newPin", "5678");
        atm.performTransaction(pinChangeDetails);
        
        atm.cancel();
    }
    
    private static void simulateMiniStatement(ATM atm) {
        Bank bank = new Bank("HDFC001", "HDFC Bank");
        Date expiryDate = new Date(System.currentTimeMillis() + 365L * 24 * 60 * 60 * 1000);
        CardDetails card = new CardDetails("1234567890123456", expiryDate, bank);
        
        atm.getCardReader().insertCard(card);
        atm.insertCard(card);
        atm.authenticatePIN("1234");
        atm.selectOperation(TransactionType.MINI_STATEMENT);
        atm.cancel();
    }
}
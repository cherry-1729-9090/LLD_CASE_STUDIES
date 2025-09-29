package services;

import interfaces.BankService;
import models.*;
import enums.TransactionStatus;
import java.util.List;
import java.util.ArrayList;
import java.util.Date;
import java.util.Random;

// Mock implementation of bank service
public class RealBankService implements BankService {
    private Random random;
    private String bankName;
    
    public RealBankService(String bankName) {
        this.bankName = bankName;
        this.random = new Random();
        System.out.println("RealBankService initialized for " + bankName);
    }
    
    @Override
    public boolean authenticate(CardDetails cardDetails, String pin) {
        System.out.println("RealBankService: Authenticating card " + cardDetails.getCardNumber() + 
                          " with " + bankName);
        
        // Simulate network delay
        simulateNetworkDelay(1000, 2000);
        
        // Mock authentication logic - accept PIN "1234" for demo
        boolean isAuthenticated = "1234".equals(pin);
        
        System.out.println("RealBankService: Authentication " + 
                          (isAuthenticated ? "successful" : "failed"));
        
        return isAuthenticated;
    }
    
    @Override
    public double getAccountBalance(Account account) {
        System.out.println("RealBankService: Getting balance for account " + 
                          account.getAccountNumber());
        
        // Simulate network delay
        simulateNetworkDelay(500, 1500);
        
        // Mock balance - generate random balance between $100 and $10000
        double balance = 100 + (random.nextDouble() * 9900);
        
        System.out.println("RealBankService: Balance retrieved: $" + 
                          String.format("%.2f", balance));
        
        return balance;
    }
    
    @Override
    public TransactionStatus executeTransaction(Transaction transaction) {
        System.out.println("RealBankService: Executing transaction " + 
                          transaction.getTransactionId() + 
                          " of type " + transaction.getType() + 
                          " for amount $" + transaction.getAmount());
        
        // Simulate network delay
        simulateNetworkDelay(1500, 3000);
        
        // Mock transaction logic - randomly succeed 90% of the time
        TransactionStatus status = (random.nextDouble() < 0.9) ? 
                                  TransactionStatus.SUCCESS : 
                                  TransactionStatus.FAILURE;
        
        System.out.println("RealBankService: Transaction " + 
                          transaction.getTransactionId() + " " + status);
        
        return status;
    }
    
    @Override
    public List<Transaction> getMiniStatement(Account account) {
        System.out.println("RealBankService: Getting mini statement for account " + 
                          account.getAccountNumber());
        
        // Simulate network delay
        simulateNetworkDelay(1000, 2000);
        
        // Mock mini statement - generate some sample transactions
        List<Transaction> transactions = new ArrayList<>();
        
        for (int i = 0; i < 5; i++) {
            Transaction transaction = new Transaction(
                "TXN" + (System.currentTimeMillis() - i * 86400000), // Different days
                account,
                getRandomTransactionType(),
                50 + (random.nextDouble() * 500), // Random amount between $50-$550
                TransactionStatus.SUCCESS,
                new Date(System.currentTimeMillis() - i * 86400000) // Past dates
            );
            transactions.add(transaction);
        }
        
        System.out.println("RealBankService: Mini statement retrieved with " + 
                          transactions.size() + " transactions");
        
        return transactions;
    }
    
    @Override
    public boolean changePin(CardDetails cardDetails, String oldPin, String newPin) {
        System.out.println("RealBankService: Changing PIN for card " + 
                          cardDetails.getCardNumber());
        
        // Simulate network delay
        simulateNetworkDelay(1500, 2500);
        
        // Mock PIN change logic - succeed if old PIN is "1234"
        boolean success = "1234".equals(oldPin) && newPin != null && newPin.length() == 4;
        
        System.out.println("RealBankService: PIN change " + 
                          (success ? "successful" : "failed"));
        
        return success;
    }
    
    private void simulateNetworkDelay(int minMs, int maxMs) {
        try {
            int delay = minMs + random.nextInt(maxMs - minMs);
            Thread.sleep(delay);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }
    
    private enums.TransactionType getRandomTransactionType() {
        enums.TransactionType[] types = enums.TransactionType.values();
        return types[random.nextInt(types.length)];
    }
}
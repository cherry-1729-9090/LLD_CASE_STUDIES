package services;

import interfaces.BankService;
import models.*;
import enums.TransactionStatus;
import java.util.List;
import java.text.SimpleDateFormat;
import java.util.Date;

// Proxy with logging, caching, and security
public class BankServiceProxy implements BankService {
    private RealBankService realBankService;
    private SimpleDateFormat dateFormat;
    
    // Cache for balance (simple caching implementation)
    private double cachedBalance = -1;
    private long lastBalanceCheck = 0;
    private static final long CACHE_VALIDITY_MS = 60000; // 1 minute cache
    
    public BankServiceProxy(String bankName) {
        this.realBankService = new RealBankService(bankName);
        this.dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        System.out.println("BankServiceProxy initialized");
    }
    
    @Override
    public boolean authenticate(CardDetails cardDetails, String pin) {
        // Logging
        logRequest("AUTHENTICATE", "Card: " + maskCardNumber(cardDetails.getCardNumber()));
        
        // Security: validate inputs
        if (cardDetails == null || pin == null || pin.length() != 4) {
            logResponse("AUTHENTICATE", "FAILED - Invalid input");
            return false;
        }
        
        long startTime = System.currentTimeMillis();
        
        // Delegate to real service
        boolean result = realBankService.authenticate(cardDetails, pin);
        
        long duration = System.currentTimeMillis() - startTime;
        logResponse("AUTHENTICATE", result ? "SUCCESS" : "FAILED", duration);
        
        return result;
    }
    
    @Override
    public double getAccountBalance(Account account) {
        // Logging
        logRequest("BALANCE_INQUIRY", "Account: " + maskAccountNumber(account.getAccountNumber()));
        
        // Check cache first
        long currentTime = System.currentTimeMillis();
        if (cachedBalance >= 0 && (currentTime - lastBalanceCheck) < CACHE_VALIDITY_MS) {
            System.out.println("BankServiceProxy: Returning cached balance");
            logResponse("BALANCE_INQUIRY", "SUCCESS (CACHED)", 0);
            return cachedBalance;
        }
        
        long startTime = System.currentTimeMillis();
        
        // Delegate to real service
        double balance = realBankService.getAccountBalance(account);
        
        // Update cache
        cachedBalance = balance;
        lastBalanceCheck = currentTime;
        
        long duration = System.currentTimeMillis() - startTime;
        logResponse("BALANCE_INQUIRY", "SUCCESS", duration);
        
        return balance;
    }
    
    @Override
    public TransactionStatus executeTransaction(Transaction transaction) {
        // Logging
        logRequest("TRANSACTION", "Type: " + transaction.getType() + 
                  ", Amount: $" + String.format("%.2f", transaction.getAmount()) +
                  ", Account: " + maskAccountNumber(transaction.getSourceAccount().getAccountNumber()));
        
        // Clear balance cache since balance will change
        cachedBalance = -1;
        
        long startTime = System.currentTimeMillis();
        
        // Delegate to real service
        TransactionStatus status = realBankService.executeTransaction(transaction);
        
        long duration = System.currentTimeMillis() - startTime;
        logResponse("TRANSACTION", status.toString(), duration);
        
        return status;
    }
    
    @Override
    public List<Transaction> getMiniStatement(Account account) {
        // Logging
        logRequest("MINI_STATEMENT", "Account: " + maskAccountNumber(account.getAccountNumber()));
        
        long startTime = System.currentTimeMillis();
        
        // Delegate to real service
        List<Transaction> transactions = realBankService.getMiniStatement(account);
        
        long duration = System.currentTimeMillis() - startTime;
        logResponse("MINI_STATEMENT", "SUCCESS - " + transactions.size() + " transactions", duration);
        
        return transactions;
    }
    
    @Override
    public boolean changePin(CardDetails cardDetails, String oldPin, String newPin) {
        // Logging
        logRequest("PIN_CHANGE", "Card: " + maskCardNumber(cardDetails.getCardNumber()));
        
        // Security: validate inputs
        if (cardDetails == null || oldPin == null || newPin == null || 
            oldPin.length() != 4 || newPin.length() != 4) {
            logResponse("PIN_CHANGE", "FAILED - Invalid input");
            return false;
        }
        
        long startTime = System.currentTimeMillis();
        
        // Delegate to real service
        boolean result = realBankService.changePin(cardDetails, oldPin, newPin);
        
        long duration = System.currentTimeMillis() - startTime;
        logResponse("PIN_CHANGE", result ? "SUCCESS" : "FAILED", duration);
        
        return result;
    }
    
    private void logRequest(String operation, String details) {
        System.out.println("PROXY LOG [" + dateFormat.format(new Date()) + "] " +
                          "REQUEST - " + operation + ": " + details);
    }
    
    private void logResponse(String operation, String result) {
        logResponse(operation, result, -1);
    }
    
    private void logResponse(String operation, String result, long durationMs) {
        String duration = durationMs >= 0 ? " (" + durationMs + "ms)" : "";
        System.out.println("PROXY LOG [" + dateFormat.format(new Date()) + "] " +
                          "RESPONSE - " + operation + ": " + result + duration);
    }
    
    private String maskCardNumber(String cardNumber) {
        if (cardNumber == null || cardNumber.length() < 8) {
            return "****";
        }
        return cardNumber.substring(0, 4) + "****" + cardNumber.substring(cardNumber.length() - 4);
    }
    
    private String maskAccountNumber(String accountNumber) {
        if (accountNumber == null || accountNumber.length() < 6) {
            return "****";
        }
        return "****" + accountNumber.substring(accountNumber.length() - 4);
    }
}
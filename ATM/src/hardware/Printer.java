package hardware;

import models.Transaction;
import java.text.SimpleDateFormat;

public class Printer {
    private boolean paperAvailable;
    private SimpleDateFormat dateFormat;
    
    public Printer() {
        this.paperAvailable = true;
        this.dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        System.out.println("Printer initialized");
    }
    
    /**
     * Print transaction receipt
     */
    public void printReceipt(Transaction transaction) {
        if (!paperAvailable) {
            throw new IllegalStateException("No paper available for printing");
        }
        
        System.out.println("\n========== RECEIPT ==========");
        System.out.println("Transaction ID: " + transaction.getTransactionId());
        System.out.println("Date: " + dateFormat.format(transaction.getDate()));
        System.out.println("Account: " + transaction.getSourceAccount().getAccountNumber());
        System.out.println("Type: " + transaction.getType());
        System.out.println("Amount: $" + String.format("%.2f", transaction.getAmount()));
        System.out.println("Status: " + transaction.getStatus());
        System.out.println("Bank: " + transaction.getSourceAccount().getBank().getName());
        System.out.println("=============================");
        System.out.println("Thank you for using our ATM!");
        System.out.println("=============================\n");
    }
    
    /**
     * Print mini statement
     */
    public void printMiniStatement(java.util.List<Transaction> transactions, String accountNumber) {
        if (!paperAvailable) {
            throw new IllegalStateException("No paper available for printing");
        }
        
        System.out.println("\n======== MINI STATEMENT ========");
        System.out.println("Account: " + accountNumber);
        System.out.println("Date: " + dateFormat.format(new java.util.Date()));
        System.out.println("=================================");
        
        if (transactions.isEmpty()) {
            System.out.println("No recent transactions found");
        } else {
            for (Transaction transaction : transactions) {
                System.out.println(dateFormat.format(transaction.getDate()) + 
                                 " | " + transaction.getType() + 
                                 " | $" + String.format("%.2f", transaction.getAmount()) +
                                 " | " + transaction.getStatus());
            }
        }
        
        System.out.println("=================================");
        System.out.println("Thank you for using our ATM!");
        System.out.println("=================================\n");
    }
    
    /**
     * Check if paper is available
     */
    public boolean isPaperAvailable() {
        return paperAvailable;
    }
    
    /**
     * Set paper availability (for simulation)
     */
    public void setPaperAvailable(boolean paperAvailable) {
        this.paperAvailable = paperAvailable;
    }
}
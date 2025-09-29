package models;

import enums.TransactionStatus;
import enums.TransactionType;
import java.util.Date;

public class Transaction {
    private String transactionId;
    private Account sourceAccount;
    private TransactionType type;
    private double amount;
    private TransactionStatus status;
    private Date date;

    public Transaction(String transactionId, Account sourceAccount, TransactionType type, 
                      double amount, TransactionStatus status, Date date) {
        this.transactionId = transactionId;
        this.sourceAccount = sourceAccount;
        this.type = type;
        this.amount = amount;
        this.status = status;
        this.date = date;
    }

    // Getters and Setters
    public String getTransactionId() {
        return transactionId;
    }

    public void setTransactionId(String transactionId) {
        this.transactionId = transactionId;
    }

    public Account getSourceAccount() {
        return sourceAccount;
    }

    public void setSourceAccount(Account sourceAccount) {
        this.sourceAccount = sourceAccount;
    }

    public TransactionType getType() {
        return type;
    }

    public void setType(TransactionType type) {
        this.type = type;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public TransactionStatus getStatus() {
        return status;
    }

    public void setStatus(TransactionStatus status) {
        this.status = status;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    @Override
    public String toString() {
        return "Transaction{" +
                "transactionId='" + transactionId + '\'' +
                ", sourceAccount=" + sourceAccount +
                ", type=" + type +
                ", amount=" + amount +
                ", status=" + status +
                ", date=" + date +
                '}';
    }
}
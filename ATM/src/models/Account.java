package models;

public class Account {
    private String accountNumber;
    private Bank bank;

    public Account(String accountNumber, Bank bank) {
        this.accountNumber = accountNumber;
        this.bank = bank;
    }

    // Getters and Setters
    public String getAccountNumber() {
        return accountNumber;
    }

    public void setAccountNumber(String accountNumber) {
        this.accountNumber = accountNumber;
    }

    public Bank getBank() {
        return bank;
    }

    public void setBank(Bank bank) {
        this.bank = bank;
    }

    @Override
    public String toString() {
        return "Account{" +
                "accountNumber='" + accountNumber + '\'' +
                ", bank=" + bank +
                '}';
    }
}
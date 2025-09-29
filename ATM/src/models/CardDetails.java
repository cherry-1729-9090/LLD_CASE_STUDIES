package models;

import java.util.Date;

public class CardDetails {
    private String cardNumber;
    private Date expiryDate;
    private Bank bank;

    public CardDetails(String cardNumber, Date expiryDate, Bank bank) {
        this.cardNumber = cardNumber;
        this.expiryDate = expiryDate;
        this.bank = bank;
    }

    // Getters and Setters
    public String getCardNumber() {
        return cardNumber;
    }

    public void setCardNumber(String cardNumber) {
        this.cardNumber = cardNumber;
    }

    public Date getExpiryDate() {
        return expiryDate;
    }

    public void setExpiryDate(Date expiryDate) {
        this.expiryDate = expiryDate;
    }

    public Bank getBank() {
        return bank;
    }

    public void setBank(Bank bank) {
        this.bank = bank;
    }

    @Override
    public String toString() {
        return "CardDetails{" +
                "cardNumber='" + cardNumber + '\'' +
                ", expiryDate=" + expiryDate +
                ", bank=" + bank +
                '}';
    }
}
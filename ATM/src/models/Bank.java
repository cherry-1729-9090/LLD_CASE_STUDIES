package models;

public class Bank {
    private String bankId;
    private String name;

    public Bank(String bankId, String name) {
        this.bankId = bankId;
        this.name = name;
    }

    // Getters and Setters
    public String getBankId() {
        return bankId;
    }

    public void setBankId(String bankId) {
        this.bankId = bankId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return "Bank{" +
                "bankId='" + bankId + '\'' +
                ", name='" + name + '\'' +
                '}';
    }
}
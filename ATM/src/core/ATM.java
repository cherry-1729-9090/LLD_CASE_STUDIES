package core;

import interfaces.ATMState;
import interfaces.BankService;
import hardware.*;
import models.CardDetails;
import enums.TransactionType;
import java.util.Map;

// Main ATM class - Facade and State pattern context
public class ATM {
    // State pattern
    private ATMState currentState;
    
    // ATM identification
    private String atmId;
    private String location;
    
    // Hardware components
    private CardReader cardReader;
    private Keypad keypad;
    private Screen screen;
    private Printer printer;
    private CashDispenser cashDispenser;
    private DepositSlot depositSlot;
    
    // Service components
    private BankService bankService;
    
    // Constructor
    public ATM(String atmId, String location, Map<Integer, Integer> initialCashInventory) {
        this.atmId = atmId;
        this.location = location;
        
        // Initialize hardware components
        this.cardReader = new CardReader();
        this.keypad = new Keypad();
        this.screen = new Screen();
        this.printer = new Printer();
        this.cashDispenser = new CashDispenser(initialCashInventory);
        this.depositSlot = new DepositSlot();
        
        // Set default cash dispensing strategy
        this.cashDispenser.setStrategy(new strategies.MinimalNotesStrategy());
        
        // Set initial state to Idle
        this.currentState = new states.IdleState();
        
        System.out.println("ATM initialized - ID: " + atmId + ", Location: " + location);
        screen.displayWelcome();
    }
    
    // State management
    public void setState(ATMState newState) {
        this.currentState = newState;
        System.out.println("ATM state changed to: " + newState.getClass().getSimpleName());
    }
    
    // Public methods that delegate to current state
    public void insertCard(CardDetails cardDetails) {
        currentState.insertCard(this, cardDetails);
    }
    
    public void authenticatePIN(String pin) {
        currentState.authenticatePIN(this, pin);
    }
    
    public void selectOperation(TransactionType type) {
        currentState.selectOperation(this, type);
    }
    
    public void performTransaction(Map<String, Object> transactionDetails) {
        currentState.performTransaction(this, transactionDetails);
    }
    
    public void cancel() {
        currentState.cancelTransaction(this);
    }
    
    public void ejectCard() {
        currentState.ejectCard(this);
    }
    
    // Getter methods for hardware components
    public CardReader getCardReader() {
        return cardReader;
    }
    
    public Keypad getKeypad() {
        return keypad;
    }
    
    public Screen getScreen() {
        return screen;
    }
    
    public Printer getPrinter() {
        return printer;
    }
    
    public CashDispenser getCashDispenser() {
        return cashDispenser;
    }
    
    public DepositSlot getDepositSlot() {
        return depositSlot;
    }
    
    public BankService getBankService() {
        return bankService;
    }
    
    public void setBankService(BankService bankService) {
        this.bankService = bankService;
    }
    
    public String getAtmId() {
        return atmId;
    }
    
    public String getLocation() {
        return location;
    }
}
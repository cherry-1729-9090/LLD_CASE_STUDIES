# ATM System - Simple Design

This is an ATM machine built in Java using design patterns. It works just like a real ATM where you can insert your card, enter PIN, and do transactions.

## What This ATM Can Do

- **Insert Card** - Put your card in the machine
- **Enter PIN** - Type your secret number
- **Check Balance** - See how much money you have
- **Withdraw Cash** - Take money out
- **Change PIN** - Update your secret number
- **Print Statement** - Get a list of recent transactions
- **Deposit Cash** - Put money in

## How It's Built

### Design Patterns Used

**1. Facade Pattern**
- The main ATM class is like a simple remote control that hides all the complex stuff inside

**2. State Pattern** 
- The ATM behaves differently based on what's happening:
  - Idle: Waiting for a card
  - HasCard: Card is in, waiting for PIN
  - TransactionSelection: PIN is correct, choose what to do
  - Different states for each transaction type

**3. Strategy Pattern**
- Different ways to give out cash:
  - Give fewer big notes (like one $100 instead of five $20s)
  - Give more small notes to keep machine balanced

**4. Proxy Pattern**
- Bank service that adds security, logging, and caching before talking to the real bank

## Class Diagram

```
                    ┌─────────────┐
                    │     ATM     │ ◄─── Facade Pattern
                    │ (Main Class)│
                    └─────────────┘
                           │
        ┌──────────────────┼──────────────────┐
        │                  │                  │
   ┌─────────┐      ┌─────────────┐    ┌──────────┐
   │Hardware │      │   States    │    │ Services │
   │Components│      │             │    │          │
   └─────────┘      └─────────────┘    └──────────┘
        │                  │                  │
┌───────┼───────┐         │          ┌───────┼───────┐
│       │       │         │          │       │       │
▼       ▼       ▼         ▼          ▼       ▼       ▼
Card   Cash   Screen   IdleState  BankService  Strategy
Reader Dispenser       HasCardState  Proxy    Pattern
Keypad  Printer       TransactionStates      (Cash)
```

## File Structure

```
src/
├── ATMDemo.java              # Try the ATM here
├── core/ATM.java            # Main ATM class
├── enums/                   # Transaction types and status
├── models/                  # Data classes (Card, Account, etc.)
├── interfaces/              # Contracts for different parts
├── hardware/                # ATM hardware (screen, keypad, etc.)
├── states/                  # Different ATM states
├── strategies/              # Ways to dispense cash
└── services/                # Bank communication
```

## How to Run

1. Open terminal in the src folder
2. Compile: `javac -cp . ATMDemo.java`  
3. Run: `java ATMDemo`

## What You'll See

The demo shows:
- Cash withdrawal with receipt
- Balance check
- Different cash dispensing methods
- PIN change
- Mini statement printing

## Key Features

**Security**
- PIN tries are limited (3 attempts)
- Cards expire and get checked
- All transactions are logged

**Hardware Simulation**
- Card reader for inserting/ejecting cards
- Screen for showing messages
- Keypad for entering PIN and amounts
- Cash dispenser with different note types
- Printer for receipts

**Smart Cash Dispensing**
- Gives the right mix of notes
- Checks if enough cash is available
- Can use different strategies (few big notes vs many small notes)

## How to Add New Features

**New Transaction Type:**
1. Create a new state class
2. Add the transaction type to the enum
3. Update the transaction selection state

**New Cash Strategy:**
1. Create a class that implements CashDispensingStrategy
2. Set it on the cash dispenser

**New Bank:**
1. Create a class that implements BankService
2. Use it with the proxy

## Example Usage

```java
// Make an ATM with cash
ATM atm = new ATM("ATM001", "Main Street", cashInventory);

// Set up bank connection  
atm.setBankService(new BankServiceProxy("HDFC Bank"));

// Use the ATM like a person would
atm.insertCard(myCard);
atm.authenticatePIN("1234");
atm.selectOperation(TransactionType.CASH_WITHDRAWAL);
atm.performTransaction(withdrawalDetails);
```

This ATM is built to be easy to understand and extend. Each part has one job, and you can easily add new features without breaking existing ones.
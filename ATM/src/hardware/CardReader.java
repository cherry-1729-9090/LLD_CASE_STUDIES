package hardware;

import models.CardDetails;

public class CardReader {
    private boolean cardInserted;
    private CardDetails currentCard;
    
    public CardReader() {
        this.cardInserted = false;
        this.currentCard = null;
    }
    
    /**
     * Simulate reading a card and return card details
     */
    public CardDetails readCard() {
        if (!cardInserted) {
            throw new IllegalStateException("No card inserted");
        }
        
        // Simulate reading card data
        // In real implementation, this would read from the magnetic strip or chip
        System.out.println("Reading card data...");
        return currentCard;
    }
    
    /**
     * Simulate card insertion
     */
    public void insertCard(CardDetails cardDetails) {
        if (cardInserted) {
            throw new IllegalStateException("Card already inserted");
        }
        this.currentCard = cardDetails;
        this.cardInserted = true;
        System.out.println("Card inserted: " + cardDetails.getCardNumber());
    }
    
    /**
     * Eject the card
     */
    public void ejectCard() {
        if (!cardInserted) {
            throw new IllegalStateException("No card to eject");
        }
        
        System.out.println("Ejecting card: " + currentCard.getCardNumber());
        this.cardInserted = false;
        this.currentCard = null;
    }
    
    public boolean isCardInserted() {
        return cardInserted;
    }
}
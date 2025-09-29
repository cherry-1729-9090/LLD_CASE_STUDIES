public class ExitGate {
    private final int gateId;
    private final IPricingStrategy pricingStrategy;

    public ExitGate(int gateId, IPricingStrategy pricingStrategy) {
        this.gateId = gateId;
        this.pricingStrategy = pricingStrategy;
    }

    public double processExit(Ticket ticket) {
        double price = pricingStrategy.calculatePrice(ticket);
        
        // Vacate the parking spot
        ticket.getSpot().vacate();
        
        return price;
    }

    public int getGateId() {
        return gateId;
    }

    public IPricingStrategy getPricingStrategy() {
        return pricingStrategy;
    }
}
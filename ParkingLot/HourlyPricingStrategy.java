import java.time.LocalDateTime;
import java.time.Duration;

public class HourlyPricingStrategy implements IPricingStrategy {
    private final double hourlyRate;

    public HourlyPricingStrategy(double hourlyRate) {
        this.hourlyRate = hourlyRate;
    }

    @Override
    public double calculatePrice(Ticket ticket) {
        LocalDateTime entryTime = ticket.getEntryTime();
        LocalDateTime exitTime = LocalDateTime.now();
        
        long minutes = Duration.between(entryTime, exitTime).toMinutes();
        
        // Calculate hours (minimum 1 hour billing)
        double hours = Math.max(1.0, Math.ceil(minutes / 60.0));
        
        double basePrice = hours * hourlyRate;
        
        // Additional charge for using electric charging
        if (ticket.isUsingCharging()) {
            basePrice += hours * 5.0; // $5 per hour for charging
        }
        
        return basePrice;
    }

    public double getHourlyRate() {
        return hourlyRate;
    }
}
import java.time.LocalDateTime;

public class Ticket {
    private final Vehicle vehicle;
    private final ParkingSpot spot;
    private final LocalDateTime entryTime;
    private final boolean isUsingCharging;

    public Ticket(Vehicle vehicle, ParkingSpot spot, boolean isUsingCharging) {
        this.vehicle = vehicle;
        this.spot = spot;
        this.entryTime = LocalDateTime.now();
        this.isUsingCharging = isUsingCharging;
    }

    public Vehicle getVehicle() {
        return vehicle;
    }

    public ParkingSpot getSpot() {
        return spot;
    }

    public LocalDateTime getEntryTime() {
        return entryTime;
    }

    public boolean isUsingCharging() {
        return isUsingCharging;
    }
}
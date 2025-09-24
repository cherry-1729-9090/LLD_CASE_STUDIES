public class ParkingSpot {
    private final SpotType spotType;
    private boolean isAvailable;
    private Vehicle currentVehicle;
    private final boolean hasChargingPoint;

    public ParkingSpot(SpotType spotType, boolean hasChargingPoint) {
        this.spotType = spotType;
        this.hasChargingPoint = hasChargingPoint;
        this.isAvailable = true;
        this.currentVehicle = null;
    }

    public void parkVehicle(Vehicle vehicle) {
        if (!isAvailable) {
            throw new IllegalStateException("Spot is already occupied");
        }
        this.currentVehicle = vehicle;
        this.isAvailable = false;
    }

    public void vacate() {
        this.currentVehicle = null;
        this.isAvailable = true;
    }

    public Vehicle getCurrentVehicle() {
        return currentVehicle;
    }

    public SpotType getSpotType() {
        return spotType;
    }

    public boolean isAvailable() {
        return isAvailable;
    }

    public boolean hasChargingPoint() {
        return hasChargingPoint;
    }
}
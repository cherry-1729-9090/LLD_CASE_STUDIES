public class EntryGate {
    private final int gateId;
    private final ParkingLot parkingLot;

    public EntryGate(int gateId, ParkingLot parkingLot) {
        this.gateId = gateId;
        this.parkingLot = parkingLot;
    }

    public Ticket generateTicket(Vehicle vehicle) {
        ParkingSpot spot = parkingLot.getAllocationStrategy().findSpot(parkingLot, vehicle);
        
        if (spot == null) {
            throw new RuntimeException("No available spot found for vehicle: " + vehicle.getVehicleNo());
        }

        boolean isUsingCharging = false;
        
        // Check if vehicle wants charging and spot supports it
        if (vehicle instanceof IElectricVehicle) {
            IElectricVehicle electricVehicle = (IElectricVehicle) vehicle;
            isUsingCharging = electricVehicle.wantsCharging() && spot.hasChargingPoint();
        }

        spot.parkVehicle(vehicle);
        return new Ticket(vehicle, spot, isUsingCharging);
    }

    public int getGateId() {
        return gateId;
    }
}
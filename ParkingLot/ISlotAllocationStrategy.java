public interface ISlotAllocationStrategy {
    ParkingSpot findSpot(ParkingLot parkingLot, Vehicle vehicle);
}
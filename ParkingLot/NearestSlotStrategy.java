public class NearestSlotStrategy implements ISlotAllocationStrategy {
    
    @Override
    public ParkingSpot findSpot(ParkingLot parkingLot, Vehicle vehicle) {
        for (ParkingFloor floor : parkingLot.getFloors()) {
            for (ParkingSpot spot : floor.getSpots()) {
                if (isSpotSuitable(spot, vehicle)) {
                    return spot;
                }
            }
        }
        return null; // No suitable spot found
    }

    private boolean isSpotSuitable(ParkingSpot spot, Vehicle vehicle) {
        if (!spot.isAvailable()) {
            return false;
        }

        // Check if electric vehicle needs charging and spot has charging point
        if (vehicle instanceof IElectricVehicle) {
            IElectricVehicle electricVehicle = (IElectricVehicle) vehicle;
            if (electricVehicle.wantsCharging() && !spot.hasChargingPoint()) {
                return false;
            }
        }

        // Check vehicle-spot compatibility
        return isVehicleCompatibleWithSpot(vehicle.getType(), spot.getSpotType());
    }

    private boolean isVehicleCompatibleWithSpot(VehicleType vehicleType, SpotType spotType) {
        switch (vehicleType) {
            case BIKE:
            case ELECTRIC_BIKE:
                return spotType == SpotType.SMALL || spotType == SpotType.ELECTRIC;
            case CAR:
                return spotType == SpotType.MEDIUM || spotType == SpotType.LARGE;
            case BUS:
                return spotType == SpotType.LARGE;
            default:
                return false;
        }
    }
}
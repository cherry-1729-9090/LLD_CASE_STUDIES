public class Bike extends Vehicle {
    public Bike(String vehicleNo) {
        super(vehicleNo);
    }

    @Override
    public VehicleType getType() {
        return VehicleType.BIKE;
    }
}
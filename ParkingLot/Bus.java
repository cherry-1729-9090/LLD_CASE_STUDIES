public class Bus extends Vehicle {
    public Bus(String vehicleNo) {
        super(vehicleNo);
    }

    @Override
    public VehicleType getType() {
        return VehicleType.BUS;
    }
}
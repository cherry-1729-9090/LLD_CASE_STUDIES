public abstract class Vehicle {
    private final String vehicleNo;

    public Vehicle(String vehicleNo) {
        this.vehicleNo = vehicleNo;
    }

    public String getVehicleNo() {
        return vehicleNo;
    }

    public abstract VehicleType getType();
}
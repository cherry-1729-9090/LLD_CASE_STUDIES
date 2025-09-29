public class Car extends Vehicle {
    public Car(String vehicleNo) {
        super(vehicleNo);
    }

    @Override
    public VehicleType getType() {
        return VehicleType.CAR;
    }
}
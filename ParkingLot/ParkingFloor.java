import java.util.List;
import java.util.ArrayList;

public class ParkingFloor {
    private final int floorNo;
    private final List<ParkingSpot> spots;

    public ParkingFloor(int floorNo) {
        this.floorNo = floorNo;
        this.spots = new ArrayList<>();
    }

    public void addSpot(ParkingSpot spot) {
        spots.add(spot);
    }

    public List<ParkingSpot> getSpots() {
        return new ArrayList<>(spots); // Return defensive copy
    }

    public int getFloorNo() {
        return floorNo;
    }
}
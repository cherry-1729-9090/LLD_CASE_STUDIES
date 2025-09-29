import java.util.Map;
import java.util.HashMap;

public class ParkingLotBuilder {
    private int noOfFloors;
    private Map<SpotType, Integer> spotsPerFloor;
    private ISlotAllocationStrategy allocationStrategy;
    private IPricingStrategy pricingStrategy;

    public ParkingLotBuilder() {
        this.spotsPerFloor = new HashMap<>();
        this.noOfFloors = 1;
    }

    public ParkingLotBuilder setFloors(int count) {
        this.noOfFloors = count;
        return this;
    }

    public ParkingLotBuilder setSpotsPerFloor(Map<SpotType, Integer> config) {
        this.spotsPerFloor = new HashMap<>(config);
        return this;
    }

    public ParkingLotBuilder setAllocationStrategy(ISlotAllocationStrategy strategy) {
        this.allocationStrategy = strategy;
        return this;
    }

    public ParkingLotBuilder setPricingStrategy(IPricingStrategy strategy) {
        this.pricingStrategy = strategy;
        return this;
    }

    public ParkingLot build() {
        if (allocationStrategy == null) {
            throw new IllegalStateException("Allocation strategy must be set");
        }

        ParkingLot parkingLot = new ParkingLot(allocationStrategy);

        // Create floors and spots
        for (int floorNum = 1; floorNum <= noOfFloors; floorNum++) {
            ParkingFloor floor = new ParkingFloor(floorNum);
            
            // Add spots to each floor
            for (Map.Entry<SpotType, Integer> entry : spotsPerFloor.entrySet()) {
                SpotType spotType = entry.getKey();
                int count = entry.getValue();
                
                for (int i = 0; i < count; i++) {
                    boolean hasCharging = (spotType == SpotType.ELECTRIC);
                    ParkingSpot spot = new ParkingSpot(spotType, hasCharging);
                    floor.addSpot(spot);
                }
            }
            
            parkingLot.addFloor(floor);
        }

        // Add default gates if pricing strategy is provided
        if (pricingStrategy != null) {
            EntryGate entryGate = new EntryGate(1, parkingLot);
            ExitGate exitGate = new ExitGate(1, pricingStrategy);
            
            parkingLot.addEntryGate(entryGate);
            parkingLot.addExitGate(exitGate);
        }

        return parkingLot;
    }
}
import java.util.List;
import java.util.ArrayList;
import java.util.stream.Collectors;

public class ParkingLot {
    private final List<ParkingFloor> floors;
    private final ISlotAllocationStrategy allocationStrategy;
    private final List<EntryGate> entryGates;
    private final List<ExitGate> exitGates;

    public ParkingLot(ISlotAllocationStrategy allocationStrategy) {
        this.allocationStrategy = allocationStrategy;
        this.floors = new ArrayList<>();
        this.entryGates = new ArrayList<>();
        this.exitGates = new ArrayList<>();
    }

    public void addFloor(ParkingFloor floor) {
        floors.add(floor);
    }

    public void addEntryGate(EntryGate gate) {
        entryGates.add(gate);
    }

    public void addExitGate(ExitGate gate) {
        exitGates.add(gate);
    }

    public List<ParkingSpot> getAvailableSpots() {
        return floors.stream()
                .flatMap(floor -> floor.getSpots().stream())
                .filter(ParkingSpot::isAvailable)
                .collect(Collectors.toList());
    }

    public List<ParkingFloor> getFloors() {
        return new ArrayList<>(floors); // Return defensive copy
    }

    public ISlotAllocationStrategy getAllocationStrategy() {
        return allocationStrategy;
    }

    public List<EntryGate> getEntryGates() {
        return new ArrayList<>(entryGates);
    }

    public List<ExitGate> getExitGates() {
        return new ArrayList<>(exitGates);
    }
}
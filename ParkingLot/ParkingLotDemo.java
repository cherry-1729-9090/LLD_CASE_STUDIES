import java.util.Map;
import java.util.HashMap;

public class ParkingLotDemo {
    
    public static void main(String[] args) {
        System.out.println("=== Parking Lot System Demo ===\n");
        
        // Create parking lot using builder pattern
        ParkingLot parkingLot = createParkingLot();
        
        // Create vehicles
        Vehicle car = new Car("ABC-123");
        Vehicle bike = new Bike("XYZ-456");
        ElectricBike electricBike = new ElectricBike("ELE-789");
        electricBike.setWantsCharging(true);
        Vehicle bus = new Bus("BUS-999");
        
        // Get entry and exit gates
        EntryGate entryGate = parkingLot.getEntryGates().get(0);
        ExitGate exitGate = parkingLot.getExitGates().get(0);
        
        System.out.println("Available spots before parking: " + parkingLot.getAvailableSpots().size());
        
        // Park vehicles and generate tickets
        try {
            Ticket carTicket = parkVehicle(entryGate, car);
            Ticket bikeTicket = parkVehicle(entryGate, bike);
            Ticket electricBikeTicket = parkVehicle(entryGate, electricBike);
            Ticket busTicket = parkVehicle(entryGate, bus);
            
            System.out.println("\nAvailable spots after parking: " + parkingLot.getAvailableSpots().size());
            
            // Simulate some time passing
            simulateTimeDelay();
            
            // Process exits and calculate charges
            System.out.println("\n=== Processing Exits ===");
            processExit(exitGate, carTicket);
            processExit(exitGate, bikeTicket);
            processExit(exitGate, electricBikeTicket);
            processExit(exitGate, busTicket);
            
            System.out.println("\nAvailable spots after exits: " + parkingLot.getAvailableSpots().size());
            
        } catch (Exception e) {
            System.err.println("Error: " + e.getMessage());
        }
        
        System.out.println("\n=== Demo Complete ===");
    }
    
    private static ParkingLot createParkingLot() {
        System.out.println("Creating parking lot with 2 floors...");
        
        // Define spots per floor
        Map<SpotType, Integer> spotsConfig = new HashMap<>();
        spotsConfig.put(SpotType.SMALL, 10);      // For bikes
        spotsConfig.put(SpotType.MEDIUM, 20);     // For cars
        spotsConfig.put(SpotType.LARGE, 5);       // For buses
        spotsConfig.put(SpotType.ELECTRIC, 3);    // For electric vehicles with charging
        
        return new ParkingLotBuilder()
                .setFloors(2)
                .setSpotsPerFloor(spotsConfig)
                .setAllocationStrategy(new NearestSlotStrategy())
                .setPricingStrategy(new HourlyPricingStrategy(10.0)) // $10 per hour
                .build();
    }
    
    private static Ticket parkVehicle(EntryGate entryGate, Vehicle vehicle) {
        System.out.println("Parking " + vehicle.getType() + " with number: " + vehicle.getVehicleNo());
        
        Ticket ticket = entryGate.generateTicket(vehicle);
        
        System.out.println("  - Assigned to " + ticket.getSpot().getSpotType() + " spot");
        if (ticket.isUsingCharging()) {
            System.out.println("  - Using charging facility");
        }
        
        return ticket;
    }
    
    private static void processExit(ExitGate exitGate, Ticket ticket) {
        Vehicle vehicle = ticket.getVehicle();
        double charge = exitGate.processExit(ticket);
        
        System.out.println(vehicle.getType() + " (" + vehicle.getVehicleNo() + ") - Charge: $" 
                          + String.format("%.2f", charge));
    }
    
    private static void simulateTimeDelay() {
        // Simulate 2 hours of parking
        System.out.println("\n--- Simulating 2 hours of parking time ---");
        
        try {
            Thread.sleep(100); // Small delay for demo purposes
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }
}
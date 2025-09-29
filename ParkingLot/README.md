# Parking Lot System

A comprehensive parking lot management system implemented in Java following SOLID principles and design patterns.

## Features

- **Multiple Vehicle Types**: Cars, Bikes, Buses, and Electric Bikes
- **Multiple Spot Types**: Small, Medium, Large, and Electric (with charging)
- **Flexible Allocation Strategy**: Pluggable spot allocation algorithms
- **Dynamic Pricing**: Configurable pricing strategies
- **Electric Vehicle Support**: Charging facility management
- **Builder Pattern**: Easy parking lot construction
- **Strategy Pattern**: Interchangeable algorithms for allocation and pricing

## Class Diagram

```
┌─────────────────────┐    ┌──────────────────────┐    ┌────────────────────────┐
│    <<enumeration>>  │    │    <<enumeration>>   │    │     <<interface>>      │
│    VehicleType      │    │      SpotType        │    │   IElectricVehicle     │
├─────────────────────┤    ├──────────────────────┤    ├────────────────────────┤
│ + CAR               │    │ + SMALL              │    │ + wantsCharging(): boolean │
│ + BIKE              │    │ + MEDIUM             │    │ + setWantsCharging(boolean)│
│ + BUS               │    │ + LARGE              │    └────────────────────────┘
│ + ELECTRIC_BIKE     │    │ + ELECTRIC           │              ▲
└─────────────────────┘    └──────────────────────┘              │
                                                                  │
┌─────────────────────────────────────────────────────────────────┼─────────────┐
│                        <<abstract>>                             │             │
│                         Vehicle                                 │             │
├─────────────────────────────────────────────────────────────────┼─────────────┤
│ - vehicleNo: String                                             │             │
├─────────────────────────────────────────────────────────────────┼─────────────┤
│ + Vehicle(vehicleNo: String)                                    │             │
│ + getVehicleNo(): String                                        │             │
│ + getType(): VehicleType {abstract}                             │             │
└─────────────────────────────────────────────────────────────────┼─────────────┘
                                    ▲                             │
                  ┌─────────────────┼─────────────────┐           │
                  │                 │                 │           │
                  │                 │                 │           │
        ┌─────────▼─────────┐ ┌─────▼─────┐ ┌─────────▼─────────┐ │
        │       Car         │ │   Bike    │ │       Bus         │ │
        ├───────────────────┤ ├───────────┤ ├───────────────────┤ │
        │ + getType(): CAR  │ │+ getType()│ │ + getType(): BUS  │ │
        └───────────────────┘ │   : BIKE  │ └───────────────────┘ │
                              └───────────┘                       │
                                    ▲                             │
                                    │                             │
                          ┌─────────▼─────────┐                   │
                          │   ElectricBike    ├───────────────────┘
                          ├───────────────────┤
                          │- wantsCharging:   │
                          │  boolean          │
                          ├───────────────────┤
                          │+ getType():       │
                          │  ELECTRIC_BIKE    │
                          │+ wantsCharging()  │
                          │+ setWantsCharging │
                          └───────────────────┘

┌────────────────────────┐    ┌─────────────────────────────────────────────┐
│    <<interface>>       │    │              ParkingSpot                    │
│ISlotAllocationStrategy │    ├─────────────────────────────────────────────┤
├────────────────────────┤    │ - spotType: SpotType                        │
│+ findSpot(ParkingLot,  │    │ - isAvailable: boolean                      │
│  Vehicle): ParkingSpot │    │ - currentVehicle: Vehicle                   │
└────────────────────────┘    │ - hasChargingPoint: boolean                 │
            ▲                 ├─────────────────────────────────────────────┤
            │                 │ + ParkingSpot(SpotType, boolean)            │
            │                 │ + parkVehicle(Vehicle): void                │
    ┌───────▼───────┐         │ + vacate(): void                            │
    │NearestSlot    │         │ + getCurrentVehicle(): Vehicle              │
    │Strategy       │         │ + getSpotType(): SpotType                   │
    ├───────────────┤         │ + isAvailable(): boolean                    │
    │+ findSpot()   │         │ + hasChargingPoint(): boolean               │
    └───────────────┘         └─────────────────────────────────────────────┘
                                              ▲
                                              │ composition
                                              │
┌────────────────────────┐              ┌─────▼────────┐
│    <<interface>>       │              │ParkingFloor  │
│   IPricingStrategy     │              ├──────────────┤
├────────────────────────┤              │- floorNo: int│
│+ calculatePrice(Ticket)│              │- spots: List │
│  : double              │              │  <ParkingSpot>│
└────────────────────────┘              ├──────────────┤
            ▲                           │+ addSpot()   │
            │                           │+ getSpots()  │
    ┌───────▼───────┐                   └──────────────┘
    │HourlyPricing  │                          ▲
    │Strategy       │                          │ composition
    ├───────────────┤                          │
    │- hourlyRate:  │                   ┌──────▼──────┐
    │  double       │                   │ ParkingLot  │
    ├───────────────┤                   ├─────────────┤
    │+ calculatePrice│◄──────────────────┤- floors: List<ParkingFloor>
    └───────────────┘                   │- allocationStrategy: ISlotAllocationStrategy
                                        │- entryGates: List<EntryGate>
                                        │- exitGates: List<ExitGate>
                                        ├─────────────┤
                                        │+ addFloor() │
                                        │+ addEntryGate()
                                        │+ addExitGate()
                                        │+ getAvailableSpots()
                                        └─────────────┘
                                              ▲
                                              │ association
                                              │
                    ┌─────────────────────────┼─────────────────────────┐
                    │                         │                         │
            ┌───────▼──────┐          ┌───────▼──────┐          ┌───────▼──────┐
            │  EntryGate   │          │    Ticket    │          │  ExitGate    │
            ├──────────────┤          ├──────────────┤          ├──────────────┤
            │- gateId: int │          │- vehicle:    │          │- gateId: int │
            │- parkingLot: │          │  Vehicle     │          │- pricingStrategy:
            │  ParkingLot  │          │- spot:       │          │  IPricingStrategy
            ├──────────────┤          │  ParkingSpot │          ├──────────────┤
            │+ generateTicket          │- entryTime:  │          │+ processExit │
            │  (Vehicle):  │          │  LocalDateTime│          │  (Ticket):   │
            │  Ticket      │          │- isUsingCharging         │  double      │
            └──────────────┘          │  : boolean   │          └──────────────┘
                                      ├──────────────┤
                                      │+ getVehicle()│
                                      │+ getSpot()   │
                                      │+ getEntryTime()
                                      │+ isUsingCharging()
                                      └──────────────┘

┌─────────────────────────────────────────────────────────────────────────────┐
│                           ParkingLotBuilder                                 │
├─────────────────────────────────────────────────────────────────────────────┤
│ - noOfFloors: int                                                           │
│ - spotsPerFloor: Map<SpotType, Integer>                                     │
│ - allocationStrategy: ISlotAllocationStrategy                               │
│ - pricingStrategy: IPricingStrategy                                         │
├─────────────────────────────────────────────────────────────────────────────┤
│ + setFloors(int): ParkingLotBuilder                                         │
│ + setSpotsPerFloor(Map<SpotType, Integer>): ParkingLotBuilder               │
│ + setAllocationStrategy(ISlotAllocationStrategy): ParkingLotBuilder         │
│ + setPricingStrategy(IPricingStrategy): ParkingLotBuilder                   │
│ + build(): ParkingLot                                                       │
└─────────────────────────────────────────────────────────────────────────────┘
```

## Architecture

### Design Patterns Used
- **Strategy Pattern**: For slot allocation and pricing strategies
- **Builder Pattern**: For constructing complex parking lot objects
- **Template Method**: Vehicle hierarchy with abstract methods

### SOLID Principles
- **Single Responsibility**: Each class has one clear purpose
- **Open/Closed**: System is open for extension (new strategies) but closed for modification
- **Liskov Substitution**: Vehicle subclasses can be used interchangeably
- **Interface Segregation**: Clean, focused interfaces
- **Dependency Inversion**: High-level modules depend on abstractions

## Class Structure

### Core Classes

**Enumerations:**
- `VehicleType` - CAR, BIKE, BUS, ELECTRIC_BIKE
- `SpotType` - SMALL, MEDIUM, LARGE, ELECTRIC

**Interfaces:**
- `IElectricVehicle` - Contract for vehicles requiring charging
- `ISlotAllocationStrategy` - Strategy for finding parking spots
- `IPricingStrategy` - Strategy for calculating parking fees

**Vehicle Hierarchy:**
- `Vehicle` (abstract) - Base class for all vehicles
- `Car`, `Bike`, `Bus` - Concrete vehicle implementations
- `ElectricBike` - Electric bike implementing IElectricVehicle

**Core System Classes:**
- `ParkingSpot` - Individual parking space with type and charging capability
- `ParkingFloor` - Collection of parking spots on a floor
- `ParkingLot` - Main system orchestrator with floors and strategies
- `Ticket` - Parking ticket with entry time, vehicle, and charging info

**Gateway Classes:**
- `EntryGate` - Handles vehicle entry and ticket generation
- `ExitGate` - Processes vehicle exit and payment calculation

**Strategy Implementations:**
- `NearestSlotStrategy` - Finds nearest available suitable spot with compatibility checking
- `HourlyPricingStrategy` - Time-based pricing with charging surcharge

**Builder Pattern:**
- `ParkingLotBuilder` - Fluent API for constructing parking lots

### Key Relationships

1. **Inheritance**: `ElectricBike` extends `Bike` extends `Vehicle`
2. **Implementation**: `ElectricBike` implements `IElectricVehicle`
3. **Composition**: `ParkingLot` contains `ParkingFloor` contains `ParkingSpot`
4. **Strategy Pattern**: `ParkingLot` uses `ISlotAllocationStrategy`, `ExitGate` uses `IPricingStrategy`
5. **Association**: `Ticket` references `Vehicle` and `ParkingSpot`
6. **Builder Pattern**: `ParkingLotBuilder` constructs `ParkingLot` instances

## Usage

```java
// Create parking lot using builder
ParkingLot parkingLot = new ParkingLotBuilder()
    .setFloors(2)
    .setSpotsPerFloor(spotsConfig)
    .setAllocationStrategy(new NearestSlotStrategy())
    .setPricingStrategy(new HourlyPricingStrategy(10.0))
    .build();

// Park a vehicle
Vehicle car = new Car("ABC-123");
EntryGate entryGate = parkingLot.getEntryGates().get(0);
Ticket ticket = entryGate.generateTicket(car);

// Exit and pay
ExitGate exitGate = parkingLot.getExitGates().get(0);
double charge = exitGate.processExit(ticket);
```

## Running the Demo

```bash
javac *.java
java ParkingLotDemo
```
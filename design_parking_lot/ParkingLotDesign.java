import java.util.*;

enum VehicleSize {
    MOTORCYCLE, COMPACT, LARGE;
}

abstract class Vehicle {
    String plate;
    VehicleSize size;
    long entryTime;

    public Vehicle(String plate, VehicleSize size) {
        this.plate = plate;
        this.size = size;
    }
}

class Motorcycle extends Vehicle {
    public Motorcycle(String plate) {
        super(plate, VehicleSize.MOTORCYCLE);
    }
}

class Car extends Vehicle {
    public Car(String plate) {
        super(plate, VehicleSize.COMPACT);
    }
}

class Bus extends Vehicle {
    public Bus(String plate) {
        super(plate, VehicleSize.LARGE);
    }
}

class ParkingSpot {
    VehicleSize size;
    boolean occupied;
    Vehicle vehicle;

    public ParkingSpot(VehicleSize size) {
        this.size = size;
        this.occupied = false;
        this.vehicle = null;
    }

    public boolean canFit(Vehicle v) {
        if (occupied) return false;
        if (v.size == VehicleSize.MOTORCYCLE) return true;
        if (v.size == VehicleSize.COMPACT) return size == VehicleSize.COMPACT || size == VehicleSize.LARGE;
        if (v.size == VehicleSize.LARGE) return size == VehicleSize.LARGE;
        return false;
    }

    public boolean park(Vehicle v) {
        if (!canFit(v)) return false;
        occupied = true;
        vehicle = v;
        v.entryTime = System.currentTimeMillis();
        return true;
    }

    public void leave() {
        occupied = false;
        vehicle = null;
    }
}

class ParkingLot {
    List<ParkingSpot> spots;

    public ParkingLot(List<ParkingSpot> spots) {
        this.spots = spots;
    }

    public boolean park(Vehicle v) {
        for (ParkingSpot s : spots) {
            if (s.park(v)) {
                System.out.println("Vehicle " + v.plate + " parked.");
                return true;
            }
        }
        System.out.println("No spot available for " + v.plate);
        return false;
    }

    public double leave(Vehicle v) {
        for (ParkingSpot s : spots) {
            if (s.vehicle == v) {
                s.leave();
                long duration = System.currentTimeMillis() - v.entryTime;
                double fee = calculateFee(duration);
                System.out.printf("Vehicle %s left. Fee: $%.2f%n", v.plate, fee);
                return fee;
            }
        }
        return 0.0;
    }

    private double calculateFee(long durationMillis) {
        double hours = durationMillis / (1000.0 * 60 * 60);
        return Math.ceil(hours) * 1.0;  // $1 per hour
    }

    public int availableSpots() {
        int count = 0;
        for (ParkingSpot s : spots) {
            if (!s.occupied) count++;
        }
        return count;
    }
}

public class ParkingLotDesign {
    public static void main(String[] args) throws InterruptedException {
        List<ParkingSpot> spots = new ArrayList<>();
        for (int i = 0; i < 5; i++) spots.add(new ParkingSpot(VehicleSize.MOTORCYCLE));
        for (int i = 0; i < 10; i++) spots.add(new ParkingSpot(VehicleSize.COMPACT));
        for (int i = 0; i < 3; i++) spots.add(new ParkingSpot(VehicleSize.LARGE));

        ParkingLot lot = new ParkingLot(spots);

        Vehicle bike = new Motorcycle("M123");
        Vehicle car = new Car("C456");
        Vehicle bus = new Bus("B789");

        lot.park(bike);
        lot.park(car);
        lot.park(bus);

        System.out.println("Available spots: " + lot.availableSpots());

        Thread.sleep(2000);  // Simulate time passing

        lot.leave(bike);
        lot.leave(car);
        lot.leave(bus);

        System.out.println("Available spots after leaving: " + lot.availableSpots());
    }
}

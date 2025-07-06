# Parking Lot Design - Interview Notes

## 🌟 Clarify requirements

Before I start designing the system, let me clarify the requirements so we’re aligned.

- What types of vehicles should we support? For example, motorcycles, cars, buses?
- Do we have different parking spot sizes, such as small, compact, and large?
- Do we want to track parking time and compute parking fees when a vehicle leaves?
- Is this for a single parking lot?

✅ Assuming the interviewer answers:
- Types of vehicles: motorcycles, cars, buses  
- Parking spot sizes: small, compact, large  
- Parking time tracking & fee calculation: yes  
- Single parking lot: yes  

Great. So, I’ll design for motorcycles, cars, and buses, with parking spots of small, compact, or large sizes. We’ll track parking time for fee calculation, and focus on a single lot.

---

## 🌟 Design explanation

I will define the following classes:

- `ParkingLot`: Represents the entire lot, holds the parking spots.
- `ParkingSpot`: Represents each individual spot, tracks its size, whether it’s occupied, and which vehicle is in it.
- `Vehicle`: An abstract class, with subclasses for `Motorcycle`, `Car`, and `Bus`. Each vehicle tracks its plate, size, and entry time.

The lot tries to find the first suitable spot when parking. When a vehicle leaves, we compute the parking fee based on its parking duration.

---

## 🌟 Code logic explanation

- When parking a vehicle, I scan the spots and park in the first available one that fits.
- When a vehicle leaves, I compute the fee based on how long it was parked.
- I’m using a simple rule: one dollar per hour, rounded up.
- I’ll define an enum for vehicle size.
- Then, I’ll define an abstract `Vehicle` class and subclasses.
- `ParkingSpot` will have size, occupied flag, and the vehicle reference.
- `ParkingLot` will have a list of spots and handle park/leave operations.

---

## 🌟 Main example explanation

In the main function:

- We create a lot with 5 small, 10 compact, and 3 large spots.
- We park a motorcycle, a car, and a bus.
- We simulate time passing, then remove the vehicles and print the fee.

Let me show how this would work:

- We initialize the lot with some spots of each type.
- We park a motorcycle, a car, and a bus.
- We simulate time passing and compute the fee when they leave.

---

## 🌟 Closing

If I had more time, I would extend the design to include levels, faster spot search, reservations, or more complex fee structures.

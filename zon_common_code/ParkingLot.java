import java.util.List;
import java.util.Map;
import java.lang.Thread.State;
import java.util.*;
public class ParkingLot {
    // requirement
    // 1.search a suitable parking spot based on the size of the car
    // 2.generate a parking ticket for the car
    // 3.when the car leaves, calculate the parking fee
    // 4. multiple parking level
    
    // strategy pattern
    // 1.how to find a parking spot -> the nearest spot? the smallest suitable spot?
    // 2.how to calculate the parking fee -> based on the hour? based on the size of spot? one-time fee?

    // entity
    // 1.parking lot: list of parking level, map of car plate number and parking ticket
    // method: park a car, unpark a car, (calculate the fee)
    // 2.parking level: list of parking lot, 
    // method:  find a spot,
    // 3.parking spot: size, current car number plate
    // method: check if available
    // 4.parking ticket: car plate number, parking spot, start time, end time
    // method: aclculate the fee
    // 5.car: size

    // change car to vehicle
    // and add vehicle type
    interface FeeCalculationStrategy{
        int calculate(ParkingTicket ticket);
    }
    public static class HourBasedFeeStrategy implements FeeCalculationStrategy {
        int hourRate;
        public HourBasedFeeStrategy(int hourRate){
            this.hourRate = hourRate;
        }
        @Override
        public int calculate(ParkingTicket ticket){
            long durationMillis =
                System.currentTimeMillis() - ticket.getStartTime();

            long durationHours =
                Math.max(1, (durationMillis + 3_600_000 - 1) / 3_600_000);

            return (int) durationHours * hourRate;
        }
    }
    public static class ParkingLotSystem {
        List<ParkingLevel> levelList;
        Map<String, ParkingTicket> plateToTicket;
        SpotSelectionStrategy spotSelectionStrategy ;
        FeeCalculationStrategy feeCalculationStrategy ;
        public ParkingLotSystem(List<ParkingLevel> levelList,SpotSelectionStrategy spotSelectionStrategy,FeeCalculationStrategy feeCalculationStrategy){
            this.levelList = levelList;
            plateToTicket = new HashMap<>();
            this.spotSelectionStrategy = spotSelectionStrategy;
            this.feeCalculationStrategy = feeCalculationStrategy;
        }
        public ParkingTicket parkCar(Car car){
            if(plateToTicket.containsKey(car.getPlateNum())) throw new IllegalArgumentException("the car has already parked");
            // find a spot
            ParkingSpot spot = spotSelectionStrategy.select(car, levelList);
            if(spot == null) throw new IllegalStateException("No available parking spot");
            // mark the spot parked
            if(!spot.parkCar(car)) throw new IllegalArgumentException("the spot is not empty");
            // create a ticket and return
            ParkingTicket ticket = new ParkingTicket(car, spot);
            plateToTicket.put(car.getPlateNum(), ticket);
            return ticket;
        }
        public int unParkCar(ParkingTicket ticket){
            
            // calculate the fee
            int fee = feeCalculationStrategy.calculate(ticket);
            // remove the car from the parking lot
            ParkingSpot spot= ticket.getParkingSpot();
            spot.removeCar();
            // remove from the plateToTicket
            plateToTicket.remove(ticket.getCar().getPlateNum()); 
            return fee;
        }
    }
    interface SpotSelectionStrategy{
        ParkingSpot select(Car car, List<ParkingLevel> levelList);
    }
    public static class SmallestFitStrategy implements SpotSelectionStrategy{
        @Override
        public ParkingSpot select(Car car, List<ParkingLevel> levelList){
            ParkingSpot ans = null;
            
            for(ParkingLevel level: levelList){
                for(ParkingSpot spot: level.getSpotList()){
                    if(!spot.isAvailable() || !spot.canFit(car)) continue;
                    // if(ans == null || spot.getSize().getRank() < ans.getSize().getRank()) ans = spot;
                    if(ans == null || spot.getSize().ordinal() < ans.getSize().ordinal()) ans = spot;
                }
            }

            return ans;
        }
    }
    public static class ParkingLevel {
        int levelNumber;
        List<ParkingSpot> spotList;
        public ParkingLevel(int levelNumber, List<ParkingSpot> spotList){
            this.levelNumber = levelNumber;
            this.spotList = spotList;
        }
        public List<ParkingSpot> getSpotList(){
            return spotList;
        }
    }
    public static class ParkingSpot {
        Size size; Car car;
        public ParkingSpot(Size size){
            this.size = size; this.car = null;
        }
        public boolean parkCar(Car car){
            if(this.car != null || !isAvailable() || !canFit(car)) return false;
            this.car = car;
            return true;
        }
        public boolean isAvailable(){
            return car==null;
        }
        public void removeCar(){
            this.car = null;
        }
        public boolean canFit(Car car){
            return car.length <= size.length && car.width<=size.width && car.height<=size.height;
        }
        public Size getSize(){
            return size;
        }
    }
    public static class ParkingTicket{
        Car car; long startAt;  ParkingSpot parkingSpot;
        public ParkingTicket(Car car, ParkingSpot parkingSpot){
             this.car = car; this.startAt = System.currentTimeMillis();this.parkingSpot = parkingSpot;
        }
        public long getStartTime(){
            return startAt;
        }
        public ParkingSpot getParkingSpot(){
            return parkingSpot;
        }
        public Car getCar(){
            return car;
        }
    }
    public static class Car{
        String plateNum; int length; int width; int height;
        public Car(String plateNum, int length, int width, int height){
            this.plateNum = plateNum; this.length = length; this.width =width; this.height = height;
        }
        public String getPlateNum(){
            return plateNum;
        }
    }
    enum Size{
        SMALL(2,3,4),
        MEDIUM (3,4,4),
        LARGE(4,5,6);
        int width; int length; int height;

        Size(int width, int length, int height){
            this.width=width; this.length=length; this.height=height;
        }
        // public int getRank(){
        //     return rank;
        // }
    }
}

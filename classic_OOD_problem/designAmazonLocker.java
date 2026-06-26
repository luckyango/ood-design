// In this chapter, we will design a Shipping Locker system similar to UPS, FedEx, or Amazon Locker. It offers
// customers a convenient and secure way to pick up their online orders. The system manages locker
// availability, assigns incoming packages to appropriate lockers, and ensures a smooth package retrieval
// process for customers.

// first confirmation: 1. delivery person arrives the locker and asks for a compartment to deposit the 
// package 2. the system find a smallest compartment to deposit the package. the system will generate 
// an access token and send it to user. 

// Q&A with interviewer: 1.what if there is no suitable lockers 2.the locker is described by height width
// and depth 3.is there any time limit for picking up the package in the locker?  -> set a free period
// when exceeding the period, charge the user based on the size of lockers

// entity design
import java.util.*;
public class designAmazonLocker {

    enum LockerSize {
        SMALL,
        MEDIUM,
        LARGE
    }

    class Parcel {
        String parcelId;
        LockerSize size;
        User user;

        public Parcel(String parcelId, LockerSize size, User user) {
            this.parcelId = parcelId;
            this.size = size;
            this.user = user;
        }
    }

    class User {
        String userId;

        public User(String userId) {
            this.userId = userId;
        }
    }

    class Locker {
        String lockerId;
        LockerSize size;
        boolean occupied;
        Parcel currentParcel;

        public Locker(String lockerId, LockerSize size) {
            this.lockerId = lockerId;
            this.size = size;
        }

        public void assignParcel(Parcel parcel) {
            this.currentParcel = parcel;
            this.occupied = true;
        }

        public Parcel releaseParcel() {
            Parcel parcel = currentParcel;

            currentParcel = null;
            occupied = false;

            return parcel;
        }
    }

    class LockerManager {

        // available lockers by size
        Map<LockerSize, Queue<Locker>> availableLockers;

        // token -> occupied locker
        Map<String, Locker> occupiedLockers;

        Random random = new Random();

        private static final String CHARS =
                "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZ";

        public LockerManager() {

            availableLockers = new HashMap<>();
            occupiedLockers = new HashMap<>();

            for (LockerSize size : LockerSize.values()) {
                availableLockers.put(size, new LinkedList<>());
            }
        }

        public void addLocker(Locker locker) {
            availableLockers
                    .get(locker.size)
                    .offer(locker);
        }

        // delivery person deposits package
        public String deliver(Parcel parcel) {

            Locker locker = findSmallestSuitableLocker(parcel);

            if (locker == null) {
                return null;
            }

            locker.assignParcel(parcel);

            String token = generateAccessToken();

            occupiedLockers.put(token, locker);

            return token;
        }

        // customer picks up package
        public Parcel pickup(String token) {

            Locker locker = occupiedLockers.get(token);

            if (locker == null) {
                throw new IllegalArgumentException(
                        "Invalid access token"
                );
            }

            occupiedLockers.remove(token);

            Parcel parcel = locker.releaseParcel();

            availableLockers
                    .get(locker.size)
                    .offer(locker);

            return parcel;
        }

        private Locker findSmallestSuitableLocker(Parcel parcel) {

            LockerSize[] sizes = LockerSize.values();

            for (LockerSize size : sizes) {

                // find smallest suitable size
                if (size.ordinal() < parcel.size.ordinal()) {
                    continue;
                }

                Queue<Locker> queue =
                        availableLockers.get(size);

                if (!queue.isEmpty()) {
                    return queue.poll();
                }
            }

            return null;
        }

        private String generateAccessToken() {

            StringBuilder sb = new StringBuilder();

            for (int i = 0; i < 6; i++) {
                sb.append(
                        CHARS.charAt(
                                random.nextInt(CHARS.length())
                        )
                );
            }

            return sb.toString();
        }
    }

}

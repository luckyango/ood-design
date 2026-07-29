import java.time.Duration;
import java.util.*;
// requirements
// 1. one locker site contains several compartments
// 2. open a compartment according to the given size of the package
// 3. generate a code for the stored package and send to user

// entity
// 1. locker site - the system to manage the whole flow, contains multiple compartments
//                  handle (code, compartment) pair
// 2. compartments - height, width, length, isAvaliable
// 3. package - weight, height, length, with code
// 4. code - code content, expiration time
// 4. user - list of packages
public class ZonLocker {
    static class LockerSystem{
        int id; List<Compartment> compartments; Map<String, Compartment> codeToCompartment;
        Map<String,Code> codeInfo;
        // method1: find an suitable(based on the package size) and available (based on idle states of compartment) compartment
        //  then return the code
        public LockerSystem(int id, List<Compartment> compartments){
            this.id = id; this.compartments = compartments;
            codeToCompartment = new HashMap<>();
            codeInfo = new HashMap<>();
        }
        public String storePackage(Parcel parcel){
            Compartment selected = findSmallestSuitableLocker(parcel);
            if(selected == null) throw new IllegalStateException("cant find suitable compartment");
            String codeVal = generateCode();

            // build the code
            Code code = new Code(codeVal, System.currentTimeMillis()+Duration.ofHours(6).toMillis());
            // add the code 
            codeToCompartment.put(codeVal, selected);
            codeInfo.put(codeVal, code);

            // set the state of the selected compartment
            selected.setParcel(parcel);
            
            return codeVal;
        }

        public Parcel pickUpParcel(String codeVal){
            // check if the code valid
            if(!codeToCompartment.containsKey(codeVal) ) throw new IllegalArgumentException("invalid code");

            // check if the code exppire
            Code code = codeInfo.get(codeVal);
            if(code.expired_at < System.currentTimeMillis()) throw new IllegalStateException("the code has expired");

            Compartment c = codeToCompartment.get(codeVal);
            
            // set the compartment's parcel to null
            Parcel ans = c.removeParcel();

            // remove the code from two maps
            codeToCompartment.remove(codeVal);
            codeInfo.remove(codeVal);

            return ans;
        }

        public Compartment findSmallestSuitableLocker(Parcel parcel){
            Compartment seleted = null;

            for(Compartment c: compartments){
                if(!c.isAvailable() || !c.canFit(parcel)) continue;
                if(seleted == null || (seleted.isAvailable() && c.size.getRank() < 
                seleted.size.getRank())) seleted = c;

            }
            return seleted;
        }
        // method2: generate the code for the compartment
        Random rand = new Random();
        public String generateCode(){
            String code;
            do{
                int number = 100000 + rand.nextInt(900000);
                code = String.valueOf(number);
            }while(codeToCompartment.containsKey(code));
            return code;
        }

    }
    static class Compartment{
        int id; Size size; private Parcel parcel;
        public Compartment(int id, Size size){
           this.id = id; this.size = size; parcel = null;
        }
        // method1. return if available
        public boolean isAvailable(){
            return parcel==null;
        }
        public void setParcel(Parcel parcel){
            this.parcel = parcel;
        }
        public boolean canFit(Parcel parcel){
            return size.getLength() >= parcel.getLength()
                    && size.getWidth() >= parcel.getWidth()
                    && size.getHeight() >= parcel.getHeight();
        }
        public Parcel removeParcel(){
            Parcel result = parcel;
            parcel=null;
            return result;
        }
    }
    static class Code{
        String codeVal; long expired_at; 
        public Code(String codeVal, long expired_at){
            this.codeVal = codeVal; this.expired_at = expired_at;
        }
    }
    enum Size{
        SMALL(3,4,5,10),
        MEDIUM(6,7,8,20),
        LARGE(9,10,11,30);
        private final int length;
        private final int width;
        private final int height;
        private final int rank;
        Size( int length, int width, int height, int rank){
            this.length = length;
            this.width = width;
            this.height = height;
            this.rank = rank;
        }
        public int getLength() {
            return length;
        }

        public int getWidth() {
            return width;
        }

        public int getHeight() {
            return height;
        }
        public int getRank(){
            return rank;
        }

    }
    static class Parcel{
        private final int weight;
        private final int length;
        private final int width;
        private final int height;

        public Parcel(
                int weight,
                int length,
                int width,
                int height
        ) {
            this.weight = weight;
            this.length = length;
            this.width = width;
            this.height = height;
        }

        public int getWeight() {
            return weight;
        }

        public int getLength() {
            return length;
        }

        public int getWidth() {
            return width;
        }

        public int getHeight() {
            return height;
        }

    }
    static class User{
        List<Parcel> packList;
    }
}

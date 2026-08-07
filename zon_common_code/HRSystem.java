// Design a function for an HR application to calculate the total cost of an employee to the company.
// The total cost of an employee is composed of four distinct parts:
// Base Salary: Provided directly by the employee record.
// Tax: Calculated as a fixed percentage of the base salary.
// Benefits: A fixed cost determined by a combination of the employee's Level, and Title.
// Location Adjustment: An additional cost (or deduction) determined by the employee's Location.

// You are provided with the following business rules:
// The Benefits cost should be retrieved from a lookup table (e.g., a Map or Dictionary) 
// where the key is a Tuple (or composite object) of (Level, Title).
// The Location Adjustment cost should be retrieved from a lookup table where the key is the string Location.
// An Employee object contains at least the following attributes: baseSalary, location, title, and level

// Task:
// Define the necessary classes and data structures (e.g., Employee, BenefitsMap, LocationMap).
// Implement a function or method that takes an Employee object and returns their Total Cost.
import java.math.BigDecimal;
import java.util.*;
public class HRSystem {
    // requirement: 
    // calculate the employee cost which consist of 4 parts
    // entity
    // 1.base salary
    // 
    // 2.tax
    // 3. benefits
    // Map<> key: level+title
    // 4. location adjustment
    // Map<> key:string location
        public static class Employee {
        private final BigDecimal baseSalary;
        private final String location;
        private final String title;
        private final int level;

        public Employee(
                BigDecimal baseSalary,
                String location,
                String title,
                int level) {

            this.baseSalary = baseSalary;
            this.location = location;
            this.title = title;
            this.level = level;
        }

        public BigDecimal getBaseSalary() {
            return baseSalary;
        }

        public String getLocation() {
            return location;
        }

        public String getTitle() {
            return title;
        }

        public int getLevel() {
            return level;
        }
    }

    /*
     * Composite key for the benefits lookup table.
     *
     * Benefits are determined by both level and title,
     * so both fields must participate in equals() and hashCode().
     */
    public static class BenefitsKey {
        private final int level;
        private final String title;

        public BenefitsKey(int level, String title) {
            this.level = level;
            this.title = title;
        }

        @Override
        public boolean equals(Object object) {
            if (this == object) {
                return true;
            }

            if (!(object instanceof BenefitsKey)) {
                return false;
            }

            BenefitsKey other = (BenefitsKey) object;

            return level == other.level
                    && Objects.equals(title, other.title);
        }

        @Override
        public int hashCode() {
            return Objects.hash(level, title);
        }
    }

    private final BigDecimal taxRate;

    private final Map<BenefitsKey, BigDecimal> benefitsMap;
    private final Map<String, BigDecimal> locationAdjustmentMap;

    public HRSystem(
            BigDecimal taxRate,
            Map<BenefitsKey, BigDecimal> benefitsMap,
            Map<String, BigDecimal> locationAdjustmentMap) {

        this.taxRate = taxRate;
        this.benefitsMap = benefitsMap;
        this.locationAdjustmentMap = locationAdjustmentMap;
    }

    public BigDecimal calculateTotalCost(Employee employee) {
        if (employee == null) {
            throw new IllegalArgumentException(
                    "Employee cannot be null"
            );
        }

        BigDecimal baseSalary = employee.getBaseSalary();

        BigDecimal tax = baseSalary.multiply(taxRate);

        BenefitsKey benefitsKey = new BenefitsKey(
                employee.getLevel(),
                employee.getTitle()
        );

        BigDecimal benefits = benefitsMap.get(benefitsKey);

        if (benefits == null) {
            throw new IllegalArgumentException(
                    "No benefits configuration found"
            );
        }

        BigDecimal locationAdjustment =
                locationAdjustmentMap.get(employee.getLocation());

        if (locationAdjustment == null) {
            throw new IllegalArgumentException(
                    "No location adjustment found"
            );
        }

        return baseSalary
                .add(tax)
                .add(benefits)
                .add(locationAdjustment);
    }

    public static void main(String[] args) {
        Map<BenefitsKey, BigDecimal> benefitsMap =
                new HashMap<>();

        benefitsMap.put(
                new BenefitsKey(5, "Software Engineer"),
                new BigDecimal("20000")
        );

        benefitsMap.put(
                new BenefitsKey(6, "Software Engineer"),
                new BigDecimal("30000")
        );

        Map<String, BigDecimal> locationMap =
                new HashMap<>();

        locationMap.put(
                "Seattle",
                new BigDecimal("10000")
        );

        locationMap.put(
                "Austin",
                new BigDecimal("5000")
        );

        HRSystem calculator =
                new HRSystem(
                        new BigDecimal("0.20"),
                        benefitsMap,
                        locationMap
                );

        Employee employee = new Employee(
                new BigDecimal("100000"),
                "Seattle",
                "Software Engineer",
                5
        );

        System.out.println(
                calculator.calculateTotalCost(employee)
        ); // 150000.00
    }
}

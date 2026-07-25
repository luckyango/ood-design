import java.util.*;
import java.math.BigDecimal;
// https://leetcode.com/discuss/post/6566858/pizza-store-lld-object-oriented-design-a-kktd/
public class PizzaOrder {
    // 1. Problem Statement
    // Design a system that allows a user to calculate the price of a pizza based on its base and toppings.. The system should be simple, storing all data in memory (no persistent storage required).
    
    // A pizza has:
    // Base: A foundational element, such as a regular or thin crust.
    // Toppings: Additional ingredients (e.g., cheese, pepperoni, vegetables) that are added on top of the base.
    // Each base and topping has a specific price.
    // Prices are store-specific, so different pizza stores will have different prices for the same base or topping.
    // The system should be able to calculate the price for a full order that includes other items also, like drinks
    // The system should support various promotional deals like order a pizza and get another for free, or free drink with pizza, or most expensive topping for free.
    // 2. Requirement Analysis
    // It's always good idea to list down core requirements during interview so that you and interviewer are on same page.
    
    // Calculate the price of a pizza consisting of a base and toppings.
    // Calculate the total price of an order that can include pizzas and other items like drinks.
    // Implement various deal mechanisms such as "buy one get one free" for pizzas, free drinks with pizza orders, and free most expensive topping.
    // Support different pricing configurations for different stores.
    // 3. Intuition Behind the Design
    // When approaching a problem like this, it's helpful to break it down into smaller, manageable parts. Here's the intuitive thinking that leads to my design:
    
    // Identify the Core Entities: What are the fundamental things we are dealing with? In this case, it's pizzas, bases, toppings, drinks, stores, and orders.
    
    // Model the Relationships: How do these entities relate to each other? A pizza has a base and toppings. An order contains one or more items (pizzas, drinks). A store sells these items and has its own pricing.
    
    // Think About Actions and Responsibilities: What actions need to be performed? We need to calculate the price of a pizza, the total price of an order, and apply discounts. Who should be responsible for these actions? It is appropriate for the Pizza class to have the responsibility of computing its price by considering the cost of its base and the prices of its associated toppings. An Order should be responsible for calculating the total price of all items it contains, taking into account any deals offered by the Store. The Store is the logical place to manage prices and the available deals.
    
    // Consider Flexibility and Future Changes: How might the requirements evolve? We might want to add new types of bases, toppings, or drinks. We'll definitely want to introduce new deals in the future. How can we design the system so that these changes can be made easily without rewriting a lot of code? This is where design patterns come in handy.
    
    // Composing Items: The idea that a pizza is made of other things (base and toppings) suggests a way to treat the whole (pizza) and its parts (base, topping) in a similar way when it comes to calculating the price. This leads to the Composite Pattern. We define a common interface (OrderItem) for anything that can be in an order and have a price.
    
    // Managing Creation and Configuration: We need a central place to create the different items and configure their prices, especially since prices can vary by store. This points towards using a Factory Pattern. The Store becomes the factory responsible for creating Base, Topping, and Drink objects with the correct prices.
    
    // Handling Different Discount Logic: We have different kinds of deals (buy one get one free, free drink, etc.). Each deal has its own way of calculating the discount. We want to be able to add new deals without modifying the core ordering logic. This is a classic scenario for the Strategy Pattern. Each deal is a separate "strategy" that knows how to calculate its discount.
    
    // Simplifying Complex Object Creation: Creating a pizza involves multiple steps (choosing a base, adding toppings). To make this process cleaner and easier to read, we can use the Builder Pattern. The PizzaBuilder helps us construct a Pizza object step by step.
    
    // Key Components: With the above thoughts, we come up with these key components:
    
    // Order Items: Represent individual items in an order, such as Pizza, Base, Topping, and Drink.
    // Store: Manages the pricing configuration for different items and the available deals. It also acts as a factory for creating order items.
    // Order: Represents a customer's order, containing a list of order items and responsible for calculating the total price, including discounts.
    // Deals: Implement different discount strategies that can be applied to an order.
    // PizzaBuilder: Facilitates the creation of Pizza objects with different bases and toppings in a clear and readable manner.
    // 4. Design Patterns
    // Composite Pattern:
    
    // In this design, a Pizza is composed of a Base and a list of Toppings. All these components (Pizza, Base, Topping) can be treated as OrderItem, allowing for uniform price calculation. The Order itself is also a composite of OrderItems.
    // Factory Pattern:
    
    // The Store class acts as a factory. It encapsulates the logic for creating Base, Topping, and Drink objects with the correct prices configured for that specific store. This decouples the creation of these objects from the rest of the system. The BuildPizza() method in Store also returns a PizzaBuilder, further abstracting the pizza creation process.
    // Strategy Pattern:
    
    // The different discount mechanisms (e.g., "buy one get one free", "free drink") are implemented as separate classes that implement the Deal interface. The Order class iterates through the available deals in the Store and applies their discounts. This allows for easy addition of new deals without modifying the core ordering logic.
    // Builder Pattern:
    
    // The PizzaBuilder class provides a fluent interface for constructing Pizza objects. This is particularly useful as a pizza can have a base and multiple toppings, making the construction process multi-step. The builder ensures that a Pizza object is always created in a valid state (e.g., with a base).
    // 5. Detailed Design
    // 5.1. OrderItem Interface
    // Represents any item that can be part of an order.
    
    // Defines a contract for getting the price of an item.
    

    
    
    public interface OrderItem {
        BigDecimal getPrice();
    }
    // 5.2. Base Class
    // Represents the base of a pizza (e.g., thin crust, regular).
    
    // Implements OrderItem to have a price.
    
    // Click to expand code
    import java.math.BigDecimal;
    
    public class Base implements OrderItem {
        private String name;
        private BigDecimal price;
    
        public Base(String name, BigDecimal price) {
            this.name = name;
            this.price = price;
        }
    
        public String getName() {
            return name;
        }
    
        public BigDecimal getPrice() {
            return price;
        }
    }
    5.3. Topping Class
    Represents a pizza topping (e.g., cheese, pepperoni).
    
    Implements OrderItem to have a price.
    
    Click to expand code
    import java.math.BigDecimal;
    
    public class Topping implements OrderItem {
        private String name;
        private BigDecimal price;
    
        public Topping(String name, BigDecimal price) {
            this.name = name;
            this.price = price;
        }
    
        public String getName() {
            return name;
        }
    
        public BigDecimal getPrice() {
            return price;
        }
    }
    5.4. Drink Class
    Represents a drink item.
    
    Implements OrderItem to have a price.
    
    Click to expand code
    import java.math.BigDecimal;
    
    public class Drink implements OrderItem {
        private String name;
        private BigDecimal price;
    
        public Drink(String name, BigDecimal price) {
            this.name = name;
            this.price = price;
        }
    
        public String getName() {
            return name;
        }
    
        public BigDecimal getPrice() {
            return price;
        }
    }
    5.5. Pizza Class
    Represents a pizza, composed of a Base and a list of Toppings.
    
    Implements OrderItem to calculate its total price based on its components.
    
    Click to expand code
    import java.math.BigDecimal;
    import java.util.ArrayList;
    import java.util.List;
    
    public class Pizza implements OrderItem {
        private Base pizzaBase;
        private List<Topping> toppings;
    
        public Pizza(Base pizzaBase) {
            this.pizzaBase = pizzaBase;
            this.toppings = new ArrayList<>();
        }
    
        public List<Topping> getToppings() {
            return toppings;
        }
    
        public void addTopping(Topping topping) {
            this.toppings.add(topping);
        }
    
        @Override
        public BigDecimal getPrice() {
            BigDecimal totalPrice = pizzaBase.getPrice();
            for (Topping topping : toppings) {
                totalPrice = totalPrice.add(topping.getPrice());
            }
            return totalPrice;
        }
    }
    5.6. Deal Interface
    Defines the contract for calculating discounts on an order.
    
    Each concrete deal implementation will implement this interface.
    
    Click to expand code
    import java.math.BigDecimal;
    
    public interface Deal {
        BigDecimal calculateDiscount(Order order);
    }
    5.7. BuyOneGetOneFreePizzaDeal Class
    Implements the "buy one get one free" deal for pizzas.
    
    Applies the discount to the cheapest pizza in each pair.
    
    Click to expand code
    import java.math.BigDecimal;
    import java.util.Comparator;
    import java.util.List;
    import java.util.stream.Collectors;
    
    public class BuyOneGetOneFreePizzaDeal implements Deal {
        @Override
        public BigDecimal calculateDiscount(Order order) {
            List<Pizza> pizzas = order.getItems().stream()
                    .filter(item -> item instanceof Pizza)
                    .map(item -> (Pizza) item)
                    .sorted(Comparator.comparing(Pizza::getPrice))
                    .collect(Collectors.toList());
    
            int pairs = pizzas.size() / 2;
            BigDecimal discount = BigDecimal.ZERO;
            for (int i = 0; i < pairs; i++) {
                discount = discount.add(pizzas.get(i * 2).getPrice());
            }
            System.out.println("Discount for BuyOneGetOneFreePizzaDeal : " + discount);
            return discount;
        }
    }
    5.8. FreeDrinkWithPizzaDeal Class
    Implements a deal where the cheapest drink is free if a pizza is ordered.
    
    Click to expand code
    import java.math.BigDecimal;
    import java.util.Comparator;
    import java.util.List;
    import java.util.Optional;
    
    public class FreeDrinkWithPizzaDeal implements Deal {
        @Override
        public BigDecimal calculateDiscount(Order order) {
            boolean hasPizza = order.getItems().stream().anyMatch(item -> item instanceof Pizza);
            if (!hasPizza) {
                return BigDecimal.ZERO;
            }
    
            Optional<Drink> cheapestDrink = order.getItems().stream()
                    .filter(item -> item instanceof Drink)
                    .map(item -> (Drink) item)
                    .min(Comparator.comparing(Drink::getPrice));
    
            BigDecimal discount = cheapestDrink.map(Drink::getPrice).orElse(BigDecimal.ZERO);
            System.out.println("Discount for FreeDrinkWithPizzaDeal : " + discount);
            return discount;
        }
    }
    5.9. MostExpensiveToppingFreeDeal Class
    Implements a deal where the most expensive topping on each pizza is free.
    
    Click to expand code
    import java.math.BigDecimal;
    import java.util.Comparator;
    import java.util.List;
    
    public class MostExpensiveToppingFreeDeal implements Deal {
        @Override
        public BigDecimal calculateDiscount(Order order) {
            BigDecimal totalDiscount = BigDecimal.ZERO;
            for (OrderItem item : order.getItems()) {
                if (item instanceof Pizza) {
                    Pizza pizza = (Pizza) item;
                    if (!pizza.getToppings().isEmpty()) {
                        Topping mostExpensiveTopping = pizza.getToppings().stream()
                                .max(Comparator.comparing(Topping::getPrice))
                                .orElse(null);
                        if (mostExpensiveTopping != null) {
                            totalDiscount = totalDiscount.add(mostExpensiveTopping.getPrice());
                        }
                    }
                }
            }
            System.out.println("Discount for MostExpensiveToppingFreeDeal : " + totalDiscount);
            return totalDiscount;
        }
    }
    5.10. Store Class
    Acts as a factory for creating Base, Topping, and Drink objects with prices specific to the store.
    
    Manages the list of available deals for the store.
    
    Provides a PizzaBuilder to construct Pizza objects.
    
    Click to expand code
    import java.math.BigDecimal;
    import java.util.ArrayList;
    import java.util.HashMap;
    import java.util.List;
    import java.util.Map;
    
    public class Store {
        private String name;
        private Map<String, BigDecimal> basePrices;
        private Map<String, BigDecimal> toppingPrices;
        private Map<String, BigDecimal> drinkPrices;
        private List<Deal> deals;
    
        public Store(String name) {
            this.name = name;
            this.basePrices = new HashMap<>();
            this.toppingPrices = new HashMap<>();
            this.drinkPrices = new HashMap<>();
            this.deals = new ArrayList<>();
        }
    
        public String getName() {
            return name;
        }
    
        public void addBasePrice(String name, BigDecimal price) {
            this.basePrices.put(name, price);
        }
    
        public void addToppingPrice(String name, BigDecimal price) {
            this.toppingPrices.put(name, price);
        }
    
        public void addDrinkPrice(String name, BigDecimal price) {
            this.drinkPrices.put(name, price);
        }
    
        public void addDeal(Deal deal) {
            this.deals.add(deal);
        }
    
        public Base createBase(String name) {
            if (!basePrices.containsKey(name)) {
                throw new IllegalArgumentException("Base '" + name + "' not found in store price list.");
            }
            return new Base(name, basePrices.get(name));
        }
    
        public Topping createTopping(String name) {
            if (!toppingPrices.containsKey(name)) {
                throw new IllegalArgumentException("Topping '" + name + "' not found in store price list.");
            }
            return new Topping(name, toppingPrices.get(name));
        }
    
        public Drink createDrink(String name) {
            if (!drinkPrices.containsKey(name)) {
                throw new IllegalArgumentException("Drink '" + name + "' not found in store price list.");
            }
            return new Drink(name, drinkPrices.get(name));
        }
    
        public List<Deal> getDeals() {
            return Collections.unmodifiableList(this.deals);
        }
    
        public PizzaBuilder buildPizza() {
            return new PizzaBuilder(this);
        }
    }
    5.11. PizzaBuilder Class
    Provides a builder pattern implementation for creating Pizza objects with a fluent interface.
    
    Click to expand code
    public class PizzaBuilder {
        private Store store;
        private Base base;
        private List<Topping> toppings;
    
        public PizzaBuilder(Store store) {
            this.store = store;
            this.toppings = new ArrayList<>();
        }
    
        public PizzaBuilder withBase(String baseName) {
            this.base = store.createBase(baseName);
            return this;
        }
    
        public PizzaBuilder addTopping(String toppingName) {
            this.toppings.add(store.createTopping(toppingName));
            return this;
        }
    
        public Pizza build() {
            if (base == null) {
                throw new IllegalStateException("Pizza base must be selected");
            }
            Pizza pizza = new Pizza(base);
            for (Topping topping : toppings) {
                pizza.addTopping(topping);
            }
            return pizza;
        }
    }
    5.12. Order Class
    Represents a customer's order, containing a list of OrderItems.
    
    Calculates the total price of the order by summing the prices of all items and subtracting the discounts applied by the store's deals.
    
    Click to expand code
    import java.math.BigDecimal;
    import java.util.ArrayList;
    import java.util.List;
    
    public class Order {
        private Store store;
        private List<OrderItem> items;
    
        public Order(Store store) {
            this.store = store;
            this.items = new ArrayList<>();
        }
    
        public List<OrderItem> getItems() {
            return Collections.unmodifiableList(items);
        }
    
        public void addItem(OrderItem item) {
            this.items.add(item);
        }
    
        public BigDecimal calculateTotal() {
            BigDecimal subTotal = BigDecimal.ZERO;
            for (OrderItem item : items) {
                subTotal = subTotal.add(item.getPrice());
            }
    
            BigDecimal discounts = BigDecimal.ZERO;
            for (Deal deal : store.getDeals()) {
                discounts = discounts.add(deal.calculateDiscount(this));
            }
    
            System.out.println("-------------------------");
            System.out.println("SubTotal : " + subTotal);
            System.out.println("Discounts : " + discounts);
            return subTotal.subtract(discounts);
        }
    }
    </details> 
    5.13. Driver class
    The driver code, which will be entry point of the application.
    
    Configures a Store, creates pizza and drink items, adds them to an Order, and calculates the total price.
    
    Click to expand code
    import java.math.BigDecimal;
    
    public class Main {
        public static void main(String[] args) {
            // Configure store
            Store londonStore = new Store("London");
            londonStore.addBasePrice("Thin Crust", new BigDecimal("5.0"));
            londonStore.addToppingPrice("Cheese", new BigDecimal("1.0"));
            londonStore.addToppingPrice("Pepperoni", new BigDecimal("2.0"));
            londonStore.addDrinkPrice("Cola", new BigDecimal("3.0"));
    
            londonStore.addDeal(new BuyOneGetOneFreePizzaDeal());
            londonStore.addDeal(new FreeDrinkWithPizzaDeal());
            londonStore.addDeal(new MostExpensiveToppingFreeDeal());
    
            // Create pizzas
            Pizza thinCrustCheesePepperoniPizza = londonStore.buildPizza()
                    .withBase("Thin Crust")
                    .addTopping("Cheese")
                    .addTopping("Pepperoni")
                    .build();
    
            Pizza thinCrustCheesePizza = londonStore.buildPizza()
                    .withBase("Thin Crust")
                    .addTopping("Cheese")
                    .build();
    
            // Create Sides
            Drink cola = londonStore.createDrink("Cola");
    
            // Create order
            Order order = new Order(londonStore);
            order.addItem(thinCrustCheesePepperoniPizza);
            order.addItem(thinCrustCheesePizza);
            order.addItem(cola);
    
            System.out.println("-------------------------");
            System.out.println("Total Price: " + order.calculateTotal());
        }
    }
    // 6. Design Choices and Trade-offs
    // Q: Why not separate classes for each base/topping with factory pattern?
    // A: While the Factory Pattern is indeed present in the Store class, creating individual classes for each base/topping type would lead to:
    
    // Class Explosion: With potentially dozens of toppings/bases, we'd get hundreds of trivial classes without unique behavior
    // Maintenance Overhead: Price changes would require code modifications instead of simple configuration
    // Violation of YAGNI: No current requirement for type-specific behavior that would justify separate classes
    // The current design achieves flexibility through composition rather than inheritance. The Store factory encapsulates creation logic while maintaining runtime configurability - different stores can have different prices for the same named items without code changes.
    
    // Q: Why not use Decorator Pattern for toppings?
    // A: It wasn't the best fit here because:
    
    // Fixed Price Additions: Our toppings simply add fixed amounts to the price. Decorator shines when responsibilities need dynamic composition with potential behavior modifications
    // Multiple Toppings Handling: A decorator chain would become complex with unlimited toppings (Pizza > Cheese > Pepperoni > Mushrooms...)
    // Ordering Challenges: Decorators make it harder to implement deals like "free most expensive topping" which requires comparing all toppings
    // However, the design is open for potential decorator adoption:
    
    // Toppings are already isolated as individual entities
    // Price calculation is centralized in Pizza class, which could be replaced with decorator logic
    // If we needed features like percentage-based toppings or conditional pricing, we could transition to decorators without breaking existing clients
    // Q:How are you avoiding multiple deals getting applied on same item?
    // A: This design does not avoid that, just to keep logic simple. Otherwise we will have to track which items from list are used or not
    
    // Follow-up Considerations:
    
    // If you need to prevent multiple deals from applying to the same item, you could consider the following approaches:
    
    // Deal Prioritization: Assign priorities to different deals. When calculating discounts, apply deals based on their priority and potentially mark items as "discounted" to prevent further discounts from being applied to them.
    // Deal Applicability Rules: Define rules for each deal specifying which items it can be applied to and under what conditions. The discount calculation logic would then need to check these rules before applying a discount.
    // Single Best Deal: Instead of applying all applicable deals, you could implement logic to find the single best deal for the order or for specific items within the order. This would involve evaluating the discount offered by each deal and selecting the one that provides the maximum benefit to the customer, perhaps Chain of Responsibility design pattern?
    // The best approach would depend on the specific business requirements and how you want to handle overlapping discounts.
    
    // 7. Closing Thoughts
    // I have aimed to create a clean and understandable design solution, clearly outlining the reasoning behind the chosen approach. I recognize that software design is often a matter of perspective, and there can be multiple valid solutions. I welcome your feedback, questions, and suggestions for improvement. I will try to answer follow up questions in comments, and will also edit my Design Choices and Trade-offs section if needed.
}
import java.lang.invoke.ClassSpecializer.Factory;
import java.lang.module.ModuleDescriptor.Provides;
import java.math.BigDecimal;

public interface OrderItem {
    BigDecimal getPrice();
}
5.2. Base Class
Represents the base of a pizza (e.g., thin crust, regular).

Implements OrderItem to have a price.

Click to expand code
import java.math.BigDecimal;

public class Base implements OrderItem {
    private String name;
    private BigDecimal price;

    public Base(String name, BigDecimal price) {
        this.name = name;
        this.price = price;
    }

    public String getName() {
        return name;
    }

    public BigDecimal getPrice() {
        return price;
    }
}
5.3. Topping Class
Represents a pizza topping (e.g., cheese, pepperoni).

Implements OrderItem to have a price.

Click to expand code
import java.math.BigDecimal;

public class Topping implements OrderItem {
    private String name;
    private BigDecimal price;

    public Topping(String name, BigDecimal price) {
        this.name = name;
        this.price = price;
    }

    public String getName() {
        return name;
    }

    public BigDecimal getPrice() {
        return price;
    }
}
5.4. Drink Class
Represents a drink item.

Implements OrderItem to have a price.

Click to expand code
import java.math.BigDecimal;

public class Drink implements OrderItem {
    private String name;
    private BigDecimal price;

    public Drink(String name, BigDecimal price) {
        this.name = name;
        this.price = price;
    }

    public String getName() {
        return name;
    }

    public BigDecimal getPrice() {
        return price;
    }
}
5.5. Pizza Class
Represents a pizza, composed of a Base and a list of Toppings.

Implements OrderItem to calculate its total price based on its components.

Click to expand code
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
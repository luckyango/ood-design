import java.math.BigDecimal;
import java.util.*;

public class PizzaOrder {

    interface Deal {
        BigDecimal getDiscount(Order order);
    }

    public static class FreeDrinkDeal implements Deal {
        @Override
        public BigDecimal getDiscount(Order order) {
            return order.getDrinks().stream()
                    .map(Drink::getPrice)
                    .min(BigDecimal::compareTo)
                    .orElse(BigDecimal.ZERO);
        }
    }

    public static class BuyOneGetOnePizzaDeal implements Deal {
        @Override
        public BigDecimal getDiscount(Order order) {
            List<Pizza> sorted =
                    new ArrayList<>(order.getPizzas());

            sorted.sort(
                    Comparator.comparing(Pizza::getPrice)
                            .reversed()
            );

            BigDecimal discount = BigDecimal.ZERO;

            for (int i = 0; i < sorted.size()/2; i ++) {
                discount =
                        discount.add(sorted.get(i).getPrice());
            }

            return discount;
        }
    }

    public static class PizzaShop {
        private final Map<String, Base> bases =
                new HashMap<>();

        private final Map<String, Topping> toppings =
                new HashMap<>();

        private final List<Deal> deals =
                new ArrayList<>();

        public void addBase(Base base) {
            bases.put(base.getId(), base);
        }

        public void removeBase(String baseId) {
            bases.remove(baseId);
        }

        public void addTopping(Topping topping) {
            toppings.put(topping.getId(), topping);
        }

        public void removeTopping(String toppingId) {
            toppings.remove(toppingId);
        }

        public void addDeal(Deal deal) {
            deals.add(deal);
        }

        public BigDecimal getFinalPrice(Order order) {
            BigDecimal subtotal = order.getPrice();
            BigDecimal discount = BigDecimal.ZERO;

            for (Deal deal : deals) {
                discount =
                        discount.add(deal.getDiscount(order));
            }

            return subtotal.subtract(discount)
                    .max(BigDecimal.ZERO);
        }
    }

    public static class Order {
        private final List<Pizza> pizzas =
                new ArrayList<>();

        private final List<Drink> drinks =
                new ArrayList<>();

        public void addPizza(Pizza pizza) {
            pizzas.add(pizza);
        }

        public void addDrink(Drink drink) {
            drinks.add(drink);
        }

        public List<Pizza> getPizzas() {
            return Collections.unmodifiableList(pizzas);
        }

        public List<Drink> getDrinks() {
            return Collections.unmodifiableList(drinks);
        }

        public BigDecimal getPrice() {
            BigDecimal total = BigDecimal.ZERO;

            for (Pizza pizza : pizzas) {
                total = total.add(pizza.getPrice());
            }

            for (Drink drink : drinks) {
                total = total.add(drink.getPrice());
            }

            return total;
        }
    }

    public static class Pizza {
        private final BigDecimal basePriceAtOrder;
        private final List<BigDecimal> toppingPricesAtOrder;

        public Pizza(Base base, List<Topping> toppings) {
            if (!base.isAvailable()) {
                throw new IllegalArgumentException(
                        "Base unavailable"
                );
            }

            this.basePriceAtOrder = base.getPrice();
            this.toppingPricesAtOrder = new ArrayList<>();

            for (Topping topping : toppings) {
                if (!topping.isAvailable()) {
                    throw new IllegalArgumentException(
                            "Topping unavailable"
                    );
                }

                toppingPricesAtOrder.add(
                        topping.getPrice()
                );
            }
        }

        public BigDecimal getPrice() {
            BigDecimal total = basePriceAtOrder;

            for (BigDecimal price : toppingPricesAtOrder) {
                total = total.add(price);
            }

            return total;
        }
    }

    public static class Base {
        private final String id;
        private final String name;
        private BigDecimal price;
        private final BaseType baseType;
        private int stock;

        public Base(
                String id,
                String name,
                BigDecimal price,
                BaseType baseType,
                int stock
        ) {
            this.id = id;
            this.name = name;
            this.price = price;
            this.baseType = baseType;
            this.stock = stock;
        }

        public String getId() {
            return id;
        }

        public BigDecimal getPrice() {
            return price;
        }

        public boolean isAvailable() {
            return stock > 0;
        }
    }

    public static class Topping {
        private final String id;
        private final String name;
        private BigDecimal price;
        private final ToppingType toppingType;
        private int stock;

        public Topping(
                String id,
                String name,
                BigDecimal price,
                ToppingType toppingType,
                int stock
        ) {
            this.id = id;
            this.name = name;
            this.price = price;
            this.toppingType = toppingType;
            this.stock = stock;
        }

        public String getId() {
            return id;
        }

        public BigDecimal getPrice() {
            return price;
        }

        public boolean isAvailable() {
            return stock > 0;
        }
    }

    public static class Drink {
        private final String name;
        private final BigDecimal price;
        private final DrinkType drinkType;

        public Drink(
                String name,
                BigDecimal price,
                DrinkType drinkType
        ) {
            this.name = name;
            this.price = price;
            this.drinkType = drinkType;
        }

        public BigDecimal getPrice() {
            return price;
        }
    }

    enum BaseType {
        THIN_CRUST,
        HAND_TOSSED,
        DEEP_DISH
    }

    enum ToppingType {
        MEAT,
        VEGETABLE
    }

    enum DrinkType {
        COKE,
        COFFEE,
        ORANGE_JUICE
    }
}
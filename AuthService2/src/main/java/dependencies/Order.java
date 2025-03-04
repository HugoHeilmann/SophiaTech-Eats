package dependencies;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import dependencies.databse.RestaurantDatabase;
import dependencies.location.Location;
import exceptions.CustomerNotFoundException;
import exceptions.RestaurantNotFoundException;

import java.time.Duration;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

public class Order {

    public static final int AVG_DELIVERY_TIME = 15;

    /* Attributes */

    private final LocalTime delivery;
    private LocalTime orderDate;
    private Location location;
    private double price;
    private final List<Menu> menus;
    private final int restaurantId;
    private final int customerId;
    private double preparationTime;

    /* Construction */
    @JsonCreator
    public Order(
            @JsonProperty("delivery") LocalTime delivery,
            @JsonProperty("orderDate") LocalTime orderDate,
            @JsonProperty("location") Location location,
            @JsonProperty("price") double price,
            @JsonProperty("menus") List<Menu> menus,
            @JsonProperty("restaurantId") int restaurantId,
            @JsonProperty("customerId") int customerId,
            @JsonProperty("preparationTime") double preparationTime) {
        this.delivery = delivery;
        this.orderDate = orderDate;
        this.location = location;
        this.price = price;
        this.menus = menus;
        this.restaurantId = restaurantId;
        this.customerId = customerId;
        this.preparationTime = preparationTime;
    }

    /* Construction : Joshua Bloch’s Builder design pattern  */


    public Order(Builder builder) {
        this.orderDate = builder.getOrderDate();
        this.delivery = builder.getDelivery();
        this.location = builder.getLocation();
        this.price = builder.getPrice();
        this.menus = builder.getMenus();
        this.restaurantId = builder.getRestaurantId();
        this.customerId = builder.getCustomerId();
        this.preparationTime = builder.getPreparationTime();

    }


    public static class Builder {
        private LocalTime delivery;
        private LocalTime orderDate;
        private LocalTime possibleDelivery;
        private Location location;
        private double price;
        private List<Menu> menus = new ArrayList<>();
        private int restaurantId;
        private int customerId;
        private double preparationTime;


        public Builder(LocalTime delivery, LocalTime orderDate, Location location, double price, List<Menu> menus, int restaurantId, int customerId) {
            this.delivery = delivery;
            this.orderDate = orderDate;
            this.possibleDelivery = LocalTime.now();
            this.location = location;
            this.price = price;
            this.menus = menus;
            this.restaurantId = restaurantId;
            this.customerId = customerId;
            this.preparationTime = 0;
        }

        @JsonCreator
        public static Builder create(
                @JsonProperty("delivery") LocalTime delivery,
                @JsonProperty("orderDate") LocalTime orderDate,
                @JsonProperty("possibleDelivery") LocalTime possibleDelivery,
                @JsonProperty("location") Location location,
                @JsonProperty("customerId") int customerId,
                @JsonProperty("price") double price,
                @JsonProperty("menus") List<Menu> menus,
                @JsonProperty("restaurant") int restaurantId,
                @JsonProperty("preparationTime") double preparationTime) {

            Builder builder = new Builder(delivery, orderDate, location, price, menus, restaurantId, customerId);
            builder.possibleDelivery = possibleDelivery != null ? possibleDelivery : LocalTime.now();
            builder.preparationTime = preparationTime;
            return builder;
        }


        public Builder(int restaurantId, int customerId) {
            this(null, null, null, 0, new ArrayList<>(), restaurantId, customerId);
        }

        public Builder delivery(LocalTime delivery) {
            this.delivery = delivery;
            return this;
        }

        public Builder location(Location location) {
            this.location = location;
            return this;
        }

        private void updateDeliveryDate(Menu menu) {
            // get the preparation time of the menu in seconds
            double preparationTime = menu.getPreparation();
            if (possibleDelivery == null) {
                possibleDelivery = LocalTime.now().plusMinutes(AVG_DELIVERY_TIME);
            }
            possibleDelivery = possibleDelivery.plusSeconds((long) preparationTime);
        }

        public Builder addMenu(Menu menu) {
            if (delivery == null) {
                updateDeliveryDate(menu);
            }
            this.preparationTime += menu.getPreparation();
            this.menus.add(menu);
            this.price += menu.getPrice();
            return this;
        }

        public Builder orderDate(LocalTime orderDate) {
            this.orderDate = orderDate;
            return this;
        }

        private Builder applyDiscount(double discount) {
            this.price *= (1. - discount);
            return this;
        }

        public Order build() throws RestaurantNotFoundException, CustomerNotFoundException {
            double discount = 0;
            Restaurant restaurant = RestaurantDatabase.getInstance().getRestaurantById(this.restaurantId);
            if (restaurant.getStrategy() != null) {
                discount = restaurant.getStrategy().availableDiscount(this);
            }
            if (delivery == null) {

                //it means that the order is being created and the delivery date is updated
                long diffInSeconds = Duration.between(orderDate, LocalTime.now()).getSeconds();
                delivery = possibleDelivery.plusSeconds(diffInSeconds);
            }
            if (10 <= menus.size()) {
                discount = Math.max(discount, 0.05);
            }
            return new Order(this.applyDiscount(discount));
        }

        public int getRestaurantId() {
            return restaurantId;
        }

        public int getCustomerId() {
            return customerId;
        }

        public List<Menu> getMenus() {
            return menus;
        }

        public double getPrice() {
            return price;
        }

        public Location getLocation() {
            return location;
        }

        public LocalTime getDelivery() {
            return delivery;
        }

        public LocalTime getOrderDate() {
            return orderDate;
        }

        public LocalTime getPossibleDelivery() {
            return possibleDelivery;
        }

        public double getPreparationTime() {
            return preparationTime;
        }

        /* Data : used by the Facade design pattern */

        public record Data(LocalTime delivery, LocalTime orderDate, Location location, double price, Menu.Data[] menus,
                           int restaurantId, int customerId) {

            public Builder toOrderBuilder() {
                List<Menu> menus = new ArrayList<>();

                for (int i = 0; i < menus().length; i++) {
                    menus.add(this.menus[i].toMenu());
                }

                return new Builder(this.delivery, this.orderDate, this.location, this.price, menus, this.restaurantId, this.customerId);
            }

        }


        public Data toData() {
            Menu.Data[] menus = new Menu.Data[this.menus.size()];

            for (int i = 0; i < this.menus.size(); i++) {
                menus[i] = this.menus.get(i).toData();
            }

            return new Data(this.delivery, this.orderDate, this.location, this.price, menus, this.restaurantId, this.customerId);
        }
    }

    /* Data : used by the Facade design pattern */

    public record Data(LocalTime delivery, LocalTime orderDate, Location location, double price,
                       Menu.Data[] menus,
                       int restaurantId, int customerId, double preparationTime) {

        public Order toOrder() {
            List<Menu> menus = new ArrayList<>();

            for (int i = 0; i < menus().length; i++) {
                menus.add(this.menus[i].toMenu());
            }

            return new Order(this.delivery, this.orderDate, this.location, this.price, menus, this.restaurantId, this.customerId, this.preparationTime);
        }

    }


    public Data toData() {
        Menu.Data[] menus = new Menu.Data[this.menus.size()];

        for (int i = 0; i < this.menus.size(); i++) {
            menus[i] = this.menus.get(i).toData();
        }

        return new Data(this.delivery, this.orderDate, this.location, this.price, menus, this.restaurantId, this.customerId, this.preparationTime);
    }

    public LocalTime getDelivery() {
        return delivery;
    }

    public Location getLocation() {
        return location;
    }

    public double getPrice() {
        return price;
    }

    public List<Menu> getMenus() {
        return menus;
    }

    public int getRestaurantId() {
        return restaurantId;
    }

    public int getCustomerId() {
        return customerId;
    }

    public LocalTime getOrderDate() {
        return orderDate;
    }
}
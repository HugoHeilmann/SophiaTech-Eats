package backend;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

public class Order {

    public static final int AVG_DELIVERY_TIME = 15;

    /* Attributes */

    private final LocalTime delivery;
    private LocalTime orderDate;
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
            @JsonProperty("price") double price,
            @JsonProperty("menus") List<Menu> menus,
            @JsonProperty("restaurantId") int restaurantId,
            @JsonProperty("customerId") int customerId,
            @JsonProperty("preparationTime") double preparationTime) {
        this.delivery = delivery;
        this.orderDate = orderDate;
        this.price = price;
        this.menus = menus;
        this.restaurantId = restaurantId;
        this.customerId = customerId;
        this.preparationTime = preparationTime;
    }

    /* Construction : Joshua Bloch’s Builder design pattern  */


    public static class Builder {
        private LocalTime delivery;
        private LocalTime orderDate;
        private LocalTime possibleDelivery;
        private double price;
        private List<Menu> menus = new ArrayList<>();
        private int restaurantId;
        private int customerId;
        private double preparationTime;


        public Builder(LocalTime delivery, LocalTime orderDate, double price, List<Menu> menus, int restaurantId, int customerId) {
            this.delivery = delivery;
            this.orderDate = orderDate;
            this.possibleDelivery = LocalTime.now();
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
                @JsonProperty("customerId") int customerId,
                @JsonProperty("price") double price,
                @JsonProperty("menus") List<Menu> menus,
                @JsonProperty("restaurant") int restaurantId,
                @JsonProperty("preparationTime") double preparationTime) {

            Builder builder = new Builder(delivery, orderDate, price, menus, restaurantId, customerId);
            builder.possibleDelivery = possibleDelivery != null ? possibleDelivery : LocalTime.now();
            builder.preparationTime = preparationTime;
            return builder;
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
    }
}
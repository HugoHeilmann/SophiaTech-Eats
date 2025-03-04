package backend.customer;

import backend.Location;
import backend.Order;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.ArrayList;


public class Customer {

    private static int ID = 0;

    public static void resetId() {
        ID = 0;
    }

    /* Attributes */

    private int id;
    private String name;
    private float balance;
    private CustomerStatus status;
    private Order.Builder basket;
    private ArrayList<Order> history;
    private ArrayList<Location> locations;

    public Customer(String name, float balance, CustomerStatus status) {
        this.id = ID++;
        this.name = name;
        this.balance = balance;
        this.status = status;
        this.history = new ArrayList<>();
        this.locations = new ArrayList<>();
    }

    @JsonCreator
    public Customer(@JsonProperty("id") int id,
                    @JsonProperty("name") String name,
                    @JsonProperty("status") CustomerStatus status) {
        this.id = id;
        this.name = name;
        this.status = status;
    }

    public String getName() {
        return name;
    }

    public int getId() {
        return id;
    }

    public CustomerStatus getStatus() {
        return status;
    }

    public Order.Builder getBasket() {
        return basket;
    }


    public int getNumberOfOrderIn(int restaurantId) {
        /*
        int count = 0;
        for (Order order : history) {
            if (order.getRestaurantId() == restaurantId) {
                count++;
            }
        }
        return count;
         */
        return 0;
    }
}
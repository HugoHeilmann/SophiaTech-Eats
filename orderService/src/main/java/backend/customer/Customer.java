package backend.customer;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import backend.Group;
import backend.Order;
import backend.database.CustomerDatabase;
import backend.database.GroupDatabase;
import backend.exceptions.CustomerNotFoundException;
import backend.exceptions.GroupNotFoundException;
import backend.location.Location;

import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

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
    private List<Order> history;
    private List<Location> locations;
    private String email;
    private String password;

    public Customer(String name, float balance, CustomerStatus status, String email, String password) {
        this.id = ID++;
        this.name = name;
        this.balance = balance;
        this.status = status;
        this.history = new ArrayList<>();
        this.locations = new ArrayList<>();
        this.email = email;
        this.password = password;

    }

    public Customer(String name, String email, String password) {
        this.id = ID++;
        this.name = name;
        this.balance = 0;
        this.status = CustomerStatus.REGULAR;
        this.history = new ArrayList<>();
        this.locations = new ArrayList<>();
        this.email = email;
        this.password = password;
    }

    @JsonCreator
    public Customer(@JsonProperty("id") int id,
                    @JsonProperty("name") String name,
                    @JsonProperty("balance") float balance,
                    @JsonProperty("status") CustomerStatus status,
                    @JsonProperty("locations") List<Location> locations,
                    @JsonProperty("email") String email,
                    @JsonProperty("password") String password,
                    @JsonProperty("basket") Order.Builder basket,
                    @JsonProperty("history") List<Order> history) {

        this.id = id;
        this.name = name;
        this.balance = balance;
        this.status = status;
        this.basket = basket;
        this.history = history;
        this.history = new ArrayList<>();
        this.locations = locations;
        this.email = email;
        this.password = password;

    }

    public String getName() {
        return name;
    }

    public int getId() {
        return id;
    }


    public float getBalance() {
        return balance;
    }

    public CustomerStatus getStatus() {
        return status;
    }

    public Order.Builder getBasket() {
        return basket;
    }

    public List<Location> getLocations() {
        return locations;
    }

    public List<Order> getHistory() {
        return history;
    }

    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }

    public void setBasket(Order.Builder basket) {
        this.basket = basket;
    }

    public void addOrderToHistory(Order order) {
        history.add(order);
    }

    public void addLocation(Location location) {
        locations.add(location);
    }

    public void deleteLocation(int locationIndex) {
        locations.remove(locationIndex);
    }

    public int getNumberOfOrderIn(int restaurantId) {
        int count = 0;
        for (Order order : history) {
            if (order.getRestaurantId() == restaurantId) {
                count++;
            }
        }
        return count;
    }

    public Group createGroup(LocalTime delivery, Location location) throws CustomerNotFoundException {
        Group group = new Group(delivery, location, this);
        GroupDatabase.getInstance().addGroup(group);
        return group;
    }

    public void joinGroup(Group group) throws CustomerNotFoundException, GroupNotFoundException {
        group.addMember(this);
    }

    /**
     * Data : used by the Facade design pattern
     */
    public record Data(int id, String name, float balance, CustomerStatus status, List<Location> locations) {

        public Customer toCustomer() throws CustomerNotFoundException {
            return CustomerDatabase.getInstance().getCustomerById(this.id);
        }

    }

    public Data toData() {
        return new Data(this.id, this.name, this.balance, this.status, this.locations);
    }
}
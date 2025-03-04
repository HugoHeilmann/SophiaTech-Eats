package backend.facade;

import backend.*;
import backend.customer.Customer;
import backend.database.CustomerDatabase;
import backend.database.GroupDatabase;
import backend.database.RestaurantDatabase;
import backend.exceptions.CustomerNotFoundException;
import backend.exceptions.GroupNotFoundException;
import backend.exceptions.RestaurantNotFoundException;
import backend.location.Location;
import backend.location.LocationFactory;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.time.LocalTime;

public final class OrderFacade {

    /* Singleton design pattern */

    private static OrderFacade instance;

    public static OrderFacade getInstance() {
        if (instance == null) {
            instance = new OrderFacade();
        }
        return instance;
    }


    private OrderFacade() {
    }


    /* Facade design pattern */


    /**
     * Usage scenarios considered :
     * <p>
     * [O2] A registered user can create a group order; in this case, she specifies the delivery location,
     * which can no longer be modified. If she specifies the delivery time, it can no longer be modified.
     * In return, she obtains a group order identifier. She broadcasts at his convenience to those
     * around her; the system does not support broadcasting.
     *
     * @param delivery a delivery date
     * @param location the location of the group command
     * @return the group after changes occurs
     */
    public Group.Data createGroup(LocalTime delivery, Location location, int customerId) throws CustomerNotFoundException {
        Customer customer = CustomerDatabase.getInstance().getCustomerById(customerId);
        Group group = customer.createGroup(delivery, location);
        return group.toData();
    }

    /**
     * usage scenarios considered :
     * <p>
     * [O2] When a user join a group, the delivery location and the delivery time of the group order are set.
     *
     * @param groupId    a group id
     * @param customerId a customer's id
     * @return the order builder after setting the location
     */
    public Order.Builder.Data setOrderGroup(int groupId, int customerId) throws CustomerNotFoundException, GroupNotFoundException {
        Customer customer = CustomerDatabase.getInstance().getCustomerById(customerId);
        Order.Builder orderBuilder = customer.getBasket();
        orderBuilder.location(GroupDatabase.getInstance().getGroupById(groupId).getLocation());
        orderBuilder.delivery(GroupDatabase.getInstance().getGroupById(groupId).getDelivery());
        return orderBuilder.toData();
    }


    public boolean joinGroup(int groupId, int customerId) throws CustomerNotFoundException, GroupNotFoundException {
        Customer customer = CustomerDatabase.getInstance().getCustomerById(customerId);
        Group group = GroupDatabase.getInstance().getGroupById(groupId);
        customer.joinGroup(group);
        return true;
    }


    /**
     * Usage scenarios considered:
     * <p>
     * [O6] The creator of the group order or one of the members who placed an order within the
     * group order can validate and thus close the group order. If no delivery time has been set, the
     * user must select a delivery time compatible with all sub-orders before validating it.
     *
     * @param groupId a group id
     * @return the status of the closure
     */
    public boolean closeGroup(int groupId, int customerId, LocalTime time) throws GroupNotFoundException {
        return Group.close(groupId, customerId, time);
    }


    /**
     * Usage scenarios considered :
     * <p>
     * [O1] A registered user can place orders for menu items from restaurants located on the campus.
     * <p>
     * [O3] (1st part) To create an order, the registered user selects a restaurant among pre-recorded restaurants.
     * <p>
     * [EX2] Implement a control that prohibits having more than n orders in progress in the same time slot for
     * a restaurant, depending on the average preparation time for an order in this restaurant.
     *
     * @param restaurantId a restaurant's id
     * @param customerId   a customer's id
     * @return the order builder after initialization
     */
    public Order.Builder.Data initOrder(int restaurantId, int customerId) throws RestaurantNotFoundException, CustomerNotFoundException {
        Restaurant restaurant = RestaurantDatabase.getInstance().getRestaurantById(restaurantId);
        Customer customer = CustomerDatabase.getInstance().getCustomerById(customerId);
        Order.Builder orderBuilder = new Order.Builder(restaurantId, customer.getId()).orderDate(LocalTime.now());
        customer.setBasket(orderBuilder);
        restaurant.decreaseOrderChargeable();
        CustomerDatabase.getInstance().saveCustomers();
        return orderBuilder.toData();
    }

    public boolean registerLocation(String location, int customerId) throws CustomerNotFoundException {
        Customer customer = CustomerDatabase.getInstance().getCustomerById(customerId);
        customer.addLocation(LocationFactory.createLocation(location));
        CustomerDatabase.getInstance().saveCustomers();
        return true;
    }

    public boolean deleteLocation(int locationIndex, int customerId) throws CustomerNotFoundException {
        Customer customer = CustomerDatabase.getInstance().getCustomerById(customerId);
        customer.deleteLocation(locationIndex);
        CustomerDatabase.getInstance().saveCustomers();
        return true;
    }

    /**
     * Usage scenarios considered :
     * <p>
     * [O3] (2nd part) If it is not a group order, she selects a pre-recorded delivery location.
     *
     * @param locationIndex a location's index
     * @param customerId    a customer's id
     * @return the order builder after setting the location
     */
    public Order.Builder.Data setOrderLocation(int locationIndex, int customerId) throws CustomerNotFoundException {
        Customer customer = CustomerDatabase.getInstance().getCustomerById(customerId);
        Order.Builder orderBuilder = customer.getBasket();
        orderBuilder.location(customer.getLocations().get(locationIndex));
        CustomerDatabase.getInstance().saveCustomers();
        return orderBuilder.toData();
    }


    /**
     * Usage scenarios considered :
     * <p>
     * [O3] (3rd part) If it is not a group order, she  chooses a delivery date within
     * the restaurant's preparation capabilities.
     *
     * @param delivery   a delivery date
     * @param customerId a customer's id
     * @return the order builder after setting the delivery date
     */
    public Order.Builder.Data setDeliveryDate(LocalTime delivery, int customerId) throws CustomerNotFoundException {
        Customer customer = CustomerDatabase.getInstance().getCustomerById(customerId);
        Order.Builder orderBuilder = customer.getBasket();
        orderBuilder.delivery(delivery);
        CustomerDatabase.getInstance().saveCustomers();
        return orderBuilder.toData();
    }


    /**
     * Usage scenarios considered :
     * <p>
     * [O4] Each time a new menu item is added to an order, the possible delivery dates change
     * according to preparation times. In the case of an order for which the delivery date has already
     * been chosen (for example, a group order), the system proposes only menu items that can be
     * prepared on time.
     * <p>
     * [O5] The system reduces the restaurant's capacity as orders are placed, with preparation allowed
     * in a reasonable time before the scheduled delivery (currently set at 2 hours).
     *
     * @param menuName   a menu's name
     * @param customerId a customer's id
     * @return the order builder after adding the menu
     */
    public Order.Builder.Data addMenuToOrder(String menuName, int customerId) throws CustomerNotFoundException, RestaurantNotFoundException {
        Customer customer = CustomerDatabase.getInstance().getCustomerById(customerId);
        Order.Builder orderBuilder = customer.getBasket();
        Restaurant restaurant = RestaurantDatabase.getInstance().getRestaurantById(orderBuilder.getRestaurantId());
        Menu menu = restaurant.getMenuByName(menuName);
        orderBuilder.addMenu(menu);
        CustomerDatabase.getInstance().saveCustomers();
        return orderBuilder.toData();
    }


    /**
     * Usage scenarios considered :
     * <p>
     * [P1] A user must process the payment step to validate her order. The system redirects user to an
     * external payment system for validation.
     * <p>
     * [P2] Successful payment results in order registration in the user's account.
     * <p>
     * [P3] Group orders don't involve payment; payment is only required for individual orders.
     * <p>
     * [O7] At the end of an individual order within a group order, the system should allow the user
     * to proceed directly to the validation step of the group order.
     * <p>
     * [E1] Orders of more than “n” menu items (currently set at 10) benefit from a discount of “r”%
     * whether the order is grouped or not. The discount applies to each sub-order when the order
     * comprises other sub-orders. The system proceeds with a credit on future orders.
     * <p>
     * [EX1] Restaurants can offer discount offers based on the number of orders a customer makes at their
     * establishment. Each restaurant can apply its own discount strategy.
     *
     * @return the status of the validation
     */
    public boolean validateAndPayOrder(int customerId) throws CustomerNotFoundException, RestaurantNotFoundException, IOException {
        Customer customer = CustomerDatabase.getInstance().getCustomerById(customerId);
        Order order = customer.getBasket().build();
        Restaurant restaurant = RestaurantDatabase.getInstance().getRestaurantById(order.getRestaurantId());
        restaurant.increaseOrderChargeable();
        customer.addOrderToHistory(order);
        customer.setBasket(null);
        CustomerDatabase.getInstance().saveCustomers();

        /* Call payment proxy */
        int responseCode = 200;
        try {
            HttpURLConnection connection = (HttpURLConnection) new URL("http://localhost:8083/api/payment/").openConnection();
            connection.setRequestMethod("GET");
            responseCode = connection.getResponseCode();
        }
        catch (Exception e) {
            System.out.println("Payment Service failed");
        }

        return (responseCode == 200);
    }
}
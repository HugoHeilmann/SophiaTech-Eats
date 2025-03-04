package backend.unitTests.restaurant;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import backend.*;
import backend.customer.Customer;
import backend.database.CustomerDatabase;
import backend.database.RestaurantDatabase;
import backend.exceptions.CustomerNotFoundException;
import backend.exceptions.RestaurantNotFoundException;
import backend.facade.OrderFacade;

import java.time.LocalTime;

import static org.junit.Assert.assertEquals;

public class TestRestaurant {

    CustomerDatabase customerDatabase = CustomerDatabase.getInstance();
    RestaurantDatabase restaurantDatabase = RestaurantDatabase.getInstance();
    Customer customer;

    @BeforeEach
    void setUp() {
        customerDatabase.initializeCustomers();
        restaurantDatabase.initializeRestaurants();
        customer = new Customer("TestMan", 100, null,"","");
        customerDatabase.addCustomer(customer);
    }


    @Test
    void testGetMenuByAvailability() throws RestaurantNotFoundException, CustomerNotFoundException {
        OrderFacade orderFacade = OrderFacade.getInstance();
        orderFacade.initOrder(0, customer.getId());

        customer.getBasket().addMenu(new Menu(FoodType.FAST_FOOD, "test", "", 10, 10, ""));

        LocalTime now = LocalTime.now();

        assertEquals(5, restaurantDatabase.getRestaurantById(0).getMenuByAvailability(customer.getId()).size());

        orderFacade.setDeliveryDate(now.plusMinutes(35), customer.getId());

        assertEquals(5, restaurantDatabase.getRestaurantById(0).getMenuByAvailability(customer.getId()).size());

        orderFacade.setDeliveryDate(now.plusMinutes(26), customer.getId());

        assertEquals(3, restaurantDatabase.getRestaurantById(0).getMenuByAvailability(customer.getId()).size());


        orderFacade.setDeliveryDate(now.plusMinutes(25), customer.getId());

        //only one menu is available because the delivery time is 15 minutes
        assertEquals(1, restaurantDatabase.getRestaurantById(0).getMenuByAvailability(customer.getId()).size());
    }
}
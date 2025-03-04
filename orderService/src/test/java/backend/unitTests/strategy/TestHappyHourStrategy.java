package backend.unitTests.strategy;


import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import backend.*;
import backend.customer.Customer;
import backend.customer.CustomerStatus;
import backend.database.CustomerDatabase;
import backend.database.RestaurantDatabase;
import backend.exceptions.CustomerNotFoundException;
import backend.exceptions.RestaurantNotFoundException;

import java.time.LocalTime;

class TestHappyHourStrategy {

    Order.Builder orderBuilder;
    Customer customer;

    @BeforeEach
    void setUp() throws RestaurantNotFoundException {
        RestaurantDatabase.getInstance().initializeRestaurants();
        CustomerDatabase.getInstance().initializeCustomers();
        customer = new Customer("TestMan", 100, CustomerStatus.REGULAR,"","");
        CustomerDatabase.getInstance().addCustomer(customer);
        orderBuilder = new Order.Builder(1, customer.getId());
    }

    @Test
    void testDiscount() throws RestaurantNotFoundException, CustomerNotFoundException {
        LocalTime localTime = LocalTime.of(17, 0); // Hour, Minute

        Order order = orderBuilder.addMenu(new Menu(FoodType.FAST_FOOD, "TestMenu", "", 10, 30, "")).orderDate(localTime).build();
        Assertions.assertEquals(7.5, order.getPrice(), 0.01);

        localTime = LocalTime.of(18, 59); // Year, Month, Day, Hour, Minute
        orderBuilder = new Order.Builder(1, customer.getId());
        order = orderBuilder.addMenu(new Menu(FoodType.FAST_FOOD, "TestMenu", "", 10, 30, "")).orderDate(localTime).build();
        Assertions.assertEquals(7.5, order.getPrice(), 0.01);
    }

    @Test
    void testNoDiscount() throws RestaurantNotFoundException, CustomerNotFoundException {
        LocalTime localTime = LocalTime.of(16, 59); // Year, Month, Day, Hour, Minute
        Order order = orderBuilder.addMenu(new Menu(FoodType.FAST_FOOD, "TestMenu", "", 10, 30, "")).orderDate(localTime).build();
        Assertions.assertEquals(10, order.getPrice(), 0.01);

        localTime = LocalTime.of(19, 0); // Year, Month, Day, Hour, Minute
        orderBuilder = new Order.Builder(1, customer.getId());
        order = orderBuilder.addMenu(new Menu(FoodType.FAST_FOOD, "TestMenu", "", 10, 30, "")).orderDate(localTime).build();
        Assertions.assertEquals(10, order.getPrice(), 0.01);

    }

}

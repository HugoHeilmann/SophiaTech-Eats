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

class TestFidelityStrategy {

    Customer customer;


    @BeforeEach
    void setUp() {
        RestaurantDatabase.getInstance().initializeRestaurants();
        CustomerDatabase.getInstance().initializeCustomers();

        customer = new Customer("lambda", 100, CustomerStatus.REGULAR,"","");
        CustomerDatabase.getInstance().addCustomer(customer);
    }

    @Test
    void testNoDiscount() throws RestaurantNotFoundException, CustomerNotFoundException {
        Order.Builder orderBuilder = new Order.Builder(0, customer.getId()).orderDate(LocalTime.now());
        orderBuilder.addMenu(new Menu(FoodType.FAST_FOOD, "Menu étudiant", "Menu étudiant", 10.0, 30, ""));
        Order order = orderBuilder.build();
        Assertions.assertEquals(10.0, order.getPrice(), 0.01);

        customer.addOrderToHistory(order);

        orderBuilder = new Order.Builder(0, customer.getId()).orderDate(LocalTime.now());
        orderBuilder.addMenu(new Menu(FoodType.FAST_FOOD, "Menu étudiant", "Menu étudiant", 10.0, 30, ""));

        order = orderBuilder.build();
        Assertions.assertEquals(10.0, order.getPrice(), 0.01);

        customer.addOrderToHistory(order);

        orderBuilder = new Order.Builder(0, customer.getId()).orderDate(LocalTime.now());
        orderBuilder.addMenu(new Menu(FoodType.FAST_FOOD, "Menu étudiant", "Menu étudiant", 10.0, 30, ""));

        order = orderBuilder.build();
        Assertions.assertEquals(10.0, order.getPrice(), 0.01);

        customer.addOrderToHistory(order);


    }

    @Test
    void testDiscount() throws RestaurantNotFoundException, CustomerNotFoundException {
        Order.Builder orderBuilder = new Order.Builder(0, customer.getId()).orderDate(LocalTime.now());
        orderBuilder.addMenu(new Menu(FoodType.FAST_FOOD, "Menu étudiant", "Menu étudiant", 10.0, 30, ""));
        Order order = orderBuilder.build();
        customer.addOrderToHistory(order);

        orderBuilder = new Order.Builder(0, customer.getId()).orderDate(LocalTime.now());
        orderBuilder.addMenu(new Menu(FoodType.FAST_FOOD, "Menu étudiant", "Menu étudiant", 10.0, 30, ""));
        order = orderBuilder.build();
        customer.addOrderToHistory(order);

        orderBuilder = new Order.Builder(0, customer.getId()).orderDate(LocalTime.now());
        orderBuilder.addMenu(new Menu(FoodType.FAST_FOOD, "Menu étudiant", "Menu étudiant", 10.0, 30, ""));
        order = orderBuilder.build();
        customer.addOrderToHistory(order);

        orderBuilder = new Order.Builder(0, customer.getId()).orderDate(LocalTime.now());
        orderBuilder.addMenu(new Menu(FoodType.FAST_FOOD, "Menu étudiant", "Menu étudiant", 10.0, 30, ""));

        order = orderBuilder.build();
        Assertions.assertEquals(8.0, order.getPrice(), 0.01);
        customer.addOrderToHistory(order);

        orderBuilder = new Order.Builder(0, customer.getId()).orderDate(LocalTime.now());
        orderBuilder.addMenu(new Menu(FoodType.FAST_FOOD, "Menu étudiant", "Menu étudiant", 10.0, 30, ""));

        order = orderBuilder.build();
        Assertions.assertEquals(10.0, order.getPrice(), 0.01);
        customer.addOrderToHistory(order);

        orderBuilder = new Order.Builder(0, customer.getId()).orderDate(LocalTime.now());
        orderBuilder.addMenu(new Menu(FoodType.FAST_FOOD, "Menu étudiant", "Menu étudiant", 10.0, 30, ""));

        order = orderBuilder.build();
        Assertions.assertEquals(10.0, order.getPrice(), 0.01);
        customer.addOrderToHistory(order);


        orderBuilder = new Order.Builder(0, customer.getId()).orderDate(LocalTime.now());
        orderBuilder.addMenu(new Menu(FoodType.FAST_FOOD, "Menu étudiant", "Menu étudiant", 10.0, 30, ""));
        order = orderBuilder.build();
        Assertions.assertEquals(10.0, order.getPrice(), 0.01);
        customer.addOrderToHistory(order);

        orderBuilder = new Order.Builder(0, customer.getId()).orderDate(LocalTime.now());
        orderBuilder.addMenu(new Menu(FoodType.FAST_FOOD, "Menu étudiant", "Menu étudiant", 10.0, 30, ""));

        order = orderBuilder.build();
        Assertions.assertEquals(8.0, order.getPrice(), 0.01);
        customer.addOrderToHistory(order);
    }

}
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
import java.util.ArrayList;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

class TestStudentStrategy {

    Order.Builder orderBuilderWithStudent;
    Order.Builder orderBuilderWithoutStudent;
    Customer student;
    Customer notAStudent;

    @BeforeEach
    void setUp() {
        RestaurantDatabase.getInstance().initializeRestaurants();
        CustomerDatabase.getInstance().initializeCustomers();

        student = new Customer("TestMan", 100, CustomerStatus.STUDENT,"","");
        CustomerDatabase.getInstance().addCustomer(student);
        notAStudent = new Customer("TestMan2", 100, CustomerStatus.REGULAR,"","");
        CustomerDatabase.getInstance().addCustomer(notAStudent);
        orderBuilderWithStudent = new Order.Builder(null, null, null, 0, new ArrayList<>(), 2, student.getId()).orderDate(LocalTime.now());
        orderBuilderWithoutStudent = new Order.Builder(null, null, null, 0, new ArrayList<>(), 2, notAStudent.getId()).orderDate(LocalTime.now());

    }

    @Test
    void testDiscount() throws RestaurantNotFoundException, CustomerNotFoundException {
        Order order = orderBuilderWithStudent.addMenu(new Menu(FoodType.FAST_FOOD, "Menu", "test", 10, 10, "")).build();
        Assertions.assertEquals(order.getPrice(), 9, 0.01);
    }

    @Test
    void testNoDiscount() throws RestaurantNotFoundException, CustomerNotFoundException {
        Order order = orderBuilderWithoutStudent.addMenu(new Menu(FoodType.FAST_FOOD, "Menu", "test", 10, 10, "")).build();
        Assertions.assertEquals(order.getPrice(), 10, 0.01);
    }

}

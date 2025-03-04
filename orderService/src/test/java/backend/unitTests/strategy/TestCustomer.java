package backend.unitTests;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import backend.customer.Customer;
import backend.customer.CustomerStatus;
import backend.database.CustomerDatabase;
import backend.database.RestaurantDatabase;
import backend.exceptions.CustomerNotFoundException;
import backend.exceptions.RestaurantNotFoundException;

import static org.junit.Assert.*;

class TestCustomer {

    @BeforeEach
    void setUp() {
        RestaurantDatabase.getInstance().initializeRestaurants();
        CustomerDatabase.getInstance().initializeCustomers();
    }

    @Test
    void testGetter() throws CustomerNotFoundException, RestaurantNotFoundException {
        Customer c1 = CustomerDatabase.getInstance().getCustomerById(0);
        assertEquals("Dorian", c1.getName());
        assertTrue(220.0 == c1.getBalance());
        assertEquals(0, c1.getId());
        assertEquals(CustomerStatus.REGULAR, c1.getStatus());
        assertEquals(0, c1.getNumberOfOrderIn(0));

    }
}

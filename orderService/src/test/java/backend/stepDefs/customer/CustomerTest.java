package backend.stepDefs.customer;


import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import backend.customer.Customer;

import static org.junit.jupiter.api.Assertions.*;
import static backend.customer.CustomerStatus.REGULAR;


public class CustomerTest {
    Customer c1;

    @BeforeEach
    void setUp() {
        c1 = new Customer("Marcel", 100, REGULAR,"","");
    }

    @Test
    void testGetName() {
        assertEquals("Marcel", c1.getName());
    }
}

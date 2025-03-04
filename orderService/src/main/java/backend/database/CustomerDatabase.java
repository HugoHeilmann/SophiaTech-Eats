package backend.database;

import backend.customer.Customer;
import backend.customer.CustomerStatus;
import backend.exceptions.CustomerNotFoundException;


import com.fasterxml.jackson.core.type.TypeReference;
import backend.utils.JsonUtil;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

public class CustomerDatabase {
    Logger logger = Logger.getLogger(CustomerDatabase.class.getName());
    private static String DATABASE_FILE = "";
    static {
        for (StackTraceElement element : Thread.currentThread().getStackTrace()) {
            if (element.getClassName().startsWith("org.junit.") ||
                    element.getClassName().startsWith("org.testng.")) {
                DATABASE_FILE = "src/test/resources/data/customers.json";
            }
        }
        if (DATABASE_FILE == "") {
            DATABASE_FILE = "../database/customers.json";
        }
    }
    private static CustomerDatabase instance;
    private List<Customer> customers;

    private CustomerDatabase() {
    }

    // Singleton Pattern
    public static CustomerDatabase getInstance() {
        if (instance == null) {
            instance = new CustomerDatabase();
            instance.loadCustomers();
        }
        return instance;
    }

    private void loadCustomers() {
        try {
            File file = new File(DATABASE_FILE);
            customers = JsonUtil.readFromFile(file, new TypeReference<List<Customer>>() {
            });
        } catch (IOException e) {
            logger.info("Error loading customers: " + e.getMessage());
            initializeCustomers();
        }
    }

    public void saveCustomers() {
        try {
            JsonUtil.writeToFile(new File(DATABASE_FILE), customers);
        } catch (IOException e) {
            logger.info("Error saving customers: " + e.getMessage());
        }
    }

    public void initializeCustomers() {
        Customer.resetId();
        customers = new ArrayList<>(List.of(
                new Customer("Dorian", 220, CustomerStatus.REGULAR,"",""),
                new Customer("Erwan", 20, CustomerStatus.STUDENT,"",""),
                new Customer("Hugo", 150, CustomerStatus.STUDENT,"",""),
                new Customer("Maxence", 300, CustomerStatus.REGULAR,"",""),
                new Customer("Mathis", 180, CustomerStatus.REGULAR,"","")
        ));
        saveCustomers();
    }

    public void addCustomer(Customer customer) {
        customers.add(customer);
        saveCustomers();
    }

    public void removeCustomerById(int id) throws CustomerNotFoundException {
        customers.remove(getCustomerById(id));
        saveCustomers();
    }

    public Customer getCustomerById(int id) throws CustomerNotFoundException {
        return customers.stream()
                .filter(customer -> customer.getId() == id)
                .findFirst()
                .orElseThrow(() -> new CustomerNotFoundException("Customer with id " + id + " not found"));
    }

    public Customer getCustomerByEmail(String email) throws CustomerNotFoundException {
        return customers.stream()
                .filter(customer -> customer.getEmail().equals(email))
                .findFirst()
                .orElseThrow(() -> new CustomerNotFoundException("Customer with email " + email + " not found"));
    }

    public List<Customer> getCustomers() {
        return customers;
    }
}

package dependencies.strategy;


import dependencies.Order;
import dependencies.customer.Customer;
import dependencies.customer.CustomerStatus;
import dependencies.databse.CustomerDatabase;
import exceptions.CustomerNotFoundException;

public class StudentStrategy implements OfferStrategy {

    private static final double DISCOUNT = 0.1;

    @Override
    public double availableDiscount(Order.Builder order) throws CustomerNotFoundException {
        Customer customer = CustomerDatabase.getInstance().getCustomerById(order.getCustomerId());
        if (customer.getStatus() == CustomerStatus.STUDENT) {
            return DISCOUNT;
        }
        return 0;
    }

    @Override
    public String getOfferDescription() {
        return "10% off for Students";
    }
}

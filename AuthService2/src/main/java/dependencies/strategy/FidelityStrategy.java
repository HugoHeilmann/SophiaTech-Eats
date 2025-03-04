package dependencies.strategy;

import dependencies.Order;
import dependencies.customer.Customer;
import dependencies.databse.CustomerDatabase;
import exceptions.CustomerNotFoundException;

public class FidelityStrategy implements OfferStrategy {
    private static final double DISCOUNT = 0.2;
    private static final int MINIMUM_ORDER = 4;


    @Override
    public double availableDiscount(Order.Builder order) throws CustomerNotFoundException {
        Customer customer = CustomerDatabase.getInstance().getCustomerById(order.getCustomerId());
        int numberOfOrders = customer.getNumberOfOrderIn(order.getRestaurantId());
        if (numberOfOrders % MINIMUM_ORDER == 3) {
            return DISCOUNT;
        }
        return 0;
    }

    @Override
    public String getOfferDescription() {
        return "20% off for Fidelity Card holders";
    }
}

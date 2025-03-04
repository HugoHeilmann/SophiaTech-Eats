package dependencies.strategy;

import dependencies.Order;

import java.time.LocalTime;

public class HappyHourStrategy implements OfferStrategy {
    private static final double DISCOUNT = 0.25;
    private static final int START_HOUR = 17;
    private static final int END_HOUR = 19;


    @Override
    public double availableDiscount(Order.Builder order) {
        LocalTime orderDate = order.getOrderDate();

        int hour = orderDate.getHour();

        if (hour >= START_HOUR && hour < END_HOUR) {
            return DISCOUNT;
        }
        return 0;
    }

    @Override
    public String getOfferDescription() {
        return "25% off during Happy Hour";
    }
}

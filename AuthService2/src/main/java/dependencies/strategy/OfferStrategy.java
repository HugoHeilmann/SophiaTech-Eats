package dependencies.strategy;

import com.fasterxml.jackson.annotation.JsonTypeInfo;
import dependencies.Order;
import exceptions.CustomerNotFoundException;


@JsonTypeInfo(
        use = JsonTypeInfo.Id.CLASS,
        include = JsonTypeInfo.As.PROPERTY,
        property = "@class"
)
public interface OfferStrategy {
    double availableDiscount(Order.Builder order) throws CustomerNotFoundException;

    String getOfferDescription();
}

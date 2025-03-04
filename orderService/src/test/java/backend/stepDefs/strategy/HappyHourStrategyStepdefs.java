// In file `RestaurantStepdefs.java`
package backend.stepDefs.strategy;

import io.cucumber.java.fr.Alors;
import io.cucumber.java.fr.Et;
import io.cucumber.java.fr.Etantdonné;
import io.cucumber.java.fr.Quand;
import backend.*;
import backend.customer.Customer;
import backend.customer.CustomerStatus;
import backend.database.CustomerDatabase;
import backend.database.RestaurantDatabase;
import backend.exceptions.CustomerNotFoundException;
import backend.exceptions.RestaurantNotFoundException;
import backend.facade.OrderFacade;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.LocalTime;

import static org.junit.Assert.assertEquals;

public class HappyHourStrategyStepdefs {
    OrderFacade facade = OrderFacade.getInstance();
    Customer customer;
    Restaurant restaurant;
    Order.Builder orderBuilder;

    @Etantdonné("Que j'ai une commande valide auprès d'un restaurant avec un stratégie de réduction happy hour")
    public void que_j_ai_une_commande_valide_auprès_d_un_restaurant_avec_un_stratégie_de_réduction_happy_hour() throws RestaurantNotFoundException, CustomerNotFoundException {
        CustomerDatabase.getInstance().initializeCustomers();
        RestaurantDatabase.getInstance().initializeRestaurants();
        customer = new Customer("TestMan", 100, CustomerStatus.REGULAR,"","");
        CustomerDatabase.getInstance().addCustomer(customer);
        restaurant = RestaurantDatabase.getInstance().getRestaurantById(1);
        facade.initOrder(restaurant.getId(), customer.getId());
        facade.addMenuToOrder("Reine", customer.getId());
    }

    @Et("Qu'il est {int}h{int}")
    public void qu_il_est_heure(Integer hour, Integer minute) {
        // Set the order date to a specific time, so that we can test the happy hour strategy
        customer.getBasket().orderDate(LocalTime.from(LocalDateTime.of(2024, 1, 1, hour, minute)));
    }

    @Quand("Je passe ma commande auprès de ce restaurant avec la stratégie de réduction happy hour")
    public void je_passe_ma_commande() throws CustomerNotFoundException, RestaurantNotFoundException, IOException {
        orderBuilder = customer.getBasket();
        facade.validateAndPayOrder(customer.getId());
    }

    @Alors("Je devrais bénéficier de la réduction de 25%")
    public void je_devrais_bénéficier_de_la_réduction_de_25() {
        assertEquals(7.5, orderBuilder.getPrice(), 0.01);
    }

    @Alors("Je ne devrais pas bénéficier de la réduction de 25%")
    public void je_ne_devrais_pas_bénéficier_de_la_réduction_de_25() {
        assertEquals(10, orderBuilder.getPrice(), 0.01);
        CustomerDatabase.getInstance().initializeCustomers();
        RestaurantDatabase.getInstance().initializeRestaurants();
    }
}
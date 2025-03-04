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

import static org.junit.Assert.assertEquals;

public class FidelityStrategyStepdefs {
    OrderFacade facade = OrderFacade.getInstance();
    Customer customer;
    Restaurant restaurant;
    Order order;
    Order.Builder orderBuilder;


    @Etantdonné("Que j'ai une commande valide auprès d'un restaurant avec un stratégie de réduction fidelité")
    public void que_j_ai_une_commande_valide_auprès_d_un_restaurant_avec_un_stratégie_de_réduction_fidelité() throws RestaurantNotFoundException, CustomerNotFoundException {
        RestaurantDatabase.getInstance().initializeRestaurants();
        CustomerDatabase.getInstance().initializeCustomers();
        customer = new Customer("lambda", 100, CustomerStatus.REGULAR,"","");
        CustomerDatabase.getInstance().addCustomer(customer);
        restaurant = RestaurantDatabase.getInstance().getRestaurantById(0);
        facade.initOrder(restaurant.getId(), customer.getId());
        facade.addMenuToOrder("Cheeseburger originel", customer.getId());
    }

    @Et("Que j'ai déjà passé {int} commandes auprès de ce restaurant")
    public void que_j_ai_déjà_passé_commandes_auprès_de_ce_restaurant(Integer int1) {
        for (int i = 0; i < int1; i++)
            customer.addOrderToHistory(new Order(null, null, null, 0, null, restaurant.getId(), customer.getId(), 0));
    }

    @Quand("Je passe ma commande auprès de ce restaurant avec la stratégie de réduction fidelité")
    public void je_passe_ma_commande() throws CustomerNotFoundException, RestaurantNotFoundException, IOException {
        orderBuilder = customer.getBasket();
        facade.validateAndPayOrder(customer.getId());
    }

    @Alors("Je devrais bénéficier de la réduction de 20%")
    public void je_devrais_bénéficier_de_la_réduction_de_20() {
        assertEquals(8.0, orderBuilder.getPrice(), 0.01);
    }

    @Alors("Je ne devrais pas bénéficier de la réduction de 20%")
    public void je_ne_devrais_pas_bénéficier_de_la_réduction_de_20() {
        assertEquals(10.0, orderBuilder.getPrice(), 0.01);
        RestaurantDatabase.getInstance().initializeRestaurants();
        CustomerDatabase.getInstance().initializeCustomers();
    }

}
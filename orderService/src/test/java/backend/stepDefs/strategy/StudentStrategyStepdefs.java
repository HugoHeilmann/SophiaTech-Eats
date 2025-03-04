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

public class StudentStrategyStepdefs {
    Customer customer;
    Restaurant restaurant;
    Order.Builder orderBuilder;
    OrderFacade facade = OrderFacade.getInstance();

    @Etantdonné("Que je ne suis pas un étudiant")
    public void que_je_ne_suis_pas_un_étudiant() {
        CustomerDatabase.getInstance().initializeCustomers();
        RestaurantDatabase.getInstance().initializeRestaurants();
        customer = new Customer("not student", 100, CustomerStatus.REGULAR,"","");
        CustomerDatabase.getInstance().addCustomer(customer);
    }

    @Etantdonné("Que je suis un étudiant")
    public void que_je_suis_un_étudiant() {
        CustomerDatabase.getInstance().initializeCustomers();
        RestaurantDatabase.getInstance().initializeRestaurants();
        customer = new Customer("student", 100, CustomerStatus.STUDENT,"","");
        CustomerDatabase.getInstance().addCustomer(customer);
    }

    @Et("Que j'ai une commande valide auprès d'un restaurant avec un stratégie de réduction pour les étudiants")
    public void que_j_ai_une_commande_valide_auprès_d_un_restaurant_avec_un_stratégie_de_réduction_pour_les_étudiants() throws RestaurantNotFoundException, CustomerNotFoundException {
        restaurant = RestaurantDatabase.getInstance().getRestaurantById(2);
        customer = CustomerDatabase.getInstance().getCustomerById(5);
        facade.initOrder(restaurant.getId(), customer.getId());
        facade.addMenuToOrder("Classique jambon", customer.getId());
    }

    @Quand("Je passe ma commande auprès de ce restaurant avec la stratégie de réduction pour les étudiants")
    public void je_passe_ma_commande() throws CustomerNotFoundException, RestaurantNotFoundException, IOException {
        orderBuilder = customer.getBasket();
        facade.validateAndPayOrder(customer.getId());
    }

    @Alors("Je devrais bénéficier de la réduction de 10%")
    public void je_devrais_bénéficier_de_la_réduction_de_10() {
        assertEquals(9.0, orderBuilder.getPrice(), 0.01);
    }

    @Alors("Je ne devrais pas bénéficier de la réduction de 10%")
    public void je_ne_devrais_pas_bénéficier_de_la_réduction_de_10() {
        assertEquals(10.0, orderBuilder.getPrice(), 0.01);
        CustomerDatabase.getInstance().initializeCustomers();
        RestaurantDatabase.getInstance().initializeRestaurants();
    }


}
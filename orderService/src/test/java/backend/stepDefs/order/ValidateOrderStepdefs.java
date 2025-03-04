package backend.stepDefs.order;

import io.cucumber.java.fr.Alors;
import io.cucumber.java.fr.Etantdonné;
import io.cucumber.java.fr.Quand;
import backend.database.CustomerDatabase;
import backend.database.RestaurantDatabase;
import backend.facade.OrderFacade;
import backend.Order;
import backend.Restaurant;
import backend.customer.Customer;
import backend.exceptions.CustomerNotFoundException;
import backend.exceptions.RestaurantNotFoundException;

import java.io.IOException;

import static org.junit.Assert.*;

public class ValidateOrderStepdefs {

    OrderFacade orderFacade = OrderFacade.getInstance();
    CustomerDatabase customerDatabase = CustomerDatabase.getInstance();
    RestaurantDatabase restaurantDatabase = RestaurantDatabase.getInstance();
    Customer customer;
    Restaurant restaurant;
    boolean result;


    @Etantdonné("Que j'ai une commande de {int} article dans mon panier")
    public void que_j_ai_une_commande_de_article_dans_mon_panier(Integer int1) throws CustomerNotFoundException, RestaurantNotFoundException {
        restaurantDatabase.initializeRestaurants();
        customerDatabase.initializeCustomers();
        customer = customerDatabase.getCustomerById(0);
        restaurant = restaurantDatabase.getRestaurantById(0);
        String menuName = restaurant.getMenus().get(0).getName();
        orderFacade.initOrder(restaurant.getId(), customer.getId());
        orderFacade.addMenuToOrder(menuName, customer.getId());
    }


    @Quand("Je valide ma commande")
    public void je_valide_ma_commande() throws CustomerNotFoundException, RestaurantNotFoundException, IOException {
        result = orderFacade.validateAndPayOrder(customer.getId());
    }


    @Alors("le système m'informe si l'opération a réussi ou échoué")
    public void le_système_m_informe_si_l_opération_a_réussi_ou_échoué() {
        assertTrue(result);
    }


    @Alors("la commande est enregistrée dans mon historique de commande")
    public void la_commande_est_enregistrée_dans_mon_historique_de_commande() {
        assertEquals(1, customer.getHistory().size());
        Order order = customer.getHistory().get(0);
        assertNotEquals(null, order);
        assertEquals(restaurant.getId(), order.getRestaurantId());
        assertEquals(1, order.getMenus().size());
        restaurantDatabase.initializeRestaurants();
        customerDatabase.initializeCustomers();
    }
}

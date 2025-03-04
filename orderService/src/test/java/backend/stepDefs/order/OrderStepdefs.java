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

import java.time.LocalTime;

import static org.junit.Assert.*;

public class OrderStepdefs {

    RestaurantDatabase restaurantDatabase = RestaurantDatabase.getInstance();
    CustomerDatabase customerDatabase = CustomerDatabase.getInstance();
    OrderFacade orderFacade = OrderFacade.getInstance();
    Customer customer;
    Restaurant restaurant;
    Order.Builder orderBuilder;

    @Etantdonné("Que je suis sur la page du restaurant d'id {int}")
    public void que_je_suis_sur_la_page_du_restaurant_d_id(Integer int1) throws RestaurantNotFoundException, CustomerNotFoundException {
        customer = customerDatabase.getCustomerById(0);
        restaurant = restaurantDatabase.getRestaurantById(int1);
    }

    @Quand("Je créé une commande dans ce restaurant")
    public void je_créé_une_commande_dans_ce_restaurant() throws RestaurantNotFoundException, CustomerNotFoundException {
        orderFacade.initOrder(restaurant.getId(), customer.getId());
    }

    @Alors("j'ai dans mon panier une commande sans articles dans le restaurant d'id {int}")
    public void j_ai_dans_mon_panier_une_commande_sans_articles_dans_le_restaurant_d_id(Integer int1) throws RestaurantNotFoundException {
        restaurant = restaurantDatabase.getRestaurantById(int1);
        orderBuilder = customer.getBasket();
        assertNotEquals(null, orderBuilder);
        assertEquals(restaurant.getId(), orderBuilder.getRestaurantId());
        assertEquals(0, orderBuilder.getMenus().size());
    }

    @Etantdonné("Que j'ai dans mon panier une commande sans articles dans le restaurant d'id {int}")
    public void que_j_ai_dans_mon_panier_une_commande_sans_articles_dans_le_restaurant_d_id(Integer int1) throws CustomerNotFoundException, RestaurantNotFoundException {
        customer = customerDatabase.getCustomerById(0);
        restaurant = restaurantDatabase.getRestaurantById(int1);
        orderFacade.initOrder(restaurant.getId(), customer.getId());
    }

    @Quand("Je choisis le lieux {string} et la date {int}-{int}-{int} à {int}h{int} de livraison")
    public void je_choisis_le_lieux_et_la_date_à_h_de_livraison(String string1, Integer int1, Integer int2, Integer int3, Integer int4, Integer int5) throws CustomerNotFoundException {
        orderFacade.registerLocation(string1, customer.getId());
        orderFacade.setOrderLocation(customer.getLocations().size() - 1, customer.getId());
        LocalTime time = LocalTime.of(int4, int5);
        orderFacade.setDeliveryDate(time, customer.getId());
    }

    @Alors("j'ai dans mon panier une commande sans articles dans le restaurant d'id {int} avec le lieux {string} et et la date {int}-{int}-{int} à {int}h{int} de livraison")
    public void j_ai_dans_mon_panier_une_commande_sans_articles_dans_le_restaurant_d_id_avec_le_lieux_et_et_la_date_à_h_de_livraison(Integer int1, String string1, Integer int2, Integer int3, Integer int4, Integer int5, Integer int6) throws RestaurantNotFoundException {
        restaurant = restaurantDatabase.getRestaurantById(int1);
        orderBuilder = customer.getBasket();
        assertNotEquals(null, orderBuilder);
        assertEquals(restaurant.getId(), orderBuilder.getRestaurantId());
        assertEquals(0, orderBuilder.getMenus().size());
        assertEquals(string1, orderBuilder.getLocation().getAddress());
        LocalTime time = LocalTime.of(int5, int6);
        assertEquals(time, orderBuilder.getDelivery());
    }
}
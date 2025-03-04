package backend.stepDefs.order;

import io.cucumber.java.fr.Alors;
import io.cucumber.java.fr.Etantdonné;
import io.cucumber.java.fr.Quand;
import backend.*;
import backend.customer.Customer;
import backend.database.CustomerDatabase;
import backend.database.RestaurantDatabase;
import backend.exceptions.CustomerNotFoundException;
import backend.exceptions.RestaurantNotFoundException;
import backend.facade.OrderFacade;

import java.io.IOException;
import java.util.List;

import static org.junit.Assert.*;

public class AddItemToOrderStepdefs {

    OrderFacade facade = OrderFacade.getInstance();
    RestaurantDatabase restaurantDatabase = RestaurantDatabase.getInstance();
    CustomerDatabase customerDatabase = CustomerDatabase.getInstance();
    Customer customer;
    Restaurant restaurant;
    Order.Builder orderBuilder;

    @Etantdonné("Que j'ai dans mon panier une commande dans le restaurant d'id {int} sans articles")
    public void que_j_ai_dans_mon_panier_une_commande_dans_le_restaurant_d_id_sans_articles(int int1) throws CustomerNotFoundException, RestaurantNotFoundException {
        customer = customerDatabase.getCustomerById(0);
        restaurant = restaurantDatabase.getRestaurantById(int1);
        facade.initOrder(restaurant.getId(), customer.getId());
    }


    @Quand("J'y ajoute le {string} {int} fois")
    public void j_y_ajoute_le_fois(String string1, int int1) throws CustomerNotFoundException, RestaurantNotFoundException {
        Menu menu = restaurant.getMenuByName(string1);
        for (int i = 0; i < int1; i++) {
            facade.addMenuToOrder(menu.getName(), 0);
        }
    }


    @Alors("j'ai dans mon panier une commande avec le {string} {int} fois dans le restaurant d'id {int}")
    public void j_ai_dans_mon_panier_une_commande_avec_fois_dans_le_restaurant_d_id(String string1, int int1, int int2) {
        orderBuilder = customer.getBasket();
        assertEquals(int2, orderBuilder.getRestaurantId());
        List<Menu> menus = orderBuilder.getMenus();
        assertEquals(int1, menus.size());
        for (Menu menu : menus) {
            assertEquals(string1, menu.getName());
        }
        restaurantDatabase.initializeRestaurants();
        customerDatabase.initializeCustomers();
    }


    /* Reduction */

    Order.Builder basket;
    double priceBeforeDiscount;

    @Etantdonné("Que j'ai une commande de plus de {int} articles auprès du restaurant d'id {int}")
    public void que_j_ai_une_commande_de_plus_de_articles_auprès_du_restaurant_d_id(Integer int1, Integer int2) throws CustomerNotFoundException, RestaurantNotFoundException {
        customer = customerDatabase.getCustomerById(0);
        restaurant = restaurantDatabase.getRestaurantById(int2);

        facade.initOrder(restaurant.getId(), customer.getId());
        for (int i = 0; i < int1; i++) {
            facade.addMenuToOrder(restaurant.getMenus().get(0).getName(), customer.getId());
        }
    }


    @Quand("Je passe ma commande auprès de ce restaurant")
    public void je_passe_ma_commande_auprès_de_ce_restaurant() throws CustomerNotFoundException, RestaurantNotFoundException, IOException {
        basket = customer.getBasket();
        priceBeforeDiscount = basket.getPrice();

        facade.validateAndPayOrder(customer.getId());
    }

    @Alors("Je devrais bénéficier d'une réduction de {int}%")
    public void jeDevraisBénéficierDUneRéductionDe(int arg0) {
        assertEquals(basket.getPrice(), priceBeforeDiscount * (1 - (double) (arg0) / 100), 0.01);
        restaurantDatabase.initializeRestaurants();
        customerDatabase.initializeCustomers();
    }
}

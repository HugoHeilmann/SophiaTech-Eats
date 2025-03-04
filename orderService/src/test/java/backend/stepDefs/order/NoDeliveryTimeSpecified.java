package backend.stepDefs.order;

import io.cucumber.java.fr.Alors;
import io.cucumber.java.fr.Et;
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
import java.time.LocalTime;
import java.time.temporal.ChronoUnit;

import java.util.ArrayList;


import static org.junit.Assert.*;

public class NoDeliveryTimeSpecified {

    OrderFacade orderFacade = OrderFacade.getInstance();
    Restaurant restaurant;
    RestaurantDatabase restaurantDatabase = RestaurantDatabase.getInstance();
    CustomerDatabase customerDatabase = CustomerDatabase.getInstance();
    Customer customer;
    Order.Builder orderBuilder;
    int id = 0;

    @Etantdonné("Que je suis en train de passer une commande")
    public void que_je_suis_en_train_de_passer_une_commande() throws CustomerNotFoundException, RestaurantNotFoundException {
        restaurant = new Restaurant("test", "test", "test", "test", new ArrayList<>(), new Capacity(), null, "", "");
        restaurantDatabase.addRestaurant(restaurant);
        customer = customerDatabase.getCustomerById(0);
        orderFacade.initOrder(restaurant.getId(), customer.getId());
    }

    @Et("Que je n'ai pas spécifié l'heure de livraison")
    public void que_je_n_ai_pas_spécifié_l_heure_de_livraison() {
        orderBuilder = customer.getBasket();
        assertNotEquals(null, orderBuilder);
        assertNull(orderBuilder.getDelivery());
    }

    @Quand("J'ajoute un menu à ma commande avec une durée de préparation de {int} secondes")
    public void j_ajoute_un_menu_à_ma_commande_avec_une_durée_de_préparation_de_minutes(Integer int1) throws CustomerNotFoundException, RestaurantNotFoundException {
        restaurant.addMenu(new Menu(FoodType.FAST_FOOD, "Menu" + int1, "", 10, int1, ""));
        orderFacade.addMenuToOrder("Menu" + int1, customer.getId());
        id++;
    }

    @Alors("La date de livraison est {int} secondes après la date de finalisation de la commande")
    public void la_date_de_livraison_est_minutes_apres_la_date_de_finalisation_de_la_commande(Integer int1) throws CustomerNotFoundException, RestaurantNotFoundException, IOException {
        LocalTime now = LocalTime.now().truncatedTo(ChronoUnit.SECONDS);
        orderBuilder = customer.getBasket();
        orderFacade.validateAndPayOrder(customer.getId());
        LocalTime delivery = orderBuilder.getDelivery().truncatedTo(ChronoUnit.SECONDS);
        long diffInSeconds = ChronoUnit.SECONDS.between(now, delivery);
        System.out.println("NOW : " + now);
        System.out.println("DELIVERY : " + delivery);
        System.out.println("DIFF : " + diffInSeconds);
        assertTrue(diffInSeconds >= int1 && diffInSeconds <= int1 + 2);

        restaurantDatabase.removeRestaurantById(restaurant.getId());
    }

}
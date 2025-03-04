package backend.stepDefs.restaurant;

import io.cucumber.java.fr.*;
import backend.FoodType;
import backend.Menu;
import backend.customer.Customer;
import backend.customer.CustomerStatus;
import backend.database.CustomerDatabase;
import backend.database.RestaurantDatabase;
import backend.exceptions.CustomerNotFoundException;
import backend.exceptions.RestaurantNotFoundException;
import backend.facade.CommonFacade;
import backend.facade.ManagementFacade;
import backend.facade.OrderFacade;

import java.time.LocalTime;
import java.util.Optional;

import static org.junit.Assert.assertTrue;

public class BrowseMenusByAvailabilityStepdefs {

    OrderFacade orderFacade = OrderFacade.getInstance();
    ManagementFacade managementFacade = ManagementFacade.getInstance();
    CustomerDatabase customerDatabase = CustomerDatabase.getInstance();
    Customer customer = new Customer("TestMan", 100, CustomerStatus.REGULAR,"","");
    Menu.Data[] res;
    int restaurantId;
    int time;
    int duration = 0;
    int id = 0;

    @Etantdonné("Que j'ai une commande avec un horaire de livraison prévu dans {int} minutes dans le restaurant {int}")
    public void que_j_ai_une_commande_avec_un_horaire_de_livraison_de_h(Integer int1, Integer int2) throws RestaurantNotFoundException, CustomerNotFoundException {
        restaurantId = int2;
        time = int1;
        customerDatabase.initializeCustomers();
        RestaurantDatabase.getInstance().initializeRestaurants();
        customerDatabase.addCustomer(customer);
        orderFacade.initOrder(restaurantId, customer.getId());
        orderFacade.setDeliveryDate(LocalTime.now().plusMinutes(time), customer.getId());
    }

    @Et("Que j'ai un item dans mon panier avec un temps de préparation de {int} minutes")
    public void que_j_ai_un_item_dans_mon_panier_avec_un_temps_de_préparation_de_minutes(Integer int1) throws CustomerNotFoundException, RestaurantNotFoundException {
        managementFacade.addMenu(restaurantId, new Menu(FoodType.FAST_FOOD, "test" + id, "", 10, int1 * 60, ""));
        orderFacade.addMenuToOrder("test" + id, customer.getId());
        duration += int1;
        id++;
    }

    @Quand("Je recherche les menus dans ce meme restaurant")
    public void je_recherche_les_menus_dans_ce_meme_restaurant() throws RestaurantNotFoundException, CustomerNotFoundException {
        CommonFacade commonFacade = CommonFacade.getInstance();
        res = commonFacade.browseMenus(0, Optional.of(customer.getId()));
    }


    @Alors("Je reçois le tableau des menus du restaurant avec un temps de préparation inférieur")
    public void je_reçois_le_tableau_des_menus_du_restaurant_avec_un_temps_imparti() throws CustomerNotFoundException {
        for (Menu.Data menu : res) {
            assertTrue((menu.preparation() + duration * 60 + 15 * 60) <= time * 60);
            System.out.println(menu.name());
        }
    }
}
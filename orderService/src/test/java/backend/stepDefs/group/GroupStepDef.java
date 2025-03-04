package backend.stepDefs.group;

import io.cucumber.java.fr.Alors;
import io.cucumber.java.fr.Etantdonné;
import io.cucumber.java.fr.Etantdonnéque;
import io.cucumber.java.fr.Quand;
import backend.*;
import backend.customer.Customer;
import backend.customer.CustomerStatus;
import backend.database.CustomerDatabase;
import backend.database.GroupDatabase;
import backend.database.RestaurantDatabase;
import backend.exceptions.CustomerNotFoundException;
import backend.exceptions.GroupNotFoundException;
import backend.exceptions.RestaurantNotFoundException;
import backend.facade.OrderFacade;
import backend.location.Location;

import java.time.LocalTime;

import static org.junit.Assert.*;

public class GroupStepDef {

    OrderFacade orderFacade = OrderFacade.getInstance();
    CustomerDatabase customerDatabase = CustomerDatabase.getInstance();
    GroupDatabase database = GroupDatabase.getInstance();
    RestaurantDatabase restaurantDatabase = RestaurantDatabase.getInstance();
    Group.Data group;
    int id;
    Restaurant restaurant;
    Order.Builder orderBuilder;
    Customer customer;
    OrderFacade facade = OrderFacade.getInstance();

    public GroupStepDef() throws CustomerNotFoundException {
    }

    @Etantdonnéque("je suis un utilisateur de SophiaEats")
    public void je_suis_un_utilisateur_de_sophia_eats() throws CustomerNotFoundException {
        customer = customerDatabase.getCustomerById(0);
    }

    /* Scenario: Créer un groupe de commande */

    @Quand("je créer une commande de groupe pour {int}h{int} aux {string}")
    public void je_créer_une_commande_de_groupe_pour_h_aux(int hour, int minutes, String location) throws GroupNotFoundException, CustomerNotFoundException {
        LocalTime dateLivraison = LocalTime.of(hour, minutes);
        Location l1 = new Location(location);
        group = orderFacade.createGroup(dateLivraison, l1, customer.getId());
        id = group.toGroup().getId();
    }


    @Alors("je recois un id pour un groupe de commande pour {int}h{int} aux {string}")
    public void je_recois_id_un_pour_un_groupe_de_commande_pour_h_aux(int hour, int minutes, String location) throws GroupNotFoundException {
        LocalTime deliveryTime = LocalTime.of(hour, minutes);
        Group g = database.getGroupById(id);
        //assertEquals(deliveryTime, g.getDelivery());
        //assertEquals(location, g.getLocation().getAddress());
        database.removeGroupById(id);
    }



    /* Scenario: Rejoindre un groupe de commande */

    @Etantdonné("un groupe de commande pour {int}h{int} aux {string}")
    public void un_groupe_de_commande_pour_h_aux(int heure, int minutes, String location) throws GroupNotFoundException, CustomerNotFoundException {
        Location l1 = new Location(location);
        group = orderFacade.createGroup(LocalTime.of(heure, minutes), l1, customer.getId());
        id = group.toGroup().getId();
    }

    @Quand("je rejoins un le groupe grace à l'id")
    public void je_rejoins_un_le_groupe_grace_à_l_id() throws GroupNotFoundException, CustomerNotFoundException, RestaurantNotFoundException {
        restaurant = restaurantDatabase.getRestaurantById(0);
        orderFacade.initOrder(restaurant.getId(), customer.getId());
        orderFacade.setOrderGroup(id, customer.getId());
    }

    @Alors("la localisation de ma commande est {string} et l'heure de livraison est {int}h{int}")
    public void la_localisation_de_ma_commande_est_et_l_heure_de_livraison_est_12h30(String location, int hour, int minutes) throws GroupNotFoundException {
        orderBuilder = customer.getBasket();
        assertEquals(location, orderBuilder.getLocation().getAddress());
        assertEquals(LocalTime.of(hour, minutes), orderBuilder.getDelivery());
        database.removeGroupById(id);
    }

    /* Scenario: Cloturer un groupe de commande */

    @Quand("je cloture le groupe grace à l'id")
    public void je_cloture_le_groupe_grace_à_l_id() throws GroupNotFoundException {
        orderFacade.closeGroup(id, customer.getId(), LocalTime.of(12, 30));
    }

    @Alors("on ne peut plus recupérer les informations du groupe")
    public void on_ne_peut_plus_recupérer_les_informations_du_groupe() {
        try {
            Group g = database.getGroupById(id);
            assertNull(g);
        } catch (GroupNotFoundException e) {
        }
    }

    @Alors("quand j'essaie de rejoindre le groupe, le site me dit qu'il ne trouve pas le groupe")
    public void le_site_me_dit_qu_il_ne_trouve_pas_le_groupe_en_question() throws RestaurantNotFoundException, CustomerNotFoundException, GroupNotFoundException {
        restaurant = restaurantDatabase.getRestaurantById(0);
        facade.initOrder(restaurant.getId(), customer.getId());
        try {
            facade.setOrderGroup(id, customer.getId());
            fail();
        } catch (GroupNotFoundException e) {
        }
    }

    @Quand("je cloture le groupe grace à l'id alors que je ne suis pas membre")
    public void je_cloture_le_groupe_grace_à_l_id_alors_que_je_ne_suis_pas_membre() throws GroupNotFoundException {
        Customer c2 = new Customer("DODO", 100, CustomerStatus.REGULAR,"","");
        orderFacade.closeGroup(id, c2.getId(), LocalTime.of(12, 30));
    }

    @Alors("le groupe existe toujours")
    public void le_groupe_existe_toujours() {
        try {
            Group g = database.getGroupById(id);
            assertNotNull(g);
        } catch (GroupNotFoundException e) {
        }
    }

    /* Scenario: End to End test d'une commande de groupe */

    Customer c2;
    Customer c3;

    @Etantdonné("2 utilisateurs de SophiaEats")
    public void un_utilisateurs_de_sophia_eats() {
        c2 = new Customer("Hugo", 200, CustomerStatus.REGULAR,"","");
        c3 = new Customer("Mathis", 320, CustomerStatus.REGULAR,"","");
        CustomerDatabase.getInstance().addCustomer(c2);
        CustomerDatabase.getInstance().addCustomer(c3);
    }

    @Quand("le premier utilisateur crée une commande de groupe aux {string}")
    public void le_premier_utilisateur_crée_une_commande_de_groupe_aux(String Location) throws GroupNotFoundException, CustomerNotFoundException {
        Location l1 = new Location(Location);
        group = orderFacade.createGroup(null, l1, c2.getId());
        id = group.toGroup().getId();
    }

    @Quand("le deuxième utilisateur rejoint le groupe")
    public void le_deuxième_utilisateur_rejoint_le_groupe() throws GroupNotFoundException, CustomerNotFoundException {
        c3.joinGroup(database.getGroupById(id));
    }

    @Quand("les deux utilisateurs réalisent leur commande")
    public void les_deux_utilisateurs_réalisent_leur_commande() {
        try {
            orderFacade.initOrder(0, c2.getId());
            orderFacade.setOrderGroup(id, c2.getId());
            orderFacade.initOrder(0, c3.getId());
            orderFacade.setOrderGroup(id, c3.getId());
        } catch (RestaurantNotFoundException | CustomerNotFoundException | GroupNotFoundException e) {
            fail();
        }
    }

    @Quand("le deuxième utilisateur cloture le groupe et met l'horaire de livraison à {int}h{int}")
    public void le_deuxième_utilisateur_cloture_le_groupe_et_met_l_horaire_de_livraison_à_h(int heure, int minute) throws GroupNotFoundException {
        orderFacade.closeGroup(id, c3.getId(), LocalTime.of(heure, minute));
    }

    @Alors("l'horaire de livraison de la commande des deux utilisateur est {int}h{int} aux {string} et le groupe n'est plus accessible")
    public void l_horaire_de_livraison_de_la_commande_des_deux_utilisateur_est_h_aux_et_le_groupe_n_est_plus_accessible(int heure, int minute, String string) {
        try {
            Order o1 = customerDatabase.getCustomerById(c2.getId()).getBasket().build();
            Order o2 = customerDatabase.getCustomerById(c3.getId()).getBasket().build();
            assertEquals(LocalTime.of(heure, minute), o1.getDelivery());
            assertEquals(LocalTime.of(heure, minute), o2.getDelivery());
            try {
                assertNull(database.getGroupById(id));
            } catch (GroupNotFoundException e) {
            }
        } catch (CustomerNotFoundException | RestaurantNotFoundException e) {
            fail();
        }
    }
}


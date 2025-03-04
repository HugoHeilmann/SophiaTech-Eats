package backend.stepDefs.order;

import io.cucumber.java.fr.Alors;
import io.cucumber.java.fr.Etantdonné;
import io.cucumber.java.fr.Quand;
import backend.customer.Customer;
import backend.customer.CustomerStatus;
import backend.database.CustomerDatabase;
import backend.exceptions.CustomerNotFoundException;
import backend.exceptions.RestaurantNotFoundException;
import backend.facade.OrderFacade;

import static org.junit.Assert.*;

public class RegisteredLocation {
    OrderFacade orderFacade = OrderFacade.getInstance();
    Customer customer;

    @Etantdonné("Que j'ai cliqué sur ajouter une localisation")
    public void que_j_ai_cliqué_sur_ajouter_supprimer_une_localisation() {
        /*frontend*/
    }

    @Etantdonné("Que j'ai une localisation enregistrée")
    public void que_j_ai_une_localisation_enregistrée() throws CustomerNotFoundException {
        CustomerDatabase.getInstance().initializeCustomers();
        customer = new Customer("Sophia", 100, CustomerStatus.REGULAR,"","");
        CustomerDatabase.getInstance().addCustomer(customer);
        orderFacade.registerLocation("3 rue des lilas, 75020 Paris", customer.getId());
    }


    @Quand("Je rentre une localisation")
    public void je_rentre_une_localisation() throws CustomerNotFoundException {
        CustomerDatabase.getInstance().initializeCustomers();
        customer = new Customer("Sophia", 100, CustomerStatus.REGULAR,"","");
        CustomerDatabase.getInstance().addCustomer(customer);
        orderFacade.registerLocation("3 rue des lilas, 75020 Paris", customer.getId());
    }

    @Quand("Je supprime une localisation")
    public void je_supprime_une_localisation() throws CustomerNotFoundException {
        CustomerDatabase.getInstance().initializeCustomers();
        customer = new Customer("Sophia", 100, CustomerStatus.REGULAR,"","");
        CustomerDatabase.getInstance().addCustomer(customer);
        orderFacade.registerLocation("3 rue des lilas, 75020 Paris", customer.getId());
        orderFacade.deleteLocation(0, customer.getId());
    }

    @Quand("Je passe une commande pour utiliser cette localisation")
    public void je_passe_une_commande() throws CustomerNotFoundException, RestaurantNotFoundException {
        orderFacade.initOrder(0, customer.getId());
    }

    @Alors("La localisation est ajoutée à ma liste de localisations")
    public void la_localisation_est_ajoutée_à_ma_liste_de_localisations() {
        assertEquals(1, customer.getLocations().size());
    }

    @Alors("La localisation est supprimée de ma liste de localisations")
    public void la_localisation_est_supprimée_de_ma_liste_de_localisations() {
        assertEquals(0, customer.getLocations().size());
    }

    @Alors("Je peux choisir cette localisation pour ma commande")
    public void je_peux_choisir_cette_localisation_pour_ma_commande() throws CustomerNotFoundException {
        orderFacade.setOrderLocation(0, customer.getId());
        assertNotNull(customer.getBasket().getLocation());
    }

}
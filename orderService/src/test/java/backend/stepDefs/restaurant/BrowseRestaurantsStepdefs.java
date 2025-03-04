package backend.stepDefs.restaurant;

import io.cucumber.java.fr.Alors;
import io.cucumber.java.fr.Quand;
import org.junit.jupiter.api.Assertions;
import backend.FoodType;
import backend.Restaurant;
import backend.exceptions.RestaurantNotFoundException;
import backend.facade.CommonFacade;

public class BrowseRestaurantsStepdefs {

    Restaurant.Data[] list;
    CommonFacade facade = CommonFacade.getInstance();

    @Quand("Un utilisateur d'internet recherche un restaurant en écrivant {string} dans la barre de recherche")
    public void unUtilisateurDInternetRechercheUnRestaurantEnÉcrivantDansLaBarreDeRecherche(String research) {
        list = facade.browseRestaurants(research, null, null);
    }

    @Alors("Il obtient une liste de {int} élément contenant le restaurant de nom {string}")
    public void ilObtientUneListeDeÉlémentContenantLeRestaurantDeNom(int nbRestaurant, String restaurantName) throws RestaurantNotFoundException {
        Assertions.assertEquals(nbRestaurant, list.length);
        Assertions.assertEquals(restaurantName, list[0].toRestaurant().getName());
    }

    @Quand("Un utilisateur d'internet recherche un restaurant qui fait de la nourriture végétarienne")
    public void unUtilisateurDInternetRechercheUnRestaurantQuiFaitDeLaNourritureVégétarienne() {
        list = facade.browseRestaurants(null, FoodType.VEGGIE, null);
    }

    @Alors("Il obtient une liste de {int} éléments")
    public void ilObtientUneListeDeÉléments(int nbRestaurants) throws RestaurantNotFoundException {
        Assertions.assertEquals(nbRestaurants, list.length);
        for (int i = 0; i < list.length; i++) {
            Assertions.assertTrue(list[i].toRestaurant().getFoodTypes().contains(FoodType.VEGGIE));
        }
    }

    @Quand("Un utilisateur d'internet ne souhaite voir que les restaurants qui peuvent se permettre de prendre une nouvelle commande")
    public void unUtilisateurDInternetNeSouhaiteVoirQueLesRestaurantsQuiPeuventSePermettreDePrendreUneNouvelleCommande() {
        list = facade.browseRestaurants(null, null, "all");
    }

    @Alors("Il obtient une liste de tous les restaurants disponible")
    public void ilObtientUneListeDeTousLesRestaurantsDisponible() throws RestaurantNotFoundException {
        for (int i = 0; i < list.length; i++) {
            Assertions.assertTrue(list[i].toRestaurant().isChargeable());
        }
    }
}

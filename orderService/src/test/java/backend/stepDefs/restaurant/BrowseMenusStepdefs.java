package backend.stepDefs.restaurant;

import io.cucumber.java.fr.*;
import backend.exceptions.CustomerNotFoundException;
import backend.facade.CommonFacade;
import backend.Menu;
import backend.exceptions.RestaurantNotFoundException;

import java.util.Optional;

import static org.junit.Assert.assertEquals;

public class BrowseMenusStepdefs {

    String menus;
    Menu.Data[] res;

    @Etantdonnéque("je suis un utilisateur d'internet")
    public void je_suis_un_utilisateur_d_internet() {
    }


    @Etantdonnéque("il existe un restaurant d'id {int} et de nom de menus {string}")
    public void il_existe_un_restaurant_d_id_et_de_nom_de_menus(Integer int1, String string) {
        menus = string;
    }


    @Quand("je recherche les menus du restaurant d'id {int}")
    public void je_recherche_les_menus_du_restaurant_d_id(Integer int1) throws RestaurantNotFoundException, CustomerNotFoundException {
        res = CommonFacade.getInstance().browseMenus(int1, Optional.empty());
    }


    @Alors("je reçois le tableau des menus du restaurant")
    public void je_reçois_le_tableau_des_menus_du_restaurant() {
        String menusTest = "";
        for (int i = 0; i < res.length - 1; i++) {
            menusTest += res[i].name() + ", ";
        }
        menusTest += res[res.length - 1].name();

        assertEquals(menus, menusTest);
    }
}
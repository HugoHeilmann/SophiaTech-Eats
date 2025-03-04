package backend.stepDefs.restaurant;

import io.cucumber.java.fr.*;
import org.junit.jupiter.api.BeforeEach;
import backend.*;
import backend.Restaurant;
import backend.database.RestaurantDatabase;
import backend.exceptions.CustomerNotFoundException;
import backend.exceptions.RestaurantNotFoundException;
import backend.facade.ManagementFacade;
import backend.facade.OrderFacade;

import java.io.IOException;
import java.time.DayOfWeek;

import static org.junit.Assert.*;

public class RestaurantStepdefs {

    Restaurant restaurant;
    ManagementFacade managementFacade = ManagementFacade.getInstance();
    RestaurantDatabase database = RestaurantDatabase.getInstance();

    @Etantdonnéque("je suis le manager du restaurant 0")
    public void je_suis_le_manager_du_restaurant() {
        database.initializeRestaurants();
        // Write code here that turns the phrase above into concrete actions
        // throw new cucumber.api.PendingException();
    }

    @BeforeEach
    void setUp() {
        RestaurantDatabase.getInstance().initializeRestaurants();
    }

    @Quand("je change les horaires d'ouverture du restaurant {int} en {int}h {int}h")
    public void le_manager_change_les_horaires_d_ouverture_du_restaurant_en(int id, int openingHour, int closingHour) throws RestaurantNotFoundException {
        managementFacade.updateScheduleForAllDays(id, String.format("%02d:00", openingHour), String.format("%02d:00", closingHour));
    }

    @Alors("le restaurant {int} ouvre à {int}h et ferme à {int}h")
    public void le_restaurant_ouvre_à_et_ferme_à(int id, int openingHour, int closingHour) throws RestaurantNotFoundException {
        restaurant = RestaurantDatabase.getInstance().getRestaurantById(id);
        assertEquals(String.format("%02d:00", openingHour), restaurant.getScheduleAtDay(0)[0].toString());
        assertEquals(String.format("%02d:00", closingHour), restaurant.getScheduleAtDay(0)[1].toString());
        restaurant.setScheduleForAllDays(String.format("09:00", openingHour), String.format("22:00", closingHour));

    }

    @Quand("je change les horaires d'ouverture du restaurant {int} en {int}h {int}h le {string}")
    public void le_manager_change_les_horaires_d_ouverture_du_restaurant_en_le(int id, int openingHour, int closingHour, String day) throws RestaurantNotFoundException {
        DayOfWeek dayOfWeek = getDayOfWeek(day);
        managementFacade.updateSchedule(id, dayOfWeek, String.format("%02d:00", openingHour), String.format("%02d:00", closingHour));
    }

    @Alors("le restaurant {int} ouvre à {int}h et ferme à {int}h le {string} et ouvre de 9h à 22h les autres jours")
    public void le_restaurant_ouvre_à_et_ferme_à_le_et_ouvre_de_9h_à_22h_les_autres_jours(int id, int openingHour, int closingHour, String day) throws RestaurantNotFoundException {
        DayOfWeek dayOfWeek = getDayOfWeek(day);
        restaurant = RestaurantDatabase.getInstance().getRestaurantById(id);
        assertEquals(String.format("%02d:00", openingHour), restaurant.getScheduleAtDay(dayOfWeek.getValue() - 1)[0].toString());
        assertEquals(String.format("%02d:00", closingHour), restaurant.getScheduleAtDay(dayOfWeek.getValue() - 1)[1].toString());

        for (int i = 0; i < 7; i++) {
            if (i != dayOfWeek.getValue() - 1) {
                assertEquals("09:00", restaurant.getScheduleAtDay(i)[0].toString());
                assertEquals("22:00", restaurant.getScheduleAtDay(i)[1].toString());
            }
        }
        restaurant.setSchedule(dayOfWeek, String.format("09:00", openingHour), String.format("22:00", closingHour));
    }

    @Quand("je change le nombre de membre du personnel disponible du restaurant {int} en {int} le {string} à {int}h{int}")
    @Et("la capacité du restaurant {int} à {int}h{int} le {string} est différente de {int}")
    public void laCapacitéDuRestaurantÀHLeEstDifférenteDe(int idRestaurant, int hour, int minute, String day, int previousCapacity) throws RestaurantNotFoundException {
        restaurant = database.getRestaurantById(idRestaurant);
        int[][] capacity = new int[7][48];

        for (int dayNum = 0; dayNum < 7; dayNum++) {
            for (int halfHour = 0; halfHour < 48; halfHour++) {
                // Calculer l'heure en minutes
                int hourNum = halfHour / 2;
                int minuteNum = (halfHour % 2) * 30;

                // Vérifier si le jour est du lundi au samedi (0 à 5)
                // et si l'heure est entre 9h (540 minutes) et 23h (1380 minutes)
                if (dayNum <= 5 && hourNum >= 9 && hourNum < 23) {
                    capacity[dayNum][halfHour] = 300;
                } else {
                    capacity[dayNum][halfHour] = 0;
                }
                if (dayNum == 0 && hourNum == 8 && minuteNum == 30) {
                    capacity[dayNum][minuteNum] = 300;
                }
            }
        }
        Capacity previous = restaurant.getCapacity();
        restaurant.getCapacity().setCapacity(capacity);
        int slot = minute == 30 ? hour * 2 + 2 : hour * 2 + 1;
        assertNotEquals(previousCapacity, restaurant.getCapacity().getCapacityForSlot(getDayOfWeek(day).getValue() - 1, slot));
        restaurant.getCapacity().setCapacity(previous.getCapacity());
    }

    @Quand("le manager change le nombre de membre du personnel disponible du restaurant {int} en {int} le {string} à {int}h{int}")
    public void leManagerChangeLeNombreDeMembreDuPersonnelDisponibleDuRestaurantEnLeÀH(int id, int staff, String day, int hour, int minute) throws RestaurantNotFoundException {
        DayOfWeek dayOfWeek = getDayOfWeek(day);
        managementFacade.updateCapacity(id, dayOfWeek.getValue(), hour, minute, staff);
    }

    @Alors("le restaurant {int} possède {int} personnes disponible le {string} à {int}h{int}")
    public void leRestaurantPossèdePersonnesDisponibleLeÀH(int id, int staff, String day, int hour, int minute) throws RestaurantNotFoundException {
        DayOfWeek dayOfWeek = getDayOfWeek(day);
        restaurant = database.getRestaurantById(id);
        int slot = minute < 30 ? hour * 2 + 1 : hour * 2 + 2;
        assertEquals(staff, restaurant.getCapacity().getCapacityForSlot(dayOfWeek.getValue(), slot));
    }

    @Quand("le manager ajoute le nouveau menu au restaurant {int}")
    public void leManagerAjouteLeNouveauMenuAuRestaurant(int id) throws RestaurantNotFoundException {
        restaurant = database.getRestaurantById(id);
        assertEquals(5, restaurant.getMenus().size());
        Menu menu = new Menu(null, "to_kill", "useless", 0, 0, "");
        ManagementFacade.getInstance().addMenu(id, menu);
    }

    @Alors("le restaurant {int} possède {int} menu dans sa liste")
    public void leRestaurantPossèdeMenuDansSaListe(int id, int nbMenu) throws RestaurantNotFoundException {
        restaurant = database.getRestaurantById(id);
        assertEquals(nbMenu, restaurant.getMenus().size());
        for (int i = 0; i < restaurant.getMenus().size(); i++) {
            if (restaurant.getMenus().get(i).getName().equals("to_kill")) {
                restaurant.getMenus().remove(restaurant.getMenus().get(i));
            }
        }
    }

    @Quand("le manager supprime le menu {string} du restaurant {int}")
    public void leManagerSupprimeLeMenuDuRestaurant(String menuName, int idRestaurant) throws RestaurantNotFoundException {
        restaurant = database.getRestaurantById(idRestaurant);
        assertEquals(4, restaurant.getMenus().size());
        ManagementFacade.getInstance().deleteMenu(idRestaurant, restaurant.getMenuByName(menuName));
    }

    @Alors("le restaurant {int} possède {int} menus dans sa liste")
    public void leRestaurantPossèdeMenusDansSaListe(int id, int nbMenus) throws RestaurantNotFoundException {
        restaurant = database.getRestaurantById(id);
        assertEquals(nbMenus, restaurant.getMenus().size());
        // échec de la réinitialisation de la database => impact sur les autres tests, donc :
        restaurant.addMenu(new Menu(FoodType.PATE, "Pates carbonara", "Pates, crême fraiche, lardons", 8, 10, " "));
    }

    @Quand("le manager modifie la description du menu {string} du restaurant {int} pour {string}")
    public void leManagerModifieLaDescriptionDuMenuDuRestaurantPour(String menuName, int idRestaurant, String newDescription) throws RestaurantNotFoundException {
        restaurant = database.getRestaurantById(idRestaurant);
        assertEquals("Pates, crême fraiche, lardons", restaurant.getMenuByName(menuName).getDescription());
        ManagementFacade.getInstance().updateMenu(idRestaurant, menuName, "description", newDescription);
    }

    @Alors("le menu {string} du restaurant {int} possède comme description {string}")
    public void leMenuDuRestaurantPossèdeCommeDescription(String menuName, int idRestaurant, String newDescription) throws RestaurantNotFoundException {
        restaurant = database.getRestaurantById(idRestaurant);
        assertEquals(newDescription, restaurant.getMenuByName(menuName).getDescription());
    }

    @Quand("le client {int} débute une commande dans le restaurant {int}")
    public void leClientDébuteUneCommandeDansLeRestaurant(int idCustomer, int idRestaurant) throws RestaurantNotFoundException, CustomerNotFoundException {
        restaurant = database.getRestaurantById(idRestaurant);
        restaurant.setOrderChargeable(1);
        OrderFacade.getInstance().initOrder(idRestaurant, idCustomer);
    }

    @Alors("le restaurant {int} voit son nombre de commande possible de prendre en charge baisser à {int}")
    public void leRestaurantVoitSonNombreDeCommandePossibleDePrendreEnChargeBaisserÀ(int idRestaurant, int nbOrderChargeable) throws RestaurantNotFoundException {
        restaurant = database.getRestaurantById(idRestaurant);
        assertEquals(nbOrderChargeable, restaurant.getOrderChargeable());
    }

    private DayOfWeek getDayOfWeek(String day) {
        switch (day.toUpperCase()) {
            case "LUNDI":
                return DayOfWeek.MONDAY;
            case "MARDI":
                return DayOfWeek.TUESDAY;
            case "MERCREDI":
                return DayOfWeek.WEDNESDAY;
            case "JEUDI":
                return DayOfWeek.THURSDAY;
            case "VENDREDI":
                return DayOfWeek.FRIDAY;
            case "SAMEDI":
                return DayOfWeek.SATURDAY;
            case "DIMANCHE":
                return DayOfWeek.SUNDAY;
            default:
                throw new IllegalArgumentException("Invalid day: " + day);
        }
    }

    @Quand("le client {int} valide sa commande faite dans le restaurant {int}")
    public void leClientValideSaCommandeFaiteDansLeRestaurant(int idCustomer, int idRestaurant) throws CustomerNotFoundException, RestaurantNotFoundException, IOException {
        leClientDébuteUneCommandeDansLeRestaurant(idCustomer, idRestaurant);
        OrderFacade.getInstance().validateAndPayOrder(idCustomer);
    }

    @Alors("le restaurant {int} voit son nombre de commandes qu'il peut prendre en charge augmenter à {int}")
    public void leRestaurantVoitSonNombreDeCommandesQuIlPeutPrendreEnChargeAugmenterÀ(int idRestaurant, int nbOrderChargeable) throws RestaurantNotFoundException {
        restaurant = database.getRestaurantById(idRestaurant);
        assertEquals(nbOrderChargeable, restaurant.getOrderChargeable());
    }
}